//Materia: Sistemas Distribuidos
//Autor: Diego Valdes Castillo
//Fecha de  creacion: 27/02/2025
//Practica 2 - Sockets C/S    //Version: 1.0
//Codigo del Servidor para la carrera
// Servidor que gestiona la carrera con un solo cliente
import java.io.*;
import java.net.*;

class Auto extends Thread {
    private String nombre;
    private PrintWriter salida;

    public Auto(String nombre, PrintWriter salida) {
        this.nombre = nombre;
        this.salida = salida;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 10; i++) {
            String mensaje = nombre + " ha avanzado " + (i * 10) + " metros";
            System.out.println(mensaje);
            salida.println(mensaje);
            salida.flush(); // Asegurar que el mensaje se envíe inmediatamente
            try {
                Thread.sleep((int) (Math.random() * 1000));
            } catch (InterruptedException e) {
                salida.println(nombre + " se ha detenido inesperadamente.");
                salida.flush();
            }
        }
        salida.println(nombre + " ha terminado la carrera!");
        salida.flush();
    }
}

public class ServidorCarrera {
    public static void main(String[] args) {
        try (ServerSocket servidor = new ServerSocket(5000)) {
            System.out.println("Servidor esperando conexión de un cliente...");

            try (Socket socket = servidor.accept();
                 PrintWriter salida = new PrintWriter(socket.getOutputStream(), true)) {

                System.out.println("Cliente conectado!");

                Auto auto1 = new Auto("Auto Rojo", salida);
                Auto auto2 = new Auto("Auto Azul", salida);
                Auto auto3 = new Auto("Auto Verde", salida);

                auto1.start();
                auto2.start();
                auto3.start();

                // Esperar a que los autos terminen la carrera antes de cerrar la conexión
                auto1.join();
                auto2.join();
                auto3.join();

                salida.println("Carrera finalizada, cerrando conexión...");
                salida.flush();
                System.out.println("Carrera terminada. Cliente desconectado.");
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Error en el servidor: " + e.getMessage());
        }
    }
}
