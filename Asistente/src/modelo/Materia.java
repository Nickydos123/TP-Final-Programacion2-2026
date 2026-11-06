package modelo;

import java.util.ArrayList;
import java.util.List;

public class Materia extends Entidad {
    private String codigo;
    private String nombre;
    private final List<Comision> comisiones = new ArrayList<>();

    public Materia() {
    }

    public Materia(String codigo, String nombre) {
        this(0, codigo, nombre);
    }

    public Materia(int id, String codigo, String nombre) {
        super(id);
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
        return "Materia{" +
                "id=" + getId() +
                ", codigo='" + codigo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", comisiones=" + comisiones +
                '}';
    }
}
