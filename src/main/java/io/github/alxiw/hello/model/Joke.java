package io.github.alxiw.hello.model;

public class Joke {

    private int id;
    private String original;
    private String russian;
    private String interpretation;

    public Joke(int id, String original, String russian, String interpretation) {
        this.id = id;
        this.original = original;
        this.russian = russian;
        this.interpretation = interpretation;
    }

    public int getId() {
        return id;
    }

    public String getOriginal() {
        return original;
    }

    public String getRussian() {
        return russian;
    }

    public String getInterpretation() {
        return interpretation;
    }
}
