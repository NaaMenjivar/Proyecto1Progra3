package datos.implementacion;

import datos.interfaces.IRecetaDAO;
import modelo.*;
import modelo.lista.Lista;
import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RecetaXMLDAO implements IRecetaDAO {
    private static final String ARCHIVO_XML = "datos/recetas.xml";
    private Lista<Receta> recetas;

    public RecetaXMLDAO() {
        recetas = new Lista<>();
        cargarDatos();
    }

    @Override
    public boolean guardar(Receta receta) {
        if (receta == null || existeReceta(receta.getNumeroReceta())) {
            return false;
        }
        recetas.agregarFinal(receta);
        return guardarDatos();
    }

    @Override
    public Receta buscarPorNumero(String numeroReceta) {
        return recetas.buscarPorId(numeroReceta);
    }

    @Override
    public Lista<Receta> obtenerTodas() {
        return recetas; // Ya es Lista<Receta>, no necesita conversión
    }

    @Override
    public boolean actualizar(Receta receta) {
        if (receta == null || !existeReceta(receta.getNumeroReceta())) {
            return false;
        }
        boolean actualizado = recetas.actualizarPorId(receta.getNumeroReceta(), receta);
        if (actualizado) {
            return guardarDatos();
        }
        return false;
    }

    @Override
    public boolean eliminar(String numeroReceta) {
        boolean eliminado = recetas.eliminarPorId(numeroReceta);
        if (eliminado) {
            return guardarDatos();
        }
        return false;
    }

    @Override
    public Lista<Receta> buscarPorPaciente(String idPaciente) {
        Lista<Receta> resultado = new Lista<>();
        for (int i = 0; i < recetas.getTam(); i++) {
            Receta r = recetas.obtenerPorPos(i);
            if (r.getIdPaciente().equalsIgnoreCase(idPaciente)) {
                resultado.agregarFinal(r);
            }
        }
        return resultado;
    }

    @Override
    public Lista<Receta> buscarPorMedico(String idMedico) {
        Lista<Receta> resultado = new Lista<>();
        for (int i = 0; i < recetas.getTam(); i++) {
            Receta r = recetas.obtenerPorPos(i);
            if (r.getIdMedico().equalsIgnoreCase(idMedico)) {
                resultado.agregarFinal(r);
            }
        }
        return resultado;
    }

    @Override
    public Lista<Receta> buscarPorEstado(EstadoReceta estado) {
        Lista<Receta> resultado = new Lista<>();
        for (int i = 0; i < recetas.getTam(); i++) {
            Receta r = recetas.obtenerPorPos(i);
            if (r.getEstado() == estado) {
                resultado.agregarFinal(r);
            }
        }
        return resultado;
    }

    @Override
    public Lista<Receta> buscarPorFechaRetiro(LocalDate fecha) {
        Lista<Receta> resultado = new Lista<>();
        for (int i = 0; i < recetas.getTam(); i++) {
            Receta r = recetas.obtenerPorPos(i);
            if (r.getFechaRetiro() != null && r.getFechaRetiro().equals(fecha)) {
                resultado.agregarFinal(r);
            }
        }
        return resultado;
    }

    @Override
    public Lista<Receta> buscarPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        Lista<Receta> resultado = new Lista<>();
        for (int i = 0; i < recetas.getTam(); i++) {
            Receta r = recetas.obtenerPorPos(i);
            if (r.getFechaRetiro() != null &&
                !r.getFechaRetiro().isBefore(fechaInicio) &&
                !r.getFechaRetiro().isAfter(fechaFin)) {
                resultado.agregarFinal(r);
            }
        }
        return resultado;
    }

    @Override
    public Lista<Receta> obtenerRecetasDespachables(LocalDate fecha) {
        Lista<Receta> resultado = new Lista<>();
        for (int i = 0; i < recetas.getTam(); i++) {
            Receta r = recetas.obtenerPorPos(i);
            if (r.puedeSerDespachada(fecha)) {
                resultado.agregarFinal(r);
            }
        }
        return resultado;
    }

    @Override
    public int contarPorEstado(EstadoReceta estado) {
        int contador = 0;
        for (int i = 0; i < recetas.getTam(); i++) {
            if (recetas.obtenerPorPos(i).getEstado() == estado) {
                contador++;
            }
        }
        return contador;
    }

    @Override
    public int contarMedicamentosEnMes(String codigoMedicamento, int año, int mes) {
        int contador = 0;
        for (int i = 0; i < recetas.getTam(); i++) {
            Receta r = recetas.obtenerPorPos(i);
            if (r.getFechaRetiro() != null &&
                r.getFechaRetiro().getYear() == año &&
                r.getFechaRetiro().getMonthValue() == mes) {
                for (int j = 0; j < r.getDetalles().getTam(); j++) {
                    DetalleReceta d = r.getDetalles().obtenerPorPos(j);
                    if (d.getCodigoMedicamento().equalsIgnoreCase(codigoMedicamento)) {
                        contador += d.getCantidad();
                    }
                }
            }
        }
        return contador;
    }

    @Override
    public Lista<Receta> obtenerRecetasDelMes(int año, int mes) {
        Lista<Receta> resultado = new Lista<>();
        for (int i = 0; i < recetas.getTam(); i++) {
            Receta r = recetas.obtenerPorPos(i);
            if (r.getFechaRetiro() != null &&
                r.getFechaRetiro().getYear() == año &&
                r.getFechaRetiro().getMonthValue() == mes) {
                resultado.agregarFinal(r);
            }
        }
        return resultado;
    }

    @Override
    public String generarNumeroReceta() {
        return "REC" + (recetas.getTam() + 1);
    }

    @Override
    public boolean existeReceta(String numeroReceta) {
        return recetas.existe(numeroReceta);
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
            Element raiz = documento.getRootElement();

            recetas = new Lista<>();

            for (Element recetaElement : raiz.getChildren("receta")) {
                Receta receta = crearRecetaDesdeXML(recetaElement);
                if (receta != null) {
                    recetas.agregarFinal(receta);
                }
            }

        } catch (Exception e) {
            System.err.println("Error al cargar recetas: " + e.getMessage());
            recetas = new Lista<>();
        }
    }

    private Receta crearRecetaDesdeXML(Element recetaElement) {
        try {
            String numeroReceta = recetaElement.getChildText("numeroReceta");
            String idPaciente = recetaElement.getChildText("idPaciente");
            String idMedico = recetaElement.getChildText("idMedico");
            String fechaConfeccionStr = recetaElement.getChildText("fechaConfeccion");
            String fechaRetiroStr = recetaElement.getChildText("fechaRetiro");
            EstadoReceta estado = EstadoReceta.valueOf(recetaElement.getChildText("estado"));

            LocalDate fechaConfeccion = null;
            if (fechaConfeccionStr != null && !fechaConfeccionStr.isEmpty()) {
                fechaConfeccion = LocalDate.parse(fechaConfeccionStr);
            }

            LocalDate fechaRetiro = null;
            if (fechaRetiroStr != null && !fechaRetiroStr.isEmpty()) {
                fechaRetiro = LocalDate.parse(fechaRetiroStr);
            }

            Receta receta = new Receta();
            receta.setNumeroReceta(numeroReceta);
            receta.setIdPaciente(idPaciente);
            receta.setIdMedico(idMedico);
            receta.setFechaConfeccion(fechaConfeccion);
            receta.setFechaRetiro(fechaRetiro);
            receta.setEstado(estado);

            Element detallesElement = recetaElement.getChild("detalles");
            if (detallesElement != null) {
                for (Element detalleElement : detallesElement.getChildren("detalle")) {
                    DetalleReceta detalle = crearDetalleRecetaDesdeXML(detalleElement);
                    if (detalle != null) {
                        receta.agregarDetalle(detalle);
                    }
                }
            }
            return receta;
        } catch (Exception e) {
            System.err.println("Error al crear receta desde XML: " + e.getMessage());
            return null;
        }
    }

    private DetalleReceta crearDetalleRecetaDesdeXML(Element detalleElement) {
        try {
            String codigoMedicamento = detalleElement.getChildText("codigoMedicamento");
            int cantidad = Integer.parseInt(detalleElement.getChildText("cantidad"));
            String indicaciones = detalleElement.getChildText("indicaciones");
            int duracionDias = 0;
            String duracionDiasStr = detalleElement.getChildText("duracionDias");
            if (duracionDiasStr != null && !duracionDiasStr.isEmpty()) {
                duracionDias = Integer.parseInt(duracionDiasStr);
            }
            return new DetalleReceta(codigoMedicamento, cantidad, indicaciones, duracionDias);
        } catch (Exception e) {
            System.err.println("Error al crear detalle receta desde XML: " + e.getMessage());
            return null;
        }
    }

    private Element crearXMLDesdeReceta(Receta receta) {
        Element recetaElement = new Element("receta");
        recetaElement.addContent(new Element("numeroReceta").setText(receta.getNumeroReceta()));
        recetaElement.addContent(new Element("idPaciente").setText(receta.getIdPaciente()));
        recetaElement.addContent(new Element("idMedico").setText(receta.getIdMedico()));
        recetaElement.addContent(new Element("fechaConfeccion").setText(receta.getFechaConfeccion().toString()));
        recetaElement.addContent(new Element("fechaRetiro").setText(receta.getFechaRetiro() != null ? receta.getFechaRetiro().toString() : ""));
        recetaElement.addContent(new Element("estado").setText(receta.getEstado().name()));

        Element detallesElement = new Element("detalles");
        for (int i = 0; i < receta.getDetalles().getTam(); i++) {
            DetalleReceta detalle = receta.getDetalles().obtenerPorPos(i);
            Element detalleElement = new Element("detalle");
            detalleElement.addContent(new Element("codigoMedicamento").setText(detalle.getCodigoMedicamento()));
            detalleElement.addContent(new Element("cantidad").setText(String.valueOf(detalle.getCantidad())));
            detalleElement.addContent(new Element("indicaciones").setText(detalle.getIndicaciones() != null ? detalle.getIndicaciones() : ""));
            detalleElement.addContent(new Element("duracionDias").setText(String.valueOf(detalle.getDuracionDias())));
            detallesElement.addContent(detalleElement);
        }
        recetaElement.addContent(detallesElement);

        return recetaElement;
    }

    private void crearArchivoInicial() {
        recetas = new Lista<>();
        Receta r1 = new Receta("REC001", "PAC001", "MED001", LocalDate.now().plusDays(2));
        r1.setFechaConfeccion(LocalDate.now());
        r1.setEstado(EstadoReceta.CONFECCIONADA);
        r1.agregarDetalle(new DetalleReceta("MED001", 2, "Cada 8 horas", 5));
        recetas.agregarFinal(r1);
        guardarDatos();
    }

    @Override
    public boolean guardarDatos() {
        FileWriter writer = null;
        try {
            Element raiz = new Element("recetas");
            Document documento = new Document(raiz);

            for (int i = 0; i < recetas.getTam(); i++) {
                Receta receta = recetas.obtenerPorPos(i);
                Element recetaElement = crearXMLDesdeReceta(receta);
                raiz.addContent(recetaElement);
            }

            File archivo = new File(ARCHIVO_XML);
            archivo.getParentFile().mkdirs();

            writer = new FileWriter(archivo);
            XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
            outputter.output(documento, writer);

            return true;
        } catch (Exception e) {
            System.err.println("Error al guardar recetas: " + e.getMessage());
            return false;
        } finally {
            try {
                if (writer != null) writer.close();
            } catch (Exception ex) {
                // Ignorar
            }
        }
    }

    @Override
    public int contarRecetas() {
        return recetas.getTam();
    }
}
