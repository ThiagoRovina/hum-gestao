/*
 * Universidade Estadual de Maringá - UEM
 * Núcleo de Processamento de Dados - NPD
 * Copyright (c) 2020. All rights reserved.
 */
package humcare.zk.custom;

import org.apache.commons.lang3.StringUtils;
import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.util.Clients;

/**
 * Classe do componente ZK personalizado para usar o stepbar.zul apenas usando
 * Java
 *
 * Para utilizar basta adicionar o componente ZK Exemplo:
 * <pre>&lt;stepbar id="stepbar" step="3" size="5" subtitles="pêra,uva,maçã,banana,abacate"/&gt;</pre>
 *
 * Pode-se também pegar o componente no controler e utilizar os getters e setter
 * para atualizar os valores do stepbar
 *
 * @author alison
 */
public class Stepbar extends HtmlMacroComponent {

    private Integer step = 0;
    private Integer size = 0;
    private String[] subtitles = null;
    private String subtitlesJoined = "";

    public Stepbar() {
        compose();
    }

    private void changeStep() {

        if (this.subtitles != null && this.subtitles.length > 0) {
            subtitlesJoined = StringUtils.join(subtitles, "','");
        }

        String javascriptFunction = "stepbarPasso(" + this.step + "," + this.size + ",['" + this.subtitlesJoined + "']);";

        Clients.evalJavaScript(javascriptFunction);
    }

    /**
     * Atualiza o stepbar para o próximo passo.
     *
     */
    public void next() {
        if (!this.getStep().equals(this.getSize())) {
            this.setStep(this.getStep() + 1);
        }
    }

    /**
     * Atualiza o stepbar para o passo anterior.
     *
     */
    public void prev() {
        if (!this.getStep().equals(1)) {
            this.setStep(this.getStep() - 1);
        }
    }

    /**
     * Atualiza o stepbar com os valores
     *
     * @param step Posição/passo atual do step
     * @param size Quantidade de passos
     */
    public void setValues(Integer step, Integer size) {
        this.step = step;
        this.size = size;
    }

    /**
     * Atualiza o stepbar com os valores
     *
     * @param step Posição/passo atual do step
     * @param size Quantidade de passos
     * @param subtitles Vetor/array de string com os subtítulos de cada passo,
     * ex: {'Pêra','Uva',Maçã','Salada mista'}
     */
    public void setValues(Integer step, Integer size, String[] subtitles) {
        this.step = step;
        this.size = size;
        this.subtitles = subtitles;
    }

    /**
     * Retorna a posição/passo atual do step
     *
     * @return Integer
     */
    public Integer getStep() {
        return step;
    }

    /**
     * Atualiza o stepbar para o passo passado por parâmetro
     *
     * @param step Posição/passo desejado
     */
    public void setStep(Integer step) {
        this.step = step;
        this.changeStep();
    }

    /**
     * Retorna a quantidade de passos
     *
     * @return Integer
     */
    public Integer getSize() {
        return size;
    }

    /**
     * Atualiza o stepbar criando uma nova quantidade de passos
     *
     * @param size
     */
    public void setSize(Integer size) {
        this.size = size;
        this.changeStep();
    }

    /**
     * Retorna os subtítulos
     *
     * @return String[]
     */
    public String[] getSubtitles() {
        return subtitles;
    }

    /**
     * Atualiza o stepbar com os novos subtítulos
     *
     * @param subtitles String com os subtítulos separado por vírgua, ex:
     * Pêra,Uva.Maçã,Salada Mista
     */
    public void setSubtitles(String subtitles) {
        this.subtitles = subtitles.strip().split(",");
        this.changeStep();
    }

}
