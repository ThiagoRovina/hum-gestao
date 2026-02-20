package humcare.application.service;

import humcare.application.model.CehLog;
import humcare.application.tipoocorrencia.TipoOcorrenciaLog;
import humcare.dao.GenericDAO;

import java.util.Date;

public class CehLogService extends GenericDAO<CehLog, Integer> {

    private static CehLogService instanceLog;

    public CehLogService() {
        super(CehLog.class);
    }

    public static CehLogService getInstance() {
        if (instanceLog == null) {
            instanceLog = new CehLogService();
        }
        return instanceLog;
    }

    public Integer insereLog(String tabela, TipoOcorrenciaLog tpOcorrencia) {
        CehLog cehLog = new CehLog();
        cehLog.setNmTabela(tabela);
        cehLog.setCdUsuario(Sessao.getInstance().getUsuario().getCdUsuario());
        cehLog.setDhOcorrencia(new Date());
        cehLog.setTpOcorrencia(tpOcorrencia);

        return this.incluirAutoincrementando(cehLog);
    }
}
