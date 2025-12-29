package implementazioneDAO;

import DAO.TeamDAO;
import Database.ConnessioneDatabase;
import Model.Team;
import Model.Utente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TeamImplementazioneDAO implements TeamDAO {

    private Connection con;

    public TeamImplementazioneDAO() {
        try {
            con = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

   public boolean aggiungiAlTeam(String nome, String Team) throws SQLException{
        try{
            //controlla l'utente non fa già parte di un team
            PreparedStatement controllo = con.prepareStatement("SELECT COUNT(*) FROM partecipante WHERE LOWER(nome) = LOWER(?) AND nome_team IS NOT NULL");
            controllo.setString(1, nome);
            ResultSet rs = controllo.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                if (count > 0) {
                    return false;
                }
            }

            PreparedStatement aggiungi = con.prepareStatement("UPDATE partecipante SET nome_team = ? where nome = ?");
            aggiungi.setString(1, Team);
            aggiungi.setString(2, nome);
            aggiungi.executeUpdate();

        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public String getTeamDB(String username) throws SQLException {
        String nome = null;
        try{
            PreparedStatement leggiTeam = con.prepareStatement("SELECT nome_team FROM partecipante WHERE nome = (?)");
            leggiTeam.setString(1, username);
            ResultSet rs = leggiTeam.executeQuery();

            if (rs.next()) {
                nome = rs.getString("nome_team");
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return nome;
    }

    public ArrayList<Utente> getMembriTeamDB(String team) throws SQLException{
        ArrayList<Utente> membri = new ArrayList<>();

        try{
            PreparedStatement recupera = con.prepareStatement("SELECT * FROM partecipante WHERE nome_team = (?)");
            recupera.setString(1, team);
            ResultSet rs = recupera.executeQuery();

            while (rs.next()) {
                Utente utente = new Utente(
                        rs.getString("nome"),
                        null);
                membri.add(utente);

            }
        }catch(SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return membri;
    }

    public void iscrivitiAllHackathon(String Team, String Hackathon) throws SQLException {

        try {
            PreparedStatement iscriviti = con.prepareStatement("UPDATE team SET hackathon = ? WHERE LOWER(nome_team) = LOWER(?)");
            iscriviti.setString(1, Hackathon);
            iscriviti.setString(2, Team);
            iscriviti.executeUpdate();

        }catch(SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public String recuperaIscrizioneHackathon(String Team) throws SQLException{
        String iscrizione = null;

        try{
            PreparedStatement recupera = con.prepareStatement("SELECT * FROM team WHERE nome_team = (?)");
            recupera.setString(1, Team);
            ResultSet rs = recupera.executeQuery();

            if (rs.next()) {
                iscrizione = rs.getString("hackathon");
            }
        }catch(SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return iscrizione;
    }
}
