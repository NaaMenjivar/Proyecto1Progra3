package presentacion.vista.sistema;

import presentacion.controlador.ControladorLogin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class VentanaLogin {
    private JPanel ventanaLogin;
    private JTextField IdField;
    private JTextField claveField;
    private JButton SALIRButton;
    private JButton CAMBIARCLAVEButton;
    private JButton ENTRARButton;
    private JLabel loginIcon;

    private ControladorLogin controlador;

    public VentanaLogin() {
        ponerImagenes();

        SALIRButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        ENTRARButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!controlador.inicioSesion(IdField.getText(),claveField.getText())){
                    vaciarCasillas();
                }
            }
        });
        CAMBIARCLAVEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(IdField.getText().equals("")){
                    JOptionPane.showMessageDialog(ventanaLogin, "Debe ingresar un ID");
                }else{
                    controlador.iniciarVentanaClave();
                }
            }
        });
    }

    public void ponerImagenes(){
        SALIRButton.setIcon(ajustarIcono("/Iconos/salir.png", 28, 28));
        ENTRARButton.setIcon(ajustarIcono("/Iconos/check.png", 28, 28));
        CAMBIARCLAVEButton.setIcon(ajustarIcono("/Iconos/iconoCambiarClave.png", 28, 28));
        loginIcon.setIcon(ajustarIcono("/Iconos/login.jpg",32,32));
        loginIcon.setText("");

        // Eliminar texto para mostrar solo la imagen
        SALIRButton.setText("");
        ENTRARButton.setText("");
        CAMBIARCLAVEButton.setText("");
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

    public String getId(){
        return IdField.getText();
    }

    public JPanel getVentanaLogin() {return ventanaLogin;}
    public void setControlador(ControladorLogin controlador) {this.controlador = controlador;  }
    public void vaciarCasillas(){
        IdField.setText("");
        claveField.setText("");
    }
}