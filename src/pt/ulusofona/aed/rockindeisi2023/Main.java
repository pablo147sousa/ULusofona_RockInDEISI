package pt.ulusofona.aed.rockindeisi2023;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Main {

    static ArrayList<Songs> songArray = new ArrayList();
    static ArrayList<Details> detailsArray = new ArrayList();
    static ArrayList<Artists> artistsArray = new ArrayList();

    public static boolean idMusicaJaExiste(String id){
        //Check if exists
        for (int j = 0; j < songArray.size(); j++) {
            if (songArray.get(j).idTemaMusical.equals(id)) {
                return true;
            }
        }
        return false;
    }

    public static boolean loadFiles(File folder) {
        songArray = new ArrayList();
        detailsArray = new ArrayList();
        artistsArray = new ArrayList();
        //songs
        try{
            Scanner scanner = new Scanner(new File(folder, "songs.txt"));
            while (scanner.hasNext()) {
                String[] partes = scanner.nextLine().split(" @ ");
                var idExists = false;
                if (partes.length == 3) {
                    if (!idMusicaJaExiste(partes[0])) {
                        songArray.add(new Songs(partes[0], partes[1], Integer.parseInt(partes[2])));
                    }
                }
            }
        }catch (Exception e){
            return false;
        }

        //songsdetails
        try{
            Scanner scanner = new Scanner(new File(folder, "song_details.txt"));
            while (scanner.hasNext()) {
                String[] partes = scanner.nextLine().split(" @ ");
                var idExists = false;
                if (partes.length == 7) {
                    detailsArray.add(new Details(partes[0], Integer.parseInt(partes[1]), Integer.parseInt(partes[2]), Integer.parseInt(partes[3]), Double.parseDouble(partes[4]), Double.parseDouble(partes[5]), Double.parseDouble(partes[6])));
                }
            }
        }catch (Exception e){
            return false;
        }

        //artists
        try{
            Scanner scanner = new Scanner(new File(folder, "song_artists.txt"));
            while (scanner.hasNext()) {
                String[] partes = scanner.nextLine().split(" @ ");
                var idExists = false;
                if (partes.length == 2) {
                        if (partes[1].charAt(0) == '"') {
                            String[] teste = partes[1].split(",");
                            for (String s : teste) {
                                //System.out.println(s);
                            }
                        }
                        /*String[] partes2 = partes[1].split(",");
                        ArrayList<String> partes2ArrayList = new ArrayList<String>(Arrays.asList(partes2));*/
                        artistsArray.add(new Artists(partes[0], partes[1]));
                }
            }
        }catch (Exception e){
            return false;
        }

        return true;
    }

    public static void excluiMusicasQueNaoInteressam(){
        //musicas nao tem detalhes
        for (Songs songs : songArray) {
            for (Details details : detailsArray) {
                if (Objects.equals(songs.idTemaMusical, details.idTemaMusical)) {
                    songs.detalhes = new Details(details.duracao, details.popularidade);
                }else{

                }
            }
        }
    }

    public static void comparaID() {


        //quantas musicas cada artista tem
        for (Songs songs : songArray) {
            for (Artists artists : artistsArray) {
                if (Objects.equals(songs.idTemaMusical, artists.idTemaMusical)) {
                    songs.artista = new Artists(artists.artista, artists.numMusicas);
                }
            }
        }
    }

    public static ArrayList getObjects(TipoEntidade tipo) {

        if (tipo == TipoEntidade.TEMA) {
            return songArray;
        } else if (tipo == TipoEntidade.ARTISTA) {
            return artistsArray;
        }

        return new ArrayList();
    }

    public static void main(String[] args) {
        System.out.println(loadFiles(new File(".")));
        System.out.println(getObjects(TipoEntidade.TEMA));

       /* if (loadFiles(new File("test-files"))) {
            Scanner scanner = new Scanner(System.in);
            comparaID();
            System.out.println("Escolha o tipo de entidade (TEMA ou ARTISTA):");
            String tipo = scanner.nextLine().toUpperCase();

            ArrayList lista = getObjects(TipoEntidade.valueOf(tipo));

            for (Object obj : lista) {
                System.out.println(obj);
            }
        }*/
    }
}