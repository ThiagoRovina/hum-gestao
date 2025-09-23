package humcare.utilitarios;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.imageio.ImageIO;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.zkoss.util.media.AMedia;
import org.zkoss.util.media.Media;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Timebox;
import org.zkoss.zul.Window;
import org.zkoss.zul.impl.InputElement;
import humcare.zk.custom.CustomMessagebox;

/**
 *
 * @authors walter, alison
 */
public class ZkUtils {

    public static final String SISTEMA = "Sistema HUM";

    /* *****************************
    
       ALERTS
    
       ***************************** */
    public void Mensagem(String mensagem) {
        if (!mensagem.isEmpty()) {
            CustomMessagebox.show(mensagem, SISTEMA, CustomMessagebox.OK, Messagebox.INFORMATION, false);
        }
    }

    public boolean MensagemConfirmacao(String mensagem) {
        if (!mensagem.isEmpty()) {
            return (CustomMessagebox.show(mensagem, SISTEMA, CustomMessagebox.YES | CustomMessagebox.NO, Messagebox.QUESTION, false) == CustomMessagebox.YES);
        } else {
            return false;
        }
    }

    public void MensagemAtencao(String mensagem) {
        if (!mensagem.isEmpty()) {
            CustomMessagebox.show(mensagem, SISTEMA, CustomMessagebox.OK, Messagebox.EXCLAMATION, false);
        }
    }

    public void MensagemErro(String mensagem) {
        if (!mensagem.isEmpty()) {
            CustomMessagebox.show(mensagem, SISTEMA, CustomMessagebox.OK, Messagebox.ERROR, false);
        }
    }


    public void Excecao(Exception e) {
        this.MensagemErro(MensagemExcecao(e));
    }

    public String MensagemExcecao(Exception e) {
        String mensagemErro = "Erro de Execução: ";
        if (e != null) {
            if (e.getCause() != null) {
                mensagemErro += e.getCause().toString();
            } else {
                if (e.getMessage() != null) {
                    mensagemErro += e.getMessage();
                } else {
                    mensagemErro += e.toString();
                }
            }

        } else {
            mensagemErro += "Erro desconhecido.";
        }
        return mensagemErro;
    }

    /* *****************************
    
       MANIPULAÇÃO DE INPUTS
    
       ***************************** */
    public void limparCampos(Window win) {
        String classe;
        Object obj;
        for (Iterator it = win.getFellows().iterator(); it.hasNext();) {
            obj = it.next();
            classe = obj.getClass().getName();
            if (classe.contains("Textbox") || classe.contains("Longbox") || classe.contains("Decimalbox")
                    || classe.contains("Datebox") || classe.contains("Timebox") || classe.contains("Combobox")
                    || classe.contains("Intbox")) {
                InputElement campo = (InputElement) obj;
                campo.setRawValue(null);
            }
        }
    }

    public void setReadOnly(Window win, boolean valor) {
        String classe;
        Object obj;
        for (Iterator it = win.getFellows().iterator(); it.hasNext();) {
            obj = it.next();
            classe = obj.getClass().getName();
            if (classe.contains("Textbox")
                    || classe.contains("Longbox")
                    || classe.contains("Decimalbox")
                    || classe.contains("Datebox")
                    || classe.contains("Timebox")
                    || classe.contains("Combobox")
                    || classe.contains("Intbox")) {
                InputElement campo = (InputElement) obj;
                campo.setReadonly(valor);
                if (valor) {
                    campo.setConstraint("");
                }
            }
            if (classe.contains("Datebox")) {
                ((Datebox) obj).setButtonVisible(!valor);
            }
            if (classe.contains("Listbox")) {
                ((Listbox) obj).setDisabled(valor);
            }
        }
    }

    public void setDisabled(Window win, boolean valor) {
        String classe;
        Object obj;
        for (Iterator it = win.getFellows().iterator(); it.hasNext();) {
            obj = it.next();
            classe = obj.getClass().getName();
            if (classe.contains("Textbox")
                    || classe.contains("Longbox")
                    || classe.contains("Decimalbox")
                    || classe.contains("Datebox")
                    || classe.contains("Timebox")
                    || classe.contains("Combobox")
                    || classe.contains("Intbox")) {
                InputElement campo = (InputElement) obj;
                campo.setDisabled(valor);
                if (valor) {
                    campo.setConstraint("");
                }
            }
            if (classe.contains("Listbox")) {
                ((Listbox) obj).setDisabled(valor);
            }
            if (classe.contains("Datebox")) {
                ((Datebox) obj).setButtonVisible(!valor);
            }
        }
    }

    public void popularCampo(Component c, Object vlr) {
        try {
            String valor = (String) vlr.toString();
            if (valor != null && !valor.equals("")) {
                String classe = c.getWidgetClass();
                if (classe.contains("Textbox")) {
                    Textbox tb = (Textbox) c;
                    tb.setValue(valor);
                }
                if (classe.contains("Longbox")) {
                    Longbox tb = (Longbox) c;
                    tb.setValue(Long.parseLong(valor));
                }
                if (classe.contains("Intbox")) {
                    Intbox tb = (Intbox) c;
                    tb.setValue(Integer.parseInt(valor));
                }
                if (classe.contains("Decimalbox")) {
                    Decimalbox tb = (Decimalbox) c;
                    tb.setValue(BigDecimal.valueOf(Double.parseDouble(valor)));
                }
                if (classe.contains("Datebox")) {
                    Datebox tb = (Datebox) c;
                    tb.setValue(DataUtil.converteStringParaSql(valor));
                }
                if (classe.contains("Timebox")) {
                    Timebox tb = (Timebox) c;
                    tb.setText(valor);
                }
                if (classe.contains("Label")) {
                    Label tb = (Label) c;
                    tb.setValue(valor);
                }
            }
        } catch (Exception e) {
        }
    }

    public boolean validarCampo(Component c) {
        boolean r = true;
        String classe = c.getWidgetClass();
        if (classe.contains("Textbox")) {
            Textbox tb = (Textbox) c;
            if (tb.getValue() == null || tb.getValue().equals("")) {
                this.MensagemAtencao("Campo obrigatório!");
                tb.focus();
                return false;
            }
        }
        if (classe.contains("Intbox")) {
            Intbox tb = (Intbox) c;
            if (tb.getValue() == null) {
                this.MensagemAtencao("Campo obrigatório!");
                tb.focus();
                return false;
            }
        }
        if (classe.contains("Longbox")) {
            Longbox tb = (Longbox) c;
            if (tb.getValue() == null) {
                this.MensagemAtencao("Campo obrigatório!");
                tb.focus();
                return false;
            }
        }
        if (classe.contains("Decimalbox")) {
            Decimalbox tb = (Decimalbox) c;
            if (tb.getValue() == null) {
                this.MensagemAtencao("Campo obrigatório!");
                tb.focus();
                return false;
            }
        }
        if (classe.contains("Datebox")) {
            Datebox tb = (Datebox) c;
            if (tb.getText() == null || tb.getText().equals("")) {
                this.MensagemAtencao("Campo obrigatório!");
                tb.focus();
                return false;
            }
        }
        if (classe.contains("Timebox")) {
            Timebox tb = (Timebox) c;
            if (tb.getText() == null || tb.getText().equals("")) {
                this.MensagemAtencao("Campo obrigatório!");
                tb.focus();
                return false;
            }
        }
        return r;
    }

    /* *****************************
    
       MANIPULAÇÃO DE LISTBOX
    
       ***************************** */
    public void selecionarLista(Listbox lb, long valor) {
        for (int i = 0; i < lb.getItemCount(); i++) {
            Listitem li = lb.getItemAtIndex(i);
            try {
                if (li.getValue().toString().equals(Long.toString(valor))) {
                    lb.selectItem(li);
                    return;
                }
            } catch (Exception ex) {
            }
        }
    }

    //.substring(0, valor.length())

    public void selecionarLista(Listbox lb, String valor) {
        if (valor != null) {
            valor = valor.trim();
        }
        for (int i = 0; i < lb.getItemCount(); i++) {
            Listitem li = lb.getItemAtIndex(i);
            try {
                if (valor != null && li.getValue().toString().equals(valor)) {
                    lb.selectItem(li);
                    return;
                }
            } catch (Exception ex) {
            }
        }
    }

    public void selecionarListaLabel(Listbox lb, String valor) {
        for (int i = 0; i < lb.getItemCount(); i++) {

            Listitem li = lb.getItemAtIndex(i);

            if (li.getLabel().equals(valor)) {
                lb.selectItem(li);
                return;
            }

        }
    }

    /**
     * Seleciona objeto do listbox, quando o listbox foi setado um model com
     * .setModel(new ListModelList(list)).
     *
     * @param lb Listbox com model
     * @param objeto Objeto a ser selecionado. Deve ter implementado o @Override
     * do equals() e do hashCode().
     */
    public void selecionarListaModel(Listbox lb, Object objeto) {
        if (objeto != null) {
            ListModelList l = (ListModelList) lb.getModel();
            l.addToSelection(objeto);
        }
    }

    public void selecionarListaLabelSiglaUF(Listbox lb, String valor) {

        for (int i = 0; i < lb.getItemCount(); i++) {
            Listitem li = lb.getItemAtIndex(i);
            Listcell cell = (Listcell) li.getChildren().get(0);
            String scell = cell.getLabel().substring(0, 2);
            try {
                if (scell.equals(valor)) {
                    lb.selectItem(li);
                    return;
                }

            } catch (Exception ex) {
            }
        }

    }

    public void limparListbox(Listbox lb) {
        lb.getItems().clear();

    }

    public void reiniciarListbox(Listbox lb) {
        lb.getItems().clear();
        for (Iterator it = lb.getChildren().iterator(); it.hasNext();) {
            it.next();
            it.remove();
        }

    }

    public void carregarListbox(Listbox listBox, List listaDeArrayDeObjetosValorDescricao) {

        for (Iterator i = listaDeArrayDeObjetosValorDescricao.iterator(); i.hasNext();) {
            Object[] obj = (Object[]) i.next();

            Listitem item = new Listitem();
            item.setValue(obj[0].toString());
            item.setParent(listBox);

            Listcell cellDescricao = new Listcell();
            cellDescricao.setLabel(obj[1].toString());
            cellDescricao.setParent(item);

        }
    }

    public String setaOrdemPagina(Listbox listbox, Listheader origem) {
        String ordem = "";
        Listheader lh = new Listheader();
        for (Iterator it = listbox.getListhead().getChildren().iterator(); it.hasNext();) {
            lh = (Listheader) it.next();
            if (!origem.getId().equals(lh.getId())) {
                lh.setSortDirection("natural");
            }
        }

        if (origem.getSortDirection().equals("natural")) {
            origem.setSortDirection("ascending");
            ordem = " asc";
        } else if (origem.getSortDirection().equals("ascending")) {
            origem.setSortDirection("descending");
            ordem = " desc";
        } else if (origem.getSortDirection().equals("descending")) {
            ordem = " asc";
            origem.setSortDirection("ascending");
        }

        ordem = " order by " + origem.getId().replace("ix_", "") + ordem;
        return ordem;
    }

    /* *****************************
    
       MANIPULAÇÃO DE ARQUIVOS
    
       ***************************** */
    public void criaDiretorio(String novoDiretorio, String caminho) {
        String nomeDiretorio = null;
        String separador = java.io.File.separator;
        try {
            nomeDiretorio = caminho + novoDiretorio;
            if (!new File(nomeDiretorio).exists()) { // Verifica se o diretório existe.   
                (new File(nomeDiretorio)).mkdir();   // Cria o diretório   
            }
        } catch (Exception ex) {
        }
    }

    public void gravarArquivo(org.zkoss.util.media.Media media, String nomeDoArquivo) throws Exception, FileNotFoundException, IOException {

        new File(new File(nomeDoArquivo).getAbsoluteFile().getParent()).mkdirs(); //cria diretório caso não exista

        File file = new File(nomeDoArquivo); //Criamos um nome para o arquivo  

        InputStream inputStream = media.getStreamData();
        OutputStream out = new FileOutputStream(file);
        byte buf[] = new byte[1024];
        int len;
        while ((len = inputStream.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        out.close();
        inputStream.close();
    }

    public boolean apagarArquivo(String caminhoDoArquivo) {
        File file = new File(caminhoDoArquivo);
        return file.delete();
    }

    public void copiarArquivo(String origem, String destino) throws Exception, FileNotFoundException, IOException {

        File file = new File(destino); //Criamos um nome para o arquivo  

        if (!file.exists()) {
            InputStream inputStream = new FileInputStream(origem);
            OutputStream out = new FileOutputStream(file);
            byte buf[] = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            inputStream.close();
        }
    }

    public boolean anexarArquivo(Media arquivo, String nomeArquivo, String[] tiposExtensaoArquivo, Integer tamanhoMaximoMegaBytes, Component elemento) {

        //valida tamanho do arquivo
        Long tamanho = 999L * 1024L * 1024L;
        try {
            tamanho = Long.valueOf(arquivo.getStreamData().available());
        } catch (IOException ex) {

        }
        if (tamanho > Long.valueOf(tamanhoMaximoMegaBytes) * 1024L * 1024L) {
            Clients.showNotification("O arquivo deve ser menor que " + tamanhoMaximoMegaBytes + " MB.", Clients.NOTIFICATION_TYPE_ERROR, elemento, "end_center", 0);
            return false;
        }

        //valida tipo do arquivo
        String formatoArquivo = arquivo.getFormat();
        if (!Arrays.asList(tiposExtensaoArquivo).contains(formatoArquivo)) {
            Clients.showNotification("Somente arquivos " + Arrays.toString(tiposExtensaoArquivo).replace("[", "").replace("]", "") + " são permitidos.", Clients.NOTIFICATION_TYPE_ERROR, elemento, "end_center", 0);
            return false;
        }

        //salva arquivo
        try {
            this.gravarArquivo(arquivo, nomeArquivo);
        } catch (Exception ex) {
            Clients.showNotification("Ocorreu um erro ao anexar arquivo! Por favor, tente novamente. Detalhes: " + ex.getMessage(), Clients.NOTIFICATION_TYPE_ERROR, elemento, "end_center", 0);
            return false;
        }

        return true;
    }

    public void downloadArquivo(String caminho, String nomeArquivo, String tipo) throws FileNotFoundException, IOException {

        InputStream input = null;
        ByteArrayOutputStream baos = null;

        try {
            File file = new File(caminho);
            input = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            baos = new ByteArrayOutputStream();
            int bytesRead;
            while ((bytesRead = input.read(buffer)) > 0) {
                baos.write(buffer, 0, bytesRead);
            }
            byte[] mybytes = baos.toByteArray();

            Filedownload.save(mybytes, tipo, nomeArquivo);
        } finally {
            if (input != null) {
                input.close();
            }
            if (baos != null) {
                baos.close();
            }
        }
    }

    public AMedia recuperarArquivo(String caminho, String nomeArquivo, String tipo) throws Exception {

        InputStream input = null;
        ByteArrayOutputStream baos = null;
        ByteArrayInputStream is = null;
        AMedia amedia = null;

        try {
            File file = new File(caminho);
            input = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            baos = new ByteArrayOutputStream();
            int bytesRead;
            while ((bytesRead = input.read(buffer)) > 0) {
                baos.write(buffer, 0, bytesRead);
            }
            is = new ByteArrayInputStream(buffer);
            amedia = new AMedia(nomeArquivo, tipo, null, is);
        } finally {
            if (input != null) {
                input.close();
            }
            if (baos != null) {
                baos.close();
            }
            if (is != null) {
                is.close();
            }
        }
        return amedia;
    }

    public AMedia recuperarArquivoImagemCortadaQuadrada(String caminho, String nomeArquivo, String tipo) throws Exception {

        InputStream input = null;
        ByteArrayOutputStream baos = null;
        ByteArrayInputStream is = null;
        AMedia amedia = null;

        try {
            File file = new File(caminho);
            if (file.exists() && !file.isDirectory()) {
                // Get a BufferedImage object from a byte array
                BufferedImage originalImage = ImageIO.read(file);
                BufferedImage croppedImage = null;
                // Get image dimensions
                int height = originalImage.getHeight();
                int width = originalImage.getWidth();
                // The image is not already a square
                if (height != width) {
                    // Compute the size of the square
                    int squareSize = (height > width ? width : height);
                    // Coordinates of the image's middle
                    int xc = width / 2;
                    int yc = height / 2;
                    // Crop
                    croppedImage = originalImage.getSubimage(
                            xc - (squareSize / 2), // x coordinate of the upper-left corner
                            yc - (squareSize / 2), // y coordinate of the upper-left corner
                            squareSize, // widht
                            squareSize // height
                    );
                } else {
                    croppedImage = originalImage;
                }

                //resizing
                ///Image resultingImage = croppedImage.getScaledInstance(44, 44, Image.SCALE_DEFAULT);
                //BufferedImage outputImage = new BufferedImage(44, 44, BufferedImage.TYPE_INT_RGB);
                //outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
                baos = new ByteArrayOutputStream();
                ImageIO.write(croppedImage, tipo, baos);
                byte[] bytes = baos.toByteArray();
                is = new ByteArrayInputStream(bytes);
                amedia = new AMedia(nomeArquivo, tipo, null, is);
            }
        } finally {
            if (input != null) {
                input.close();
            }
            if (baos != null) {
                baos.close();
            }
            if (is != null) {
                is.close();
            }
        }
        return amedia;
    }

    public boolean fileExists(String caminhoCompleto) {
        File file = new File(caminhoCompleto);
        return file.exists() && !file.isDirectory();
    }

    public void downloadFilesAsZip(String diretorio, String nomeArquivo) {

        String zipFile = diretorio + "/" + nomeArquivo;
        File folder = new File(diretorio);
        File[] listOfFiles = folder.listFiles();

        try {
            FileOutputStream fos = new FileOutputStream(zipFile);
            ZipOutputStream zos = new ZipOutputStream(fos);

            for (int i = 0; i < listOfFiles.length; i++) {
                File srcFile = listOfFiles[i];

                FileInputStream fis = new FileInputStream(srcFile);
                zos.putNextEntry(new ZipEntry(srcFile.getName()));

                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }
                zos.closeEntry();
                fis.close();
            }

            zos.close();
            fos.close();

        } catch (Exception ex) {
            System.out.println("Erro ao criar arquivo zip: " + ExceptionUtils.getStackTrace(ex));

        }
        try {
            File fileToDownload = new File(zipFile);
            Filedownload.save(fileToDownload, null);
        } catch (FileNotFoundException ex) {
            System.out.println("Erro ao baixar arquivo zip: " + ExceptionUtils.getStackTrace(ex));
        }
    }

    public boolean validarArquivo(Media arquivo, Component component, Long TAMANHO_MAX_MEGABYTES, String[] EXTENSOES_PERMITIDAS) {

        //valida tamanho do arquivo
        Long tamanho = 999L * 1024L * 1024L;
        try {
            tamanho = Long.valueOf(arquivo.getStreamData().available());
        } catch (Exception ex) {
            Clients.showNotification("Não foi possível identificar o tamanho do arquivo.", Clients.NOTIFICATION_TYPE_ERROR, component, "end_center", 0);
            return false;
        }
        if (tamanho > (TAMANHO_MAX_MEGABYTES * 1024L * 1024L)) {
            Clients.showNotification("O arquivo deve ser menor que " + TAMANHO_MAX_MEGABYTES + " MB.", Clients.NOTIFICATION_TYPE_ERROR, component, "end_center", 0);
            return false;
        }

        //valida tipo do arquivo
        String formatoArquivo = arquivo.getFormat();
        if (!Arrays.asList(EXTENSOES_PERMITIDAS).contains(formatoArquivo)) {
            Clients.showNotification("Somente arquivos " + Arrays.toString(EXTENSOES_PERMITIDAS).replace("[", "").replace("]", "") + " são permitidos.", Clients.NOTIFICATION_TYPE_ERROR, component, "end_center", 0);
            return false;
        }

        return true;

    }

    /* *****************************
    
       PASSAGEM DE PARAMETROS DE UM CONTROLLER ZK PARA OUTRO
    
       ***************************** */
    public HashMap getParametros() {
        Execution execution = Executions.getCurrent();
        HashMap hm = null;
        if (execution != null) {
            hm = (HashMap) execution.getArg();
            execution.popArg();
        }
        return hm;
    }

    public void setParametros(HashMap map) {
        Execution execution = Executions.getCurrent();
        if (execution != null) {
            execution.pushArg(map);
        }
    }

    public Object getParametro(String chave) {
        Execution execution = Executions.getCurrent();
        Object obj = null;
        if (execution != null) {
            Map hm = execution.getArg();
            if (hm != null) {
                obj = hm.get(chave);
            }
        }
        return obj;
    }

    public void setParametro(String chave, Object obj) {
        Execution execution = Executions.getCurrent();
        if (execution != null) {
            Map hm = execution.getArg();
            if (hm != null && !hm.isEmpty()) {
                execution.popArg();
                hm.put(chave, obj);
                execution.pushArg(hm);
            } else {
                hm = new HashMap();
                hm.put(chave, obj);
                execution.pushArg(hm);
            }
        }
    }

    public void clearParametros() {
        Execution execution = Executions.getCurrent();
        if (execution != null) {
            execution.popArg();
        }
    }

    /* *****************************
    
       INFORMAÇõES DO LADO CLIENTE
    
       ***************************** */
    public String getIpCliente() {
        String[] IP_HEADER_CANDIDATES = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
        };
        for (String header : IP_HEADER_CANDIDATES) {
            String ip = Executions.getCurrent().getHeader(header);
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }
        return Executions.getCurrent().getRemoteAddr();
    }

    /* *****************************
    
       INFORMAÇõES DO LADO SERVIDOR
    
       ***************************** */
    public boolean validaPaginaExiste(String caminhoZul) {
        String caminho = Sessions.getCurrent().getWebApp().getServletContext().getRealPath(caminhoZul.startsWith("/") ? caminhoZul : "/" + caminhoZul);
        Path path = (Path) Paths.get(caminho);
        return Files.exists(path);
    }

    public FileOutputStream abrirArquivoTexto(String nomeArq) throws FileNotFoundException {
        File arquivo;
        arquivo = new File(nomeArq);
        return new FileOutputStream(arquivo);
    }

    public void fecharArquivoTexto(FileOutputStream arquivo) throws IOException {
        arquivo.close();
    }

    public boolean downloadObjectListAsCsv(List dadosList, String nomeArquivo, String tipo) {

        Object[] dados = dadosList.toArray();

        //Se os dados for vazio, sai fora
        if (dados.length == 0) {
            return false;
        }

        //Cria um construtor de strings
        Class classType = dados[0].getClass();
        StringBuilder builder = new StringBuilder();

        //Para cada método, se for get() ou is(), imprime o nome do método no construtor de strings
        Method[] methods = classType.getDeclaredMethods();
        for (Method m : methods) {
            if (m.getParameterTypes().length == 0) {
                if (m.getName().startsWith("get")) {
                    builder.append('"' + m.getName().substring(3) + '"').append(';');
                } else if (m.getName().startsWith("is")) {
                    builder.append('"' + m.getName().substring(2) + '"').append(':');
                }
            }
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append('\n');

        //Para cada objeto do array, percorre cada método get() ou is() e imprime seu valor no construtor de strings
        for (Object d : dados) {
            for (Method m : methods) {
                if (m.getParameterTypes().length == 0) {
                    if (m.getName().startsWith("get") || m.getName().startsWith("is")) {
                        try {
                            builder.append('"' + m.invoke(d).toString() + '"').append(';');
                        } catch (Exception ex) {
                            builder.append(';');
                        }
                    }
                }
            }
            builder.append('\n');
        }
        builder.deleteCharAt(builder.length() - 1);

        //Cria um arquivo com os dados que estão no construtor de strings
        FileWriter fw = null;
        File file = new File(nomeArquivo);
        try {
            fw = new FileWriter(file);
            fw.write(builder.toString());
            fw.flush();

            //Se o arquivo foi construido com sucesso, faz o download
            Filedownload.save(file, tipo);
        } catch (Exception e) {
            return false;
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (Exception e) {
                }
                fw = null;
            }
        }

        return true;
    }

}
