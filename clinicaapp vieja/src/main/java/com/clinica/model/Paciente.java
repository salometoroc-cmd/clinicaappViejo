package com.clinica.model;

import java.time.LocalDate;
import java.time.Period;



public class Paciente extends Persona {
    private String direccion;
    private LocalDate fechaNacimiento;
    private String historialMedico;

    // Constructor
    public Paciente(String documento, String nombre, String apellido, String email, String telefono,
                    String direccion, LocalDate fechaNacimiento, String historialMedico) {
        super(documento, nombre, apellido, email, telefono);
        this.direccion = direccion;
        this.fechaNacimiento = fechaNacimiento;
        this.historialMedico = historialMedico;
    }

    // Getters y Setters
    public String getDireccion() {
        return direccion;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public String getHistorialMedico() {
        return historialMedico;
    }
    
    public void setDireccion(String direccion) { 
        this.direccion = direccion; 
    }
    
    public void setFechaNacimiento(LocalDate fechaNacimiento) { 
        this.fechaNacimiento = fechaNacimiento; 
    }
    
    public void setHistorialMedico(String historialMedico) { 
        this.historialMedico = historialMedico; 
    }
    
    public int getEdad() {
        if (fechaNacimiento == null) 
            return 0;
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }

    
    @Override
    public TipoPersona getTipoPersona() {
        return TipoPersona.PACIENTE;
    }
    
    @Override
    public String toString() {
        return getNombreCompleto() + "(" + getDocumento() + ")";
    }
}
