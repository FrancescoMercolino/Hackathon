package GUI;

import Controller.ControllerHackathon;
import Model.Piattaforma;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class SchermataRegistrazione {
    private JPanel panelRegistrazione;
    private JTextField textNome;
    private JPasswordField textPassword;
    private JComboBox ruoloComboBox;
    private JButton confermaButton;
    private JButton tornaIndietroButton;
    private ControllerHackathon controller;
    private Piattaforma piattaforma;
    public JFrame frame;

    public SchermataRegistrazione(ControllerHackathon controller, JFrame frameHome) {
        frame = new JFrame("Registrazione");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(panelRegistrazione);
        frame.pack();
        frame.setVisible(true);



        this.ruoloComboBox.addItem((""));
        this.ruoloComboBox.addItem("Partecipante");
        this.ruoloComboBox.addItem("Giudice");
        this.ruoloComboBox.addItem("Organizzatore");


        confermaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controller.registraUtenteDB(textNome.getText(), textPassword.getText());
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        tornaIndietroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameHome.setVisible(true);
                frame.dispose();
            }
        });
    }
}
