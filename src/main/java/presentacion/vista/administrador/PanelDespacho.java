package presentacion.vista.administrador;

import presentacion.controlador.ControladorPrincipal;
import presentacion.modelo.TableModelPrincipal;
import logica.entidades.Receta;
import logica.entidades.EstadoReceta;
import logica.entidades.Paciente;
import logica.entidades.lista.Lista;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanelDespacho {
    private ControladorPrincipal controlador;
    private JPanel panelPrincipal;
    private JTextField txtIdPaciente;
    private JButton buscar;
    private JTable table1;
    private JButton cambiarEstadoBoton;
    private JLabel idPacienteLbl;
    private JPanel panelBusqueda;
    private JPanel panelRecetas;
    private JPanel panelCambiarEstado;

    private TableModelPrincipal tableModel;
    private Lista<Receta> recetasPaciente;
    private Receta recetaSeleccionada;

    public PanelDespacho(ControladorPrincipal controlador) {
        this.controlador = controlador;
        this.recetasPaciente = new Lista<>();
        this.recetaSeleccionada = null;
        inicializar();
    }

    private void inicializar() {
        configurarComponentes();
        configurarEventos();
        cargarTablaVacia();
    }

    private void configurarComponentes() {
        tableModel = TableModelPrincipal.crearModeloRecetas();

        if (table1 != null) {
            table1.setModel(tableModel);
            table1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            table1.getColumnModel().getColumn(0).setPreferredWidth(120);
            table1.getColumnModel().getColumn(1).setPreferredWidth(80);
            table1.getColumnModel().getColumn(2).setPreferredWidth(80);
            table1.getColumnModel().getColumn(3).setPreferredWidth(100);
            table1.getColumnModel().getColumn(4).setPreferredWidth(120);
        }

        if (idPacienteLbl != null) {
            idPacienteLbl.setText("ID Paciente:");
        }

        if (cambiarEstadoBoton != null) {
            cambiarEstadoBoton.setText("Cambiar Estado");
            cambiarEstadoBoton.setEnabled(false);
        }
    }

    private void configurarEventos() {
        if (buscar != null) {
            buscar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    buscarRecetasPaciente();
                }
            });
        }

        if (cambiarEstadoBoton != null) {
            cambiarEstadoBoton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cambiarEstadoReceta();
                }
            });
        }

        if (table1 != null) {
            table1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    if (!e.getValueIsAdjusting()) {
                        seleccionarReceta();
                    }
                }
            });
        }
    }

    private void buscarRecetasPaciente() {
        try {
            String idPaciente = txtIdPaciente.getText().trim();

            if (idPaciente.isEmpty()) {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "Ingrese un ID de paciente",
                        "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!controlador.existePaciente(idPaciente)) {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "Paciente no encontrado: " + idPaciente,
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                cargarTablaVacia();
                return;
            }

            recetasPaciente = controlador.obtenerRecetasPorPaciente(idPaciente);

            if (recetasPaciente.getTam() == 0) {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "No se encontraron recetas para el paciente: " + idPaciente,
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE);
                cargarTablaVacia();
            } else {
                cargarTablaRecetas();
                JOptionPane.showMessageDialog(panelPrincipal,
                        "Se encontraron " + recetasPaciente.getTam() + " receta(s)",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
            }

            recetaSeleccionada = null;
            cambiarEstadoBoton.setEnabled(false);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Error al buscar recetas: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            cargarTablaVacia();
        }
    }

    private void seleccionarReceta() {
        try {
            int filaSeleccionada = table1.getSelectedRow();

            if (filaSeleccionada >= 0 && filaSeleccionada < recetasPaciente.getTam()) {
                recetaSeleccionada = recetasPaciente.obtenerPorPos(filaSeleccionada);

                if (cambiarEstadoBoton != null) {
                    cambiarEstadoBoton.setEnabled(true);
                    String estadoActual = recetaSeleccionada.getEstado().getDescripcion();
                    String siguienteEstado = obtenerSiguienteEstado(recetaSeleccionada.getEstado());
                    cambiarEstadoBoton.setText("Cambiar a: " + siguienteEstado);
                }

                JOptionPane.showMessageDialog(panelPrincipal,
                        "Receta seleccionada: " + recetaSeleccionada.getNumeroReceta() +
                                "\nEstado actual: " + recetaSeleccionada.getEstado().getDescripcion(),
                        "Receta Seleccionada",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                recetaSeleccionada = null;
                if (cambiarEstadoBoton != null) {
                    cambiarEstadoBoton.setEnabled(false);
                    cambiarEstadoBoton.setText("Cambiar Estado");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Error al seleccionar receta: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cambiarEstadoReceta() {
        try {
            if (recetaSeleccionada == null) {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "Seleccione una receta primero",
                        "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            EstadoReceta estadoActual = recetaSeleccionada.getEstado();
            EstadoReceta nuevoEstado = obtenerProximoEstado(estadoActual);

            if (nuevoEstado == estadoActual) {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "La receta ya está en el estado final: " + estadoActual.getDescripcion(),
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            boolean exito = controlador.cambiarEstadoReceta(recetaSeleccionada.getNumeroReceta(), nuevoEstado);

            if (exito) {
                recetaSeleccionada.setEstado(nuevoEstado);
                cargarTablaRecetas();

                String siguienteEstado = obtenerSiguienteEstado(nuevoEstado);
                cambiarEstadoBoton.setText("Cambiar a: " + siguienteEstado);

                JOptionPane.showMessageDialog(panelPrincipal,
                        "Estado cambiado exitosamente a: " + nuevoEstado.getDescripcion(),
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "No se pudo cambiar el estado de la receta",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Error al cambiar estado: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarTablaVacia() {
        Lista<Object> datosVacios = new Lista<>();
        tableModel.setDatos(datosVacios);

        recetasPaciente = new Lista<>();
        recetaSeleccionada = null;

        if (cambiarEstadoBoton != null) {
            cambiarEstadoBoton.setEnabled(false);
            cambiarEstadoBoton.setText("Cambiar Estado");
        }
    }

    private void cargarTablaRecetas() {
        try {
            Lista<Object> datosRecetas = new Lista<>();

            for (int i = 0; i < recetasPaciente.getTam(); i++) {
                Receta receta = recetasPaciente.obtenerPorPos(i);
                datosRecetas.agregarFinal(receta);
            }

            tableModel.setDatos(datosRecetas);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Error al cargar tabla: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            cargarTablaVacia();
        }
    }

    private EstadoReceta obtenerProximoEstado(EstadoReceta estadoActual) {
        switch (estadoActual) {
            case CONFECCIONADA:
                return EstadoReceta.PROCESO;
            case PROCESO:
                return EstadoReceta.LISTA;
            case LISTA:
                return EstadoReceta.ENTREGADA;
            case ENTREGADA:
                return EstadoReceta.ENTREGADA;
            default:
                return estadoActual;
        }
    }

    private String obtenerSiguienteEstado(EstadoReceta estadoActual) {
        EstadoReceta siguiente = obtenerProximoEstado(estadoActual);
        return siguiente == estadoActual ? "Final" : siguiente.getDescripcion();
    }

    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }

    public void refrescarDatos() {
        cargarTablaVacia();
        if (txtIdPaciente != null) {
            txtIdPaciente.setText("");
        }
    }
}