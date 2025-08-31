package presentacion.vista.administrador;

import logica.*;
import javax.swing.*;
import java.awt.*;
import modelo.*;
import modelo.lista.Lista;
import presentacion.modelo.ModeloTablaUsuarios;
import javax.swing.border.EmptyBorder;

public class PanelGestionMedicos extends JPanel {
    // Sección Médico
    private JTextField txtId;
    private JTextField txtNombre;
    private JTextField txtEspecialidad;
    private JButton btnGuardar;
    private JButton btnLimpiar;
    private JButton btnBorrar;

    // Sección Búsqueda
    private JTextField txtBusquedaNombre;
    private JButton btnBuscar;
    private JButton btnReporte;

    // Sección Listado
    private JTable tabla;
    private ModeloTablaUsuarios modeloTabla;

    // Gestores
    private GestorCatalogos gestor;

    public PanelGestionMedicos() {
        initGestores();
        initComponents();
        initEvents();
        initData();
    }

    private void initGestores() {
        gestor = new GestorCatalogos();
    }

    private void initComponents() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(10, 10, 10, 10)); // 👈 margen uniforme

        // Panel Médico
        JPanel panelMedico = new JPanel(new GridBagLayout());
        panelMedico.setBorder(BorderFactory.createTitledBorder("Médico"));
        panelMedico.setAlignmentX(Component.LEFT_ALIGNMENT); // 👈 alinear izquierda
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        panelMedico.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        txtId = new JTextField(15);
        panelMedico.add(txtId, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panelMedico.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        txtNombre = new JTextField(15);
        panelMedico.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panelMedico.add(new JLabel("Especialidad:"), gbc);
        gbc.gridx = 1;
        txtEspecialidad = new JTextField(15);
        panelMedico.add(txtEspecialidad, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        JPanel panelBotonesMedico = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0)); // 👈 botones a la izquierda
        btnGuardar = new JButton("Guardar");
        btnLimpiar = new JButton("Limpiar");
        btnBorrar = new JButton("Borrar");
        panelBotonesMedico.add(btnGuardar);
        panelBotonesMedico.add(btnLimpiar);
        panelBotonesMedico.add(btnBorrar);
        panelMedico.add(panelBotonesMedico, gbc);

        add(panelMedico);

        // Panel Búsqueda
        JPanel panelBusqueda = new JPanel(new GridBagLayout());
        panelBusqueda.setBorder(BorderFactory.createTitledBorder("Búsqueda"));
        panelBusqueda.setAlignmentX(Component.LEFT_ALIGNMENT); // 👈 alinear izquierda
        GridBagConstraints gbcBusqueda = new GridBagConstraints();
        gbcBusqueda.insets = new Insets(5,5,5,5);
        gbcBusqueda.anchor = GridBagConstraints.WEST;

        gbcBusqueda.gridx = 0; gbcBusqueda.gridy = 0;
        panelBusqueda.add(new JLabel("Nombre:"), gbcBusqueda);
        gbcBusqueda.gridx = 1;
        txtBusquedaNombre = new JTextField(15);
        panelBusqueda.add(txtBusquedaNombre, gbcBusqueda);

        gbcBusqueda.gridx = 0; gbcBusqueda.gridy = 1; gbcBusqueda.gridwidth = 2;
        JPanel panelBotonesBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0)); // 👈 botones a la izquierda
        btnBuscar = new JButton("Buscar");
        btnReporte = new JButton("Reporte");
        panelBotonesBusqueda.add(btnBuscar);
        panelBotonesBusqueda.add(btnReporte);
        panelBusqueda.add(panelBotonesBusqueda, gbcBusqueda);

        add(panelBusqueda);

        // Panel Listado
        JPanel panelListado = new JPanel(new BorderLayout());
        panelListado.setBorder(BorderFactory.createTitledBorder("Listado"));
        panelListado.setAlignmentX(Component.LEFT_ALIGNMENT); // 👈 alinear izquierda
        modeloTabla = new ModeloTablaUsuarios();
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(tabla);
        panelListado.add(scrollPane, BorderLayout.CENTER);
        panelListado.setPreferredSize(new Dimension(0, 200));
        add(panelListado);
    }

    private void initEvents() {
        btnGuardar.addActionListener(e -> guardarMedico());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnBorrar.addActionListener(e -> borrarMedico());
        btnBuscar.addActionListener(e -> buscarMedico());
        btnReporte.addActionListener(e -> generarReporte());

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarDatosSeleccionados();
                actualizarBotones();
            }
        });

        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    modificarMedico();
                }
            }
        });
    }

    private void initData() {
        cargarMedicos();
        actualizarBotones();
    }

    private void guardarMedico() {
        String id = txtId.getText().trim();
        String nombre = txtNombre.getText().trim();
        String especialidad = txtEspecialidad.getText().trim();

        if (id.isEmpty() || nombre.isEmpty() || especialidad.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Medico medico = new Medico(id, nombre, id, especialidad); // clave = id por defecto
            boolean exito = gestor.agregarMedico(medico);
            if (exito) {
                cargarMedicos();
                limpiarFormulario();
                JOptionPane.showMessageDialog(this, "Médico guardado exitosamente");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarFormulario() {
        txtId.setText("");
        txtNombre.setText("");
        txtEspecialidad.setText("");
        tabla.clearSelection();
    }

    private void borrarMedico() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un médico para eliminar", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario usuario = modeloTabla.getUsuarioEnFila(fila);
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar al médico " + usuario.getNombre() + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                boolean exito = gestor.eliminarUsuario(usuario.getId());
                if (exito) {
                    cargarMedicos();
                    limpiarFormulario();
                    JOptionPane.showMessageDialog(this, "Médico eliminado exitosamente");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void buscarMedico() {
        String busqueda = txtBusquedaNombre.getText().trim().toLowerCase();
        if (busqueda.isEmpty()) {
            cargarMedicos();
            return;
        }

        Lista<Usuario> todosMedicos = gestor.buscarMedicos();
        Lista<Usuario> medicosFiltrados = new Lista<>();

        for (int i = 0; i < todosMedicos.getTam(); i++) {
            Usuario medico = todosMedicos.obtenerPorPos(i);
            String nombre = medico.getNombre().toLowerCase();
            String id = medico.getId().toLowerCase();
            if (nombre.contains(busqueda) || id.contains(busqueda)) {
                medicosFiltrados.agregarFinal(medico);
            }
        }

        modeloTabla.setUsuarios(medicosFiltrados);
    }

    private void generarReporte() {
        JOptionPane.showMessageDialog(this, "Funcionalidad de reporte en desarrollo", "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private void modificarMedico() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un médico para modificar");
            return;
        }

        Usuario usuario = modeloTabla.getUsuarioEnFila(fila);
        if (usuario instanceof Medico) {
            Medico medico = (Medico) usuario;

            String nuevoNombre = JOptionPane.showInputDialog(this, "Nuevo nombre:", medico.getNombre());
            if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()) {
                medico.setNombre(nuevoNombre);

                String nuevaEspecialidad = JOptionPane.showInputDialog(this, "Nueva especialidad:", medico.getEspecialidad());
                if (nuevaEspecialidad != null && !nuevaEspecialidad.trim().isEmpty()) {
                    medico.setEspecialidad(nuevaEspecialidad);

                    try {
                        boolean exito = gestor.actualizarUsuario(medico);
                        if (exito) {
                            cargarMedicos();
                            JOptionPane.showMessageDialog(this, "Médico actualizado exitosamente");
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }

    private void cargarDatosSeleccionados() {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            Usuario usuario = modeloTabla.getUsuarioEnFila(fila);
            if (usuario instanceof Medico) {
                Medico medico = (Medico) usuario;
                txtId.setText(medico.getId());
                txtNombre.setText(medico.getNombre());
                txtEspecialidad.setText(medico.getEspecialidad());
            }
        }
    }

    private void actualizarBotones() {
        boolean haySeleccion = tabla.getSelectedRow() >= 0;
        btnBorrar.setEnabled(haySeleccion);
    }

    private void cargarMedicos() {
        Lista<Usuario> medicos = gestor.buscarMedicos();
        modeloTabla.setUsuarios(medicos);
    }
}

