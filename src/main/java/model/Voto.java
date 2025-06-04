package Model;

public class Voto {
    //attributi
    private int valore;
    private Giudice giudice;
    private Team team;


    public Voto(Giudice giudice, Team team, int valore) {
        if(valore < 0 )
            throw new IllegalArgumentException("Valore negativo");
        this.valore = valore;
        this.giudice = giudice;
        this.team = team;
    }

    //getter
    public int getValore() {
        return valore;
    }
    public Giudice getGiudice() { return giudice;}
    public Team getTeam() { return team;}


}
