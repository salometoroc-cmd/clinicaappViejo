package com.clinica.view.dialogs;

import com.clinica.model.Especialidad;

import javax.swing.*;
import java.awt.*;

public class EspecialidadDialog extends JDialog {

    private JTextField nombreField;
    private JTextArea descripcionArea;
    private boolean confirmed = false;
    private Especialidad especialidad;

    public EspecialidadDialog(Frame owner, boolean modal) {
        super(owner, modal);
        setTitle("Agregar Nueva Especialidad");
        initComponents();
        pack();
        setLocationRelativeTo(owner);
    }

    public EspecialidadDialog(Frame owner, boolean modal, Especialidad especialidad) {
        super(owner, modal);
        setTitle("Editar Especialidad");
        this.especialidad = especialidad;
        initComponents();
        populateFields();
        pack();
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        formPanel.add(new JLabel("Nombre:"));
        nombreField = new JTextField(20);
        formPanel.add(nombreField);

        formPanel.add(new JLabel("Descripción:"));
        descripcionArea = new JTextArea(3, 20);
        descripcionArea.setLineWrap(true);
        descripcionArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descripcionArea);
        formPanel.add(scrollPane);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Guardar");
        JButton cancelButton = new JButton("Cancelar");

        saveButton.addActionListener(e -> {
            if (validateInput()) {
                confirmed = true;
                setVisible(false);
            }
        });

        cancelButton.addActionListener(e -> {
            confirmed = false;
            setVisible(false);
        });

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void populateFields() {
        if (especialidad != null) {
            nombreField.setText(especialidad.getNombre());
            descripcionArea.setText(especialidad.getDescripcion());
        }
    }

    private boolean validateInput() {
        if (nombreField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre de la especialidad no puede estar vacío.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Especialidad getEspecialidad() {
        if (!confirmed) {
            return null;
        }
        if (especialidad == null) {
            especialidad = new Especialidad();
        }
        especialidad.setNombre(nombreField.getText().trim());
        especialidad.setDescripcion(descripcionArea.getText().trim());
        return especialidad;
    }
}
