package humcare.usuario.dao;

import humcare.usuario.model.CehUsuario;
import humcare.dao.GenericDAO;
import humcare.utilitarios.LdapUtil;
import org.hibernate.Session;

import javax.persistence.NoResultException;
import java.util.List;

public class CehUsuarioDAO extends GenericDAO<CehUsuario, Integer> {



    public CehUsuarioDAO() {
        super(CehUsuario.class);
    }

    public CehUsuario buscaPorEmail(String email) {
        Session conexao = openSession();
        try{
            return conexao
                    .createQuery("SELECT u FROM CehUsuario u WHERE u.deEmail = :email", CehUsuario.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            closeSession(conexao);
        }
    }




    public String buscaNomePorEmail(String email) {
        Session conexao = openSession();
        try{
            return conexao
                    .createQuery("SELECT u FROM CehUsuario u WHERE u.deEmail = :email", String.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            closeSession(conexao);
        }
    }
    //na verdade aqui ele deve buscar pela matricula
    public CehUsuario buscarPorId(Integer id) {
        Session conexao = openSession();
        try{
            return conexao
                    .createQuery("SELECT u FROM CehUsuario u WHERE u.cdPessoa = :id", CehUsuario.class)
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
            List<CehUsuario> usuarios = conexao.createQuery("SELECT u FROM CehUsuario u WHERE u.dePerfil = 2", CehUsuario.class).getResultList();
            return usuarios.isEmpty() ? null : usuarios.get(0);
        } catch (NoResultException e) {
            return null;
        } finally {
            closeSession(conexao);
        }
    }
}
