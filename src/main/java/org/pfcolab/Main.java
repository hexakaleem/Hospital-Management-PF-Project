package org.pfcolab;

import javax.lang.model.element.NestingKind;
import java.io.File;
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
 * Show doctors, patients, and wards by Alphabetic order
 * remove by name or unique id
 * make it ready for next presentation, remove all the previous files and data

 * */


public class Main {
    
    static Scanner scanner = new Scanner(System.in);
    
    final static String DATABASE = "HospitalData";
    static String currentDir = System.getProperty("user.dir");
    final static String databaseFilePath = currentDir + "//" + DATABASE + ".accdb";
  
    //Doctors lists
    //Switching to hashmaps now
    static Map<String, String[]> doctorDatabase = new HashMap<>();
    
    //Receptionist lists
    static ArrayList<String> receptionistNamesList = new ArrayList<String>();
    static ArrayList<Integer> receptionistPaswordList = new ArrayList<Integer>();
    
    //Ward lists
    static ArrayList<String> wardNameList = new ArrayList<String>();
    //needed to be predecleared since there are only a few type of wards.
    static ArrayList<String> wardTypeList = new ArrayList<String>();
    static ArrayList<String> wardTotalBedsList = new ArrayList<String>();
    static ArrayList<String> wardAvailableBedsList = new ArrayList<String>();
    static ArrayList<String> wardInUseBedsList = new ArrayList<String>();


    
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
                //Navigate to the case statement handelling the admin portal
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
   
    public static void adminPortal(){
        int choice = scanner.nextInt();
        //Making another switch statement for the admin portal. The admin will choose from three options
        //1-Edit Doctors data 2- Edit Recptionists Data 3- Edit Wards Data
        System.out.println("1. Edit Doctors Data");
        System.out.println("2. Edit Receptionists Data");
        System.out.println("3. Edit Wards Data");

        switch (choice){ 

            case 1:

                while (true) {
                    System.out.println("\n=== Doctor Database Menu ===");
                    System.out.println("1. Add Doctor");
                    System.out.println("2. View All Doctors");
                    System.out.println("3. View Specific Doctor Details");
                    System.out.println("4. Edit Doctor Details");
                    System.out.println("5. Delete Doctor by Name");
                    System.out.println("6. View Doctor ID by Name");
                    System.out.println("7. Exit");
                    System.out.print("Enter your choice: ");

                    int choiceDoc = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline character

                    switch (choiceDoc) {
                        case 1:
                            addDoctorFromUserInput(doctorDatabase, scanner);
                            break;
                        case 2:
                            displayAllDoctors(doctorDatabase);
                            break;
                        case 3:
                            viewSpecificDoctorDetails(doctorDatabase, scanner);
                            break;
                        case 4:
                            editDoctorDetails(doctorDatabase, scanner);
                            break;
                        case 5:
                            deleteDoctorByNameFromUserInput(doctorDatabase, scanner);
                            break;
                        case 6:
                            viewDoctorIdByName(doctorDatabase, scanner);
                            break;
                        case 7:
                            System.out.println("Exiting program. Goodbye!");
                            scanner.close();
                            System.exit(0);
                        default:
                            System.out.println("Invalid choice. Please enter a valid option.");
                    }
                }

                case 2:
                //receptionists details
        
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

    //Flollowing are the methods delcared for the admin to edit the doctors database
    //These will be used by the admin only.
    // Display all doctors in the database
    private static void displayAllDoctors(Map<String, String[]> database) {
        System.out.println("\nList of Doctors:");
        for (Map.Entry<String, String[]> entry : database.entrySet()) {
            System.out.println("Doctor ID: " + entry.getKey() + ", Doctor Info: " + arrayToString(entry.getValue()));
        }
    }

    // View specific doctor details based on user input (by name)
    private static void viewSpecificDoctorDetails(Map<String, String[]> database, Scanner scanner) {
        System.out.print("\nEnter the name of the doctor to view details: ");
        String doctorNameToView = scanner.nextLine();

        // Check if the doctor name exists in the database
        String doctorIdToView = getDoctorIdByName(database, doctorNameToView);

        if (doctorIdToView != null) {
            // View the details of the specific doctor
            String[] doctorDetails = database.get(doctorIdToView);
            System.out.println("Details of Doctor '" + doctorNameToView + "': " + arrayToString(doctorDetails));
        } else {
            System.out.println("Doctor with name '" + doctorNameToView + "' not found in the database.");
        }
    }

    // Edit details of a doctor based on user input (by name)
    private static void editDoctorDetails(Map<String, String[]> database, Scanner scanner) {
        System.out.print("\nEnter the name of the doctor to edit details: ");
        String doctorNameToEdit = scanner.nextLine();

        // Check if the doctor name exists in the database
        String doctorIdToEdit = getDoctorIdByName(database, doctorNameToEdit);

        if (doctorIdToEdit != null) {
            // Edit the details of the specific doctor
            String[] doctorDetails = database.get(doctorIdToEdit);
            System.out.println("Current Details of Doctor '" + doctorNameToEdit + "': " + arrayToString(doctorDetails));

            System.out.println("\nSelect the option to edit:");
            System.out.println("1. Edit Timing");
            System.out.println("2. Edit Ward");
            System.out.println("3. Edit Specialty");
            System.out.print("Enter your choice: ");
            int editChoice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (editChoice) {
                case 1:
                    System.out.print("Enter new timing: ");
                    String newTiming = scanner.nextLine();
                    doctorDetails[1] = newTiming;
                    break;
                case 2:
                    System.out.print("Enter new ward: ");
                    String newWard = scanner.nextLine();
                    doctorDetails[2] = newWard;
                    break;
                case 3:
                    System.out.print("Enter new specialty: ");
                    String newSpecialty = scanner.nextLine();
                    doctorDetails[3] = newSpecialty;
                    break;
                default:
                    System.out.println("Invalid edit choice. No changes made.");
            }

            System.out.println("Details of Doctor '" + doctorNameToEdit + "' after edit: " + arrayToString(doctorDetails));
        } else {
            System.out.println("Doctor with name '" + doctorNameToEdit + "' not found in the database.");
        }
    }

    // Delete a doctor from the database based on user input (by name)
    private static void deleteDoctorByNameFromUserInput(Map<String, String[]> database, Scanner scanner) {
        System.out.print("\nEnter the name of the doctor to delete: ");
        String doctorNameToDelete = scanner.nextLine();

        // Check if the doctor name exists in the database
        String doctorIdToDelete = getDoctorIdByName(database, doctorNameToDelete);

        if (doctorIdToDelete != null) {
            // Delete the doctor
            database.remove(doctorIdToDelete);
            System.out.println("Doctor with name '" + doctorNameToDelete + "' has been deleted.");
        } else {
            System.out.println("Doctor with name '" + doctorNameToDelete + "' not found in the database.");
        }
    }

    // View the ID of a doctor by name
    private static void viewDoctorIdByName(Map<String, String[]> database, Scanner scanner) {
        System.out.print("\nEnter the name of the doctor to view ID: ");
        String doctorNameToViewId = scanner.nextLine();

        // Check if the doctor name exists in the database
        String doctorIdToView = getDoctorIdByName(database, doctorNameToViewId);

        if (doctorIdToView != null) {
            System.out.println("Doctor ID for '" + doctorNameToViewId + "': " + doctorIdToView);
        } else {
            System.out.println("Doctor with name '" + doctorNameToViewId + "' not found in the database.");
        }
    }

    // Get the ID of a doctor by name
    private static String getDoctorIdByName(Map<String, String[]> database, String name) {
        for (Map.Entry<String, String[]> entry : database.entrySet()) {
            if (name.equals(entry.getValue()[0])) {
                return entry.getKey();
            }
        }
        return null;
    }

    // Helper method to convert an array to a string
    private static String arrayToString(String[] array) {
        StringBuilder result = new StringBuilder("[");
        for (int i = 0; i < array.length; i++) {
            result.append(array[i]);
            if (i < array.length - 1) {
                result.append(", ");
            }
        }
        result.append("]");
        return result.toString();
    }


}