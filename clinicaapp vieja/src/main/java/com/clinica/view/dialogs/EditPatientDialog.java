package com.clinica.view.dialogs;

import com.clinica.model.Paciente;
import com.clinica.service.PacienteService;
import java.awt.*;
import java.time.LocalDate;
import java.util.Calendar;
import javax.swing.*;

public class EditPatientDialog extends JDialog {
    private JTextField documentoField, nombreField, apellidoField, emailField, telefonoField, direccionField, historialMedicoField;
    private JComboBox<Integer> diaComboBox, mesComboBox, anioComboBox;
    private JButton guardarButton, cancelButton;
    private PacienteService pacienteService;
    private Paciente paciente;
    private Runnable onPatientUpdated; // ✅ NUEVO: Callback para actualizar

    // ✅ MODIFICADO: Constructor con callback
    public EditPatientDialog(Frame owner, PacienteService pacienteService, Paciente paciente, Runnable onPatientUpdated) {
        super(owner, "Editar Paciente", true);
        this.pacienteService = pacienteService;
        this.paciente = paciente;
        this.onPatientUpdated = onPatientUpdated; // ✅ NUEVO: Recibir callback
        initializeUI();
        fillData();
    }

    // ✅ NUEVO: Constructor antiguo para compatibilidad
    public EditPatientDialog(Frame owner, PacienteService pacienteService, Paciente paciente) {
        this(owner, pacienteService, paciente, null);
    }

    // El resto de tu código se mantiene IGUAL...
    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setSize(400, 500);
        setLocationRelativeTo(getOwner());

        add(createFormPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridLayout(9, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("Documento:"));
        documentoField = new JTextField();
        documentoField.setEditable(false);
        formPanel.add(documentoField);

        formPanel.add(new JLabel("Nombre:"));
        nombreField = new JTextField();
        formPanel.add(nombreField);

        formPanel.add(new JLabel("Apellido:"));
        apellidoField = new JTextField();
        formPanel.add(apellidoField);

        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);

        formPanel.add(new JLabel("Teléfono:"));
        telefonoField = new JTextField();
        formPanel.add(telefonoField);

        formPanel.add(new JLabel("Dirección:"));
        direccionField = new JTextField();
        formPanel.add(direccionField);

        formPanel.add(new JLabel("Fecha de Nacimiento:"));
        JPanel fechaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        diaComboBox = new JComboBox<>();
        mesComboBox = new JComboBox<>();
        anioComboBox = new JComboBox<>();
        for (int i = 1; i <= 31; i++) diaComboBox.addItem(i);
        for (int i = 1; i <= 12; i++) mesComboBox.addItem(i);
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear; i >= currentYear - 100; i--) anioComboBox.addItem(i);
        fechaPanel.add(diaComboBox);
        fechaPanel.add(mesComboBox);
        fechaPanel.add(anioComboBox);
        formPanel.add(fechaPanel);

        formPanel.add(new JLabel("Historial Médico:"));
        historialMedicoField = new JTextField();
        formPanel.add(historialMedicoField);

        return formPanel;
    }

    private void fillData() {
        documentoField.setText(paciente.getDocumento());
        nombreField.setText(paciente.getNombre());
        apellidoField.setText(paciente.getApellido());
        emailField.setText(paciente.getEmail());
        telefonoField.setText(paciente.getTelefono());
        direccionField.setText(paciente.getDireccion());
        LocalDate fechaNacimiento = paciente.getFechaNacimiento();
        if (fechaNacimiento != null) {
            diaComboBox.setSelectedItem(fechaNacimiento.getDayOfMonth());
            mesComboBox.setSelectedItem(fechaNacimiento.getMonthValue());
            anioComboBox.setSelectedItem(fechaNacimiento.getYear());
        }
        historialMedicoField.setText(paciente.getHistorialMedico());
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        guardarButton = new JButton("Guardar");
        cancelButton = new JButton("Cancelar");

        guardarButton.addActionListener(e -> savePatient());
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(guardarButton);
        buttonPanel.add(cancelButton);
        return buttonPanel;
    }

    private void savePatient() {
        try {
            paciente.setNombre(nombreField.getText());
            paciente.setApellido(apellidoField.getText());
            paciente.setEmail(emailField.getText());
            paciente.setTelefono(telefonoField.getText());
            paciente.setDireccion(direccionField.getText());
            int dia = (int) diaComboBox.getSelectedItem();
            int mes = (int) mesComboBox.getSelectedItem();
            int anio = (int) anioComboBox.getSelectedItem();
            paciente.setFechaNacimiento(LocalDate.of(anio, mes, dia));
            paciente.setHistorialMedico(historialMedicoField.getText());

            pacienteService.actualizarPaciente(paciente);

            JOptionPane.showMessageDialog(this, "Paciente actualizado exitosamente");
            
            // ✅ NUEVO: ACTUALIZAR LA LISTA EN LA VENTANA PRINCIPAL
            if (onPatientUpdated != null) {
                onPatientUpdated.run(); // Esto actualiza la tabla
            }
            
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar el paciente: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}