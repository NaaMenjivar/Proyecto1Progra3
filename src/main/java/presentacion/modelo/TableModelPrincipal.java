package presentacion.modelo;

import logica.entidades.*;
import logica.entidades.lista.Lista;

/**
 * Modelo de tabla principal que maneja diferentes tipos de datos
 * SIN usar enum para evitar problemas de inicialización
 */
public class TableModelPrincipal extends AbstractTableModelPrincipal {

    // Usar constantes en lugar de enum para evitar problemas
    public static final int TIPO_MEDICOS = 1;
    public static final int TIPO_FARMACEUTAS = 2;
    public static final int TIPO_PACIENTES = 3;
    public static final int TIPO_MEDICAMENTOS = 4;
    public static final int TIPO_RECETAS = 5;
    public static final int TIPO_DETALLES_RECETA = 6;

    private int tipoTabla;

    public TableModelPrincipal(int tipo) {
        super();
        this.tipoTabla = tipo;
        inicializarColumnas();
    }

    public TableModelPrincipal(int tipo, Lista<Object> datos) {
        super(datos);
        this.tipoTabla = tipo;
        inicializarColumnas();
    }

    private void inicializarColumnas() {
        this.nombreColumnas = definirNombresColumnas();
        this.tiposColumnas = definirTiposColumnas();
    }

    @Override
    protected String[] definirNombresColumnas() {
        switch (tipoTabla) {
            case TIPO_MEDICOS:
                return new String[]{"ID", "Nombre", "Especialidad"};
            case TIPO_FARMACEUTAS:
                return new String[]{"ID", "Nombre"};
            case TIPO_PACIENTES:
                return new String[]{"ID", "Nombre", "Fecha Nacimiento", "Teléfono"};
            case TIPO_MEDICAMENTOS:
                return new String[]{"Código", "Nombre", "Presentación", "Stock"};
            case TIPO_RECETAS:
                return new String[]{"Número", "ID Paciente", "ID Médico", "Fecha Confección", "Estado"};
            case TIPO_DETALLES_RECETA:
                return new String[]{"Código Medicamento", "Cantidad", "Indicaciones", "Duración (días)"};
            default:
                return new String[]{"Datos"};
        }
    }

    @Override
    protected Class<?>[] definirTiposColumnas() {
        switch (tipoTabla) {
            case TIPO_MEDICOS:
                return new Class<?>[]{String.class, String.class, String.class};
            case TIPO_FARMACEUTAS:
                return new Class<?>[]{String.class, String.class};
            case TIPO_PACIENTES:
                return new Class<?>[]{String.class, String.class, String.class, String.class};
            case TIPO_MEDICAMENTOS:
                return new Class<?>[]{String.class, String.class, String.class, Integer.class};
            case TIPO_RECETAS:
                return new Class<?>[]{String.class, String.class, String.class, String.class, String.class};
            case TIPO_DETALLES_RECETA:
                return new Class<?>[]{String.class, Integer.class, String.class, Integer.class};
            default:
                return new Class<?>[]{String.class};
        }
    }

    @Override
    protected Object obtenerValorEspecifico(Object objeto, int columna) {
        switch (tipoTabla) {
            case TIPO_MEDICOS:
                return obtenerValorMedico(objeto, columna);
            case TIPO_FARMACEUTAS:
                return obtenerValorFarmaceuta(objeto, columna);
            case TIPO_PACIENTES:
                return obtenerValorPaciente(objeto, columna);
            case TIPO_MEDICAMENTOS:
                return obtenerValorMedicamento(objeto, columna);
            case TIPO_RECETAS:
                return obtenerValorReceta(objeto, columna);
            case TIPO_DETALLES_RECETA:
                return obtenerValorDetalleReceta(objeto, columna);
            default:
                return objeto != null ? objeto.toString() : "";
        }
    }

    @Override
    protected boolean esObjetoValido(Object objeto) {
        if (objeto == null) return false;

        switch (tipoTabla) {
            case TIPO_MEDICOS:
                return objeto instanceof Medico;
            case TIPO_FARMACEUTAS:
                return objeto instanceof Farmaceuta;
            case TIPO_PACIENTES:
                return objeto instanceof Paciente;
            case TIPO_MEDICAMENTOS:
                return objeto instanceof Medicamento;
            case TIPO_RECETAS:
                return objeto instanceof Receta;
            case TIPO_DETALLES_RECETA:
                return objeto instanceof DetalleReceta;
            default:
                return true;
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
                    receta.getFechaConfeccion().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) :
                    "N/A";
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
    // MÉTODOS FACTORY ESTÁTICOS
    // ================================

    public static TableModelPrincipal crearModeloMedicos() {
        return new TableModelPrincipal(TIPO_MEDICOS);
    }

    public static TableModelPrincipal crearModeloMedicos(Lista<Object> datos) {
        return new TableModelPrincipal(TIPO_MEDICOS, datos);
    }

    public static TableModelPrincipal crearModeloFarmaceutas() {
        return new TableModelPrincipal(TIPO_FARMACEUTAS);
    }

    public static TableModelPrincipal crearModeloFarmaceutas(Lista<Object> datos) {
        return new TableModelPrincipal(TIPO_FARMACEUTAS, datos);
    }

    public static TableModelPrincipal crearModeloPacientes() {
        return new TableModelPrincipal(TIPO_PACIENTES);
    }

    public static TableModelPrincipal crearModeloPacientes(Lista<Object> datos) {
        return new TableModelPrincipal(TIPO_PACIENTES, datos);
    }

    public static TableModelPrincipal crearModeloMedicamentos() {
        return new TableModelPrincipal(TIPO_MEDICAMENTOS);
    }

    public static TableModelPrincipal crearModeloMedicamentos(Lista<Object> datos) {
        return new TableModelPrincipal(TIPO_MEDICAMENTOS, datos);
    }

    public static TableModelPrincipal crearModeloRecetas() {
        return new TableModelPrincipal(TIPO_RECETAS);
    }

    public static TableModelPrincipal crearModeloRecetas(Lista<Object> datos) {
        return new TableModelPrincipal(TIPO_RECETAS, datos);
    }

    public static TableModelPrincipal crearModeloDetallesReceta() {
        return new TableModelPrincipal(TIPO_DETALLES_RECETA);
    }

    public static TableModelPrincipal crearModeloDetallesReceta(Lista<Object> datos) {
        return new TableModelPrincipal(TIPO_DETALLES_RECETA, datos);
    }

    // ================================
    // GETTERS Y SETTERS
    // ================================

    public int getTipoTabla() {
        return tipoTabla;
    }

    public void setTipoTabla(int tipoTabla) {
        this.tipoTabla = tipoTabla;
        inicializarColumnas();
        fireTableStructureChanged();
    }
}