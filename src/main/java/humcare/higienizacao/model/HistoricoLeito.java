package humcare.higienizacao.model;

import humcare.application.model.Usuario;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Modelo para representar um item no histórico de status de um leito.
 */
public class HistoricoLeito {
    private Date dataHora;
    private String status;
    private Usuario responsavel;

    public HistoricoLeito(Date dataHora, String status, Usuario responsavel) {
        this.dataHora = dataHora;
        this.status = status;
        this.responsavel = responsavel;
    }

    public Date getDataHora() {
        return dataHora;
    }

    public String getDataHoraFormatada() {
        if (dataHora == null) {
            return "";
        }
        return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(dataHora);
    }

    public void setDataHora(Date dataHora) {
        this.dataHora = dataHora;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Usuario getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(Usuario responsavel) {
        this.responsavel = responsavel;
    }
}