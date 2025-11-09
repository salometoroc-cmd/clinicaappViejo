package com.clinica.view.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class SpecialityPanel extends JPanel {
    private JTable specialityTable;
    private DefaultTableModel tableModel;
    
    public SpecialityPanel() {
        initializePanel();
        loadSampleData();
    }
    
    private void initializePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(new Color(245, 245, 245));
        
        add(createTablePanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }
    
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("🎓 Especialidades Médicas"));
        tablePanel.setBackground(Color.WHITE);
        
        String[] columnNames = {"ID", "Nombre", "Descripción", "Número de Médicos"};
        tableModel = new DefaultTableModel(columnNames, 0);
        specialityTable = new JTable(tableModel);
        
        // Estilo de tabla
        specialityTable.getTableHeader().setBackground(new Color(70, 130, 180));
        specialityTable.getTableHeader().setForeground(Color.WHITE);
        specialityTable.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(specialityTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        return tablePanel;
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(245, 245, 245));
        
        JButton addButton = new JButton("Agregar Especialidad");
        JButton editButton = new JButton("Editar Especialidad");
        JButton deleteButton = new JButton("Eliminar Especialidad");
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        
        return buttonPanel;
    }
    
    private void loadSampleData() {
        Object[][] sampleData = {
            {"1", "Cardiología", "Enfermedades del corazón", "3"},
            {"2", "Pediatría", "Medicina para niños", "2"},
            {"3", "Dermatología", "Enfermedades de la piel", "4"}
        };
        
        for (Object[] row : sampleData) {
            tableModel.addRow(row);
        }
    }
}