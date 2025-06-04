package implementazioneDAO;

import DAO.RegistrazioneDAO;
import Database.ConnessioneDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegistrazioneImplementazioneDAO implements RegistrazioneDAO {

    private Connection con;

    public RegistrazioneImplementazioneDAO() {
        try {
            con = ConnessioneDatabase.getInstance().connection;
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void registraUtente(String nome, String password) throws SQLException {
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO UTENTE" + " (nome, password)" +
                    "VALUES ('"+nome+"', '"+password+"');");
            ps.executeUpdate();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
