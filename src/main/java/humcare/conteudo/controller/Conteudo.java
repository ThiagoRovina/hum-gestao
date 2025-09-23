package humcare.conteudo.controller;

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

    public void onCreate(){
        this.conteudo = (Include) getFellow("conteudo");
        this.winIndex = (Window) getFellow("winIndex");
        Execution current = Executions.getCurrent();

        String pagina = current.getParameter("pagina");

        if (pagina != null) {
            Map<String, String[]> parameterMap = Executions.getCurrent().getParameterMap();
            parameterMap.forEach((s, strings) -> {
                if (!s.equals("pagina")) {
                    zkUtils.setParametro(s, current.getParameter(s));
                }
            });

            this.conteudo.setSrc(pagina);
        }
    }
}