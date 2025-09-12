package data;

import logica.entidades.Medicamento;
import logica.entidades.lista.CatalogoMedicamentos;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class XmlPersisterMedicamentos {

    public static void guardar(CatalogoMedicamentos catalogo, String archivo) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document doc = builder.newDocument();

            Element root = doc.createElement("CatalogoMedicamentos");
            doc.appendChild(root);

            for (Medicamento med : catalogo) {
                Element medicamentoElem = doc.createElement("Medicamento");

                Element codigo = doc.createElement("Codigo");
                codigo.setTextContent(med.getCodigo());
                medicamentoElem.appendChild(codigo);

                Element nombre = doc.createElement("Nombre");
                nombre.setTextContent(med.getNombre());
                medicamentoElem.appendChild(nombre);

                Element presentacion = doc.createElement("Presentacion");
                presentacion.setTextContent(med.getPresentacion());
                medicamentoElem.appendChild(presentacion);

                Element stock = doc.createElement("Stock");
                stock.setTextContent(String.valueOf(med.getStock()));
                medicamentoElem.appendChild(stock);

                root.appendChild(medicamentoElem);
            }

            // Guardar a archivo
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(archivo));

            transformer.transform(source, result);

            System.out.println("Catálogo guardado correctamente en: " + archivo);

        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }

    public static CatalogoMedicamentos cargar(String archivo) {
        CatalogoMedicamentos catalogo = new CatalogoMedicamentos();

        try {
            File file = new File(archivo);
            if (!file.exists()) {
                System.out.println("El archivo no existe: " + archivo);
                return catalogo; // retorna catálogo vacío
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);

            doc.getDocumentElement().normalize();

            NodeList listaMedicamentos = doc.getElementsByTagName("Medicamento");

            for (int i = 0; i < listaMedicamentos.getLength(); i++) {
                Node nodo = listaMedicamentos.item(i);

                if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                    Element elemento = (Element) nodo;

                    String codigo = elemento.getElementsByTagName("Codigo").item(0).getTextContent();
                    String nombre = elemento.getElementsByTagName("Nombre").item(0).getTextContent();
                    String presentacion = elemento.getElementsByTagName("Presentacion").item(0).getTextContent();
                    int stock = Integer.parseInt(elemento.getElementsByTagName("Stock").item(0).getTextContent());

                    Medicamento med = new Medicamento(codigo, nombre, presentacion);
                    med.setStock(stock);

                    catalogo.agregarMedicamento(med);
                }
            }

            System.out.println("Catálogo cargado correctamente desde: " + archivo);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return catalogo;
    }
}

