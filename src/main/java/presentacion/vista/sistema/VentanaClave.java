package presentacion.vista.sistema;

import presentacion.controlador.ControladorLogin;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaClave {
    private JPanel panelClave;
    private JPasswordField claveNuevaField;
    private JPasswordField claveActualField;
    private JPasswordField claveNuevaField2;
    private JPanel panelBotones;
    private JButton checkBoton;
    private JButton xBoton;
    private JLabel claveActual;
    private JLabel claveNueva;
    private JLabel claveNueva2;
    private ControladorLogin controlador;

    public VentanaClave() {

        checkBoton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String claveActual = claveNuevaField.getText();
                String claveNueva = claveNuevaField.getText();
                String claveNueva2 = claveNuevaField2.getText();
                controlador.cambiarClave(claveActual,claveNueva,claveNueva2);
            }
        });
        xBoton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controlador.cerrarVentanaClave();
            }
        });
    }

    public JPanel getPanelClave() {return panelClave;}
    public void setControlador(ControladorLogin controlador) {this.controlador = controlador;}
}
