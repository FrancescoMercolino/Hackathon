package GUI;

import Controller.ControllerHackathon;

import javax.swing.*;

public class SchermataPartecipante {
    private JPanel panel1;
    private JButton creaTeamButton;
    private JButton gestisciTeamButton;
    public JFrame frame;

    public SchermataPartecipante(ControllerHackathon controller, JFrame frameHome) {
        frame = frameHome;
        panel1 = new JPanel();
        panel1.setLayout(null);
        creaTeamButton = new JButton("Crea Team");

    }
}
