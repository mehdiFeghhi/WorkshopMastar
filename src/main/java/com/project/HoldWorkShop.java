package com.project;

import javax.xml.crypto.Data;
import java.time.LocalTime;
import java.util.Date;

public class HoldWorkShop {
    private Date Start;
    private Date End;
    private LocalTime hourStart;
    private LocalTime hourEnd;
    private String Name;
    private int id;
    private Managment managment;
    private Workshop workshop;
    private Boolean is_installment;
    private Long money;
    private int payMoneyInHowTimes;

    public HoldWorkShop(Date start, Date end, LocalTime hourStart, LocalTime hourEnd, String name,Managment managment,Workshop workshop,Boolean is_installment,Long Money) {
        Start = start;
        End = end;
        this.hourStart = hourStart;
        this.hourEnd = hourEnd;
        Name = name;
        this.managment = managment;
        this.workshop = workshop;
        this.is_installment = is_installment;
        this.money = money;
    }

    public HoldWorkShop(LocalTime hourStrart, LocalTime hourEnd, Date start, Date end, String name, int id, Managment managment, Workshop workshop, Boolean is_installment, Long Money) {
        this.hourEnd = hourEnd;
        this.hourStart = hourStrart;
        Start = start;
        End = end;
        Name = name;
        this.id = id;
        this.managment = managment;
        this.workshop = workshop;
        this.is_installment = is_installment;
        this.money = Money;
    }

    public LocalTime getHourStart() {
        return hourStart;
    }

    public LocalTime getHourEnd() {
        return hourEnd;
    }

    public void setHourStart(LocalTime hourStart) {
        this.hourStart = hourStart;
    }

    public void setHourEnd(LocalTime hourEnd) {
        this.hourEnd = hourEnd;
    }

    public int getPayMoneyInHowTimes() {
        return payMoneyInHowTimes;
    }

    public void setPayMoneyInHowTimes(int payMoneyInHowTimes) {
        this.payMoneyInHowTimes = payMoneyInHowTimes;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    public Boolean getIs_installment() {
        return is_installment;
    }

    public void setIs_installment(Boolean is_installment) {
        this.is_installment = is_installment;
    }

    public void setStart(Date start) {
        Start = start;
    }

    public void setEnd(Date end) {
        End = end;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setManagment(Managment managment) {
        this.managment = managment;
    }

    public void setWorkshop(Workshop workshop) {
        this.workshop = workshop;
    }

    public Date getStart() {
        return Start;
    }

    public Date getEnd() {
        return End;
    }

    public String getName() {
        return Name;
    }

    public int getId() {
        return id;
    }

    public Managment getManagment() {
        return managment;
    }

    public Workshop getWorkshop() {
        return workshop;
    }
}
