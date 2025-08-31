package presentacion.vista.principales;

import presentacion.controlador.ControladorPrincipal;
import presentacion.modelo.ModeloPrincipal;
import presentacion.vista.administrador.PanelGestionFarmaceutas;
import presentacion.vista.administrador.PanelGestionMedicos;
import presentacion.vista.administrador.PanelGestionMedicamentos;
import presentacion.vista.administrador.PanelGestionPacientes;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.UIManager;

public class VentanaPrincipal extends JFrame {
    private JTabbedPane tabbedPane;
    private JTextField txtId;
    private JTextField txtNombre;
    private JTextField txtEspecialidad;
    private JTextField txtBusquedaNombre;
    private JButton btnGuardar;
    private JButton btnLimpiar;
    private JButton btnBorrar;
    private JButton btnBuscar;
    private JButton btnReporte;
    private JTable tableMedicos;
    private DefaultTableModel tableModel;

    private ControladorPrincipal controllerPrincipal;
    private ModeloPrincipal modeloPrincipal;


    public VentanaPrincipal() {
        initializeComponents();
        setupLayout();
        setupTable();
        setupEventListeners();

        // TODO: Inicializar controlador
        // this.controller = new MedicosController(this, new MedicosModel());
    }

    private void initializeComponents() {
        setTitle("Ventana Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Crear JTabbedPane principal
        tabbedPane = new JTabbedPane();

        // Componentes del formulario
        txtId = new JTextField("MED-111", 15);
        txtNombre = new JTextField("David", 15);
        txtEspecialidad = new JTextField("Pediatria", 15);
        txtBusquedaNombre = new JTextField(15);

        // Botones
        btnGuardar = new JButton("Guardar");
        btnGuardar.setIcon(createColoredIcon(Color.BLUE));

        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setIcon(createColoredIcon(Color.RED));

        btnBorrar = new JButton("Borrar");
        btnBorrar.setIcon(createColoredIcon(Color.RED));

        btnBuscar = new JButton("Buscar");
        btnBuscar.setIcon(createColoredIcon(Color.ORANGE));

        btnReporte = new JButton("Reporte");
        btnReporte.setIcon(createColoredIcon(Color.RED));
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Panel principal para la pestaña Médicos
        JPanel medicosPanel = new PanelGestionMedicos();

        // Panel Farmaceutas
        JPanel farmaceutasPanel = new PanelGestionFarmaceutas();

        // Panel Pacientes
        JPanel pacientesPanel = new PanelGestionPacientes();

        // Panel Medicamentos
        JPanel medicamentosPanel = new PanelGestionMedicamentos();

        // Panel Dashboard y otros siguen igual
        JPanel dashboardPanel = new JPanel();
        JPanel historicoPanel = new JPanel();
        JPanel acercaPanel = new JPanel();

        // Agregar pestañas con los paneles de gestión
        tabbedPane.addTab("Médicos", createTabIcon(Color.RED), medicosPanel);
        tabbedPane.addTab("Farmaceutas", createTabIcon(Color.ORANGE), farmaceutasPanel);
        tabbedPane.addTab("Pacientes", createTabIcon(Color.BLUE), pacientesPanel);
        tabbedPane.addTab("Medicamentos", createTabIcon(Color.GREEN), medicamentosPanel);
        tabbedPane.addTab("Dashboard", createTabIcon(Color.MAGENTA), dashboardPanel);
        tabbedPane.addTab("Histórico", createTabIcon(Color.CYAN), historicoPanel);
        tabbedPane.addTab("Acerca de...", createTabIcon(Color.GRAY), acercaPanel);

        add(tabbedPane, BorderLayout.CENTER);
    }

    private void setupTable() {
        // Configurar modelo de tabla
        String[] columnNames = {"Id", "Nombre", "Especialidad"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer tabla de solo lectura
            }
        };

        tableMedicos = new JTable(tableModel);
        tableMedicos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Agregar datos de ejemplo

        tableModel.addRow(new Object[]{"MED-111", "David", "Pediatría"});
        tableModel.addRow(new Object[]{"MED-222", "Miguel", "Neurocirugía"});

        // Seleccionar primera fila por defecto
        if (tableModel.getRowCount() > 0) {
            tableMedicos.setRowSelectionInterval(0, 0);
        }

        JScrollPane scrollPane = new JScrollPane(tableMedicos);
        scrollPane.setPreferredSize(new Dimension(0, 200));

        // Agregar la tabla al panel correspondiente
        Component medicosPanel = tabbedPane.getComponentAt(0);
        if (medicosPanel instanceof JPanel) {
            JPanel panel = (JPanel) medicosPanel;
            Component[] components = panel.getComponents();
            for (Component comp : components) {
                if (comp instanceof JPanel) {
                    JPanel subPanel = (JPanel) comp;
                    if (subPanel.getBorder() instanceof TitledBorder) {
                        TitledBorder border = (TitledBorder) subPanel.getBorder();
                        if ("Listado".equals(border.getTitle())) {
                            subPanel.add(scrollPane, BorderLayout.CENTER);
                            break;
                        }
                    }
                }
            }
        }
    }

    private void setupEventListeners() {
        // TODO: Implementar listeners que deleguen al controlador
        // Ejemplo de cómo debería ser la implementación siguiendo MVC:

        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: Delegar al controlador
                // controller.guardarMedico();
                guardarMedico(); // Implementación temporal
            }
        });

        btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: Delegar al controlador
                // controller.limpiarFormulario();
                limpiarFormulario(); // Implementación temporal
            }
        });

        btnBorrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: Delegar al controlador
                // controller.borrarMedico();
                borrarMedico(); // Implementación temporal
            }
        });

        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: Delegar al controlador
                // controller.buscarMedico(txtBusquedaNombre.getText());
                buscarMedico(); // Implementación temporal
            }
        });

        btnReporte.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: Delegar al controlador
                // controller.generarReporte();
                generarReporte(); // Implementación temporal
            }
        });

        // Listener para selección en tabla
        tableMedicos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                // TODO: Delegar al controlador
                // controller.seleccionarMedico(tableMedicos.getSelectedRow());
                cargarDatosSeleccionados(); // Implementación temporal
            }
        });
    }

    // TODO: Los siguientes métodos son implementaciones temporales
    // En el patrón MVC, la vista solo debería tener métodos para:
    // - Obtener datos de los componentes (getters)
    // - Establecer datos en los componentes (setters)
    // - Mostrar mensajes al usuario
    // La lógica de negocio debería estar en el controlador

    private void guardarMedico() {
        // TODO: Este método debería ser reemplazado por controller.guardarMedico()
        String id = txtId.getText();
        String nombre = txtNombre.getText();
        String especialidad = txtEspecialidad.getText();

        if (nombre.trim().isEmpty() || especialidad.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Verificar si es edición o nuevo registro
        int selectedRow = tableMedicos.getSelectedRow();
        if (selectedRow >= 0) {
            // Editar registro existente
            tableModel.setValueAt(id, selectedRow, 0);
            tableModel.setValueAt(nombre, selectedRow, 1);
            tableModel.setValueAt(especialidad, selectedRow, 2);
        } else {
            // Agregar nuevo registro
            tableModel.addRow(new Object[]{id, nombre, especialidad});
        }

        JOptionPane.showMessageDialog(this, "Médico guardado exitosamente",
                "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private void limpiarFormulario() {
        // TODO: Este método debería ser reemplazado por controller.limpiarFormulario()
        txtId.setText("");
        txtNombre.setText("");
        txtEspecialidad.setText("");
        tableMedicos.clearSelection();
    }

    private void borrarMedico() {
        // TODO: Este método debería ser reemplazado por controller.borrarMedico()
        int selectedRow = tableMedicos.getSelectedRow();
        if (selectedRow >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de eliminar el médico seleccionado?",
                    "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                tableModel.removeRow(selectedRow);
                limpiarFormulario();
                JOptionPane.showMessageDialog(this, "Médico eliminado exitosamente",
                        "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un médico para eliminar",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void buscarMedico() {
        // TODO: Este método debería ser reemplazado por controller.buscarMedico()
        String busqueda = txtBusquedaNombre.getText().toLowerCase();
        if (busqueda.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un criterio de búsqueda",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Búsqueda simple en la tabla
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String nombre = tableModel.getValueAt(i, 1).toString().toLowerCase();
            String id = tableModel.getValueAt(i, 0).toString().toLowerCase();

            if (nombre.contains(busqueda) || id.contains(busqueda)) {
                tableMedicos.setRowSelectionInterval(i, i);
                tableMedicos.scrollRectToVisible(tableMedicos.getCellRect(i, 0, true));
                cargarDatosSeleccionados();
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "No se encontraron médicos con ese criterio",
                "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private void generarReporte() {
        // TODO: Este método debería ser reemplazado por controller.generarReporte()
        JOptionPane.showMessageDialog(this, "Funcionalidad de reporte en desarrollo",
                "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private void cargarDatosSeleccionados() {
        // TODO: Este método debería ser reemplazado por controller.cargarDatosSeleccionados()
        int selectedRow = tableMedicos.getSelectedRow();
        if (selectedRow >= 0) {
            txtId.setText(tableModel.getValueAt(selectedRow, 0).toString());
            txtNombre.setText(tableModel.getValueAt(selectedRow, 1).toString());
            txtEspecialidad.setText(tableModel.getValueAt(selectedRow, 2).toString());
        }
    }

    // Métodos auxiliares para crear iconos
    private Icon createColoredIcon(Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                g.setColor(color);
                g.fillRect(x, y, getIconWidth(), getIconHeight());
            }

            @Override
            public int getIconWidth() {
                return 16;
            }

            @Override
            public int getIconHeight() {
                return 16;
            }
        };
    }

    private Icon createTabIcon(Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                g.setColor(color);
                g.fillOval(x + 2, y + 2, getIconWidth() - 4, getIconHeight() - 4);
            }

            @Override
            public int getIconWidth() {
                return 16;
            }

            @Override
            public int getIconHeight() {
                return 16;
            }
        };
    }

    // TODO: Métodos que debería tener la Vista en el patrón MVC
    // Estos métodos permiten al controlador interactuar con la vista

    /**
     * Obtiene los datos del médico desde el formulario
     * @return Objeto con los datos del médico (debería ser MedicoDTO)
     */
    /*
    public MedicoDTO obtenerDatosMedico() {
        return new MedicoDTO(txtId.getText(), txtNombre.getText(), txtEspecialidad.getText());
    }
    */

    /**
     * Establece los datos del médico en el formulario
     * @param medico Objeto con los datos del médico
     */
    /*
    public void establecerDatosMedico(MedicoDTO medico) {
        txtId.setText(medico.getId());
        txtNombre.setText(medico.getNombre());
        txtEspecialidad.setText(medico.getEspecialidad());
    }
    */

    /**
     * Actualiza la tabla con la lista de médicos
     * @param medicos Lista de médicos
     */
    /*
    public void actualizarTablaMedicos(List<MedicoDTO> medicos) {
        tableModel.setRowCount(0);
        for (MedicoDTO medico : medicos) {
            tableModel.addRow(new Object[]{
                medico.getId(),
                medico.getNombre(),
                medico.getEspecialidad()
            });
        }
    }
    */

    /**
     * Muestra un mensaje de error al usuario
     *
     * @param mensaje Mensaje de error
     */
    public void mostrarMensajeError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Muestra un mensaje de información al usuario
     *
     * @param mensaje Mensaje de información
     */
    public void mostrarMensajeInformacion(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Obtiene el índice de la fila seleccionada en la tabla
     *
     * @return Índice de la fila seleccionada
     */
    public int obtenerFilaSeleccionada() {
        return tableMedicos.getSelectedRow();
    }
}

/*
TODO: IMPLEMENTACIÓN COMPLETA DEL PATRÓN MVC

1. MODELO (MedicosModel.java):
=================================
public class MedicosModel {
    private List<MedicoDTO> medicos;
    private MedicoDAO medicoDAO;

    public MedicosModel() {
        this.medicoDAO = new MedicoDAO();
        this.medicos = new ArrayList<>();
    }

    public void guardarMedico(MedicoDTO medico) {
        // Validaciones de negocio
        // Guardar en XML usando DAO
        medicoDAO.guardar(medico);
    }

    public List<MedicoDTO> obtenerTodosMedicos() {
        return medicoDAO.obtenerTodos();
    }

    public void eliminarMedico(String id) {
        medicoDAO.eliminar(id);
    }

    public List<MedicoDTO> buscarMedicos(String criterio) {
        return medicoDAO.buscar(criterio);
    }
}

2. CONTROLADOR (MedicosController.java):
========================================
public class MedicosController {
    private MedicosView vista;
    private MedicosModel modelo;

    public MedicosController(MedicosView vista, MedicosModel modelo) {
        this.vista = vista;
        this.modelo = modelo;
        inicializarVista();
    }

    private void inicializarVista() {
        cargarTodosMedicos();
    }

    public void guardarMedico() {
        try {
            MedicoDTO medico = vista.obtenerDatosMedico();
            modelo.guardarMedico(medico);
            vista.mostrarMensajeInformacion("Médico guardado exitosamente");
            cargarTodosMedicos();
        } catch (Exception e) {
            vista.mostrarMensajeError("Error al guardar: " + e.getMessage());
        }
    }

    public void eliminarMedico() {
        int fila = vista.obtenerFilaSeleccionada();
        if (fila >= 0) {
            // Obtener ID del médico seleccionado
            // Confirmar eliminación
            // modelo.eliminarMedico(id);
            // vista.mostrarMensajeInformacion("Médico eliminado");
            // cargarTodosMedicos();
        }
    }

    public void buscarMedico(String criterio) {
        try {
            List<MedicoDTO> resultados = modelo.buscarMedicos(criterio);
            vista.actualizarTablaMedicos(resultados);
        } catch (Exception e) {
            vista.mostrarMensajeError("Error en búsqueda: " + e.getMessage());
        }
    }

    private void cargarTodosMedicos() {
        try {
            List<MedicoDTO> medicos = modelo.obtenerTodosMedicos();
            vista.actualizarTablaMedicos(medicos);
        } catch (Exception e) {
            vista.mostrarMensajeError("Error al cargar médicos: " + e.getMessage());
        }
    }
}

3. DTO (MedicoDTO.java):
========================
public class MedicoDTO {
    private String id;
    private String nombre;
    private String especialidad;
    private String clave;

    // Constructores, getters y setters
}

4. DAO (MedicoDAO.java):
========================
public class MedicoDAO {
    private static final String XML_FILE = "medicos.xml";

    public void guardar(MedicoDTO medico) {
        // Implementar guardado en XML usando DOM o JAXB
    }

    public List<MedicoDTO> obtenerTodos() {
        // Implementar lectura desde XML
        return new ArrayList<>();
    }

    public void eliminar(String id) {
        // Implementar eliminación en XML
    }

    public List<MedicoDTO> buscar(String criterio) {
        // Implementar búsqueda en XML
        return new ArrayList<>();
    }
}

5. INTEGRACIÓN:
===============
En el constructor de MedicosView:
    this.controller = new MedicosController(this, new MedicosModel());

Y reemplazar todas las implementaciones temporales con llamadas al controlador.
*/
