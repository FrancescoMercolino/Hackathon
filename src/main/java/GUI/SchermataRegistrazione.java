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


        confermaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (textNome.getText().isEmpty() || textPassword.getPassword().length == 0 ) {
                    JOptionPane.showMessageDialog(frame, "Compila i campi");
                    return;
                }

                if (textPassword.getPassword().length < 6) {
                    JOptionPane.showMessageDialog(frame, "Password troppo breve");
                    return;
                }

                try {
                    controller.registraUtenteDB(textNome.getText(), textPassword.getPassword());
                    JOptionPane.showMessageDialog(panelRegistrazione, "Registrazione con successo!");
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
