package org.pfcolab;
import java.util.*;
import java.lang.*;
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
    //Pasword for admin
    static String savedAdminPassword = "kaleem";
    static String currentDir = System.getProperty("user.dir");
    final static String databaseFilePath = currentDir + "//" + DATABASE + ".accdb";

    //Array lists for the stored data of multiple entries
    static ArrayList<String[]> doctorsList = new ArrayList<>();
    static ArrayList<String[]> receptionitsList = new ArrayList<>();
    static ArrayList<String[]> wardsList = new ArrayList<>();
    static ArrayList<String[]> patientsList = new ArrayList<>();
    static ArrayList<String[]> diagnosisList = new ArrayList<>();
    static ArrayList<String[]> appointmentsList = new ArrayList<>();



    //main function! wow!! ;)
    public static void main(String[] args) {

        mainPortal();

    }

    //function to display options of the main portal
    static void displayOptionsMainPortal() {
        System.out.println("\n=== Menu ===");
        System.out.println("1. Navigate to Admin Portal ");
        System.out.println("2. Navigate to Receptionist Portal");
        System.out.println("3. Navigate to Doctors Portal");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");
    }

    //function to display options of the admin portal
    static void displayOptionsAdminPortal() {
        //Doctors data options
        System.out.println("\n=== Admin Portal Menu ===");
        System.out.println("1. Add Doctors Data");
        System.out.println("2. Edit Doctors Data");
        System.out.println("3. Delete Doctors Data");
        System.out.println("4. Display all Doctors");
        System.out.println("5. Display Specific Doctor's Data");

        //Receptionist data options
        System.out.println("6. Add Receptionists");
        System.out.println("7. Edit Receptionists");
        System.out.println("8. Delete Receptionists");
        System.out.println("9. Display Receptions");

        //Ward data options
        System.out.println("10. Add Ward");
        System.out.println("11. Edit Wards");
        System.out.println("12. Delete Wards");
        System.out.println("13. Display Wards");
        System.out.println("14. Return to Main Menu");
    }

    //main portal to ask the user to be directed to.
    public static void mainPortal() {
        displayOptionsMainPortal();
        int choice = scanner.nextInt();
        scanner.nextLine();
        switch (choice) {
            case 1:
                // Navigate to Admin Portal
                adminPortal();
                break;
            case 2:
                // Navigate to Doctors Portal
                break;
            case 3:
                // Navigate to the case statement handling the receptionist portal
                break;
            case 4:
                // Exit the system
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
    }

    //Now we are making a function for admin login it works only if the user has the correct pasword
    //and login Name.
    static boolean isAdmin(){

        System.out.println("Enter your Password: ");
        String enteredAdminPassword = scanner.nextLine();
        System.out.println(enteredAdminPassword);
        System.out.println(savedAdminPassword);
        return(enteredAdminPassword.equals(savedAdminPassword));

    }

    static void paswordChecker(){

        //Checking if the password is correct
        int loginTries = 0;
        while (isAdmin() != true) {
            System.out.println("Wrong Password!");
            loginTries++;
            if (loginTries == 5) {
                System.out.println("You have entered wrong pasword 5 times, returing to Main Menu");
                mainPortal();
            }
        }
    }

    //This is the admin portal. It has the ability to add and remove doctors, receptionists and wards
    public static void adminPortal() {

        //checking password
        paswordChecker();
        //diplaying options for admin portal
        displayOptionsAdminPortal();
        //inputing user for admin portal options
        System.out.print("Enter your choice: ");
        int choiceAdminPortal = scanner.nextInt();

        switch (choiceAdminPortal) {
            case 1:
                System.out.println("You selected: Add Doctors Data");
                // Add your logic for this option
                addDoctorFromUserInput(doctorsList, scanner);

                break;
            case 2:
                System.out.println("You selected: Edit Doctors Data");
                // Add your logic for this option
                break;
            case 3:
                System.out.println("You selected: Delete Doctors Data");
                // Add your logic for this option
                break;
            case 4:
                System.out.println("You selected: Display all Doctors");
                // Add your logic for this option
                break;
            case 5:
                System.out.println("You selected: Display Specific Doctor's Data");
                // Add your logic for this option
                break;
            case 6:
                System.out.println("You selected: Add Receptionists");
                // Add your logic for this option
                break;
            case 7:
                System.out.println("You selected: Edit Receptionists");
                // Add your logic for this option
                break;
            case 8:
                System.out.println("You selected: Delete Receptionists");
                // Add your logic for this option
                break;
            case 9:
                System.out.println("You selected: Display Receptions");
                // Add your logic for this option
                break;
            case 10:
                System.out.println("You selected: Add Ward");
                // Add your logic for this option
                break;
            case 11:
                System.out.println("You selected: Edit Wards");
                // Add your logic for this option
                break;
            case 12:
                System.out.println("You selected: Delete Wards");
                // Add your logic for this option
                break;
            case 13:
                System.out.println("You selected: Display Wards");
                // Add your logic for this option
                break;
            case 14:
                System.out.println("You selected: Return to Main Menu");
                mainPortal();
                break;
            default:
                System.out.println("Invalid choice");
        }
    }
    /* Functions to manipulate doctor database are below:
    These include add, edit, delete, display all, display specific doctors
    Use with care ;)
     */

    // Add a new doctor to the database based on user input
    private static void addDoctorFromUserInput(ArrayList<String[]> doctorsList, Scanner scanner) {
        System.out.println("\nEnter details for the new doctor:");
        System.out.print("Enter doctor name: ");
        String name = scanner.nextLine();
        System.out.print("Enter doctor timing: ");
        String timing = scanner.nextLine();
        System.out.print("Enter doctor ward: ");
        String ward = scanner.nextLine();
        System.out.print("Enter doctor specialty: ");
        String specialty = scanner.nextLine();

        // Creating a new array to store doctor details
        String[] newDoctor = {name, timing, ward, specialty};

        // Adding the new doctor to the ArrayList
        doctorsList.add(newDoctor);

        System.out.println("New doctor added successfully.");
    }

    // Display all doctors in the database
    private static void displayAllDoctors(ArrayList<String[]> doctorsList) {
        System.out.println("\nList of Doctors:");
        for (int i = 0; i < doctorsList.size(); i++) {
            System.out.println("Doctor ID: " + (i + 1) + ", Doctor Info: " + arrayToString(doctorsList.get(i)));
        }
    }

    // View specific doctor details based on user input (by name)
    private static void viewSpecificDoctorDetails(ArrayList<String[]> doctorsList, Scanner scanner) {
        System.out.print("\nEnter the name of the doctor to view details: ");
        String doctorNameToView = scanner.nextLine();

        // Check if the doctor name exists in the database
        int doctorIndexToView = getDoctorIndexByName(doctorsList, doctorNameToView);

        if (doctorIndexToView != -1) {
            // View the details of the specific doctor
            String[] doctorDetails = doctorsList.get(doctorIndexToView);
            System.out.println("Details of Doctor '" + doctorNameToView + "': " + arrayToString(doctorDetails));
        } else {
            System.out.println("Doctor with name '" + doctorNameToView + "' not found in the database.");
        }
    }

    // Edit details of a doctor based on user input (by name)
    private static void editDoctorDetails(ArrayList<String[]> doctorsList, Scanner scanner) {
        System.out.print("\nEnter the name of the doctor to edit details: ");
        String doctorNameToEdit = scanner.nextLine();

        // Check if the doctor name exists in the database
        int doctorIndexToEdit = getDoctorIndexByName(doctorsList, doctorNameToEdit);

        if (doctorIndexToEdit != -1) {
            // Edit the details of the specific doctor
            String[] doctorDetails = doctorsList.get(doctorIndexToEdit);
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
    private static void deleteDoctorByNameFromUserInput(ArrayList<String[]> doctorsList, Scanner scanner) {
        System.out.print("\nEnter the name of the doctor to delete: ");
        String doctorNameToDelete = scanner.nextLine();

        // Check if the doctor name exists in the database
        int doctorIndexToDelete = getDoctorIndexByName(doctorsList, doctorNameToDelete);

        if (doctorIndexToDelete != -1) {
            // Delete the doctor
            doctorsList.remove(doctorIndexToDelete);
            System.out.println("Doctor with name '" + doctorNameToDelete + "' has been deleted.");
        } else {
            System.out.println("Doctor with name '" + doctorNameToDelete + "' not found in the database.");
        }
    }

    // View the ID of a doctor by name
    private static void viewDoctorIdByName(ArrayList<String[]> doctorsList, Scanner scanner) {
        System.out.print("\nEnter the name of the doctor to view ID: ");
        String doctorNameToViewId = scanner.nextLine();

        // Check if the doctor name exists in the database
        int doctorIndexToViewId = getDoctorIndexByName(doctorsList, doctorNameToViewId);

        if (doctorIndexToViewId != -1) {
            System.out.println("Doctor ID for '" + doctorNameToViewId + "': " + (doctorIndexToViewId + 1));
        } else {
            System.out.println("Doctor with name '" + doctorNameToViewId + "' not found in the database.");
        }
    }

    // Get the index of a doctor by name
    private static int getDoctorIndexByName(ArrayList<String[]> doctorsList, String name) {
        for (int i = 0; i < doctorsList.size(); i++) {
            if (name.equals(doctorsList.get(i)[0])) {
                return i;
            }
        }
        return -1;
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