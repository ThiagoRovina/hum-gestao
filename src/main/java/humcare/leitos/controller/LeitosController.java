package humcare.leitos.controller;

import humcare.application.service.CehLogService;
import humcare.application.tipoocorrencia.TipoOcorrenciaLog;
import humcare.leitos.dao.LeitoDAO;
import humcare.leitos.model.Leitos;
import humcare.leitos.status.StatusLeito; // Supondo que você criou este Enum
import humcare.unidade.dao.UnidadeDAO;
import humcare.unidade.model.Unidade;
import humcare.utilitarios.ZkUtils;
import humcare.zk.custom.Toast;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.*;

import java.text.ParseException;
import java.util.List;

public class LeitosController extends Window {

    private Window win;
    private Label lbAtivo;
    private Label cdLeito;
    private Textbox idLeito;
    private Listbox cdUnidade;
    private Listbox stLeito;
    private Checkbox flAtivo;

    private Button btnSalvar;
    private Button btnExcluir;
    private Button btnCancelar;

    private final ZkUtils zkUtils = new ZkUtils();
    private final LeitoDAO leitoDAO = new LeitoDAO();
    private final UnidadeDAO unidadeDAO = new UnidadeDAO();
    private Leitos leito = new Leitos();

    // Parâmetros para navegação
    private String urlRetorno = "";
    private String filtroStatus = "";
    private Integer paginaSalva;

    public void onCreate() {
        this.win = (Window) getFellow("winLeito");

        this.cdLeito = (Label) getFellow("cdLeito");
        this.idLeito = (Textbox) getFellow("idLeito");
        this.cdUnidade = (Listbox) getFellow("cdUnidade");
        this.stLeito = (Listbox) getFellow("stLeito");
        this.flAtivo = (Checkbox) getFellow("flAtivo");
        this.lbAtivo = (Label) getFellow("lbAtivo");

        this.btnSalvar = (Button) getFellow("salvar");
        this.btnExcluir = (Button) getFellow("excluir");
        this.btnCancelar = (Button) getFellow("cancelar");

        popularListboxes();

        Integer idLeito = (Integer) zkUtils.getParametro("cdLeito");
        String acao = (String) zkUtils.getParametro("acao");
        this.urlRetorno = (String) zkUtils.getParametro("url_retorno");
        this.filtroStatus = (String) zkUtils.getParametro("filtroStatus");
        this.paginaSalva = (Integer) zkUtils.getParametro("paginaAtual");

        limparCampos();
        configurarTelaPorAcao(acao);

        if (idLeito != null) {
            leito = leitoDAO.buscar(idLeito);
            if (leito != null) {
                popularCampos();
            }
        }
    }

    /**
     * Carrega as opções para os listboxes de Unidade e Status.
     */
    private void popularListboxes() {
        List<Unidade> unidadesAtivas = unidadeDAO.listar("t.flAtivo = 1", "order by t.deUnidade");
        for (Unidade unidade : unidadesAtivas) {
            this.cdUnidade.appendChild(new Listitem(unidade.getDeUnidade(), unidade.getCdUnidade()));
        }

        this.limpaeAdicionaSituacaoDisbledLeito();
        for (StatusLeito status : StatusLeito.values()) {
            this.adicionarItemStatus(status);
        }
    }

    private void limpaeAdicionaSituacaoDisbledLeito(){
        this.stLeito.clearSelection();
        this.stLeito.getItems().clear();
        Listitem item = new Listitem();
        item.setLabel("Selecione um status...");
        item.setSelected(true);
        item.setDisabled(true);
        this.stLeito.appendChild(item);
    }

    private void configurarTelaPorAcao(String acao) {
        if ("novo".equals(acao)) {
            this.btnSalvar.setVisible(true);
            this.btnCancelar.setVisible(true);
            this.btnExcluir.setVisible(false);
            this.cdLeito.setVisible(false);
            this.idLeito.setReadonly(false);
            this.flAtivo.setChecked(true);
            this.idLeito.setFocus(true);
        } else if ("editar".equals(acao)) {
            this.btnSalvar.setVisible(true);
            this.btnCancelar.setVisible(true);
            this.btnExcluir.setVisible(true);
            this.idLeito.setReadonly(false);
            this.flAtivo.setDisabled(false);
        } else if ("ler".equals(acao)) {
            this.btnSalvar.setVisible(false);
            this.btnExcluir.setVisible(false);
            this.btnCancelar.setLabel("Voltar");
            this.idLeito.setReadonly(true);
            this.cdUnidade.setDisabled(true);
            this.stLeito.setDisabled(true);
            this.flAtivo.setDisabled(true);
        }
        mudarStatusLabel();
    }

    public void limparCampos() {
        this.cdLeito.setValue("-1");
        this.idLeito.setValue(null);
        this.cdUnidade.setSelectedIndex(0);
        this.stLeito.setSelectedIndex(0);
        this.flAtivo.setChecked(false);
    }

    private void adicionarItemStatus(StatusLeito status) {
        Listitem item = new Listitem();
        item.setLabel(status.getDescricao());
        item.setValue(status.getValor());
        this.stLeito.appendChild(item);
    }

    public void popularCampos() {
        limparCampos();
        zkUtils.popularCampo(cdLeito, this.leito.getCdLeito());
        zkUtils.popularCampo(idLeito, this.leito.getIdLeito());
        zkUtils.selecionarLista(cdUnidade, this.leito.getCdUnidade());
        if (this.leito.getStLeito() != null) {

            this.limpaeAdicionaSituacaoDisbledLeito();
            // Adiciona apenas os permitidos pela sua regra de negócio
            for (StatusLeito permitido : this.leito.getStLeito().getProximosStatusPossiveis()) {
                adicionarItemStatus(permitido);
            }

            zkUtils.selecionarLista(stLeito, this.leito.getStLeito().getValor());
        }
        this.flAtivo.setChecked(this.leito.getFlAtivo());
        mudarStatusLabel();
    }

    public void mudarStatusLabel() {
        lbAtivo.setValue(flAtivo.isChecked() ? "Status: Ativo" : "Status: Inativo");
    }

    public boolean validarCampos() {
        if (this.idLeito.getValue().trim().isEmpty()) {
            Clients.showNotification("O identificador do leito é obrigatório!", Clients.NOTIFICATION_TYPE_WARNING, idLeito, "end_center", 2000);
            return false;
        }
        if (this.cdUnidade.getSelectedIndex() <= 0) {
            Clients.showNotification("A seleção de uma unidade é obrigatória!", Clients.NOTIFICATION_TYPE_WARNING, cdUnidade, "end_center", 2000);
            return false;
        }
        if (this.stLeito.getSelectedIndex() <= 0) {
            Clients.showNotification("A seleção do status do leito é obrigatória!", Clients.NOTIFICATION_TYPE_WARNING, stLeito, "end_center", 2000);
            return false;
        }
        return true;
    }

    public void gravar() throws ParseException {
        if (validarCampos()) {
            String statusString = this.stLeito.getSelectedItem().getValue();
            StatusLeito statusEnum = StatusLeito.fromString(statusString);
            leito.setStLeito(statusEnum);
            leito.setCdUnidade(this.cdUnidade.getSelectedItem().getValue());
            leito.setIdLeito(this.idLeito.getValue());
            leito.setFlAtivo(this.flAtivo.isChecked());

            if (Integer.parseInt(this.cdLeito.getValue()) == -1) {
                Integer id = leitoDAO.incluirAutoincrementando(leito);
                if(id != null && id > 0){
                    Toast.show("Unidade cadastrado com sucesso!", "Sucesso", Toast.Type.SUCCESS);
                    CehLogService.getInstance().insereLog("BDC_LEITO", TipoOcorrenciaLog.INSERCAO);
                    this.cdLeito.setValue(String.valueOf(leito.getCdLeito()));
                    this.voltar();
                }else{
                    zkUtils.MensagemErro("Houve um erro e não foi possivel incluir");
                }
            } else {
                leito.setCdLeito(Integer.parseInt(this.cdLeito.getValue()));
                boolean atualizou = leitoDAO.atualizar(leito);
                if (atualizou) {
                    Toast.show("Leito atualizada com sucesso!", "Sucesso", Toast.Type.SUCCESS);
                    CehLogService.getInstance().insereLog("BDC_LEITO", TipoOcorrenciaLog.ALTERACAO);
                    this.voltar();
                } else {
                    zkUtils.MensagemErro("Houve um erro e não foi possível atualizar o usuário.");
                }
            }
        }
    }

    public void excluir() {
        if (leito.getCdLeito() != null && zkUtils.MensagemConfirmacao("Deseja realmente inativar este leito?")) {
            leito.setFlAtivo(false);
            leitoDAO.atualizar(leito);
            Toast.show("Leito inativado com sucesso!", "Sucesso", Toast.Type.SUCCESS);
            CehLogService.getInstance().insereLog("BDC_LEITO", TipoOcorrenciaLog.DESATIVACAO);
            voltar();
        }
    }

    public void voltar() {
        Include include = (Include) win.getParent();
        zkUtils.setParametro("paginaAtual", paginaSalva);
        zkUtils.setParametro("filtroStatus", filtroStatus);
        include.setSrc(urlRetorno);
    }
}