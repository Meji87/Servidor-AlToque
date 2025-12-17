package com.altoque.altoque_server.servidor;

import com.altoque.altoque_server.gestor.GestorPeticions;
import com.altoque.altoque_server.gestor.GestorXifrat;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Fil que atén un client.
 * Llegeix missatges del socket, els passa al GestorPeticions i envia la resposta.
 * 
 * @author marc mestres
 */
public class GestorServidor extends Thread{
    private final Socket socket;
    private final GestorPeticions gestorPeticions;

    /**
     * Crea el gestor per a un socket de client.
     * @param socket connexió amb el client
     * @param gestorPeticions  gestor de peticions compartit
     */
    public GestorServidor(Socket socket, GestorPeticions gestorPeticions) {
        this.socket = socket;
        this.gestorPeticions = gestorPeticions;
    }
    
    @Override
    public void run() {
        BufferedReader input = null;
        PrintWriter output = null;
        try {
            System.out.println();
            System.out.println("~~~~~~~~   Informació Servidor    ~~~~~~~~");
            // Obtenim els flux d'entrada del sòcol per rebre la petició del client
            input = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            // Obtenim els flux de sortida del sòcol per respondre al client
            output = new PrintWriter(
                    new OutputStreamWriter(socket.getOutputStream()), true);
            
            // Llegir dades encriptades
            String dataEncriptada = input.readLine();
            System.out.println("  ~ Peticio xifrada:   " + dataEncriptada); // ***************************************************************
            
            // Desxifrar
            String data = GestorXifrat.desxifrar(dataEncriptada);
            System.out.println("    - Peticio rebuda:   " + data); // ***************************************************************
            
            // Processar
            String resposta = gestorPeticions.processar(data); //GestorPeticions.processar(data);
            System.out.println("    - Resposta a enviar: " + resposta); // ***************************************************************
            
            // Xifrar resposta
            String respostaEncriptada = GestorXifrat.xifrar(resposta);
            System.out.println("  ~ Resposta xifrada:   " + respostaEncriptada); // ***************************************************************
            
            // Enviar
            output.println(respostaEncriptada);
            
        } catch(GestorException ge){
            System.out.println("Error en xifrat/desxifrat: " + ge.getMessage());
            ge.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error en rebre la petició (I/O): " + e.getMessage());
            e.printStackTrace();
        } catch (Exception ex){
            System.out.println("Error inesperat: " + ex.getMessage());
            ex.printStackTrace();
        } finally{
            // Tanquem tots els fluxos i el sòcol
            try{
                if (input != null) input.close();
            }catch(Exception e){}
            try{
                if (output != null) output.close();
            }catch(Exception e){}
            try{
                socket.close();
            }catch(Exception e){}
            
            System.out.println("   · Connexio amb el client tancada correctament ·");
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        }
    }
}
