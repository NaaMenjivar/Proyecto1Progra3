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
            // Buscar paciente primero para validar que existe
            Paciente paciente = controlador.getModelo().obtenerPacientes().buscarPorId(idPaciente);
            if (paciente == null) {
                JOptionPane.showMessageDialog(null,
                        "No se encontró un paciente con ID: " + idPaciente,
                        "Paciente no encontrado",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

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
                JOptionPane.showMessageDialog(null,
                        "No hay recetas disponibles para despacho para el paciente: " + paciente.getNombre() +
                                "\n(Se muestran solo recetas confeccionadas con fecha de retiro válida)",
                        "Sin recetas para despachar",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null,
                        "Se encontraron " + recetasParaDespacho.getTam() +
                                " receta(s) para despacho del paciente: " + paciente.getNombre(),
                        "Recetas encontradas",
                        JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
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
            Object objeto = tableModel.getObjetoEnFila(filaSeleccionada);
            if (objeto instanceof Receta) {
                recetaSeleccionada = (Receta) objeto;

                // Habilitar botón de cambiar estado
                cambiarEstado.setEnabled(true);

                // Actualizar texto del botón según estado actual
                actualizarTextoBotonCambiarEstado();

                // Mostrar información de la receta seleccionada
                mostrarInformacionReceta(recetaSeleccionada);
            }
        } else {
            recetaSeleccionada = null;
            cambiarEstado.setEnabled(false);
            cambiarEstado.setText("Cambiar Estado");
        }
    }

    private void actualizarTextoBotonCambiarEstado() {
        if (recetaSeleccionada == null) {
            cambiarEstado.setText("Cambiar Estado");
            return;
        }

        EstadoReceta estadoActual = recetaSeleccionada.getEstado();

        switch (estadoActual) {
            case CONFECCIONADA:
                cambiarEstado.setText("Poner en Proceso");
                break;
            case PROCESO:
                cambiarEstado.setText("Marcar como Lista");
                break;
            case LISTA:
                cambiarEstado.setText("Entregar");
                break;
            case ENTREGADA:
                cambiarEstado.setText("Ya Entregada");
                cambiarEstado.setEnabled(false);
                break;
            default:
                cambiarEstado.setText("Cambiar Estado");
                break;
        }
    }

    private void cambiarEstadoReceta() {
        if (recetaSeleccionada == null) {
            return;
        }

        // Validar permisos
        if (!controlador.getModelo().puedeDespachar()) {
            JOptionPane.showMessageDialog(null,
                    "No tiene permisos para despachar recetas",
                    "Acceso denegado",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        EstadoReceta estadoActual = recetaSeleccionada.getEstado();
        EstadoReceta nuevoEstado = null;
        String mensaje = "";

        // Determinar siguiente estado según el flujo del PDF
        switch (estadoActual) {
            case CONFECCIONADA:
                nuevoEstado = EstadoReceta.PROCESO;
                mensaje = "¿Desea poner la receta en proceso?\n" +
                        "El farmaceuta iniciará la preparación de medicamentos.";
                break;
            case PROCESO:
                nuevoEstado = EstadoReceta.LISTA;
                mensaje = "¿Desea marcar la receta como lista?\n" +
                        "Los medicamentos han sido alistados y están listos para entrega.";
                break;
            case LISTA:
                nuevoEstado = EstadoReceta.ENTREGADA;
                mensaje = "¿Desea marcar la receta como entregada?\n" +
                        "El paciente ha retirado todos los medicamentos.";
                break;
            case ENTREGADA:
                JOptionPane.showMessageDialog(null,
                        "Esta receta ya ha sido entregada",
                        "Receta ya procesada",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
        }

        if (nuevoEstado != null) {
            int confirmacion = JOptionPane.showConfirmDialog(null,
                    mensaje,
                    "Confirmar cambio de estado",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (confirmacion == JOptionPane.YES_OPTION) {
                if (controlador.cambiarEstadoReceta(recetaSeleccionada.getNumeroReceta(), nuevoEstado)) {
                    // Actualizar la receta en la tabla
                    recetaSeleccionada.setEstado(nuevoEstado);

                    // Refrescar la tabla
                    table1.repaint();

                    // Actualizar botón
                    actualizarTextoBotonCambiarEstado();

                    // Mostrar mensaje de éxito
                    mostrarMensajeExitoso(estadoActual, nuevoEstado);
                }
            }
        }
    }

    private void mostrarMensajeExitoso(EstadoReceta estadoAnterior, EstadoReceta nuevoEstado) {
        String mensaje = "";

        switch (nuevoEstado) {
            case PROCESO:
                mensaje = "✅ Receta puesta en proceso exitosamente.\n" +
                        "El farmaceuta puede proceder a alistar los medicamentos.";
                break;
            case LISTA:
                mensaje = "✅ Receta marcada como lista exitosamente.\n" +
                        "Los medicamentos están listos para entrega al paciente.";
                break;
            case ENTREGADA:
                mensaje = "✅ Receta entregada exitosamente.\n" +
                        "El proceso de despacho ha sido completado.";
                break;
        }

        JOptionPane.showMessageDialog(null, mensaje,
                "Estado actualizado",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarInformacionReceta(Receta receta) {
        try {
            // Obtener información del paciente
            Paciente paciente = controlador.getModelo().obtenerPacientes().buscarPorId(receta.getIdPaciente());
            String nombrePaciente = paciente != null ? paciente.getNombre() : "Paciente no encontrado";

            // Obtener información del médico
            Lista<Usuario> medicos = controlador.getModelo().obtenerMedicos();
            Usuario medico = medicos.buscarPorId(receta.getIdMedico());
            String nombreMedico = medico != null ? medico.getNombre() : "Médico no encontrado";

            String mensaje = String.format(
                    "Receta seleccionada:\n\n" +
                            "Número: %s\n" +
                            "Paciente: %s (%s)\n" +
                            "Médico: %s (%s)\n" +
                            "Estado actual: %s\n" +
                            "Fecha de retiro: %s\n" +
                            "Total medicamentos: %d",
                    receta.getNumeroReceta(),
                    nombrePaciente, receta.getIdPaciente(),
                    nombreMedico, receta.getIdMedico(),
                    receta.getEstado().getDescripcion(),
                    receta.getFechaRetiro() != null ?
                            receta.getFechaRetiro().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) :
                            "No especificada",
                    receta.getTotalMedicamentos()
            );

            // Mostrar mensaje con timeout
            Timer timer = new Timer(4000, e -> {
                // El mensaje se cierra automáticamente
            });
            timer.setRepeats(false);

            JOptionPane optionPane = new JOptionPane(mensaje, JOptionPane.INFORMATION_MESSAGE);
            JDialog dialog = optionPane.createDialog("Información de Receta");
            timer.start();

            // Cerrar diálogo cuando expire el timer
            timer.addActionListener(e -> dialog.dispose());

            dialog.setVisible(true);

        } catch (Exception e) {
            // Silenciar errores de información adicional
        }
    }

    private void cargarRecetasDisponibles() {
        try {
            // Cargar todas las recetas que no están entregadas
            Lista<Receta> todasRecetas = controlador.getModelo().getGestorCatalogos().obtenerTodasRecetas();
            Lista<Object> recetasDisponibles = new Lista<>();

            LocalDate fechaActual = LocalDate.now();

            for (int i = 0; i < todasRecetas.getTam(); i++) {
                Receta receta = todasRecetas.obtenerPorPos(i);

                // Incluir recetas que pueden ser gestionadas en el despacho
                if (receta.getEstado() != EstadoReceta.ENTREGADA &&
                        puedeSerDespachada(receta, fechaActual)) {
                    recetasDisponibles.agregarFinal(receta);
                }
            }

            tableModel.setDatos(recetasDisponibles);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error al cargar recetas: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public JPanel getPanel() {
        // Nota: Este método retornaría el panel principal si estuviera declarado
        // Como no está en el .form visible, necesitarías agregarlo
        return null; // Temporal hasta que se declare panelPrincipal en el .form
    }

    // Método para refrescar datos desde el exterior
    public void refrescarDatos() {
        cargarRecetasDisponibles();
        idPacienteFld.setText("");
        recetaSeleccionada = null;
        cambiarEstado.setEnabled(false);
        cambiarEstado.setText("Cambiar Estado");
    }

    // Método para obtener estadísticas de despacho
    public void mostrarEstadisticasDespacho() {
        try {
            Lista<Receta> todasRecetas = controlador.getModelo().getGestorCatalogos().obtenerTodasRecetas();

            int confeccionadas = 0, enProceso = 0, listas = 0, entregadas = 0;

            for (int i = 0; i < todasRecetas.getTam(); i++) {
                Receta receta = todasRecetas.obtenerPorPos(i);
                switch (receta.getEstado()) {
                    case CONFECCIONADA: confeccionadas++; break;
                    case PROCESO: enProceso++; break;
                    case LISTA: listas++; break;
                    case ENTREGADA: entregadas++; break;
                }
            }

            String estadisticas = String.format(
                    "ESTADÍSTICAS DE DESPACHO\n\n" +
                            "📋 Confeccionadas: %d\n" +
                            "⚙️ En proceso: %d\n" +
                            "✅ Listas: %d\n" +
                            "📦 Entregadas: %d\n\n" +
                            "Total: %d recetas",
                    confeccionadas, enProceso, listas, entregadas,
                    todasRecetas.getTam()
            );

            JOptionPane.showMessageDialog(null, estadisticas,
                    "Estadísticas de Despacho",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error al generar estadísticas: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}