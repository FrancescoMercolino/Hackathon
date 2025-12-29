package Controller;

import DAO.*;
import Model.*;
import implementazioneDAO.*;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class ControllerHackathon {

    //Attributi
    private PiattaformaDAO piattaformaDAO;
    private UtenteDAO utenteDAO;
    private HackathonDAO hackathonDAO;
    private TeamDAO teamDAO;
    private ProblemaDAO problemaDAO;
    private DocumentoDAO documentoDAO;

    private Utente utente;
    private Problema problema;
    private Documento documento;
    private Team team;

    //Costruttore
    public ControllerHackathon() {
        this.utenteDAO = new UtenteImplementazioneDAO();
        this.piattaformaDAO = new PiattaformaImplementazioneDAO();
        this.hackathonDAO = new HackathonImplementazioneDAO();
        this.teamDAO = new TeamImplementazioneDAO();
        this.problemaDAO = new ProblemaImplementazioneDAO();
        this.documentoDAO  = new DocumentoImplementazioneDAO();
    }

    //Metodi

    //utenti
    public boolean login(String nome, char[] password) throws SQLException {
        boolean login = utenteDAO.eseguiLoginDB(nome, password);
        if(login) {
            utente = new Utente(nome, password);
        }
        return login;
    }

    public boolean registraUtenteDB(String nome, char[] password) throws SQLException {
        boolean registrato = utenteDAO.registraUtente(nome, password);
        if(registrato) {
            utente = new Utente(nome, password);
        }
        return registrato;
    }

    public boolean faParteTeam(String nome) throws SQLException {
        //controllo in memoria
        if(team != null){
            for (Utente u : team.getMembri()) {
                if(u.getNome().equals(nome)) return true;
            }
        }

        //lo cerco nel database e lo inserisco in memoria e carico tutti i membri già presenti
        if(utenteDAO.faParteTeam(nome)){
            this.team = new Team(teamDAO.getTeamDB(nome));
            ArrayList<Utente> membri = this.getMembri(teamDAO.getTeamDB(nome));

            this.team.setMembri(membri);

            return true;
        }

        //non fa parte di alcun team
        return false;
    }

    public String tipoUtente(String nome) throws SQLException {
        return utenteDAO.tipoUtente(nome);
    }

    public boolean utenteEsiste(String nome) throws SQLException {
        return utenteDAO.utenteEsiste(nome);
    }

    public boolean inserisciGiudice(String nome, String organizzatore) throws SQLException {
        return utenteDAO.inserisciGiudice(nome, organizzatore);
    }

    //Giudice
    public void votaSoluzione(String giudice, String nomeTeam, int voto) throws SQLException{
        utenteDAO.votaSoluzione(giudice, nomeTeam, voto);
    }

    //team
    public boolean registraTeamDB(String nome, String nomeTeam) throws SQLException {
        //crea il team in memoria
        this.team = new Team(nomeTeam);
        this.team.addUtente(utente);

        //salva nel DB
        return piattaformaDAO.registraTeam(nome, nomeTeam);
    }

    public void iscriviHackathon(String nomeTeam, String nomeHackathon) throws SQLException {
        Hackathon hackathon = hackathonDAO.getHackathonFromDB(nomeHackathon);
        team.iscriviHackathon(hackathon);
        teamDAO.iscrivitiAllHackathon(nomeTeam, nomeHackathon);
    }

    public boolean aggiungiAlTeam(String nome, String Team, String username) throws SQLException {
        //aggiungi il nuovo utente nel team in memoria
        Utente u = new Utente(nome, null);
        this.team.addUtente(u);

        //aggiorna il database
        return teamDAO.aggiungiAlTeam(nome, Team);
    }

    public String getTeam(String username) throws SQLException {
        return teamDAO.getTeamDB(username);
    }

    public ArrayList<Utente> getMembri(String nomeTeam) throws SQLException {
        ArrayList<Utente> membri = new ArrayList<>();

        membri = teamDAO.getMembriTeamDB(nomeTeam);
        return membri;

    }
    public String recuperaIscrizione(String nomeTeam) throws SQLException {
        return teamDAO.recuperaIscrizioneHackathon(nomeTeam);
    }

    //piattaforma
    public ArrayList<Team> getClassifica(String hackathon) throws SQLException {
        return piattaformaDAO.getClassifica(hackathon);
    }

    //Hackathon

    public void inserisciHackathon(String titolo, String sede, Date dataInizio, Date dataFine, int partecipanti, String stato) throws SQLException {
        hackathonDAO.inserisciNuovoHackathon(titolo, sede, dataInizio, dataFine, partecipanti, stato);
    }

    public ArrayList<String> riempiBox(JComboBox comboBox) throws SQLException {
        return hackathonDAO.getAllHackathon();
    }

    public ArrayList<String> recuperaIscrizioni(String hackathon) throws SQLException {
        return hackathonDAO.recuperaIscrizioniHackathon(hackathon);
    }

    public String getStato(String hackathon) throws SQLException {
        return hackathonDAO.getStatoHackathon(hackathon);
    }

    public int maxIscrittiHackathon (String hackathon) throws SQLException {
        return hackathonDAO.getMaxIscritti(hackathon);
    }

    public int teamIscrittiHackathon(String hackathon) throws SQLException {
        return hackathonDAO.teamIscrittiHackathon(hackathon);
    }

    public void apriIscrizioniHackathon(String hackathon) throws SQLException{
        hackathonDAO.apriIscrizioniHackathon(hackathon);
    }

    public void chiudiIscrizioniHackathon(String hackathon) throws SQLException{
        hackathonDAO.chiudiIscrizioniHackathon(hackathon);
    }

    public void terminaCompetizione(String hackathon) throws SQLException{
        hackathonDAO.terminaCompetizione(hackathon);
    }

    //Problema
    public void pubblicaProblema(String giudice, String problema, String hackathon) throws SQLException {
        problemaDAO.pubblicaProblema(giudice, problema, hackathon);
    }

    public String recuperaProblema(String hackathon) throws SQLException {
        return problemaDAO.leggiProblema(hackathon);
    }

    //Documento
    public void pubblicaSoluzione(String soluzione, String nomeTeam) throws SQLException {
       documentoDAO.aggiungiDocumento(nomeTeam, soluzione);
    }

    public String recuperaDocumento(String nomeTeam) throws SQLException {
        return documentoDAO.recuperaDocumento(nomeTeam);
    }

}
