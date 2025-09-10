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
        // Inicialización básica segura - sin llamadas a métodos abstractos
        this.nombreColumnas = new String[]{};
        this.tiposColumnas = new Class<?>[]{};
    }

    public AbstractTableModelPrincipal(Lista<Object> datos) {
        this.datos = datos != null ? datos : new Lista<>();
        // Inicialización básica segura - sin llamadas a métodos abstractos
        this.nombreColumnas = new String[]{};
        this.tiposColumnas = new Class<?>[]{};
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
        return nombreColumnas != null ? nombreColumnas.length : 0;
    }

    @Override
    public String getColumnName(int column) {
        if (nombreColumnas != null && column >= 0 && column < nombreColumnas.length) {
            return nombreColumnas[column];
        }
        return "";
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (tiposColumnas != null && columnIndex >= 0 && columnIndex < tiposColumnas.length) {
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
                columnIndex < 0 || (nombreColumnas != null && columnIndex >= nombreColumnas.length)) {
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
            // creamos una nueva lista con el objeto actualizado
            Lista<Object> nuevaLista = new Lista<>();
            for (int i = 0; i < datos.getTam(); i++) {
                if (i == fila) {
                    nuevaLista.agregarFinal(objetoActualizado);
                } else {
                    nuevaLista.agregarFinal(datos.obtenerPorPos(i));
                }
            }
            datos = nuevaLista;
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

    // Métodos alias para compatibilidad
    public void agregarFila(Object objeto) { agregarObjeto(objeto); }
    public void eliminarFila(int indice) { eliminarObjeto(indice); }
    public void actualizarFila(int indice, Object objeto) { actualizarObjeto(indice, objeto); }

    /**
     * Obtiene todos los datos
     */
    public Lista<Object> getDatos() {
        return datos;
    }

    /**
     * Determina si un objeto coincide con el criterio de búsqueda
     */
    protected boolean coincideConCriterio(Object objeto, String criterio) {
        if (objeto == null || criterio == null) {
            return false;
        }
        return objeto.toString().toLowerCase().contains(criterio);
    }
}