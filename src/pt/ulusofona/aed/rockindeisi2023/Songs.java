package pt.ulusofona.aed.rockindeisi2023;

import java.util.ArrayList;

public class Songs {
    String idTemaMusical, nome;
    Integer anoLancamento;
    Details detalhes;


    ArrayList<String> artistsName = new ArrayList<String>();
    public Songs(String idTemaMusical, String nome, Integer anoLancamento) {
        this.idTemaMusical = idTemaMusical;
        this.nome = nome;
        this.anoLancamento = anoLancamento;
    }
    public static String converteDuracao(long millis) {
        int seconds = (int) (millis / 1000) % 60;
        int minutes = (int) ((millis / (1000*60)) % 60);
        return String.format("%d:%02d", minutes, seconds);
    }
    @Override
    public String toString() {
        String texto = idTemaMusical + " | " + nome + " | " + anoLancamento;
        if(anoLancamento>= 1995 && anoLancamento < 2000){
            texto += " | " + converteDuracao(detalhes.duracao)  + " | " + detalhes.popularidade;
        }
        if (anoLancamento>=2000) {
            texto += " | " + converteDuracao(detalhes.duracao) + " | " + detalhes.popularidade + " | " + artistsName.size();
        }
        return texto;
    }
    public int getYear() {
        return anoLancamento;
    }

}
