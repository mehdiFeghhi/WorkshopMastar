package com.project;

import java.util.Date;

public class ValidationProperty {
    Date date;
    String user;
    String token;

    public ValidationProperty(Date date, String user, String token) {
        this.date = date;
        this.user = user;
        this.token = token;
    }
}
