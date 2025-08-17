package modelo;

public class NodoPaciente {
    private Paciente dato;
    private NodoPaciente siguiente;

    public NodoPaciente() {
        this.dato = null;
        this.siguiente = null;
    }

    public NodoPaciente(Paciente dato){
        this.dato = dato;
        this.siguiente = null;
    }

    public NodoPaciente(Paciente dato, NodoPaciente siguiente){
        this.dato = dato;
        this.siguiente = siguiente;
    }

    public Paciente getDato() {
        return dato;
    }
    public void setDato(Paciente dato) {
        this.dato = dato;
    }
    public NodoPaciente getSiguiente() {
        return siguiente;
    }
    public void setSiguiente(NodoPaciente siguiente) {
        this.siguiente = siguiente;
    }
}
