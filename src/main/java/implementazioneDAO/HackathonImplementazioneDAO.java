package implementazioneDAO;

import DAO.HackathonDAO;
import Database.ConnessioneDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class HackathonImplementazioneDAO implements HackathonDAO {

    private Connection con;

    public HackathonImplementazioneDAO() {
        try {
            con = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getHackathon() throws SQLException{
        ArrayList<String> hackathon = new ArrayList<>();

        try{
            PreparedStatement ps = con.prepareStatement("select titolo from Hackathon");
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                hackathon.add(rs.getString("titolo"));
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }
        return hackathon;
    }

    public boolean registraHackathon(String team, String hackathon) throws SQLException{

        try{
            PreparedStatement ps = con.prepareStatement("ISERT INTO piattaforma(team_nome, hackathon) VALUES (?, ?)");
            ps.setString(1, team);
            ps.setString(2, hackathon);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public ArrayList<String> recuperaIscrizioniHackathon(String hackathon) throws SQLException{
        ArrayList<String> iscritti = new ArrayList<>();

        try{
            PreparedStatement ps = con.prepareStatement("SELECT team_nome FROM piattaforma WHERE hackathon_nome = ? ");
            ps.setString(1, hackathon);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                iscritti.add(rs.getString("team_nome"));
            }

        }catch (SQLException e){
            e.printStackTrace();
            throw e;
        }

        return iscritti;
    }
}
