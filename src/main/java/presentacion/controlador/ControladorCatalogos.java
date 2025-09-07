package presentacion.controlador;

import logica.GestorCatalogos;
import modelo.*;
import modelo.lista.Lista;
import utilidades.ManejadorExcepciones;
import utilidades.GeneradorIds;
import utilidades.ValidadorDatos;
import logica.excepciones.CatalogoException;
import presentacion.modelo.ModeloTablaUsuarios;

/**
 * CONTROLLER - Controlador para catálogos siguiendo patrón MVC tradicional
 * Coordina entre Vista (PanelGestionMedicos) y Modelo (ModeloTablaUsuarios + GestorCatalogos)
 */
public class ControladorCatalogos {

    // ============================================
    // ATRIBUTOS
    // ============================================
    private GestorCatalogos gestorCatalogos;        // Lógica de negocio
    private ModeloTablaUsuarios modelo;              // Modelo de la vista

    // ============================================
    // CONSTRUCTOR
    // ============================================
    public ControladorCatalogos() {
        this.gestorCatalogos = new GestorCatalogos();
        ManejadorExcepciones.logInfo("ControladorCatalogos", "Controlador inicializado correctamente");
    }

    // ============================================
    // CONFIGURACIÓN DEL MODELO
    // ============================================

    /**
     * Establece el modelo de la vista para coordinación MVC
     */
    public void setModelo(ModeloTablaUsuarios modelo) {
        this.modelo = modelo;
        // Cargar datos iniciales
        cargarTodosMedicos();
    }

    // ================================
    // MÉTODOS PRINCIPALES DE COORDINACIÓN MVC
    // ================================

    /**
     * Carga todos los médicos y actualiza el modelo
     */
    public void cargarTodosMedicos() {
        try {
            Lista<Usuario> medicos = gestorCatalogos.buscarMedicos();
            if (modelo != null) {
                modelo.setListaCompleta(medicos);
                modelo.limpiarMedicoActual();
            }
            ManejadorExcepciones.logInfo("ControladorCatalogos.cargarTodosMedicos",
                    "Cargados " + medicos.getTam() + " médicos");
        } catch (Exception e) {
            ManejadorExcepciones.logError("ControladorCatalogos.cargarTodosMedicos", e);
            if (modelo != null) {
                modelo.setListaCompleta(new Lista<Usuario>());
            }
        }
    }

    /**
     * Crea un nuevo médico y actualiza el modelo
     */
    public void create(Medico medico) throws CatalogoException {
        try {
            // Validaciones antes de crear
            if (!validarMedico(medico)) {
                throw new CatalogoException("Datos del médico son inválidos");
            }

            // Generar ID si está vacío
            if (medico.getId() == null || medico.getId().trim().isEmpty()) {
                medico.setId(GeneradorIds.generarIdMedico());
            }

            // Verificar duplicados
            if (existeUsuario(medico.getId())) {
                throw new CatalogoException("Ya existe un usuario con el ID: " + medico.getId());
            }

            // Establecer clave igual al ID por defecto
            medico.setClave(medico.getId());

            // Crear en la lógica de negocio
            boolean resultado = gestorCatalogos.agregarMedico(medico);

            if (resultado) {
                // Actualizar modelo
                if (modelo != null) {
                    modelo.agregarUsuario(medico);
                    modelo.limpiarMedicoActual();
                }
                ManejadorExcepciones.logInfo("ControladorCatalogos.create",
                        "Médico creado exitosamente: " + medico.getId());
            } else {
                throw new CatalogoException("No se pudo agregar el médico al sistema");
            }

        } catch (CatalogoException e) {
            ManejadorExcepciones.logError("ControladorCatalogos.create", e);
            throw e;
        } catch (Exception e) {
            ManejadorExcepciones.logError("ControladorCatalogos.create", e);
            throw new CatalogoException("Error interno al crear médico: " + e.getMessage());
        }
    }

    /**
     * Lee un médico por ID y lo establece como actual en el modelo
     */
    public void read(String id) throws Exception {
        try {
            if (id == null || id.trim().isEmpty()) {
                throw new Exception("ID de médico es requerido");
            }

            // Buscar en la lógica de negocio
            Lista<Usuario> medicos = gestorCatalogos.buscarMedicos();
            Medico medicoEncontrado = null;

            for (int i = 0; i < medicos.getTam(); i++) {
                Usuario usuario = medicos.obtenerPorPos(i);
                if (usuario instanceof Medico && usuario.getId().equals(id.trim())) {
                    medicoEncontrado = (Medico) usuario;
                    break;
                }
            }

            if (medicoEncontrado == null) {
                throw new Exception("Médico con ID '" + id + "' no encontrado");
            }

            // Establecer como actual en el modelo
            if (modelo != null) {
                modelo.setMedicoActual(medicoEncontrado);
                modelo.setModoEdicion(true);
            }

            ManejadorExcepciones.logInfo("ControladorCatalogos.read",
                    "Médico encontrado: " + medicoEncontrado.getId());

        } catch (Exception e) {
            ManejadorExcepciones.logError("ControladorCatalogos.read", e);
            throw e;
        }
    }

    /**
     * Actualiza el médico actual del modelo
     */
    public void update(Medico medico) throws CatalogoException {
        try {
            if (medico == null) {
                throw new CatalogoException("Médico no puede ser null");
            }

            if (!validarMedico(medico)) {
                throw new CatalogoException("Datos del médico son inválidos");
            }

            // Actualizar en la lógica de negocio
            boolean resultado = gestorCatalogos.actualizarUsuario(medico);

            if (resultado) {
                // Recargar datos en el modelo
                cargarTodosMedicos();
                ManejadorExcepciones.logInfo("ControladorCatalogos.update",
                        "Médico actualizado exitosamente: " + medico.getId());
            } else {
                throw new CatalogoException("No se pudo actualizar el médico");
            }

        } catch (CatalogoException e) {
            ManejadorExcepciones.logError("ControladorCatalogos.update", e);
            throw e;
        } catch (Exception e) {
            ManejadorExcepciones.logError("ControladorCatalogos.update", e);
            throw new CatalogoException("Error interno al actualizar médico: " + e.getMessage());
        }
    }

    /**
     * Elimina un médico por ID
     */
    public void delete(String id) throws CatalogoException {
        try {
            if (id == null || id.trim().isEmpty()) {
                throw new CatalogoException("ID de médico es requerido");
            }

            if (!existeUsuario(id)) {
                throw new CatalogoException("Médico no existe: " + id);
            }

            // Eliminar en la lógica de negocio
            boolean resultado = gestorCatalogos.eliminarUsuario(id);

            if (resultado) {
                // Recargar datos en el modelo
                cargarTodosMedicos();
                ManejadorExcepciones.logInfo("ControladorCatalogos.delete",
                        "Médico eliminado exitosamente: " + id);
            } else {
                throw new CatalogoException("No se pudo eliminar el médico");
            }

        } catch (CatalogoException e) {
            ManejadorExcepciones.logError("ControladorCatalogos.delete", e);
            throw e;
        } catch (Exception e) {
            ManejadorExcepciones.logError("ControladorCatalogos.delete", e);
            throw new CatalogoException("Error interno al eliminar médico: " + e.getMessage());
        }
    }

    /**
     * Limpia el médico actual del modelo
     */
    public void clear() {
        if (modelo != null) {
            modelo.limpiarMedicoActual();
        }
    }

    /**
     * Aplica filtro de búsqueda
     */
    public void filtrar(String filtro) {
        if (modelo != null) {
            modelo.setFiltroActual(filtro);
        }
    }

    /**
     * Selecciona un médico por posición en la tabla
     */
    public void seleccionarMedico(int fila) {
        if (modelo != null) {
            Usuario usuario = modelo.getUsuarioEnFila(fila);
            if (usuario instanceof Medico) {
                modelo.setMedicoActual((Medico) usuario);
                modelo.setModoEdicion(false); // Para selección, no edición automática
            }
        }
    }

    // ================================
    // MÉTODOS DE BÚSQUEDA AVANZADA
    // ================================

    /**
     * Busca médicos por especialidad
     */
    public void buscarPorEspecialidad(String especialidad) {
        try {
            Lista<Usuario> todosMedicos = gestorCatalogos.buscarMedicos();
            Lista<Usuario> medicosFiltrados = new Lista<>();

            if (especialidad == null || especialidad.trim().isEmpty()) {
                medicosFiltrados = todosMedicos;
            } else {
                String especialidadBusqueda = especialidad.toLowerCase().trim();

                for (int i = 0; i < todosMedicos.getTam(); i++) {
                    Usuario usuario = todosMedicos.obtenerPorPos(i);
                    if (usuario instanceof Medico) {
                        Medico medico = (Medico) usuario;
                        String especialidadMedico = medico.getEspecialidad();
                        if (especialidadMedico != null &&
                                especialidadMedico.toLowerCase().contains(especialidadBusqueda)) {
                            medicosFiltrados.agregarFinal(medico);
                        }
                    }
                }
            }

            if (modelo != null) {
                modelo.setUsuarios(medicosFiltrados);
            }

        } catch (Exception e) {
            ManejadorExcepciones.logError("ControladorCatalogos.buscarPorEspecialidad", e);
        }
    }

    // ================================
    // MÉTODOS DE REPORTES Y ESTADÍSTICAS
    // ================================

    /**
     * Obtiene un resumen estadístico completo del sistema
     */
    public String obtenerResumenEstadisticas() {
        try {
            StringBuilder resumen = new StringBuilder();
            resumen.append("=".repeat(50)).append("\n");
            resumen.append("    ESTADÍSTICAS DEL SISTEMA HOSPITAL\n");
            resumen.append("=".repeat(50)).append("\n\n");

            // Estadísticas generales
            int totalMedicos = gestorCatalogos.contarMedicos();
            int totalFarmaceutas = gestorCatalogos.contarFarmaceutas();
            int totalPacientes = gestorCatalogos.contarPacientes();
            int totalMedicamentos = gestorCatalogos.contarMedicamentos();

            resumen.append("📊 RESUMEN GENERAL:\n");
            resumen.append("   • Médicos registrados: ").append(totalMedicos).append("\n");
            resumen.append("   • Farmaceutas registrados: ").append(totalFarmaceutas).append("\n");
            resumen.append("   • Pacientes registrados: ").append(totalPacientes).append("\n");
            resumen.append("   • Medicamentos en catálogo: ").append(totalMedicamentos).append("\n\n");

            // Estadísticas específicas del modelo actual
            if (modelo != null) {
                resumen.append("👨‍⚕️ ESTADO ACTUAL DE LA VISTA:\n");
                resumen.append("   • Médicos mostrados: ").append(modelo.getRowCount()).append("\n");
                resumen.append("   • Filtro activo: ").append(
                        modelo.getFiltroActual().isEmpty() ? "Ninguno" : modelo.getFiltroActual()).append("\n");
                resumen.append("   • Modo edición: ").append(modelo.isModoEdicion() ? "Sí" : "No").append("\n");
                if (modelo.getMedicoActual() != null &&
                        modelo.getMedicoActual().getId() != null &&
                        !modelo.getMedicoActual().getId().isEmpty()) {
                    resumen.append("   • Médico seleccionado: ").append(modelo.getMedicoActual().getNombre()).append("\n");
                }
                resumen.append("\n");
            }

            // Estadísticas de médicos por especialidad
            resumen.append("👨‍⚕️ MÉDICOS POR ESPECIALIDAD:\n");
            resumen.append(obtenerEstadisticasMedicos()).append("\n");

            // Medicamentos con stock bajo
            Lista<Medicamento> bajoStock = gestorCatalogos.obtenerMedicamentosBajoStock(10);
            resumen.append("💊 MEDICAMENTOS CON STOCK BAJO (≤10 unidades):\n");
            resumen.append("   • Total con stock bajo: ").append(bajoStock.getTam()).append("\n");

            if (bajoStock.getTam() > 0) {
                resumen.append("   • Medicamentos afectados:\n");
                for (int i = 0; i < Math.min(5, bajoStock.getTam()); i++) {
                    Medicamento med = bajoStock.obtenerPorPos(i);
                    resumen.append("     - ").append(med.getNombre())
                            .append(" (").append(med.getStock()).append(" unidades)\n");
                }
                if (bajoStock.getTam() > 5) {
                    resumen.append("     ... y ").append(bajoStock.getTam() - 5).append(" más\n");
                }
            }

            resumen.append("\n").append("=".repeat(50)).append("\n");
            resumen.append("Reporte generado: ").append(java.time.LocalDateTime.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));

            return resumen.toString();
        } catch (Exception e) {
            ManejadorExcepciones.logError("ControladorCatalogos.obtenerResumenEstadisticas", e);
            return "Error al obtener estadísticas del sistema: " + e.getMessage();
        }
    }

    /**
     * Obtiene estadísticas detalladas de médicos por especialidad
     */
    private String obtenerEstadisticasMedicos() {
        try {
            Lista<Usuario> medicos = gestorCatalogos.buscarMedicos();
            if (medicos.getTam() == 0) {
                return "   • No hay médicos registrados\n";
            }

            // Contar médicos por especialidad
            java.util.Map<String, Integer> especialidades = new java.util.HashMap<>();
            int medicosActivos = 0;

            for (int i = 0; i < medicos.getTam(); i++) {
                Usuario usuario = medicos.obtenerPorPos(i);
                if (usuario instanceof Medico) {
                    Medico medico = (Medico) usuario;
                    String especialidad = medico.getEspecialidad();
                    if (especialidad == null || especialidad.trim().isEmpty()) {
                        especialidad = "Sin especialidad";
                    }
                    especialidades.put(especialidad, especialidades.getOrDefault(especialidad, 0) + 1);

                    if (medico.isSesionActiva()) {
                        medicosActivos++;
                    }
                }
            }

            StringBuilder sb = new StringBuilder();
            sb.append("   • Total de médicos: ").append(medicos.getTam()).append("\n");
            sb.append("   • Médicos activos: ").append(medicosActivos).append("\n");
            sb.append("   • Distribución por especialidad:\n");

            for (java.util.Map.Entry<String, Integer> entry : especialidades.entrySet()) {
                double porcentaje = (entry.getValue() * 100.0) / medicos.getTam();
                sb.append("     - ").append(entry.getKey()).append(": ")
                        .append(entry.getValue()).append(" (")
                        .append(String.format("%.1f%%", porcentaje)).append(")\n");
            }

            return sb.toString();
        } catch (Exception e) {
            return "   • Error al calcular estadísticas de médicos\n";
        }
    }

    // ================================
    // MÉTODOS DE VALIDACIÓN
    // ================================

    /**
     * Valida si un médico puede ser agregado al sistema
     */
    public boolean validarMedico(Medico medico) {
        try {
            if (medico == null) {
                return false;
            }

            // Validar ID
            if (!ValidadorDatos.esTextoValido(medico.getId(), 3)) {
                return false;
            }

            // Validar nombre
            if (!ValidadorDatos.esTextoValido(medico.getNombre(), 2)) {
                return false;
            }

            // Validar especialidad
            if (!ValidadorDatos.esTextoValido(medico.getEspecialidad(), 2)) {
                return false;
            }

            // Validar que la especialidad no contenga caracteres especiales
            String especialidad = medico.getEspecialidad().trim();
            if (!especialidad.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")) {
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
            if (id == null || id.trim().isEmpty()) {
                return false;
            }

            // Buscar en médicos
            Lista<Usuario> medicos = gestorCatalogos.buscarMedicos();
            for (int i = 0; i < medicos.getTam(); i++) {
                if (medicos.obtenerPorPos(i).getId().equals(id)) {
                    return true;
                }
            }

            // Buscar en farmaceutas
            Lista<Usuario> farmaceutas = gestorCatalogos.buscarFarmaceutas();
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
    // MÉTODOS AUXILIARES PARA OTROS PANELES
    // ================================

    /**
     * Obtiene todos los farmaceutas (para otros paneles)
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
     * Obtiene todos los pacientes (para otros paneles)
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
     * Obtiene todos los medicamentos (para otros paneles)
     */
    public Lista<Medicamento> obtenerTodosMedicamentos() {
        try {
            return gestorCatalogos.obtenerTodosMedicamentos();
        } catch (Exception e) {
            ManejadorExcepciones.logError("ControladorCatalogos.obtenerTodosMedicamentos", e);
            return new Lista<Medicamento>();
        }
    }

    // ================================
    // MÉTODOS DE ESTADÍSTICAS (para compatibilidad)
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

    /**
     * Obtiene el gestor de catálogos para acceso directo (si es necesario)
     */
    public GestorCatalogos getGestorCatalogos() {
        return gestorCatalogos;
    }
}
