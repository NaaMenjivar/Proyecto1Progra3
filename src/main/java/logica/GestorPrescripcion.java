package logica;

import modelo.Receta;
import java.time.LocalDate;

public class GestorPrescripcion {
    private Receta[] recetas;
    private int cantidad;

    public GestorPrescripcion() {
        recetas = new Receta[10];
        cantidad = 0;
    }

    public boolean crearReceta(String numeroReceta, String idPaciente, String idMedico, LocalDate fechaRetiro) {
        if (cantidad == recetas.length) {
            Receta[] nuevo = new Receta[recetas.length + 10];
            for (int i = 0; i < recetas.length; i++) {
                nuevo[i] = recetas[i];
            }
            recetas = nuevo;
        }
        recetas[cantidad] = new Receta(numeroReceta, idPaciente, idMedico, fechaRetiro);
        cantidad++;
        return true;
    }

    public Receta obtenerReceta(int index) {
        if (index >= 0 && index < cantidad) {
            return recetas[index];
        }
        return null;
    }

}
