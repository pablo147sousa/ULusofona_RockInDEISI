package pt.ulusofona.aed.rockindeisi2023;

public class Songs {
    String idTemaMusical, nome;
    int anoLancamento, numArtistas = 0;

    Details detalhes;



    public Songs(String idTemaMusical, String nome, int anoLancamento) {
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
            texto += " | " + converteDuracao(detalhes.duracao) + " | " + detalhes.popularidade + " | " + numArtistas;
        }
        return texto;
    }
}
