package com.project;

import org.apache.commons.math3.analysis.function.Abs;

import java.util.ArrayList;

public class Form_DB
{
	public int Form_Id;
    public int absForm_ID;
    public int HeldWorkShop_ID;
    public int deleted;

    public Form_DB(int form_Id, int absForm_ID, int heldWorkShop_ID, int deleted) {
        Form_Id = form_Id;
        this.absForm_ID = absForm_ID;
        HeldWorkShop_ID = heldWorkShop_ID;
        this.deleted = deleted;
    }
    public Form makeForm(){
        DataBaseConnection dataBaseConnection = new DataBaseConnection();
        Form form  = new Form();
        form.id_Form = this.Form_Id;
        AbsForm absForm = new AbsForm();
        ///must find from data bas
        form.setAbsForm(absForm);
        HoldWorkShop holdWorkShop= new HoldWorkShop();
        ///must find in data base holdwork shop
        form.setHoldWorkShop(holdWorkShop);
        return form;
    }
}
