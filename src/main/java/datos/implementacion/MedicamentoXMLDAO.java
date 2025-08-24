package datos.implementacion;

import datos.interfaces.IMedicamentoDAO;
import modelo.Medicamento;
import modelo.lista.Lista;
import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileWriter;

public class MedicamentoXMLDAO implements IMedicamentoDAO {
    private static final String ARCHIVO_XML = "datos/medicamentos.xml";
    private Lista<Medicamento> medicamentos;

    public MedicamentoXMLDAO() {
        medicamentos = new Lista<>();
        cargarDatos();
    }

    @Override
    public boolean guardar(Medicamento medicamento) {
        if (medicamento == null || existeMedicamento(medicamento.getCodigo())) {
            return false;
        }
        medicamentos.agregarFinal(medicamento);
        return guardarDatos();
    }

    @Override
    public Medicamento buscarPorCodigo(String codigo) {
        return medicamentos.buscarPorId(codigo);
    }

    @Override
    public Lista<Medicamento> obtenerTodos() {
        return medicamentos; // Ya es Lista<Medicamento>, no necesita conversión
    }

    @Override
    public boolean actualizar(Medicamento medicamento) {
        if (medicamento == null || !existeMedicamento(medicamento.getCodigo())) {
            return false;
        }
        boolean actualizado = medicamentos.actualizarPorId(medicamento.getCodigo(), medicamento);
        if (actualizado) {
            return guardarDatos();
        }
        return false;
    }

    @Override
    public boolean eliminar(String codigo) {
        boolean eliminado = medicamentos.eliminarPorId(codigo);
        if (eliminado) {
            return guardarDatos();
        }
        return false;
    }

    @Override
    public Lista<Medicamento> buscarPorNombre(String nombre) {
        Lista<Medicamento> resultado = new Lista<>();
        for (int i = 0; i < medicamentos.getTam(); i++) {
            Medicamento m = medicamentos.obtenerPorPos(i);
            if (m.getNombre().equalsIgnoreCase(nombre)) {
                resultado.agregarFinal(m);
            }
        }
        return resultado;
    }

    @Override
    public Lista<Medicamento> buscarPorDescripcion(String descripcion) {
        Lista<Medicamento> resultado = new Lista<>();
        for (int i = 0; i < medicamentos.getTam(); i++) {
            Medicamento m = medicamentos.obtenerPorPos(i);
            if (m.getPresentacion().equalsIgnoreCase(descripcion)) {
                resultado.agregarFinal(m);
            }
        }
        return resultado;
    }

    @Override
    public Lista<Medicamento> buscarPorDescripcionAproximada(String patron) {
        Lista<Medicamento> resultado = new Lista<>();
        for (int i = 0; i < medicamentos.getTam(); i++) {
            Medicamento m = medicamentos.obtenerPorPos(i);
            if (m.getPresentacion().toLowerCase().contains(patron.toLowerCase())) {
                resultado.agregarFinal(m);
            }
        }
        return resultado;
    }

    @Override
    public Lista<Medicamento> obtenerMedicamentosBajoStock(int umbral) {
        Lista<Medicamento> resultado = new Lista<>();
        for (int i = 0; i < medicamentos.getTam(); i++) {
            Medicamento m = medicamentos.obtenerPorPos(i);
            if (m.getStock() <= umbral) {
                resultado.agregarFinal(m);
            }
        }
        return resultado;
    }

    @Override
    public boolean existeMedicamento(String codigo) {
        return medicamentos.existe(codigo);
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

            medicamentos = new Lista<>();

            for (Element medicamentoElement : raiz.getChildren("medicamento")) {
                Medicamento medicamento = crearMedicamentoDesdeXML(medicamentoElement);
                if (medicamento != null) {
                    medicamentos.agregarFinal(medicamento);
                }
            }

        } catch (Exception e) {
            System.err.println("Error al cargar medicamentos: " + e.getMessage());
            medicamentos = new Lista<>();
        }
    }

    private Medicamento crearMedicamentoDesdeXML(Element medicamentoElement) {
        try {
            String codigo = medicamentoElement.getChildText("codigo");
            String nombre = medicamentoElement.getChildText("nombre");
            String presentacion = medicamentoElement.getChildText("presentacion");
            int stock = Integer.parseInt(medicamentoElement.getChildText("stock"));
            Medicamento med = new Medicamento(codigo, nombre, presentacion);
            med.setStock(stock);
            return med;
        } catch (Exception e) {
            System.err.println("Error al crear medicamento desde XML: " + e.getMessage());
            return null;
        }
    }

    private Element crearXMLDesdeMedicamento(Medicamento medicamento) {
        Element medicamentoElement = new Element("medicamento");
        medicamentoElement.addContent(new Element("codigo").setText(medicamento.getCodigo()));
        medicamentoElement.addContent(new Element("nombre").setText(medicamento.getNombre()));
        medicamentoElement.addContent(new Element("presentacion").setText(medicamento.getPresentacion()));
        medicamentoElement.addContent(new Element("stock").setText(String.valueOf(medicamento.getStock())));
        return medicamentoElement;
    }

    private void crearArchivoInicial() {
        medicamentos = new Lista<>();
        Medicamento m1 = new Medicamento("MED001", "Paracetamol", "Analgésico");
        m1.setStock(100);
        Medicamento m2 = new Medicamento("MED002", "Ibuprofeno", "Antiinflamatorio");
        m2.setStock(50);
        medicamentos.agregarFinal(m1);
        medicamentos.agregarFinal(m2);
        guardarDatos();
    }

    @Override
    public boolean guardarDatos() {
        FileWriter writer = null;
        try {
            Element raiz = new Element("medicamentos");
            Document documento = new Document(raiz);

            for (int i = 0; i < medicamentos.getTam(); i++) {
                Medicamento medicamento = medicamentos.obtenerPorPos(i);
                Element medicamentoElement = crearXMLDesdeMedicamento(medicamento);
                raiz.addContent(medicamentoElement);
            }

            File archivo = new File(ARCHIVO_XML);
            archivo.getParentFile().mkdirs();

            writer = new FileWriter(archivo);
            XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
            outputter.output(documento, writer);

            return true;
        } catch (Exception e) {
            System.err.println("Error al guardar medicamentos: " + e.getMessage());
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
    public int contarMedicamentos() {
        return medicamentos.getTam();
    }
}
