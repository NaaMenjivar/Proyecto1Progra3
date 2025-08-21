package logica;

import modelo.Medicamento;
import modelo.Paciente;
import modelo.lista.Lista;

public class GestorReportes {
    private Lista<Paciente> pacientes;
    private Lista<Medicamento> medicamentos;

    public GestorReportes(Lista<Paciente> pacientes, Lista<Medicamento> medicamentos) {
        this.pacientes = pacientes;
        this.medicamentos = medicamentos;
    }

    public String generarReporteGeneral() {
        StringBuilder sd = new StringBuilder();
        sd.append("Pacientes registrados: " + pacientes.getTam());
        sd.append("Medicamentos registrados: " + medicamentos.getTam());

        return sd.toString();
    }

    public void generarEstadisticas(int u) {
        System.out.println("Medicamentos con bajo stock:");
        medicamentos.mostrarBajoStock(u);
    }
    public String generarReportePacientes() {
        return pacientes.toString();
    }
    public String generarReporteMedicamentos() {
        return medicamentos.toString();
    }
}
