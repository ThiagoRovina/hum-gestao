package humcare.utilitarios;

import java.net.MalformedURLException;
import java.net.URL;

import lombok.*;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.mail.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Email {

    private final String HOST = "smtp.uem.br";
    private final Integer PORT = 25;

    private String deEmail = "";
    private String deNome = "";
    private String paraEmail = "";
    private String assunto = "";
    private String mensagem = "";
    private String cabecalhoPrh = "";
    private String rodapePrh = "";
    private String pathAnexo = "";

    /**
     * Envia e-mail com mensagem em HTML.
     *
     * @return true caso sucesso ou false caso erro
     */
    public boolean enviarEmailHtml() {
        try {
            HtmlEmail email = new HtmlEmail();
            email.setHostName(HOST);
            email.setSmtpPort(PORT);
            email.setCharset("UTF-8");

            email.addTo(getParaEmail(), getParaEmail());
            email.setFrom(getDeEmail(), getDeNome());
            email.setSubject(getAssunto());
            email.setHtmlMsg(getMensagem());

            String messagaID = email.send();

            return true;
        } catch (EmailException ex) {
            System.out.println("Houve um erro ao enviar e-mail: " + ExceptionUtils.getStackTrace(ex));
            return false;
        }
    }

    /**
     * Envia e-mail com texto puro como mensagem.
     *
     * @return true caso sucesso ou false caso erro
     */
    public boolean enviarEmailTexto() {
        try {
            SimpleEmail email = new SimpleEmail();
            email.setHostName(HOST);
            email.setSmtpPort(PORT);
            email.setCharset("UTF-8");

            email.addTo(getParaEmail(), getParaEmail());
            email.setFrom(getDeEmail(), getDeNome());
            email.setSubject(getAssunto());
            email.setMsg(getMensagem());

            String messagaID = email.send();

            return true;
        } catch (EmailException ex) {
            System.out.println("Houve um erro ao enviar e-mail: " + ExceptionUtils.getStackTrace(ex));
            return false;
        }
    }

    /**
     * Envia e-mail com mensagem em HTML.
     *
     * @return true caso sucesso ou false caso erro
     */
    public boolean enviarEmailPRH(String bodyHTML) {
        String mensagemEmail = this.htmlCabecalhoPrh() + bodyHTML + this.htmlRodapePRH();
        try {
            HtmlEmail email = new HtmlEmail();
            email.setHostName(HOST);
            email.setSmtpPort(PORT);
            email.setCharset("UTF-8");

            email.addTo(this.getParaEmail(), this.getParaEmail());
            email.setFrom("naoresponder-prh@uem.br", "PRH - Pró Reitoria de Recursos Humanos");
            email.setSubject(this.getAssunto());
            email.setHtmlMsg(mensagemEmail);

            String messagaID = email.send();

            return true;
        } catch (EmailException ex) {
            System.out.println("Houve um erro ao enviar e-mail: " + ExceptionUtils.getStackTrace(ex));
            return false;
        }
    }
    
    public boolean enviarEmailHUM(String bodyHTML) {
        String mensagemEmail = htmlCabecalhoHUM() + bodyHTML + this.htmlRodapeHUM();
        try {
            HtmlEmail email = new HtmlEmail();
            email.setHostName(HOST);
            email.setSmtpPort(PORT);
            email.setCharset("UTF-8");

            email.addTo(this.getParaEmail(), this.getParaEmail());
            email.setFrom("naoresponder-hum@uem.br", "HUM - Hospital Universitário Regional de Maringá");
            email.setSubject(this.getAssunto());
            email.setHtmlMsg(mensagemEmail);

            String messagaID = email.send();

            return true;
        } catch (EmailException ex) {
            System.out.println("Houve um erro ao enviar e-mail: " + ExceptionUtils.getStackTrace(ex));
            return false;
        }
    }

    private boolean ExemploEnviaEmailComAnexo() {
        try {

            // configura o email
            MultiPartEmail email = new MultiPartEmail();
            email.setHostName(HOST);
            email.setSmtpPort(PORT);
            email.setCharset("UTF-8");

            // popula os dados básicos
            email.addTo(getParaEmail(), getParaEmail()); //destinatário
            email.setFrom(getDeEmail(), getDeNome()); // remetente
            email.setSubject(getAssunto()); // assunto do e-mail
            email.setMsg(getMensagem()); //conteudo do e-mail

            // cria o anexo
            EmailAttachment anexo1 = new EmailAttachment();
            anexo1.setPath(getPathAnexo());
            anexo1.setDisposition(EmailAttachment.ATTACHMENT);
            anexo1.setDescription("Email que envia anexo");
            anexo1.setName(getPathAnexo());
            email.attach(anexo1);

            // envia o email
            String messagaID = email.send();

            return true;
        } catch (EmailException ex) {
            System.out.println("Houve um erro ao enviar e-mail: " + ExceptionUtils.getStackTrace(ex));
            return false;
        }
    }

    private boolean ExemploEnviaEmailFormatoHtml() {
        try {
            HtmlEmail email = new HtmlEmail();
            email.setHostName(HOST);
            email.setSmtpPort(PORT);
            email.setCharset("UTF-8");

            email.addTo(getParaEmail(), getParaEmail()); //destinatário
            email.setFrom(getDeEmail(), getDeNome()); // remetente
            email.setSubject(getAssunto()); // assunto do e-mail
            email.setMsg(getMensagem()); //conteudo do e-mail

            // adiciona uma imagem ao corpo da mensagem e retorna seu id
            URL url = new URL("http://www.apache.org/images/asf_logo_wide.gif");
            String cid = email.embed(url, "Apache logo");

            // configura a mensagem para o formato HTML
            email.setHtmlMsg("<html>Logo do Apache - <img ></html>");

            // configure uma mensagem alternativa caso o servidor não suporte HTML
            email.setTextMsg("Seu servidor de e-mail não suporta mensagem HTML");

            String messagaID = email.send();

            return true;
        } catch (EmailException | MalformedURLException ex) {
            System.out.println("Houve um erro ao enviar e-mail: " + ExceptionUtils.getStackTrace(ex));
            return false;
        }

    }

    private String htmlCabecalhoPrh() {
        return "<!DOCTYPE html>" +
                "<html lang=\"pt-BR\">" +
                "<head>" +
                "    <meta charset=\"UTF-8\">" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "    <title>Pró-Reitoria de Recursos Humanos - PRH/UEM</title>" +
                "    <style>" +
                "        body {" +
                "            font-family: Arial, sans-serif;" +
                "            margin: 0;" +
                "            padding: 0;" +
                "            background-color: #f4f4f9;" +
                "            color: #333;" +
                "        }" +
                "        .container {" +
                "            max-width: 800px;" +
                "            margin: 20px auto;" +
                "            background: #fff;" +
                "            border: 1px solid #ddd;" +
                "            border-radius: 8px;" +
                "            overflow: hidden;" +
                "        }" +
                "        .header {" +
                "            color: #fff;" +
                "            padding: 15px;" +
                "            text-align: center;" +
                "            border-bottom: 1px solid #ddd;" +
                "        }" +
                "        .content {" +
                "            padding: 20px;" +
                "            font-size: 16px;" +
                "            line-height: 1.6;" +
                "        }" +
                "        .content strong {" +
                "            color: #004080;" +
                "        }" +
                "        .footer {" +
                "            background-color: #004080;" +
                "            color: #fff;" +
                "            text-align: center;" +
                "            padding: 10px;" +
                "            font-size: 14px;" +
                "        }" +
                "        .footer a {" +
                "            color: #ffffff;" +
                "            text-decoration: none;" +
                "        }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class=\"container\">" +
                "        <div class=\"header\">" +
                "            <img src=\"https://prh.uem.br/@@site-logo/prh.png\" style=\"width: 100%;heigth: 100%\" />" +
                "        </div>" +
                "        <div class=\"content\">";
    }
    
    private String htmlCabecalhoHUM() {
        return "<!DOCTYPE html>" +
                "<html lang=\"pt-BR\">" +
                "<head>" +
                "    <meta charset=\"UTF-8\">" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "    <title>Gestão de equipamentos - HUM/UEM</title>" +
                "    <style>" +
                "        body {" +
                "            font-family: Arial, sans-serif;" +
                "            margin: 0;" +
                "            padding: 0;" +
                "            background-color: #f4f4f9;" +
                "            color: #333;" +
                "        }" +
                "        .container {" +
                "            max-width: 800px;" +
                "            margin: 20px auto;" +
                "            background: #fff;" +
                "            border: 1px solid #ddd;" +
                "            border-radius: 8px;" +
                "            overflow: hidden;" +
                "        }" +
                "        .header {" +
                "            color: #fff;" +
                "            padding: 15px;" +
                "            text-align: center;" +
                "            border-bottom: 1px solid #ddd;" +
                "        }" +
                "        .content {" +
                "            padding: 20px;" +
                "            font-size: 16px;" +
                "            line-height: 1.6;" +
                "        }" +
                "        .content strong {" +
                "            color: #004080;" +
                "        }" +
                "        .footer {" +
                "            background-color: #004080;" +
                "            color: #fff;" +
                "            text-align: center;" +
                "            padding: 10px;" +
                "            font-size: 14px;" +
                "        }" +
                "        .footer a {" +
                "            color: #ffffff;" +
                "            text-decoration: none;" +
                "        }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class=\"container\">" +
                "        <div class=\"header\">" +
                "            <img src=\"https://prh.uem.br/@@site-logo/prh.png\" style=\"width: 100%;heigth: 100%\" />" +
                "        </div>" +
                "        <div class=\"content\">";
    }
    
    private String htmlRodapePRH() {
        return  "        </div>" +
                "        <div class=\"footer\">" +
                "            <p>PRH - Universidade Estadual de Maringá</p>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }
    
    private String htmlRodapeHUM() {
        return  "        </div>" +
                "        <div class=\"footer\">" +
                "            <p>HUM - Hospital Universitário Regional de Maringá</p>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }
}