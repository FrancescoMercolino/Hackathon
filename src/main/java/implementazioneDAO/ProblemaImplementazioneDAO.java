package implementazioneDAO;

import DAO.ProblemaDAO;
import Database.ConnessioneDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProblemaImplementazioneDAO implements ProblemaDAO {

    private Connection con;

    public ProblemaImplementazioneDAO() {
        try {
            con = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pubblicaProblema(String giudice, String problema, String Hackathon) throws SQLException {
        try {
            PreparedStatement ps = con.prepareStatement("SELECT pubblica_problema(?, ?, ?)");
            ps.setString(1, problema);
            ps.setString(2, Hackathon);
            ps.setString(3, giudice);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public String leggiProblema(String hackathon) throws SQLException{

        String prob = null;

        try {
            PreparedStatement ps = con.prepareStatement("SELECT descrizione_problema FROM problema WHERE hackathon = ?");
            ps.setString(1, hackathon);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                prob = rs.getString("descrizione_problema");
            }
        }catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return prob;
    }
}
