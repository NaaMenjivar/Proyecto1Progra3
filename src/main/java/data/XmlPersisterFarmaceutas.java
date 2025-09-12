package data;

import logica.entidades.Farmaceuta;
import logica.entidades.lista.ListaFarmaceutas;
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

public class XmlPersisterFarmaceutas {

    public static void guardar(ListaFarmaceutas listaFarmaceutas, String archivo) {
        try {
            // Crear documento XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            // Nodo raíz
            Element rootElement = doc.createElement("Farmaceutas");
            doc.appendChild(rootElement);

            // Recorrer la lista y crear nodos
            for (Farmaceuta farmaceuta : listaFarmaceutas) {
                Element farmaceutaElement = doc.createElement("Farmaceuta");

                crearElemento(doc, farmaceutaElement, "Id", farmaceuta.getId());
                crearElemento(doc, farmaceutaElement, "Nombre", farmaceuta.getNombre());
                crearElemento(doc, farmaceutaElement, "Clave", farmaceuta.getClave());

                rootElement.appendChild(farmaceutaElement);
            }

            // Guardar en archivo físico
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes"); // Formato bonito
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            transformer.transform(new DOMSource(doc), new StreamResult(new File(archivo)));

            System.out.println("Archivo XML de farmaceutas guardado correctamente en: " + archivo);

        } catch (Exception e) {
            System.err.println("Error al guardar farmaceutas en XML: " + e.getMessage());
        }
    }

    /**
     * Carga farmaceutas desde un archivo XML y los retorna en una ListaFarmaceutas
     */
    public static ListaFarmaceutas cargar(String archivo) {
        ListaFarmaceutas lista = new ListaFarmaceutas();

        try {
            File file = new File(archivo);
            if (!file.exists()) {
                System.out.println("El archivo XML no existe, se retorna una lista vacía.");
                return lista;
            }

            // Leer archivo XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList listaNodos = doc.getElementsByTagName("Farmaceuta");

            // Recorrer los elementos <Farmaceuta>
            for (int i = 0; i < listaNodos.getLength(); i++) {
                Node nodo = listaNodos.item(i);

                if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                    Element elemento = (Element) nodo;

                    String id = elemento.getElementsByTagName("Id").item(0).getTextContent();
                    String nombre = elemento.getElementsByTagName("Nombre").item(0).getTextContent();
                    String clave = elemento.getElementsByTagName("Clave").item(0).getTextContent();

                    Farmaceuta farmaceuta = new Farmaceuta(id, nombre);
                    farmaceuta.setClave(clave);

                    lista.agregarFarmaceuta(farmaceuta);
                }
            }

            System.out.println("Lista de farmaceutas cargada correctamente desde el XML.");

        } catch (Exception e) {
            System.err.println("Error al cargar farmaceutas desde XML: " + e.getMessage());
        }

        return lista;
    }

    private static void crearElemento(Document doc, Element parent, String tagName, String valor) {
        Element elem = doc.createElement(tagName);
        elem.appendChild(doc.createTextNode(valor != null ? valor : ""));
        parent.appendChild(elem);
    }
}
