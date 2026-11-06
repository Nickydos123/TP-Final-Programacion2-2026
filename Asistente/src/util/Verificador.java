package util;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Verificador {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final String MSG_NUMERO_POSITIVO = "Opción no válida, intente ingresar un numero positivo.\n";

    private Verificador() {
    }

    public static int leerNumeroPositivoMax(int max) {
        do {
            try {
                int opcion = SCANNER.nextInt();
                SCANNER.nextLine();
                if (opcion >= 0 && opcion <= max) {
                    return opcion;
                } else {
                    System.out.println(MSG_NUMERO_POSITIVO + "(0/" + max + ")");
                }
            } catch (InputMismatchException | IllegalArgumentException e) {
                System.out.println(MSG_NUMERO_POSITIVO + "(0/" + max + ")");
                SCANNER.nextLine();
            }
        } while (true);
    }

    public static int leerNumeroPositivoMin(int min) {
        do {
            try {
                int opcion = SCANNER.nextInt();
                SCANNER.nextLine();
                if (opcion >= 0 && opcion >= min) {
                    return opcion;
                } else {
                    System.out.println(MSG_NUMERO_POSITIVO + "(Desde " + min + ")");
                }
            } catch (InputMismatchException | IllegalArgumentException e) {
                System.out.println(MSG_NUMERO_POSITIVO + "(Desde " + min + ")");
                SCANNER.nextLine();
            }
        } while (true);
    }

    public static int leerNumeroPositivoMinMax(int min, int max) {
        do {
            try {
                int opcion = SCANNER.nextInt();
                SCANNER.nextLine();
                if (opcion >= 0 && min <= opcion && max >= opcion) {
                    return opcion;
                } else {
                    System.out.println(MSG_NUMERO_POSITIVO + "Entre " + min + " y " + max);
                }
            } catch (InputMismatchException | IllegalArgumentException e) {
                System.out.println(MSG_NUMERO_POSITIVO + "Entre " + min + " y " + max);
                SCANNER.nextLine();
            }
        } while (true);
    }

    public static int leerEntero(String mensaje) {
        do {
            try {
                System.out.println(mensaje);
                int opcion = SCANNER.nextInt();
                SCANNER.nextLine();

                if (opcion > 0) {
                    return opcion;
                } else {
                    System.out.println("El número debe ser mayor que 0. Intente nuevamente.");
                }

            } catch (InputMismatchException e) {
                System.out.println(MSG_NUMERO_POSITIVO);
                SCANNER.nextLine();
            }
        } while (true);
    }

    public static Boolean leerBoolean(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String input = SCANNER.nextLine().trim().toLowerCase();
            if (input.equals("true") || input.equals("false")) {
                return Boolean.parseBoolean(input);
            } else {
                System.out.println("\nEntrada inválida. Debe ingresar true o false.");
            }
        }
    }

    public static Integer leerEdad(String mensaje) {
        System.out.println(mensaje);
        return Verificador.leerNumeroPositivoMinMax(16, 110);
    }

    public static String leerNombre(String msg) {
        String nombre;
        do {
            System.out.println(msg);
            nombre = SCANNER.nextLine().trim();

            if (!nombre.isEmpty() && nombre.matches("[a-zA-Záéíóú ]+")) {
                return nombre;
            } else {
                System.out.println("El nombre solo debe contener letras y espacios.");
            }

        } while (true);
    }

    public static String leerTexto(String mensaje) {
        String texto;
        do {
            System.out.println(mensaje);
            texto = SCANNER.nextLine().trim();
            if (!texto.isEmpty()) {
                return texto;
            } else {
                System.out.println("No se puede dejar el espacio vacio");
            }
        } while (true);
    }

    public static int leerPiso(String mensaje) {
        do {
            try {
                System.out.println(mensaje);
                int opcion = SCANNER.nextInt();
                SCANNER.nextLine();

                if (opcion >= 0) {
                    return opcion;
                } else {
                    System.out.println("El número debe ser mayor o igual que 0. Intente nuevamente.");
                }

            } catch (InputMismatchException e) {
                System.out.println(MSG_NUMERO_POSITIVO);
                SCANNER.nextLine();
            }
        } while (true);
    }

    public static String leerDni(String mensaje) {
        String dni;
        do {
            System.out.println(mensaje);
            dni = SCANNER.nextLine().trim();

            if (!dni.isEmpty() && dni.matches("\\d{8}")) {
                return dni;
            } else {
                System.out.println("El DNI debe contener solo numeros y debe tener 8 digitos. Intente nuevamente.");
            }
        } while (true);
    }
}
