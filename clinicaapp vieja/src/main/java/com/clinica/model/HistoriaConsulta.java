package com.clinica.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Representa el historial de una consulta médica realizada.
 * Relaciona una cita con el diagnóstico, tratamiento y observaciones.
 */
public class HistoriaConsulta {
    private int id;
    private Cita cita;
    private String diagnostico;
    private String tratamiento;
    private String observaciones;
    private String medicamentosRecetados;
    private LocalDateTime fechaConsulta;
    private boolean seguimientoRequerido;
    private LocalDateTime fechaProximoSeguimiento;

    // Constructores
    public HistoriaConsulta() {}

    public HistoriaConsulta(Cita cita, String diagnostico, String tratamiento, 
                           String observaciones, String medicamentosRecetados) {
        this.cita = cita;
        this.diagnostico = diagnostico;
        this.tratamiento = tratamiento;
        this.observaciones = observaciones;
        this.medicamentosRecetados = medicamentosRecetados;
        this.fechaConsulta = LocalDateTime.now();
        this.seguimientoRequerido = false;
    }

    public HistoriaConsulta(int id, Cita cita, String diagnostico, String tratamiento,
                           String observaciones, String medicamentosRecetados, 
                           LocalDateTime fechaConsulta, boolean seguimientoRequerido,
                           LocalDateTime fechaProximoSeguimiento) {
        this(cita, diagnostico, tratamiento, observaciones, medicamentosRecetados);
        this.id = id;
        this.fechaConsulta = fechaConsulta;
        this.seguimientoRequerido = seguimientoRequerido;
        this.fechaProximoSeguimiento = fechaProximoSeguimiento;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cita getCita() {
        return cita;
    }

    public void setCita(Cita cita) {
        this.cita = cita;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getMedicamentosRecetados() {
        return medicamentosRecetados;
    }

    public void setMedicamentosRecetados(String medicamentosRecetados) {
        this.medicamentosRecetados = medicamentosRecetados;
    }

    public LocalDateTime getFechaConsulta() {
        return fechaConsulta;
    }

    public void setFechaConsulta(LocalDateTime fechaConsulta) {
        this.fechaConsulta = fechaConsulta;
    }

    public boolean isSeguimientoRequerido() {
        return seguimientoRequerido;
    }

    public void setSeguimientoRequerido(boolean seguimientoRequerido) {
        this.seguimientoRequerido = seguimientoRequerido;
    }

    public LocalDateTime getFechaProximoSeguimiento() {
        return fechaProximoSeguimiento;
    }

    public void setFechaProximoSeguimiento(LocalDateTime fechaProximoSeguimiento) {
        this.fechaProximoSeguimiento = fechaProximoSeguimiento;
    }

    // Métodos de utilidad
    public String getCodigo() {
        return "HC-" + id;
    }

    public String getResumen() {
        return "Consulta #" + id + " - " + cita.getPaciente().getNombreCompleto() + 
               " - " + diagnostico;
    }

    public boolean requiereSeguimiento() {
        return seguimientoRequerido && fechaProximoSeguimiento != null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        HistoriaConsulta that = (HistoriaConsulta) obj;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "HistoriaConsulta{" +
                "id=" + id +
                ", paciente=" + cita.getPaciente().getNombreCompleto() +
                ", medico=" + cita.getMedico().getNombreCompleto() +
                ", diagnostico='" + diagnostico + '\'' +
                ", fechaConsulta=" + fechaConsulta +
                '}';
    }
}