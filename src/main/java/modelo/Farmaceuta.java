package modelo;

public class Farmaceuta extends Usuario {
    public Farmaceuta() {
        super();
        this.tipo = TipoUsuario.FARMACEUTA;
    }

    public Farmaceuta(String nombre, String id, String clave) {
        super(id, nombre, clave, TipoUsuario.FARMACEUTA);
    }

    // Métodos específicos
    public boolean puedeDespachar() {
        return true;
    }
    @Override
    public String toString(){
        return "Farm. " + nombre + " (" + id + ") ";
    }
}
