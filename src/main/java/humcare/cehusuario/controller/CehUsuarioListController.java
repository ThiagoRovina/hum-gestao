package humcare.cehusuario.controller;

import humcare.application.service.CehLogService;
import humcare.application.service.Sessao;
import humcare.application.tipoocorrencia.TipoOcorrenciaLog;
import humcare.cehusuario.dao.CehUsuarioDAO;
import humcare.cehusuario.model.CehUsuario;
import humcare.cehusuario.tipopermissao.TipoCehUsuario;
//import humcare.destino.dao.DestinoDAO;
//import humcare.destino.model.Destino;
//import humcare.solicitacao.status.StatusSolicitacao;
import org.apache.commons.lang3.math.NumberUtils;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.*;
import humcare.utilitarios.Utils;
import humcare.utilitarios.ZkUtils;
import humcare.zk.custom.Toast;

import java.text.Normalizer;
import java.util.List;

public class CehUsuarioListController extends Window {

    private Window win;

    private Listbox vlCampoFiltroPerfil;
    private Textbox vlPesquisa;
    private Grid resultados;

    private final Utils utils = new Utils();
    private final ZkUtils zkUtils = new ZkUtils();

    private final CehUsuarioDAO cehUsuarioDAO = new CehUsuarioDAO();

    public void onCreate() {
        this.win = (Window) getFellow("winCehUsuarioList");

        //pegamos os campos definidos no .zul e vinculamos à uma variável
        //para pegarmos ou pularmos com valores
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

        Integer paginaSalva = (Integer) zkUtils.getParametro("paginaAtual");;
        if (paginaSalva != null) {
            this.filtrar(filtroStatus);
            this.resultados.setActivePage(paginaSalva);
        } else {
            this.filtrar(filtroStatus);
        }
    }

    /**
     * Busca no banco os itens do grid. Caso tenha algum filtro, este filtro é
     * adicionado à consulta ao banco.
     */
    public void filtrar(String filtroStatus) {
        filtroStatus = filtroStatus == null ? this.vlCampoFiltroPerfil.getSelectedItem().getValue() : filtroStatus;
        String filtro;

        if (!filtroStatus.equals("TODOS")) {
            filtro = " 1=1 AND tpPermissaoAcesso = " + TipoCehUsuario.getTipoCehUsuariotoInt(filtroStatus) + " ";
        } else {
            filtro = " 1=1 ";
        }

        String ordem = " order by nuMatricula ";

        //Aqui buscamos se o valor digitado está no campo de código ou no campo de título,
        //assim a pessoa tem a liberade de pesquisar por código ou título usando o mesmo campo
        String textoDaPesquisa = this.vlPesquisa.getValue();
        if (textoDaPesquisa != null && !textoDaPesquisa.isEmpty()) {
            if (NumberUtils.isCreatable(textoDaPesquisa)) {
                //Se for número filtramos por código
                filtro += " AND nuMatricula LIKE '%" + textoDaPesquisa + "%'" + " ";
            } else {
                //Se for texto, fazermos a "normalização" do texto digitado e tembém do valor do banco, ou seja,
                //removemos os acentos e caracteres especiais tanto da string digitada quanto do valor do banco,
                //depois colocamos tudo em caixa alta, e aí sim comparamos. Com isso conseguimos encontrar o nome
                //independentemente se foi digitado com acentuação ou não.
                String textoTratado = Normalizer.normalize(textoDaPesquisa, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").trim().toUpperCase();
                filtro += " AND UPPER(npd.str100_sem_acento(nmUsuario)) LIKE '%" + textoTratado + "%'";
            }
        }

        List<CehUsuario> destino = cehUsuarioDAO.listar(filtro, ordem);

        if (destino != null) {
            SimpleListModel listModel = new SimpleListModel(destino);
            this.resultados.setModel(listModel);
        }

    }

    /**
     * Executado quando é clicado no botão "novo curso". Abre a tela de cadastro
     * passando como parâmetro a ação "novo". Essa ação fará que apenas o botão
     * de Salvar apareça.
     */
    public void novoCehUsuario() throws Exception {
        zkUtils.setParametro("ação", "novo");
        this.redirecionar();
    }

    /**
     * Executado com é clicado em "ver curso". Abre a tela de cadastro passando
     * como parâmetro a ação "ler" e o código do curso. Essa ação fará com que
     * os botões de Salvar e Excluir NÃO apareça.
     */
    public void onClickedVerCehUsuario(Event event) throws Exception {
        //Long cdCurso = Long.valueOf(event.getData().toString());
        Integer cdUsuario = Integer.valueOf(event.getTarget().getClientAttribute("id"));
        zkUtils.setParametro("cehUsuario", cdUsuario);
        zkUtils.setParametro("ação", "ler");
        zkUtils.setParametro("filtroStatus", this.vlCampoFiltroPerfil.getSelectedItem().getValue());
        this.redirecionar();
    }

    /**
     * Executado quando é clicado em "editar curso". Abre a tela de cadastro
     * passando como parâmetro a ação "editar" e o código do curso. Essa ação
     * fará com que os botões de Salvar e Excluir apareça.
     */
    public void onClickedAlterarCehUsuario(Event event) throws Exception {
        //Long cdCurso = Long.valueOf(event.getData().toString());
        Integer cdUsuario = Integer.valueOf(event.getTarget().getClientAttribute("id"));
        Integer cdUsuario1 = Sessao.getInstance().getUsuario().getCdUsuario();
        if (cdUsuario.equals(cdUsuario1)) {
            this.onClickedVerCehUsuario(event);
        }else{
            zkUtils.setParametro("cehUsuario", cdUsuario);
            zkUtils.setParametro("ação", "editar");
            zkUtils.setParametro("filtroStatus", this.vlCampoFiltroPerfil.getSelectedItem().getValue());
            this.redirecionar();
        }
    }

    /**
     * Redireciona para a página de cadastro do Curso. É passado como parâmetro
     * a url de retorno, ou seja, a url que aparecerá no botão "Voltar" e a url
     * que será redirecionado automaticamente após salvar ou excluir os dados.
     */
    private void redirecionar() {
        Include include = (Include) win.getParent(); //pega o "pai" da desta tela, o qual é um "include"
        String urlOrigem = include.getSrc(); //o source do include é a página atual
        include.setSrc(null);
        zkUtils.setParametro("paginaAtual", this.resultados.getActivePage());
        zkUtils.setParametro("url_retorno", urlOrigem); //a página atual será a url de retorno
        include.setSrc("paginas/cehusuario/cehUsuario.zul"); //vai para a página de cadastro de curso
    }

    public void onClickedDeletarCehUsuario(Event event) {
        int cdUsuario = Integer.parseInt(event.getTarget().getClientAttribute("id"));
        Integer cdUsuario1 = Sessao.getInstance().getUsuario().getCdUsuario();

        if (!cdUsuario1.equals(cdUsuario)) {
            if (zkUtils.MensagemConfirmacao("Deseja excluir o Usuário atual?")) {
                if (cdUsuario > 0) {
                    CehUsuario buscar = cehUsuarioDAO.buscar(cdUsuario);
                    buscar.setFlAtivo(false);
                    cehUsuarioDAO.atualizar(buscar);
                    Toast.show("usuário excluído com sucesso!", "Sucesso", Toast.Type.SUCCESS);
                    CehLogService.getInstance().insereLog("CEH_USUARIO", TipoOcorrenciaLog.DESATIVACAO);
                    this.filtrar(this.vlCampoFiltroPerfil.getSelectedItem().getValue());
                } else {
                    zkUtils.MensagemErro("Usuário inválido");
                }
            }
        }

    }
}
