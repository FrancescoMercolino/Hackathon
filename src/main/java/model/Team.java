package model;

import java.util.ArrayList;

public class Team {

    //attributi
    private String nome;
    private int voto;
    private Hackathon hackathon;
    private ArrayList<Utente> membri;

    //costruttore
    public Team(String nome) {
        this.nome = nome;
        this.membri = new ArrayList<>();
        this.hackathon = null;
        this.voto = 0;
    }

    //costruttore recupero team
    public Team(String nome, int voto, Hackathon hackathon) {
        this.nome = nome;
        this.voto = voto;
        this.hackathon = hackathon;
    }

    //getter
    public ArrayList<Utente> getMembri() {
        return membri;
    }

    public String getNome() {
        return nome;
    }

    public int getVoto() {
        return voto;
    }

    public Hackathon getHackathon() {
        return hackathon;
    }

    //setter
    public void addUtente(Utente utente) {
        membri.add(utente);
    }

    public void iscriviHackathon(Hackathon hackathon) {
        this.hackathon = hackathon;
    }

    public void setVoto(int voto) {
        this.voto = voto;
    }

    public void setMembri(ArrayList<Utente> membri) {
        this.membri = membri;
    }

}
