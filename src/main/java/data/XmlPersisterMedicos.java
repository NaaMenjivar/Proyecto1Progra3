package data;

import logica.entidades.Medico;
import logica.entidades.lista.ListaMedicos;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class XmlPersisterMedicos {
    private String archivo;

    public XmlPersisterMedicos(String archivo) {
        this.archivo = archivo;
    }

    /**
     * Guarda un catálogo de médicos en un archivo XML
     */
    public void guardar(ListaMedicos catalogo) {
        try {
            // Crear documento XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            // Nodo raíz
            Element rootElement = doc.createElement("Medicos");
            doc.appendChild(rootElement);

            // Recorrer catálogo y crear nodos
            for (Medico medico : catalogo) {
                Element medicoElement = doc.createElement("Medico");

                crearElemento(doc, medicoElement, "Id", medico.getId());
                crearElemento(doc, medicoElement, "Nombre", medico.getNombre());
                crearElemento(doc, medicoElement, "Clave", medico.getClave());
                crearElemento(doc, medicoElement, "Especialidad", medico.getEspecialidad());

                rootElement.appendChild(medicoElement);
            }

            // Guardar en archivo
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes"); // formato bonito
            transformer.transform(new DOMSource(doc), new StreamResult(new File(archivo)));

            System.out.println("Archivo XML guardado correctamente en: " + archivo);

        } catch (Exception e) {
            System.err.println("Error al guardar XML: " + e.getMessage());
        }
    }

    /**
     * Carga médicos desde el XML y retorna un CatalogoMedicos
     */
    public ListaMedicos cargar() {
        ListaMedicos catalogo = new ListaMedicos();

        try {
            File file = new File(archivo);
            if (!file.exists()) {
                System.out.println("El archivo XML no existe, se retorna un catálogo vacío.");
                return catalogo;
            }

            // Leer XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);

            doc.getDocumentElement().normalize();

            NodeList listaMedicos = doc.getElementsByTagName("Medico");

            // Recorrer nodos <Medico>
            for (int i = 0; i < listaMedicos.getLength(); i++) {
                Node nodo = listaMedicos.item(i);

                if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                    Element elemento = (Element) nodo;

                    String id = elemento.getElementsByTagName("Id").item(0).getTextContent();
                    String nombre = elemento.getElementsByTagName("Nombre").item(0).getTextContent();
                    String clave = elemento.getElementsByTagName("Clave").item(0).getTextContent();
                    String especialidad = elemento.getElementsByTagName("Especialidad").item(0).getTextContent();

                    Medico medico = new Medico(id, nombre, especialidad);
                    medico.setClave(clave); // asignar clave

                    catalogo.agregarMedico(medico);
                }
            }

            System.out.println("Catálogo cargado correctamente desde XML.");

        } catch (Exception e) {
            System.err.println("Error al cargar XML: " + e.getMessage());
        }

        return catalogo;
    }

    /**
     * Método auxiliar para crear nodos XML
     */
    private void crearElemento(Document doc, Element parent, String tagName, String valor) {
        Element elem = doc.createElement(tagName);
        elem.appendChild(doc.createTextNode(valor != null ? valor : ""));
        parent.appendChild(elem);
    }
}
