package implementazioneDAO;

import DAO.LoginDAO;
import Database.ConnessioneDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginImplementazioneDAO implements LoginDAO {

    private Connection connection;

    public LoginImplementazioneDAO() {
        try {
            connection = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

     public boolean eseguiLoginDB(String nome, String password) {
        return true;
     }
        /* String sql = "SELECT 1 FROM Utente WHERE nome = ? AND pwd = ? ";
        try {
            PreparedStatement login = connection.prepareStatement(sql);
            login.setString(1, nome);
            login.setString(2, password);

            ResultSet rs = login.executeQuery();
            rs.next();
            rs.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    } */
}
