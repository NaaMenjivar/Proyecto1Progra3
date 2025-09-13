package presentacion.vista.principal;

import logica.entidades.TipoUsuario;
import presentacion.controlador.ControladorPrincipal;
import presentacion.vista.administrador.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

public class VentanaPrincipal extends JFrame {

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
    private JPanel prescribir;

    private final ControladorPrincipal controlador;

    private PanelGestionMedicos panelGestionMedicos;
    private PanelGestionFarmaceutas panelGestionFarmaceutas;
    private PanelGestionPacientes panelGestionPacientes;
    private PanelGestionMedicamentos panelGestionMedicamentos;
    private PanelDespacho panelDespacho;
    private PanelDashboard panelDashboard;
    private PanelHistorico panelHistorico;
    private PanelAcercaDe panelAcercaDe;
    private PanelPrescribir prescribirPanel;

    private final TipoUsuario tipoUsuario;

    public VentanaPrincipal(ControladorPrincipal controlador, TipoUsuario tipoUsuario) {
        this.controlador = controlador;
        this.tipoUsuario = tipoUsuario;
        inicializarVentana();
        crearPaneles();
        integrarPanelesEnTabs();
        configurarEventos();
    }

    private void inicializarVentana() {
        String titulo = "Sistema de Recetas - " + tipoUsuario.toString();
        setTitle(titulo);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setContentPane(panelPrincipal);

        // Configurar icono de la ventana
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/Iconos/hospital.png"));
            setIconImage(icon.getImage());
        } catch (Exception e) {
            // Continuar sin icono si no se encuentra
        }
    }

    private void crearPaneles() {

        switch(tipoUsuario){
            case ADMINISTRADOR:{
                panelGestionMedicos = new PanelGestionMedicos(controlador);
                panelGestionFarmaceutas = new PanelGestionFarmaceutas(controlador);
                panelGestionPacientes = new PanelGestionPacientes(controlador);
                panelGestionMedicamentos = new PanelGestionMedicamentos(controlador);
                panelDespacho = new PanelDespacho(controlador);
                panelDashboard = new PanelDashboard(controlador);
                panelHistorico = new PanelHistorico(controlador);
                prescribirPanel = new PanelPrescribir(controlador);
                panelAcercaDe = new PanelAcercaDe();

            }break;
            case MEDICO:{
                prescribirPanel = new PanelPrescribir(controlador);
                panelDashboard = new PanelDashboard(controlador);
                panelHistorico = new PanelHistorico(controlador);
                panelAcercaDe = new PanelAcercaDe();
            }break;
            case FARMACEUTA:{
                panelGestionMedicamentos = new PanelGestionMedicamentos(controlador);
            }break;
        }

    }

    private void integrarPanelesEnTabs() {
        panelTabs.removeAll();
        switch(tipoUsuario){
            case ADMINISTRADOR:{
                medicosPanel.setLayout(new BorderLayout());
                farmaceutasPanel.setLayout(new BorderLayout());
                pacientesPanel.setLayout(new BorderLayout());
                medicamentosPanel.setLayout(new BorderLayout());
                despachoPanel.setLayout(new BorderLayout());
                dashboardPanel.setLayout(new BorderLayout());
                historicoPanel.setLayout(new BorderLayout());
                acercaDePanel.setLayout(new BorderLayout());
                prescribir.setLayout(new BorderLayout());


                medicosPanel.add(panelGestionMedicos.getPanelPrincipal(), BorderLayout.CENTER);
                farmaceutasPanel.add(panelGestionFarmaceutas.getPanelPrincipal(), BorderLayout.CENTER);
                pacientesPanel.add(panelGestionPacientes.getPanelPrincipal(), BorderLayout.CENTER);
                medicamentosPanel.add(panelGestionMedicamentos.getPanelPrincipal(), BorderLayout.CENTER);
                despachoPanel.add(panelDespacho.getPanelPrincipal(), BorderLayout.CENTER);
                dashboardPanel.add(panelDashboard.getPanelPrincipal(), BorderLayout.CENTER);
                historicoPanel.add(panelHistorico.getPanelPrincipal(), BorderLayout.CENTER);
                acercaDePanel.add(panelAcercaDe.getPanelPrincipal(), BorderLayout.CENTER);
                prescribir.add(prescribirPanel.getPanelPrincipal(),BorderLayout.CENTER);

                panelTabs.addTab("Médicos", ajustarIcono("/Iconos/medico.png",30,30), medicosPanel);
                panelTabs.addTab("Farmacéutas", ajustarIcono("/Iconos/farmaceuta.png",30,30), farmaceutasPanel);
                panelTabs.addTab("Pacientes", ajustarIcono("/Iconos/pasciente.png",30,30), pacientesPanel);
                panelTabs.addTab("Medicamentos", ajustarIcono("/Iconos/medicamentos.png",30,30), medicamentosPanel);
                panelTabs.addTab("Prescribir",ajustarIcono("/Iconos/prescribir.png",30,30), prescribir);
                panelTabs.addTab("Despacho", ajustarIcono("/Iconos/despacho.png",30,30), despachoPanel);
                panelTabs.addTab("Dashboard", ajustarIcono("/Iconos/dashboard.png",30,30), dashboardPanel);
                panelTabs.addTab("Histórico", ajustarIcono("/Iconos/historico.png",30,30), historicoPanel);
                panelTabs.addTab("Acerca de", ajustarIcono("/Iconos/info.png",30,30), acercaDePanel);

        }break;
            case MEDICO:{
                despachoPanel.setLayout(new BorderLayout());
                dashboardPanel.setLayout(new BorderLayout());
                historicoPanel.setLayout(new BorderLayout());
                acercaDePanel.setLayout(new BorderLayout());
                prescribir.setLayout(new BorderLayout());

                despachoPanel.add(panelDespacho.getPanelPrincipal(), BorderLayout.CENTER);
                dashboardPanel.add(panelDashboard.getPanelPrincipal(), BorderLayout.CENTER);
                historicoPanel.add(panelHistorico.getPanelPrincipal(), BorderLayout.CENTER);
                acercaDePanel.add(panelAcercaDe.getPanelPrincipal(), BorderLayout.CENTER);
                prescribir.add(prescribirPanel.getPanelPrincipal(),BorderLayout.CENTER);

                panelTabs.addTab("Prescribir",ajustarIcono("/Iconos/prescribir.png",30,30), prescribir);
                panelTabs.addTab("Despacho", ajustarIcono("/Iconos/despacho.png",30,30), despachoPanel);
                panelTabs.addTab("Dashboard", ajustarIcono("/Iconos/dashboard.png",30,30), dashboardPanel);
                panelTabs.addTab("Histórico", ajustarIcono("/Iconos/historico.png",30,30), historicoPanel);
                panelTabs.addTab("Acerca de", ajustarIcono("/Iconos/info.png",30,30), acercaDePanel);
            }break;
            case FARMACEUTA:{
                despachoPanel.setLayout(new BorderLayout());
                medicamentosPanel.setLayout(new BorderLayout());

                despachoPanel.add(panelDespacho.getPanelPrincipal(), BorderLayout.CENTER);
                medicamentosPanel.add(panelGestionMedicamentos.getPanelPrincipal(), BorderLayout.CENTER);

                panelTabs.addTab("Despacho", ajustarIcono("/Iconos/despacho.png",30,30), despachoPanel);
                panelTabs.addTab("Medicamentos", ajustarIcono("/Iconos/medicamentos.png",30,30), medicamentosPanel);
            }break;
        }

        //configurarIconosPestanas();

        panelTabs.revalidate();
        panelTabs.repaint();
    }

    private void configurarIconosPestanas() {
        try {
            // Configurar iconos para cada pestaña
            panelTabs.setIconAt(0, ajustarIcono("/Iconos/medico.png",30,30));
            panelTabs.setIconAt(1, ajustarIcono("/Iconos/farmaceuta.png",30,30));
            panelTabs.setIconAt(2, ajustarIcono("/Iconos/pasciente.png",30,30));
            panelTabs.setIconAt(3, ajustarIcono("/Iconos/medicamentos.png",30,30));
            panelTabs.setIconAt(4, ajustarIcono("/Iconos/despacho.png",30,30));
            panelTabs.setIconAt(5, ajustarIcono("/Iconos/dashboard.png",30,30));
            panelTabs.setIconAt(6, ajustarIcono("/Iconos/historico.png",30,30));
            panelTabs.setIconAt(7, ajustarIcono("/Iconos/info.png",30,30));
        } catch (Exception e) {
            System.out.println("Iconos no encontrados, continuando sin iconos en pestañas");
        }
    }

    private ImageIcon ajustarIcono(String ruta, int ancho, int alto) {
        URL url = getClass().getResource(ruta);
        if (url == null) {
            System.err.println("No se encontró la imagen: " + ruta);
            return null;
        }
        ImageIcon iconoOriginal = new ImageIcon(url);
        Image imagen = iconoOriginal.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        return new ImageIcon(imagen);
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
        /*panelTabs.addChangeListener(e -> {
            int tabSeleccionado = panelTabs.getSelectedIndex();
            onCambioDeTab(tabSeleccionado);
        });*/
    }

    private void onCambioDeTab(int indiceTab) {
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
            case 5: // Dashboard
                panelDashboard.refrescarDatos();
                break;
            case 6: // Histórico
                panelHistorico.refrescarDatos();
                break;
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
        }
    }


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