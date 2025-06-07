package GUI;

import Controller.ControllerHackathon;

import javax.swing.*;

public class SchermataClassifica {

    private JPanel panelClassifica;
    private JTable table1;
    public JFrame frame;
    private ControllerHackathon controller;

    public SchermataClassifica(ControllerHackathon controller, JFrame frameHome) {
        this.frame = new JFrame("Schermata Classifica");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setContentPane(panelClassifica);
        this.frame.pack();
        this.frame.setVisible(true);

        controller = new ControllerHackathon();

        TableModelTeam table = new TableModelTeam();
        table1.setModel(table);
        table.setData(controller.getTeam());
    }
}
