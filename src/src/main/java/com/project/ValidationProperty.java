package com.project;

import java.io.Serializable;
import java.util.Date;

public class ValidationProperty implements Serializable {
    Date date;
    String user;
    String token;

    public ValidationProperty(Date date, String user, String token) {
        this.date = date;
        this.user = user;
        this.token = token;
    }
}
