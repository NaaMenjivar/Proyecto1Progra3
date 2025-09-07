package presentacion.modelo;

import javax.swing.table.AbstractTableModel;
import modelo.Usuario;
import modelo.lista.Lista;
import modelo.Administrador;
import modelo.Farmaceuta;
import modelo.Medico;

/**
 * MODELO - MVC Simple
 * Responsabilidad: Solo gestionar los datos de la tabla
 */
public class ModeloTablaUsuarios extends AbstractTableModel {

    private Lista<Usuario> usuarios;
    private String[] columnas = {"ID", "Nombre", "Especialidad", "Estado"};

    public ModeloTablaUsuarios() {
        this.usuarios = new Lista<>();
    }

    // ================================
    // MÉTODOS BÁSICOS DEL MODELO
    // ================================

    public void setUsuarios(Lista<Usuario> usuarios) {
        this.usuarios = usuarios;
        fireTableDataChanged(); // Notifica a la tabla que los datos cambiaron
    }

    public Lista<Usuario> getUsuarios() {
        return usuarios;
    }

    public Usuario getUsuarioEnFila(int fila) {
        if (fila >= 0 && fila < usuarios.getTam()) {
            return usuarios.obtenerPorPos(fila);
        }
        return null;
    }

    public void agregarUsuario(Usuario usuario) {
        usuarios.agregarFinal(usuario);
        fireTableRowsInserted(usuarios.getTam() - 1, usuarios.getTam() - 1);
    }

    public void limpiar() {
        this.usuarios = new Lista<>();
        fireTableDataChanged();
    }

    // ================================
    // MÉTODOS DE AbstractTableModel
    // ================================

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
        if (usuario == null) return "";

        switch (col) {
            case 0: return usuario.getId();
            case 1: return usuario.getNombre();
            case 2:
                // Mostrar especialidad para médicos, tipo para otros
                if (usuario instanceof Medico) {
                    return ((Medico) usuario).getEspecialidad();
                } else {
                    return usuario.getTipo().getDescripcion();
                }
            case 3: return usuario.isSesionActiva() ? "Activo" : "Inactivo";
            default: return "";
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false; // No permitir edición directa en la tabla
    }
}