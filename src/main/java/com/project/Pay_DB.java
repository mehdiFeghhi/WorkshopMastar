package com.project;

public class Pay_DB
{
    public int Pay_Id;
    public int moneyMustPay;
    public int payComplete;
    public int canBeImpliment;
    public int is_installment;
    public int how_much_installment_must_pay;

    public Pay_DB(int pay_Id, int moneyMustPay, int payComplete, int canBeImpliment, int is_installment, int how_much_installment_must_pay) {
        Pay_Id = pay_Id;
        this.moneyMustPay = moneyMustPay;
        this.payComplete = payComplete;
        this.canBeImpliment = canBeImpliment;
        this.is_installment = is_installment;
        this.how_much_installment_must_pay = how_much_installment_must_pay;
    }

}
