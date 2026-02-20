package humcare.leitos.dto;

import humcare.leitos.status.StatusLeito;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LeitosListDTO implements Serializable {

    private Integer cdLeito;
    private Integer cdUnidade;
    private String idLeito;
    private StatusLeito stLeito;
    private Boolean flAtivo;
    private String deUnidade;

    // Ajuste o construtor para bater com o "Expected arguments" do erro:
    public LeitosListDTO(String deUnidade, int cdLeito, String idLeito, StatusLeito stLeito, boolean flAtivo) {
        this.deUnidade = deUnidade;
        this.cdLeito = cdLeito;
        this.idLeito = idLeito;
        this.stLeito = stLeito;
        this.flAtivo = flAtivo;
    }
}
