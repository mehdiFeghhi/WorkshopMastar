package com.project;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;

public class HeldWorkshop_DB
{
    public int HeldWorkshop_id;
    public int Management_Id;
    public int Workshop_ID;
    public String Start;
    public String End;
    public String hourStart;
    public String hourEnd;
    public String Name;
    public int is_installment;
    public int money;
    public int payMoneyInHowTimes;
    public int deleted;

	public HeldWorkshop_DB(int heldWorkshop_id, int management_id, int workshop_id, String name, String start, String end, String hourend, String hourStart, int is_installment, int pay_in_howmany_times, int deleted) {
		this.HeldWorkshop_id = heldWorkshop_id;
		this.Management_Id = management_id;
		this.Workshop_ID = workshop_id;
		this.Name = name;
		this.Start = start;
		this.End = end;
		this.is_installment = is_installment;
		this.hourEnd = hourend;
		this.hourEnd = hourStart;
		this.payMoneyInHowTimes = pay_in_howmany_times;
		this.deleted  = deleted;
	}

	public HoldWorkShop makeHoldWorkShop() throws ParseException {
    	DataBaseConnection dataBaseConnection = new DataBaseConnection();
		SimpleDateFormat formatter5=new SimpleDateFormat("E, MMM dd yyyy HH:mm:ss");
		HoldWorkShop holdWorkShop = new HoldWorkShop();
    	holdWorkShop.setEnd(formatter5.parse(this.End));
    	holdWorkShop.setStart(formatter5.parse(this.Start));
    	holdWorkShop.setId(this.HeldWorkshop_id);
    	holdWorkShop.setMoney((long) this.money);
    	if ( is_installment== 0){
    		holdWorkShop.setIs_installment(false);
		}
    	else if ( is_installment == 1){
    		holdWorkShop.setIs_installment(true);
		}
    	holdWorkShop.setName(this.Name);
		holdWorkShop.setHourStart(LocalTime.parse(this.hourStart));
		holdWorkShop.setHourEnd(LocalTime.parse(this.hourEnd));
		holdWorkShop.setPayMoneyInHowTimes(this.payMoneyInHowTimes);
		//get mangement of this id
		Managment managment = new Managment();
		//get mangement of this id
		holdWorkShop.setManagment(managment);
		Workshop workshop = new Workshop();
		//get workshop of this id
		holdWorkShop.setWorkshop(workshop);
		return holdWorkShop;
	}
}
