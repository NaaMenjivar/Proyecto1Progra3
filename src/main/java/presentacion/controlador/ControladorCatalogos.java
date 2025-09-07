package presentacion.controlador;

import logica.GestorCatalogos;
import modelo.*;
import modelo.lista.Lista;
import utilidades.ManejadorExcepciones;
import logica.excepciones.CatalogoException;

public class ControladorCatalogos {

    private GestorCatalogos gestorCatalogos;

    public ControladorCatalogos() {
        this.gestorCatalogos = new GestorCatalogos();
        ManejadorExcepciones.logInfo("ControladorCatalogos", "Controlador inicializado correctamente");
    }

    // ================================
    // MÉTODOS PARA MÉDICOS
    // ================================

    /**
     * Obtiene todos los médicos del sistema
     */
    public Lista<Usuario> buscarMedicos() {
        try {
            return gestorCatalogos.buscarMedicos();
        } catch (Exception e) {
            ManejadorExcepciones.logError("ControladorCatalogos.buscarMedicos", e);
            return new Lista<Usuario>(); // Retorna lista vacía en caso de error
        }
    }

    /**
     * Agrega un nuevo médico al sistema
     */
    public boolean agregarMedico(Medico medico) throws CatalogoException {
        try {
            boolean resultado = gestorCatalogos.agregarMedico(medico);
            if (resultado) {
                ManejadorExcepciones.logInfo("ControladorCatalogos.agregarMedico",
                        "Médico agregado exitosamente: " + medico.getId());
            }
            return resultado;
        } catch (CatalogoException e) {
            ManejadorExcepciones.logError("ControladorCatalogos.agregarMedico", e);
            throw e; // Re-lanzar para que la vista maneje el error
        } catch (Exception e) {
            ManejadorExcepciones.logError("ControladorCatalogos.agregarMedico", e);
            throw new CatalogoException("Error interno al agregar médico: " + e.getMessage());
        }
    }

    /**
     * Actualiza un médico existente
     */
    public boolean actualizarUsuario(Usuario usuario) throws CatalogoException {
        try {
            boolean resultado = gestorCatalogos.actualizarUsuario(usuario);
            if (resultado) {
                ManejadorExcepciones.logInfo("ControladorCatalogos.actualizarUsuario",
                        "Usuario actualizado exitosamente: " + usuario.getId());
            }
            return resultado;
        } catch (CatalogoException e) {
            ManejadorExcepciones.logError("ControladorCatalogos.actualizarUsuario", e);
            throw e;
        } catch (Exception e) {
            ManejadorExcepciones.logError("ControladorCatalogos.actualizarUsuario", e);
            throw new CatalogoException("Error interno al actualizar usuario: " + e.getMessage());
        }
    }

    /**
     * Elimina un usuario del sistema
     */
    public boolean eliminarUsuario(String id) throws CatalogoException {
        try {
            boolean resultado = gestorCatalogos.eliminarUsuario(id);
            if (resultado) {
                ManejadorExcepciones.logInfo("ControladorCatalogos.eliminarUsuario",
                        "Usuario eliminado exitosamente: " + id);
            }
            return resultado;
        } catch (CatalogoException e) {
            ManejadorExcepciones.logError("ControladorCatalogos.eliminarUsuario", e);
            throw e;
        } catch (Exception e) {
            ManejadorExcepciones.logError("ControladorCatalogos.eliminarUsuario", e);
            throw new CatalogoException("Error interno al eliminar usuario: " + e.getMessage());
        }
    }

    // ================================
    // MÉTODOS PARA FARMACEUTAS
    // ================================

    /**
     * Obtiene todos los farmaceutas del sistema
     */
    public Lista<Usuario> buscarFarmaceutas() {
        try {
            return gestorCatalogos.buscarFarmaceutas();
        } catch (Exception e) {
            ManejadorExcepciones.logError("ControladorCatalogos.buscarFarmaceutas", e);
            return new Lista<Usuario>();
        }
    }

    /**
     * Agrega un nuevo farmaceuta al sistema
     */
    public boolean agregarFarmaceuta(Farmaceuta farmaceuta) throws CatalogoException {
        try {
            boolean resultado = gestorCatalogos.agregarFarmaceuta(farmaceuta);
            if (resultado) {
                ManejadorExcepciones.logInfo("ControladorCatalogos.agregarFarmaceuta",
                        "Farmaceuta agregado exitosamente: " + farmaceuta.getId());
            }
            return resultado;
        } catch (CatalogoException e) {
            ManejadorExcepciones.logError("ControladorCatalogos.agregarFarmaceuta", e);
            throw e;
        } catch (Exception e) {
            ManejadorExcepciones.logError("ControladorCatalogos.agregarFarmaceuta", e);
            throw new CatalogoException("Error interno al agregar farmaceuta: " + e.getMessage());
        }
    }

    // ================================
    // MÉTODOS PARA PACIENTES
    // ================================

    /**
     * Obtiene todos los pacientes del sistema
     */
    public Lista<Paciente> obtenerTodosPacientes() {
        try {
            return gestorCatalogos.obtenerTodosPacientes();
        } catch (Exception e) {
            ManejadorExcepciones.logError("ControladorCatalogos.obtenerTodosPacientes", e);
            return new Lista<Paciente>();
        }
    }

    /**
     * Busca un paciente por ID
     */
    public Paciente buscarPaciente(String id) {
        try {
            return gestorCatalogos.buscarPaciente(id);
        } catch (Exception e) {
            ManejadorExcepciones.logError("ControladorCatalogos.buscarPaciente", e);
            return null;
        }
    }

    /**
     * Agrega un nuevo paciente al sistema
     */
    public boolean agregarPaciente(Paciente paciente) throws CatalogoException {
        try {
            boolean resultado = gestorCatalogos.agregarPaciente(paciente);
            if (resultado) {
                ManejadorExcepciones.logInfo("ControladorCatalogos.agregarPaciente",
                        "Paciente agregado exitosamente: " + paciente.getId());
            }
            return resultado;
        } catch (CatalogoException e) {
            ManejadorExcepciones.logError("ControladorCatalogos.agregarPaciente", e);
            throw e;
        } catch (Exception e) {
            ManejadorExcepciones.logError("ControladorCatalogos.agregarPaciente", e);
            throw new CatalogoException("Error interno al agregar paciente: " + e.getMessage());
        }
    }

    /**
     * Actualiza un paciente existente
     */
    public boolean actualizarPaciente(Paciente paciente) throws CatalogoException {
        try {
            boolean resultado = gestorCatalogos.actualizarPaciente(paciente);
            if (resultado) {
                ManejadorExcepciones.logInfo("ControladorCatalogos.actualizarPaciente",
                        "Paciente actualizado exitosamente: " + paciente.getId());
            }
            return resultado;
        } catch (CatalogoException e) {
            ManejadorExcepciones.logError("ControladorCatalogos.actualizarPaciente", e);
            throw e;
        } catch (Exception e) {
            ManejadorExcepciones.logError("ControladorCatalogos.actualizarPaciente", e);
            throw new CatalogoException("Error interno al actualizar paciente: " + e.getMessage());
        }
    }

    /**
     * Elimina un paciente del sistema
     */
    public boolean eliminarPaciente(String id) throws CatalogoException {
        try {
            boolean resultado = gestorCatalogos.eliminarPaciente(id);
            if (resultado) {
                ManejadorExcepciones.logInfo("ControladorCatalogos.eliminarPaciente",
                        "Paciente eliminado exitosamente: " + id);
            }
            return resultado;
        } catch (CatalogoException e) {
            ManejadorExcepciones.logError("ControladorCatalogos.eliminarPaciente", e);
            throw e;
        } catch (Exception e) {
            ManejadorExcepciones.logError("ControladorCatalogos.eliminarPaciente", e);
            throw new CatalogoException("Error interno al eliminar paciente: " + e.getMessage());
        }
    }

    /**
     * Busca pacientes por nombre aproximado
     */
    public Lista<Paciente> buscarPacientesPorNombre(String nombre) {
        try {
            return gestorCatalogos.buscarPacientesPorNombre(nombre);
        } catch (Exception e) {
            ManejadorExcepciones.logError("ControladorCatalogos.buscarPacientesPorNombre", e);
            return new Lista<Paciente>();
        }
    }

    // ================================
    // MÉTODOS PARA MEDICAMENTOS
    // ================================

    /**
     * Obtiene todos los medicamentos del sistema
     */
    public Lista<Medicamento> obtenerTodosMedicamentos() {
        try {
            return gestorCatalogos.obtenerTodosMedicamentos();
        } catch (Exception e) {
            ManejadorExcepciones.logError("ControladorCatalogos.obtenerTodosMedicamentos", e);
            return new Lista<Medicamento>();
        }
    }

    /**
     * Busca un medicamento por código
     */
    public Medicamento buscarMedicamento(String codigo) {
        try {
            return gestorCatalogos.buscarMedicamento(codigo);
        } catch (Exception e) {
            ManejadorExcepciones.logError("ControladorCatalogos.buscarMedicamento", e);
            return null;
        }
    }

    /**
     * Agrega un nuevo medicamento al sistema
     */
    public boolean agregarMedicamento(Medicamento medicamento) throws CatalogoException {
        try {
            boolean resultado = gestorCatalogos.agregarMedicamento(medicamento);
            if (resultado) {
                ManejadorExcepciones.logInfo("ControladorCatalogos.agregarMedicamento",
                        "Medicamento agregado exitosamente: " + medicamento.getCodigo());
            }
            return resultado;
        } catch (CatalogoException e) {
            ManejadorExcepciones.logError("ControladorCatalogos.agregarMedicamento", e);
            throw e;
        } catch (Exception e) {
            ManejadorExcepciones.logError("ControladorCatalogos.agregarMedicamento", e);
            throw new CatalogoException("Error interno al agregar medicamento: " + e.getMessage());
        }
    }

    /**
     * Actualiza un medicamento existente
     */
    public boolean actualizarMedicamento(Medicamento medicamento) throws CatalogoException {
        try {
            boolean resultado = gestorCatalogos.actualizarMedicamento(medicamento);
            if (resultado) {
                ManejadorExcepciones.logInfo("ControladorCatalogos.actualizarMedicamento",
                        "Medicamento actualizado exitosamente: " + medicamento.getCodigo());
            }
            return resultado;
        } catch (CatalogoException e) {
            ManejadorExcepciones.logError("ControladorCatalogos.actualizarMedicamento", e);
            throw e;
        } catch (Exception e) {
            ManejadorExcepciones.logError("ControladorCatalogos.actualizarMedicamento", e);
            throw new CatalogoException("Error interno al actualizar medicamento: " + e.getMessage());
        }
    }

    /**
     * Elimina un medicamento del sistema
     */
    public boolean eliminarMedicamento(String codigo) throws CatalogoException {
        try {
            boolean resultado = gestorCatalogos.eliminarMedicamento(codigo);
            if (resultado) {
                ManejadorExcepciones.logInfo("ControladorCatalogos.eliminarMedicamento",
                        "Medicamento eliminado exitosamente: " + codigo);
            }
            return resultado;
        } catch (CatalogoException e) {
            ManejadorExcepciones.logError("ControladorCatalogos.eliminarMedicamento", e);
            throw e;
        } catch (Exception e) {
            ManejadorExcepciones.logError("ControladorCatalogos.eliminarMedicamento", e);
            throw new CatalogoException("Error interno al eliminar medicamento: " + e.getMessage());
        }
    }

    /**
     * Busca medicamentos por descripción aproximada
     */
    public Lista<Medicamento> buscarMedicamentosPorDescripcion(String descripcion) {
        try {
            return gestorCatalogos.buscarMedicamentosPorDescripcion(descripcion);
        } catch (Exception e) {
            ManejadorExcepciones.logError("ControladorCatalogos.buscarMedicamentosPorDescripcion", e);
            return new Lista<Medicamento>();
        }
    }

    /**
     * Obtiene medicamentos con stock bajo
     */
    public Lista<Medicamento> obtenerMedicamentosBajoStock(int umbral) {
        try {
            return gestorCatalogos.obtenerMedicamentosBajoStock(umbral);
        } catch (Exception e) {
            ManejadorExcepciones.logError("ControladorCatalogos.obtenerMedicamentosBajoStock", e);
            return new Lista<Medicamento>();
        }
    }

    // ================================
    // MÉTODOS DE ESTADÍSTICAS
    // ================================

    /**
     * Obtiene el número total de médicos
     */
    public int contarMedicos() {
        try {
            return gestorCatalogos.contarMedicos();
        } catch (Exception e) {
            ManejadorExcepciones.logError("ControladorCatalogos.contarMedicos", e);
            return 0;
        }
    }

    /**
     * Obtiene el número total de farmaceutas
     */
    public int contarFarmaceutas() {
        try {
            return gestorCatalogos.contarFarmaceutas();
        } catch (Exception e) {
            ManejadorExcepciones.logError("ControladorCatalogos.contarFarmaceutas", e);
            return 0;
        }
    }

    /**
     * Obtiene el número total de pacientes
     */
    public int contarPacientes() {
        try {
            return gestorCatalogos.contarPacientes();
        } catch (Exception e) {
            ManejadorExcepciones.logError("ControladorCatalogos.contarPacientes", e);
            return 0;
        }
    }

    /**
     * Obtiene el número total de medicamentos
     */
    public int contarMedicamentos() {
        try {
            return gestorCatalogos.contarMedicamentos();
        } catch (Exception e) {
            ManejadorExcepciones.logError("ControladorCatalogos.contarMedicamentos", e);
            return 0;
        }
    }

    /**
     * Obtiene un resumen estadístico del sistema
     */
    public String obtenerResumenEstadisticas() {
        try {
            StringBuilder resumen = new StringBuilder();
            resumen.append("=== ESTADÍSTICAS DEL SISTEMA ===\n");
            resumen.append("Médicos registrados: ").append(contarMedicos()).append("\n");
            resumen.append("Farmaceutas registrados: ").append(contarFarmaceutas()).append("\n");
            resumen.append("Pacientes registrados: ").append(contarPacientes()).append("\n");
            resumen.append("Medicamentos en catálogo: ").append(contarMedicamentos()).append("\n");

            // Medicamentos con stock bajo
            Lista<Medicamento> bajoStock = obtenerMedicamentosBajoStock(10);
            resumen.append("Medicamentos con stock bajo: ").append(bajoStock.getTam()).append("\n");

            return resumen.toString();
        } catch (Exception e) {
            ManejadorExcepciones.logError("ControladorCatalogos.obtenerResumenEstadisticas", e);
            return "Error al obtener estadísticas del sistema";
        }
    }

    // ================================
    // MÉTODO PARA VALIDACIONES
    // ================================

    /**
     * Valida si un médico puede ser agregado al sistema
     */
    public boolean validarMedico(Medico medico) {
        try {
            if (medico == null) {
                return false;
            }

            // Validar campos obligatorios
            if (medico.getId() == null || medico.getId().trim().isEmpty()) {
                return false;
            }

            if (medico.getNombre() == null || medico.getNombre().trim().isEmpty()) {
                return false;
            }

            if (medico.getEspecialidad() == null || medico.getEspecialidad().trim().isEmpty()) {
                return false;
            }

            return true;
        } catch (Exception e) {
            ManejadorExcepciones.logError("ControladorCatalogos.validarMedico", e);
            return false;
        }
    }

    /**
     * Verifica si existe un usuario con el ID especificado
     */
    public boolean existeUsuario(String id) {
        try {
            // Buscar en médicos
            Lista<Usuario> medicos = buscarMedicos();
            for (int i = 0; i < medicos.getTam(); i++) {
                if (medicos.obtenerPorPos(i).getId().equals(id)) {
                    return true;
                }
            }

            // Buscar en farmaceutas
            Lista<Usuario> farmaceutas = buscarFarmaceutas();
            for (int i = 0; i < farmaceutas.getTam(); i++) {
                if (farmaceutas.obtenerPorPos(i).getId().equals(id)) {
                    return true;
                }
            }

            return false;
        } catch (Exception e) {
            ManejadorExcepciones.logError("ControladorCatalogos.existeUsuario", e);
            return false;
        }
    }

    /**
     * Obtiene el gestor de catálogos para acceso directo (si es necesario)
     */
    public GestorCatalogos getGestorCatalogos() {
        return gestorCatalogos;
    }
}
