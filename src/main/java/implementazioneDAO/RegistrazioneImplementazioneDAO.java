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

    public void registraUtente(String nome, char[] password) throws SQLException {
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO UTENTE (nome, pwd) VALUES ( ?, ?)");
            ps.setString(1, nome);
            ps.setString(2, new String(password));

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
