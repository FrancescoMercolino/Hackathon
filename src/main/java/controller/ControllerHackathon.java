package Controller;

import DAO.LoginDAO;
import DAO.RegistrazioneDAO;
import Model.*;
import implementazioneDAO.LoginImplementazioneDAO;
import implementazioneDAO.RegistrazioneImplementazioneDAO;

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


    public boolean login(String nome, String password) {
        LoginDAO utenteLogin = new LoginImplementazioneDAO();
        return utenteLogin.eseguiLoginDB(nome, password);
    }

    public void registraUtenteDB(String nome, String password) throws SQLException {
        Utente u = new Utente(nome, password);
        u.registrazione(nome, password);

        RegistrazioneDAO r = new RegistrazioneImplementazioneDAO();
        r.registraUtente(nome, password);
    }


    public void addTeam(Team t) {
       this.listaSquadre.add(t);
    }
}
