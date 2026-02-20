package humcare.usuario.controller;

import humcare.application.model.Usuario;
import humcare.application.service.CehLogService;
import humcare.application.service.Sessao;
import humcare.application.tipoocorrencia.TipoOcorrenciaLog;
import humcare.usuario.dao.CehUsuarioDAO;
import humcare.usuario.funcao.FuncaoCdPerfil;
import humcare.usuario.model.CehUsuario;
import humcare.utilitarios.LdapUtil;
import humcare.utilitarios.PasswordUtil;
import humcare.utilitarios.Utils;
import humcare.utilitarios.ZkUtils;
import humcare.zk.custom.Toast;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.*;

import java.text.ParseException;

public class CehUsuarioController extends Window {

    private Window win;
    private Label lbAtivo;

    private Label cdPessoa;
    private Textbox nmPessoa;
    private Textbox nuCpf;
    private Textbox nuFone;
    private Textbox deEmail;
    private Listbox dePerfil;
    private Label lblSenha;
    private Checkbox flAtivo;
    private Textbox deSenha;

    private Button btnSalvar;
//    private Button btnExcluir;
    private Button btnCancelar;

    private final ZkUtils zkUtils = new ZkUtils();
    private final Utils utils = new Utils();
    private final CehUsuarioDAO cehUsuarioDAO = new CehUsuarioDAO();
    private CehUsuario cehUsuario = new CehUsuario();
    private final LdapUtil ldapUtil = new LdapUtil();

    private String urlRetorno = "";
    private String filtroStatus = "";
    private Integer paginaSalva;

    public void onCreate() {
        this.win = (Window) getFellow("winCehUsuario");

        this.cdPessoa = (Label) getFellow("cdPessoa");
        this.nmPessoa = (Textbox) getFellow("nmPessoa");
        this.lblSenha = (Label) getFellow("lblSenha");
        this.nuCpf = (Textbox) getFellow("nuCpf");
        this.nuFone = (Textbox) getFellow("nuFone");
        this.deSenha = (Textbox) getFellow("deSenha");
        this.deEmail = (Textbox) getFellow("deEmail");
        this.dePerfil = (Listbox) getFellow("dePerfil");
        this.flAtivo = (Checkbox) getFellow("flAtivo");
        this.lbAtivo = (Label) getFellow("lbAtivo");

        this.btnSalvar = (Button) getFellow("salvar");
//        this.btnExcluir = (Button) getFellow("excluir");
        this.btnCancelar = (Button) getFellow("cancelar");

        Integer cehUsuarioId = (Integer) zkUtils.getParametro("cehUsuario");
        String acao = (String) zkUtils.getParametro("ação");
        this.urlRetorno = (String) zkUtils.getParametro("url_retorno");
        this.filtroStatus = (String) zkUtils.getParametro("filtroStatus");
        this.paginaSalva = (Integer) zkUtils.getParametro("paginaAtual");

        if (cehUsuarioId != null) {
            cehUsuario = cehUsuarioDAO.buscar(cehUsuarioId);
            popularCampos();
        } else {
            limparCampos();
        }

        configurarTelaPorAcao(acao);
    }

    private void configurarTelaPorAcao(String acao) {
        if (acao.equals("novo")) {
            this.btnSalvar.setVisible(true);
            this.btnCancelar.setVisible(true);
            this.cdPessoa.setVisible(false);
            this.nmPessoa.setReadonly(false);
            this.lblSenha.setVisible(true);
            this.deSenha.setVisible(true);
            this.nuCpf.setReadonly(false);
            this.flAtivo.setChecked(true);
            this.deEmail.setFocus(true);
        } else if (acao.equals("editar")) {
            this.btnSalvar.setVisible(true);
            this.btnCancelar.setVisible(true);
//            this.btnExcluir.setVisible(true);
            this.deEmail.setReadonly(true);
            this.nuCpf.setReadonly(true);

            boolean isLdapUser = ldapUtil.emailExists(this.deEmail.getValue());

            if (isLdapUser) {
                this.deSenha.setVisible(false);
                this.lblSenha.setVisible(false);
            } else {
                this.deSenha.setVisible(true);
                this.lblSenha.setVisible(true);
            }

            if (verificaPerfilUsuario()) {
                this.dePerfil.setDisabled(false);
                this.flAtivo.setDisabled(false);
            } else {
                this.dePerfil.setDisabled(true);
                this.flAtivo.setDisabled(true);
            }
        } else if (acao.equals("ler")) {
            this.btnSalvar.setVisible(false);
            this.btnCancelar.setVisible(false);
            this.deEmail.setReadonly(true);
            this.deSenha.setVisible(false);
            this.lblSenha.setVisible(false);
            this.nmPessoa.setReadonly(true);
            this.nuCpf.setReadonly(true);
            this.nuFone.setReadonly(true);
            this.dePerfil.setDisabled(true);
            this.flAtivo.setDisabled(true);
        }
    }

    public void limparCampos() {
        this.cdPessoa.setValue("-1");
        this.nmPessoa.setValue(null);
        this.nuCpf.setValue(null);
        this.nuFone.setValue(null);
        this.deEmail.setValue(null);
        zkUtils.selecionarLista(dePerfil, 0);
        this.flAtivo.setChecked(false);
        mudarativo();
    }

    public void popularCampos() {
        limparCampos();
        zkUtils.popularCampo(cdPessoa, this.cehUsuario.getCdPessoa());
        zkUtils.popularCampo(nmPessoa, cehUsuario.getNmPessoa());
        zkUtils.popularCampo(nuCpf, cehUsuario.getNuCpf());
        zkUtils.popularCampo(nuFone, cehUsuario.getNuFone());
        zkUtils.popularCampo(deEmail, cehUsuario.getDeEmail());

        if (cehUsuario.getDePerfil() != null) {
            zkUtils.selecionarLista(dePerfil, cehUsuario.getDePerfil().toString());
        }
        this.flAtivo.setChecked(cehUsuario.getFlAtivo());
        mudarativo();
    }

    public void mudarativo() {
        lbAtivo.setValue(flAtivo.isChecked() ? " Ativo:" : "Inativo:");
    }

    public boolean validarCampos() throws ParseException {
        boolean gerouErro = false;

        if (this.nuFone.getValue().trim().isEmpty()) {
            Clients.showNotification("Telefone é obrigatório!", Clients.NOTIFICATION_TYPE_WARNING, nuFone, "end_center", 2000);
            gerouErro = true;
            return false;
        }
        if (this.nuCpf.getValue().trim().isEmpty()) {
            Clients.showNotification("CPF é obrigatório!", Clients.NOTIFICATION_TYPE_WARNING, nuCpf, "end_center", 2000);
            gerouErro = true;
            this.nuCpf.setFocus(true);
        }
        if (this.deEmail.getValue().trim().isEmpty()) {
            Clients.showNotification("E-mail é obrigatório!", Clients.NOTIFICATION_TYPE_WARNING, deEmail, "end_center", 2000);
            gerouErro = true;
            this.deEmail.setFocus(true);
        }
        if (nmPessoa.getValue().trim().isEmpty() || nuCpf.getValue() == null) {
            Clients.showNotification("Nome e Matrícula são obrigatórios! Busque o usuário pelo e-mail.", Clients.NOTIFICATION_TYPE_WARNING, nmPessoa, "end_center", 3000);
            gerouErro = true;
            this.nmPessoa.setFocus(true);
        }
        if (dePerfil.getSelectedIndex() <= 0) {
            Clients.showNotification("Função (Perfil) é obrigatória!", Clients.NOTIFICATION_TYPE_WARNING, dePerfil, "end_center", 2000);
            gerouErro = true;
            this.dePerfil.setFocus(true);
        }

        return !gerouErro;
    }

    private boolean verificaPerfilUsuario() {
        Usuario usuarioLogado = Sessao.getInstance().getUsuario();
        if (usuarioLogado != null && usuarioLogado.getTpPermissaoAcesso() != null) {
            return usuarioLogado.getTpPermissaoAcesso() == FuncaoCdPerfil.ADMINISTRADOR;
        } else {
            return false;
        }
    }

    public boolean verificaSeJaTemAdm() {
        CehUsuario admin = cehUsuarioDAO.buscarAdministrador();
        return admin != null;
    }

    public void gravar() throws ParseException {
        boolean novoUsuario = Integer.parseInt(this.cdPessoa.getValue()) == -1;

        if (!validarCampos()) {
            return;
        }

        if (novoUsuario && (deSenha.getValue() == null || deSenha.getValue().trim().isEmpty())) {
            if (!ldapUtil.emailExists(deEmail.getValue())) {
                Clients.showNotification("Senha é obrigatória para usuários não cadastrados no LDAP!", Clients.NOTIFICATION_TYPE_WARNING, deSenha, "end_center", 3000);
                return;
            }
        }

        cehUsuario.setNmPessoa(nmPessoa.getValue());
        cehUsuario.setNuCpf(this.nuCpf.getValue());
        cehUsuario.setNuFone(utils.soNumeros(this.nuFone.getValue()));
        cehUsuario.setDeEmail(deEmail.getValue());
        cehUsuario.setDePerfil(FuncaoCdPerfil.valueOf(dePerfil.getSelectedItem().getValue()));
        cehUsuario.setFlAtivo(flAtivo.isChecked());

        if (deSenha.getValue() != null && !deSenha.getValue().trim().isEmpty()) {
            cehUsuario.setDeSenha(PasswordUtil.hashPassword(deSenha.getValue()));
        }

        if (novoUsuario) {
            Integer id = cehUsuarioDAO.incluirAutoincrementando(cehUsuario);
            if (id != null && id > 0) {
                Toast.show("Usuário cadastrado com sucesso!", "Sucesso", Toast.Type.SUCCESS);
                CehLogService.getInstance().insereLog("BDC_PESSOA", TipoOcorrenciaLog.INSERCAO);
                this.voltar();
            } else {
                zkUtils.MensagemErro("Houve um erro e não foi possivel incluir");
            }
        } else {
            cehUsuario.setCdPessoa(Integer.parseInt(this.cdPessoa.getValue()));
            boolean atualizou = cehUsuarioDAO.atualizar(cehUsuario);
            if (atualizou) {
                Toast.show("Usuário atualizado com sucesso!", "Sucesso", Toast.Type.SUCCESS);
                CehLogService.getInstance().insereLog("BDC_PESSOA", TipoOcorrenciaLog.ALTERACAO);
                this.voltar();
            } else {
                zkUtils.MensagemErro("Houve um erro e não foi possível atualizar o usuário.");
            }
        }
    }

    public void excluir() {
        if (cehUsuario.getCdPessoa() != null && zkUtils.MensagemConfirmacao("Deseja realmente inativar este usuário?")) {
            cehUsuario.setFlAtivo(false);
            cehUsuarioDAO.atualizar(cehUsuario);
            Toast.show("Usuário inativado com sucesso!", "Sucesso", Toast.Type.SUCCESS);
            CehLogService.getInstance().insereLog("BDC_PESSOA", TipoOcorrenciaLog.DESATIVACAO);
            voltar();
        }
    }

    public void voltar() {
        Include include = (Include) win.getParent();
        zkUtils.setParametro("paginaAtual", paginaSalva);
        zkUtils.setParametro("filtroStatus", filtroStatus);
        include.setSrc(urlRetorno);
    }

    public void searchLdap() {
        searchLdapByEmail(deEmail.getValue());
    }

    private void searchLdapByEmail(String email) {
        if (email == null || email.trim().isEmpty()) return;

        LdapUtil ldapSearch = new LdapUtil();
        try {
            ldapSearch.search(email);
            nmPessoa.setValue(ldapSearch.getNomeCompleto());
            String matriculaLdap = ldapSearch.getMatricula();
        } catch (Exception e) {
            nmPessoa.setValue(null);
            Toast.show("Usuário não encontrado no LDAP: " + e.getMessage(), "Erro", Toast.Type.ERROR);
        }
    }
}