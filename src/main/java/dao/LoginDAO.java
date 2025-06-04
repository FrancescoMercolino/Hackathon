package DAO;

import java.sql.SQLException;

public interface LoginDAO {

    boolean eseguiLoginDB(String nome, char[] password) throws SQLException;
}
