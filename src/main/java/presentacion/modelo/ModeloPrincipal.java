package presentacion.modelo;

import logica.entidades.lista.*;
import logica.gestores.GestorCatalogos;
import logica.entidades.*;
import logica.excepciones.CatalogoException;
import java.time.LocalDate;

public class ModeloPrincipal extends AbstractModel {

    private Usuario usuarioActual;
    private Paciente pacienteSeleccionado;
    private Receta recetaActual;

    public ModeloPrincipal() {
        super();
        inicializar();
        usuarioActual = null;
        pacienteSeleccionado = null;
        recetaActual = null;
    }

    public ModeloPrincipal(GestorCatalogos gestor) {
        super(gestor);
        inicializar();
        usuarioActual = null;
        pacienteSeleccionado = null;
        recetaActual = null;
    }

    @Override
    public void inicializar() {
        gestorCatalogos.cargarDatos();
        marcarComoNoModificado();
    }

    @Override
    public void limpiar() {
        usuarioActual = null;
        pacienteSeleccionado = null;
        recetaActual = null;
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

    public boolean autenticarUsuario(String id, String clave) {
        Usuario user = gestorCatalogos.autenticarUsuario(id,clave);
        if(user != null){
            usuarioActual = user;
            return true;
        }
        return false;
    }

    public boolean cambiarClave(String claveActual, String claveNueva, String id) {
        try {
            boolean resultado = gestorCatalogos.cambiarClave(claveNueva,id);
            return resultado;
        } catch (CatalogoException e) {
            return false;
        }

    }

    public void cerrarSesion() {
        usuarioActual = null;
        pacienteSeleccionado = null;
        recetaActual = null;
        gestorCatalogos.guardarDatos();
        marcarComoModificado();
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }


    public boolean agregarMedico(String id, String nombre, String especialidad) {
        if (!antesDeModificar()) return false;

        try {
            Medico medico = new Medico(id, nombre, especialidad); // clave = id inicialmente
            boolean resultado = gestorCatalogos.agregarMedico(medico);
            if (resultado) {
                despuesDeModificar();
            }
            return resultado;
        } catch (CatalogoException e) {
            return false;
        }
    }

    public boolean eliminarMedicamento(String codigo){
        try{
            boolean resultado = gestorCatalogos.eliminarMedicamento(codigo);
            if(resultado){
                despuesDeModificar();
            }
            return resultado;
        }catch (Exception e){
            return false;
        }
    }

    public boolean eliminarFarmaceuta(String id){
        try{
            boolean resultado = gestorCatalogos.eliminarFarmaceuta(id);
            if(resultado){
                despuesDeModificar();
            }
            return resultado;
        }catch (Exception e){
            return false;
        }
    }

    public boolean eliminarMedico(String id) {
        try {
            boolean resultado = gestorCatalogos.eliminarMedico(id);
            if (resultado) {
                despuesDeModificar();
            }
            return resultado;
        } catch (CatalogoException e) {
            return false;
        }
    }

    public boolean eliminarPaciente(String id) {
        try {
            boolean resultado = gestorCatalogos.eliminarPaciente(id);
            if (resultado) {
                despuesDeModificar();
            }
            return resultado;
        } catch (CatalogoException e) {
            return false;
        }
    }

    public ListaMedicos obtenerMedicos() {
        return gestorCatalogos.buscarMedicos();
    }

    public Medico buscarMedicosPorNombre(String nombre) {
        return gestorCatalogos.buscarMedicosPorNombre(nombre);
    }

    public boolean agregarFarmaceuta(String id, String nombre) {
        if (!antesDeModificar()) return false;

        try {
            Farmaceuta farmaceuta = new Farmaceuta(id, nombre);
            boolean resultado = gestorCatalogos.agregarFarmaceuta(farmaceuta);
            if (resultado) {
                despuesDeModificar();
            }
            return resultado;
        } catch (CatalogoException e) {
            return false;
        }
    }

    public Farmaceuta buscarFarmaceutaId(String id) {
        return gestorCatalogos.buscarFarmaceutaId(id);
    }

    public ListaFarmaceutas obtenerFarmaceutas() {
        return gestorCatalogos.buscarFarmaceutas();
    }

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

    public ListaPacientes obtenerPacientes() {
        return gestorCatalogos.obtenerTodosPacientes();
    }

    public Paciente buscarPacientesPorNombre(String nombre) {
        return gestorCatalogos.buscarPacientesPorNombre(nombre);
    }

    public void setPacienteSeleccionado(Paciente paciente) {
        this.pacienteSeleccionado = paciente;
        marcarComoModificado();
    }

    public Paciente getPacienteSeleccionado() {
        return pacienteSeleccionado;
    }


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

    public CatalogoMedicamentos obtenerMedicamentos() {
        return gestorCatalogos.obtenerTodosMedicamentos();
    }

    public Medicamento buscarMedicamentosPorDescripcion(String descripcion) {
        return gestorCatalogos.buscarMedicamentosPorDescripcion(descripcion);
    }


    public void agregarReceta(Receta receta) throws CatalogoException {
        gestorCatalogos.agregarReceta(receta);
    }

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
                recetaActual = null;
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

    public Lista<Receta> obtenerRecetas(){
        return gestorCatalogos.obtenerTodasRecetas();
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
            sb.append("=== INFORMACIÓN DE LA RECETA ===\n");
            sb.append("Número: ").append(receta.getNumeroReceta()).append("\n");
            sb.append("Fecha de Confección: ").append(receta.getFechaConfeccion()).append("\n");
            sb.append("Fecha de Retiro: ").append(receta.getFechaRetiro()).append("\n");
            sb.append("Estado: ").append(receta.getEstado().getDescripcion()).append("\n");

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

    private Paciente buscarPacientePorId(String id) {
        return gestorCatalogos.buscarPacientePorId(id);
    }

    private Medico buscarMedicoPorId(String id) {
        return gestorCatalogos.buscarMedicoId(id);
    }

    private Medicamento buscarMedicamentoPorCodigo(String codigo) {
        return gestorCatalogos.buscarMedicamentoPorCodigo(codigo);
    }


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


    private void crearDatosPrueba() {
        try {
            // Crear médico de prueba
            if (gestorCatalogos.contarMedicos() == 0) {
                Medico medico = new Medico("MED001", "Dr. Juan Pérez", "Medicina General");
                gestorCatalogos.agregarMedico(medico);
            }

            // Crear farmaceuta de prueba
            if (gestorCatalogos.contarFarmaceutas() == 0) {
                Farmaceuta farmaceuta = new Farmaceuta("FARM001", "María González");
                gestorCatalogos.agregarFarmaceuta(farmaceuta);
            }
            Usuario adminAux = new Administrador("ADMIN", "Administrador");

            gestorCatalogos.agregarUsuario(adminAux);

        } catch (CatalogoException e) {
            // Silenciar errores de datos de prueba
        }
    }

    private String generarNumeroReceta() {
        return "REC" + System.currentTimeMillis();
    }

    public String generarReporteCompleto() {
        return gestorCatalogos.generarReporteGeneral();
    }

    public Lista<Receta> obtenerTodasLasRecetas() {
        try {
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
    public Lista<Receta> obtenerRecetasPorMedico(String idMedico) {
        try {
            if (usuarioActual == null) {
                throw new SecurityException("Usuario no autenticado");
            }

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

    public boolean puedeAccederHistorico() {
        if (usuarioActual == null) {
            return false;
        }

        TipoUsuario tipo = usuarioActual.getTipo();
        return tipo == TipoUsuario.MEDICO ||
                tipo == TipoUsuario.FARMACEUTA ||
                tipo == TipoUsuario.ADMINISTRADOR;
    }

    public boolean existePaciente(String idPaciente) {
        return gestorCatalogos.buscarPacientePorId(idPaciente) != null;
    }
}