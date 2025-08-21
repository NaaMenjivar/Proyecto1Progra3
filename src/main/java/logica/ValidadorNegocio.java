package logica;

import modelo.*;
import modelo.lista.Lista;

public class ValidadorNegocio {
    private Lista<Paciente> pacientes;
    private Lista<Medicamento> medicamentos;
    private Lista<Usuario> usuarios;

    public ValidadorNegocio(Lista<Paciente> pacientes, Lista<Medicamento> medicamentos) {
        this.pacientes = pacientes;
        this.medicamentos = medicamentos;
    }

    public boolean validarPaciente(String idPaciente) {
        return pacientes.buscarPorId(idPaciente) != null;
    }

    public boolean validarMedicamento(String codigo) {
        return medicamentos.buscarPorId(codigo) != null;
    }

    public boolean validarUsuario(String idUsuario) {
        return usuarios.buscarPorId(idUsuario) != null;
    }
}
