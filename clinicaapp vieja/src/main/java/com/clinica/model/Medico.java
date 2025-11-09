package com.clinica.model;

public class Medico extends Persona {
    private Especialidad especialidad;
    
    // Constructor
    public Medico(String documento, String nombre, String apellido, String email, String telefono,
                    Especialidad especialidad) {
        super(documento, nombre, apellido, email, telefono);
        this.especialidad = especialidad;
    }
    
    // Getters y Setters
    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }

    
    @Override
    public TipoPersona getTipoPersona() {
        return TipoPersona.MEDICO;
    }
    
    @Override
    public String toString() {
        return getNombreCompleto() + "(" + especialidad.getNombre() + ")";
    }
    
}
