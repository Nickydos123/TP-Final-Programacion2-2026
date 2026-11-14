package features.materias.modelo;

import features.alumnos.modelo.Alumno;
import features.establecimientos.modelo.Establecimiento;
import features.establecimientos.modelo.Horario;
import features.clases.modelo.Clase;
import comun.modelo.Entidad;
import features.materias.modelo.materia.model.Materia;

import java.util.ArrayList;
import java.util.List;

public class Comision extends Entidad {
    private String nombre;
    private Materia materia;
    private Establecimiento establecimiento;
    private List<Alumno> alumnos;
    private List<Horario> horarios;
    private List<Clase> clases;


    public Comision() {
        this.alumnos = new ArrayList<>();
        this.horarios = new ArrayList<>();
        this.clases = new ArrayList<>();
    }

    public Comision(String nombre, Materia materia) {
        this(0, nombre, materia, null);
    }

    public Comision(String nombre, Materia materia, Establecimiento establecimiento) {
        this(0, nombre, materia, establecimiento);
    }

    public Comision(int id, String nombre, Materia materia) {
        this(id, nombre, materia, null);
    }

    public Comision(int id, String nombre, Materia materia, Establecimiento establecimiento) {
        super(id);
        this.alumnos = new ArrayList<>();
        this.horarios = new ArrayList<>();
        this.clases = new ArrayList<>();
        this.nombre = nombre;
        this.materia = materia;
        this.establecimiento = establecimiento;
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    public Establecimiento getEstablecimiento() {
        return establecimiento;
    }

    public void setEstablecimiento(Establecimiento establecimiento) {
        this.establecimiento = establecimiento;
    }

    public List<Alumno> getAlumnos() {
        return alumnos;
    }

    public void agregarAlumno(Alumno alumno) {
        if (alumno != null && !alumnos.contains(alumno)) {
            alumnos.add(alumno);
        }
    }

    public void quitarAlumno(Alumno alumno) {
        alumnos.remove(alumno);
    }

    /**
     Agregar:
     -agregarHorario()
     -quitarHorario()
     -agregarClase()
     -quitarClase
     */


    @Override
    public String toString() {
        return "Comision{" +
                "id=" + getId() +
                ", nombre='" + nombre + '\'' +
                ", materia=" + materia +
                ", alumnos=" + alumnos +
                '}';
    }
}
