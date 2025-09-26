package implementazioneDAO;

import DAO.DocumentoDAO;
import Database.ConnessioneDatabase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;

public class DocumentoImplementazioneDAO implements DocumentoDAO {

    private Connection con;

    public DocumentoImplementazioneDAO() {
        try {
            con = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void aggiungiDocumento(String nomeTeam, String testo) throws SQLException {

        try{
            PreparedStatement ps = con.prepareStatement("INSERT INTO documento (descrizione, team_nome) VALUES (?,?)");
            ps.setString(1, testo);
            ps.setString(2, nomeTeam);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public String recuperaDocumento(String nomeTeam) throws SQLException {
        String soluzione = null;

        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM documento WHERE team_nome = ?");
            ps.setString(1, nomeTeam);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                soluzione = rs.getString("descrizione");
            }

        }catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return soluzione;
    }
}
