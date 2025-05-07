//Materia: Sistemas Distribuidos
//Autor: Diego Valdes Castillo
//Fecha de  creacion: 27/02/2025
//Version: 1.0
//Practica 2 - Sockets C/S 
//Codigo del Cliente para la carrera

// Cliente que recibe las actualizaciones de la carrera y se desconecta correctamente
import java.io.*;
import java.net.*;

public class ClienteCarrera {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5000);
             BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            String mensaje;
            while ((mensaje = entrada.readLine()) != null) {
                System.out.println("[SERVIDOR]: " + mensaje);
            }
        } catch (IOException e) {
            System.out.println("No se pudo conectar con el servidor o la conexión fue cerrada.");
        } finally {
            System.out.println("Cliente desconectado correctamente.");
        }
    }
}
