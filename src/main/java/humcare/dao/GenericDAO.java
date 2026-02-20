package humcare.dao;

import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.Id;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

public abstract class GenericDAO<T, I extends Serializable> extends DAO {

    private final Class<T> classe;

    public GenericDAO(Class<T> classe) {
        this.classe = classe;
    }

    public Class<T> getClasse() {
        return classe;
    }

    public I incluirAutoincrementando(T entidade) {
        I id = null;
        Session session = null;
        try {
            session = openSession();
            beginTransaction(session);
            autoIncrementarId(session, entidade);
            id = incluir(session, entidade);
            commit(session);
        } catch (Exception e) {
            printException(e);
            rollback(session);
        } finally {
            closeSession(session);
        }
        return id;
    }

    public I incluirAutoincrementando(Session session, T entidade) throws Exception {
        autoIncrementarId(session, entidade);
        return incluir(session, entidade);
    }

    public I incluir(T entidade) {
        I id = null;
        Session session = null;
        try {
            session = openSession();
            beginTransaction(session);
            id = incluir(session, entidade);
            commit(session);
        } catch (Exception e) {
            printException(e);
            rollback(session);
        } finally {
            closeSession(session);
        }
        return id;
    }

    public I incluir(Session session, T entidade) {
        I idEntidade = (I) session.save(entidade);
        session.flush();
        session.refresh(entidade);
        return idEntidade;
    }

    public void autoIncrementarId(Session session, T entidade) throws Exception {
        Field fieldId = getIdField(entidade);
        if (fieldId != null) {
            Query q = session.createQuery("select coalesce(max(" + fieldId.getName() + "), 0) + 1 as id from " + entidade.getClass().getName());
            I idAutoincremento = (I) q.uniqueResult();
            fieldId.set(entidade, idAutoincremento);
        }
    }

    private Field getIdField(T entidade) {
        Field fieldId = null;
        for (Field field : entidade.getClass().getDeclaredFields()) {
            if (field.getAnnotation(Id.class) != null) {
                field.setAccessible(true);
                fieldId = field;
                break;
            }
        }
        return fieldId;
    }

    public boolean excluir(T entidade) {
        boolean excluded = false;
        Session session = null;
        try {
            session = openSession();
            beginTransaction(session);
            excluir(session, entidade);
            commit(session);
            excluded = true;
        } catch (Exception e) {
            printException(e);
            rollback(session);
        } finally {
            closeSession(session);
        }
        return excluded;
    }

    public void excluir(Session session, T entidade) {
        session.delete(entidade);
        session.flush();
    }

    public boolean excluir(I codigo) {
        boolean excluded = false;
        Session session = null;
        try {
            session = openSession();
            beginTransaction(session);
            excluir(session, codigo);
            commit(session);
            excluded = true;
        } catch (Exception e) {
            printException(e);
            rollback(session);
        } finally {
            closeSession(session);
        }
        return excluded;
    }

    public void excluir(Session session, I codigo) {
        T genericClass = (T) session.get(classe, codigo);
        if (genericClass.getClass().getName() != null) {
            session.delete(genericClass);
        }
    }

    public boolean atualizar(T entidade) {
        boolean updated = false;
        Session session = null;
        try {
            session = openSession();
            beginTransaction(session);
            atualizar(session, entidade);
            commit(session);
            updated = true;
        } catch (Exception e) {
            printException(e);
            rollback(session);
        } finally {
            closeSession(session);
        }
        return updated;
    }

    public void atualizar(Session session, T entidade) {
        session.update(entidade);
        session.flush();
        session.refresh(entidade);
    }

    public T buscar(I codigo) {
        T object = null;
        Session session = null;
        try {
            session = openSession();
            object = (T) session.get(classe, codigo);
        } catch (Exception e) {
            printException(e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return object;
    }

    public List<T> listar() {
        List<T> objs = null;
        Session session = null;
        try {
            session = openSession();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<T> query = builder.createQuery(classe);
            Root<T> variableRoot = query.from(classe);
            query.select(variableRoot);

            objs = session.createQuery(query).getResultList();
        } catch (Exception e) {
            printException(e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return objs;
    }

    public List<T> listar(String filtro, String ordem) {
        List<T> objs = null;
        Session session = null;
        try {
            session = openSession();
            Query query = session.createQuery("from " + classe.getSimpleName() + " t where " + filtro + " " + ordem);

            printQuery(query);

            objs = query.list();
        } catch (Exception e) {
            printException(e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return objs;
    }

    public List<T> listarPaginado(String filtro, String ordem, int page, int pageSize) {
        List<T> objs = null;
        Session session = null;
        try {
            session = openSession();
            Query query = session.createQuery("from " + classe.getSimpleName() + " t where " + filtro + " " + ordem);
            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);

            printQuery(query);

            objs = query.list();
        } catch (Exception e) {
            printException(e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return objs;
    }

    public int contar(String filtro) {
        int count = 0;
        Session session = null;
        try {
            session = openSession();
            Query query = session.createQuery("select count(*) from " + classe.getSimpleName() + " t where " + filtro);

            printQuery(query);

            count = ((Long) query.uniqueResult()).intValue();
        } catch (Exception e) {
            printException(e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return count;
    }

    public List<Object[]> executarSelectHql(String hql) {
        List<Object[]> objs = null;
        Session session = null;
        try {
            session = openSession();
            Query query = session.createQuery(hql);

            printQuery(query);

            objs = query.list();
        } catch (Exception e) {
            printException(e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return objs;
    }

    public List<Object[]> executarSelectSql(String sql) {
        List<Object[]> objs = null;
        Session session = null;
        try {
            session = openSession();
            Query query = session.createNativeQuery(sql);
            objs = query.list();
        } catch (Exception e) {
            printException(e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return objs;
    }

    public int executarUpdateSql(String sql) {
        int result = 0;
        Session session = null;
        try {
            session = openSession();
            beginTransaction(session);
            result = executarUpdateSql(session, sql);
            commit(session);
        } catch (Exception e) {
            printException(e);
            rollback(session);
        } finally {
            closeSession(session);
        }
        return result;
    }

    public int executarUpdateSql(Session session, String sql) {
        int result = 0;
        try {
            result = session.createNativeQuery(sql).executeUpdate();
        } catch (Exception e) {
            printException(e);
        }
        return result;
    }

}
