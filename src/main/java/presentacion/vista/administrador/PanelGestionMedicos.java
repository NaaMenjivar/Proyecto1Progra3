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
                    seleccionarMedico();
                }
            }
        });

        // Enter para buscar
        nombreBusquedaFld.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarMedicos();
            }
        });
    }

    private void guardarMedico() {
        String id = idFld.getText().trim();
        String nombre = nombreFld.getText().trim();
        String especialidad = especialidadFld.getText().trim();

        // Validaciones básicas en la vista
        if (id.isEmpty() || nombre.isEmpty() || especialidad.isEmpty()) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Todos los campos son obligatorios",
                    "Error de validación",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (modoEdicion) {
            if (controlador.actualizarMedico(id, nombre, especialidad)) {
                cargarDatos();
                limpiarCampos();
            }
        } else {
            if (controlador.agregarMedico(id, nombre, especialidad)) {
                cargarDatos();
                limpiarCampos();
            }
        }
    }

    private void eliminarMedico() {
        if (idSeleccionado != null) {
            if (controlador.eliminarMedico(idSeleccionado)) {
                cargarDatos();
                limpiarCampos();
            }
        } else {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Seleccione un médico para eliminar",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void buscarMedicos() {
        String criterio = nombreBusquedaFld.getText().trim();
        if (criterio.isEmpty()) {
            cargarDatos();
        } else {
            // Filtrar datos usando el tableModel
            Lista<Usuario> medicos = controlador.getModelo().obtenerMedicos();
            Lista<Object> datosFiltrados = new Lista<>();

            for (int i = 0; i < medicos.getTam(); i++) {
                Usuario medico = medicos.obtenerPorPos(i);
                if (medico instanceof Medico) {
                    Medico med = (Medico) medico;
                    if (med.getNombre().toLowerCase().contains(criterio.toLowerCase()) ||
                            med.getId().toLowerCase().contains(criterio.toLowerCase()) ||
                            med.getEspecialidad().toLowerCase().contains(criterio.toLowerCase())) {
                        datosFiltrados.agregarFinal(medico);
                    }
                }
            }

            tableModel.setDatos(datosFiltrados);
        }
    }

    private void seleccionarMedico() {
        int filaSeleccionada = list.getSelectedRow();
        if (filaSeleccionada >= 0) {
            Object objeto = tableModel.getObjetoEnFila(filaSeleccionada);
            if (objeto instanceof Medico) {
                Medico medico = (Medico) objeto;

                idFld.setText(medico.getId());
                nombreFld.setText(medico.getNombre());
                especialidadFld.setText(medico.getEspecialidad());

                idSeleccionado = medico.getId();
                modoEdicion = true;
                guardar.setText("Actualizar");

                // Habilitar botón borrar
                borrar.setEnabled(true);
            }
        }
    }

    private void limpiarCampos() {
        idFld.setText("");
        nombreFld.setText("");
        especialidadFld.setText("");
        nombreBusquedaFld.setText("");

        idSeleccionado = null;
        modoEdicion = false;
        guardar.setText("Guardar");
        borrar.setEnabled(false);
        list.clearSelection();

        // Recargar todos los datos
        cargarDatos();
    }

    private void cargarDatos() {
        Lista<Usuario> medicos = controlador.getModelo().obtenerMedicos();
        Lista<Object> datos = new Lista<>();

        for (int i = 0; i < medicos.getTam(); i++) {
            datos.agregarFinal(medicos.obtenerPorPos(i));
        }

        tableModel.setDatos(datos);
    }

    private void generarReporte() {
        try {
            String reporte = controlador.getModelo().getGestorCatalogos().generarReporteMedicos();

            JTextArea textArea = new JTextArea(reporte);
            textArea.setEditable(false);
            textArea.setFont(new java.awt.Font("Courier New", java.awt.Font.PLAIN, 12));

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new java.awt.Dimension(600, 400));

            JOptionPane.showMessageDialog(panelPrincipal, scrollPane,
                    "Reporte de Médicos", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Error al generar reporte: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public JPanel getPanel() {
        return panelPrincipal;
    }

    // Método para refrescar datos desde el exterior
    public void refrescarDatos() {
        cargarDatos();
        limpiarCampos();
    }
}
