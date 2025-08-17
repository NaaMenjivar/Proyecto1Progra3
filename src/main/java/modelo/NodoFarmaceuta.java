package modelo;

public class NodoFarmaceuta {
    private Farmaceuta dato;
    private NodoFarmaceuta siguiente;

    public NodoFarmaceuta(){
        this.dato = null;
        this.siguiente = null;
    }

    public NodoFarmaceuta(Farmaceuta dato){
        this.dato = dato;
        this.siguiente = null;
    }

    public NodoFarmaceuta(Farmaceuta dato, NodoFarmaceuta siguiente){
        this.dato = dato;
        this.siguiente = siguiente;
    }

    public Farmaceuta getDato() {
        return dato;
    }
    public void setDato(Farmaceuta dato) {
        this.dato = dato;
    }
    public NodoFarmaceuta getSiguiente() {
        return siguiente;
    }
    public void setSiguiente(NodoFarmaceuta siguiente) {
        this.siguiente = siguiente;
    }
}
