package com.clinica.service;

import com.clinica.model.Medico;
import java.util.List;

public interface IMedicoService {
    Medico registrarMedico(Medico medico) throws Exception;
    Medico actualizarMedico(Medico medico) throws Exception;
    void eliminarMedico(String documento) throws Exception;
    Medico buscarPorDocumento(String documento);
    List<Medico> listarMedicos();
    List<Medico> listarPorEspecialidad(String nombreEspecialidad);
}