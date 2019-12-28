package com.project;

public class Installment extends Pay {
    private int how_much_installment_must_pay;
    public Installment(long moneyMustPayment,int how_much_installment_must_pay) {
        super(moneyMustPayment,true);
        this.how_much_installment_must_pay = how_much_installment_must_pay;

    }
    public int getHow_much_installment_must_pay() {
        return how_much_installment_must_pay;
    }

    public void setHow_much_installment_must_pay(int how_much_installment_must_pay) {
        this.how_much_installment_must_pay = how_much_installment_must_pay;
    }

    public boolean decreseInstallment(int mount){
        if (this.getHow_much_installment_must_pay() < mount)
            return false;
        else{
            this.setHow_much_installment_must_pay(this.getHow_much_installment_must_pay()-mount);
            if (this.getHow_much_installment_must_pay() == 0)
                super.ChangePayComplite();
            return true;

        }

    }

}
