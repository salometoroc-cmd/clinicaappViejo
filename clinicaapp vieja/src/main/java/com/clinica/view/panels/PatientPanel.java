package com.clinica.view.panels;

import com.clinica.model.Paciente;
import com.clinica.service.PacienteService;
import com.clinica.view.dialogs.AddPatientDialog;
import com.clinica.view.dialogs.EditPatientDialog;
import com.clinica.view.listeners.DataChangeListener;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PatientPanel extends JPanel {
    private JTable patientTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton addButton, editButton, deleteButton, searchButton;
    private PacienteService pacienteService;
    private DataChangeListener listener;
    
    public PatientPanel(PacienteService pacienteService, DataChangeListener listener) {
        this.pacienteService = pacienteService;
        this.listener = listener;
        System.out.println("🆕 DEBUG: PatientPanel CONSTRUCTOR - Servicio: " + (pacienteService != null));
        initializePanel();
        loadPatientsData();
    }
    
    private void initializePanel() {
        System.out.println("🔧 DEBUG: Inicializando PatientPanel UI");
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(new Color(245, 245, 245));
        
        add(createSearchPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
        
        System.out.println("✅ DEBUG: PatientPanel UI inicializado");
    }
    
    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createTitledBorder("🔍 Búsqueda"));
        searchPanel.setBackground(Color.WHITE);
        
        searchField = new JTextField(20);
        searchButton = new JButton("Buscar");
        
        searchButton.addActionListener(e -> searchPatients());
        
        searchPanel.add(new JLabel("Buscar paciente:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        
        return searchPanel;
    }
    
    private JPanel createTablePanel() {
        System.out.println("🔧 DEBUG: Creando tabla de pacientes");
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("📋 Lista de Pacientes"));
        tablePanel.setBackground(Color.WHITE);
        
        String[] columnNames = {"Documento", "Nombre", "Apellido", "Email", "Teléfono", "Dirección", "Fecha Nac."};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer tabla no editable
            }
        };
        patientTable = new JTable(tableModel);
        
        // Estilo de tabla
        patientTable.getTableHeader().setBackground(new Color(70, 130, 180));
        patientTable.getTableHeader().setForeground(Color.WHITE);
        patientTable.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(patientTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        System.out.println("✅ DEBUG: Tabla creada - Columnas: " + columnNames.length);
        return tablePanel;
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(245, 245, 245));
        
        addButton = new JButton("Agregar Paciente");
        editButton = new JButton("Editar Paciente");
        deleteButton = new JButton("Eliminar Paciente");
        
        addButton.addActionListener(e -> addPatient());
        editButton.addActionListener(e -> editPatient());
        deleteButton.addActionListener(e -> deletePatient());
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        
        return buttonPanel;
    }
    
    private void loadPatientsData() {
    System.out.println("\n=== 🔍 DEBUG PatientPanel: INICIANDO loadPatientsData() ===");
    try {
        System.out.println("📋 DEBUG PatientPanel: Llamando a pacienteService.listarPacientes()");
        
        List<Paciente> pacientes = pacienteService.listarPacientes();
        
        System.out.println("✅ DEBUG PatientPanel: Service devolvió " + pacientes.size() + " pacientes");
        
        if (pacientes.isEmpty()) {
            System.out.println("⚠️ DEBUG PatientPanel: La lista de pacientes está VACÍA");
            // Verificar el servicio directamente
            System.out.println("🔍 DEBUG PatientPanel: Verificando servicio...");
            if (pacienteService == null) {
                System.out.println("❌ DEBUG PatientPanel: pacienteService es NULL!");
            } else {
                System.out.println("✅ DEBUG PatientPanel: pacienteService existe");
            }
        } else {
            for (int i = 0; i < pacientes.size(); i++) {
                Paciente p = pacientes.get(i);
                System.out.println("👤 DEBUG PatientPanel Paciente " + i + ": " + 
                    "Doc='" + p.getDocumento() + "', " +
                    "Nombre='" + p.getNombre() + "', " +
                    "Apellido='" + p.getApellido() + "', " +
                    "Email='" + p.getEmail() + "'");
            }
        }
        
        updateTable(pacientes);
        
    } catch (Exception e) {
        System.err.println("❌ ERROR en loadPatientsData: " + e.getMessage());
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al cargar pacientes: " + e.getMessage());
    }
    System.out.println("=== 🔍 DEBUG PatientPanel: loadPatientsData() FINALIZADO ===\n");
}
    
    private void searchPatients() {
        String searchText = searchField.getText().trim();
        if (searchText.isEmpty()) {
            loadPatientsData();
            return;
        }
        
        try {
            List<Paciente> pacientes = pacienteService.listarPacientes();
            pacientes.removeIf(p -> 
                !p.getNombre().toLowerCase().contains(searchText.toLowerCase()) &&
                !p.getApellido().toLowerCase().contains(searchText.toLowerCase()) &&
                !p.getDocumento().contains(searchText)
            );
            updateTable(pacientes);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al buscar pacientes: " + e.getMessage());
        }
    }
    
    private void updateTable(List<Paciente> pacientes) {
        System.out.println("🔄 DEBUG updateTable(): Actualizando con " + pacientes.size() + " pacientes");
        
        int filasAntes = tableModel.getRowCount();
        tableModel.setRowCount(0);
        System.out.println("📊 DEBUG: Tabla limpiada - Filas antes: " + filasAntes + ", después: 0");
        
        for (Paciente paciente : pacientes) {
            Object[] row = {
                paciente.getDocumento(),
                paciente.getNombre(),
                paciente.getApellido(),
                paciente.getEmail(),
                paciente.getTelefono(),
                paciente.getDireccion(),
                paciente.getFechaNacimiento()
            };
            tableModel.addRow(row);
            System.out.println("➕ DEBUG: Fila agregada - " + paciente.getNombre() + " " + paciente.getApellido());
        }
        
        System.out.println("📊 DEBUG: Tabla actualizada - Total filas ahora: " + tableModel.getRowCount());
        
        // Forzar actualización visual
        tableModel.fireTableDataChanged();
        patientTable.repaint();
        System.out.println("🎨 DEBUG: Actualización visual forzada");
    }
    
    private void addPatient() {
        System.out.println("\n🎯 DEBUG: Botón 'Agregar Paciente' clickeado");
        
        AddPatientDialog dialog = new AddPatientDialog(
            (Frame) SwingUtilities.getWindowAncestor(this), 
            pacienteService,
            () -> {
                System.out.println("🔄 DEBUG: Callback ejecutado desde diálogo");
                // Ejecutar en el hilo de Swing
                SwingUtilities.invokeLater(() -> {
                    System.out.println("🔄 DEBUG: Recargando datos en Swing Thread");
                    loadPatientsData();
                    this.revalidate();
                    this.repaint();
                });
            }
        );
        dialog.setVisible(true);
        
        if (listener != null) {
            listener.onDataChanged();
        }
        
        System.out.println("✅ DEBUG: Diálogo cerrado\n");
    }
    
    private void editPatient() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow >= 0) {
            String documento = (String) tableModel.getValueAt(selectedRow, 0);
            try {
                Paciente paciente = pacienteService.buscarPorDocumento(documento);
                if (paciente != null) {
                    EditPatientDialog dialog = new EditPatientDialog(
                        (Frame) SwingUtilities.getWindowAncestor(this), 
                        pacienteService, 
                        paciente,
                        () -> {
                            SwingUtilities.invokeLater(() -> {
                                loadPatientsData();
                                repaint();
                            });
                        }
                    );
                    dialog.setVisible(true);
                    if (listener != null) {
                        listener.onDataChanged();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontró el paciente");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al buscar el paciente: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un paciente para editar");
        }
    }
    
    private void deletePatient() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow >= 0) {
            String documento = (String) tableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar al paciente con documento: " + documento + "?",
                "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
                
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    pacienteService.eliminarPaciente(documento);
                    loadPatientsData();
                    if (listener != null) {
                        listener.onDataChanged();
                    }
                    JOptionPane.showMessageDialog(this, "Paciente eliminado exitosamente");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error al eliminar paciente: " + e.getMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un paciente para eliminar");
        }
    }

    private void initializePanel() {
    
    // ✅ VERIFICAR VISIBILIDAD Y TAMAÑO
    System.out.println("🔍 DEBUG: Verificando visibilidad del panel:");
    System.out.println("   - Visible: " + this.isVisible());
    System.out.println("   - Tamaño: " + this.getSize());
    System.out.println("   - Preferido: " + this.getPreferredSize());
    
    if (patientTable != null) {
        System.out.println("🔍 DEBUG: Verificando tabla:");
        System.out.println("   - Tabla visible: " + patientTable.isVisible());
        System.out.println("   - Tabla tamaño: " + patientTable.getSize());
        System.out.println("   - Filas en modelo: " + tableModel.getRowCount());
        System.out.println("   - Columnas en modelo: " + tableModel.getColumnCount());
    }
}
}