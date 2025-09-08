package logica.entidades;

import java.util.Objects;

public class Paciente {
    private String id;
    private String nombre;
    private String fechaNacimiento;  // Cambiado a String
    private String telefono;

    public Paciente(){
    }

    public Paciente(String id, String nombre, String fechaNacimiento, String telefono){
        this.id = id;
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
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
    public String getFechaNacimiento() {  // Cambiado a String
        return fechaNacimiento;
    }
    public void setFechaNacimiento(String fechaNacimiento) {  // Cambiado a String
        this.fechaNacimiento = fechaNacimiento;
    }
    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    // Métodos específicos simplificados
    public String getEdad(){
        // Simplificado - retorna la fecha como string o mensaje
        if(fechaNacimiento == null || fechaNacimiento.trim().isEmpty()){
            return "No especificada";
        }
        return fechaNacimiento; // Retorna la fecha directamente
    }

    public boolean esMayorDeEdad(){
        if(fechaNacimiento == null || fechaNacimiento.trim().isEmpty()){
            return false;
        }

        // Intentar calcular edad desde formato "dd/MM/yyyy" o "yyyy" o similar
        try {
            String fecha = fechaNacimiento.trim();
            int añoNacimiento;

            // Si contiene "/" asumimos formato dd/MM/yyyy
            if(fecha.contains("/")) {
                String[] partes = fecha.split("/");
                if(partes.length >= 3) {
                    añoNacimiento = Integer.parseInt(partes[2]); // Último elemento es el año
                } else {
                    return false;
                }
            }
            // Si contiene "-" asumimos formato yyyy-MM-dd
            else if(fecha.contains("-")) {
                String[] partes = fecha.split("-");
                if(partes.length >= 3) {
                    añoNacimiento = Integer.parseInt(partes[0]); // Primer elemento es el año
                } else {
                    return false;
                }
            }
            // Si es solo un número, asumimos que es el año
            else if(fecha.matches("\\d{4}")) {
                añoNacimiento = Integer.parseInt(fecha);
            }
            // Otros casos
            else {
                return false; // No se puede determinar
            }

            int añoActual = java.time.LocalDate.now().getYear();
            int edad = añoActual - añoNacimiento;

            return edad >= 18;

        } catch (Exception e) {
            return false; // Si hay error en el parsing, asumimos menor de edad
        }
    }

    //Métodos propios de Object
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Paciente paciente = (Paciente) o;
        return Objects.equals(id, paciente.id);
    }

    /*@Override
    public int hashCode() {
        return Objects.hash(id);
    }*/

    @Override
    public String toString() {
        return nombre + " (" + id + ") - " + fechaNacimiento;
    }

    public boolean esValido() {
        return id != null && !id.trim().isEmpty()
                && nombre != null && !nombre.trim().isEmpty()
                && fechaNacimiento != null && !fechaNacimiento.trim().isEmpty()
                && telefono != null && !telefono.trim().isEmpty();
    }
}
