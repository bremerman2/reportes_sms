package org.example;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Numero_oficialDAO dao = new Numero_oficialDAO();
        Pais_prefijoDAO paisPrefijoDAO = new Pais_prefijoDAO();
        ReportesDAO reportesDAO = new ReportesDAO();
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        int opcion;

        do {
            System.out.println("\n=== ABM Numero Oficial ===");
            System.out.println("1. Registrar numero oficial");
            System.out.println("2. Editar numero oficial");
            System.out.println("3. Eliminar numero oficial");
            System.out.println("4. Listar numero oficial");

            System.out.println("\n\n=== GESTION DE PREFIJOS ===");
            System.out.println("5. Registrar nuevo prefijo");
            System.out.println("6. Editar prefijo");
            System.out.println("7. Eliminar prefijo");
            System.out.println("8. Listar prefijos");

            System.out.println("\n\n=== GESTION DE REPORTES ===");
            System.out.println("9. Registrar un reporte");
            System.out.println("10. Listar reportes");
            System.out.println("11. Confirmar reporte");
            System.out.println("12. Registrar falso positivo");
            System.out.println("13. Buscar reportes por estado");

            System.out.println("\n\n=== GESTION DE USUARIOS ===");
            System.out.println("14. Buscar usuario");
            System.out.println("15. Bloquear usuario");
            System.out.println("16. Eliminar usuario");

            System.out.println("0. Salir");
            System.out.print("Elegi una opcion: ");
            opcion = Integer.parseInt(sc.nextLine());

            switch (opcion) {
                case 1:
                    registrar(sc, dao);
                    pausar(sc);
                    break;
                case 2:
                    editar(sc, dao);
                    pausar(sc);
                    break;
                case 3:
                    eliminar(sc, dao);
                    pausar(sc);
                    break;
                case 4:
                    listarNumOficiales(dao);
                    pausar(sc);
                    break;
                case 5:
                    registrarPrefijo(sc, paisPrefijoDAO);
                    pausar(sc);
                    break;
                case 6:
                    editarPrefijo(sc, paisPrefijoDAO);
                    pausar(sc);
                    break;
                case 7:
                    eliminarPrefijo(sc, paisPrefijoDAO);
                    pausar(sc);
                    break;
                case 8:
                    listarPrefijos(paisPrefijoDAO);
                    pausar(sc);
                    break;
                case 9:
                    registrarReporte(sc, reportesDAO);
                    pausar(sc);
                    break;
                case 10:
                    listarReportes(reportesDAO);
                    pausar(sc);
                    break;
                case 11:
                    confirmarReporte(sc, reportesDAO);
                    pausar(sc);
                    break;
                case 12:
                    registrarFalsoPositivo(sc, reportesDAO);
                    pausar(sc);
                    break;
                case 13:
                    buscarReportesPorEstado(sc, reportesDAO);
                    pausar(sc);
                    break;

                case 15:
                    bloquearUsuario(sc, usuarioDAO);
                    break;

                case 16:
                    eliminarUsuario(sc, usuarioDAO);
                    break;
                case 0:
                    System.out.println("Chau!");
                    break;
                default:
                    System.out.println("Opcion invalida");
            }

        } while (opcion != 0);

        sc.close();
    }

    private static void registrar(Scanner sc, Numero_oficialDAO dao) {
        System.out.println("\n-- Registrar numero oficial --");

        System.out.print("Numero de telefono: ");
        String telefono = sc.nextLine();

        System.out.print("Nombre de entidad: ");
        String nombre_entidad = sc.nextLine();

        System.out.print("ID prefijo: ");
        int idPrefijo = Integer.parseInt(sc.nextLine());

        System.out.print("ID administrador: ");
        int idAdmin = Integer.parseInt(sc.nextLine());

        Pais_prefijo prefijo = new Pais_prefijo();
        prefijo.setId_prefijo(idPrefijo);

        Administrador admin = new Administrador();
        admin.setId_administrador(idAdmin);

        // el id lo asigna la db (autoincremental) y la fecha de carga es la del momento del alta
        Numero_oficial numeroOficial = new Numero_oficial(0, telefono, nombre_entidad, LocalDate.now(), prefijo, admin);

        boolean ok = dao.registrarNumOficial(numeroOficial);
        if (ok) {
            System.out.println("Numero oficial registrado con exito. ID asignado: " + numeroOficial.getId_numero_oficial());
        } else {
            System.out.println("No se pudo registrar el numero oficial.");
        }
    }

    private static void editar(Scanner sc, Numero_oficialDAO dao) {
        System.out.println("\n-- Editar numero oficial --");

        System.out.print("ID del numero oficial a editar: ");
        int id = Integer.parseInt(sc.nextLine());

        System.out.print("Nuevo numero de telefono: ");
        String telefono = sc.nextLine();

        System.out.print("Nuevo nombre de entidad: ");
        String nombre_entidad = sc.nextLine();

        System.out.print("Nueva fecha de carga (aaaa-mm-dd): ");
        LocalDate fecha = LocalDate.parse(sc.nextLine());

        System.out.print("Nuevo ID prefijo: ");
        int idPrefijo = Integer.parseInt(sc.nextLine());

        System.out.print("Nuevo ID administrador: ");
        int idAdmin = Integer.parseInt(sc.nextLine());

        Pais_prefijo prefijo = new Pais_prefijo();
        prefijo.setId_prefijo(idPrefijo);

        Administrador admin = new Administrador();
        admin.setId_administrador(idAdmin);

        Numero_oficial numeroOficial = new Numero_oficial(id, telefono, nombre_entidad, fecha, prefijo, admin);

        boolean ok = dao.editarNumOficial(numeroOficial);
        System.out.println(ok ? "Numero oficial editado con exito." : "No se pudo editar el numero oficial.");
    }

    private static void eliminar(Scanner sc, Numero_oficialDAO dao) {
        System.out.println("\n-- Eliminar numero oficial --");

        System.out.print("ID del numero oficial a eliminar: ");
        int id = Integer.parseInt(sc.nextLine());

        boolean ok = dao.eliminarNumOficial(id);
        System.out.println(ok ? "Numero oficial eliminado con exito." : "No se pudo eliminar el numero oficial.");
    }

    private static void listarNumOficiales(Numero_oficialDAO dao) {

        List<Numero_oficial> numeros = dao.listarNumOficiales();

        if (numeros.isEmpty()) {
            System.out.println("\n=== No hay numeros oficiales registrados ===");
        } else {
            System.out.println("\n==--== Numeros oficiales registrados ==--==\n");

            for (Numero_oficial numero : numeros) {

                System.out.println("ID: " + numero.getId_numero_oficial());
                System.out.println("NUMERO: " + numero.getNumero_telefono());
                System.out.println("ENTIDAD: " + numero.getNombre_entidad());
                System.out.println("PREFIJO: " + numero.getPais_prefijo().getPrefijo());
                System.out.println("PAIS: " + numero.getPais_prefijo().getNombre_pais());
                System.out.println("FECHA DE CARGA: " + numero.getFecha_carga());
                System.out.println("ADMINISTRADOR: " + numero.getAdministrador().getNombre());
                System.out.println("-----------------------");
            }
        }
    }

    private static void registrarPrefijo(Scanner sc, Pais_prefijoDAO paisPrefijoDAO) {
        System.out.println("\n======================================");
        System.out.println("====== Registrar nuevo prefijo ======");
        System.out.println("======================================\n");

        System.out.print("Prefijo: ");
        String prefijo = sc.nextLine().trim();

        //si no se ingresó el '+' se agrega
        if (!prefijo.startsWith("+")) {
            prefijo = "+" + prefijo;
        }

        System.out.print("Nombre de país: ");
        String nombre_pais = sc.nextLine().trim();

        //controlar campos vacios
        if(prefijo.equals("+") || nombre_pais.isEmpty()) {
            System.out.println("Debe ingresar valores validos en ambos campos");
            return;
        }
        //controlar duplicados prefijo+pais
        if (paisPrefijoDAO.existePrefijoYPais(prefijo, nombre_pais)) {
            System.out.println("--- El prefijo y nombre del pais ya se encuentra registrado ---");
            return;
        }
        Pais_prefijo paisPrefijo = new Pais_prefijo(0, prefijo, nombre_pais);

        boolean ok = paisPrefijoDAO.registrarPaisPrefijo(paisPrefijo);
        if (ok) {
            System.out.println("==--== Prefijo registrado correctamente ==--==");
        } else {
            System.out.println("--- No se pudo registrar el prefijo ---");
        }
    }

    private static void editarPrefijo(Scanner sc, Pais_prefijoDAO paisPrefijoDAO) {
        System.out.println("\n-----------------------------");
        System.out.println("====== Editar prefijo ======");
        System.out.println("------------------------------");

        //mostrar prefijos
        listarPrefijos(paisPrefijoDAO);

        System.out.print("ID del prefijo a editar: ");
        int id = Integer.parseInt(sc.nextLine());

        //buscar prefijo que se quiere editar
        Pais_prefijo actual = paisPrefijoDAO.obtenerPrefijoPorId(id);

        if(actual == null) {
            System.out.println("El ID ingresado no existe");
            return;
        }

        System.out.print("Nuevo prefijo(Enter para mantener): ");
        String prefijo = sc.nextLine().trim();

        if(prefijo.isEmpty()) {
            prefijo = actual.getPrefijo();
        } else if (!prefijo.startsWith("+")) {
            prefijo = "+" + prefijo;
        }

        System.out.print("Nuevo nombre de pais: ");
        String nombre_pais = sc.nextLine().trim();

        if(nombre_pais.isEmpty()) {
            nombre_pais = actual.getNombre_pais();
        }

        //controlar campos vacios
        if(prefijo.equals("+") || nombre_pais.isEmpty()) {
            System.out.println("Debe ingresar valores validos en ambos campos");
            return;
        }
        Pais_prefijo paisPrefijo = new Pais_prefijo(id, prefijo, nombre_pais);

        boolean ok = paisPrefijoDAO.editarPrefijo(paisPrefijo);
        if (ok) {
            System.out.println("==--== Prefijo editado correctamente ==--==");
        } else {
            System.out.println("--- No se pudo editar el prefijo ---");
        }
    }

    private static void eliminarPrefijo(Scanner sc, Pais_prefijoDAO paisPrefijoDAO) {
        System.out.println("\n------------------------------");
        System.out.println("====== Eliminar prefijo ======");
        System.out.println("-------------------------------");

        listarPrefijos(paisPrefijoDAO);

        System.out.print("ID del prefijo a eliminar: ");
        int id = Integer.parseInt(sc.nextLine());

        boolean ok = paisPrefijoDAO.eliminarPrefijo(id);
        if (ok) {
            System.out.println("==--== Prefijo eliminado correctamente ==--==");
        } else {
            System.out.println("--- No se pudo eliminar el prefijo ---");
        }
    }

    private static void listarPrefijos(Pais_prefijoDAO paisPrefijoDAO) {
        List<Pais_prefijo> prefijos = paisPrefijoDAO.obtenerPrefijos();

        if(prefijos.isEmpty()) {
            System.out.println("\n=== No hay prefijos registrados ===");
        } else {
            System.out.println("\n==--== Prefijos registrados ==--==\n");
            for (Pais_prefijo p : prefijos) {
                System.out.println("ID: " + p.getId_prefijo());
                System.out.println("PREFIJO:" + p.getPrefijo());
                System.out.println("PAIS: " + p.getNombre_pais());
                System.out.println("-----------------------");
            }
        }
    }

    private static void registrarReporte(Scanner sc, ReportesDAO reportesDAO) {
        System.out.println("\n======================================");
        System.out.println("====== Registrar nuevo reporte ======");
        System.out.println("======================================\n");

        System.out.print("Cuerpo del SMS: ");
        String cuerpoSms = sc.nextLine().trim();

        System.out.print("¿Es un reporte manual? (s/n): ");
        boolean esManual = sc.nextLine().trim().equalsIgnoreCase("s");

        //solo se pide captura si el reporte es manual
        String capturaPantalla = null;
        if (esManual) {
            System.out.print("Ruta/nombre de la captura de pantalla: ");
            capturaPantalla = sc.nextLine().trim();
        }

        System.out.print("ID del usuario que reporta: ");
        int idUsuario = Integer.parseInt(sc.nextLine().trim());

        System.out.print("ID del numero reportado: ");
        int idNumReportado = Integer.parseInt(sc.nextLine().trim());

        //controlar campos obligatorios
        if (cuerpoSms.isEmpty() || (esManual && capturaPantalla.isEmpty())) {
            System.out.println("Debe ingresar valores validos en los campos obligatorios");
            return;
        }

        Usuario usuario = new Usuario();
        usuario.setId_usuario(idUsuario);

        Numero_reportado numeroReportado = new Numero_reportado();
        numeroReportado.setId_numero_reportado(idNumReportado);

        //id, fecha_hora y estado se resuelven solos dentro del DAO (autoincremental, ahora y pendiente)
        Reportes reporte = new Reportes(0, LocalDateTime.now(), cuerpoSms, capturaPantalla, esManual, estado_reporte.PENDIENTE, usuario, numeroReportado);

        boolean ok = reportesDAO.registrarReporte(reporte);
        if (ok) {
            System.out.println("==--== Reporte registrado correctamente (ID: " + reporte.getId_reporte() + ") ==--==");
        } else {
            System.out.println("--- No se pudo registrar el reporte ---");
        }
    }

    private static void listarReportes(ReportesDAO reportesDAO) {
        System.out.println("\n======================================");
        System.out.println("====== Listado de reportes ======");
        System.out.println("======================================\n");

        List<Reportes> lista = reportesDAO.listarReportes();
        mostrarReportes(lista);
    }

    private static void confirmarReporte(Scanner sc, ReportesDAO reportesDAO) {
        System.out.println("\n======================================");
        System.out.println("====== Confirmar reporte ======");
        System.out.println("======================================\n");

        System.out.print("ID del reporte a confirmar: ");
        int id = Integer.parseInt(sc.nextLine().trim());

        boolean ok = reportesDAO.confirmarReporte(id);
        if (ok) {
            System.out.println("==--== Reporte confirmado correctamente ==--==");
        } else {
            System.out.println("--- No se pudo confirmar el reporte (verificar que exista y este pendiente) ---");
        }
    }

    private static void registrarFalsoPositivo(Scanner sc, ReportesDAO reportesDAO) {
        System.out.println("\n======================================");
        System.out.println("====== Registrar falso positivo ======");
        System.out.println("======================================\n");

        System.out.print("ID del reporte a marcar como falso positivo: ");
        int id = Integer.parseInt(sc.nextLine().trim());

        boolean ok = reportesDAO.gestionarFalsoPositivo(id);
        if (ok) {
            System.out.println("==--== Reporte marcado como falso positivo ==--==");
        } else {
            System.out.println("--- No se pudo actualizar el reporte ---");
        }
    }

    private static void buscarReportesPorEstado(Scanner sc, ReportesDAO reportesDAO) {
        System.out.println("\n======================================");
        System.out.println("====== Buscar reportes por estado ======");
        System.out.println("======================================\n");

        System.out.println("Estados posibles: CONFIRMADO, PENDIENTE, FALSO_POSITIVO");
        System.out.print("Estado a buscar: ");
        String entrada = sc.nextLine().trim().toUpperCase();

        estado_reporte estado;
        try {
            estado = estado_reporte.valueOf(entrada);
        } catch (IllegalArgumentException e) {
            System.out.println("--- Estado invalido ---");
            return;
        }

        List<Reportes> lista = reportesDAO.buscarPorEstado(estado);
        mostrarReportes(lista);
    }

    //imprime una lista de reportes, la usan listarReportes y buscarReportesPorEstado
    private static void mostrarReportes(List<Reportes> lista) {
        if (lista.isEmpty()) {
            System.out.println("No se encontraron reportes.");
            return;
        }

        for (Reportes r : lista) {
            System.out.println("ID: " + r.getId_reporte() +
                    " | Fecha: " + r.getFecha_hora() +
                    " | Estado: " + r.getEstado_reporte() +
                    " | Manual: " + r.isEs_manual() +
                    " | Usuario ID: " + r.getUsuario().getId_usuario() +
                    " | Numero reportado ID: " + r.getNumero_reportado().getId_numero_reportado() +
                    " | SMS: " + r.getCuerpo_sms());
        }
    }

    private static void pausar(Scanner sc) {
        System.out.println("\nPresiona ENTER para continuar...");
        sc.nextLine();
    }

    private static void bloquearUsuario(Scanner sc, UsuarioDAO usuarioDAO) {

        System.out.println("\n======================================");
        System.out.println("          BLOQUEAR USUARIO");
        System.out.println("======================================");

        System.out.print("Ingrese el ID del usuario: ");
        int idUsuario = Integer.parseInt(sc.nextLine());

        Usuario usuario = usuarioDAO.obtenerUsuarioPorId(idUsuario);

        if (usuario == null) {
            System.out.println("No existe un usuario con ese ID.");
            return;
        }

        System.out.println("\nUsuario encontrado:");
        System.out.println("ID: " + usuario.getId_usuario());
        System.out.println("Nombre: " +
                usuario.getNombre() + " " + usuario.getApellido());
        System.out.println("Email: " + usuario.getEmail());

        if (usuario.isBloqueado()) {
            System.out.println("El usuario ya se encuentra bloqueado.");
            return;
        }

        System.out.print("\n¿Desea bloquear este usuario? (s/n): ");
        String confirmacion = sc.nextLine();

        if (!confirmacion.equalsIgnoreCase("s")) {
            System.out.println("Operacion cancelada.");
            return;
        }

        boolean ok = usuarioDAO.bloquearUsuario(idUsuario);

        if (ok) {
            System.out.println("Usuario bloqueado correctamente.");
        } else {
            System.out.println("No se pudo bloquear el usuario.");
        }
    }

    private static void eliminarUsuario(Scanner sc, UsuarioDAO usuarioDAO) {

        System.out.println("\n======================================");
        System.out.println("          ELIMINAR USUARIO");
        System.out.println("======================================");

        System.out.print("Ingrese el ID del usuario: ");
        int idUsuario = Integer.parseInt(sc.nextLine());

        Usuario usuario = usuarioDAO.obtenerUsuarioPorId(idUsuario);

        if (usuario == null) {
            System.out.println("No existe un usuario con ese ID.");
            return;
        }

        System.out.println("\nUsuario encontrado:");
        System.out.println("ID: " + usuario.getId_usuario());
        System.out.println("Nombre: " +
                usuario.getNombre() + " " + usuario.getApellido());
        System.out.println("Email: " + usuario.getEmail());

        System.out.print("\n¿Está seguro que desea eliminarlo? (s/n): ");
        String confirmacion = sc.nextLine();

        if (!confirmacion.equalsIgnoreCase("s")) {
            System.out.println("Operacion cancelada.");
            return;
        }

        boolean ok = usuarioDAO.eliminarUsuario(idUsuario);

        if (ok) {
            System.out.println("Usuario eliminado correctamente.");
        } else {
            System.out.println("No se pudo eliminar el usuario.");
        }
    }
}
