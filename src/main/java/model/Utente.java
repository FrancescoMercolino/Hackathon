package Model;

import implementazioneDAO.UtenteImplementazioneDAO;

import java.sql.SQLException;
import java.util.ArrayList;

public class Utente {
    //Attributi
    private String nome;
    private char[] password;
    private ArrayList<Utente> utenti;

    //costruttore
    public Utente(String nome, char[] password) {
        this.nome = nome;
        this.password = password;

        utenti = new ArrayList<Utente>();
    }

    //metodi da implementare
    public void registrazione(String nome, char[] password) {
        Utente u = new Utente(nome, password);
        utenti.add(u);
    };

    public void eseguiLogin(String nome, char[] password) throws SQLException {
        DAO.UtenteDAO utenteLogin = new UtenteImplementazioneDAO();
        utenteLogin.eseguiLoginDB(nome, password);
    }

    //getter
    public String getNome() {
        return nome;
    }

    //setter
    public void setNome(String nome) {
        this.nome = nome;
    }
}
