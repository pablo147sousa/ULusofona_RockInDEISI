package pt.ulusofona.aed.rockindeisi2023;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.ArrayList;

public class TestXXX {
    @Test
    public void parseMultipleArtists() {
        Assertions.assertArrayEquals(new String[]{"aaa"}, Main.parseMultipleArtists("['aaa']").toArray());
        Assertions.assertArrayEquals(new String[]{"aaa", "bbb"}, Main.parseMultipleArtists("['aaa', 'bbb']").toArray());
        Assertions.assertArrayEquals(new String[]{"a,aa", "bbb"}, Main.parseMultipleArtists("['a,aa', 'bbb']").toArray());
        Assertions.assertArrayEquals(new String[]{"aaa", "bb b"}, Main.parseMultipleArtists("['aaa', 'bb b']").toArray());
        Assertions.assertArrayEquals(new String[]{"a'aa", "bbb"}, Main.parseMultipleArtists("[\"\"a'aa\"\", 'bbb']").toArray());
        Assertions.assertArrayEquals(new String[]{"a' aa", "b, bb", "ccc"}, Main.parseMultipleArtists("[\"\"a' aa\"\", 'b, bb', 'ccc']").toArray());
    }
}