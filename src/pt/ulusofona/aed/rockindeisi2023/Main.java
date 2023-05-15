package pt.ulusofona.aed.rockindeisi2023;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    static ArrayList<Songs> songArray = new ArrayList<>();
    static ArrayList<Songs> songArrayProcessado = new ArrayList<>();
    static ArrayList<Details> detailsArray = new ArrayList<>();
    static ArrayList<Artists> artistsArray = new ArrayList<>();

    static ArrayList<FileInputResult> fileInputResults = new ArrayList<>();

    public enum LineResult {
        OK, ERRO;
    }

    public static boolean validarArtista(String artistaTexto) {
        boolean ocorreVirgula = false;
        for (char c : artistaTexto.toCharArray()) {
            if (c == ',') {
                ocorreVirgula = true;
            }
        }
        if (ocorreVirgula) {
            short count = 0;
            for (char c : artistaTexto.toCharArray()) {
                if (c == '"') {
                    count++;
                }
            }
            return count == 2 && artistaTexto.charAt(0) == '"';

        }
        return true;
    }

    public static void clearAll() {
        songArray.clear();
        songArrayProcessado.clear();
        detailsArray.clear();
        artistsArray.clear();
        fileInputResults.clear();
    }

    public static boolean idMusicaExiste(String id) {
        //Check if exists
        for (int j = 0; j < songArray.size(); j++) {
            if (songArray.get(j).idTemaMusical.equals(id)) {
                System.out.println("IGUAL");
                System.out.println(songArray.get(j).idTemaMusical);
                System.out.println(id);
                return true;
            }
        }
        return false;
    }

    public static LineResult adicionarMusica(String musica) {
        String[] partes = musica.split(" @ ");
        int count = 0;
        for (int i = 0; i < musica.length(); i++) {
            if (musica.charAt(i) == '@') {
                count++;
            }
        }
        if (count != 2) {
            return LineResult.ERRO;
        }
        if (partes.length == 3) {
            String id = partes[0].trim();
            String nome = partes[1].trim();
            int ano = Integer.parseInt(partes[2].replace("@", "").trim());

            if (!idMusicaExiste(partes[0].trim())) {
                songArray.add(new Songs(id, nome, ano));
            } else {
                System.out.println("ERRO ID DUPLICADO");
                return LineResult.ERRO;
            }
        }
        return LineResult.OK;
    }

    public static LineResult adicionarDetalhes(String detalhes) {
        String[] partes = detalhes.split(" @ ");
        if (partes.length == 7) {
            detailsArray.add(new Details(partes[0].trim(), Integer.parseInt(partes[1].trim()), Integer.parseInt(partes[2].trim()), Integer.parseInt(partes[3].trim()), Double.parseDouble(partes[4].trim()), Double.parseDouble(partes[5].trim()), Double.parseDouble(partes[6].trim())));
        } else {
            System.out.println("ERRO: DIFRENTE DE 7");
            return LineResult.ERRO;
        }
        return LineResult.OK;
    }

    public static boolean adicionarArtista(String artistasStr) {
        int count = 0;
        for (int i = 0; i < artistasStr.length(); i++) {
            if (artistasStr.charAt(i) == '@') {
                count++;
            }
        }
        if (count != 1) {
            //System.out.println("ERRO: DIFRENTE DE 2");
            return false;
        }

        String[] partes = artistasStr.split(" @ ");
        boolean musicaExiste = false;
        if (partes.length == 2 && validarArtista(partes[1])) {
            for (String artista : getArtists(partes[1])) {
                for (Songs songs : songArray) {
                    if (songs.idTemaMusical.equals(partes[0])) {
                        songs.numArtistas += 1;
                        musicaExiste = true;
                    }
                }
                if (musicaExiste) {
                    boolean entered = false;
                    for (Artists artist : artistsArray) {
                        if (artist.nomeArtista.equals(artista)) {
                            artist.numMusicas++;
                            entered = true;
                            break;
                        }
                    }
                    if (!entered) {
                        artistsArray.add(new Artists(partes[0], artista));
                    }
                }
            }
            return true;
        } else {
            System.out.println("ERRO: DIFRENTE DE 2");
            return false;
        }
    }

    //RECEBE A STRING DO ARTISTA E REMOVE OS CARACTERES INDESEJAVEIS RETORNANDO UMA STRING PROCESSADA.
    public static ArrayList<String> getArtists(String artists) {
        ArrayList<String> artistas = new ArrayList<>();
        artists = artists.replace("[", "");
        artists = artists.replace("]", "");
        artists = artists.replace("'", "");
        artists = artists.replace("\"", "");
        String[] partes = artists.split(",");
        for (int i = 0; i < partes.length; i++) {
            artistas.add(partes[i].trim());
        }
        return artistas;
    }

    public static boolean loadFiles(File folder) {
        clearAll();

        int numSemErro = 0;
        int numNaoOk = 0;
        int primeiraLinhaNOK = -1;
        int numLinha;
        //SONGS
        try {
            Scanner scanner = new Scanner(new File(folder, "songs.txt"));
            numLinha = 0;
            while (scanner.hasNext()) {
                numLinha++;
                if (adicionarMusica(scanner.nextLine()) == LineResult.ERRO) {
                    numNaoOk++;
                    if (primeiraLinhaNOK == -1) {
                        primeiraLinhaNOK = numLinha;
                    }
                } else {
                    numSemErro++;
                }
            }
            fileInputResults.add(new FileInputResult("songs.txt", numSemErro, numNaoOk, primeiraLinhaNOK));
        } catch (Exception e) {
            System.out.println("ERRO");
            System.out.println(e.getMessage());
            return false;
        }
        //DETALHES
        numSemErro = 0;
        numNaoOk = 0;
        primeiraLinhaNOK = -1;

        try {
            Scanner scanner = new Scanner(new File(folder, "song_details.txt"));
            numLinha = 0;
            while (scanner.hasNext()) {
                numLinha++;
                if (adicionarDetalhes(scanner.nextLine()) == LineResult.ERRO) {
                    numNaoOk++;
                    if (primeiraLinhaNOK == -1) {
                        primeiraLinhaNOK = numLinha;
                    }
                } else {
                    numSemErro++;
                }
            }
            fileInputResults.add(new FileInputResult("song_details.txt", numSemErro, numNaoOk, primeiraLinhaNOK));
        } catch (Exception e) {
            System.out.println("ERRO");
            System.out.println(e.getMessage());
            return false;
        }
        // ARTISTS
        numSemErro = 0;
        numNaoOk = 0;
        primeiraLinhaNOK = -1;

        try {
            Scanner scanner = new Scanner(new File(folder, "song_artists.txt"));
            numLinha = 0;
            while (scanner.hasNext()) {
                numLinha++;
                if (adicionarArtista(scanner.nextLine())) {
                    numSemErro++;
                } else {
                    numNaoOk++;
                    if (primeiraLinhaNOK == -1) {
                        primeiraLinhaNOK = numLinha;
                    }
                }
            }
            fileInputResults.add(new FileInputResult("song_artists.txt", numSemErro, numNaoOk, primeiraLinhaNOK));
        } catch (Exception e) {
            System.out.println("ERRO");
            System.out.println(e.getMessage());
            return false;
        }
        //verMusicasSemDetalhes();
        //adicionaArtistas();
        return true;
    }

    // PROVAVELMENTE SERÁ ALTERADO NA PARTE 2
    public static void verMusicasSemDetalhes() {
        // musicas nao tem detalhes
        songArrayProcessado.clear();
        for (Songs songs : songArray) {
            for (Details details : detailsArray) {
                if (songs.idTemaMusical.equals(details.idTemaMusical)) {
                    songs.detalhes = new Details(details.duracao, details.popularidade);
                    if (songs.numArtistas > 0) {
                        for (Songs songArrayProcessado : songArrayProcessado) {
                            if (songs.idTemaMusical.equals(songArrayProcessado.idTemaMusical)) {
                                break;
                            }
                        }
                        songArrayProcessado.add(songs);
                    }
                }
            }
        }
    }

    public static boolean artistaExiste(String artista) {
        //Check if exists
        for (int j = 0; j < artistsArray.size(); j++) {
            if (artistsArray.get(j).nomeArtista.equals(artista)) {
                return true;
            }
        }
        return false;
    }


    public static ArrayList getObjects(TipoEntidade tipo) {
        verMusicasSemDetalhes();
        if (tipo == TipoEntidade.TEMA) {
            return songArrayProcessado;
        } else if (tipo == TipoEntidade.ARTISTA) {
            return artistsArray;
        } else if (tipo == TipoEntidade.INPUT_INVALIDO) {
            return fileInputResults;
        }

        return new ArrayList();
    }

    public static void main(String[] args) {
        //System.out.println(loadFiles(new File("test-files")));
        if (loadFiles(new File("src/pt/ulusofona/aed/rockindeisi2023/files"))) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Escolha o tipo de entidade (TEMA ou ARTISTA):");
            String tipo = scanner.nextLine().toUpperCase();

            ArrayList lista = getObjects(TipoEntidade.valueOf(tipo));

            for (Object obj : lista) {
                System.out.println(obj);
            }
        }
    }
}