package org.pfcolab;

import java.io.File;
import java.util.ArrayList;



/*
 * What the system should do?
 * will check if it's running for the first time
 * IF first time run
 *   create the database file
 *   create relevant tables
 *   Show message that it is running for the first time
 *   only the admin panel will show
 *   the option for adding fake data will be shown to admin along with other options
 *
 * //creating separate files for doctors, receptionists,
 * //Doctor creation method will receive JSON string and each property of doctor will be extracted using regEx or json string parser.
 * // tables can be created using SQL CREATE TABLE query
 *
 *
 *
 * ///some features
 * Show doctors, patients, and wards  by Alphabetic order
 * remove by name or unique id
 *
 * */


public class Main {
    static String currentDir = System.getProperty("user.dir");
    final static String DATABASE = "HospitalData";
    final static String databaseFilePath = currentDir + "//" + DATABASE + ".accdb";
    static ArrayList<String> doctorsNamesList = new ArrayList<String>();
    static ArrayList<String> doctorsWardsList = new ArrayList<String>();
    static ArrayList<Integer> doctorsContactsList = new ArrayList<Integer>();


    public static void main(String[] args) {

        String databaseURL = "jdbc:ucanaccess://" + databaseFilePath + ";newdatabaseversion=V2010";
        if (!fileExists(databaseFilePath)) {
            //if the file does not exist , it means its is running for the first time
            System.out.print("running for the first time");
        }
    }

    public static boolean isNumeric(String inputS) {
        return inputS.chars().allMatch(Character::isDigit);
    }

    public static boolean fileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    //Function will receive a string of information of doctor, with | as the separator
    public static void AddDoctor(String doctorInfo) {
        String[] doctorData = doctorInfo.split(",");
        String name = doctorData[0];
        String ward = doctorData[1];
        String contactString = doctorData[2];
        if (isNumeric(contactString)) {
            int contactInt = Integer.parseInt(contactString);
            AddDoctor(name, ward, contactInt);
        }
    }

    //Function will receive each info as the parameter
    public static void AddDoctor(String name, String ward, int contact) {
        try {
            doctorsNamesList.add(name);
            doctorsWardsList.add(ward);
            doctorsContactsList.add(contact);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}