package modelo;

public class ListaUsuarios {
    private NodoUsuario primero;
    private int tam;

    public ListaUsuarios() {
        this.primero = null;
        this.tam = 0;
    }

    // Agregar final
    public void agregarFinal(Usuario detalle) {
        if (detalle != null && detalle.validarCredenciales(detalle.id, detalle.clave)) {
            NodoUsuario nuevo = new NodoUsuario(detalle);

            if (vacia()) {
                primero = nuevo;
            } else {
                NodoUsuario actual = primero;
                while (actual.getSiguiente() != null) {
                    actual = actual.getSiguiente();
                }
                actual.setSiguiente(nuevo);
            }
            tam++;
        }
    }

    // Agregar inicio
    public void agregarInicio(Usuario detalle) {
        if (detalle != null && detalle.validarCredenciales(detalle.id, detalle.clave)) {
            primero = new NodoUsuario(detalle, primero);
            tam++;
        }
    }

    // Eliminar por código
    public boolean eliminar(String id) {
        if (vacia() || id == null) {
            return false;
        }

        if (primero.getDato().getId().equals(id)) {
            primero = primero.getSiguiente();
            tam--;
            return true;
        }

        NodoUsuario actual = primero;
        while (actual.getSiguiente() != null) {
            if (actual.getSiguiente().getDato().getId().equals(id)) {
                actual.setSiguiente(actual.getSiguiente().getSiguiente());
                tam--;
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    public Usuario buscarPorId(String id) {
        if (vacia() || id == null) {
            return null;
        }

        NodoUsuario actual = primero;
        while (actual != null) {
            if (actual.getDato().getId().equals(id)) {
                return actual.getDato();
            }
            actual = actual.getSiguiente();
        }
        return null;
    }

    // Verificar si existe
    public boolean contiene(String id) {
        return buscarPorId(id) != null;
    }

    // Get tam
    public int getTam() {
        return tam;
    }

    // Lista vacía
    public boolean vacia() {
        return primero == null;
    }

    // Vaciar
    public void vaciar() {
        primero = null;
        tam = 0;
    }

    // Get primero
    public Usuario getPrimero() {
        return primero != null ? primero.getDato() : null;
    }

    // Get último
    public Usuario getUltimo() {
        if (vacia()) {
            return null;
        }

        NodoUsuario actual = primero;
        while (actual.getSiguiente() != null) {
            actual = actual.getSiguiente();
        }
        return actual.getDato();
    }

    // Todos los detalles son válidos
    public boolean todosLosDetallesSonValidos() {
        NodoUsuario actual = primero;

        while (actual != null) {
            if (!actual.getDato().validarCredenciales(actual.getDato().getId(), actual.getDato().getClave())) {
                return false;
            }
            actual = actual.getSiguiente();
        }
        return true;
    }

    public boolean modificar(int i, Usuario detalle) {
        if (i < 0 || i >= tam || detalle == null || !detalle.validarCredenciales(detalle.getId(), detalle.getClave())) {
            return false;
        }

        NodoUsuario actual = primero;
        for (int j = 0; j < i; j++) {
            actual = actual.getSiguiente();
        }

        actual.setDato(detalle);
        return true;
    }

    public Usuario obtener(int i) {
        if (i < 0 || i >= tam || vacia()) {
            return null;
        }

        NodoUsuario actual = primero;
        for (int j = 0; j < i; j++) {
            actual = actual.getSiguiente();
        }
        return actual.getDato();
    }

    @Override
    public String toString() {
        if (vacia()) {
            return "Lista vacía";
        }

        StringBuilder s = new StringBuilder();
        s.append("Lista [").append(tam).append(" elementos]: ");

        NodoUsuario actual = primero;
        while (actual != null) {
            s.append(actual.getDato().getNombre());
            if (actual.getSiguiente() != null) {
                s.append(", ");
            }
            actual = actual.getSiguiente();
        }
        return s.toString();
    }
}