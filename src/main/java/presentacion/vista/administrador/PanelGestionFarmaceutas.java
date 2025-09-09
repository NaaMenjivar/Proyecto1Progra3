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
 * Panel de Gestión de Farmaceutas - Vista MVC
 */
public class PanelGestionFarmaceutas {
    // Componentes del formulario (declarados en el .form)
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

    // MVC Components
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
        // Configurar tabla
        tableModel = TableModelPrincipal.crearModeloFarmaceutas();
        list.setModel(tableModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Configurar columnas
        list.getColumnModel().getColumn(0).setPreferredWidth(80);  // ID
        list.getColumnModel().getColumn(1).setPreferredWidth(250); // Nombre

        // Configurar tabla para mejor visualización
        list.setRowHeight(25);
        list.getTableHeader().setReorderingAllowed(false);
    }

    private void configurarEventos() {
        // Botón Guardar
        guardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarFarmaceuta();
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
                eliminarFarmaceuta();
            }
        });

        // Botón Buscar
        buscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarFarmaceutas();
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
                    seleccionarFarmaceuta();
                }
            }
        });

        // Enter para buscar
        nombreBusquedaFld.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarFarmaceutas();
            }
        });
    }

    private void guardarFarmaceuta() {
        String id = idFld.getText().trim();
        String nombre = nombreFld.getText().trim();

        // Validaciones básicas en la vista
        if (id.isEmpty() || nombre.isEmpty()) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "ID y nombre son obligatorios",
                    "Error de validación",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (modoEdicion) {
            // Nota: Implementar actualización cuando esté disponible en el controlador
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Función de actualización no implementada aún",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            if (controlador.agregarFarmaceuta(id, nombre)) {
                cargarDatos();
                limpiarCampos();
            }
        }
    }

    private void eliminarFarmaceuta() {
        if (idSeleccionado != null) {
            // Nota: Implementar eliminación cuando esté disponible en el controlador
            int confirmacion = JOptionPane.showConfirmDialog(panelPrincipal,
                    "¿Está seguro de eliminar el farmaceuta con ID: " + idSeleccionado + "?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION);

            if (confirmacion == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "Función de eliminación no implementada aún",
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Seleccione un farmaceuta para eliminar",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void buscarFarmaceutas() {
        String criterio = nombreBusquedaFld.getText().trim();
        if (criterio.isEmpty()) {
            cargarDatos();
        } else {
            // Filtrar datos
            Lista<Usuario> farmaceutas = controlador.getModelo().obtenerFarmaceutas();
            Lista<Object> datosFiltrados = new Lista<>();

            for (int i = 0; i < farmaceutas.getTam(); i++) {
                Usuario farmaceuta = farmaceutas.obtenerPorPos(i);
                if (farmaceuta instanceof Farmaceuta) {
                    Farmaceuta farm = (Farmaceuta) farmaceuta;
                    if (farm.getNombre().toLowerCase().contains(criterio.toLowerCase()) ||
                            farm.getId().toLowerCase().contains(criterio.toLowerCase())) {
                        datosFiltrados.agregarFinal(farmaceuta);
                    }
                }
            }

            tableModel.setDatos(datosFiltrados);
        }
    }

    private void seleccionarFarmaceuta() {
        int filaSeleccionada = list.getSelectedRow();
        if (filaSeleccionada >= 0) {
            Object objeto = tableModel.getObjetoEnFila(filaSeleccionada);
            if (objeto instanceof Farmaceuta) {
                Farmaceuta farmaceuta = (Farmaceuta) objeto;

                idFld.setText(farmaceuta.getId());
                nombreFld.setText(farmaceuta.getNombre());

                idSeleccionado = farmaceuta.getId();
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
        Lista<Usuario> farmaceutas = controlador.getModelo().obtenerFarmaceutas();
        Lista<Object> datos = new Lista<>();

        for (int i = 0; i < farmaceutas.getTam(); i++) {
            datos.agregarFinal(farmaceutas.obtenerPorPos(i));
        }

        tableModel.setDatos(datos);
    }

    private void generarReporte() {
        try {
            String reporte = controlador.getModelo().getGestorCatalogos().generarReporteGeneral();

            // Filtrar solo la sección de farmaceutas del reporte
            StringBuilder reporteFarmaceutas = new StringBuilder();
            reporteFarmaceutas.append("=== REPORTE DE FARMACEUTAS ===\n\n");
            reporteFarmaceutas.append("Total de farmaceutas: ")
                    .append(controlador.getModelo().getGestorCatalogos().contarFarmaceutas())
                    .append("\n\n");

            Lista<Usuario> farmaceutas = controlador.getModelo().obtenerFarmaceutas();
            if (farmaceutas.getTam() > 0) {
                reporteFarmaceutas.append("Lista detallada:\n");
                for (int i = 0; i < farmaceutas.getTam(); i++) {
                    Usuario farmaceuta = farmaceutas.obtenerPorPos(i);
                    reporteFarmaceutas.append("- ID: ").append(farmaceuta.getId())
                            .append(" | Nombre: ").append(farmaceuta.getNombre())
                            .append("\n");
                }
            } else {
                reporteFarmaceutas.append("No hay farmaceutas registrados en el sistema.\n");
            }

            JTextArea textArea = new JTextArea(reporteFarmaceutas.toString());
            textArea.setEditable(false);
            textArea.setFont(new java.awt.Font("Courier New", java.awt.Font.PLAIN, 12));

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new java.awt.Dimension(600, 400));

            JOptionPane.showMessageDialog(panelPrincipal, scrollPane,
                    "Reporte de Farmaceutas", JOptionPane.INFORMATION_MESSAGE);
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
