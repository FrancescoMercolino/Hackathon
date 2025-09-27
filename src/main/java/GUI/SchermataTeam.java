package GUI;

import Controller.ControllerHackathon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

public class SchermataTeam {
    private JPanel panelTeam;
    private JButton proponiSoluzioneButton;
    private JLabel labelNomeTeam;
    private JLabel labelUtenti;
    private JButton tornaIndietroButton;
    private JLabel labelPartecipanti;
    private JButton selezionaHackathonButton;
    private JComboBox HackathonBox;
    private JLabel labelSeleziona;
    private JButton aggiungiPartecipanteButton;
    private JButton leggiProblemaButton;
    private JLabel hackathonCorrenteLabel;
    public JFrame frameT;

    public SchermataTeam(ControllerHackathon controller, JFrame frameUtente, String username) throws SQLException {
        frameT = new JFrame("Schermata Team");
        frameT.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameT.setContentPane(panelTeam);
        frameT.setVisible(true);
        frameT.setLocationRelativeTo(null);

        String nomeTeam;
        aggiornaBottoni(controller, username);
        try{
            nomeTeam = controller.getTeam(username);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        hackathonCorrenteLabel.setText(controller.recuperaIscrizione(nomeTeam));
        try {
            ArrayList<String> li = controller.riempiBox(HackathonBox);
            for(String s : li){
                HackathonBox.addItem(s);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }

        labelNomeTeam.setText(controller.getTeam(username));

        tornaIndietroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameUtente.setVisible(true);
                frameT.dispose();
            }
        });

        frameT.pack();

        aggiungiPartecipanteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String scelta = JOptionPane.showInputDialog(frameUtente, "Inserisci un Partecipante");
                if(scelta.isBlank()) {
                    JOptionPane.showMessageDialog(frameUtente, "Nome non valido");
                } else {
                    try {
                        controller.aggiungiAlTeam(scelta, nomeTeam);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        selezionaHackathonButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    String hackathon = HackathonBox.getSelectedItem().toString();
                    controller.iscriviHackathon(nomeTeam, hackathon);
                    JOptionPane.showMessageDialog(frameUtente, "Iscrizione effettuata per: " + HackathonBox.getSelectedItem());

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frameUtente,"Siete già iscritti ad un Hackathon", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        proponiSoluzioneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextArea textArea = new JTextArea();
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(400, 300));
                JOptionPane.showConfirmDialog(null, scrollPane, "Problema", JOptionPane.OK_CANCEL_OPTION);
                System.out.println(nomeTeam);
                String soluzione = textArea.getText();
                try {
                    controller.pubblicaSoluzione(soluzione, nomeTeam);
                }catch(SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frameUtente, "Problema", "Errore", JOptionPane.ERROR_MESSAGE);
                }


            }
        });
        leggiProblemaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String hackathon = controller.recuperaIscrizione(nomeTeam);
                    JOptionPane.showMessageDialog(null, controller.recuperaProblema(hackathon));
                }catch(SQLException ex) {
                    ex.printStackTrace();
                }

            }
        });
    }

    public void aggiornaBottoni(ControllerHackathon controller, String username){


    }
}
