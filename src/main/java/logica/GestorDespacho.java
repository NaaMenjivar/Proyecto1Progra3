package logica;

import modelo.Medicamento;
import modelo.lista.Lista;

public class GestorDespacho {
    private Lista<Medicamento> medicamentos;

    public GestorDespacho(Lista<Medicamento> medicamentos) {
        this.medicamentos = medicamentos;
    }

    public boolean despacharMedicamento(String idPaciente, String codigo, int cantidad) {
        Medicamento med = medicamentos.buscarPorId(codigo);
        if (med != null && med.getStock() >= cantidad) {
            med.setStock(med.getStock() - cantidad);
            return true;
        }
        return false;
    }

}
