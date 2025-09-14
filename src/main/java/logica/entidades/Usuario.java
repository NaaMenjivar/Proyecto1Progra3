package logica.entidades;

import java.util.Objects;

public abstract class Usuario {
    protected String id;
    protected String nombre;
    protected String clave;
    protected TipoUsuario tipo;
    private boolean sesionActiva = false;

    public Usuario(){

    }

    public Usuario(String id, String nombre, String clave, TipoUsuario tipo) {
        this.id = id;
        this.nombre = nombre;
        this.clave = clave;
        this.tipo = tipo;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getClave() {
        return clave;
    }
    public void setClave(String clave) {
        this.clave = clave;
    }
    public TipoUsuario getTipo() {
        return tipo;
    }
    public void setTipo(TipoUsuario tipo) {
        this.tipo = tipo;
    }

    public boolean isSesionActiva() {
        return sesionActiva;
    }

    public void setSesionActiva(boolean sesionActiva) {
        this.sesionActiva = sesionActiva;
    }

    public boolean validarCredenciales(String id, String clave){
        return this.id != null && this.clave != null &&
                this.clave.equals(clave) && this.id.equals(id);
    }

    public boolean puedeAccederA(TipoUsuario tipo){
        return this.tipo == tipo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id);
    }

    @Override
    public String toString() {
        return nombre + " (" + id + ") ";
    }
}
