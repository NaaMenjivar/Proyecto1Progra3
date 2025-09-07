package presentacion.modelo;

import javax.swing.table.AbstractTableModel;
import modelo.Usuario;
import modelo.lista.Lista;
import modelo.Administrador;
import modelo.Farmaceuta;
import modelo.Medico;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * MODEL - Modelo de tabla para usuarios siguiendo patrón MVC tradicional
 * Maneja tanto la tabla como el estado de los médicos con PropertyChangeSupport
 */
public class ModeloTablaUsuarios extends AbstractTableModel {

    // ============================================
    // CONSTANTES PARA PROPERTY CHANGE
    // ============================================
    public static final String LIST = "list";
    public static final String CURRENT = "current";
    public static final String FILTER = "filter";

    // ============================================
    // ATRIBUTOS DE TABLA
    // ============================================
    private Lista<Usuario> usuarios;
    private String[] columnas = {"ID", "Nombre", "Especialidad/Tipo", "Estado"};

    // ============================================
    // ATRIBUTOS DE ESTADO MVC
    // ============================================
    private Lista<Usuario> listaCompleta;      // Lista completa sin filtros
    private Medico medicoActual;               // Médico seleccionado actualmente
    private String filtroActual;               // Filtro de búsqueda actual
    private boolean modoEdicion;               // Si está en modo edición

    // Para el patrón Observer
    private PropertyChangeSupport propertyChangeSupport;

    // ============================================
    // CONSTRUCTOR
    // ============================================
    public ModeloTablaUsuarios() {
        this.usuarios = new Lista<>();
        this.listaCompleta = new Lista<>();
        this.medicoActual = new Medico(); // Médico vacío por defecto
        this.filtroActual = "";
        this.modoEdicion = false;
        this.propertyChangeSupport = new PropertyChangeSupport(this);
    }

    // ============================================
    // MÉTODOS DE AbstractTableModel (OBLIGATORIOS)
    // ============================================

    @Override
    public int getRowCount() {
        return usuarios != null ? usuarios.getTam() : 0;
    }

    @Override
    public int getColumnCount() {
        return columnas.length;
    }

    @Override
    public String getColumnName(int column) {
        return column >= 0 && column < columnas.length ? columnas[column] : "";
    }

    @Override
    public Object getValueAt(int row, int col) {
        if (row < 0 || row >= usuarios.getTam() || col < 0 || col >= columnas.length) {
            return "";
        }

        Usuario usuario = usuarios.obtenerPorPos(row);
        if (usuario == null) {
            return "";
        }

        switch (col) {
            case 0: // ID
                return usuario.getId() != null ? usuario.getId() : "";

            case 1: // Nombre
                return usuario.getNombre() != null ? usuario.getNombre() : "";

            case 2: // Especialidad/Tipo
                if (usuario instanceof Medico) {
                    Medico medico = (Medico) usuario;
                    return medico.getEspecialidad() != null ? medico.getEspecialidad() : "Sin especialidad";
                } else if (usuario instanceof Farmaceuta) {
                    return "Farmaceuta";
                } else if (usuario instanceof Administrador) {
                    return "Administrador";
                } else {
                    return usuario.getTipo().getDescripcion();
                }

            case 3: // Estado
                return usuario.isSesionActiva() ? "Activo" : "Inactivo";

            default:
                return "";
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        // Solo permitir edición de nombre y especialidad
        return column == 1 || column == 2;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= usuarios.getTam() || columnIndex < 0 || columnIndex >= columnas.length) {
            return;
        }

        Usuario usuario = usuarios.obtenerPorPos(rowIndex);
        if (usuario == null || aValue == null) {
            return;
        }

        try {
            switch (columnIndex) {
                case 1: // Nombre
                    usuario.setNombre(aValue.toString().trim());
                    fireTableCellUpdated(rowIndex, columnIndex);
                    break;

                case 2: // Especialidad (solo para médicos)
                    if (usuario instanceof Medico && !aValue.toString().trim().isEmpty()) {
                        ((Medico) usuario).setEspecialidad(aValue.toString().trim());
                        fireTableCellUpdated(rowIndex, columnIndex);
                    }
                    break;

                case 3: // Estado
                    boolean estado = "Activo".equals(aValue.toString());
                    usuario.setSesionActiva(estado);
                    fireTableCellUpdated(rowIndex, columnIndex);
                    break;
            }
        } catch (Exception e) {
            System.err.println("Error al actualizar celda: " + e.getMessage());
        }
    }

    // ============================================
    // MÉTODOS PARA PROPERTY CHANGE (OBSERVER)
    // ============================================

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    private void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }

    // ============================================
    // MÉTODOS DE TABLA OPTIMIZADOS
    // ============================================

    /**
     * Establece la lista de usuarios y notifica cambios
     */
    public void setUsuarios(Lista<Usuario> usuarios) {
        Lista<Usuario> oldList = this.usuarios;
        this.usuarios = usuarios != null ? usuarios : new Lista<>();

        // También guardar como lista completa si no hay filtro
        if (filtroActual.isEmpty()) {
            this.listaCompleta = this.usuarios;
        }

        fireTableDataChanged();
        firePropertyChange(LIST, oldList, this.usuarios);
    }

    /**
     * Obtiene el usuario en una fila específica
     */
    public Usuario getUsuarioEnFila(int fila) {
        if (fila >= 0 && fila < usuarios.getTam()) {
            return usuarios.obtenerPorPos(fila);
        }
        return null;
    }

    /**
     * Agrega un usuario y notifica cambios
     */
    public void agregarUsuario(Usuario usuario) {
        if (usuario != null) {
            usuarios.agregarFinal(usuario);
            listaCompleta.agregarFinal(usuario);
            int filaInsertada = usuarios.getTam() - 1;
            fireTableRowsInserted(filaInsertada, filaInsertada);
            firePropertyChange(LIST, null, usuarios);
        }
    }

    /**
     * Elimina un usuario por posición
     */
    public boolean eliminarUsuario(int fila) {
        if (fila >= 0 && fila < usuarios.getTam()) {
            Usuario usuario = usuarios.obtenerPorPos(fila);
            if (usuario != null) {
                boolean eliminado = usuarios.eliminarPorId(usuario.getId());
                listaCompleta.eliminarPorId(usuario.getId());
                if (eliminado) {
                    fireTableRowsDeleted(fila, fila);
                    firePropertyChange(LIST, null, usuarios);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Busca un usuario por ID y retorna su posición
     */
    public int buscarUsuarioPorId(String id) {
        if (id != null && !id.trim().isEmpty()) {
            for (int i = 0; i < usuarios.getTam(); i++) {
                Usuario usuario = usuarios.obtenerPorPos(i);
                if (usuario != null && id.equals(usuario.getId())) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Limpia toda la tabla
     */
    public void limpiar() {
        int tamañoAnterior = usuarios.getTam();
        Lista<Usuario> oldList = this.usuarios;
        this.usuarios = new Lista<>();
        this.listaCompleta = new Lista<>();
        if (tamañoAnterior > 0) {
            fireTableRowsDeleted(0, tamañoAnterior - 1);
        }
        firePropertyChange(LIST, oldList, this.usuarios);
    }

    // ============================================
    // MÉTODOS DE ESTADO MVC
    // ============================================

    /**
     * Obtiene la lista completa de usuarios (sin filtros)
     */
    public Lista<Usuario> getListaCompleta() {
        return listaCompleta;
    }

    /**
     * Establece la lista completa de usuarios
     */
    public void setListaCompleta(Lista<Usuario> listaCompleta) {
        Lista<Usuario> oldList = this.listaCompleta;
        this.listaCompleta = listaCompleta != null ? listaCompleta : new Lista<>();

        // Si no hay filtro, mostrar toda la lista
        if (filtroActual.isEmpty()) {
            setUsuarios(this.listaCompleta);
        }

        firePropertyChange(LIST, oldList, this.listaCompleta);
    }

    /**
     * Obtiene el médico actual seleccionado
     */
    public Medico getMedicoActual() {
        return medicoActual;
    }

    /**
     * Establece el médico actual y notifica cambios
     */
    public void setMedicoActual(Medico medicoActual) {
        Medico oldCurrent = this.medicoActual;
        this.medicoActual = medicoActual != null ? medicoActual : new Medico();
        firePropertyChange(CURRENT, oldCurrent, this.medicoActual);
    }

    /**
     * Obtiene el filtro actual
     */
    public String getFiltroActual() {
        return filtroActual;
    }

    /**
     * Establece el filtro actual y aplica filtrado
     */
    public void setFiltroActual(String filtroActual) {
        String oldFilter = this.filtroActual;
        this.filtroActual = filtroActual != null ? filtroActual : "";

        // Aplicar filtro
        aplicarFiltro();

        firePropertyChange(FILTER, oldFilter, this.filtroActual);
    }

    /**
     * Verifica si está en modo edición
     */
    public boolean isModoEdicion() {
        return modoEdicion;
    }

    /**
     * Establece el modo edición
     */
    public void setModoEdicion(boolean modoEdicion) {
        this.modoEdicion = modoEdicion;
    }

    // ============================================
    // MÉTODOS DE FILTRADO
    // ============================================

    /**
     * Aplica el filtro actual a la lista completa
     */
    private void aplicarFiltro() {
        if (filtroActual.isEmpty()) {
            // Sin filtro, mostrar toda la lista
            setUsuarios(listaCompleta);
        } else {
            // Con filtro, buscar coincidencias
            Lista<Usuario> usuariosFiltrados = new Lista<>();
            String filtroLower = filtroActual.toLowerCase();

            for (int i = 0; i < listaCompleta.getTam(); i++) {
                Usuario usuario = listaCompleta.obtenerPorPos(i);
                if (usuario != null) {
                    String nombre = usuario.getNombre() != null ? usuario.getNombre().toLowerCase() : "";
                    String id = usuario.getId() != null ? usuario.getId().toLowerCase() : "";
                    String especialidad = "";

                    if (usuario instanceof Medico) {
                        especialidad = ((Medico) usuario).getEspecialidad();
                        especialidad = especialidad != null ? especialidad.toLowerCase() : "";
                    }

                    if (nombre.contains(filtroLower) ||
                            id.contains(filtroLower) ||
                            especialidad.contains(filtroLower)) {
                        usuariosFiltrados.agregarFinal(usuario);
                    }
                }
            }

            setUsuarios(usuariosFiltrados);
        }
    }

    /**
     * Remueve el filtro y muestra toda la lista
     */
    public void removerFiltro() {
        setFiltroActual("");
    }

    // ============================================
    // MÉTODOS DE UTILIDAD
    // ============================================

    /**
     * Obtiene todos los usuarios (para acceso desde el controlador)
     */
    public Lista<Usuario> getUsuarios() {
        return usuarios;
    }

    /**
     * Verifica si la tabla está vacía
     */
    public boolean estaVacia() {
        return usuarios.getTam() == 0;
    }

    /**
     * Obtiene estadísticas rápidas
     */
    public String obtenerEstadisticasRapidas() {
        if (usuarios.getTam() == 0) {
            return "Sin usuarios registrados";
        }

        int medicos = 0, farmaceutas = 0, administradores = 0, activos = 0;

        for (int i = 0; i < usuarios.getTam(); i++) {
            Usuario usuario = usuarios.obtenerPorPos(i);
            if (usuario instanceof Medico) medicos++;
            else if (usuario instanceof Farmaceuta) farmaceutas++;
            else if (usuario instanceof Administrador) administradores++;

            if (usuario.isSesionActiva()) activos++;
        }

        return String.format("Total: %d | Médicos: %d | Farmaceutas: %d | Admins: %d | Activos: %d",
                usuarios.getTam(), medicos, farmaceutas, administradores, activos);
    }

    /**
     * Selecciona un médico por ID y lo establece como actual
     */
    public boolean seleccionarMedicoPorId(String id) {
        for (int i = 0; i < usuarios.getTam(); i++) {
            Usuario usuario = usuarios.obtenerPorPos(i);
            if (usuario instanceof Medico && usuario.getId().equals(id)) {
                setMedicoActual((Medico) usuario);
                return true;
            }
        }
        return false;
    }

    /**
     * Limpia el médico actual (para nuevo médico)
     */
    public void limpiarMedicoActual() {
        setMedicoActual(new Medico());
        setModoEdicion(false);
    }
}