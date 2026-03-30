package DAO;

import model.Team;
import model.Utente;

import java.sql.SQLException;
import java.util.ArrayList;

public interface TeamDAO {

    ArrayList<Team> getClassifica(String hackathon) throws SQLException;
    boolean registraTeam(String utente, String team) throws SQLException;
    boolean votoPresente(String teamCorrente) throws SQLException;
    boolean aggiungiAlTeam(String nome, String Team) throws SQLException;
    String getTeamDB(String username) throws SQLException;
    ArrayList<Utente> getMembriTeamDB(String team) throws SQLException;
    void iscrivitiAllHackathon(String Team, String Hackathon) throws SQLException;
    String recuperaIscrizioneHackathon(String Team) throws SQLException;

}
