package logica.entidades.lista;

import logica.entidades.DetalleReceta;
import logica.entidades.Medicamento;
import logica.entidades.Usuario;
import logica.entidades.Receta;
import logica.entidades.Paciente;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Lista<T> implements Iterable<T>{
    private Nodo<T> cabeza;
    private int tam;

    public Lista() {
        cabeza = null;
        tam = 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Nodo<T> actual = cabeza;

            @Override
            public boolean hasNext() {
                return actual != null;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                T dato = actual.getDato();
                actual = actual.getSiguiente();
                return dato;
            }
        };
    }

    public int getTam() {
        return tam;
    }
    
    public void agregarFinal(T dato){
        Nodo<T> nuevo = new Nodo<>(dato);
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            Nodo<T> temp = cabeza;
            while (temp.getSiguiente() != null) {
                temp = temp.getSiguiente();
            }
            temp.setSiguiente(nuevo);
        }
        tam++;
    }
    
    public void agregarInicio(T dato){
        Nodo<T> nuevo = new Nodo<>(dato);
        nuevo.setSiguiente(cabeza);
        cabeza = nuevo;
        tam++;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Nodo<T> temp = cabeza;
        while (temp != null) {
            sb.append(temp.getDato()).append(" -> ");
            temp = temp.getSiguiente();
        }
        sb.append("null");
        return sb.toString();
    }

    public boolean eliminar(T dato) {
        if (cabeza == null) {
            return false;
        }

        if (cabeza.getDato().equals(dato)) {
            cabeza = cabeza.getSiguiente();
            return true;
        }

        Nodo<T> actual = cabeza;
        while (actual.getSiguiente() != null) {
            if (actual.getSiguiente().getDato().equals(dato)) {
                actual.setSiguiente(actual.getSiguiente().getSiguiente());
                return true;
            }
            actual = actual.getSiguiente();
        }

        return false;
    }

    public boolean eliminarPorId(String id) {
        if (cabeza == null) {
            return false;
        }

        if (cabeza.getDato() instanceof Usuario) {
            Usuario u = (Usuario) cabeza.getDato();
            if (u.getId().equals(id)) {
                cabeza = cabeza.getSiguiente();
                tam--;
                return true;
            }
        }

        Nodo<T> actual = cabeza;
        while (actual.getSiguiente() != null) {
            if (actual.getSiguiente().getDato() instanceof Usuario) {
                Usuario u = (Usuario) actual.getSiguiente().getDato();
                if (u.getId().equals(id)) {
                    actual.setSiguiente(actual.getSiguiente().getSiguiente());
                    tam--;
                    return true;
                }
            }
            actual = actual.getSiguiente();
        }

        return false;
    }

    public DetalleReceta[] toArrayDetalleReceta() {
        DetalleReceta[] array = new DetalleReceta[tam];
        Nodo<T> temp = cabeza;
        int i = 0;
        while (temp != null) {
            array[i++] = (DetalleReceta) temp.getDato();
            temp = temp.getSiguiente();
        }
        return array;
    }

    public void vaciar(){
        cabeza = null;
        tam = 0;
    }

    public T buscarPorId(String id) {
        Nodo<T> temp = cabeza;
        while (temp != null) {
            T dato = temp.getDato();

            if (dato instanceof Usuario) {
                Usuario u = (Usuario) dato;
                if (u.getId().equals(id)) {
                    return dato;
                }
            }

            else if (dato instanceof Paciente) {
                Paciente p = (Paciente) dato;
                if (p.getId().equals(id)) {
                    return dato;
                }
            }

            else if (dato instanceof Medicamento) {
                Medicamento m = (Medicamento) dato;
                if (m.getCodigo().equals(id)) {
                    return dato;
                }
            }

            else if (dato instanceof DetalleReceta) {
                DetalleReceta d = (DetalleReceta) dato;
                if (d.getCodigoMedicamento().equals(id)) {
                    return dato;
                }
            }

            else if (dato instanceof Receta) {
                Receta r = (Receta) dato;
                if (r.getNumeroReceta().equals(id)) {
                    return dato;
                }
            }

            temp = temp.getSiguiente();
        }
        return null;
    }

    public boolean existe(String id) {
        Nodo<T> temp = cabeza;
        while (temp != null) {
            if (temp.getDato() instanceof Usuario) {
                Usuario u = (Usuario) temp.getDato();
                if (u.getId().equals(id)) {
                    return true;
                }
            }
            temp = temp.getSiguiente();
        }
        return false;
    }

    public boolean actualizarPorId(String id, T nuevoUsuario) {
        Nodo<T> temp = cabeza;
        while (temp != null) {
            if (temp.getDato() instanceof Usuario) {
                Usuario u = (Usuario) temp.getDato();
                if (u.getId().equals(id)) {
                    temp.setDato(nuevoUsuario);
                    return true;
                }
            }
            temp = temp.getSiguiente();
        }
        return false;
    }

    public String mostrarBajoStock(int umbral) {
        StringBuilder sb = new StringBuilder();
        Nodo<T> actual = cabeza;
        boolean hayBajoStock = false;

        while (actual != null) {
            if (actual.getDato() instanceof Medicamento) {
                Medicamento med = (Medicamento) actual.getDato();
                if (med.getStock() <= umbral) {
                    sb.append(med.getCodigoYNombre())
                            .append(" - Stock: ")
                            .append(med.getStock())
                            .append("\n");
                    hayBajoStock = true;
                }
            }
            actual = actual.getSiguiente();
        }

        if (!hayBajoStock) {
            sb.append("No hay medicamentos bajo stock");
        }
        return sb.toString();
    }

    public boolean vacia(){
        return cabeza == null;
    }

    public T obtenerPorPos(int i) {
        if (i < 0 || i >= tam || cabeza == null) {
            return null;
        }

        Nodo<T> actual = cabeza;
        for (int j = 0; j < i; j++) {
            actual = actual.getSiguiente();
        }
        return actual.getDato();
    }

    public boolean modificarPorPos(int i, T detalle) {
        if (i < 0 || i >= tam || detalle == null) {
            return false;
        }

        if (detalle instanceof DetalleReceta) {
            DetalleReceta dr = (DetalleReceta) detalle;
            if (!dr.esValidoPrescripcion()) {
                return false;
            }
        }

        Nodo<T> actual = cabeza;
        for (int j = 0; j < i; j++) {
            actual = actual.getSiguiente();
        }

        actual.setDato(detalle);
        return true;
    }

    public Nodo<T> getCabeza() {
        return cabeza;
    }

    public Nodo<T> getUltimo() {
        if (cabeza == null) {
            return null;
        }

        Nodo<T> actual = cabeza;
        while (actual.getSiguiente() != null) {
            actual = actual.getSiguiente();
        }
        return actual;
    }
}
