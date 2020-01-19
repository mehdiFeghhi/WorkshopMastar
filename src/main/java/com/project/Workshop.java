package com.project;

import java.io.Serializable;

public class Workshop implements Serializable {
    private int id;
    private String title;
    private String description;
    public Workshop(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        if (title == null)
            return "unkown";
        return title;
    }

    public String getDescription() {
        if (description == null)
            return "unkwon";
        return description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
