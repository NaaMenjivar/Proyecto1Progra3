package logica.entidades;

import java.util.Objects;

public class Medicamento {
    private String codigo;
    private String nombre;
    private String presentacion;
    private int stock;

    public Medicamento(){
        stock = 0;
    }

    public Medicamento(String codigo, String nombre, String presentacion) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.presentacion = presentacion;
        stock = 0;
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

    public void setStock(int stock) {
        this.stock = stock;
    }

    // Métodos específicos
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

    // Métodos propios de Object
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Medicamento medicamento = (Medicamento) o;
        return Objects.equals(codigo, medicamento.codigo);
    }

    /*@Override
    public int hashCode() {
        return Objects.hash(codigo);
    }*/

    @Override
    public String toString() {
        return getDescripcionCompleta() + " (" + codigo + ")";
    }
}
