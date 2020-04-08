package clonewhatsapp.Clases;

import java.io.Serializable;


public interface IObserver extends Serializable {

    public void update(Class clase,Object argumento,Enum anEnum);
}