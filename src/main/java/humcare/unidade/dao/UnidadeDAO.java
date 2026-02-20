package humcare.unidade.dao;

import humcare.dao.GenericDAO;
import humcare.unidade.model.Unidade;
import org.hibernate.Session;

import javax.persistence.NoResultException;

public class UnidadeDAO extends GenericDAO<Unidade, Integer> {
    public UnidadeDAO(){super(Unidade.class);}

    public Unidade buscaPorIdUnidade(Integer cdUnidade) {
        Session conexao = openSession();
        try{
            return conexao
                    .createQuery("SELECT u FROM Unidade u WHERE u.cdUnidade = :cdUnidade", Unidade.class)
                    .setParameter("cdUnidade", cdUnidade)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            closeSession(conexao);
        }
    }

}
