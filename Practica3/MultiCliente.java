//Materia: Sistemas Distribuidos
//Autor: Diego Valdes Castillo
//Fecha de  creacion: 06/03/2025
//Version: 1.0
//Practica 3 - Servidor / Multiple Cliente 
//Codigo del Cliente para la carrera

// Cliente que recibe las actualizaciones de la carrera y se desconecta correctamente

import java.io.*;
import java.net.*;

public class MultiCliente {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5000);
             BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            
            System.out.println("Conectado al servidor. Esperando inicio de la carrera...");
            
            String mensaje;
            while ((mensaje = entrada.readLine()) != null) {
                System.out.println(mensaje);
            }
        } catch (IOException e) {
            System.out.println("No se pudo conectar con el servidor o la conexion fue cerrada.");
        } finally {
            System.out.println("Cliente desconectado correctamente.");
        }
    }
}
