
package com.altoque.altoque_server.dto;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.util.ArrayList;

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
    private ArrayList <String> data = new ArrayList<>();
    
    /**
     * Afegeix un valor a la llista de dades.
     * Internament es desa com a JSON per poder recuperar-lo com a objecte.
     * @param data valor a afegir (qualsevol tipus serialitzable a JSON)
     */
    public void addDataObject(Object data){
        Gson gson = new Gson();
        this.data.add(gson.toJson(data));
    }
    
    /**
     * Retorna el valor de la posició indicada convertit al tipus demanat.
     * Si el JSON no es pot convertir, retorna null.
     * @param index posició del paràmetre (0, 1, 2...)
     * @param clase classe/Tipus a convertir (p. ex. String.class, Integer.class)
     * @return l'objecte convertit o null si la conversió falla
     * 
     */
    public Object getData(int index, Class clase){
        try{
            Gson gson = new Gson();
            return gson.fromJson(data.get(index),clase);
        }catch(JsonSyntaxException ex){return null;}
    }
    
    
    public boolean isDadesBuides(){
        if(data.isEmpty()){
            return true;
        }
        if(data.size()<1){
            return true;
        }
        return false;
    }
    
    public int tamany(){
        return data.size();
    }
}
