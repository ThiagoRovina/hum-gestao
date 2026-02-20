package humcare.unidade.controller;

import humcare.application.service.CehLogService;
import humcare.application.tipoocorrencia.TipoOcorrenciaLog;
import humcare.unidade.dao.UnidadeDAO;
import humcare.unidade.model.Unidade;
import humcare.usuario.funcao.FuncaoCdPerfil;
import humcare.utilitarios.PasswordUtil;
import humcare.utilitarios.ZkUtils;
import humcare.zk.custom.Toast;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.*;

import java.text.ParseException;


public class UnidadeController extends Window {
    private Window win;
    private Label lbAtivo;
    private Label cdUnidade;
    private Textbox deUnidade;
    private Checkbox flAtivo;

    private Button btnSalvar;
    private Button btnExcluir;
    private Button btnCancelar;

    private final ZkUtils zkUtils = new ZkUtils();
    private final UnidadeDAO unidadeDAO = new UnidadeDAO();
    private Unidade unidade = new Unidade();

    private String urlRetorno = "";
    private String filtroStatus = "";
    private Integer paginaSalva;

    public void onCreate() {

        this.win = (Window) getFellow("winUnidade");
        this.cdUnidade = (Label) getFellow("cdUnidade");
        this.deUnidade = (Textbox) getFellow("deUnidade");
        this.flAtivo = (Checkbox) getFellow("flAtivo");
        this.lbAtivo = (Label) getFellow("lbAtivo");

        this.btnSalvar = (Button) getFellow("salvar");
        this.btnExcluir = (Button) getFellow("excluir");
        this.btnCancelar = (Button) getFellow("cancelar");


        Integer unidadeId = (Integer) zkUtils.getParametro("cdUnidade");
        String acao = (String) zkUtils.getParametro("acao");
        this.urlRetorno = (String) zkUtils.getParametro("url_retorno");
        this.filtroStatus = (String) zkUtils.getParametro("filtroStatus");
        this.paginaSalva = (Integer) zkUtils.getParametro("paginaAtual");

        limparCampos();
        configurarTelaPorAcao(acao);

        if (unidadeId != null) {
            unidade = unidadeDAO.buscar(unidadeId);
            if (unidade != null) {
                popularCampos();
            }
        }
    }

    private void configurarTelaPorAcao(String acao) {
        if ("novo".equals(acao)) {
            this.btnSalvar.setVisible(true);
            this.btnCancelar.setVisible(true);
            this.btnExcluir.setVisible(false);
            this.cdUnidade.setVisible(false);
            this.deUnidade.setReadonly(false);
            this.flAtivo.setChecked(true);
            this.deUnidade.setFocus(true);
        } else if ("editar".equals(acao)) {
            this.btnSalvar.setVisible(true);
            this.btnCancelar.setVisible(true);
            this.btnExcluir.setVisible(true);
            this.deUnidade.setReadonly(false);
            this.flAtivo.setDisabled(false);
        } else if ("ler".equals(acao)) {
            this.btnSalvar.setVisible(false);
            this.btnExcluir.setVisible(false);
            this.btnCancelar.setLabel("Voltar");
            this.deUnidade.setReadonly(true);
            this.flAtivo.setDisabled(true);
        }
        mudarStatusLabel();
    }

    /**
     * Limpa os campos do formulário para o estado inicial.
     */

    public void limparCampos() {
        this.cdUnidade.setValue("-1");
        this.deUnidade.setValue(null);
        this.flAtivo.setChecked(false);
    }

    /**
     * Preenche os campos do formulário com os dados da entidade 'Unidade'.
     */

    public void popularCampos() {
        limparCampos();
        zkUtils.popularCampo(cdUnidade, this.unidade.getCdUnidade());
        zkUtils.popularCampo(deUnidade, this.unidade.getDeUnidade());
        this.flAtivo.setChecked(this.unidade.getFlAtivo());
        mudarStatusLabel();
    }

    /**
     * Atualiza o texto do label de status (Ativo/Inativo).
     */

    public void mudarStatusLabel() {
        lbAtivo.setValue(flAtivo.isChecked() ? "Status: Ativo" : "Status: Inativo");
    }

    /**
     * Valida se os campos obrigatórios foram preenchidos.
     */
    public boolean validarCampos() {
        if (this.deUnidade.getValue().trim().isEmpty()) {
            Clients.showNotification("O nome da unidade é obrigatório!", Clients.NOTIFICATION_TYPE_WARNING, deUnidade, "end_center", 2000);
            this.deUnidade.setFocus(true);
            return false;
        }
        return true;
    }

    /**
     * Salva uma nova unidade ou atualiza uma existente.
     */
    public void gravar() throws ParseException {
        if (validarCampos()) {
            unidade.setDeUnidade(this.deUnidade.getValue());
            unidade.setFlAtivo(this.flAtivo.isChecked());

            if (Integer.parseInt(this.cdUnidade.getValue()) == -1) {
                Integer id = unidadeDAO.incluirAutoincrementando(unidade);
                if(id != null && id > 0){
                    Toast.show("Unidade cadastrado com sucesso!", "Sucesso", Toast.Type.SUCCESS);
                    CehLogService.getInstance().insereLog("BDC_UNIDADE", TipoOcorrenciaLog.INSERCAO);
                    this.cdUnidade.setValue(String.valueOf(unidade.getCdUnidade()));
                    this.voltar();
                }else{
                    zkUtils.MensagemErro("Houve um erro e não foi possivel incluir");
                }
            } else {
                unidade.setCdUnidade(Integer.parseInt(this.cdUnidade.getValue()));
                boolean atualizou = unidadeDAO.atualizar(unidade);
                if (atualizou) {
                    Toast.show("Unidade atualizada com sucesso!", "Sucesso", Toast.Type.SUCCESS);
                    CehLogService.getInstance().insereLog("BDC_UNIDADE", TipoOcorrenciaLog.ALTERACAO);
                    this.voltar();
                } else {
                    zkUtils.MensagemErro("Houve um erro e não foi possível atualizar o usuário.");
                }
            }
        }
    }

    /**
     * Realiza a exclusão lógica (inativação) da unidade.
     */
    public void excluir() {
        if (unidade.getCdUnidade() != null && zkUtils.MensagemConfirmacao("Deseja realmente inativar esta unidade?")) {
            unidade.setFlAtivo(false);
            unidadeDAO.atualizar(unidade);
            Toast.show("Unidade inativada com sucesso!", "Sucesso", Toast.Type.SUCCESS);
            CehLogService.getInstance().insereLog("BDC_UNIDADE", TipoOcorrenciaLog.DESATIVACAO);
            voltar();
        }
    }

    /**
     * Retorna para a tela de listagem, mantendo os filtros e a páginação.
     */
    public void voltar() {
        Include include = (Include) win.getParent();
        zkUtils.setParametro("paginaAtual", paginaSalva);
        zkUtils.setParametro("filtroStatus", filtroStatus);
        include.setSrc(urlRetorno);
    }
}
