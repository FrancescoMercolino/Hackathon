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
        String query = "SELECT COUNT(*) FROM partecipante WHERE LOWER(nome) = LOWER(?)";
        String query2 = "INSERT INTO partecipante (nome, pwd) VALUES ( ?, ?)";

        //controllo che nome non sia già inserito
        try (PreparedStatement controllo = con.prepareStatement(query);){
            controllo.setString(1, nome);
            try(ResultSet rs = controllo.executeQuery();){

                if (rs.next()) {
                    int count = rs.getInt(1);
                    if (count > 0) {
                        return false;
                    }
                }
            }

            //Non esiste già in database, procedi ad inserire
            try (PreparedStatement ps = con.prepareStatement(query2);) {
                ps.setString(1, nome);
                ps.setString(2, new String(password));

                ps.executeUpdate();
            }

        }
        return true;
    }

    @Override
     public boolean eseguiLoginDB(String nome, char[] password) throws SQLException {
        boolean login;
        String query = "SELECT 1 FROM login WHERE nome = ? AND pwd = ? ";
        try(PreparedStatement ps = con.prepareStatement(query)){
            ps.setString(1, nome);
            ps.setString(2, new String(password));

            try(ResultSet rs = ps.executeQuery()){
                login = rs.next();
            }
        }
        return login;
     }

     public boolean faParteTeam(String nome) throws SQLException {
        boolean team = false;
        String query = "SELECT nome_team FROM partecipante WHERE nome = ? AND nome_team IS NOT NULL";

        try (PreparedStatement ps = con.prepareStatement(query);){
            ps.setString(1, nome);
            try(ResultSet rs = ps.executeQuery()){
                if (rs.next()) {
                    team = true;
                }

            }
        }
        return team;
     }

     public String tipoUtente(String nome) throws SQLException {

        String[] tabella = {"organizzatore", "giudice", "partecipante"};


        for(String tabella1 : tabella) {
            String query = "Select 1 FROM " + tabella1 + " WHERE LOWER(nome) = LOWER(?)";
            try (PreparedStatement privilegio = con.prepareStatement(query);){
                privilegio.setString(1, nome);
                try(ResultSet rs = privilegio.executeQuery()){
                    if(rs.next()) {
                        return tabella1;
                    }
                }
            }
        }

        return null;
     }

    public boolean inserisciGiudice(String nome, String organizzatore) throws SQLException{
        String query = "SELECT pwd FROM partecipante WHERE LOWER(nome) = LOWER(?)";
        String query2 = "INSERT INTO giudice VALUES (?, ?, ?)";

        try (PreparedStatement getinfo = con.prepareStatement(query);){
            getinfo.setString(1, nome);
            String password;

            try(ResultSet rs = getinfo.executeQuery()){
                if(rs.next()) {
                    password = rs.getString(1);
                } else {
                    return false;
                }
            }

            try(PreparedStatement ps = con.prepareStatement(query2)) {
                ps.setString(1, nome);
                ps.setString(2, password);
                ps.setString(3, organizzatore);
                ps.executeUpdate();
            }
        }
        return true;
    }

    public void votaSoluzione(String giudice, String nomeTeam, int voto) throws SQLException {
        String query = "INSERT INTO voto VALUES (?, ?, ?)";

        try(PreparedStatement vota = con.prepareStatement(query)){
            vota.setString(1, giudice);
            vota.setString(2, nomeTeam);
            vota.setInt(3, voto);
            vota.executeUpdate();

        }
    }

    public boolean utenteEsiste(String nome) throws SQLException{
        String query = "SELECT 1 FROM partecipante WHERE nome = ? ";

        try(PreparedStatement controllo = con.prepareStatement(query)){
            controllo.setString(1, nome);
            try(ResultSet rs = controllo.executeQuery()){
                if(rs.next()) {
                    return true;
                }
            }
        }
        return false;
    }

}
