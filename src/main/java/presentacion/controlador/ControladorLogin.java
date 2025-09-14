package presentacion.controlador;

import presentacion.vista.sistema.VentanaClave;
import presentacion.vista.sistema.VentanaLogin;


import javax.swing.*;

public class ControladorLogin {
    private VentanaLogin ventanaLogin;
    private VentanaClave ventanaClave;
    private ControladorPrincipal controladorPrincipal;
    private String id;

    public ControladorLogin(VentanaLogin ventanaLogin, ControladorPrincipal controladorPrincipal) {
        this.ventanaLogin = ventanaLogin;
        ventanaLogin.setControlador(this);
        this.ventanaClave = new VentanaClave();
        ventanaClave.setControlador(this);
        this.controladorPrincipal = controladorPrincipal;
    }

    public void iniciarLogin(){
        JFrame frame = new JFrame("Login");
        frame.setContentPane(ventanaLogin.getVentanaLogin());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400,400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public Boolean inicioSesion(String username, String password){
        if(controladorPrincipal.autenticarUsuario(username,password)){
            cerrarLogin();
            return true;
        }
        return false;
    }

    public void cerrarLogin(){
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(ventanaLogin.getVentanaLogin());
        if(frame != null){
            frame.dispose();
        }
    }

    public void iniciarVentanaClave(){
        id = ventanaLogin.getId();
        JFrame frameLogin = (JFrame) SwingUtilities.getWindowAncestor(ventanaLogin.getVentanaLogin());
        frameLogin.setVisible(false);

        JFrame frame = new JFrame("Cambiar Clave");
        frame.setContentPane(ventanaClave.getPanelClave());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400,400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void cerrarVentanaClave(){
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(ventanaClave.getPanelClave());
        if(frame != null){
            frame.dispose();
        }
        JFrame frameLogin = (JFrame) SwingUtilities.getWindowAncestor(ventanaLogin.getVentanaLogin());
        frameLogin.setVisible(true);
    }

    public Boolean cambiarClave(String claveActual,String claveNueva,String confirmarClave){
        return controladorPrincipal.cambiarClave(claveActual,claveNueva,confirmarClave,id);
    }

}
