/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clonewhatsapp.Clases;

import java.util.Date;
import javax.swing.JList;

/**
 * Clase para encapsular la información del mensaje.
 * @author vacax
 */
public class Mensaje {

    String usuario;
    Date fecha;
    String mensaje;

    public Mensaje(String usuario, String mensaje, Date fecha) {
        this.usuario = usuario;
        this.mensaje = mensaje;
        this.fecha = fecha;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}

