package presentacion.modelo;

import logica.gestores.GestorCatalogos;
import logica.entidades.lista.Lista;

public abstract class AbstractModel {
    protected GestorCatalogos gestorCatalogos;
    protected boolean datosModificados;

    public AbstractModel() {
        this.gestorCatalogos = new GestorCatalogos();
        this.datosModificados = false;
    }

    public AbstractModel(GestorCatalogos gestor) {
        if(gestor != null){
            this.gestorCatalogos = gestor;
        }else{
            this.gestorCatalogos = new GestorCatalogos();
        }
        this.datosModificados = false;
    }

    public abstract void inicializar();

    public abstract void limpiar();

    public abstract boolean validar();

    public abstract int getTotalElementos();

    public boolean isDatosModificados() {
        return datosModificados;
    }

    public void marcarComoModificado() {
        this.datosModificados = true;
    }

    public void marcarComoNoModificado() {
        this.datosModificados = false;
    }

    public GestorCatalogos getGestorCatalogos() {
        return gestorCatalogos;
    }

    public void setGestorCatalogos(GestorCatalogos gestor) {
        this.gestorCatalogos = gestor != null ? gestor : new GestorCatalogos();
        marcarComoModificado();
    }

    protected boolean esListaValida(Lista<?> lista) {
        return lista != null && lista.getTam() > 0;
    }

    protected boolean esCadenaValida(String texto) {
        return texto != null && !texto.trim().isEmpty();
    }

    public String getEstadisticas() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ESTADÍSTICAS DEL MODELO ===\n");
        sb.append("Total de elementos: ").append(getTotalElementos()).append("\n");
        sb.append("Datos modificados: ").append(datosModificados ? "Sí" : "No").append("\n");
        sb.append("Tipo de modelo: ").append(getClass().getSimpleName()).append("\n");
        return sb.toString();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "elementos=" + getTotalElementos() +
                ", modificado=" + datosModificados +
                '}';
    }

    protected boolean antesDeModificar() {
        return true;
    }

    protected void despuesDeModificar() {
        marcarComoModificado();
    }

}
