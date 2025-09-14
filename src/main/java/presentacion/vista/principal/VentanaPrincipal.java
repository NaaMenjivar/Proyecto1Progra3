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
                panelDespacho = new PanelDespacho(controlador);
            }break;
            case FARMACEUTA:{
                panelGestionMedicamentos = new PanelGestionMedicamentos(controlador);
                panelDespacho = new PanelDespacho(controlador);
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

        panelTabs.revalidate();
        panelTabs.repaint();
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

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cerrarSistema();
            }
        });
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
}