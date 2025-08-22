package datos.fabricas;

import datos.interfaces.*;
import datos.implementacion.*;

public class DAOFactory {
    private static DAOFactory instancia;

    // Instancias singleton de los DAOs
    private IUsuarioDAO usuarioDAO;
    private IPacienteDAO pacienteDAO;
    private IMedicamentoDAO medicamentoDAO;
    private IRecetaDAO recetaDAO;

    private DAOFactory() {
        // Constructor privado para singleton
    }

    public static synchronized DAOFactory getInstance() {
        if (instancia == null) {
            instancia = new DAOFactory();
        }
        return instancia;
    }

    // Factory methods para obtener DAOs
    public IUsuarioDAO getUsuarioDAO() {
        if (usuarioDAO == null) {
            usuarioDAO = new UsuarioXMLDAO();
        }
        return usuarioDAO;
    }

    public IPacienteDAO getPacienteDAO() {
        if (pacienteDAO == null) {
            pacienteDAO = new PacienteXMLDAO();
        }
        return pacienteDAO;
    }

    public IMedicamentoDAO getMedicamentoDAO() {
        if (medicamentoDAO == null) {
            medicamentoDAO = new MedicamentoXMLDAO();
        }
        return medicamentoDAO;
    }

    public IRecetaDAO getRecetaDAO() {
        if (recetaDAO == null) {
            recetaDAO = new RecetaXMLDAO();
        }
        return recetaDAO;
    }

    // Método para inicializar todos los DAOs
    public void inicializarDAOs() {
        getUsuarioDAO().cargarDatos();
        getPacienteDAO().cargarDatos();
        getMedicamentoDAO().cargarDatos();
        getRecetaDAO().cargarDatos();
    }

    // Método para guardar todos los datos
    public boolean guardarTodos() {
        boolean exito = true;
        exito &= usuarioDAO != null ? usuarioDAO.guardarDatos() : true;
        exito &= pacienteDAO != null ? pacienteDAO.guardarDatos() : true;
        exito &= medicamentoDAO != null ? medicamentoDAO.guardarDatos() : true;
        exito &= recetaDAO != null ? recetaDAO.guardarDatos() : true;
        return exito;
    }
}
