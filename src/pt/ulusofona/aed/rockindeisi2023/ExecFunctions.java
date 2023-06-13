package pt.ulusofona.aed.rockindeisi2023;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static pt.ulusofona.aed.rockindeisi2023.Main.artistsMap;
import static pt.ulusofona.aed.rockindeisi2023.Main.songMap;

public class ExecFunctions {

    //TODO Retorna o total de temas musicais que foram lançados no ano X. Input: "COUNT_SONGS_YEAR 2000"
    public static QueryResult count_Songs_Year(String[] args) {
        QueryResult result = new QueryResult();

        long startTime = System.currentTimeMillis();
        int numSongs = 0;
        int year = Integer.parseInt(args[0]);

        for (Songs song : songMap.values()) {
            if (song.anoLancamento == year) {
                numSongs++;
            }
        }

        long endTime = System.currentTimeMillis();

        result.result = String.valueOf(numSongs);
        result.time = endTime - startTime;

        return result;
    }


    //TODO Query para o video. Input: "COUNT_DUPLICATE_SONGS_YEAR 2000"
    public static QueryResult count_Duplicate_Songs_Year(String[] args) {
        QueryResult result = new QueryResult();

        long startTime = System.currentTimeMillis();
        int numSongs = 0;
        int year = Integer.parseInt(args[0]);
        ArrayList<String> nameSongs = new ArrayList<String>();

        for (Songs song : songMap.values()) {
            if (song.anoLancamento == year) {
                if (nameSongs.contains(song.nome)) {
                    numSongs++;
                } else {
                    nameSongs.add(song.nome);
                }
            }
        }

        long endTime = System.currentTimeMillis();
        result.result = String.valueOf(numSongs);
        result.time = endTime - startTime;

        return result;
    }

    //TODO Função Obrigatoria. Input : "GET_SONGS_BY_ARTIST 10 Nine Inch Nails"
    public static QueryResult get_Songs_By_Artists(String[] args) {
        QueryResult result = new QueryResult();
        String artistName = args[1];
        int numSongsExist = artistsMap.get(artistName).numMusicas;
        int numSongsWanted = (Integer.parseInt(args[0]) > numSongsExist) ? numSongsExist : Integer.parseInt(args[0]);

        long startTime = System.currentTimeMillis();
        if (numSongsWanted == 0) {
            result.result = "No songs";
        } else {
            for (Songs song : songMap.values()) {
                if (song.artistsName.contains(artistName)) {
                    numSongsWanted--;
                    result.result = result.result + song.nome + " : " + song.anoLancamento + ((numSongsWanted == 0) ? "" : "\n");
                }
                if (numSongsWanted == 0) {
                    break;
                }
            }
        }

        long endTime = System.currentTimeMillis();
        result.time = endTime - startTime;

        return result;
    }

    //TODO Função Obrigatoria. Input : "GET_MOST_DANCEABLE 2011 2013 3"
    public static QueryResult get_Most_Danceable(String[] args) {
        QueryResult result = new QueryResult();
        long startTime = System.currentTimeMillis();
        int startYear = Integer.parseInt(args[0]);
        int endYear = Integer.parseInt(args[1]);
        int numSongs = Integer.parseInt(args[2]);
        ArrayList<Songs> songs = new ArrayList<Songs>();

        for (Songs song : songMap.values()) {
            if (song.anoLancamento >= startYear && song.anoLancamento <= endYear) {
                songs.add(song);
            }
        }
        Collections.sort(songs, Comparator.comparing((Songs p) -> p.detalhes.dancabilidade).reversed());

        for (int i = 0; i < songs.size() && i < numSongs; i++) {
            result.result = result.result + songs.get(i).nome + " : " + songs.get(i).anoLancamento + " : " + songs.get(i).detalhes.dancabilidade + ((i == songs.size() - 1 || i == numSongs - 1) ? "" : "\n");
        }

        long endTime = System.currentTimeMillis();

        result.time = endTime - startTime;

        return result;
    }

    public static QueryResult get_Artists_One_Song(String[] args) {
        QueryResult result = new QueryResult();
        long startTime = System.currentTimeMillis();
        int low = Integer.parseInt(args[0]);
        int high = Integer.parseInt(args[1]);
        if (low >= high) {
            result.result = "Invalid period";
        } else {
            ArrayList<String> arrayArtists = new ArrayList<String>();
            ArrayList<String[]> arraySongs = new ArrayList<String[]>();
            ArrayList<String> toDelete = new ArrayList<String>();

            for (Songs song : songMap.values()) {
                if (song.anoLancamento >= low && song.anoLancamento <= high) {
                    for (String art : song.artistsName) {
                        if (arrayArtists.contains(art)) {
                            if (!toDelete.contains(art)) {
                                toDelete.add(art);
                            }
                        } else {
                            arrayArtists.add(art);
                            arraySongs.add(new String[]{art, song.idTemaMusical});
                        }
                    }
                }

            }

            for (String name : toDelete) {
                int index = arrayArtists.indexOf(name);
                arrayArtists.remove(index);
                arraySongs.remove(index);
            }
            Collections.sort(arraySongs, Comparator.comparing((String[] a) -> a[0]));
            for (int i = 0; i < arraySongs.size(); i++) {
                result.result = result.result + arraySongs.get(i)[0] + " | " + songMap.get(arraySongs.get(i)[1]).nome + " | " + songMap.get(arraySongs.get(i)[1]).anoLancamento + ((i == arraySongs.size() - 1) ? "" : "\n");
            }
        }

        long endTime = System.currentTimeMillis();

        result.time = endTime - startTime;
        return result;
    }

    public static QueryResult get_Top_Artists_With_Songs_Between(String[] args) {
        QueryResult result = new QueryResult();
        long startTime = System.currentTimeMillis();
        int numSongs = Integer.parseInt(args[0]);
        int low = Integer.parseInt(args[1]);
        int high = Integer.parseInt(args[2]);
        if (low > high) {
            int aux = low;
            low = high;
            high = aux;
        }
        ArrayList<Artists> artists = new ArrayList<Artists>();

        for (Artists art : artistsMap.values()) {
            if (art.numMusicas >= low && art.numMusicas <= high) {
                artists.add(art);
            }
        }
        Collections.sort(artists, Comparator.comparing((Artists a) -> a.numMusicas).reversed());

        for (int i = 0; i < artists.size() && i < numSongs; i++) {
            result.result = result.result + artists.get(i).nomeArtista + " " + artists.get(i).numMusicas + ((i == artists.size() - 1 || i == numSongs - 1) ? "" : "\n");
        }

        if (result.result.isEmpty()) {
            result.result = "No results";
        }

        long endTime = System.currentTimeMillis();

        result.time = endTime - startTime;

        return result;
    }

    public static QueryResult most_Frequent_Words_In_Artist_Name(String[] args) {
        QueryResult result = new QueryResult();
        long startTime = System.currentTimeMillis();
        int numWords = Integer.parseInt(args[0].trim());
        int numSongs = Integer.parseInt(args[1].trim());
        int wordSize = Integer.parseInt(args[2].trim());

        ArrayList<ArrayList<String>> wordCounter = new ArrayList<ArrayList<String>>();

        for (Artists art : artistsMap.values()) {
            if (art.numMusicas >= numSongs) {
                String[] names = art.nomeArtista.split(" ");
                for (String name : names) {
                    if (name.length() >= wordSize) {
                        boolean added = false;
                        for (ArrayList<String> array : wordCounter) {
                            if (array.get(0).equals(name)) {
                                array.add(name);
                                added = true;
                                break;
                            }
                        }
                        if (!added) {
                            ArrayList<String> newWordArray = new ArrayList<String>();
                            newWordArray.add(name);
                            wordCounter.add(newWordArray);
                        }
                    }
                }
            }
        }
        Collections.sort(wordCounter, Comparator.comparing((ArrayList<String> a) -> a.size()));

        numWords = (wordCounter.size() > numWords) ? numWords : wordCounter.size();
        for (int i = 0; i < numWords; i++) {
            result.result = result.result + wordCounter.get(wordCounter.size() - numWords + i).get(0) + " " + wordCounter.get(wordCounter.size() - numWords + i).size() + ((i == numWords - 1) ? "" : "\n");
        }

        long endTime = System.currentTimeMillis();

        result.time = endTime - startTime;

        return result;
    }

    public static QueryResult get_Unique_Tags(String[] args) {
        QueryResult result = new QueryResult();
        long startTime = System.currentTimeMillis();

        ArrayList<ArrayList<String>> tagCounter = new ArrayList<ArrayList<String>>();

        for (Artists art : artistsMap.values()) {
            for (String tag : art.tags) {
                boolean added = false;
                for (ArrayList<String> array : tagCounter) {
                    if (array.get(0).equals(tag)) {
                        array.add(tag);
                        added = true;
                        break;
                    }
                }
                if (!added) {
                    ArrayList<String> newWordArray = new ArrayList<String>();
                    newWordArray.add(tag);
                    tagCounter.add(newWordArray);
                }
            }
        }
        Collections.sort(tagCounter, Comparator.comparing((ArrayList<String> a) -> a.size()));

        for (int i = 0; i < tagCounter.size(); i++) {
            result.result = result.result + tagCounter.get(i).get(0) + " " + tagCounter.get(i).size() + ((i == tagCounter.size() - 1) ? "" : "\n");
        }
        if (result.result.isEmpty()) {
            result.result = "No results";
        }

        long endTime = System.currentTimeMillis();

        result.time = endTime - startTime;

        return result;
    }

    public static QueryResult get_Unique_Tags_In_Between_Years(String[] args) {
        QueryResult result = new QueryResult();
        int low = Integer.parseInt(args[0].trim());
        int high = Integer.parseInt(args[1].trim());
        long startTime = System.currentTimeMillis();

        if (low > high) {
            int aux = low;
            low = high;
            high = aux;
        }

        ArrayList<ArrayList<String>> tagCounter = new ArrayList<ArrayList<String>>();

        for (Songs song : songMap.values()) {
            if (song.anoLancamento >= low && song.anoLancamento <= high) {
                for (String artistName : song.artistsName) {
                    for (String tag : artistsMap.get(artistName).tags) {
                        boolean added = false;
                        for (ArrayList<String> array : tagCounter) {
                            if (array.get(0).equals(tag)) {
                                array.add(tag);
                                added = true;
                                break;
                            }
                        }
                        if (!added) {
                            ArrayList<String> newWordArray = new ArrayList<String>();
                            newWordArray.add(tag);
                            tagCounter.add(newWordArray);
                        }
                    }
                }
            }

        }
        Collections.sort(tagCounter, Comparator.comparing((ArrayList<String> a) -> a.size()).reversed());

        for (int i = 0; i < tagCounter.size(); i++) {
            result.result = result.result + tagCounter.get(i).get(0) + " " + tagCounter.get(i).size() + ((i == tagCounter.size() - 1) ? "" : "\n");
        }
        if (result.result.isEmpty()) {
            result.result = "No results";
        }

        long endTime = System.currentTimeMillis();

        result.time = endTime - startTime;
        return result;
    }

    public static QueryResult get_Rising_Stars(String[] args) {
        QueryResult result = new QueryResult();

        return result;
    }

    //TODO Função Obrigatoria. Input: "ADD_TAGS Nirvana;Rockalhada"
    public static QueryResult add_Tags(String[] args) {
        QueryResult result = new QueryResult();
        long startTime = System.currentTimeMillis();
        if (artistsMap.get(args[0]) != null) {
            for (int i = 1; i < args.length; i++) {
                if (!artistsMap.get(args[0]).tags.contains(args[i].toUpperCase())) {
                    artistsMap.get(args[0]).tags.add(args[i].toUpperCase());
                }
            }
            result.result = args[0] + " | ";
            for (String tag : artistsMap.get(args[0]).tags) {
                result.result = result.result + tag.toUpperCase() + ",";
            }
            result.result = result.result.substring(0, result.result.length() - 1);
        } else {
            result.result = "Inexistent artist";
        }

        long endTime = System.currentTimeMillis();
        result.time = endTime - startTime;

        return result;
    }

    //TODO Função Obrigatoria. REMOVE_TAGS Nirvana;Rockalhada
    public static QueryResult remove_Tags(String[] args) {
        QueryResult result = new QueryResult();
        long startTime = System.currentTimeMillis();

        if (artistsMap.get(args[0]) != null) {
            for (int i = 1; i < args.length; i++) {
                artistsMap.get(args[0]).tags.remove(args[i].toUpperCase());
            }
            result.result = args[0] + " | ";
            if (artistsMap.get(args[0]).tags.size() == 0) {
                result.result = result.result + "No tags";
            } else {
                for (String tag : artistsMap.get(args[0]).tags) {
                    result.result = result.result + tag.toUpperCase() + ",";
                }
                result.result = result.result.substring(0, result.result.length() - 1);
            }
        } else {
            result.result = "Inexistent artist";
        }

        long endTime = System.currentTimeMillis();
        result.time = endTime - startTime;

        return result;
    }

    //TODO Função Obrigatoria. Input: "GET_ARTISTS_FOR_TAG Rockalhada"
    public static QueryResult get_Artists_For_Tag(String[] args) {
        QueryResult result = new QueryResult();
        long startTime = System.currentTimeMillis();

        for (Artists art : artistsMap.values()) {
            if (art.tags.contains(args[0].toUpperCase())) {
                result.result = result.result + art.nomeArtista + ";";
            }
        }

        if (result.result.isEmpty()) {
            result.result = "No results";
        } else {
            result.result = result.result.substring(0, result.result.length() - 1);
        }

        long endTime = System.currentTimeMillis();
        result.time = endTime - startTime;

        return result;
    }

    //    Query Componente de Criatividade
    public static QueryResult get_Songs_Between_Years(String[] args) {
        QueryResult result = new QueryResult();
        int low = Integer.parseInt(args[0].trim());
        int high = Integer.parseInt(args[1].trim());
        long startTime = System.currentTimeMillis();

        if (low > high) {
            int aux = low;
            low = high;
            high = aux;
        }

        ArrayList<Songs> songsList = new ArrayList<>();

        for (Songs song : songMap.values()) {
            if (song.anoLancamento >= low && song.anoLancamento <= high) {
                songsList.add(song);
            }

        }
        Collections.sort(songsList, Comparator.comparingInt(Songs::getYear));
        StringBuilder resultado = new StringBuilder();
        for (int i = 0; i < songsList.size(); i++) {
            resultado.append(songsList.get(i).nome);
            resultado.append("\n");
        }
        result.result = resultado.toString();
        long endTime = System.currentTimeMillis();

        result.time = endTime - startTime;
        return result;
    }
}
