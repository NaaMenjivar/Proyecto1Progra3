package presentacion.vista.administrador;


import logica.entidades.*;
import logica.entidades.lista.CatalogoMedicamentos;
import logica.entidades.lista.Lista;
import logica.entidades.lista.ListaPacientes;
import logica.excepciones.CatalogoException;
import presentacion.controlador.ControladorPrincipal;
import presentacion.modelo.TableModelPrincipal;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class PanelPrescribir {
    private JPanel panelControl;
    private JPanel panelRecetaMedica;
    private JPanel panelAjustarPrescripcion;
    private JSpinner fechaRetiro;
    private JButton button1;
    private JButton buscarPacienteButton;
    private JButton agregarMedicamentoButton;
    private JTable tablaRecetas;
    private JButton guardarButton;
    private JButton limpiarButton;
    private JButton descartarMedicamentosButton;
    private JButton detallesButton;
    private JPanel panelPrincipal;
    private JLabel pacienteActualText;

    private DefaultTableModel modeloRecetas;

    private ControladorPrincipal controlador;

    private Paciente actual;
    private Medicamento medicamentoSeleccionado;

    public PanelPrescribir(ControladorPrincipal controlador){
        actual = null;
        medicamentoSeleccionado = null;

        this.controlador = controlador;
        inicializarComponentes();
        configurarEventos();
        cargarDatos();

        buscarPacienteButton.addActionListener(e -> mostrarPopupBusquedaPacientes());
        agregarMedicamentoButton.addActionListener(e -> mostrarPopupBusquedaMedicamentos());
    }

    public void inicializarComponentes(){
        modeloRecetas = new DefaultTableModel(
                new String[]{"Medicamento", "Presentación", "Cantidad", "Indicaciones", "Duración (días)"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaRecetas.setModel(modeloRecetas);
        tablaRecetas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tablaRecetas.getColumnModel().getColumn(0).setPreferredWidth(150); // Medicamento
        tablaRecetas.getColumnModel().getColumn(1).setPreferredWidth(120); // Presentación
        tablaRecetas.getColumnModel().getColumn(2).setPreferredWidth(70);  // Cantidad
        tablaRecetas.getColumnModel().getColumn(3).setPreferredWidth(200); // Indicaciones
        tablaRecetas.getColumnModel().getColumn(4).setPreferredWidth(90);  // Duración

        tablaRecetas.setRowHeight(25);
        tablaRecetas.getTableHeader().setReorderingAllowed(false);

        Date fechaHoy = new Date();
        SpinnerDateModel modeloFecha = new SpinnerDateModel(fechaHoy, null, null, Calendar.DAY_OF_MONTH);
        fechaRetiro.setModel(modeloFecha);

        JSpinner.DateEditor editor = new JSpinner.DateEditor(fechaRetiro, "dd/MM/yyyy");
        fechaRetiro.setEditor(editor);

        if (pacienteActualText != null) {
            pacienteActualText.setText("Paciente actual: Ninguno");
        }
    }

    private void configurarEventos() {

    }

    public void cargarDatos(){
        modeloRecetas.setRowCount(0);
    }

    private void mostrarPopupBusquedaPacientes() {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(panelPrincipal), "Pacientes", true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(panelPrincipal);
        dialog.setLayout(new BorderLayout());

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JLabel labelFiltrar = new JLabel("Filtrar por: ");
        String[] opciones = {"Nombre", "ID"};
        JComboBox<String> comboBox = new JComboBox<>(opciones);
        JTextField textField = new JTextField(15);

        panelBusqueda.add(labelFiltrar);
        panelBusqueda.add(comboBox);
        panelBusqueda.add(textField);

        dialog.add(panelBusqueda, BorderLayout.NORTH);

        String[] columnas = {"ID", "Nombre", "Edad"};
        DefaultTableModel tableModelPacientes = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable tablaPacientes = new JTable(tableModelPacientes);
        tablaPacientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaPacientes.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(tablaPacientes);
        dialog.add(scrollPane, BorderLayout.CENTER);

        cargarTablaPacientes(tableModelPacientes, controlador.getListaPacientes());

        textField.addActionListener(evt -> {
            String criterio = (String) comboBox.getSelectedItem();
            String valor = textField.getText().trim();
            ListaPacientes listaFiltrada = new ListaPacientes();

            ListaPacientes listaPacientes = controlador.getListaPacientes();
            for (Paciente paciente : listaPacientes) {
                if ("Nombre".equals(criterio) && paciente.getNombre().equalsIgnoreCase(valor)) {
                    listaFiltrada.agregarPaciente(paciente);
                } else if ("ID".equals(criterio) && paciente.getId().equalsIgnoreCase(valor)) {
                    listaFiltrada.agregarPaciente(paciente);
                }
            }
            cargarTablaPacientes(tableModelPacientes, listaFiltrada);
        });

        tablaPacientes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int filaSeleccionada = tablaPacientes.getSelectedRow();
                    if (filaSeleccionada != -1) {
                        String idSeleccionado = tablaPacientes.getValueAt(filaSeleccionada, 0).toString();

                        ListaPacientes listaPacientes = controlador.getListaPacientes();
                        for (Paciente p : listaPacientes) {
                            if (p.getId().equalsIgnoreCase(idSeleccionado)) {
                                actual = p;
                                break;
                            }
                        }

                        if (actual != null) {
                            pacienteActualText.setText("Paciente actual: " + actual.getNombre());
                            cargarRecetasDePacienteActual();
                        } else {
                            pacienteActualText.setText("Paciente actual: Ninguno");
                            modeloRecetas.setRowCount(0);
                        }

                        dialog.dispose();
                    }
                }
            }
        });

        dialog.setVisible(true);
    }

    private void cargarTablaPacientes(DefaultTableModel model, ListaPacientes pacientes) {
        model.setRowCount(0);
        for (Paciente paciente : pacientes) {
            model.addRow(new Object[]{paciente.getId(), paciente.getNombre(), paciente.getEdad()});
        }
    }

    private void mostrarPopupBusquedaMedicamentos() {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(panelPrincipal), "Medicamentos", true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(panelPrincipal);
        dialog.setLayout(new BorderLayout());

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JLabel labelFiltrar = new JLabel("Filtrar por: ");
        String[] opciones = {"Nombre", "ID"};
        JComboBox<String> comboBox = new JComboBox<>(opciones);
        JTextField textField = new JTextField(15);

        panelBusqueda.add(labelFiltrar);
        panelBusqueda.add(comboBox);
        panelBusqueda.add(textField);

        dialog.add(panelBusqueda, BorderLayout.NORTH);

        String[] columnas = {"ID", "Nombre", "Stock"};
        DefaultTableModel tableModelMedicamentos = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        JTable tablaMedicamentos = new JTable(tableModelMedicamentos);
        tablaMedicamentos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaMedicamentos.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(tablaMedicamentos);
        dialog.add(scrollPane, BorderLayout.CENTER);

        cargarTablaMedicamentos(tableModelMedicamentos, controlador.getMedicamentos());

        textField.addActionListener(evt -> {
            String criterio = (String) comboBox.getSelectedItem();
            String valor = textField.getText().trim();
            CatalogoMedicamentos listaFiltrada = new CatalogoMedicamentos();

            CatalogoMedicamentos listaMedicamentos = controlador.getMedicamentos();
            for (Medicamento med : listaMedicamentos) {
                if ("Nombre".equals(criterio) && med.getNombre().equalsIgnoreCase(valor)) {
                    listaFiltrada.agregarMedicamento(med);
                } else if ("ID".equals(criterio) && med.getCodigo().equalsIgnoreCase(valor)) {
                    listaFiltrada.agregarMedicamento(med);
                }
            }
            cargarTablaMedicamentos(tableModelMedicamentos, listaFiltrada);
        });

        tablaMedicamentos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int filaSeleccionada = tablaMedicamentos.getSelectedRow();
                    if (filaSeleccionada != -1) {
                        String idSeleccionado = tablaMedicamentos.getValueAt(filaSeleccionada, 0).toString();

                        CatalogoMedicamentos listaMedicamentos = controlador.getMedicamentos();
                        for (Medicamento m : listaMedicamentos) {
                            if (m.getCodigo().equalsIgnoreCase(idSeleccionado)) {
                                medicamentoSeleccionado = m;
                                break;
                            }
                        }

                        if (medicamentoSeleccionado != null) {
                            mostrarPopupDetallesMedicamento(medicamentoSeleccionado, dialog);
                        }
                    }
                }
            }
        });

        dialog.setVisible(true);
    }

    private void cargarTablaMedicamentos(DefaultTableModel model, CatalogoMedicamentos medicamentos) {
        model.setRowCount(0);
        for (Medicamento med : medicamentos) {
            model.addRow(new Object[]{med.getCodigo(), med.getNombre(), med.getStock()});
        }
    }

    private void mostrarPopupDetallesMedicamento(Medicamento medicamento, JDialog dialogPadre) {
        JDialog dialogDetalles = new JDialog((JFrame) SwingUtilities.getWindowAncestor(panelPrincipal), medicamento.getNombre(), true);
        dialogDetalles.setSize(400, 250);
        dialogDetalles.setLocationRelativeTo(panelPrincipal);
        dialogDetalles.setLayout(new GridLayout(4, 2, 10, 10));
        JLabel lblCantidad = new JLabel("Cantidad:");
        JTextField txtCantidad = new JTextField();

        JLabel lblDuracion = new JLabel("Duración (Días):");
        JTextField txtDuracion = new JTextField();

        JLabel lblIndicaciones = new JLabel("Indicaciones:");
        JTextField txtIndicaciones = new JTextField();

        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        dialogDetalles.add(lblCantidad);
        dialogDetalles.add(txtCantidad);

        dialogDetalles.add(lblDuracion);
        dialogDetalles.add(txtDuracion);

        dialogDetalles.add(lblIndicaciones);
        dialogDetalles.add(txtIndicaciones);

        dialogDetalles.add(btnGuardar);
        dialogDetalles.add(btnCancelar);

        btnGuardar.addActionListener(e -> {
            try {
                int cantidad = Integer.parseInt(txtCantidad.getText().trim());
                int duracion = Integer.parseInt(txtDuracion.getText().trim());
                String indicaciones = txtIndicaciones.getText().trim();

                if (cantidad <= 0 || duracion <= 0) {
                    JOptionPane.showMessageDialog(dialogDetalles, "Cantidad y duración deben ser mayores a 0.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Validar paciente seleccionado
                if (actual == null) {
                    JOptionPane.showMessageDialog(dialogDetalles, "Seleccione un paciente antes de agregar medicamentos.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Crear DetalleReceta
                DetalleReceta detalle = new DetalleReceta(medicamento.getCodigo(), cantidad, indicaciones, duracion);

                // Crear Receta
                String numeroReceta = generarNumeroReceta();
                String idPaciente = actual.getId();
                String idMedico = controlador.getModelo().getUsuarioActual().getId();

                Date fechaSeleccionada = (Date) fechaRetiro.getValue();
                LocalDate fechaRetiroLD = fechaSeleccionada.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                Receta nuevaReceta = new Receta(numeroReceta, idPaciente, idMedico, fechaRetiroLD);
                nuevaReceta.setEstado(EstadoReceta.PROCESO);
                nuevaReceta.getDetalles().agregarFinal(detalle);

                controlador.getModelo().agregarReceta(nuevaReceta);

                cargarRecetasDePacienteActual();

                dialogDetalles.dispose();
                dialogPadre.dispose();

                JOptionPane.showMessageDialog(panelPrincipal,
                        "Receta creada exitosamente para: " + actual.getNombre(),
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialogDetalles, "Cantidad y duración deben ser números válidos.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (CatalogoException ex) {
                throw new RuntimeException(ex);
            }
        });

        btnCancelar.addActionListener(e -> dialogDetalles.dispose());
        dialogDetalles.setVisible(true);
    }

    private void cargarRecetasDePacienteActual() {
        modeloRecetas.setRowCount(0);
        if (actual == null) return;

        Lista<Receta> todas = controlador.getModelo().obtenerTodasLasRecetas();
        for (Receta r : todas) {
            if (r.getIdPaciente() != null && r.getIdPaciente().equalsIgnoreCase(actual.getId())) {
                for (DetalleReceta d : r.getDetalles()) {
                    Medicamento med = buscarMedicamentoPorCodigo(d.getCodigoMedicamento());
                    String nombre = med != null ? med.getNombre() : d.getCodigoMedicamento();
                    String presentacion = med != null ? med.getPresentacion() : "";
                    modeloRecetas.addRow(new Object[]{
                            nombre,
                            presentacion,
                            d.getCantidad(),
                            d.getIndicaciones(),
                            d.getDuracionDias()
                    });
                }
            }
        }
    }

    private Medicamento buscarMedicamentoPorCodigo(String codigo) {
        CatalogoMedicamentos catalogo = controlador.getMedicamentos();
        for (Medicamento m : catalogo) {
            if (m.getCodigo().equalsIgnoreCase(codigo)) return m;
        }
        return null;
    }

    private String generarNumeroReceta() {
        return "REC-" + System.currentTimeMillis();
    }

    // Métodos auxiliares
    public JPanel getPanelPrincipal(){ return panelPrincipal; }
    public Paciente getPacienteActual(){ return actual; }
}