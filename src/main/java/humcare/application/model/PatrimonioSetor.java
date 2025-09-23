package humcare.application.model;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "CEH_PATRIMONIO_SETOR", schema = "CEH")
public class PatrimonioSetor implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "CD_PATRIMONIO")
    private Integer cdPatrimonio;

    @Basic(optional = false)
    @Column(name = "CD_PATRIMONIO_GPM")
    private BigInteger cdPatrimonioGPM;

    @Basic(optional = false)
    @Column(name = "DE_EQUIPAMENTO")
    private String deEquipamento;

    @Basic(optional = false)
    @Column(name = "TP_AQUISICAO")
    private String tipoAquisicao;

    @Basic(optional = false)
    @Column(name = "NM_MARCA")
    private String nmMarca;

    @Basic(optional = false)
    @Column(name = "NM_MODELO")
    private String nmModelo;

    @Basic(optional = false)
    @Column(name = "NU_SERIE")
    private String nuSerie;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
    }
}