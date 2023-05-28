package pt.ulusofona.aed.rockindeisi2023;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static pt.ulusofona.aed.rockindeisi2023.QuerysEnum.*;

public class Main {
    static HashSet<String> idTemasMusicais = new HashSet<>();
    static LinkedHashMap<String,Songs> songMap = new LinkedHashMap<>();
    static LinkedHashMap<String,Details> detailsMap = new LinkedHashMap<>();
    static LinkedHashMap<String,Artists> artistsMap = new LinkedHashMap<>();
    static HashSet<String> idsDuplicados = new HashSet<>();
    static ArrayList<FileInputResult> fileInputResults = new ArrayList<>();
    static ArrayList<Songs> songArrayFinal = new ArrayList<>();
    static ArrayList<Artists> artistsArrayFinal = new ArrayList<>();

    public enum LineResult {
        OK, ERRO
    }


    public static boolean validarArtista(String artistaTexto) {
        String artistaSolo = "\\['[^']*']";
        String artistasMultiplos = "\"\\['([^']', )[^']*']\"";
        return artistaTexto.matches(artistaSolo) || artistaTexto.matches(artistasMultiplos);
    }

    public static void clearAll() {
        songMap.clear();
        detailsMap.clear();
        artistsMap.clear();
        fileInputResults.clear();
    }
    // Inicialize o conjunto para rastrear IDs duplicados


    public static LineResult adicionarMusica(String musica) {
        String[] partes = musica.split(" @ ");

        if (partes.length != 3) {
            return LineResult.ERRO;
        }

        String id = partes[0].trim();
        String nome = partes[1].trim();

        if (idsDuplicados.contains(id) || !idTemasMusicais.contains(id)) {
            return LineResult.ERRO;
        } else {
            int ano = Integer.parseInt(partes[2].trim().replace("@", ""));
            Songs songAdd = new Songs(id, nome, ano);
            songMap.put(id, songAdd);
            songAdd.detalhes = new Details(detailsMap.get(id).duracao, detailsMap.get(id).popularidade);
            // Adicione o ID ao conjunto de IDs duplicados
            idsDuplicados.add(id);
        }
        return LineResult.OK;
    }

    public static LineResult adicionarDetalhes(String detalhes) {
        String[] partes = detalhes.split(" @ ");
        if (partes.length == 7) {
            idTemasMusicais.add(partes[0].trim());
            detailsMap.put(partes[0].trim(),new Details(partes[0].trim(), Integer.parseInt(partes[1].trim()), Integer.parseInt(partes[2].trim()), Integer.parseInt(partes[3].trim()), Double.parseDouble(partes[4].trim()), Double.parseDouble(partes[5].trim()), Double.parseDouble(partes[6].trim())));
        } else {
            return LineResult.ERRO;
        }
        return LineResult.OK;
    }
    /*
    * public static void verMusicasSemDetalhes() {
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
    * */
    public static boolean adicionarArtista(String artistasStr) {
        String[] partes = artistasStr.split(" @ ");
        if (partes.length != 2){
            return false;
        }

        if (validarArtista(partes[1])) {
            String musicaID = partes[0];
            String artista = processArtistString(partes[1]);

            // Verificar se a música existe antes de percorrer os artistas
            if (songMap.containsKey(musicaID)) {
                // Verificar se o artista já foi adicionado antes
                if (!artistsMap.containsKey(artista)){
                    artistsMap.put(artista,new Artists(musicaID,artista));
                    return true;
                }
            }
        }
        return false;
    }


    //RECEBE A STRING DO ARTISTA E REMOVE OS CARACTERES INDESEJAVEIS RETORNANDO UMA STRING PROCESSADA.
    public static String processArtistString(String artists) {
        artists = artists.trim();
        artists = artists.replace("[", "");
        artists = artists.replace("]", "");
        artists = artists.replace("\"", "");
        artists = artists.trim();
        String[] partes = artists.split("'");
        StringBuilder processedString = new StringBuilder();
        for (String parte : partes) {
            String part = parte.trim();
            if (!part.isEmpty() && !part.equals(",")) {
                processedString.append(part).append(" ");
            }
        }
        return processedString.toString().trim();
    }
    public static String[] dividirArgs(String args, String divisor){
        String[] resultado = null;
        if (divisor.equals(";")){
            resultado =args.split(";");
        }
        if (divisor.equals(" ")){
            resultado =args.split(" ");
        }
        return resultado;
    }

    public static boolean loadFiles(File folder) {
        clearAll();
        int numNaoOk = 0;
        int primeiraLinhaNOK = -1;
        int numLinha;

        //DETALHES
        try {
            Scanner scanner = new Scanner(new File(folder, "song_details.txt"));
            numLinha = 0;
            while (scanner.hasNext()) {
                numLinha++;
                if (adicionarDetalhes(scanner.nextLine()) == LineResult.ERRO) {
                    numNaoOk++;
                    if (primeiraLinhaNOK == -1) {
                        primeiraLinhaNOK = numNaoOk + numNaoOk;
                    }
                }
            }
            fileInputResults.add(new FileInputResult("song_details.txt", numLinha - numNaoOk, numNaoOk, primeiraLinhaNOK));
        } catch (Exception e) {
            return false;
        }
        //SONGS
        numNaoOk = 0;
        primeiraLinhaNOK = -1;
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
                }
            }
            fileInputResults.add(new FileInputResult("songs.txt", numLinha - numNaoOk, numNaoOk, primeiraLinhaNOK));
        } catch (Exception e) {
            return false;
        }
        // ARTISTS
        idsDuplicados.clear();
        numNaoOk = 0;
        primeiraLinhaNOK = -1;

        try {
            Scanner scanner = new Scanner(new File(folder, "song_artists.txt"));
            while (scanner.hasNext()) {
                numLinha++;
                if (!adicionarArtista(scanner.nextLine())) {
                    numNaoOk++;
                    if (primeiraLinhaNOK == -1) {
                        primeiraLinhaNOK = numNaoOk + numNaoOk;
                    }
                }
            }
            fileInputResults.add(new FileInputResult("song_artists.txt", numLinha - numNaoOk, numNaoOk, primeiraLinhaNOK));
        } catch (Exception e) {

            return false;
        }
        return true;
    }
    //parse command vai receber a string e converter isso em um objeto Query
    public static Query parseCommand(String command) {
        String[] commandParts = command.split(" ", 2);
        if (commandParts.length < 2) {
            return null; // Comando inválido, retorna null
        }
        String[] args = commandParts[1].split("\\s+");
        return new Query(commandParts[0], args);
    }

    public static ArrayList<String> parseMultipleArtists(String line) {
        ArrayList<String> nomes = new ArrayList<>();

        Pattern padrao = Pattern.compile("'((?:[^']|'')*)'");
        Matcher matcher = padrao.matcher(line);

        while (matcher.find()) {
            String nome = matcher.group(1).replace("''", "'");
            nomes.add(nome);
        }

        return nomes;
    }
 // O execute tem que executar o parsecommand
    static QueryResult execute(String command) {
        Query comando = parseCommand(command);
        if (comando != null)
        {
            if (comando.name.equals(COUNT_SONGS_YEAR.toString())){
                ExecFunctions.count_Songs_Year(comando.args);
                return new QueryResult();
            }
            if (comando.name.equals(ADD_TAGS.toString())){
                ExecFunctions.add_Tags(comando.args);
                return new QueryResult();
            }
            if (comando.name.equals(GET_ARTISTS_FOR_TAG.toString())){
                ExecFunctions.get_Artists_For_Tag(comando.args);
                return new QueryResult();
            }
            if (comando.name.equals(GET_SONGS_BY_ARTIST.toString())){
                ExecFunctions.get_Songs_By_Artists(comando.args);
                return new QueryResult();
            }
            if (comando.name.equals(REMOVE_TAGS.toString())){
                ExecFunctions.remove_Tags(comando.args);
                return new QueryResult();
            }
            if (comando.name.equals(GET_MOST_DANCEABLE.toString())){
                ExecFunctions.get_Most_Danceable(comando.args);
                return new QueryResult();
            }
            if (comando.name.equals(COUNT_DUPLICATE_SONGS_YEAR.toString())){
                ExecFunctions.count_Duplicate_Songs_Year(comando.args);
                return new QueryResult();
            }
        }

        return null;
    }
    public static ArrayList getObjects(TipoEntidade tipo) {
        idsDuplicados.clear();
        if (tipo == TipoEntidade.TEMA) {
            for (Map.Entry<String, Songs> entry : songMap.entrySet()) {
                songArrayFinal.add(entry.getValue());
            }
            return songArrayFinal;
        } else if (tipo == TipoEntidade.ARTISTA) {
            for (Map.Entry<String, Artists> entry : artistsMap.entrySet()) {
                artistsArrayFinal.add(entry.getValue());
            }
            return artistsArrayFinal;
        } else if (tipo == TipoEntidade.INPUT_INVALIDO) {
            return fileInputResults;
        }

        return new ArrayList();
    }

    public static void main(String[] args)
    {
        if (loadFiles(new File("src/pt/ulusofona/aed/rockindeisi2023/files"))) {
            Scanner scanner = new Scanner(System.in);
            String tipo = scanner.nextLine().toUpperCase();
            while (tipo != null && !tipo.equals(EXIT.toString())) {
                QueryResult result = execute(tipo);
                if (result == null) {
                    System.out.println("Illegal command. Try again");
                } else {
                    System.out.println(result.result);
                    System.out.println("(took " + result.time + " ms)");
                }
                tipo = scanner.nextLine();
            }
        }
    }
}
