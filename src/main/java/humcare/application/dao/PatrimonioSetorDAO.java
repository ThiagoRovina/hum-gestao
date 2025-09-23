package humcare.application.dao;

import humcare.application.model.PatrimonioSetor;
import humcare.dao.GenericDAO;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.Session;

import javax.persistence.Query;

public class PatrimonioSetorDAO extends GenericDAO<PatrimonioSetor, Integer> {

    public PatrimonioSetorDAO() {
        super(PatrimonioSetor.class);
    }

    public PatrimonioSetor listarEquipamentosByCdPatrimonio(Integer cdPatrimonio) {
//        Session conexao = openSession();
//        try {
//            Query query = conexao.createQuery("SELECT ps FROM PatrimonioSetor ps WHERE cdPatrimonio = " + cdPatrimonio, PatrimonioSetor.class);
//
//            return (PatrimonioSetor) query.getSingleResult();
//        } catch (Exception e) {
//            System.out.println(ExceptionUtils.getStackTrace(e));
//            return null;
//        } finally {
//            closeSession(conexao);
//        }

        return null;

    }
}
