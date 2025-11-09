package com.clinica.service;

import com.clinica.dao.HistoriaConsultaDAO;
import com.clinica.model.HistoriaConsulta;
import java.util.List;
import java.util.stream.Collectors;

public class HistoriaConsultaService implements IHistoriaConsultaService {
    private HistoriaConsultaDAO historiaConsultaDAO;

    public HistoriaConsultaService() {
        this.historiaConsultaDAO = new HistoriaConsultaDAO();
    }

    @Override
    public HistoriaConsulta registrarHistoriaConsulta(HistoriaConsulta historia) throws Exception {
        if (historia == null) {
            throw new Exception("La historia clínica no puede ser nula");
        }
        
        if (historia.getCita() == null) {
            throw new Exception("La cita asociada es obligatoria");
        }
        
        if (historia.getDiagnostico() == null || historia.getDiagnostico().trim().isEmpty()) {
            throw new Exception("El diagnóstico es obligatorio");
        }
        
        // Verificar si ya existe una historia para esta cita
        if (historiaConsultaDAO.obtenerHistoriasConsulta().stream().anyMatch(h -> h.getCita().getId() == historia.getCita().getId())) {
            throw new Exception("Ya existe una historia clínica para esta cita");
        }
        
        historiaConsultaDAO.guardarHistoriaConsulta(historia);
        return historia;
    }

    @Override
    public HistoriaConsulta actualizarHistoriaConsulta(HistoriaConsulta historia) throws Exception {
        if (historia == null || historia.getId() <= 0) {
            throw new Exception("Historia clínica inválida");
        }
        
        if (historiaConsultaDAO.obtenerHistoriasConsulta().stream().noneMatch(h -> h.getId() == historia.getId())) {
            throw new Exception("Historia clínica no encontrada con ID: " + historia.getId());
        }
        historiaConsultaDAO.actualizarHistoriaConsulta(historia);
        return historia;
    }

    @Override
    public void eliminarHistoriaConsulta(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("ID de historia clínica inválido");
        }
        
        if (historiaConsultaDAO.obtenerHistoriasConsulta().stream().noneMatch(h -> h.getId() == id)) {
            throw new Exception("Historia clínica no encontrada con ID: " + id);
        }
        historiaConsultaDAO.eliminarHistoriaConsulta(id);
    }

    @Override
    public HistoriaConsulta buscarPorId(int id) {
        return historiaConsultaDAO.obtenerHistoriasConsulta().stream()
                .filter(h -> h.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<HistoriaConsulta> listarHistoriasConsulta() {
        return historiaConsultaDAO.obtenerHistoriasConsulta();
    }

    @Override
    public List<HistoriaConsulta> buscarPorPaciente(String documentoPaciente) {
        return historiaConsultaDAO.obtenerHistoriasConsulta().stream()
                .filter(h -> h.getCita().getPaciente().getDocumento().equals(documentoPaciente))
                .collect(Collectors.toList());
    }

    @Override
    public List<HistoriaConsulta> buscarPorMedico(String documentoMedico) {
        return historiaConsultaDAO.obtenerHistoriasConsulta().stream()
                .filter(h -> h.getCita().getMedico().getDocumento().equals(documentoMedico))
                .collect(Collectors.toList());
    }

    @Override
    public HistoriaConsulta buscarPorCita(int idCita) {
        return historiaConsultaDAO.obtenerHistoriasConsulta().stream()
                .filter(h -> h.getCita().getId() == idCita)
                .findFirst()
                .orElse(null);
    }
}