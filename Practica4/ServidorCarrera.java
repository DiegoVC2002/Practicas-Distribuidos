//Materia: Sistemas Distribuidos
//Autor: Diego Valdes Castillo
//Fecha de  creacion: 12/03/2025
//Version: 1.0
//Practica 4 Multiple Servidor / Multiple Cliente 
//Codigo de los Servidores para la carrera
// ServidorCarrera.java - Servidores de carrera que manejan clientes y envian resultados
import java.io.*;
import java.net.*;
import java.util.*;

class Auto extends Thread {
    private String nombre;
    private PrintWriter salida;
    private int progreso = 0;
    private static int relojLamport = 0;
    private static Map<String, Integer> posiciones = new LinkedHashMap<>();
    private static int posicionActual = 1;
    
    public Auto(String nombre, PrintWriter salida) {
        this.nombre = nombre;
        this.salida = salida;
    }

    private synchronized void incrementarReloj() {
        relojLamport++;
    }

    @Override
    public void run() {
        Random random = new Random();
        while (progreso < 100) {
            progreso += random.nextInt(20) + 1;
            if (progreso > 100) progreso = 100;
            incrementarReloj();
            salida.println("[T=" + relojLamport + "] " + nombre + " ha avanzado a " + progreso + " metros");
            salida.flush();
            try {
                Thread.sleep(random.nextInt(1000));
            } catch (InterruptedException e) {
                return;
            }
        }
        synchronized (posiciones) {
            posiciones.put(nombre, posicionActual++);
        }
        salida.println("[T=" + relojLamport + "] " + nombre + " ha terminado la carrera!");
        salida.flush();
    }

    public static Map<String, Integer> getPosiciones() {
        return posiciones;
    }
}

public class ServidorCarrera {
    private static int clientesConectados = 0;
    private static List<Auto> autos = new ArrayList<>();
    private static List<Socket> clientes = new ArrayList<>();
    
    public static void main(String[] args) {
        int puerto = args.length > 0 ? Integer.parseInt(args[0]) : 5001;
        try (ServerSocket servidor = new ServerSocket(puerto)) {
            System.out.println("Servidor de carrera activo en puerto " + puerto);
            while (clientesConectados < 3) {
                Socket cliente = servidor.accept();
                clientes.add(cliente);
                PrintWriter salida = new PrintWriter(cliente.getOutputStream(), true);
                Auto auto = new Auto("Auto " + (clientesConectados + 1), salida);
                autos.add(auto);
                clientesConectados++;
                System.out.println("Cliente conectado en puerto " + puerto);
            }
            
            System.out.println("Iniciando carrera en puerto " + puerto);
            for (Auto auto : autos) {
                auto.start();
            }
            for (Auto auto : autos) {
                auto.join();
            }
            
            enviarPodio(puerto);
            System.out.println("Carrera finalizada en puerto " + puerto);
            for (Socket cliente : clientes) {
                cliente.close();
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Error en el servidor de carrera: " + e.getMessage());
        }
    }
    
    private static void enviarPodio(int puerto) {
        try (Socket socket = new Socket("localhost", 7000);
             PrintWriter salida = new PrintWriter(socket.getOutputStream(), true)) {
            salida.println("\n--- PODIO FINAL EN SERVIDOR " + puerto + " ---");
            Auto.getPosiciones().forEach((auto, posicion) -> salida.println(posicion + ". " + auto));
            salida.flush();
        } catch (IOException e) {
            System.out.println("Error al enviar resultados: " + e.getMessage());
        }
    }
}