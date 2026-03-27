package DAO;

import model.Problema;

import java.sql.SQLException;

public interface ProblemaDAO {
    void pubblicaProblema(String giudice, String problema, String Hackathon) throws SQLException;
    String leggiProblema(String hackathon) throws SQLException;
}
