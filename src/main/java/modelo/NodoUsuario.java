package modelo;

public class NodoUsuario {
    private Usuario dato;
    private NodoUsuario siguiente;

    public NodoUsuario(){
        this.dato = null;
        this.siguiente = null;
    }

    public NodoUsuario(Usuario dato){
        this.dato = dato;
        this.siguiente = null;
    }

    public NodoUsuario(Usuario dato, NodoUsuario siguiente){
        this.dato = dato;
        this.siguiente = siguiente;
    }

    public Usuario getDato() {
        return dato;
    }
    public void setDato(Usuario dato) {
        this.dato = dato;
    }
    public NodoUsuario getSiguiente() {
        return siguiente;
    }
    public void setSiguiente(NodoUsuario siguiente) {
        this.siguiente = siguiente;
    }
}
