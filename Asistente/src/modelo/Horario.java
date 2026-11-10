package modelo;

import modelo.Enums.DiaSemana;

import java.time.LocalTime;

public class Horario extends Entidad{
    private DiaSemana dia;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Establecimiento establecimiento;

    public Horario() {
    }

    public Horario(int id, DiaSemana dia, LocalTime horaInicio, LocalTime horaFin, Establecimiento establecimiento) {
        super(id);
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.establecimiento = establecimiento;
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

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public Establecimiento getEstablecimiento() {
        return establecimiento;
    }

    public void setEstablecimiento(Establecimiento establecimiento) {
        this.establecimiento = establecimiento;
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
