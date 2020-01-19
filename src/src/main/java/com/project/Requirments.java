package com.project;

import java.io.Serializable;

public class Requirments implements Serializable {
    Workshop workShopNeed;
    Workshop mainWorkshop;
    Relation relation;

    public Requirments(Workshop workShopNeed, Workshop mainWorkshop, Relation relation) {
        this.workShopNeed = workShopNeed;
        this.mainWorkshop = mainWorkshop;
        this.relation = relation;
    }

    public Workshop getWorkShopNeed() {
        return workShopNeed;
    }

    public Workshop getMainWorkshop() {
        return mainWorkshop;
    }

    public Relation getRelation() {
        return relation;
    }

    public void setWorkShopNeed(Workshop workShopNeed) {
        this.workShopNeed = workShopNeed;
    }

    public void setMainWorkshop(Workshop mainWorkshop) {
        this.mainWorkshop = mainWorkshop;
    }

    public void setRelation(Relation relation) {
        this.relation = relation;
    }
}
