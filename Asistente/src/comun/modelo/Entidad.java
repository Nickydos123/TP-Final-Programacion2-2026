package comun.modelo;

import java.util.Objects;

/**
 * Clase base abstracta de todas las entidades del sistema que se guardan
 * en la base de datos. Solo aporta el identificador y la lógica de igualdad.
 *
 * Regla: id == 0 significa que la entidad es nueva y todavía no fue guardada.
 *
 * Advertencia: como hashCode depende del id, una entidad no debe usarse como
 * clave de un Map antes de ser guardada. Primero se guarda (el repositorio
 * asigna el id) y recién después se la agrega a mapas o sets.
 */
public abstract class Entidad {

    private int id;

    protected Entidad() {
        this(0);
    }

    protected Entidad(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("El id no puede ser negativo");
        }
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("El id no puede ser negativo");
        }
        this.id = id;
    }

    public boolean esNueva() {
        return id == 0;
    }

    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) {
            return true;
        }
        if (objeto == null || getClass() != objeto.getClass()) {
            return false;
        }
        Entidad otra = (Entidad) objeto;
        return id != 0 && id == otra.id;
    }

    @Override
    public int hashCode() {
        return id == 0 ? System.identityHashCode(this) : Objects.hash(getClass(), id);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{id=" + id + "}";
    }
}
