package humcare.utilitarios;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.Format;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.swing.text.MaskFormatter;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.commons.validator.routines.UrlValidator;
import org.zkoss.zk.ui.Executions;

/**
 *
 * @author equipes
 */
public class Utils {


    /* ******************************
    
       DATAS
    
       ****************************** */
    public String formatarData(Date dt, String mascara) {
        Format formatter;
        formatter = new SimpleDateFormat(mascara);
        return (formatter.format(dt));

    }

    public String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public String getDateTime(String mascara) {
        DateFormat dateFormat = new SimpleDateFormat(mascara);
        Date date = new Date();
        return dateFormat.format(date);
    }

    public Date strToDate(String data_normal) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        Date dt = null;
        try {
            dt = sdf.parse(data_normal);
        } catch (ParseException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return dt;
    }

    public Date strToDateTime(String data_completa) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

        Date dt = null;
        try {
            dt = sdf.parse(data_completa);
        } catch (ParseException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return dt;
    }

    public String converteDataParaFormatoAmericano(String data) {

        // converte data no formata dd-mm-aaaa para aaaa-mm-dd
        String[] sPartes = new String[3];

        if (data.length() == 10) {

            sPartes = data.split("[-./]");

            if (sPartes[2].length() < 4) { // se a última parte não for o ano
                return data;
            }
            return sPartes[2] + "-" + sPartes[1] + "-" + sPartes[0];
        }
        return null;
    }

    public String mascaraDataDdMmAAAA(String data) {

        // converte data no formata dd-mm-aaaa para dd/mm/aaaa
        String[] sPartes = new String[3];

        if (data.length() == 10) {

            sPartes = data.split("[-./]");

            if (sPartes[0].length() < 4) {
                return sPartes[0] + "/" + sPartes[1] + "/" + sPartes[2];
            } else if (sPartes[2].length() < 4) { // se a última parte não for o ano
                return sPartes[2] + "/" + sPartes[1] + "/" + sPartes[0];
            } else if (sPartes[1].length() < 4) { // se a última parte não for o ano
                return sPartes[0] + "/" + sPartes[1] + "/" + sPartes[1];
            }
        }
        return null;
    }

    public String diferencaDias(String dataNormalInicio, String dataNormalFim) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date di = null;
        Date df = null;
        try {
            di = sdf.parse(dataNormalInicio);
            df = sdf.parse(dataNormalFim);
        } catch (ParseException e) {
            return "";
        }

        long diferencaMS = df.getTime() - di.getTime();
        long diferencaSegundos = diferencaMS / 1000;
        long diferencaMinutos = diferencaSegundos / 60;
        long diferencaHoras = diferencaMinutos / 60;
        long diferencaDias = diferencaHoras / 24;

        String retorno = Long.toString(diferencaDias);

        return retorno;

    }


    /* ******************************
    
       VALIDAÇAO
    
       ****************************** */
    public boolean validaCPF(String strCpf) {
        int d1, d2;
        int digito1, digito2, resto;
        int digitoCPF;
        String nDigResult;

        if (strCpf == null) {
            return false;
        }
        strCpf = soNumeros(strCpf);
        if (strCpf.length() != 11) {
            return false;
        }

        //Verificar se é uma sequencia de caracteres repetidos
        if (strCpf.equals("00000000000") || strCpf.equals("11111111111") || strCpf.equals("22222222222") || strCpf.equals("33333333333") || strCpf.equals("44444444444") || strCpf.equals("55555555555") || strCpf.equals("66666666666") || strCpf.equals("77777777777") || strCpf.equals("88888888888") || strCpf.equals("99999999999")) {
            return false;
        }

        d1 = d2 = 0;
        digito1 = digito2 = resto = 0;

        for (int nCount = 1; nCount < strCpf.length() - 1; nCount++) {
            digitoCPF = Integer.valueOf(strCpf.substring(nCount - 1, nCount)).intValue();

            //multiplique a ultima casa por 2 a seguinte por 3 a seguinte por 4 e assim por diante.
            d1 = d1 + (11 - nCount) * digitoCPF;

            //para o segundo digito repita o procedimento incluindo o primeiro digito calculado no passo anterior.
            d2 = d2 + (12 - nCount) * digitoCPF;
        }
        ;

        //Primeiro resto da divisão por 11.
        resto = (d1 % 11);

        //Se o resultado for 0 ou 1 o digito é 0 caso contrário o digito é 11 menos o resultado anterior.
        if (resto < 2) {
            digito1 = 0;
        } else {
            digito1 = 11 - resto;
        }

        d2 += 2 * digito1;

        //Segundo resto da divisão por 11.
        resto = (d2 % 11);

        //Se o resultado for 0 ou 1 o digito é 0 caso contrário o digito é 11 menos o resultado anterior.
        if (resto < 2) {
            digito2 = 0;
        } else {
            digito2 = 11 - resto;
        }

        //Digito verificador do CPF que está sendo validado.
        String nDigVerific = strCpf.substring(strCpf.length() - 2, strCpf.length());

        //Concatenando o primeiro resto com o segundo.
        nDigResult = String.valueOf(digito1) + String.valueOf(digito2);

        //comparar o digito verificador do cpf com o primeiro resto + o segundo resto.
        return nDigVerific.equals(nDigResult);
    }

    private boolean validaEmailRegex(String email) {
        String regex = "^[\\w-]+(\\.[\\w-]+)*@([\\w-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern p = Pattern.compile(regex);
        return p.matcher(email).matches();
    }

    private boolean validaEmailJavax(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

    public boolean isCnpj(String cnpj) {
        cnpj = cnpj.replace(".", "");
        cnpj = cnpj.replace("-", "");
        cnpj = cnpj.replace("/", "");

        try{
            Long.parseLong(cnpj);
        } catch(NumberFormatException e){
            return false;
        }

        // considera-se erro CNPJ's formados por uma sequencia de numeros iguais
        if (cnpj.equals("00000000000000") || cnpj.equals("11111111111111")
                || cnpj.equals("22222222222222") || cnpj.equals("33333333333333")
                || cnpj.equals("44444444444444") || cnpj.equals("55555555555555")
                || cnpj.equals("66666666666666") || cnpj.equals("77777777777777")
                || cnpj.equals("88888888888888") || cnpj.equals("99999999999999") || (cnpj.length() != 14))
            return (false);
        char dig13, dig14;
        int sm, i, r, num, peso; // "try" - protege o código para eventuais
        // erros de conversao de tipo (int)
        try {
            // Calculo do 1o. Digito Verificador
            sm = 0;
            peso = 2;
            for (i = 11; i >= 0; i--) {
                // converte o i-ésimo caractere do CNPJ em um número: // por
                // exemplo, transforma o caractere '0' no inteiro 0 // (48 eh a
                // posição de '0' na tabela ASCII)
                num = (int) (cnpj.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso + 1;
                if (peso == 10)
                    peso = 2;
            }

            r = sm % 11;
            if ((r == 0) || (r == 1))
                dig13 = '0';
            else
                dig13 = (char) ((11 - r) + 48);

            // Calculo do 2o. Digito Verificador
            sm = 0;
            peso = 2;
            for (i = 12; i >= 0; i--) {
                num = (int) (cnpj.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso + 1;
                if (peso == 10)
                    peso = 2;
            }
            r = sm % 11;
            if ((r == 0) || (r == 1))
                dig14 = '0';
            else
                dig14 = (char) ((11 - r) + 48);
            // Verifica se os dígitos calculados conferem com os dígitos
            // informados.
            if ((dig13 == cnpj.charAt(12)) && (dig14 == cnpj.charAt(13)))
                return (true);
            else
                return (false);
        } catch (InputMismatchException erro) {
            return (false);
        }
    }

    private boolean validaEmailApache(String email) {
        return EmailValidator.getInstance().isValid(email);
    }

    public boolean validaEmail(String email) {
        return this.validaEmailApache(email);
    }

    public boolean validaURL(String url) {
        UrlValidator validator = new UrlValidator();
        return validator.isValid(url);
    }

    public boolean validaPIS(String pisOrPasep) {
        int digit_count = 11;
        if (pisOrPasep == null) {
            return false;
        }
        String n = pisOrPasep.replaceAll("[^0-9]*", "");
        if (n.length() != digit_count) {
            return false;
        }
        int i;          // just count 
        int digit;      // A number digit
        int coeficient; // A coeficient  
        int sum;        // The sum of (Digit * Coeficient)
        int foundDv;    // The found Dv (Chek Digit)
        int dv = Integer.parseInt(String.valueOf(n.charAt(n.length() - 1)));
        sum = 0;
        coeficient = 2;
        for (i = n.length() - 2; i >= 0; i--) {
            digit = Integer.parseInt(String.valueOf(n.charAt(i)));
            sum += digit * coeficient;
            coeficient++;
            if (coeficient > 9) {
                coeficient = 2;
            }
        }
        foundDv = 11 - sum % 11;
        if (foundDv >= 10) {
            foundDv = 0;
        }
        return dv == foundDv;
    }

    /* ******************************
    
       CRIPTOGRAFIA
    
       ****************************** */
    public byte[] gerarHash(String frase, String algoritmo) {
        try {
            MessageDigest md = MessageDigest.getInstance(algoritmo);
            md.update(frase.getBytes());
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public String criptografaMD5(String chave) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        BigInteger hash = new BigInteger(1, md.digest(chave.getBytes()));
        String s = hash.toString(16);
        if (s.length() % 2 != 0) {
            s = "0" + s;
        }
        return s;
    }

    /* ******************************
    
       CALCULO NUMERICO
    
       ****************************** */
    public double getMenorValor(double[] vetor) {
        double menorValor = vetor[0];
        for (double valor : vetor) {
            if (menorValor > valor && valor > 0) {
                menorValor = valor;
            }
        }
        return menorValor;
    }

    public double getMaiorValor(double[] vetor) {
        double maiorValor = vetor[0];
        for (double valor : vetor) {
            if (maiorValor < valor && valor > 0) {
                maiorValor = valor;
            }
        }
        return maiorValor;
    }

    /**
     * 1 - Valor a arredondar. 2 - Quantidade de casas depois da vírgula. 3 -
     * Arredondar para cima ou para baixo? Para cima = 0 (ceil) Para baixo = 1
     * ou qualquer outro inteiro (floor)
     *
     */
    public double arredondar(double valor, int casas, int ceilOrFloor) {
        double arredondado = valor;
        arredondado *= (Math.pow(10, casas));
        if (ceilOrFloor == 0) {
            arredondado = Math.ceil(arredondado);
        } else {
            arredondado = Math.floor(arredondado);
        }
        arredondado /= (Math.pow(10, casas));
        return arredondado;
    }

    /* ******************************
    
       TEXTO TRATATIVA
    
       ****************************** */
    public String removeEspacos(String texto) {
        if (texto != null) {
            texto = texto.replaceAll("\\s+", " ");
            texto = texto.replaceAll("^\\s+", "");
            texto = texto.replaceAll("\\s+$", "");
            return texto;
        } else {
            return "";
        }
    }

    public String truncar(String texto, int tamanho) {
        if (texto != null) {
            if (texto.length() > tamanho) {
                texto = texto.substring(0, tamanho);
            }
            return texto;
        } else {
            return "";
        }
    }

    public String soNumeros(String texto) {
        return texto.replaceAll("[^0-9]", "");
    }

    public String removeCaracteresEspeciais(String texto) {
        if (texto != null) {
            String signos = "~\"`!@#$%^&*_+=;:[]{}\\|?()/";

            for (int v = 0; v < signos.length(); v++) {
                String i = signos.substring(v, v + 1);
                texto = texto.replace(i, "");
            }
            return texto;
        } else {
            return "";
        }
    }

    public String normalizaTexto(String texto) {
        if (texto != null) {
            return maiusculas(removeEspacos(removeSignos(texto)));
        } else {
            return "";
        }
    }

    public String removeSignos(String texto) {
        if (texto != null) {
            //String ConSignos ="áàäâãéèëêíìïîĩóòöôõúùüûũñÃÂÀÄÁÂÉÈËÊÎÍÌĨÔÓÒÖÕÚÙÜÛŨÑçÇ~'\"`!@#$%^&*()-_+=,./;:[]{}\\|?";
            //String SinSignos ="aaaaaeeeeiiiiiooooouuuuunAAAAAAEEEEIIIIOOOOOUUUUUNcC                              ";
            String ConSignos = "áàäâãéèëêíìïîĩóòöôõúùüûũñÃÂÀÄÁÂÉÈËÊÎÍÌĨÔÓÒÖÕÚÙÜÛŨÑçÇ~\"`!@#$%^&*_+=;:[]{}\\|?";
            String SinSignos = "aaaaaeeeeiiiiiooooouuuuunAAAAAAEEEEIIIIOOOOOUUUUUNcC                       ";
            int v;

            for (v = 0; v < SinSignos.length(); v++) {

                String i = ConSignos.substring(v, v + 1);
                String j = SinSignos.substring(v, v + 1);
                texto = texto.replace(i, j);
            }
            return removeCaracterIlegal(texto);
        } else {
            return "";
        }
    }

    public String removeCaracterIlegal(String frase) {
        //Retorna a Frase dada sem caracteres estranhos;
        String validos = " abcdefghijklmnopqrstuvxywzABCDEFGHIJKLMNOPQRSTUVXYWZ()/_.,;:+-%º\"*&@?0123457689àâêôûãõáéíóúçÀÂÊÔÛÃÕÁÉÍÓÚÇ'\\|";
        int i;
        String retorno = "";
        for (i = 0; i < frase.length(); i++) {
            if (validos.indexOf(frase.charAt(i)) > -1) {
                retorno += frase.charAt(i);
            }
        }
        return retorno;
    }

    public String removeSignos(String texto, boolean manterAcentos) {
        if (texto != null) {
            String ConSignos = "";
            String SinSignos = "";

            if (manterAcentos) {
                ConSignos = "áàäâéèëêíìïîóòöôúùüûÂÀÄÁÂÉÈËÊÎÍÌÔÓÒÖÚÙÜÛçÇ'\"`!@#$%^&*()-_+=,./;:[]{}\\|?";
                SinSignos = "aaaaeeeeiiiioooouuuuAAAAAEEEEIIIOOOOUUUUcC                             ";
            } else {
                ConSignos = "'\"`!#$%^&*()_+=,/;:[]{}\\|?";
                SinSignos = "                          ";
            }
            int v;

            for (v = 0; v < SinSignos.length(); v++) {

                String i = ConSignos.substring(v, v + 1);
                String j = SinSignos.substring(v, v + 1);
                texto = texto.replace(i, j);
            }
            return texto;
        } else {
            return "";
        }
    }

    public String maiusculas(String texto) {
        if (texto != null) {
            return texto.toUpperCase();
        } else {
            return "";
        }
    }

    public String espacosEsquerda(String texto, int tamanho) {
        if (texto == null) {
            texto = "";
        }
        int tt = texto.length();

        if (tt < tamanho) {
            for (int i = 0; i < (tamanho - tt); i++) {
                texto = " " + texto;
            }
        } else {
            texto = texto.substring(0, tamanho);
        }
        return texto;
    }

    public String espacosDireita(String texto, int tamanho) {
        if (texto == null) {
            texto = "";
        }
        int tt = texto.length();

        if (tt < tamanho) {
            for (int i = 0; i < (tamanho - tt); i++) {
                texto = texto + " ";
            }
        } else {
            texto = texto.substring(0, tamanho);
        }
        return texto;
    }

    public String textoCapitulado(String frase) {
        /**
         * Retorna um texto com as iniciais maiúsculas
         */
        if (frase != null && !frase.isBlank()) {
            StringBuffer res = new StringBuffer();
            String[] strArr = this.removeEspacos(frase).toLowerCase().split(" ");
            for (String str : strArr) {
                char[] stringArray = str.trim().toCharArray();
                stringArray[0] = Character.toUpperCase(stringArray[0]);
                str = new String(stringArray);
                res.append(str).append(" ");
            }
            frase = res.toString().trim();

            frase = frase.replace(" E ", " e ");
            frase = frase.replace(" A ", " a ");
            frase = frase.replace(" Da ", " da ");
            frase = frase.replace(" Das ", " das ");
            frase = frase.replace(" De ", " de ");
            frase = frase.replace(" Do ", " do ");
            frase = frase.replace(" Dos ", " dos ");
            frase = frase.replace(" Ii ", " II ");
            frase = frase.replace(" Iii ", " III ");
            frase = frase.replace(" Iv ", " IV ");
            frase = frase.replace(" Vi ", " VI ");
            frase = frase.replace(" Vii ", " VII ");
            frase = frase.replace(" Viii ", " VIII ");
            frase = frase.replace(" Ix ", " IX ");
            frase = frase.replace(" Xi ", " XI ");
            frase = frase.replace(" Xii ", " XII ");
            frase = frase.replace(" Xiii ", " XIII ");
            return frase;
        } else {
            return "";
        }
    }

    public String plic(String s) {
        if (s == null) {
            return "''";
        } else {
            if (s.contains("'")) {
                s = s.replace("'", "''");
            }
            return "'" + s.trim() + "'";
        }
    }

    public Long valorNumero(String valor) {
        valor = soNumeros(valor);
        if (valor.equals("")) {
            valor = "0";
        }
        return Long.valueOf(valor);
    }

    public String valorString(Object obj) {
        if (obj == null) {
            return "";
        } else {
            return obj.toString();
        }
    }

    public String dataBanco(Date data) {
        if (data == null) {
            return null;
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(data);
    }

    public String dataNormal(Date data) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(data);
    }

    public String nomeAbreviado(String nomeInteiro, Integer tamanhoMaximoDoNome) {
        //configurações
        String[] naoAbreviados = new String[]{"DE", "DA", "DAS", "DO", "DOS", "E", "A", "DI", "DEL", "LOS", "JR", "JR.", "FILHO", "FILHA", "NETO", "NETA"};
        String[] especiais = new String[]{"JR", "FILHO", "FILHA", "NETO", "NETA"};

        //remove signos
        nomeInteiro = removeCaracteresEspeciais(nomeInteiro);

        //remove espaços extras
        nomeInteiro = nomeInteiro.replaceAll("\\s+", " ");
        nomeInteiro = nomeInteiro.replaceAll("^\\s+", "");
        nomeInteiro = nomeInteiro.replaceAll("\\s+$", "");

        //deixa tudo em maiúscula para padronizar e simplificar os tratamentos
        nomeInteiro = nomeInteiro.toUpperCase();

        //verifica se da para reduzir o "junior". esses serão os primeiros a abreviar.
        if (nomeInteiro.length() > tamanhoMaximoDoNome) {
            nomeInteiro = nomeInteiro.replace("JUNIOR", "JR");
            nomeInteiro = nomeInteiro.replace("JÚNIOR", "JR");
        }

        //variáveis auxiliares
        String[] nomePedacos = nomeInteiro.split(" ");
        List<String> naoAbreviadosLista = Arrays.asList(naoAbreviados);
        List<String> especiaisLista = Arrays.asList(especiais);

        //inverte o nome
        for (int i = 0; i < nomePedacos.length / 2; i++) {
            String temp = nomePedacos[i];
            nomePedacos[i] = nomePedacos[nomePedacos.length - i - 1];
            nomePedacos[nomePedacos.length - i - 1] = temp;
        }

        //verifica se pula ultimo sobrenome, pois se conter nome especial no final, não abrevia o último sobrenome
        int pula = 0;
        if (especiaisLista.contains(nomePedacos[0])) {
            pula = 1;
        }

        //vai reduzindo somente os nomes do meio até caber ou até acabar os nomes do meio
        for (int i = 1 + pula; i < nomePedacos.length - 1; i++) {
            //se já deu o tamanho, cai fora
            if (Arrays.toString(nomePedacos).replaceAll(",", "").replaceAll("\\[", "").replaceAll("\\]", "").length() <= tamanhoMaximoDoNome) {
                break;
            }

            //se é algum nome que não abrevia, pula
            if (naoAbreviadosLista.contains(nomePedacos[i])) {
                continue;
            }

            //pega primeira letra do nome e coloca um ponto
            nomePedacos[i] = nomePedacos[i].substring(0, 1).concat(".");

        }

        //desinverte o nome
        for (int i = 0; i < nomePedacos.length / 2; i++) {
            String temp = nomePedacos[i];
            nomePedacos[i] = nomePedacos[nomePedacos.length - i - 1];
            nomePedacos[nomePedacos.length - i - 1] = temp;
        }

        //junta o nome
        String nomeInteiroAbreviado = Arrays.toString(nomePedacos).replaceAll(",", "").replaceAll("\\[", "").replaceAll("\\]", "");

        //retorna em caixa alta. verificar se precisa chamar textoCapitulado()
        //Atenção: sugiro truncar pois pode ser que ainda não ficou no tamanho desejado.
        return nomeInteiroAbreviado;
    }

    public String valorPorExtenso(double vlr) {
        if (vlr == 0) {
            return ("zero");
        }

        long inteiro = (long) Math.abs(vlr); // parte inteira do valor
        double resto = vlr - inteiro;       // parte fracionária do valor

        String vlrS = String.valueOf(inteiro);
        if (vlrS.length() > 15) {
            return ("Erro: valor superior a 999 trilhões.");
        }

        String s = "", saux, vlrP;
        String centavos = String.valueOf((int) Math.round(resto * 100));

        String[] unidade = {"", "um", "dois", "três", "quatro", "cinco", "seis", "sete", "oito", "nove", "dez", "onze", "doze", "treze", "quatorze", "quinze", "dezesseis", "dezessete", "dezoito", "dezenove"};
        String[] centena = {"", "cento", "duzentos", "trezentos", "quatrocentos", "quinhentos", "seiscentos", "setecentos", "oitocentos", "novecentos"};
        String[] dezena = {"", "", "vinte", "trinta", "quarenta", "cinquenta", "sessenta", "setenta", "oitenta", "noventa"};
        String[] qualificaS = {"", "mil", "milhão", "bilhão", "trilhão"};
        String[] qualificaP = {"", "mil", "milhões", "bilhões", "trilhões"};

        // definindo o extenso da parte inteira do valor
        int n, unid, dez, cent, tam, i = 0;
        boolean umReal = false, tem = false;
        while (!vlrS.equals("0")) {
            tam = vlrS.length();
            // retira do valor a 1a. parte, 2a. parte, por exemplo, para 123456789:
            // 1a. parte = 789 (centena)
            // 2a. parte = 456 (mil)
            // 3a. parte = 123 (milhões)
            if (tam > 3) {
                vlrP = vlrS.substring(tam - 3, tam);
                vlrS = vlrS.substring(0, tam - 3);
            } else { // última parte do valor
                vlrP = vlrS;
                vlrS = "0";
            }
            if (!vlrP.equals("000")) {
                saux = "";
                if (vlrP.equals("100")) {
                    saux = "cem";
                } else {
                    n = Integer.parseInt(vlrP, 10);  // para n = 371, tem-se:
                    cent = n / 100;                  // cent = 3 (centena trezentos)
                    dez = (n % 100) / 10;            // dez  = 7 (dezena setenta)
                    unid = (n % 100) % 10;           // unid = 1 (unidade um)
                    if (cent != 0) {
                        saux = centena[cent];
                    }
                    if ((n % 100) <= 19) {
                        if (saux.length() != 0) {
                            saux = saux + " e " + unidade[n % 100];
                        } else {
                            saux = unidade[n % 100];
                        }
                    } else {
                        if (saux.length() != 0) {
                            saux = saux + " e " + dezena[dez];
                        } else {
                            saux = dezena[dez];
                        }
                        if (unid != 0) {
                            if (saux.length() != 0) {
                                saux = saux + " e " + unidade[unid];
                            } else {
                                saux = unidade[unid];
                            }
                        }
                    }
                }
                if (vlrP.equals("1") || vlrP.equals("001")) {
                    if (i == 0) // 1a. parte do valor (um real)
                    {
                        umReal = true;
                    } else {
                        saux = saux + " " + qualificaS[i];
                    }
                } else if (i != 0) {
                    saux = saux + " " + qualificaP[i];
                }
                if (s.length() != 0) {
                    s = saux + ", " + s;
                } else {
                    s = saux;
                }
            }
            if (((i == 0) || (i == 1)) && s.length() != 0) {
                tem = true; // tem centena ou mil no valor
            }
            i = i + 1; // próximo qualificador: 1- mil, 2- milhão, 3- bilhão, ...
        }

        if (s.length() != 0) {
            if (umReal) {
                s = s + " real";
            } else if (tem) {
                s = s + " reais";
            } else {
                s = s + " de reais";
            }
        }

        // definindo o extenso dos centavos do valor
        if (!centavos.equals("0")) { // valor com centavos
            if (s.length() != 0) // se não é valor somente com centavos
            {
                s = s + " e ";
            }
            if (centavos.equals("1")) {
                s = s + "um centavo";
            } else {
                n = Integer.parseInt(centavos, 10);
                if (n <= 19) {
                    s = s + unidade[n];
                } else {             // para n = 37, tem-se:
                    unid = n % 10;   // unid = 37 % 10 = 7 (unidade sete)
                    dez = n / 10;    // dez  = 37 / 10 = 3 (dezena trinta)
                    s = s + dezena[dez];
                    if (unid != 0) {
                        s = s + " e " + unidade[unid];
                    }
                }
                s = s + " centavos";
            }
        }
        return (s);
    }

    /* ******************************
    
       TEXTO FORMATAÇAO
    
       ****************************** */
    public String formataCep(String cep1, String cep2) {

        String cepFormatado = "";

        if (!cep1.trim().equals("")) {

            if (cep1.length() != 5) {
                while (cep1.length() < 5) {
                    cep1 = "0" + cep1;
                }
            }

            if (cep2.length() != 3) {
                while (cep2.length() < 3) {
                    cep2 = "0" + cep2;
                }
            }

            cepFormatado = cep1.substring(0, 2) + "." + cep1.substring(2, 5) + "-" + cep2;
        }

        return cepFormatado;
    }

    public String formataCPF(Long cpf) {
        String cpfFormatado = "";
        if (cpf != null) {
            cpfFormatado = this.zerosEsquerda(cpf.toString(), 11);
            try {
                cpfFormatado = this.formatarString(cpfFormatado, "###.###.###-##");
            } catch (ParseException ex) {
                cpfFormatado = "ERRO FORMATO";
            }
        }
        return cpfFormatado;
    }

    private String formatarString(String texto, String mascara) throws ParseException {
        MaskFormatter mf = new MaskFormatter(mascara);
        mf.setValueContainsLiteralCharacters(false);
        return mf.valueToString(texto);
    }

    public String zerosEsquerda(String texto, int tamanho) {
        if (texto != null) {
            int tt = texto.length();

            if (tt < tamanho) {
                for (int i = 0; i < (tamanho - tt); i++) {
                    texto = "0" + texto;
                }
            }
            return texto;
        } else {
            return "";
        }
    }

    public String removeAcentos(String palavra) {
        return Normalizer.normalize(palavra, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }

    public String insereMascaraTelefone(String telefone) {

        if (telefone.length() == 10) {
            telefone = "(" + telefone.substring(0, 2) + ")" + telefone.substring(2, 6) + "-" + telefone.substring(6, 10);
            return telefone;
        }
        return "";
    }

    public String insereMascaraCelular(String celular) {

        if (celular.length() == 11) {
            celular = "(" + celular.substring(0, 2) + ")" + celular.substring(2, 7) + "-" + celular.substring(7, 11);
            return celular;
        }
        return "";
    }

    /* ******************************
    
       OBJETOS
    
       ****************************** */
    public static Object deserializeObjectFromBase64String(String s) throws IOException, ClassNotFoundException {
        byte[] data = Base64.getDecoder().decode(s);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
        Object o = ois.readObject();
        ois.close();
        return o;
    }

    /**
     * Write the object to a Base64 string.
     */
    public static String serializeObjectToBase64String(Object o) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(o);
        oos.close();
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    /* ******************************
    
       ARQUIVOS
    
       ****************************** */
    public Date getClassDateCompilation(Class clazz) {
        Date date = null;
        try {
            String className = clazz.getCanonicalName().replace('.', '/') + ".class";
            URI uri = getClass().getClassLoader().getResource(className).toURI();
            Long timestamp = new File(uri).lastModified();
            date = new Date(timestamp);
        } catch (Exception ex) {
            System.out.println("Não foi possível ver a data de compilaçao da classe." + ExceptionUtils.getStackTrace(ex));
        }
        return date;
    }

    public String getCaminhoDiretorioPublicoTemporario() {
        HttpServletRequest request = (HttpServletRequest) Executions.getCurrent().getNativeRequest();
        String diretorioPublico = request.getRealPath("");
        String diretorioPublicoTemporario = diretorioPublico + "tmp/";
        new File(diretorioPublicoTemporario).mkdirs();
        return diretorioPublicoTemporario;
    }

}
