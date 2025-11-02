
package com.altoque.altoque_server.servidor;

import com.altoque.altoque_server.servei.GestorEmpresa;
import com.altoque.altoque_server.servei.GestorPeticions;
import com.altoque.altoque_server.servei.GestorUsuari;
import java.io.IOException;
import java.net.InetSocketAddress;
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
    private final String IP;
    private final int PORT;
//    private final GestorSessions sessions = new GestorSessions();
//    private final GestorUsuari gestorUsuari;
//    private final GestorEmpresa gestorEmpresa;
    private final GestorPeticions gestorPeticions;
    
    
    /**
     * Crea el servidor indicant la ip i el port.
     * @param ip IP a utilitzar
     * @param port port TCP a utilitzar
     * @param gestorPeticions  instancia compartida de gestorPeticions
     */
    public ServidorAlToque(String ip, int port, GestorPeticions gestorPeticions) {
        this.IP = ip;
        this.PORT = port;
        this.gestorPeticions = gestorPeticions;
    }
    
    /**
     * Bucle principal: accepta connexions i crea un fil per cada socket.
     */
    public void iniciar(){
        try {
            ServerSocket serverSocket = new ServerSocket();
            InetSocketAddress addr = new InetSocketAddress(IP, PORT);
            serverSocket.bind(addr);
            
            System.out.println("Servidor iniciat al port " + PORT);

            while (true) {
                System.out.println("Esperant connexions dels clients");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nova connexió acceptada");

                // Creem un nou fil per cada client
                new GestorServidor(clientSocket, gestorPeticions).start();
            }
        } catch (IOException e) {
            System.err.println("Error al servidor: " + e.getMessage());
        }
    }
}

