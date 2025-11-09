package com.clinica.dao;

import com.clinica.model.HistoriaConsulta;
import com.clinica.model.Cita;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para manejar la persistencia de historias clínicas en archivo .txt
 */
public class HistoriaConsultaDAO {
    private final String archivo = "historias_consulta.txt";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public HistoriaConsultaDAO() {
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
                System.out.println("Archivo de historias de consulta creado: " + f.getAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Error al crear archivo de historias de consulta: " + e.getMessage());
        }
    }

    // Guardar historia clínica
    public void guardarHistoriaConsulta(HistoriaConsulta historia) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, true))) {
            bw.write(formatearHistoria(historia));
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Error al guardar historia clínica: " + e.getMessage());
        }
    }

    // Obtener todas las historias clínicas
    public List<HistoriaConsulta> obtenerHistoriasConsulta() {
        List<HistoriaConsulta> historias = new ArrayList<>();
        File f = new File(archivo);
        
        if (!f.exists()) return historias;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                HistoriaConsulta historia = parsearHistoria(linea);
                if (historia != null) {
                    historias.add(historia);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer historias clínicas: " + e.getMessage());
        }

        return historias;
    }

    // Actualizar historia clínica
    public void actualizarHistoriaConsulta(HistoriaConsulta historiaActualizada) {
        List<HistoriaConsulta> historias = obtenerHistoriasConsulta();

        for (int i = 0; i < historias.size(); i++) {
            if (historias.get(i).getId() == historiaActualizada.getId()) {
                historias.set(i, historiaActualizada);
                break;
            }
        }

        guardarTodas(historias);
    }

    // Eliminar historia clínica
    public void eliminarHistoriaConsulta(int id) {
        List<HistoriaConsulta> historias = obtenerHistoriasConsulta();
        historias.removeIf(h -> h.getId() == id);
        guardarTodas(historias);
    }

    // Guardar todas las historias (reescribe el archivo)
    private void guardarTodas(List<HistoriaConsulta> historias) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            for (HistoriaConsulta historia : historias) {
                bw.write(formatearHistoria(historia));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar historias clínicas: " + e.getMessage());
        }
    }

    // Formatear historia para guardar en archivo
    private String formatearHistoria(HistoriaConsulta h) {
        return h.getId() + ";" +
               h.getCita().getId() + ";" +
               h.getDiagnostico() + ";" +
               h.getTratamiento() + ";" +
               h.getObservaciones() + ";" +
               h.getMedicamentosRecetados() + ";" +
               h.getFechaConsulta().format(formatter) + ";" +
               h.isSeguimientoRequerido() + ";" +
               (h.getFechaProximoSeguimiento() != null ? h.getFechaProximoSeguimiento().format(formatter) : "null");
    }

    // Parsear línea del archivo a objeto HistoriaConsulta
    private HistoriaConsulta parsearHistoria(String linea) {
        try {
            String[] datos = linea.split(";");
            if (datos.length >= 9) {
                int id = Integer.parseInt(datos[0]);
                int idCita = Integer.parseInt(datos[1]);
                String diagnostico = datos[2];
                String tratamiento = datos[3];
                String observaciones = datos[4];
                String medicamentos = datos[5];
                LocalDateTime fechaConsulta = LocalDateTime.parse(datos[6], formatter);
                boolean seguimientoRequerido = Boolean.parseBoolean(datos[7]);
                LocalDateTime fechaSeguimiento = !"null".equals(datos[8]) ? 
                    LocalDateTime.parse(datos[8], formatter) : null;

                // Necesitarías tener acceso al CitaService para obtener la cita completa
                // Por simplicidad, creamos una cita mínima
                Cita cita = new Cita();
                cita.setId(idCita);

                HistoriaConsulta historia = new HistoriaConsulta();
                historia.setId(id);
                historia.setCita(cita);
                historia.setDiagnostico(diagnostico);
                historia.setTratamiento(tratamiento);
                historia.setObservaciones(observaciones);
                historia.setMedicamentosRecetados(medicamentos);
                historia.setFechaConsulta(fechaConsulta);
                historia.setSeguimientoRequerido(seguimientoRequerido);
                historia.setFechaProximoSeguimiento(fechaSeguimiento);

                return historia;
            }
        } catch (Exception e) {
            System.err.println("Error al parsear historia clínica: " + e.getMessage());
        }
        return null;
    }
}