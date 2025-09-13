package logica.entidades;

import java.util.Objects;

public class Medicamento {
    private String codigo;
    private String nombre;
    private String presentacion;
    private String indicaciones;
    private int cantidad;
    private int duracion;
    private int stock;

    public Medicamento(){
        this.codigo = "";
        this.nombre = "";
        this.presentacion = "";
        stock = 0;
        this.cantidad = 0;
        this.duracion = 0;
        this.indicaciones = "";
    }

    public Medicamento(String codigo, String nombre, String presentacion) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.presentacion = presentacion;
        stock = 0;
        this.cantidad = 0;
        this.duracion = 0;
        this.indicaciones = "";
    }

    public String getCodigo() {
        return codigo;
    }
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getPresentacion() {
        return presentacion;
    }
    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }
    public int getStock() {
        return stock;
    }
    public String getIndicaciones() {return indicaciones; }
    public void setIndicaciones(String indicaciones) {
        this.indicaciones = indicaciones;
    }
    public int getDuracion() {
        return duracion;
    }
    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }
    public int getCantidad() {
        return cantidad;
    }
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getDescripcionCompleta(){
        return nombre + " - " + presentacion;
    }

    public String getCodigoYNombre(){
        return codigo + " - " + nombre;
    }

    public boolean esValido(){
        return codigo != null && !codigo.trim().isEmpty() &&
                nombre != null && !nombre.trim().isEmpty() &&
                presentacion != null && !presentacion.trim().isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Medicamento medicamento = (Medicamento) o;
        return Objects.equals(codigo, medicamento.codigo);
    }

    @Override
    public String toString() {
        return getDescripcionCompleta() + " (" + codigo + ")";
    }
}
