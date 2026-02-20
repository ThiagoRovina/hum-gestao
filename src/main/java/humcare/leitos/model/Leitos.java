package humcare.leitos.model;

import humcare.application.model.BaseEntity;
import humcare.leitos.status.StatusLeito;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "BDC_LEITO", schema= "HUM")
public class Leitos extends BaseEntity {
    @Id
    @Column(name = "CD_LEITO")
    private Integer cdLeito;

    @Column(name = "CD_UNIDADE")
    private Integer cdUnidade;

    @Column(name = "ID_LEITO")
    private String idLeito;

    @Column(name = "ST_LEITO")
    private StatusLeito stLeito;

    @Column(name= "FL_ATIVO")
    private Boolean flAtivo;
}
