package modelo;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

public class Paciente {
    private String id;
    private String nombre;
    private LocalDate fechaNacimiento;
    private String telefono;

    public Paciente(){
    }

    public Paciente(String id, String nombre, LocalDate fechaNacimiento, String telefono){
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
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    // Métodos específicos
    public int getEdad(){
        if(fechaNacimiento == null){
            return 0;
        }
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }

    public boolean esMayorDeEdad(){
        return getEdad() >= 18;
    }

    public String getEdadTexto(){
        int edad = getEdad();
        return edad + (edad == 1 ? " año" : " años");
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
        return nombre + " (" + id + ") " + getEdadTexto();
    }
}
