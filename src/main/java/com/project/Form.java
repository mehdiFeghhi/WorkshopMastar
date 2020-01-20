package com.project;

import java.io.Serializable;
import java.util.ArrayList;

public class Form implements Serializable {

    AbsForm absForm;
    HoldWorkShop holdWorkShop;
    public Form(AbsForm absForm, HoldWorkShop holdWorkShop) {
        this.absForm = absForm;
        this.holdWorkShop = holdWorkShop;
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
