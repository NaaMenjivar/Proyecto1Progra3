package logica.excepciones;

public class CatalogoException extends Exception{
    //Errores en el CRUD (Crear, Leer, Actualizar y Eliminar) pero en ingles jaja

    public CatalogoException(String mensaje){
        super(mensaje);
    }
}
