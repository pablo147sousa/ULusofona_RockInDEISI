package pt.ulusofona.aed.rockindeisi2023;

public class FileInputResult {
    public String nomeFicheiro;
    public int linhasOK;
    public int linhasNOK;

    public int primeiraLinhaNOK = -1;

    public FileInputResult(String nomeFicheiro, int linhasOK, int linhasNOK, int primeiraLinhaNOK) {
        this.nomeFicheiro = nomeFicheiro;
        this.linhasOK = linhasOK;
        this.linhasNOK = linhasNOK;
        this.primeiraLinhaNOK = primeiraLinhaNOK;
    }

    public String toString(){
        return nomeFicheiro + " | " + linhasOK + " | " + linhasNOK + " | " + primeiraLinhaNOK;
    }


}
