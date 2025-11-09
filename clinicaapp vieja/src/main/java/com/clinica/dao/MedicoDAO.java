package com.clinica.dao;

import com.clinica.model.Medico;
import com.clinica.model.Especialidad;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gestionar los médicos de la clínica.
 */
public class MedicoDAO {

    private final String archivo = "data/medicos.txt";

    public MedicoDAO() {
        crearArchivoSiNoExiste();
    }

    // Crea el archivo y carpeta si no existen
    private void crearArchivoSiNoExiste() {
        try {
            File f = new File(archivo);
            if (f.getParentFile() != null && !f.getParentFile().exists()) {
                f.getParentFile().mkdirs();
            }
            if (!f.exists()) {
                f.createNewFile();
                System.out.println("Archivo de médicos creado: " + f.getAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Error al crear archivo de médicos: " + e.getMessage());
        }
    }

    // Agrega médico
    public void agregarMedico(Medico m) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, true))) {
            bw.write(m.getDocumento() + ";" + m.getNombre() + ";" + m.getApellido() + ";" +
                     m.getEmail() + ";" + m.getTelefono() + ";" +
                     m.getEspecialidad().getId() + ";" + m.getEspecialidad().getNombre());
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Error al agregar médico: " + e.getMessage());
        }
    }

    // Obtiene lista de médicos
    public List<Medico> obtenerMedicos() {
        List<Medico> lista = new ArrayList<>();
        File f = new File(archivo);
        if (!f.exists()) return lista;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length >= 7) {
                    String documento = datos[0];
                    String nombre = datos[1];
                    String apellido = datos[2];
                    String email = datos[3];
                    String telefono = datos[4];
                    int idEspecialidad = Integer.parseInt(datos[5]);
                    String nombreEspecialidad = datos[6];

                    Especialidad esp = new Especialidad(idEspecialidad, nombreEspecialidad, "");
                    Medico m = new Medico(documento, nombre, apellido, email, telefono, esp);
                    lista.add(m);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer archivo de médicos: " + e.getMessage());
        }
        return lista;
    }

    // Actualiza médico existente
    public void actualizarMedico(Medico actualizado) {
        List<Medico> medicos = obtenerMedicos();
        for (int i = 0; i < medicos.size(); i++) {
            if (medicos.get(i).getDocumento().equals(actualizado.getDocumento())) {
                medicos.set(i, actualizado);
                break;
            }
        }
        guardarTodos(medicos);
    }

    // Elimina médico
    public void eliminarMedico(String documento) {
        List<Medico> medicos = obtenerMedicos();
        medicos.removeIf(m -> m.getDocumento().equals(documento));
        guardarTodos(medicos);
    }

    // Guarda lista completa (reescribir el archivo)
    private void guardarTodos(List<Medico> lista) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            for (Medico m : lista) {
                bw.write(m.getDocumento() + ";" + m.getNombre() + ";" + m.getApellido() + ";" +
                         m.getEmail() + ";" + m.getTelefono() + ";" +
                         m.getEspecialidad().getId() + ";" + m.getEspecialidad().getNombre());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar médicos: " + e.getMessage());
        }
    }
}