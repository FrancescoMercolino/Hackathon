package implementazioneDAO;

import DAO.TeamDAO;
import DAO.HackathonDAO;
import Database.ConnessioneDatabase;
import model.Hackathon;
import model.Team;
import model.Utente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TeamImplementazioneDAO implements TeamDAO {

    private Connection con;
    private HackathonDAO hackathonDAO;

    public TeamImplementazioneDAO() {
        try {
            con = ConnessioneDatabase.getInstance().connection;
            hackathonDAO = new HackathonImplementazioneDAO();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean registraTeam(String utente, String team) throws SQLException {
        String query = "Select COUNT(*) FROM partecipante WHERE LOWER(nome_team) = LOWER(?) AND LOWER(nome) = LOWER(?)";

        try (PreparedStatement controllo = con.prepareStatement(query);) {
            controllo.setString(1, team);
            controllo.setString(2, utente);

            try (ResultSet rs = controllo.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return false;
                }
            }
        }

        String query2 = "UPDATE partecipante SET nome_team = ? WHERE LOWER(nome) = LOWER(?)";
        try (PreparedStatement ps = con.prepareStatement(query2);) {
            ps.setString(1, team);
            ps.setString(2, utente);
            ps.executeUpdate();
        }

        return true;
    }

    public boolean aggiungiAlTeam(String nome, String Team) throws SQLException{
        String query = "SELECT COUNT(*) FROM partecipante WHERE LOWER(nome) = LOWER(?) AND nome_team IS NOT NULL";
        String query2 = "UPDATE partecipante SET nome_team = ? where nome = ?";

        //controlla l'utente non fa già parte di un team
        try(PreparedStatement controllo = con.prepareStatement(query)){
            controllo.setString(1, nome);
            try (ResultSet rs = controllo.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    if (count > 0) {
                        return false;
                    }
                }
            }

            try (PreparedStatement aggiungi = con.prepareStatement(query2)){
                aggiungi.setString(1, Team);
                aggiungi.setString(2, nome);
                aggiungi.executeUpdate();
            }
        }
        return true;
    }

    public String getTeamDB(String username) throws SQLException {
        String nome = null;
        String query = "SELECT nome_team FROM partecipante WHERE nome = (?)";

        try(PreparedStatement leggiTeam = con.prepareStatement(query);){
            leggiTeam.setString(1, username);
            try (ResultSet rs = leggiTeam.executeQuery()) {
                if (rs.next()) {
                    nome = rs.getString("nome_team");
                }
            }
        }
        return nome;
    }

    public ArrayList<Utente> getMembriTeamDB(String team) throws SQLException{
        ArrayList<Utente> membri = new ArrayList<>();
        String query = "SELECT * FROM partecipante WHERE nome_team = (?)";

        try(PreparedStatement recupera = con.prepareStatement(query)){
            recupera.setString(1, team);
            try (ResultSet rs = recupera.executeQuery()) {
                while (rs.next()) {
                    Utente utente = new Utente(
                            rs.getString("nome"),
                            null);
                    membri.add(utente);
                }
            }
        }
        return membri;
    }

    public void iscrivitiAllHackathon(String Team, String Hackathon) throws SQLException {
        String query = "UPDATE team SET hackathon = ? WHERE LOWER(nome_team) = LOWER(?)";

        try (PreparedStatement iscriviti = con.prepareStatement(query)){
            iscriviti.setString(1, Hackathon);
            iscriviti.setString(2, Team);
            iscriviti.executeUpdate();
        }
    }

    public String recuperaIscrizioneHackathon(String Team) throws SQLException{
        String iscrizione = null;
        String query = "SELECT * FROM team WHERE nome_team = (?)";
        try(PreparedStatement recupera = con.prepareStatement(query)){
            recupera.setString(1, Team);

            try(ResultSet rs = recupera.executeQuery()){
                if (rs.next()) {
                    iscrizione = rs.getString("hackathon");
                }
            }
        }
        return iscrizione;
    }

    public boolean votoPresente(String teamCorrente) throws SQLException{
        String query = "SELECT COUNT(*) FROM voto WHERE nome_team = (?)";

        try (PreparedStatement recupera = con.prepareStatement(query)){
                recupera.setString(1, teamCorrente);

                try (ResultSet rs = recupera.executeQuery()){

                    if (rs.next()) {
                        int count = rs.getInt(1);
                        return count > 0;
                    }
                }
        }
        return false;
    }

    public ArrayList<Team> getClassifica(String hackathon) throws SQLException {
        ArrayList<Team> classifica = new ArrayList<>();
        String query = "SELECT * FROM classifica WHERE hackathon = ? ORDER BY punti DESC ";

        try (PreparedStatement ps = con.prepareStatement(query)){
            ps.setString(1, hackathon);
            try(ResultSet rs = ps.executeQuery()) {
                Hackathon hack = hackathonDAO.getHackathonFromDB(hackathon);

                while (rs.next()) {
                    classifica.add(new Team(
                            rs.getString("nome_team"),
                            rs.getInt("punti"),
                            hack
                    ));
                }
            }
        }
        return classifica;
    }
}
