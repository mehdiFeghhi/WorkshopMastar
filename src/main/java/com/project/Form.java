package com.project;

import java.io.Serializable;
import java.util.ArrayList;

public class Form implements Serializable {
    int id_Form;
    AbsForm absForm;
    HoldWorkShop holdWorkShop;
    public Form(AbsForm absForm, HoldWorkShop holdWorkShop) {
        this.absForm = absForm;
        this.holdWorkShop = holdWorkShop;
    }

    public Form() {

    }

    public int getId_Form() {
        return id_Form;
    }

    public void setId_Form(int id_Form) {
        this.id_Form = id_Form;
    }

    public AbsForm getAbsForm() {
        return absForm;
    }

    public HoldWorkShop getHoldWorkShop() {
        return holdWorkShop;
    }

    public void setAbsForm(AbsForm absForm) {
        this.absForm = absForm;
    }

    public void setHoldWorkShop(HoldWorkShop holdWorkShop) {
        this.holdWorkShop = holdWorkShop;
    }
}
