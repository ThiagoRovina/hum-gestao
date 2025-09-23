package humcare.permissao.model;

import humcare.application.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "CEH_PERMISSAO", schema = "CEH")
public class Permissao extends BaseEntity {

    @Id
    @Basic(optional = false)
    @Column(name = "CD_PERMISSAO", nullable = false)
    private Integer cdPermissao;

    @Basic(optional = false)
    @Column(name = "TP_PERMISSAO_ACESSO", length = 1, nullable = false)
    private Integer tpPermissaoAcesso;

    @Basic(optional = false)
    @Column(name = "DE_ARQUIVO", length = 50)
    private String deArquivo;

    @Basic(optional = false)
    @Column(name = "DE_MENU", length = 50)
    private String deMenu;

    @Basic(optional = false)
    @Column(name = "FL_INCLUIR", length = 1)
    private Boolean flIncluir;

    @Basic(optional = false)
    @Column(name = "FL_EXCLUIR", length = 1)
    private Boolean flExcluir;

    @Basic(optional = false)
    @Column(name = "FL_ALTERAR", length = 1)
    private Boolean flAlterar;

    @Basic(optional = false)
    @Column(name = "FL_CONSULTAR", length = 1)
    private Boolean flConsultar;
}
