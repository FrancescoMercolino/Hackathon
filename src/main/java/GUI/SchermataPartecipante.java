package GUI;

import Controller.ControllerHackathon;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class SchermataPartecipante {
    private JPanel panelPartecipante;
    private JButton creaTeamButton;
    private JButton gestisciTeamButton;
    private JButton scegliHackathonButton;
    private JLabel labelUtente;
    private JButton logOutButton;
    public JFrame frameP;

    public SchermataPartecipante(ControllerHackathon controller, JFrame frameHome, String username) {
        frameP = new JFrame("Schermata Partecipante");
        frameP.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameP.setContentPane(panelPartecipante);
        frameP.pack();
        frameP.setVisible(true);
        frameP.setLocationRelativeTo(null);


        labelUtente.setText("Benvenuto: " + username);

        creaTeamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = JOptionPane.showInputDialog(frameP, "Inserisci nome del team");
                if(s.isBlank()) {
                    JOptionPane.showMessageDialog(frameP, "Inserisci nome del team");
                } else {
                    try {
                       boolean sentinella = controller.registraTeamDB(username, s);

                       if(!sentinella) {
                           JOptionPane.showMessageDialog(frameP, "Errore registrazione!");
                       } else {
                           JOptionPane.showMessageDialog(frameP, "Registrazione con successo!");
                       }
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameHome.setVisible(true);
                frameP.dispose();
            }
        });
    }
}
