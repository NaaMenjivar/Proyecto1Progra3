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
 * Panel Histórico para Médicos - Vista MVC
 */
public class PanelHistorico {
    // Componentes del formulario (declarados en el .form)
    private JPanel panelPrincipal;
    private JTextField numeroRecetaFld;  // CAMPO CORREGIDO - antes faltaba
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
        list.getColumnModel().getColumn(4).setPreferredWidth(80);  // Estado

        // Configurar tabla para mejor visualización
        list.setRowHeight(25);
        list.getTableHeader().setReorderingAllowed(false);

        // Configurar área de detalles
        detalles.setEditable(false);
        detalles.setWrapStyleWord(true);
        detalles.setLineWrap(true);
        detalles.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12));
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
        numeroRecetaFld.addActionListener(new ActionListener() {
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
                    mostrarDetallesRecetaSeleccionada();
                }
            }
        });
    }

    private void buscarReceta() {
        String numeroReceta = numeroRecetaFld.getText().trim();

        if (numeroReceta.isEmpty()) {
            cargarTodasLasRecetas();
            return;
        }

        try {
            Lista<Receta> recetasEncontradas = controlador.getModelo()
                    .getGestorCatalogos().obtenerTodasRecetas();
            Lista<Object> resultados = new Lista<>();

            for (int i = 0; i < recetasEncontradas.getTam(); i++) {
                Receta receta = recetasEncontradas.obtenerPorPos(i);
                if (receta.getNumeroReceta().toLowerCase().contains(numeroReceta.toLowerCase())) {
                    resultados.agregarFinal(receta);
                }
            }

            tableModel.setDatos(resultados);

            if (resultados.getTam() == 0) {
                detalles.setText("No se encontraron recetas que coincidan con: " + numeroReceta);
            } else {
                detalles.setText("Se encontraron " + resultados.getTam() +
                        " receta(s) que coinciden con la búsqueda.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Error al buscar receta: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarDetallesRecetaSeleccionada() {
        int filaSeleccionada = list.getSelectedRow();

        if (filaSeleccionada == -1) {
            recetaSeleccionada = null;
            return;
        }

        try {
            recetaSeleccionada = (Receta) tableModel.getValueAt(filaSeleccionada, -1);
            mostrarDetallesReceta(recetaSeleccionada);
        } catch (Exception e) {
            detalles.setText("Error al cargar detalles de la receta seleccionada.");
        }
    }

    private void mostrarDetallesReceta(Receta receta) {
        if (receta == null) {
            detalles.setText("No se puede mostrar información de la receta.");
            return;
        }

        StringBuilder detallesTexto = new StringBuilder();

        // Información básica de la receta
        detallesTexto.append("INFORMACIÓN DE LA RECETA\n");
        detallesTexto.append("═".repeat(50)).append("\n");
        detallesTexto.append("Número:           ").append(receta.getNumeroReceta()).append("\n");
        detallesTexto.append("Paciente:         ").append(receta.getIdPaciente()).append("\n");
        detallesTexto.append("Médico:           ").append(receta.getIdMedico()).append("\n");
        detallesTexto.append("Fecha Confección: ").append(
                receta.getFechaConfeccion() != null ?
                        receta.getFechaConfeccion().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) :
                        "N/A"
        ).append("\n");
        detallesTexto.append("Fecha Retiro:     ").append(
                receta.getFechaRetiro() != null ?
                        receta.getFechaRetiro().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) :
                        "N/A"
        ).append("\n");
        detallesTexto.append("Estado:           ").append(receta.getEstado().getDescripcion()).append("\n\n");

        // Detalles de medicamentos
        detallesTexto.append("MEDICAMENTOS PRESCRITOS:\n");
        detallesTexto.append("─".repeat(30)).append("\n");

        if (receta.tieneDetalles()) {
            Lista<DetalleReceta> detallesList = receta.getDetalles();

            for (int i = 0; i < detallesList.getTam(); i++) {
                DetalleReceta detalle = detallesList.obtenerPorPos(i);

                detallesTexto.append((i + 1)).append(". ");
                detallesTexto.append("Medicamento: ").append(detalle.getCodigoMedicamento()).append("\n");
                detallesTexto.append("   Cantidad:     ").append(detalle.getCantidadTexto()).append("\n");
                detallesTexto.append("   Duración:     ").append(detalle.getDuracionTexto()).append("\n");
                detallesTexto.append("   Indicaciones: ").append(detalle.getIndicaciones()).append("\n");

                if (i < detallesList.getTam() - 1) {
                    detallesTexto.append("\n");
                }
            }

            detallesTexto.append("\n");
            detallesTexto.append("Total de medicamentos: ").append(receta.getTotalMedicamentos()).append("\n");
        } else {
            detallesTexto.append("   Esta receta no tiene medicamentos registrados.\n\n");
        }

        // Mostrar en el área de texto
        detalles.setText(detallesTexto.toString());
        detalles.setCaretPosition(0); // Ir al inicio del texto
    }

    private void cargarTodasLasRecetas() {
        try {
            Lista<Receta> todasLasRecetas = controlador.getModelo()
                    .getGestorCatalogos().obtenerTodasRecetas();
            Lista<Object> datos = new Lista<>();

            for (int i = 0; i < todasLasRecetas.getTam(); i++) {
                datos.agregarFinal(todasLasRecetas.obtenerPorPos(i));
            }

            tableModel.setDatos(datos);

            if (todasLasRecetas.getTam() > 0) {
                detalles.setText("Se cargaron " + todasLasRecetas.getTam() +
                        " receta(s). Seleccione una para ver detalles.");
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

    public JPanel getPanel() {
        return panelPrincipal;
    }

    public void limpiarBusqueda() {
        numeroRecetaFld.setText("");
        cargarTodasLasRecetas();
    }
}