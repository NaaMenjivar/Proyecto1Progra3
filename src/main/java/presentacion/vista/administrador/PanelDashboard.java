package presentacion.vista.administrador;

import presentacion.controlador.ControladorPrincipal;

import javax.swing.*;

public class PanelDashboard {
    private ControladorPrincipal controlador;
    private JPanel panelDashBoard;
    private JPanel panelDatos;
    private JPanel panelGraficoMedicamentos;
    private JPanel panelGraficoRecetas;
    private JTable tablaMedicamentos;
    private JComboBox comboBoxMedicamentos;
    private JButton dobleCheckButton;
    private JButton checkButton;
    private JLabel hastaField;
    private JLabel desdeField;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JComboBox comboBox3;
    private JComboBox comboBox4;
    private JPanel panelBotonesInferiores;
    private JButton botonInferior2;
    private JButton botonInferior1;
    private JPanel panelFecha;
    private JPanel panelSeleccionarMedicamentos;
    private JPanel panelTabla;

    public PanelDashboard(ControladorPrincipal controlador) {
        this.controlador = controlador;
    }

    public JPanel getPanelPrincipal() {
        return panelDashBoard;
    }

    public void refrescarDatos(){}
}
