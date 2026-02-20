package humcare.higienizacao.tipoHigienizacao;

import humcare.usuario.tipopermissao.TipoPermissaoUsuario;
import lombok.Getter;

@Getter
public enum TipoHigienizacao {
    SIMPLES(0),
    MR(1),
    MR_PRETO(2);

    private final Integer valor;

    TipoHigienizacao(Integer i) { this.valor = i; }

    public static TipoHigienizacao getTipoHigienizacao(String tipo) {
        switch (tipo) {
            case "SIMPLES":
                return TipoHigienizacao.SIMPLES;
            case "MR":
                return TipoHigienizacao.MR;
            case "MR_PRETO":
                return TipoHigienizacao.MR_PRETO;
            default:
                return null;
        }
    }

    public static TipoHigienizacao getTipoHigienizacaoInt(Integer tipo) {
        switch (tipo) {
            case 0:
                return TipoHigienizacao.SIMPLES;
            case 1:
                return TipoHigienizacao.MR;
            case 2:
                return TipoHigienizacao.MR_PRETO;
            default:
                return null;
        }
    }

    public static String getTipoHigienizacaoString(Integer tipo) {
        switch (tipo) {
            case 0:
                return "SIMPLES";
            case 1:
                return "MR";
            case 2:
                return "MR PRETO";
            default:
                return null;
        }
    }

    public static Integer getTipoHigienizacaoInt(String tipo) {
        switch (tipo) {
            case "SIMPLES":
                return 0;
            case "MR":
                return 1;
            case "MR_PRETO":
                return 2;
            default:
                return null;
        }
    }
}
