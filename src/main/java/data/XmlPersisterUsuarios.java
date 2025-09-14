package data;

import logica.entidades.Administrador;
import logica.entidades.Farmaceuta;
import logica.entidades.Medico;
import logica.entidades.Usuario;
import logica.entidades.lista.ListaUsuarios;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class XmlPersisterUsuarios {

    public static void guardar(ListaUsuarios lista, String rutaArchivo) {
    try {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();

        Element root = doc.createElement("Usuarios");
        doc.appendChild(root);

        for (Usuario usuario : lista) {
            Element eUsuario = doc.createElement("Usuario");

            eUsuario.setAttribute("tipo", usuario.getTipo().toString());

            crearElemento(doc, eUsuario, "ID", usuario.getId());
            crearElemento(doc, eUsuario, "Nombre", usuario.getNombre());
            crearElemento(doc, eUsuario, "Clave", usuario.getClave());

            if (usuario instanceof Medico) {
                Medico medico = (Medico) usuario;
                crearElemento(doc, eUsuario, "Especialidad", medico.getEspecialidad());
            }

            root.appendChild(eUsuario);
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(rutaArchivo));

        transformer.transform(source, result);

        System.out.println("Usuarios guardados en: " + rutaArchivo);

    } catch (ParserConfigurationException | TransformerException e) {
        e.printStackTrace();
    }
}

    public static ListaUsuarios cargar(String rutaArchivo) {
        ListaUsuarios lista = new ListaUsuarios();
        File archivo = new File(rutaArchivo);

        if (!archivo.exists()) {
            System.out.println("Archivo no encontrado: " + rutaArchivo);
            return lista;
        }

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(archivo);
            doc.getDocumentElement().normalize();

            NodeList usuariosNodes = doc.getElementsByTagName("Usuario");

            for (int i = 0; i < usuariosNodes.getLength(); i++) {
                Node nodo = usuariosNodes.item(i);
                if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                    Element eUsuario = (Element) nodo;

                    String tipo = eUsuario.getAttribute("tipo");
                    String id = eUsuario.getElementsByTagName("ID").item(0).getTextContent();
                    String nombre = eUsuario.getElementsByTagName("Nombre").item(0).getTextContent();
                    String clave = eUsuario.getElementsByTagName("Clave").item(0).getTextContent();

                    if ("MEDICO".equalsIgnoreCase(tipo)) {
                        String especialidad = eUsuario.getElementsByTagName("Especialidad").item(0).getTextContent();
                        Medico medico = new Medico(id, nombre, especialidad);
                        medico.setClave(clave);
                        lista.agregarUsuario(medico);

                    } else if ("FARMACEUTA".equalsIgnoreCase(tipo)) {
                        Farmaceuta farmaceuta = new Farmaceuta(id, nombre);
                        farmaceuta.setClave(clave);
                        lista.agregarUsuario(farmaceuta);

                    } else if ("ADMINISTRADOR".equalsIgnoreCase(tipo)) {
                        Administrador admin = new Administrador(id, nombre);
                        admin.setClave(clave);
                        lista.agregarUsuario(admin);
                    }
                }
            }
            Usuario user = lista.getUsuarioId("admin");
            if(user!=null){
                System.out.println("Se cargo correctamente al usuario admin");
            }else{
                System.out.println("No se cargo correctamente al usuario admin");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }


    private static void crearElemento(Document doc, Element padre, String nombre, String valor) {
        Element elemento = doc.createElement(nombre);
        elemento.appendChild(doc.createTextNode(valor));
        padre.appendChild(elemento);
    }
}