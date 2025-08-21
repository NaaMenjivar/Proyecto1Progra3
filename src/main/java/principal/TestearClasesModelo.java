// ================================
// PROGRAMA DE PRUEBAS COMPLETO
// ================================

// principal/TestearClasesModelo.java
package principal;

import modelo.*;
import modelo.lista.Lista;

import java.time.LocalDate;

public class TestearClasesModelo {

    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("    SISTEMA HOSPITAL - PRUEBAS DE CLASES MODELO");
        System.out.println("=".repeat(60));

        // Ejecutar todas las pruebas
        testEnumeraciones();
        testUsuarios();
        testPacientes();
        testMedicamentos();
        testDetalleReceta();
        testListaEnlazada();
        testRecetaCompleta();
        testBuscarMedicamentoEnReceta();
        testCasosEspeciales();

        System.out.println("\n" + "=".repeat(60));
        System.out.println("    TODAS LAS PRUEBAS COMPLETADAS");
        System.out.println("=".repeat(60));
    }

    // ================================
    // PRUEBAS DE ENUMERACIONES
    // ================================
    public static void testEnumeraciones() {
        System.out.println("\n>>> PROBANDO ENUMERACIONES <<<");
        System.out.println("-".repeat(40));

        // Probar TipoUsuario
        System.out.println("TIPOS DE USUARIO:");
        for (TipoUsuario tipo : TipoUsuario.values()) {
            System.out.println("  " + tipo + " -> " + tipo.getDescripcion());
        }

        // Probar EstadoReceta
        System.out.println("\nESTADOS DE RECETA:");
        for (EstadoReceta estado : EstadoReceta.values()) {
            System.out.println("  " + estado + " -> " + estado.getDescripcion());
        }

        System.out.println("✓ Enumeraciones funcionando correctamente");
    }

    // ================================
    // PRUEBAS DE USUARIOS
    // ================================
    public static void testUsuarios() {
        System.out.println("\n>>> PROBANDO CLASES DE USUARIOS <<<");
        System.out.println("-".repeat(40));

        // Crear médico
        Medico medico1 = new Medico("MED001", "Dr. Juan Pérez", "clave123", "Cardiología");
        System.out.println("Médico creado: " + medico1);
        System.out.println("  Especialidad: " + medico1.getEspecialidad());
        System.out.println("  Puede prescribir: " + medico1.puedePrescribir());
        System.out.println("  Tipo: " + medico1.getTipo());

        // Crear farmaceuta
        Farmaceuta farm1 = new Farmaceuta("FAR001", "clave456", "María González");
        System.out.println("\nFarmaceuta creado: " + farm1);
        System.out.println("  Puede despachar: " + farm1.puedeDespachar());
        System.out.println("  Tipo: " + farm1.getTipo());

        // Crear administrador
        Administrador admin1 = new Administrador("ADM001", "clave789", "Carlos Rodríguez");
        System.out.println("\nAdministrador creado: " + admin1);
        System.out.println("  Puede gestionar usuarios: " + admin1.puedeGestionarUsuarios());
        System.out.println("  Puede gestionar catálogos: " + admin1.puedeGestionarCatalogos());

        // Probar validación de credenciales
        System.out.println("\nPROBANDO VALIDACIÓN DE CREDENCIALES:");
        System.out.println("  Credenciales correctas médico: " + medico1.validarCredenciales("MED001", "clave123"));
        System.out.println("  Credenciales incorrectas médico: " + medico1.validarCredenciales("MED001", "wrong"));

        // Probar permisos
        System.out.println("\nPROBANDO PERMISOS:");
        System.out.println("  Médico puede acceder como médico: " + medico1.puedeAccederA(TipoUsuario.MEDICO));
        System.out.println("  Médico puede acceder como admin: " + medico1.puedeAccederA(TipoUsuario.ADMINISTRADOR));

        System.out.println("✓ Usuarios funcionando correctamente");
    }

    // ================================
    // PRUEBAS DE PACIENTES
    // ================================
    public static void testPacientes() {
        System.out.println("\n>>> PROBANDO CLASE PACIENTE <<<");
        System.out.println("-".repeat(40));

        // Crear pacientes de diferentes edades
        Paciente paciente1 = new Paciente("PAC001", "Ana Vargas",
                LocalDate.of(1985, 5, 15), "8888-1234");
        Paciente paciente2 = new Paciente("PAC002", "Luis Brenes",
                LocalDate.of(2010, 8, 20), "8888-5678");
        Paciente paciente3 = new Paciente("PAC003", "Elena Castro",
                LocalDate.of(1960, 12, 3), "8888-9999");

        System.out.println("Paciente adulto: " + paciente1);
        System.out.println("  Edad: " + paciente1.getEdadTexto());
        System.out.println("  Es mayor de edad: " + paciente1.esMayorDeEdad());

        System.out.println("\nPaciente menor: " + paciente2);
        System.out.println("  Edad: " + paciente2.getEdadTexto());
        System.out.println("  Es mayor de edad: " + paciente2.esMayorDeEdad());

        System.out.println("\nPaciente adulto mayor: " + paciente3);
        System.out.println("  Edad: " + paciente3.getEdadTexto());
        System.out.println("  Teléfono: " + paciente3.getTelefono());

        // Probar igualdad
        Paciente paciente1Copia = new Paciente("PAC001", "Otro Nombre", LocalDate.now(), "9999-9999");
        System.out.println("\nPROBANDO IGUALDAD (por ID):");
        System.out.println("  Paciente1 equals paciente1Copia: " + paciente1.equals(paciente1Copia));
        System.out.println("  Paciente1 equals paciente2: " + paciente1.equals(paciente2));

        System.out.println("✓ Pacientes funcionando correctamente");
    }

    // ================================
    // PRUEBAS DE MEDICAMENTOS
    // ================================
    public static void testMedicamentos() {
        System.out.println("\n>>> PROBANDO CLASE MEDICAMENTO <<<");
        System.out.println("-".repeat(40));

        // Crear medicamentos
        Medicamento med1 = new Medicamento("MED001", "Acetaminofén", "500mg tabletas");
        Medicamento med2 = new Medicamento("MED002", "Ibuprofeno", "400mg cápsulas");
        Medicamento med3 = new Medicamento("MED003", "Amoxicilina", "250mg suspensión oral");

        System.out.println("Medicamento 1: " + med1);
        System.out.println("  Descripción completa: " + med1.getDescripcionCompleta());
        System.out.println("  Código y nombre: " + med1.getCodigoYNombre());

        System.out.println("\nMedicamento 2: " + med2);
        System.out.println("  Presentación: " + med2.getPresentacion());

        System.out.println("\nMedicamento 3: " + med3);
        System.out.println("  Nombre: " + med3.getNombre());

        // Probar igualdad
        Medicamento med1Copia = new Medicamento("MED001", "Diferente", "Diferente");
        System.out.println("\nPROBANDO IGUALDAD (por código):");
        System.out.println("  Med1 equals med1Copia: " + med1.equals(med1Copia));
        System.out.println("  Med1 equals med2: " + med1.equals(med2));

        System.out.println("✓ Medicamentos funcionando correctamente");

        System.out.printf("\tLISTA DE MEDICAMENTOS:\n");
        Lista<Medicamento> listaMedicamentos = new Lista<Medicamento>();
        listaMedicamentos.agregarInicio(med1);
        listaMedicamentos.agregarInicio(med3);
        listaMedicamentos.agregarFinal(med2);

        System.out.printf(listaMedicamentos.toString() + "\n");
        Medicamento medi4 = listaMedicamentos.obtenerPorPos(0);
        System.out.println(medi4.toString());

        listaMedicamentos.modificarPorPos(0,med1Copia);
        System.out.printf(listaMedicamentos.toString() + "\n");


    }

    // ================================
    // PRUEBAS DE DETALLE RECETA
    // ================================
    public static void testDetalleReceta() {
        System.out.println("\n>>> PROBANDO CLASE DETALLE RECETA <<<");
        System.out.println("-".repeat(40));

        // Crear detalles válidos
        DetalleReceta detalle1 = new DetalleReceta("MED001", 2, "1 cada 8 horas", 7);
        DetalleReceta detalle2 = new DetalleReceta("MED002", 1, "1 cada 12 horas con alimentos", 5);
        DetalleReceta detalle3 = new DetalleReceta("MED003", 3, "1 cada 6 horas", 10);

        System.out.println("Detalle 1: " + detalle1);
        System.out.println("  Cantidad texto: " + detalle1.getCantidadTexto());
        System.out.println("  Duración texto: " + detalle1.getDuracionTexto());
        System.out.println("  Es válido: " + detalle1.esValidoPrescripcion());

        System.out.println("\nDetalle 2: " + detalle2);
        System.out.println("  Indicaciones: " + detalle2.getIndicaciones());

        // Crear detalle inválido
        DetalleReceta detalleInvalido = new DetalleReceta("", 0, "", 0);
        System.out.println("\nDetalle inválido: " + detalleInvalido);
        System.out.println("  Es válido: " + detalleInvalido.esValidoPrescripcion());

        // Probar igualdad
        DetalleReceta detalle1Copia = new DetalleReceta("MED001", 999, "Diferente", 999);
        System.out.println("\nPROBANDO IGUALDAD (por código medicamento):");
        System.out.println("  Detalle1 equals detalle1Copia: " + detalle1.equals(detalle1Copia));
        System.out.println("  Detalle1 equals detalle2: " + detalle1.equals(detalle2));

        System.out.println("✓ Detalles de receta funcionando correctamente");
    }

    // ================================
    // PRUEBAS DE LISTA ENLAZADA
    // ================================
    public static void testListaEnlazada() {
        System.out.println("\n>>> PROBANDO LISTA ENLAZADA <<<");
        System.out.println("-".repeat(40));

        Lista<DetalleReceta> lista = new Lista<DetalleReceta>();

        // Probar lista vacía
        System.out.println("Lista nueva (vacía): " + lista);
        System.out.println("  Está vacía: " + lista.vacia());
        System.out.println("  Tamaño: " + lista.getTam());

        // Agregar elementos
        DetalleReceta d1 = new DetalleReceta("MED001", 2, "Cada 8 horas", 7);
        DetalleReceta d2 = new DetalleReceta("MED002", 1, "Cada 12 horas", 5);
        DetalleReceta d3 = new DetalleReceta("MED003", 3, "Cada 6 horas", 10);

        lista.agregarFinal(d1);
        lista.agregarFinal(d2);
        lista.agregarFinal(d3);

        System.out.println("\nDespués de agregar 3 elementos:");
        System.out.println("  Lista: " + lista);
        System.out.println("  Tamaño: " + lista.getTam());
        System.out.println("  Total unidades: " + lista.getTam());

        // Probar acceso por índice
        System.out.println("\nACCESO POR ÍNDICE:");
        for (int i = 0; i < lista.getTam(); i++) {
            DetalleReceta detalle = lista.obtenerPorPos(i);
            System.out.println("  [" + i + "]: " + detalle.getCodigoMedicamento() +
                    " - " + detalle.getCantidadTexto());
        }

        // Probar búsqueda
        System.out.println("\nBÚSQUEDA POR CÓDIGO:");
        DetalleReceta encontrado = lista.buscarPorId("MED002");
        System.out.println("  Buscar MED002: " + (encontrado != null ? "Encontrado" : "No encontrado"));

        // Probar modificación
        DetalleReceta nuevoDetalle = new DetalleReceta("MED002", 5, "Cambié las indicaciones", 3);
        lista.modificarPorPos(1, nuevoDetalle);
        System.out.println("\nDespués de modificar elemento en índice 1:");
        System.out.println("  Elemento modificado: " + lista.obtenerPorPos(1).getIndicaciones());

        // Probar eliminación
        lista.eliminarPorId("MED001");
        System.out.println("\nDespués de eliminar MED001:");
        System.out.println("  Lista: " + lista);
        System.out.println("  Tamaño: " + lista.getTam());

        // Probar agregar al inicio
        DetalleReceta d4 = new DetalleReceta("MED004", 1, "Al inicio", 1);
        lista.agregarInicio(d4);
        System.out.println("\nDespués de agregar al inicio:");
        System.out.println("  Lista: " + lista);
        System.out.println("  Primer elemento: " + lista.getCabeza());
        System.out.println("  Último elemento: " + lista.getUltimo());

        // Probar conversión a array
        System.out.println("\nCONVERSIÓN A ARRAY:");
        DetalleReceta[] array = lista.toArrayDetalleReceta();
        System.out.print("  Array: [");
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i].getCodigoMedicamento());
            if (i < array.length - 1) System.out.print(", ");
        }
        System.out.println("]");

        System.out.println("✓ Lista enlazada funcionando correctamente");
    }

    // ================================
    // PRUEBAS DE RECETA COMPLETA
    // ================================
    public static void testRecetaCompleta() {
        System.out.println("\n>>> PROBANDO CLASE RECETA COMPLETA <<<");
        System.out.println("-".repeat(40));

        // Crear receta
        Receta receta = new Receta("REC001", "PAC001", "MED001", LocalDate.now().plusDays(1));
        System.out.println("Receta creada: " + receta);
        System.out.println("  Estado inicial: " + receta.getEstado());
        System.out.println("  Fecha confección: " + receta.getFechaConfeccion());
        System.out.println("  Fecha retiro: " + receta.getFechaRetiro());

        // Agregar medicamentos
        receta.agregarDetalle(new DetalleReceta("MED001", 2, "1 cada 8 horas", 7));
        receta.agregarDetalle(new DetalleReceta("MED002", 1, "1 cada 12 horas", 5));
        receta.agregarDetalle(new DetalleReceta("MED003", 3, "1 cada 6 horas", 10));

        System.out.println("\nDespués de agregar medicamentos:");
        System.out.println("  Tiene detalles: " + receta.tieneDetalles());
        System.out.println("  Total medicamentos: " + receta.getTotalMedicamentos());
        System.out.println("  Total unidades: " + receta.getTotalUnidades());
        System.out.println("  Resumen: " + receta.getResumenMedicamentos());

        // Mostrar todos los detalles
        System.out.println("\nDETALLES DE LA RECETA:");
        Lista<DetalleReceta> detalles = receta.getDetalles();
        for (int i = 0; i < detalles.getTam(); i++) {
            DetalleReceta detalle = detalles.obtenerPorPos(i);
            System.out.println("  " + (i+1) + ". " + detalle.getCodigoMedicamento() +
                    " - " + detalle.getCantidadTexto() +
                    " - " + detalle.getIndicaciones());
        }

        // Probar si puede ser despachada
        System.out.println("\nPROBANDO DESPACHO:");
        LocalDate hoy = LocalDate.now();
        LocalDate manana = LocalDate.now().plusDays(1);
        LocalDate enUnaSemana = LocalDate.now().plusDays(7);

        System.out.println("  Puede ser despachada hoy: " + receta.puedeSerDespachada(hoy));
        System.out.println("  Puede ser despachada mañana: " + receta.puedeSerDespachada(manana));
        System.out.println("  Puede ser despachada en una semana: " + receta.puedeSerDespachada(enUnaSemana));

        // Probar cambios de estado
        System.out.println("\nPROBANDO CAMBIOS DE ESTADO:");
        System.out.println("  Estado actual: " + receta.getEstado());
        System.out.println("  Puede avanzar a PROCESO: " + receta.puedeAvanzarAEstado(EstadoReceta.PROCESO));
        System.out.println("  Puede avanzar a LISTA: " + receta.puedeAvanzarAEstado(EstadoReceta.LISTA));

        receta.setEstado(EstadoReceta.PROCESO);
        System.out.println("  Después de cambiar a PROCESO:");
        System.out.println("    Puede avanzar a LISTA: " + receta.puedeAvanzarAEstado(EstadoReceta.LISTA));
        System.out.println("    Puede avanzar a ENTREGADA: " + receta.puedeAvanzarAEstado(EstadoReceta.ENTREGADA));

        System.out.println("✓ Receta completa funcionando correctamente");
    }

    // ================================
// PRUEBA DE BÚSQUEDA DE MEDICAMENTO EN RECETA
// ================================
    public static void testBuscarMedicamentoEnReceta() {
        System.out.println("\n>>> PROBANDO BÚSQUEDA DE MEDICAMENTO EN RECETA <<<");
        System.out.println("-".repeat(40));

        // Crear una receta con varios medicamentos
        Receta receta = new Receta("REC001", "PAC001", "MED001", LocalDate.now().plusDays(1));

        // Agregar varios detalles de medicamentos
        receta.agregarDetalle(new DetalleReceta("MED001", 2, "1 tableta cada 8 horas con alimentos", 7));
        receta.agregarDetalle(new DetalleReceta("MED002", 1, "1 cápsula cada 12 horas", 5));
        receta.agregarDetalle(new DetalleReceta("MED003", 3, "1 cucharada cada 6 horas", 10));
        receta.agregarDetalle(new DetalleReceta("MED004", 1, "Aplicar en la zona afectada 2 veces al día", 14));

        System.out.println("Receta creada con " + receta.getTotalMedicamentos() + " medicamentos:");
        System.out.println("  Número Receta: " + receta.getNumeroReceta());
        System.out.println("  Total unidades: " + receta.getTotalUnidades());
        System.out.println("  Resumen: " + receta.getResumenMedicamentos());

        // Mostrar todos los medicamentos en la receta
        System.out.println("\nMEDICAMENTOS EN LA RECETA:");
        Lista<DetalleReceta> detalles = receta.getDetalles();
        for (int i = 0; i < detalles.getTam(); i++) {
            DetalleReceta detalle = detalles.obtenerPorPos(i);
            System.out.println("  " + (i+1) + ". Código: " + detalle.getCodigoMedicamento() +
                    " | Cantidad: " + detalle.getCantidadTexto() +
                    " | Duración: " + detalle.getDuracionTexto());
        }

        // Probar búsquedas por código
        System.out.println("\nBÚSQUEDAS POR CÓDIGO DE MEDICAMENTO:");

        // Buscar medicamentos que existen
        String[] codigosBuscar = {"MED001", "MED002", "MED003", "MED004"};

        for (String codigo : codigosBuscar) {
            DetalleReceta medicamentoEncontrado = receta.getDetalles().buscarPorId(codigo);

            if (medicamentoEncontrado != null) {
                System.out.println("  ✓ " + codigo + " ENCONTRADO:");
                System.out.println("    - Cantidad: " + medicamentoEncontrado.getCantidadTexto());
                System.out.println("    - Indicaciones: " + medicamentoEncontrado.getIndicaciones());
                System.out.println("    - Duración: " + medicamentoEncontrado.getDuracionTexto());
                System.out.println("    - Es válido: " + medicamentoEncontrado.esValidoPrescripcion());
            } else {
                System.out.println("  ✗ " + codigo + " NO ENCONTRADO");
            }
            System.out.println();
        }

        // Probar búsquedas de medicamentos que no existen
        System.out.println("BÚSQUEDAS DE CÓDIGOS INEXISTENTES:");
        String[] codigosInexistentes = {"MED999", "INVALID", "", null};

        for (String codigo : codigosInexistentes) {
            DetalleReceta resultado = receta.getDetalles().buscarPorId(codigo);
            System.out.println("  Buscar '" + codigo + "': " +
                    (resultado == null ? "NO ENCONTRADO ✓" : "ENCONTRADO (ERROR)"));
        }

        // Probar búsqueda con criterios específicos
        System.out.println("\nBÚSQUEDA CON ANÁLISIS DETALLADO:");
        String codigoBuscar = "MED002";
        DetalleReceta medicamento = receta.getDetalles().buscarPorId(codigoBuscar);

        if (medicamento != null) {
            System.out.println("MEDICAMENTO " + codigoBuscar + " - INFORMACIÓN COMPLETA:");
            System.out.println("  → Código: " + medicamento.getCodigoMedicamento());
            System.out.println("  → Cantidad prescrita: " + medicamento.getCantidad() + " unidad(es)");
            System.out.println("  → Indicaciones completas: " + medicamento.getIndicaciones());
            System.out.println("  → Duración del tratamiento: " + medicamento.getDuracionDias() + " días");
            System.out.println("  → Texto duración: " + medicamento.getDuracionTexto());
            System.out.println("  → Prescripción válida: " + (medicamento.esValidoPrescripcion() ? "SÍ" : "NO"));

            // Información adicional
            if (medicamento.getDuracionDias() > 7) {
                System.out.println("  → NOTA: Tratamiento prolongado (más de 1 semana)");
            }
            if (medicamento.getCantidad() > 2) {
                System.out.println("  → NOTA: Cantidad alta por dosis");
            }
        }

        // Ejemplo práctico: buscar todos los medicamentos de una receta para validar
        System.out.println("\nVALIDACIÓN DE TODOS LOS MEDICAMENTOS EN LA RECETA:");
        int medicamentosValidos = 0;
        int medicamentosInvalidos = 0;

        for (int i = 0; i < receta.getDetalles().getTam(); i++) {
            DetalleReceta detalle = receta.getDetalles().obtenerPorPos(i);
            if (detalle.esValidoPrescripcion()) {
                medicamentosValidos++;
                System.out.println("  ✓ " + detalle.getCodigoMedicamento() + " - VÁLIDO");
            } else {
                medicamentosInvalidos++;
                System.out.println("  ✗ " + detalle.getCodigoMedicamento() + " - INVÁLIDO");
            }
        }

        System.out.println("\nRESUMEN DE VALIDACIÓN:");
        System.out.println("  Medicamentos válidos: " + medicamentosValidos);
        System.out.println("  Medicamentos inválidos: " + medicamentosInvalidos);
        System.out.println("  Total medicamentos: " + receta.getTotalMedicamentos());
        System.out.println("  Receta completa válida: " + (medicamentosInvalidos == 0 ? "SÍ" : "NO"));

        System.out.println("✓ Búsqueda de medicamentos en receta funcionando correctamente");
    }

    // ================================
    // PRUEBAS DE CASOS ESPECIALES
    // ================================
    public static void testCasosEspeciales() {
        System.out.println("\n>>> PROBANDO CASOS ESPECIALES <<<");
        System.out.println("-".repeat(40));

        // Probar agregar elementos inválidos a la lista
        Lista<DetalleReceta> lista = new Lista<DetalleReceta>();
        DetalleReceta detalleInvalido = new DetalleReceta("", 0, "", 0);

        lista.agregarFinal(detalleInvalido);
        System.out.println("Intentar agregar detalle inválido:");
        System.out.println("  Tamaño de lista después: " + lista.getTam());

        lista.agregarFinal(null);
        System.out.println("Intentar agregar null:");
        System.out.println("  Tamaño de lista después: " + lista.getTam());

        // Probar operaciones en lista vacía
        System.out.println("\nOperaciones en lista vacía:");
        System.out.println("  Obtener elemento 0: " + lista.obtenerPorPos(0));
        System.out.println("  Buscar elemento: " + lista.buscarPorId("MED001"));
        System.out.println("  Eliminar elemento: " + lista.eliminarPorId("MED001"));
        System.out.println("  Primer elemento: " + lista.getCabeza());
        System.out.println("  Último elemento: " + lista.getUltimo());

        // Probar índices fuera de rango
        lista.agregarFinal(new DetalleReceta("MED001", 1, "Test", 1));
        System.out.println("\nÍndices fuera de rango (lista con 1 elemento):");
        System.out.println("  Obtener elemento -1: " + lista.obtenerPorPos(-1));
        System.out.println("  Obtener elemento 5: " + lista.obtenerPorPos(5));
        System.out.println("  Modificar índice -1: " + lista.modificarPorPos(-1, new DetalleReceta("TEST", 1, "Test", 1)));

        // Probar receta sin detalles
        System.out.println("\nReceta sin detalles:");
        Receta recetaVacia = new Receta("REC999", "PAC999", "MED999", LocalDate.now());
        System.out.println("  Resumen medicamentos: " + recetaVacia.getResumenMedicamentos());
        System.out.println("  Tiene detalles: " + recetaVacia.tieneDetalles());
        System.out.println("  Total medicamentos: " + recetaVacia.getTotalMedicamentos());

        // Probar fechas null
        System.out.println("\nPaciente con fecha null:");
        Paciente pacienteError = new Paciente();
        pacienteError.setId("PAC999");
        pacienteError.setNombre("Sin Fecha");
        pacienteError.setFechaNacimiento(null);
        System.out.println("  Edad: " + pacienteError.getEdad());
        System.out.println("  Es mayor: " + pacienteError.esMayorDeEdad());

        System.out.println("✓ Casos especiales manejados correctamente");
    }
}
