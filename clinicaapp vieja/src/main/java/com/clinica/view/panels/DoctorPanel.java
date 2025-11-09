package com.clinica.view.panels;

import com.clinica.model.Especialidad;
import com.clinica.model.Medico;
import com.clinica.service.EspecialidadService;
import com.clinica.service.MedicoService;
import com.clinica.view.dialogs.AddDoctorDialog;
import com.clinica.view.dialogs.EditDoctorDialog;
import com.clinica.view.listeners.DataChangeListener;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DoctorPanel extends JPanel {
    private JTable doctorTable;
    private DefaultTableModel tableModel;
    private JComboBox<Especialidad> specialityComboBox;
    private MedicoService medicoService;
    private EspecialidadService especialidadService;
    private DataChangeListener listener;

    public DoctorPanel(MedicoService medicoService, EspecialidadService especialidadService, DataChangeListener listener) {
        this.medicoService = medicoService;
        this.especialidadService = especialidadService;
        this.listener = listener;
        initializePanel();
        loadDoctorsData();
        loadSpecialities();
    }

    private void initializePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(new Color(245, 245, 245));

        add(createFilterPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createFilterPanel() {
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBorder(BorderFactory.createTitledBorder("🎯 Filtros"));
        filterPanel.setBackground(Color.WHITE);

        specialityComboBox = new JComboBox<>();

        JButton filterButton = new JButton("Filtrar");
        filterButton.addActionListener(e -> filterDoctors());

        filterPanel.add(new JLabel("Especialidad:"));
        filterPanel.add(specialityComboBox);
        filterPanel.add(filterButton);

        return filterPanel;
    }

    private void loadSpecialities() {
        try {
            List<Especialidad> especialidades = especialidadService.listarEspecialidades();
            specialityComboBox.addItem(new Especialidad(0, "Todas las especialidades", ""));
            for (Especialidad especialidad : especialidades) {
                specialityComboBox.addItem(especialidad);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar especialidades: " + e.getMessage());
        }
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("👨‍⚕️ Cuerpo Médico"));
        tablePanel.setBackground(Color.WHITE);

        String[] columnNames = {"Documento", "Nombre", "Apellido", "Email", "Teléfono", "Especialidad"};
        tableModel = new DefaultTableModel(columnNames, 0);
        doctorTable = new JTable(tableModel);

        // Estilo de tabla
        doctorTable.getTableHeader().setBackground(new Color(70, 130, 180));
        doctorTable.getTableHeader().setForeground(Color.WHITE);
        doctorTable.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(doctorTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(245, 245, 245));

        JButton addButton = new JButton("Agregar Médico");
        JButton editButton = new JButton("Editar Médico");
        JButton deleteButton = new JButton("Eliminar Médico");

        addButton.addActionListener(e -> addDoctor());
        editButton.addActionListener(e -> editDoctor());
        deleteButton.addActionListener(e -> deleteDoctor());

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        return buttonPanel;
    }

    private void loadDoctorsData() {
        try {
            List<Medico> medicos = medicoService.listarMedicos();
            updateTable(medicos);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar médicos: " + e.getMessage());
        }
    }

    private void filterDoctors() {
        Especialidad especialidad = (Especialidad) specialityComboBox.getSelectedItem();
        if (especialidad.getId() == 0) {
            loadDoctorsData();
            return;
        }

        try {
            List<Medico> medicos = medicoService.listarPorEspecialidad(especialidad.getNombre());
            updateTable(medicos);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al filtrar médicos: " + e.getMessage());
        }
    }

    private void updateTable(List<Medico> medicos) {
        tableModel.setRowCount(0);
        for (Medico medico : medicos) {
            Object[] row = {
                medico.getDocumento(),
                medico.getNombre(),
                medico.getApellido(),
                medico.getEmail(),
                medico.getTelefono(),
                medico.getEspecialidad().getNombre()
            };
            tableModel.addRow(row);
        }
    }

    private void addDoctor() {
        try {
            List<Especialidad> especialidades = especialidadService.listarEspecialidades();
            AddDoctorDialog dialog = new AddDoctorDialog((Frame) SwingUtilities.getWindowAncestor(this), medicoService, especialidades);
            dialog.setVisible(true);
            loadDoctorsData(); // Recargar la tabla
            listener.onDataChanged();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar especialidades: " + e.getMessage());
        }
    }

    private void editDoctor() {
        int selectedRow = doctorTable.getSelectedRow();
        if (selectedRow >= 0) {
            String documento = (String) tableModel.getValueAt(selectedRow, 0);
            try {
                Medico medico = medicoService.buscarPorDocumento(documento);
                if (medico != null) {
                    List<Especialidad> especialidades = especialidadService.listarEspecialidades();
                    EditDoctorDialog dialog = new EditDoctorDialog((Frame) SwingUtilities.getWindowAncestor(this), medicoService, medico, especialidades);
                    dialog.setVisible(true);
                    loadDoctorsData(); // Recargar la tabla
                    listener.onDataChanged();
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontró el médico");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al buscar el médico: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un médico para editar");
        }
    }

    private void deleteDoctor() {
        int selectedRow = doctorTable.getSelectedRow();
        if (selectedRow >= 0) {
            String documento = (String) tableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this,
                "¿Eliminar médico con documento: " + documento + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    medicoService.eliminarMedico(documento);
                    loadDoctorsData();
                    listener.onDataChanged();
                    JOptionPane.showMessageDialog(this, "Médico eliminado");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un médico");
        }
    }
}