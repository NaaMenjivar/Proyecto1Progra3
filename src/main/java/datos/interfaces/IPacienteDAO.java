package datos.interfaces;

import modelo.Paciente;
import modelo.lista.Lista;

public interface IPacienteDAO {
    // CRUD básico
    boolean guardar(Paciente paciente);
    Paciente buscarPorId(String id);
    Lista<Paciente> obtenerTodos();  // ✅ Cambio aquí
    boolean actualizar(Paciente paciente);
    boolean eliminar(String id);

    // Métodos específicos del negocio
    Lista<Paciente> buscarPorNombre(String nombre);  // ✅ Cambio aquí
    Lista<Paciente> buscarPorNombreAproximado(String patron);  // ✅ Cambio aquí
    boolean existePaciente(String id);

    // Métodos de utilidad
    void cargarDatos();
    boolean guardarDatos();
    int contarPacientes();
}