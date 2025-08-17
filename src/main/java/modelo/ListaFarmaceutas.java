package modelo;

public class ListaFarmaceutas {
    private NodoFarmaceuta primero;
    private int tam;

    public ListaFarmaceutas(){
        this.primero = null;
        this.tam = 0;
    }

    // Agregar final
    public void agregarFinal(Farmaceuta detalle){
        if (detalle != null){
            NodoFarmaceuta nuevo = new NodoFarmaceuta(detalle);

            if (vacia()){
                primero = nuevo;
            } else {
                NodoFarmaceuta actual = primero;
                while (actual.getSiguiente() != null) {
                    actual = actual.getSiguiente();
                }
                actual.setSiguiente(nuevo);
            }
            tam++;
        }
    }

    // Agregar inicio
    public void agregarInicio(Farmaceuta detalle){
        if (detalle != null){
            primero = new NodoFarmaceuta(detalle, primero);
            tam++;
        }
    }

    // Eliminar por ID
    public boolean eliminar(String id){
        if(vacia() || id == null){
            return false;
        }

        if(primero.getDato().id.equals(id)){
            primero = primero.getSiguiente();
            tam--;
            return true;
        }

        NodoFarmaceuta actual = primero;
        while(actual.getSiguiente() != null) {
            if(actual.getSiguiente().getDato().id.equals(id)){
                actual.setSiguiente(actual.getSiguiente().getSiguiente());
                tam--;
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    public Farmaceuta buscarPorid(String id){
        if(vacia() || id == null){
            return null;
        }

        NodoFarmaceuta actual = primero;
        while(actual != null){
            if(actual.getDato().id.equals(id)){
                return actual.getDato();
            }
            actual = actual.getSiguiente();
        }
        return null;
    }

    // Verificar si existe
    public boolean contiene(String id){
        return buscarPorid(id) != null;
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
    public Farmaceuta getPrimero(){
        return primero != null ? primero.getDato() : null;
    }

    // Get último
    public Farmaceuta getUltimo(){
        if (vacia()){
            return null;
        }

        NodoFarmaceuta actual = primero;
        while(actual.getSiguiente() != null) {
            actual = actual.getSiguiente();
        }
        return actual.getDato();
    }

    // Pasar a vector
    public Farmaceuta[] toArray(){
        Farmaceuta[] array = new Farmaceuta[tam];
        NodoFarmaceuta actual = primero;
        int i = 0;

        while(actual != null){
            array[i] = actual.getDato();
            actual = actual.getSiguiente();
            i++;
        }
        return array;
    }

    public boolean modificar(int i, Farmaceuta detalle){
        if (i < 0 || i >= tam || detalle == null){
            return false;
        }

        NodoFarmaceuta actual = primero;
        for (int j = 0; j < i; j++){
            actual = actual.getSiguiente();
        }

        actual.setDato(detalle);
        return true;
    }

    public Farmaceuta obtener(int i){
        if(i < 0 || i >= tam || vacia()){
            return null;
        }

        NodoFarmaceuta actual = primero;
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

        NodoFarmaceuta actual = primero;
        while(actual != null){
            s.append(actual.getDato().getNombre());
            if(actual.getSiguiente() != null){
                s.append(", ");
            }
            actual = actual.getSiguiente();
        }
        return s.toString();
    }
}
