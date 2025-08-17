package logica.excepciones;

public class AutenticacionException extends Exception {
    //Errores de Login

    public AutenticacionException(String mensaje){
        super(mensaje);
    }
}
