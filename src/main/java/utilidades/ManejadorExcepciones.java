package utilidades;

import java.time.LocalDateTime;
import java.io.FileWriter;
import java.io.IOException;

public class ManejadorExcepciones {
    private static final String ARCHIVO_LOG = "logs/sistema_errores.log";

    public static void logError(String origen, Exception e) {
        String mensaje = String.format("[%s] ERROR en %s: %s",
                FormateadorFechas.formatearFechaHora(LocalDateTime.now()),
                origen,
                e.getMessage());

        System.err.println(mensaje);
        escribirEnArchivo(mensaje);

        if (e.getCause() != null) {
            System.err.println("Causa: " + e.getCause().getMessage());
        }
    }

    public static void logInfo(String origen, String mensaje) {
        String mensajeCompleto = String.format("[%s] INFO en %s: %s",
                FormateadorFechas.formatearFechaHora(LocalDateTime.now()),
                origen,
                mensaje);

        System.out.println(mensajeCompleto);
        escribirEnArchivo(mensajeCompleto);
    }

    public static void logWarning(String origen, String mensaje) {
        String mensajeCompleto = String.format("[%s] WARNING en %s: %s",
                FormateadorFechas.formatearFechaHora(LocalDateTime.now()),
                origen,
                mensaje);

        System.out.println(mensajeCompleto);
        escribirEnArchivo(mensajeCompleto);
    }

    private static void escribirEnArchivo(String mensaje) {
        try {
            // Crear directorio si no existe
            java.io.File archivo = new java.io.File(ARCHIVO_LOG);
            archivo.getParentFile().mkdirs();

            try (FileWriter writer = new FileWriter(archivo, true)) {
                writer.write(mensaje + System.lineSeparator());
            }
        } catch (IOException e) {
            System.err.println("No se pudo escribir en el archivo de log: " + e.getMessage());
        }
    }

    public static String obtenerMensajeAmigable(Exception e) {
        if (e instanceof logica.excepciones.AutenticacionException) {
            return "Error de autenticación: " + e.getMessage();
        } else if (e instanceof logica.excepciones.PrescripcionException) {
            return "Error en prescripción: " + e.getMessage();
        } else if (e instanceof logica.excepciones.DespachoException) {
            return "Error en despacho: " + e.getMessage();
        } else if (e instanceof logica.excepciones.CatalogoException) {
            return "Error en catálogo: " + e.getMessage();
        } else {
            return "Error del sistema: " + e.getMessage();
        }
    }
}
