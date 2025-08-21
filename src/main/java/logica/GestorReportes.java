package logica;

import modelo.ListaPacientes;
import modelo.ListaMedicamentos;

public class GestorReportes {
    private ListaPacientes pacientes;
    private ListaMedicamentos medicamentos;

    public GestorReportes(ListaPacientes pacientes, ListaMedicamentos medicamentos) {
        this.pacientes = pacientes;
        this.medicamentos = medicamentos;
    }

    public void generarReporteGeneral() {
        System.out.println("Pacientes registrados: " + pacientes.getTam());
        System.out.println("Medicamentos registrados: " + medicamentos.getTam());
    }

    public void generarEstadisticas(int u) {
        System.out.println("Medicamentos con bajo stock:");
        medicamentos.mostrarBajoStock(u);
    }
    public void generarReportePacientes() {
        System.out.println("Lista de Pacientes:");
        for (int i = 0; i < pacientes.getTam(); i++) {
            System.out.println(pacientes.obtener(i));
        }
    }
    public void generarReporteMedicamentos() {
        System.out.println("Lista de Medicamentos:");
        for (int i = 0; i < medicamentos.getTam(); i++) {
            System.out.println(medicamentos.obtener(i));
        }
    }
}
