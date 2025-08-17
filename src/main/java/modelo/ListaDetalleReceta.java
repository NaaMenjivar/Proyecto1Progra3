package modelo;

public class ListaDetalleReceta {
    private NodoDetalleReceta primero;
    private int tam;

    public ListaDetalleReceta(){
        this.primero = null;
        this.tam = 0;
    }

    // Agregar final
    public void agregarFinal(DetalleReceta detalle){
        if (detalle != null && detalle.esValidoPrescripcion()){
            NodoDetalleReceta nuevo = new NodoDetalleReceta(detalle);

            if (vacia()){
                primero = nuevo;
            } else {
                NodoDetalleReceta actual = primero;
                while (actual.getSiguiente() != null) {
                    actual = actual.getSiguiente();
                }
                actual.setSiguiente(nuevo);
            }
            tam++;
        }
    }

    // Agregar inicio
    public void agregarInicio(DetalleReceta detalle){
        if (detalle != null && detalle.esValidoPrescripcion()){
            primero = new NodoDetalleReceta(detalle, primero);
            tam++;
        }
    }

    // Eliminar por código
    public boolean eliminar(String codigoMedicamento){
        if(vacia() || codigoMedicamento == null){
            return false;
        }

        if(primero.getDato().getCodigoMedicamento().equals(codigoMedicamento)){
            primero = primero.getSiguiente();
            tam--;
            return true;
        }

        NodoDetalleReceta actual = primero;
        while(actual.getSiguiente() != null) {
            if(actual.getSiguiente().getDato().getCodigoMedicamento().equals(codigoMedicamento)){
                actual.setSiguiente(actual.getSiguiente().getSiguiente());
                tam--;
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    public DetalleReceta buscarPorCodigo(String codigoMedicamento){
        if(vacia() || codigoMedicamento == null){
            return null;
        }

        NodoDetalleReceta actual = primero;
        while(actual != null){
            if(actual.getDato().getCodigoMedicamento().equals(codigoMedicamento)){
                return actual.getDato();
            }
            actual = actual.getSiguiente();
        }
        return null;
    }

    // Verificar si existe
    public boolean contiene(String codigoMedicamento){
        return buscarPorCodigo(codigoMedicamento) != null;
    }

    // Get tam
    public int getTam(){
        return tam;
    }

    // Lista vacía
    public boolean vacia(){
        return primero == null;
    }

    // Vaciar
    public void vaciar(){
        primero = null;
        tam = 0;
    }

    // Get primero
    public DetalleReceta getPrimero(){
        return primero != null ? primero.getDato() : null;
    }

    // Get último
    public DetalleReceta getUltimo(){
        if (vacia()){
            return null;
        }

        NodoDetalleReceta actual = primero;
        while(actual.getSiguiente() != null) {
            actual = actual.getSiguiente();
        }
        return actual.getDato();
    }

    public int getTotalUnidades(){
        int total = 0;
        NodoDetalleReceta actual = primero;

        while(actual!=null){
            total += actual.getDato().getCantidad();
            actual = actual.getSiguiente();
        }
        return total;
    }

    // Todos los detalles son válidos
    public boolean todosLosDetallesSonValidos(){
        NodoDetalleReceta actual = primero;

        while(actual != null){
            if(!actual.getDato().esValidoPrescripcion()){
                return false;
            }
            actual = actual.getSiguiente();
        }
        return true;
    }

    // Pasar a vector
    public DetalleReceta[] toArray(){
        DetalleReceta[] array = new DetalleReceta[tam];
        NodoDetalleReceta actual = primero;
        int i = 0;

        while(actual != null){
            array[i] = actual.getDato();
            actual = actual.getSiguiente();
            i++;
        }
        return array;
    }

    public boolean modificar(int i, DetalleReceta detalle){
        if (i < 0 || i >= tam || detalle == null || !detalle.esValidoPrescripcion()){
            return false;
        }

        NodoDetalleReceta actual = primero;
        for (int j = 0; j < i; j++){
            actual = actual.getSiguiente();
        }

        actual.setDato(detalle);
        return true;
    }

    public DetalleReceta obtener(int i){
        if(i < 0 || i >= tam || vacia()){
            return null;
        }

        NodoDetalleReceta actual = primero;
        for (int j = 0; j < i; j++){
            actual = actual.getSiguiente();
        }
        return actual.getDato();
    }

    @Override
    public String toString(){
        if(vacia()){
            return "Lista vacía";
        }

        StringBuilder s = new StringBuilder();
        s.append("Lista [").append(tam).append(" elementos]: ");

        NodoDetalleReceta actual = primero;
        while(actual != null){
            s.append(actual.getDato().getCodigoMedicamento());
            if(actual.getSiguiente() != null){
                s.append(", ");
            }
            actual = actual.getSiguiente();
        }
        return s.toString();
    }
}
