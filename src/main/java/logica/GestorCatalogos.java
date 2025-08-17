package logica;

import modelo.ListaMedicamentos;
import modelo.ListaPacientes;

public class GestorCatalogos {
    private ListaPacientes pacientes;
    private ListaMedicamentos medicamentos;

    public GestorCatalogos() {
        pacientes = new ListaPacientes();
        medicamentos = new ListaMedicamentos();
    }
}
