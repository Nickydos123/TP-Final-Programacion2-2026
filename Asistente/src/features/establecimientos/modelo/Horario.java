package features.establecimientos.modelo;

import comun.excepcion.ValidacionException;
import comun.modelo.Entidad;
import comun.enums.DiaSemana;

import java.time.LocalTime;

public class Horario extends Entidad {
    private DiaSemana dia;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Establecimiento establecimiento;

    public Horario() {
    }

    public Horario(int id, DiaSemana dia, LocalTime horaInicio, LocalTime horaFin, Establecimiento establecimiento)
        throws ValidacionException
    {
        super(id);
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.establecimiento = establecimiento;
        validarHorario();
    }

    public DiaSemana getDia() {
        return dia;
    }

    public void setDia(DiaSemana dia) {
        this.dia = dia;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio)
        throws ValidacionException
    {
        if (horaInicio == null)
        {
            throw new ValidacionException("La hora de inicio es obligatoria");
        }
        if (horaFin != null && !horaInicio.isBefore(horaFin)) /// Si hora fin es null && la hora de inicio no es anterior a la hora fin
        {
            throw new ValidacionException ("La hora de inicio debe ser anterior a la hora de fin");
        }
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin)
        throws ValidacionException
    {
        if(horaFin == null)
        {
            throw new ValidacionException("La hora de fin es obligatoria");
        }
        if (horaInicio != null && !horaInicio.isBefore(horaFin))
        {
            throw new ValidacionException("La hora de inicio debe ser anterior a la hora de fin");
        }
        this.horaFin = horaFin;
    }

    public Establecimiento getEstablecimiento() {
        return establecimiento;
    }

    public void setEstablecimiento(Establecimiento establecimiento) {
        this.establecimiento = establecimiento;
    }

    public void validarHorario() throws ValidacionException {
        if (horaInicio == null || horaFin == null) {
            throw new ValidacionException("La hora de inicio y la hora de fin son obligatorias");
        }
        if (!horaInicio.isBefore(horaFin)) {
            throw new ValidacionException("La hora de inicio debe ser anterior a la hora de fin");
        }
    }

    @Override
    public String toString() {
        return "Horario{" +
                "dia=" + dia +
                ", horaInicio=" + horaInicio +
                ", horaFin=" + horaFin +
                ", establecimiento=" + establecimiento +
                '}';
    }
}
