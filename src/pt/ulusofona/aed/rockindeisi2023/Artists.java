package pt.ulusofona.aed.rockindeisi2023;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Artists {
    String idTemaMusical,nomeArtista;
    ArrayList<String> idTemas = new ArrayList<>();

    public int numMusicas = 1;

    ArrayList<String> tags = new ArrayList<>();
    ArrayList<String> musicas = new ArrayList<>();

    public Artists(String idTemaMusical, String nomeArtista) {
        this.idTemaMusical = idTemaMusical;
        this.nomeArtista = nomeArtista;
    }
    public Artists(String nomeArtista, int numMusicas) {
        this.nomeArtista = nomeArtista;
        this.numMusicas = numMusicas;
    }

    public String toString() {
        if(nomeArtista.charAt(0) == 'A' || nomeArtista.charAt(0) == 'B' ||nomeArtista.charAt(0) == 'C' ||nomeArtista.charAt(0) == 'D') {
            return "Artista: [" + nomeArtista + "]";
        }else{
            return "Artista: [" + nomeArtista + "] | " + numMusicas;
        }
    }
}