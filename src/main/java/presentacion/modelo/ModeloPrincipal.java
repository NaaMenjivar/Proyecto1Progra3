package presentacion.modelo;

import modelo.Medico;
import modelo.lista.Lista;

public class ModeloPrincipal {
    private Lista<Medico> listaMedicos;
    // tenemos que implementar el resto de listas que va a manejar la ventanaPrincipal
    //private manejoDePersistencia de los datos

    public ModeloPrincipal () {
        listaMedicos = new Lista<Medico>();
    }

    public void addMedicoLista(Medico medico) {
        listaMedicos.agregarInicio(medico);
    }
    public Medico getMedicoLista(String  id){
        return listaMedicos.buscarPorId(id);
    }

    public String toStringMedicoLista(){
        return listaMedicos.toString();
    }
}
