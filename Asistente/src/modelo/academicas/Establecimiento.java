package modelo.academicas;

import modelo.Entidad;

public class Establecimiento extends Entidad {
    private String nombre;
    private String direccion;
    private String localidad;

    public Establecimiento() {
    }

    public Establecimiento(int id, String nombre, String direccion, String localidad) {
        super(id);
        this.nombre = nombre;
        this.direccion = direccion;
        this.localidad = localidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }


    @Override
    public String toString() {
        return "Establecimiento{" +
                "nombre='" + nombre + '\'' +
                ", direccion='" + direccion + '\'' +
                ", localidad='" + localidad + '\'' +
                '}';
    }
}
