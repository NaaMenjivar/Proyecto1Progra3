package presentacion.modelo;

import javax.swing.table.AbstractTableModel;
import modelo.Paciente;
import modelo.lista.Lista;

public class ModeloTablaPacientes extends AbstractTableModel {
    private Lista<Paciente> pacientes;
    private String[] columnas = {"ID", "Nombre", "Fecha Nacimiento", "Edad", "Teléfono"};

    public ModeloTablaPacientes() {
        this.pacientes = new Lista<>();
    }

    public void setPacientes(Lista<Paciente> pacientes) {
        this.pacientes = pacientes;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return pacientes.getTam();
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
        Paciente paciente = pacientes.obtenerPorPos(row);
        switch (col) {
            case 0: return paciente.getId();
            case 1: return paciente.getNombre();
            case 2: return paciente.getFechaNacimiento();
            case 3: return paciente.getEdad();
            case 4: return paciente.getTelefono();
            default: return "";
        }
    }

    public Paciente getPacienteEnFila(int fila) {
        if (fila >= 0 && fila < pacientes.getTam()) {
            return pacientes.obtenerPorPos(fila);
        }
        return null;
    }
}
