package humcare.nir.controller;

import humcare.application.model.Usuario;
import humcare.application.service.CehLogService;
import humcare.application.service.Sessao;
import humcare.application.tipoocorrencia.TipoOcorrenciaLog;
import humcare.leitos.dao.LeitoDAO;
import humcare.leitos.model.Leitos;
import humcare.unidade.dao.UnidadeDAO;
import humcare.unidade.model.Unidade;
import humcare.usuario.funcao.FuncaoCdPerfil;
import humcare.utilitarios.ZkUtils;
import humcare.zk.custom.Toast;
import org.apache.commons.lang3.math.NumberUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.*;

import java.text.Normalizer;
import java.util.List;

public class CentralNirLIstController extends Window{
    private Window win;
    private Div resultados;
    private Paging pagingResultados;

    private List<Leitos> listaCompletaLeitos;
    private final ZkUtils zkUtils = new ZkUtils();
    private final LeitoDAO leitoDAO = new LeitoDAO();

    public void onCreate() {
        this.win = (Window) getFellow("winCentralNIRList");
        this.resultados = (Div) getFellowIfAny("resultados");
        this.pagingResultados = (Paging) getFellow("pagingResultados");

        this.resultados.setSclass("resultados-container");
        this.resultados.setStyle("display: flex; flex-wrap: wrap; justify-content: center; gap: 24px; padding: 16px;");

        this.filtrar();
        Integer paginaSalva = (Integer) zkUtils.getParametro("paginaAtual");
        if (paginaSalva != null && paginaSalva < this.pagingResultados.getPageCount()) {
            this.pagingResultados.setActivePage(paginaSalva);
        }
    }

    public void filtrar() {
        this.listaCompletaLeitos = leitoDAO.listar(" 1=1 ", " order by t.cdLeito ");
        this.pagingResultados.setTotalSize(this.listaCompletaLeitos.size());
        this.pagingResultados.setActivePage(0);
        renderizarPagina();
    }

    public void renderizarPagina() {
        resultados.getChildren().clear();
        int pageSize = pagingResultados.getPageSize();
        int activePage = pagingResultados.getActivePage();
        int startIndex = activePage * pageSize;
        int endIndex = Math.min(startIndex + pageSize, listaCompletaLeitos.size());

        for (int i = startIndex; i < endIndex; i++) {
            Leitos leito = listaCompletaLeitos.get(i);
            resultados.appendChild(criarCardLeito(leito));
        }
    }

    public void voltar() {
        Component parent = win.getParent();
        if (parent instanceof Include) {
            Include include = (Include) parent;
            include.setSrc("/paginas/home/HomeLogada.zul");
        } else {
            Executions.sendRedirect("/login.zul");
        }
    }

    private String getStatusCssClass(String statusDescricao) {
        if (statusDescricao == null) {
            return "desativado";
        }

        switch (statusDescricao.toLowerCase().replaceAll("[^a-z]", "")) {
            case "ocupado":
                return "ocupado";
            case "higienizacao":
                return "higienizacao";
            case "livre":
                return "livre";
            case "desativado":
                return "desativado";
            default:
                return "desativado";
        }
    }

    private Div criarCardLeito(Leitos leito) {
        Div container = new Div();
        container.setSclass("bed-container");
        container.setStyle("cursor: pointer;");

        container.setAttribute("cdLeito", leito.getCdLeito());
        container.addEventListener("onClick", this::onClickedVerHigienizacao);

        Div card = new Div();
        String statusDescricao = (leito.getStLeito() != null) ? leito.getStLeito().getDescricao() : "Desativado";
        String statusClass = getStatusCssClass(statusDescricao);
        card.setSclass("bed-card " + statusClass);

        Div mainContent = new Div();
        mainContent.setSclass("card-main-content");

        Label statusLabel = new Label(statusDescricao);
        statusLabel.setSclass("status-label");
        mainContent.appendChild(statusLabel);

        Div footer = new Div();
        footer.setSclass("card-footer");

        card.appendChild(mainContent);
        card.appendChild(footer);

        Label bedIdLabel = new Label(leito.getIdLeito());
        bedIdLabel.setSclass("bed-id-label");

        container.appendChild(card);
        container.appendChild(bedIdLabel);

        return container;
    }

    public void novoLeito() {
        Component parent = win.getParent();

        if (parent instanceof Include) {
            Include include = (Include) parent;
            String urlOrigem = include.getSrc();
            include.setSrc(null);

            zkUtils.setParametro("acao", "novo");
            zkUtils.setParametro("url_retorno", urlOrigem);

            include.setSrc("/paginas/leitos/Leitos.zul");
        } else {
            Executions.sendRedirect("/paginas/leitos/Leitos.zul?acao=novo");
        }
    }

    public void onClickedVerHigienizacao(Event event) {
        Integer cdLeito = (Integer) event.getTarget().getAttribute("cdLeito");

        Component parent = win.getParent();

        if (parent instanceof Include) {
            Include include = (Include) parent;
            String urlOrigem = include.getSrc();
            include.setSrc(null);

            zkUtils.setParametro("cdLeito", cdLeito);
            zkUtils.setParametro("acao", "editar");
            zkUtils.setParametro("url_retorno", urlOrigem);
            zkUtils.setParametro("paginaAtual", this.pagingResultados.getActivePage());

            include.setSrc("/paginas/higienizacao/higienizacao.zul");
        } else {
            // Standalone page, so redirect with URL parameters
            String targetUrl = "/paginas/higienizacao/higienizacao.zul?cdLeito=" + cdLeito;
            Executions.sendRedirect(targetUrl);
        }
    }
}
