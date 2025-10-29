
package com.altoque.altoque_server.servidor;

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
    private final GestorSessions sessions = new GestorSessions();
    
    
    /**
     * Crea el servidor indicant la ip i el port.
     * @param ip IP a utilitzar
     * @param port port TCP a utilitzar
     */
    public ServidorAlToque(String ip, int port) {
        this.IP = ip;
        this.PORT = port;
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
                new GestorServidor(clientSocket, sessions).start();
            }
        } catch (IOException e) {
            System.err.println("Error al servidor: " + e.getMessage());
        }
    }
}

