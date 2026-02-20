package humcare.conteudo.controller;

import humcare.application.service.Sessao; // Adicione esta importação
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Include;
import org.zkoss.zul.Window;
import humcare.utilitarios.ZkUtils;

import java.util.Map;

public class Conteudo extends Window {
    private Include conteudo;
    private Window winIndex;

    ZkUtils zkUtils = new ZkUtils();

    public void onCreate() {
        if (!Sessao.getInstance().validarSessao()) {
            return;
        }

        this.conteudo = (Include) getFellow("conteudo");
        this.winIndex = (Window) getFellow("winIndex");
        Execution current = Executions.getCurrent();

        String pagina = current.getParameter("pagina");

        if (pagina != null && !pagina.trim().isEmpty()) {

            Map<String, String[]> parameterMap = Executions.getCurrent().getParameterMap();
            parameterMap.forEach((s, strings) -> {
                if (!s.equals("pagina")) {
                    zkUtils.setParametro(s, current.getParameter(s));
                }
            });
            this.conteudo.setSrc(pagina);
        } else {
            this.conteudo.setSrc("paginas/home/HomeLogada.zul");
        }
    }
}