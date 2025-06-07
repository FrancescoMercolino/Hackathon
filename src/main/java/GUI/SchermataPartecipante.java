package GUI;

import Controller.ControllerHackathon;

import javax.swing.*;

public class SchermataPartecipante {
    private JPanel panelPartecipante;
    private JButton creaTeamButton;
    private JButton gestisciTeamButton;
    private JButton scegliHackathonButton;
    public JFrame frameP;

    public SchermataPartecipante(ControllerHackathon controller, JFrame frameHome) {
        frameP = new JFrame("Schermata Partecipante");
        frameP.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameP.setContentPane(panelPartecipante);
        frameP.pack();
        frameP.setVisible(true);

    }
}
