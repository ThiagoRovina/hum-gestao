package humcare.permissao.dao;

import humcare.dao.GenericDAO;
import org.hibernate.Session;
import humcare.permissao.model.Permissao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class PermissaoDAO extends GenericDAO<Permissao, Integer> {

    public PermissaoDAO() {
        super(Permissao.class);
    }

    public static List<Permissao> buscaPorTpPermissao(Integer tpPermissaoAcesso) {
        Session conexao = openSession();
        try{
            return conexao
                    .createQuery("SELECT u FROM Permissao u WHERE u.tpPermissaoAcesso = :tpPermissaoAcesso", Permissao.class)
                    .setParameter("tpPermissaoAcesso", tpPermissaoAcesso)
                    .getResultList();
        }catch (NoResultException nre){
            return new ArrayList<>();
        } finally {
            closeSession(conexao);
        }
    }
}
