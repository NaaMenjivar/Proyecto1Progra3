import logica.entidades.TipoUsuario;
import presentacion.controlador.ControladorLogin;
import presentacion.controlador.ControladorPrincipal;
import presentacion.modelo.ModeloLogin;
import presentacion.vista.principal.VentanaPrincipal;
import presentacion.modelo.ModeloPrincipal;
import presentacion.vista.sistema.VentanaLogin;

import javax.swing.*;

public class Application {

    private static ControladorPrincipal controlador;
    private static VentanaPrincipal ventanaPrincipal;

    /**
     * Método principal que inicia la aplicación
     */
    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("SISTEMA DE PRESCRIPCIÓN Y DESPACHO DE RECETAS");
        System.out.println("Universidad Nacional - EIF206 Programación 3");
        System.out.println("PRUEBA: VENTANA PRINCIPAL");
        System.out.println("=".repeat(60));

        // Configurar Look and Feel del sistema
        configurarLookAndFeel();

        // Inicializar el sistema
        SwingUtilities.invokeLater(() -> {
            try {
                inicializarSistema();

                //agregarDatosListas();
                //mostrarInformacionSistema();
                //abrirVentanaPrincipal();
            } catch (Exception e) {
                mostrarErrorFatal("Error al inicializar el sistema", e);
            }
        });
    }

    /**
     * Configura el Look and Feel de la aplicación
     */
    private static void configurarLookAndFeel() {
        try {
            // Usar el Look and Feel del sistema
            UIManager.setLookAndFeel(UIManager.getLookAndFeel());
            System.out.println("✅ Look and Feel configurado correctamente.");
        } catch (Exception e) {
            System.err.println("⚠️ No se pudo establecer el Look and Feel: " + e.getMessage());
        }
    }

    /**
     * Inicializa todos los componentes del sistema
     */
    private static void inicializarSistema() {
        System.out.println("Inicializando sistema...");

        // Crear controlador principal (que a su vez crea el modelo)
        controlador = new ControladorPrincipal();

        System.out.println("✅ Sistema inicializado correctamente.");
        System.out.println("✅ Controlador está listo para usar.");
    }

    /**
     * Abre la ventana principal para pruebas
     */
    private static void abrirVentanaPrincipal() {
        try {
            System.out.println("Abriendo ventana principal...");

            // Crear la ventana principal pasando el controlador
            TipoUsuario tipo = TipoUsuario.MEDICO;
            ventanaPrincipal = new VentanaPrincipal(controlador,tipo);

            // Configurar la ventana
            configurarVentanaPrincipal();

            // Mostrar la ventana
            ventanaPrincipal.setVisible(true);

            System.out.println("✅ Ventana principal abierta exitosamente!");

        } catch (Exception e) {
            mostrarErrorFatal("Error al abrir ventana principal", e);
        }
    }

    /**
     * Configura propiedades adicionales de la ventana principal
     */
    private static void configurarVentanaPrincipal() {
        if (ventanaPrincipal != null) {
            // Configurar como JFrame si es necesario
            if (ventanaPrincipal instanceof JFrame) {
                JFrame frame = (JFrame) ventanaPrincipal;
                frame.setTitle("Sistema de Recetas - Ventana Principal");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(800, 600);
                frame.setLocationRelativeTo(null); // Centrar en pantalla
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximizar
            }
        }
    }

    /**
     * Muestra información sobre el sistema inicializado
     */
    private static void mostrarInformacionSistema() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("INFORMACIÓN DEL SISTEMA");
        System.out.println("=".repeat(50));

        System.out.println("✅ Sistema iniciado correctamente");
        System.out.println("📋 Patrón MVC implementado:");
        System.out.println("   • Modelo: ModeloPrincipal");
        System.out.println("   • Vista: VentanaPrincipal");
        System.out.println("   • Controlador: ControladorPrincipal");

        System.out.println("\n🏥 FUNCIONALIDADES DISPONIBLES:");
        System.out.println("   • Login de usuarios");
        System.out.println("   • Gestión de médicos (administrador)");
        System.out.println("   • Gestión de farmaceutas (administrador)");
        System.out.println("   • Gestión de pacientes (administrador)");
        System.out.println("   • Catálogo de medicamentos (administrador)");
        System.out.println("   • Despacho de recetas (farmaceutas)");
        // System.out.println("   • Dashboard con indicadores"); // No implementado aún
        System.out.println("   • Histórico de recetas");

        System.out.println("\n🔧 VENTANA DE PRUEBA:");
        System.out.println("   • Se abrirá la VentanaPrincipal");
        System.out.println("   • Podrás navegar por las pestañas");
        System.out.println("   • El dashboard está pendiente de implementación");

        System.out.println("\n📊 ESTADO DEL MODELO:");
        if (controlador != null && controlador.getModelo() != null) {
            ModeloPrincipal modelo = controlador.getModelo();
            System.out.println("   • Total de elementos: " + modelo.getTotalElementos());
            System.out.println("   • Sistema validado: " + (modelo.validar() ? "Sí" : "No"));
            System.out.println("   • Datos modificados: " + (modelo.isDatosModificados() ? "Sí" : "No"));
        }

        System.out.println("\n✅ Listo para probar la interfaz!");
        System.out.println("=".repeat(50));
    }

    /**
     * Muestra un error fatal y termina la aplicación
     */
    private static void mostrarErrorFatal(String mensaje, Exception e) {
        System.err.println("ERROR FATAL: " + mensaje);
        e.printStackTrace();

        String mensajeCompleto = mensaje + "\n\nDetalles técnicos:\n" + e.getMessage() +
                "\n\nRevise que todas las clases estén correctamente compiladas.";

        JOptionPane.showMessageDialog(
                null,
                mensajeCompleto,
                "Error Fatal - Sistema de Recetas",
                JOptionPane.ERROR_MESSAGE
        );

        System.exit(1);
    }

    /**
     * Obtiene el controlador principal del sistema
     */
    public static ControladorPrincipal getControlador() {
        return controlador;
    }

    /**
     * Obtiene la ventana principal
     */
    public static VentanaPrincipal getVentanaPrincipal() {
        return ventanaPrincipal;
    }

    /**
     * Información de la aplicación
     */
    public static void mostrarInformacion() {
        String info = "SISTEMA DE PRESCRIPCIÓN Y DESPACHO DE RECETAS\n\n" +
                "Universidad Nacional\n" +
                "Facultad de Ciencias Exactas y Naturales\n" +
                "Escuela de Informática\n\n" +
                "Curso: EIF206 - Programación 3\n" +
                "Proyecto #1\n\n" +
                "PRUEBA: VENTANA PRINCIPAL\n\n" +
                "Funcionalidades:\n" +
                "✓ Login y cambio de clave\n" +
                "✓ Gestión de médicos (administrador)\n" +
                "✓ Gestión de farmaceutas (administrador)\n" +
                "✓ Gestión de pacientes (administrador)\n" +
                "✓ Catálogo de medicamentos (administrador)\n" +
                "✓ Despacho de recetas (farmaceutas)\n" +
                "• Dashboard con indicadores (pendiente)\n" +
                "✓ Histórico de recetas\n\n" +
                "Arquitectura: Modelo-Vista-Controlador (MVC)\n" +
                "Persistencia: Archivos XML\n" +
                "Interfaz: Swing\n\n" +
                "Clases principales utilizadas:\n" +
                "• ControladorPrincipal\n" +
                "• ModeloPrincipal\n" +
                "• VentanaPrincipal\n" +
                "• TableModelPrincipal";

        JOptionPane.showMessageDialog(
                null,
                info,
                "Acerca del Sistema",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * Método para cerrar la aplicación de forma segura
     */
    public static void cerrarAplicacion() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("CERRANDO APLICACIÓN");
        System.out.println("=".repeat(40));

        if (ventanaPrincipal != null) {
            ventanaPrincipal.setVisible(false);
            ventanaPrincipal.dispose();
            System.out.println("✅ Ventana principal cerrada");
        }

        System.out.println("✅ Aplicación cerrada correctamente");
        System.out.println("¡Gracias por usar el Sistema de Recetas!");

        System.exit(0);
    }

    /**
     * Método para probar que todo funcione correctamente
     */
    public static void probarSistema() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("PRUEBA DEL SISTEMA");
        System.out.println("=".repeat(40));

        if (controlador != null) {
            System.out.println("✅ Controlador inicializado correctamente");

            if (controlador.getModelo() != null) {
                System.out.println("✅ Modelo inicializado correctamente");
                System.out.println("   • Total elementos: " + controlador.getModelo().getTotalElementos());
            }

            if (ventanaPrincipal != null) {
                System.out.println("✅ Ventana principal creada correctamente");
                System.out.println("✅ Lista para interactuar con el usuario");
            }

            System.out.println("✅ Sistema funcionando correctamente");

        } else {
            System.err.println("❌ Error: Controlador no inicializado");
        }

        System.out.println("=".repeat(40));
    }

    public static void agregarDatosListas(){
        controlador.agregarMedico("1234","juan","oculista");
        controlador.agregarMedico("5678","miguel","general");
        controlador.agregarMedico("91011","ricardo","fisioterapeuta");
        controlador.agregarMedico("1213","fiona","familiar");
    }
}