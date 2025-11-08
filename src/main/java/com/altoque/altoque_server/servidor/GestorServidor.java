package com.altoque.altoque_server.servidor;

import com.altoque.altoque_server.gestor.GestorPeticions;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

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
        try {
            // Obtenim els flux d'entrada del sòcol per rebre la petició del client
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            String data = input.readLine();
            
            String responseText = gestorPeticions.processar(data); //GestorPeticions.processar(data);
            
            // Obtenim els flux de sortida del sòcol per respondre al client
            PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        
            output.println(responseText);
            
            // Tanquem tots els fluxos i el sòcol
            input.close();
            output.close();
            socket.close();
            
            System.out.println("Connexió amb el client tancada correctament");
            
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error en rebre la petició");
        }
    }
}
