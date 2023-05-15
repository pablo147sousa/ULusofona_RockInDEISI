package pt.ulusofona.aed.rockindeisi2023;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.Scanner;

public class TestXXX
{
    //Leitura dos 3 ficheiros (sem erros) contendo pelo menos 2 músicas e 2 artistas
    @Test
    public void test3FicheirosSemErros(){
        Main.loadFiles(new File("test-files"));
        Main.getObjects(TipoEntidade.TEMA);
        boolean arquivosOk = false;
        for (FileInputResult fileInputResult : Main.fileInputResults) {
            if (fileInputResult.linhasNOK==0 && Main.songArrayProcessado.size()>=2){
                for (Songs songs : Main.songArrayProcessado) {
                    if (songs.numArtistas>=2){
                        arquivosOk = true;
                    }
                }
            }
        }
        System.out.println(arquivosOk);
        assert arquivosOk;
    }
    //Leitura dos 3 ficheiros com erros (linhas com elementos em falta)
    @Test
    public void test3FicheirosComErros(){
        Main.loadFiles(new File("test-files"));
        boolean arquivosOk = true;
        for (FileInputResult fileInputResult : Main.fileInputResults) {
            if (fileInputResult.linhasNOK!=0){
                arquivosOk = false;
            }
        }
        System.out.println(arquivosOk);
        assert !arquivosOk;
    }

    //Conversão para String de objetos contendo informação de músicas anteriores a 1995
    @Test
    public void musicasAnteriores1995(){
        Main.loadFiles(new File("test-files"));
        Main.getObjects(TipoEntidade.TEMA);
        String textoEsperado = "";
        for (Songs songs : Main.songArrayProcessado) {
            if (songs.anoLancamento<1995){
                textoEsperado += songs.toString();
            }
        }
        assert !textoEsperado.equals("");

    }
    //Conversão para String de objetos contendo informação de músicas anteriores a 2000
    @Test
    public void musicasAnteriores2000(){
        Main.loadFiles(new File("test-files"));
        Main.getObjects(TipoEntidade.TEMA);
        String textoEsperado = "";
        for (Songs songs : Main.songArrayProcessado) {
            if (songs.anoLancamento>=1995 && songs.anoLancamento<2000){
                textoEsperado += songs.toString();
            }
        }
        assert !textoEsperado.equals("");
    }
    //Conversão para String de objetos contendo informação de músicas superiores a 2000
    @Test
    public void musicasSuperiores2000(){
        Main.loadFiles(new File("test-files"));
        Main.getObjects(TipoEntidade.TEMA);
        String textoEsperado = "";
        for (Songs songs : Main.songArrayProcessado) {
            if (songs.anoLancamento>2000){
                textoEsperado += songs.toString();
            }
        }
        assert !textoEsperado.equals("");
    }

    //Conversão para String de objetos contendo informação de artistas
    @Test
    public void stringArtistas(){
        Main.loadFiles(new File("test-files"));
        Main.getObjects(TipoEntidade.ARTISTA);
        String textoEsperado = "";
        for (Artists artists : Main.artistsArray) {
            if (artists != null){
                textoEsperado += artists.toString();
            }
        }
        assert !textoEsperado.equals("");
    }

}
