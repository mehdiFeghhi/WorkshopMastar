package com.project;

public class Workshop_DB
{
	public int workshop_id;
	public String Title;
	public String description;

	public Workshop_DB(int workshop_id, String title, String description) {
		this.workshop_id = workshop_id;
		Title = title;
		this.description = description;
	}
	public Workshop makeWorkShop(){
		Workshop workshop = new Workshop();
		workshop.setId(workshop_id);
		workshop.setTitle(this.Title);
		workshop.setDescription(this.description);
		return workshop;
	}
}
