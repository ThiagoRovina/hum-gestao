package humcare.usuario.model;

import com.ibm.db2.cmx.annotation.Required;
import humcare.application.model.BaseEntity;
import humcare.application.model.Usuario;
import humcare.usuario.funcao.FuncaoCdPerfil;
import humcare.usuario.tipopermissao.TipoPermissaoUsuario;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "BDC_PESSOA", schema = "HUM")
public class CehUsuario extends BaseEntity {

    @Id
    @Column(name= "CD_PESSOA")
    private Integer cdPessoa;

    @Column(name= "NM_PESSOA")
    private String nmPessoa;

    @Column(name="NU_CPF")
    private String nuCpf;

    @Column(name="NU_FONE")
    private String nuFone;

    @Column(name="DE_EMAIL")
    private String deEmail;

    @Column(name="DE_PERFIL")
    private FuncaoCdPerfil dePerfil;

    @Column(name="DE__SENHA", nullable = true)
    private String deSenha;

    @Column(name="FL_ATIVO")
    private Boolean flAtivo;


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
    }
}
