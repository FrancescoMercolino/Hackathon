package implementazioneDAO;

import DAO.TeamDAO;
import Database.ConnessioneDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
            PreparedStatement controllo = con.prepareStatement("SELECT COUNT(*) FROM team_utente WHERE LOWER(utente_nome) = LOWER(?)");
            controllo.setString(1, nome);
            ResultSet rs = controllo.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                if (count > 0) {
                    return false;
                }
            }

            PreparedStatement aggiungi = con.prepareStatement("insert into team_utente (team_nome, utente_nome) values (?, ?)");
            aggiungi.setString(1, Team);
            aggiungi.setString(2, nome);
            aggiungi.executeUpdate();

        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public String leggiNomeTeam(String username) throws SQLException {
        String nome = null;
        try{
            PreparedStatement leggiTeam = con.prepareStatement("SELECT team_nome FROM team_utente WHERE utente_nome = (?)");
            leggiTeam.setString(1, username);
            ResultSet rs = leggiTeam.executeQuery();

            if (rs.next()) {
                nome = rs.getString("team_nome");
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return nome;
    }

    public void iscrivitiAllHackathon(String Team, String Hackathon) throws SQLException {

        try {
            PreparedStatement iscriviti = con.prepareStatement("INSERT INTO team_hackathon (team_nome, hackathon_nome) VALUES (?, ?)");
            iscriviti.setString(1, Team);
            iscriviti.setString(2, Hackathon);
            iscriviti.executeUpdate();

        }catch(SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public String recuperaIscrizioneHackathon(String Team) throws SQLException{
        String iscrizione = null;

        try{
            PreparedStatement recupera = con.prepareStatement("SELECT * FROM team_hackathon WHERE team_nome = (?)");
            recupera.setString(1, Team);
            ResultSet rs = recupera.executeQuery();

            if (rs.next()) {
                iscrizione = rs.getString("hackathon_nome");
            }
        }catch(SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return iscrizione;
    }

    public void votaSoluzione(String nomeTeam, int voto) throws SQLException {

        try{
            PreparedStatement vota = con.prepareStatement("UPDATE team SET voto = ? WHERE nome_team = (?)");
            vota.setInt(1, voto);
            vota.setString(2, nomeTeam);
            vota.executeUpdate();

        }catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
