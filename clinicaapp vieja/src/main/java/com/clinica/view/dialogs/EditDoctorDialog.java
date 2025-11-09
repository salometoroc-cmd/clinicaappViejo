package com.clinica.view.dialogs;

import com.clinica.model.Especialidad;
import com.clinica.model.Medico;
import com.clinica.service.MedicoService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class EditDoctorDialog extends JDialog {
    private JTextField documentoField, nombreField, apellidoField, emailField, telefonoField;
    private JComboBox<Especialidad> especialidadComboBox;
    private JButton guardarButton, cancelButton;
    private MedicoService medicoService;
    private Medico medico;
    private List<Especialidad> especialidades;

    public EditDoctorDialog(Frame owner, MedicoService medicoService, Medico medico, List<Especialidad> especialidades) {
        super(owner, "Editar Médico", true);
        this.medicoService = medicoService;
        this.medico = medico;
        this.especialidades = especialidades;
        initializeUI();
        fillData();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setSize(400, 300);
        setLocationRelativeTo(getOwner());

        add(createFormPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
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

        formPanel.add(new JLabel("Especialidad:"));
        especialidadComboBox = new JComboBox<>(especialidades.toArray(new Especialidad[0]));
        formPanel.add(especialidadComboBox);

        return formPanel;
    }

    private void fillData() {
        documentoField.setText(medico.getDocumento());
        nombreField.setText(medico.getNombre());
        apellidoField.setText(medico.getApellido());
        emailField.setText(medico.getEmail());
        telefonoField.setText(medico.getTelefono());
        especialidadComboBox.setSelectedItem(medico.getEspecialidad());
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        guardarButton = new JButton("Guardar");
        cancelButton = new JButton("Cancelar");

        guardarButton.addActionListener(e -> saveDoctor());
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(guardarButton);
        buttonPanel.add(cancelButton);
        return buttonPanel;
    }

    private void saveDoctor() {
        try {
            medico.setNombre(nombreField.getText());
            medico.setApellido(apellidoField.getText());
            medico.setEmail(emailField.getText());
            medico.setTelefono(telefonoField.getText());
            Especialidad especialidad = (Especialidad) especialidadComboBox.getSelectedItem();
            medico.setEspecialidad(especialidad);

            medicoService.actualizarMedico(medico);

            JOptionPane.showMessageDialog(this, "Médico actualizado exitosamente");
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar el médico: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
