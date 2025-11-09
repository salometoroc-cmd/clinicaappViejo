package com.clinica.view;

import com.clinica.service.*;
import com.clinica.view.panels.*;
import com.clinica.view.listeners.DataChangeListener;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame implements DataChangeListener {
    private JTabbedPane tabbedPane;
    private PacienteService pacienteService;
    private MedicoService medicoService;
    private CitaService citaService;
    private EspecialidadService especialidadService;
    
    public MainFrame(PacienteService pacienteService, MedicoService medicoService, 
                     CitaService citaService, HistoriaConsultaService historiaService, EspecialidadService especialidadService) {
        this.pacienteService = pacienteService;
        this.medicoService = medicoService;
        this.citaService = citaService;
        this.especialidadService = especialidadService;
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("🏥 Sistema de Gestión Médica - Clínica");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));
        
        
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Panel de pestañas
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        // Inyectar servicios en los paneles
        tabbedPane.addTab("👥 Pacientes", new PatientPanel(pacienteService, this));
        tabbedPane.addTab("👨‍⚕️ Médicos", new DoctorPanel(medicoService, especialidadService, this));
        tabbedPane.addTab("🎓 Especialidades", new EspecialidadPanel(especialidadService));
        tabbedPane.addTab("📅 Citas", new AppointmentPanel(citaService, pacienteService, medicoService));
        tabbedPane.addTab("📋 Historial", new HistoryPanel(citaService, pacienteService));  
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        mainPanel.add(createStatusBar(), BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
    }

    @Override
    public void onDataChanged() {
        AppointmentPanel appointmentPanel = (AppointmentPanel) tabbedPane.getComponentAt(3);
        appointmentPanel.loadDoctorsAndPatients();
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("🏥 Clínica Médica San Lucas");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("Sistema de Gestión Integral");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(200, 220, 255));
        
        JPanel textPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        textPanel.setBackground(new Color(70, 130, 180));
        textPanel.add(titleLabel);
        
        headerPanel.add(textPanel, BorderLayout.WEST);
        headerPanel.add(subtitleLabel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createStatusBar() {
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        statusPanel.setBackground(new Color(240, 240, 240));
        
        JLabel statusLabel = new JLabel("✅ Sistema integrado y funcionando");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        JLabel userLabel = new JLabel("Usuario: Administrador");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        userLabel.setForeground(Color.DARK_GRAY);
        
        statusPanel.add(statusLabel, BorderLayout.WEST);
        statusPanel.add(userLabel, BorderLayout.EAST);
        
        return statusPanel;
    }
}
