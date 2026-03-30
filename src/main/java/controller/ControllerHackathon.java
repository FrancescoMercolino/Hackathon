package controller;

import DAO.*;
import model.*;
import implementazioneDAO.*;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class ControllerHackathon {

    //Attributi
    private UtenteDAO utenteDAO;
    private HackathonDAO hackathonDAO;
    private TeamDAO teamDAO;
    private ProblemaDAO problemaDAO;
    private DocumentoDAO documentoDAO;

    private Utente utente;
    private Problema problema;
    private Documento documento;
    private Team team;
    private Hackathon hackathon;

    //Costruttore
    public ControllerHackathon() {
        this.utenteDAO = new UtenteImplementazioneDAO();
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
        boolean faParteTeam = faParteTeam(nome);
        if(faParteTeam) {
            throw new IllegalStateException("L'utente selezionato è attivo in un team");
        }
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
        return teamDAO.registraTeam(nome, nomeTeam);
    }

    public void iscriviHackathon(String nomeTeam, String nomeHackathon) throws SQLException {
        Hackathon hackathon = hackathonDAO.getHackathonFromDB(nomeHackathon);
        String stato = hackathonDAO.getStatoHackathon(nomeHackathon);

        if(!"aperto".equals(stato)){
            throw new SQLException("Iscrizioni terminate per: " + nomeHackathon);
        }
        if (teamIscrittiHackathon(nomeHackathon) >= maxIscrittiHackathon(nomeHackathon)) {
            throw new SQLException("Impossibile iscriversi ad: " + nomeHackathon +" numero massimo partecipanti raggiunto");
        }
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

    //Classifica
    public ArrayList<Team> getClassifica(String hackathon) throws SQLException {
        return teamDAO.getClassifica(hackathon);
    }

    //Hackathon

    public void inserisciHackathon(String titolo, String sede, Date dataInizio, Date dataFine, int partecipanti, String stato) throws SQLException {
        hackathonDAO.inserisciNuovoHackathon(titolo, sede, dataInizio, dataFine, partecipanti, stato);
    }

    public ArrayList<String> getListaHackathonAperti() throws SQLException {
        return hackathonDAO.getAllHackathonAperti();
    }

    public ArrayList<String> riempiBox() throws SQLException {
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

    public void apriIscrizioniHackathon(String hackathon) throws SQLException, IllegalStateException{
        String stato = getStato(hackathon);

        if(stato.equals("chiuse")){
            hackathonDAO.apriIscrizioniHackathon(hackathon);
        }else if(stato.equals("aperto")){
            throw new IllegalStateException("Le iscrizioni per l'hackathon selezionato sono già aperte");
        }else if(stato.equals("terminate")){
            throw new IllegalStateException("L'hackathon è terminato.");
        }
    }

    public void chiudiIscrizioniHackathon(String hackathon) throws SQLException, IllegalStateException{
        String stato = getStato(hackathon);

        if(stato.equals("aperto")){
            hackathonDAO.chiudiIscrizioniHackathon(hackathon);
        }else if(stato.equals("chiuse")){
            throw new IllegalStateException("Le iscrizioni per l'hackathon selezionato sono già chiuse");
        }else if(stato.equals("terminate")){
            throw new IllegalStateException("L'hackathon è terminato.");
        }

    }

    public void terminaCompetizione(String hackathon) throws SQLException, IllegalStateException{
        String stato = getStato(hackathon);
        if(!stato.equals("terminate")){
            hackathonDAO.terminaCompetizione(hackathon);
        }else {
            throw new IllegalStateException("L'hackathon selezionato è già terminato");
        }
    }

    //Problema
    public void pubblicaProblema(String giudice, String problema, String hackathon) throws SQLException {
        problemaDAO.pubblicaProblema(giudice, problema, hackathon);
    }

    public String recuperaProblema(String hackathon) throws SQLException {

        String stato = getStato(hackathon);

        if("aperto".equals(stato)){
            throw new SQLException("Hackathon non ancora inziato");
        }

        String problema = problemaDAO.leggiProblema(hackathon);

        if(problema == null) {
            throw new IllegalArgumentException("Problema non ancora pubblicato");
        }

        return problema;
    }

    //Documento
    public void pubblicaSoluzione(String soluzione, String nomeTeam) throws SQLException {
       documentoDAO.aggiungiDocumento(nomeTeam, soluzione);
    }

    public String recuperaDocumento(String nomeTeam) throws SQLException {
        return documentoDAO.recuperaDocumento(nomeTeam);
    }

    public boolean votoPresente(String teamCorrente) throws SQLException{
        return teamDAO.votoPresente(teamCorrente);
    }
}
