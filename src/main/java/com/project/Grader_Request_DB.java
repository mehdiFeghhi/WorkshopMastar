package com.project;

public class Grader_Request_DB
{
    public int Grader_Request_Id;
    public int grader_ID;
    public int type;
    public int Request;

    public Grader_Request_DB(int grader_Request_Id, int grader_ID, int type, int request) {
        Grader_Request_Id = grader_Request_Id;
        this.grader_ID = grader_ID;
        this.type = type;
        Request = request;
    }
    public Grader_Request makeGrader_Request(){
        Grader_Request grader_request = new Grader_Request();
        //grader_request.getType()
        Grader grader = new Grader();
        ////////////////////////find in data base
        grader_request.setGrader(grader);
        return grader_request;
    }
}
