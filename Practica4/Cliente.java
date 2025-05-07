//Materia: Sistemas Distribuidos
//Autor: Diego Valdes Castillo
//Fecha de  creacion: 12/03/2025
//Version: 1.0
//Practica 4 Multiple Servidor / Multiple Cliente 
//Codigo del Cliente para la carrera

// Cliente.java - Cliente que se conecta al balanceador y luego al servidor de carrera
import java.io.*;
import java.net.*;

public class Cliente {
    public static void main(String[] args) {
        try (Socket balanceador = new Socket("localhost", 6000);
             BufferedReader entrada = new BufferedReader(new InputStreamReader(balanceador.getInputStream()))) {
            String direccionServidor = entrada.readLine();
            String[] partes = direccionServidor.split(":");
            String host = partes[0];
            int puerto = Integer.parseInt(partes[1]);
            
            try (Socket socket = new Socket(host, puerto);
                 BufferedReader entradaServidor = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                System.out.println("Conectado a " + direccionServidor);
                String mensaje;
                while ((mensaje = entradaServidor.readLine()) != null) {
                    System.out.println(mensaje);
                }
            }
        } catch (IOException e) {
            System.out.println("Error en el cliente: " + e.getMessage());
        }
    }
}
