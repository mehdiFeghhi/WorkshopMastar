package com.project;

import org.omg.CORBA.Request;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Workshop implements Serializable {
    private int id;
    private String Title;
    private String description;
    public Workshop(String title, String description) {
        Title = title;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        if (Title == null)
            return "unkown";
        return Title;
    }

    public String getDescription() {
        if (description == null)
            return "unkwon";
        return description;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
