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
import java.time.LocalDate;

/**
 * Panel de Despacho - Vista MVC
 * Implementa el proceso de despacho de recetas según especificaciones del PDF
 */
public class PanelDespacho {
    // Componentes del formulario (declarados en el .form)
    private JPanel panelPrincipal;
    private JTextField idPacienteFld;
    private JButton buscar;
    private JTable table1;
    private JButton cambiarEstado;

    // MVC Components
    private ControladorPrincipal controlador;
    private TableModelPrincipal tableModel;
    private Receta recetaSeleccionada = null;

    public PanelDespacho(ControladorPrincipal controlador) {
        this.controlador = controlador;
        inicializarComponentes();
        configurarEventos();
        cargarRecetasDisponibles();
    }

    private void inicializarComponentes() {
        // Configurar tabla de recetas
        tableModel = TableModelPrincipal.crearModeloRecetas();
        table1.setModel(tableModel);
        table1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Configurar columnas
        table1.getColumnModel().getColumn(0).setPreferredWidth(120); // Número
        table1.getColumnModel().getColumn(1).setPreferredWidth(80);  // ID Paciente
        table1.getColumnModel().getColumn(2).setPreferredWidth(80);  // ID Médico
        table1.getColumnModel().getColumn(3).setPreferredWidth(100); // Fecha
        table1.getColumnModel().getColumn(4).setPreferredWidth(100); // Estado

        // Configurar tabla para mejor visualización
        table1.setRowHeight(25);
        table1.getTableHeader().setReorderingAllowed(false);

        // Deshabilitar botón cambiar estado inicialmente
        cambiarEstado.setEnabled(false);
    }

    private void configurarEventos() {
        // Botón Buscar
        buscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarRecetasPorPaciente();
            }
        });

        // Enter en campo de búsqueda
        idPacienteFld.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarRecetasPorPaciente();
            }
        });

        // Botón Cambiar Estado
        cambiarEstado.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cambiarEstadoReceta();
            }
        });

        // Selección en tabla
        table1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    seleccionarReceta();
                }
            }
        });
    }

    private void buscarRecetasPorPaciente() {
        String idPaciente = idPacienteFld.getText().trim();

        if (idPaciente.isEmpty()) {
            // Si no hay criterio, mostrar recetas disponibles para despacho
            cargarRecetasDisponibles();
            return;
        }

        try {
            // Buscar recetas del paciente que pueden ser despachadas
            Lista<Receta> recetasPaciente = controlador.getModelo().obtenerRecetasPorPaciente(idPaciente);
            Lista<Object> recetasParaDespacho = new Lista<>();

            LocalDate fechaActual = LocalDate.now();

            for (int i = 0; i < recetasPaciente.getTam(); i++) {
                Receta receta = recetasPaciente.obtenerPorPos(i);

                // Según el PDF: recetas "confeccionadas" con fecha de retiro
                // para ese día o a lo sumo tres días anteriores o posteriores
                if (puedeSerDespachada(receta, fechaActual)) {
                    recetasParaDespacho.agregarFinal(receta);
                }
            }

            tableModel.setDatos(recetasParaDespacho);

            if (recetasParaDespacho.getTam() == 0) {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "No hay recetas disponibles para despacho para el paciente: " + idPaciente +
                                "\n(Se muestran solo recetas confeccionadas con fecha de retiro válida)",
                        "Sin recetas para despachar",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "Se encontraron " + recetasParaDespacho.getTam() +
                                " receta(s) para despacho del paciente: " + idPaciente,
                        "Recetas encontradas",
                        JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Error al buscar recetas: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean puedeSerDespachada(Receta receta, LocalDate fechaActual) {
        // Solo recetas confeccionadas, en proceso, listas pueden ser gestionadas
        EstadoReceta estado = receta.getEstado();
        if (estado == EstadoReceta.ENTREGADA) {
            return false; // Ya entregada, no se puede gestionar
        }

        // Verificar fecha de retiro (±3 días según PDF)
        if (receta.getFechaRetiro() != null) {
            long diferenciaDias = java.time.temporal.ChronoUnit.DAYS.between(
                    receta.getFechaRetiro(), fechaActual);
            return Math.abs(diferenciaDias) <= 3;
        }

        return true; // Si no tiene fecha de retiro, permitir gestión
    }

    private void seleccionarReceta() {
        int filaSeleccionada = table1.getSelectedRow();
        if (filaSeleccionada >= 0) {
            try {
                Object elemento = tableModel.getObjetoEnFila(filaSeleccionada);
                if (elemento instanceof Receta) {
                    recetaSeleccionada = (Receta) elemento;

                    // Habilitar botón de cambiar estado
                    cambiarEstado.setEnabled(true);
                    cambiarEstado.setText("Cambiar a " + obtenerSiguienteEstado(recetaSeleccionada.getEstado()));
                }
            } catch (Exception e) {
                recetaSeleccionada = null;
                cambiarEstado.setEnabled(false);
            }
        } else {
            recetaSeleccionada = null;
            cambiarEstado.setEnabled(false);
        }
    }

    private String obtenerSiguienteEstado(EstadoReceta estadoActual) {
        switch (estadoActual) {
            case CONFECCIONADA:
                return "En Proceso";
            case PROCESO:
                return "Lista";
            case LISTA:
                return "Entregada";
            default:
                return "Estado";
        }
    }

    private EstadoReceta obtenerSiguienteEstadoEnum(EstadoReceta estadoActual) {
        switch (estadoActual) {
            case CONFECCIONADA:
                return EstadoReceta.PROCESO;
            case PROCESO:
                return EstadoReceta.LISTA;
            case LISTA:
                return EstadoReceta.ENTREGADA;
            default:
                return estadoActual;
        }
    }

    private void cambiarEstadoReceta() {
        if (recetaSeleccionada == null) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Seleccione una receta de la lista",
                    "Receta no seleccionada",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            EstadoReceta estadoActual = recetaSeleccionada.getEstado();
            EstadoReceta nuevoEstado = obtenerSiguienteEstadoEnum(estadoActual);

            if (estadoActual == nuevoEstado) {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "Esta receta ya está en el estado final",
                        "Sin cambios",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            int confirmacion = JOptionPane.showConfirmDialog(
                    panelPrincipal,
                    "¿Está seguro de cambiar el estado de la receta " + recetaSeleccionada.getNumeroReceta() +
                            "\nde '" + estadoActual.getDescripcion() + "' a '" + nuevoEstado.getDescripcion() + "'?",
                    "Confirmar cambio de estado",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirmacion == JOptionPane.YES_OPTION) {
                if (controlador.cambiarEstadoReceta(recetaSeleccionada.getNumeroReceta(), nuevoEstado)) {
                    // Actualizar la vista
                    buscarRecetasPorPaciente();
                    recetaSeleccionada = null;
                    cambiarEstado.setEnabled(false);
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Error al cambiar estado de receta: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarRecetasDisponibles() {
        try {
            // Cargar todas las recetas que pueden ser despachadas (no entregadas)
            Lista<Receta> todasLasRecetas = controlador.obtenerTodasLasRecetas();
            Lista<Object> recetasParaDespacho = new Lista<>();

            LocalDate fechaActual = LocalDate.now();

            for (int i = 0; i < todasLasRecetas.getTam(); i++) {
                Receta receta = todasLasRecetas.obtenerPorPos(i);
                if (puedeSerDespachada(receta, fechaActual)) {
                    recetasParaDespacho.agregarFinal(receta);
                }
            }

            tableModel.setDatos(recetasParaDespacho);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Error al cargar recetas: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
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
        cargarRecetasDisponibles();
        idPacienteFld.setText("");
        recetaSeleccionada = null;
        cambiarEstado.setEnabled(false);
    }
}