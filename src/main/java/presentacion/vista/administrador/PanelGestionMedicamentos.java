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
    private boolean modoEdicion = false;
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
        list.getColumnModel().getColumn(3).setPreferredWidth(60);  // Stock

        // Configurar tabla para mejor visualización
        list.setRowHeight(25);
        list.getTableHeader().setReorderingAllowed(false);

        // Configurar campo de stock para solo números
        configurarCampoStock();
    }

    private void configurarCampoStock() {
        // Agregar validación para que solo acepte números
        stockFld.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (!Character.isDigit(c) && c != java.awt.event.KeyEvent.VK_BACK_SPACE) {
                    evt.consume();
                }
            }
        });
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
                    seleccionarMedicamento();
                }
            }
        });

        // Enter para buscar
        nombreBusquedaFld.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarMedicamentos();
            }
        });
    }

    private void guardarMedicamento() {
        String codigo = codigoFld.getText().trim();
        String nombre = nombreFld.getText().trim();
        String presentacion = presentacionFld.getText().trim();
        String stockTexto = stockFld.getText().trim();

        // Validaciones básicas en la vista
        if (codigo.isEmpty() || nombre.isEmpty() || presentacion.isEmpty()) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Código, nombre y presentación son obligatorios",
                    "Error de validación",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int stock = 0;
        if (!stockTexto.isEmpty()) {
            try {
                stock = Integer.parseInt(stockTexto);
                if (stock < 0) {
                    JOptionPane.showMessageDialog(panelPrincipal,
                            "El stock no puede ser negativo",
                            "Error de validación",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "El stock debe ser un número válido",
                        "Error de validación",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        if (modoEdicion) {
            // Nota: Implementar actualización cuando esté disponible en el controlador
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Función de actualización no implementada aún",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            if (controlador.agregarMedicamento(codigo, nombre, presentacion, stock)) {
                cargarDatos();
                limpiarCampos();
            }
        }
    }

    private void eliminarMedicamento() {
        if (codigoSeleccionado != null) {
            int confirmacion = JOptionPane.showConfirmDialog(panelPrincipal,
                    "¿Está seguro de eliminar el medicamento con código: " + codigoSeleccionado + "?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION);

            if (confirmacion == JOptionPane.YES_OPTION) {
                // Nota: Implementar eliminación cuando esté disponible en el controlador
                JOptionPane.showMessageDialog(panelPrincipal,
                        "Función de eliminación no implementada aún",
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Seleccione un medicamento para eliminar",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void buscarMedicamentos() {
        String criterio = nombreBusquedaFld.getText().trim();
        if (criterio.isEmpty()) {
            cargarDatos();
        } else {
            // Usar el método de búsqueda del modelo
            Lista<Medicamento> medicamentosEncontrados = controlador.getModelo().buscarMedicamentosPorDescripcion(criterio);
            Lista<Object> datosFiltrados = new Lista<>();

            for (int i = 0; i < medicamentosEncontrados.getTam(); i++) {
                datosFiltrados.agregarFinal(medicamentosEncontrados.obtenerPorPos(i));
            }

            tableModel.setDatos(datosFiltrados);
        }
    }

    private void seleccionarMedicamento() {
        int filaSeleccionada = list.getSelectedRow();
        if (filaSeleccionada >= 0) {
            Object objeto = tableModel.getObjetoEnFila(filaSeleccionada);
            if (objeto instanceof Medicamento) {
                Medicamento medicamento = (Medicamento) objeto;

                codigoFld.setText(medicamento.getCodigo());
                nombreFld.setText(medicamento.getNombre());
                presentacionFld.setText(medicamento.getPresentacion());
                stockFld.setText(String.valueOf(medicamento.getStock()));

                codigoSeleccionado = medicamento.getCodigo();
                modoEdicion = true;
                guardar.setText("Actualizar");

                // Habilitar botón borrar
                borrar.setEnabled(true);
            }
        }
    }

    private void limpiarCampos() {
        codigoFld.setText("");
        nombreFld.setText("");
        presentacionFld.setText("");
        stockFld.setText("0");
        nombreBusquedaFld.setText("");

        codigoSeleccionado = null;
        modoEdicion = false;
        guardar.setText("Guardar");
        borrar.setEnabled(false);
        list.clearSelection();

        // Recargar todos los datos
        cargarDatos();
    }

    private void cargarDatos() {
        Lista<Medicamento> medicamentos = controlador.getModelo().obtenerMedicamentos();
        Lista<Object> datos = new Lista<>();

        for (int i = 0; i < medicamentos.getTam(); i++) {
            datos.agregarFinal(medicamentos.obtenerPorPos(i));
        }

        tableModel.setDatos(datos);
    }

    private void generarReporte() {
        try {
            String reporte = controlador.getModelo().getGestorCatalogos().generarReporteMedicamentos();

            JTextArea textArea = new JTextArea(reporte);
            textArea.setEditable(false);
            textArea.setFont(new java.awt.Font("Courier New", java.awt.Font.PLAIN, 12));

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new java.awt.Dimension(600, 400));

            JOptionPane.showMessageDialog(panelPrincipal, scrollPane,
                    "Reporte de Medicamentos", JOptionPane.INFORMATION_MESSAGE);
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

    // Método para mostrar medicamentos con stock bajo
    public void mostrarMedicamentosBajoStock() {
        try {
            Lista<Medicamento> medicamentosBajoStock = controlador.getModelo()
                    .getGestorCatalogos()
                    .obtenerMedicamentosBajoStock(10);

            if (medicamentosBajoStock.getTam() > 0) {
                StringBuilder mensaje = new StringBuilder();
                mensaje.append("Medicamentos con stock bajo (≤10 unidades):\n\n");

                for (int i = 0; i < medicamentosBajoStock.getTam(); i++) {
                    Medicamento med = medicamentosBajoStock.obtenerPorPos(i);
                    mensaje.append("• ").append(med.getCodigoYNombre())
                            .append(" - Stock: ").append(med.getStock()).append("\n");
                }

                JTextArea textArea = new JTextArea(mensaje.toString());
                textArea.setEditable(false);

                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new java.awt.Dimension(400, 300));

                JOptionPane.showMessageDialog(panelPrincipal, scrollPane,
                        "Alerta de Stock Bajo",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "Todos los medicamentos tienen stock suficiente",
                        "Stock Normal",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Error al verificar stock: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
