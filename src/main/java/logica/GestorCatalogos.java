package logica;

import datos.fabricas.DAOFactory;
import datos.interfaces.*;
import modelo.*;
import logica.excepciones.CatalogoException;
import java.util.List;

public class GestorCatalogos {
    private IUsuarioDAO usuarioDAO;
    private IPacienteDAO pacienteDAO;
    private IMedicamentoDAO medicamentoDAO;

    public GestorCatalogos() {
        DAOFactory factory = DAOFactory.getInstance();
        this.usuarioDAO = factory.getUsuarioDAO();
        this.pacienteDAO = factory.getPacienteDAO();
        this.medicamentoDAO = factory.getMedicamentoDAO();
    }

    // ================================
    // GESTIÓN DE USUARIOS
    // ================================

    public boolean agregarMedico(Medico medico) throws CatalogoException {
        if (medico == null) {
            throw new CatalogoException("Médico no puede ser null");
        }

        if (!medico.validarCredenciales(medico.getId(), medico.getClave())) {
            throw new CatalogoException("Datos del médico son inválidos");
        }

        if (usuarioDAO.existeUsuario(medico.getId())) {
            throw new CatalogoException("Ya existe un usuario con el ID: " + medico.getId());
        }

        // Establecer clave igual al ID por defecto
        medico.setClave(medico.getId());

        return usuarioDAO.guardar(medico);
    }

    public boolean agregarFarmaceuta(Farmaceuta farmaceuta) throws CatalogoException {
        if (farmaceuta == null) {
            throw new CatalogoException("Farmaceuta no puede ser null");
        }

        if (!farmaceuta.validarCredenciales(farmaceuta.getId(),  farmaceuta.getClave())) {
            throw new CatalogoException("Datos del farmaceuta son inválidos");
        }

        if (usuarioDAO.existeUsuario(farmaceuta.getId())) {
            throw new CatalogoException("Ya existe un usuario con el ID: " + farmaceuta.getId());
        }

        // Establecer clave igual al ID por defecto
        farmaceuta.setClave(farmaceuta.getId());

        return usuarioDAO.guardar(farmaceuta);
    }

    public boolean actualizarUsuario(Usuario usuario) throws CatalogoException {
        if (usuario == null) {
            throw new CatalogoException("Usuario no puede ser null");
        }

        if (!usuarioDAO.existeUsuario(usuario.getId())) {
            throw new CatalogoException("Usuario no existe: " + usuario.getId());
        }

        return usuarioDAO.actualizar(usuario);
    }

    public boolean eliminarUsuario(String id) throws CatalogoException {
        if (!usuarioDAO.existeUsuario(id)) {
            throw new CatalogoException("Usuario no existe: " + id);
        }

        return usuarioDAO.eliminar(id);
    }

    public List<Usuario> buscarMedicos() {
        return usuarioDAO.buscarPorTipo(TipoUsuario.MEDICO);
    }

    public List<Usuario> buscarFarmaceutas() {
        return usuarioDAO.buscarPorTipo(TipoUsuario.FARMACEUTA);
    }

    // ================================
    // GESTIÓN DE PACIENTES
    // ================================

    public boolean agregarPaciente(Paciente paciente) throws CatalogoException {
        if (paciente == null) {
            throw new CatalogoException("Paciente no puede ser null");
        }

        if (!paciente.esValido()) {
            throw new CatalogoException("Datos del paciente son inválidos");
        }

        if (pacienteDAO.existePaciente(paciente.getId())) {
            throw new CatalogoException("Ya existe un paciente con el ID: " + paciente.getId());
        }

        return pacienteDAO.guardar(paciente);
    }

    public Paciente buscarPaciente(String id) {
        return pacienteDAO.buscarPorId(id);
    }

    public List<Paciente> buscarPacientesPorNombre(String nombre) {
        return pacienteDAO.buscarPorNombreAproximado(nombre);
    }

    public boolean actualizarPaciente(Paciente paciente) throws CatalogoException {
        if (paciente == null) {
            throw new CatalogoException("Paciente no puede ser null");
        }

        if (!pacienteDAO.existePaciente(paciente.getId())) {
            throw new CatalogoException("Paciente no existe: " + paciente.getId());
        }

        return pacienteDAO.actualizar(paciente);
    }

    public boolean eliminarPaciente(String id) throws CatalogoException {
        if (!pacienteDAO.existePaciente(id)) {
            throw new CatalogoException("Paciente no existe: " + id);
        }

        return pacienteDAO.eliminar(id);
    }

    public List<Paciente> obtenerTodosPacientes() {
        return pacienteDAO.obtenerTodos();
    }

    // ================================
    // GESTIÓN DE MEDICAMENTOS
    // ================================

    public boolean agregarMedicamento(Medicamento medicamento) throws CatalogoException {
        if (medicamento == null) {
            throw new CatalogoException("Medicamento no puede ser null");
        }

        if (!medicamento.esValido()) {
            throw new CatalogoException("Datos del medicamento son inválidos");
        }

        if (medicamentoDAO.existeMedicamento(medicamento.getCodigo())) {
            throw new CatalogoException("Ya existe un medicamento con el código: " + medicamento.getCodigo());
        }

        return medicamentoDAO.guardar(medicamento);
    }

    public Medicamento buscarMedicamento(String codigo) {
        return medicamentoDAO.buscarPorCodigo(codigo);
    }

    public List<Medicamento> buscarMedicamentosPorDescripcion(String descripcion) {
        return medicamentoDAO.buscarPorDescripcionAproximada(descripcion);
    }

    public boolean actualizarMedicamento(Medicamento medicamento) throws CatalogoException {
        if (medicamento == null) {
            throw new CatalogoException("Medicamento no puede ser null");
        }

        if (!medicamentoDAO.existeMedicamento(medicamento.getCodigo())) {
            throw new CatalogoException("Medicamento no existe: " + medicamento.getCodigo());
        }

        return medicamentoDAO.actualizar(medicamento);
    }

    public boolean eliminarMedicamento(String codigo) throws CatalogoException {
        if (!medicamentoDAO.existeMedicamento(codigo)) {
            throw new CatalogoException("Medicamento no existe: " + codigo);
        }

        return medicamentoDAO.eliminar(codigo);
    }

    public List<Medicamento> obtenerTodosMedicamentos() {
        return medicamentoDAO.obtenerTodos();
    }

    public List<Medicamento> obtenerMedicamentosBajoStock(int umbral) {
        return medicamentoDAO.obtenerMedicamentosBajoStock(umbral);
    }

    // ================================
    // MÉTODOS DE UTILIDAD
    // ================================

    public int contarPacientes() {
        return pacienteDAO.contarPacientes();
    }

    public int contarMedicamentos() {
        return medicamentoDAO.contarMedicamentos();
    }

    public int contarMedicos() {
        return buscarMedicos().size();
    }

    public int contarFarmaceutas() {
        return buscarFarmaceutas().size();
    }
}