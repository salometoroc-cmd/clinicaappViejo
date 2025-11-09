package com.clinica.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;


/**
 * Representa una cita médica entre un paciente y un médico.
 */
public class Cita {
    private int id; 
    private LocalDate fecha;
    private LocalTime hora;
    private Paciente paciente;
    private Medico medico;
    private EstadoCita estado;

 
    public Cita() {}

   
    public Cita(LocalDate fecha, LocalTime hora, Paciente paciente, Medico medico) {
        this.fecha = fecha;
        this.hora = hora;
        this.paciente = paciente;
        this.medico = medico;
        this.estado = EstadoCita.PROGRAMADA;
    }

    // Constructor para citas existentes 
    public Cita(int id, LocalDate fecha, LocalTime hora, Paciente paciente, Medico medico) {
        this(fecha, hora, paciente, medico); // Llama al constructor de arriba
        this.id = id; 
    }

    //Getters y Setters
    public int getId() { 
        return id; 
    }
    
    public LocalDate getFecha() { 
        return fecha; 
    } 
    public LocalTime getHora() {
        return hora; 
    } 
        
    public Paciente getPaciente() { 
        return paciente; 
    } 
    
    public Medico getMedico() { 
        return medico; 
    } 
    
    public EstadoCita getEstado() { 
        return estado; 
    } 
    
    public void setId(int id) { 
        this.id = id; 
    }

    public void setFecha(LocalDate fecha) { 
        this.fecha = fecha; 
    } 
    
    public void setHora(LocalTime hora) { 
        this.hora = hora; 
    }
    
    public void setEstado(EstadoCita estado) { 
        this.estado = estado; 
    }
   
    public String getCodigo() {
        return "#" + id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Cita cita = (Cita) obj;
        return id == cita.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        return "Cita " + getCodigo() + " - " + fecha.format(dateFormatter) + " " + hora.format(timeFormatter) +
               " | Médico: " + medico.getNombreCompleto() +
               " | Paciente: " + paciente.getNombreCompleto() +
               " | Estado: " + estado;
    }
}
