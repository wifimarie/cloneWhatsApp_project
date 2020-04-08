/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clonewhatsapp.Clases;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServidorComunicacion implements Runnable {

    private static ServidorComunicacion instancia;
    private ServerSocket servidor;
    private List<ManejoComunicacion> listaManejoComunicacion = new ArrayList<>();
    private boolean arrancado;
    private SubjectHelper subjectHelper = new SubjectHelper();

    private ServidorComunicacion() {
    }

    /**
     * @return
     */
    public static ServidorComunicacion getInstacia() {
        if (instancia == null) {
            instancia = new ServidorComunicacion();
        }
        return instancia;
    }

    /**
     * @param puerto
     * @throws Exception
     */
    public void iniciarServidor(int puerto) throws Exception {
        mostrarLog("Iniciando Servidor en puerto: " + puerto);
        servidor = new ServerSocket(puerto);
        mostrarLog("Servidor Inicializado...");
        arrancado = true;
        new Thread(this).start();
    }

    /**
     *
     */
    public void pararServidor() {
        mostrarLog("Parando el servidor");
        arrancado = false;
        try {
            servidor.close();
            servidor = null;
            listaManejoComunicacion.clear();
            subjectHelper.removeAllObserver();
        } catch (IOException ex) {
            Logger.getLogger(ServidorComunicacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void enviarMensajeUsuariosConectados(Mensaje mensaje) {
        for (ManejoComunicacion m : listaManejoComunicacion) {
            try {
                m.enviarInformacion(mensaje.getUsuario(), mensaje.getMensaje());
            } catch (IOException ex) {
                Logger.getLogger(ServidorComunicacion.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void run() {
        while(arrancado){
            try {
                //Abriendo el sockect.
                Socket s = servidor.accept();
                mostrarLog("Nuevo Cliente: "+s.getRemoteSocketAddress().toString());
                //
                subjectHelper.notify(ServerSocket.class, "nueva conexion", TipoNotificacion.CONEXION);
                //agregando la lista el socket nuevo.
                ManejoComunicacion m = new ManejoComunicacion(s);
                //
                m.addObservadorListener((Class clase, Object argumento, Enum anEnum) -> {
                    subjectHelper.notify(clase, argumento, anEnum);
                });
                //
                listaManejoComunicacion.add(m);
                //Iniciando el hilo
                new Thread(m).start();
            } catch (IOException ex) {
                Logger.getLogger(ServidorComunicacion.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * 
     * @param observer
     */
    public void addObservadorListener(IObserver observer) {
        subjectHelper.addObserver(observer);
    }

    /**
     * 
     * @param observer
     */
    public void removeObservadorListener(IObserver observer) {
        subjectHelper.removeObserver(observer);
    }

    /**
     * @param log
     */
    private void mostrarLog(String log) {
        System.out.println(log);
        subjectHelper.notify(ServidorComunicacion.class, log, TipoNotificacion.LOG);
    }
}
