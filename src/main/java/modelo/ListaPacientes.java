package modelo;

public class ListaPacientes {
    private NodoPaciente primero;
    private int tam;

    public ListaPacientes(){
        this.primero = null;
        this.tam = 0;
    }

    // Agregar final
    public void agregarFinal(Paciente paciente){
        if (paciente != null){
            NodoPaciente nuevo = new NodoPaciente(paciente);

            if (vacia()){
                primero = nuevo;
            } else {
                NodoPaciente actual = primero;
                while (actual.getSiguiente() != null) {
                    actual = actual.getSiguiente();
                }
                actual.setSiguiente(nuevo);
            }
            tam++;
        }
    }

    // Agregar inicio
    public void agregarInicio(Paciente paciente){
        if (paciente != null){
            primero = new NodoPaciente(paciente, primero);
            tam++;
        }
    }

    // Eliminar por ID
    public boolean eliminar(String id){
        if(vacia() || id == null){
            return false;
        }

        if(primero.getDato().getId().equals(id)){
            primero = primero.getSiguiente();
            tam--;
            return true;
        }

        NodoPaciente actual = primero;
        while(actual.getSiguiente() != null) {
            if(actual.getSiguiente().getDato().getId().equals(id)){
                actual.setSiguiente(actual.getSiguiente().getSiguiente());
                tam--;
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    public Paciente buscarPorID(String id){
        if(vacia() || id == null){
            return null;
        }

        NodoPaciente actual = primero;
        while(actual != null){
            if(actual.getDato().getId().equals(id)){
                return actual.getDato();
            }
            actual = actual.getSiguiente();
        }
        return null;
    }

    // Verificar si existe
    public boolean contiene(String id){
        return buscarPorID(id) != null;
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
    public Paciente getPrimero(){
        return primero != null ? primero.getDato() : null;
    }

    // Get último
    public Paciente getUltimo(){
        if (vacia()){
            return null;
        }

        NodoPaciente actual = primero;
        while(actual.getSiguiente() != null) {
            actual = actual.getSiguiente();
        }
        return actual.getDato();
    }

    // Pasar a vector
    public Paciente[] toArray(){
        Paciente[] array = new Paciente[tam];
        NodoPaciente actual = primero;
        int i = 0;

        while(actual != null){
            array[i] = actual.getDato();
            actual = actual.getSiguiente();
            i++;
        }
        return array;
    }

    public boolean modificar(int i, Paciente paciente){
        if (i < 0 || i >= tam || paciente == null){
            return false;
        }

        NodoPaciente actual = primero;
        for (int j = 0; j < i; j++){
            actual = actual.getSiguiente();
        }

        actual.setDato(paciente);
        return true;
    }

    public Paciente obtener(int i){
        if(i < 0 || i >= tam || vacia()){
            return null;
        }

        NodoPaciente actual = primero;
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

        NodoPaciente actual = primero;
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
