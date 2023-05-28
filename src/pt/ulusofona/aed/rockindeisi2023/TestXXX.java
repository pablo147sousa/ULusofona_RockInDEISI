package pt.ulusofona.aed.rockindeisi2023;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.File;

public class TestXXX
{
    //Leitura dos 3 ficheiros (sem erros) contendo pelo menos 2 músicas e 2 artistas

    @Test
    public void parseMultipleArtists() {
        Assertions.assertArrayEquals(new String[] { "aaa" }, Main.parseMultipleArtists("['aaa']").toArray());
        Assertions.assertArrayEquals(new String[] { "aaa", "bbb"}, Main.parseMultipleArtists("['aaa', 'bbb']").toArray());
        Assertions.assertArrayEquals(new String[] { "a,aa", "bbb"}, Main.parseMultipleArtists("['a,aa', 'bbb']").toArray());
        Assertions.assertArrayEquals(new String[] { "aaa", "bb b"}, Main.parseMultipleArtists("['aaa', 'bb b']").toArray());
        Assertions.assertArrayEquals(new String[] { "a'aa", "bbb"}, Main.parseMultipleArtists("[\"\"a'aa\"\", 'bbb']").toArray());
        Assertions.assertArrayEquals(new String[] { "a' aa", "b, bb", "ccc"}, Main.parseMultipleArtists("[\"\"a' aa\"\", 'b, bb', 'ccc']").toArray());
    }
    @Test
    public void medirTempos()
    {

        long tempoInicial = System.currentTimeMillis();
        Main.loadFiles(new File("teste_files"));
        long tempoFinal = System.currentTimeMillis();
        long resultado = tempoFinal-tempoInicial;
        System.out.println(resultado);
    }
}
