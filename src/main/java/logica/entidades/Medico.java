package logica.entidades;

public class Medico extends Usuario {
    private String especialidad;

    public Medico() {
        super();
        this.tipo = TipoUsuario.MEDICO;
    }
    public Medico(String id, String nombre, String especialidad) {
        super(id, nombre, id, TipoUsuario.MEDICO);
        this.especialidad = especialidad;
    }

    public String getEspecialidad() {
        return especialidad;
    }
    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }
    public boolean puedePrescribir(){
        return true;
    }
    @Override
    public String toString() {
        return "Dr. " + nombre + " - " + especialidad + " (" + id + ") ";
    }
}
