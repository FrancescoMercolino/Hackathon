package DAO;

import java.sql.SQLException;

public interface RegistrazioneDAO {

    void registraUtente(String nome, char[] password) throws SQLException;
}
