package DAO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface HackathonDAO {

    ArrayList<String> getHackathon() throws SQLException;
    boolean registraHackathon(String team, String hackathon) throws SQLException;
    ArrayList<String> recuperaIscrizioniHackathon(String hackathon) throws SQLException;
}
