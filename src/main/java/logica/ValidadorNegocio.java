package logica;

import modelo.ListaPacientes;
import modelo.ListaMedicamentos;
import modelo.ListaUsuarios;

public class ValidadorNegocio {
    private ListaPacientes pacientes;
    private ListaMedicamentos medicamentos;
    private ListaUsuarios usuarios;

    public ValidadorNegocio(ListaPacientes pacientes, ListaMedicamentos medicamentos) {
        this.pacientes = pacientes;
        this.medicamentos = medicamentos;
    }

    public boolean validarPaciente(String idPaciente) {
        return pacientes.buscarPorID(idPaciente) != null;
    }

    public boolean validarMedicamento(String codigo) {
        return medicamentos.buscarPorCodigo(codigo) != null;
    }

    public boolean validarUsuario(String idUsuario) {
        return usuarios.buscarPorId(idUsuario) != null;
    }
}
