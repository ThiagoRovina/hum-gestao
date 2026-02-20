package humcare.utilitarios;

/**
 *
 * @author equipe3
 */

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.jdbc.ReturningWork;
import org.hibernate.service.ServiceRegistry;

import java.sql.Connection;
import java.sql.SQLException;

public class HibernateUtil {

    private static SessionFactory sessionFactory = openSessionFactory();

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }

    public static Session open() {
        return HibernateUtil.getSessionFactory().openSession();
    }

    public static SessionFactory openSessionFactory() {
        try {
            Configuration configuration = new Configuration();
            configuration.configure();
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
                    configuration.getProperties()).build();
            SessionFactory factory = configuration.buildSessionFactory(serviceRegistry);

            return factory;

        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static void iniciaTransacao(Session session) {
        session.beginTransaction();

    }

    public static void commit(Session session) {
        if (session.getTransaction().isActive()) {
            session.getTransaction().commit();
        }
    }

    public static void rollback(Session session) {
        if (session.getTransaction().isActive()) {
            session.getTransaction().rollback();
        }
    }

    public static int executeSQL(Session session, String sqlDml) {

        int r = 0;

        iniciaTransacao(session);

        r = session.createSQLQuery(sqlDml).executeUpdate();

        commit(session);

        return r;

    }

    public static int executeSQLLote(Session session, String sqlDml) {

        int r = 0;

        r = session.createSQLQuery(sqlDml).executeUpdate();
        return r;

    }

    public static void close(Session session) {
        session.close();
    }

    public String getConnectionName() {

        String dbURL = "";
        Session sessao = null;

        try {
            sessao = HibernateUtil.getSessionFactory().getCurrentSession();
            HibernateUtil.iniciaTransacao(sessao);

            ReturningWork<String> getConName;
            getConName = new ReturningWork<String>() {

                @Override
                public String execute(Connection cnctn) throws SQLException {
                    return cnctn.getMetaData().getURL().toString();
                }

            };

            dbURL = sessao.doReturningWork(getConName);

        } finally {
            if (sessao != null && sessao.isOpen()) {
                sessao.close();
            }
        }

        return dbURL;
    }
}
