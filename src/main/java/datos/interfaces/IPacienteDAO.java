package datos.interfaces;

import modelo.Paciente;
import java.util.List;

public interface IPacienteDAO {
    // CRUD básico
    boolean guardar(Paciente paciente);
    Paciente buscarPorId(String id);
    List<Paciente> obtenerTodos();
    boolean actualizar(Paciente paciente);
    boolean eliminar(String id);

    // Métodos específicos del negocio
    List<Paciente> buscarPorNombre(String nombre);
    List<Paciente> buscarPorNombreAproximado(String patron);
    boolean existePaciente(String id);

    // Métodos de utilidad
    void cargarDatos();
    boolean guardarDatos();
    int contarPacientes();
}