package Model;

import implementazioneDAO.UtenteImplementazioneDAO;

import java.sql.SQLException;
import java.util.ArrayList;

public class Utente {
    //Attributi
    private String nome;
    private char[] password;

    //costruttore
    public Utente(String nome, char[] password) {
        this.nome = nome;
        this.password = password;

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