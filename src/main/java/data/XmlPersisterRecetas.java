package data;

import logica.entidades.lista.Lista;
import logica.entidades.DetalleReceta;
import logica.entidades.EstadoReceta;
import logica.entidades.Receta;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class XmlPersisterRecetas {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void guardar(Lista<Receta> lista, String rutaArchivo) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element root = doc.createElement("Recetas");
            doc.appendChild(root);

            for (Receta receta : lista) {
                Element eReceta = doc.createElement("Receta");

                crearElemento(doc, eReceta, "NumeroReceta", receta.getNumeroReceta());
                crearElemento(doc, eReceta, "IdPaciente", receta.getIdPaciente());
                crearElemento(doc, eReceta, "IdMedico", receta.getIdMedico());
                crearElemento(doc, eReceta, "FechaConfeccion", receta.getFechaConfeccion().format(FORMATO_FECHA));
                crearElemento(doc, eReceta, "FechaRetiro", receta.getFechaRetiro() != null ? receta.getFechaRetiro().format(FORMATO_FECHA) : "");
                crearElemento(doc, eReceta, "Estado", receta.getEstado().name());

                // Detalles
                Element eDetalles = doc.createElement("Detalles");
                for (DetalleReceta detalle : receta.getDetalles().toArrayDetalleReceta()) {
                    Element eDetalle = doc.createElement("Detalle");

                    crearElemento(doc, eDetalle, "CodigoMedicamento", detalle.getCodigoMedicamento());
                    crearElemento(doc, eDetalle, "Cantidad", String.valueOf(detalle.getCantidad()));
                    crearElemento(doc, eDetalle, "Indicaciones", detalle.getIndicaciones());
                    crearElemento(doc, eDetalle, "DuracionDias", String.valueOf(detalle.getDuracionDias()));

                    eDetalles.appendChild(eDetalle);
                }
                eReceta.appendChild(eDetalles);

                root.appendChild(eReceta);
            }

            // Guardar en archivo
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(rutaArchivo));
            transformer.transform(source, result);

            System.out.println("Archivo de recetas guardado en: " + rutaArchivo);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Lista<Receta> cargar(String rutaArchivo) {
        Lista<Receta> lista = new Lista<>();
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

            NodeList recetasNodes = doc.getElementsByTagName("Receta");

            for (int i = 0; i < recetasNodes.getLength(); i++) {
                Node nodoReceta = recetasNodes.item(i);
                if (nodoReceta.getNodeType() == Node.ELEMENT_NODE) {
                    Element eReceta = (Element) nodoReceta;

                    String numero = eReceta.getElementsByTagName("NumeroReceta").item(0).getTextContent();
                    String idPaciente = eReceta.getElementsByTagName("IdPaciente").item(0).getTextContent();
                    String idMedico = eReceta.getElementsByTagName("IdMedico").item(0).getTextContent();
                    LocalDate fechaConfeccion = LocalDate.parse(
                            eReceta.getElementsByTagName("FechaConfeccion").item(0).getTextContent(),
                            FORMATO_FECHA
                    );

                    String fechaRetiroStr = eReceta.getElementsByTagName("FechaRetiro").item(0).getTextContent();
                    LocalDate fechaRetiro = fechaRetiroStr.isEmpty() ? null : LocalDate.parse(fechaRetiroStr, FORMATO_FECHA);

                    EstadoReceta estado = EstadoReceta.valueOf(
                            eReceta.getElementsByTagName("Estado").item(0).getTextContent()
                    );

                    Receta receta = new Receta(numero, idPaciente, idMedico, fechaRetiro);
                    receta.setFechaConfeccion(fechaConfeccion);
                    receta.setEstado(estado);

                    // Cargar detalles
                    NodeList detallesNodes = ((Element) eReceta.getElementsByTagName("Detalles").item(0)).getElementsByTagName("Detalle");
                    for (int j = 0; j < detallesNodes.getLength(); j++) {
                        Element eDetalle = (Element) detallesNodes.item(j);

                        String codigoMed = eDetalle.getElementsByTagName("CodigoMedicamento").item(0).getTextContent();
                        int cantidad = Integer.parseInt(eDetalle.getElementsByTagName("Cantidad").item(0).getTextContent());
                        String indicaciones = eDetalle.getElementsByTagName("Indicaciones").item(0).getTextContent();
                        int duracionDias = Integer.parseInt(eDetalle.getElementsByTagName("DuracionDias").item(0).getTextContent());

                        DetalleReceta detalle = new DetalleReceta(codigoMed, cantidad, indicaciones, duracionDias);
                        receta.agregarDetalle(detalle);
                    }

                    lista.agregarFinal(receta);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    private static void crearElemento(Document doc, Element padre, String nombre, String valor) {
        Element elemento = doc.createElement(nombre);
        elemento.appendChild(doc.createTextNode(valor != null ? valor : ""));
        padre.appendChild(elemento);
    }
}
