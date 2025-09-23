package humcare.utilitarios;

import humcare.dao.DAO;
import net.sf.jasperreports.engine.*;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.Session;
import org.hibernate.jdbc.ReturningWork;

import javax.persistence.EntityManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;

/**
 *
 * @author alison
 */
public class JasperReportsUtil {

    /**
     * Gera o arquivo PDF e retorna um byte array. Este método é a forma
     * recomendada, pois você trabalha apenas com o memória, não correndo o
     * risco de gastar espaço em disco desnecessariamente.
     *
     * @param jasperPrint
     * @return
     */
    public byte[] geraArquivoPDFByteArray(JasperPrint jasperPrint) {
        byte[] byteArray = null;
        try {
            byteArray = JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (JRException ex) {
            System.out.println(ExceptionUtils.getStackTrace(ex));
        }
        return byteArray;
    }

    /**
     * Gera o arquivo PDF e salva em disco. Útil caso seja necessário salvar o
     * arquivo no Servidor de Arquivos.
     *
     * @param jasperPrint
     * @param destinationFileName
     */
    public void geraArquivoPDFDisco(JasperPrint jasperPrint, String destinationFileName) {
        try {
            JasperExportManager.exportReportToPdfFile(jasperPrint, destinationFileName);
        } catch (JRException ex) {
            System.out.println(ExceptionUtils.getStackTrace(ex));
        }
    }

    /**
     * Exemplo de popular o Jasper com os dados do banco e parâmetros. Funciona
     * no Hibernate 4 e 5. Funciona tanto com arquivo .jasper quanto .jrxml.
     *
     * @param arquivoJasper
     * @param parametros
     * @return JasperPrint
     */
    public JasperPrint populaArquivoJasper(String arquivoJasper, HashMap parametros) {

        if (arquivoJasper.endsWith(".jrxml") && !new File(arquivoJasper.replace(".jrxml", ".jasper")).exists()) {
            try {
                JasperCompileManager.compileReportToFile(arquivoJasper, arquivoJasper.replace(".jrxml", ".jasper"));
            } catch (JRException ex) {
                System.out.println(ExceptionUtils.getStackTrace(ex));
            }
        }

        Session sessao = DAO.openSession();


        ReturningWork<JasperPrint> fillReportJasper = (Connection cnctn) -> {
            JasperPrint print = new JasperPrint();
            try {
                InputStream stream = new FileInputStream(arquivoJasper.replace(".jrxml", ".jasper"));
                print = JasperFillManager.fillReport(stream, parametros, cnctn);
            } catch (FileNotFoundException | JRException ex) {
                System.out.println(ExceptionUtils.getStackTrace(ex));
            }
            return print;
        };
        JasperPrint impressao = sessao.doReturningWork(fillReportJasper);

        return impressao;
    }
}
