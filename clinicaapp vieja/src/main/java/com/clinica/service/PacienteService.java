package com.clinica.service;

import com.clinica.dao.PacienteDAO;
import com.clinica.model.Paciente;
import java.util.List;

public class PacienteService implements IPacienteService{
    private PacienteDAO pacienteDAO;

    public PacienteService() {
        this.pacienteDAO = new PacienteDAO();
    }

    @Override
    public Paciente registrarPaciente(Paciente paciente) throws Exception {
        if (paciente == null) {
            throw new Exception("El paciente no puede ser nulo.");
        }
        if (paciente.getDocumento() == null || paciente.getDocumento().isEmpty()) {
            throw new Exception("El documento del paciente es obligatorio.");
        }
        // Check for existing patient by document
        if (pacienteDAO.obtenerPacientes().stream().anyMatch(p -> p.getDocumento().equals(paciente.getDocumento()))) {
            throw new Exception("Ya existe un paciente con ese documento.");
        }
        pacienteDAO.agregarPaciente(paciente);
        return paciente;
    }

    @Override
    public Paciente actualizarPaciente(Paciente paciente) throws Exception {
        if (paciente == null || paciente.getDocumento() == null || paciente.getDocumento().isEmpty()) {
            throw new Exception("Datos del paciente inválidos.");
        }
        if (pacienteDAO.obtenerPacientes().stream().noneMatch(p -> p.getDocumento().equals(paciente.getDocumento()))) {
            throw new Exception("No se encontró el paciente para actualizar.");
        }
        pacienteDAO.actualizarPaciente(paciente);
        return paciente;
    }

    @Override
    public void eliminarPaciente(String documento) throws Exception {
        if (pacienteDAO.obtenerPacientes().stream().noneMatch(p -> p.getDocumento().equals(documento))) {
            throw new Exception("No se encontró el paciente con documento: " + documento);
        }
        pacienteDAO.eliminarPaciente(documento);
    }
    
    @Override
    public Paciente buscarPorDocumento(String documento) {
        return pacienteDAO.obtenerPacientes().stream()
                .filter(p -> p.getDocumento().equals(documento))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Paciente> listarPacientes() {
        return pacienteDAO.obtenerPacientes();
    }
}