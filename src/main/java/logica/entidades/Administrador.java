package logica.entidades;

public class Administrador extends Usuario {
    public Administrador() {
        super();
        this.tipo = TipoUsuario.ADMINISTRADOR;
    }

    public Administrador(String id, String nombre) {
        super(id, nombre, id, TipoUsuario.ADMINISTRADOR);
    }

    // Métodos específicos
    public boolean puedeGestionarUsuarios(){
        return true;
    }

    public boolean puedeGestionarCatalogos(){
        return true;
    }

    @Override
    public String toString(){
        return "Admin. " + nombre + " (" + id + ") ";
    }
}
