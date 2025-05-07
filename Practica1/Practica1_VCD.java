//Materia: Sistemas Distribuidos
//Autor: Diego Valdés Castillo
//Fecha de  creación: 20/02/2025
//Versión: 1.0
//Práctica 1 - Proceso e Hilos en Java 

// Clase Auto que extiende Thread para representar un automóvil en la carrera.
class Auto extends Thread {
    private String nombre; // Nombre del auto

    // Constructor que asigna el nombre del auto
    public Auto(String nombre) {
        this.nombre = nombre;
    }

    // Método que define el comportamiento del hilo
    @Override
    public void run() {
        // El auto avanza en intervalos de 10 metros hasta llegar a 100 metros
        for (int i = 1; i <= 10; i++) {
            System.out.println(nombre + " ha avanzado " + (i * 10) + " metros");
            try {
                // Simula el tiempo de avance del auto con un retraso aleatorio
                Thread.sleep((int) (Math.random() * 1000));
            } catch (InterruptedException e) {
                System.out.println(nombre + " se ha detenido inesperadamente.");
            }
        }
        // Mensaje cuando el auto termina la carrera
        System.out.println(nombre + " ha terminado la carrera!");
    }
}

// Clase principal que inicia la carrera
public class Practica1_VCD {
    public static void main(String[] args) {
        // Se crean tres objetos Auto con diferentes nombres
        Auto auto1 = new Auto("Auto Rojo");
        Auto auto2 = new Auto("Auto Azul");
        Auto auto3 = new Auto("Auto Verde");

        // Se inician los hilos para que los autos comiencen la carrera simultáneamente
        auto1.start();
        auto2.start();
        auto3.start();
    }
}