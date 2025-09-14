package presentacion.vista.administrador;

import presentacion.controlador.ControladorPrincipal;
import logica.entidades.Medicamento;
import logica.entidades.Receta;
import logica.entidades.DetalleReceta;
import logica.entidades.EstadoReceta;
import logica.entidades.lista.Lista;
import logica.entidades.lista.CatalogoMedicamentos;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class PanelDashboard {
    private ControladorPrincipal controlador;
    private JPanel panelDashBoard;
    private JPanel panelDatos;
    private JPanel panelGraficoMedicamentos;
    private JPanel panelGraficoRecetas;
    private JTable tablaMedicamentos;
    private JComboBox comboBoxMedicamentos;
    private JButton dobleCheckButton;
    private JButton checkButton;
    private JLabel hastaField;
    private JLabel desdeField;
    private JComboBox comboDesdeAnnio;
    private JComboBox comboHastaAnnio;
    private JComboBox comboDesdeDiaMes;
    private JComboBox comboHastaDiaMes;
    private JPanel panelBotonesInferiores;
    private JButton botonInferior2;
    private JButton botonInferior1;
    private JPanel panelFecha;
    private JPanel panelSeleccionarMedicamentos;
    private JPanel panelTabla;

    private Medicamento medicamentoSeleccionado1;
    private Medicamento medicamentoSeleccionado2;

    public PanelDashboard(ControladorPrincipal controlador) {
        this.controlador = controlador;
        this.medicamentoSeleccionado1 = null;
        this.medicamentoSeleccionado2 = null;
        inicializar();
    }

    private void inicializar() {
        configurarComponentes();
        configurarEventos();
        crearGraficosVacios();
        cargarTablaInicial();
    }

    private void configurarComponentes() {
        if (comboBoxMedicamentos != null) {
            try {
                CatalogoMedicamentos catalogo = controlador.obtenerMedicamentos();
                comboBoxMedicamentos.removeAllItems();
                for (int i = 0; i < catalogo.getTam(); i++) {
                    Medicamento med = catalogo.get(i);
                    comboBoxMedicamentos.addItem(med.getNombre());
                }
            } catch (Exception e) {
                // Error silencioso
            }
        }

        LocalDate hoy = LocalDate.now();
        int anioActual = hoy.getYear();

        if (comboDesdeAnnio != null) {
            comboDesdeAnnio.removeAllItems();
            for (int anio = anioActual - 2; anio <= anioActual; anio++) {
                comboDesdeAnnio.addItem(String.valueOf(anio));
            }
            comboDesdeAnnio.setSelectedItem(String.valueOf(anioActual));
        }

        if (comboHastaAnnio != null) {
            comboHastaAnnio.removeAllItems();
            for (int anio = anioActual - 2; anio <= anioActual; anio++) {
                comboHastaAnnio.addItem(String.valueOf(anio));
            }
            comboHastaAnnio.setSelectedItem(String.valueOf(anioActual));
        }

        String[] meses = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};

        if (comboDesdeDiaMes != null) {
            comboDesdeDiaMes.removeAllItems();
            for (String mes : meses) {
                comboDesdeDiaMes.addItem(mes);
            }
            comboDesdeDiaMes.setSelectedItem("01");
        }

        if (comboHastaDiaMes != null) {
            comboHastaDiaMes.removeAllItems();
            for (String mes : meses) {
                comboHastaDiaMes.addItem(mes);
            }
            comboHastaDiaMes.setSelectedItem("10");
        }

        if (desdeField != null) {
            desdeField.setText("Desde:");
        }
        if (hastaField != null) {
            hastaField.setText("Hasta:");
        }
    }

    private void configurarEventos() {
        if (checkButton != null) {
            checkButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    seleccionarPrimerMedicamento();
                }
            });
        }

        if (dobleCheckButton != null) {
            dobleCheckButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    seleccionarSegundoMedicamento();
                }
            });
        }

        if (botonInferior1 != null) {
            botonInferior1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    quitarPrimerMedicamento();
                }
            });
        }

        if (botonInferior2 != null) {
            botonInferior2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    quitarSegundoMedicamento();
                }
            });
        }
    }

    private void seleccionarPrimerMedicamento() {
        try {
            String nombreSeleccionado = (String) comboBoxMedicamentos.getSelectedItem();
            if (nombreSeleccionado != null) {
                medicamentoSeleccionado1 = buscarMedicamentoPorNombre(nombreSeleccionado);

                if (medicamentoSeleccionado1 != null) {
                    actualizarTabla();
                    actualizarGraficos();

                    JOptionPane.showMessageDialog(panelDashBoard,
                            "Primer medicamento seleccionado: " + medicamentoSeleccionado1.getNombre(),
                            "Información",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelDashBoard,
                    "Error al seleccionar medicamento: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void seleccionarSegundoMedicamento() {
        try {
            String nombreSeleccionado = (String) comboBoxMedicamentos.getSelectedItem();
            if (nombreSeleccionado != null) {
                medicamentoSeleccionado2 = buscarMedicamentoPorNombre(nombreSeleccionado);

                if (medicamentoSeleccionado2 != null) {
                    actualizarTabla();
                    actualizarGraficos();

                    JOptionPane.showMessageDialog(panelDashBoard,
                            "Segundo medicamento seleccionado: " + medicamentoSeleccionado2.getNombre(),
                            "Información",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelDashBoard,
                    "Error al seleccionar medicamento: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void quitarPrimerMedicamento() {
        if (medicamentoSeleccionado1 != null) {
            String nombre = medicamentoSeleccionado1.getNombre();
            medicamentoSeleccionado1 = null;
            actualizarTabla();
            actualizarGraficos();

            JOptionPane.showMessageDialog(panelDashBoard,
                    "Primer medicamento removido: " + nombre,
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(panelDashBoard,
                    "No hay primer medicamento seleccionado",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void quitarSegundoMedicamento() {
        if (medicamentoSeleccionado2 != null) {
            String nombre = medicamentoSeleccionado2.getNombre();
            medicamentoSeleccionado2 = null;
            actualizarTabla();
            actualizarGraficos();

            JOptionPane.showMessageDialog(panelDashBoard,
                    "Segundo medicamento removido: " + nombre,
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(panelDashBoard,
                    "No hay segundo medicamento seleccionado",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void crearGraficosVacios() {
        crearGraficoMedicamentosVacio();
        crearGraficoRecetasVacio();
    }

    private void crearGraficoMedicamentosVacio() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(0, "Seleccione medicamentos", "");

        JFreeChart chart = ChartFactory.createLineChart(
                "Medicamentos",
                "Mes",
                "Cantidad",
                dataset
        );

        if (panelGraficoMedicamentos != null) {
            panelGraficoMedicamentos.removeAll();
            panelGraficoMedicamentos.setLayout(new BorderLayout());
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(400, 300));
            panelGraficoMedicamentos.add(chartPanel, BorderLayout.CENTER);
        }
    }

    private void crearGraficoRecetasVacio() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Sin datos", 1);

        JFreeChart chart = ChartFactory.createPieChart(
                "Recetas",
                dataset,
                true,
                true,
                false
        );

        if (panelGraficoRecetas != null) {
            panelGraficoRecetas.removeAll();
            panelGraficoRecetas.setLayout(new BorderLayout());
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(400, 300));
            panelGraficoRecetas.add(chartPanel, BorderLayout.CENTER);
        }
    }

    private void actualizarGraficos() {
        actualizarGraficoMedicamentos();
        actualizarGraficoRecetas();
    }

    private void actualizarGraficoMedicamentos() {
        try {
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            if (medicamentoSeleccionado1 != null || medicamentoSeleccionado2 != null) {
                Lista<Receta> recetas = controlador.obtenerTodasRecetas();
                String[] meses = obtenerRangoMeses();

                if (medicamentoSeleccionado1 != null) {
                    for (String mes : meses) {
                        int cantidad = contarMedicamentoPorMes(recetas, medicamentoSeleccionado1.getCodigo(), mes);
                        dataset.addValue(cantidad, medicamentoSeleccionado1.getNombre(), mes);
                    }
                }

                if (medicamentoSeleccionado2 != null) {
                    for (String mes : meses) {
                        int cantidad = contarMedicamentoPorMes(recetas, medicamentoSeleccionado2.getCodigo(), mes);
                        dataset.addValue(cantidad, medicamentoSeleccionado2.getNombre(), mes);
                    }
                }
            } else {
                dataset.addValue(0, "Seleccione medicamentos", "");
            }

            JFreeChart chart = ChartFactory.createLineChart(
                    "Medicamentos",
                    "Mes",
                    "Cantidad",
                    dataset
            );

            if (panelGraficoMedicamentos != null) {
                panelGraficoMedicamentos.removeAll();
                panelGraficoMedicamentos.setLayout(new BorderLayout());
                ChartPanel chartPanel = new ChartPanel(chart);
                chartPanel.setPreferredSize(new Dimension(400, 300));
                panelGraficoMedicamentos.add(chartPanel, BorderLayout.CENTER);
                panelGraficoMedicamentos.revalidate();
                panelGraficoMedicamentos.repaint();
            }

        } catch (Exception e) {
            crearGraficoMedicamentosVacio();
        }
    }

    private void actualizarGraficoRecetas() {
        try {
            DefaultPieDataset dataset = new DefaultPieDataset();
            Lista<Receta> recetas = controlador.obtenerTodasRecetas();

            int confeccionadas = 0;
            int enProceso = 0;
            int listas = 0;
            int entregadas = 0;

            for (int i = 0; i < recetas.getTam(); i++) {
                Receta receta = recetas.obtenerPorPos(i);
                EstadoReceta estado = receta.getEstado();

                if (estado == EstadoReceta.CONFECCIONADA) {
                    confeccionadas++;
                } else if (estado == EstadoReceta.PROCESO) {
                    enProceso++;
                } else if (estado == EstadoReceta.LISTA) {
                    listas++;
                } else if (estado == EstadoReceta.ENTREGADA) {
                    entregadas++;
                }
            }

            if (confeccionadas > 0) dataset.setValue("CONFECCIONADA", confeccionadas);
            if (enProceso > 0) dataset.setValue("EN PROCESO", enProceso);
            if (listas > 0) dataset.setValue("LISTA", listas);
            if (entregadas > 0) dataset.setValue("ENTREGADA", entregadas);

            if (dataset.getItemCount() == 0) {
                dataset.setValue("Sin recetas", 1);
            }

            JFreeChart chart = ChartFactory.createPieChart(
                    "Recetas",
                    dataset,
                    true,
                    true,
                    false
            );

            if (panelGraficoRecetas != null) {
                panelGraficoRecetas.removeAll();
                panelGraficoRecetas.setLayout(new BorderLayout());
                ChartPanel chartPanel = new ChartPanel(chart);
                chartPanel.setPreferredSize(new Dimension(400, 300));
                panelGraficoRecetas.add(chartPanel, BorderLayout.CENTER);
                panelGraficoRecetas.revalidate();
                panelGraficoRecetas.repaint();
            }

        } catch (Exception e) {
            crearGraficoRecetasVacio();
        }
    }

    private void cargarTablaInicial() {
        String[] columnas = {"Medicamento"};
        Object[][] datos = {};

        if (tablaMedicamentos != null) {
            tablaMedicamentos.setModel(new DefaultTableModel(datos, columnas));
        }
    }

    private void actualizarTabla() {
        try {
            Lista<Receta> recetas = controlador.obtenerTodasRecetas();
            String[] meses = obtenerRangoMeses();

            String[] columnas = new String[meses.length + 1];
            columnas[0] = "Medicamento";
            for (int i = 0; i < meses.length; i++) {
                columnas[i + 1] = meses[i];
            }

            int numFilas = 0;
            if (medicamentoSeleccionado1 != null) numFilas++;
            if (medicamentoSeleccionado2 != null) numFilas++;

            if (numFilas == 0) {
                cargarTablaInicial();
                return;
            }

            Object[][] datos = new Object[numFilas][columnas.length];
            int fila = 0;

            if (medicamentoSeleccionado1 != null) {
                datos[fila][0] = medicamentoSeleccionado1.getNombre();
                for (int i = 0; i < meses.length; i++) {
                    int cantidad = contarMedicamentoPorMes(recetas, medicamentoSeleccionado1.getCodigo(), meses[i]);
                    datos[fila][i + 1] = cantidad > 0 ? String.valueOf(cantidad) : "";
                }
                fila++;
            }

            if (medicamentoSeleccionado2 != null) {
                datos[fila][0] = medicamentoSeleccionado2.getNombre();
                for (int i = 0; i < meses.length; i++) {
                    int cantidad = contarMedicamentoPorMes(recetas, medicamentoSeleccionado2.getCodigo(), meses[i]);
                    datos[fila][i + 1] = cantidad > 0 ? String.valueOf(cantidad) : "";
                }
            }

            if (tablaMedicamentos != null) {
                tablaMedicamentos.setModel(new javax.swing.table.DefaultTableModel(datos, columnas));
            }

        } catch (Exception e) {
            cargarTablaInicial();
        }
    }

    public JPanel getPanelPrincipal() {
        return panelDashBoard;
    }

    public void refrescarDatos() {
        configurarComponentes();
        actualizarTabla();
        actualizarGraficos();
    }

    private Medicamento buscarMedicamentoPorNombre(String nombre) {
        try {
            CatalogoMedicamentos catalogo = controlador.obtenerMedicamentos();
            for (int i = 0; i < catalogo.getTam(); i++) {
                Medicamento med = catalogo.get(i);
                if (med.getNombre().equals(nombre)) {
                    return med;
                }
            }
        } catch (Exception e) {

        }
        return null;
    }

    private String[] obtenerRangoMeses() {
        String anioDesde = "2025";
        String anioHasta = "2025";
        String mesDesde = "01";
        String mesHasta = "12";

        if (comboDesdeAnnio != null && comboDesdeAnnio.getSelectedItem() != null) {
            anioDesde = comboDesdeAnnio.getSelectedItem().toString();
        }
        if (comboHastaAnnio != null && comboHastaAnnio.getSelectedItem() != null) {
            anioHasta = comboHastaAnnio.getSelectedItem().toString();
        }
        if (comboDesdeDiaMes != null && comboDesdeDiaMes.getSelectedItem() != null) {
            mesDesde = comboDesdeDiaMes.getSelectedItem().toString();
        }
        if (comboHastaDiaMes != null && comboHastaDiaMes.getSelectedItem() != null) {
            mesHasta = comboHastaDiaMes.getSelectedItem().toString();
        }

        Lista<String> mesesRango = new Lista<>();

        try {
            int anioIni = Integer.parseInt(anioDesde);
            int anioFin = Integer.parseInt(anioHasta);
            int mesIni = Integer.parseInt(mesDesde);
            int mesFin = Integer.parseInt(mesHasta);

            if (anioIni == anioFin) {
                for (int mes = mesIni; mes <= mesFin; mes++) {
                    String mesStr = String.format("%02d", mes);
                    mesesRango.agregarFinal(anioIni + "-" + mesStr);
                }
            } else {
                for (int mes = mesIni; mes <= 12; mes++) {
                    String mesStr = String.format("%02d", mes);
                    mesesRango.agregarFinal(anioIni + "-" + mesStr);
                }

                for (int anio = anioIni + 1; anio < anioFin; anio++) {
                    for (int mes = 1; mes <= 12; mes++) {
                        String mesStr = String.format("%02d", mes);
                        mesesRango.agregarFinal(anio + "-" + mesStr);
                    }
                }

                if (anioFin > anioIni) {
                    for (int mes = 1; mes <= mesFin; mes++) {
                        String mesStr = String.format("%02d", mes);
                        mesesRango.agregarFinal(anioFin + "-" + mesStr);
                    }
                }
            }

        } catch (Exception e) {
            mesesRango.agregarFinal("2025-01");
            mesesRango.agregarFinal("2025-02");
            mesesRango.agregarFinal("2025-03");
        }

        String[] resultado = new String[mesesRango.getTam()];
        for (int i = 0; i < mesesRango.getTam(); i++) {
            resultado[i] = mesesRango.obtenerPorPos(i);
        }

        return resultado;
    }

    private int contarMedicamentoPorMes(Lista<Receta> recetas, String codigoMedicamento, String mes) {
        int contador = 0;

        for (int i = 0; i < recetas.getTam(); i++) {
            Receta receta = recetas.obtenerPorPos(i);
            Lista<DetalleReceta> detalles = receta.getDetalles();

            for (int j = 0; j < detalles.getTam(); j++) {
                DetalleReceta detalle = detalles.obtenerPorPos(j);

                if (detalle.getCodigoMedicamento().equals(codigoMedicamento)) {
                    contador += detalle.getCantidad();
                }
            }
        }

        return contador;
    }
}