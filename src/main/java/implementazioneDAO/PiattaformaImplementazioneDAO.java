package implementazioneDAO;

import DAO.HackathonDAO;
import DAO.PiattaformaDAO;
import Database.ConnessioneDatabase;
import model.Hackathon;
import model.Team;
import model.Utente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class PiattaformaImplementazioneDAO implements PiattaformaDAO {


    private Connection con;
    private HackathonDAO hackathonDAO;

    public PiattaformaImplementazioneDAO() {
        try {
            con = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        hackathonDAO = new HackathonImplementazioneDAO();
    }

    @Override
    public ArrayList<Team> getClassifica(String hackathon) throws SQLException {

        ArrayList<Team> classifica = new ArrayList<>();

        try {
                PreparedStatement ps = con.prepareStatement("select * from classifica WHERE hackathon = ? ORDER BY punti DESC ");
                ps.setString(1, hackathon);
                ResultSet rs = ps.executeQuery();


                while (rs.next()) {
                    Hackathon hack = hackathonDAO.getHackathonFromDB(rs.getString("hackathon"));
                    classifica.add(new Team(
                            rs.getString("nome_team"),
                            rs.getInt("punti"),
                            hack
                    ));
                }

        } catch(SQLException e){
            e.printStackTrace();

        }

        return classifica;
    }

    @Override
    public boolean registraTeam(String utente, String team) throws SQLException {
        try {
            PreparedStatement controllo = con.prepareStatement("Select COUNT(*) FROM partecipante WHERE LOWER(nome_team) = LOWER(?) AND LOWER(nome) = LOWER(?)");
            controllo.setString(1, team);
            controllo.setString(2, utente);
            ResultSet rs = controllo.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return false;
            }

            PreparedStatement ps = con.prepareStatement("UPDATE partecipante SET nome_team = ? WHERE LOWER(nome) = LOWER(?)");
            ps.setString(1, team);
            ps.setString(2, utente);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}

