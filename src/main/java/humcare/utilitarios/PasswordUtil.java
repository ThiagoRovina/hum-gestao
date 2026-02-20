package humcare.utilitarios;
import org.springframework.security.crypto.bcrypt.BCrypt;
public class PasswordUtil {
    /**
     * Gera um hash seguro da senha (a ser usado no cadastro de credenciados).
     * @param senhaPura A senha digitada pelo usuário.
     * @return Uma string com o hash da senha, pronta para ser salva no banco.
     */
    public static String hashPassword(String senhaPura) {
        // O gensalt() gera um "sal" aleatório para cada senha, tornando o hash único e seguro.
        return BCrypt.hashpw(senhaPura, BCrypt.gensalt(12));
    }

    /**
     * Verifica se a senha digitada corresponde ao hash salvo no banco.
     * @param senhaPura A senha digitada pelo usuário no login.
     * @param hashSalvo O hash que está salvo no banco de dados.
     * @return true se a senha corresponde, false caso contrário.
     */
    public static boolean checkPassword(String senhaPura, String hashSalvo) {
        if (senhaPura == null || hashSalvo == null) {
            return false;
        }
        return BCrypt.checkpw(senhaPura, hashSalvo);
    }
}
