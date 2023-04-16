package pt.ulusofona.aed.rockindeisi2023;

public class Songs {
    String idTemaMusical, nome;
    int anoLancamento, duracao;
    Integer popularidade;
    Artists artista = new Artists();

    Details detalhes = new Details();


    public Songs(String idTemaMusical, String nome, Integer anoLancamento) {
        this.idTemaMusical = idTemaMusical;
        this.nome = nome;
        this.anoLancamento = anoLancamento;
    }

    @Override
    public String toString() {

        if(anoLancamento < 1995) {
            return idTemaMusical + " | " + nome + " | " + anoLancamento;
        }
        if(anoLancamento < 2000){
            return idTemaMusical + " | " + nome + " | " + anoLancamento + " | " + duracao + " | " + popularidade;
        }
        return idTemaMusical + " | " + nome + " | " + anoLancamento + " | " + detalhes.duracao + " | " + detalhes.popularidade + " | " + artista.artista;
    }
}
