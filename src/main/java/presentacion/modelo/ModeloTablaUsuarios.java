package presentacion.modelo;

import javax.swing.table.AbstractTableModel;
import modelo.Usuario;
import modelo.lista.Lista;

public class ModeloTablaUsuarios extends AbstractTableModel {
    private Lista<Usuario> usuarios;
    private String[] columnas = {"ID", "Nombre", "Tipo", "Estado"};

    public ModeloTablaUsuarios() {
        this.usuarios = new Lista<>();
    }

    public void setUsuarios(Lista<Usuario> usuarios) {
        this.usuarios = usuarios;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return usuarios.getTam();
    }

    @Override
    public int getColumnCount() {
        return columnas.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnas[column];
    }

    @Override
    public Object getValueAt(int row, int col) {
        Usuario usuario = usuarios.obtenerPorPos(row);
        switch (col) {
            case 0: return usuario.getId();
            case 1: return usuario.getNombre();
            case 2: return usuario.getTipo().getDescripcion();
            case 3: return usuario.isSesionActiva() ? "Activo" : "Inactivo";
            default: return "";
        }
    }

    public Usuario getUsuarioEnFila(int fila) {
        if (fila >= 0 && fila < usuarios.getTam()) {
            return usuarios.obtenerPorPos(fila);
        }
        return null;
    }
}
