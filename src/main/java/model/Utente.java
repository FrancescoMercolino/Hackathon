package Model;

import DAO.LoginDAO;
import implementazioneDAO.LoginImplementazioneDAO;

import java.util.ArrayList;

public class Utente {
    //Attributi
    private String nome;
    private String password;
    private ArrayList<Utente> utenti;

    //costruttore
    public Utente(String nome, String password) {
        this.nome = nome;
        this.password = password;

        utenti = new ArrayList<Utente>();
    }

    //metodi da implementare
    public void registrazione(String nome, String password) {
        Utente u = new Utente(nome, password);
        utenti.add(u);
    };

    public void eseguiLogin(String nome, String password) {
        LoginDAO utenteLogin = new LoginImplementazioneDAO();
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
