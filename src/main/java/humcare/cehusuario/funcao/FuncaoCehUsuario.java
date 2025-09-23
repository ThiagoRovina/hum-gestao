package humcare.cehusuario.funcao;

import lombok.Getter;

public enum FuncaoCehUsuario {
    MEDICO(0),
    ENFERMEIRO(1),
    TEC_ENFERMAGEM(2),
    ADMINISTRATIVO(3);

    private final Integer valor;

    FuncaoCehUsuario(Integer i) { this.valor = i; }

    public static FuncaoCehUsuario getFuncaoCehUsuario(String tipo) {
        switch (tipo) {
            case "MEDICO":
                return FuncaoCehUsuario.MEDICO;
            case "ENFERMEIRO":
                return FuncaoCehUsuario.ENFERMEIRO;
            case "TEC_ENFERMAGEM":
                return FuncaoCehUsuario.TEC_ENFERMAGEM;
            case "ADMINISTRATIVO":
                return FuncaoCehUsuario.ADMINISTRATIVO;
            default:
                return null;
        }
    }
    public static FuncaoCehUsuario getFuncaoCehUsuarioInt(Integer tipo) {
        switch (tipo) {
            case 0:
                return FuncaoCehUsuario.MEDICO;
            case 1:
                return FuncaoCehUsuario.ENFERMEIRO;
            case 2:
                return FuncaoCehUsuario.TEC_ENFERMAGEM;
            case 3:
                return FuncaoCehUsuario.ADMINISTRATIVO;
            default:
                return null;
        }
    }
}
