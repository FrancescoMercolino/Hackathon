package DAO;

import Model.Team;
import Model.Utente;

import java.sql.SQLException;
import java.util.ArrayList;

public interface TeamDAO {
    boolean aggiungiAlTeam(String nome, String Team) throws SQLException;
    String getTeamDB(String username) throws SQLException;
    ArrayList<Utente> getMembriTeamDB(String team) throws SQLException;
    void iscrivitiAllHackathon(String Team, String Hackathon) throws SQLException;
    String recuperaIscrizioneHackathon(String Team) throws SQLException;

}
