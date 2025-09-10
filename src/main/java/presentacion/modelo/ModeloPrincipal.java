package presentacion.modelo;

import logica.gestores.GestorCatalogos;
import logica.entidades.*;
import logica.entidades.lista.Lista;
import logica.excepciones.CatalogoException;
import java.time.LocalDate;

/**
 * Modelo principal del sistema que extiende AbstractModel
 * Maneja todos los datos y operaciones principales
 */
public class ModeloPrincipal extends AbstractModel {

    // Estado actual del sistema
    private Usuario usuarioActual;
    private Paciente pacienteSeleccionado;
    private Receta recetaActual;

    public ModeloPrincipal() {
        super();
        inicializar();
    }

    public ModeloPrincipal(GestorCatalogos gestor) {
        super(gestor);
        inicializar();
    }

    // ================================
    // IMPLEMENTACIÓN DE MÉTODOS ABSTRACTOS
    // ================================

    @Override
    public void inicializar() {
        // Crear datos de prueba si es necesario
        crearDatosPrueba();
        marcarComoNoModificado();
    }

    @Override
    public void limpiar() {
        usuarioActual = null;
        pacienteSeleccionado = null;
        recetaActual = null;
        // No limpiamos gestorCatalogos porque perdería todos los datos
        marcarComoModificado();
    }

    @Override
    public boolean validar() {
        return gestorCatalogos != null;
    }

    @Override
    public int getTotalElementos() {
        if (gestorCatalogos == null) return 0;

        return gestorCatalogos.contarMedicos() +
                gestorCatalogos.contarFarmaceutas() +
                gestorCatalogos.contarPacientes() +
                gestorCatalogos.contarMedicamentos() +
                gestorCatalogos.contarRecetas();
    }

    // ================================
    // GESTIÓN DE USUARIOS Y AUTENTICACIÓN
    // ================================

    public boolean autenticarUsuario(String id, String clave) {
        if (!esCadenaValida(id) || !esCadenaValida(clave)) {
            return false;
        }

        Lista<Usuario> medicos = gestorCatalogos.buscarMedicos();
        for (int i = 0; i < medicos.getTam(); i++) {
            Usuario usuario = medicos.obtenerPorPos(i);
            if (usuario.getId().equals(id) && usuario.getClave().equals(clave)) {
                usuarioActual = usuario;
                marcarComoModificado();
                return true;
            }
        }

        Lista<Usuario> farmaceutas = gestorCatalogos.buscarFarmaceutas();
        for (int i = 0; i < farmaceutas.getTam(); i++) {
            Usuario usuario = farmaceutas.obtenerPorPos(i);
            if (usuario.getId().equals(id) && usuario.getClave().equals(clave)) {
                usuarioActual = usuario;
                marcarComoModificado();
                return true;
            }
        }

        // TODO: Agregar autenticación para administradores cuando se implemente
        return false;
    }

    public boolean cambiarClave(String claveActual, String claveNueva) {
        if (usuarioActual == null) return false;

        if (!usuarioActual.getClave().equals(claveActual)) {
            return false;
        }

        try {
            usuarioActual.setClave(claveNueva);
            boolean resultado = gestorCatalogos.actualizarUsuario(usuarioActual);
            if (resultado) {
                marcarComoModificado();
            }
            return resultado;
        } catch (CatalogoException e) {
            return false;
        }
    }

    public void cerrarSesion() {
        usuarioActual = null;
        pacienteSeleccionado = null;
        recetaActual = null;
        marcarComoModificado();
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    // ================================
    // GESTIÓN DE MÉDICOS
    // ================================

    public boolean agregarMedico(String id, String nombre, String especialidad) {
        if (!antesDeModificar()) return false;

        try {
            Medico medico = new Medico(id, nombre, id, especialidad); // clave = id inicialmente
            boolean resultado = gestorCatalogos.agregarMedico(medico);
            if (resultado) {
                despuesDeModificar();
            }
            return resultado;
        } catch (CatalogoException e) {
            return false;
        }
    }

    public boolean eliminarMedico(String id) {
        if (!antesDeModificar()) return false;

        try {
            boolean resultado = gestorCatalogos.eliminarUsuario(id);
            if (resultado) {
                despuesDeModificar();
            }
            return resultado;
        } catch (CatalogoException e) {
            return false;
        }
    }

    public Lista<Medico> obtenerMedicos() {
        Lista<Usuario> usuarios = gestorCatalogos.buscarMedicos();
        Lista<Medico> medicos = new Lista<>();

        for (int i = 0; i < usuarios.getTam(); i++) {
            Usuario usuario = usuarios.obtenerPorPos(i);
            if (usuario instanceof Medico) {
                medicos.agregarFinal((Medico) usuario);
            }
        }
        return medicos;
    }

    public Lista<Medico> buscarMedicosPorNombre(String nombre) {
        Lista<Usuario> usuarios = gestorCatalogos.buscarMedicosPorNombre(nombre);
        Lista<Medico> medicos = new Lista<>();

        for (int i = 0; i < usuarios.getTam(); i++) {
            Usuario usuario = usuarios.obtenerPorPos(i);
            if (usuario instanceof Medico) {
                medicos.agregarFinal((Medico) usuario);
            }
        }
        return medicos;
    }

    // ================================
    // GESTIÓN DE FARMACEUTAS
    // ================================

    public boolean agregarFarmaceuta(String id, String nombre) {
        if (!antesDeModificar()) return false;

        try {
            Farmaceuta farmaceuta = new Farmaceuta(id, nombre, id); // clave = id inicialmente
            boolean resultado = gestorCatalogos.agregarFarmaceuta(farmaceuta);
            if (resultado) {
                despuesDeModificar();
            }
            return resultado;
        } catch (CatalogoException e) {
            return false;
        }
    }

    public Lista<Farmaceuta> obtenerFarmaceutas() {
        Lista<Usuario> usuarios = gestorCatalogos.buscarFarmaceutas();
        Lista<Farmaceuta> farmaceutas = new Lista<>();

        for (int i = 0; i < usuarios.getTam(); i++) {
            Usuario usuario = usuarios.obtenerPorPos(i);
            if (usuario instanceof Farmaceuta) {
                farmaceutas.agregarFinal((Farmaceuta) usuario);
            }
        }
        return farmaceutas;
    }

    // ================================
    // GESTIÓN DE PACIENTES
    // ================================

    public boolean agregarPaciente(String id, String nombre, LocalDate fechaNacimiento, String telefono) {
        if (!antesDeModificar()) return false;

        try {
            Paciente paciente = new Paciente(id, nombre, fechaNacimiento, telefono);
            boolean resultado = gestorCatalogos.agregarPaciente(paciente);
            if (resultado) {
                despuesDeModificar();
            }
            return resultado;
        } catch (CatalogoException e) {
            return false;
        }
    }

    public Lista<Paciente> obtenerPacientes() {
        return gestorCatalogos.obtenerTodosPacientes();
    }

    public Lista<Paciente> buscarPacientesPorNombre(String nombre) {
        return gestorCatalogos.buscarPacientesPorNombre(nombre);
    }

    public void setPacienteSeleccionado(Paciente paciente) {
        this.pacienteSeleccionado = paciente;
        marcarComoModificado();
    }

    public Paciente getPacienteSeleccionado() {
        return pacienteSeleccionado;
    }

    // ================================
    // GESTIÓN DE MEDICAMENTOS
    // ================================

    public boolean agregarMedicamento(String codigo, String nombre, String presentacion, int stock) {
        if (!antesDeModificar()) return false;

        try {
            Medicamento medicamento = new Medicamento(codigo, nombre, presentacion);
            medicamento.setStock(stock);
            boolean resultado = gestorCatalogos.agregarMedicamento(medicamento);
            if (resultado) {
                despuesDeModificar();
            }
            return resultado;
        } catch (CatalogoException e) {
            return false;
        }
    }

    public Lista<Medicamento> obtenerMedicamentos() {
        return gestorCatalogos.obtenerTodosMedicamentos();
    }

    public Lista<Medicamento> buscarMedicamentosPorDescripcion(String descripcion) {
        return gestorCatalogos.buscarMedicamentosPorDescripcion(descripcion);
    }

    // ================================
    // GESTIÓN DE RECETAS
    // ================================

    public void iniciarNuevaReceta(LocalDate fechaRetiro) {
        if (pacienteSeleccionado != null && usuarioActual != null) {
            String numeroReceta = generarNumeroReceta();
            recetaActual = new Receta(numeroReceta, pacienteSeleccionado.getId(),
                    usuarioActual.getId(), fechaRetiro);
            marcarComoModificado();
        }
    }

    public boolean agregarMedicamentoAReceta(String codigoMedicamento, int cantidad,
                                             String indicaciones, int duracionDias) {
        if (recetaActual == null) return false;

        DetalleReceta detalle = new DetalleReceta(codigoMedicamento, cantidad, indicaciones, duracionDias);
        if (detalle.esValidoPrescripcion()) {
            recetaActual.agregarDetalle(detalle);
            marcarComoModificado();
            return true;
        }
        return false;
    }

    public boolean guardarReceta() {
        if (recetaActual == null || !recetaActual.tieneDetalles()) {
            return false;
        }

        try {
            boolean resultado = gestorCatalogos.agregarReceta(recetaActual);
            if (resultado) {
                recetaActual = null; // Limpiar receta actual
                marcarComoModificado();
            }
            return resultado;
        } catch (CatalogoException e) {
            return false;
        }
    }

    public Receta getRecetaActual() {
        return recetaActual;
    }

    public Lista<Receta> obtenerRecetasPorPaciente(String idPaciente) {
        return gestorCatalogos.buscarRecetasPorPaciente(idPaciente);
    }

    public Lista<Receta> obtenerRecetasPorEstado(EstadoReceta estado) {
        return gestorCatalogos.buscarRecetasPorEstado(estado);
    }

    public boolean cambiarEstadoReceta(String numeroReceta, EstadoReceta nuevoEstado) {
        try {
            boolean resultado = gestorCatalogos.cambiarEstadoReceta(numeroReceta, nuevoEstado);
            if (resultado) {
                marcarComoModificado();
            }
            return resultado;
        } catch (CatalogoException e) {
            return false;
        }
    }

    // ================================
    // NUEVOS MÉTODOS PARA HISTÓRICO MVC
    // ================================


    public Lista<Receta> buscarRecetasPorMedicoYNumero(String idMedico, String numeroReceta) {
        if (!esCadenaValida(idMedico) || !esCadenaValida(numeroReceta)) {
            return new Lista<Receta>();
        }

        Lista<Receta> recetasDelMedico = obtenerRecetasPorMedico(idMedico);
        Lista<Receta> recetasEncontradas = new Lista<Receta>();

        String criterioBusqueda = numeroReceta.toLowerCase();

        for (int i = 0; i < recetasDelMedico.getTam(); i++) {
            Receta receta = recetasDelMedico.obtenerPorPos(i);
            if (receta.getNumeroReceta().toLowerCase().contains(criterioBusqueda)) {
                recetasEncontradas.agregarFinal(receta);
            }
        }

        return recetasEncontradas;
    }


    public Lista<Receta> buscarRecetasPorCriterio(String criterio) {
        if (!esCadenaValida(criterio)) {
            return obtenerTodasLasRecetas();
        }

        Lista<Receta> todasLasRecetas = gestorCatalogos.obtenerTodasRecetas();
        Lista<Receta> recetasEncontradas = new Lista<Receta>();

        String criterioBusqueda = criterio.toLowerCase();

        for (int i = 0; i < todasLasRecetas.getTam(); i++) {
            Receta receta = todasLasRecetas.obtenerPorPos(i);
            if (receta.getNumeroReceta().toLowerCase().contains(criterioBusqueda) ||
                    receta.getIdPaciente().toLowerCase().contains(criterioBusqueda) ||
                    receta.getIdMedico().toLowerCase().contains(criterioBusqueda)) {
                recetasEncontradas.agregarFinal(receta);
            }
        }

        return recetasEncontradas;
    }

    public String generarDetallesReceta(Receta receta) {
        if (receta == null) {
            return "No hay receta seleccionada";
        }

        StringBuilder sb = new StringBuilder();

        try {
            // Información de la receta
            sb.append("=== INFORMACIÓN DE LA RECETA ===\n");
            sb.append("Número: ").append(receta.getNumeroReceta()).append("\n");
            sb.append("Fecha de Confección: ").append(receta.getFechaConfeccion()).append("\n");
            sb.append("Fecha de Retiro: ").append(receta.getFechaRetiro()).append("\n");
            sb.append("Estado: ").append(receta.getEstado().getDescripcion()).append("\n");

            // Información del paciente
            sb.append("\n=== INFORMACIÓN DEL PACIENTE ===\n");
            Paciente paciente = buscarPacientePorId(receta.getIdPaciente());
            if (paciente != null) {
                sb.append("ID: ").append(paciente.getId()).append("\n");
                sb.append("Nombre: ").append(paciente.getNombre()).append("\n");
                sb.append("Teléfono: ").append(paciente.getTelefono()).append("\n");
            } else {
                sb.append("ID: ").append(receta.getIdPaciente()).append("\n");
                sb.append("(Información del paciente no encontrada)\n");
            }

            // Información del médico
            sb.append("\n=== INFORMACIÓN DEL MÉDICO ===\n");
            Medico medico = buscarMedicoPorId(receta.getIdMedico());
            if (medico != null) {
                sb.append("ID: ").append(medico.getId()).append("\n");
                sb.append("Nombre: ").append(medico.getNombre()).append("\n");
                sb.append("Especialidad: ").append(medico.getEspecialidad()).append("\n");
            } else {
                sb.append("ID: ").append(receta.getIdMedico()).append("\n");
                sb.append("(Información del médico no encontrada)\n");
            }

            // Detalles de medicamentos
            sb.append("\n=== MEDICAMENTOS PRESCRITOS ===\n");
            Lista<DetalleReceta> detalles = receta.getDetalles();
            if (detalles != null && detalles.getTam() > 0) {
                for (int i = 0; i < detalles.getTam(); i++) {
                    DetalleReceta detalle = detalles.obtenerPorPos(i);
                    sb.append("\n").append(i + 1).append(". ");

                    Medicamento med = buscarMedicamentoPorCodigo(detalle.getCodigoMedicamento());
                    if (med != null) {
                        sb.append(med.getNombre()).append(" (").append(med.getCodigo()).append(")\n");
                        sb.append("   Presentación: ").append(med.getPresentacion()).append("\n");
                    } else {
                        sb.append("Código: ").append(detalle.getCodigoMedicamento()).append("\n");
                    }

                    sb.append("   Cantidad: ").append(detalle.getCantidad()).append("\n");
                    sb.append("   Duración: ").append(detalle.getDuracionDias()).append(" días\n");
                    sb.append("   Indicaciones: ").append(detalle.getIndicaciones()).append("\n");
                }
            } else {
                sb.append("(Sin medicamentos registrados)");
            }

        } catch (Exception e) {
            sb.append("\nError al generar detalles: ").append(e.getMessage());
        }

        return sb.toString();
    }

    // ================================
    // MÉTODOS DE BÚSQUEDA AUXILIARES
    // ================================

    private Paciente buscarPacientePorId(String id) {
        Lista<Paciente> pacientes = gestorCatalogos.obtenerTodosPacientes();
        for (int i = 0; i < pacientes.getTam(); i++) {
            Paciente p = pacientes.obtenerPorPos(i);
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }

    private Medico buscarMedicoPorId(String id) {
        Lista<Usuario> medicos = gestorCatalogos.buscarMedicos();
        for (int i = 0; i < medicos.getTam(); i++) {
            Usuario u = medicos.obtenerPorPos(i);
            if (u instanceof Medico && u.getId().equals(id)) {
                return (Medico) u;
            }
        }
        return null;
    }

    private Medicamento buscarMedicamentoPorCodigo(String codigo) {
        Lista<Medicamento> medicamentos = gestorCatalogos.obtenerTodosMedicamentos();
        for (int i = 0; i < medicamentos.getTam(); i++) {
            Medicamento m = medicamentos.obtenerPorPos(i);
            if (m.getCodigo().equals(codigo)) {
                return m;
            }
        }
        return null;
    }

    // ================================
    // VALIDACIONES DE PERMISOS
    // ================================

    public boolean puedeGestionarCatalogos() {
        return usuarioActual != null &&
                usuarioActual.getTipo() == TipoUsuario.ADMINISTRADOR;
    }

    public boolean puedePrescribir() {
        return usuarioActual != null &&
                usuarioActual.getTipo() == TipoUsuario.MEDICO;
    }

    public boolean puedeDespachar() {
        return usuarioActual != null &&
                usuarioActual.getTipo() == TipoUsuario.FARMACEUTA;
    }

    // ================================
    // MÉTODOS DE UTILIDAD
    // ================================

    private void crearDatosPrueba() {
        try {
            // Crear médico de prueba
            if (gestorCatalogos.contarMedicos() == 0) {
                Medico medico = new Medico("MED001", "Dr. Juan Pérez", "MED001", "Medicina General");
                gestorCatalogos.agregarMedico(medico);
            }

            // Crear farmaceuta de prueba
            if (gestorCatalogos.contarFarmaceutas() == 0) {
                Farmaceuta farmaceuta = new Farmaceuta("FARM001", "María González", "FARM001");
                gestorCatalogos.agregarFarmaceuta(farmaceuta);
            }

            // Crear administrador de prueba
            // TODO: Implementar cuando se tenga la clase Administrador

        } catch (CatalogoException e) {
            // Silenciar errores de datos de prueba
        }
    }

    private String generarNumeroReceta() {
        // Generar número único basado en timestamp
        return "REC" + System.currentTimeMillis();
    }

    public String generarReporteCompleto() {
        return gestorCatalogos.generarReporteGeneral();
    }

    /**
     * Obtiene todas las recetas del sistema (solo para ADMINISTRADORES y FARMACEUTAS)
     */
    public Lista<Receta> obtenerTodasLasRecetas() {
        try {
            // Verificar permisos
            if (usuarioActual == null) {
                throw new SecurityException("Usuario no autenticado");
            }

            TipoUsuario tipo = usuarioActual.getTipo();
            if (tipo != TipoUsuario.ADMINISTRADOR && tipo != TipoUsuario.FARMACEUTA) {
                throw new SecurityException("No tiene permisos para ver todas las recetas");
            }

            return gestorCatalogos.obtenerTodasLasRecetas();

        } catch (Exception e) {
            System.err.println("Error al obtener todas las recetas: " + e.getMessage());
            return new Lista<>();
        }
    }

    /**
     * Obtiene las recetas de un médico específico
     */
    public Lista<Receta> obtenerRecetasPorMedico(String idMedico) {
        try {
            // Verificar permisos
            if (usuarioActual == null) {
                throw new SecurityException("Usuario no autenticado");
            }

            // Los médicos solo pueden ver sus propias recetas
            if (usuarioActual.getTipo() == TipoUsuario.MEDICO &&
                    !usuarioActual.getId().equals(idMedico)) {
                throw new SecurityException("Los médicos solo pueden ver sus propias recetas");
            }

            return gestorCatalogos.obtenerRecetasPorMedico(idMedico);

        } catch (Exception e) {
            System.err.println("Error al obtener recetas del médico: " + e.getMessage());
            return new Lista<>();
        }
    }

    /**
     * Busca una receta por su número
     */
    public Receta buscarRecetaPorNumero(String numeroReceta) {
        try {
            if (usuarioActual == null) {
                throw new SecurityException("Usuario no autenticado");
            }

            return gestorCatalogos.buscarRecetaPorNumero(numeroReceta);

        } catch (Exception e) {
            System.err.println("Error al buscar receta: " + e.getMessage());
            return null;
        }
    }

    /**
     * Verifica si el usuario actual puede acceder al histórico de recetas
     */
    public boolean puedeAccederHistorico() {
        if (usuarioActual == null) {
            return false;
        }

        TipoUsuario tipo = usuarioActual.getTipo();
        return tipo == TipoUsuario.MEDICO ||
                tipo == TipoUsuario.FARMACEUTA ||
                tipo == TipoUsuario.ADMINISTRADOR;
    }
}