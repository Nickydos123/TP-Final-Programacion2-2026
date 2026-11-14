import comun.excepcion.EntidadNoEncontradaException;
import comun.excepcion.PersistenciaException;
import features.alumnos.modelo.Alumno;
import features.alumnos.persistencia.AlumnoRepositorio;
import features.ayudantes.modelo.Ayudante;
import features.ayudantes.persistencia.AyudanteRepositorio;

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

        Ayudante ayudante1 = new Ayudante("Rafael", "Santarone", "40307053", "rafa.d.santa@gmail.com", "2235335564", "Programacion");
        Ayudante ayudante2 = new Ayudante("Rafael", "Dopplegenger", "40307054", "rafa.d.santa@gmail.com", "2235335564", "Matematica");
        AyudanteRepositorio ayudantesR = new AyudanteRepositorio();

        try {
            ayudantesR.guardar(ayudante1);
            System.out.println(ayudantesR.listar());
            System.out.println(ayudantesR.buscarPorId(ayudante1.getId()));

            ayudantesR.guardar(ayudante2);
            System.out.println(ayudantesR.listar());
            System.out.println(ayudantesR.buscarPorId(ayudante2.getId()));

            ayudantesR.eliminar(ayudante1.getId());
            System.out.println(ayudantesR.listar());
        } catch (PersistenciaException | EntidadNoEncontradaException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }


    }
}
