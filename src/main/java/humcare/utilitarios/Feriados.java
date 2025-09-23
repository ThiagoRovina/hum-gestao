package humcare.utilitarios;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author alison
 */
public final class Feriados {

    private final List<LocalDate> datas = new ArrayList<>();

    private final Integer anoAtual;

    public Feriados() {
        anoAtual = LocalDate.now().getYear();
        _init();
    }

    public Feriados(Integer ano) {
        anoAtual = ano;
        _init();
    }

    private void _init() {

        //feriados fixos
        datas.add(getAnoNovo());
        datas.add(getTiradentes());
        datas.add(getTrabalhador());
        datas.add(getIndependenciaDoBrasil());
        if (anoAtual >= 1980) {
            datas.add(getNossaSenharaAparecida());
        }
        datas.add(getFinados());
        datas.add(getProclamacaoDaRepublica());
        datas.add(getNatal());

        //feriados móveis
        datas.add(getCarnaval().minusDays(1));
        datas.add(getCarnaval());
        datas.add(getCarnaval().plusDays(1));
        datas.add(getSextaFeiraSanta());
        datas.add(getCorpusChristi());

    }

    private LocalDate _date(int year, int month, int day) {
        return LocalDate.of(year, month, day);
    }

    private LocalDate _getPascoa() {
        int a = anoAtual % 19;
        int b = anoAtual / 100;
        int c = anoAtual % 100;
        int d = b / 4;
        int e = b % 4;
        int f = (b + 8) / 25;
        int g = (b - f + 1) / 3;
        int h = (19 * a + b - d - g + 15) % 30;
        int i = c / 4;
        int k = c % 4;
        int l = (32 + 2 * e + 2 * i - h - k) % 7;
        int m = (a + 11 * h + 22 * l) / 451;
        int month = (h + l - 7 * m + 114) / 31;
        int day = ((h + l - 7 * m + 114) % 31) + 1;
        return LocalDate.of(anoAtual, month, day);
    }

    public boolean isFeriado(Date data) {
        return datas.contains(data.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
    }

    public List<LocalDate> listFeriados() {
        return datas;
    }

    public LocalDate getAnoNovo() {
        return _date(anoAtual, 1, 1);
    }

    public LocalDate getTiradentes() {
        return _date(anoAtual, 4, 21);
    }

    public LocalDate getTrabalhador() {
        return _date(anoAtual, 5, 1);
    }

    public LocalDate getIndependenciaDoBrasil() {
        return _date(anoAtual, 9, 7);
    }

    public LocalDate getNossaSenharaAparecida() {
        return _date(anoAtual, 10, 12);
    }

    public LocalDate getFinados() {
        return _date(anoAtual, 11, 2);
    }

    public LocalDate getProclamacaoDaRepublica() {
        return _date(anoAtual, 11, 15);
    }

    public LocalDate getNatal() {
        return _date(anoAtual, 12, 25);
    }

    public LocalDate getCarnaval() {
        return _getPascoa().minusDays(47);
    }

    public LocalDate getSextaFeiraSanta() {
        return _getPascoa().minusDays(2);
    }

    public LocalDate getCorpusChristi() {
        return _getPascoa().plusDays(60);
    }

}
