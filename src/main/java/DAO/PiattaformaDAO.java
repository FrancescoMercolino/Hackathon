package DAO;

import Model.Hackathon;
import Model.Team;

import java.sql.SQLException;
import java.util.ArrayList;

public interface PiattaformaDAO {

    public ArrayList<Team> getClassifica(String hackathon) throws SQLException;
    public boolean registraTeam(String utente, String team) throws SQLException;

}
