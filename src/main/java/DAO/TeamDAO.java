package DAO;

import Model.Team;

import java.sql.SQLException;

public interface TeamDAO {
    boolean aggiungiAlTeam(String nome, String Team) throws SQLException;
    String leggiNomeTeam(String username) throws SQLException;
    void iscrivitiAllHackathon(String Team, String Hackathon) throws SQLException;
    String recuperaIscrizioneHackathon(String Team) throws SQLException;
    void votaSoluzione(String nomeTean, int voto) throws SQLException;
}
