package Model;

public class Documento {
    //attributi
    private String nome;
    private String dettagli;

    //costruttore
    public Documento(String nome, String dettagli) {
        this.nome = nome;
        this.dettagli = dettagli;
    }

    // getter
    public String getNome() {
        return nome;
    }
    public String getDettagli() {
        return dettagli;
    }

    //setter
    public void setNome(String Nome) {}
    public void setDettagli(String Dettagli) {}
}
