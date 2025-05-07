//Materia: Sistemas Distribuidos
//Autor: Diego Valdes Castillo
//Fecha de  creacion: 06/03/2025
//Practica 3 - Servidor / Multiple Cliente   
//Version: 1.0
//Codigo del Servidor para la carrera
// Servidor que gestiona la carrera con multiples clientes

import java.io.*;
import java.net.*;
import java.util.*;

class Auto extends Thread {
    private String nombre;
    private PrintWriter salida;
    private int progreso = 0;
    private static Map<String, Integer> posiciones = new LinkedHashMap<>();
    private static int posicionActual = 1;

    public Auto(String nombre, PrintWriter salida) {
        this.nombre = nombre;
        this.salida = salida;
    }

    @Override
    public void run() {
        Random random = new Random();
        while (progreso < 100) {
            progreso += random.nextInt(20) + 1;
            if (progreso > 100) progreso = 100;
            salida.println(nombre + " ha avanzado a " + progreso + " metros");
            salida.flush();
            System.out.println(nombre + " ha avanzado a " + progreso + " metros");
            try {
                Thread.sleep(random.nextInt(1000));
            } catch (InterruptedException e) {
                salida.println(nombre + " se ha detenido inesperadamente.");
                salida.flush();
                return;
            }
        }
        synchronized (posiciones) {
            posiciones.put(nombre, posicionActual++);
        }
        salida.println(nombre + " ha terminado la carrera!");
        salida.flush();
        UnServidor.verificarCarreraFinalizada();
    }

    public static Map<String, Integer> getPosiciones() {
        return posiciones;
    }
}

public class UnServidor {
    private static final int PUERTO = 5000;
    private static List<Socket> clientes = new ArrayList<>();
    private static List<Auto> autos = new ArrayList<>();
    private static boolean carreraIniciada = false;
    
    public static synchronized void verificarCarreraFinalizada() {
        if (autos.stream().allMatch(auto -> Auto.getPosiciones().containsKey(auto.getName()))) {
            System.out.println("\n--- PODIO FINAL ---");
            Auto.getPosiciones().forEach((auto, posicion) -> System.out.println(posicion + ". " + auto));
            
            for (int i = 0; i < clientes.size(); i++) {
                try {
                    PrintWriter salida = new PrintWriter(clientes.get(i).getOutputStream(), true);
                    salida.println("\n--- PODIO FINAL ---");
                    Auto.getPosiciones().forEach((auto, posicion) -> salida.println(posicion + ". " + auto));
                    salida.println("\nTu posición final: " + Auto.getPosiciones().get("Auto " + (i + 1)));
                    salida.flush();
                } catch (IOException e) {
                    System.out.println("Error al enviar el podio a un cliente.");
                }
            }
        }
    }

    public static void main(String[] args) {
        try (ServerSocket servidor = new ServerSocket(PUERTO)) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Ingrese el número de autos en la carrera: ");
            int numAutos = scanner.nextInt();
            
            System.out.println("Esperando la conexión de " + numAutos + " clientes...");
            
            for (int i = 1; i <= numAutos; i++) {
                Socket socket = servidor.accept();
                clientes.add(socket);
                PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
                Auto auto = new Auto("Auto " + i, salida);
                autos.add(auto);
                System.out.println("Cliente " + i + " conectado!");
            }
            
            System.out.println("Todos los clientes conectados. Iniciando carrera...");
            carreraIniciada = true;
            
            for (Auto auto : autos) {
                auto.start();
            }
            
            for (Auto auto : autos) {
                auto.join();
            }
            
            System.out.println("\nCarrera finalizada. Podio de la carrera:");
            Auto.getPosiciones().forEach((auto, posicion) -> System.out.println(posicion + ". " + auto));
            
            System.out.println("Cerrando conexiones...");
            for (Socket cliente : clientes) {
                cliente.close();
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Error en el servidor: " + e.getMessage());
        }
    }
}
