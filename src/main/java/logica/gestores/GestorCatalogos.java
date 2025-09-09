package logica.gestores;

import logica.entidades.*;
import logica.excepciones.CatalogoException;
import logica.entidades.lista.Lista;
import java.time.LocalDate;

/**
 * Gestor único para todos los catálogos del sistema
 * Basado en tu implementación existente pero completado
 */
public class GestorCatalogos {
    // Datos en memoria - NO persistencia por ahora
    private Lista<Usuario> usuarios;
    private Lista<Paciente> pacientes;
    private Lista<Medicamento> medicamentos;
    private Lista<Receta> recetas; // Para prescripciones y despacho

    public GestorCatalogos() {
        // Inicializar listas vacías
        this.usuarios = new Lista<>();
        this.pacientes = new Lista<>();
        this.medicamentos = new Lista<>();
        this.recetas = new Lista<>();
    }

    // ================================
    // GESTIÓN DE USUARIOS (MÉDICOS)
    // ================================

    public boolean agregarMedico(Medico medico) throws CatalogoException {
        if (medico == null) {
            throw new CatalogoException("Médico no puede ser null");
        }

        if (!esValidoMedico(medico)) {
            throw new CatalogoException("Datos del médico son inválidos");
        }

        if (existeUsuario(medico.getId())) {
            throw new CatalogoException("Ya existe un usuario con el ID: " + medico.getId());
        }

        // Establecer clave igual al ID por defecto
        medico.setClave(medico.getId());
        usuarios.agregarFinal(medico);
        return true;
    }

    public boolean agregarFarmaceuta(Farmaceuta farmaceuta) throws CatalogoException {
        if (farmaceuta == null) {
            throw new CatalogoException("Farmaceuta no puede ser null");
        }

        if (!esValidoFarmaceuta(farmaceuta)) {
            throw new CatalogoException("Datos del farmaceuta son inválidos");
        }

        if (existeUsuario(farmaceuta.getId())) {
            throw new CatalogoException("Ya existe un usuario con el ID: " + farmaceuta.getId());
        }

        farmaceuta.setClave(farmaceuta.getId());
        usuarios.agregarFinal(farmaceuta);
        return true;
    }

    public boolean actualizarUsuario(Usuario usuario) throws CatalogoException {
        if (usuario == null) {
            throw new CatalogoException("Usuario no puede ser null");
        }

        if (!existeUsuario(usuario.getId())) {
            throw new CatalogoException("Usuario no existe: " + usuario.getId());
        }

        return usuarios.actualizarPorId(usuario.getId(), usuario);
    }

    public boolean eliminarUsuario(String id) throws CatalogoException {
        if (!existeUsuario(id)) {
            throw new CatalogoException("Usuario no existe: " + id);
        }

        return usuarios.eliminarPorId(id);
    }

    public Lista<Usuario> buscarMedicos() {
        Lista<Usuario> medicos = new Lista<>();
        for (int i = 0; i < usuarios.getTam(); i++) {
            Usuario usuario = usuarios.obtenerPorPos(i);
            if (usuario.getTipo() == TipoUsuario.MEDICO) {
                medicos.agregarFinal(usuario);
            }
        }
        return medicos;
    }

    public Lista<Usuario> buscarFarmaceutas() {
        Lista<Usuario> farmaceutas = new Lista<>();
        for (int i = 0; i < usuarios.getTam(); i++) {
            Usuario usuario = usuarios.obtenerPorPos(i);
            if (usuario.getTipo() == TipoUsuario.FARMACEUTA) {
                farmaceutas.agregarFinal(usuario);
            }
        }
        return farmaceutas;
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

        if (existePaciente(paciente.getId())) {
            throw new CatalogoException("Ya existe un paciente con el ID: " + paciente.getId());
        }

        pacientes.agregarFinal(paciente);
        return true;
    }

    public Paciente buscarPaciente(String id) {
        return pacientes.buscarPorId(id);
    }

    public Lista<Paciente> buscarPacientesPorNombre(String nombre) {
        Lista<Paciente> resultado = new Lista<>();
        for (int i = 0; i < pacientes.getTam(); i++) {
            Paciente p = pacientes.obtenerPorPos(i);
            if (p.getNombre().toLowerCase().contains(nombre.toLowerCase())) {
                resultado.agregarFinal(p);
            }
        }
        return resultado;
    }

    public boolean actualizarPaciente(Paciente paciente) throws CatalogoException {
        if (paciente == null) {
            throw new CatalogoException("Paciente no puede ser null");
        }

        if (!existePaciente(paciente.getId())) {
            throw new CatalogoException("Paciente no existe: " + paciente.getId());
        }

        return pacientes.actualizarPorId(paciente.getId(), paciente);
    }

    public boolean eliminarPaciente(String id) throws CatalogoException {
        if (!existePaciente(id)) {
            throw new CatalogoException("Paciente no existe: " + id);
        }

        return pacientes.eliminarPorId(id);
    }

    public Lista<Paciente> obtenerTodosPacientes() {
        return pacientes;
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

        if (existeMedicamento(medicamento.getCodigo())) {
            throw new CatalogoException("Ya existe un medicamento con el código: " + medicamento.getCodigo());
        }

        medicamentos.agregarFinal(medicamento);
        return true;
    }

    public Medicamento buscarMedicamento(String codigo) {
        return medicamentos.buscarPorId(codigo);
    }

    public Lista<Medicamento> buscarMedicamentosPorDescripcion(String descripcion) {
        Lista<Medicamento> resultado = new Lista<>();
        for (int i = 0; i < medicamentos.getTam(); i++) {
            Medicamento m = medicamentos.obtenerPorPos(i);
            if (m.getNombre().toLowerCase().contains(descripcion.toLowerCase()) ||
                    m.getPresentacion().toLowerCase().contains(descripcion.toLowerCase())) {
                resultado.agregarFinal(m);
            }
        }
        return resultado;
    }

    public boolean actualizarMedicamento(Medicamento medicamento) throws CatalogoException {
        if (medicamento == null) {
            throw new CatalogoException("Medicamento no puede ser null");
        }

        if (!existeMedicamento(medicamento.getCodigo())) {
            throw new CatalogoException("Medicamento no existe: " + medicamento.getCodigo());
        }

        return medicamentos.actualizarPorId(medicamento.getCodigo(), medicamento);
    }

    public boolean eliminarMedicamento(String codigo) throws CatalogoException {
        if (!existeMedicamento(codigo)) {
            throw new CatalogoException("Medicamento no existe: " + codigo);
        }

        return medicamentos.eliminarPorId(codigo);
    }

    public Lista<Medicamento> obtenerTodosMedicamentos() {
        return medicamentos;
    }

    public Lista<Medicamento> obtenerMedicamentosBajoStock(int umbral) {
        Lista<Medicamento> resultado = new Lista<>();
        for (int i = 0; i < medicamentos.getTam(); i++) {
            Medicamento m = medicamentos.obtenerPorPos(i);
            if (m.getStock() <= umbral) {
                resultado.agregarFinal(m);
            }
        }
        return resultado;
    }

    // ================================
    // GESTIÓN DE RECETAS (Prescripción y Despacho)
    // ================================

    public boolean agregarReceta(Receta receta) throws CatalogoException {
        if (receta == null) {
            throw new CatalogoException("Receta no puede ser null");
        }

        if (!receta.tieneDetalles()) {
            throw new CatalogoException("Receta debe tener al menos un medicamento");
        }

        recetas.agregarFinal(receta);
        return true;
    }

    public Lista<Receta> obtenerTodasRecetas() {
        return recetas;
    }

    public Receta buscarReceta(String numeroReceta) {
        return recetas.buscarPorId(numeroReceta);
    }

    public Lista<Receta> buscarRecetasPorPaciente(String idPaciente) {
        Lista<Receta> resultado = new Lista<>();
        for (int i = 0; i < recetas.getTam(); i++) {
            Receta receta = recetas.obtenerPorPos(i);
            if (receta.getIdPaciente().equals(idPaciente)) {
                resultado.agregarFinal(receta);
            }
        }
        return resultado;
    }

    public Lista<Receta> buscarRecetasPorEstado(EstadoReceta estado) {
        Lista<Receta> resultado = new Lista<>();
        for (int i = 0; i < recetas.getTam(); i++) {
            Receta receta = recetas.obtenerPorPos(i);
            if (receta.getEstado() == estado) {
                resultado.agregarFinal(receta);
            }
        }
        return resultado;
    }

    public boolean cambiarEstadoReceta(String numeroReceta, EstadoReceta nuevoEstado) throws CatalogoException {
        Receta receta = buscarReceta(numeroReceta);
        if (receta == null) {
            throw new CatalogoException("Receta no encontrada: " + numeroReceta);
        }

        if (!receta.puedeAvanzarAEstado(nuevoEstado)) {
            throw new CatalogoException("Transición de estado no válida");
        }

        receta.setEstado(nuevoEstado);
        return true;
    }

    // ================================
    // MÉTODOS DE UTILIDAD Y VALIDACIÓN
    // ================================

    private boolean esValidoMedico(Medico medico) {
        return medico.getId() != null && !medico.getId().trim().isEmpty() &&
                medico.getNombre() != null && !medico.getNombre().trim().isEmpty() &&
                medico.getEspecialidad() != null && !medico.getEspecialidad().trim().isEmpty();
    }

    private boolean esValidoFarmaceuta(Farmaceuta farmaceuta) {
        return farmaceuta.getId() != null && !farmaceuta.getId().trim().isEmpty() &&
                farmaceuta.getNombre() != null && !farmaceuta.getNombre().trim().isEmpty();
    }

    private boolean existeUsuario(String id) {
        return usuarios.buscarPorId(id) != null;
    }

    private boolean existePaciente(String id) {
        return pacientes.buscarPorId(id) != null;
    }

    private boolean existeMedicamento(String codigo) {
        return medicamentos.buscarPorId(codigo) != null;
    }

    // ================================
    // MÉTODOS DE CONTEO
    // ================================

    public int contarPacientes() {
        return pacientes.getTam();
    }

    public int contarMedicamentos() {
        return medicamentos.getTam();
    }

    public int contarMedicos() {
        return buscarMedicos().getTam();
    }

    public int contarFarmaceutas() {
        return buscarFarmaceutas().getTam();
    }

    public int contarRecetas() {
        return recetas.getTam();
    }

    // ================================
    // REPORTES Y ESTADÍSTICAS
    // ================================

    public String generarReporteGeneral() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ESTADÍSTICAS GENERALES DEL SISTEMA ===\n\n");
        sb.append("Médicos registrados: ").append(contarMedicos()).append("\n");
        sb.append("Farmaceutas registrados: ").append(contarFarmaceutas()).append("\n");
        sb.append("Pacientes registrados: ").append(contarPacientes()).append("\n");
        sb.append("Medicamentos en catálogo: ").append(contarMedicamentos()).append("\n");
        sb.append("Recetas en el sistema: ").append(contarRecetas()).append("\n\n");

        // Medicamentos con stock bajo
        Lista<Medicamento> bajoStock = obtenerMedicamentosBajoStock(10);
        sb.append("Medicamentos con stock bajo (≤10): ").append(bajoStock.getTam()).append("\n");
        if (bajoStock.getTam() > 0) {
            sb.append("\nMedicamentos con stock crítico:\n");
            for (int i = 0; i < bajoStock.getTam(); i++) {
                Medicamento med = bajoStock.obtenerPorPos(i);
                sb.append("- ").append(med.getCodigoYNombre())
                        .append(" (Stock: ").append(med.getStock()).append(")\n");
            }
        }

        return sb.toString();
    }

    public String generarReporteMedicos() {
        StringBuilder sb = new StringBuilder();
        Lista<Usuario> medicos = buscarMedicos();

        sb.append("=== REPORTE DE MÉDICOS ===\n\n");
        sb.append("Total de médicos: ").append(medicos.getTam()).append("\n\n");

        if (medicos.getTam() > 0) {
            sb.append("Lista detallada:\n");
            for (int i = 0; i < medicos.getTam(); i++) {
                if (medicos.obtenerPorPos(i) instanceof Medico) {
                    Medico medico = (Medico) medicos.obtenerPorPos(i);
                    sb.append("- ID: ").append(medico.getId())
                            .append(" | Nombre: ").append(medico.getNombre())
                            .append(" | Especialidad: ").append(medico.getEspecialidad())
                            .append("\n");
                }
            }
        } else {
            sb.append("No hay médicos registrados en el sistema.\n");
        }

        return sb.toString();
    }

    public String generarReportePacientes() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== REPORTE DE PACIENTES ===\n\n");
        sb.append("Total de pacientes: ").append(pacientes.getTam()).append("\n\n");

        if (pacientes.getTam() > 0) {
            sb.append("Lista detallada:\n");
            for (int i = 0; i < pacientes.getTam(); i++) {
                Paciente paciente = pacientes.obtenerPorPos(i);
                sb.append("- ID: ").append(paciente.getId())
                        .append(" | Nombre: ").append(paciente.getNombre())
                        .append(" | Edad: ").append(paciente.getEdadTexto())
                        .append(" | Teléfono: ").append(paciente.getTelefono())
                        .append("\n");
            }
        } else {
            sb.append("No hay pacientes registrados en el sistema.\n");
        }

        return sb.toString();
    }

    public String generarReporteMedicamentos() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== REPORTE DE MEDICAMENTOS ===\n\n");
        sb.append("Total de medicamentos: ").append(medicamentos.getTam()).append("\n\n");

        if (medicamentos.getTam() > 0) {
            sb.append("Lista detallada:\n");
            for (int i = 0; i < medicamentos.getTam(); i++) {
                Medicamento med = medicamentos.obtenerPorPos(i);
                String statusStock = med.getStock() <= 10 ? " ⚠️ STOCK BAJO" : "";
                sb.append("- Código: ").append(med.getCodigo())
                        .append(" | Nombre: ").append(med.getNombre())
                        .append(" | Presentación: ").append(med.getPresentacion())
                        .append(" | Stock: ").append(med.getStock())
                        .append(statusStock)
                        .append("\n");
            }
        } else {
            sb.append("No hay medicamentos registrados en el sistema.\n");
        }

        return sb.toString();
    }

    // ================================
    // VALIDACIONES DE NEGOCIO
    // ================================

    public boolean validarPacienteExiste(String idPaciente) {
        return buscarPaciente(idPaciente) != null;
    }

    public boolean validarMedicamentoExiste(String codigo) {
        return buscarMedicamento(codigo) != null;
    }

    public boolean validarUsuarioExiste(String idUsuario) {
        return usuarios.buscarPorId(idUsuario) != null;
    }

    public boolean validarStockDisponible(String codigoMedicamento, int cantidadRequerida) {
        Medicamento med = buscarMedicamento(codigoMedicamento);
        return med != null && med.getStock() >= cantidadRequerida;
    }
}