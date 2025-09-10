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

        // Deshabilitar botón OK inicialmente
        ok.setEnabled(false);
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

        // Búsqueda al escribir
        atributoFld.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarPacientes();
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

        // Doble click en tabla
        table1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2 && pacienteSeleccionado != null) {
                    confirmarSeleccion();
                }
            }
        });
    }

    private void buscarPacientes() {
        String criterio = atributoFld.getText().trim();
        String atributoSeleccionado = (String) comboAtributosPaciente.getSelectedItem();

        try {
            if (criterio.isEmpty()) {
                cargarTodosPacientes();
            } else {
                Lista<Paciente> pacientes = controlador.getModelo().obtenerPacientes();
                Lista<Object> pacientesFiltrados = new Lista<>();

                for (int i = 0; i < pacientes.getTam(); i++) {
                    Paciente paciente = pacientes.obtenerPorPos(i);
                    boolean coincide = false;

                    switch (atributoSeleccionado) {
                        case "Nombre":
                            coincide = paciente.getNombre().toLowerCase().contains(criterio.toLowerCase());
                            break;
                        case "ID":
                            coincide = paciente.getId().toLowerCase().contains(criterio.toLowerCase());
                            break;
                        case "Teléfono":
                            coincide = paciente.getTelefono().toLowerCase().contains(criterio.toLowerCase());
                            break;
                    }

                    if (coincide) {
                        pacientesFiltrados.agregarFinal(paciente);
                    }
                }

                tableModel.setDatos(pacientesFiltrados);

                if (pacientesFiltrados.getTam() == 0) {
                    JOptionPane.showMessageDialog(this,
                            "No se encontraron pacientes con el criterio: " + criterio,
                            "Sin resultados",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al buscar pacientes: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarTodosPacientes() {
        try {
            Lista<Paciente> pacientes = controlador.getModelo().obtenerPacientes();
            Lista<Object> datos = new Lista<>();

            for (int i = 0; i < pacientes.getTam(); i++) {
                datos.agregarFinal(pacientes.obtenerPorPos(i));
            }

            tableModel.setDatos(datos);

            // Actualizar título
            setTitle("Buscar Paciente - " + pacientes.getTam() + " paciente(s) disponible(s)");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar pacientes: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void seleccionarPaciente() {
        int filaSeleccionada = table1.getSelectedRow();
        if (filaSeleccionada >= 0) {
            try {
                Object elemento = tableModel.getObjetoEnFila(filaSeleccionada);
                if (elemento instanceof Paciente) {
                    pacienteSeleccionado = (Paciente) elemento;
                    ok.setEnabled(true);
                }
            } catch (Exception e) {
                pacienteSeleccionado = null;
                ok.setEnabled(false);
            }
        } else {
            pacienteSeleccionado = null;
            ok.setEnabled(false);
        }
    }

    private void confirmarSeleccion() {
        if (pacienteSeleccionado == null) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un paciente de la lista",
                    "Paciente no seleccionado",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        confirmado = true;
        dispose();
    }

    private void cancelarSeleccion() {
        // Confirmar cancelación si hay cambios
        if (pacienteSeleccionado != null) {
            int confirmacion = JOptionPane.showConfirmDialog(
                    this,
                    "¿Está seguro de cancelar la selección del paciente?",
                    "Confirmar cancelación",
                    JOptionPane.YES_NO_OPTION);

            if (confirmacion != JOptionPane.YES_OPTION) {
                return;
            }
        }

        pacienteSeleccionado = null;
        confirmado = false;
        dispose();
    }

    // Método público para obtener el paciente seleccionado
    public Paciente getPacienteSeleccionado() {
        return confirmado ? pacienteSeleccionado : null;
    }

    // Método para verificar si se confirmó la operación
    public boolean isConfirmado() {
        return confirmado;
    }

    // Método para centrar en pantalla
    public void centrarEnPantalla() {
        setLocationRelativeTo(null);
    }

    // Método para mostrar ayuda
    public void mostrarAyuda() {
        String ayuda =
                "CÓMO BUSCAR UN PACIENTE:\n\n" +
                        "1. 🔍 Seleccione el criterio de búsqueda:\n" +
                        "   • Nombre: Busca por nombre del paciente\n" +
                        "   • ID: Busca por identificación\n" +
                        "   • Teléfono: Busca por número telefónico\n\n" +
                        "2. ✏️ Escriba el texto a buscar y presione Enter\n\n" +
                        "3. 👆 Haga click en un paciente para seleccionarlo\n\n" +
                        "4. ✅ Presione 'OK' para confirmar la selección\n" +
                        "   o haga doble click en el paciente\n\n" +
                        "5. ❌ Presione 'Cancelar' para cerrar sin seleccionar";

        JOptionPane.showMessageDialog(this, ayuda, "Ayuda - Buscar Paciente",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
