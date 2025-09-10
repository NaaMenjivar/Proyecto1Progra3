package presentacion.vista.principal;

import presentacion.controlador.ControladorPrincipal;
import presentacion.vista.medico.*;
import logica.entidades.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Ventana Principal para Médicos
 * Utiliza la estructura de .form existente con JTabbedPane
 */
public class VentanaMedico extends JFrame {

    // Componentes del .form
    private JPanel panelPrincipal;
    private JTabbedPane panelTabs;
    private JPanel prescribirMedico;
    // private JPanel dashboardMedico;  // Dashboard comentado
    private JPanel historicoMedico;
    private JPanel acercaDeMedico;

    // Instancias de los paneles de gestión
    private PanelPrescribir panelPrescribir;
    // private PanelDashboard panelDashboard;  // Dashboard comentado
    private presentacion.vista.medico.PanelHistorico panelHistorico;
    private presentacion.vista.medico.PanelAcercaDe panelAcercaDe;

    // Controlador MVC
    private ControladorPrincipal controlador;

    public VentanaMedico(ControladorPrincipal controlador) {
        this.controlador = controlador;
        inicializarVentana();
        inicializarPaneles();
        configurarEventos();
    }

    private void inicializarVentana() {
        setTitle("Sistema Hospitalario - Módulo Médico");
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
        itemAcercaDe.addActionListener(e -> panelTabs.setSelectedComponent(acercaDeMedico));
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
                setTitle("Sistema Hospitalario - Médico: " + usuario.getNombre());
            }
        } catch (Exception e) {
            setTitle("Sistema Hospitalario - Módulo Médico");
        }
    }

    private void inicializarPaneles() {
        try {
            // Panel Prescribir
            panelPrescribir = new PanelPrescribir(controlador);
            prescribirMedico.setLayout(new BorderLayout());
            prescribirMedico.add(panelPrescribir.getPanel(), BorderLayout.CENTER);

            // Panel Dashboard - COMENTADO
            /*panelDashboard = new PanelDashboard(controlador);
            dashboardMedico.setLayout(new BorderLayout());
            dashboardMedico.add(panelDashboard.getPanel(), BorderLayout.CENTER);*/

            // Panel Histórico
            panelHistorico = new presentacion.vista.medico.PanelHistorico(controlador);
            historicoMedico.setLayout(new BorderLayout());
            historicoMedico.add(panelHistorico.getPanel(), BorderLayout.CENTER);

            // Panel Acerca De
            panelAcercaDe = new presentacion.vista.medico.PanelAcercaDe();
            acercaDeMedico.setLayout(new BorderLayout());
            acercaDeMedico.add(panelAcercaDe.getPanel(), BorderLayout.CENTER);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al inicializar paneles: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
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

            if (panelActivo == prescribirMedico && panelPrescribir != null) {
                panelPrescribir.refrescarDatos();
                // Dashboard comentado
            /*} else if (panelActivo == dashboardMedico && panelDashboard != null) {
                panelDashboard.actualizarDatos();*/
            } else if (panelActivo == historicoMedico && panelHistorico != null) {
                panelHistorico.refrescarDatos();
            } else if (panelActivo == acercaDeMedico && panelAcercaDe != null) {
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
    public PanelPrescribir getPanelPrescribir() {
        return panelPrescribir;
    }

    public presentacion.vista.medico.PanelHistorico getPanelHistorico() {
        return panelHistorico;
    }

    public presentacion.vista.medico.PanelAcercaDe getPanelAcercaDe() {
        return panelAcercaDe;
    }
}
