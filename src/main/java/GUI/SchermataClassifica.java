package GUI;

import Controller.ControllerHackathon;
import Model.Team;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

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

        try {
            ArrayList<String> li = controller.riempiBox(hackathonComboBox);
            for(String s : li){
                hackathonComboBox.addItem(s);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }

        TableModelTeam table = new TableModelTeam();
        table1.setModel(table);

        mostraClassificaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String hackathon = hackathonComboBox.getSelectedItem().toString();
                if(!hackathon.isEmpty() && hackathon != null) {
                    try {;
                        ArrayList<Team> classifica = controller.getClassifica(hackathon);
                        table.setData(classifica);
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
