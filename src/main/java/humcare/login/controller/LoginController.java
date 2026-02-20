package humcare.login.controller;

import humcare.application.model.Usuario;
import humcare.application.service.Application;
import humcare.application.service.Sessao;
import humcare.permissao.dao.PermissaoDAO;
import humcare.permissao.model.Permissao;
import humcare.usuario.dao.CehUsuarioDAO;
import humcare.usuario.funcao.FuncaoCdPerfil;
import humcare.usuario.model.CehUsuario;
import humcare.utilitarios.*;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.*;

import java.util.List;

public class LoginController extends Window {
    private Window win;
    private Textbox usuarioDigitado;
    private Textbox senhaDigitada;
    private Label lbVersao;
    private Label lbErro;
    private Div divDesenv;

    // ATENÇÃO: SENHA MESTRA PARA DESENVOLVIMENTO. REMOVER EM PRODUÇÃO.
    private final String SENHA_MESTRA = "admincehu1";

    private final LdapUtil ldapUtil = new LdapUtil();
    private final ZkUtils zkUtils = new ZkUtils();
    private final CehUsuarioDAO cehUsuarioDAO = new CehUsuarioDAO();
    private final Utils utils = new Utils();

    private String parametroCdUsuario;
    private String parametroUrlRedirect;

    public void onCreate() {
        // Mapeamento dos componentes da tela .zul
        this.win = (Window) getFellow("winLogin");
        this.usuarioDigitado = (Textbox) getFellow("usuario");
        this.senhaDigitada = (Textbox) getFellow("senha");
        this.lbVersao = (Label) getFellow("lb_versao");
        this.lbErro = (Label) getFellow("msg_erro");
        this.divDesenv = (Div) getFellow("div_desenv");

        // Configurações iniciais da tela
        String versao = Application.getInstance().getVersion();
        this.lbVersao.setValue("Versão " + versao);
        this.mensagemDesenvol();
        Application.getInstance().atualizaTituloDaPagina();
        verificaCookie();
        getParametrosURL();
    }

    /**
     * Ponto de entrada do botão de login. Prepara os dados e chama a validação principal.
     */
    public void acessar() {
        try {
            hideErro();
            String login = usuarioDigitado.getValue().trim();
            String senha = senhaDigitada.getValue();

            if (!login.contains("@")) {
                login = login.concat("@uem.br");
            }

            if (!utils.validaEmail(login)) {
                showErro("O e-mail informado é inválido!");
                return;
            }

            validarLogin(login, senha);


        } catch (Exception e) {
            System.out.println("--------------------- ERRO ----------------------");
            e.printStackTrace();
            System.out.println("-------------------------------------------------");
        }
    }

    public void cadastrarUsuario(){
        Executions.sendRedirect("/cadastroUsuario.zul");
    }

    /**
     * Valida o login de forma híbrida: primeiro checa o tipo de usuário (local ou LDAP)
     * e depois valida a senha correspondente.
     */
    private void validarLogin(String login, String senha) {
        // Busca o usuário no nosso banco de dados primeiro
        CehUsuario usuarioDoBanco = this.cehUsuarioDAO.buscaPorEmail(login);

        if (usuarioDoBanco == null) {
            zkUtils.MensagemAtencao("Usuário não encontrado. Se você ainda não tem acesso, por favor, realize o seu cadastro.");
            return;
        }

        boolean senhaValida;

        // Lógica para Senha Mestra (apenas para desenvolvimento)
        if (senha.equals(SENHA_MESTRA)) {
            senhaValida = true;
        } else {
            // Se o usuário TEM uma senha no nosso banco, ele é um "Credenciado"
            if (usuarioDoBanco.getDeSenha() != null && !usuarioDoBanco.getDeSenha().isEmpty()) {
                senhaValida = PasswordUtil.checkPassword(senha, usuarioDoBanco.getDeSenha());
            } else {
                // Se o campo de senha é nulo, ele é um usuário UEM -> valida no LDAP
                senhaValida = isSenhaValidaLdap(login, senha);
            }
        }

        if (!senhaValida) {
            showErro("Login ou Senha incorretos!");
            return;
        }


        if (!usuarioDoBanco.getFlAtivo()) {
            showErro("Seu acesso está pendente de aprovação ou foi inativado. Por favor, contate o administrador.");
            return;
        }


        if (usuarioDoBanco.getDePerfil() == null) {
            showErro("Acesso bloqueado: seu usuário não possui um perfil de permissões definido. Contate o administrador.");
            return;
        }


        this.configuraUsuarioSessao(usuarioDoBanco);
    }

    /**
     * Configura a sessão do usuário após um login bem-sucedido.
     * @param usuarioVerificado O objeto CehUsuario já validado.
     */
    private void configuraUsuarioSessao(CehUsuario usuarioVerificado) {
        // Define os dados do usuário na sessão
        FuncaoCdPerfil funcaoCdPerfil = usuarioVerificado.getDePerfil();
        Usuario usuarioSession = new Usuario();
        usuarioSession.setCdUsuario(usuarioVerificado.getCdPessoa());
        usuarioSession.setEmail(usuarioVerificado.getDeEmail());
        usuarioSession.setUsername(usuarioVerificado.getDeEmail().split("@")[0]);
        usuarioSession.setTpPermissaoAcesso(funcaoCdPerfil);
        usuarioSession.setNome(usuarioVerificado.getNmPessoa());

        Sessao.getInstance().setUsuario(usuarioSession);
        atualizaCookie();

        // Carrega as permissões do perfil do usuário
        List<Permissao> permissoes = PermissaoDAO.buscaPorTpPermissao(funcaoCdPerfil.getValor());
        if (permissoes != null && !permissoes.isEmpty()) {
            permissoes.forEach(p -> {
                if (p.getFlIncluir()) Sessao.getInstance().setPermissao(p.getDeArquivo(), Sessao.INCLUIR);
                if (p.getFlAlterar()) Sessao.getInstance().setPermissao(p.getDeArquivo(), Sessao.ALTERAR);
                if (p.getFlExcluir()) Sessao.getInstance().setPermissao(p.getDeArquivo(), Sessao.EXCLUIR);
                if (p.getFlConsultar()) Sessao.getInstance().setPermissao(p.getDeArquivo(), Sessao.CONSULTAR);
                if (p.getDeMenu() != null) Sessao.getInstance().setPermissao(p.getDeMenu(), Sessao.MENU);
            });
        }

        if (parametroUrlRedirect != null && !parametroUrlRedirect.isBlank() && usuarioSession.getTpPermissaoAcesso() != FuncaoCdPerfil.NIR) {
            Executions.sendRedirect("/" + this.parametroUrlRedirect + "/?cdUsuario=" + this.parametroCdUsuario);
        }else if(usuarioSession.getTpPermissaoAcesso() == FuncaoCdPerfil.NIR){
            Executions.sendRedirect("paginas/higienizacao/higienizacaoList.zul");
        }
        else{
            Executions.sendRedirect("/");
        }
    }

    private boolean isSenhaValidaLdap(String login, String senha) {
        try {
            ldapUtil.bind(login, senha);
            return true;
        } catch (Exception e) {
            // Não mostre o erro técnico para o usuário, apenas retorne false.
            // A mensagem de "Login ou Senha incorretos" é mais segura.
            System.err.println("LDAP Auth Error: " + e.getMessage());
            return false;
        }
    }

    private void getParametrosURL() {
        this.parametroCdUsuario = Executions.getCurrent().getParameter("cdUsuario");
        this.parametroUrlRedirect = Executions.getCurrent().getParameter("urlredirect");
    }
    private void verificaCookie() {
        String ultimoUsuario = CookieUtil.getCookie("usuariologin");
        if (ultimoUsuario != null) {
            this.usuarioDigitado.setValue(ultimoUsuario);
        }
    }
    private void atualizaCookie() {
        CookieUtil.setCookie("usuariologin", this.usuarioDigitado.getValue());
    }
    public void showErro(String msg) {
        if (this.lbErro != null) {
            this.lbErro.setValue(msg);
            this.lbErro.setVisible(true);
        } else {
            // Se o label não for encontrado, mostra um alerta padrão do navegador
            zkUtils.MensagemErro("Erro: " + msg);
            System.err.println("ERRO GRAVE: Componente com id='msg_erro' não foi encontrado no arquivo .zul!");
        }
    }
    public void hideErro() {
        this.lbErro.setValue("");
        this.lbErro.setVisible(false);
    }
    private void mensagemDesenvol() {
        if (Application.getInstance().getEnviroment().equals(Application.DEVELOP)) {
            this.divDesenv.setVisible(true);
        }
    }
}