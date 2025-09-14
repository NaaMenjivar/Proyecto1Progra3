package presentacion.vista.administrador;

import logica.entidades.lista.ListaPacientes;
import presentacion.controlador.ControladorPrincipal;
import presentacion.modelo.TableModelPrincipal;
import logica.entidades.*;
import logica.entidades.lista.Lista;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class PanelGestionPacientes {
    private JPanel panelPrincipal;
    private JTextField idFld;
    private JTextField nombreFld;
    private JTextField telefonoFld;
    private JSpinner fechaNacimientoFld;
    private JButton guardar;
    private JButton limpiar;
    private JButton borrar;
    private JTextField nombreBusquedaFld;
    private JButton buscar;
    private JButton reporte;
    private JTable list;

    private ControladorPrincipal controlador;
    private TableModelPrincipal tableModel;
    private boolean modoEdicion = false;
    private String idSeleccionado = null;

    public PanelGestionPacientes(ControladorPrincipal controlador) {
        this.controlador = controlador;
        inicializarComponentes();
        configurarEventos();
        cargarDatos();
    }

    private void inicializarComponentes() {
        tableModel = TableModelPrincipal.crearModeloPacientes();
        list.setModel(tableModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        list.getColumnModel().getColumn(0).setPreferredWidth(80);  // ID
        list.getColumnModel().getColumn(1).setPreferredWidth(200); // Nombre
        list.getColumnModel().getColumn(2).setPreferredWidth(120); // Fecha Nacimiento
        list.getColumnModel().getColumn(3).setPreferredWidth(100); // Teléfono

        list.setRowHeight(25);
        list.getTableHeader().setReorderingAllowed(false);

        fechaNacimientoFld.setModel(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(fechaNacimientoFld, "dd/MM/yyyy");
        fechaNacimientoFld.setEditor(dateEditor);
    }

    private void configurarEventos() {
        guardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarPaciente();
            }
        });

        limpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarCampos();
            }
        });

        borrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarPaciente();
            }
        });

        buscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarPacientes();
            }
        });

        nombreBusquedaFld.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarPacientes();
            }
        });

        reporte.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generarReporte();
            }
        });

        list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    cargarPacienteSeleccionado();
                }
            }
        });
    }

    private void guardarPaciente() {
        try {
            String id = idFld.getText().trim();
            String nombre = nombreFld.getText().trim();
            String telefono = telefonoFld.getText().trim();

            if (id.isEmpty() || nombre.isEmpty()) {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "ID y nombre son obligatorios",
                        "Datos incompletos",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Date fechaDate = (Date) fechaNacimientoFld.getValue();
            LocalDate fechaNacimiento = fechaDate.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            if (modoEdicion) {

                JOptionPane.showMessageDialog(panelPrincipal,
                        "Funcionalidad de edición en desarrollo",
                        "En desarrollo",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                if (controlador.agregarPaciente(id, nombre, fechaNacimiento, telefono)) {
                    limpiarCampos();
                    cargarTodosPacientes();
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Error al guardar paciente: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarPaciente() {
        if (idSeleccionado == null) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Seleccione un paciente de la lista para eliminar",
                    "Paciente no seleccionado",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            controlador.eliminarPaciente(idSeleccionado);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Error al eliminar paciente: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarPacientes() {
        String criterio = nombreBusquedaFld.getText().trim();

        try {
            if (criterio.isEmpty()) {
                cargarTodosPacientes();
            } else {
                Paciente pacientesEncontrados = controlador.getModelo().buscarPacientesPorNombre(criterio);
                Lista<Object> datos = new Lista<>();

                datos.agregarFinal(pacientesEncontrados);



                tableModel.setDatos(datos);

                if (datos.getTam() == 0) {
                    JOptionPane.showMessageDialog(panelPrincipal,
                            "No se encontraron pacientes con el criterio: " + criterio,
                            "Sin resultados",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Error al buscar pacientes: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarPacienteSeleccionado() {
        int filaSeleccionada = list.getSelectedRow();
        if (filaSeleccionada >= 0) {
            try {
                Object elemento = tableModel.getObjetoEnFila(filaSeleccionada);
                if (elemento instanceof Paciente) {
                    Paciente paciente = (Paciente) elemento;

                    idFld.setText(paciente.getId());
                    nombreFld.setText(paciente.getNombre());
                    telefonoFld.setText(paciente.getTelefono());

                    if (paciente.getFechaNacimiento() != null) {
                        Date fecha = Date.from(paciente.getFechaNacimiento()
                                .atStartOfDay(ZoneId.systemDefault()).toInstant());
                        fechaNacimientoFld.setValue(fecha);
                    }

                    idSeleccionado = paciente.getId();
                    modoEdicion = true;

                    idFld.setEnabled(false);
                }
            } catch (Exception e) {
                limpiarCampos();
            }
        }
    }

    private void cargarTodosPacientes() {
        try {
            ListaPacientes pacientes = controlador.getModelo().obtenerPacientes();
            Lista<Object> datos = new Lista<>();

            for (Paciente paciente : pacientes) {
                datos.agregarFinal(paciente);
            }

            tableModel.setDatos(datos);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Error al cargar pacientes: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        idFld.setText("");
        nombreFld.setText("");
        telefonoFld.setText("");
        fechaNacimientoFld.setValue(new Date());
        nombreBusquedaFld.setText("");

        idSeleccionado = null;
        modoEdicion = false;
        idFld.setEnabled(true);

        list.clearSelection();
    }

    private void generarReporte() {
        try {
            String reporte = controlador.generarReporteCompleto();

            JTextArea textArea = new JTextArea(reporte);
            textArea.setEditable(false);
            textArea.setFont(new java.awt.Font("Monospaced", 0, 12));

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new java.awt.Dimension(600, 400));

            JOptionPane.showMessageDialog(panelPrincipal,
                    scrollPane,
                    "Reporte del Sistema",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Error al generar reporte: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarDatos() {
        cargarTodosPacientes();
    }
    public JPanel getPanel() {
        return panelPrincipal;
    }
    public void refrescarDatos() {
        cargarTodosPacientes();
        limpiarCampos();
    }
    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }
}