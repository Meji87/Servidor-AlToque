package com.altoque.altoque_server.servidor;

import com.altoque.altoque_server.gestor.GestorPeticions;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Servidor principal basat en sockets.
 * Obre el ServerSocket, accepta connexions i crea un fil GestorServidor per client.
 * Es crea una instancia de GestorSessions que serà la mateixa per tots els clients.
 * 
 * * @author marc mestres
 */
public class ServidorAlToque {
    //private final String IP;
    private final int PORT;
//    private final GestorSessions sessions = new GestorSessions();
//    private final GestorUsuari gestorUsuari;
//    private final GestorEmpresa gestorEmpresa;
    private final GestorPeticions gestorPeticions;
    
    
    /**
     * Crea el servidor indicant la ip i el port.
     * @param port port TCP a utilitzar
     * @param gestorPeticions  instancia compartida de gestorPeticions
     */
    public ServidorAlToque(int port, GestorPeticions gestorPeticions) {
        this.PORT = port;
        this.gestorPeticions = gestorPeticions;
    }
    
    /**
     * Bucle principal: accepta connexions i crea un fil per cada socket.
     */
    public void iniciar(){
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);

            System.out.println();
            System.out.println("_____________________________________________");
            System.out.println();
            System.out.println("    Servidor AlToque iniciat al port " + PORT);
            System.out.println("_____________________________________________");

            while (true) {
                System.out.println();
                System.out.println("*** Esperant connexions dels clients...");
                Socket clientSocket = serverSocket.accept();
                System.out.println();
                System.out.println("------------------------------------------");
                System.out.println("Nova connexio acceptada");
                System.out.println("    Client:         " + clientSocket.getInetAddress());

                // Creem un nou fil per cada client
                new GestorServidor(clientSocket, gestorPeticions).start();
                System.out.println("------------------------------------------");
            }
        } catch (IOException e) {
            System.err.println("Error al servidor: " + e.getMessage());
        }
    }
}

