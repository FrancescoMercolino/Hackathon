package DAO;

import model.Hackathon;
import model.Team;

import java.sql.SQLException;
import java.util.ArrayList;

public interface PiattaformaDAO {

    ArrayList<Team> getClassifica(String hackathon) throws SQLException;
    boolean registraTeam(String utente, String team) throws SQLException;

}
