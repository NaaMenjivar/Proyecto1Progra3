package modelo;

public enum TipoUsuario{
    MEDICO("Médico"),
    FARMACEUTA("Farmaceuta"),
    ADMINISTRADOR("Administrador");

    private String descripcion;
    TipoUsuario(String descripcion){
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
