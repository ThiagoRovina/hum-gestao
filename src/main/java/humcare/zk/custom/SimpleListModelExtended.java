/*
 * Universidade Estadual de Maringá - UEM
 * Núcleo de Processamento de Dados - NPD
 * Copyright (c) 2020. All rights reserved.
 */
package humcare.zk.custom;

import java.text.Normalizer;
import java.util.LinkedList;
import java.util.List;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.SimpleListModel;

/**
 *
 * @author alison
 * 
 * Para o autocomplete do combobox ter um comportamento diferente do padrão:
 * - Compara a palavra digitada de modo Case Insensitive
 * - Compara a palavra digitada desconsiderando acentos e caracteres especiais
 * - Comparação "se a palavra digitada contém na string" em vez de "a string começar com a palavra digitada"
 * 
 */
public class SimpleListModelExtended extends SimpleListModel {
    
    public SimpleListModelExtended(List data) {
        super(data);
    }

    public SimpleListModelExtended(String[] data) {
        super(data);
    }

    public ListModel getSubModel(Object value, int nRows) {
        final String idx = value == null ? "" : objectToString(value);
        
        if (nRows < 0)
            nRows = 10;
        
        final LinkedList data = new LinkedList();
        for (int i = 0; i < getSize(); i++) {
            if (idx.equals("") || entryMatchesText(getElementAt(i).toString(), idx)) {
                data.add(getElementAt(i));
                if (nRows < 0)
                    nRows = 10;
            }
        }
        return new SimpleListModelExtended(data);
    }

    public boolean entryMatchesText(String entry, String text) {
        //remove acentos
        text = Normalizer.normalize(text, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");

        //compara canse insensitive utilizando contains
        return entry.toLowerCase().contains(text.toLowerCase());
    }
    
}
