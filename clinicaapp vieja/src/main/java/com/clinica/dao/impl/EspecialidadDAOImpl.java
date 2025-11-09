package com.clinica.dao.impl;

import com.clinica.dao.EspecialidadDAO;
import com.clinica.model.Especialidad;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class EspecialidadDAOImpl implements EspecialidadDAO {

    private final String archivo = "especialidades.txt";
    private AtomicInteger nextId = new AtomicInteger(0);

    public EspecialidadDAOImpl() {
        // Initialize nextId based on existing data
        List<Especialidad> especialidades = listarEspecialidades();
        if (!especialidades.isEmpty()) {
            nextId.set(especialidades.stream().mapToInt(Especialidad::getId).max().orElse(0) + 1);
        } else {
            nextId.set(1); // Start from 1 if no specialties exist
        }
    }

    @Override
    public void agregarEspecialidad(Especialidad especialidad) {
        especialidad.setId(nextId.getAndIncrement());
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, true))) {
            bw.write(formatearEspecialidad(especialidad));
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Error al agregar especialidad: " + e.getMessage());
        }
    }

    @Override
    public void editarEspecialidad(Especialidad especialidadActualizada) {
        List<Especialidad> especialidades = listarEspecialidades();
        boolean found = false;
        for (int i = 0; i < especialidades.size(); i++) {
            if (especialidades.get(i).getId() == especialidadActualizada.getId()) {
                especialidades.set(i, especialidadActualizada);
                found = true;
                break;
            }
        }
        if (found) {
            guardarTodas(especialidades);
        }
    }

    @Override
    public void eliminarEspecialidad(int id) {
        List<Especialidad> especialidades = listarEspecialidades();
        boolean removed = especialidades.removeIf(e -> e.getId() == id);
        if (removed) {
            guardarTodas(especialidades);
        }
    }

    @Override
    public List<Especialidad> listarEspecialidades() {
        List<Especialidad> especialidades = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                Especialidad especialidad = parsearEspecialidad(linea);
                if (especialidad != null) {
                    especialidades.add(especialidad);
                }
            }
        } catch (IOException e) {
            // File might not exist yet, or other read error
            System.err.println("Error al leer especialidades: " + e.getMessage());
        }
        return especialidades;
    }

    @Override
    public Especialidad obtenerEspecialidadPorId(int id) {
        return listarEspecialidades().stream()
                .filter(e -> e.getId() == id)
                .findFirst()
                .orElse(null);
    }

    private void guardarTodas(List<Especialidad> especialidades) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            for (Especialidad especialidad : especialidades) {
                bw.write(formatearEspecialidad(especialidad));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar especialidades: " + e.getMessage());
        }
    }

    private String formatearEspecialidad(Especialidad especialidad) {
        return especialidad.getId() + ";" + especialidad.getNombre() + ";" + especialidad.getDescripcion();
    }

    private Especialidad parsearEspecialidad(String linea) {
        String[] datos = linea.split(";");
        if (datos.length == 3) {
            try {
                int id = Integer.parseInt(datos[0]);
                String nombre = datos[1];
                String descripcion = datos[2];
                return new Especialidad(id, nombre, descripcion);
            } catch (NumberFormatException e) {
                System.err.println("Error al parsear ID de especialidad: " + e.getMessage());
            }
        }
        return null;
    }
}
