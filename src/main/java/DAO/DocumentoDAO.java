package DAO;

import model.Documento;

import java.sql.SQLException;

public interface DocumentoDAO {

    void aggiungiDocumento(String nomeTeam, String testo) throws SQLException;
    String recuperaDocumento(String nomeTeam) throws SQLException;
}
