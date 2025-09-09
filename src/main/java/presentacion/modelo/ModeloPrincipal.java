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
            if (usuario.validarCredenciales(id, clave)) {
                usuarioActual = usuario;
                usuarioActual.setSesionActiva(true);
                marcarComoModificado();
                return true;
            }
        }

        Lista<Usuario> farmaceutas = gestorCatalogos.buscarFarmaceutas();
        for (int i = 0; i < farmaceutas.getTam(); i++) {
            Usuario usuario = farmaceutas.obtenerPorPos(i);
            if (usuario.validarCredenciales(id, clave)) {
                usuarioActual = usuario;
                usuarioActual.setSesionActiva(true);
                marcarComoModificado();
                return true;
            }
        }

        return false;
    }

    public void cerrarSesion() {
        if (usuarioActual != null) {
            usuarioActual.setSesionActiva(false);
            usuarioActual = null;
        }
        limpiar();
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public boolean cambiarClave(String claveActual, String claveNueva) {
        if (usuarioActual == null || !esCadenaValida(claveNueva)) {
            return false;
        }

        if (usuarioActual.validarCredenciales(usuarioActual.getId(), claveActual)) {
            usuarioActual.setClave(claveNueva);
            marcarComoModificado();
            return true;
        }
        return false;
    }

    // ================================
    // GESTIÓN DE MÉDICOS
    // ================================

    public boolean agregarMedico(String id, String nombre, String especialidad) {
        if (!antesDeModificar()) return false;

        try {
            Medico medico = new Medico(id, nombre, id, especialidad); // clave = id por defecto
            boolean resultado = gestorCatalogos.agregarMedico(medico);
            if (resultado) {
                despuesDeModificar();
            }
            return resultado;
        } catch (CatalogoException e) {
            return false;
        }
    }

    public Lista<Usuario> obtenerMedicos() {
        return gestorCatalogos.buscarMedicos();
    }

    public boolean actualizarMedico(String id, String nombre, String especialidad) {
        try {
            Medico medico = new Medico(id, nombre, null, especialidad);
            // Mantener la clave existente
            Usuario existente = gestorCatalogos.buscarMedicos().buscarPorId(id);
            if (existente != null) {
                medico.setClave(existente.getClave());
            }

            boolean resultado = gestorCatalogos.actualizarUsuario(medico);
            if (resultado) {
                marcarComoModificado();
            }
            return resultado;
        } catch (CatalogoException e) {
            return false;
        }
    }

    public boolean eliminarMedico(String id) {
        try {
            boolean resultado = gestorCatalogos.eliminarUsuario(id);
            if (resultado) {
                marcarComoModificado();
            }
            return resultado;
        } catch (CatalogoException e) {
            return false;
        }
    }

    // ================================
    // GESTIÓN DE FARMACEUTAS
    // ================================

    public boolean agregarFarmaceuta(String id, String nombre) {
        if (!antesDeModificar()) return false;

        try {
            Farmaceuta farmaceuta = new Farmaceuta(id, nombre, id); // clave = id por defecto
            boolean resultado = gestorCatalogos.agregarFarmaceuta(farmaceuta);
            if (resultado) {
                despuesDeModificar();
            }
            return resultado;
        } catch (CatalogoException e) {
            return false;
        }
    }

    public Lista<Usuario> obtenerFarmaceutas() {
        return gestorCatalogos.buscarFarmaceutas();
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
            if (gestorCatalogos.contarMedicos() + gestorCatalogos.contarFarmaceutas() < 3) {
                Administrador admin = new Administrador("ADMIN", "Administrador", "ADMIN");
                // Como no hay método específico para agregar admin, lo agregamos como usuario
                // Este sería un punto donde necesitarías extender el gestor
            }

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
}
