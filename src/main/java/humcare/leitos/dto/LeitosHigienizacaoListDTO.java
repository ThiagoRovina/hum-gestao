package humcare.leitos.dto;

import humcare.higienizacao.tipoHigienizacao.TipoHigienizacao;
import humcare.leitos.status.StatusLeito;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.http.annotation.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@Entity
@Immutable
public class LeitosHigienizacaoListDTO implements Serializable {

    @Id
    @Column(name = "CD_LEITO")
    private Integer cdLeito;

    @Column(name = "CD_UNIDADE")
    private Integer cdUnidade;

    @Column(name = "ID_LEITO")
    private String idLeito;

    @Column(name = "ST_LEITO")
    private Integer stLeitoValor;

    @Column(name = "FL_ATIVO")
    private Boolean flAtivo;

    @Column(name = "TP_HIGIENIZACAO")
    private Integer tpHigienizacaoValor;

    public StatusLeito getStLeito() {
        return StatusLeito.fromValor(this.stLeitoValor);
    }
    
    public TipoHigienizacao getTpHigienizacao() {
        // Se o valor do banco for nulo, retorne null imediatamente
        if (this.tpHigienizacaoValor == null) {
            return null;
        }
        return TipoHigienizacao.getTipoHigienizacaoInt(this.tpHigienizacaoValor);
    }
}
