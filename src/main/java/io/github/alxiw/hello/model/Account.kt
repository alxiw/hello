package io.github.alxiw.hello.model;

public class Account {

    private int id;
    private String uin;
    private String name;

    public Account(int id, String uin, String name) {
        this.id = id;
        this.uin = uin;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getUin() {
        return uin;
    }

    public String getName() {
        return name;
    }
}
