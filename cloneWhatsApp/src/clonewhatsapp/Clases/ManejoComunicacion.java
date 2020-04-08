package clonewhatsapp.Clases;

import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase encargada de encapsular toda la comunicación de los clientes.
 */
public class ManejoComunicacion implements Runnable {

    //Propiedades
    private Socket socket;
    private DataOutputStream enviarInformacion;
    private DataInputStream recibirInformacion;
    private Gson gson = new Gson();
    private SubjectHelper subjectHelper = new SubjectHelper();
    //


    /**
     *
     * @param socket
     * @throws IOException
     */
    public ManejoComunicacion(Socket socket) throws IOException {
        this.socket = socket;
        //Abriendo los flujos de datos.
        recibirInformacion = new DataInputStream(this.socket.getInputStream());
        enviarInformacion = new DataOutputStream(this.socket.getOutputStream());
    }

    /**
     *
     * @throws IOException
     */
    public void recibirInformacion() throws IOException {
        while (socket.isConnected()) {
            //recibiendo información
            String mensaje = recibirInformacion.readUTF();
            Mensaje m = gson.fromJson(mensaje, Mensaje.class);
            //
            mostrarLog("Mensaje Json Raw: "+mensaje);
            //enviar el mensaje a todos los clientes conectado.
            ServidorComunicacion.getInstacia().enviarMensajeUsuariosConectados(m);
        }
    }

    /**
     * No es necesario tener en un hilo para enviar información.
     *
     * @param enviadoPor
     * @param mensajes 
     * @throws IOException
     */
    public void enviarInformacion(String enviadoPor, String mensajes) throws IOException {
        Mensaje m = new Mensaje(enviadoPor, mensajes, new Date());
        enviarInformacion.writeUTF(gson.toJson(m));
        enviarInformacion.flush();
    }

    @Override
    public void run() {
        try {
            recibirInformacion();
        } catch (EOFException ex) {
            mostrarLog("Flujo de datos cerrados.");
            subjectHelper.notify(ManejoComunicacion.class, "Desconectando", TipoNotificacion.DESCONEXION);
        } catch (IOException ex) {
            Logger.getLogger(ManejoComunicacion.class.getName()).log(Level.SEVERE, null, ex);
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
     *
     * @param log
     */
    private void mostrarLog(String log) {
        System.out.println(log);
        subjectHelper.notify(ManejoComunicacion.class, log, TipoNotificacion.LOG);
    }
}
