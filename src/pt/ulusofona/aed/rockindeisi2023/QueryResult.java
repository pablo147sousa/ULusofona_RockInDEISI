package pt.ulusofona.aed.rockindeisi2023;

public class QueryResult
{
    String result;
    Long time;

    public QueryResult() {
        result = "";
    }

    public QueryResult(String result, Long time) {
        this.result = result;
        this.time = time;
    }
}
