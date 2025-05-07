//Materia: Sistemas Distribuidos
//Autor: Diego Valdes Castillo
//Fecha de  creacion: 20/03/2025 - Version: 1.0
//Practica 5 Objetos Distribuidos con RMI - Codigo del Servidor para la carrera
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ServidorCarrera implements CarreraRMI {
    private List<String> autos = new ArrayList<>();
    private List<Integer> posiciones = new ArrayList<>();
    private List<Long> tiempos = new ArrayList<>();
    private boolean carreraIniciada = false;
    private boolean carreraTerminada = false;

    public ServidorCarrera() throws RemoteException {}

    public synchronized void registrarAuto(String nombre) throws RemoteException {
        if (!carreraIniciada) {
            autos.add(nombre);
            posiciones.add(0);
            tiempos.add(0L);
            System.out.println(nombre + " registrado en la carrera.");

            if (autos.size() == 3) {
                iniciarCarrera();
            }
        }
    }

    public synchronized void iniciarCarrera() throws RemoteException {
        if (!carreraIniciada && autos.size() == 3) {
            carreraIniciada = true;
            System.out.println("La carrera ha comenzado!");

            for (int i = 0; i < autos.size(); i++) {
                int autoIndex = i;
                new Thread(() -> {
                    long tiempoInicio = System.currentTimeMillis();
                    while (posiciones.get(autoIndex) < 100) {
                        posiciones.set(autoIndex, posiciones.get(autoIndex) + (int) (Math.random() * 10));
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mostrarEstado();
                    }
                    tiempos.set(autoIndex, System.currentTimeMillis() - tiempoInicio);
                    System.out.println(autos.get(autoIndex) + " ha terminado la carrera!");
                    if (posiciones.stream().allMatch(p -> p >= 100)) {
                        mostrarPodio();
                        carreraTerminada = true;
                    }
                }).start();
            }
        }
    }

    public synchronized void mostrarEstado() {
        StringBuilder estado = new StringBuilder("Progreso de la carrera:\n");
        for (int i = 0; i < autos.size(); i++) {
            estado.append(autos.get(i)).append(": ").append(posiciones.get(i)).append(" metros\n");
        }
        System.out.println(estado);
    }

    public synchronized void mostrarPodio() {
        List<String> podio = autos.stream()
                .sorted((a1, a2) -> Long.compare(tiempos.get(autos.indexOf(a1)), tiempos.get(autos.indexOf(a2))))
                .collect(Collectors.toList());

        System.out.println("=== Podio Final ===");
        for (int i = 0; i < podio.size(); i++) {
            System.out.println((i + 1) + "° lugar: " + podio.get(i));
        }
    }

    public String obtenerEstado() throws RemoteException {
        if (carreraTerminada) return "La carrera ha terminado.";
        StringBuilder estado = new StringBuilder();
        for (int i = 0; i < autos.size(); i++) {
            estado.append(autos.get(i)).append(": ").append(posiciones.get(i)).append(" metros\n");
        }
        return estado.toString();
    }

    public static void main(String[] args) {
        try {
            System.setProperty("java.rmi.server.hostname", "192.168.56.1");
            ServidorCarrera server = new ServidorCarrera();
            CarreraRMI stub = (CarreraRMI) UnicastRemoteObject.exportObject(server, 0);
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("Carrera", stub);
            System.out.println("Servidor de carrera listo en 192.168.56.1:1099");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
