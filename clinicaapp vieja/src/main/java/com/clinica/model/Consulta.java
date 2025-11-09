package com.clinica.model;

import java.util.Objects;

public class Consulta {
    private int id; 
    private Cita cita;
    private String diagnostico;
    private String receta; 

    // Constructores
    public Consulta() {} 

    public Consulta(Cita cita, String diagnostico, String receta) {
        this.cita = cita;
        this.diagnostico = diagnostico;
        this.receta = receta;
    }

    public Consulta(int id, Cita cita, String diagnostico, String receta) {
        this(cita, diagnostico, receta); 
        this.id = id;
    }


    //Getters y Setters
    public int getId() { 
        return id; 
    }
    public Cita getCita() { 
        return cita; 
    }
    public String getDiagnostico() { 
        return diagnostico; 
    }
    public String getReceta() { 
        return receta; 
    }

    public void setId(int id) { 
    this.id = id; 
    }

    public void setCita(Cita cita) { 
    this.cita = cita; 
    }

    public void setDiagnostico(String diagnostico) { 
        this.diagnostico = diagnostico; 
    }
    
    public void setReceta(String receta) { 
        this.receta = receta; 
    }

    public String getCodigo() {
        return "#" + id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Consulta consulta = (Consulta) obj;
        return id == consulta.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Consulta #" + id + " | Paciente: " + cita.getPaciente().getNombre() +
               " | Médico: " + cita.getMedico().getNombre() +
               " | Diagnóstico: " + diagnostico;

    }


}
