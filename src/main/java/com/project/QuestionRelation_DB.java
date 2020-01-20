package com.project;

public class QuestionRelation_DB
{
    public int QuestionRelation_ID;
    public String question;
    int absForm_ID;
    int Deleted;

    public QuestionRelation_DB(int questionRelation_ID, String question, int absForm_ID, int deleted) {
        QuestionRelation_ID = questionRelation_ID;
        this.question = question;
        this.absForm_ID = absForm_ID;
        Deleted = deleted;
    }
}
