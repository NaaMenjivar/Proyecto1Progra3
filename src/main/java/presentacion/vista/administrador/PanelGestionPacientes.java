package presentacion.vista.administrador;

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

/**
 * Panel de Gestión de Pacientes - Vista MVC
 */
public class PanelGestionPacientes {
    // Componentes del formulario (declarados en el .form)
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

    // MVC Components
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
        // Configurar tabla
        tableModel = TableModelPrincipal.crearModeloPacientes();
        list.setModel(tableModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Configurar columnas
        list.getColumnModel().getColumn(0).setPreferredWidth(80);  // ID
        list.getColumnModel().getColumn(1).setPreferredWidth(200); // Nombre
        list.getColumnModel().getColumn(2).setPreferredWidth(120); // Fecha Nacimiento
        list.getColumnModel().getColumn(3).setPreferredWidth(100); // Teléfono

        // Configurar tabla para mejor visualización
        list.setRowHeight(25);
        list.getTableHeader().setReorderingAllowed(false);

        // Configurar spinner de fecha
        configurarSpinnerFecha();
    }

    private void configurarSpinnerFecha() {
        // Configurar spinner para fechas
        SpinnerDateModel dateModel = new SpinnerDateModel();
        fechaNacimientoFld.setModel(dateModel);

        // Configurar formato de fecha
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(fechaNacimientoFld, "dd/MM/yyyy");
        fechaNacimientoFld.setEditor(dateEditor);

        // Establecer fecha por defecto (hace 30 años)
        LocalDate fechaDefault = LocalDate.now().minusYears(30);
        Date fecha = Date.from(fechaDefault.atStartOfDay(ZoneId.systemDefault()).toInstant());
        fechaNacimientoFld.setValue(fecha);
    }

    private void configurarEventos() {
        // Botón Guardar
        guardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarPaciente();
            }
        });

        // Botón Limpiar
        limpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarCampos();
            }
        });

        // Botón Borrar
        borrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarPaciente();
            }
        });

        // Botón Buscar
        buscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarPacientes();
            }
        });

        // Botón Reporte
        reporte.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generarReporte();
            }
        });

        // Selección en tabla
        list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    seleccionarPaciente();
                }
            }
        });

        // Enter para buscar
        nombreBusquedaFld.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarPacientes();
            }
        });
    }

    private void guardarPaciente() {
        String id = idFld.getText().trim();
        String nombre = nombreFld.getText().trim();
        String telefono = telefonoFld.getText().trim();

        // Obtener fecha del spinner
        Date fechaDate = (Date) fechaNacimientoFld.getValue();
        LocalDate fechaNacimiento = fechaDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        // Validaciones básicas en la vista
        if (id.isEmpty() || nombre.isEmpty()) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "ID y nombre son obligatorios",
                    "Error de validación",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (telefono.isEmpty()) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "El teléfono es obligatorio",
                    "Error de validación",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar que la fecha no sea futura
        if (fechaNacimiento.isAfter(LocalDate.now())) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "La fecha de nacimiento no puede ser futura",
                    "Error de validación",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (modoEdicion) {
            // Nota: Implementar actualización cuando esté disponible en el controlador
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Función de actualización no implementada aún",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            if (controlador.agregarPaciente(id, nombre, fechaNacimiento, telefono)) {
                cargarDatos();
                limpiarCampos();
            }
        }
    }

    private void eliminarPaciente() {
        if (idSeleccionado != null) {
            int confirmacion = JOptionPane.showConfirmDialog(panelPrincipal,
                    "¿Está seguro de eliminar el paciente con ID: " + idSeleccionado + "?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION);

            if (confirmacion == JOptionPane.YES_OPTION) {
                // Nota: Implementar eliminación cuando esté disponible en el controlador
                JOptionPane.showMessageDialog(panelPrincipal,
                        "Función de eliminación no implementada aún",
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Seleccione un paciente para eliminar",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void buscarPacientes() {
        String criterio = nombreBusquedaFld.getText().trim();
        if (criterio.isEmpty()) {
            cargarDatos();
        } else {
            // Usar el método de búsqueda del modelo
            Lista<Paciente> pacientesEncontrados = controlador.getModelo().buscarPacientesPorNombre(criterio);
            Lista<Object> datosFiltrados = new Lista<>();

            for (int i = 0; i < pacientesEncontrados.getTam(); i++) {
                datosFiltrados.agregarFinal(pacientesEncontrados.obtenerPorPos(i));
            }

            tableModel.setDatos(datosFiltrados);
        }
    }

    private void seleccionarPaciente() {
        int filaSeleccionada = list.getSelectedRow();
        if (filaSeleccionada >= 0) {
            Object objeto = tableModel.getObjetoEnFila(filaSeleccionada);
            if (objeto instanceof Paciente) {
                Paciente paciente = (Paciente) objeto;

                idFld.setText(paciente.getId());
                nombreFld.setText(paciente.getNombre());
                telefonoFld.setText(paciente.getTelefono());

                // Configurar fecha en el spinner
                if (paciente.getFechaNacimiento() != null) {
                    Date fecha = Date.from(paciente.getFechaNacimiento()
                            .atStartOfDay(ZoneId.systemDefault())
                            .toInstant());
                    fechaNacimientoFld.setValue(fecha);
                }

                idSeleccionado = paciente.getId();
                modoEdicion = true;
                guardar.setText("Actualizar");

                // Habilitar botón borrar
                borrar.setEnabled(true);
            }
        }
    }

    private void limpiarCampos() {
        idFld.setText("");
        nombreFld.setText("");
        telefonoFld.setText("");
        nombreBusquedaFld.setText("");

        // Restablecer fecha por defecto
        LocalDate fechaDefault = LocalDate.now().minusYears(30);
        Date fecha = Date.from(fechaDefault.atStartOfDay(ZoneId.systemDefault()).toInstant());
        fechaNacimientoFld.setValue(fecha);

        idSeleccionado = null;
        modoEdicion = false;
        guardar.setText("Guardar");
        borrar.setEnabled(false);
        list.clearSelection();

        // Recargar todos los datos
        cargarDatos();
    }

    private void cargarDatos() {
        Lista<Paciente> pacientes = controlador.getModelo().obtenerPacientes();
        Lista<Object> datos = new Lista<>();

        for (int i = 0; i < pacientes.getTam(); i++) {
            datos.agregarFinal(pacientes.obtenerPorPos(i));
        }

        tableModel.setDatos(datos);
    }

    private void generarReporte() {
        try {
            String reporte = controlador.getModelo().getGestorCatalogos().generarReportePacientes();

            JTextArea textArea = new JTextArea(reporte);
            textArea.setEditable(false);
            textArea.setFont(new java.awt.Font("Courier New", java.awt.Font.PLAIN, 12));

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new java.awt.Dimension(600, 400));

            JOptionPane.showMessageDialog(panelPrincipal, scrollPane,
                    "Reporte de Pacientes", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Error al generar reporte: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public JPanel getPanel() {
        return panelPrincipal;
    }

    // Método para refrescar datos desde el exterior
    public void refrescarDatos() {
        cargarDatos();
        limpiarCampos();
    }
}
