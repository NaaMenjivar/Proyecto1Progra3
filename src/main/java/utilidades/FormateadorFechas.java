package utilidades;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class FormateadorFechas {

    // Formatos estándar
    public static final DateTimeFormatter FORMATO_FECHA_CORTA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final DateTimeFormatter FORMATO_FECHA_LARGA = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy");
    public static final DateTimeFormatter FORMATO_FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    public static final DateTimeFormatter FORMATO_ARCHIVO = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    // Formateo de fechas
    public static String formatearFecha(LocalDate fecha) {
        if (fecha == null) return "";
        return fecha.format(FORMATO_FECHA_CORTA);
    }

    public static String formatearFechaLarga(LocalDate fecha) {
        if (fecha == null) return "";
        return fecha.format(FORMATO_FECHA_LARGA);
    }

    public static String formatearFechaHora(LocalDateTime fechaHora) {
        if (fechaHora == null) return "";
        return fechaHora.format(FORMATO_FECHA_HORA);
    }

    public static String formatearParaArchivo(LocalDateTime fechaHora) {
        if (fechaHora == null) return "";
        return fechaHora.format(FORMATO_ARCHIVO);
    }

    // Parsing de fechas
    public static LocalDate parsearFecha(String fechaTexto) {
        if (fechaTexto == null || fechaTexto.trim().isEmpty()) {
            return null;
        }

        try {
            return LocalDate.parse(fechaTexto.trim(), FORMATO_FECHA_CORTA);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    // Métodos de utilidad
    public static String obtenerFechaHoyFormateada() {
        return formatearFecha(LocalDate.now());
    }

    public static String obtenerFechaMañanaFormateada() {
        return formatearFecha(LocalDate.now().plusDays(1));
    }

    public static boolean esFechaHoy(LocalDate fecha) {
        return fecha != null && fecha.equals(LocalDate.now());
    }

    public static boolean esFechaFutura(LocalDate fecha) {
        return fecha != null && fecha.isAfter(LocalDate.now());
    }

    public static boolean esFechaPasada(LocalDate fecha) {
        return fecha != null && fecha.isBefore(LocalDate.now());
    }

    public static long diasEntreFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio == null || fechaFin == null) return 0;
        return java.time.temporal.ChronoUnit.DAYS.between(fechaInicio, fechaFin);
    }
}

