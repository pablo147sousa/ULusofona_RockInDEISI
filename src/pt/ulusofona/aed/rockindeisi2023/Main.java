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
    static ArrayList<Songs> songArrayFinal;
    static ArrayList<Artists> artistsArrayFinal;

    public enum LineResult {
        OK, ERRO
    }


    private static boolean validarArtista(String part) {
        String unicoArtista = "\\['[^']*'\\]";
        String artistasMultiplos = "\"\\['([^']', )[^']*'\\]\"";
        return part.matches(unicoArtista) || part.matches(artistasMultiplos);
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
        // VERIFICA O INPUT CORRETO
        if (partes.length != 3) {
            return LineResult.ERRO;
        }

        String id = partes[0].trim();
        String nome = partes[1].trim();
        // VERIFICA SE JÁ FOI CRIADO ANTERIORMENTE
        if (idsDuplicados.contains(id) || !idTemasMusicais.contains(id)) {
            return null;
        } else {
            // CRIA O SONGS
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
        String idTema = partes[0].trim();
        // VERIFICA O INPUT CORRETO
        if (partes.length != 7){
            return LineResult.ERRO;
        }
        // VERIFICA SE JÁ FOI CRIADO ANTERIORMENTE
        if (detailsMap.containsKey(idTema)){
            return null;
        }
        idTemasMusicais.add(idTema);
        detailsMap.put(idTema,new Details(idTema, Integer.parseInt(partes[1].trim()), Integer.parseInt(partes[2].trim()), Integer.parseInt(partes[3].trim()), Double.parseDouble(partes[4].trim()), Double.parseDouble(partes[5].trim()), Double.parseDouble(partes[6].trim())));
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
    public static LineResult adicionarArtista(String artistasStr) {
        String[] partes = artistasStr.split(" @ ");

        // VERIFICA O INPUT CORRETO
        if (partes.length != 2) {
            return LineResult.ERRO;
        }

        // VERIFICA SE O INPUT ESTÁ BEM FORMATADO
        if (validarArtista(partes[1])) {
            String musicaID = partes[0];
            List<String> artistaArray = getArtists(partes[1]);

            // Verificar se a música existe antes de percorrer os artistas
            if (songMap.containsKey(musicaID)) {
                for (String artista : artistaArray) {
                    // Verificar se o artista já foi adicionado antes
                    if (!artistsMap.containsKey(artista)) {
                        Artists newArtist = new Artists(artista);
                        artistsMap.put(artista, newArtist);
                    } else {
                        Artists existingArtist = artistsMap.get(artista);

                        // Verificar se a música já foi adicionada pelo artista antes
                        if (!existingArtist.musicas.contains(musicaID)) {
                            existingArtist.musicas.add(musicaID);
                            existingArtist.numMusicas++;
                        }
                    }
                    songMap.get(musicaID).numArtistas++;
                }
                return LineResult.OK;
            }
        }
        return null;
    }




    //RECEBE A STRING DO ARTISTA E REMOVE OS CARACTERES INDESEJAVEIS RETORNANDO UMA STRING PROCESSADA.
    public static List<String> getArtists(String artists) {
        artists = artists.trim();

        boolean multiple = artists.startsWith("\"");

        artists = artists.replaceAll("[\\[\\]\"']", "").trim();

        String[] parts = artists.split("'");

        List<String> artistas = new ArrayList<>();

        for (String part : parts) {
            part = part.trim();
            if (!part.isEmpty()) {
                artistas.add(part);
            }
        }

        if (multiple) {
            return artistas;
        } else if (artistas.size() > 1) {
            return Collections.emptyList();
        } else {
            return artistas;
        }
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
                        primeiraLinhaNOK = numLinha;
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
        numNaoOk = 0;
        primeiraLinhaNOK = -1;

        try {
            Scanner scanner = new Scanner(new File(folder, "song_artists.txt"));
            numLinha = 0;
            while (scanner.hasNext()) {
                numLinha++;
                if (adicionarArtista(scanner.nextLine()) == LineResult.ERRO) {
                    numNaoOk++;
                    if (primeiraLinhaNOK == -1) {
                        primeiraLinhaNOK = numLinha;
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
            songArrayFinal = new ArrayList<>(songMap.values());
            return songArrayFinal;
        } else if (tipo == TipoEntidade.ARTISTA) {
            artistsArrayFinal = new ArrayList<>(artistsMap.values());
            return artistsArrayFinal;
        } else if (tipo == TipoEntidade.INPUT_INVALIDO) {
            return fileInputResults;
        }

        return null;
    }

    public static void main(String[] args)
    {
        if (loadFiles(new File("src/pt/ulusofona/aed/rockindeisi2023/files"))) {
            System.out.println(adicionarArtista("1fEepJX6lz8fqhDRsNxkQp @ \"['Antônio Carlos Jobim', 'Vinícius de Moraes']\""));
            Scanner scanner = new Scanner(System.in);
            ArrayList resultado = Main.getObjects(TipoEntidade.TEMA);

            for (Object o : resultado) {
                System.out.println(o);
            }
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
