package com.clinica.dao;

import com.clinica.model.Paciente;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class PacienteDAO {

    private final String archivo = "pacientes.txt";

    public PacienteDAO() {
        crearArchivoSiNoExiste();
        verificarContenidoArchivo(); // ✅ Verificar contenido al iniciar
    }

    private void crearArchivoSiNoExiste() {
        try {
            File f = new File(archivo);
            System.out.println("🔍 DEBUG DAO INIT: Ruta archivo: " + f.getAbsolutePath());
            
            if (f.getParentFile() != null && !f.getParentFile().exists()) {
                f.getParentFile().mkdirs();
                System.out.println("📁 DEBUG DAO INIT: Directorio creado");
            }
            if (!f.exists()) {
                f.createNewFile();
                System.out.println("✅ DEBUG DAO INIT: Archivo creado: " + f.getAbsolutePath());
            } else {
                System.out.println("✅ DEBUG DAO INIT: Archivo ya existe");
            }
        } catch (IOException e) {
            System.err.println("❌ DEBUG DAO INIT: Error al crear archivo: " + e.getMessage());
        }
    }

    private void verificarContenidoArchivo() {
        try {
            File f = new File(archivo);
            System.out.println("🔍 DEBUG DAO INIT: Verificando contenido del archivo...");
            System.out.println("🔍 DEBUG DAO INIT: Archivo existe: " + f.exists());
            System.out.println("🔍 DEBUG DAO INIT: Tamaño archivo: " + f.length() + " bytes");
            
            if (f.exists() && f.length() > 0) {
                System.out.println("🔍 DEBUG DAO INIT: Leyendo contenido completo...");
                List<String> lineas = java.nio.file.Files.readAllLines(f.toPath());
                System.out.println("🔍 DEBUG DAO INIT: Número de líneas en archivo: " + lineas.size());
                for (int i = 0; i < lineas.size(); i++) {
                    System.out.println("🔍 DEBUG DAO INIT: Línea " + i + ": '" + lineas.get(i) + "'");
                }
            } else {
                System.out.println("🔍 DEBUG DAO INIT: Archivo vacío o no existe");
            }
        } catch (Exception e) {
            System.err.println("❌ DEBUG DAO INIT: Error verificando archivo: " + e.getMessage());
        }
        System.out.println("🔍 DEBUG DAO INIT: Verificación completada\n");
    }

    // Agregar paciente
    public void agregarPaciente(Paciente p) {
        System.out.println("➕ DEBUG DAO: Agregando paciente: " + p.getNombre() + " " + p.getApellido());
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, true))) {
            String linea = formatoTexto(p);
            bw.write(linea);
            bw.newLine();
            
            System.out.println("✅ DEBUG DAO: Paciente guardado -> " + linea);
            
        } catch (IOException e) {
            System.err.println("❌ DEBUG DAO: Error guardando paciente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Obtener lista de pacientes
    public List<Paciente> obtenerPacientes() {
        System.out.println("\n=== 📖 DEBUG DAO: INICIANDO obtenerPacientes() ===");
        List<Paciente> lista = new ArrayList<>();

        File f = new File(archivo);
        System.out.println("🔍 DEBUG DAO: Verificando archivo: " + f.getAbsolutePath());
        System.out.println("🔍 DEBUG DAO: Archivo existe: " + f.exists());
        System.out.println("🔍 DEBUG DAO: Tamaño archivo: " + f.length() + " bytes");
        
        if (!f.exists() || f.length() == 0) {
            System.out.println("⚠️ DEBUG DAO: Archivo no existe o está vacío");
            System.out.println("=== 📖 DEBUG DAO: obtenerPacientes() FINALIZADO - Lista vacía ===\n");
            return lista;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linea;
            int lineCount = 0;
            int pacientesCargados = 0;
            System.out.println("📖 DEBUG DAO: Leyendo archivo línea por línea...");
            
            while ((linea = br.readLine()) != null) {
                lineCount++;
                linea = linea.trim(); // Limpiar espacios
                System.out.println("📄 DEBUG DAO: Línea " + lineCount + ": '" + linea + "'");
                
                if (linea.isEmpty()) {
                    System.out.println("⚠️ DEBUG DAO: Línea " + lineCount + " vacía, omitiendo");
                    continue;
                }
                
                String[] datos = linea.split(";", -1); // -1 para incluir campos vacíos al final
                System.out.println("🔢 DEBUG DAO: Campos encontrados: " + datos.length);
                
                // Mostrar cada campo individualmente
                for (int i = 0; i < datos.length; i++) {
                    System.out.println("   Campo " + i + " ('" + datos[i] + "')");
                }
                
                // Validación de campos esperados
                if (datos.length == 8) {
                    try {
                        String documento = datos[0];
                        String nombre = datos[1];
                        String apellido = datos[2];
                        String email = datos[3];
                        String telefono = datos[4];
                        String direccion = datos[5];
                        
                        // Manejar fecha de nacimiento
                        LocalDate fechaNacimiento = null;
                        if (!datos[6].isEmpty()) {
                            fechaNacimiento = LocalDate.parse(datos[6]);
                        }
                        
                        String historialMedico = datos[7];

                        Paciente p = new Paciente(documento, nombre, apellido, email, telefono,
                                                  direccion, fechaNacimiento, historialMedico);
                        lista.add(p);
                        pacientesCargados++;
                        System.out.println("✅ DEBUG DAO: Paciente CARGADO: " + nombre + " " + apellido);
                        
                    } catch (DateTimeParseException e) {
                        System.err.println("❌ DEBUG DAO: Error parseando fecha en línea " + lineCount + ": " + datos[6]);
                        System.err.println("❌ DEBUG DAO: Mensaje error: " + e.getMessage());
                    } catch (Exception e) {
                        System.err.println("❌ DEBUG DAO: Error parseando línea " + lineCount + ": " + e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    System.err.println("❌ DEBUG DAO: Línea " + lineCount + " con formato incorrecto. Esperados 8 campos, encontrados: " + datos.length);
                }
            }
            
            System.out.println("📊 DEBUG DAO: Resumen:");
            System.out.println("   - Total líneas leídas: " + lineCount);
            System.out.println("   - Total pacientes cargados: " + pacientesCargados);
            System.out.println("   - Tamaño lista final: " + lista.size());
            
        } catch (IOException e) {
            System.err.println("❌ DEBUG DAO: Error de IO leyendo archivo: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("=== 📖 DEBUG DAO: obtenerPacientes() FINALIZADO ===\n");
        return lista;
    }

    // Actualizar paciente
    public void actualizarPaciente(Paciente actualizado) {
        System.out.println("✏️ DEBUG DAO: Actualizando paciente: " + actualizado.getDocumento());
        List<Paciente> pacientes = obtenerPacientes();

        for (int i = 0; i < pacientes.size(); i++) {
            if (pacientes.get(i).getDocumento().equals(actualizado.getDocumento())) {
                pacientes.set(i, actualizado);
                break;
            }
        }
        guardarTodos(pacientes);
    }

    // Eliminar paciente
    public void eliminarPaciente(String documento) {
        System.out.println("🗑️ DEBUG DAO: Eliminando paciente: " + documento);
        List<Paciente> pacientes = obtenerPacientes();
        pacientes.removeIf(p -> p.getDocumento().equals(documento));
        guardarTodos(pacientes);
    }

    // Guardar toda la lista (reescribir archivo completo)
    private void guardarTodos(List<Paciente> lista) {
        System.out.println("💾 DEBUG DAO: Guardando todos los pacientes: " + lista.size());
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            for (Paciente p : lista) {
                String linea = formatoTexto(p);
                bw.write(linea);
                bw.newLine();
                System.out.println("💾 DEBUG DAO: Guardado: " + linea);
            }
            System.out.println("✅ DEBUG DAO: Todos los pacientes guardados");
        } catch (IOException e) {
            System.err.println("❌ DEBUG DAO: Error guardando todos los pacientes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Formato estándar para escribir en archivo
    private String formatoTexto(Paciente p) {
        String resultado = String.join(";",
                p.getDocumento() != null ? p.getDocumento() : "",
                p.getNombre() != null ? p.getNombre() : "",
                p.getApellido() != null ? p.getApellido() : "",
                p.getEmail() != null ? p.getEmail() : "",
                p.getTelefono() != null ? p.getTelefono() : "",
                p.getDireccion() != null ? p.getDireccion() : "",
                p.getFechaNacimiento() != null ? p.getFechaNacimiento().toString() : "",
                p.getHistorialMedico() != null ? p.getHistorialMedico() : ""
        );
        System.out.println("🔧 DEBUG DAO: Formateando paciente -> " + resultado);
        return resultado;
    }
}