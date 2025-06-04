package Model;

public class Giudice extends Utente{

    //costruttore
    public Giudice(String nome, String password) {
        super(nome, password);
    }

    //metodi da implementare

    public void pubblicaProblema() {}

    public void esaminaDocumento() {}

    public Voto assegnaVoto(Team team, int valore ) {
        return new Voto(this, team, valore);
    }
}
