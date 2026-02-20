package humcare.leitos.status;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum StatusLeito {

    LIVRE(0, "Livre"),
    OCUPADO(1, "Ocupado"),
    HIGIENIZANDO(2, "Em Higienização"),
    PRERESERVADO(3, "Pré-Reservado"),
    AGUARDANDO(4, "Aguardando Liberação"),
    DESATIVADO(5, "Desativado");

    private final int valor;
    private final String descricao;

    StatusLeito(int valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return this.descricao;
    }

    public static StatusLeito fromValor(Integer valor) {
        if (valor == null) {
            return null;
        }
        for (StatusLeito status : StatusLeito.values()) {
            if (status.getValor() == valor) {
                return status;
            }
        }
        return null;
    }


    public static StatusLeito fromString(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return null;
        }
        try {
            return StatusLeito.valueOf(nome.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public List<StatusLeito> getProximosStatusPossiveis() {
        switch (this) {
            case LIVRE:
                return Arrays.asList(OCUPADO, PRERESERVADO, DESATIVADO, LIVRE);
            case OCUPADO:
                return Arrays.asList(LIVRE, OCUPADO);
            case PRERESERVADO:
                return Arrays.asList(LIVRE, OCUPADO, PRERESERVADO);
            case DESATIVADO:
                return Arrays.asList(LIVRE, DESATIVADO);
            default:
                return Arrays.asList(StatusLeito.values());
        }
    }
}