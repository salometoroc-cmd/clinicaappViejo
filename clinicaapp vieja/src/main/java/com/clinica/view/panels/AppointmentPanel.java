package com.clinica.view.panels;

import com.clinica.model.*;
import com.clinica.service.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class AppointmentPanel extends JPanel {
    private JTable appointmentTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> doctorComboBox, patientComboBox;
    private JTextField dateField, timeField;
    private CitaService citaService;
    private PacienteService pacienteService;
    private MedicoService medicoService;
    
    public AppointmentPanel(CitaService citaService, PacienteService pacienteService, MedicoService medicoService) {
        this.citaService = citaService;
        this.pacienteService = pacienteService;
        this.medicoService = medicoService;
        initializePanel();
        loadAppointmentsData();
        loadDoctorsAndPatients();
    }
    
    private void initializePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(new Color(245, 245, 245));
        
        add(createFormPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }
    
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridLayout(2, 4, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("📅 Agendar Nueva Cita"));
        formPanel.setBackground(Color.WHITE);
        
        doctorComboBox = new JComboBox<>();
        patientComboBox = new JComboBox<>();
        dateField = new JTextField();
        timeField = new JTextField();
        
        formPanel.add(new JLabel("Médico:"));
        formPanel.add(doctorComboBox);
        formPanel.add(new JLabel("Paciente:"));
        formPanel.add(patientComboBox);
        formPanel.add(new JLabel("Fecha (yyyy-MM-dd):"));
        formPanel.add(dateField);
        formPanel.add(new JLabel("Hora (HH:mm):"));
        formPanel.add(timeField);
        
        return formPanel;
    }
    
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("📋 Citas Programadas"));
        tablePanel.setBackground(Color.WHITE);
        
        String[] columnNames = {"ID", "Paciente", "Médico", "Fecha", "Hora", "Estado"};
        tableModel = new DefaultTableModel(columnNames, 0);
        appointmentTable = new JTable(tableModel);
        
        appointmentTable.getTableHeader().setBackground(new Color(70, 130, 180));
        appointmentTable.getTableHeader().setForeground(Color.WHITE);
        appointmentTable.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(appointmentTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        return tablePanel;
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(245, 245, 245));
        
        JButton scheduleButton = new JButton("Agendar Cita");
        JButton cancelButton = new JButton("Cancelar Cita");
        JButton rescheduleButton = new JButton("Reprogramar");
        
        scheduleButton.addActionListener(e -> scheduleAppointment());
        cancelButton.addActionListener(e -> cancelAppointment());
        rescheduleButton.addActionListener(e -> rescheduleAppointment());
        
        buttonPanel.add(scheduleButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(rescheduleButton);
        
        return buttonPanel;
    }
    
    public void loadDoctorsAndPatients() {
        try {
            // Cargar médicos
            List<Medico> medicos = medicoService.listarMedicos();
            doctorComboBox.removeAllItems();
            for (Medico medico : medicos) {
                doctorComboBox.addItem(medico.getNombreCompleto() + " - " + medico.getEspecialidad().getNombre());
            }
            
            // Cargar pacientes
            List<Paciente> pacientes = pacienteService.listarPacientes();
            patientComboBox.removeAllItems();
            for (Paciente paciente : pacientes) {
                patientComboBox.addItem(paciente.getNombreCompleto() + " - " + paciente.getDocumento());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + e.getMessage());
        }
    }
    
    private void loadAppointmentsData() {
        try {
            List<Cita> citas = citaService.listarCitas();
            updateTable(citas);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar citas: " + e.getMessage());
        }
    }
    
    private void updateTable(List<Cita> citas) {
        tableModel.setRowCount(0);
        for (Cita cita : citas) {
            Object[] row = {
                cita.getId(),
                cita.getPaciente().getNombreCompleto(),
                cita.getMedico().getNombreCompleto(),
                cita.getFecha(),
                cita.getHora(),
                cita.getEstado()
            };
            tableModel.addRow(row);
        }
    }
    
    private void scheduleAppointment() {
        try {
            // Validar campos
            if (doctorComboBox.getSelectedIndex() == -1 || 
                patientComboBox.getSelectedIndex() == -1 ||
                dateField.getText().trim().isEmpty() ||
                timeField.getText().trim().isEmpty()) {
                
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios");
                return;
            }
            
            // Parsear fecha y hora
            LocalDate fecha = LocalDate.parse(dateField.getText().trim());
            LocalTime hora = LocalTime.parse(timeField.getText().trim());
            
            // Obtener médico y paciente seleccionados
            List<Medico> medicos = medicoService.listarMedicos();
            List<Paciente> pacientes = pacienteService.listarPacientes();
            
            Medico medicoSeleccionado = medicos.get(doctorComboBox.getSelectedIndex());
            Paciente pacienteSeleccionado = pacientes.get(patientComboBox.getSelectedIndex());
            
            // Crear nueva cita
            Cita nuevaCita = new Cita(fecha, hora, pacienteSeleccionado, medicoSeleccionado);
            
            // Guardar usando el servicio
            citaService.crearCita(nuevaCita);
            
            // Recargar datos
            loadAppointmentsData();
            
            // Limpiar campos
            dateField.setText("");
            timeField.setText("");
            
            JOptionPane.showMessageDialog(this, "✅ Cita agendada exitosamente");
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Error al agendar cita: " + e.getMessage());
        }
    }
    
    private void cancelAppointment() {
        int selectedRow = appointmentTable.getSelectedRow();
        if (selectedRow >= 0) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            try {
                citaService.cancelarCita(id);
                loadAppointmentsData();
                JOptionPane.showMessageDialog(this, "✅ Cita cancelada exitosamente");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "❌ Error al cancelar cita: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una cita para cancelar");
        }
    }
    
    private void rescheduleAppointment() {
        int selectedRow = appointmentTable.getSelectedRow();
        if (selectedRow >= 0) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            
            String nuevaFecha = JOptionPane.showInputDialog(this, 
                "Ingrese nueva fecha (yyyy-MM-dd):", "Reprogramar Cita", JOptionPane.QUESTION_MESSAGE);
            
            if (nuevaFecha != null && !nuevaFecha.trim().isEmpty()) {
                try {
                    LocalDate fecha = LocalDate.parse(nuevaFecha.trim());
                    citaService.reprogramarCita(id, fecha);
                    loadAppointmentsData();
                    JOptionPane.showMessageDialog(this, "✅ Cita reprogramada exitosamente");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "❌ Error al reprogramar: " + e.getMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una cita para reprogramar");
        }
    }
}