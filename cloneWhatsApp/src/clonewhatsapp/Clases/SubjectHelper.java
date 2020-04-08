package clonewhatsapp.Clases;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase para simplificar el uso del patron Sujeto o Observador.
 */
public class SubjectHelper implements ISubject {

    private final List<IObserver> listaObservadores=new ArrayList();

    @Override
    public void addObserver(IObserver observador) {
        listaObservadores.add(observador);
    }

    @Override
    public void removeObserver(IObserver observador) {
        System.out.println("Removiendo obsevador");
        listaObservadores.remove(observador);
    }

    @Override
    public void notify(Class clase, Object argumento, Enum anEnum){
        for(IObserver observador : listaObservadores){
            observador.update(clase, argumento, anEnum);
        }
    }
    
    /**
     * 
     */
    public void removeAllObserver(){
        listaObservadores.clear();
    }
}
