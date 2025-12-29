package implementazioneDAO;

import DAO.HackathonDAO;
import Database.ConnessioneDatabase;
import Model.Hackathon;

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

        try{
            PreparedStatement ps = con.prepareStatement("select titolo from Hackathon");
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                hackathon.add(rs.getString("titolo"));
            }

        } catch(SQLException e) {
            e.printStackTrace();
            throw e;
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
            PreparedStatement ps = con.prepareStatement("SELECT * FROM team WHERE hackathon = ? ");
            ps.setString(1, hackathon);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                iscritti.add(rs.getString("nome_team"));
            }

        }catch (SQLException e){
            e.printStackTrace();
            throw e;
        }

        return iscritti;
    }

    public void apriIscrizioniHackathon(String hackathon) throws SQLException{
        try{
            PreparedStatement apri = con.prepareStatement("UPDATE Hackathon SET stato = 'aperto' WHERE titolo = ?");
            apri.setString(1, hackathon);
            apri.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
            throw e;
        }
    }

    public Hackathon getHackathonFromDB(String nomeHackathon) throws SQLException{

        try{
            PreparedStatement recupera = con.prepareStatement("SELECT * FROM Hackathon WHERE titolo = ?");
            recupera.setString(1, nomeHackathon);

            ResultSet rs = recupera.executeQuery();

            Hackathon hackathon = null;
            if(rs.next()){
                hackathon = new Hackathon(rs.getString("titolo"),
                        rs.getString("sede"),
                        rs.getDate("datainizio"),
                        rs.getDate("datfine"),
                        rs.getInt("numeroIscritti"));
            }
            return hackathon;
        }catch (SQLException e){
            e.printStackTrace();
            throw e;
        }

    }

    public void inserisciNuovoHackathon(String titolo, String sede, Date dataInizio, Date dataFine, int partecipanti, String stato) throws SQLException{

        try{
            PreparedStatement inserisci = con.prepareStatement("INSERT INTO Hackathon VALUES (?, ?, ?, ?, ?, ?)");
            inserisci.setString(1, titolo);
            inserisci.setString(2, sede);
            inserisci.setDate(3, new java.sql.Date(dataInizio.getTime()));
            inserisci.setDate(4, new java.sql.Date(dataFine.getTime()));
            inserisci.setInt(5, partecipanti);
            inserisci.setString(6, stato);
            inserisci.executeUpdate();



        }catch (SQLException e){
            e.printStackTrace();
            throw e;
        }
    }

    public void terminaCompetizione(String hackathon) throws SQLException{
        try{
            PreparedStatement termina = con.prepareStatement("UPDATE Hackathon SET stato = ? WHERE titolo = ?");
            termina.setString(1, "terminate");
            termina.setString(2, hackathon);
            termina.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
            throw e;
        }
    }

    public void chiudiIscrizioniHackathon(String hackathon) throws SQLException{
        try{
            PreparedStatement chiudi = con.prepareStatement("UPDATE Hackathon SET stato = ? WHERE titolo = ?");
            chiudi.setString(1, "chiuse");
            chiudi.setString(2, hackathon);
            chiudi.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
            throw e;
        }
    }

    public String getStatoHackathon(String hackathon) throws SQLException{
        String stato = null;

        try{
            PreparedStatement ps = con.prepareStatement("SELECT stato FROM Hackathon WHERE titolo = ?");
            ps.setString(1, hackathon);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                stato = rs.getString("stato");
            }

        }catch(SQLException e){
            e.printStackTrace();
            throw e;
        }
        return stato;
    }

    public int teamIscrittiHackathon(String hackathon) throws SQLException{
        int iscrittiAttuali = 0;

        try{
            PreparedStatement numIscritti = con.prepareStatement("SELECT COUNT (*) FROM team WHERE hackathon = ?");
            numIscritti.setString(1, hackathon);
            ResultSet rs = numIscritti.executeQuery();
            if(rs.next()){
                iscrittiAttuali = rs.getInt(1);
            }
        }catch(SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return iscrittiAttuali;
    }

    public int getMaxIscritti(String hackathon) throws SQLException{
        int maxIscritti = 0;

        try{
            PreparedStatement numMax = con.prepareStatement("SELECT numeromaxteam FROM Hackathon WHERE titolo = ?");
            numMax.setString(1, hackathon);
            ResultSet rs = numMax.executeQuery();
            if(rs.next()){
                maxIscritti = rs.getInt(1);
            }
        }catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return maxIscritti;
    }
}
