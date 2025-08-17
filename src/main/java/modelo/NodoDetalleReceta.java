package modelo;

public class NodoDetalleReceta {
    private DetalleReceta dato;
    private NodoDetalleReceta siguiente;

    public NodoDetalleReceta(){
        this.dato = null;
        this.siguiente = null;
    }

    public NodoDetalleReceta(DetalleReceta dato){
        this.dato = dato;
        this.siguiente = null;
    }

    public NodoDetalleReceta(DetalleReceta dato,NodoDetalleReceta siguiente){
        this.dato = dato;
        this.siguiente = siguiente;
    }

    public DetalleReceta getDato() {
        return dato;
    }
    public void setDato(DetalleReceta dato) {
        this.dato = dato;
    }
    public NodoDetalleReceta getSiguiente() {
        return siguiente;
    }
    public void setSiguiente(NodoDetalleReceta siguiente) {
        this.siguiente = siguiente;
    }
}
