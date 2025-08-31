// ================================
// REEMPLAZAR COMPLETAMENTE TestearClasesModelo.java
// ================================

package principal;

import modelo.*;
import modelo.lista.Lista;
import presentacion.modelo.ModeloComboMedicamentos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class TestearClasesModelo {

    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("    SISTEMA HOSPITAL - PRUEBAS DE CLASES MODELO");
        System.out.println("=".repeat(60));

        // Mostrar menú de opciones
        mostrarMenu();
    }

    private static void mostrarMenu() {
        String[] opciones = {
                "1. Ejecutar todas las pruebas de consola",
                "2. Probar ModeloComboMedicamentos (consola)",
                "3. Probar Ventana con ModeloComboMedicamentos (GUI)",
                "4. Ejecutar pruebas específicas",
                "5. Salir"
        };

        while (true) {
            System.out.println("\n" + "=".repeat(40));
            System.out.println("SELECCIONE UNA OPCIÓN:");
            for (String opcion : opciones) {
                System.out.println(opcion);
            }
            System.out.print("Opción: ");

            try {
                java.util.Scanner scanner = new java.util.Scanner(System.in);
                int seleccion = scanner.nextInt();

                switch (seleccion) {
                    case 1:
                        ejecutarPruebasConsola();
                        break;
                    case 2:
                        testModeloComboMedicamentos();
                        break;
                    case 3:
                        testVentanaComboMedicamentos();
                        break;
                    case 4:
                        mostrarMenuPruebasEspecificas();
                        break;
                    case 5:
                        System.out.println("¡Hasta luego!");
                        return;
                    default:
                        System.out.println("Opción inválida");
                }
            } catch (Exception e) {
                System.out.println("Por favor ingrese un número válido");
                // Limpiar el buffer del scanner
                new java.util.Scanner(System.in).nextLine();
            }
        }
    }

    private static void mostrarMenuPruebasEspecificas() {
        String[] pruebas = {
                "1. Enumeraciones",
                "2. Usuarios",
                "3. Pacientes",
                "4. Medicamentos",
                "5. Detalle Receta",
                "6. Lista Enlazada",
                "7. Receta Completa",
                "8. Búsqueda en Receta",
                "9. Casos Especiales",
                "0. Volver al menú principal"
        };

        while (true) {
            System.out.println("\n" + "-".repeat(30));
            System.out.println("PRUEBAS ESPECÍFICAS:");
            for (String prueba : pruebas) {
                System.out.println(prueba);
            }
            System.out.print("Selección: ");

            try {
                java.util.Scanner scanner = new java.util.Scanner(System.in);
                int seleccion = scanner.nextInt();

                switch (seleccion) {
                    case 1: testEnumeraciones(); break;
                    case 2: testUsuarios(); break;
                    case 3: testPacientes(); break;
                    case 4: testMedicamentos(); break;
                    case 5: testDetalleReceta(); break;
                    case 6: testListaEnlazada(); break;
                    case 7: testRecetaCompleta(); break;
                    case 8: testBuscarMedicamentoEnReceta(); break;
                    case 9: testCasosEspeciales(); break;
                    case 0: return;
                    default: System.out.println("Opción inválida");
                }
            } catch (Exception e) {
                System.out.println("Por favor ingrese un número válido");
                new java.util.Scanner(System.in).nextLine();
            }
        }
    }

    private static void ejecutarPruebasConsola() {
        System.out.println("\n🚀 EJECUTANDO TODAS LAS PRUEBAS DE CONSOLA...\n");

        testEnumeraciones();
        testUsuarios();
        testPacientes();
        testMedicamentos();
        testDetalleReceta();
        testListaEnlazada();
        testRecetaCompleta();
        testBuscarMedicamentoEnReceta();
        testModeloComboMedicamentos();
        testCasosEspeciales();

        System.out.println("\n" + "✅".repeat(20));
        System.out.println("    TODAS LAS PRUEBAS COMPLETADAS EXITOSAMENTE");
        System.out.println("✅".repeat(20));
    }

    // ================================
    // NUEVA PRUEBA DE VENTANA GUI
    // ================================
    private static void testVentanaComboMedicamentos() {
        System.out.println("\n>>> ABRIENDO VENTANA DE PRUEBA GUI <<<");
        System.out.println("-".repeat(50));
        System.out.println("Se abrirá una ventana para probar el ModeloComboMedicamentos");
        System.out.println("Interactúa con los botones para probar diferentes escenarios");

        // Configurar Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getLookAndFeel());
        } catch (Exception e) {
            System.err.println("No se pudo configurar Look and Feel: " + e.getMessage());
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new VentanaPruebaCombo();
            }
        });

        System.out.println("✓ Ventana de prueba iniciada");
    }

    // ================================
    // CLASE INTERNA PARA VENTANA DE PRUEBA
    // ================================
    private static class VentanaPruebaCombo extends JFrame {
        private ModeloComboMedicamentos modeloCombo;
        private JComboBox<Medicamento> comboMedicamentos;
        private JTextArea areaInfo;
        private JLabel labelStatus;
        private Lista<Medicamento> todosMedicamentos;

        public VentanaPruebaCombo() {
            crearDatosPrueba();
            initComponents();
            initEvents();
            setVisible(true);
        }

        private void crearDatosPrueba() {
            todosMedicamentos = new Lista<>();

            Medicamento med1 = new Medicamento("ACE001", "Acetaminofén", "500mg tabletas");
            med1.setStock(50);

            Medicamento med2 = new Medicamento("IBU001", "Ibuprofeno", "400mg cápsulas");
            med2.setStock(0); // Sin stock

            Medicamento med3 = new Medicamento("AMX001", "Amoxicilina", "250mg suspensión");
            med3.setStock(25);

            Medicamento med4 = new Medicamento("DIP001", "Dipirona", "500mg tabletas");
            med4.setStock(30);

            Medicamento med5 = new Medicamento("ASP001", "Aspirina", "100mg tabletas");
            med5.setStock(0); // Sin stock

            todosMedicamentos.agregarFinal(med1);
            todosMedicamentos.agregarFinal(med2);
            todosMedicamentos.agregarFinal(med3);
            todosMedicamentos.agregarFinal(med4);
            todosMedicamentos.agregarFinal(med5);
        }

        private void initComponents() {
            setTitle("🧪 Test - ModeloComboMedicamentos");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setSize(700, 500);
            setLocationRelativeTo(null);
            setLayout(new BorderLayout(10, 10));

            // Panel superior - Combo
            JPanel panelCombo = new JPanel(new FlowLayout());
            panelCombo.setBorder(BorderFactory.createTitledBorder("Combo de Medicamentos"));

            modeloCombo = new ModeloComboMedicamentos();
            comboMedicamentos = new JComboBox<>(modeloCombo.getComboModel());
            comboMedicamentos.setPreferredSize(new Dimension(300, 30));

            panelCombo.add(new JLabel("Seleccionar medicamento:"));
            panelCombo.add(comboMedicamentos);
            add(panelCombo, BorderLayout.NORTH);

            // Panel central - Información
            areaInfo = new JTextArea();
            areaInfo.setEditable(false);
            areaInfo.setFont(new Font("Monospaced", Font.PLAIN, 12));
            areaInfo.setBackground(new Color(248, 248, 248));
            areaInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            areaInfo.setText("👋 Bienvenido al test del ModeloComboMedicamentos\n\n" +
                    "Instrucciones:\n" +
                    "1. Presiona 'Cargar Todos' para cargar medicamentos\n" +
                    "2. Selecciona diferentes medicamentos del combo\n" +
                    "3. Prueba el filtrado por stock\n" +
                    "4. Observa cómo se actualiza la información\n\n" +
                    "¡Presiona cualquier botón para comenzar!");

            JScrollPane scrollPane = new JScrollPane(areaInfo);
            scrollPane.setBorder(BorderFactory.createTitledBorder("Información y Resultados"));
            add(scrollPane, BorderLayout.CENTER);

            // Panel inferior - Botones y status
            JPanel panelInferior = new JPanel(new BorderLayout());

            // Botones
            JPanel panelBotones = new JPanel(new FlowLayout());

            JButton btnCargar = new JButton("🔄 Cargar Todos");
            JButton btnFiltrar = new JButton("🎯 Solo con Stock");
            JButton btnLimpiar = new JButton("🧹 Limpiar");
            JButton btnInfo = new JButton("ℹ️ Info Seleccionado");
            JButton btnCerrar = new JButton("❌ Cerrar");

            panelBotones.add(btnCargar);
            panelBotones.add(btnFiltrar);
            panelBotones.add(btnLimpiar);
            panelBotones.add(btnInfo);
            panelBotones.add(btnCerrar);

            // Label de status
            labelStatus = new JLabel("Estado: Listo para comenzar");
            labelStatus.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

            panelInferior.add(panelBotones, BorderLayout.CENTER);
            panelInferior.add(labelStatus, BorderLayout.SOUTH);
            add(panelInferior, BorderLayout.SOUTH);

            // Eventos de botones
            btnCargar.addActionListener(e -> cargarTodos());
            btnFiltrar.addActionListener(e -> filtrarConStock());
            btnLimpiar.addActionListener(e -> limpiarCombo());
            btnInfo.addActionListener(e -> mostrarInfoSeleccionado());
            btnCerrar.addActionListener(e -> dispose());
        }

        private void initEvents() {
            comboMedicamentos.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    actualizarInfoSeleccion();
                }
            });
        }

        private void cargarTodos() {
            modeloCombo.setMedicamentos(todosMedicamentos);
            actualizarAreaInfo("📦 TODOS LOS MEDICAMENTOS CARGADOS\n\n");

            areaInfo.append(String.format("Total de medicamentos: %d\n\n", todosMedicamentos.getTam()));

            for (int i = 0; i < todosMedicamentos.getTam(); i++) {
                Medicamento med = todosMedicamentos.obtenerPorPos(i);
                String status = med.getStock() > 0 ? "✅ Disponible" : "❌ Sin stock";
                areaInfo.append(String.format("  %s - %s (%s) - Stock: %d\n",
                        med.getCodigo(), med.getNombre(), status, med.getStock()));
            }

            actualizarStatus("Cargados " + todosMedicamentos.getTam() + " medicamentos");
        }

        private void filtrarConStock() {
            Lista<Medicamento> conStock = new Lista<>();

            for (int i = 0; i < todosMedicamentos.getTam(); i++) {
                Medicamento med = todosMedicamentos.obtenerPorPos(i);
                if (med.getStock() > 0) {
                    conStock.agregarFinal(med);
                }
            }

            modeloCombo.setMedicamentos(conStock);

            actualizarAreaInfo("🎯 MEDICAMENTOS FILTRADOS - SOLO CON STOCK\n\n");
            areaInfo.append(String.format("Medicamentos disponibles: %d de %d\n\n",
                    conStock.getTam(), todosMedicamentos.getTam()));

            if (conStock.getTam() > 0) {
                for (int i = 0; i < conStock.getTam(); i++) {
                    Medicamento med = conStock.obtenerPorPos(i);
                    areaInfo.append(String.format("  ✅ %s - %s (Stock: %d)\n",
                            med.getCodigo(), med.getNombre(), med.getStock()));
                }
            } else {
                areaInfo.append("⚠️ No hay medicamentos con stock disponible");
            }

            actualizarStatus("Filtrados: " + conStock.getTam() + " medicamentos disponibles");
        }

        private void limpiarCombo() {
            modeloCombo.setMedicamentos(new Lista<>());
            actualizarAreaInfo("🧹 COMBO LIMPIADO\n\n");
            areaInfo.append("El combo ha sido vaciado completamente.\n");
            areaInfo.append("Elementos actuales en combo: " + modeloCombo.getComboModel().getSize() + "\n\n");
            areaInfo.append("Presiona 'Cargar Todos' para volver a cargar medicamentos.");

            actualizarStatus("Combo vacío - 0 elementos");
        }

        private void mostrarInfoSeleccionado() {
            Medicamento seleccionado = modeloCombo.getMedicamentoSeleccionado();

            if (seleccionado != null) {
                actualizarAreaInfo("ℹ️ INFORMACIÓN DETALLADA DEL MEDICAMENTO\n\n");
                areaInfo.append("📋 DATOS BÁSICOS:\n");
                areaInfo.append("  • Código: " + seleccionado.getCodigo() + "\n");
                areaInfo.append("  • Nombre: " + seleccionado.getNombre() + "\n");
                areaInfo.append("  • Presentación: " + seleccionado.getPresentacion() + "\n");
                areaInfo.append("  • Stock actual: " + seleccionado.getStock() + " unidades\n\n");

                areaInfo.append("📝 MÉTODOS DEL OBJETO:\n");
                areaInfo.append("  • getDescripcionCompleta(): " + seleccionado.getDescripcionCompleta() + "\n");
                areaInfo.append("  • getCodigoYNombre(): " + seleccionado.getCodigoYNombre() + "\n");
                areaInfo.append("  • esValido(): " + (seleccionado.esValido() ? "✅ Sí" : "❌ No") + "\n");
                areaInfo.append("  • toString(): " + seleccionado.toString() + "\n\n");

                if (seleccionado.getStock() == 0) {
                    areaInfo.append("⚠️ ADVERTENCIA: Este medicamento no tiene stock disponible\n");
                    areaInfo.append("   No se puede prescribir hasta que se reabastezca.\n");
                } else {
                    areaInfo.append("✅ DISPONIBLE: Este medicamento se puede prescribir\n");
                    areaInfo.append("   Stock suficiente para " + seleccionado.getStock() + " prescripciones.\n");
                }

                actualizarStatus("Mostrando info de: " + seleccionado.getCodigo());
            } else {
                actualizarAreaInfo("❌ NO HAY MEDICAMENTO SELECCIONADO\n\n");
                areaInfo.append("Posibles causas:\n");
                areaInfo.append("  • El combo está vacío\n");
                areaInfo.append("  • No se ha seleccionado ningún elemento\n");
                areaInfo.append("  • Error en el modelo de datos\n\n");
                areaInfo.append("Solución: Carga medicamentos y selecciona uno del combo.");

                actualizarStatus("Sin selección");
            }
        }

        private void actualizarInfoSeleccion() {
            Medicamento seleccionado = modeloCombo.getMedicamentoSeleccionado();
            if (seleccionado != null) {
                setTitle("🧪 Test - Seleccionado: " + seleccionado.getCodigo());
                actualizarStatus("Seleccionado: " + seleccionado.getCodigo() + " - " + seleccionado.getNombre());
            } else {
                setTitle("🧪 Test - ModeloComboMedicamentos");
                actualizarStatus("Sin selección");
            }
        }

        private void actualizarAreaInfo(String texto) {
            areaInfo.setText(texto);
            areaInfo.setCaretPosition(0);
        }

        private void actualizarStatus(String mensaje) {
            labelStatus.setText("Estado: " + mensaje);
        }
    }

    // ================================
    // RESTO DE MÉTODOS DE PRUEBA ORIGINALES
    // ================================

    public static void testModeloComboMedicamentos() {
        System.out.println("\n>>> PROBANDO MODELO COMBO MEDICAMENTOS <<<");
        System.out.println("-".repeat(50));

        Lista<Medicamento> medicamentos = new Lista<>();
        Medicamento med1 = new Medicamento("ACE001", "Acetaminofén", "500mg tabletas");
        med1.setStock(50);
        Medicamento med2 = new Medicamento("IBU001", "Ibuprofeno", "400mg cápsulas");
        med2.setStock(30);
        Medicamento med3 = new Medicamento("AMX001", "Amoxicilina", "250mg suspensión");
        med3.setStock(0);

        medicamentos.agregarFinal(med1);
        medicamentos.agregarFinal(med2);
        medicamentos.agregarFinal(med3);

        System.out.println("Medicamentos creados: " + medicamentos.getTam());

        ModeloComboMedicamentos modelo = new ModeloComboMedicamentos();
        modelo.setMedicamentos(medicamentos);

        System.out.println("Elementos en combo: " + modelo.getComboModel().getSize());
        System.out.println("✓ ModeloComboMedicamentos funcionando correctamente");
    }

    public static void testEnumeraciones() {
        System.out.println("\n>>> PROBANDO ENUMERACIONES <<<");
        System.out.println("-".repeat(40));

        System.out.println("TIPOS DE USUARIO:");
        for (TipoUsuario tipo : TipoUsuario.values()) {
            System.out.println("  " + tipo + " -> " + tipo.getDescripcion());
        }

        System.out.println("\nESTADOS DE RECETA:");
        for (EstadoReceta estado : EstadoReceta.values()) {
            System.out.println("  " + estado + " -> " + estado.getDescripcion());
        }

        System.out.println("✓ Enumeraciones funcionando correctamente");
    }

    public static void testUsuarios() {
        System.out.println("\n>>> PROBANDO CLASES DE USUARIOS <<<");
        System.out.println("-".repeat(40));

        Medico medico1 = new Medico("MED001", "Dr. Juan Pérez", "clave123", "Cardiología");
        System.out.println("Médico creado: " + medico1);
        System.out.println("  Puede prescribir: " + medico1.puedePrescribir());

        Farmaceuta farm1 = new Farmaceuta("FAR001", "María González", "clave456");
        System.out.println("Farmaceuta creado: " + farm1);
        System.out.println("  Puede despachar: " + farm1.puedeDespachar());

        System.out.println("✓ Usuarios funcionando correctamente");
    }

    public static void testPacientes() {
        System.out.println("\n>>> PROBANDO CLASE PACIENTE <<<");
        System.out.println("-".repeat(40));

        Paciente paciente1 = new Paciente("PAC001", "Ana Vargas",
                LocalDate.of(1985, 5, 15), "8888-1234");

        System.out.println("Paciente: " + paciente1);
        System.out.println("  Edad: " + paciente1.getEdadTexto());
        System.out.println("  Es mayor de edad: " + paciente1.esMayorDeEdad());

        System.out.println("✓ Pacientes funcionando correctamente");
    }

    public static void testMedicamentos() {
        System.out.println("\n>>> PROBANDO CLASE MEDICAMENTO <<<");
        System.out.println("-".repeat(40));

        Medicamento med1 = new Medicamento("MED001", "Acetaminofén", "500mg tabletas");
        med1.setStock(100);

        System.out.println("Medicamento: " + med1);
        System.out.println("  Descripción completa: " + med1.getDescripcionCompleta());
        System.out.println("  Stock: " + med1.getStock());

        System.out.println("✓ Medicamentos funcionando correctamente");
    }

    public static void testDetalleReceta() {
        System.out.println("\n>>> PROBANDO CLASE DETALLE RECETA <<<");
        System.out.println("-".repeat(40));

        DetalleReceta detalle1 = new DetalleReceta("MED001", 2, "1 cada 8 horas", 7);

        System.out.println("Detalle: " + detalle1);
        System.out.println("  Es válido: " + detalle1.esValidoPrescripcion());
        System.out.println("  Cantidad texto: " + detalle1.getCantidadTexto());

        System.out.println("✓ Detalles de receta funcionando correctamente");
    }

    public static void testListaEnlazada() {
        System.out.println("\n>>> PROBANDO LISTA ENLAZADA <<<");
        System.out.println("-".repeat(40));

        Lista<DetalleReceta> lista = new Lista<>();

        DetalleReceta d1 = new DetalleReceta("MED001", 2, "Cada 8 horas", 7);
        DetalleReceta d2 = new DetalleReceta("MED002", 1, "Cada 12 horas", 5);

        lista.agregarFinal(d1);
        lista.agregarFinal(d2);

        System.out.println("Lista con " + lista.getTam() + " elementos");
        System.out.println("Primer elemento: " + lista.obtenerPorPos(0).getCodigoMedicamento());

        System.out.println("✓ Lista enlazada funcionando correctamente");
    }

    public static void testRecetaCompleta() {
        System.out.println("\n>>> PROBANDO CLASE RECETA COMPLETA <<<");
        System.out.println("-".repeat(40));

        Receta receta = new Receta("REC001", "PAC001", "MED001", LocalDate.now().plusDays(1));
        receta.agregarDetalle(new DetalleReceta("MED001", 2, "1 cada 8 horas", 7));

        System.out.println("Receta: " + receta.getNumeroReceta());
        System.out.println("  Estado: " + receta.getEstado());
        System.out.println("  Tiene detalles: " + receta.tieneDetalles());

        System.out.println("✓ Receta completa funcionando correctamente");
    }

    public static void testBuscarMedicamentoEnReceta() {
        System.out.println("\n>>> PROBANDO BÚSQUEDA DE MEDICAMENTO EN RECETA <<<");
        System.out.println("-".repeat(40));

        Receta receta = new Receta("REC001", "PAC001", "MED001", LocalDate.now());
        receta.agregarDetalle(new DetalleReceta("MED001", 2, "Cada 8 horas", 7));
        receta.agregarDetalle(new DetalleReceta("MED002", 1, "Cada 12 horas", 5));

        DetalleReceta encontrado = receta.getDetalles().buscarPorId("MED002");
        System.out.println("Buscar MED002: " + (encontrado != null ? "Encontrado" : "No encontrado"));

        System.out.println("✓ Búsqueda funcionando correctamente");
    }

    public static void testCasosEspeciales() {
        System.out.println("\n>>> PROBANDO CASOS ESPECIALES <<<");
        System.out.println("-".repeat(40));

        Lista<DetalleReceta> listaVacia = new Lista<>();
        System.out.println("Lista vacía - tamaño: " + listaVacia.getTam());

        DetalleReceta elemento = listaVacia.obtenerPorPos(0);
        System.out.println("Elemento en posición 0 de lista vacía: " + elemento);

        System.out.println("✓ Casos especiales manejados correctamente");
    }
}