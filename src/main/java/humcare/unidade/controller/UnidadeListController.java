package humcare.unidade.controller;

import humcare.application.model.Usuario;
import humcare.application.service.CehLogService;
import humcare.application.service.Sessao;
import humcare.application.tipoocorrencia.TipoOcorrenciaLog;
import humcare.unidade.dao.UnidadeDAO;
import humcare.usuario.funcao.FuncaoCdPerfil;
import humcare.unidade.model.Unidade;
import humcare.usuario.tipopermissao.TipoPermissaoUsuario;
import humcare.utilitarios.Utils;
import humcare.utilitarios.ZkUtils;
import humcare.zk.custom.Toast;
import org.apache.commons.lang3.math.NumberUtils;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.*;

import java.text.Normalizer;
import java.util.List;

public class UnidadeListController extends Window {
    private Window win;

    private Listbox vlCampoFiltroPerfil;
    private Textbox vlPesquisa;
    private Grid resultados;
    private Button btnNovo;

    private final ZkUtils zkUtils = new ZkUtils();
    private final UnidadeDAO unidadeDAO = new UnidadeDAO();

    public void onCreate() {

        this.win = (Window) getFellow("winUnidadeList");

        this.btnNovo = (Button) getFellow("btnNovo");
        this.vlCampoFiltroPerfil = (Listbox) getFellow("vlCampoFiltroPerfil");
        this.vlPesquisa = (Textbox) getFellow("vlPesquisa");
        this.resultados = (Grid) getFellow("resultados");
        String filtroStatus = (String) zkUtils.getParametro("filtroStatus");

        if (filtroStatus != null) {
            for (Listitem item : this.vlCampoFiltroPerfil.getItems()) {
                if (item.getValue().equals(filtroStatus)) {
                    this.vlCampoFiltroPerfil.setSelectedItem(item);
                    break;
                }
            }
        }

        Usuario usuarioLogado = Sessao.getInstance().getUsuario();
        if (usuarioLogado.getTpPermissaoAcesso() != FuncaoCdPerfil.ADMINISTRADOR) {
            this.btnNovo.setVisible(false);
        }

        Integer paginaSalva = (Integer) zkUtils.getParametro("paginaAtual");
        if (paginaSalva != null) {
            this.filtrar();
            this.resultados.setActivePage(paginaSalva);
        } else {
            this.filtrar();
        }
    }

    /**
     * Busca no banco os itens do grid. Caso tenha algum filtro, este filtro é
     * adicionado à consulta ao banco.
     */
    public void filtrar() {
        String filtroStatus = this.vlCampoFiltroPerfil.getSelectedItem().getValue();
        String filtro = " 1=1";

        if ("ATIVOS".equals(filtroStatus)) {
            filtro+=" AND u.flAtivo = true ";
        } else if ("INATIVOS".equals(filtroStatus)) {
            filtro+=" AND u.flAtivo = false ";
        }

        String ordem = " order by t.cdUnidade ";

        String textoDaPesquisa = this.vlPesquisa.getValue();
        if (textoDaPesquisa != null && !textoDaPesquisa.isEmpty()) {
            if (NumberUtils.isCreatable(textoDaPesquisa)) {
                filtro+=" AND t.cdUnidade LIKE '%"+ textoDaPesquisa + "%'";
            } else {

                String textoTratado = Normalizer.normalize(textoDaPesquisa, Normalizer.Form.NFD)
                        .replaceAll("[^\\p{ASCII}]", "").trim().toUpperCase();
                filtro+= " AND UPPER(npd.str100_sem_acento(t.deUnidade)) LIKE '%" + textoTratado + "%'";
            }
        }

        List<Unidade> destino = unidadeDAO.listar(filtro, ordem);

        SimpleListModel<Unidade> listModel = new SimpleListModel<>(destino);
        this.resultados.setModel(listModel);
    }

    public void novaUnidade() {
        Usuario usuarioLogado = Sessao.getInstance().getUsuario();
        if (usuarioLogado.getTpPermissaoAcesso() != FuncaoCdPerfil.ADMINISTRADOR) {
            zkUtils.MensagemErro("Acesso negado!");
            return;
        }
        zkUtils.setParametro("acao", "novo");
        this.redirecionar();
    }

    /**
     * Abre a tela para visualização dos dados da unidade.
     */
    public void onClickedVerUnidade(Event event) {
        Integer cdUnidade = Integer.parseInt(event.getTarget().getClientAttribute("id"));
        zkUtils.setParametro("cdUnidade", cdUnidade);
        zkUtils.setParametro("acao", "ler");
        zkUtils.setParametro("filtroStatus", this.vlCampoFiltroPerfil.getSelectedItem().getValue());
        this.redirecionar();
    }

    /**
     * Abre a tela para edição dos dados da unidade.
     */
    public void onClickedAlterarUnidade(Event event) {
        Usuario usuarioLogado = Sessao.getInstance().getUsuario();
        if (usuarioLogado.getTpPermissaoAcesso() != FuncaoCdPerfil.ADMINISTRADOR) {
            zkUtils.MensagemErro("Acesso negado!");
            return;
        }

        Integer cdUnidade = Integer.parseInt(event.getTarget().getClientAttribute("id"));
        zkUtils.setParametro("cdUnidade", cdUnidade);
        zkUtils.setParametro("acao", "editar");
        zkUtils.setParametro("filtroStatus", this.vlCampoFiltroPerfil.getSelectedItem().getValue());
        this.redirecionar();
    }

    /**
     * Realiza a exclusão lógica da unidade.
     */
    public void onClickedDeletarUnidade(Event event) {
        Usuario usuarioLogado = Sessao.getInstance().getUsuario();
        // Apenas administradores podem excluir
        if (usuarioLogado.getTpPermissaoAcesso() != FuncaoCdPerfil.ADMINISTRADOR) {
            zkUtils.MensagemErro("Acesso negado!");
            return;
        }

        int cdUnidade = Integer.parseInt(event.getTarget().getClientAttribute("id"));

        if (zkUtils.MensagemConfirmacao("Deseja realmente excluir esta Unidade?")) {
            Unidade unidadeParaExcluir = unidadeDAO.buscaPorIdUnidade(cdUnidade);

            if (unidadeParaExcluir != null) {
                unidadeParaExcluir.setFlAtivo(false);
                unidadeDAO.atualizar(unidadeParaExcluir);

                Toast.show("Unidade excluída com sucesso!", "Sucesso", Toast.Type.SUCCESS);

                CehLogService.getInstance().insereLog("BDC_UNIDADE", TipoOcorrenciaLog.DESATIVACAO);
                this.filtrar();
            } else {
                zkUtils.MensagemErro("Unidade inválida ou não encontrada!");
            }
        }
    }

    public void voltar(){
        Include include = (Include) win.getParent();
        include.setSrc("/paginas/home/HomeLogada.zul");
    }

    /**
     * Redireciona para a página de cadastro/edição da Unidade.
     */
    private void redirecionar() {
        Include include = (Include) win.getParent();
        String urlOrigem = include.getSrc();
        include.setSrc(null);
        zkUtils.setParametro("paginaAtual", this.resultados.getActivePage());
        zkUtils.setParametro("url_retorno", urlOrigem);

        include.setSrc("paginas/unidade/unidade.zul");
    }

}
