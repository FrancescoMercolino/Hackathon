package GUI;

import controller.ControllerHackathon;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class SchermataRegistrazione {
    private JPanel panelRegistrazione;
    private JTextField textNome;
    private JPasswordField textPassword;
    private JButton confermaButton;
    private JButton tornaIndietroButton;
    private JLabel labelNome;
    private JLabel labelPassword;
    private JFrame frame;

    public SchermataRegistrazione(ControllerHackathon controller, JFrame frameHome) {
        frame = new JFrame("Registrazione");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(panelRegistrazione);
        frame.pack();
        frame.setSize(400, 300);


        confermaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String nome = textNome.getText();
                char[] password = textPassword.getPassword();

                if (nome.isEmpty() || password.length == 0 ) {
                    JOptionPane.showMessageDialog(frame, "Compila i campi");
                    return;
                }

                if (password.length < 6) {
                    JOptionPane.showMessageDialog(frame, "Password troppo breve");
                    return;
                }

                try {
                   boolean sentinella = controller.registraUtenteDB(nome, password);

                   if(!sentinella) {
                       JOptionPane.showMessageDialog(panelRegistrazione, "Utente già esistente");
                   } else {
                       JOptionPane.showMessageDialog(panelRegistrazione, "Registrazione con successo!");
                       frameHome.setVisible(true);
                       frame.dispose();
                   }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage());
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

        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

    }

    public void mostraPanelRegistrazione() {
        frame.setVisible(true);
    }
}
