package presentacion.vista.principal;

import presentacion.controlador.ControladorPrincipal;
import presentacion.vista.administrador.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Ventana Principal del Sistema - Vista para Administradores y Farmaceutas
 * Contiene todos los paneles de gestión en pestañas organizadas
 */
public class VentanaPrincipal extends JFrame {

    // Componentes del formulario (vinculados desde el .form)
    private JPanel panelPrincipal;
    private JTabbedPane panelTabs;
    private JPanel medicosPanel;
    private JPanel farmaceutasPanel;
    private JPanel pacientesPanel;
    private JPanel medicamentosPanel;
    private JPanel despachoPanel;
    private JPanel dashboardPanel;
    private JPanel historicoPanel;
    private JPanel acercaDePanel;

    // Controlador MVC
    private ControladorPrincipal controlador;

    // Instancias de los paneles de gestión
    private PanelGestionMedicos panelGestionMedicos;
    private PanelGestionFarmaceutas panelGestionFarmaceutas;
    private PanelGestionPacientes panelGestionPacientes;
    private PanelGestionMedicamentos panelGestionMedicamentos;
    private PanelDespacho panelDespacho;
    private PanelDashboard panelDashboard;
    private PanelHistorico panelHistorico;
    private PanelAcercaDe panelAcercaDe;

    public VentanaPrincipal(ControladorPrincipal controlador) {
        this.controlador = controlador;
        inicializarVentana();
        crearPaneles();
        integrarPanelesEnTabs();
        configurarEventos();
    }

    private void inicializarVentana() {
        setTitle("Sistema de Recetas - Administración");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setContentPane(panelPrincipal);

        // Configurar icono de la ventana
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/iconos/hospital.png"));
            setIconImage(icon.getImage());
        } catch (Exception e) {
            // Continuar sin icono si no se encuentra
        }
    }

    private void crearPaneles() {
        // Crear instancias de todos los paneles de gestión
        panelGestionMedicos = new PanelGestionMedicos(controlador);
        panelGestionFarmaceutas = new PanelGestionFarmaceutas(controlador);
        panelGestionPacientes = new PanelGestionPacientes(controlador);
        panelGestionMedicamentos = new PanelGestionMedicamentos(controlador);
        panelDespacho = new PanelDespacho(controlador);
        //panelDashboard = new PanelDashboard(controlador);
        panelHistorico = new PanelHistorico(controlador);
        panelAcercaDe = new PanelAcercaDe();
    }

    private void integrarPanelesEnTabs() {
        // Limpiar paneles existentes del formulario
        medicosPanel.removeAll();
        farmaceutasPanel.removeAll();
        pacientesPanel.removeAll();
        medicamentosPanel.removeAll();
        despachoPanel.removeAll();
        dashboardPanel.removeAll();
        historicoPanel.removeAll();
        acercaDePanel.removeAll();

        // Configurar layout para cada panel del tab
        medicosPanel.setLayout(new BorderLayout());
        farmaceutasPanel.setLayout(new BorderLayout());
        pacientesPanel.setLayout(new BorderLayout());
        medicamentosPanel.setLayout(new BorderLayout());
        despachoPanel.setLayout(new BorderLayout());
        dashboardPanel.setLayout(new BorderLayout());
        historicoPanel.setLayout(new BorderLayout());
        acercaDePanel.setLayout(new BorderLayout());

        // Integrar cada panel de gestión en su respectivo tab
        medicosPanel.add(panelGestionMedicos.getPanelPrincipal(), BorderLayout.CENTER);
        farmaceutasPanel.add(panelGestionFarmaceutas.getPanelPrincipal(), BorderLayout.CENTER);
        pacientesPanel.add(panelGestionPacientes.getPanelPrincipal(), BorderLayout.CENTER);
        medicamentosPanel.add(panelGestionMedicamentos.getPanelPrincipal(), BorderLayout.CENTER);
        //despachoPanel.add(panelDespacho.getPanelPrincipal(), BorderLayout.CENTER);
        //dashboardPanel.add(panelDashboard.getPanelPrincipal(), BorderLayout.CENTER);
        historicoPanel.add(panelHistorico.getPanelPrincipal(), BorderLayout.CENTER);
        acercaDePanel.add(panelAcercaDe.getPanelPrincipal(), BorderLayout.CENTER);

        // Configurar iconos para las pestañas (si están disponibles)
        configurarIconosPestanas();

        // Revalidar y repintar para mostrar los cambios
        panelTabs.revalidate();
        panelTabs.repaint();
    }

    private void configurarIconosPestanas() {
        try {
            // Configurar iconos para cada pestaña
            panelTabs.setIconAt(0, new ImageIcon(getClass().getResource("/iconos/medico_small.png")));
            panelTabs.setIconAt(1, new ImageIcon(getClass().getResource("/iconos/farmaceuta_small.png")));
            panelTabs.setIconAt(2, new ImageIcon(getClass().getResource("/iconos/paciente_small.png")));
            panelTabs.setIconAt(3, new ImageIcon(getClass().getResource("/iconos/medicamento_small.png")));
            panelTabs.setIconAt(4, new ImageIcon(getClass().getResource("/iconos/despacho_small.png")));
            panelTabs.setIconAt(5, new ImageIcon(getClass().getResource("/iconos/dashboard_small.png")));
            panelTabs.setIconAt(6, new ImageIcon(getClass().getResource("/iconos/historico_small.png")));
            panelTabs.setIconAt(7, new ImageIcon(getClass().getResource("/iconos/info_small.png")));
        } catch (Exception e) {
            // Continuar sin iconos si no se encuentran
            System.out.println("Iconos no encontrados, continuando sin iconos en pestañas");
        }
    }

    private void configurarEventos() {
        // Evento de cierre de ventana
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cerrarSistema();
            }
        });

        // Evento de cambio de pestaña (opcional para optimizaciones)
        panelTabs.addChangeListener(e -> {
            int tabSeleccionado = panelTabs.getSelectedIndex();
            onCambioDeTab(tabSeleccionado);
        });
    }

    private void onCambioDeTab(int indiceTab) {
        // Actualizar datos cuando se cambie de pestaña (si es necesario)
        switch (indiceTab) {
            case 0: // Médicos
                panelGestionMedicos.refrescarDatos();
                break;
            case 1: // Farmaceutas
                panelGestionFarmaceutas.refrescarDatos();
                break;
            case 2: // Pacientes
                panelGestionPacientes.refrescarDatos();
                break;
            case 3: // Medicamentos
                panelGestionMedicamentos.refrescarDatos();
                break;
            case 4: // Despacho
                panelDespacho.refrescarDatos();
                break;
            //case 5: // Dashboard
              //  panelDashboard.refrescarDatos();
                //break;
            case 6: // Histórico
                panelHistorico.refrescarDatos();
                break;
            // AcercaDe no necesita refrescar datos
        }
    }

    private void cerrarSistema() {
        int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro que desea cerrar el sistema?",
                "Confirmar salida",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (opcion == JOptionPane.YES_OPTION) {
            controlador.cerrarSesion();
            dispose();
        }
    }

    // ================================
    // MÉTODOS PÚBLICOS PARA EL CONTROLADOR
    // ================================

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarError(String error) {
        JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void mostrarAdvertencia(String advertencia) {
        JOptionPane.showMessageDialog(this, advertencia, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }

    public void seleccionarTab(int indice) {
        if (indice >= 0 && indice < panelTabs.getTabCount()) {
            panelTabs.setSelectedIndex(indice);
        }
    }

    public void actualizarTitulo(String nuevoTitulo) {
        setTitle("Sistema de Recetas - " + nuevoTitulo);
    }

    // Métodos para acceder a paneles específicos desde el controlador
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

    public PanelDashboard getPanelDashboard() {
        return panelDashboard;
    }

    public PanelHistorico getPanelHistorico() {
        return panelHistorico;
    }

    public PanelAcercaDe getPanelAcercaDe() {
        return panelAcercaDe;
    }
}