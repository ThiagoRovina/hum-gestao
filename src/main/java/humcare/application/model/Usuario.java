package humcare.application.model;

import humcare.usuario.funcao.FuncaoCdPerfil;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


/**
 *
 * @author alison
 */

@Getter
@Setter
public class Usuario implements Serializable {
    private Long id;
    private String nome;
    private String username;
    private String email;
    private FuncaoCdPerfil tpPermissaoAcesso;
    private Integer cdUsuario;
    private Integer nuMatricula;

}
