package com.clinica.view.panels;

import com.clinica.model.Cita;
import com.clinica.model.Paciente;
import com.clinica.service.CitaService;
import com.clinica.service.PacienteService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class HistoryPanel extends JPanel {

    private JTable historyTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> patientComboBox;
    private JTextArea detailArea;

    private CitaService historiaService;
    private PacienteService pacienteService;

    public HistoryPanel(CitaService historiaService, PacienteService pacienteService) {
        this.historiaService = historiaService;
        this.pacienteService = pacienteService;
        initializePanel();
        loadPatients();
        loadHistoryData();
    }

    private void initializePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(new Color(250, 250, 250));

        // Top: Filtro por paciente
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBackground(new Color(250, 250, 250));
        filterPanel.add(new JLabel("Filtrar por paciente:"));

        patientComboBox = new JComboBox<>();
        patientComboBox.addItem("Todos los pacientes");
        filterPanel.add(patientComboBox);

        JButton filterButton = new JButton("Filtrar");
        filterButton.addActionListener(e -> filterHistory());
        filterPanel.add(filterButton);

        add(filterPanel, BorderLayout.NORTH);

        // Centro: Tabla de historial
        String[] columnNames = {"ID", "Fecha", "Paciente", "Médico", "Estado"};
        tableModel = new DefaultTableModel(columnNames, 0);
        historyTable = new JTable(tableModel);
        historyTable.setRowHeight(24);
        historyTable.getTableHeader().setBackground(new Color(100, 149, 237));
        historyTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(historyTable);
        add(scrollPane, BorderLayout.CENTER);

        // Detalles
        detailArea = new JTextArea(5, 40);
        detailArea.setEditable(false);
        detailArea.setLineWrap(true);
        detailArea.setWrapStyleWord(true);
        JScrollPane detailScroll = new JScrollPane(detailArea);
        detailScroll.setBorder(BorderFactory.createTitledBorder("📝 Detalles de la Cita"));
        add(detailScroll, BorderLayout.SOUTH);

        // Listener para mostrar detalles
        historyTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = historyTable.getSelectedRow();
                if (row >= 0) {
                    String detalles = "ID: " + tableModel.getValueAt(row, 0) + "\n" +
                                      "Fecha: " + tableModel.getValueAt(row, 1) + "\n" +
                                      "Paciente: " + tableModel.getValueAt(row, 2) + "\n" +
                                      "Médico: " + tableModel.getValueAt(row, 3) + "\n" +
                                      "Estado: " + tableModel.getValueAt(row, 4);
                    detailArea.setText(detalles);
                }
            }
        });
    }

    private void loadPatients() {
        try {
            List<Paciente> pacientes = pacienteService.listarPacientes();
            for (Paciente p : pacientes) {
                patientComboBox.addItem(p.getNombreCompleto());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar pacientes: " + e.getMessage());
        }
    }

    private void loadHistoryData() {
        try {
            List<Cita> historial = historiaService.listarCitas();
            updateTable(historial);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar historial: " + e.getMessage());
        }
    }

    private void updateTable(List<Cita> citas) {
        tableModel.setRowCount(0);
        for (Cita cita : citas) {
            tableModel.addRow(new Object[]{
                cita.getId(),
                cita.getFecha(),
                cita.getPaciente().getNombreCompleto(),
                cita.getMedico().getNombreCompleto(),
                cita.getEstado().toString()
            });
        }
    }

    private void filterHistory() {
        String seleccionado = (String) patientComboBox.getSelectedItem();
        if (seleccionado == null || seleccionado.equals("Todos los pacientes")) {
            loadHistoryData();
            return;
        }

        try {
            List<Cita> todas = historiaService.listarCitas();
            List<Cita> filtradas = todas.stream()
                .filter(c -> c.getPaciente().getNombreCompleto().equalsIgnoreCase(seleccionado))
                .collect(Collectors.toList());
            updateTable(filtradas);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al filtrar historial: " + e.getMessage());
        }
    }
}
