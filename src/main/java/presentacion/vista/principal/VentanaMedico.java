package presentacion.vista.principal;

import presentacion.controlador.ControladorPrincipal;
import presentacion.vista.medico.PanelPrescribir;
import presentacion.vista.medico.PanelHistorico;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Ventana principal para médicos - Vista MVC
 */
public class VentanaMedico extends JFrame {
    // Componentes del formulario (declarados en el .form)
    private JPanel panelPrincipal;
    private JPanel prescribirMedico;
    private JPanel dashboardMedico;    // CAMPO CORREGIDO - este ya estaba
    private JTabbedPane panelTabs;
    private JPanel historicoMedico;
    private JPanel acercaDeMedico;

    // MVC Components
    private ControladorPrincipal controlador;
    private PanelPrescribir panelPrescribir;
    private PanelHistorico panelHistorico;

    public VentanaMedico(ControladorPrincipal controlador) {
        this.controlador = controlador;

        inicializarVentana();
        inicializarComponentes();
        configurarEventos();
        cargarPaneles();
    }

    private void inicializarVentana() {
        setTitle("Sistema de Recetas - Médico");
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void inicializarComponentes() {
        // Los componentes ya están inicializados por el .form
        // Solo configuramos propiedades adicionales si es necesario
    }

    private void configurarEventos() {
        // Evento de cierre de ventana
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cerrarVentana();
            }
        });

        // Evento de cambio de pestaña
        panelTabs.addChangeListener(e -> {
            actualizarPestanaActiva();
        });
    }

    private void cargarPaneles() {
        try {
            // Panel Prescribir
            panelPrescribir = new PanelPrescribir(controlador);
            prescribirMedico.removeAll();
            prescribirMedico.add(panelPrescribir.getPanel());

            // Panel Histórico
            panelHistorico = new PanelHistorico(controlador);
            historicoMedico.removeAll();
            historicoMedico.add(panelHistorico.getPanel());

            // Dashboard - temporal vacío
            dashboardMedico.removeAll();
            JLabel lblDashboard = new JLabel("Dashboard en desarrollo...", SwingConstants.CENTER);
            dashboardMedico.add(lblDashboard);

            // Acerca de - panel informativo
            cargarPanelAcercaDe();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar paneles: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarPanelAcercaDe() {
        acercaDeMedico.removeAll();

        JTextArea txtAcercaDe = new JTextArea();
        txtAcercaDe.setEditable(false);
        txtAcercaDe.setText(
                "SISTEMA DE PRESCRIPCIÓN Y DESPACHO DE RECETAS\n\n" +
                        "Módulo para Médicos\n" +
                        "Universidad Nacional - EIF206 Programación 3\n\n" +
                        "Funcionalidades disponibles:\n" +
                        "• Prescribir recetas para pacientes\n" +
                        "• Consultar histórico de recetas\n" +
                        "• Ver dashboard con estadísticas\n\n" +
                        "Usuario actual: " +
                        (controlador.getUsuarioActual() != null ?
                                controlador.getUsuarioActual().getNombre() : "No identificado")
        );

        JScrollPane scrollAcercaDe = new JScrollPane(txtAcercaDe);
        acercaDeMedico.add(scrollAcercaDe);
    }

    private void actualizarPestanaActiva() {
        int pestanaActiva = panelTabs.getSelectedIndex();

        // Actualizar datos cuando se cambia de pestaña
        switch (pestanaActiva) {
            case 1: // Dashboard
                // Aquí se actualizaría el dashboard cuando esté implementado
                break;
            case 2: // Histórico
                if (panelHistorico != null) {
                    // Recargar datos del histórico
                }
                break;
        }
    }

    private void cerrarVentana() {
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de cerrar la sesión?",
                "Confirmar cierre",
                JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            controlador.cerrarSesion();
        }
    }

    // Métodos públicos para acceder a los paneles
    public PanelPrescribir getPanelPrescribir() {
        return panelPrescribir;
    }

    public PanelHistorico getPanelHistorico() {
        return panelHistorico;
    }
}
