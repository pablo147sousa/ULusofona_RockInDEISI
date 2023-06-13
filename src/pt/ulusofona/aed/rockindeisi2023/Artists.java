package pt.ulusofona.aed.rockindeisi2023;

import java.util.ArrayList;

public class Artists {
    String nomeArtista;
    public int numMusicas = 1;

    ArrayList<String> tags = new ArrayList<>();

    public Artists(String nomeArtista) {
        this.nomeArtista = nomeArtista;
    }

    public String toString() {
        if(nomeArtista.charAt(0) == 'A' || nomeArtista.charAt(0) == 'B' || nomeArtista.charAt(0) == 'C' || nomeArtista.charAt(0) == 'D') {
            return "Artista: [" + nomeArtista + "]";
        }else{
            return "Artista: [" + nomeArtista + "] | " + numMusicas;
        }
    }
}