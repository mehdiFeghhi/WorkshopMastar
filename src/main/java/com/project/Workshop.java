package com.project;

import org.omg.CORBA.Request;

import java.util.ArrayList;
import java.util.Date;

public class Workshop {
    private String Title;
    private String description;

    public Workshop(String title, String description) {
        Title = title;
        this.description = description;
    }

    public String getTitle() {
        return Title;
    }

    public String getDescription() {
        return description;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
