package Model;

import java.util.Date;

public class Hackathon {
    //attributi
    private String titolo;
    private String sede;
    private Date dataInizio;
    private Date dataFine;
    private int numeroIscritti;

    //costruttore
    public Hackathon(String titolo, String sede, Date dataInizio, Date dataFine, int numeroIscritti) {
        this.titolo = titolo;
        this.sede = sede;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.numeroIscritti = numeroIscritti;
    }

    //getter
    public String getTitolo() {
        return titolo;
    }
    public String getSede() {
        return sede;
    }
    public Date getDataInizio() {
        return dataInizio;
    }
    public Date getDataFine() {
        return dataFine;
    }
    public int getNumeroIscritti() {
        return numeroIscritti;
    }

    //setter
    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }
    public void setSede(String sede) {
        this.sede = sede;
    }
    public void setDataInizio(Date dataInizio) {
        this.dataInizio = dataInizio;
    }
    public void setDataFine(Date dataFine) {
        this.dataFine = dataFine;
    }
    public void setNumeroIscritti(int numeroIscritti) {
        this.numeroIscritti = numeroIscritti;
    }


}
