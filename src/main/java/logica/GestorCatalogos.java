package logica;

import modelo.Paciente;
import modelo.Medicamento;
import modelo.lista.Lista;

public class GestorCatalogos {
    private Lista<Paciente> pacientes;
    private Lista<Medicamento> medicamentos;

    public GestorCatalogos() {
        pacientes = new Lista<Paciente>();
        medicamentos = new Lista<Medicamento>();
    }

    public Lista<Paciente> getPacientes() {
        return pacientes;
    }

    public Lista<Medicamento> getMedicamentos() {
        return medicamentos;
    }

    // Agregar paciente solo si no existe
    public boolean agregarPaciente(Paciente paciente) {
        if (paciente == null) { //Buscar forma de comprobar si existe
            return false;
        }
        pacientes.agregarFinal(paciente);
        return true;
    }

    public Paciente buscarPaciente(String id) {
        return pacientes.buscarPorId(id);
    }

    // Eliminar paciente solo si existe
    public boolean eliminarPaciente(String id) {
        if (!pacientes.existe(id)) {
            return false;
        }
        return pacientes.eliminarPorId(id);
    }

    // Actualizar paciente por id
    public boolean actualizarPaciente(String id, Paciente nuevoPaciente) {
        return pacientes.actualizarPorId(id, nuevoPaciente);
    }

    // Agregar medicamento solo si no existe
    public boolean agregarMedicamento(Medicamento medicamento) {
        if (medicamento == null || medicamentos.existe(medicamento.getCodigo())) {
            return false;
        }
        medicamentos.agregarFinal(medicamento);
        return true;
    }

    public Medicamento buscarMedicamento(String codigo) {
        return medicamentos.buscarPorId(codigo);
    }

    // Eliminar medicamento solo si existe
    public boolean eliminarMedicamento(String codigo) {
        if (!medicamentos.existe(codigo)) {
            return false;
        }
        return medicamentos.eliminarPorId(codigo);
    }

    // Actualizar medicamento por código
    public boolean actualizarMedicamento(String codigo, Medicamento nuevoMedicamento) {
        return medicamentos.actualizarPorId(codigo, nuevoMedicamento);
    }
}
