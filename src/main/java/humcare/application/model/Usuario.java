package humcare.application.model;

import humcare.cehusuario.tipopermissao.TipoCehUsuario;

import java.io.Serializable;


/**
 *
 * @author alison
 */
public class Usuario implements Serializable {

    private Long id;
    private String nome;
    private String username;
    private String email;
    private TipoCehUsuario tpPermissaoAcesso;
    private Integer cdUsuario;
    private Integer nuMatricula;

    public Integer getCdUsuario() {
        return cdUsuario;
    }

    public void setCdUsuario(Integer cdUsuario) {
        this.cdUsuario = cdUsuario;
    }

    public Integer getNuMatricula() {
        return nuMatricula;
    }

    public void setNuMatricula(Integer nuMatricula) {
        this.nuMatricula = nuMatricula;
    }

    public TipoCehUsuario getTpPermissaoAcesso() {
        return tpPermissaoAcesso;
    }

    public void setTpPermissaoAcesso(TipoCehUsuario tpPermissaoAcesso) {
        this.tpPermissaoAcesso = tpPermissaoAcesso;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

   
}
