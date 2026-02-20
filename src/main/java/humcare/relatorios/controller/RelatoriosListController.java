package humcare.relatorios.controller;

import humcare.unidade.dao.UnidadeDAO;
import humcare.unidade.model.Unidade;
import humcare.utilitarios.JasperReportsUtil;
import humcare.utilitarios.ZkUtils;
import humcare.zk.custom.Toast;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.HashMap;
import java.util.List;

public class RelatoriosListController extends Window {
    private Window win;
    private ZkUtils zkUtils = new ZkUtils();
    private JasperReportsUtil jasperUtil = new JasperReportsUtil();
    private Listbox unidadeHigienizacao;
    private Datebox dtInicioHigienizacao;
    private Datebox dtFimHigienizacao;

    public void onCreate() {
        this.win = (Window) getFellow("winRelatoriosList");
        this.unidadeHigienizacao = (Listbox) getFellow("unidadeHigienizacao");
        this.dtInicioHigienizacao = (Datebox) getFellow("dtInicioHigienizacao");
        this.dtFimHigienizacao = (Datebox) getFellow("dtFimHigienizacao");

        this.popularFiltros();
    }

    private void popularFiltros() {
        // Carrega Unidades Ativas para o filtro
        UnidadeDAO unidadeDAO = new UnidadeDAO();
        List<Unidade> unidadesAtivas = unidadeDAO.listar("t.flAtivo = 1", "order by t.deUnidade");
        for (Unidade unidade : unidadesAtivas) {
            this.unidadeHigienizacao.appendChild(new Listitem(unidade.getDeUnidade(), unidade.getCdUnidade()));
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

    private void gerarRelatorio(String nomeArquivo, String nomeSaida) {
        if (nomeArquivo.equals("higienizacao") && !this.validaCamposHigienizacao()) {
            return;
        }

        String path = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/relatorios/");
        
        if (path == null) {
             Messagebox.show("Não foi possível localizar o diretório de relatórios.", "Erro", Messagebox.OK, Messagebox.ERROR);
             return;
        }

        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String jrxmlPath = path + File.separator + nomeArquivo + ".jrxml";
        String jasperPath = path + File.separator + nomeArquivo + ".jasper";

        try {
            File jrxmlFile = new File(jrxmlPath);
            if (!jrxmlFile.exists()) {
                Messagebox.show("Arquivo de modelo não encontrado: " + jrxmlPath, "Erro", Messagebox.OK, Messagebox.ERROR);
                return;
            }

            // Compile if jasper doesn't exist or is older than jrxml
            File jasperFile = new File(jasperPath);
            if (!jasperFile.exists() || jasperFile.lastModified() < jrxmlFile.lastModified()) {
                try {
                    JasperCompileManager.compileReportToFile(jrxmlPath, jasperPath);
                } catch (Exception e) {
                    e.printStackTrace();
                    Messagebox.show("Erro na compilação do relatório: " + e.getMessage(), "Erro", Messagebox.OK, Messagebox.ERROR);
                    return;
                }
            }

            HashMap<String, Object> params = new HashMap<>();
            // Pass jasperPath to util. It will use it to fill.
            if (nomeArquivo.equals("higienizacao")) {
                params.put("unidadeHigienizacao", this.unidadeHigienizacao.getSelectedItem().getValue());
                params.put("dtInicioHigienizacao", this.dtInicioHigienizacao.getValue());
                params.put("dtFimHigienizacao", this.dtFimHigienizacao.getValue());
            }

            JasperPrint print = jasperUtil.populaArquivoJasper(jasperPath, params);

            if (print != null && print.getPages() != null && !print.getPages().isEmpty()) {
                byte[] pdf = jasperUtil.geraArquivoPDFByteArray(print);
                Filedownload.save(new ByteArrayInputStream(pdf), "application/pdf", nomeSaida + ".pdf");
            } else {
                Messagebox.show("O relatório foi gerado mas está vazio ou ocorreu um erro na consulta.", "Aviso", Messagebox.OK, Messagebox.EXCLAMATION);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Messagebox.show("Erro ao gerar relatório: " + e.getMessage(), "Erro", Messagebox.OK, Messagebox.ERROR);
        }
    }

    private Boolean validaCamposHigienizacao() {
        if (this.unidadeHigienizacao.getSelectedItem().getValue().toString().equals("0")) {
            Toast.show("Selecione uma unidade para gerar o relatório de higienização.", "Ops!", Toast.Type.ERROR);
            return false;
        }
        if (this.dtInicioHigienizacao.getValue() == null) {
            Toast.show("Selecione a data de início para gerar o relatório de higienização.", "Ops!", Toast.Type.ERROR);
            return false;
        }
        if (this.dtFimHigienizacao.getValue() == null) {
            Toast.show("Selecione a data de fim para gerar o relatório de higienização.", "Ops!", Toast.Type.ERROR);
            return false;
        }
        return true;
    }

    public void gerarRelatorioOcupacao() {
        gerarRelatorio("ocupacao", "RelatorioOcupacao");
    }

    public void gerarRelatorioHigienizacao() {
        gerarRelatorio("higienizacao", "RelatorioHigienizacao");
    }
}
