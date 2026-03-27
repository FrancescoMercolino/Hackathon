package GUI;

import controller.ControllerHackathon;
import model.Utente;

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
    private JButton selezionaHackathonButton;
    private JComboBox HackathonBox;
    private JLabel labelSeleziona;
    private JButton aggiungiPartecipanteButton;
    private JButton leggiProblemaButton;
    private JLabel hackathonCorrenteLabel;
    private JLabel labelPartecipanti;
    private JLabel infoLabel;
    private String nomeTeam;
    private String username;
    private ControllerHackathon controller;
    public JFrame frameT;

    public SchermataTeam(ControllerHackathon controller, JFrame frameUtente, String username) throws SQLException {
        frameT = new JFrame("Schermata Team");
        frameT.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frameT.setContentPane(panelTeam);
        frameT.setVisible(true);
        frameT.setLocationRelativeTo(null);

        this.controller = controller;
        this.username = username;

        aggiornaInfo();
        aggiornaBottoni(controller);
        aggiornaBox();

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

                //controlla che la scelta sia valida
                if(scelta.isBlank() || scelta.isEmpty()) {
                    JOptionPane.showMessageDialog(frameUtente, "Nome non valido.");
                } else {
                    try {
                        //controlla che l'utente scelto esiste tra i registrati
                        boolean sentinella = controller.utenteEsiste(scelta);
                        if(sentinella && !controller.faParteTeam(scelta)) {
                            controller.aggiungiAlTeam(scelta, nomeTeam, username);
                            JOptionPane.showConfirmDialog(frameUtente, "Hai aggiunto " + scelta + " al team.");
                        } else {
                            JOptionPane.showMessageDialog(frameUtente, "Impossibile inserire l'utente " + scelta +".");
                        }
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
                    String stato = controller.getStato(hackathon);

                    if(!stato.equals("aperto")){
                        JOptionPane.showMessageDialog(frameUtente, "Impossibile iscriversi ad: " + hackathon);
                        return;
                    } else if (controller.teamIscrittiHackathon(hackathon) >= controller.maxIscrittiHackathon(hackathon) ) {
                        JOptionPane.showMessageDialog(frameUtente, "Iscrizioni chiuse per " + hackathon + " numero massimo partecipanti raggiunto.");
                        return;
                    }else {
                        controller.iscriviHackathon(nomeTeam, hackathon);
                        JOptionPane.showMessageDialog(frameUtente, "Iscrizione effettuata per: " + HackathonBox.getSelectedItem());
                    }

                    aggiornaBottoni(controller);
                    hackathonCorrenteLabel.setText(controller.recuperaIscrizione(nomeTeam));
                    frameT.pack();

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
                    String statoHackathon = controller.getStato(hackathonCorrenteLabel.getText());
                    if("aperto".equals(statoHackathon)) {
                        JOptionPane.showMessageDialog(frameUtente, "Hackathon non ancora iniziato");
                    }else {
                        String hackathon = controller.recuperaIscrizione(nomeTeam);
                        JOptionPane.showMessageDialog(null, controller.recuperaProblema(hackathon));
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frameUtente, "Problema", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    //metodi
    private void aggiornaBottoni(ControllerHackathon controller) throws SQLException {

        String stato = controller.getStato(hackathonCorrenteLabel.getText());

        String iscrizione = controller.recuperaIscrizione(nomeTeam);

        if(iscrizione != null){
            labelSeleziona.setVisible(false);
            HackathonBox.setVisible(false);
            selezionaHackathonButton.setVisible(false);
        }

       if(!stato.equals("aperto")) {
            aggiungiPartecipanteButton.setVisible(false);
        }
    }

    private void aggiornaInfo() throws SQLException {

        try{
            nomeTeam = controller.getTeam(username);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        labelNomeTeam.setText(nomeTeam);

        ArrayList<Utente> membri = controller.getMembri(nomeTeam);
        StringBuilder utentiBuilder = new StringBuilder();
        for(Utente u : membri){
            String nome = u.getNome();
            utentiBuilder.append(nome +" ");
        }
        String utenti = utentiBuilder.toString();
        labelUtenti.setText(utenti);

        hackathonCorrenteLabel.setText(controller.recuperaIscrizione(nomeTeam));
    }

    private void aggiornaBox(){

        try {
            ArrayList<String> li = controller.getListaHackathonAperti();
            for(String s : li){
                HackathonBox.addItem(s);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}

