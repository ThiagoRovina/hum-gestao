package humcare.usuario.tipopermissao;

import lombok.Getter;

@Getter
public enum TipoPermissaoUsuario {
    EQUIPE(0),
    INVENTARIANTE(1),
    ADMINISTRADOR(2);

    private final Integer valor;

    TipoPermissaoUsuario(Integer i) { this.valor = i; }

    public static TipoPermissaoUsuario getTipoUsuario(String tipo) {
        switch (tipo) {
            case "EQUIPE":
                return TipoPermissaoUsuario.EQUIPE;
            case "INVENTARIANTE":
                return TipoPermissaoUsuario.INVENTARIANTE;
            case "ADMINISTRADOR":
                return TipoPermissaoUsuario.ADMINISTRADOR;
            default:
                return null;
        }
    }

    public static TipoPermissaoUsuario getTipoPermissaoUsuarioInt(Integer tipo) {
        switch (tipo) {
            case 0:
                return TipoPermissaoUsuario.EQUIPE;
            case 1:
                return TipoPermissaoUsuario.ADMINISTRADOR;
            case 2:
                return TipoPermissaoUsuario.INVENTARIANTE;
            default:
                return null;
        }
    }

    public static Integer getTipoCehUsuariotoInt(String tipo) {
        switch (tipo) {
            case "INVENTARIANTE":
                return 0;
            case "EQUIPE":
                return 1;
            case "ADMINISTRADOR":
                return 2;
            default:
                return null;
        }
    }
}