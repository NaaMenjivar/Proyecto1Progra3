package datos.implementacion;

import datos.interfaces.IUsuarioDAO;
import modelo.*;
import modelo.lista.Lista;
import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.ArrayList;

public class UsuarioXMLDAO implements IUsuarioDAO {
    private static final String ARCHIVO_XML = "datos/usuarios.xml";
    private Lista<Usuario> usuarios;

    public UsuarioXMLDAO() {
        usuarios = new Lista<>();
        cargarDatos();
    }

    @Override
    public boolean guardar(Usuario usuario) {
        if (usuario == null || existeUsuario(usuario.getId())) {
            return false;
        }
        usuarios.agregarFinal(usuario);
        return guardarDatos();
    }

    @Override
    public Usuario buscarPorId(String id) {
        return usuarios.buscarPorId(id);
    }

    @Override
    public List<Usuario> obtenerTodos() {
        List<Usuario> lista = new ArrayList<>();
        for (int i = 0; i < usuarios.getTam(); i++) {
            lista.add(usuarios.obtenerPorPos(i));
        }
        return lista;
    }

    @Override
    public boolean actualizar(Usuario usuario) {
        if (usuario == null || !existeUsuario(usuario.getId())) {
            return false;
        }
        boolean actualizado = usuarios.actualizarPorId(usuario.getId(), usuario);
        if (actualizado) {
            return guardarDatos();
        }
        return false;
    }

    @Override
    public boolean eliminar(String id) {
        boolean eliminado = usuarios.eliminarPorId(id);
        if (eliminado) {
            return guardarDatos();
        }
        return false;
    }

    @Override
    public Usuario autenticar(String id, String clave, TipoUsuario tipo) {
        Usuario usuario = buscarPorId(id);
        if (usuario != null && usuario.validarCredenciales(id, clave) && usuario.getTipo() == tipo) {
            return usuario;
        }
        return null;
    }

    @Override
    public List<Usuario> buscarPorTipo(TipoUsuario tipo) {
        List<Usuario> resultado = new ArrayList<>();
        for (int i = 0; i < usuarios.getTam(); i++) {
            Usuario usuario = usuarios.obtenerPorPos(i);
            if (usuario.getTipo() == tipo) {
                resultado.add(usuario);
            }
        }
        return resultado;
    }

    @Override
    public boolean existeUsuario(String id) {
        return usuarios.existe(id);
    }

    @Override
    public boolean cambiarClave(String id, String claveNueva) {
        Usuario usuario = buscarPorId(id);
        if (usuario != null) {
            usuario.setClave(claveNueva);
            return guardarDatos();
        }
        return false;
    }

    @Override
    public void cargarDatos() {
        try {
            File archivo = new File(ARCHIVO_XML);
            if (!archivo.exists()) {
                crearArchivoInicial();
                return;
            }

            SAXBuilder builder = new SAXBuilder();
            Document documento = builder.build(archivo);
            Element raiz = (Element) documento.getRootElement();

            usuarios = new Lista<>();

            for (Element usuarioElement : raiz.getChildren("usuario")) {
                Usuario usuario = crearUsuarioDesdeXML(usuarioElement);
                if (usuario != null) {
                    usuarios.agregarFinal(usuario);
                }
            }

        } catch (Exception e) {
            System.err.println("Error al cargar usuarios: " + e.getMessage());
            usuarios = new Lista<>(); // Lista vacía en caso de error
        }
    }

    private Usuario crearUsuarioDesdeXML(Element usuarioElement) {
        try {
            String id = usuarioElement.getChildText("id");
            String nombre = usuarioElement.getChildText("nombre");
            String clave = usuarioElement.getChildText("clave");
            TipoUsuario tipo = TipoUsuario.valueOf(usuarioElement.getChildText("tipo"));

            switch (tipo) {
                case MEDICO:
                    String especialidad = usuarioElement.getChildText("especialidad");
                    // Constructor: Medico(String id, String nombre, String clave, String especialidad)
                    return new Medico(id, nombre, clave, especialidad != null ? especialidad : "");

                case FARMACEUTA:
                    // Constructor: Farmaceuta(String id, String nombre, String clave)
                    return new Farmaceuta(id, nombre, clave);

                case ADMINISTRADOR:
                    // Constructor: Administrador(String id, String nombre, String clave)
                    return new Administrador(id, nombre, clave);

                default:
                    return null;
            }
        } catch (Exception e) {
            System.err.println("Error al crear usuario desde XML: " + e.getMessage());
            return null;
        }
    }

    private Element crearXMLDesdeUsuario(Usuario usuario) {
        Element usuarioElement = new Element("usuario");

        usuarioElement.addContent(new Element("id").setText(usuario.getId()));
        usuarioElement.addContent(new Element("nombre").setText(usuario.getNombre()));
        usuarioElement.addContent(new Element("clave").setText(usuario.getClave()));
        usuarioElement.addContent(new Element("tipo").setText(usuario.getTipo().toString()));

        // Agregar campos específicos según el tipo
        if (usuario instanceof Medico) {
            Medico medico = (Medico) usuario;
            usuarioElement.addContent(new Element("especialidad").setText(medico.getEspecialidad()));
        }

        return usuarioElement;
    }

    private void crearArchivoInicial() {
        // Crear usuarios por defecto
        usuarios = new Lista<>();

        // Usuario administrador por defecto
        // Constructor: Administrador(String id, String nombre, String clave)
        Administrador admin = new Administrador("admin", "Administrador Sistema", "admin");
        usuarios.agregarFinal(admin);

        // Médico de prueba
        // Constructor: Medico(String id, String nombre, String clave, String especialidad)
        Medico medico = new Medico("MED001", "Dr. Juan Pérez", "MED001", "Medicina General");
        usuarios.agregarFinal(medico);

        // Farmaceuta de prueba
        // Constructor: Farmaceuta(String id, String nombre, String clave)
        Farmaceuta farmaceuta = new Farmaceuta("FAR001", "María González", "FAR001");
        usuarios.agregarFinal(farmaceuta);

        guardarDatos();
    }

    @Override
    public boolean guardarDatos() {
        FileWriter writer = null;
        try {
            Element raiz = new Element("usuarios");
            Document documento = new Document(raiz);

            for (int i = 0; i < usuarios.getTam(); i++) {
                Usuario usuario = usuarios.obtenerPorPos(i);
                Element usuarioElement = crearXMLDesdeUsuario(usuario);
                raiz.addContent(usuarioElement);
            }

            // Crear directorios si no existen
            File archivo = new File(ARCHIVO_XML);
            archivo.getParentFile().mkdirs();

            writer = new FileWriter(archivo);
            XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
            outputter.output(documento, writer);

            return true;

        } catch (Exception e) {
            System.err.println("Error al guardar usuarios: " + e.getMessage());
            return false;
        } finally {
            try {
                if (writer != null) writer.close();
            } catch (Exception ex) {
                // Ignorar
            }
        }
    }
}
