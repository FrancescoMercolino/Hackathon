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
            PreparedStatement controllo = con.prepareStatement("SELECT COUNT(*) FROM partecipante WHERE LOWER(nome) = LOWER(?)");
            controllo.setString(1, nome);
            ResultSet rs = controllo.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                if (count > 0) {
                    return false;
                }
            }

            //Non esiste già in database, procedi ad inserire
            PreparedStatement ps = con.prepareStatement("INSERT INTO partecipante (nome, pwd) VALUES ( ?, ?)");
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

        boolean login;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT 1 FROM login WHERE nome = ? AND pwd = ? ");

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
            PreparedStatement ps = con.prepareStatement("SELECT nome_team FROM partecipante WHERE nome = ? AND nome_team IS NOT NULL");
            ps.setString(1, nome);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                team = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return team;
     }

     public String tipoUtente(String nome) throws SQLException {

        String[] tabella = {"organizzatore", "giudice", "partecipante"};

        for(String tabella1 : tabella) {
            try {
                PreparedStatement privilegio = con.prepareStatement("Select 1 FROM " + tabella1 + " WHERE LOWER(nome) = LOWER(?)");
                privilegio.setString(1, nome);
                ResultSet rs = privilegio.executeQuery();
                if(rs.next()) {
                    return tabella1;
                }
            }catch(SQLException e){
                e.printStackTrace();
                throw e;
            }
        }

        return null;
     }

    public boolean inserisciGiudice(String nome, String organizzatore) throws SQLException{

        try {
            PreparedStatement getinfo = con.prepareStatement("SELECT pwd FROM partecipante WHERE LOWER(nome) = LOWER(?)");
            getinfo.setString(1, nome);
            ResultSet rs = getinfo.executeQuery();
            String password;

            if(rs.next()) {
                password = rs.getString(1);
            } else {

                return false;
            }

            PreparedStatement ps = con.prepareStatement("INSERT INTO giudice VALUES (?, ?, ?)");
            ps.setString(1, nome);
            ps.setString(2, password);
            ps.setString(3, organizzatore);
            ps.executeUpdate();


        }catch(SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return true;
    }

    public void votaSoluzione(String giudice, String nomeTeam, int voto) throws SQLException {

        try{
            PreparedStatement vota = con.prepareStatement("INSERT INTO voto VALUES (?, ?, ?)");
            vota.setString(1, giudice);
            vota.setString(2, nomeTeam);
            vota.setInt(3, voto);
            vota.executeUpdate();

        }catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public boolean utenteEsiste(String nome) throws SQLException{

        try{
            PreparedStatement controllo = con.prepareStatement("SELECT 1 FROM partecipante WHERE nome = ? ");
            controllo.setString(1, nome);
            ResultSet rs = controllo.executeQuery();
            if(rs.next()) {
                return true;
            }
        }catch(SQLException ex) {
            ex.printStackTrace();
            throw ex;
        }
        return false;
    }

}
