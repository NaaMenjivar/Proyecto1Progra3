package utilidades;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

public class GeneradorIds {
    private static AtomicLong contadorRecetas = new AtomicLong(1);
    private static final String PREFIJO_RECETA = "REC";

    public static String generarNumeroReceta() {
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long numero = contadorRecetas.getAndIncrement();
        return String.format("%s%s%04d", PREFIJO_RECETA, fecha, numero);
    }

    public static String generarIdUsuario(String prefijo) {
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        long numero = System.currentTimeMillis() % 10000;
        return String.format("%s%s%04d", prefijo, fecha, numero);
    }

    public static String generarIdMedico() {
        return generarIdUsuario("MED");
    }

    public static String generarIdFarmaceuta() {
        return generarIdUsuario("FAR");
    }

    public static String generarIdPaciente() {
        return generarIdUsuario("PAC");
    }

    public static String generarCodigoMedicamento() {
        return generarIdUsuario("MED");
    }
}
