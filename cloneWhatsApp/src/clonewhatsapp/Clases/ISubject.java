package clonewhatsapp.Clases;

import java.io.Serializable;


public interface ISubject extends Serializable {

    public void addObserver(IObserver observador);
    public void removeObserver(IObserver observador);
    public void notify(Class clase,Object argumento,Enum anEnum);
}