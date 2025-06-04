package implementazioneDAO;

import DAO.LoginDAO;
import Database.ConnessioneDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginImplementazioneDAO implements LoginDAO {

    private Connection con;

    public LoginImplementazioneDAO() {
        try {
            con = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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
}
