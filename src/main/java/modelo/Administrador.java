package modelo;

public class Administrador extends Usuario {
    public Administrador() {
        super();
        this.tipo = TipoUsuario.ADMINISTRADOR;
    }

    public Administrador(String id, String nombre, String clave) {
        super(id, nombre, clave, TipoUsuario.ADMINISTRADOR);
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
