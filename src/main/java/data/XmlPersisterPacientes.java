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

    /**
     * Guarda la lista de pacientes en un archivo XML.
     */
    public static void guardar(ListaPacientes lista, String rutaArchivo) {
        try {
            // Crear el documento XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            // Nodo raíz
            Element root = doc.createElement("Pacientes");
            doc.appendChild(root);

            // Iterar sobre la lista y agregar cada paciente
            for (Paciente paciente : lista) {
                Element ePaciente = doc.createElement("Paciente");

                crearElemento(doc, ePaciente, "ID", paciente.getId());
                crearElemento(doc, ePaciente, "Nombre", paciente.getNombre());
                crearElemento(doc, ePaciente, "FechaNacimiento", paciente.getFechaNacimiento().format(FORMATO_FECHA));
                crearElemento(doc, ePaciente, "Telefono", paciente.getTelefono());

                root.appendChild(ePaciente);
            }

            // Escribir el XML al archivo
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

    /**
     * Carga la lista de pacientes desde un archivo XML.
     */
    public static ListaPacientes cargar(String rutaArchivo) {
        ListaPacientes lista = new ListaPacientes();
        File archivo = new File(rutaArchivo);

        if (!archivo.exists()) {
            System.out.println("Archivo no encontrado, devolviendo lista vacía.");
            return lista;
        }

        try {
            // Preparar lector XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(archivo);
            doc.getDocumentElement().normalize();

            NodeList pacientesNodes = doc.getElementsByTagName("Paciente");

            // Iterar nodos <Paciente>
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

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Método de ayuda para crear elementos XML con texto.
     */
    private static void crearElemento(Document doc, Element padre, String nombre, String valor) {
        Element elemento = doc.createElement(nombre);
        elemento.appendChild(doc.createTextNode(valor));
        padre.appendChild(elemento);
    }
}
