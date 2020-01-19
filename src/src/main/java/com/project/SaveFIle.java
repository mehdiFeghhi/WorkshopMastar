package com.project;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class SaveFIle {
    public static void saveArrayListInFile(String directory, ArrayList arrayList){
        try
        {
            FileOutputStream fos = new FileOutputStream(directory);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(arrayList);
            oos.close();
            fos.close();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }
    public static ArrayList loadFromFileArrayList(String directory) {
        ArrayList<String> namesList = new ArrayList<String>();

        try {
            FileInputStream fis = new FileInputStream(directory);
            ObjectInputStream ois = new ObjectInputStream(fis);

            namesList = (ArrayList) ois.readObject();
            ois.close();
            fis.close();
            return namesList;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        } catch (ClassNotFoundException c) {
            System.out.println("Class not found");
            c.printStackTrace();
            return null;
        }
    }
    public static void saveHashMap(String directory, HashMap hashMap){
        try
        {
            FileOutputStream fos = new FileOutputStream(directory);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(hashMap);
            oos.close();
            fos.close();
            System.out.printf("Serialized HashMap data is saved in hashmap.ser");
        }catch(IOException ioe)
        {
            ioe.printStackTrace();
        }
    }
    public static HashMap loadHashMap(String directory){
        HashMap<Integer, String> map = null;
        try
        {
            FileInputStream fis = new FileInputStream(directory);
            ObjectInputStream ois = new ObjectInputStream(fis);
            map = (HashMap) ois.readObject();
            ois.close();
            fis.close();
            return map;
        }catch(IOException ioe)
        {
            ioe.printStackTrace();
            return null;
        }catch(ClassNotFoundException c)
        {
            System.out.println("Class not found");
            c.printStackTrace();
            return null;
        }
    }

}
