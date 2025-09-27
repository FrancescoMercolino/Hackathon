package GUI;

import Controller.ControllerHackathon;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

public class SchermataUtente {
    private JPanel panelPartecipante;
    private JButton creaTeamButton;
    private JButton gestisciTeamButton;
    private JLabel labelUtente;
    private JButton logOutButton;
    private JButton selezionaGiudiceButton;
    private JButton classificaButton;
    private JButton pubblicaProblemaButton;
    private JButton apriIscrizioniButton;
    private JComboBox hackathonBox;
    private JLabel gestisciHackathon;
    private JButton valutaSoluzioniButton;
    private JButton assegnaVotoButton;
    public JFrame frameP;

    public SchermataUtente(ControllerHackathon controller, JFrame frameHome, String username) throws SQLException {
        frameP = new JFrame("Schermata Partecipante");
        frameP.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameP.setContentPane(panelPartecipante);
        frameP.pack();
        frameP.setVisible(true);
        frameP.setLocationRelativeTo(null);

        String tipo = controller.tipoUtente(username);
        selezionaGiudiceButton.setVisible(false);
        pubblicaProblemaButton.setVisible(false);
        apriIscrizioniButton.setVisible(false);
        aggiornaBottoni(controller, username, tipo);

        labelUtente.setText("Benvenuto: " + username);

        try {
            ArrayList<String> li = controller.riempiBox(hackathonBox);
            for(String s : li){
                hackathonBox.addItem(s);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }

        creaTeamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = JOptionPane.showInputDialog(frameP, "Inserisci nome del team");
                if(s.isBlank()) {
                    JOptionPane.showMessageDialog(frameP, "Inserisci nome del team");

                } else {
                    try {
                       boolean sentinella = controller.registraTeamDB(username, s);
                        aggiornaBottoni(controller, username, tipo);
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

        gestisciTeamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                SchermataTeam sct = null;
                try {
                    sct = new SchermataTeam(controller, frameP, username);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
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

        classificaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SchermataClassifica classifica = new SchermataClassifica(controller, frameP);
                classifica.frame.setVisible(true);
                frameP.setVisible(false);
            }
        });
        selezionaGiudiceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    String scelta = JOptionPane.showInputDialog(frameP, "Inserisci nome utente da promuovere come giudice");
                    if(scelta.isBlank()) {
                        JOptionPane.showMessageDialog(frameP, "Nome non valido");
                    } else {
                        try {
                            controller.inserisciGiudice(scelta);
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                                }
                            }
                    }
        });
        apriIscrizioniButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        pubblicaProblemaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextArea textArea = new JTextArea(20, 20);
                JOptionPane.showConfirmDialog(null, textArea, "Problema", JOptionPane.OK_CANCEL_OPTION);
                String descrizione = textArea.getText();
                try {
                    controller.pubblicaProblema(descrizione, hackathonBox.getSelectedItem().toString());
                }catch(SQLException ex) {
                    JOptionPane.showMessageDialog(frameP, ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });
        valutaSoluzioniButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ArrayList<String> listaTeam = controller.recuperaIscrizioni(hackathonBox.getSelectedItem().toString());

                    String[] teamArray = listaTeam.toArray(new String[0]);
                    int seleziona = JOptionPane.showOptionDialog(null, "Seleziona team:", "Boh", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, teamArray, teamArray[0]);

                    if(seleziona >= 0) {
                        String soluzione = controller.recuperaDocumento(teamArray[seleziona]);
                        if(soluzione != null) {
                            JOptionPane.showMessageDialog(frameP, soluzione);
                        }else {
                            JOptionPane.showMessageDialog(frameP, "Il team " + teamArray[seleziona] + " non ha ancora una soluzione.");
                        }
                    }


                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                } catch (ArrayIndexOutOfBoundsException ex) {
                    JOptionPane.showMessageDialog(frameP, "Non ci sono team registrati");
                }

            }
        });
        assegnaVotoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ArrayList<String> listaTeam = controller.recuperaIscrizioni(hackathonBox.getSelectedItem().toString());

                    String[] teamArray = listaTeam.toArray(new String[0]);
                    int seleziona = JOptionPane.showOptionDialog(null, "Seleziona team:", "Boh", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, teamArray, teamArray[0]);

                    if(seleziona >= 0) {
                        int voto = Integer.parseInt(JOptionPane.showInputDialog(null, "Inserisci voto al tem:", "Votare", JOptionPane.PLAIN_MESSAGE));
                        controller.votaSoluzione(teamArray[seleziona], voto);
                    }

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frameP, "Il voto è compreso tra 0 e 10. Riprova.");
                }
            }
        });
    }

    private void aggiornaBottoni(ControllerHackathon controller, String username, String privilegio) throws SQLException {
        boolean teamEsiste = controller.faParteTeam(username);
        creaTeamButton.setEnabled(!teamEsiste);
        gestisciTeamButton.setVisible(teamEsiste);
        if(privilegio.equalsIgnoreCase("organizzatore")) {
            selezionaGiudiceButton.setVisible(true);
            apriIscrizioniButton.setVisible(true);
            gestisciTeamButton.setVisible(false);
            creaTeamButton.setVisible(false);
            valutaSoluzioniButton.setVisible(false);
            assegnaVotoButton.setVisible(false);
        }
        if(privilegio.equalsIgnoreCase("giudice")) {
            pubblicaProblemaButton.setVisible(true);
            creaTeamButton.setVisible(false);
            gestisciTeamButton.setVisible(false);
        }
        if(privilegio.equalsIgnoreCase("partecipante")) {
            pubblicaProblemaButton.setVisible(false);
            valutaSoluzioniButton.setVisible(false);
            assegnaVotoButton.setVisible(false);
            gestisciHackathon.setVisible(false);
            hackathonBox.setVisible(false);
            apriIscrizioniButton.setVisible(false);
        }
    }
}
