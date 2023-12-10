package org.pfcolab;

import javax.lang.model.element.NestingKind;
import java.io.File;
import java.util.ArrayList;
import java.util.*;


/*

 * What the system should do?
 *
 * will check if it's running for the first time
 * IF first time run
 *   create the database file
 *   create relevant tables
 *   Show message that it is running for the first time
 *   only the admin panel will show
 *   the option for adding fake data will be shown to admin along with other options
 *  IF admin panel

 * //creating separate files for doctors, receptionists,
 * //Doctor creation method will receive JSON string and each property of doctor will be extracted using regEx or json string parser.
 * // tables can be created using SQL CREATE TABLE query
 *
 *
 *
 * ///some features
 * Show doctors, patients, and wards  by Alphabetic order
 * remove by name or unique id
 *  make it ready for next presentation, remove all the previous files and data

 * */


public class Main {
    final static String DATABASE = "HospitalData";
    static String currentDir = System.getProperty("user.dir");
    final static String databaseFilePath = currentDir + "//" + DATABASE + ".accdb";
    static ArrayList<String> doctorsNamesList = new ArrayList<String>();
    static ArrayList<String> doctorsWardsList = new ArrayList<String>();
    static ArrayList<Integer> doctorsContactsList = new ArrayList<Integer>();
    static Scanner scanner = new Scanner(System.in);
    public static void handleUserInput() {

        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                // Navigate to Reception Portal
                break;
            case 2:
                // Navigate to Doctors Portal
                break;
            case 3:
                adminPortal();
                break;
            case 4:
                // Exit the system
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
    }
    public  static void adminPortal(){
        System.out.println("1. Add Doctor");
        System.out.println("2. Edit Doctor");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                System.out.print("Enter Doctor Name: ");
                String docName = scanner.nextLine();
                System.out.print("Enter Doctor Ward: ");
                String docWard = scanner.nextLine();
                System.out.print("Enter Doctor contact: ");
                String docContact = scanner.next();
                String docInfo = docName + "," + docWard + "," + docContact;
                addDoctor(docInfo);
                break;
            case 2:

                break;
        }
    }
    public static void displayMainMenu() {
        System.out.println("1. Reception Portal");
        System.out.println("2. Doctors Portal");
        System.out.println("3. Admin Portal");
        System.out.println("4. Exit");
    }
    public static void firstTimeRun(){
        //Create table in the database
        //Create files (doctors, receptionists , patients, wards, appointments, )
        String databaseURL = "jdbc:ucanaccess://" + databaseFilePath + ";newdatabaseversion=V2010";

    }

    public static void main(String[] args) {
        displayMainMenu();
    }

    public static boolean isNumeric(String inputS) {
        return inputS.chars().allMatch(Character::isDigit);
    }

    public static boolean fileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    public static void addDoctor(String doctorInfo) {
        String[] doctorData = doctorInfo.split(",");
        String name = doctorData[0];
        String ward = doctorData[1];
        String contactString = doctorData[2];
        if (isNumeric(contactString)) {
            int contactInt = Integer.parseInt(contactString);
            addDoctor(name, ward, contactInt);
        }
    }

    public static void addDoctor(String name, String ward, int contact) {
        try {
            doctorsNamesList.add(name);
            doctorsWardsList.add(ward);
            doctorsContactsList.add(contact);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}