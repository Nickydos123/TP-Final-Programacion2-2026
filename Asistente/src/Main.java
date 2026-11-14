import excepcion.EntidadNoEncontradaException;
import excepcion.PersistenciaException;
import modelo.Alumno;
import persistencia.AlumnoRepositorio;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        /**
         * Crear evento agenda.
         *         Clase:
         *          -agregarMaterial();
         *         Materia:
         *          -quitarComision()
         *         Ayudante:
         *          -quitarComision()
         *         comision:
         *         Agregar:
     *                      -agregarHorario()
         *                 -quitarHorario()
         *                 -agregarClase()
         *                 -quitarClase
         *                 */

        Alumno rafaSantarone = new Alumno("Rafael","Santarone","40307052","rafa.d.santa@gmail.com","2235335564","a1");
        Alumno Rsanta2 = new Alumno("Rafael","Dopplegenger","40307051","rafa.d.santa@gmail.com","2235335564","a2");
        AlumnoRepositorio alumnosR = new AlumnoRepositorio();

        try {
             alumnosR.eliminar(6);
            alumnosR.guardar(rafaSantarone);
            System.out.println(alumnosR.listar());
            System.out.println(alumnosR.buscarPorId(7));
            alumnosR.guardar(Rsanta2);
            System.out.println(alumnosR.listar());
            System.out.println(alumnosR.buscarPorId(8));
        }
        catch (PersistenciaException  | EntidadNoEncontradaException e)
        {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }




    }
}
