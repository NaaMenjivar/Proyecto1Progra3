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

    // ================================
    // MÉTODOS ABSTRACTOS (deben implementar las subclases)
    // ================================

    /**
     * Inicializa los datos del modelo
     */
    public abstract void inicializar();

    /**
     * Limpia todos los datos del modelo
     */
    public abstract void limpiar();

    /**
     * Valida que los datos del modelo sean consistentes
     */
    public abstract boolean validar();

    /**
     * Obtiene el número total de elementos en el modelo
     */
    public abstract int getTotalElementos();

    // ================================
    // MÉTODOS CONCRETOS (implementación común)
    // ================================

    /**
     * Indica si los datos han sido modificados
     */
    public boolean isDatosModificados() {
        return datosModificados;
    }

    /**
     * Marca los datos como modificados
     */
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

    /**
     * Representación en cadena del modelo
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "elementos=" + getTotalElementos() +
                ", modificado=" + datosModificados +
                '}';
    }

    // ================================
    // MÉTODOS PARA SUBCLASES (pueden sobrescribir)
    // ================================

    /**
     * Método llamado antes de realizar cambios importantes
     * Las subclases pueden sobrescribirlo para validaciones específicas
     */
    protected boolean antesDeModificar() {
        return true;
    }

    /**
     * Método llamado después de realizar cambios importantes
     * Las subclases pueden sobrescribirlo para notificaciones
     */
    protected void despuesDeModificar() {
        marcarComoModificado();
    }

    /**
     * Notifica a observadores sobre cambios en el modelo
     * Las subclases pueden implementar el patrón Observer
     */
    protected void notificarCambios() {
        // Implementación básica vacía
        // Las subclases pueden sobrescribir para notificaciones específicas
    }
}
