package humcare.higienizacao.dao;

import humcare.dao.GenericDAO;
import humcare.higienizacao.model.Higienizacao;
import org.hibernate.Session;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import java.util.List;

import static org.zkoss.zkplus.jpa.JpaUtil.getEntityManager;

public class HigienizacaoDAO extends GenericDAO<Higienizacao, Long> {
    public HigienizacaoDAO() {
        super(Higienizacao.class);
    }

    public Higienizacao buscarHigienizacaoAtivaPorLeito(Integer cdLeito) {
        try {
            String sql = "SELECT h FROM Higienizacao h WHERE h.cdLeito = :cdLeito AND h.flAtivo = true ORDER BY h.dhInicio DESC";
            TypedQuery<Higienizacao> query = getEntityManager().createQuery(sql, Higienizacao.class);
            query.setParameter("cdLeito", cdLeito);
            query.setMaxResults(1);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Higienizacao buscarHigienizacaoAtivaPorLeito(Session session, Integer cdLeito) {
        try {
            String sql = "SELECT h FROM Higienizacao h WHERE h.cdLeito = :cdLeito AND h.flAtivo = true ORDER BY h.dhInicio DESC";
            TypedQuery<Higienizacao> query = session.createQuery(sql, Higienizacao.class);
            query.setParameter("cdLeito", cdLeito);
            query.setMaxResults(1);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Higienizacao> listarHistorico() {
        return listar(" 1=1 ", " order by t.dhSolicitacao desc ");
    }
}
