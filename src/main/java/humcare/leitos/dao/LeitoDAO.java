package humcare.leitos.dao;

import humcare.application.model.Usuario;
import humcare.dao.GenericDAO;
import humcare.leitos.dto.LeitosHigienizacaoListDTO;
import humcare.leitos.dto.LeitosListDTO;
import humcare.leitos.model.Leitos;
import humcare.usuario.funcao.FuncaoCdPerfil;
import humcare.utilitarios.SQLUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LeitoDAO extends GenericDAO<Leitos, Integer> {
    private SQLUtil sqlUt = new SQLUtil();
    private Usuario usuarioLogado;
    public LeitoDAO(){super(Leitos.class);}

    public List<LeitosListDTO> listarLeitosUnidade(String filtro, String ordem) {
        Session conexao = openSession();
        try {
            // Você deve usar o caminho completo da classe DTO
            String hql = "SELECT NEW humcare.leitos.dto.LeitosListDTO( " +
                    "   u.deUnidade, t.cdLeito, t.idLeito, t.stLeito, t.flAtivo" +
                    ") " +
                    " FROM Leitos t " +
                    " INNER JOIN Unidade u " +
                    "   ON u.cdUnidade = t.cdUnidade " +
                    " WHERE " + filtro + " " + ordem;

            Query<LeitosListDTO> query = conexao.createQuery(hql, LeitosListDTO.class);
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        } finally {
            closeSession(conexao);
        }
    }

    public List<LeitosHigienizacaoListDTO> listarLeitosHigienizacao() {
        Session conexao = openSession();
        try {
            String sql = "SELECT L.CD_LEITO, L.CD_UNIDADE, L.ID_LEITO, L.ST_LEITO, L.FL_ATIVO " +
                    "   , (SELECT U.TP_HIGIENIZACAO FROM HUM.BDC_HISTORICO_HIGIENIZACAO U WHERE U.CD_LEITO = L.CD_LEITO AND U.FL_ATIVO = 1 FETCH FIRST 1 ROW ONLY) " +
                    " FROM HUM.BDC_LEITO L " +
                    " WHERE 1=1  " +
                    "   AND L.FL_ATIVO = 1 " +
                    "   AND L.ST_LEITO IN (0,2) " +
                    "ORDER BY L.CD_LEITO ";

            return conexao.createNativeQuery(sql, LeitosHigienizacaoListDTO.class).getResultList();
        } catch (NoResultException e) {
            return null;
        } finally {
            closeSession(conexao);
        }
    }


//    public List<LeitoDTO> listar(String filtro, String ordem, Usuario profissional) {
//        boolean isAdmin = usuarioLogado.getTpPermissaoAcesso() == FuncaoCdPerfil.ADMINISTRADOR;
//
//        List<LeitoDTO> leitos  = new ArrayList<>();
//
//        StringBuilder strSQL = new StringBuilder();
//        strSQL.append("SELECT \n" +
//                "    l.CD_LEITO, \n" +
//                "    l.CD_QUARTO, \n" +
//                "    l.ID_LEITO, \n" +
//                "    q.ID_QUARTO,                 \n" +
//                "    l.FL_ATIVO, \n" +
//                "    un.NM_UNIDADE,     \n" +
//                "    h.NM_HOSPITAL, \n" +
//                "    lb.ST_LEITO_BMR, \n" +
//                "    b.DE_TIPO_BMR, \n" +
//                "    b.DE_SIMPLIFICADA, \n" +
//                "    b.LT_CLASSIFICACAO, \n" +
//                "    b.LT_HEX_COR, \n" +
//                "    b.LT_SIMBOLO \n" +
//                "FROM \n" +
//                "    MCI.MCI_LEITO l \n" +
//                "    INNER JOIN MCI.MCI_QUARTO q \n" +
//                "        ON q.CD_QUARTO = l.CD_QUARTO \n" +
//                "    INNER JOIN MCI.MCI_UNIDADE_HOSPITALAR un \n" +
//                "        ON un.CD_UNIDADE_HOSPITALAR = q.CD_UNIDADE_HOSPITALAR \n" +
//                "    INNER JOIN MCI.MCI_HOSPITAL h \n" +
//                "        ON h.CD_HOSPITAL = un.CD_HOSPITAL \n" +
//                "    LEFT JOIN (\n" +
//                "        SELECT lb1.*\n" +
//                "        FROM MCI.MCI_LEITO_BMR lb1\n" +
//                "        WHERE lb1.DT_CADASTRO = (\n" +
//                "            SELECT MAX(lb2.DT_CADASTRO)\n" +
//                "            FROM MCI.MCI_LEITO_BMR lb2\n" +
//                "            WHERE lb2.CD_LEITO = lb1.CD_LEITO\n" +
//                "        )\n" +
//                "    ) lb ON lb.CD_LEITO = l.CD_LEITO \n" +
//                "    LEFT JOIN MCI.MCI_TIPO_BMR b \n" +
//                "        ON b.CD_TIPO_BMR = lb.CD_TIPO_BMR \n");
//
//        if (!isAdmin && profissional.getId() != null) {
//            strSQL.append("INNER JOIN MCI.MCI_UNIDADE_PROFISSIONAL up \n" +
//                    "    ON up.CD_UNIDADE_HOSPITALAR = un.CD_UNIDADE_HOSPITALAR \n" +
//                    "WHERE up.CD_PROFISSIONAL = " + profissional.getId() + " AND " + filtro + " " + ordem);
//        } else {
//            strSQL.append("WHERE " + filtro + " " + ordem);
//        }
//
//        List candidatosList = sqlUt.pesquisar(strSQL.toString());
//
//        if (candidatosList != null && !candidatosList.isEmpty()) {
//            for (Iterator it = candidatosList.iterator(); it.hasNext();) {
//                Object[] obj = (Object[]) it.next();
//
//                LeitoDTO leito = new LeitoDTO();
//                leito.setCdLeito(obj[0] != null ? Integer.valueOf(obj[0].toString()) : null);
//                leito.setCdQuarto(obj[1] != null ? Integer.valueOf(obj[1].toString()) : null);
//                leito.setIdLeito(obj[2] != null ? obj[2].toString() : null);
//                leito.setIdQuarto(obj[3] != null ? obj[3].toString() : null);
//                leito.setFlAtivo(obj[4] != null ? Integer.valueOf(obj[4].toString()) : null);
//                leito.setNmUnidade(obj[5] != null ? obj[5].toString() : null);
//                leito.setNmHospital(obj[6] != null ? obj[6].toString() : null);
//
//                leito.setStLeitoBmr(obj[7] != null ? Integer.valueOf(obj[7].toString()) : null);
//
//                leito.setDeTipoBmr(obj[8] != null ? obj[8].toString() : null);
//                leito.setDeSimplificada(obj[9] != null ? obj[9].toString() : null);
//                leito.setLtClassificacao(obj[10] != null ? obj[10].toString() : null);
//                leito.setLtHexCor(obj[11] != null ? obj[11].toString() : null);
//                leito.setLtSimbolo(obj[12] != null ? obj[12].toString() : null);
//                leitos.add(leito);
//            }
//        }
//        return leitos;
//    }


}
