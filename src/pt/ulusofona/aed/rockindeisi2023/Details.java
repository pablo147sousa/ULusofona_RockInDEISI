package pt.ulusofona.aed.rockindeisi2023;

public class Details {
    String idTemaMusical;
    int duracao, popularidade, letraExplicita;
    double dancabilidade, vivacidade, volume;

    public Details(String idTemaMusical, int duracao, int letraExplicita, int popularidade, double dancabilidade, double vivacidade, double volume) {
        this.idTemaMusical = idTemaMusical;
        this.duracao = duracao;
        this.popularidade = popularidade;
        this.letraExplicita = letraExplicita;
        this.dancabilidade = dancabilidade;
        this.vivacidade = vivacidade;
        this.volume = volume;
    }

    public Details(int duracao, int popularidade) {
        this.duracao = duracao;
        this.popularidade = popularidade;
    }

    @Override
    public String toString() {
        return "Details{" +
                "idTemaMusical='" + idTemaMusical + '\'' +
                ", duracao=" + duracao +
                ", popularidade=" + popularidade +
                ", letraExplicita=" + letraExplicita +
                ", dancabilidade=" + dancabilidade +
                ", vivacidade=" + vivacidade +
                ", volume=" + volume +
                '}';
    }
}
