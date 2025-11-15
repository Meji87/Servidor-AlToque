
package com.altoque.altoque_server;

import com.altoque.altoque_server.gestor.GestorEmpresa;
import com.altoque.altoque_server.gestor.GestorPeticions;
import com.altoque.altoque_server.gestor.GestorUsuari;
import com.altoque.altoque_server.servidor.ServidorAlToque;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Punt d'entrada de l'aplicació.
 * Arrenca el servidor AlToque al port 5050.
 * 
 * * @author marc mestres
 */
@SpringBootApplication
@Configuration
@EnableJpaRepositories
public class Principal {
    
    private final GestorPeticions peticions; // per Spring
    public Principal(GestorPeticions peticions) { this.peticions = peticions; }
    
    public static void main(String[] args) {
        SpringApplication.run(Principal.class, args);
    }
    
    @Bean
    @Profile("!test")   // NOMES es crea si el perfil NO es "test"
    ApplicationRunner run(GestorUsuari gestorUsuari, GestorEmpresa gestorEmpresa){
        return args ->{
            int port = Const.Servidor.PORT_DEFECTE;

            for (String a : args.getSourceArgs()) {
                if (a.startsWith("--port=")) port = Integer.parseInt(a.substring(7));
            }

            ServidorAlToque s = new ServidorAlToque(port, peticions);
            s.iniciar();
        };
        
    }
}
