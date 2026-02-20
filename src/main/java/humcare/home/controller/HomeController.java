package humcare.home.controller;

import humcare.application.model.Usuario;
import humcare.application.service.Sessao;
import humcare.usuario.funcao.FuncaoCdPerfil;
import humcare.utilitarios.ZkUtils;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Window;

import java.io.Serializable;

public class HomeController extends Window implements Serializable {

    private Usuario usuarioLogado;
    private ZkUtils zkUtils = new ZkUtils();



    public void onCreate() {
        // Pega o usuário logado da sessão
        this.usuarioLogado = Sessao.getInstance().getUsuario();

        // Garante que se não houver usuário, a sessão seja destruída
        if (this.usuarioLogado == null) {
            Sessao.getInstance().destruirSessao();
        }
    }


    public Usuario getUsuarioLogado() {
        return this.usuarioLogado;
    }


    public void irParaEquipe() {
        Executions.sendRedirect("/index.zul?pagina=paginas/cehusuario/cehUsuarioList.zul");
    }

    public void irParaCentralNIR() {
        Executions.sendRedirect("/index.zul?pagina=paginas/centralNIR/centralNIRList.zul");
    }
    public void irParaUnidade() {
        Executions.sendRedirect("/index.zul?pagina=paginas/unidade/unidadeList.zul");
    }

    public void irParaLeitos() {
        Executions.sendRedirect("/index.zul?pagina=paginas/leitos/LeitosList.zul");
    }

    public void irParaRelatorios() {
        Executions.sendRedirect("/index.zul?pagina=paginas/relatorios/relatoriosLIst.zul");
    }

    public void irParaHigienizacao() {
        Executions.sendRedirect("/index.zul?pagina=paginas/higienizacao/higienizacaoList.zul");
    }


    public void logout() {
        Sessao.getInstance().destruirSessao();
    }
}