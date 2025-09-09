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
        detalles.setFont(new java.awt.Font("Courier New", java.awt.Font.PLAIN, 12));
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

        if (numeroReceta.isEmpty()) {
            // Si no hay criterio, mostrar todas las recetas
            cargarTodasLasRecetas();
        } else {
            // Buscar receta específica
            Lista<Receta> todasLasRecetas = obtenerTodasLasRecetas();
            Lista<Object> recetasFiltradas = new Lista<>();

            for (int i = 0; i < todasLasRecetas.getTam(); i++) {
                Receta receta = todasLasRecetas.obtenerPorPos(i);
                if (receta.getNumeroReceta().toLowerCase().contains(numeroReceta.toLowerCase()) ||
                        receta.getIdPaciente().toLowerCase().contains(numeroReceta.toLowerCase()) ||
                        receta.getIdMedico().toLowerCase().contains(numeroReceta.toLowerCase())) {
                    recetasFiltradas.agregarFinal(receta);
                }
            }

            tableModel.setDatos(recetasFiltradas);

            if (recetasFiltradas.getTam() == 0) {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "No se encontraron recetas con el criterio especificado",
                        "Búsqueda sin resultados",
                        JOptionPane.INFORMATION_MESSAGE);
                detalles.setText("No se encontraron recetas...");
            } else {
                detalles.setText("Se encontraron " + recetasFiltradas.getTam() + " receta(s). Seleccione una para ver detalles.");
            }
        }
    }

    private void mostrarDetallesReceta() {
        int filaSeleccionada = list.getSelectedRow();
        if (filaSeleccionada >= 0) {
            Object objeto = tableModel.getObjetoEnFila(filaSeleccionada);
            if (objeto instanceof Receta) {
                recetaSeleccionada = (Receta) objeto;
                mostrarDetallesCompletos(recetaSeleccionada);
            }
        }
    }

    private void mostrarDetallesCompletos(Receta receta) {
        StringBuilder detallesTexto = new StringBuilder();

        // Encabezado de la receta
        detallesTexto.append("═══════════════════════════════════════════════════════════\n");
        detallesTexto.append("                    DETALLES DE RECETA                     \n");
        detallesTexto.append("═══════════════════════════════════════════════════════════\n\n");

        // Información básica
        detallesTexto.append("Número de Receta: ").append(receta.getNumeroReceta()).append("\n");
        detallesTexto.append("ID Paciente:      ").append(receta.getIdPaciente()).append("\n");
        detallesTexto.append("ID Médico:        ").append(receta.getIdMedico()).append("\n");
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

        // Información del paciente (si está disponible)
        try {
            Paciente paciente = controlador.getModelo().obtenerPacientes().buscarPorId(receta.getIdPaciente());
            if (paciente != null) {
                detallesTexto.append("INFORMACIÓN DEL PACIENTE:\n");
                detallesTexto.append("─────────────────────────\n");
                detallesTexto.append("Nombre:           ").append(paciente.getNombre()).append("\n");
                detallesTexto.append("Fecha Nacimiento: ").append(paciente.getFechaNacimientoTexto()).append("\n");
                detallesTexto.append("Edad:             ").append(paciente.getEdadTexto()).append("\n");
                detallesTexto.append("Teléfono:         ").append(paciente.getTelefono()).append("\n\n");
            }
        } catch (Exception e) {
            // Si no se puede obtener información del paciente, continuar
        }

        // Información del médico (si está disponible)
        try {
            Lista<Usuario> medicos = controlador.getModelo().obtenerMedicos();
            Usuario medico = medicos.buscarPorId(receta.getIdMedico());
            if (medico instanceof Medico) {
                Medico med = (Medico) medico;
                detallesTexto.append("INFORMACIÓN DEL MÉDICO:\n");
                detallesTexto.append("─────────────────────────\n");
                detallesTexto.append("Nombre:           ").append(med.getNombre()).append("\n");
                detallesTexto.append("Especialidad:     ").append(med.getEspecialidad()).append("\n\n");
            }
        } catch (Exception e) {
            // Si no se puede obtener información del médico, continuar
        }

        // Detalles de medicamentos
        detallesTexto.append("MEDICAMENTOS PRESCRITOS:\n");
        detallesTexto.append("─────────────────────────\n");

        if (receta.tieneDetalles()) {
            Lista<DetalleReceta> detallesList = receta.getDetalles();

            for (int i = 0; i < detallesList.getTam(); i++) {
                DetalleReceta detalle = detallesList.obtenerPorPos(i);

                detallesTexto.append((i + 1)).append(". ");

                // Obtener información del medicamento
                try {
                    Medicamento medicamento = controlador.getModelo().obtenerMedicamentos()
                            .buscarPorId(detalle.getCodigoMedicamento());
                    if (medicamento != null) {
                        detallesTexto.append(medicamento.getNombre())
                                .append(" (").append(medicamento.getPresentacion()).append(")\n");
                        detallesTexto.append("   Código:       ").append(detalle.getCodigoMedicamento()).append("\n");
                    } else {
                        detallesTexto.append("Medicamento: ").append(detalle.getCodigoMedicamento()).append("\n");
                    }
                } catch (Exception e) {
                    detallesTexto.append("Medicamento: ").append(detalle.getCodigoMedicamento()).append("\n");
                }

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

        // Resumen final
        detallesTexto.append("═══════════════════════════════════════════════════════════\n");
        detallesTexto.append("Resumen: ").append(receta.getResumenMedicamentos()).append("\n");
        detallesTexto.append("═══════════════════════════════════════════════════════════");

        // Mostrar en el área de texto
        detalles.setText(detallesTexto.toString());
        detalles.setCaretPosition(0); // Ir al inicio del texto
    }

    private void cargarTodasLasRecetas() {
        Lista<Receta> todasLasRecetas = obtenerTodasLasRecetas();
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
    }

    private Lista<Receta> obtenerTodasLasRecetas() {
        try {
            return controlador.getModelo().getGestorCatalogos().obtenerTodasRecetas();
        } catch (Exception e) {
            return new Lista<Receta>();
        }
    }

    public JPanel getPanel() {
        return panelPrincipal;
    }

    // Método para refrescar datos desde el exterior
    public void refrescarDatos() {
        cargarTodasLasRecetas();
        numFld.setText("");
        recetaSeleccionada = null;
    }

    // Método para buscar recetas por estado
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

    // Método para buscar recetas por paciente
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
