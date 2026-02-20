package humcare.higienizacao.model;


import humcare.application.model.BaseEntity;
import humcare.higienizacao.tipoHigienizacao.TipoHigienizacao;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name="BDC_HISTORICO_HIGIENIZACAO", schema="HUM")
public class Higienizacao extends BaseEntity {

    @Id
    @Column(name="CD_HISTORICO_HIGIENIZACAO")
    private Integer cdHigienizacao;

    @Column(name="CD_LEITO")
    private Integer cdLeito;

    @Column(name="CD_USUARIO_SOLICITACAO")
    private Integer cdUsuarioSolicitacao;

    @Transient // Não persistir este campo no banco de dados
    private String nmUsuarioSolicitacao;

    @Column(name="DH_SOLICITACAO")
    private Timestamp dhSolicitacao;

    @Column(name="TP_HIGIENIZACAO")
    private TipoHigienizacao tpHigienizacao;

    @Column(name= "CD_USUARIO_HIGIENIZACAO")
    private Integer cdUsuarioHigienizacao;

    @Transient // Não persistir este campo no banco de dados
    private String nmUsuarioHigienizacao;

    @Column(name="DH_INICIO")
    private Timestamp dhInicio;

    @Column(name="DH_FIM")
    private Timestamp dhFim;

    @Column(name="FL_ATIVO")
    private Boolean flAtivo;

    @Transient // Não persistir este campo no banco de dados
    private String duracaoMinutos;

    public String getDuracaoMinutos() {
        if (this.dhInicio == null || this.dhFim == null) {
            return null;
        }

        long duracaoMs = this.dhFim.getTime() - this.dhInicio.getTime();

        long horas = (duracaoMs / (1000 * 60 * 60));
        long minutos = (duracaoMs / (1000 * 60)) % 60;
        long segundos = (duracaoMs / 1000) % 60;

        return String.format("%02d:%02d:%02d", horas, minutos, segundos);
    }

}