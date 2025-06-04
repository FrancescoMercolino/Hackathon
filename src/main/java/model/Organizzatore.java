package Model;

import java.util.Date;

public class Organizzatore extends Utente{

    //costruttore
    public Organizzatore(String nome, String password) {
        super(nome, password);
    }

    //metodi da implementare
    public void selezionaGiudice(Utente utente) {
    };
    public void aperturaRegistrazioni(Date dataInizio, Date dataFine) {}
}
