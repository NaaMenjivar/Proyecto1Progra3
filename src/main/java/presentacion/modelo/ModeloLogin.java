package presentacion.modelo;

import logica.entidades.Usuario;
import logica.entidades.lista.Lista;

public class ModeloLogin {
    private Lista<Usuario> listaUsuario;

    public ModeloLogin(){
        listaUsuario = new Lista<>();
    }

    public Lista<Usuario> getListaUsuario() {
        return listaUsuario;
    }

    public void addUsuario(Usuario usuario){
        listaUsuario.agregarInicio(usuario);
    }

    public Boolean inicioSesion(String username, String password){
        for(Usuario user : listaUsuario){
            if(user.getNombre().equals(username) && user.getClave().equals(password)){
                return true;
            }
        }
        return false;
    }

    public Boolean comprobarClave(String claveActual,String id){
        for(Usuario user : listaUsuario){
            if(user.getId().equals(id)){
                if(user.getClave().equals(claveActual)){
                    return true;
                }
            }
        }
        return false;
    }

    public Boolean cambiarClave(){

        return false;
    }
}
