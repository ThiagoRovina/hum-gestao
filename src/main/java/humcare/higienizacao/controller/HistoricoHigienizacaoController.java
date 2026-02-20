package humcare.higienizacao.controller;

import humcare.higienizacao.dao.HigienizacaoDAO;
import humcare.higienizacao.model.Higienizacao;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Window;

import java.util.List;

public class HistoricoHigienizacaoController extends SelectorComposer<Window> {

    @Wire
    private Listbox historicoListbox;

    private final HigienizacaoDAO higienizacaoDAO = new HigienizacaoDAO();

    @Override
    public void doAfterCompose(Window comp) throws Exception {
        super.doAfterCompose(comp);
        List<Higienizacao> historico = higienizacaoDAO.listarHistorico();
        historicoListbox.setModel(new ListModelList<>(historico));
    }
}
