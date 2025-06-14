package Controller;

import DAO.PiattaformaDAO;
import Model.*;
import implementazioneDAO.PiattaformaImplementazioneDAO;
import implementazioneDAO.UtenteImplementazioneDAO;

import java.sql.SQLException;
import java.util.ArrayList;

public class ControllerHackathon {

    //Attributi
    public ArrayList<Team> listaSquadre;
    private Piattaforma piattaforma;

    //Costruttore
    public ControllerHackathon() {

    }

    //Metodi


    public boolean login(String nome, char[] password) throws SQLException {
        DAO.UtenteDAO utenteLogin = new UtenteImplementazioneDAO();
        return utenteLogin.eseguiLoginDB(nome, password);
    }

    public boolean registraUtenteDB(String nome, char[] password) throws SQLException {
        Utente u = new Utente(nome, password);
        u.registrazione(nome, password);

        DAO.UtenteDAO r = new UtenteImplementazioneDAO();
        return r.registraUtente(nome, password);

    }

    public boolean registraTeamDB(String nome, String team) throws SQLException {
        DAO.PiattaformaDAO p = new PiattaformaImplementazioneDAO();
        return p.registraTeam(nome, team);
    }

    public ArrayList<Team> getClassifica(String hackathon) throws SQLException {
        DAO.PiattaformaDAO piattaformaDAO = new PiattaformaImplementazioneDAO();
        listaSquadre = piattaformaDAO.getClassifica(hackathon);
        return listaSquadre;
    }

    public void addTeam(Team t) {
       this.listaSquadre.add(t);
    }

    public ArrayList<Team> getTeam() {

        ArrayList<Team> teams = new ArrayList<>();



        return teams;

    }
}
