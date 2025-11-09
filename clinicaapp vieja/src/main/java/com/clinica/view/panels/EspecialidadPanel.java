package com.clinica.view.panels;

import com.clinica.model.Especialidad;
import com.clinica.service.EspecialidadService;
import com.clinica.view.dialogs.EspecialidadDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class EspecialidadPanel extends JPanel {

    private JTable especialidadTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton addButton, editButton, deleteButton, searchButton;
    private EspecialidadService especialidadService;

    public EspecialidadPanel(EspecialidadService especialidadService) {
        this.especialidadService = especialidadService;
        initializePanel();
        loadEspecialidades();
    }

    private void initializePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(new Color(245, 245, 245));

        add(createSearchPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createTitledBorder("🔍 Búsqueda"));
        searchPanel.setBackground(Color.WHITE);

        searchField = new JTextField(20);
        searchButton = new JButton("Buscar");

        searchButton.addActionListener(e -> searchEspecialidades());

        searchPanel.add(new JLabel("Buscar especialidad:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        return searchPanel;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("📋 Lista de Especialidades"));
        tablePanel.setBackground(Color.WHITE);

        String[] columnNames = {"ID", "Nombre", "Descripción"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make cells uneditable
            }
        };
        especialidadTable = new JTable(tableModel);
        especialidadTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Estilo de tabla
        especialidadTable.getTableHeader().setBackground(new Color(70, 130, 180));
        especialidadTable.getTableHeader().setForeground(Color.WHITE);
        especialidadTable.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(especialidadTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(245, 245, 245));

        addButton = new JButton("Agregar Especialidad");
        editButton = new JButton("Editar Especialidad");
        deleteButton = new JButton("Eliminar Especialidad");

        addButton.addActionListener(e -> addEspecialidad());
        editButton.addActionListener(e -> editEspecialidad());
        deleteButton.addActionListener(e -> deleteEspecialidad());

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        return buttonPanel;
    }

    public void loadEspecialidades() {
        try {
            List<Especialidad> especialidades = especialidadService.listarEspecialidades();
            updateTable(especialidades);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar especialidades: " + e.getMessage());
        }
    }

    private void searchEspecialidades() {
        String searchText = searchField.getText().trim();
        if (searchText.isEmpty()) {
            loadEspecialidades();
            return;
        }

        try {
            List<Especialidad> especialidades = especialidadService.listarEspecialidades();
            List<Especialidad> filteredEspecialidades = especialidades.stream()
                    .filter(e -> e.getNombre().toLowerCase().contains(searchText.toLowerCase()) ||
                                   e.getDescripcion().toLowerCase().contains(searchText.toLowerCase()))
                    .collect(Collectors.toList());
            updateTable(filteredEspecialidades);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al buscar especialidades: " + e.getMessage());
        }
    }

    private void updateTable(List<Especialidad> especialidades) {
        tableModel.setRowCount(0); // Clear existing data
        for (Especialidad especialidad : especialidades) {
            tableModel.addRow(new Object[]{especialidad.getId(), especialidad.getNombre(), especialidad.getDescripcion()});
        }
    }

    private void addEspecialidad() {
        EspecialidadDialog dialog = new EspecialidadDialog(null, true);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            Especialidad newEspecialidad = dialog.getEspecialidad();
            especialidadService.agregarEspecialidad(newEspecialidad);
            loadEspecialidades();
        }
    }

    private void editEspecialidad() {
        int selectedRow = especialidadTable.getSelectedRow();
        if (selectedRow >= 0) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            Especialidad existingEspecialidad = especialidadService.obtenerEspecialidadPorId(id);

            if (existingEspecialidad != null) {
                EspecialidadDialog dialog = new EspecialidadDialog(null, true, existingEspecialidad);
                dialog.setVisible(true);
                if (dialog.isConfirmed()) {
                    Especialidad updatedEspecialidad = dialog.getEspecialidad();
                    // Ensure the ID remains the same for update
                    updatedEspecialidad.setId(id);
                    especialidadService.editarEspecialidad(updatedEspecialidad);
                    loadEspecialidades();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una especialidad para editar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteEspecialidad() {
        int selectedRow = especialidadTable.getSelectedRow();
        if (selectedRow >= 0) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea eliminar esta especialidad?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                especialidadService.eliminarEspecialidad(id);
                loadEspecialidades();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una especialidad para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }
}