import comun.excepcion.EntidadNoEncontradaException;
import comun.excepcion.PersistenciaException;
import features.alumnos.modelo.Alumno;
import features.alumnos.persistencia.AlumnoRepositorio;
import features.ayudantes.modelo.Ayudante;
import features.ayudantes.persistencia.AyudanteRepositorio;
import features.establecimientos.modelo.Establecimiento;
import features.establecimientos.modelo.persistencia.EstablecimientoRepositorio;
import features.materias.modelo.materia.model.Materia;
import features.materias.modelo.materia.persistencia.MateriaRepositorio;

import java.util.Arrays;

public class TestRepositories {
    static void main() {
        Alumno rafaSantarone = new Alumno("Rafael","Santarone","40307052","rafa.d.santa@gmail.com","2235335564","a1");
        Alumno Rsanta2 = new Alumno("Rafael","Dopplegenger","40307051","rafa.d.santa@gmail.com","2235335564","a2");
        AlumnoRepositorio alumnosR = new AlumnoRepositorio();

        try {
            /// alumnosR.guardar(rafaSantarone);
            System.out.println(alumnosR.listar());
            System.out.println(alumnosR.buscarPorId(1));
            /// alumnosR.guardar(Rsanta2);
            System.out.println(alumnosR.listar());
            System.out.println(alumnosR.buscarPorId(2));
        }
        catch (PersistenciaException  | EntidadNoEncontradaException e)
        {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }

        System.out.println("-------------------------------------------------------------------------------------------");
        System.out.println("-------------------------------------------------------------------------------------------");
        System.out.println("-------------------------------------------------------------------------------------------");


        Ayudante ayudante1 = new Ayudante("Rafael", "Santarone", "40307053", "rafa.d.santa@gmail.com", "2235335564", "Programacion");
        Ayudante ayudante2 = new Ayudante("Rafael", "Dopplegenger", "40307054", "rafa.d.santa@gmail.com", "2235335564", "Matematica");
        AyudanteRepositorio ayudantesR = new AyudanteRepositorio();

        try {
            /// ayudantesR.guardar(ayudante1);
            System.out.println(ayudantesR.listar());
            System.out.println(ayudantesR.buscarPorId(1));

            /// ayudantesR.guardar(ayudante2);
            System.out.println(ayudantesR.listar());
            System.out.println(ayudantesR.buscarPorId(2));

           /// ayudantesR.eliminar(ayudante1.getId());
            System.out.println(ayudantesR.listar());
        } catch (PersistenciaException | EntidadNoEncontradaException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }

        System.out.println("-------------------------------------------------------------------------------------------");
        System.out.println("-------------------------------------------------------------------------------------------");
        System.out.println("-------------------------------------------------------------------------------------------");

        Materia programacion = new Materia("PROG2", "Programación II");
        Materia baseDatos = new Materia("BD1", "Base de Datos I");
        MateriaRepositorio materiasR = new MateriaRepositorio();

        try {
            /// materiasR.guardar(programacion);
            System.out.println(materiasR.listar());
            System.out.println(materiasR.buscarPorId(1));

            /// materiasR.guardar(baseDatos);
            System.out.println(materiasR.listar());
            System.out.println(materiasR.buscarPorId(2));

            programacion.setNombre("Programación II (actualizada)");
            materiasR.actualizar(programacion);
            System.out.println(materiasR.buscarPorId(1));

            /// materiasR.eliminar(baseDatos.getId());
            System.out.println(materiasR.listar());
        } catch (PersistenciaException | EntidadNoEncontradaException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }

        System.out.println("-------------------------------------------------------------------------------------------");
        System.out.println("-------------------------------------------------------------------------------------------");
        System.out.println("-------------------------------------------------------------------------------------------");

        Establecimiento central = new Establecimiento(0, "Sede Central", "Av. Siempre Viva 742", "Springfield");
        Establecimiento anexo = new Establecimiento(0, "Anexo Norte", "Calle Falsa 123", "Shelbyville");
        EstablecimientoRepositorio establecimientosR = new EstablecimientoRepositorio();


        try {
            establecimientosR.guardar(central);
            System.out.println(establecimientosR.listar());
            System.out.println(establecimientosR.buscarPorId(central.getId()));

            establecimientosR.guardar(anexo);
            System.out.println(establecimientosR.listar());
            System.out.println(establecimientosR.buscarPorId(anexo.getId()));

            central.setLocalidad("Springfield Centro");
            establecimientosR.actualizar(central);
            System.out.println(establecimientosR.buscarPorId(central.getId()));

            establecimientosR.eliminar(anexo.getId());
            System.out.println(establecimientosR.listar());
        } catch (PersistenciaException | EntidadNoEncontradaException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }


    }
}
