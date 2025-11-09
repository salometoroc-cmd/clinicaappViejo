package com.clinica.service;

import com.clinica.model.Cita;
import java.time.LocalDate;
import java.util.List;

public interface ICitaService {
    void crearCita(Cita cita) throws Exception;
    void cancelarCita(int id) throws Exception;
    void reprogramarCita(int id, LocalDate nuevaFecha) throws Exception;
    Cita buscarPorId(int id);
    List<Cita> listarCitas();
}

