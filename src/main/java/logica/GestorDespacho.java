package logica;

import modelo.ListaMedicamentos;
import modelo.Medicamento;

public class GestorDespacho {
    private ListaMedicamentos medicamentos;

    public GestorDespacho(ListaMedicamentos medicamentos) {
        this.medicamentos = medicamentos;
    }

    public boolean despacharMedicamento(String idPaciente, String codigo, int cantidad) {
        Medicamento med = medicamentos.buscarPorCodigo(codigo);
        if (med != null && med.getStock() >= cantidad) {
            med.setStock(med.getStock() - cantidad);
            return true;
        }
        return false;
    }

}
