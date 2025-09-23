package humcare.cehusuario.dao;

import humcare.cehusuario.model.CehUsuario;
import humcare.dao.GenericDAO;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.Session;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

public class CehUsuarioDAO extends GenericDAO<CehUsuario, Integer> {
    public CehUsuarioDAO() {
        super(CehUsuario.class);
    }

    public CehUsuario buscaPorEmail(String email) {
        Session conexao = openSession();
        try{
            return conexao
                    .createQuery("SELECT u FROM CehUsuario u WHERE u.ltEmail = :email", CehUsuario.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            closeSession(conexao);
        }
    }

    public CehUsuario buscarPorId(Integer id) {
        Session conexao = openSession();
        try{
            return conexao
                    .createQuery("SELECT u FROM CehUsuario u WHERE u.cdUsuario = :id", CehUsuario.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            closeSession(conexao);
        }
    }

    public CehUsuario buscarAdministrador() {
        Session conexao = openSession();
        try{
            List<CehUsuario> usuarios = conexao.createQuery("SELECT u FROM CehUsuario u WHERE u.tpPermissaoAcesso = 1", CehUsuario.class).getResultList();
            return usuarios.isEmpty() ? null : usuarios.get(0);
        } catch (NoResultException e) {
            return null;
        } finally {
            closeSession(conexao);
        }
    }

//    public List<CehUsuario> listarTodos(String filtro, String ordem) {
//        Session conexao = openSession();
//        try {
//            Query query = conexao.createQuery("from CehUsuario t where " + filtro + " " + ordem, CehUsuario.class);
//
//            return query.getResultList();
//        } catch (Exception e) {
//            System.out.println(ExceptionUtils.getStackTrace(e));
//            return null;
//        } finally {
//            closeSession(conexao);
//        }
//    }
}
