package com.clinica.service;
import com.clinica.dao.CitaDAO;
import com.clinica.model.Cita;
import com.clinica.model.EstadoCita;
import java.time.LocalDate;
import java.util.List;

public class CitaService implements ICitaService {

    private CitaDAO citaDAO;

    public CitaService() {
        this.citaDAO = new CitaDAO();
    }

   @Override
    public void crearCita(Cita cita) throws Exception {
        // Validar que la cita no sea nula
        if (cita == null) {
            throw new Exception("La cita no puede ser nula");
        }
        
        validarCamposObligatorios(cita);
        
        if (existeCitaDuplicada(cita)) {
            throw new Exception("Ya existe una cita para este paciente en la misma fecha y hora");
        }
        
        if (cita.getFecha().isBefore(LocalDate.now())) {
            throw new Exception("No se pueden crear citas en fechas pasadas");
        }
        
        // ID will be handled by DAO if needed, or assigned before calling DAO
        citaDAO.agregarCita(cita);
    }
    
    @Override
        public void cancelarCita(int id) throws Exception {
            if (id <= 0) {
                throw new Exception("ID de cita inválido");
        }
        
        Cita cita = buscarPorId(id);
        if (cita != null) {
            
            if (cita.getEstado() == EstadoCita.CANCELADA) {
                throw new Exception("La cita ya está cancelada");
            }
            
            
            if (cita.getEstado() == EstadoCita.FINALIZADA) {
                throw new Exception("No se puede cancelar una cita ya completada");
            }
            
            cita.setEstado(EstadoCita.CANCELADA);
            citaDAO.actualizarCita(cita);
        } else {
            throw new Exception("Cita no encontrada con ID: " + id);
        }
    }
    
    @Override 
    public void reprogramarCita(int id, LocalDate nuevaFecha) throws Exception {
        
        if (id <= 0) {
            throw new Exception("ID de cita inválido");
        }
        
        
        if (nuevaFecha == null) {
            throw new Exception("La nueva fecha no puede ser nula");
        }
        
        if (nuevaFecha.isBefore(LocalDate.now())) {
            throw new Exception("No se puede reprogramar a una fecha pasada");
        }
        
        Cita cita = buscarPorId(id);
        if (cita != null) {
            
            if (cita.getEstado() == EstadoCita.CANCELADA) {
                throw new Exception("No se puede reprogramar una cita cancelada");
            }
            
            
            if (cita.getEstado() == EstadoCita.FINALIZADA) {
                throw new Exception("No se puede reprogramar una cita completada");
            }
            
            
            boolean existeDuplicado = citaDAO.obtenerCitas().stream()
                .anyMatch(c -> c.getPaciente().equals(cita.getPaciente()) &&
                              c.getFecha().equals(nuevaFecha) &&
                              c.getHora().equals(cita.getHora()) &&
                              c.getEstado() != EstadoCita.CANCELADA &&
                              c.getId() != id); // Excluir la cita actual
            
            if (existeDuplicado) {
                throw new Exception("Ya existe una cita para este paciente en la nueva fecha y hora");
            }
            
            cita.setFecha(nuevaFecha);
            citaDAO.actualizarCita(cita);
        } else {
            throw new Exception("Cita no encontrada con ID: " + id);
        }
    }

    @Override 
    public Cita buscarPorId(int id) {
        if (id <= 0) {
            return null;
        }
        
        return citaDAO.obtenerCitas().stream()
                    .filter(c -> c.getId() == id)
                    .findFirst()
                    .orElse(null);
    }

    @Override 
    public List<Cita> listarCitas() {
        return citaDAO.obtenerCitas();
    }
    
    
    private void validarCamposObligatorios(Cita cita) throws Exception {
        if (cita.getPaciente() == null) {
            throw new Exception("El paciente es obligatorio");
        }
        
        if (cita.getFecha() == null) {
            throw new Exception("La fecha de la cita es obligatoria");
        }
        
        if (cita.getHora() == null) {
            throw new Exception("La hora de la cita es obligatoria");
        }
        
        if (cita.getMedico() == null) {
            throw new Exception("El médico es obligatorio");
        }
    }
    
    private boolean existeCitaDuplicada(Cita cita) {
        return citaDAO.obtenerCitas().stream()
                   .anyMatch(c -> c.getPaciente().equals(cita.getPaciente()) &&
                                 c.getFecha().equals(cita.getFecha()) &&
                                 c.getHora().equals(cita.getHora()) &&
                                 c.getEstado() != EstadoCita.CANCELADA);
    }
}