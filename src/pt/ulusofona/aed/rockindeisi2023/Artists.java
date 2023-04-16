package pt.ulusofona.aed.rockindeisi2023;

import java.util.ArrayList;

public class Artists {
    String idTemaMusical;
    public int numMusicas;
    String artista;

    public Artists(String id, String artista) {
        this.idTemaMusical = idTemaMusical;
        this.artista = artista;
        this.numMusicas = numMusicas;
    }

    public Artists(String artista, int numMusicas) {
        this.artista = artista;
        this.numMusicas = numMusicas;
    }

    public Artists() {
    }

    public String toString() {
        if(artista.charAt(2) == 'A' || artista.charAt(2) == 'B' ||artista.charAt(2) == 'C' ||artista.charAt(2) == 'D') {
            return "Artista: " + artista;
        }else{
            return "Artista: " + artista + " | " + numMusicas;
        }
    }
}