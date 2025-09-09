package presentacion.modelo;

import logica.entidades.*;
import logica.entidades.lista.Lista;

/**
 * Clase principal que contiene todos los modelos de tabla como clases internas
 * Soluciona el problema de múltiples clases públicas en un archivo
 */
public class TableModelPrincipal extends AbstractTableModelPrincipal {

    private TipoTabla tipoTabla;

    public enum TipoTabla {
        MEDICOS, FARMACEUTAS, PACIENTES, MEDICAMENTOS, RECETAS, DETALLES_RECETA
    }

    public TableModelPrincipal(TipoTabla tipo) {
        super();
        this.tipoTabla = tipo;
    }

    public TableModelPrincipal(TipoTabla tipo, Lista<Object> datos) {
        super(datos);
        this.tipoTabla = tipo;
    }

    @Override
    protected String[] definirNombresColumnas() {
        switch (tipoTabla) {
            case MEDICOS:
                return new String[]{"ID", "Nombre", "Especialidad"};
            case FARMACEUTAS:
                return new String[]{"ID", "Nombre"};
            case PACIENTES:
                return new String[]{"ID", "Nombre", "Fecha Nacimiento", "Teléfono"};
            case MEDICAMENTOS:
                return new String[]{"Código", "Nombre", "Presentación", "Stock"};
            case RECETAS:
                return new String[]{"Número", "ID Paciente", "ID Médico", "Fecha Confección", "Estado"};
            case DETALLES_RECETA:
                return new String[]{"Código Medicamento", "Cantidad", "Indicaciones", "Duración (días)"};
            default:
                return new String[]{};
        }
    }

    @Override
    protected Class<?>[] definirTiposColumnas() {
        switch (tipoTabla) {
            case MEDICOS:
                return new Class<?>[]{String.class, String.class, String.class};
            case FARMACEUTAS:
                return new Class<?>[]{String.class, String.class};
            case PACIENTES:
                return new Class<?>[]{String.class, String.class, String.class, String.class};
            case MEDICAMENTOS:
                return new Class<?>[]{String.class, String.class, String.class, Integer.class};
            case RECETAS:
                return new Class<?>[]{String.class, String.class, String.class, String.class, String.class};
            case DETALLES_RECETA:
                return new Class<?>[]{String.class, Integer.class, String.class, Integer.class};
            default:
                return new Class<?>[]{};
        }
    }

    @Override
    protected Object obtenerValorEspecifico(Object objeto, int columna) {
        switch (tipoTabla) {
            case MEDICOS:
                return obtenerValorMedico(objeto, columna);
            case FARMACEUTAS:
                return obtenerValorFarmaceuta(objeto, columna);
            case PACIENTES:
                return obtenerValorPaciente(objeto, columna);
            case MEDICAMENTOS:
                return obtenerValorMedicamento(objeto, columna);
            case RECETAS:
                return obtenerValorReceta(objeto, columna);
            case DETALLES_RECETA:
                return obtenerValorDetalleReceta(objeto, columna);
            default:
                return "";
        }
    }

    @Override
    protected boolean esObjetoValido(Object objeto) {
        switch (tipoTabla) {
            case MEDICOS:
                return objeto instanceof Medico;
            case FARMACEUTAS:
                return objeto instanceof Farmaceuta;
            case PACIENTES:
                return objeto instanceof Paciente;
            case MEDICAMENTOS:
                return objeto instanceof Medicamento;
            case RECETAS:
                return objeto instanceof Receta;
            case DETALLES_RECETA:
                return objeto instanceof DetalleReceta;
            default:
                return false;
        }
    }

    @Override
    protected boolean coincideConCriterio(Object objeto, String criterio) {
        switch (tipoTabla) {
            case MEDICOS:
                return coincideCriterioMedico(objeto, criterio);
            case FARMACEUTAS:
                return coincideCriterioFarmaceuta(objeto, criterio);
            case PACIENTES:
                return coincideCriterioPaciente(objeto, criterio);
            case MEDICAMENTOS:
                return coincideCriterioMedicamento(objeto, criterio);
            case RECETAS:
                return coincideCriterioReceta(objeto, criterio);
            case DETALLES_RECETA:
                return coincideCriterioDetalleReceta(objeto, criterio);
            default:
                return false;
        }
    }

    // ================================
    // MÉTODOS ESPECÍFICOS POR TIPO DE OBJETO
    // ================================

    private Object obtenerValorMedico(Object objeto, int columna) {
        if (!(objeto instanceof Medico)) return "";

        Medico medico = (Medico) objeto;
        switch (columna) {
            case 0: return medico.getId();
            case 1: return medico.getNombre();
            case 2: return medico.getEspecialidad();
            default: return "";
        }
    }

    private Object obtenerValorFarmaceuta(Object objeto, int columna) {
        if (!(objeto instanceof Farmaceuta)) return "";

        Farmaceuta farmaceuta = (Farmaceuta) objeto;
        switch (columna) {
            case 0: return farmaceuta.getId();
            case 1: return farmaceuta.getNombre();
            default: return "";
        }
    }

    private Object obtenerValorPaciente(Object objeto, int columna) {
        if (!(objeto instanceof Paciente)) return "";

        Paciente paciente = (Paciente) objeto;
        switch (columna) {
            case 0: return paciente.getId();
            case 1: return paciente.getNombre();
            case 2: return paciente.getFechaNacimientoTexto();
            case 3: return paciente.getTelefono();
            default: return "";
        }
    }

    private Object obtenerValorMedicamento(Object objeto, int columna) {
        if (!(objeto instanceof Medicamento)) return "";

        Medicamento medicamento = (Medicamento) objeto;
        switch (columna) {
            case 0: return medicamento.getCodigo();
            case 1: return medicamento.getNombre();
            case 2: return medicamento.getPresentacion();
            case 3: return medicamento.getStock();
            default: return "";
        }
    }

    private Object obtenerValorReceta(Object objeto, int columna) {
        if (!(objeto instanceof Receta)) return "";

        Receta receta = (Receta) objeto;
        switch (columna) {
            case 0: return receta.getNumeroReceta();
            case 1: return receta.getIdPaciente();
            case 2: return receta.getIdMedico();
            case 3: return receta.getFechaConfeccion() != null ?
                    receta.getFechaConfeccion().toString() : "";
            case 4: return receta.getEstado().getDescripcion();
            default: return "";
        }
    }

    private Object obtenerValorDetalleReceta(Object objeto, int columna) {
        if (!(objeto instanceof DetalleReceta)) return "";

        DetalleReceta detalle = (DetalleReceta) objeto;
        switch (columna) {
            case 0: return detalle.getCodigoMedicamento();
            case 1: return detalle.getCantidad();
            case 2: return detalle.getIndicaciones();
            case 3: return detalle.getDuracionDias();
            default: return "";
        }
    }

    // ================================
    // MÉTODOS DE CRITERIO DE BÚSQUEDA
    // ================================

    private boolean coincideCriterioMedico(Object objeto, String criterio) {
        if (!(objeto instanceof Medico)) return false;

        Medico medico = (Medico) objeto;
        return medico.getId().toLowerCase().contains(criterio) ||
                medico.getNombre().toLowerCase().contains(criterio) ||
                medico.getEspecialidad().toLowerCase().contains(criterio);
    }

    private boolean coincideCriterioFarmaceuta(Object objeto, String criterio) {
        if (!(objeto instanceof Farmaceuta)) return false;

        Farmaceuta farmaceuta = (Farmaceuta) objeto;
        return farmaceuta.getId().toLowerCase().contains(criterio) ||
                farmaceuta.getNombre().toLowerCase().contains(criterio);
    }

    private boolean coincideCriterioPaciente(Object objeto, String criterio) {
        if (!(objeto instanceof Paciente)) return false;

        Paciente paciente = (Paciente) objeto;
        return paciente.getId().toLowerCase().contains(criterio) ||
                paciente.getNombre().toLowerCase().contains(criterio) ||
                paciente.getTelefono().toLowerCase().contains(criterio);
    }

    private boolean coincideCriterioMedicamento(Object objeto, String criterio) {
        if (!(objeto instanceof Medicamento)) return false;

        Medicamento medicamento = (Medicamento) objeto;
        return medicamento.getCodigo().toLowerCase().contains(criterio) ||
                medicamento.getNombre().toLowerCase().contains(criterio) ||
                medicamento.getPresentacion().toLowerCase().contains(criterio);
    }

    private boolean coincideCriterioReceta(Object objeto, String criterio) {
        if (!(objeto instanceof Receta)) return false;

        Receta receta = (Receta) objeto;
        return receta.getNumeroReceta().toLowerCase().contains(criterio) ||
                receta.getIdPaciente().toLowerCase().contains(criterio) ||
                receta.getIdMedico().toLowerCase().contains(criterio) ||
                receta.getEstado().getDescripcion().toLowerCase().contains(criterio);
    }

    private boolean coincideCriterioDetalleReceta(Object objeto, String criterio) {
        if (!(objeto instanceof DetalleReceta)) return false;

        DetalleReceta detalle = (DetalleReceta) objeto;
        return detalle.getCodigoMedicamento().toLowerCase().contains(criterio) ||
                detalle.getIndicaciones().toLowerCase().contains(criterio);
    }

    // ================================
    // MÉTODOS FACTORY ESTÁTICOS
    // ================================

    public static TableModelPrincipal crearModeloMedicos() {
        return new TableModelPrincipal(TipoTabla.MEDICOS);
    }

    public static TableModelPrincipal crearModeloMedicos(Lista<Object> datos) {
        return new TableModelPrincipal(TipoTabla.MEDICOS, datos);
    }

    public static TableModelPrincipal crearModeloFarmaceutas() {
        return new TableModelPrincipal(TipoTabla.FARMACEUTAS);
    }

    public static TableModelPrincipal crearModeloFarmaceutas(Lista<Object> datos) {
        return new TableModelPrincipal(TipoTabla.FARMACEUTAS, datos);
    }

    public static TableModelPrincipal crearModeloPacientes() {
        return new TableModelPrincipal(TipoTabla.PACIENTES);
    }

    public static TableModelPrincipal crearModeloPacientes(Lista<Object> datos) {
        return new TableModelPrincipal(TipoTabla.PACIENTES, datos);
    }

    public static TableModelPrincipal crearModeloMedicamentos() {
        return new TableModelPrincipal(TipoTabla.MEDICAMENTOS);
    }

    public static TableModelPrincipal crearModeloMedicamentos(Lista<Object> datos) {
        return new TableModelPrincipal(TipoTabla.MEDICAMENTOS, datos);
    }

    public static TableModelPrincipal crearModeloRecetas() {
        return new TableModelPrincipal(TipoTabla.RECETAS);
    }

    public static TableModelPrincipal crearModeloRecetas(Lista<Object> datos) {
        return new TableModelPrincipal(TipoTabla.RECETAS, datos);
    }

    public static TableModelPrincipal crearModeloDetallesReceta() {
        return new TableModelPrincipal(TipoTabla.DETALLES_RECETA);
    }

    public static TableModelPrincipal crearModeloDetallesReceta(Lista<Object> datos) {
        return new TableModelPrincipal(TipoTabla.DETALLES_RECETA, datos);
    }

    // ================================
    // GETTERS Y SETTERS
    // ================================

    public TipoTabla getTipoTabla() {
        return tipoTabla;
    }

    public void setTipoTabla(TipoTabla tipoTabla) {
        this.tipoTabla = tipoTabla;
        fireTableStructureChanged();
    }
}