package Model;

import java.util.ArrayList;

public class Team {

    //attributi
    private String nome;
    private String membri;
    private int punti;
    private String hackathon;
    private Piattaforma piattaforma;

    //costruttore
    public Team(String nome, String membri, String hackathon, int punti) {
        this.nome = nome;
        this.membri = membri;
        this.punti = punti;
        this.hackathon = hackathon;
    }

    //metodi da implementare
    public void aggiornaProgressi() {};

    public String getHackathon() {
        return hackathon;
    }

    //metodi
    /* public void aggiungiMembro(Utente utente) {
        membri.add(utente);
    }; */

    /* public void stampaMembri() {
        System.out.println("NOME TEAM " + nome);
        for (Utente utente : membri) {
            System.out.println("Membro: " + utente.getNome());
        };
    } */

    public int getPunti() {
       return punti;
    }

    //getter
    public String getNome() {
        return nome;
    }

    public String getMembri() {
        return membri;
    }

    //setter
    public void setNome(String Nome) {
        this.nome = Nome;
    }

    /* public Hackathon getHackathon() {
        return hackathon;
    }
    */
}
