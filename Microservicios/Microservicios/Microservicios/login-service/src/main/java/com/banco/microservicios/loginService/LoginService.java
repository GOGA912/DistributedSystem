package com.banco.microservicios.loginService;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

public class LoginService {
    public static void main(String[] args) {
        try {
            // Crear el servidor en puerto 8081
            
            int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

            // Asociar la ruta /login con el manejador
            server.createContext("/login", new LoginHandler());

            // Empezar el servidor
            server.setExecutor(null); // Usa el executor por defecto
            server.start();

            System.out.println("Servidor de autenticación iniciado en http://localhost:"+port+"/login");
        } catch (Exception e) {
            System.out.println("Error al iniciar el servidor: " + e.getMessage());
        }
    }
}

