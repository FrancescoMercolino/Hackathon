package Model;

import java.util.ArrayList;

public class Team {

    //attributi
    private String nome;
    private ArrayList<Utente> membri;
    private int dimensioneMassima;
    private Piattaforma piattaforma;

    //costruttore
    public Team(String nome, int dimensioneMassima) {
        this.nome = nome;
        this.dimensioneMassima = dimensioneMassima;
        this.membri = new ArrayList<>();
    }

    //metodi da implementare
    public void aggiornaProgressi() {};

    //metodi
    public void aggiungiMembro(Utente utente) {
        if(this.membri.size() + 1 > dimensioneMassima) {
            throw new IllegalArgumentException("Troppi Utenti nel team");
        } else membri.add(utente);
    };

    public void stampaMembri() {
        System.out.println("NOME TEAM " + nome);
        for (Utente utente : membri) {
            System.out.println("Membro: " + utente.getNome());
        };
    }

    public int getPunti() {
       return  0;
        // piattaforma.getPunti(this.nome);
    }

    //getter
    public String getNome() {
        return nome;
    }

    public ArrayList<Utente> getMembri() {
        return membri;
    }

    public int getDimensioneMassima() {
        return 5;
    }

    //setter
    public void setNome(String Nome) {
        this.nome = Nome;
    }

    public void setDimensioneMassima(int DimensioneMassima) {
        this.dimensioneMassima = DimensioneMassima;
    }

}
