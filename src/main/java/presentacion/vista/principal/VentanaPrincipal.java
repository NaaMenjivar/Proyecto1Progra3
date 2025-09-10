package presentacion.vista.principal;

import presentacion.controlador.ControladorPrincipal;
import presentacion.vista.administrador.*;
import logica.entidades.Usuario;
import logica.entidades.TipoUsuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Ventana Principal del Sistema - Para Administradores y Farmaceutas
 * Utiliza la estructura de .form existente con JTabbedPane
 */
public class VentanaPrincipal extends JFrame {

    // Componentes del .form
    private JPanel panelPrincipal;
    private JTabbedPane panelTabs;
    private JPanel medicosPanel;
    private JPanel farmaceutasPanel;
    private JPanel pacientesPanel;
    private JPanel medicamentosPanel;
    private JPanel despachoPanel;
    // private JPanel dashboardPanel;  // Dashboard comentado
    private JPanel historicoPanel;
    private JPanel acercaDePanel;

    // Instancias de los paneles de gestión
    private PanelGestionMedicos panelGestionMedicos;
    private PanelGestionFarmaceutas panelGestionFarmaceutas;
    private PanelGestionPacientes panelGestionPacientes;
    private PanelGestionMedicamentos panelGestionMedicamentos;
    private PanelDespacho panelDespacho;
    // private PanelDashboard panelDashboard;  // Dashboard comentado
    private PanelHistorico panelHistorico;
    private PanelAcercaDe panelAcercaDe;

    // Controlador MVC
    private ControladorPrincipal controlador;

    public VentanaPrincipal(ControladorPrincipal controlador) {
        this.controlador = controlador;
        inicializarVentana();
        inicializarPaneles();
        configurarEventos();
    }

    private void inicializarVentana() {
        setTitle("Sistema Hospitalario - Gestión de Recetas");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1024, 768);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);

        // Usar el panelPrincipal del .form
        setContentPane(panelPrincipal);

        // Actualizar título con información del usuario
        actualizarTituloVentana();

        // Crear menú
        crearMenuBar();
    }

    private void crearMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Menú Sistema
        JMenu menuSistema = new JMenu("Sistema");

        JMenuItem itemCambiarClave = new JMenuItem("Cambiar Contraseña");
        itemCambiarClave.addActionListener(e -> mostrarCambiarContrasena());

        JMenuItem itemCerrarSesion = new JMenuItem("Cerrar Sesión");
        itemCerrarSesion.addActionListener(e -> cerrarSesion());

        JMenuItem itemSalir = new JMenuItem("Salir");
        itemSalir.addActionListener(e -> salirAplicacion());

        menuSistema.add(itemCambiarClave);
        menuSistema.addSeparator();
        menuSistema.add(itemCerrarSesion);
        menuSistema.add(itemSalir);

        // Menú Ayuda
        JMenu menuAyuda = new JMenu("Ayuda");
        JMenuItem itemAcercaDe = new JMenuItem("Acerca de");
        itemAcercaDe.addActionListener(e -> panelTabs.setSelectedComponent(acercaDePanel));
        menuAyuda.add(itemAcercaDe);

        menuBar.add(menuSistema);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(menuAyuda);

        setJMenuBar(menuBar);
    }

    private void actualizarTituloVentana() {
        try {
            Usuario usuario = controlador.getModelo().getUsuarioActual();
            if (usuario != null) {
                String tipoUsuario = usuario.getTipo() == TipoUsuario.ADMINISTRADOR ? "Administrador" : "Farmaceuta";
                setTitle("Sistema Hospitalario - " + tipoUsuario + ": " + usuario.getNombre());
            }
        } catch (Exception e) {
            setTitle("Sistema Hospitalario - Gestión de Recetas");
        }
    }

    private void inicializarPaneles() {
        try {
            Usuario usuario = controlador.getModelo().getUsuarioActual();

            if (usuario.getTipo() == TipoUsuario.ADMINISTRADOR) {
                inicializarPanelesAdministrador();
            } else if (usuario.getTipo() == TipoUsuario.FARMACEUTA) {
                inicializarPanelesFarmaceuta();
            }

            inicializarPanelesComunes();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al inicializar paneles: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void inicializarPanelesAdministrador() {
        // Panel Gestión Médicos
        panelGestionMedicos = new PanelGestionMedicos(controlador);
        medicosPanel.setLayout(new BorderLayout());
        medicosPanel.add(panelGestionMedicos.getPanel(), BorderLayout.CENTER);

        // Panel Gestión Farmaceutas
        panelGestionFarmaceutas = new PanelGestionFarmaceutas(controlador);
        farmaceutasPanel.setLayout(new BorderLayout());
        farmaceutasPanel.add(panelGestionFarmaceutas.getPanel(), BorderLayout.CENTER);

        // Panel Gestión Pacientes
        panelGestionPacientes = new PanelGestionPacientes(controlador);
        pacientesPanel.setLayout(new BorderLayout());
        pacientesPanel.add(panelGestionPacientes.getPanel(), BorderLayout.CENTER);

        // Panel Gestión Medicamentos
        panelGestionMedicamentos = new PanelGestionMedicamentos(controlador);
        medicamentosPanel.setLayout(new BorderLayout());
        medicamentosPanel.add(panelGestionMedicamentos.getPanel(), BorderLayout.CENTER);
    }

    private void inicializarPanelesFarmaceuta() {
        // Para farmaceuta, quitar pestañas de gestión
        panelTabs.remove(medicosPanel);
        panelTabs.remove(farmaceutasPanel);
        panelTabs.remove(pacientesPanel);
        panelTabs.remove(medicamentosPanel);

        // Panel Despacho
        panelDespacho = new PanelDespacho(controlador);
        despachoPanel.setLayout(new BorderLayout());
        despachoPanel.add(panelDespacho.getPanel(), BorderLayout.CENTER);
    }

    private void inicializarPanelesComunes() {
        // Panel Dashboard - COMENTADO
        /*panelDashboard = new PanelDashboard(controlador);
        dashboardPanel.setLayout(new BorderLayout());
        dashboardPanel.add(panelDashboard.getPanel(), BorderLayout.CENTER);*/

        // Panel Histórico
        panelHistorico = new PanelHistorico(controlador);
        historicoPanel.setLayout(new BorderLayout());
        historicoPanel.add(panelHistorico.getPanel(), BorderLayout.CENTER);

        // Panel Acerca De
        panelAcercaDe = new PanelAcercaDe();
        acercaDePanel.setLayout(new BorderLayout());
        acercaDePanel.add(panelAcercaDe.getPanel(), BorderLayout.CENTER);
    }

    private void configurarEventos() {
        // Evento de cierre de ventana
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                salirAplicacion();
            }
        });

        // Evento de cambio de pestaña
        panelTabs.addChangeListener(e -> actualizarPanelActivo());
    }

    private void actualizarPanelActivo() {
        int indiceSeleccionado = panelTabs.getSelectedIndex();
        if (indiceSeleccionado >= 0) {
            Component panelActivo = panelTabs.getComponentAt(indiceSeleccionado);

            if (panelActivo == medicosPanel && panelGestionMedicos != null) {
                panelGestionMedicos.refrescarDatos();
            } else if (panelActivo == farmaceutasPanel && panelGestionFarmaceutas != null) {
                panelGestionFarmaceutas.refrescarDatos();
            } else if (panelActivo == pacientesPanel && panelGestionPacientes != null) {
                panelGestionPacientes.refrescarDatos();
            } else if (panelActivo == medicamentosPanel && panelGestionMedicamentos != null) {
                panelGestionMedicamentos.refrescarDatos();
            } else if (panelActivo == despachoPanel && panelDespacho != null) {
                panelDespacho.refrescarDatos();
                // Dashboard comentado
            /*} else if (panelActivo == dashboardPanel && panelDashboard != null) {
                panelDashboard.actualizarDatos();*/
            } else if (panelActivo == historicoPanel && panelHistorico != null) {
                panelHistorico.refrescarDatos();
            } else if (panelActivo == acercaDePanel && panelAcercaDe != null) {
                panelAcercaDe.refrescarDatos();
            }
        }
    }

    private void mostrarCambiarContrasena() {
        try {
            // VentanaCambiarClave ventana = new VentanaCambiarClave(controlador);
            // ventana.setLocationRelativeTo(this);
            // ventana.setVisible(true);

            JOptionPane.showMessageDialog(this,
                    "Funcionalidad de cambio de contraseña pendiente de implementar",
                    "En desarrollo",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al abrir ventana de cambio de contraseña: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cerrarSesion() {
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea cerrar la sesión?",
                "Confirmar cierre de sesión",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            controlador.cerrarSesion();
        }
    }

    private void salirAplicacion() {
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea salir del sistema?",
                "Confirmar salida",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    // Getters para acceso desde el controlador si es necesario
    public PanelGestionMedicos getPanelGestionMedicos() {
        return panelGestionMedicos;
    }

    public PanelGestionFarmaceutas getPanelGestionFarmaceutas() {
        return panelGestionFarmaceutas;
    }

    public PanelGestionPacientes getPanelGestionPacientes() {
        return panelGestionPacientes;
    }

    public PanelGestionMedicamentos getPanelGestionMedicamentos() {
        return panelGestionMedicamentos;
    }

    public PanelDespacho getPanelDespacho() {
        return panelDespacho;
    }

    public PanelHistorico getPanelHistorico() {
        return panelHistorico;
    }

    public PanelAcercaDe getPanelAcercaDe() {
        return panelAcercaDe;
    }
}