package implementazioneDAO;

import DAO.HackathonDAO;
import Database.ConnessioneDatabase;
import model.Hackathon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class HackathonImplementazioneDAO implements HackathonDAO {

    private Connection con;

    public HackathonImplementazioneDAO() {
        try {
            con = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getAllHackathon() throws SQLException{
        ArrayList<String> hackathon = new ArrayList<>();
        String query = "SELECT titolo FROM Hackathon";

        try(PreparedStatement ps = con.prepareStatement(query)){
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    hackathon.add(rs.getString("titolo"));
                }
            }
        }
        return hackathon;
    }

    public ArrayList<String> getAllHackathonAperti() throws SQLException{
        ArrayList<String> hackathon = new ArrayList<>();
        String query = "SELECT titolo FROM Hackathon WHERE stato = 'aperto'";

        try(PreparedStatement ps = con.prepareStatement(query)){
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    hackathon.add(rs.getString("titolo"));
                }
            }
        }
        return hackathon;
    }

    public ArrayList<String> recuperaIscrizioniHackathon(String hackathon) throws SQLException{
        ArrayList<String> iscritti = new ArrayList<>();
        String query = "SELECT * FROM team WHERE hackathon = ? ";

        try(PreparedStatement ps = con.prepareStatement(query)){
            ps.setString(1, hackathon);
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    iscritti.add(rs.getString("nome_team"));
                }
            }
        }
        return iscritti;
    }

    public void apriIscrizioniHackathon(String hackathon) throws SQLException{
        String query = "UPDATE Hackathon SET stato = 'aperto' WHERE titolo = ?";

        try(PreparedStatement apri = con.prepareStatement(query)){
            apri.setString(1, hackathon);
            apri.executeUpdate();
        }
    }

    public Hackathon getHackathonFromDB(String nomeHackathon) throws SQLException{
        String query = "SELECT * FROM Hackathon WHERE titolo = ?";

        try(PreparedStatement recupera = con.prepareStatement(query)){
            recupera.setString(1, nomeHackathon);
            Hackathon hackathon = null;

            try(ResultSet rs = recupera.executeQuery()){

                if(rs.next()){
                    hackathon = new Hackathon(rs.getString("titolo"),
                            rs.getString("sede"),
                            rs.getDate("datainizio"),
                            rs.getDate("datfine"),
                            rs.getInt("numeroIscritti"));
                }

            }
            return hackathon;
        }
    }

    public void inserisciNuovoHackathon(String titolo, String sede, Date dataInizio, Date dataFine, int partecipanti, String stato) throws SQLException{

        int numeroMaxTeam = partecipanti / 2;
        String query = "INSERT INTO Hackathon VALUES (?, ?, ?, ?, ?, ?, ?)";

        try(PreparedStatement inserisci = con.prepareStatement(query)){

            inserisci.setString(1, titolo);
            inserisci.setString(2, sede);
            inserisci.setDate(3, new java.sql.Date(dataInizio.getTime()));
            inserisci.setDate(4, new java.sql.Date(dataFine.getTime()));
            inserisci.setInt(5, partecipanti);
            inserisci.setString(6, stato);
            inserisci.setInt(7, numeroMaxTeam);
            inserisci.executeUpdate();

        }
    }

    public void terminaCompetizione(String hackathon) throws SQLException{
        String query = "UPDATE Hackathon SET stato = ? WHERE titolo = ?";

        try(PreparedStatement termina = con.prepareStatement(query);){
            termina.setString(1, "terminate");
            termina.setString(2, hackathon);
            termina.executeUpdate();
        }
    }

    public void chiudiIscrizioniHackathon(String hackathon) throws SQLException{
        String query = "UPDATE Hackathon SET stato = ? WHERE titolo = ?";

        try(PreparedStatement chiudi = con.prepareStatement(query);){
            chiudi.setString(1, "chiuse");
            chiudi.setString(2, hackathon);
            chiudi.executeUpdate();
        }
    }

    public String getStatoHackathon(String hackathon) throws SQLException{
        String stato = null;
        String query = "SELECT stato FROM Hackathon WHERE titolo = ?";

        try(PreparedStatement ps = con.prepareStatement(query);){
            ps.setString(1, hackathon);

            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    stato = rs.getString("stato");
                }
            }
        }
        return stato;
    }

    public int teamIscrittiHackathon(String hackathon) throws SQLException{
        int iscrittiAttuali = 0;
        String query = "SELECT COUNT (*) FROM team WHERE hackathon = ?";

        try(PreparedStatement numIscritti = con.prepareStatement(query)){
            numIscritti.setString(1, hackathon);
            try(ResultSet rs = numIscritti.executeQuery()){
                if(rs.next()){
                    iscrittiAttuali = rs.getInt(1);
                }
            }
        }
        return iscrittiAttuali;
    }

    public int getMaxIscritti(String hackathon) throws SQLException{
        int maxIscritti = 0;
        String query = "SELECT numeromaxteam FROM Hackathon WHERE titolo = ?";

        try(PreparedStatement numMax = con.prepareStatement(query)){
            numMax.setString(1, hackathon);

            try(ResultSet rs = numMax.executeQuery()){
                if(rs.next()){
                    maxIscritti = rs.getInt(1);
                }
            }
        }

        return maxIscritti;
    }
}
