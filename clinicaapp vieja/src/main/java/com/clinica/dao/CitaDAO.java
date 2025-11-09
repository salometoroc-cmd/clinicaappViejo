package com.clinica.dao;

import com.clinica.model.Cita;
import com.clinica.model.EstadoCita;
import com.clinica.model.Medico;
import com.clinica.model.Paciente;
import com.clinica.model.Especialidad;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para manejar la persistencia de las citas médicas en un .txt.
 */
public class CitaDAO {

    private final String archivo = "citas.txt";

    public CitaDAO() {
        crearArchivoSiNoExiste();
    }

    private void crearArchivoSiNoExiste() {
        try {
            File f = new File(archivo);
            if (f.getParentFile() != null && !f.getParentFile().exists()) {
                f.getParentFile().mkdirs();
            }
            if (!f.exists()) {
                f.createNewFile();
                System.out.println("Archivo de citas creado: " + f.getAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Error al crear archivo de citas: " + e.getMessage());
        }
    }

    // Agrega una cita
    public void agregarCita(Cita cita) {
        cita.setId(generarNuevoId());
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, true))) {
            bw.write(formatearCita(cita));
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Error al agregar cita: " + e.getMessage());
        }
    }

    // Obtiene todas las citas
    public List<Cita> obtenerCitas() {
        List<Cita> citas = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");

                // Estructura esperada:
                // id;fecha;hora;pacienteDocumento;pacienteNombre;pacienteApellido;medicoDocumento;medicoNombre;medicoApellido;especialidadId;especialidadNombre;estado
                if (datos.length >= 12) {
                    int id = Integer.parseInt(datos[0]);
                    LocalDate fecha = LocalDate.parse(datos[1]);
                    LocalTime hora = LocalTime.parse(datos[2]);

                    // reconstruir objetos relacionados
                    Paciente paciente = new Paciente(
                            datos[3], datos[4], datos[5], "", "", "", LocalDate.now(), ""
                    );

                    Especialidad esp = new Especialidad(Integer.parseInt(datos[9]), datos[10], "");
                    Medico medico = new Medico(datos[6], datos[7], datos[8], "", "", esp);

                    EstadoCita estado = EstadoCita.valueOf(datos[11]);

                    Cita cita = new Cita(id, fecha, hora, paciente, medico);
                    cita.setEstado(estado);
                    citas.add(cita);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer citas: " + e.getMessage());
        }

        return citas;
    }

    // Actualiza una cita
    public void actualizarCita(Cita citaActualizada) {
        List<Cita> citas = obtenerCitas();

        for (int i = 0; i < citas.size(); i++) {
            if (citas.get(i).getId() == citaActualizada.getId()) {
                citas.set(i, citaActualizada);
                break;
            }
        }

        guardarTodas(citas);
    }

    // Elimina una cita por ID
    public void eliminarCita(int id) {
        List<Cita> citas = obtenerCitas();
        citas.removeIf(c -> c.getId() == id);
        guardarTodas(citas);
    }

    // nuevo
    private int generarNuevoId() {
        return obtenerCitas().stream()
            .mapToInt(Cita::getId)
            .max()
            .orElse(0) + 1;
}


    // Guarda todas las citas (reescribe el archivo)
    private void guardarTodas(List<Cita> citas) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            for (Cita cita : citas) {
                bw.write(formatearCita(cita));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar citas: " + e.getMessage());
        }
    }

    // Formatear cita para guardar en archivo
    private String formatearCita(Cita c) {
        return c.getId() + ";" +
               c.getFecha() + ";" +
               c.getHora() + ";" +
               c.getPaciente().getDocumento() + ";" +
               c.getPaciente().getNombre() + ";" +
               c.getPaciente().getApellido() + ";" +
               c.getMedico().getDocumento() + ";" +
               c.getMedico().getNombre() + ";" +
               c.getMedico().getApellido() + ";" +
               c.getMedico().getEspecialidad().getId() + ";" +
               c.getMedico().getEspecialidad().getNombre() + ";" +
               c.getEstado();
    }
}