package GUI;

import controller.ControllerHackathon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
    private JButton organizzaHackathonButton;
    private JButton terminaCompetizioneButton;
    private JButton chiudiIscrizioniButton;
    public JFrame frameP;
    private final ControllerHackathon controller;

    public SchermataUtente(ControllerHackathon controller, JFrame frameHome, String username) throws SQLException {
        this.controller = controller;
        frameP = new JFrame("Schermata Partecipante");
        frameP.setContentPane(panelPartecipante);
        frameP.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        String tipo = controller.tipoUtente(username);
        aggiornaBottoni(controller, username, tipo); //choose which button will be visible

        frameP.setMinimumSize(frameP.getSize());
        frameP.pack();
        frameP.setLocationRelativeTo(null);

        labelUtente.setText("Benvenuto: " + username);
        aggiornaBox();

        creaTeamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    String nomeTeam = JOptionPane.showInputDialog(frameP, "Inserisci nome del team");

                    if(nomeTeam.isBlank()) {
                        JOptionPane.showMessageDialog(frameP, "Inserisci nome del team");
                    }else {
                       boolean sentinella = controller.registraTeamDB(username, nomeTeam);
                        aggiornaBottoni(controller, username, tipo);
                       if(!sentinella) {
                           JOptionPane.showMessageDialog(frameP, "Errore registrazione!");
                       } else {
                           JOptionPane.showMessageDialog(frameP, "Registrazione con successo!");
                           frameP.pack();
                       }
                    }
                }catch (SQLException ex) {
                    ex.printStackTrace();
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

                    if(scelta == null) {
                        return;
                    }

                    if(scelta.isBlank()) {
                        JOptionPane.showMessageDialog(frameP, "Nome non valido");
                        return;
                    }


                    try {

                        boolean inserimento = controller.inserisciGiudice(scelta, username);

                        if(inserimento) {
                            JOptionPane.showMessageDialog(frameP, "Giudice " +scelta + " inserito con successo!");
                        }else {
                            JOptionPane.showMessageDialog(frameP, "Il giudice " +scelta + " non esiste!");
                        }
                    }catch(SQLException ex) {
                        JOptionPane.showMessageDialog(frameP, ex);
                        ex.printStackTrace();
                    }
            }
        });

        pubblicaProblemaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextArea textArea = new JTextArea();
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(400, 300));
                JOptionPane.showConfirmDialog(null, scrollPane, "Problema", JOptionPane.OK_CANCEL_OPTION);

                String descrizione = textArea.getText();
                try {
                    controller.pubblicaProblema(username, descrizione, hackathonBox.getSelectedItem().toString());
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
                    int seleziona = JOptionPane.showOptionDialog(null, "Seleziona team:", "Team", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, teamArray, teamArray[0]);

                    if(seleziona >= 0) {
                        int voto = Integer.parseInt(JOptionPane.showInputDialog(null, "Inserisci voto al team:", "Votare", JOptionPane.PLAIN_MESSAGE));
                        controller.votaSoluzione(username, teamArray[seleziona], voto);
                    }

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frameP, "Il voto è compreso tra 0 e 10. Riprova.");
                } catch (ArrayIndexOutOfBoundsException ex) {
                    JOptionPane.showMessageDialog(frameP, "Non ci sono team registrati");
                }
            }
        });
        organizzaHackathonButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               try{
                   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                   formatter.setLenient(false);

                   String titolo = JOptionPane.showInputDialog(frameP, "Inserisci Nome hackathon");
                   if(titolo == null) return;

                   String dataInizio = JOptionPane.showInputDialog(frameP, "Inserisci Data Inizio (formato yyyy-MM-dd)");
                   if(dataInizio == null) return;
                   Date dataI = formatter.parse(dataInizio);

                   String dataFine = JOptionPane.showInputDialog(frameP, "Inserisci Data Fine (formato yyyy-MM-dd)");
                   if(dataFine== null) return;
                   Date dataF = formatter.parse(dataFine);

                   //controllo che le date siano giuste, così da fermare subito in caso di errore
                   if(!dataI.before(dataF)){
                       JOptionPane.showMessageDialog(frameP, "La data di inizio deve essere precedente alla data di fine");
                       return;
                   }

                   String sede = JOptionPane.showInputDialog(frameP, "Inserisci Sede");
                   if(sede == null) return;
                   String max = JOptionPane.showInputDialog(frameP, "Inserisci numero massimo partecipanti");
                   if(max == null) return;

                   int maxPartecipanti = Integer.parseInt(max);
                   controller.inserisciHackathon(titolo, sede, dataI, dataF, maxPartecipanti, "chiuse");
                   aggiornaBox();

               }catch(SQLException ex){
                   JOptionPane.showMessageDialog(frameP, ex.getMessage());
                   ex.printStackTrace();
               }catch (ParseException px){
                   JOptionPane.showMessageDialog(frameP, px.getMessage());
               }

            }
        });

        apriIscrizioniButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    controller.apriIscrizioniHackathon(hackathonBox.getSelectedItem().toString());
                    JOptionPane.showMessageDialog(frameP, "Iscrizioni aperte.");
                }catch (IllegalStateException ex){
                    JOptionPane.showMessageDialog(frameP, ex.getMessage());
                }catch(SQLException ex){
                    JOptionPane.showMessageDialog(frameP, ex);
                }
            }
        });

        terminaCompetizioneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int scelta = JOptionPane.showConfirmDialog(null, "Terminare competizione?", "ATTENZION", JOptionPane.YES_NO_OPTION);
                if (scelta == JOptionPane.YES_OPTION) {
                    try {
                    controller.terminaCompetizione(hackathonBox.getSelectedItem().toString());
                    JOptionPane.showMessageDialog(frameP, "Competizione terminata.");
                    } catch (IllegalStateException ex) {
                        JOptionPane.showMessageDialog(frameP, ex.getMessage());
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(frameP, ex.getMessage());
                    }
                }
            }
        });
        chiudiIscrizioniButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    controller.chiudiIscrizioniHackathon(hackathonBox.getSelectedItem().toString());
                    JOptionPane.showMessageDialog(frameP, "Iscrizioni chiuse.");
                }catch(IllegalStateException ex){
                    JOptionPane.showMessageDialog(frameP, ex.getMessage());
                }catch(SQLException ex){
                    JOptionPane.showMessageDialog(frameP, ex.getMessage());
                }
            }
        });

        frameP.setVisible(true);
    }

    private void aggiornaBox(){
        hackathonBox.removeAllItems();
        try {
            ArrayList<String> li = controller.riempiBox();
            for(String s : li){
                hackathonBox.addItem(s);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
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
            pubblicaProblemaButton.setVisible(false);

        }
        if(privilegio.equalsIgnoreCase("giudice")) {
            creaTeamButton.setVisible(false);
            gestisciTeamButton.setVisible(false);
            apriIscrizioniButton.setVisible(false);
            selezionaGiudiceButton.setVisible(false);
            apriIscrizioniButton.setVisible(false);
            terminaCompetizioneButton.setVisible(false);
            organizzaHackathonButton.setVisible(false);
            chiudiIscrizioniButton.setVisible(false);
        }
        if(privilegio.equalsIgnoreCase("partecipante")) {
            selezionaGiudiceButton.setVisible(false);
            pubblicaProblemaButton.setVisible(false);
            valutaSoluzioniButton.setVisible(false);
            assegnaVotoButton.setVisible(false);
            gestisciHackathon.setVisible(false);
            hackathonBox.setVisible(false);
            apriIscrizioniButton.setVisible(false);
            terminaCompetizioneButton.setVisible(false);
            organizzaHackathonButton.setVisible(false);
            chiudiIscrizioniButton.setVisible(false);
        }
    }
}
