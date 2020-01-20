package com.project;

import java.io.Serializable;
import java.util.ArrayList;

public class AbsForm implements Serializable {
    int number;
    ArrayList<String> question = new ArrayList<String>();

    public AbsForm(ArrayList<String> question) {
        this.question = question;
    }

    public AbsForm() {

    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public ArrayList<String> getQuestion() {
        return question;
    }

    public void setQuestion(ArrayList<String> question) {
        this.question = question;
    }

}