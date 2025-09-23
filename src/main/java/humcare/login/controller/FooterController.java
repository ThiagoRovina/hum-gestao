package humcare.login.controller;

import humcare.application.service.Application;
import org.zkoss.zul.Div;
import org.zkoss.zul.Window;

public class FooterController extends Window {
    private Div divDesenv;
    private Div divProd;

    public void onCreate() {
        this.divDesenv = (Div) getFellow("div_desenv");
        this.divProd = (Div) getFellow("div_prod");
        this.mensagemDesenvol();
    }

    /**
     * Caso o ambiente de desenvolvimento seja de testes (não esteja conectado
     * em produção), então mostra um aviso.
     */
    private void mensagemDesenvol() {
        if (Application.getInstance().getEnviroment().equals(Application.DEVELOP)) {
            this.divDesenv.setVisible(true);
        }else{
            this.divProd.setVisible(true);
        }
    }
}
