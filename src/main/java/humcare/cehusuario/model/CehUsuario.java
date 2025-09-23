package humcare.cehusuario.model;

import humcare.application.model.BaseEntity;
import humcare.cehusuario.funcao.FuncaoCehUsuario;
import humcare.cehusuario.tipopermissao.TipoCehUsuario;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "CEH_USUARIO", schema = "CEH")
public class CehUsuario extends BaseEntity {

    @Id
    @Basic(optional = false)
    @Column(name = "CD_USUARIO")
    private Integer cdUsuario;

    @Basic(optional = false)
    @Column(name = "NU_MATRICULA")
    private Integer nuMatricula;

    @Column(name = "TP_PERMISSAO_ACESSO", length = 1)
    private TipoCehUsuario tpPermissaoAcesso;

    @Column(name = "LT_EMAIL", length = 50)
    private String ltEmail;

    @Column(name = "NM_USUARIO", length = 100)
    private String nmUsuario;

    @Column(name = "DE_FUNCAO", length = 1)
    private FuncaoCehUsuario deFuncao;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
    }
}
