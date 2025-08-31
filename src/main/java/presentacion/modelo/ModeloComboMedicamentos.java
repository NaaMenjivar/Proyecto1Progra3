package presentacion.modelo;

import modelo.Medicamento;
import modelo.lista.Lista;
import javax.swing.DefaultComboBoxModel;

public class ModeloComboMedicamentos {
    private Lista<Medicamento> medicamentos;
    private DefaultComboBoxModel<Medicamento> comboModel;

    public ModeloComboMedicamentos() {
        this.medicamentos = new Lista<>();
        this.comboModel = new DefaultComboBoxModel<>();
    }

    public void setMedicamentos(Lista<Medicamento> medicamentos) {
        this.medicamentos = medicamentos;
        actualizarModelo();
    }

    private void actualizarModelo() {
        comboModel.removeAllElements();
        for (int i = 0; i < medicamentos.getTam(); i++) {
            comboModel.addElement(medicamentos.obtenerPorPos(i));
        }
    }

    public DefaultComboBoxModel<Medicamento> getComboModel() {
        return comboModel;
    }

    public Medicamento getMedicamentoSeleccionado() {
        return (Medicamento) comboModel.getSelectedItem();
    }

    public Lista<Medicamento> getMedicamentos() {
        return medicamentos;
    }
}
