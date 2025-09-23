package humcare.login.controller;

import humcare.application.service.Sessao;
import humcare.application.service.Application;
import humcare.application.model.Usuario;
import humcare.cehusuario.tipopermissao.TipoCehUsuario;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Div;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import humcare.cehusuario.dao.CehUsuarioDAO;
import humcare.cehusuario.model.CehUsuario;
import humcare.permissao.dao.PermissaoDAO;
import humcare.permissao.model.Permissao;
import humcare.utilitarios.CookieUtil;
import humcare.utilitarios.LdapUtil;
import humcare.utilitarios.Utils;
import humcare.utilitarios.ZkUtils;
import humcare.zk.custom.Toast;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author Alison
 */
public class LoginController extends Window {

    private Textbox usuarioDigitado;
    private Textbox senhaDigitada;
    private Include conteudo;
    private Label lbVersao;
    private Label lbErro;
    private Div divDesenv;
    private Window janela;

    //ATENCAO: EXCLUA OU ALTERE ESSA SENHA MESTRA
    private final String SENHA_MESTRA = "admincehu1";

    private final LdapUtil ldapUtil = new LdapUtil();
    private final ZkUtils zkUtils = new ZkUtils();
    CehUsuarioDAO cehUsuarioDAO = new CehUsuarioDAO();
    CehUsuario cehUsuario = new CehUsuario();

    private String parametroCdEquipamento;
    private String parametroUrlRedirect;

    private Utils utils = new Utils();

    public void onCreate() {
        this.conteudo = (Include) getFellowIfAny("conteudo", true);
        this.janela = (Window) getFellow("login");
        this.usuarioDigitado = (Textbox) getFellow("usuario");
        this.senhaDigitada = (Textbox) getFellow("senha");
        this.lbVersao = (Label) getFellow("lb_versao");
        this.lbErro = (Label) getFellow("msg_erro");
        this.divDesenv = (Div) getFellow("div_desenv");

        String versao = Application.getInstance().getVersion();
        this.lbVersao.setValue("Versão " + versao);

        this.mensagemDesenvol();
//        Application.getInstance().insereDadosNoConsole(this.janela);
        Application.getInstance().atualizaTituloDaPagina();

        verificaCookie();

        getParametrosURL();
    }

    public void getParametrosURL() {
        this.parametroCdEquipamento = Executions.getCurrent().getParameter("cdequipamento");
        this.parametroUrlRedirect = Executions.getCurrent().getParameter("urlredirect");
    }

    /**
     * Executado quando clica no botão para entrar.
     */
    public void acessar() {
        this.validaEmail();
    }

    /**
     * Verifica se tem o último usuário que logou salvo em cookie, se tiver
     * preenche o campo automaticamente.
     */
    private void verificaCookie() {
        String ultimoUsuario = CookieUtil.getCookie("usuariologin");
        if (ultimoUsuario != null) {
            this.usuarioDigitado.setValue(ultimoUsuario);
        }
    }

    /**
     * Salva em cookie o último usuário que logou.
     */
    private void atualizaCookie() {
        CookieUtil.setCookie("usuariologin", this.usuarioDigitado.getValue());
    }

    /**
     * Valida a senha no LDAP da UEM. Funciona para todos que tiver um e-mail
     * válido @uem.br
     *
     * @param login é o e-mail ou apenas o nome do usuário
     * @param senha senha
     * @return booleano
     */
    private boolean isSenhaValidaLdap(String login, String senha) {
        try {
            ldapUtil.bind(login, senha);
        } catch (Exception e) {
            this.showErro(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Valida a senha conforme cadastrado no banco de dados. Para implementar
     * essa funcionalidade recomendo usar Argon2PasswordEncoder. Veja tutorial
     * https://foojay.io/today/how-to-do-password-hashing-in-java-applications-the-right-way/
     *
     * @param login é o e-mail ou apenas o nome do usuário
     * @param senha senha
     * @return booleando
     */
    private boolean isSenhaValidaBanco(String login, String senha) {
        return false;
    }

    private boolean isSenhaValidaBanco(String login) {
        return false;
    }

    private boolean isSenhaMestra(String senha) {
        return senha.equals(SENHA_MESTRA);
    }

    /**
     * Validação do Email, permitindo o usuário acessar mesmo sem colocar um endereço (@....)
     */
    private void validaEmail() {
        String login = this.usuarioDigitado.getValue();
        String senha = this.senhaDigitada.getValue();
        if (login.equals("bi-hum@uem.br") || login.equals("bi-hum")){
            login = "ecacarva@uem.br";
        }
        if (login.contains("@")) {
            if (!utils.validaEmail(login)) {
                this.showErro("Login ou Senha incorretos!");
            } else {
                this.validarSessao(login, senha);
            }
        } else {
            login = login.concat("@uem.br");
            this.validarSessao(login, senha);
        }
    }

    /**
     * Se validar o email e a senha, salva na sessão o usuário que acessou com todos os
     * dados necessários para o sistema funcionar, suas permissões, perfis, etc.
     */
    private void validarSessao(String login, String senha) {
        boolean senhaValidaLdap = isSenhaValidaLdap(login, senha);
        if (senhaValidaLdap) {
            CehUsuario usu = this.criaUsuarioCasoNaoExista(login);

            if (isSenhaMestra(senha) && usu.getFlAtivo()) {
                this.configuraUsuarioSessao(login);
            }

            if (!usu.getFlAtivo()) {
                this.showErro("Seu login esta Inativo, fale com o Administrador!");
            } else {
                this.configuraUsuarioSessao(login);
            }

        }

        if (!senhaValidaLdap) {
            this.showErro("Login ou Senha incorretos!");
        }
    }

    private CehUsuario criaUsuarioCasoNaoExista(String login) {
        CehUsuario uCeh = cehUsuarioDAO.buscaPorEmail(login);
        if (uCeh == null) {
            uCeh = new CehUsuario();
            uCeh.setTpPermissaoAcesso(TipoCehUsuario.OPERADOR);
            uCeh.setFlAtivo(true);
            uCeh.setLtEmail(login);
            uCeh.setNmUsuario(this.ldapUtil.getNomeCompleto().toUpperCase());
            uCeh.setNuMatricula(Integer.parseInt(this.ldapUtil.getMatricula()));
            Integer i = cehUsuarioDAO.incluirAutoincrementando(uCeh);
            uCeh.setNuMatricula(i);
            return uCeh;
        }

        return uCeh;
    }

    private void configuraUsuarioSessao(String login) {
        cehUsuario = cehUsuarioDAO.buscaPorEmail(login);

        // colocando na session
        TipoCehUsuario tipoCehUsuario = TipoCehUsuario.getTipoCehUsuarioInt(cehUsuario.getTpPermissaoAcesso().getValor());
        Usuario usuarioSession = new Usuario();
        usuarioSession.setEmail(cehUsuario.getLtEmail());
        usuarioSession.setUsername(cehUsuario.getLtEmail().split("@")[0]);
        usuarioSession.setNome(cehUsuario.getNmUsuario());
        usuarioSession.setTpPermissaoAcesso(tipoCehUsuario);
        usuarioSession.setCdUsuario(cehUsuario.getCdUsuario());
        usuarioSession.setNuMatricula(cehUsuario.getNuMatricula());

        Sessao.getInstance().setUsuario(usuarioSession);
        atualizaCookie();

        List<Permissao> permissoes = PermissaoDAO.buscaPorTpPermissao(tipoCehUsuario.getValor());
        if (!permissoes.isEmpty()) {
            permissoes.forEach(p -> {
                if (p.getFlIncluir()) {
                    Sessao.getInstance().setPermissao(p.getDeArquivo(), Sessao.INCLUIR);
                }
                if (p.getFlAlterar()) {
                    Sessao.getInstance().setPermissao(p.getDeArquivo(), Sessao.ALTERAR);
                }
                if (p.getFlExcluir()) {
                    Sessao.getInstance().setPermissao(p.getDeArquivo(), Sessao.EXCLUIR);
                }
                if (p.getFlConsultar()) {
                    Sessao.getInstance().setPermissao(p.getDeArquivo(), Sessao.CONSULTAR);
                }
                if (p.getDeMenu() != null) {
                    Sessao.getInstance().setPermissao(p.getDeMenu(), Sessao.MENU);
                }
            });
        }

        if (this.parametroUrlRedirect != null
                && this.parametroCdEquipamento != null
                && !this.parametroUrlRedirect.isBlank()
                && !this.parametroCdEquipamento.isBlank()
        ) {
            Executions.sendRedirect("/" + this.parametroUrlRedirect + "/?cdEquipamento=" + this.parametroCdEquipamento);
        } else {
            Executions.sendRedirect("/");
        }
    }


    /**
     * Mostra erro na tela de login
     *
     * @param msg = mensagem
     */
    public void showErro(String msg) {
        this.lbErro.setValue(msg);
        this.lbErro.setVisible(true);
    }

    /**
     * Esconde o erro na tela de login.
     */
    public void hideErro() {
        this.lbErro.setValue("");
        this.lbErro.setVisible(false);
    }

    /**
     * Caso o ambiente de desenvolvimento seja de testes (não esteja conectado
     * em produção), então mostra um aviso.
     */
    private void mensagemDesenvol() {
        if (Application.getInstance().getEnviroment().equals(Application.DEVELOP)) {
            this.divDesenv.setVisible(true);
        }
    }

}
