package com.clinica.service;

import com.clinica.model.HistoriaConsulta;
import java.util.List;

public interface IHistoriaConsultaService {
    HistoriaConsulta registrarHistoriaConsulta(HistoriaConsulta historia) throws Exception;
    HistoriaConsulta actualizarHistoriaConsulta(HistoriaConsulta historia) throws Exception;
    void eliminarHistoriaConsulta(int id) throws Exception;
    HistoriaConsulta buscarPorId(int id);
    List<HistoriaConsulta> listarHistoriasConsulta();
    List<HistoriaConsulta> buscarPorPaciente(String documentoPaciente);
    List<HistoriaConsulta> buscarPorMedico(String documentoMedico);
    HistoriaConsulta buscarPorCita(int idCita);
}