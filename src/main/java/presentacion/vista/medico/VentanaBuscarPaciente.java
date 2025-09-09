package presentacion.vista.medico;

import presentacion.controlador.ControladorPrincipal;
import presentacion.modelo.TableModelPrincipal;
import logica.entidades.*;
import logica.entidades.lista.Lista;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Ventana para buscar y seleccionar pacientes - Vista Modal
 */
public class VentanaBuscarPaciente extends JDialog {
    // Componentes del formulario (declarados en el .form)
    private JPanel panelPrincipal;
    private JComboBox comboAtributosPaciente;
    private JTextField atributoFld;
    private JTable table1;
    private JButton cancel;
    private JButton ok;

    // MVC Components
    private ControladorPrincipal controlador;
    private TableModelPrincipal tableModel;
    private Paciente pacienteSeleccionado = null;
    private boolean confirmado = false;

    public VentanaBuscarPaciente(ControladorPrincipal controlador) {
        super((JFrame) null, "Buscar Paciente", true);
        this.controlador = controlador;

        inicializarVentana();
        inicializarComponentes();
        configurarEventos();
        cargarTodosPacientes();
    }

    private void inicializarVentana() {
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600, 450);
        setLocationRelativeTo(null);
        setResizable(true);
    }

    private void inicializarComponentes() {
        // Configurar combo de atributos
        comboAtributosPaciente.addItem("Nombre");
        comboAtributosPaciente.addItem("ID");
        comboAtributosPaciente.addItem("Teléfono");
        comboAtributosPaciente.setSelectedIndex(0); // Por defecto: Nombre

        // Configurar tabla de pacientes
        tableModel = TableModelPrincipal.crearModeloPacientes();
        table1.setModel(tableModel);
        table1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Configurar columnas
        table1.getColumnModel().getColumn(0).setPreferredWidth(80);  // ID
        table1.getColumnModel().getColumn(1).setPreferredWidth(200); // Nombre
        table1.getColumnModel().getColumn(2).setPreferredWidth(120); // Fecha Nacimiento
        table1.getColumnModel().getColumn(3).setPreferredWidth(100); // Teléfono

        // Configurar tabla para mejor visualización
        table1.setRowHeight(25);
        table1.getTableHeader().setReorderingAllowed(false);

        // Estado inicial de botones
        ok.setEnabled(false);

        // Placeholder en campo de búsqueda
        atributoFld.setText("Escriba para buscar...");
        atributoFld.setForeground(java.awt.Color.GRAY);
    }

    private void configurarEventos() {
        // Botón OK
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmarSeleccion();
            }
        });

        // Botón Cancel
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelarSeleccion();
            }
        });

        // Selección en tabla
        table1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    seleccionarPaciente();
                }
            }
        });

        // Doble clic en tabla para confirmar
        table1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2 && pacienteSeleccionado != null) {
                    confirmarSeleccion();
                }
            }
        });

        // Cambio en combo de atributos
        comboAtributosPaciente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarPlaceholderBusqueda();
                realizarBusqueda();
            }
        });

        // Eventos del campo de búsqueda
        atributoFld.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (atributoFld.getText().equals("Escriba para buscar...")) {
                    atributoFld.setText("");
                    atributoFld.setForeground(java.awt.Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (atributoFld.getText().trim().isEmpty()) {
                    atributoFld.setText("Escriba para buscar...");
                    atributoFld.setForeground(java.awt.Color.GRAY);
                }
            }
        });

        // Búsqueda en tiempo real
        atributoFld.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { realizarBusqueda(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { realizarBusqueda(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { realizarBusqueda(); }
        });

        // Enter para confirmar selección
        atributoFld.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pacienteSeleccionado != null) {
                    confirmarSeleccion();
                }
            }
        });

        // ESC para cancelar
        getRootPane().registerKeyboardAction(
                e -> cancelarSeleccion(),
                KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        // Enter para OK cuando tabla tiene foco
        table1.getInputMap(JComponent.WHEN_FOCUSED).put(
                KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ENTER, 0), "confirmar");
        table1.getActionMap().put("confirmar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pacienteSeleccionado != null) {
                    confirmarSeleccion();
                }
            }
        });
    }

    private void actualizarPlaceholderBusqueda() {
        String atributoSeleccionado = (String) comboAtributosPaciente.getSelectedItem();
        if (atributoFld.getText().equals("Escriba para buscar...") ||
                atributoFld.getForeground().equals(java.awt.Color.GRAY)) {

            switch (atributoSeleccionado) {
                case "Nombre":
                    atributoFld.setText("Escriba el nombre...");
                    break;
                case "ID":
                    atributoFld.setText("Escriba el ID...");
                    break;
                case "Teléfono":
                    atributoFld.setText("Escriba el teléfono...");
                    break;
                default:
                    atributoFld.setText("Escriba para buscar...");
            }
            atributoFld.setForeground(java.awt.Color.GRAY);
        }
    }

    private void realizarBusqueda() {
        String criterio = atributoFld.getText().trim();

        // No buscar si es texto placeholder
        if (criterio.isEmpty() ||
                criterio.equals("Escriba para buscar...") ||
                criterio.equals("Escriba el nombre...") ||
                criterio.equals("Escriba el ID...") ||
                criterio.equals("Escriba el teléfono...") ||
                atributoFld.getForeground().equals(java.awt.Color.GRAY)) {
            cargarTodosPacientes();
            return;
        }

        String atributoSeleccionado = (String) comboAtributosPaciente.getSelectedItem();

        try {
            Lista<Paciente> pacientesEncontrados;

            if ("Nombre".equals(atributoSeleccionado)) {
                // Usar método específico de búsqueda por nombre
                pacientesEncontrados = controlador.getModelo().buscarPacientesPorNombre(criterio);
            } else {
                // Filtrar todos los pacientes por otros criterios
                Lista<Paciente> todosPacientes = controlador.getModelo().obtenerPacientes();
                pacientesEncontrados = new Lista<>();

                for (int i = 0; i < todosPacientes.getTam(); i++) {
                    Paciente paciente = todosPacientes.obtenerPorPos(i);
                    boolean coincide = false;

                    switch (atributoSeleccionado) {
                        case "ID":
                            coincide = paciente.getId().toLowerCase().contains(criterio.toLowerCase());
                            break;
                        case "Teléfono":
                            coincide = paciente.getTelefono().toLowerCase().contains(criterio.toLowerCase());
                            break;
                    }

                    if (coincide) {
                        pacientesEncontrados.agregarFinal(paciente);
                    }
                }
            }

            // Actualizar tabla
            Lista<Object> datos = new Lista<>();
            for (int i = 0; i < pacientesEncontrados.getTam(); i++) {
                datos.agregarFinal(pacientesEncontrados.obtenerPorPos(i));
            }

            tableModel.setDatos(datos);

            // Actualizar título de la ventana con número de resultados
            if (pacientesEncontrados.getTam() > 0) {
                setTitle("Buscar Paciente - " + pacientesEncontrados.getTam() + " encontrado(s)");
            } else {
                setTitle("Buscar Paciente - Sin resultados");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al buscar pacientes: " + e.getMessage(),
                    "Error de búsqueda",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void seleccionarPaciente() {
        int filaSeleccionada = table1.getSelectedRow();
        if (filaSeleccionada >= 0) {
            Object objeto = tableModel.getObjetoEnFila(filaSeleccionada);
            if (objeto instanceof Paciente) {
                pacienteSeleccionado = (Paciente) objeto;
                ok.setEnabled(true);

                // Mostrar información del paciente seleccionado
                mostrarInfoPacienteSeleccionado();
            }
        } else {
            pacienteSeleccionado = null;
            ok.setEnabled(false);
        }
    }

    private void mostrarInfoPacienteSeleccionado() {
        if (pacienteSeleccionado != null) {
            // Actualizar título con información del paciente
            setTitle("Buscar Paciente - Seleccionado: " + pacienteSeleccionado.getNombre());
        }
    }

    private void confirmarSeleccion() {
        if (pacienteSeleccionado != null) {
            confirmado = true;

            // Mostrar mensaje de confirmación
            JOptionPane.showMessageDialog(this,
                    "Paciente seleccionado:\n" +
                            pacienteSeleccionado.getNombre() + " (" + pacienteSeleccionado.getId() + ")\n" +
                            "Edad: " + pacienteSeleccionado.getEdadTexto(),
                    "Paciente confirmado",
                    JOptionPane.INFORMATION_MESSAGE);

            dispose();
        }
    }

    private void cancelarSeleccion() {
        pacienteSeleccionado = null;
        confirmado = false;
        dispose();
    }

    private void cargarTodosPacientes() {
        try {
            Lista<Paciente> todosPacientes = controlador.getModelo().obtenerPacientes();
            Lista<Object> datos = new Lista<>();

            for (int i = 0; i < todosPacientes.getTam(); i++) {
                datos.agregarFinal(todosPacientes.obtenerPorPos(i));
            }

            tableModel.setDatos(datos);

            // Actualizar título
            setTitle("Buscar Paciente - " + todosPacientes.getTam() + " paciente(s) registrado(s)");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar pacientes: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método público para obtener el paciente seleccionado
    public Paciente getPacienteSeleccionado() {
        return confirmado ? pacienteSeleccionado : null;
    }

    // Método para verificar si se confirmó la selección
    public boolean isConfirmado() {
        return confirmado;
    }

    // Método para centrar en pantalla
    public void centrarEnPantalla() {
        setLocationRelativeTo(null);
    }

    // Override para manejar cierre de ventana
    @Override
    public void dispose() {
        // Si no se confirmó, limpiar selección
        if (!confirmado) {
            pacienteSeleccionado = null;
        }
        super.dispose();
    }
}
