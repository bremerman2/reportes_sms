package org.example;

import java.time.LocalDate;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Numero_oficialDAO dao = new Numero_oficialDAO();
        int opcion;

        do {
            System.out.println("\n=== ABM Numero Oficial ===");
            System.out.println("1. Registrar numero oficial");
            System.out.println("2. Editar numero oficial");
            System.out.println("3. Eliminar numero oficial");
            System.out.println("4. Salir");
            System.out.print("Elegi una opcion: ");
            opcion = Integer.parseInt(sc.nextLine());

            switch (opcion) {
                case 1:
                    registrar(sc, dao);
                    break;
                case 2:
                    editar(sc, dao);
                    break;
                case 3:
                    eliminar(sc, dao);
                    break;
                case 4:
                    System.out.println("Chau!");
                    break;
                default:
                    System.out.println("Opcion invalida");
            }

        } while (opcion != 4);

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
}