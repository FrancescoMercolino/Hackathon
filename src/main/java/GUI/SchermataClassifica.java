package GUI;

import controller.ControllerHackathon;
import model.Team;

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
        this.controller = controller;

        this.frame = new JFrame("Schermata Classifica");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setContentPane(panelClassifica);
        this.frame.pack();
        this.frame.setVisible(true);
        this.frame.setLocationRelativeTo(null);

        aggiornaHackathonBox();

        TableModelTeam table = new TableModelTeam();
        table1.setModel(table);

        mostraClassificaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String hackathon = hackathonComboBox.getSelectedItem().toString();
                if(!hackathon.isEmpty() && hackathon != null) {
                    try {
                        String stato = controller.getStato(hackathon);
                        if(!stato.equals("terminate")) {
                            JOptionPane.showMessageDialog(frame, "Competizione non ancora terminata.");
                        }else{
                            ArrayList<Team> classifica = controller.getClassifica(hackathon);
                            if(classifica.isEmpty()){
                                JOptionPane.showMessageDialog(frame, "La classifica non è ancora stata aggiornata.");
                            }else {
                                table.setData(classifica);
                            }
                        }
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

    private void aggiornaHackathonBox() {
        try {
            ArrayList<String> li = controller.riempiBox();
            for(String s : li){
                hackathonComboBox.addItem(s);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
