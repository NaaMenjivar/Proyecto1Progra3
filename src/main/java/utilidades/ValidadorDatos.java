package utilidades;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class ValidadorDatos {

    // Patrones de validación
    private static final Pattern PATRON_EMAIL = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    private static final Pattern PATRON_TELEFONO = Pattern.compile(
            "^[0-9]{4}-[0-9]{4}$"
    );

    private static final Pattern PATRON_ID_USUARIO = Pattern.compile(
            "^[A-Z]{3}[0-9]{6,10}$"
    );

    // Validaciones de strings
    public static boolean esTextoValido(String texto, int longitudMinima) {
        return texto != null && !texto.trim().isEmpty() && texto.trim().length() >= longitudMinima;
    }

    public static boolean esTextoValido(String texto) {
        return esTextoValido(texto, 1);
    }

    // Validaciones específicas
    public static boolean esEmailValido(String email) {
        return email != null && PATRON_EMAIL.matcher(email).matches();
    }

    public static boolean esTelefonoValido(String telefono) {
        return telefono != null && PATRON_TELEFONO.matcher(telefono).matches();
    }

    public static boolean esIdUsuarioValido(String id) {
        return id != null && PATRON_ID_USUARIO.matcher(id).matches();
    }

    // Validaciones numéricas
    public static boolean esNumeroPositivo(int numero) {
        return numero > 0;
    }

    public static boolean esNumeroEnRango(int numero, int minimo, int maximo) {
        return numero >= minimo && numero <= maximo;
    }

    // Validaciones de fechas
    public static boolean esFechaValida(LocalDate fecha) {
        return fecha != null && !fecha.isAfter(LocalDate.now());
    }

    public static boolean esFechaNacimientoValida(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) return false;
        LocalDate ahora = LocalDate.now();
        return fechaNacimiento.isBefore(ahora) &&
                fechaNacimiento.isAfter(ahora.minusYears(150));
    }

    public static boolean esFechaFuturaValida(LocalDate fecha) {
        if (fecha == null) return false;
        LocalDate ahora = LocalDate.now();
        return fecha.isAfter(ahora) && fecha.isBefore(ahora.plusYears(1));
    }

    // Validaciones de medicamentos
    public static boolean esCantidadMedicamentoValida(int cantidad) {
        return cantidad > 0 && cantidad <= 100; // máximo 100 unidades por prescripción
    }

    public static boolean esDuracionTratamientoValida(int dias) {
        return dias > 0 && dias <= 365; // máximo 1 año de tratamiento
    }

    // Validaciones de contraseñas
    public static boolean esClaveValida(String clave) {
        return clave != null && clave.length() >= 4 && clave.length() <= 20;
    }

    public static boolean esClaveSegura(String clave) {
        if (!esClaveValida(clave)) return false;

        boolean tieneNumero = clave.matches(".*[0-9].*");
        boolean tieneLetra = clave.matches(".*[a-zA-Z].*");

        return tieneNumero && tieneLetra && clave.length() >= 6;
    }

    // Método utilitario para limpiar strings
    public static String limpiarTexto(String texto) {
        if (texto == null) return "";
        return texto.trim().replaceAll("\\s+", " ");
    }
}