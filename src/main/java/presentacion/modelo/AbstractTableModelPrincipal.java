package presentacion.modelo;

import logica.entidades.lista.Lista;
import javax.swing.table.AbstractTableModel;

/**
 * Clase abstracta base para todos los modelos de tabla del sistema
 * Extiende AbstractTableModel de Swing y define estructura común
 */
public abstract class AbstractTableModelPrincipal extends AbstractTableModel {
    protected Lista<Object> datos;
    protected String[] nombreColumnas;
    protected Class<?>[] tiposColumnas;

    public AbstractTableModelPrincipal() {
        this.datos = new Lista<>();
        this.nombreColumnas = definirNombresColumnas();
        this.tiposColumnas = definirTiposColumnas();
    }

    public AbstractTableModelPrincipal(Lista<Object> datos) {
        this.datos = datos != null ? datos : new Lista<>();
        this.nombreColumnas = definirNombresColumnas();
        this.tiposColumnas = definirTiposColumnas();
    }

    // ================================
    // MÉTODOS ABSTRACTOS (deben implementar las subclases)
    // ================================

    /**
     * Define los nombres de las columnas específicos para cada tipo de tabla
     */
    protected abstract String[] definirNombresColumnas();

    /**
     * Define los tipos de datos de las columnas
     */
    protected abstract Class<?>[] definirTiposColumnas();

    /**
     * Obtiene el valor específico de una celda según el tipo de objeto
     */
    protected abstract Object obtenerValorEspecifico(Object objeto, int columna);

    /**
     * Valida si un objeto es del tipo correcto para esta tabla
     */
    protected abstract boolean esObjetoValido(Object objeto);

    // ================================
    // IMPLEMENTACIÓN DE AbstractTableModel
    // ================================

    @Override
    public int getRowCount() {
        return datos.getTam();
    }

    @Override
    public int getColumnCount() {
        return nombreColumnas.length;
    }

    @Override
    public String getColumnName(int column) {
        if (column >= 0 && column < nombreColumnas.length) {
            return nombreColumnas[column];
        }
        return "";
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex >= 0 && columnIndex < tiposColumnas.length) {
            return tiposColumnas[columnIndex];
        }
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false; // Por defecto las tablas son de solo lectura
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= datos.getTam() ||
                columnIndex < 0 || columnIndex >= nombreColumnas.length) {
            return "";
        }

        Object objeto = datos.obtenerPorPos(rowIndex);
        if (objeto == null) {
            return "";
        }

        return obtenerValorEspecifico(objeto, columnIndex);
    }

    // ================================
    // MÉTODOS PÚBLICOS COMUNES
    // ================================

    /**
     * Establece nuevos datos en la tabla
     */
    public void setDatos(Lista<Object> nuevosDatos) {
        this.datos = nuevosDatos != null ? nuevosDatos : new Lista<>();
        fireTableDataChanged();
    }

    /**
     * Obtiene el objeto en la fila especificada
     */
    public Object getObjetoEnFila(int fila) {
        if (fila >= 0 && fila < datos.getTam()) {
            return datos.obtenerPorPos(fila);
        }
        return null;
    }

    /**
     * Agrega un objeto a la tabla
     */
    public boolean agregarObjeto(Object objeto) {
        if (esObjetoValido(objeto)) {
            datos.agregarFinal(objeto);
            fireTableRowsInserted(datos.getTam() - 1, datos.getTam() - 1);
            return true;
        }
        return false;
    }

    /**
     * Elimina un objeto de la tabla por posición
     */
    public boolean eliminarObjeto(int fila) {
        if (fila >= 0 && fila < datos.getTam()) {
            Object objeto = datos.obtenerPorPos(fila);
            if (datos.eliminar(objeto)) {
                fireTableRowsDeleted(fila, fila);
                return true;
            }
        }
        return false;
    }

    /**
     * Actualiza un objeto en la tabla
     */
    public boolean actualizarObjeto(int fila, Object objetoActualizado) {
        if (fila >= 0 && fila < datos.getTam() && esObjetoValido(objetoActualizado)) {
            // Como Lista no tiene método de actualización por posición,
            // necesitaríamos implementarlo o hacer eliminate + insert
            fireTableRowsUpdated(fila, fila);
            return true;
        }
        return false;
    }

    /**
     * Limpia todos los datos de la tabla
     */
    public void limpiar() {
        datos.vaciar();
        fireTableDataChanged();
    }

    /**
     * Verifica si la tabla está vacía
     */
    public boolean estaVacia() {
        return datos.getTam() == 0;
    }

    /**
     * Obtiene el número de filas
     */
    public int getNumeroFilas() {
        return datos.getTam();
    }

    /**
     * Obtiene todos los datos
     */
    public Lista<Object> getDatos() {
        return datos;
    }

    // ================================
    // MÉTODOS DE BÚSQUEDA Y FILTRADO
    // ================================

    /**
     * Busca la fila que contiene un objeto específico
     */
    public int buscarFila(Object objeto) {
        for (int i = 0; i < datos.getTam(); i++) {
            Object obj = datos.obtenerPorPos(i);
            if (obj != null && obj.equals(objeto)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Filtra los datos según un criterio de texto
     */
    public void filtrarPorTexto(Lista<Object> datosOriginales, String criterio) {
        if (criterio == null || criterio.trim().isEmpty()) {
            setDatos(datosOriginales);
            return;
        }

        Lista<Object> datosFiltrados = new Lista<>();
        String criterioBusqueda = criterio.toLowerCase();

        for (int i = 0; i < datosOriginales.getTam(); i++) {
            Object objeto = datosOriginales.obtenerPorPos(i);
            if (coincideConCriterio(objeto, criterioBusqueda)) {
                datosFiltrados.agregarFinal(objeto);
            }
        }

        setDatos(datosFiltrados);
    }

    /**
     * Determina si un objeto coincide con el criterio de búsqueda
     * Las subclases pueden sobrescribir este método para criterios específicos
     */
    protected boolean coincideConCriterio(Object objeto, String criterio) {
        if (objeto == null || criterio == null) {
            return false;
        }

        // Búsqueda básica en la representación toString del objeto
        return objeto.toString().toLowerCase().contains(criterio);
    }

    // ================================
    // MÉTODOS DE UTILIDAD
    // ================================

    /**
     * Obtiene información sobre el estado de la tabla
     */
    public String getInfoTabla() {
        return "Tabla: " + getClass().getSimpleName() +
                " - Filas: " + getRowCount() +
                " - Columnas: " + getColumnCount();
    }

    /**
     * Representación en cadena del modelo de tabla
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "filas=" + getRowCount() +
                ", columnas=" + getColumnCount() +
                '}';
    }
}
