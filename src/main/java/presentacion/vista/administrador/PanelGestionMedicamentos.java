package presentacion.vista.administrador;

import logica.entidades.lista.CatalogoMedicamentos;
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

/**
 * Panel de Gestión de Medicamentos - Vista MVC
 */
public class PanelGestionMedicamentos {
    // Componentes del formulario (declarados en el .form)
    private JPanel panelPrincipal;
    private JTextField codigoFld;
    private JTextField nombreFld;
    private JTextField presentacionFld;
    private JTextField stockFld;
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
    private String codigoSeleccionado = null;

    public PanelGestionMedicamentos(ControladorPrincipal controlador) {
        this.controlador = controlador;
        inicializarComponentes();
        configurarEventos();
        cargarDatos();
    }

    private void inicializarComponentes() {
        // Configurar tabla
        tableModel = TableModelPrincipal.crearModeloMedicamentos();
        list.setModel(tableModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Configurar columnas
        list.getColumnModel().getColumn(0).setPreferredWidth(80);  // Código
        list.getColumnModel().getColumn(1).setPreferredWidth(200); // Nombre
        list.getColumnModel().getColumn(2).setPreferredWidth(150); // Presentación
        list.getColumnModel().getColumn(3).setPreferredWidth(80);  // Stock

        // Configurar tabla para mejor visualización
        list.setRowHeight(25);
        list.getTableHeader().setReorderingAllowed(false);
    }

    private void configurarEventos() {
        // Botón Guardar
        guardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarMedicamento();
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
                eliminarMedicamento();
            }
        });

        // Botón Buscar
        buscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarMedicamentos();
            }
        });

        // Enter en campo de búsqueda
        nombreBusquedaFld.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarMedicamentos();
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
                    cargarMedicamentoSeleccionado();
                }
            }
        });
    }

    private void guardarMedicamento() {
        try {
            String codigo = codigoFld.getText().trim();
            String nombre = nombreFld.getText().trim();
            String presentacion = presentacionFld.getText().trim();
            String stockTexto = stockFld.getText().trim();

            if (codigo.isEmpty() || nombre.isEmpty()) {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "Código y nombre son obligatorios",
                        "Datos incompletos",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            int stock = 0;
            try {
                stock = Integer.parseInt(stockTexto);
                if (stock < 0) {
                    JOptionPane.showMessageDialog(panelPrincipal,
                            "El stock no puede ser negativo",
                            "Stock inválido",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "El stock debe ser un número válido",
                        "Stock inválido",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (controlador.agregarMedicamento(codigo, nombre, presentacion, stock)) {
                limpiarCampos();
                cargarTodosMedicamentos();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Error al guardar medicamento: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarMedicamento() {
        if (codigoSeleccionado == null) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Seleccione un medicamento de la lista para eliminar",
                    "Medicamento no seleccionado",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            controlador.eliminarMedicamento(codigoSeleccionado);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Error al eliminar medicamento: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarMedicamentos() {
        String criterio = nombreBusquedaFld.getText().trim();

        try {
            if (criterio.isEmpty()) {
                cargarTodosMedicamentos();
            } else {
                Medicamento medicamentosEncontrados = controlador.buscarMedicamentoPorDescripcion(criterio);
                Lista<Object> datos = new Lista<>();

                datos.agregarInicio(medicamentosEncontrados);

                tableModel.setDatos(datos);

                if (medicamentosEncontrados == null) {
                    JOptionPane.showMessageDialog(panelPrincipal,
                            "No se encontraron medicamentos con el criterio: " + criterio,
                            "Sin resultados",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Error al buscar medicamentos: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarMedicamentoSeleccionado() {
        int filaSeleccionada = list.getSelectedRow();
        if (filaSeleccionada >= 0) {
            try {
                Object elemento = tableModel.getObjetoEnFila(filaSeleccionada);
                if (elemento instanceof Medicamento) {
                    Medicamento medicamento = (Medicamento) elemento;

                    codigoFld.setText(medicamento.getCodigo());
                    nombreFld.setText(medicamento.getNombre());
                    presentacionFld.setText(medicamento.getPresentacion());
                    stockFld.setText(String.valueOf(medicamento.getStock()));

                    codigoSeleccionado = medicamento.getCodigo();

                    codigoFld.setEnabled(false);
                }
            } catch (Exception e) {
                limpiarCampos();
            }
        }
    }

    private void cargarTodosMedicamentos() {
        try {
            CatalogoMedicamentos medicamentos = controlador.getModelo().obtenerMedicamentos();
            Lista<Object> datos = new Lista<>();

            for (Medicamento medicamento : medicamentos) {
                datos.agregarFinal(medicamento);
            }

            tableModel.setDatos(datos);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Error al cargar medicamentos: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        codigoFld.setText("");
        nombreFld.setText("");
        presentacionFld.setText("");
        stockFld.setText("0");
        nombreBusquedaFld.setText("");

        codigoSeleccionado = null;
        codigoFld.setEnabled(true);

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
        cargarTodosMedicamentos();
    }

    public JPanel getPanel() {
        return panelPrincipal;
    }

    public void refrescarDatos() {
        cargarTodosMedicamentos();
        limpiarCampos();
    }

    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }
}
