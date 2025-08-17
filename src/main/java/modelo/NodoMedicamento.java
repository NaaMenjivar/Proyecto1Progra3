package modelo;

public class NodoMedicamento {
    private Medicamento dato;
    private NodoMedicamento siguiente;

    public NodoMedicamento(){
        this.dato = null;
        this.siguiente = null;
    }

    public NodoMedicamento(Medicamento dato){
        this.dato = dato;
        this.siguiente = null;
    }

    public NodoMedicamento(Medicamento dato, NodoMedicamento siguiente){
        this.dato = dato;
        this.siguiente = siguiente;
    }

    public Medicamento getDato() {
        return dato;
    }
    public void setDato(Medicamento dato) {
        this.dato = dato;
    }
    public NodoMedicamento getSiguiente() {
        return siguiente;
    }
    public void setSiguiente(NodoMedicamento siguiente) {
        this.siguiente = siguiente;
    }
}
