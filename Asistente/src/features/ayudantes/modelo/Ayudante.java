package features.ayudantes.modelo;

import features.alumnos.modelo.Alumno;
import comun.modelo.Persona;
import features.materias.modelo.Comision;

import java.util.ArrayList;
import java.util.List;

public class Ayudante extends Persona {
    private String area;
    private Alumno alumnoVinculado;
    private List<Comision> comisiones = new ArrayList<>(); /// Lista de comisiones a la cual el ayudante pertenece

    public Ayudante() {
    }

    public Ayudante(String nombre, String apellido, String dni, String email, String telefono, String area) {
        super(nombre, apellido, dni, email, telefono);
        this.comisiones = new ArrayList<>();
        this.area = area;
    }

    public Ayudante(int id, String nombre, String apellido, String dni, String email, String telefono, String area) {
        super(id, nombre, apellido, dni, email, telefono);
        this.area = area;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Alumno getAlumnoVinculado() {
        return alumnoVinculado;
    }

    public void setAlumnoVinculado(Alumno alumnoVinculado) {
        this.alumnoVinculado = alumnoVinculado;
    }

    public List<Comision> getComisiones() {
        return comisiones;
    }

    public void agregarComision(Comision comision) {
        if (comision != null && !comisiones.contains(comision)) {
            comisiones.add(comision);
        }
    }

    public void quitarComision(Comision comision) {
        comisiones.remove(comision);
    }

    @Override
    public String toString() {
        return "Ayudante{" +
                "id=" + getId() +
                ", nombre='" + getNombre() + ' ' + getApellido() + '\'' +
                ", area='" + area + '\'' +
                ", alumnoVinculado=" + alumnoVinculado +
                ", comisiones=" + comisiones +
                '}';
    }
}
