package DAO;
import model.Hackathon;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public interface HackathonDAO {

    ArrayList<String> getAllHackathon() throws SQLException;
    ArrayList<String> getAllHackathonAperti() throws SQLException;
    Hackathon getHackathonFromDB(String nomeHackathon) throws SQLException;
    ArrayList<String> recuperaIscrizioniHackathon(String hackathon) throws SQLException;
    void apriIscrizioniHackathon(String hackahton) throws SQLException;
    void inserisciNuovoHackathon(String titolo, String sede, Date dataInizio, Date dataFine, int partecipanti, String stato) throws SQLException;
    void terminaCompetizione(String hackathon) throws SQLException;
    void chiudiIscrizioniHackathon(String hackathon) throws SQLException;
    String getStatoHackathon(String hackathon) throws SQLException;
    int teamIscrittiHackathon(String hackathon) throws SQLException;
    int getMaxIscritti(String hackathon) throws SQLException;
}
