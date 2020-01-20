package com.project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DataBaseConnection 
{
	Connection c = null;
	Statement stm = null;
	
	public DataBaseConnection ()
	{
		try 
		{
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:DataBase.db");
			System.out.println("yaaayyy, connected");
			
		}
		catch (Exception e)
		{
			System.out.println("fail while creating connection");
		}
	}
	public void CloseConnection()
	{
		try 
		{
			c.close();
			System.out.println("connection closed");
		}
		catch (Exception e)
		{
			System.out.println("fail to close the connection");
			System.out.println(e.toString());
		}
	}
	public void Get_Group_ALL()
	{
		try
		{
		stm = c.createStatement();
		ResultSet rs = stm.executeQuery("SELECT * FROM  \"GroupG\"");
		while (rs.next())
		{
			int gid = rs.getInt("Group_ID");
			String name = rs.getString("Name");
			String head = rs.getString("Head");
			int number = rs.getInt("Number");
			int workshopid = rs.getInt("HeldWorkshop_ID");
			
			//construct new object and put it in array
			System.out.println(gid + "    "+ name+ "   "+ head + "  " + number+ "   "+ workshopid);
		}
		}
		catch (Exception e)
		{
			System.out.println("fail while getting all groups");
			System.out.println(e.toString());
		}
	}
	public void Add_Group(String name, String head,int number,int heldworkshop_ID)
	{
		try
		{
			stm = c.createStatement();
			if(heldworkshop_ID==-1)
			{
				stm.executeQuery("INSERT INTO \"GroupG\" (Name,Head,Number,HeldWorkshop_ID) VALUES ('"+name+"','"+head+"',"+number+",NULL)");
			}
			else
			{
			stm.executeQuery("INSERT INTO \"GroupG\" (Name,Head,Number,HeldWorkshop_ID) VALUES ('"+name+"','"+head+"',"+number+","+heldworkshop_ID+")");
			}
		}
		catch (Exception e)
		{
			System.out.println("fail while adding object to group");
			System.out.println(e.toString());
		}
	}
	public void Update_Group(int Group_ID,String name, String head,int number,int heldworkshop_ID)
	{
		try
		{
			stm = c.createStatement();
			
			stm.executeQuery("UPDATE \"GroupG\" " +
					"   SET Name= '"+name+"', Head = '"+head+"', Number = "+number+", HeldWorkshop_ID=" +heldworkshop_ID+" " + 
					" WHERE (Group_ID = "+Group_ID+")");
			
		}
		catch (Exception e)
		{
			System.out.println("fail while updating object to group");
			System.out.println(e.toString());
		}
	}
	public void Get_AbsForm_ALL()
	{
		try
		{
		stm = c.createStatement();
		ResultSet rs = stm.executeQuery("SELECT * FROM  \"Absform\"");
		while (rs.next())
		{
			int Abs_id = rs.getInt("AbsformID");
			int number = rs.getInt("number");
			int deleted = rs.getInt("deleted");
			
			//construct new object and put it in array
			System.out.println(Abs_id + "    "+ number + "   "+ deleted );
		}
		}
		catch (Exception e)
		{
			System.out.println("fail while getting all abs_forms");
			System.out.println(e.toString());
		}
	}
	public void Add_AbsForm(int number, int deleted)
	{
		try
		{
			stm = c.createStatement();

			stm.executeQuery("INSERT INTO \"Absform\" (number,deleted) VALUES ('" + number +"','"+deleted+"'");
		}
		catch (Exception e)
		{
			System.out.println("fail while adding object to abs_form");
			System.out.println(e.toString());
		}
	}
	public void Update_AbsForm(int Abs_id,int number, int deleted)
	{
		try
		{
			stm = c.createStatement();
			
			stm.executeQuery("UPDATE \"Absform\" SET number = '"+number+"', deleted = '"+deleted+"' WHERE (AbsformID="+ Abs_id +")");
			
		}
		catch (Exception e)
		{
			System.out.println("fail while updating object to abs_form");
			System.out.println(e.toString());
		}
	}
	
	//admin now
	public void Get_Admin_ALL()
	{
		try
		{
		stm = c.createStatement();
		ResultSet rs = stm.executeQuery("SELECT * FROM  \"Admin\"");
		while (rs.next())
		{
			int Admin_id = rs.getInt("Admin_ID");
			int Role_of_Workshop_ID = rs.getInt("Role_of_Workshop_ID");
			int type = rs.getInt("type");
			int deleted = rs.getInt("Deleted");
			
			//construct new object and put it in array
			System.out.println(Admin_id + "    "+ Role_of_Workshop_ID + "    "+ type + "   "+ deleted );
		}
		}
		catch (Exception e)
		{
			System.out.println("fail while getting all Admins");
			System.out.println(e.toString());
		}
	}
	public Admin_DB Get_Admin(int id)
	{
		try
		{
		stm = c.createStatement();
		ResultSet rs = stm.executeQuery("SELECT * from \"Admin\" where Admin_ID = "+id);
		while (rs.next()) {
			int Admin_id = rs.getInt("Admin_ID");
			int Role_of_Workshop_ID = rs.getInt("Role_of_Workshop_ID");
			int type = rs.getInt("type");
			int deleted = rs.getInt("Deleted");
			
			//construct new object and put it in array
			System.out.println(Admin_id + "    "+ Role_of_Workshop_ID + "    "+ type + "   "+ deleted );
			return new Admin_DB(Admin_id,Role_of_Workshop_ID,type,deleted);
			}
		}
		catch (Exception e)
		{
			System.out.println("fail while getting  Admin");
			System.out.println(e.toString());
		}
		return null;
	}
	public void Add_Admin(int Role_of_Workshop_ID, int type, int deleted)
	{
		try
		{
			stm = c.createStatement();

			stm.executeQuery("INSERT INTO \"Admin\" (Role_of_Workshop_ID,type,Deleted) VALUES ('" + Role_of_Workshop_ID +"','"+type+"','"+deleted+"')");
		}
		catch (Exception e)
		{
			System.out.println("fail while adding object to Admin");
			System.out.println(e.toString());
		}
	}
	public void Update_Admin(int Admin_ID,int Role_of_Workshop_ID,int type, int deleted)
	{
		try
		{
			stm = c.createStatement();
			
			stm.executeQuery("UPDATE \"Admin\" SET Role_of_Workshop_ID = "+Role_of_Workshop_ID+" type = '"+type+"', Deleted = '"+deleted+"' WHERE (Admin_ID="+ Admin_ID +")");
			
		}
		catch (Exception e)
		{
			System.out.println("fail while updating object to Admin");
			System.out.println(e.toString());
		}
	}
	//form
	
	public void Get_Form_ALL()
	{
		try
		{
		stm = c.createStatement();
		ResultSet rs = stm.executeQuery("SELECT * FROM  \"Form\"");
		while (rs.next())
		{
			int Form_ID = rs.getInt("Form_ID");
			int AbsForm_ID = rs.getInt("Absform_ID");
			int HeldWorkshop_ID = rs.getInt("HeldWorkshop_ID");
			int deleted = rs.getInt("Deleted");
			
			//construct new object and put it in array
			System.out.println(Form_ID + "    "+ AbsForm_ID + "   "+ HeldWorkshop_ID+ "   "+ deleted );
		}
		}
		catch (Exception e)
		{
			System.out.println("fail while getting all Forms");
			System.out.println(e.toString());
		}
	}
	public void Get_Form(int form_ID)
	{
		try
		{
		stm = c.createStatement();
		ResultSet rs = stm.executeQuery("SELECT * FROM  \"Form\" where Form_ID = "+form_ID);
		while (rs.next())
		{
			int Form_ID = rs.getInt("Form_ID");
			int AbsForm_ID = rs.getInt("Absform_ID");
			int HeldWorkshop_ID = rs.getInt("HeldWorkshop_ID");
			int deleted = rs.getInt("Deleted");
			
			//construct new object and put it in array
			System.out.println(Form_ID + "    "+ AbsForm_ID + "   "+ HeldWorkshop_ID+ "   "+ deleted );
		}
		}
		catch (Exception e)
		{
			System.out.println("fail while getting all Forms");
			System.out.println(e.toString());
		}
	}
	public void Add_Form(int Absform_ID, int HeldWorkshop_ID, int deleted)
	{
		try
		{
			stm = c.createStatement();

			stm.executeQuery("INSERT INTO \"Form\" (Absform_ID,HeldWorkshop_ID,Deleted) VALUES (" + Absform_ID +","+HeldWorkshop_ID+","+deleted+")");
		}
		catch (Exception e)
		{
			System.out.println("fail while adding object to Form");
			System.out.println(e.toString());
		}
	}
	public void Update_Form(int Form_ID,int Absform_ID, int HeldWorkshop_ID, int deleted)
	{
		try
		{
			stm = c.createStatement();
			
			stm.executeQuery("UPDATE \"Form\" SET Absform_ID = "+Absform_ID+", HeldWorkshop_ID = "+HeldWorkshop_ID+", Deleted = "+deleted+" WHERE (Form_ID="+ Form_ID +")");
			
		}
		catch (Exception e)
		{
			System.out.println("fail while updating object to Form");
			System.out.println(e.toString());
		}
	}
	//Grader
	public void Get_Grader_ALL()
	{
		try
		{
		stm = c.createStatement();
		ResultSet rs = stm.executeQuery("SELECT * FROM  \"Grader\"");
		while (rs.next())
		{
			int Grader_ID = rs.getInt("Grader_ID");
			int Role_of_Workshop_ID = rs.getInt("Role_of_Workshop_ID");
			int Deleted = rs.getInt("Deleted");
			
			//construct new object and put it in array
			System.out.println(Grader_ID + "    "+ Role_of_Workshop_ID+ "    "+Deleted);
		}
		}
		catch (Exception e)
		{
			System.out.println("fail while getting all Graders");
			System.out.println(e.toString());
		}
	}
	public void Get_Grader(int Grader_ID)
	{
		try
		{
		stm = c.createStatement();
		ResultSet rs = stm.executeQuery("SELECT * FROM  \"Grader\" where Grader_ID = "+Grader_ID);
		while (rs.next())
		{
			Grader_ID = rs.getInt("Grader_ID");
			int Role_of_Workshop_ID = rs.getInt("Role_of_Workshop_ID");
			int Deleted = rs.getInt("Deleted");

			//construct new object and put it in array
			System.out.println(Grader_ID + "    "+ Role_of_Workshop_ID+ "    "+Deleted);
		}
		}
		catch (Exception e)
		{
			System.out.println("fail while getting Grader");
			System.out.println(e.toString());
		}
	}
	public void Add_Grader(int Role_of_Workshop_ID,int Deleted)
	{
		try
		{
			stm = c.createStatement();

			stm.executeQuery("INSERT INTO \"Grader\" (Role_of_Workshop_ID,Deleted) VALUES ( "+Role_of_Workshop_ID+","+Deleted+" )");
		}
		catch (Exception e)
		{
			System.out.println("fail while adding object to Grader");
			System.out.println(e.toString());
		}
	}
	public void Update_Grader(int Grader_ID,int Role_of_Workshop_ID,int Deleted)
	{
		try
		{
			stm = c.createStatement();
			
			stm.executeQuery("UPDATE \"Grader\" SET Role_of_Workshop_ID = "+Role_of_Workshop_ID+", Deleted = "+Deleted+" WHERE (Grader_ID="+ Grader_ID +")");
			
		}
		catch (Exception e)
		{
			System.out.println("fail while updating object to Grader");
			System.out.println(e.toString());
		}
	}
	//Grader_Request
	public void Get_Grader_Request_All()
	{
		try
		{
		stm = c.createStatement();
		ResultSet rs = stm.executeQuery("SELECT * FROM  \"Grader_Request\"");
		while (rs.next())
		{
			int Grader_Request_ID = rs.getInt("Grader_Request_ID");
			int Grader_ID = rs.getInt("Grader_ID");
			int Request_ID = rs.getInt("Request_ID");
			int type =  rs.getInt("type");
			int request =  rs.getInt("request");
			int Deleted = rs.getInt("Deleted");
			
			//construct new object and put it in array
			System.out.println(Grader_Request_ID + "    "+ Grader_ID + "    "+ Request_ID + "    "+ type + "    "+ request + "    "+ Deleted);
		}
		}
		catch (Exception e)
		{
			System.out.println("fail while getting all Grader_Request");
			System.out.println(e.toString());
		}
	}
	public void Get_Grader_Request(int Grader_Request_ID)
	{
		try
		{
		stm = c.createStatement();
		ResultSet rs = stm.executeQuery("SELECT * FROM  \"Grader_Request\" Where Grader_Request_ID = "+Grader_Request_ID);
		while (rs.next())
		{
			Grader_Request_ID = rs.getInt("Grader_Request_ID");
			int Grader_ID = rs.getInt("Grader_ID");
			int Request_ID = rs.getInt("Request_ID");
			int type =  rs.getInt("type");
			int request =  rs.getInt("request");
			int Deleted = rs.getInt("Deleted");
			
			//construct new object and put it in array
			System.out.println(Grader_Request_ID + "    "+ Grader_ID + "    "+ Request_ID + "    "+ type + "    "+ request + "    "+ Deleted);
		}
		}
		catch (Exception e)
		{
			System.out.println("fail while getting Grader_Request");
			System.out.println(e.toString());
		}
	}
	public void Add_Grader_Request(int Grader_ID,int Request_ID,int type,int request,int Deleted)
	{
		try
		{
			stm = c.createStatement();

			stm.executeQuery("INSERT INTO \"Grader_Request\" (Grader_ID,Request_ID,type,request,Deleted) VALUES ( "+Grader_ID+" , "+Request_ID+" , "+type+" , "+request+" , "+Deleted+" )");
		}
		catch (Exception e)
		{
			System.out.println("fail while adding object to Grader_Request");
			System.out.println(e.toString());
		}
	}
	public void Update_Grader_Request(int Grader_Request_ID,int Grader_ID,int Request_ID,int type,int request,int Deleted)
	{
		try
		{
			stm = c.createStatement();
			
			stm.executeQuery("UPDATE \"Grader_Request\" SET Grader_ID = "+Grader_ID+", Request_ID = "+Request_ID+", type = "+type+", request = "+request+", Deleted = "+Deleted+" WHERE (Grader_Request_ID="+ Grader_Request_ID +")");
			
		}
		catch (Exception e)
		{
			System.out.println("fail while updating object to Grader_Request");
			System.out.println(e.toString());
		}
	}
	//Group_Status_Attendence
	
	public void Get_Group_Status_Attendence_All()
	{
		try
		{
		stm = c.createStatement();
		ResultSet rs = stm.executeQuery("SELECT * FROM  \"Group_Status_Attendence\"");
		while (rs.next())
		{
			int Group_Status_Attendence_ID = rs.getInt("Group_Status_Attendence_ID");
			int Group_Satus_ID = rs.getInt("Group_Satus_ID");
			int Attendence =  rs.getInt("Attendence");
			int Deleted = rs.getInt("Deleted");
			
			//construct new object and put it in array
			System.out.println(Group_Status_Attendence_ID + "    "+ Group_Satus_ID + "    "+ Attendence +"    "+ Deleted);
		}
		}
		catch (Exception e)
		{
			System.out.println("fail while getting all Group_Status_Attendence");
			System.out.println(e.toString());
		}
	}
	public void Get_Group_Status_Attendence(int Group_Status_Attendence_ID )
	{
		try
		{
		stm = c.createStatement();
		ResultSet rs = stm.executeQuery("SELECT * FROM  \"Group_Status_Attendence\" WHERE Group_Status_Attendence_ID = "+Group_Status_Attendence_ID);
		while (rs.next())
		{
			Group_Status_Attendence_ID = rs.getInt("Group_Status_Attendence_ID");
			int Group_Satus_ID = rs.getInt("Group_Satus_ID");
			int Attendence =  rs.getInt("Attendence");
			int Deleted = rs.getInt("Deleted");
			
			//construct new object and put it in array
			System.out.println(Group_Status_Attendence_ID + "    "+ Group_Satus_ID + "    "+ Attendence +"    "+ Deleted);
		}
		}
		catch (Exception e)
		{
			System.out.println("fail while getting Group_Status_Attendence");
			System.out.println(e.toString());
		}
	}
	public void Add_Group_Status_Attendence(int Group_Satus_ID,int Attendence,int Deleted)
	{
		try
		{
			stm = c.createStatement();

			stm.executeQuery("INSERT INTO \"Group_Status_Attendence\" (Group_Satus_ID , AttendenceDeleted, Deleted) VALUES ( "+Group_Satus_ID+" , "+Attendence+" , "+Deleted+" )");
		}
		catch (Exception e)
		{
			System.out.println("fail while adding object to Group_Status_Attendence");
			System.out.println(e.toString());
		}
	}
	public void Update_Group_Status_Attendence(int Group_Status_Attendence_ID,int Group_Satus_ID,int Attendence,int Deleted)
	{
		try
		{
			stm = c.createStatement();
			
			stm.executeQuery("UPDATE \"Group_Status_Attendence\" SET Group_Satus_ID = "+Group_Satus_ID+", Attendence = "+Attendence+" , Deleted = "+Deleted+" WHERE (Group_Status_Attendence_ID ="+ Group_Status_Attendence_ID +")");
			
		}
		catch (Exception e)
		{
			System.out.println("fail while updating object to Update_Group_Status_Attendence");
			System.out.println(e.toString());
		}
	}
	/*
	 Group_status
	 	int Group_Status_ID
	 	int Group_ID
	 	int Role_of_workshop_ID
	 	int Deleted
	 */
	public void Get_Group_status_All()
	{
		try
		{
		stm = c.createStatement();
		ResultSet rs = stm.executeQuery("SELECT * FROM  \"Group_status\"");
		while (rs.next())
		{
			int Group_Status_ID = rs.getInt("Group_Status_ID");
			int Group_ID = rs.getInt("Group_ID");
			int Role_of_workshop_ID =  rs.getInt("Role_of_workshop_ID");
			int Deleted = rs.getInt("Deleted");
			
			//construct new object and put it in array
			System.out.println(Group_Status_ID + "    "+ Group_ID + "    "+ Role_of_workshop_ID +"    "+ Deleted);
		}
		}
		catch (Exception e)
		{
			System.out.println("fail while getting all Group_status");
			System.out.println(e.toString());
		}
	}
	public void Get_Group_status(int Group_Status_ID )
	{
		try
		{
		stm = c.createStatement();
		ResultSet rs = stm.executeQuery("SELECT * FROM  \"Group_status\" WHERE Group_Status_ID = "+Group_Status_ID);
		while (rs.next())
		{
			Group_Status_ID = rs.getInt("Group_Status_ID");
			int Group_ID = rs.getInt("Group_ID");
			int Role_of_workshop_ID =  rs.getInt("Role_of_workshop_ID");
			int Deleted = rs.getInt("Deleted");
			
			//construct new object and put it in array
			System.out.println(Group_Status_ID + "    "+ Group_ID + "    "+ Role_of_workshop_ID +"    "+ Deleted);
		}
		}
		catch (Exception e)
		{
			System.out.println("fail while getting Group_status");
			System.out.println(e.toString());
		}
	}
	public void Add_Group_status(int Group_ID, int Role_of_workshop_ID, int Deleted)
	{
		try
		{
			stm = c.createStatement();

			stm.executeQuery("INSERT INTO \"Group_status\" (Group_ID , Role_of_workshop_ID , Deleted) VALUES ( "+Group_ID+" , "+Role_of_workshop_ID+" , "+Deleted+" )");
		}
		catch (Exception e)
		{
			System.out.println("fail while adding object to Group_status");
			System.out.println(e.toString());
		}
	}
	public void Update_Group_status(int Group_Status_ID,int Group_ID,int Role_of_workshop_ID,int Deleted)
	{
		try
		{
			stm = c.createStatement();
			stm.executeQuery("UPDATE \"Group_status\" SET Group_ID = "+Group_ID+" , Role_of_workshop_ID = "+Role_of_workshop_ID+" , Deleted = "+Deleted+" WHERE (Group_Status_ID ="+ Group_Status_ID +")");
			
		}
		catch (Exception e)
		{
			System.out.println("fail while updating object to Update_Group_status");
			System.out.println(e.toString());
		}
	}
	/*
	 	HeldWorkshop
	 	int HeldWorkshop_ID
	 	int Management_ID
	 	int Workshop_ID
	 	String name
	 	String start
	 	String end
	 	String hourstart
	 	String hourend
	 	int is_installment
	 	int money
	 	int pay_in_howmany_times
	 	int Deleted
	 */
	public void Get_HeldWorkshop_All()
	{
		try
		{
		stm = c.createStatement();
		ResultSet rs = stm.executeQuery("SELECT * FROM  \"HeldWorkshop\"");
		while (rs.next())
		{
			int HeldWorkshop_ID = rs.getInt("HeldWorkshop_ID");
		 	int Management_ID = rs.getInt("Management_ID");
		 	int Workshop_ID = rs.getInt("Workshop_ID");
		 	String name = rs.getString("name");
		 	String start = rs.getString("start");
		 	String end = rs.getString("end");
		 	String hourstart = rs.getString("hourstart");
		 	String hourend = rs.getString("hourend");
		 	int is_installment = rs.getInt("is_installment");
		 	int money = rs.getInt("money");
		 	int pay_in_howmany_times = rs.getInt("pay_in_howmany_times");
		 	int Deleted = rs.getInt("Deleted");
			
			//construct new object and put it in array
			//System.out.println(Group_Status_ID + "    "+ Group_ID + "    "+ Role_of_workshop_ID +"    "+ Deleted);
		}
		}
		catch (Exception e)
		{
			System.out.println("fail while getting all HeldWorkshop");
			System.out.println(e.toString());
		}
	}
	public void Get_HeldWorkshop(int HeldWorkshop_ID)
	{
		try
		{
		stm = c.createStatement();
		ResultSet rs = stm.executeQuery("SELECT * FROM  \"HeldWorkshop\" WHERE HeldWorkshop_ID ="+HeldWorkshop_ID);
		while (rs.next())
		{
			HeldWorkshop_ID = rs.getInt("HeldWorkshop_ID");
		 	int Management_ID = rs.getInt("Management_ID");
		 	int Workshop_ID = rs.getInt("Workshop_ID");
		 	String name = rs.getString("name");
		 	String start = rs.getString("start");
		 	String end = rs.getString("end");
		 	String hourstart = rs.getString("hourstart");
		 	String hourend = rs.getString("hourend");
		 	int is_installment = rs.getInt("is_installment");
		 	int money = rs.getInt("money");
		 	int pay_in_howmany_times = rs.getInt("pay_in_howmany_times");
		 	int Deleted = rs.getInt("Deleted");
			
			//construct new object and put it in array
			//System.out.println(Group_Status_ID + "    "+ Group_ID + "    "+ Role_of_workshop_ID +"    "+ Deleted);
		}
		}
		catch (Exception e)
		{
			System.out.println("fail while getting HeldWorkshop");
			System.out.println(e.toString());
		}
	}
	public void Add_HeldWorkshop(
			//int HeldWorkshop_ID,
		 	int Management_ID,
		 	int Workshop_ID,
		 	String name,
		 	String start,
		 	String end,
		 	String hourstart,
		 	String hourend,
		 	int is_installment,
		 	int money,
		 	int pay_in_howmany_times,
		 	int Deleted
			)
	{
		try
		{
			stm = c.createStatement();

			stm.executeQuery("INSERT INTO \"HeldWorkshop\" (Management_ID , Workshop_ID , name , "
					+ " start , end , hourstart , hourend , is_installment , money . "
					+ " pay_in_howmany_times , Deleted) VALUES ( "+Management_ID+" , "+Workshop_ID+" , '"+name+
					"' , '"+start+"' , '"+end+"' , '"+hourstart+"' , '"+hourend+"' , "+is_installment+" , "+money+" , "+pay_in_howmany_times+" , "+Deleted+" )");
		}
		catch (Exception e)
		{
			System.out.println("fail while adding object to Group_status");
			System.out.println(e.toString());
		}
	}
	public void Update_HeldWorkshop(
			int HeldWorkshop_ID,
		 	int Management_ID,
		 	int Workshop_ID,
		 	String name,
		 	String start,
		 	String end,
		 	String hourstart,
		 	String hourend,
		 	int is_installment,
		 	int money,
		 	int pay_in_howmany_times,
		 	int Deleted
			)
	{
		try
		{
			stm = c.createStatement();
			stm.executeQuery("UPDATE \"HeldWorkshop\" SET Management_ID = "+Management_ID+" , Workshop_ID = "+Workshop_ID+
					" , name = '"+name+"' , start = '"+Workshop_ID+
					"' , end = '"+end+"' , hourstart = '"+Workshop_ID+
					"' , hourend = '"+hourend+"' , is_installment = "+is_installment+
					" , money = "+money+" , pay_in_howmany_times = "+pay_in_howmany_times+
					" , Deleted = "+Deleted+" WHERE (HeldWorkshop_ID ="+ HeldWorkshop_ID +")");
			
		}
		catch (Exception e)
		{
			System.out.println("fail while updating object to HeldWorkshop");
			System.out.println(e.toString());
		}
	}
	
}
