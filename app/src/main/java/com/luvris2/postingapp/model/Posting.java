package com.luvris2.postingapp.model;

import java.io.Serializable;

public class Posting implements Serializable {
    public String title, content;
    public int id, userId;

    public Posting(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Posting(int id, int userId, String title, String content) {
        this.title = title;
        this.content = content;
        this.id = id;
        this.userId = userId;


    }
}

