package com.project;

public class Student implements RoleOfWorkShape {
    Type type = Type.Student;
    String user;

    public Student(String user) {
        this.user = user;
    }

    public Student() {

    }

    public String getUser() {
        return user;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Type getType() {
        return type;
    }
}
