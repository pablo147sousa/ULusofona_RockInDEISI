package pt.ulusofona.aed.rockindeisi2023;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

public class TestXXX {
    //     Conversão para ‘String’ de objetos contendo informação de músicas anteriores a 1995
    @Test
    public void test_Song_ToString_Prior1995() {
        if (Main.loadFiles(new File("test-files"))) {

            var expected = "6r8k1vznHrzlEKYxL4dZEe | La Isla Bonita | 1986";

            for (Songs song : Main.songMap.values()) {
                if (song.idTemaMusical.equals("6r8k1vznHrzlEKYxL4dZEe")) {
                    Assertions.assertEquals(expected, song.toString(), "test failed");
                }
            }
        }
    }

    //     Conversão para String de objetos contendo informação de músicas anteriores a 1995
    @Test
    public void test_Song_ToString_Prior2000() {
        if (Main.loadFiles(new File("test-files"))) {

            var expected = "3uXp4hZmCnEmVjffupKCiT | Be Not Right | 1997 | " + Songs.converteDuracao(167680) + " | 36";

            for (Songs song : Main.songMap.values()) {
                if (song.idTemaMusical.equals("3uXp4hZmCnEmVjffupKCiT")) {
                    Assertions.assertEquals(expected, song.toString(), "test failed");
                }
            }
        }
    }

    //     Conversão para String de objetos contendo informação de músicas superiores a 2000
    @Test
    public void test_Song_ToString_After2000() {
        if (Main.loadFiles(new File("test-files"))) {

            var expected = "1SlMYQmLe0yNEvBIfaPTAW | Ich will | 2001 | 3:37 | 57 | 1";

            for (Songs song : Main.songMap.values()) {
                if (song.idTemaMusical.equals("1SlMYQmLe0yNEvBIfaPTAW")) {
                    Assertions.assertEquals(expected, song.toString(), "test failed");
                }
            }
        }
    }

    //    Conversão para String de objetos contendo informação de artistas
    @Test
    public void test_Artist_To_String() {
        if (Main.loadFiles(new File("test-files"))) {
            var expected = "Artista: [Rammstein] | 2";
            for (String key : Main.artistsMap.keySet()) {
                if (Main.artistsMap.get(key).equals("Rammstein")) {
                    Assertions.assertEquals(expected, Main.artistsMap.get(key).toString(), "test failed");
                }
            }
        }
    }

    //    Leitura dos 3 ficheiros (sem erros) contendo pelo menos 2 músicas e 2 artistas
    @Test
    public void test_Loadfiles_With_No_Errors() {
        if (Main.loadFiles(new File("test-files/noErrors"))) {
            var expected = "[songs.txt | 5 | 0 | -1, song_details.txt | 5 | 0 | -1, song_artists.txt | 5 | 0 | -1]";
            Assertions.assertEquals(expected, Main.getObjects(TipoEntidade.INPUT_INVALIDO).toString(), "test failed");
        }
    }

    //    Leitura dos 3 ficheiros com erros (linhas com elementos em falta)
    @Test
    public void test_Loadfiles_With_Errors() {
        if (Main.loadFiles(new File("test-files"))) {
            var expected = "[songs.txt | 11 | 1 | 3, song_details.txt | 11 | 0 | -1, song_artists.txt | 7 | 5 | 1]";
            Assertions.assertEquals(expected, Main.getObjects(TipoEntidade.INPUT_INVALIDO).toString(), "test failed");
        }
    }
    // Componente de Criatividade teste
    @Test
    public void testCreativeQuery() {
        if (Main.loadFiles(new File("test-files"))) {
            QueryResult obtido = Main.execute("GET_SONGS_BETWEEN_YEARS 1986 2001");
            String esperado = "La Isla Bonita\nBe Not Right\nIch will\nIch bill\n";
            Assertions.assertEquals(esperado, obtido.result);
            obtido = Main.execute("GET_SONGS_BETWEEN_YEARS 1997 2001");
            esperado = "Be Not Right\nIch will\nIch bill\n";
            Assertions.assertEquals(esperado, obtido.result);
        }
    }

    //Teste Query COUNT_SONGS_YEAR
    @Test
    public void test_COUNT_SONGS_YEAR(){
        if (Main.loadFiles(new File("test-files"))){
            var expected = "2";
            QueryResult result = Main.execute("COUNT_SONGS_YEAR 2001");
            Assertions.assertEquals(expected,result.result);
            expected = "1";
            result = Main.execute("COUNT_SONGS_YEAR 1986");
            Assertions.assertEquals(expected,result.result);
        }
    }

    //Teste Query GET_SONGS_BY_ARTIST
    @Test
    public void test_GET_SONGS_BY_ARTIST(){
        if (Main.loadFiles(new File("test-files"))){
            var expected = "La Isla Bonita : 1986";
            QueryResult result = Main.execute("GET_SONGS_BY_ARTIST 1 Madonna");
            Assertions.assertEquals(expected,result.result);
            expected = "Ich will : 2001\nHeirate mich : 2002";
            result = Main.execute("GET_SONGS_BY_ARTIST 2 Rammstein");
            Assertions.assertEquals(expected,result.result);
        }
    }


    //Teste Query GET_MOST_DANCEABLE
    @Test
    public void test_GET_MOST_DANCEABLE(){
        if (Main.loadFiles(new File("test-files"))){
            var expected = "Ich will : 2001 : 0.648\nBe Not Right : 1997 : 0.5770000000000001";
            QueryResult result = Main.execute("GET_MOST_DANCEABLE 1997 2001 2");
            Assertions.assertEquals(expected,result.result);
            expected = "La Isla Bonita : 1986 : 0.725";
            result = Main.execute("GET_MOST_DANCEABLE 1985 1987 1");
            Assertions.assertEquals(expected,result.result);
        }
    }


    //Teste Query ADD_TAGS
    @Test
    public void test_ADD_TAGS(){
        if (Main.loadFiles(new File("test-files"))){
            var expected = "Rammstein | ARTISTA";
            QueryResult result = Main.execute("ADD_TAGS Rammstein;Artista");
            Assertions.assertEquals(expected,result.result);
            expected = "Rammstein | ARTISTA,CANTOR";
            result = Main.execute("ADD_TAGS Rammstein;Cantor");
            Assertions.assertEquals(expected,result.result);
        }
    }


    //Teste Query REMOVE_TAGS
    @Test
    public void test_REMOVE_TAGS(){
        if (Main.loadFiles(new File("test-files"))){
            var expected = "Rammstein | ARTISTA";
            QueryResult result = Main.execute("ADD_TAGS Rammstein;Artista");
            result = Main.execute("ADD_TAGS Rammstein;Cantor");
            result = Main.execute("REMOVE_TAGS Rammstein;Cantor");
            Assertions.assertEquals(expected,result.result);
            expected = "Rammstein | No tags";
            result = Main.execute("REMOVE_TAGS Rammstein;Artista");
            Assertions.assertEquals(expected,result.result);
        }
    }


    //Teste Query GET_ARTISTS_FOR_TAG
    @Test
    public void test_GET_ARTISTS_FOR_TAG(){
        if (Main.loadFiles(new File("test-files"))){
            var expected = "Rammstein";
            QueryResult result = Main.execute("ADD_TAGS Rammstein;Artista");
            result = Main.execute("GET_ARTISTS_FOR_TAG Artista");
            Assertions.assertEquals(expected,result.result);
            result = Main.execute("ADD_TAGS Madonna;Artista");
            expected = "Madonna;Rammstein";
            result = Main.execute("GET_ARTISTS_FOR_TAG Artista");
            Assertions.assertEquals(expected,result.result);
        }
    }


    //Teste Query GET_TOP_ARTISTS_WITH_SONGS_BETWEEN
    @Test
    public void test_GET_TOP_ARTISTS_WITH_SONGS_BETWEEN(){
        if (Main.loadFiles(new File("test-files"))){
            var expected = "Madonna 2\nRammstein 2";
            QueryResult result = Main.execute("GET_TOP_ARTISTS_WITH_SONGS_BETWEEN 2 2 2");
            Assertions.assertEquals(expected,result.result);
            expected = "No results";
            result = Main.execute("GET_TOP_ARTISTS_WITH_SONGS_BETWEEN 2 3 3");
            Assertions.assertEquals(expected,result.result);
        }
    }


    //Teste Query COUNT_DUPLICATE_SONGS_YEAR
    @Test
    public void test_COUNT_DUPLICATE_SONGS_YEAR(){
        if (Main.loadFiles(new File("test-files"))){
            var expected = "0";
            QueryResult result = Main.execute("COUNT_DUPLICATE_SONGS_YEAR 2001");
            Assertions.assertEquals(expected,result.result);
            expected = "0";
            result = Main.execute("COUNT_DUPLICATE_SONGS_YEAR 1986");
            Assertions.assertEquals(expected,result.result);
        }
    }


    //Teste Query GET_ARTISTS_ONE_SONG
    @Test
    public void test_GET_ARTISTS_ONE_SONG(){
        if (Main.loadFiles(new File("test-files"))){
            var expected = "Madonna | Ich bill | 2001\nRammstein | Ich will | 2001";
            QueryResult result = Main.execute("GET_ARTISTS_ONE_SONG 2000 2001");
            Assertions.assertEquals(expected,result.result);
            expected = "Rammstein | Heirate mich | 2002";
            result = Main.execute("GET_ARTISTS_ONE_SONG 2002 2003");
            Assertions.assertEquals(expected,result.result);
        }
    }


    //Teste Query MOST_FREQUENT_WORDS_IN_ARTIST_NAME
    @Test
    public void test_MOST_FREQUENT_WORDS_IN_ARTIST_NAME(){
        if (Main.loadFiles(new File("test-files"))){
            var expected = "Madonna 1\nRammstein 1";
            QueryResult result = Main.execute("MOST_FREQUENT_WORDS_IN_ARTIST_NAME 2 2 2");
            Assertions.assertEquals(expected,result.result);
            expected = "Rammstein 1";
            result = Main.execute("MOST_FREQUENT_WORDS_IN_ARTIST_NAME 1 2 5");
            Assertions.assertEquals(expected,result.result);
        }
    }

    //Teste Query GET_UNIQUE_TAGS
    @Test
    public void test_GET_UNIQUE_TAGS(){
        if (Main.loadFiles(new File("test-files"))){
            var expected = "ARTISTA 1";
            QueryResult result = Main.execute("ADD_TAGS Rammstein;Artista");
            result = Main.execute("GET_UNIQUE_TAGS Artista");
            Assertions.assertEquals(expected,result.result);
            result = Main.execute("ADD_TAGS Madonna;Artista");
            expected = "ARTISTA 2";
            result = Main.execute("GET_UNIQUE_TAGS Artista");
            Assertions.assertEquals(expected,result.result);
        }
    }
    @Test
    public void test_REMOVE_TAGS_And_GET_ARTISTS_FOR_TAG(){
        if (Main.loadFiles(new File("test-files"))){
            var expected = "Rammstein | CANTOR";
            QueryResult result = Main.execute("ADD_TAGS Rammstein;Artista");
            result = Main.execute("ADD_TAGS Rammstein;Cantor");
            result = Main.execute("REMOVE_TAGS Rammstein;Artista");
            Assertions.assertEquals(expected,result.result);
            expected = "No results";
            result = Main.execute("GET_ARTISTS_FOR_TAG Artista");
            Assertions.assertEquals(expected,result.result);
        }
    }


    //Teste Error files
    @Test
    public void test_ERROR_FILES(){
        Assertions.assertEquals(false,Main.loadFiles(new File("test-files/ErrorFiles")));
    }

}