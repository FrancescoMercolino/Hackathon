package DAO;

import java.sql.SQLException;

public interface UtenteDAO {

    public boolean eseguiLoginDB(String nome, char[] password) throws SQLException;
    public boolean registraUtente(String nome, char[] password) throws SQLException;
}
