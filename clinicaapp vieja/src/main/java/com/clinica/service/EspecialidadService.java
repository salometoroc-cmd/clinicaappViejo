package com.clinica.service;

import com.clinica.dao.EspecialidadDAO;
import com.clinica.dao.impl.EspecialidadDAOImpl;
import com.clinica.model.Especialidad;

import java.util.List;

public class EspecialidadService {

    private EspecialidadDAO especialidadDAO;

    public EspecialidadService() {
        this.especialidadDAO = new EspecialidadDAOImpl();
    }

    public void agregarEspecialidad(Especialidad especialidad) {
        especialidadDAO.agregarEspecialidad(especialidad);
    }

    public void editarEspecialidad(Especialidad especialidad) {
        especialidadDAO.editarEspecialidad(especialidad);
    }

    public void eliminarEspecialidad(int id) {
        especialidadDAO.eliminarEspecialidad(id);
    }

    public List<Especialidad> listarEspecialidades() {
        return especialidadDAO.listarEspecialidades();
    }

    public Especialidad obtenerEspecialidadPorId(int id) {
        return especialidadDAO.obtenerEspecialidadPorId(id);
    }
}
