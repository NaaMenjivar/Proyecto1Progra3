package presentacion.vista.administrador;

import presentacion.controlador.ControladorPrincipal;
import presentacion.modelo.TableModelPrincipal;
import logica.entidades.*;
import logica.entidades.lista.Lista;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Panel de Gestión de Médicos - Vista MVC
 */
public class PanelGestionMedicos {
    // Componentes del formulario (declarados en el .form)
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

    // MVC Components
    private ControladorPrincipal controlador;
    private TableModelPrincipal tableModel;
    private boolean modoEdicion = false;
    private String idSeleccionado = null;

    public PanelGestionMedicos(ControladorPrincipal controlador) {
        this.controlador = controlador;
        inicializarComponentes();
        configurarEventos();
        cargarDatos();
    }

    private void inicializarComponentes() {
        // Configurar tabla
        tableModel = TableModelPrincipal.crearModeloMedicos();
        list.setModel(tableModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Configurar columnas
        list.getColumnModel().getColumn(0).setPreferredWidth(80);  // ID
        list.getColumnModel().getColumn(1).setPreferredWidth(200); // Nombre
        list.getColumnModel().getColumn(2).setPreferredWidth(150); // Especialidad

        // Configurar tabla para mejor visualización
        list.setRowHeight(25);
        list.getTableHeader().setReorderingAllowed(false);
    }

    private void configurarEventos() {
        // Botón Guardar
        guardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarMedico();
            }
        });

        // Botón Limpiar
        limpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarCampos();
            }
        });

        // Botón Borrar
        borrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarMedico();
            }
        });

        // Botón Buscar
        buscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarMedicos();
            }
        });

        // Enter en campo de búsqueda
        nombreBusquedaFld.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarMedicos();
            }
        });

        // Botón Reporte
        reporte.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generarReporte();
            }
        });

        // Selección en tabla
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

            if (modoEdicion) {
                // TODO: Implementar actualización cuando esté disponible en el controlador
                JOptionPane.showMessageDialog(panelPrincipal,
                        "Funcionalidad de edición en desarrollo",
                        "En desarrollo",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                if (controlador.agregarMedico(id, nombre, especialidad)) {
                    limpiarCampos();
                    cargarTodosMedicos();
                }
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
                Lista<Medico> medicosEncontrados = controlador.getModelo().buscarMedicosPorNombre(criterio);
                Lista<Object> datos = new Lista<>();

                for (int i = 0; i < medicosEncontrados.getTam(); i++) {
                    datos.agregarFinal(medicosEncontrados.obtenerPorPos(i));
                }

                tableModel.setDatos(datos);

                if (medicosEncontrados.getTam() == 0) {
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
                    modoEdicion = true;

                    // Deshabilitar campo ID en modo edición
                    idFld.setEnabled(false);
                }
            } catch (Exception e) {
                limpiarCampos();
            }
        }
    }

    private void cargarTodosMedicos() {
        try {
            Lista<Medico> medicos = controlador.getModelo().obtenerMedicos();
            Lista<Object> datos = new Lista<>();

            for (int i = 0; i < medicos.getTam(); i++) {
                datos.agregarFinal(medicos.obtenerPorPos(i));
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
        cargarTodosMedicos();
    }

    /**
     * Obtiene el panel principal para ser añadido a contenedores
     * @return JPanel principal del formulario
     */
    public JPanel getPanel() {
        return panelPrincipal;
    }

    /**
     * Método para refrescar datos desde el exterior
     */
    public void refrescarDatos() {
        cargarTodosMedicos();
        limpiarCampos();
    }
}