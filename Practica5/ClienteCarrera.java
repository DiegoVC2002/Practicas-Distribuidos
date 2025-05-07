//Materia: Sistemas Distribuidos
//Autor: Diego Valdes Castillo
//Fecha de  creacion: 20/03/2025
//Version: 1.0
//Practica 5 Objetos Distribuidos con RMI
//Codigo del Cliente para la carrera

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.net.InetAddress;

public class ClienteCarrera {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Uso: java ClienteCarrera <NombreAuto>");
            return;
        }

        try {
            String servidorIP = InetAddress.getLocalHost().getHostAddress();
            Registry registry = LocateRegistry.getRegistry(servidorIP, 1099);
            CarreraRMI carrera = (CarreraRMI) registry.lookup("Carrera");

            String nombreAuto = args[0]; // Ahora el usuario proporciona el nombre del auto
            carrera.registrarAuto(nombreAuto);
            System.out.println(nombreAuto + " ha entrado en la carrera.");

            while (true) {
                Thread.sleep(2000);
                String estado = carrera.obtenerEstado();
                System.out.println("Estado de la carrera:\n" + estado);

                if (estado.contains("La carrera ha terminado")) {
                    System.out.println("La carrera ha finalizado.");
                    break;
                }
            }

            System.out.println("Cliente cerrado correctamente.");
            System.exit(0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
