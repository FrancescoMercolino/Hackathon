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
        String query = "INSERT INTO documento (descrizione, team_nome) VALUES (?,?)";

        try(PreparedStatement ps = con.prepareStatement(query)){
            ps.setString(1, testo);
            ps.setString(2, nomeTeam);
            ps.executeUpdate();
        }
    }

    @Override
    public String recuperaDocumento(String nomeTeam) throws SQLException {
        String soluzione = null;
        String query = "SELECT * FROM documento WHERE team_nome = ?";

        try (PreparedStatement ps = con.prepareStatement(query)){
            ps.setString(1, nomeTeam);
            try (ResultSet rs = ps.executeQuery()){
                if (rs.next()) {
                    soluzione = rs.getString("descrizione");
                }
            }
        }
        return soluzione;
    }
}
