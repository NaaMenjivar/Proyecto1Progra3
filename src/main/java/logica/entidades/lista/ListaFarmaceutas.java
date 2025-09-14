package logica.entidades.lista;

import logica.entidades.Farmaceuta;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ListaFarmaceutas implements Iterable<Farmaceuta> {
    private Nodo<Farmaceuta> cabeza;
    int tam = 0;

    public ListaFarmaceutas() {
        this.cabeza = null;
        this.tam = 0;
    }

    public Iterator<Farmaceuta> iterator() {
        return new Iterator<Farmaceuta>() {
            private Nodo<Farmaceuta> actual = cabeza;

            @Override
            public boolean hasNext() {
                return actual != null;
            }

            @Override
            public Farmaceuta next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                Farmaceuta dato = actual.getDato();
                actual = actual.getSiguiente();
                return dato;
            }
        };
    }

    public void agregarFarmaceuta(Farmaceuta farmaceuta) {
        if (farmaceuta == null) {
            throw new IllegalArgumentException("El farmaceuta no puede ser nulo");
        }

        Nodo<Farmaceuta> nuevo = new Nodo<>(farmaceuta);
        nuevo.setSiguiente(cabeza);
        cabeza = nuevo;
        tam++;
    }

    public Farmaceuta buscarFarmaceutaId(String id) {
        if (cabeza == null || id == null) {
            return null;
        }

        Nodo<Farmaceuta> actual = cabeza;
        while (actual != null) {
            if (actual.getDato().getId().equalsIgnoreCase(id)) {
                return actual.getDato();
            }
            actual = actual.getSiguiente();
        }
        return null;
    }

    public Farmaceuta buscarFarmaceutaNombre(String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            return null;
        }

        Nodo<Farmaceuta> actual = cabeza;
        while (actual != null) {
            if (actual.getDato().getNombre().equalsIgnoreCase(nombre)) {
                return actual.getDato();
            }
            actual = actual.getSiguiente();
        }
        return null;
    }

    public Boolean modificarFarmaceuta(Farmaceuta farmaceuta) {
        if (farmaceuta == null || farmaceuta.getId() == null) {
            return false;
        }

        Nodo<Farmaceuta> actual = cabeza;
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

    public Boolean eliminarFarmaceuta(String id) {
        if (id == null || cabeza == null) {
            return false;
        }

        if (cabeza.getDato().getId().equals(id)) {
            cabeza = cabeza.getSiguiente();
            return true;
        }

        Nodo<Farmaceuta> actual = cabeza;
        while (actual.getSiguiente() != null) {
            if (actual.getSiguiente().getDato().getId().equals(id)) {
                actual.setSiguiente(actual.getSiguiente().getSiguiente());
                return true;
            }
            actual = actual.getSiguiente();
        }

        return false;
    }

    @Override
    public String toString() {
        if (cabeza == null) {
            return "No hay farmaceutas registrados.";
        }

        StringBuilder sb = new StringBuilder("Lista de Farmaceutas:\n");
        for (Farmaceuta f : this) {
            sb.append("- ").append(f.toString()).append("\n");
        }
        return sb.toString();
    }

    public int getTam(){return tam;}
}
