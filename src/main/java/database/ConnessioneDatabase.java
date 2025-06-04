package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnessioneDatabase {

    //Attributi
    private static ConnessioneDatabase instance;
    public Connection connection = null;
    private String nome = "Hackathon";
    private String password = "pessi";
    private String url = "jdbc:postgresql://localhost:5432/Hackathon";
    private String driver = "org.postgresql.Driver";

    //Costruttore
    private ConnessioneDatabase() throws SQLException {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, nome, password);
        } catch (ClassNotFoundException e) {
            System.out.println("Database Connection Creation Failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static ConnessioneDatabase getInstance() throws SQLException {
        if (instance == null) {
            instance = new ConnessioneDatabase();
        } else if (instance.connection.isClosed()) {
            instance = new ConnessioneDatabase();
        }
        return instance;
    }
}
