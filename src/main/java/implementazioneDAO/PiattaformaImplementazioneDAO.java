package implementazioneDAO;

import DAO.PiattaformaDAO;
import Database.ConnessioneDatabase;
import Model.Hackathon;
import Model.Team;
import Model.Utente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class PiattaformaImplementazioneDAO implements PiattaformaDAO {


    private Connection con;

    public PiattaformaImplementazioneDAO() {
        try {
            con = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Team> getClassifica(String hackathon) throws SQLException {

        ArrayList<Team> classifica = new ArrayList<>();


        try {
                PreparedStatement ps = con.prepareStatement("select * from team WHERE hackathon = ? ORDER BY punti DESC ");
                ps.setString(1, hackathon);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    String nome = rs.getString("nome");
                    String membri = rs.getString("membr01");
                    int punti = rs.getInt("punti");

                    Team t = new Team(nome, membri, hackathon, punti);

                    classifica.add(t);
                }

        } catch(SQLException e){
            e.printStackTrace();

        }

        return classifica;
    }

    @Override
    public boolean registraTeam(String utente, String team) throws SQLException {
        try {
            PreparedStatement controllo = con.prepareStatement("Select COUNT(*) FROM team_utente WHERE LOWER(team_nome) = LOWER(?)");
            controllo.setString(1, team);
            ResultSet rs = controllo.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return false;
            }

            PreparedStatement ps = con.prepareStatement("INSERT INTO team_utente (team_nome, utente_nome) VALUES ( ?, ?)");
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

