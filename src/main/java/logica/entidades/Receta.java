package logica.entidades;

import logica.entidades.lista.Lista;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Receta {
    private String numeroReceta;
    private String idPaciente;
    private String idMedico;
    private LocalDate fechaConfeccion;
    private LocalDate fechaRetiro;
    private EstadoReceta estado;
    private Lista<DetalleReceta> detalles;

    public Receta(){
        this.detalles = new Lista<DetalleReceta>();
        this.estado = EstadoReceta.CONFECCIONADA;
        this.fechaConfeccion = LocalDate.now();
    }

    public Receta(String numeroReceta, String idPaciente, String idMedico, LocalDate fechaRetiro){
        this();
        this.numeroReceta = numeroReceta;
        this.idPaciente = idPaciente;
        this.idMedico = idMedico;
        this.fechaRetiro = fechaRetiro;
    }


    public String getNumeroReceta() {
        return numeroReceta;
    }
    public void setNumeroReceta(String numeroReceta) {
        this.numeroReceta = numeroReceta;
    }
    public String getIdPaciente() {
        return idPaciente;
    }
    public void setIdPaciente(String idPaciente) {
        this.idPaciente = idPaciente;
    }
    public String getIdMedico() {
        return idMedico;
    }
    public void setIdMedico(String idMedico) {
        this.idMedico = idMedico;
    }
    public LocalDate getFechaConfeccion() {
        return fechaConfeccion;
    }
    public void setFechaConfeccion(LocalDate fechaConfeccion) {
        this.fechaConfeccion = fechaConfeccion;
    }
    public LocalDate getFechaRetiro() {
        return fechaRetiro;
    }
    public void setFechaRetiro(LocalDate fechaRetiro) {
        this.fechaRetiro = fechaRetiro;
    }
    public EstadoReceta getEstado() {
        return estado;
    }
    public void setEstado(EstadoReceta estado) {
        this.estado = estado;
    }
    public Lista<DetalleReceta> getDetalles() {
        return detalles;
    }

    // Gestión de detalles
    public void agregarDetalle(DetalleReceta detalle){
        detalles.agregarFinal(detalle);
    }

    public boolean eliminarDetalle(String codigoMedicamento){
        return detalles.eliminarPorId(codigoMedicamento);
    }

    public void modificarDetalle(int i, DetalleReceta detalle){
        detalles.modificarPorPos(i, detalle);
    }

    public DetalleReceta obtenerDetalle(int i){
        return detalles.obtenerPorPos(i);
    }

    public boolean tieneDetalles(){
        return !detalles.vacia();
    }

    public int getTotalMedicamentos(){
        return detalles.getTam();
    }

    public int getTotalUnidades(){
        return detalles.getTam();
    }

    // Métodos de librerías
    public boolean puedeSerDespachada(LocalDate fecha){
        if (estado != EstadoReceta.CONFECCIONADA){
            return false;
        }
        long diferenciaDias = ChronoUnit.DAYS.between(fechaRetiro, fecha);
        return diferenciaDias >= -3 && diferenciaDias <= 3;
    }

    public boolean puedeAvanzarAEstado(EstadoReceta nuevoEstado) {
        switch (estado) {
            case CONFECCIONADA:
                return nuevoEstado == EstadoReceta.PROCESO;
            case PROCESO:
                return nuevoEstado == EstadoReceta.LISTA;
            case LISTA:
                return nuevoEstado == EstadoReceta.ENTREGADA;
            case ENTREGADA:
                return false; // No puede cambiar una vez entregada
            default:
                return false;
        }
    }

    public String getResumenMedicamentos(){
        if(detalles.vacia()){
            return "Sin medicamentos";
        }
        return getTotalMedicamentos() + " medicamento(s) - " + getTotalUnidades() + " unidad(es)";
    }

    @Override
    public String toString() {
        return numeroReceta + " - " + estado.getDescripcion() +
                " (" + (fechaConfeccion != null ? fechaConfeccion.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "") +
                ") - " + getResumenMedicamentos();
    }
}
