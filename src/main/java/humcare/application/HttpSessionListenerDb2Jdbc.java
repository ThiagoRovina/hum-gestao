package humcare.application;

import javax.servlet.ServletContextEvent;

import humcare.dao.DAO;
import org.zkoss.zk.ui.http.HttpSessionListener;

/**
 * Avoid DB2 driver Classloader Memory leak on Tomcat application .war file
 * redeployment.
 * https://stackoverflow.com/questions/34212501/how-to-avoid-db2-driver-classloader-memory-leak-on-tomcat-application-war-file
 *
 * Quando se faz o deploy múltiplas vezes, por exemplo, clicando em "Run
 * Project" no Netbean múltiplas vezes sem antes dar um "Stop the Server" no
 * Tomcat, começa então aparecer erros no catalina.out com a mensagem
 * "java.lang.IllegalStateException: Illegal access: this web application
 * instance has been stopped already. Could not load
 * [com/ibm/db2/jcc/DB2JccConfiguration.properties]".
 *
 * Isso ocorre pois, ao iniciar a aplicação na primeira vez, o driver JDBC do
 * DB2 inicia algumas threads que ficam rodando no Tomcat. Ao fazer deploy
 * novamente sem parar o Tomcat, as Threads antigas não morrem e continuam sendo
 * executadam, porém elas acusam erro por não encontrar a referência ao programa
 * antigo que estava rodando anteriormente.
 *
 * Desse modo, esta classe extende de HttpSessionListener para que, quando a
 * sessão morrer então as Threads sejam encerradas.
 *
 * Caso a versão do driver DB2 seja alterado, é necessário rever a implementação
 * desta classe.
 *
 * @author alison
 */
public class HttpSessionListenerDb2Jdbc extends HttpSessionListener {

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        // This fixes the JDBC driver not unloading corectly on a context reload for DB2 JDBC
        try {
            System.out.println("Trying to stop the DB2 timer thread");

            new com.ibm.db2.jcc.am.lh() {
                // instance initializer to execute the fix when the anonymous class is instantiated, i.e. now
                {
                    if (a != null) {
                        a.cancel();
                    } else {
                        System.out.println("Timer is null, skipped");
                    }
                }
            };

            System.out.println("Stopped the timer");
        } catch (Exception e) {
            System.out.println("Could not stop the DB2 timer thread " + e.getMessage());
        }

        // This fixes persistent connections from old contexts 
        try {
            System.out.println("Trying to close all database connections");
            
//            HibernateUtil.shutdown();
            DAO.shutdown();

            System.out.println("All database connections was closed");
        } catch (Exception e) {
            System.out.println("Could not close all database connections " + e.getMessage());
        }
    }

}
