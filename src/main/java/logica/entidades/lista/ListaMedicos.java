package logica.entidades.lista;

import logica.entidades.Farmaceuta;
import logica.entidades.Medico;
import logica.entidades.Medicamento;
import logica.entidades.Medico;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ListaMedicos implements Iterable<Medico> {
    private Nodo<Medico> cabeza;
    int tam;

    public ListaMedicos() {
        this.cabeza = null;
        this.tam = 0;
    }

    public Iterator<Medico> iterator() {
        return new Iterator<Medico>() {
            private Nodo<Medico> actual = cabeza;

            @Override
            public boolean hasNext() {
                return actual != null;
            }

            @Override
            public Medico next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                Medico dato = actual.getDato();
                actual = actual.getSiguiente();
                return dato;
            }
        };
    }

    public void agregarMedico(Medico medico) {
        if (medico == null) {
            throw new IllegalArgumentException("El farmaceuta no puede ser nulo");
        }

        Nodo<Medico> nuevo = new Nodo<>(medico);
        nuevo.setSiguiente(cabeza);
        cabeza = nuevo;
    }
    
    public Medico buscarMedicoId(String id) {
        if (cabeza == null || id == null) {
            return null;
        }

        Nodo<Medico> actual = cabeza;
        while (actual != null) {
            if (actual.getDato().getId().equalsIgnoreCase(id)) {
                return actual.getDato();
            }
            actual = actual.getSiguiente();
        }
        return null;
    }

    public Medico buscarMedicoNombre(String nombre) {
        if (nombre.equals("")) {
            return null;
        }

        Nodo<Medico> actual = cabeza;
        while (actual != null) {
            if (actual.getDato().getNombre().equalsIgnoreCase(nombre)) {
                return actual.getDato();
            }
            actual = actual.getSiguiente();
        }
        return null;
    }

    public Boolean modificarMedico(Medico farmaceuta) {
        if (farmaceuta == null || farmaceuta.getId() == null) {
            return false;
        }

        Nodo<Medico> actual = cabeza;
        while (actual != null) {
            if (actual.getDato().getId().equalsIgnoreCase(farmaceuta.getId())) {
                actual.getDato().setNombre(farmaceuta.getNombre());
                actual.getDato().setClave(farmaceuta.getClave());
                actual.getDato().setTipo(farmaceuta.getTipo());
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    public Boolean eliminarMedico(String id) {
        if (id == null || cabeza == null) {
            return false;
        }

        if (cabeza.getDato().getId().equals(id)) {
            cabeza = cabeza.getSiguiente();
            return true;
        }

        Nodo<Medico> actual = cabeza;
        while (actual.getSiguiente() != null) {
            if (actual.getSiguiente().getDato().getId().equals(id)) {
                actual.setSiguiente(actual.getSiguiente().getSiguiente());
                return true;
            }
            actual = actual.getSiguiente();
        }

        return false;
    }

    public int getTam(){return tam;}
}
