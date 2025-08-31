package presentacion.modelo;

import javax.swing.table.AbstractTableModel;
import modelo.Medicamento;
import modelo.lista.Lista;

public class ModeloTablaMedicamentos extends AbstractTableModel {
    private Lista<Medicamento> medicamentos;
    private String[] columnas = {"Código", "Nombre", "Presentación", "Stock"};

    public ModeloTablaMedicamentos() {
        this.medicamentos = new Lista<>();
    }

    public void setMedicamentos(Lista<Medicamento> medicamentos) {
        this.medicamentos = medicamentos;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return medicamentos.getTam();
    }

    @Override
    public int getColumnCount() {
        return columnas.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnas[column];
    }

    @Override
    public Object getValueAt(int row, int col) {
        Medicamento medicamento = medicamentos.obtenerPorPos(row);
        switch (col) {
            case 0: return medicamento.getCodigo();
            case 1: return medicamento.getNombre();
            case 2: return medicamento.getPresentacion();
            case 3: return medicamento.getStock();
            default: return "";
        }
    }

    public Medicamento getMedicamentoEnFila(int fila) {
        if (fila >= 0 && fila < medicamentos.getTam()) {
            return medicamentos.obtenerPorPos(fila);
        }
        return null;
    }
}
