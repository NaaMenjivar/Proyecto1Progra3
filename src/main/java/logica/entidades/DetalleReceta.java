package logica.entidades;

import java.util.Objects;

public class DetalleReceta {
    private String codigoMedicamento;
    private int cantidad;
    private String indicaciones;
    private int duracionDias;

    public DetalleReceta() {
    }

    public DetalleReceta(String codigoMedicamento, int cantidad, String indicaciones, int duracionDias) {
        this.codigoMedicamento = codigoMedicamento;
        this.cantidad = cantidad;
        this.indicaciones = indicaciones;
        this.duracionDias = duracionDias;
    }

    public String getCodigoMedicamento() {
        return codigoMedicamento;
    }
    public void setCodigoMedicamento(String codigoMedicamento) {
        this.codigoMedicamento = codigoMedicamento;
    }
    public int getCantidad() {
        return cantidad;
    }
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    public String getIndicaciones() {
        return indicaciones;
    }
    public void setIndicaciones(String indicaciones) {
        this.indicaciones = indicaciones;
    }
    public int getDuracionDias() {
        return duracionDias;
    }
    public void setDuracionDias(int duracionDias) {
        this.duracionDias = duracionDias;
    }

    public boolean esValidoPrescripcion(){
        return codigoMedicamento != null && !codigoMedicamento.trim().isEmpty() &&
                cantidad > 0 && duracionDias > 0 &&
                indicaciones != null && !indicaciones.trim().isEmpty();
    }

    public String getDuracionTexto(){
        return duracionDias + (duracionDias == 1 ? " día" : " días");
    }

    public String getCantidadTexto(){
        return cantidad + (cantidad == 1 ? " unidad" : " unidades");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DetalleReceta that = (DetalleReceta) o;
        return Objects.equals(codigoMedicamento, that.codigoMedicamento);
    }

    @Override
    public String toString() {
        return codigoMedicamento + " - " + getCantidadTexto() + " por " + getDuracionTexto();
    }
}
