package com.project;

public class Grader_Request_DB
{
    int Grader_Request_ID ;
    int Grader_ID ;
    int Request_ID ;
    int type ;
    int request ;
    int Deleted ;


    public Grader_Request_DB(int grader_request_id, int grader_Request_ID, int grader_ID, int request_ID, int type, int request, int deleted) {
        Grader_Request_ID = grader_Request_ID;
        Grader_ID = grader_ID;
        Request_ID = request_ID;
        this.type = type;
        this.request = request;
        Deleted = deleted;
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
