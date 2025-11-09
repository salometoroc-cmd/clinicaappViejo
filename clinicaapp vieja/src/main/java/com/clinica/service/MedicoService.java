package com.clinica.service;

import com.clinica.dao.MedicoDAO;
import com.clinica.model.Medico;
import java.util.List;
import java.util.stream.Collectors;

public class MedicoService implements IMedicoService{
    private MedicoDAO medicoDAO;

    public MedicoService() {
        this.medicoDAO = new MedicoDAO();
    }

    @Override
    public Medico registrarMedico(Medico medico) throws Exception {
        if (medico == null) {
            throw new Exception("El médico no puede ser nulo.");
        }
        if (medico.getDocumento() == null || medico.getDocumento().isEmpty()) {
            throw new Exception("El documento del médico es obligatorio.");
        }
        if (medicoDAO.obtenerMedicos().stream().anyMatch(m -> m.getDocumento().equals(medico.getDocumento()))) {
            throw new Exception("Ya existe un médico con ese documento.");
        }

        medicoDAO.agregarMedico(medico);
        return medico;
    }
    @Override
    public Medico actualizarMedico(Medico medico) throws Exception {
        if (medico == null || medico.getDocumento()== null || medico.getDocumento().isEmpty()) {
            throw new Exception("Datos del médico inválidos.");
        }

        if (medicoDAO.obtenerMedicos().stream().noneMatch(m -> m.getDocumento().equals(medico.getDocumento()))) {
            throw new Exception("No se encontró el médico para actualizar.");
        }
        medicoDAO.actualizarMedico(medico);
        return medico;
    }

    @Override
    public void eliminarMedico(String documento) throws Exception {
        if (documento == null || documento.isEmpty()) {
            throw new Exception("El documento no puede ser nulo o vacío.");
        }
        
        if (medicoDAO.obtenerMedicos().stream().noneMatch(m -> m.getDocumento().equals(documento))) {
            throw new Exception("No se encontró el médico para eliminar.");
        }
        medicoDAO.eliminarMedico(documento);
    }

    @Override
    public Medico buscarPorDocumento(String documento) {
        if (documento == null || documento.isEmpty()) {
            return null;
        }
        
        return medicoDAO.obtenerMedicos().stream()
                .filter(m -> documento.equals(m.getDocumento()))
                .findFirst()
                .orElse(null);
    }
    

    @Override
    public List<Medico> listarMedicos() {
        return medicoDAO.obtenerMedicos();
    }

    @Override
    public List<Medico> listarPorEspecialidad(String nombreEspecialidad) {
        return medicoDAO.obtenerMedicos().stream()
                .filter(m -> m.getEspecialidad() != null && 
                             m.getEspecialidad().getNombre().equalsIgnoreCase(nombreEspecialidad))
                .collect(Collectors.toList());
    }
}