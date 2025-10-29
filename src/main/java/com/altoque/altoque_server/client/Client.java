
package com.altoque.altoque_server.client;

import com.altoque.altoque_server.dto.RespostaPeticio;
import com.altoque.altoque_server.dto.Peticio;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Client de proves.
 * Obre un socket, envia una petició en JSON i mostra la resposta del servidor.
 * Per comprovar el flux.
 * 
 * @author marc mestres
 */
public class Client {
    
    public static void main(String[] args) {
        
        Scanner scanner = new Scanner(System.in);
        boolean sortir = false;
        
        while(!sortir){
            
            RespostaPeticio r = new RespostaPeticio();
            System.out.println("-----------------------------------------");
            System.out.println("Tipus [1]LOGIN / [2]LOGOUT:(tecla sortir) ");
            String tipus = scanner.nextLine();

            if (tipus.equals("1")){
                System.out.println("Login [1]USUARI / [2]EMPRESA: ");
                String rol = scanner.nextLine();
                System.out.println("Introdueix el nom d'usuari: ");
                String nomUsuari = scanner.nextLine();
                System.out.println("Introdueix la contrasenya: ");
                String contrasenya = scanner.nextLine();
                
                if (rol.equals("1")){
                    Peticio loginUsuari = new Peticio("LOGIN_USUARI");
                    loginUsuari.addDataObject(nomUsuari);
                    loginUsuari.addDataObject(contrasenya);
                    RespostaPeticio res = processar(loginUsuari);
                    r = res;
                } else {
                    Peticio loginEmpresa = new Peticio("LOGIN_EMPRESA");
                    loginEmpresa.addDataObject(nomUsuari);
                    loginEmpresa.addDataObject(contrasenya);
                    RespostaPeticio res = processar(loginEmpresa);
                    r = res;
                }
                
            } else if(tipus.equals("2")){
                System.out.println("Introdueix el token: ");
                String token = scanner.nextLine();
                Peticio logout = new Peticio("LOGOUT");
                logout.addDataObject(token);
                RespostaPeticio res = processar(logout);
                r = res;
            } else {
                    sortir = true;
            }
            System.out.println();
            System.out.println("RESPOSTA SERVIDOR:");
            System.out.println("Codi: " + r.getCodi());
            System.out.println("Missatge: " + r.getMissatge());
            if (r.getCodi()==0 && !r.isDadesBuides()){
                System.out.println("Token: " + r.getData(0, String.class));
            }
            
            
        }
    }
    
    private static RespostaPeticio processar(Peticio message){
        try {       
            Socket socket = new Socket("localhost", 5050);
            Gson gson= new Gson();

            if(socket==null) return null;

            PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            output.println(gson.toJson(message));
            
            //System.out.println(message.getPeticio());

            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream())); 

            String data = input.readLine();
            //System.out.println("Resposta del servido: " + data);

            RespostaPeticio resposta = gson.fromJson(data,RespostaPeticio.class);

            socket.close();

            return resposta;

        } catch (IOException ex) {
            return null;
        }

    }
    
}
