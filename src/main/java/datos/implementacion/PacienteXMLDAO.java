package datos.implementacion;

import datos.interfaces.IPacienteDAO;
import modelo.Paciente;
import modelo.lista.Lista;
import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;

public class PacienteXMLDAO implements IPacienteDAO {
    private static final String ARCHIVO_XML = "datos/pacientes.xml";
    private Lista<Paciente> pacientes;

    public PacienteXMLDAO() {
        pacientes = new Lista<>();
        cargarDatos();
    }

    @Override
    public boolean guardar(Paciente paciente) {
        if (paciente == null || existePaciente(paciente.getId())) {
            return false;
        }
        pacientes.agregarFinal(paciente);
        return guardarDatos();
    }

    @Override
    public Paciente buscarPorId(String id) {
        return pacientes.buscarPorId(id);
    }

    @Override
    public Lista<Paciente> obtenerTodos() {
        return pacientes; // Ya es Lista<Paciente>, no necesita conversión
    }

    @Override
    public boolean actualizar(Paciente paciente) {
        if (paciente == null || !existePaciente(paciente.getId())) {
            return false;
        }
        boolean actualizado = pacientes.actualizarPorId(paciente.getId(), paciente);
        if (actualizado) {
            return guardarDatos();
        }
        return false;
    }

    @Override
    public boolean eliminar(String id) {
        boolean eliminado = pacientes.eliminarPorId(id);
        if (eliminado) {
            return guardarDatos();
        }
        return false;
    }

    @Override
    public Lista<Paciente> buscarPorNombre(String nombre) {
        Lista<Paciente> resultado = new Lista<>();
        for (int i = 0; i < pacientes.getTam(); i++) {
            Paciente p = pacientes.obtenerPorPos(i);
            if (p.getNombre().equalsIgnoreCase(nombre)) {
                resultado.agregarFinal(p);
            }
        }
        return resultado;
    }

    @Override
    public Lista<Paciente> buscarPorNombreAproximado(String patron) {
        Lista<Paciente> resultado = new Lista<>();
        for (int i = 0; i < pacientes.getTam(); i++) {
            Paciente p = pacientes.obtenerPorPos(i);
            if (p.getNombre().toLowerCase().contains(patron.toLowerCase())) {
                resultado.agregarFinal(p);
            }
        }
        return resultado;
    }

    @Override
    public boolean existePaciente(String id) {
        return pacientes.existe(id);
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

            pacientes = new Lista<>();

            for (Element pacienteElement : raiz.getChildren("paciente")) {
                Paciente paciente = crearPacienteDesdeXML(pacienteElement);
                if (paciente != null) {
                    pacientes.agregarFinal(paciente);
                }
            }

        } catch (Exception e) {
            System.err.println("Error al cargar pacientes: " + e.getMessage());
            pacientes = new Lista<>();
        }
    }

    private Paciente crearPacienteDesdeXML(Element pacienteElement) {
        try {
            String id = pacienteElement.getChildText("id");
            String nombre = pacienteElement.getChildText("nombre");
            String fechaNacimientoStr = pacienteElement.getChildText("fechaNacimiento");
            String telefono = pacienteElement.getChildText("telefono");
            LocalDate fechaNacimiento = null;
            if (fechaNacimientoStr != null && !fechaNacimientoStr.isEmpty()) {
                fechaNacimiento = LocalDate.parse(fechaNacimientoStr);
            }
            return new Paciente(id, nombre, fechaNacimiento, telefono);
        } catch (Exception e) {
            System.err.println("Error al crear paciente desde XML: " + e.getMessage());
            return null;
        }
    }

    private Element crearXMLDesdePaciente(Paciente paciente) {
        Element pacienteElement = new Element("paciente");
        pacienteElement.addContent(new Element("id").setText(paciente.getId()));
        pacienteElement.addContent(new Element("nombre").setText(paciente.getNombre()));
        pacienteElement.addContent(new Element("fechaNacimiento").setText(
            paciente.getFechaNacimiento() != null ? paciente.getFechaNacimiento().toString() : ""));
        pacienteElement.addContent(new Element("telefono").setText(paciente.getTelefono() != null ? paciente.getTelefono() : ""));
        return pacienteElement;
    }

    private void crearArchivoInicial() {
        pacientes = new Lista<>();
        Paciente p1 = new Paciente("PAC001", "Ana López", LocalDate.of(1990, 5, 12), "912345678");
        Paciente p2 = new Paciente("PAC002", "Carlos Ruiz", LocalDate.of(1985, 8, 20), "987654321");
        pacientes.agregarFinal(p1);
        pacientes.agregarFinal(p2);
        guardarDatos();
    }

    @Override
    public boolean guardarDatos() {
        FileWriter writer = null;
        try {
            Element raiz = new Element("pacientes");
            Document documento = new Document(raiz);

            for (int i = 0; i < pacientes.getTam(); i++) {
                Paciente paciente = pacientes.obtenerPorPos(i);
                Element pacienteElement = crearXMLDesdePaciente(paciente);
                raiz.addContent(pacienteElement);
            }

            File archivo = new File(ARCHIVO_XML);
            archivo.getParentFile().mkdirs();

            writer = new FileWriter(archivo);
            XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
            outputter.output(documento, writer);

            return true;
        } catch (Exception e) {
            System.err.println("Error al guardar pacientes: " + e.getMessage());
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
    public int contarPacientes() {
        return pacientes.getTam();
    }
}
