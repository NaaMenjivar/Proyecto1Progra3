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
 * Panel Histórico para Administrador - Vista MVC
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
        cargarTodasLasRecetas();
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
        list.getColumnModel().getColumn(4).setPreferredWidth(100); // Estado

        // Configurar tabla para mejor visualización
        list.setRowHeight(25);
        list.getTableHeader().setReorderingAllowed(false);

        // Configurar área de detalles
        detalles.setEditable(false);
        detalles.setLineWrap(true);
        detalles.setWrapStyleWord(true);
        detalles.setFont(new java.awt.Font("Monospaced", 0, 12));
        detalles.setText("Seleccione una receta para ver sus detalles...");
    }

    private void configurarEventos() {
        // Botón Buscar
        buscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarReceta();
            }
        });

        // Enter en campo de búsqueda
        numFld.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarReceta();
            }
        });

        // Selección en tabla
        list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    mostrarDetallesReceta();
                }
            }
        });
    }

    private void buscarReceta() {
        String numeroReceta = numFld.getText().trim();

        try {
            if (numeroReceta.isEmpty()) {
                // Si no hay criterio, mostrar todas las recetas
                cargarTodasLasRecetas();
            } else {
                // Buscar receta específica a través del controlador
                Lista<Receta> recetasEncontradas = controlador.buscarRecetasPorCriterio(numeroReceta);
                Lista<Object> datos = new Lista<>();

                for (int i = 0; i < recetasEncontradas.getTam(); i++) {
                    datos.agregarFinal(recetasEncontradas.obtenerPorPos(i));
                }

                tableModel.setDatos(datos);

                if (recetasEncontradas.getTam() == 0) {
                    JOptionPane.showMessageDialog(panelPrincipal,
                            "No se encontraron recetas con el criterio especificado",
                            "Búsqueda sin resultados",
                            JOptionPane.INFORMATION_MESSAGE);
                    limpiarDetalles();
                } else {
                    limpiarDetalles();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Error al buscar recetas: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarDetallesReceta() {
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

    private void cargarTodasLasRecetas() {
        try {
            Lista<Receta> todasLasRecetas = controlador.obtenerTodasLasRecetas();
            Lista<Object> datos = new Lista<>();

            for (int i = 0; i < todasLasRecetas.getTam(); i++) {
                datos.agregarFinal(todasLasRecetas.obtenerPorPos(i));
            }

            tableModel.setDatos(datos);

            if (todasLasRecetas.getTam() > 0) {
                limpiarDetalles();
            } else {
                detalles.setText("No hay recetas registradas en el sistema.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Error al cargar recetas: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarDetalles() {
        detalles.setText("Seleccione una receta para ver sus detalles...");
        recetaSeleccionada = null;
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
        cargarTodasLasRecetas();
        numFld.setText("");
        recetaSeleccionada = null;
    }

    /**
     * Método para buscar recetas por estado
     */
    public void filtrarPorEstado(EstadoReceta estado) {
        try {
            Lista<Receta> recetasPorEstado = controlador.getModelo().obtenerRecetasPorEstado(estado);
            Lista<Object> datos = new Lista<>();

            for (int i = 0; i < recetasPorEstado.getTam(); i++) {
                datos.agregarFinal(recetasPorEstado.obtenerPorPos(i));
            }

            tableModel.setDatos(datos);
            detalles.setText("Mostrando " + recetasPorEstado.getTam() +
                    " receta(s) en estado: " + estado.getDescripcion() +
                    "\nSeleccione una para ver detalles.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Error al filtrar recetas: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método para buscar recetas por paciente
     */
    public void filtrarPorPaciente(String idPaciente) {
        try {
            Lista<Receta> recetasPaciente = controlador.getModelo().obtenerRecetasPorPaciente(idPaciente);
            Lista<Object> datos = new Lista<>();

            for (int i = 0; i < recetasPaciente.getTam(); i++) {
                datos.agregarFinal(recetasPaciente.obtenerPorPos(i));
            }

            tableModel.setDatos(datos);
            detalles.setText("Mostrando " + recetasPaciente.getTam() +
                    " receta(s) para el paciente: " + idPaciente +
                    "\nSeleccione una para ver detalles.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Error al filtrar recetas por paciente: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
