package humcare.utilitarios;

import org.hibernate.Session;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author equipes
 */
public class SQLUtil {

    public SQLUtil() {
    }

    public boolean executarSQL(String sql) {

        Session session = HibernateUtil.open();
        boolean r = false;
        try {
            int ex = HibernateUtil.executeSQL(session, sql);

            HibernateUtil.close(session);

            if (ex > 0) {
                r = true;
            }
            return r;

        } catch (Exception e) {
            HibernateUtil.close(session);
            return false;
        }


    }

    public boolean executarSQL(String[] arraysql, int nu_lote) {

        Session session = HibernateUtil.open();
        boolean r = false;
        int ex = 0;
        try {

            for (int i = 0; i < nu_lote; i++) {
                String sql = arraysql[i];
                ex = HibernateUtil.executeSQLLote(session, sql);
            }

            HibernateUtil.close(session);

            if (ex > 0) {
                r = true;
            }
            return r;

        } catch (Exception e) {
            HibernateUtil.close(session);
            return false;
        }


    }

    public boolean executarSQLLote(Session session, String sql) {
        boolean r = false;
        try {
            int ex = HibernateUtil.executeSQLLote(session, sql);
            if (ex > 0) {
                r = true;
            }

            return r;
        } catch (Exception e) {
            return false;
        }

    }

    public String gerarInsertSQL(String tabela, List campos, List valores, String campoAuto) {

        //VARIAVEIS BASICAS PARA GERAÇÃO DO SQL
        String vsql = "insert into " + tabela + "(";
        String cam = "";
        String val = "";
        String virgula = "";
        long valorAuto = 0;

        //ATRIBUIÇÃO DO CAMPO AUTOINCREMENTO
        if (campoAuto != null && !campoAuto.equals("")) {
            valorAuto = autoIncremento(tabela, campoAuto);
            if (valorAuto > 0) {
                cam = campoAuto + ",";
                val = Long.toString(valorAuto) + ",";

            } else {
                cam = "";
                val = "";

            }
        } else {
            cam = "";
            val = "";

        }


        //LAÇÃO PARA COMPOSIÇÃO DOS CAMPOS
        for (Iterator i = campos.iterator(); i.hasNext();) {
            cam += virgula + i.next().toString();
            virgula = ",";

        }

        virgula = "";
        //LAÇÃO PARA COMPOSIÇÃO DOS VALORES
        for (Iterator i = valores.iterator(); i.hasNext();) {
            val += virgula + i.next().toString();
            virgula = ",";


        }

        vsql += cam + ") " + " values (" + val + ")";
        return vsql;

    }

    public String gerarUpdateSQL(String tabela, List campos, List valores, String condicao) {

        //VARIAVEIS BASICAS PARA GERAÇÃO DO SQL
        String vsql = "update " + tabela + " set ";
        String cam = "";
        String val = "";
        String virgula = "";
        long valorAuto = 0;


        //Laço para composição dos campos
        Iterator j = valores.iterator();
        for (Iterator i = campos.iterator(); i.hasNext();) {

            cam += virgula + i.next().toString();
            cam += " = " + j.next().toString();
            virgula = ",";

        }

        vsql += cam + " where " + condicao;
        return vsql;

    }

    public long autoIncremento(String tabela, String campoAuto) {
        long r = 0;
        String vsql = "select max(" + campoAuto + ") + 1 as id from " + tabela;
        try {
            r = Long.parseLong(resultado(pesquisar(vsql)));
        } catch (Exception e) {
            r = 0;
        }

        return r;
    }

    public List pesquisar(String sql) {

        StringBuilder bsql = new StringBuilder();

        bsql.append(sql);
        bsql.append(" for read only with UR");

        Session session = HibernateUtil.open();
        try {
            List query = session.createSQLQuery(bsql.toString()).list();
            HibernateUtil.close(session);
            return query;

        } catch (Exception e) {
            //System.out.println(e);
            HibernateUtil.close(session);
            return null;
        }

    }

    public List pesquisar(String sql, Session session) {
        StringBuilder bsql = new StringBuilder();
        bsql.append(sql);
        bsql.append(" for read only with UR");
        try {
            List query = session.createSQLQuery(bsql.toString()).list();
            return query;

        } catch (Exception e) {
            return null;
        }
    }

    public String[][] pesquisarArray(List query, String[][] itens) {

        int j = 1;
        for (Iterator i = query.iterator(); i.hasNext();) {
            Object[] obj = (Object[]) i.next();
            int t = obj.length;
            for (int k = 0; k < t; k++) {
                itens[k][j] = obj[k].toString();
            }
            j++;
        }

        return itens;
    }

    public double[] pesquisarArrayDouble(List query) {

        double[] itens = new double[query.size()];

        int j = 0;
        for (Iterator i = query.iterator(); i.hasNext();) {
            Object obj = (Object) i.next();

            itens[j] = Double.parseDouble(obj.toString());

            j++;
        }

        return itens;
    }

    public ArrayList itensSelect(List query) {

        ArrayList itens = new ArrayList();
        itens.add("<option value=0>Nenhum</option>");

        String s = "";
        for (Iterator i = query.iterator(); i.hasNext();) {
            Object[] obj = (Object[]) i.next();

            s = "<option value=" + obj[0].toString() + ">";
            s += obj[1].toString() + "</option>";
            itens.add(s);


        }
        return itens;
    }

    public ArrayList itensCombo(List query) {

        ArrayList itens = new ArrayList();
        itens.add("0;Selecione");

        String s = "";

        for (Iterator i = query.iterator(); i.hasNext();) {
            Object[] obj = (Object[]) i.next();
            s = obj[0].toString() + ";";
            s += obj[1].toString().trim();
            itens.add(s);
        }

        return itens;
    }

    public String resultado(List query) {
        String s = "";
        try {
            int t = query.size();
            for (Iterator i = query.iterator(); i.hasNext();) {
                s = i.next().toString();
            }
        } catch (Exception ex) {
            s = "";
        }

        return s;

    }

    public Object resultadoObj(List query) {
        Object s = null;
        try {
            int t = query.size();
            for (Iterator i = query.iterator(); i.hasNext();) {
                s = i.next();
            }
        } catch (Exception ex) {
            s = null;
        }

        return s;

    }

    public Object[] resultadoObj2(List query) {
        Object[] s = null;
        int j = -1;
        try {
            int t = query.size();

            for (Iterator i = query.iterator(); i.hasNext();) {
                Object[] obj = (Object[]) i.next();
                j++;
                s[j] = obj;

            }

        } catch (Exception ex) {
            s[j] = null;
        }

        return s;

    }

    public long recordCount(List query) {
        return query.size();
    }

    public long incremento(String schema, String tabela, String campo_chave) {


        String sql = "select max(" + campo_chave + ") +1 as id from " + schema + "." + tabela;
        SQLUtil sqlu = new SQLUtil();
        List query = sqlu.pesquisar(sql);
        String s = sqlu.resultado(sqlu.pesquisar(sql));
        if (s.equals("")) {
            s = "1";
        }
        return Long.parseLong(s);

    }

    public long incrementoCampoString(String schema, String tabela, String campo_chave) {


        String sql = "select max(int(" + campo_chave + ")) +1 as id from " + schema + "." + tabela;
        SQLUtil sqlu = new SQLUtil();
        List query = sqlu.pesquisar(sql);
        String s = sqlu.resultado(sqlu.pesquisar(sql));
        if (s.equals("")) {
            s = "1";
        }
        return Long.parseLong(s);

    }
}