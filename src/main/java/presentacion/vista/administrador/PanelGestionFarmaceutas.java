package presentacion.vista.administrador;

import logica.entidades.lista.ListaFarmaceutas;
import presentacion.controlador.ControladorPrincipal;
import presentacion.modelo.TableModelPrincipal;
import logica.entidades.*;
import logica.entidades.lista.Lista;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanelGestionFarmaceutas {
    private JPanel panelPrincipal;
    private JTextField idFld;
    private JTextField nombreFld;
    private JButton guardar;
    private JButton limpiar;
    private JButton borrar;
    private JTextField nombreBusquedaFld;
    private JButton buscar;
    private JButton reporte;
    private JTable list;

    private ControladorPrincipal controlador;
    private TableModelPrincipal tableModel;
    private boolean modoEdicion = false;
    private String idSeleccionado = null;

    public PanelGestionFarmaceutas(ControladorPrincipal controlador) {
        this.controlador = controlador;
        inicializarComponentes();
        configurarEventos();
        cargarDatos();
    }

    private void inicializarComponentes() {
        tableModel = TableModelPrincipal.crearModeloFarmaceutas();
        list.setModel(tableModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        list.getColumnModel().getColumn(0).setPreferredWidth(80);  // ID
        list.getColumnModel().getColumn(1).setPreferredWidth(200); // Nombre

        list.setRowHeight(25);
        list.getTableHeader().setReorderingAllowed(false);
    }

    private void configurarEventos() {
        guardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarFarmaceuta();
            }
        });

        limpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarCampos();
            }
        });

        borrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarFarmaceuta();
            }
        });

        buscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarFarmaceutas();
            }
        });

        nombreBusquedaFld.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarFarmaceutas();
            }
        });

        reporte.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generarReporte();
            }
        });

        list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    cargarFarmaceutaSeleccionado();
                }
            }
        });
    }

    private void guardarFarmaceuta() {
        try {
            String id = idFld.getText().trim();
            String nombre = nombreFld.getText().trim();

            if (id.isEmpty() || nombre.isEmpty()) {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "Todos los campos son obligatorios",
                        "Datos incompletos",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (modoEdicion) {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "Funcionalidad de edición en desarrollo",
                        "En desarrollo",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                if (controlador.agregarFarmaceuta(id, nombre)) {
                    limpiarCampos();
                    cargarTodosFarmaceutas();
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Error al guardar farmaceuta: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarFarmaceuta() {
        if (idSeleccionado == null) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Seleccione un farmaceuta de la lista para eliminar",
                    "Farmaceuta no seleccionado",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            controlador.eliminarFarmaceuta(idSeleccionado);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Error al eliminar farmaceuta: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarFarmaceutas() {
        String criterio = nombreBusquedaFld.getText().trim();

        try {
            if (criterio.isEmpty()) {
                cargarTodosFarmaceutas();
            } else {
                Farmaceuta farmaceuta = controlador.getModelo().obtenerFarmaceutas().buscarFarmaceutaNombre(criterio);
                Lista<Object> datos = new Lista<>();

                datos.agregarFinal(farmaceuta);

                tableModel.setDatos(datos);

                if (datos.getTam() == 0) {
                    JOptionPane.showMessageDialog(panelPrincipal,
                            "No se encontraron farmaceutas con el criterio: " + criterio,
                            "Sin resultados",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Error al buscar farmaceutas: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarFarmaceutaSeleccionado() {
        int filaSeleccionada = list.getSelectedRow();
        if (filaSeleccionada >= 0) {
            try {
                Object elemento = tableModel.getObjetoEnFila(filaSeleccionada);
                if (elemento instanceof Farmaceuta) {
                    Farmaceuta farmaceuta = (Farmaceuta) elemento;

                    idFld.setText(farmaceuta.getId());
                    nombreFld.setText(farmaceuta.getNombre());

                    idSeleccionado = farmaceuta.getId();
                    modoEdicion = true;

                    idFld.setEnabled(false);
                }
            } catch (Exception e) {
                limpiarCampos();
            }
        }
    }

    private void cargarTodosFarmaceutas() {
        try {
            ListaFarmaceutas farmaceutas = controlador.getModelo().obtenerFarmaceutas();
            Lista<Object> datos = new Lista<>();

            for (Farmaceuta farmaceuta :  farmaceutas) {
                datos.agregarFinal(farmaceuta);
            }

            tableModel.setDatos(datos);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Error al cargar farmaceutas: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        idFld.setText("");
        nombreFld.setText("");
        nombreBusquedaFld.setText("");

        idSeleccionado = null;
        modoEdicion = false;
        idFld.setEnabled(true);

        list.clearSelection();
    }

    private void generarReporte() {
        try {
            String reporte = controlador.generarReporteCompleto();

            JTextArea textArea = new JTextArea(reporte);
            textArea.setEditable(false);
            textArea.setFont(new java.awt.Font("Monospaced", 0, 12));

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new java.awt.Dimension(600, 400));

            JOptionPane.showMessageDialog(panelPrincipal,
                    scrollPane,
                    "Reporte del Sistema",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Error al generar reporte: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarDatos() {
        cargarTodosFarmaceutas();
    }

    public JPanel getPanel() {
        return panelPrincipal;
    }

    public void refrescarDatos() {
        cargarTodosFarmaceutas();
        limpiarCampos();
    }

    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }
}
