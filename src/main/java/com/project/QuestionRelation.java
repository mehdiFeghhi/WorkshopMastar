package com.project;

public class QuestionRelation {
            int id_question;
            String question;
            AbsForm absForm;

    public QuestionRelation(int id_question, String question, AbsForm absForm) {
        this.id_question = id_question;
        this.question = question;
        this.absForm = absForm;
    }

    public int getId_question() {
        return id_question;
    }

    public String getQuestion() {
        return question;
    }

    public AbsForm getAbsForm() {
        return absForm;
    }

    public void setId_question(int id_question) {
        this.id_question = id_question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAbsForm(AbsForm absForm) {
        this.absForm = absForm;
    }
}
