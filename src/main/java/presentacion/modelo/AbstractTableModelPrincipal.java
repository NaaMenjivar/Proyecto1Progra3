package presentacion.modelo;

import logica.entidades.lista.Lista;
import javax.swing.table.AbstractTableModel;

public abstract class AbstractTableModelPrincipal extends AbstractTableModel {
    protected Lista<Object> datos;
    protected String[] nombreColumnas;
    protected Class<?>[] tiposColumnas;

    public AbstractTableModelPrincipal() {
        this.datos = new Lista<>();
        this.nombreColumnas = new String[]{};
        this.tiposColumnas = new Class<?>[]{};
    }

    public AbstractTableModelPrincipal(Lista<Object> datos) {
        this.datos = datos != null ? datos : new Lista<>();
        this.nombreColumnas = new String[]{};
        this.tiposColumnas = new Class<?>[]{};
    }

    protected abstract String[] definirNombresColumnas();

    protected abstract Class<?>[] definirTiposColumnas();

    protected abstract Object obtenerValorEspecifico(Object objeto, int columna);

    protected abstract boolean esObjetoValido(Object objeto);

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

    public void setDatos(Lista<Object> nuevosDatos) {
        this.datos = nuevosDatos != null ? nuevosDatos : new Lista<>();
        fireTableDataChanged();
    }

    public Object getObjetoEnFila(int fila) {
        if (fila >= 0 && fila < datos.getTam()) {
            return datos.obtenerPorPos(fila);
        }
        return null;
    }

    public boolean agregarObjeto(Object objeto) {
        if (esObjetoValido(objeto)) {
            datos.agregarFinal(objeto);
            fireTableRowsInserted(datos.getTam() - 1, datos.getTam() - 1);
            return true;
        }
        return false;
    }

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

    public boolean actualizarObjeto(int fila, Object objetoActualizado) {
        if (fila >= 0 && fila < datos.getTam() && esObjetoValido(objetoActualizado)) {
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

    public void limpiar() {
        datos.vaciar();
        fireTableDataChanged();
    }

    public void agregarFila(Object objeto) { agregarObjeto(objeto); }
    public void eliminarFila(int indice) { eliminarObjeto(indice); }
    public void actualizarFila(int indice, Object objeto) { actualizarObjeto(indice, objeto); }

    public Lista<Object> getDatos() {
        return datos;
    }

    protected boolean coincideConCriterio(Object objeto, String criterio) {
        if (objeto == null || criterio == null) {
            return false;
        }
        return objeto.toString().toLowerCase().contains(criterio);
    }
}