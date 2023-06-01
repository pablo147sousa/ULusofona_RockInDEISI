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
    static ArrayList<FileInputResult> fileInputResults = new ArrayList<>();

    public enum LineResult {
        OK, ERRO
    }
    public static void clearAll() {
        songMap.clear();
        detailsMap.clear();
        artistsMap.clear();
        fileInputResults.clear();
    }

    private static ArrayList<String> verificarPadrao(String texto){
        // "['\"]([^\\s]+)['\"]"
        String padrao = "\"(.*?)\"|'([^']*)'";

        ArrayList<String> resultado = new ArrayList<>();
        // Compila o padrão em uma expressão regular
        Pattern pattern = Pattern.compile(padrao);
        Matcher matcher = pattern.matcher(texto);

        // Itera sobre as correspondências encontradas
        while (matcher.find()) {
            // Obtém o valor correspondente entre as aspas simples ou duplas
            String valor = matcher.group(1);
            //valor = valor.replace("\"","");
            resultado.add(valor);
        }
        return resultado;
    }
    private static boolean validarArtista(String part) {
        String unicoArtista = "\\['[^']*'\\]";
        String artistasMultiplos = "^\\\"\\[('[\\w\\s]+')(?:,\\s\\1)*\\]\\\"$";

        return part.matches(unicoArtista) || part.matches(artistasMultiplos);
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
        }
        else {
            // CRIA O SONGS
            Songs songAdd = new Songs(id, nome, ano);
            songMap.put(id, songAdd);
            idTemasMusicais.add(id);
            return LineResult.OK;
        }
    }
    public static boolean validacaoArtista(String artistaTexto) {
        int count = 0;
        boolean ocorreVirgula = false;

        for (int i = 0; i < artistaTexto.length(); i++) {
            char c = artistaTexto.charAt(i);
            if (c == ',') {
                ocorreVirgula = true;
            }
            if (c == '"') {
                count++;
            }
        }

        return !ocorreVirgula || (count == 2 && artistaTexto.charAt(0) == '"');
    }


    public static LineResult adicionarDetalhes(String detalhes) {
        String[] partes = detalhes.split(" @ ");
        String idTema = partes[0].trim();
        // VERIFICA O INPUT CORRETO
        if (partes.length != 7){
            return LineResult.ERRO;
        }
        Details detalhe = new Details(idTema, Integer.parseInt(partes[1].trim()), Integer.parseInt(partes[2].trim()), Integer.parseInt(partes[3].trim()), Double.parseDouble(partes[4].trim()), Double.parseDouble(partes[5].trim()), Double.parseDouble(partes[6].trim()));
        if (songMap.containsKey(idTema)){
            detailsMap.put(idTema,detalhe);
            songMap.get(idTema).detalhes = detalhe;
            return LineResult.OK;
        }
        return null;
    }
    public static void verMusicasSemDetalhes() {
        // musicas nao tem detalhes
        for (String id : idTemasMusicais) {
            if (!detailsMap.containsKey(id)){
                songMap.remove(id);
            }
        }
    }
    public static LineResult adicionarArtista(String artistasStr) {
        String[] partes = artistasStr.split(" @ ");

        // VERIFICA O INPUT CORRETO
        if (partes.length != 2) {
            return LineResult.ERRO;
        }
        // VERIFICA SE O INPUT ESTÁ BEM FORMATADO
        if (validacaoArtista(partes[1])) {
            // Verificar se a música existe antes de percorrer os artistas
            String musicaID = partes[0];
            ArrayList<String> artistaArray = getArtists(partes[1]);
            for (String artista : artistaArray) {
                if (songMap.containsKey(musicaID)) {
                    songMap.get(musicaID).numArtistas++;
                    // Verificar se o artista já foi adicionado antes
                    if (!artistsMap.containsKey(artista)) {
                        Artists newArtist = new Artists(musicaID,artista);
                        artistsMap.put(artista, newArtist);
                    } else {
                        artistsMap.get(artista).numMusicas++;
                        
                    }
                }
            }

        }
        return LineResult.OK;
    }
    //RECEBE A STRING DO ARTISTA E REMOVE OS CARACTERES INDESEJAVEIS RETORNANDO UMA STRING PROCESSADA.
    public static ArrayList<String> getArtists(String artists) {
        ArrayList<String> artistas = new ArrayList<>();
        artists = artists.trim();
        boolean multiple = false;
        if (artists.charAt(0) == '\"'){
            multiple = true;
        }
        artists = artists.replace("[", "");
        artists = artists.replace("]", "");
        artists = artists.replace("\"", "");
        artists = artists.trim();
        String[] partes = artists.split("'");
        for (String parte : partes) {
            artistas.add(parte.trim());
        }
        // remove any empty strings or strings with ,
        for (int i = 0; i < artistas.size(); i++) {
            if (artistas.get(i).equals("") || artistas.get(i).equals(",")) {
                artistas.remove(i);
            }
        }
        if (multiple){
            return artistas;
        }else if(artistas.size() > 1) {
            return new ArrayList<>();
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
        return true;
    }
    //parse command vai receber a string e converter isso em um objeto Query
    public static Query parseCommand(String command) {
        String[] commandParts = command.split(" ", 2);
        if (commandParts.length < 2) {
            return null; // Comando inválido, retorna null
        }
        String[] args;
        if (commandParts[0].equals(ADD_TAGS) || commandParts[0].equals(REMOVE_TAGS)){
            args = dividirArgs(commandParts[1],";");
        }else {
            args = dividirArgs(commandParts[1]," ");
        }

        return new Query(commandParts[0], args);
    }

    public static ArrayList<String> parseMultipleArtists(String line) {
        String padrao = "['\"]([^\\s]+)['\"]";
        ArrayList<String> nomes = new ArrayList<>();
        // Compila o padrão em uma expressão regular
        Pattern pattern = Pattern.compile(padrao);
        Matcher matcher = pattern.matcher(line);

        // Itera sobre as correspondências encontradas
        while (matcher.find()) {
            // Obtém o valor correspondente entre as aspas simples ou duplas
            String valor = matcher.group(1);
            nomes.add(valor.replace("\"",""));
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
