package presentacion.controlador;

public class ControladorPrincipal {

    private ControladorLogin login;

    public ControladorPrincipal () {
        System.out.printf("\tSe Creo un Menu Principal:");
        login = new ControladorLogin();
    }

    public void iniciarApp() {
        login.mostrarLogin();
    }
}
