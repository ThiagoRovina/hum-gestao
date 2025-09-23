package humcare.cehusuario.tipopermissao;

import lombok.Getter;

@Getter
public enum TipoCehUsuario {
    INVENTARIANTE(0),
    ADMINISTRADOR(1),
    COORDENADOR(2),
    SOLICITANTE(3),
    OPERADOR(4);

    private final Integer valor;

    TipoCehUsuario(Integer i) { this.valor = i; }

    public static TipoCehUsuario getTipoCehUsuario(String tipo) {
        switch (tipo) {
            case "INVENTARIANTE":
                return TipoCehUsuario.INVENTARIANTE;
            case "ADMINISTRADOR":
                return TipoCehUsuario.ADMINISTRADOR;
            case "COORDENADOR":
                return TipoCehUsuario.COORDENADOR;
            case "SOLICITANTE":
                return TipoCehUsuario.SOLICITANTE;
            case "OPERADOR":
                return TipoCehUsuario.OPERADOR;
            default:
                return null;
        }
    }

    public static TipoCehUsuario getTipoCehUsuarioInt(Integer tipo) {
        switch (tipo) {
            case 0:
                return TipoCehUsuario.INVENTARIANTE;
            case 1:
                return TipoCehUsuario.ADMINISTRADOR;
            case 2:
                return TipoCehUsuario.COORDENADOR;
            case 3:
                return TipoCehUsuario.SOLICITANTE;
            case 4:
                return TipoCehUsuario.OPERADOR;
            default:
                return null;
        }
    }

    public static Integer getTipoCehUsuariotoInt(String tipo) {
        switch (tipo) {
            case "INVENTARIANTE":
                return 0;
            case "ADMINISTRADOR":
                return 1;
            case "COORDENADOR":
                return 2;
            case "SOLICITANTE":
                return 3;
            case "OPERADOR":
                return 4;
            default:
                return null;
        }
    }
}