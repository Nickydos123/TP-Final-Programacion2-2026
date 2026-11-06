package menu;

import java.util.InputMismatchException;
import java.util.Scanner;


public class Verificador {

    static Scanner scanner = new Scanner(System.in);
    static String msgNumeroPositivo = "Opción no válida, intente ingresar un numero positivo.\n";


    public static int leerNumeroPositivoMax(int max) {
        do {
            try {
                int opcion = scanner.nextInt();
                scanner.nextLine();
                if (max >= opcion) {
                    return opcion;
                } else {
                    System.out.println(msgNumeroPositivo + "(0/" + max + ")");
                }
            } catch (InputMismatchException | IllegalArgumentException e) {
                System.out.println(msgNumeroPositivo + "(0/" + max + ")");
                scanner.nextLine();
            }
        } while (true);
    }

    public static int leerNumeroPositivoMin(int min) {
        do {
            try {
                int opcion = scanner.nextInt();
                scanner.nextLine();
                if (min <= opcion) {
                    return opcion;
                } else {
                    System.out.println(msgNumeroPositivo + "(0/" + min + ")");
                }
            } catch (InputMismatchException | IllegalArgumentException e) {
                System.out.println(msgNumeroPositivo + "(0/" + min + ")");
                scanner.nextLine();
            }
        } while (true);
    }

    public static int leerNumeroPositivoMinMax(int min, int max) {
        do {
            try {
                int opcion = scanner.nextInt();
                scanner.nextLine();
                if (min <= opcion && max >= opcion) {
                    return opcion;
                } else {
                    System.out.println(msgNumeroPositivo + "Entre " + min + " y " + max);
                }
            } catch (InputMismatchException | IllegalArgumentException e) {
                System.out.println(msgNumeroPositivo + "Entre " + min + " y " + max);
                scanner.nextLine();
            }
        } while (true);
    }

    //Para enteros
    public static int leerEntero(String mensaje) {
        do {
            try {
                System.out.println(mensaje);
                int opcion = scanner.nextInt();
                scanner.nextLine();

                if (opcion > 0) {
                    return opcion;
                } else {
                    System.out.println("El número debe ser mayor que 0. Intente nuevamente.");
                }

            } catch (InputMismatchException e) {
                System.out.println(msgNumeroPositivo);
                scanner.nextLine();
            }
        } while (true);
    }

    //Para boolean
    public static Boolean leerBoolean(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("true") || input.equals("false")) {
                return Boolean.parseBoolean(input);
            } else {
                System.out.println("\nEntrada inválida. Debe ingresar true o false.");
            }
        }
    }



    // Leer Edad
    public static Integer leerEdad(String mensaje){
        System.out.println(mensaje);
        return Verificador.leerNumeroPositivoMinMax(16, 110);

    }

    //Verificar nombres
    public static String leerNombre(String msg) {
        String nombre;
        do {
            System.out.println(msg);
            nombre = scanner.nextLine().trim();

            if (!nombre.isEmpty() && nombre.matches("[a-zA-Záéíóú ]+")) {
                //Por si ponen con acentos
                return nombre;
            } else {
                System.out.println("El nombre solo debe contener letras y espacios.");
            }

        } while (true);
    }

    //Verificar que el usuario no ponga nada en blanco
    public static String leerTexto(String mensaje) {
        String texto;
        do {
            System.out.println(mensaje);
            texto = scanner.nextLine().trim();
            if (!texto.isEmpty()) {
                return texto;
            } else {
                System.out.println("No se puede dejar el espacio vacio");
            }
        } while (true);
    }

    //Verificar piso (Porque si puede ser 0)
    public static int leerPiso(String mensaje) {
        do {
            try {
                System.out.println(mensaje);
                int opcion = scanner.nextInt();
                scanner.nextLine();

                if (opcion >= 0) {
                    return opcion;
                } else {
                    System.out.println("El número debe ser mayor o igual que 0. Intente nuevamente.");
                }

            } catch (InputMismatchException e) {
                System.out.println(msgNumeroPositivo);
                scanner.nextLine();
            }
        } while (true);
    }

    //Verificar dni
    public static String leerDni(String mensaje){
        String dni;
        do {
            System.out.println(mensaje);
            dni = scanner.nextLine().trim();

            if (!dni.isEmpty() && dni.matches("\\d{8}")) {
                return dni;
            } else {
                System.out.println("El DNI debe contener solo numeros y debe tener 8 digitos. Intente nuevamente.");
            }
        } while (true);

    }

}
