//Materia: Sistemas Distribuidos
//Autor: Diego Valdes Castillo
//Fecha de  creacion: 12/03/2025
//Version: 1.0
//Practica 4 Multiple Servidor / Multiple Cliente 
//Codigo del Distribuidor para la carrera

// Distribuidor.java - Distribuye clientes a servidores y recibe resultados
import java.io.*;
import java.net.*;
import java.util.*;

public class Distribuidor {
    private static final int PUERTO = 6000;
    private static List<String> servidores = Arrays.asList(
        "localhost:5001", "localhost:5002", "localhost:5003"
    );
    private static int indice = 0;
    private static List<String> resultados = new ArrayList<>();

    public static void main(String[] args) {
        new Thread(Distribuidor::recibirResultados).start();
        
        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            System.out.println("Distribuidor activo en el puerto " + PUERTO);
            while (true) {
                Socket cliente = serverSocket.accept();
                String servidorAsignado = servidores.get(indice);
                indice = (indice + 1) % servidores.size();
                
                PrintWriter salida = new PrintWriter(cliente.getOutputStream(), true);
                salida.println(servidorAsignado);
                cliente.close();
            }
        } catch (IOException e) {
            System.out.println("Error en el distribuidor: " + e.getMessage());
        }
    }

    private static void recibirResultados() {
        try (ServerSocket serverSocket = new ServerSocket(7000)) {
            while (true) {
                Socket servidor = serverSocket.accept();
                BufferedReader entrada = new BufferedReader(new InputStreamReader(servidor.getInputStream()));
                String resultado;
                StringBuilder podio = new StringBuilder();
                while ((resultado = entrada.readLine()) != null) {
                    synchronized (resultados) {
                        resultados.add(resultado);
                        podio.append(resultado).append("\n");
                    }
                }
                System.out.println("\nResumen de la carrera recibida:");
                System.out.println(podio.toString());
                servidor.close();
            }
        } catch (IOException e) {
            System.out.println("Error al recibir resultados: " + e.getMessage());
        }
    }
}