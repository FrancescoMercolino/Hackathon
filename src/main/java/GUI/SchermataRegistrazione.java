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
    private JLabel labelNome;
    private JLabel labelPassword;

    private Piattaforma piattaforma;

    public JFrame frame;

    public SchermataRegistrazione(ControllerHackathon controller, JFrame frameHome) {
        frame = new JFrame("Registrazione");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(panelRegistrazione);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);


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
                   boolean sentinella = controller.registraUtenteDB(textNome.getText(), textPassword.getPassword());

                   if(!sentinella) {
                       JOptionPane.showMessageDialog(panelRegistrazione, "Utente già esistente");
                   } else {
                       JOptionPane.showMessageDialog(panelRegistrazione, "Registrazione con successo!");
                       frameHome.setVisible(true);
                       frame.dispose();
                   }
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
