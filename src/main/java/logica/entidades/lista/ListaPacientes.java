package logica.entidades.lista;

import logica.entidades.Paciente;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ListaPacientes implements Iterable<Paciente> {
    private Nodo<Paciente> cabeza;
    int tam;

    public ListaPacientes() {
        this.cabeza = null;
        this.tam = 0;
    }

    public Iterator<Paciente> iterator() {
        return new Iterator<Paciente>() {
            private Nodo<Paciente> actual = cabeza;

            @Override
            public boolean hasNext() {
                return actual != null;
            }

            @Override
            public Paciente next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                Paciente dato = actual.getDato();
                actual = actual.getSiguiente();
                return dato;
            }
        };
    }

    public void agregarPaciente(Paciente medico) {
        if (medico == null) {
            throw new IllegalArgumentException("El farmaceuta no puede ser nulo");
        }

        Nodo<Paciente> nuevo = new Nodo<>(medico);
        nuevo.setSiguiente(cabeza);
        cabeza = nuevo;
    }

    public Paciente buscarPacienteId(String id) {
        if (id == null || id.isEmpty()) {
            return null;
        }

        Nodo<Paciente> actual = cabeza;
        while (actual != null) {
            if (actual.getDato().getId().equalsIgnoreCase(id)) {
                return actual.getDato();
            }
            actual = actual.getSiguiente();
        }
        return null;
    }

    public Paciente buscarPacienteNombre(String nombre) {
        if (nombre == null ||  nombre.isEmpty()) {
            return null;
        }

        Nodo<Paciente> actual = cabeza;
        while (actual != null) {
            if (actual.getDato().getNombre().equalsIgnoreCase(nombre)) {
                return actual.getDato();
            }
            actual = actual.getSiguiente();
        }
        return null;
    }

    public Boolean eliminarPaciente(String id) {
        if (id == null || id.isEmpty()) {
            return false;
        }

        if (cabeza.getDato().getId().equals(id)) {
            cabeza = cabeza.getSiguiente();
            return true;
        }

        Nodo<Paciente> actual = cabeza;
        while (actual.getSiguiente() != null) {
            if (actual.getSiguiente().getDato().getId().equals(id)) {
                actual.setSiguiente(actual.getSiguiente().getSiguiente());
                return true;
            }
            actual = actual.getSiguiente();
        }

        return false;
    }

    public boolean actualizarPaciente(Paciente paciente){

        return true;
    }

    public int getTam(){return tam;}
}
