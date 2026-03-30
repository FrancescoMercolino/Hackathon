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
        String query = "SELECT pubblica_problema(?, ?, ?)";

        try(PreparedStatement ps = con.prepareStatement(query)){
            ps.setString(1, problema);
            ps.setString(2, Hackathon);
            ps.setString(3, giudice);
            ps.execute();
        }
    }

    @Override
    public String leggiProblema(String hackathon) throws SQLException{

        String problema;
        String query = "SELECT descrizione_problema FROM problema WHERE hackathon = ?";

        try (PreparedStatement ps = con.prepareStatement(query)){
            ps.setString(1, hackathon);

            try (ResultSet rs = ps.executeQuery()){
                if (rs.next() ) {
                    return rs.getString("descrizione_problema");
                }
            }
        }
        return null;
    }
}
