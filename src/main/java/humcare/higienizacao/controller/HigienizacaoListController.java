package humcare.higienizacao.controller;

import humcare.higienizacao.tipoHigienizacao.TipoHigienizacao;
import humcare.leitos.dao.LeitoDAO;
import humcare.leitos.dto.LeitosHigienizacaoListDTO;
import humcare.leitos.model.Leitos;
import humcare.utilitarios.ZkUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.*;

import java.util.List;

public class HigienizacaoListController extends Window {
    private Window win;
    private Div resultados;
    private Paging pagingResultados;

    private List<LeitosHigienizacaoListDTO> listaCompletaLeitos;
    private final ZkUtils zkUtils = new ZkUtils();
    private final LeitoDAO leitoDAO = new LeitoDAO();

    public void onCreate() {
        this.win = (Window) getFellow("winHigienizacaoList");
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
        this.listaCompletaLeitos = leitoDAO.listarLeitosHigienizacao();
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
            LeitosHigienizacaoListDTO leito = listaCompletaLeitos.get(i);
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
            case "emhigienizao":
                return "higienizacao";
            case "livre":
                return "livre";
            case "desativado":
                return "desativado";
            default:
                return "desativado";
        }
    }

    private Div criarCardLeito(LeitosHigienizacaoListDTO leito) {
        Div container = new Div();
        // Adicionamos as classes de grid do Bootstrap 3
        // col-xs-12 (1 por linha no celular), col-sm-6 (2 por linha), col-md-3 (4 por linha)
        container.setSclass("bed-container col-xs-12 col-sm-6 col-md-4 col-lg-2");
        container.setStyle("cursor: pointer; margin-bottom: 20px;"); // Margem para não colarem verticalmente

        container.setAttribute("cdLeito", leito.getCdLeito());
        container.addEventListener("onClick", this::onClickedVerHigienizacao);

        Div card = new Div();
        String statusDescricao = (leito.getStLeito() != null) ? leito.getStLeito().getDescricao() : "Desativado";
        String statusClass = getStatusCssClass(statusDescricao);
        // Adicionada a classe 'thumbnail' ou 'panel' do Bootstrap se quiser um visual padrão,
        // mas mantive sua estrutura original com bed-card
        card.setSclass("bed-card " + statusClass);

        Div mainContent = new Div();
        mainContent.setSclass("card-main-content");

        Label statusLabel = new Label(statusDescricao);
        statusLabel.setSclass("status-label");
        mainContent.appendChild(statusLabel);

        // Rodapé responsivo
        Div footer = new Div();
        footer.setSclass("card-footer");

        TipoHigienizacao tipo = leito.getTpHigienizacao();

        if (tipo != null && tipo.getValor() != null) {
            Div divMr = new Div();
            divMr.setSclass(this.getLabelMr(tipo.getValor()) + " center-block text-center");
            divMr.setStyle("padding: 4px 8px; border-radius: 4px; font-weight: bold;");

            Label labelMr = new Label(TipoHigienizacao.getTipoHigienizacaoString(tipo.getValor()));
            divMr.appendChild(labelMr);
            footer.appendChild(divMr);
        }

        card.appendChild(mainContent);
        card.appendChild(footer);

        Label bedIdLabel = new Label(leito.getIdLeito());
        bedIdLabel.setSclass("bed-id-label text-center"); // Centraliza o ID do leito abaixo do card
        bedIdLabel.setStyle("display: block; width: 100%; margin-top: 5px;");

        container.appendChild(card);
        container.appendChild(bedIdLabel);

        return container;
    }

//    private Div criarCardLeito(LeitosHigienizacaoListDTO leito) {
//        Div container = new Div();
//        container.setSclass("bed-container");
//        container.setStyle("cursor: pointer;");
//
//        container.setAttribute("cdLeito", leito.getCdLeito());
//        container.addEventListener("onClick", this::onClickedVerHigienizacao);
//
//        Div card = new Div();
//        String statusDescricao = (leito.getStLeito() != null) ? leito.getStLeito().getDescricao() : "Desativado";
//        String statusClass = getStatusCssClass(statusDescricao);
//        card.setSclass("bed-card " + statusClass);
//
//        Div mainContent = new Div();
//        mainContent.setSclass("card-main-content");
//
//        Label statusLabel = new Label(statusDescricao);
//        statusLabel.setSclass("status-label");
//        mainContent.appendChild(statusLabel);
//
//        Div footer = new Div();
//        footer.setSclass("card-footer");
//
//        Div divMr = new Div();
//        divMr.setSclass(this.getLabelMr(leito.getTpHigienizacao().getValor()));
//        divMr.setStyle("padding: 4px 8px; border-radius: 4px;");
//
//        Label labelMr = new Label(TipoHigienizacao.getTipoHigienizacaoString(leito.getTpHigienizacao().getValor()));
//        divMr.appendChild(labelMr);
//        footer.appendChild(divMr);
//
//
//        card.appendChild(mainContent);
//        card.appendChild(footer);
//
//        Label bedIdLabel = new Label(leito.getIdLeito());
//        bedIdLabel.setSclass("bed-id-label");
//
//        container.appendChild(card);
//        container.appendChild(bedIdLabel);
//
//        return container;
//    }

    private String getLabelMr(Integer tpHigienizacao) {
        switch (tpHigienizacao) {
            case 0:
                return "simples";
            case 1:
                return "mr";
            case 2:
                return "mr-preto";
            default:
                return null;
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