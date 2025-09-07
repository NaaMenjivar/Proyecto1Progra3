package presentacion.modelo;

import javax.swing.table.AbstractTableModel;
import modelo.Usuario;
import modelo.lista.Lista;
import modelo.Administrador;
import modelo.Farmaceuta;
import modelo.Medico;

public class ModeloTablaUsuarios extends AbstractTableModel {
    private Lista<Usuario> usuarios;
    private String[] columnas = {"ID", "Nombre", "Especialidad", "Estado"};

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

    public void limpiar() {
        this.usuarios = new Lista<>();
        fireTableDataChanged();
    }

    public void addRow(Object[] fila) {
        // fila = {id, nombre, especialidad/tipo}
        String id = (String) fila[0];
        String nombre = (String) fila[1];
        String especialidad = (String) fila[2];

        // Por defecto creamos un médico si se usa addRow
        Usuario usuario = new Medico(id, nombre, id, especialidad);

        usuarios.agregarFinal(usuario);
        fireTableRowsInserted(usuarios.getTam() - 1, usuarios.getTam() - 1);
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Usuario usuario = usuarios.obtenerPorPos(rowIndex);
        if (usuario != null) {
            switch (columnIndex) {
                case 0: // ID - normalmente no se cambia
                    break;
                case 1: // Nombre
                    usuario.setNombre((String) aValue);
                    break;
                case 2: // Especialidad (solo para médicos)
                    if (usuario instanceof Medico) {
                        ((Medico) usuario).setEspecialidad((String) aValue);
                    }
                    break;
                case 3: // Estado
                    usuario.setSesionActiva("Activo".equals(aValue));
                    break;
            }
            fireTableCellUpdated(rowIndex, columnIndex);
        }
    }

    public void removeRow(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < usuarios.getTam()) {
            // Para simular eliminación, creamos una nueva lista sin el elemento
            Lista<Usuario> nuevaLista = new Lista<>();
            for (int i = 0; i < usuarios.getTam(); i++) {
                if (i != rowIndex) {
                    nuevaLista.agregarFinal(usuarios.obtenerPorPos(i));
                }
            }
            usuarios = nuevaLista;
            fireTableRowsDeleted(rowIndex, rowIndex);
        }
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
        // Solo permitir edición de nombre y especialidad
        return column == 1 || column == 2;
    }

    public Usuario getUsuarioEnFila(int fila) {
        if (fila >= 0 && fila < usuarios.getTam()) {
            return usuarios.obtenerPorPos(fila);
        }
        return null;
    }

    // Método para obtener todos los usuarios
    public Lista<Usuario> getUsuarios() {
        return usuarios;
    }

    // Método para agregar un usuario específico
    public void agregarUsuario(Usuario usuario) {
        usuarios.agregarFinal(usuario);
        fireTableRowsInserted(usuarios.getTam() - 1, usuarios.getTam() - 1);
    }

    // Método para buscar un usuario por ID
    public int buscarUsuarioPorId(String id) {
        for (int i = 0; i < usuarios.getTam(); i++) {
            if (usuarios.obtenerPorPos(i).getId().equals(id)) {
                return i;
            }
        }
        return -1;
    }

    // Método para actualizar un usuario en una posición específica
    public void actualizarUsuario(int fila, Usuario usuario) {
        if (fila >= 0 && fila < usuarios.getTam()) {
            // Como no tenemos un método directo para reemplazar, simulamos
            Lista<Usuario> nuevaLista = new Lista<>();
            for (int i = 0; i < usuarios.getTam(); i++) {
                if (i == fila) {
                    nuevaLista.agregarFinal(usuario);
                } else {
                    nuevaLista.agregarFinal(usuarios.obtenerPorPos(i));
                }
            }
            usuarios = nuevaLista;
            fireTableRowsUpdated(fila, fila);
        }
    }
}