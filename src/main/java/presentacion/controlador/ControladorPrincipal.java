package presentacion.controlador;

import presentacion.modelo.ModeloPrincipal;
import presentacion.vista.principales.VentanaPrincipal;

public class ControladorPrincipal {

    private ModeloPrincipal modeloPrincipal;
    private VentanaPrincipal ventanaPrincipal;

    public ControladorPrincipal() {
        ventanaPrincipal = new VentanaPrincipal();
        modeloPrincipal = new ModeloPrincipal();
    }

    public void iniciarVentanaPrincipal() {
        ventanaPrincipal.setVisible(true);
    }
}
