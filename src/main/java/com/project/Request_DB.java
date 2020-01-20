package com.project;

public class Request_DB
{
	public int Request_ID;
    public int holdWorkShop_id;
    public String message;
    public int acceptly;
    public int intherit_type;
    public int deleted;

    public Request_DB(int request_ID, int holdWorkShop_id, String message, int acceptly, int intherit_type, int deleted) {
        Request_ID = request_ID;
        this.holdWorkShop_id = holdWorkShop_id;
        this.message = message;
        this.acceptly = acceptly;
        this.intherit_type = intherit_type;
        this.deleted = deleted;
    }
}
