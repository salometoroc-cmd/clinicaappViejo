package com.clinica.dao;

import com.clinica.model.Especialidad;
import java.util.List;

public interface EspecialidadDAO {
    void agregarEspecialidad(Especialidad especialidad);
    void editarEspecialidad(Especialidad especialidad);
    void eliminarEspecialidad(int id);
    List<Especialidad> listarEspecialidades();
    Especialidad obtenerEspecialidadPorId(int id);
}
