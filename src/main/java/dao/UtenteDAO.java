package DAO;

import java.sql.SQLException;

public interface UtenteDAO {

    boolean eseguiLoginDB(String nome, char[] password) throws SQLException;
    boolean registraUtente(String nome, char[] password) throws SQLException;
    boolean faParteTeam(String nome) throws SQLException;
    String tipoUtente(String nome) throws SQLException;
    boolean inserisciGiudice(String nome) throws SQLException;
}
