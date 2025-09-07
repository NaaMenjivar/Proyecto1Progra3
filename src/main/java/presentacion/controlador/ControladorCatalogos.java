package presentacion.controlador;

import logica.GestorCatalogos;
import modelo.*;
import modelo.lista.Lista;
import utilidades.ManejadorExcepciones;
import logica.excepciones.CatalogoException;

/**
 * CONTROLADOR - MVC Simple
 * Responsabilidad: Lógica de negocio y comunicación entre Vista y Modelo
 */
public class ControladorCatalogos {

    private GestorCatalogos gestorCatalogos;

    public ControladorCatalogos() {
        this.gestorCatalogos = new GestorCatalogos();
        ManejadorExcepciones.logInfo("ControladorCatalogos", "Controlador inicializado correctamente");
    }

    // ================================
    // MÉTODOS PARA MÉDICOS
    // ================================

    public Lista<Usuario> buscarMedicos() {
        try {
            return gestorCatalogos.buscarMedicos();
        } catch (Exception e) {
            ManejadorExcepciones.logError("ControladorCatalogos.buscarMedicos", e);
            return new Lista<Usuario>();
        }
    }

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
            throw e;
        } catch (Exception e) {
            ManejadorExcepciones.logError("ControladorCatalogos.agregarMedico", e);
            throw new CatalogoException("Error interno al agregar médico: " + e.getMessage());
        }
    }

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

    public Lista<Usuario> filtrarMedicos(String filtro) {
        try {
            Lista<Usuario> todosMedicos = buscarMedicos();

            if (filtro == null || filtro.trim().isEmpty()) {
                return todosMedicos;
            }

            Lista<Usuario> medicosFiltrados = new Lista<>();
            String filtroLower = filtro.toLowerCase();

            for (int i = 0; i < todosMedicos.getTam(); i++) {
                Usuario medico = todosMedicos.obtenerPorPos(i);
                if (medico.getNombre().toLowerCase().contains(filtroLower) ||
                        medico.getId().toLowerCase().contains(filtroLower)) {
                    medicosFiltrados.agregarFinal(medico);
                }
            }

            return medicosFiltrados;

        } catch (Exception e) {
            ManejadorExcepciones.logError("ControladorCatalogos.filtrarMedicos", e);
            return new Lista<Usuario>();
        }
    }

    public boolean existeUsuario(String id) {
        try {
            Lista<Usuario> medicos = buscarMedicos();
            for (int i = 0; i < medicos.getTam(); i++) {
                if (medicos.obtenerPorPos(i).getId().equals(id)) {
                    return true;
                }
            }

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

    // ================================
    // MÉTODOS PARA FARMACEUTAS
    // ================================

    public Lista<Usuario> buscarFarmaceutas() {
        try {
            return gestorCatalogos.buscarFarmaceutas();
        } catch (Exception e) {
            ManejadorExcepciones.logError("ControladorCatalogos.buscarFarmaceutas", e);
            return new Lista<Usuario>();
        }
    }

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

    public Lista<Paciente> obtenerTodosPacientes() {
        try {
            return gestorCatalogos.obtenerTodosPacientes();
        } catch (Exception e) {
            ManejadorExcepciones.logError("ControladorCatalogos.obtenerTodosPacientes", e);
            return new Lista<Paciente>();
        }
    }

    public Paciente buscarPaciente(String id) {
        try {
            return gestorCatalogos.buscarPaciente(id);
        } catch (Exception e) {
            ManejadorExcepciones.logError("ControladorCatalogos.buscarPaciente", e);
            return null;
        }
    }

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

    public Lista<Medicamento> obtenerTodosMedicamentos() {
        try {
            return gestorCatalogos.obtenerTodosMedicamentos();
        } catch (Exception e) {
            ManejadorExcepciones.logError("ControladorCatalogos.obtenerTodosMedicamentos", e);
            return new Lista<Medicamento>();
        }
    }

    public Medicamento buscarMedicamento(String codigo) {
        try {
            return gestorCatalogos.buscarMedicamento(codigo);
        } catch (Exception e) {
            ManejadorExcepciones.logError("ControladorCatalogos.buscarMedicamento", e);
            return null;
        }
    }

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

    public Lista<Medicamento> buscarMedicamentosPorDescripcion(String descripcion) {
        try {
            return gestorCatalogos.buscarMedicamentosPorDescripcion(descripcion);
        } catch (Exception e) {
            ManejadorExcepciones.logError("ControladorCatalogos.buscarMedicamentosPorDescripcion", e);
            return new Lista<Medicamento>();
        }
    }

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

    public int contarMedicos() {
        try {
            return gestorCatalogos.contarMedicos();
        } catch (Exception e) {
            ManejadorExcepciones.logError("ControladorCatalogos.contarMedicos", e);
            return 0;
        }
    }

    public int contarFarmaceutas() {
        try {
            return gestorCatalogos.contarFarmaceutas();
        } catch (Exception e) {
            ManejadorExcepciones.logError("ControladorCatalogos.contarFarmaceutas", e);
            return 0;
        }
    }

    public int contarPacientes() {
        try {
            return gestorCatalogos.contarPacientes();
        } catch (Exception e) {
            ManejadorExcepciones.logError("ControladorCatalogos.contarPacientes", e);
            return 0;
        }
    }

    public int contarMedicamentos() {
        try {
            return gestorCatalogos.contarMedicamentos();
        } catch (Exception e) {
            ManejadorExcepciones.logError("ControladorCatalogos.contarMedicamentos", e);
            return 0;
        }
    }

    public String obtenerResumenEstadisticas() {
        try {
            StringBuilder resumen = new StringBuilder();
            resumen.append("=== ESTADÍSTICAS DEL SISTEMA ===\n");
            resumen.append("Médicos registrados: ").append(contarMedicos()).append("\n");
            resumen.append("Farmaceutas registrados: ").append(contarFarmaceutas()).append("\n");
            resumen.append("Pacientes registrados: ").append(contarPacientes()).append("\n");
            resumen.append("Medicamentos en catálogo: ").append(contarMedicamentos()).append("\n");

            Lista<Medicamento> bajoStock = obtenerMedicamentosBajoStock(10);
            resumen.append("Medicamentos con stock bajo: ").append(bajoStock.getTam()).append("\n");

            return resumen.toString();
        } catch (Exception e) {
            ManejadorExcepciones.logError("ControladorCatalogos.obtenerResumenEstadisticas", e);
            return "Error al obtener estadísticas del sistema";
        }
    }
}
