package Model;

import java.util.ArrayList;

public class Piattaforma {
    //attributi
    private String nomePiattaforma;
    private ArrayList<Utente> listaUtenti;

    //costruttore
    public Piattaforma(String nomePiattaforma) {
        this.nomePiattaforma = nomePiattaforma;

    }

    //metodi da implementare
    public void acquisisciVoti(Giudice giudice, Team team, Voto voto) {
    };

    public void pubblicaClassifica() {};

    public boolean registraUtente(String nome, String password, String ruolo) {

        Utente utente = switch(ruolo.toLowerCase()) {
            case "partecipante" -> new Partecipante(nome, password);
            case "giudice"  -> new Giudice(nome, password);
            case "organizzatore" -> new Organizzatore(nome, password);
            default -> throw new IllegalArgumentException("Ruolo non valido");
        };

        listaUtenti.add(utente);
        return true;
    }

    //getter
    public String getNomePiattaforma() {
        return nomePiattaforma;
    }

    //setter
    public void setNomePiattaforma(String nomePiattaforma) {
        this.nomePiattaforma = nomePiattaforma;
    }
}
