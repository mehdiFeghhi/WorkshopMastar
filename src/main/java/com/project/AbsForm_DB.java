package com.project;

public class AbsForm_DB
{
    public int AbsForm_ID;
    public int number;
    public int deleted;

    public AbsForm_DB(int absForm_ID, int number, int deleted) {
        AbsForm_ID = absForm_ID;
        this.number = number;
        this.deleted = deleted;
    }

    public AbsForm makeAbsForm_DB(){
        AbsForm absForm = new AbsForm();
        absForm.setNumber(this.number);
        return absForm;
    }
}

