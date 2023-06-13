package pt.ulusofona.aed.rockindeisi2023;

import java.io.File;
import java.sql.SQLOutput;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static pt.ulusofona.aed.rockindeisi2023.QuerysEnum.*;

public class Main {
    static LinkedHashMap<String,Songs> songMap = new LinkedHashMap<>();
    //static LinkedHashMap<String,Details> detailsMap = new LinkedHashMap<>();
    static LinkedHashMap<String,Artists> artistsMap = new LinkedHashMap<>();
    static ArrayList<FileInputResult> fileInputResults = new ArrayList<>();

    public enum LineResult {
        OK, ERRO
    }
    public static void clearAll() {
        songMap.clear();
        //detailsMap.clear();
        artistsMap.clear();
        fileInputResults.clear();
    }

    public static LineResult adicionarMusica(String musica) {
        String[] partes = musica.split("@");
        // VERIFICA O INPUT CORRETO
        if (partes.length != 3) {
            return LineResult.ERRO;
        }

        String id = partes[0].trim();
        String nome = partes[1].trim();
        int ano = Integer.parseInt(partes[2].trim().replace("@", ""));
        if (ano < 1 || ano > 2023){
            return LineResult.ERRO;
        }
        // VERIFICA SE JÁ FOI CRIADO ANTERIORMENTE
        if (songMap.containsKey(id)){
            return LineResult.ERRO;
        } else {
            // CRIA O SONGS
            Songs songAdd = new Songs(id, nome, ano);
            songMap.put(id, songAdd);
            return LineResult.OK;
        }
    }

    public static LineResult adicionarDetalhes(String detalhes) {
        String[] partes = detalhes.split("@");
        String idTema = partes[0].trim();
        // VERIFICA O INPUT CORRETO
        if (partes.length != 7){
            return LineResult.ERRO;
        }
        Details detalhe = new Details(idTema, Integer.parseInt(partes[1].trim()), Integer.parseInt(partes[2].trim()), Integer.parseInt(partes[3].trim()), Double.parseDouble(partes[4].trim()), Double.parseDouble(partes[5].trim()), Double.parseDouble(partes[6].trim()));
        if (songMap.containsKey(idTema)){
            //detailsMap.put(idTema,detalhe);
            songMap.get(idTema).detalhes = detalhe;
            return LineResult.OK;
        }
        return null;
    }

    public static void verMusicasSemDetalhes() {
        // musicas nao tem detalhes
        ArrayList<String> toDelete = new ArrayList<String>();
        for (Songs song : songMap.values()) {
            if (song.detalhes == null){
                toDelete.add(song.idTemaMusical);
            }
        }
        for (String id : toDelete) {
            songMap.remove(id);
        }
    }

    public static void verMusicasSemArtistas() {
        ArrayList<String> toDelete = new ArrayList<String>();
        for (Songs song : songMap.values()) {
            if (song.artistsName.size() == 0){
                toDelete.add(song.idTemaMusical);
            }
        }
        for (String id : toDelete) {
            songMap.remove(id);
        }
    }
    public static LineResult adicionarArtista(String artistasStr) {
        String[] partes = artistasStr.split("@");

        // VERIFICA O INPUT CORRETO
        if (partes.length != 2) {
            return LineResult.ERRO;
        }
        // VERIFICA SE O INPUT ESTÁ BEM FORMATADO
        ArrayList<String> artistaArray = parseMultipleArtists(partes[1].trim());
        if (!artistaArray.isEmpty()) {

            // Verificar se a música existe antes de percorrer os artistas
            String musicaID = partes[0].trim();

            if (songMap.containsKey(musicaID)) {
                for (String artista : artistaArray) {
                    if(songMap.get(musicaID).artistsName.contains(artista)){
                        continue;
                    }
                    songMap.get(musicaID).artistsName.add(artista);
                    // Verificar se o artista já foi adicionado antes
                    if (!artistsMap.containsKey(artista)) {
                        Artists newArtist = new Artists(artista);
                        artistsMap.put(artista, newArtist);
                    } else {
                        artistsMap.get(artista).numMusicas++;
                    }
                }
            }else{
                return LineResult.ERRO;
            }
        }else{
            return LineResult.ERRO;
        }
        return LineResult.OK;
    }



    //RECEBE A STRING DO ARTISTA E REMOVE OS CARACTERES INDESEJAVEIS RETORNANDO UMA STRING PROCESSADA.

    public static String[] dividirArgs(QuerysEnum command, String args) {
        String[] resultado = null;
        switch (command) {
            case COUNT_SONGS_YEAR -> resultado = new String[]{args};
            case ADD_TAGS, REMOVE_TAGS -> resultado = args.split(";");
            case GET_SONGS_BY_ARTIST -> resultado = args.split(" ", 2);
            case GET_ARTISTS_FOR_TAG -> resultado = new String[]{args};
            case GET_MOST_DANCEABLE -> resultado = args.split(" ", 3);
            case GET_TOP_ARTISTS_WITH_SONGS_BETWEEN -> resultado = args.split(" ", 3);
            case MOST_FREQUENT_WORDS_IN_ARTIST_NAME -> resultado = args.split(" ", 3);
            case GET_ARTISTS_ONE_SONG -> resultado = args.split(" ", 2);
            case GET_RISING_STARS -> resultado = args.split(" ", 3);
            case COUNT_DUPLICATE_SONGS_YEAR -> resultado = new String[]{args};
            case GET_UNIQUE_TAGS_IN_BETWEEN_YEARS -> resultado = args.split(" ", 2);
            case GET_UNIQUE_TAGS -> resultado = new String[]{};
            case GET_SONGS_BETWEEN_YEARS -> resultado = args.split(" ", 2);
        }
        return resultado;
    }

    public static boolean loadFiles(File folder) {
        clearAll();
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
                }
            }
            fileInputResults.add(new FileInputResult("songs.txt", numLinha - numNaoOk, numNaoOk, primeiraLinhaNOK));
        } catch (Exception e) {
            return false;
        }

        //DETALHES
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
                }
            }
            fileInputResults.add(new FileInputResult("song_details.txt", numLinha - numNaoOk, numNaoOk, primeiraLinhaNOK));
        } catch (Exception e) {
            return false;
        }
        verMusicasSemDetalhes();

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
        verMusicasSemArtistas();
        return true;
    }

    //parse command vai receber a string e converter isso em um objeto Query
    public static Query parseCommand(String command) {
        String[] commandParts = command.split(" ", 2);
        if (commandParts.length < 2 && !commandParts[0].equals(GET_UNIQUE_TAGS.toString())) {
            return null; // Comando inválido, retorna null
        }
        String[] args = dividirArgs(QuerysEnum.valueOf(commandParts[0].toUpperCase()), (commandParts.length < 2) ? null : commandParts[1]);
        if (args != null) {
            return new Query(commandParts[0], args);
        } else {
            return null;
        }
    }
    public static Query parseCommandGPT(String command) {
        String[] parts = command.split(" ");
        if (parts.length < 2) {
            return null; // Comando inválido se não houver pelo menos dois componentes
        }

        String name = parts[0];
        String[] args = Arrays.copyOfRange(parts, 1, parts.length);

        return new Query(name, args);
    }

    public static ArrayList<String> parseMultipleArtists(String str) {
        ArrayList<String> result = new ArrayList<>();

        String cleanInput = str.trim().substring(1, str.length() - 1);
        Pattern pattern = Pattern.compile("\"\"(.*?)\"\"|'(.*?)'");

        Matcher matcher = pattern.matcher(cleanInput);
        while (matcher.find()) {
            String match = matcher.group(1) != null ? matcher.group(1) : matcher.group(2);
            result.add(match);
        }
        return result;
    }

    public static ArrayList<String> parseMultipleGPT(String str) {
        ArrayList<String> artists = new ArrayList<>();

        // Remover os caracteres '[' e ']'
        str = str.replace("[", "").replace("]", "");

        // Regex para encontrar os nomes entre aspas duplas ou aspas simples
        Pattern pattern = Pattern.compile("\"{2}([^,]*?)\"{2}|'([^']*?)'");

        Matcher matcher = pattern.matcher(str);

        while (matcher.find()) {
            String artist = matcher.group(1) != null ? matcher.group(1) : matcher.group(2);
            artists.add(artist);
        }

        return artists;
    }

 // O execute tem que executar o parsecommand
    static QueryResult execute(String command) {
        Query comando = parseCommand(command);
        if (comando != null)
        {
            if (comando.name.equals(COUNT_SONGS_YEAR.toString())){
                return ExecFunctions.count_Songs_Year(comando.args);
            }
            if (comando.name.equals(ADD_TAGS.toString())){
                return ExecFunctions.add_Tags(comando.args);
            }
            if (comando.name.equals(GET_ARTISTS_FOR_TAG.toString())){
                return ExecFunctions.get_Artists_For_Tag(comando.args);
            }
            if (comando.name.equals(GET_SONGS_BY_ARTIST.toString())){
                return ExecFunctions.get_Songs_By_Artists(comando.args);
            }
            if (comando.name.equals(REMOVE_TAGS.toString())){
                return ExecFunctions.remove_Tags(comando.args);
            }
            if (comando.name.equals(GET_MOST_DANCEABLE.toString())){
                return ExecFunctions.get_Most_Danceable(comando.args);
            }
            if (comando.name.equals(COUNT_DUPLICATE_SONGS_YEAR.toString())){
                return ExecFunctions.count_Duplicate_Songs_Year(comando.args);
            }
            if (comando.name.equals(GET_TOP_ARTISTS_WITH_SONGS_BETWEEN.toString())){
                return ExecFunctions.get_Top_Artists_With_Songs_Between(comando.args);
            }
            if (comando.name.equals(MOST_FREQUENT_WORDS_IN_ARTIST_NAME.toString())){
                return ExecFunctions.most_Frequent_Words_In_Artist_Name(comando.args);
            }
            if (comando.name.equals(GET_ARTISTS_ONE_SONG.toString())){
                return ExecFunctions.get_Artists_One_Song(comando.args);
            }
            if (comando.name.equals(GET_RISING_STARS.toString())){
                return ExecFunctions.get_Rising_Stars(comando.args);
            }
            if (comando.name.equals(GET_UNIQUE_TAGS_IN_BETWEEN_YEARS.toString())){
                return ExecFunctions.get_Unique_Tags_In_Between_Years(comando.args);
            }
            if (comando.name.equals(GET_UNIQUE_TAGS.toString())){
                return ExecFunctions.get_Unique_Tags(comando.args);
            }
            if (comando.name.equals(GET_SONGS_BETWEEN_YEARS.toString())){
                return ExecFunctions.get_Songs_Between_Years(comando.args);
            }
        }

        return null;
    }
    public static ArrayList getObjects(TipoEntidade tipo) {
        if (tipo == TipoEntidade.TEMA) {
            return new ArrayList<>(songMap.values());
        } else if (tipo == TipoEntidade.ARTISTA) {
            return new ArrayList<>(artistsMap.values());
        } else if (tipo == TipoEntidade.INPUT_INVALIDO) {
            return fileInputResults;
        }

        return null;
    }

    public static void main(String[] args)
    {
        if (loadFiles(new File("test-files"))) {
            Scanner scanner = new Scanner(System.in);
            String tipo = scanner.nextLine();
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
