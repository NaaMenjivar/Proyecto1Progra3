package modelo;

public class ListaMedicamentos {
    private NodoMedicamento primero;
    private int tam;

    public ListaMedicamentos(){
        this.primero = null;
        this.tam = 0;
    }

    // Agregar final
    public void agregarFinal(Medicamento detalle){
        if (detalle != null && detalle.esValido()){
            NodoMedicamento nuevo = new NodoMedicamento(detalle);

            if (vacia()){
                primero = nuevo;
            } else {
                NodoMedicamento actual = primero;
                while (actual.getSiguiente() != null) {
                    actual = actual.getSiguiente();
                }
                actual.setSiguiente(nuevo);
            }
            tam++;
        }
    }

    // Agregar inicio
    public void agregarInicio(Medicamento detalle){
        if (detalle != null && detalle.esValido()){
            primero = new NodoMedicamento(detalle, primero);
            tam++;
        }
    }

    // Eliminar por código
    public boolean eliminar(String codigo){
        if(vacia() || codigo == null){
            return false;
        }

        if(primero.getDato().getCodigo().equals(codigo)){
            primero = primero.getSiguiente();
            tam--;
            return true;
        }

        NodoMedicamento actual = primero;
        while(actual.getSiguiente() != null) {
            if(actual.getSiguiente().getDato().getCodigo().equals(codigo)){
                actual.setSiguiente(actual.getSiguiente().getSiguiente());
                tam--;
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    public Medicamento buscarPorCodigo(String codigo){
        if(vacia() || codigo == null){
            return null;
        }

        NodoMedicamento actual = primero;
        while(actual != null){
            if(actual.getDato().getCodigo().equals(codigo)){
                return actual.getDato();
            }
            actual = actual.getSiguiente();
        }
        return null;
    }

    // Verificar si existe
    public boolean contiene(String codigo){
        return buscarPorCodigo(codigo) != null;
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
    public Medicamento getPrimero(){
        return primero != null ? primero.getDato() : null;
    }

    // Get último
    public Medicamento getUltimo(){
        if (vacia()){
            return null;
        }

        NodoMedicamento actual = primero;
        while(actual.getSiguiente() != null) {
            actual = actual.getSiguiente();
        }
        return actual.getDato();
    }

    // Todos los detalles son válidos
    public boolean todosLosDetallesSonValidos(){
        NodoMedicamento actual = primero;

        while(actual != null){
            if(!actual.getDato().esValido()){
                return false;
            }
            actual = actual.getSiguiente();
        }
        return true;
    }

    // Pasar a vector
    public Medicamento[] toArray(){
        Medicamento[] array = new Medicamento[tam];
        NodoMedicamento actual = primero;
        int i = 0;

        while(actual != null){
            array[i] = actual.getDato();
            actual = actual.getSiguiente();
            i++;
        }
        return array;
    }

    public boolean modificar(int i, Medicamento detalle){
        if (i < 0 || i >= tam || detalle == null || !detalle.esValido()){
            return false;
        }

        NodoMedicamento actual = primero;
        for (int j = 0; j < i; j++){
            actual = actual.getSiguiente();
        }

        actual.setDato(detalle);
        return true;
    }

    public Medicamento obtener(int i){
        if(i < 0 || i >= tam || vacia()){
            return null;
        }

        NodoMedicamento actual = primero;
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

        NodoMedicamento actual = primero;
        while(actual != null){
            s.append(actual.getDato().getNombre());
            if(actual.getSiguiente() != null){
                s.append(", ");
            }
            actual = actual.getSiguiente();
        }
        return s.toString();
    }

    public void mostrarBajoStock(int umbral) {
        NodoMedicamento actual = primero;
        boolean hayBajoStock = false;
        while (actual != null) {
            Medicamento med = actual.getDato();
            if (med.getStock() <= umbral) {
                System.out.println(med.getCodigoYNombre() + " - Stock: " + med.getStock());
                hayBajoStock = true;
            }
            actual = actual.getSiguiente();
        }
        if (!hayBajoStock) {
            System.out.println("No hay medicamentos con bajo stock.");
        }
    }
}
