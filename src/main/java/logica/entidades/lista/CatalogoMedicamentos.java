package logica.entidades.lista;

import logica.entidades.Medicamento;

import javax.print.attribute.standard.Media;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class CatalogoMedicamentos implements Iterable<Medicamento> {
    private Nodo<Medicamento> cabeza;
    int tam;

    public CatalogoMedicamentos(){
        this.cabeza = null;
        this.tam = 0;
    }

    public Iterator<Medicamento> iterator() {
        return new Iterator<Medicamento>() {
            private Nodo<Medicamento> actual = cabeza;

            @Override
            public boolean hasNext() {
                return actual != null;
            }

            @Override
            public Medicamento next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                Medicamento dato = actual.getDato();
                actual = actual.getSiguiente();
                return dato;
            }
        };
    }

    public void agregarMedicamento(Medicamento medicamento) {
        if (medicamento == null || !medicamento.esValido()) {
            throw new IllegalArgumentException("Medicamento inválido");
        }

        Nodo<Medicamento> nuevo = new Nodo<>(medicamento);
        nuevo.setSiguiente(cabeza);
        cabeza = nuevo;
        tam++;

    }

    public Medicamento buscarMedicamentoCodigo(String codigo){
        Nodo<Medicamento> actual = cabeza;
        while (actual != null) {
            if (actual.getDato().getCodigo().equalsIgnoreCase(codigo)) {
                return actual.getDato();
            }
            actual = actual.getSiguiente();
        }
        return null;
    }

    public Medicamento buscarMedicamentoDescripcion(String descripcion){
        Nodo<Medicamento> actual = cabeza;
        while (actual != null) {
            if (actual.getDato().getDescripcionCompleta().toLowerCase().contains(descripcion.toLowerCase())) {
                return actual.getDato();
            }
            actual = actual.getSiguiente();
        }
        return null;
    }

    public Boolean modificarMedicamento(String codigo,Medicamento medicamento){
        if (medicamento == null || !medicamento.esValido()) {
            return false;
        }

        Nodo<Medicamento> actual = cabeza;
        while (actual != null || actual.getDato().getCodigo().equalsIgnoreCase(codigo)) {
            if (actual.getDato().getCodigo().equalsIgnoreCase(codigo)) {
                actual.setDato(medicamento);
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    public Boolean eliminarMedicamento(String codigo){
        if (cabeza == null || codigo == null) {
            return false;
        }

        if (cabeza.getDato().getCodigo().equals(codigo)) {
            cabeza = cabeza.getSiguiente();
            return true;
        }

        Nodo<Medicamento> actual = cabeza;
        while (actual.getSiguiente() != null) {
            if (actual.getSiguiente().getDato().getCodigo().equals(codigo)) {
                actual.setSiguiente(actual.getSiguiente().getSiguiente());
                return true;
            }
            actual = actual.getSiguiente();
        }

        return false;
    }

    public CatalogoMedicamentos medicamentosBajoStock(int umbral) {
        CatalogoMedicamentos aux = new CatalogoMedicamentos();
        if (umbral <= 0 || cabeza == null) {
           Nodo<Medicamento> actual = cabeza;
           while(actual.getSiguiente() != null) {
               if(actual.getDato().getStock() >= umbral) {
                   aux.agregarMedicamento(actual.getDato());
               }
               actual = actual.getSiguiente();
           }
        }else {
            return null;
        }
        return aux;
    }

    @Override
    public String toString() {
        if (cabeza == null) {
            return "No hay medicamentos en el catálogo.";
        }

        StringBuilder sb = new StringBuilder("Catálogo de Medicamentos:\n");
        for (Medicamento m : this) {
            sb.append("- ").append(m.toString()).append("\n");
        }
        return sb.toString();
    }

    public int getTam(){return tam;}

    public Medicamento get(int m) {
        if (m < 0 || m >= tam) {
            throw new IndexOutOfBoundsException("Índice fuera de rango");
        }
        Nodo<Medicamento> actual = cabeza;
        for (int i = 0; i < m; i++) {
            actual = actual.getSiguiente();
        }
        return actual.getDato();
    }
}
