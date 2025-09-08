package logica.entidades;

public enum EstadoReceta {
    CONFECCIONADA("Confeccionada"),
    PROCESO("En proceso"),
    LISTA("Lista"),
    ENTREGADA("Entregada");

    private String descripcion;
    EstadoReceta(String descripcion){
        this.descripcion = descripcion;
    }

    public String getDescripcion()
    {
        return descripcion;
    }

    @Override
    public String toString()
    {
        return descripcion;
    }
}
