package implementazioneDAO;

import Database.ConnessioneDatabase;
import org.postgresql.util.PSQLException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtenteImplementazioneDAO implements DAO.UtenteDAO {

    private Connection con;

    public UtenteImplementazioneDAO() {
        try {
            con = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean registraUtente(String nome, char[] password) throws SQLException {
        //controllo che nome non sia già inserito
        try {
            PreparedStatement controllo = con.prepareStatement("SELECT COUNT(*) FROM UTENTE WHERE LOWER(nome) = LOWER(?)");
            controllo.setString(1, nome);
            ResultSet rs = controllo.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                if (count > 0) {
                    return false;
                }
            }

            //Non esiste già in database, procedi ad inserire
            PreparedStatement ps = con.prepareStatement("INSERT INTO UTENTE (nome, pwd) VALUES ( ?, ?)");
            ps.setString(1, nome);
            ps.setString(2, new String(password));

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
     public boolean eseguiLoginDB(String nome, char[] password) throws SQLException {

        boolean login = false;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT 1 FROM UTENTE WHERE nome = ? AND pwd = ? ");

            ps.setString(1, nome);
            ps.setString(2, new String(password));

            ResultSet rs = ps.executeQuery();

            login = rs.next();

        } catch(SQLException e){
            e.printStackTrace();
            return false;
         }

        return login;
     }

     public boolean faParteTeam(String nome) throws SQLException {
        boolean team = false;

        try {
            PreparedStatement ps = con.prepareStatement("SELECT 1 FROM team_utente WHERE utente_nome = ?");
            ps.setString(1, nome);

            ResultSet rs = ps.executeQuery();

            team = rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return team;
     }

     public String tipoUtente(String nome) throws SQLException {
        try {
            PreparedStatement ps = con.prepareStatement("SELECT privilegio FROM privilegi_utente WHERE utente = ?");
            ps.setString(1, nome);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("privilegio");
            }


        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
     }

    public boolean inserisciGiudice(String nome) throws SQLException{
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO utente_giudice (nome_giudice) VALUES (?)");
            ps.setString(1, nome);
            ps.executeUpdate();


        }catch(SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

}
