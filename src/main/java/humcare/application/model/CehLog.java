package humcare.application.model;

import humcare.application.tipoocorrencia.TipoOcorrenciaLog;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "CEH_LOG", schema = "CEH")
public class CehLog implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "CD_LOG")
    private Integer cdLog;

    @Basic(optional = false)
    @Column(name = "NM_TABELA", length = 50)
    private String nmTabela;

    @Basic(optional = false)
    @Column(name = "CD_USUARIO", length = 50)
    private Integer cdUsuario;

    @Basic(optional = false)
    @Column(name = "DH_OCORRENCIA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dhOcorrencia;

    @Basic(optional = false)
    @Column(name = "TP_OCORRENCIA")
    private TipoOcorrenciaLog tpOcorrencia;
}
