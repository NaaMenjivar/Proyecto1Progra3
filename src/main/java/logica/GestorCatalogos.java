package logica;

import modelo.ListaMedicamentos;
import modelo.ListaPacientes;
import modelo.Paciente;
import modelo.Medicamento;

public class GestorCatalogos {
    private ListaPacientes pacientes;
    private ListaMedicamentos medicamentos;

    public GestorCatalogos() {
        pacientes = new ListaPacientes();
        medicamentos = new ListaMedicamentos();
    }

    public ListaPacientes getPacientes() {
        return pacientes;
    }

    public ListaMedicamentos getMedicamentos() {
        return medicamentos;
    }

    // Agregar paciente solo si no existe
    public boolean agregarPaciente(Paciente paciente) {
        if (paciente == null || pacientes.contiene(paciente.getId())) {
            return false;
        }
        pacientes.agregarFinal(paciente);
        return true;
    }

    public Paciente buscarPaciente(String id) {
        return pacientes.buscarPorID(id);
    }

    // Eliminar paciente solo si existe
    public boolean eliminarPaciente(String id) {
        if (!pacientes.contiene(id)) {
            return false;
        }
        return pacientes.eliminar(id);
    }

    // Actualizar paciente por id
    public boolean actualizarPaciente(String id, Paciente nuevoPaciente) {
        for (int i = 0; i < pacientes.getTam(); i++) {
            Paciente actual = pacientes.obtener(i);
            if (actual != null && actual.getId().equals(id)) {
                return pacientes.modificar(i, nuevoPaciente);
            }
        }
        return false;
    }

    // Agregar medicamento solo si no existe
    public boolean agregarMedicamento(Medicamento medicamento) {
        if (medicamento == null || medicamentos.contiene(medicamento.getCodigo())) {
            return false;
        }
        medicamentos.agregarFinal(medicamento);
        return true;
    }

    public Medicamento buscarMedicamento(String codigo) {
        return medicamentos.buscarPorCodigo(codigo);
    }

    // Eliminar medicamento solo si existe
    public boolean eliminarMedicamento(String codigo) {
        if (!medicamentos.contiene(codigo)) {
            return false;
        }
        return medicamentos.eliminar(codigo);
    }

    // Actualizar medicamento por código
    public boolean actualizarMedicamento(String codigo, Medicamento nuevoMedicamento) {
        for (int i = 0; i < medicamentos.getTam(); i++) {
            Medicamento actual = medicamentos.obtener(i);
            if (actual != null && actual.getCodigo().equals(codigo)) {
                return medicamentos.modificar(i, nuevoMedicamento);
            }
        }
        return false;
    }
}
