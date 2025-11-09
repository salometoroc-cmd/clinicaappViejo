package com.clinica.service;

import com.clinica.model.Paciente;
import java.util.List;


public interface IPacienteService {
    
    Paciente registrarPaciente(Paciente paciente) throws Exception;
    
    Paciente actualizarPaciente(Paciente paciente) throws Exception;
    
    void eliminarPaciente(String documento) throws Exception;
    
    Paciente buscarPorDocumento(String documento);
    
    List<Paciente> listarPacientes();
}

