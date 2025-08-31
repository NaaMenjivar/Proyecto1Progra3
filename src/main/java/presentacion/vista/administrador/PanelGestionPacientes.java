package presentacion.vista.administrador;

import logica.*;
import logica.excepciones.CatalogoException;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.*;
import modelo.*;
import modelo.lista.Lista;
import presentacion.modelo.ModeloTablaPacientes;
import java.time.LocalDate;

public class PanelGestionPacientes extends JPanel {
    private JTable tabla;
    private ModeloTablaPacientes modeloTabla;
    private JButton btnAgregar;
    private JButton btnModificar;
    private JButton btnEliminar;
    private JTextField campoFiltro;
    private GestorCatalogos gestor;

    public PanelGestionPacientes() {
        initGestores();
        initComponents();
        initEvents();
        initData();
    }

    private void initGestores() {
        gestor = new GestorCatalogos();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Gestión de Pacientes"));

        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        campoFiltro = new JTextField(20);
        panelFiltro.add(new JLabel("Filtrar:"));
        panelFiltro.add(campoFiltro);
        add(panelFiltro, BorderLayout.NORTH);

        modeloTabla = new ModeloTablaPacientes();
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(tabla);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnAgregar = new JButton("Agregar");
        btnModificar = new JButton("Modificar");
        btnEliminar = new JButton("Eliminar");

        panelBotones.add(btnAgregar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void initEvents() {
        btnAgregar.addActionListener(e -> agregarPaciente());
        btnModificar.addActionListener(e -> modificarPaciente());
        btnEliminar.addActionListener(e -> eliminarPaciente());

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                actualizarBotones();
            }
        });

        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    modificarPaciente();
                }
            }
        });

        campoFiltro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filtrarPacientes();
            }
        });
    }

    private void initData() {
        cargarPacientes();
        actualizarBotones();
    }

    private void agregarPaciente() {
        String id = JOptionPane.showInputDialog(this, "ID del paciente:");
        if (id == null || id.trim().isEmpty()) return;

        String nombre = JOptionPane.showInputDialog(this, "Nombre del paciente:");
        if (nombre == null || nombre.trim().isEmpty()) return;

        String fechaStr = JOptionPane.showInputDialog(this, "Fecha de nacimiento (YYYY-MM-DD):");
        if (fechaStr == null || fechaStr.trim().isEmpty()) return;
        LocalDate fechaNacimiento;
        try {
            fechaNacimiento = LocalDate.parse(fechaStr.trim());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Fecha inválida. Use formato YYYY-MM-DD", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String telefono = JOptionPane.showInputDialog(this, "Teléfono:");
        if (telefono == null || telefono.trim().isEmpty()) return;

        try {
            Paciente paciente = new Paciente(id, nombre, fechaNacimiento, telefono);
            boolean exito = gestor.agregarPaciente(paciente);
            if (exito) {
                cargarPacientes();
                JOptionPane.showMessageDialog(this, "Paciente agregado exitosamente");
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo agregar el paciente", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificarPaciente() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un paciente para modificar");
            return;
        }

        Paciente paciente = modeloTabla.getPacienteEnFila(fila);
        if (paciente != null) {
            String nuevoNombre = JOptionPane.showInputDialog(this, "Nuevo nombre:", paciente.getNombre());
            if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) return;

            String nuevaFechaStr = JOptionPane.showInputDialog(this, "Nueva fecha de nacimiento (YYYY-MM-DD):", paciente.getFechaNacimiento());
            if (nuevaFechaStr == null || nuevaFechaStr.trim().isEmpty()) return;
            LocalDate nuevaFecha;
            try {
                nuevaFecha = LocalDate.parse(nuevaFechaStr.trim());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Fecha inválida. Use formato YYYY-MM-DD", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String nuevoTelefono = JOptionPane.showInputDialog(this, "Nuevo teléfono:", paciente.getTelefono());
            if (nuevoTelefono == null || nuevoTelefono.trim().isEmpty()) return;

            paciente.setNombre(nuevoNombre);
            paciente.setFechaNacimiento(nuevaFecha);
            paciente.setTelefono(nuevoTelefono);

            try {
                boolean exito = gestor.actualizarPaciente(paciente);
                if (exito) {
                    cargarPacientes();
                    JOptionPane.showMessageDialog(this, "Paciente actualizado exitosamente");
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo actualizar el paciente", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarPaciente() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un paciente para eliminar");
            return;
        }

        Paciente paciente = modeloTabla.getPacienteEnFila(fila);
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar el paciente " + paciente.getNombre() + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                boolean exito = gestor.eliminarPaciente(paciente.getId());
                if (exito) {
                    cargarPacientes();
                    JOptionPane.showMessageDialog(this, "Paciente eliminado exitosamente");
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo eliminar el paciente", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void actualizarBotones() {
        boolean haySeleccion = tabla.getSelectedRow() >= 0;
        btnModificar.setEnabled(haySeleccion);
        btnEliminar.setEnabled(haySeleccion);
    }

    private void cargarPacientes() {
        Lista<Paciente> pacientes = gestor.obtenerTodosPacientes();
        modeloTabla.setPacientes(pacientes);
    }

    private void filtrarPacientes() {
        String filtro = campoFiltro.getText().trim().toLowerCase();

        if (filtro.isEmpty()) {
            cargarPacientes();
            return;
        }

        Lista<Paciente> todosPacientes = gestor.obtenerTodosPacientes();
        Lista<Paciente> pacientesFiltrados = new Lista<>();

        for (int i = 0; i < todosPacientes.getTam(); i++) {
            Paciente paciente = todosPacientes.obtenerPorPos(i);
            String nombre = paciente.getNombre().toLowerCase();
            String id = paciente.getId().toLowerCase();
            String telefono = paciente.getTelefono().toLowerCase();

            if (nombre.contains(filtro) || id.contains(filtro) || telefono.contains(filtro)) {
                pacientesFiltrados.agregarFinal(paciente);
            }
        }

        modeloTabla.setPacientes(pacientesFiltrados);
    }

    public void refrescarDatos() {
        cargarPacientes();
    }

    public int getNumeroPacientes() {
        return modeloTabla.getRowCount();
    }
}
