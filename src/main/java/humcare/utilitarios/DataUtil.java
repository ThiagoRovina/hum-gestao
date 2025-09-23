package humcare.utilitarios;

import java.sql.Date;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class DataUtil {

    public DataUtil() {
        super();
    }

    public static java.sql.Date getDataAtualJavaSqlDate() {
        return new java.sql.Date(Calendar.getInstance().getTimeInMillis());
    }

    public static java.util.Date getDataAtualJavaUtilDate() {
        return new java.util.Date(Calendar.getInstance().getTimeInMillis());
    }

    public static int getDataAtualAnoMesDia() {
        return getAnoMesDia(getDataAtualJavaSqlDate());
    }

    public static int getDia(Date data) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        return Integer.parseInt(sdf.format(data));
    }

    public static int getMes(Date data) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM");
        return Integer.parseInt(sdf.format(data));
    }

    public static int getAno(Date data) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        return Integer.parseInt(sdf.format(data));
    }

    public static int getAnoMesDia(java.sql.Date data) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return Integer.parseInt(sdf.format(data));
    }

    public static int getDiaMesAno(java.sql.Date data) {
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
        return Integer.parseInt(sdf.format(data));
    }

    public static String getDataFormatada(java.sql.Date data) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(data);
    }

    public static String getDataFormatada(java.sql.Timestamp timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(timestamp);
    }

    public static Date getDataSql(java.sql.Timestamp timestamp) {
        return new java.sql.Date(timestamp.getTime());
    }

    public static String getDataAtualFormatada() {
        return getDataFormatada(getDataAtualJavaSqlDate());
    }

    public static java.sql.Date converteStringParaSql(String dataInvertidaStr) {
        return java.sql.Date.valueOf(dataInvertidaStr);
    }

    /**
     * @param data1 A data menor.
     * @param data2 A data maior.
     * @return Quantidade de dias.
     */
    public static int diferencaDeDias(java.sql.Date data1, java.sql.Date data2) {
        Calendar c1 = new GregorianCalendar();
        Calendar c2 = new GregorianCalendar();
        c1.setTime(data1);
        c2.setTime(data2);
        long m1 = c1.getTimeInMillis();
        long m2 = c2.getTimeInMillis();
        return (int) ((m2 / (24 * 60 * 60 * 1000)) - (m1 / (24 * 60 * 60 * 1000)));
    }

    /**
     * @param data Data base.
     * @param qtdeDias Informar valor positivo para Adicionar, ou valor negativo para Subtrair dias.
     * @return Nova data com quantidade de dias adicionada ou subtraída.
     */
    public static java.sql.Date adicionaSubtraiDias(java.sql.Date data, int qtdeDias) {
        Calendar c = new GregorianCalendar();
        c.setTime(data);
        c.add(Calendar.DATE, qtdeDias);
        return new java.sql.Date(c.getTimeInMillis());
    }

    public static int getDiaAtual() {
        return (Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
    }

    public static int getMesAtual() {
        return (Calendar.getInstance().get(Calendar.MONTH)) + 1;
    }

    public static int getAnoAtual() {
        return (Calendar.getInstance().get(Calendar.YEAR));
    }

    public static Date getDataVencimentoCalculada(int nDias) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, +nDias);
        return new java.sql.Date(c.getTimeInMillis());
    }

    public static Date getDataVencimentoCalculada(int nDias, int nMeses) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, +nDias);
        c.add(Calendar.MONTH, +nMeses);
        return new java.sql.Date(c.getTimeInMillis());
    }

    public static boolean isDataIgual(Date dataUm, Date dataDois) {
        if (DataUtil.getAnoMesDia(dataUm) == DataUtil.getAnoMesDia(dataDois)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isDataMenor(Date dataUm, Date dataDois) {
        if (DataUtil.getAnoMesDia(dataUm) < DataUtil.getAnoMesDia(dataDois)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isDataMaior(Date dataUm, Date dataDois) {
        if (DataUtil.getAnoMesDia(dataUm) > DataUtil.getAnoMesDia(dataDois)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isDataMenorIgual(Date dataUm, Date dataDois) {
        if (DataUtil.getAnoMesDia(dataUm) <= DataUtil.getAnoMesDia(dataDois)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isDataMaiorIgual(Date dataUm, Date dataDois) {
        if (DataUtil.getAnoMesDia(dataUm) >= DataUtil.getAnoMesDia(dataDois)) {
            return true;
        } else {
            return false;
        }
    }

    /*
     * Compara com a Data Atual
     */
    public static boolean isMenorQueDataAtual(Date data) {
        if (DataUtil.getAnoMesDia(data) < DataUtil.getDataAtualAnoMesDia()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isMaiorQueDataAtual(Date data) {
        if (DataUtil.getAnoMesDia(data) > DataUtil.getDataAtualAnoMesDia()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isIgualADataAtual(Date data) {
        if (DataUtil.getAnoMesDia(data) == DataUtil.getDataAtualAnoMesDia()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isMenorOuIgualDataAtual(Date data) {
        if (DataUtil.getAnoMesDia(data) <= DataUtil.getDataAtualAnoMesDia()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isMaiorOuIgualDataAtual(Date data) {
        if (DataUtil.getAnoMesDia(data) >= DataUtil.getDataAtualAnoMesDia()) {
            return true;
        } else {
            return false;
        }
    }

    public static String getMesPorExtenso(int mes) {
        switch (mes) {
            case 1:
                return "janeiro";
            case 2:
                return "fevereiro";
            case 3:
                return "março";
            case 4:
                return "abril";
            case 5:
                return "maio";
            case 6:
                return "junho";
            case 7:
                return "julho";
            case 8:
                return "agosto";
            case 9:
                return "setembro";
            case 10:
                return "outubro";
            case 11:
                return "novembro";
            case 12:
                return "dezembro";
        }
        return "";
    }

    public static String getMesAbreviado(int mes) {
        return DataUtil.getMesPorExtenso(mes).substring(0, 3);
    }

    public static String getDataPorExtenso(Date data) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String[] partes = sdf.format(data).split("-");
        return (Integer.parseInt(partes[0]) + " de " + DataUtil.getMesPorExtenso(Integer.parseInt(partes[1])) + " de " + partes[2]);
    }

    // dias da semana
    public static String getDiaDaSemana(int diaDaSemana) {
        switch (diaDaSemana) {
            case 0:
                return "Em branco";
            case 1:
                return "Domingo";
            case 2:
                return "Segunda";
            case 3:
                return "Terça";
            case 4:
                return "Quarta";
            case 5:
                return "Quinta";
            case 6:
                return "Sexta";
            case 7:
                return "Sábado";
        }
        return "";
    }

    public static String getDiaDaSemanaInclusiveBranco(int diaDaSemana) {
        switch (diaDaSemana) {
            case 0:
                return "Em branco";
            case 1:
                return "Domingo";
            case 2:
                return "Segunda";
            case 3:
                return "Terça";
            case 4:
                return "Quarta";
            case 5:
                return "Quinta";
            case 6:
                return "Sexta";
            case 7:
                return "Sábado";
        }
        return "";
    }

    public static String getDiaDaSemanaAbreviado(int diaDaSemana) {
        switch (diaDaSemana) {
            case 1:
                return "DOM";
            case 2:
                return "SEG";
            case 3:
                return "TER";
            case 4:
                return "QUA";
            case 5:
                return "QUI";
            case 6:
                return "SEX";
            case 7:
                return "SAB";
        }
        return "";
    }

    public static List<String> getDiasDaSemana() {
        List<String> diasDaSemana = new ArrayList<String>();
        diasDaSemana.add(1, "Domingo");
        diasDaSemana.add(2, "Segunda");
        diasDaSemana.add(3, "Terça");
        diasDaSemana.add(4, "Quarta");
        diasDaSemana.add(5, "Quinta");
        diasDaSemana.add(6, "Sexta");
        diasDaSemana.add(7, "Sábado");
        return diasDaSemana;
    }

    public static List<String> getDiasDaSemanaInclusiveBranco() {
        List<String> diasDaSemana = new ArrayList<String>();
        diasDaSemana.add(0, "Em branco");
        diasDaSemana.add(1, "Domingo");
        diasDaSemana.add(2, "Segunda");
        diasDaSemana.add(3, "Terça");
        diasDaSemana.add(4, "Quarta");
        diasDaSemana.add(5, "Quinta");
        diasDaSemana.add(6, "Sexta");
        diasDaSemana.add(7, "Sábado");
        return diasDaSemana;
    }

    public static String formatarData(Date dt, String mascara) {
        Format formatter;
        formatter = new SimpleDateFormat(mascara);
        return (formatter.format(dt));
    }

    public static Date strToDate(String data_normal) {
        Date dt = (Date) new java.util.Date(data_normal);
        //dt.setDate(Integer.parseInt(data_normal.substring(0, 1)));
        //dt.setMonth(Integer.parseInt(data_normal.substring(3, 4)));
        //dt.setYear(Integer.parseInt(data_normal.substring(6, 9)));
        return dt;
    }

    public static java.util.Date strToDateUtil(String data_normal) {
        SimpleDateFormat formatoData = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date dt;
        try {
            dt = formatoData.parse(data_normal);
        } catch (ParseException ex) {
            dt = null;
        }
        return dt;
    }

    public static java.util.Date strToDateFormatado(String data, String formato) {
        SimpleDateFormat formatoData = new SimpleDateFormat(formato);
        java.util.Date dt;
        try {
            dt = formatoData.parse(data);
        } catch (ParseException ex) {
            dt = null;
        }
        return dt;
    }

    public static String dataBanco(String dtNormal) {
        if (dtNormal != null && !dtNormal.equals("")) {
            int b1 = dtNormal.indexOf("/");
            int b2 = dtNormal.indexOf("/", b1 + 1);

            String dtBanco = dtNormal.substring(b2 + 1, b2 + 5) + "-"
                    + dtNormal.substring(b1 + 1, b2) + "-" + dtNormal.substring(0, 2) + " ";

            if (dtNormal.length() > 10) {
                dtBanco += dtNormal.substring(b2 + 6);
            } else if (dtBanco.length() == 10) {
                dtBanco += "00:00:00";
            }

            // JOptionPane.showMessageDialog(null,dtBanco);
            return dtBanco;
        }
        return dtNormal;
    }

    public static String dataNormal(String dtBanco) {
        if (dtBanco != null && !dtBanco.equals("")) {
            int b1 = dtBanco.indexOf("-");
            int b2 = dtBanco.indexOf("-", b1 + 1);
            String dtNormal = "";
            if (dtBanco.length() > 10) {
                dtNormal = dtBanco.substring(b2 + 1, b2 + 3) + "/"
                        + dtBanco.substring(b1 + 1, b2) + "/" + dtBanco.substring(0, 4) + " "
                        + dtBanco.substring(b2 + 4, b2 + 12);
            } else if (dtBanco.length() == 10) {
                dtNormal = dtBanco.substring(b2 + 1, b2 + 3) + "/"
                        + dtBanco.substring(b1 + 1, b2) + "/" + dtBanco.substring(0, 4);

            }
            return dtNormal;
        }
        return dtBanco;

    }

    public static String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = (Date) new java.util.Date();
        return dateFormat.format(date);
    }

    public static String dataString(java.util.Date date) {
        if (date == null) {
            return "";
        }
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(date);
    }

    public static String horaString(java.util.Date date) {
        if (date == null) {
            return "";
        }
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        return dateFormat.format(date);
    }
    
    public static String dataHoraString(java.util.Date date) {
        if (date == null) {
            return "";
        }
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return dateFormat.format(date);
    }
    
    public static String converteData(String data) {
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

    public static boolean verificaData(String data) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date date = (Date) new java.util.Date();
        try {
            date = (Date) format.parse(data);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
