package Model;

import java.util.ArrayList;

public class Team {

    //attributi
    private String nome;
    private int voto;
    private String hackathon;
    private Piattaforma piattaforma;

    //costruttore
    public Team(String nome, int voto, String hackathon) {
        this.nome = nome;
        this.hackathon = hackathon;
        this.voto = voto;
    }

    //metodi da implementare
    public void aggiornaProgressi() {};

    //metodi

    //getter
    public String getNome() {
        return nome;
    }
    public int getVoto() {return voto;}
    public String getHackathon() {
        return hackathon;
    }


    //setter
    public void setNome(String Nome) {
        this.nome = Nome;
    }

}
