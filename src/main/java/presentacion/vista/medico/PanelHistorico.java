package presentacion.vista.medico;

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
 * Panel Histórico para Médico - Vista MVC CORRECTA
 * Respeta arquitectura MVC y principios SOLID
 */
public class PanelHistorico {
    // Componentes del formulario (declarados en el .form)
    private JPanel panelPrincipal;
    private JTextField numFld;
    private JButton buscar;
    private JTable list;
    private JTextArea detalles;

    // MVC Components
    private ControladorPrincipal controlador;
    private TableModelPrincipal tableModel;
    private Receta recetaSeleccionada = null;

    public PanelHistorico(ControladorPrincipal controlador) {
        this.controlador = controlador;
        inicializarComponentes();
        configurarEventos();
        cargarRecetasDelMedico();
    }

    private void inicializarComponentes() {
        // Configurar tabla de recetas
        tableModel = TableModelPrincipal.crearModeloRecetas();
        list.setModel(tableModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Configurar columnas
        list.getColumnModel().getColumn(0).setPreferredWidth(100); // Número
        list.getColumnModel().getColumn(1).setPreferredWidth(80);  // ID Paciente
        list.getColumnModel().getColumn(2).setPreferredWidth(80);  // ID Médico
        list.getColumnModel().getColumn(3).setPreferredWidth(100); // Fecha
        list.getColumnModel().getColumn(4).setPreferredWidth(80);  // Estado

        // Configurar tabla para mejor visualización
        list.setRowHeight(25);
        list.getTableHeader().setReorderingAllowed(false);

        // Configurar área de detalles
        detalles.setEditable(false);
        detalles.setLineWrap(true);
        detalles.setWrapStyleWord(true);
        detalles.setFont(new java.awt.Font("Monospaced", 0, 12));
    }

    private void configurarEventos() {
        // Botón Buscar
        buscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarRecetaPorNumero();
            }
        });

        // Enter en campo de búsqueda
        numFld.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarRecetaPorNumero();
            }
        });

        // Selección en tabla
        list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    mostrarDetallesRecetaSeleccionada();
                }
            }
        });
    }

    // ================================
    // MÉTODOS QUE RESPETAN MVC
    // ================================

    private void buscarRecetaPorNumero() {
        String numeroReceta = numFld.getText().trim();

        try {
            Lista<Receta> recetasEncontradas;

            if (numeroReceta.isEmpty()) {
                // Si no hay criterio, cargar todas las recetas del médico
                recetasEncontradas = controlador.obtenerRecetasDelMedicoActual();
            } else {
                // Buscar por número específico a través del controlador
                recetasEncontradas = controlador.buscarRecetasDelMedicoActualPorNumero(numeroReceta);
            }

            // Convertir a datos para tabla
            Lista<Object> datos = new Lista<>();
            for (int i = 0; i < recetasEncontradas.getTam(); i++) {
                datos.agregarFinal(recetasEncontradas.obtenerPorPos(i));
            }

            tableModel.setDatos(datos);

            if (recetasEncontradas.getTam() == 0) {
                String mensaje = numeroReceta.isEmpty() ?
                        "No hay recetas registradas para este médico" :
                        "No se encontraron recetas con ese número";

                JOptionPane.showMessageDialog(panelPrincipal,
                        mensaje,
                        "Sin resultados",
                        JOptionPane.INFORMATION_MESSAGE);
                limpiarDetalles();
            } else {
                limpiarDetalles();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Error al buscar recetas: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarRecetasDelMedico() {
        try {
            // Delegar al controlador la lógica de obtener recetas del médico actual
            Lista<Receta> recetasDelMedico = controlador.obtenerRecetasDelMedicoActual();

            // Convertir a datos para tabla
            Lista<Object> datos = new Lista<>();
            for (int i = 0; i < recetasDelMedico.getTam(); i++) {
                datos.agregarFinal(recetasDelMedico.obtenerPorPos(i));
            }

            tableModel.setDatos(datos);
            limpiarDetalles();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Error al cargar recetas: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarDetallesRecetaSeleccionada() {
        int filaSeleccionada = list.getSelectedRow();
        if (filaSeleccionada >= 0) {
            try {
                Object elemento = tableModel.getObjetoEnFila(filaSeleccionada);
                if (elemento instanceof Receta) {
                    recetaSeleccionada = (Receta) elemento;

                    // Delegar al controlador la generación de detalles
                    String detallesTexto = controlador.obtenerDetallesReceta(recetaSeleccionada);

                    detalles.setText(detallesTexto);
                    detalles.setCaretPosition(0);
                }
            } catch (Exception e) {
                detalles.setText("Error al mostrar detalles: " + e.getMessage());
            }
        } else {
            limpiarDetalles();
        }
    }

    private void limpiarDetalles() {
        detalles.setText("Seleccione una receta para ver sus detalles...");
        recetaSeleccionada = null;
    }

    // ================================
    // MÉTODOS PÚBLICOS PARA INTEGRACIÓN
    // ================================

    /**
     * Obtiene el panel principal para ser añadido a contenedores
     * @return JPanel principal del formulario
     */
    public JPanel getPanel() {
        return panelPrincipal;
    }

    /**
     * Método para refrescar datos
     */
    public void refrescarDatos() {
        cargarRecetasDelMedico();
        numFld.setText("");
    }
}