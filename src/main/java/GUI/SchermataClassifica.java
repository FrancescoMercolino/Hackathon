package GUI;

import Controller.ControllerHackathon;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class SchermataClassifica {

    private JPanel panelClassifica;
    private JTable table1;
    private JButton tornaIndietroButton;
    private JButton mostraClassificaButton;
    private JComboBox hackathonComboBox;
    public JFrame frame;
    private ControllerHackathon controller;

    public SchermataClassifica(ControllerHackathon controller, JFrame frameHome) {
        this.frame = new JFrame("Schermata Classifica");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setContentPane(panelClassifica);
        this.frame.pack();
        this.frame.setVisible(true);
        this.frame.setLocationRelativeTo(null);


        this.hackathonComboBox.addItem(" ");
        this.hackathonComboBox.addItem("Hackathon Spring 2026");
        this.hackathonComboBox.addItem("Hackathon Summer 2026");

        TableModelTeam table = new TableModelTeam();
        table1.setModel(table);

        mostraClassificaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String hackathon = ((String) hackathonComboBox.getSelectedItem());
                if(!hackathon.isEmpty()) {
                    try {
                        table.setData(controller.getClassifica(hackathon));
                        table.fireTableDataChanged();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }

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
