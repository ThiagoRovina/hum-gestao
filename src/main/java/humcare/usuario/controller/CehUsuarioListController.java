package humcare.usuario.controller;

import humcare.application.model.Usuario;
import humcare.application.service.CehLogService;
import humcare.application.service.Sessao;
import humcare.application.tipoocorrencia.TipoOcorrenciaLog;
import humcare.usuario.dao.CehUsuarioDAO;
import humcare.usuario.funcao.FuncaoCdPerfil;
import humcare.usuario.model.CehUsuario;
import humcare.usuario.tipopermissao.TipoPermissaoUsuario;
import humcare.utilitarios.LdapUtil;
import org.apache.commons.lang3.math.NumberUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.*;
import humcare.utilitarios.Utils;
import humcare.utilitarios.ZkUtils;
import humcare.zk.custom.Toast;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class CehUsuarioListController extends Window {

    private Window win;

    private Listbox vlCampoFiltroPerfil;
    private Textbox vlPesquisa;
    private Grid resultados;
    private Button btnNovo;

    private final Utils utils = new Utils();
    private final ZkUtils zkUtils = new ZkUtils();

    private final CehUsuarioDAO cehUsuarioDAO = new CehUsuarioDAO();

    public void onCreate() {
        this.win = (Window) getFellow("winUsuarioList");

        //pegamos os campos definidos no .zul e vinculamos à uma variável
        //para pegarmos ou pularmos com valores
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
        if(usuarioLogado.getTpPermissaoAcesso() != FuncaoCdPerfil.ADMINISTRADOR){
            this.btnNovo.setVisible(false);
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
            filtro = " 1=1 AND dePerfil = " + TipoPermissaoUsuario.getTipoCehUsuariotoInt(filtroStatus) + " ";
            System.out.println(filtro);
        } else {
            filtro = " 1=1 ";
        }
        String ordem = " order by cdPessoa ";

        //Aqui buscamos se o valor digitado está no campo de código ou no campo de título,
        //assim a pessoa tem a liberade de pesquisar por código ou título usando o mesmo campo
        String textoDaPesquisa = this.vlPesquisa.getValue();
        if (textoDaPesquisa != null && !textoDaPesquisa.isEmpty()) {
            if (NumberUtils.isCreatable(textoDaPesquisa)) {
                //Se for número filtramos por código
                filtro += " AND cdPessoa LIKE '%" + textoDaPesquisa + "%'" + " ";
            } else {
                //Se for texto, fazermos a "normalização" do texto digitado e tembém do valor do banco, ou seja,
                //removemos os acentos e caracteres especiais tanto da string digitada quanto do valor do banco,
                //depois colocamos tudo em caixa alta, e aí sim comparamos. Com isso conseguimos encontrar o nome
                //independentemente se foi digitado com acentuação ou não.
                String textoTratado = Normalizer.normalize(textoDaPesquisa, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").trim().toUpperCase();
                filtro += " AND UPPER(npd.str100_sem_acento(nmPessoa)) LIKE '%" + textoTratado + "%'";
            }
        }

        List<CehUsuario> destino = cehUsuarioDAO.listar(filtro, ordem);


        SimpleListModel<CehUsuario> listModel = new SimpleListModel<>(destino);
        this.resultados.setModel(listModel);
    }

    /**
     * Executado quando é clicado no botão "novo curso". Abre a tela de cadastro
     * passando como parâmetro a ação "novo". Essa ação fará que apenas o botão
     * de Salvar apareça.
     */

    public void novoCehUsuario() throws Exception {
        Usuario usuarioLogado = Sessao.getInstance().getUsuario();
        if(usuarioLogado.getTpPermissaoAcesso() != FuncaoCdPerfil.ADMINISTRADOR){
            zkUtils.MensagemErro("Acesso negado!");
            return;
        }
        zkUtils.setParametro("ação", "novo");
        this.redirecionar();
    }

    public boolean verificaEmailClicado(Integer cdPessoa){
        CehUsuario cehUsuario = new CehUsuario();
        cehUsuario = cehUsuarioDAO.buscar(cdPessoa);
        return cehUsuario.getDeEmail().equals(verificaEmail());
    }

    /**
     * Executado com é clicado em "ver curso". Abre a tela de cadastro passando
     * como parâmetro a ação "ler" e o código do curso. Essa ação fará com que
     * os botões de Salvar e Excluir NÃO apareça.
     */
    public void onClickedVerCehUsuario(Event event) throws Exception {
        Integer cdPessoa = Integer.parseInt(event.getTarget().getClientAttribute("id"));

        if (cdPessoa.equals(Sessao.getInstance().getUsuario().getCdUsuario())) {
            this.onClickedVerCehUsuario(event);
        } else {
            zkUtils.setParametro("cehUsuario", cdPessoa);
            zkUtils.setParametro("ação", "ler");
            zkUtils.setParametro("filtroStatus", this.vlCampoFiltroPerfil.getSelectedItem().getValue());
            this.redirecionar();
        }

    }

    private String verificaEmail(){
        Usuario usuarioLogado = Sessao.getInstance().getUsuario();
        if(usuarioLogado != null){
            return usuarioLogado.getEmail();
        }
        return null;
    }

    /**
     * Executado quando é clicado em "editar curso". Abre a tela de cadastro
     * passando como parâmetro a ação "editar" e o código do curso. Essa ação
     * fará com que os botões de Salvar e Excluir apareça.
     */
    public void onClickedAlterarCehUsuario(Event event) throws Exception {
        try {
            Usuario usuarioLogado = Sessao.getInstance().getUsuario();
            Integer cdPessoa = Integer.parseInt(event.getTarget().getClientAttribute("id"));
            if(usuarioLogado.getTpPermissaoAcesso() != FuncaoCdPerfil.ADMINISTRADOR && !verificaEmailClicado(cdPessoa)){
                zkUtils.MensagemErro("Acesso negado!");
                return;
            }

            if (cdPessoa.equals(Sessao.getInstance().getUsuario().getCdUsuario())) {
                this.onClickedVerCehUsuario(event);
            } else {
                zkUtils.setParametro("cehUsuario", cdPessoa);
                zkUtils.setParametro("ação", "editar");
                zkUtils.setParametro("filtroStatus", this.vlCampoFiltroPerfil.getSelectedItem().getValue());
                this.redirecionar();
            }
        }catch (Exception e){
            Integer cdPessoa = Integer.parseInt(event.getTarget().getClientAttribute("id"));
            System.out.println("erro! cdPessoa= " + cdPessoa);

        }

    }

    public void voltar(){
        Include include = (Include) win.getParent();
        include.setSrc("/paginas/home/HomeLogada.zul");
    }


    public void onClickedDeletarCehUsuario(Event event) {
        Usuario usuarioLogado = Sessao.getInstance().getUsuario();
        if(usuarioLogado.getTpPermissaoAcesso() != FuncaoCdPerfil.ADMINISTRADOR){
            zkUtils.MensagemErro("Acesso negado!");
            return;
        }
        int cdPessoa = Integer.parseInt(event.getTarget().getClientAttribute("id"));
        if (cdPessoa != Sessao.getInstance().getUsuario().getCdUsuario()) {
            if (zkUtils.MensagemConfirmacao("Deseja excluir o Usuário atual?")) {
                if (cdPessoa > 0) {
                    CehUsuario buscar = cehUsuarioDAO.buscar(cdPessoa);
                    buscar.setFlAtivo(false);
                    cehUsuarioDAO.atualizar(buscar);
                    Toast.show("usuário excluído com sucesso!", "Sucesso", Toast.Type.SUCCESS);
                    CehLogService.getInstance().insereLog("BDC_PESSOA", TipoOcorrenciaLog.DESATIVACAO);
                    this.filtrar(this.vlCampoFiltroPerfil.getSelectedItem().getValue());
                } else {
                    zkUtils.MensagemErro("Usuário inválido ou não encontrado!");
                }
            }
        }

    }
    /**
     * Redireciona para a página de cadastro do Curso. É passado como parâmetro
     * a url de retorno, ou seja, a url que aparecerá no botão "Voltar" e a url
     * que será redirecionado automaticamente após salvar ou excluir os dados.
     */
    private void redirecionar() {
        Include include = (Include) win.getParent();
        String urlOrigem = include.getSrc();
        include.setSrc(null);
        zkUtils.setParametro("paginaAtual", this.resultados.getActivePage());
        zkUtils.setParametro("url_retorno", urlOrigem);
        include.setSrc("paginas/cehusuario/cehUsuario.zul");
    }
}
