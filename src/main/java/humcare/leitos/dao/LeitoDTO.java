package humcare.leitos.dao;

import java.util.Date;

public class LeitoDTO {
    private Integer cdQuarto;
    private Integer cdLeito;
    private String idLeito;
    private String idQuarto;
    private Integer flAtivo;

    private String nmUnidade;
    private String nmHospital;

    private String deTipoBmr; // Changed from cdTipoBmr to deTipoBmr and type to String
    private Date dtCadastro;
    private Integer stLeitoBmr;
    private Integer nuProntuario;

    private String nmPaciente;
    private String sgSexo;

    private String ltClassificacao;
    private String ltHexCor;
    private String deSimplificada;
    private String ltSimbolo;

    // Getters and Setters
    public Integer getCdQuarto() {
        return cdQuarto;
    }

    public void setCdQuarto(Integer cdQuarto) {
        this.cdQuarto = cdQuarto;
    }

    public Integer getCdLeito() {
        return cdLeito;
    }

    public void setCdLeito(Integer cdLeito) {
        this.cdLeito = cdLeito;
    }

    public String getIdLeito() {
        return idLeito;
    }

    public void setIdLeito(String idLeito) {
        this.idLeito = idLeito;
    }

    public String getIdQuarto() {
        return idQuarto;
    }

    public void setIdQuarto(String idQuarto) {
        this.idQuarto = idQuarto;
    }

    public Integer getFlAtivo() {
        return flAtivo;
    }

    public void setFlAtivo(Integer flAtivo) {
        this.flAtivo = flAtivo;
    }

    public String getNmUnidade() {
        return nmUnidade;
    }

    public void setNmUnidade(String nmUnidade) {
        this.nmUnidade = nmUnidade;
    }

    public String getNmHospital() {
        return nmHospital;
    }

    public void setNmHospital(String nmHospital) {
        this.nmHospital = nmHospital;
    }

    public String getDeTipoBmr() {
        return deTipoBmr;
    }

    public void setDeTipoBmr(String deTipoBmr) {
        this.deTipoBmr = deTipoBmr;
    }

    public Date getDtCadastro() {
        return dtCadastro;
    }

    public void setDtCadastro(Date dtCadastro) {
        this.dtCadastro = dtCadastro;
    }

    public Integer getStLeitoBmr() {
        return stLeitoBmr;
    }

    public void setStLeitoBmr(Integer stLeitoBmr) {
        this.stLeitoBmr = stLeitoBmr;
    }

    public Integer getNuProntuario() {
        return nuProntuario;
    }

    public void setNuProntuario(Integer nuProntuario) {
        this.nuProntuario = nuProntuario;
    }

    public String getNmPaciente() {
        return nmPaciente;
    }

    public void setNmPaciente(String nmPaciente) {
        this.nmPaciente = nmPaciente;
    }

    public String getSgSexo() {
        return sgSexo;
    }

    public void setSgSexo(String sgSexo) {
        this.sgSexo = sgSexo;
    }

    public String getLtClassificacao() {
        return ltClassificacao;
    }

    public void setLtClassificacao(String ltClassificacao) {
        this.ltClassificacao = ltClassificacao;
    }

    public String getLtHexCor() {
        return ltHexCor;
    }

    public void setLtHexCor(String ltHexCor) {
        this.ltHexCor = ltHexCor;
    }

    public String getDeSimplificada() {
        return deSimplificada;
    }

    public void setDeSimplificada(String deSimplificada) {
        this.deSimplificada = deSimplificada;
    }

    public String getLtSimbolo() {
        return ltSimbolo;
    }

    public void setLtSimbolo(String ltSimbolo) {
        this.ltSimbolo = ltSimbolo;
    }
}
