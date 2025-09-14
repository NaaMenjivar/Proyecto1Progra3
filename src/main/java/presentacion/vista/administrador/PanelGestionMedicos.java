package presentacion.vista.administrador;

import logica.entidades.lista.ListaMedicos;
import presentacion.controlador.ControladorPrincipal;
import presentacion.modelo.TableModelPrincipal;
import logica.entidades.*;
import logica.entidades.lista.Lista;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanelGestionMedicos {
    private JPanel panelPrincipal;
    private JTextField idFld;
    private JTextField especialidadFld;
    private JTextField nombreFld;
    private JButton guardar;
    private JButton limpiar;
    private JButton borrar;
    private JTextField nombreBusquedaFld;
    private JButton buscar;
    private JButton reporte;
    private JTable list;

    private final ControladorPrincipal controlador;
    private TableModelPrincipal tableModel;
    private String idSeleccionado = null;

    public PanelGestionMedicos(ControladorPrincipal controlador) {
        this.controlador = controlador;
        inicializarComponentes();
        configurarEventos();
        cargarDatos();
    }

    private void inicializarComponentes() {
        tableModel = TableModelPrincipal.crearModeloMedicos();
        list.setModel(tableModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        list.getColumnModel().getColumn(0).setPreferredWidth(80);  // ID
        list.getColumnModel().getColumn(1).setPreferredWidth(200); // Nombre
        list.getColumnModel().getColumn(2).setPreferredWidth(150); // Especialidad

        list.setRowHeight(25);
        list.getTableHeader().setReorderingAllowed(false);
    }

    private void configurarEventos() {
        guardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarMedico();
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
                eliminarMedico();
            }
        });

        buscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarMedicos();
            }
        });

        nombreBusquedaFld.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarMedicos();
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
                    cargarMedicoSeleccionado();
                }
            }
        });
    }

    private void guardarMedico() {
        try {
            String id = idFld.getText().trim();
            String nombre = nombreFld.getText().trim();
            String especialidad = especialidadFld.getText().trim();

            if (id.isEmpty() || nombre.isEmpty() || especialidad.isEmpty()) {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "Todos los campos son obligatorios",
                        "Datos incompletos",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (controlador.agregarMedico(id, nombre, especialidad)) {
                limpiarCampos();
                cargarTodosMedicos();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Error al guardar médico: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarMedico() {
        if (idSeleccionado == null) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Seleccione un médico de la lista para eliminar",
                    "Médico no seleccionado",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            if (controlador.eliminarMedico(idSeleccionado)) {
                limpiarCampos();
                cargarTodosMedicos();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Error al eliminar médico: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarMedicos() {
        String criterio = nombreBusquedaFld.getText().trim();

        try {
            if (criterio.isEmpty()) {
                cargarTodosMedicos();
            } else {
                Medico medicosEncontrados = controlador.getModelo().buscarMedicosPorNombre(criterio);
                Lista<Object> datos = new Lista<>();

                datos.agregarFinal(medicosEncontrados);

                tableModel.setDatos(datos);

                if (medicosEncontrados == null) {
                    JOptionPane.showMessageDialog(panelPrincipal,
                            "No se encontraron médicos con el criterio: " + criterio,
                            "Sin resultados",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Error al buscar médicos: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarMedicoSeleccionado() {
        int filaSeleccionada = list.getSelectedRow();
        if (filaSeleccionada >= 0) {
            try {
                Object elemento = tableModel.getObjetoEnFila(filaSeleccionada);
                if (elemento instanceof Medico) {
                    Medico medico = (Medico) elemento;

                    idFld.setText(medico.getId());
                    nombreFld.setText(medico.getNombre());
                    especialidadFld.setText(medico.getEspecialidad());

                    idSeleccionado = medico.getId();

                    idFld.setEnabled(false);
                }
            } catch (Exception e) {
                limpiarCampos();
            }
        }
    }

    private void cargarTodosMedicos() {
        try {
            ListaMedicos medicos = controlador.getModelo().obtenerMedicos();
            Lista<Object> datos = new Lista<>();

            for (Medico medico : medicos) {
                datos.agregarFinal(medico);
            }

            tableModel.setDatos(datos);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Error al cargar médicos: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        idFld.setText("");
        nombreFld.setText("");
        especialidadFld.setText("");
        nombreBusquedaFld.setText("");

        idSeleccionado = null;
        //modoEdicion = false;
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
        cargarTodosMedicos();
    }

    public JPanel getPanel() {
        return panelPrincipal;
    }

    public void refrescarDatos() {
        limpiarCampos();
        cargarTodosMedicos();
    }

    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }
}