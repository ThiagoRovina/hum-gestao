package humcare.cehusuario.controller;

import humcare.application.service.CehLogService;
import humcare.application.tipoocorrencia.TipoOcorrenciaLog;
import humcare.cehusuario.dao.CehUsuarioDAO;
import humcare.cehusuario.funcao.FuncaoCehUsuario;
import humcare.cehusuario.model.CehUsuario;
import humcare.cehusuario.tipopermissao.TipoCehUsuario;
//import humcare.destino.dao.DestinoDAO;
//import humcare.destino.model.Destino;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.*;
import humcare.utilitarios.LdapUtil;
import humcare.utilitarios.Utils;
import humcare.utilitarios.ZkUtils;
import humcare.zk.custom.Toast;

import java.text.ParseException;

public class CehUsuarioController extends Window {

    private Window win;
    private Label cdUsuario;
    private Label lbAtivo;
    private Intbox nuMatricula;
    private Listbox tpPermissaoAcesso;
    private Listbox deFuncao;
    private Checkbox flAtivo;
    private Textbox ltEmail;
    private Textbox nmUsuario;

    private Button btnSalvar;
    private Button btnExcluir;
    private Button btnCancelar;

    private final Utils utils = new Utils();
    private final ZkUtils zkUtils = new ZkUtils();

    private CehUsuarioDAO cehUsuarioDAO = new CehUsuarioDAO();

    //A variável curso inicialmente é um Curso vazio,
    //caso tenha algum id passado por parâmetro, o curso será buscado no banco e vinculado à esta variável
    private CehUsuario cehUsuario = new CehUsuario();

    private String urlRetorno = "";
    private String filtroStatus = "";
    private Integer paginaSalva;

    public void onCreate() {

        this.win = ((Window) getFellow("winCehUsuario"));

        this.cdUsuario = (Label) getFellow("cdUsuario");
        this.nuMatricula = (Intbox) getFellow("nuMatricula");
        this.tpPermissaoAcesso = (Listbox) getFellow("tpPermissaoAcesso");
        this.deFuncao = (Listbox) getFellow("deFuncao");
        this.flAtivo = (Checkbox) getFellow("flAtivo");
        this.ltEmail = (Textbox) getFellow("ltEmail");
        this.nmUsuario = (Textbox) getFellow("nmUsuario");

        this.btnSalvar = (Button) getFellow("salvar");
        this.btnExcluir = (Button) getFellow("excluir");
        this.btnCancelar = (Button) getFellow("cancelar");
        this.lbAtivo = (Label) getFellow("lbAtivo");

        //pega os parâmetros que veio da outra página
        Integer cehUsuarioId = (Integer) zkUtils.getParametro("cehUsuario");
        String acao = (String) zkUtils.getParametro("ação");
        this.urlRetorno = (String) zkUtils.getParametro("url_retorno");
        this.filtroStatus = (String) zkUtils.getParametro("filtroStatus");
        this.paginaSalva = (Integer) zkUtils.getParametro("paginaAtual");

        //mostra os botões de acordo com a ação
        if (acao.equals("novo")) {
            this.btnSalvar.setVisible(true);
            this.btnCancelar.setVisible(true);
            this.cdUsuario.setVisible(false);
            this.nmUsuario.setReadonly(true);
            this.nuMatricula.setReadonly(true);
            this.flAtivo.setChecked(true);
            this.ltEmail.setFocus(true);
        } else if (acao.equals("editar")) {
            this.btnSalvar.setVisible(true);
            this.btnCancelar.setVisible(true);
            this.btnExcluir.setVisible(true);
        } else if (acao.equals("ler")) {
            this.ltEmail.setReadonly(true);
            this.nuMatricula.setReadonly(true);
            this.tpPermissaoAcesso.setDisabled(true);
            this.deFuncao.setDisabled(true);
            this.nmUsuario.setReadonly(true);
            this.flAtivo.setDisabled(true);
        }

        limparCampos();

        //caso tenha algum id de curso que veio por parâmetro,
        //então busca esse curso no banco e popula os campos com os valores
        if (cehUsuarioId != null) {
            cehUsuario = cehUsuarioDAO.buscar(cehUsuarioId);
            popularCampos();
        }

    }

    /**
     * Limpa os campos do .zul.
     */
    public void limparCampos() {
        this.cdUsuario.setValue("-1");
        this.nuMatricula.setValue(null);
        zkUtils.selecionarLista(tpPermissaoAcesso, 0);
        zkUtils.selecionarLista(deFuncao, 0);
        this.flAtivo.setValue(false);
        this.ltEmail.setValue(null);
        this.nmUsuario.setValue(null);
    }

    /**
     * Popula os campos .zul com os calores do banco.
     */
    public void popularCampos() {
        limparCampos();
        zkUtils.popularCampo(this.cdUsuario, this.cehUsuario.getCdUsuario());
        zkUtils.popularCampo(this.nuMatricula, this.cehUsuario.getNuMatricula());
        zkUtils.selecionarLista(this.tpPermissaoAcesso, String.valueOf(this.cehUsuario.getTpPermissaoAcesso()));
        zkUtils.selecionarLista(deFuncao, String.valueOf(this.cehUsuario.getDeFuncao()));
        this.flAtivo.setChecked(this.cehUsuario.getFlAtivo());
        zkUtils.popularCampo(this.ltEmail, this.cehUsuario.getLtEmail());
        zkUtils.popularCampo(this.nmUsuario, this.cehUsuario.getNmUsuario());
        mudarativo();
    }

    public void mudarativo() {
        if (this.flAtivo.isChecked()) {
            this.lbAtivo.setValue(" Ativo:");
        } else {
            this.lbAtivo.setValue("Inativo:");
        }

    }

    /**
     * Valida se os campos preenchidos estão corretos.
     */
    public boolean validarCampos() throws ParseException {
        boolean gerouErro = false;

        if (this.tpPermissaoAcesso.getSelectedIndex() == 0) {
            Clients.showNotification("Perfil é obrigatório!", Clients.NOTIFICATION_TYPE_WARNING, this.tpPermissaoAcesso, "end_center", 0);
            gerouErro = true;
            this.tpPermissaoAcesso.setFocus(true);
        }
        if (this.ltEmail.getValue().trim().equals("")) {
            Clients.showNotification("E-Mail é obrigatório!", Clients.NOTIFICATION_TYPE_WARNING, this.ltEmail, "end_center", 0);
            gerouErro = true;
            this.ltEmail.setFocus(true);
        }
        if (this.nmUsuario.getValue().trim().equals("")) {
            Clients.showNotification("Nome é obrigatório!", Clients.NOTIFICATION_TYPE_WARNING, this.nmUsuario, "end_center", 0);
            gerouErro = true;
            this.nmUsuario.setFocus(true);
        }

        if (this.deFuncao.getSelectedIndex() == 0) {
            Clients.showNotification("Função é obrigatória!", Clients.NOTIFICATION_TYPE_WARNING, this.deFuncao, "end_center", 0);
            gerouErro = true;
            this.deFuncao.setFocus(true);
        }

        return !gerouErro;
    }

    /**
     * Grava os dados no banco.
     */
    public void gravar() throws ParseException {
        if (validarCampos()) {
            //se validou os dados prenchidos, então atualizamos nosso objeto Curso com os dados preenchidos
            cehUsuario.setNuMatricula(this.nuMatricula.getValue());
            cehUsuario.setTpPermissaoAcesso(TipoCehUsuario.getTipoCehUsuario(this.tpPermissaoAcesso.getSelectedItem().getValue()));
            cehUsuario.setDeFuncao(FuncaoCehUsuario.getFuncaoCehUsuario(this.deFuncao.getSelectedItem().getValue()));
            cehUsuario.setFlAtivo(this.flAtivo.isChecked());
            cehUsuario.setLtEmail(this.ltEmail.getValue());
            cehUsuario.setNmUsuario(this.nmUsuario.getValue());

            if (Integer.parseInt(this.cdUsuario.getValue()) == -1) {
                //se não tem id significa que é objeto novo, então incluímos no banco
                Integer id = cehUsuarioDAO.incluirAutoincrementando(cehUsuario);
                if (id != null && id > 0) {
                    Toast.show("Usuário cadastrado com sucesso!", "Sucesso", Toast.Type.SUCCESS);
                    CehLogService.getInstance().insereLog("CEH_USUARIO", TipoOcorrenciaLog.INSERCAO);
                    this.cdUsuario.setValue(String.valueOf(cehUsuario.getCdUsuario()));
                    this.voltar();
                } else {
                    zkUtils.MensagemErro("Houve um erro e não foi possível incluir");
                }
            } else {
                //se já tem id significa que é objeto que já existe no banco, então fazemos update no banco
                cehUsuario.setCdUsuario(Integer.valueOf(this.cdUsuario.getValue()));
                boolean atualizou = cehUsuarioDAO.atualizar(cehUsuario);
                if (atualizou) {
                    Toast.show("Usuário atualizado com sucesso!", "Sucesso", Toast.Type.SUCCESS);
                    CehLogService.getInstance().insereLog("CEH_USUARIO", TipoOcorrenciaLog.ALTERACAO);
                    this.voltar();
                } else {
                    zkUtils.MensagemErro("Houve um erro e não foi possível atualizar");
                }
            }

        }
    }

    /**
     * Exclui o objeto Curso do bando de dados.
     */
    public void excluir() {
        if (zkUtils.MensagemConfirmacao("Deseja excluir o Usuário atual?")) {
            if (!this.cdUsuario.getValue().equals("-1")) {
                cehUsuario.setFlAtivo(false);
                cehUsuarioDAO.atualizar(cehUsuario);
                Toast.show("Usuário excluído com sucesso!", "Sucesso", Toast.Type.SUCCESS);
                CehLogService.getInstance().insereLog("CEH_USUARIO", TipoOcorrenciaLog.DESATIVACAO);
                limparCampos();
                this.voltar();
            } else {
                zkUtils.MensagemErro("Usuário inválido");
            }
        }
    }

    /**
     * Volta para a url de retorno.
     */
    public void voltar() {
        Include include = (Include) win.getParent();
        String urlOrigem = include.getSrc();
        zkUtils.setParametro("paginaAtual", this.paginaSalva);
        zkUtils.setParametro("filtroStatus", this.filtroStatus);
        include.setSrc(this.urlRetorno);
    }

    public void searchLdap() {
        String email = this.ltEmail.getValue();
        LdapUtil ldapSearch = new LdapUtil();

        try{
            ldapSearch.search(email);
            this.nmUsuario.setValue(ldapSearch.getNomeCompleto());
            this.nuMatricula.setValue(Integer.parseInt(ldapSearch.getMatricula()));
            this.ltEmail.setValue(ldapSearch.getEmail());
        } catch (Exception e) {
            this.nmUsuario.setValue(null);
            this.nuMatricula.setValue(null);
            this.ltEmail.setValue(null);
            Toast.show(e.getMessage(), "Danger", Toast.Type.ERROR);
//            throw new RuntimeException(e);
        }
    }
}