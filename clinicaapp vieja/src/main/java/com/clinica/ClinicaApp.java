package com.clinica;

import com.clinica.service.*;
import com.clinica.view.MainFrame;
import javax.swing.*;

public class ClinicaApp {
    public static void main(String[] args) {
        PacienteService pacienteService = new PacienteService();
        MedicoService medicoService = new MedicoService();
        CitaService citaService = new CitaService();
        HistoriaConsultaService historiaService = new HistoriaConsultaService();
        EspecialidadService especialidadService = new EspecialidadService();

        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Crear y mostrar la ventana principal
            MainFrame mainFrame = new MainFrame(pacienteService, medicoService, citaService, historiaService, especialidadService);
            mainFrame.setVisible(true);
        });
    }
}