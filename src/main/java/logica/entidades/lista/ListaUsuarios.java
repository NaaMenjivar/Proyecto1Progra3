package logica.entidades.lista;

import logica.entidades.Usuario;
import logica.excepciones.CatalogoException;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ListaUsuarios implements Iterable<Usuario> {
    private Nodo<Usuario> cabeza;
    int tam;

    public ListaUsuarios() {
        this.cabeza = null;
        this.tam = 0;
    }

    public Iterator<Usuario> iterator() {
        return new Iterator<Usuario>() {
            private Nodo<Usuario> actual = cabeza;

            @Override
            public boolean hasNext() {
                return actual != null;
            }

            @Override
            public Usuario next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                Usuario dato = actual.getDato();
                actual = actual.getSiguiente();
                return dato;
            }
        };
    }

    public void agregarUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario inválido");
        }

        Nodo<Usuario> nuevo = new Nodo<>(usuario);
        nuevo.setSiguiente(cabeza);
        cabeza = nuevo;
        tam++;
    }

    public boolean existeUsuarioId(String id){
        if(cabeza == null || id == null){
            return false;
        }

        Nodo<Usuario> aux = cabeza;
        while(aux.getSiguiente() != null){
            if(aux.getDato().getId().equals(id)){
                return true;
            }
            aux = aux.getSiguiente();
        }
        return false;
    }

    public Usuario getUsuarioId(String id){
        if(cabeza == null || id == null){
            return null;
        }
        Nodo<Usuario> aux = cabeza;
        while(aux.getSiguiente() != null){
            if(aux.getDato().getId().equals(id)){
                return aux.getDato();
            }
            aux = aux.getSiguiente();
        }
        return null;
    }

    public Usuario autenticarUsuario(String id,String clave){
        if(cabeza == null){
            return null;
        }
        if(cabeza.getDato().getClave().equals(clave) && cabeza.getDato().getId().equals(id)){
            return cabeza.getDato();
        }
        Nodo<Usuario> aux = cabeza;
        while(aux.getSiguiente() != null){
            if(aux.getDato().getId().equals(id) && aux.getDato().getClave().equals(clave)){
                return aux.getDato();
            }
            aux = aux.getSiguiente();
        }
        return null ;
    }

    public boolean cambiarClave(Usuario user) throws CatalogoException {
        if(cabeza == null || user == null){
            throw new CatalogoException("Usuario Null o Lista de Usuarios Vacia");
        }

        Nodo<Usuario> actual = cabeza;
        while(actual.getSiguiente() != null){
            if(actual.getDato().getId().equals(user.getId())){
                actual.setDato(user);
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    public boolean eliminarUsuario(String id) throws CatalogoException {
        if(cabeza == null || id == null){
            throw new CatalogoException("Usuario Null o Lista de Usuarios Vacia");
        }
        if(cabeza.getDato().getId().equals(id)){
            cabeza = cabeza.getSiguiente();
        }

        Nodo<Usuario> aux = cabeza;
        while(aux != null){
            if(aux.getSiguiente().getDato().getId().equals(id)){
                Nodo<Usuario> auxSiguiente = aux.getSiguiente();
                aux.setSiguiente(auxSiguiente.getSiguiente());
                return true;
            }
            aux = aux.getSiguiente();
        }
        return false;
    }

    public int getTam(){
        return tam;
    }
}
