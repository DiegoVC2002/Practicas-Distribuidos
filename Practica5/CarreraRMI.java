//Materia: Sistemas Distribuidos
//Autor: Diego Valdes Castillo
//Fecha de  creacion: 20/03/2025
//Version: 1.0
//Practica 5 Objetos Distribuidos con RMI
//Codigo de la interfaz de RMI para la carrera

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CarreraRMI extends Remote {
    void registrarAuto(String nombre) throws RemoteException;
    void iniciarCarrera() throws RemoteException;
    String obtenerEstado() throws RemoteException;
}
