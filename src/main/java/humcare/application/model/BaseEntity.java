package humcare.application.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    @Column(name = "FL_ATIVO")
    private Boolean flAtivo;

    @PrePersist
    protected void onCreate() {
        this.flAtivo = Boolean.TRUE;
    }
}
