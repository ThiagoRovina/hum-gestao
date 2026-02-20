package humcare.leitos.controller;

import humcare.application.model.Usuario;
import humcare.application.service.CehLogService;
import humcare.application.service.Sessao;
import humcare.application.tipoocorrencia.TipoOcorrenciaLog;
import humcare.leitos.dao.LeitoDAO;
import humcare.leitos.dto.LeitosListDTO;
import humcare.leitos.model.Leitos;
import humcare.unidade.dao.UnidadeDAO;
import humcare.unidade.model.Unidade;
import humcare.usuario.funcao.FuncaoCdPerfil;
import humcare.utilitarios.ZkUtils;
import humcare.zk.custom.Toast;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.*;

import java.text.Normalizer;
import java.util.List;

public class LeitosListController extends Window {

    private Window win;
    private Textbox vlPesquisa;
    private Listbox vlFiltroUnidade;
    private Listbox vlFiltroStatusLeito;
    private Listbox vlFiltroAtivo;
    private Grid resultados;
    private Button btnNovo;

    private final ZkUtils zkUtils = new ZkUtils();
    private final LeitoDAO leitoDAO = new LeitoDAO();
    private final UnidadeDAO unidadeDAO = new UnidadeDAO();

    public void onCreate() {
        this.win = (Window) getFellow("winLeitosList");

        this.vlPesquisa = (Textbox) getFellow("vlPesquisa");
        this.vlFiltroUnidade = (Listbox) getFellow("vlFiltroUnidade");
        this.vlFiltroStatusLeito = (Listbox) getFellow("vlFiltroStatusLeito");
        this.vlFiltroAtivo = (Listbox) getFellow("vlFiltroAtivo");
        this.resultados = (Grid) getFellow("resultados");
        this.btnNovo = (Button) getFellow("btnNovo");

        // Permissão para criar novo leito
        Usuario usuarioLogado = Sessao.getInstance().getUsuario();
        if (usuarioLogado.getTpPermissaoAcesso() != FuncaoCdPerfil.ADMINISTRADOR) {
            this.btnNovo.setVisible(false);
        }

        popularFiltros();

        // Mantém a páginação ao voltar do formulário
        Integer paginaSalva = (Integer) zkUtils.getParametro("paginaAtual");
        if (paginaSalva != null) {
            this.filtrar();
            this.resultados.setActivePage(paginaSalva);
        } else {
            this.filtrar();
        }
    }

    /**
     * Carrega as opções para os filtros de Unidade e Status do Leito.
     */
    private void popularFiltros() {
        // Carrega Unidades Ativas para o filtro
        List<Unidade> unidadesAtivas = unidadeDAO.listar("t.flAtivo = 1", "order by t.deUnidade");
        for (Unidade unidade : unidadesAtivas) {
            this.vlFiltroUnidade.appendChild(new Listitem(unidade.getDeUnidade(), unidade.getCdUnidade()));
        }

    }

    /**
     * Busca no banco os leitos, aplicando os filtros selecionados na tela.
     */
    public void filtrar() {
        String filtro =" 1=1 ";

        // Filtro por texto (Identificador do Leito)
        String textoDaPesquisa = this.vlPesquisa.getValue();
        if (textoDaPesquisa != null && !textoDaPesquisa.isEmpty()) {
            String textoTratado = Normalizer.normalize(textoDaPesquisa, Normalizer.Form.NFD)
                    .replaceAll("[^\\p{ASCII}]", "").trim().toUpperCase();
            filtro+= " AND UPPER(t.idLeito) LIKE '%" + textoTratado + "%'";
        }

        // Filtro por Unidade
        if (vlFiltroUnidade.getSelectedIndex() > 0) { // Index 0 é "Todas"
            filtro+= " AND t.cdUnidade = " + vlFiltroUnidade.getSelectedItem().getValue();
        }

        // Filtro por Status do Leito (Livre, Ocupado, etc.)
        if (vlFiltroStatusLeito.getSelectedIndex() > 0) { // Index 0 é "Todos"
            filtro+= " AND t.stLeito = '" + vlFiltroStatusLeito.getSelectedItem().getValue()+ "'";
        }

        // Filtro por Ativo/Inativo
        String filtroAtivo = vlFiltroAtivo.getSelectedItem().getValue();
        if ("ATIVOS".equals(filtroAtivo)) {
            filtro+=" AND t.flAtivo = true ";
        } else if ("INATIVOS".equals(filtroAtivo)) {
            filtro+= " AND t.flAtivo = false ";
        }

        String ordem = " ORDER BY t.cdLeito ";
        List<LeitosListDTO> leitosEncontrados = leitoDAO.listarLeitosUnidade(filtro, ordem);

        SimpleListModel<LeitosListDTO> listModel = new SimpleListModel<>(leitosEncontrados);
        this.resultados.setModel(listModel);

    }

    /**
     * Abre a tela de cadastro para um novo leito.
     */
    public void novoLeito() {
        zkUtils.setParametro("acao", "novo");
        this.redirecionar();
    }

    /**
     * Abre a tela para visualização dos dados do leito.
     */
    public void onClickedVerLeito(Event event) {
        Integer cdLeito = Integer.parseInt(event.getTarget().getClientAttribute("id"));
        zkUtils.setParametro("cdLeito", cdLeito);
        zkUtils.setParametro("acao", "ler");
        this.redirecionar();
    }

    /**
     * Abre a tela para edição dos dados do leito.
     */
    public void onClickedAlterarLeito(Event event) {
        Usuario usuarioLogado = Sessao.getInstance().getUsuario();
        if (usuarioLogado.getTpPermissaoAcesso() != FuncaoCdPerfil.ADMINISTRADOR) {
            zkUtils.MensagemErro("Acesso negado!");
            return;
        }

        Integer cdLeito = Integer.parseInt(event.getTarget().getClientAttribute("id"));
        zkUtils.setParametro("cdLeito", cdLeito);
        zkUtils.setParametro("acao", "editar");
        this.redirecionar();
    }

    /**
     * Realiza a exclusão lógica (inativação) do leito.
     */
    public void onClickedDeletarLeito(Event event) {
        Usuario usuarioLogado = Sessao.getInstance().getUsuario();
        if (usuarioLogado.getTpPermissaoAcesso() != FuncaoCdPerfil.ADMINISTRADOR) {
            zkUtils.MensagemErro("Acesso negado!");
            return;
        }

        int cdLeito = Integer.parseInt(event.getTarget().getClientAttribute("id"));
        if (zkUtils.MensagemConfirmacao("Deseja realmente inativar este Leito?")) {
            Leitos leitoParaInativar = leitoDAO.buscar(cdLeito);
            if (leitoParaInativar != null) {
                leitoParaInativar.setFlAtivo(false);
                leitoDAO.atualizar(leitoParaInativar);

                Toast.show("Leito inativado com sucesso!", "Sucesso", Toast.Type.SUCCESS);
                CehLogService.getInstance().insereLog("BDC_LEITO", TipoOcorrenciaLog.DESATIVACAO);
                this.filtrar();
            } else {
                zkUtils.MensagemErro("Leito inválido ou não encontrado!");
            }
        }
    }

    public void voltar() {
        Include include = (Include) win.getParent();
        include.setSrc("/paginas/home/HomeLogada.zul");
    }

    /**
     * Redireciona para a página de formulário do Leito.
     */
    private void redirecionar() {
        Include include = (Include) win.getParent();
        String urlOrigem = include.getSrc();
        include.setSrc(null);
        zkUtils.setParametro("paginaAtual", this.resultados.getActivePage());
        zkUtils.setParametro("url_retorno", urlOrigem);

        include.setSrc("paginas/leitos/Leitos.zul");
    }
}