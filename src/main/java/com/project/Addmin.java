package com.project;

import java.util.ArrayList;
import java.util.Date;

public class Addmin implements RoleOfWorkShape {
    private Type type = Type.Admin;
    ArrayList<Person> persons ;
    private String user;
    public Addmin(String user){
        this.user = user;
    }
    public Workshop makeNewWorkshpe(String nameOfWorkshape, Date firstWorkshape, Date endWorksahpe, Date dayOfWeakStart, Date dayOfWeakEnd, Date hourStart, Date hourEnd, int numberStudent, int numberGreater, String User){
        Workshop newWorkshope = new Workshop(nameOfWorkshape, firstWorkshape, endWorksahpe, dayOfWeakStart, dayOfWeakEnd,  hourStart,  hourEnd,  numberStudent, numberGreater);
        Person personFind = findThisPerson(User);
        Managment whoMakeWorkShape = (Managment) personFind.findOurType(Type.management);
        whoMakeWorkShape.addToWorksahpes(newWorkshope);
        return newWorkshope;
    }
    public Person findThisPerson(String user){
        for (Person i : this.persons){
            if(i.getUser().equals(user))
                return i;
        }
        return null;
    }

    public Type getType() {
        return type;
    }
}
