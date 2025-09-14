package data;

import logica.entidades.Paciente;
import logica.entidades.lista.ListaPacientes;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class XmlPersisterPacientes {
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void guardar(ListaPacientes lista, String rutaArchivo) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element root = doc.createElement("Pacientes");
            doc.appendChild(root);

            for (Paciente paciente : lista) {
                Element ePaciente = doc.createElement("Paciente");

                crearElemento(doc, ePaciente, "ID", paciente.getId());
                crearElemento(doc, ePaciente, "Nombre", paciente.getNombre());
                crearElemento(doc, ePaciente, "FechaNacimiento", paciente.getFechaNacimiento().format(FORMATO_FECHA));
                crearElemento(doc, ePaciente, "Telefono", paciente.getTelefono());

                root.appendChild(ePaciente);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(rutaArchivo));

            transformer.transform(source, result);

            System.out.println("Archivo guardado correctamente en: " + rutaArchivo);

        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }

    public static ListaPacientes cargar(String rutaArchivo) {
        ListaPacientes lista = new ListaPacientes();
        File archivo = new File(rutaArchivo);

        if (!archivo.exists()) {
            System.out.println("Archivo no encontrado, devolviendo lista vacía.");
            return lista;
        }

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(archivo);
            doc.getDocumentElement().normalize();

            NodeList pacientesNodes = doc.getElementsByTagName("Paciente");

            for (int i = 0; i < pacientesNodes.getLength(); i++) {
                Node nodo = pacientesNodes.item(i);
                if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                    Element ePaciente = (Element) nodo;

                    String id = ePaciente.getElementsByTagName("ID").item(0).getTextContent();
                    String nombre = ePaciente.getElementsByTagName("Nombre").item(0).getTextContent();
                    LocalDate fechaNacimiento = LocalDate.parse(
                            ePaciente.getElementsByTagName("FechaNacimiento").item(0).getTextContent(),
                            FORMATO_FECHA
                    );
                    String telefono = ePaciente.getElementsByTagName("Telefono").item(0).getTextContent();

                    Paciente paciente = new Paciente(id, nombre, fechaNacimiento, telefono);
                    lista.agregarPaciente(paciente);
                }
            }
            System.out.println("Lista Pacientes creada correctamente desde: " + rutaArchivo);

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
