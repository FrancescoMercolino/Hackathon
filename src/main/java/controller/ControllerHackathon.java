package Controller;

import DAO.*;
import Model.*;
import implementazioneDAO.*;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class ControllerHackathon {

    //Attributi
    public ArrayList<Team> listaSquadre;
    private PiattaformaDAO piattaformaDAO;
    private UtenteDAO utenteDAO;
    private HackathonDAO hackathonDAO;
    private TeamDAO teamDAO;
    private ProblemaDAO problemaDAO;
    private DocumentoDAO documentoDAO;

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

    public boolean login(String nome, char[] password) throws SQLException {
        return utenteDAO.eseguiLoginDB(nome, password);
    }

    public boolean registraUtenteDB(String nome, char[] password) throws SQLException {
        return utenteDAO.registraUtente(nome, password);
    }

    public boolean registraTeamDB(String nome, String team) throws SQLException {

        return piattaformaDAO.registraTeam(nome, team);
    }

    public ArrayList<Team> getClassifica(String hackathon) throws SQLException {
        return piattaformaDAO.getClassifica(hackathon);
    }

    public boolean faParteTeam(String nome) throws SQLException {
        return utenteDAO.faParteTeam(nome);
    }

    public String tipoUtente(String nome) throws SQLException {
        return utenteDAO.tipoUtente(nome);
    }

    public ArrayList<String> riempiBox(JComboBox comboBox) throws SQLException {
        return hackathonDAO.getHackathon();
    }

    public void iscriviHackathon(String team, String hackathon) throws SQLException {
        teamDAO.iscrivitiAllHackathon(team, hackathon);
    }

    public void pubblicaProblema(String problema, String hackathon) throws SQLException {
        problemaDAO.pubblicaProblema(problema, hackathon);
    }

    public boolean inserisciGiudice(String nome) throws SQLException {
        utenteDAO.inserisciGiudice(nome);
        return true;
    }

    public boolean aggiungiAlTeam(String nome, String Team) throws SQLException {
        return teamDAO.aggiungiAlTeam(nome, Team);
    }

    public void addTeam(Team t) {
       this.listaSquadre.add(t);
    }

    public String getTeam(String username) throws SQLException /* throws SQLException */{
        return teamDAO.leggiNomeTeam(username);
    }

    public String recuperaProblema(String hackathon) throws SQLException {
        return problemaDAO.leggiProblema(hackathon);
    }

    public String recuperaIscrizione(String nomeTeam) throws SQLException {
        return teamDAO.recuperaIscrizioneHackathon(nomeTeam);
    }

    public void pubblicaSoluzione(String soluzione, String nomeTeam) throws SQLException {
       documentoDAO.aggiungiDocumento(nomeTeam, soluzione);
    }

    public String recuperaDocumento(String nomeTeam) throws SQLException {
        return documentoDAO.recuperaDocumento(nomeTeam);
    }

    public ArrayList<String> recuperaIscrizioni(String hackathon) throws SQLException {
        return hackathonDAO.recuperaIscrizioniHackathon(hackathon);
    }

    public void votaSoluzione(String nomeTeam, int voto) throws SQLException{
        teamDAO.votaSoluzione(nomeTeam, voto);
    }
}
