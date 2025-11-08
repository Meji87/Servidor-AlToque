
package com.altoque.altoque_server.dto;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Contenidor senzill per als valors d'una petició.
 * Desa una llista de dades (paràmetres) en format text i ofereix ajuda
 * per afegir-hi objectes i per recuperar-los convertits al tipus que calgui.
 *
 * S'utilitza com a "cos" de la petició, la classe Peticio
 * hereta d'aquesta per afegir-hi el tipus de petició.
 * 
 * @author marc mestres
 */
public class DadesPeticio {
    //private List<String> data = new ArrayList<>();
    private List<JsonElement> data = new ArrayList<>();
    
    /**
     * Afegeix un valor a la llista de dades.Internament es desa com a JSON per poder recuperar-lo com a objecte.
     * @param <T> Tipus de dada del objecte a afegir
     * @param objecte qualsevol tipus d'objecte serialitzable a JSON
     * 
     */
    public <T> void addData(T objecte){
        Gson gson = new Gson();
        data.add(gson.toJsonTree(objecte));
    }
    
    /**
     * Retorna el valor de la posició indicada convertit al tipus demanat.
     * Si el JSON no es pot convertir, retorna null.
     * @param index posició del paràmetre (0, 1, 2...)
     * @param clase classe/Tipus a convertir (p. ex. String.class, Empresa.class)
     * @param <T> Tipus de dada del objecte a recuperar
     * @return l'objecte convertit o null si la conversió falla
     * 
     */
    public <T> T getData(int index, Class<T> clase){
        Gson gson = new Gson();
        return gson.fromJson(data.get(index), clase);
    }
    
    /**
     * Indica si data és buit o no.
     * @return boolea indicant si data (llista d'elements Json) es buida o no.
     */
    public boolean esDataBuit (){
        return data == null || data.isEmpty();
    }
    
    /**
     * Indica el nombre d'elements de data.
     * @return un enter indicant el tamany de data, si data es buit, retorna 0
     */
    public int sizeData(){
        if (esDataBuit()) return 0;
        return data.size();
    }
    
}
