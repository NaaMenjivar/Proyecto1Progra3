package utilidades;

public class ConfiguracionSistema {

    // Rutas de archivos
    public static final String RUTA_DATOS = "datos/";
    public static final String RUTA_USUARIOS_XML = RUTA_DATOS + "usuarios.xml";
    public static final String RUTA_PACIENTES_XML = RUTA_DATOS + "pacientes.xml";
    public static final String RUTA_MEDICAMENTOS_XML = RUTA_DATOS + "medicamentos.xml";
    public static final String RUTA_RECETAS_XML = RUTA_DATOS + "recetas.xml";
    public static final String RUTA_CONFIGURACION_XML = RUTA_DATOS + "configuracion.xml";

    // Rutas de recursos
    public static final String RUTA_IMAGENES = "resources/imagenes/";
    public static final String RUTA_ICONOS = RUTA_IMAGENES + "iconos/";
    public static final String RUTA_LOGOS = RUTA_IMAGENES + "logos/";
    public static final String RUTA_FONDOS = RUTA_IMAGENES + "fondos/";

    // Configuraciones de la aplicación
    public static final String NOMBRE_APLICACION = "Sistema Hospital";
    public static final String VERSION = "1.0.0";
    public static final String TITULO_VENTANA = NOMBRE_APLICACION + " v" + VERSION;

    // Límites del sistema
    public static final int MAX_MEDICAMENTOS_POR_RECETA = 10;
    public static final int MAX_CANTIDAD_MEDICAMENTO = 100;
    public static final int MAX_DIAS_TRATAMIENTO = 365;
    public static final int UMBRAL_STOCK_BAJO = 10;
    public static final int DIAS_TOLERANCIA_DESPACHO = 3;

    // Configuraciones de validación
    public static final int MIN_LONGITUD_CLAVE = 4;
    public static final int MAX_LONGITUD_CLAVE = 20;
    public static final int MIN_LONGITUD_NOMBRE = 2;
    public static final int MAX_LONGITUD_NOMBRE = 50;

    // Configuraciones de interfaz
    public static final int ANCHO_VENTANA_PRINCIPAL = 1200;
    public static final int ALTO_VENTANA_PRINCIPAL = 800;
    public static final int ANCHO_DIALOGO_ESTANDAR = 500;
    public static final int ALTO_DIALOGO_ESTANDAR = 400;

    // Métodos utilitarios
    public static boolean esVersionCompatible(String version) {
        return VERSION.equals(version);
    }

    public static String obtenerTituloCompleto() {
        return TITULO_VENTANA;
    }
}
