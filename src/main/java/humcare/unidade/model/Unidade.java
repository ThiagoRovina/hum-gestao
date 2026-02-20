package humcare.unidade.model;

import humcare.application.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "BDC_UNIDADE", schema= "HUM")
public class Unidade extends BaseEntity{

    @Id
    @Column(name = "CD_UNIDADE")
    private Integer cdUnidade;

    @Column(name = "DE_UNIDADE")
    private String deUnidade;

    @Column(name= "FL_ATIVO")
    private Boolean flAtivo;

}
