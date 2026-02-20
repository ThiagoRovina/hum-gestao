package humcare.usuario.funcao;


import lombok.Getter;

@Getter
public enum FuncaoCdPerfil {
    HIGIENIZACAO(0),
    EQUIPE(1),
    ADMINISTRADOR(2),
    NIR(3);

    private final Integer valor;

    FuncaoCdPerfil(Integer i) { this.valor = i; }

    public static FuncaoCdPerfil getFuncaoCdPerfil(String tipo) {
        switch (tipo) {
//            case "HIGIENIZACAO":
//                return FuncaoCdPerfil.HIGIENIZACAO;
//            case "EQUIPE":
//                return FuncaoCdPerfil.EQUIPE;
            case "ADMINISTRADOR":
                return FuncaoCdPerfil.ADMINISTRADOR;
            case "NIR":
                return FuncaoCdPerfil.NIR;
            default:
                return null;
        }
    }
    public static FuncaoCdPerfil getFuncaoCdPerfilInt(Integer tipo) {
        switch (tipo) {
//            case 0:
//                return FuncaoCdPerfil.HIGIENIZACAO;
//            case 1:
//                return FuncaoCdPerfil.EQUIPE;
            case 2:
                return FuncaoCdPerfil.ADMINISTRADOR;
            case 3:
                return FuncaoCdPerfil.NIR;
            default:
                return null;
        }
    }
}
