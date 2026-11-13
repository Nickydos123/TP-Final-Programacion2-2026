package modelo.academicas;

import modelo.Alumno;
import modelo.Entidad;

import java.util.ArrayList;
import java.util.List;

public class Comision extends Entidad {
    private String nombre;
    private Materia materia;
    private List<Alumno> alumnos;
    private List<Horario> horarios;
    private List<Clase> clases;


    public Comision() {
    }

    public Comision(String nombre, Materia materia) {
        this(0, nombre, materia);
    }

    public Comision(int id, String nombre, Materia materia) {
        super(id);
        this.alumnos = new ArrayList<>();
        this.nombre = nombre;
        this.materia = materia;
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
