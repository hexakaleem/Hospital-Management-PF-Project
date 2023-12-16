package org.pfcolab;

import java.io.*;
import java.util.*; 

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

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

    // Different files for different entity data and their path stored in variables
    final static String DATABASE = "HospitalData";
    final static String DOCTORS_FILENAME = "Doctors.txt";
    static File doctorDataFilePath = new File(DOCTORS_FILENAME);
    final static String RECEPTIONISTS_FILE_NAME = "Receptionists.txt";
    static File receptionistDataFilePath = new File(RECEPTIONISTS_FILE_NAME);
    final static String WARDS_FILE_NAME = "Wards.txt";
    static File wardDataFilePath = new File(WARDS_FILE_NAME);
    final static String PATIENTS_FILE_NAME = "Patients.txt";
    static File patientDataFilePath = new File(PATIENTS_FILE_NAME);
    final static String DIAGNOSIS_FILE_NAME = "Diagnosis.txt";
    static File diagnosisDataFilePath = new File(DIAGNOSIS_FILE_NAME);
    final static String APPOINTMENTS_FILE_NAME = "Appointments.txt";
    static File appointmentsDataFilePath = new File(APPOINTMENTS_FILE_NAME);
    final static String SYSTEM_VARIABLES_FILE_NAME = "SystemVariables.txt";
    static File systemVariablesFilePath = new File(SYSTEM_VARIABLES_FILE_NAME);

    // Password for admin
    static String adminPassword = "kaleem";

    // --------------------------------------------------------------------------------------------------------------------//
    /*
     * Array lists for the stored data of multiple entries
     * We are using String array to store the data of one entity
     * The structure of each Entity is commented before declaration the line
     */

    // ['Doc ID', 'Doc Name' , 'Time Start', 'Time End', 'Ward Name', 'Specialized
    // In']
    static ArrayList<String[]> doctorsList = new ArrayList<>();

    // ['Recep ID', 'Receptionist Name' , 'Password']
    static ArrayList<String[]> receptionistsList = new ArrayList<>();

    // ['Ward ID', 'Ward Name' , 'Total Beds','Occupied Beds','Type']
    static ArrayList<String[]> wardsList = new ArrayList<>();

    // ['Patient ID', 'Patient Name' , 'Gender','Age','Contact']
    static ArrayList<String[]> patientsList = new ArrayList<>();

    // ['Diagnosis ID', 'Patient ID' ,
    // 'DoctorID','Prescriptions','Diagnosis','Appointment ID']
    static ArrayList<String[]> diagnosisList = new ArrayList<>();

    // ['Appointment ID', 'Patient ID' , 'Doctor ID','Time Start','Time End']
    static ArrayList<String[]> appointmentsList = new ArrayList<>();

    // { numberOfDoctors, numberOfReceptionists, numberOfWards, numberOfPatients,
    // numberOfDiagnosis, numberOfAppointments }
    static int[] numberOfEntitiesArray = new int[6];

    // --------------------------------------------------------------------------------------------------------------------//

    // main function! wow!! ;)
    public static void main(String[] args) {
        // Reading all the data after the program boots
        initilizeProgramme();// function to load data and make the system ready

        // opening the main portal
        mainPortal();
    }

    // this function updates the file of Entity by using Async threading, it will
    // run in background and ui will not be hanged.
    public static void updateDatabaseFile(String nameOfEntityChanged) {
        if (nameOfEntityChanged.equals("VariablesArray")) {
            updateSystemVariablesFile();
        } else if (nameOfEntityChanged.equals("Doctor")) {
            updateDatabaseFile(DOCTORS_FILENAME, doctorsList);
        } else if (nameOfEntityChanged.equals("Receptionist")) {
            updateDatabaseFile(RECEPTIONISTS_FILE_NAME, receptionistsList);
        } else if (nameOfEntityChanged.equals("Ward")) {
            updateDatabaseFile(WARDS_FILE_NAME, wardsList);
        } else if (nameOfEntityChanged.equals("Patient")) {
            updateDatabaseFile(PATIENTS_FILE_NAME, patientsList);
        } else if (nameOfEntityChanged.equals("Diagnosis")) {
            updateDatabaseFile(DIAGNOSIS_FILE_NAME, diagnosisList);
        } else if (nameOfEntityChanged.equals("Appointment")) {
            updateDatabaseFile(APPOINTMENTS_FILE_NAME, appointmentsList);
        }

    }

    // This funtion checks for the password strength and updates it in file and
    // global variable
    public static boolean setAdminPassword(String password) {
        if (isValidPassword(password)) {
            adminPassword = password;
            System.out.println("\n Password is set Successfully");
            updateDatabaseFile("VariablesArray");
            return true;
        } else {
            System.out.println(
                    "\n Password is too weak, It must contain a Digit, UpperCaseLetter and should have length of 8");
            return false;
        }

    }

    private static void updateAdminPassword(Scanner scanner) {
        System.out.print("\nEnter the current admin password: ");
        String currentPassword = scanner.nextLine();

        if (currentPassword.equals(adminPassword)) {
            String newPassword;
            do {
                System.out.print("Enter the new admin password: ");
                newPassword = scanner.nextLine();
            } while (!setAdminPassword(newPassword));
        } else {
            System.out.println("Incorrect current password. Admin password not updated.");
        }
    }

    /*
     * Need to check if the password contains digits, uppercase character and has
     * length of 8
     */
    public static boolean isValidPassword(String password) {
        if (password.length() < 8)
            return false;
        char[] passwordCharacters = password.toCharArray();
        boolean hasDigit = false, hasUpCharacter = false;
        for (char ch : passwordCharacters) {
            if (ch != ' ') {
                if (Character.isUpperCase(ch))
                    hasUpCharacter = true;
                if (Character.isDigit(ch))
                    hasDigit = true;
            }
        }
        if (hasDigit && hasUpCharacter)
            return true;
        else
            return false;
    }

    public static void initilizeProgramme() {
        if (!systemVariablesFilePath.exists()) {
            createEmptyDatabaseFiles();
        } else {
            loadDatabaseFiles();
        }
    }

    private static void addDummyDoctor() {
        String doctorID = generateID("Doctor");
        String doctorName = "Doctor" + RandomStringUtils.randomNumeric(2);
        String timingStart = RandomStringUtils.randomNumeric(1) + "AM";
        String timingEnd = RandomStringUtils.randomNumeric(1) + "PM";
        String ward = "Ward" + RandomStringUtils.randomNumeric(2);
        String specialty = "Specialty" + RandomStringUtils.randomNumeric(2);

        String[] dummyDoctor = { doctorID, doctorName, timingStart, timingEnd, ward, specialty };
        doctorsList.add(dummyDoctor);

    }

    private static void addDummyReceptionist() {
        String receptionistID = generateID("Receptionist");
        String receptionistName = "Receptionist" + RandomStringUtils.randomNumeric(2);
        String password = RandomStringUtils.randomAlphanumeric(8);

        String[] dummyReceptionist = { receptionistID, receptionistName, password };
        receptionistsList.add(dummyReceptionist);

    }

    private static void addDummyWard() {
        String wardID = generateID("Ward");
        String wardName = "Ward" + RandomStringUtils.randomNumeric(2);
        String totalBeds = RandomStringUtils.randomNumeric(2);
        String occupiedBeds = RandomStringUtils.randomNumeric(1);
        String wardType = "Type" + RandomStringUtils.randomNumeric(2);

        String[] dummyWard = { wardID, wardName, totalBeds, occupiedBeds, wardType };
        wardsList.add(dummyWard);

    }

    private static void addDummyPatient() {
        String patientID = generateID("Patient");
        String patientName = "Patient" + RandomStringUtils.randomNumeric(2);
        String gender = (RandomUtils.nextInt(0, 0) == 1) ? "Male" : "Female";
        String age = RandomStringUtils.randomNumeric(2);
        String contact = RandomStringUtils.randomNumeric(10);

        String[] dummyPatient = { patientID, patientName, gender, age, contact };
        patientsList.add(dummyPatient);

    }

  
    // Add similar functions for other entities (8 more for each entity)

    private static void createEmptyFakeFiles() {
        for (int i = 0; i < 10; ++i) {
            addDummyDoctor();
            addDummyReceptionist();
            addDummyWard();
            addDummyPatient();
            
        }
        updateDatabaseFile(DOCTORS_FILENAME, doctorsList);
        updateDatabaseFile(APPOINTMENTS_FILE_NAME, appointmentsList);
        updateDatabaseFile(DIAGNOSIS_FILE_NAME, diagnosisList);
        updateDatabaseFile(PATIENTS_FILE_NAME, patientsList);
        updateDatabaseFile(WARDS_FILE_NAME, wardsList);
        updateDatabaseFile(RECEPTIONISTS_FILE_NAME, receptionistsList);
        updateDatabaseFile("VariablesArray");
    }

    // here all the files will be checked, if the system in running for the first
    // time, all files should be made empty, or added with fake data :TODO
    public static void createEmptyDatabaseFiles() {
        if (!systemVariablesFilePath.exists()) {
            System.out.println(
                    "The system is running for the first time!! . You are required to setup the admin Password");
            createEmptySystemVariablesFile();
            
            String pass;
            do {
                System.out.print("Enter Password:");
                pass = scanner.nextLine();
            } while (!setAdminPassword(pass));
            createEmptyFakeFiles();
            updateDatabaseFile("VariablesArray");
        }
    }

    // function to display options of the main portal
    static void displayOptionsMainPortal() {
        System.out.println("\n=== Menu ===");
        System.out.println("1. Navigate to Admin Portal ");
        System.out.println("2. Navigate to Receptionist Portal");
        System.out.println("3. Navigate to Doctors Portal");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");
    }

    // function to display options of the admin portal
    static void displayOptionsAdminPortal() {
        System.out.println("\n=== Admin Portal Menu ===");
        System.out.printf("%-2s %-24s\t%-2s %-24s\t%-2s %-24s\t%-2s %-24s\n", "1", "Add Doctors Data", "6",
                "Add Receptionists", "10", "Add Ward", "14", "Return to Main Menu");
        System.out.printf("%-2s %-24s\t%-2s %-24s\t%-2s %-24s\n", "2", "Edit Doctors Data", "7", "Edit Receptionists",
                "11", "Edit Wards");
        System.out.printf("%-2s %-24s\t%-2s %-24s\t%-2s %-24s\n", "3", "Delete Doctors Data", "8",
                "Delete Receptionists", "12", "Delete Wards");
        System.out.printf("%-2s %-24s\t%-2s %-24s\t%-2s %-24s\n", "4", "Display all Doctors", "9", "Display Receptions",
                "13", "Display Wards");
        System.out.printf("%-2s %-24s\t\n", "5", "Display Specific Doctor");
    }

    //function to display options of doctors portal
    static void displayOptionsDoctorsPortal() {
        System.out.println("\n=== Doctors Portal Menu ===");
        System.out.println("1. Add Diagnosis Information");
        System.out.println("2. Full History of Patient");
        System.out.println("3. Check Upcoming Appointments");
        System.out.println("4. Return to Main Menu");
    }
   
    // main portal to ask the user to be directed to.
    public static void mainPortal() {
        boolean flag = true;
        while (flag) {
            displayOptionsMainPortal();
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    // Navigate to Admin Portal
                    adminPortal();
                    break;
                case 2:
                    // Navigate to Doctors PortalA
                    DoctorsPortal();
                    break;
                case 3:
                    // Navigate to the case statement handling the receptionist portal
                    break;
                case 4:
                    flag = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    // Now we are making a function for admin login it works only if the user has
    // the correct password
    // and login Name.
    static boolean isAdminPasswordCorrect() {
        System.out.println("Enter your Password: ");
        String enteredAdminPassword = scanner.nextLine();
        return enteredAdminPassword.equals(adminPassword);
    }

    public static boolean passwordChecker() {
        // Checking if the password is correct
        int loginTries = 1;
        while (!isAdminPasswordCorrect()) {
            System.out.println("Wrong Password! " + (5 - loginTries) + " tries left.");

            if (loginTries == 5) {
                System.out.println(
                        "You have entered the wrong password " + loginTries + " times, returning to Main Menu");

                return false; // Exit the method to avoid the unnecessary check below
            }
            loginTries++;

        }

        // This code is reached only if the password is correct
        System.out.println("Password is Correct");
        return true;
    }

    // This is the admin portal. It has the ability to add and remove doctors,
    // receptionists and wards
    public static void adminPortal() {

        if (passwordChecker()) {
            boolean exit = false;
            while (!exit) {
                // displaying options for admin portal
                displayOptionsAdminPortal();
                // inputting user for admin portal options
                System.out.print("Enter your choice: ");
                int choiceAdminPortal = scanner.nextInt();
                scanner.nextLine();
                switch (choiceAdminPortal) {
                    case 1:
                        System.out.println("You selected: Add Doctors Data");
                        // Add your logic for this option
                        addDoctorFromUserInput(doctorsList, scanner);
                        break;
                    case 2:
                        System.out.println("You selected: Edit Doctors Data");
                        editDoctorDetails(doctorsList, scanner);
                        break;
                    case 3:
                        System.out.println("You selected: Delete Doctors Data");
                        deleteDoctorFromUserInput(doctorsList, scanner);
                        break;
                    case 4:
                        System.out.println("You selected: Display all Doctors");
                        displayAllDoctors(doctorsList, appointmentsList);
                        break;
                    case 5:
                        System.out.println("You selected: Display Specific Doctor's Data");
                        viewSpecificDoctorDetails(doctorsList, scanner);
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

                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid choice");
                }
            }
        }
    }


    //this is the doctors portal. It will add diagnosis information,check patient's
    //history and check upcoming appointments for doctor
    public static void DoctorsPortal() {
        displayOptionsDoctorsPortal();
        System.out.print("Enter your choice: ");
        int choiceDoctorsPortal = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        switch (choiceDoctorsPortal) {
            case 1:
                System.out.println("You selected: Add Diagnosis Information");
                addDiagnosisInformation(diagnosisList, scanner);
                break;
            case 2:
                System.out.println("You selected: Full History of Patient");
                displayFullPatientHistory(diagnosisList, scanner);
                break;
            case 3:
                System.out.println("You selected: Check Upcoming Appointments");
                checkUpcomingAppointments(appointmentsList, scanner);
                break;
            case 4:
                System.out.println("You selected: Return to Main Menu");
                mainPortal();
                break;
            default:
                System.out.println("Invalid choice");
        }
    }

    /*
     * Functions to Generate ID are below:
     * These include checking if a file for Entity Numbers exits and adding initial
     * 0 values in a newly created file
     * Reading the file and writing values back in it
     */

    // checking if systemVariablesFile is created, if not, then it is created with
    // initial
    // values staring from 0
    public static void createEmptySystemVariablesFile() {
        try {
            // Check if the file doesn't exist, create it
            if (!systemVariablesFilePath.exists()) {
                systemVariablesFilePath.createNewFile();
                FileOutputStream fos = new FileOutputStream(systemVariablesFilePath, false);
                PrintStream ps = new PrintStream(fos);

                // Storing 0 values in new file
                for (int i = 0; i < numberOfEntitiesArray.length; i++) {
                    ps.println(0);
                }
                ps.close();
                fos.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // It is to be called by Updating thread only, precondition for this is to have
    // the empty file., it overwrites the previous file
    public static void updateSystemVariablesFile() {
        try (FileOutputStream fos = new FileOutputStream(systemVariablesFilePath, false)) {
            PrintStream ps = new PrintStream(fos);
            ps.println(adminPassword);
            for (int i = 0; i < numberOfEntitiesArray.length; i++) {
                ps.println(numberOfEntitiesArray[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Load the system variables like the total number of entites and admin password
    public static void loadSystemVariables() {
        try (FileReader fr = new FileReader(systemVariablesFilePath);
                Scanner inputGetter = new Scanner(fr)) {

            int i = 0;
            while (inputGetter.hasNextLine() && i < numberOfEntitiesArray.length) {
                if (i == 0) {
                    adminPassword = inputGetter.nextLine();
                } else {
                    numberOfEntitiesArray[i] = Integer.parseInt(inputGetter.nextLine());
                }
                ++i;
            }
        } catch (Exception e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }
    }

    private static String generateID(String entityType) {

        switch (entityType) {
            case "Doctor":
                numberOfEntitiesArray[0] += 1;
                updateDatabaseFile("Doctor");
                return "DOC-" + String.format("%03d", numberOfEntitiesArray[0]);
            case "Receptionist":
                numberOfEntitiesArray[1] += 1;
                updateDatabaseFile("Receptionist");
                return "REP-" + String.format("%03d", numberOfEntitiesArray[1]);
            case "Ward":
                numberOfEntitiesArray[2] += 1;
                updateDatabaseFile("Ward");
                return "W-" + String.format("%03d", numberOfEntitiesArray[2]);
            case "Patient":
                numberOfEntitiesArray[3] += 1;
                updateDatabaseFile("Patient");
                return "P-" + String.format("%03d", numberOfEntitiesArray[3]);
            case "Diagnosis":
                numberOfEntitiesArray[4] += 1;
                updateDatabaseFile("Diagnosis");
                return "DIA-" + String.format("%03d", numberOfEntitiesArray[4]);
            case "Appointment":
                numberOfEntitiesArray[5] += 1;
                updateDatabaseFile("Appointment");
                return "APT-" + String.format("%03d", numberOfEntitiesArray[5]);
            default:
                return "";
        }

    }

    // --------------------------------------------------------------------------------------------------------------------//
    /*
     * Functions to Read Data into their corresponding ArrayLists are below:
     * These include functions for Doctor Data, Receptionist Data, Ward Data,
     * Patient Data, Diagnosis Data,
     * Appointment Data.
     * These are all then connected to a main Data Storage Function
     * mainDataReader();
     */
    // 1-Doctor Data, 2-Receptionist Data, 3-Ward Data, 4-Patient Data, 5-Diagnosis
    // Data, 6-Appointment Data.
    // Main Data reader menu containing all the functions for other readers
    // Changing mainDataReader to read all data once the program boots

    public static void loadDatabaseFiles() {
        loadSystemVariables();
        readDataToArrayList(doctorDataFilePath, doctorsList);
        readDataToArrayList(receptionistDataFilePath, receptionistsList);
        readDataToArrayList(wardDataFilePath, wardsList);
        readDataToArrayList(patientDataFilePath, patientsList);
        readDataToArrayList(diagnosisDataFilePath, diagnosisList);
        readDataToArrayList(appointmentsDataFilePath, appointmentsList);

    }

    private static void readDataToArrayList(File file, ArrayList<String[]> dataList) {
        if (file.exists()) {
            try (Scanner reading = new Scanner(file)) {
                while (reading.hasNextLine()) {
                    String[] data = reading.nextLine().replaceAll("[\\[\\]]", "").split(", ");
                    dataList.add(data);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }
    // --------------------------------------------------------------------------------------------------------------------//

    /*
     * Functions to write Data into their corresponding txt files are below:
     * These include functions for Doctor Data, Receptionist Data, Ward Data,
     * Patient Data, Diagnosis Data,
     * Appointment Data.
     * These are all then connected to a main Data Storage Function
     * mainDataWriter();
     */

    // Store the doctor data to the txt file
    public static void storeDoctorToFile(String doctorsData) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(doctorDataFilePath, true))) {
            writer.println(doctorsData);
        } catch (Exception e) {
            System.out.println(
                    "\nThere is an error saving the data to the file. Make sure the file is not opened anywhere.");
            e.printStackTrace();
        }
    }

    // Rest functions to be added...

    // --------------------------------------------------------------------------------------------------------------------//

    /*
     * Functions to manipulate doctor to the array (which are then updated to the
     * database txt file) are below:
     * These include add, edit, delete, display all, display specific doctors
     * Use with care ;)
     */

    // Add a new doctor to the database based on user input
    private static void addDoctorFromUserInput(ArrayList<String[]> doctorsList, Scanner scanner) {
        System.out.println("\nEnter details for the new doctor:");
        System.out.print("Enter doctor name: ");
        String name = scanner.nextLine();
        System.out.print("Enter doctor start(e.g 10AM): ");
        String timingStart = scanner.nextLine();
        System.out.print("Enter doctor end(e.g 3PM): ");
        String timingEnd = scanner.nextLine();
        System.out.print("Enter doctor ward(e.g Cardiology): ");
        String ward = scanner.nextLine();
        System.out.print("Enter doctor specialty(e.g Cardiologist): ");
        String specialty = scanner.nextLine();

        // Creating a new array to store doctor details
        String[] newDoctor = { generateID("Doctor"), name, timingStart, timingEnd, ward, specialty };

        // Adding the new doctor to the ArrayList
        // Although I think since we are reading data from the txt file, adding to
        // ArrayList would be pointless since
        // it will be overwritten every time from the adminPortal() function when a new
        // doctor is added
        // One thing we can do is only read once at the start of program instead of
        // reading everytime??
        doctorsList.add(newDoctor);

        // Adding the doctor to the file data
        String docData = Arrays.toString(newDoctor);
        storeDoctorToFile(docData);
        System.out.println("New doctor added successfully.");
    }

    // Display all doctors in the database
    private static void displayAllDoctors(ArrayList<String[]> doctorsList, ArrayList<String[]> appointmentsList) {
        System.out.println("\nList of Doctors:");
        System.out.printf("%-10s %-20s %-15s %-15s %-20s %-20s %-15s\n", "Doctor ID", "Doctor Name", "Start Time",
                "End Time", "Ward", "Specialty", "Appointments Count");

        for (String[] doctor : doctorsList) {
            String doctorId = doctor[0];
            String doctorName = doctor[1];
            String startTime = doctor[2];
            String endTime = doctor[3];
            String ward = doctor[4];
            String specialty = doctor[5];

            // Count the number of appointments made by each doctor
            long appointmentsCount = countAppointmentsForDoctor(appointmentsList, doctorId);

            System.out.printf("%-10s %-20s %-15s %-15s %-20s %-20s %-15s\n", doctorId, doctorName, startTime, endTime,
                    ward, specialty, appointmentsCount);
        }
    }

    // Helper method to count the number of appointments made by a specific doctor
    private static long countAppointmentsForDoctor(ArrayList<String[]> appointmentsList, String doctorId) {
        return appointmentsList.stream().filter(appointment -> doctorId.equals(appointment[2])).count();
    }

    // Get the index of a doctor by ID
    private static int getDoctorIndexById(ArrayList<String[]> doctorsList, String id) {
        for (int i = 0; i < doctorsList.size(); i++) {
            if (id.equals(doctorsList.get(i)[0])) {
                return i;
            }
        }
        return -1;
    }

    // Edit details of a doctor based on user input (by name or ID)
    private static void editDoctorDetails(ArrayList<String[]> doctorsList, Scanner scanner) {
        System.out.println("\nSelect the option to edit:");
        System.out.println("1. Edit by Name");
        System.out.println("2. Edit by ID");
        System.out.print("Enter your choice: ");
        int editOption = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        switch (editOption) {
            case 1:
                editDoctorByName(doctorsList, scanner);
                break;
            case 2:
                editDoctorById(doctorsList, scanner);
                break;
            default:
                System.out.println("Invalid edit option. No changes made.");
        }
    }

    // Helper method to edit details of a doctor by name
    private static void editDoctorByName(ArrayList<String[]> doctorsList, Scanner scanner) {
        System.out.print("Enter the name of the doctor to edit details: ");
        String doctorNameToEdit = scanner.nextLine();

        // Check if the doctor name exists in the database
        int doctorIndexToEdit = getDoctorIndexByName(doctorsList, doctorNameToEdit);

        if (doctorIndexToEdit != -1) {
            editDoctorDetails(doctorIndexToEdit, doctorsList, scanner);
            // Update the file after editing
            updateDatabaseFile(DOCTORS_FILENAME, doctorsList);
            System.out.println("Details of Doctor '" + doctorNameToEdit + "' have been edited.");
        } else {
            System.out.println("Doctor with name '" + doctorNameToEdit + "' not found in the database.");
        }
    }

    // Helper method to edit details of a doctor by ID
    private static void editDoctorById(ArrayList<String[]> doctorsList, Scanner scanner) {
        System.out.print("Enter the ID of the doctor to edit details: ");
        String doctorIdToEdit = scanner.nextLine();

        // Check if the doctor ID exists in the database
        int doctorIndexToEdit = getDoctorIndexById(doctorsList, doctorIdToEdit);

        if (doctorIndexToEdit != -1) {
            editDoctorDetails(doctorIndexToEdit, doctorsList, scanner);
            // Update the file after editing
            updateDatabaseFile(DOCTORS_FILENAME, doctorsList);
            System.out.println("Details of Doctor '" + doctorIdToEdit + "' have been edited.");
        } else {
            System.out.println("Doctor with ID '" + doctorIdToEdit + "' not found in the database.");
        }
    }

    // Helper method to edit details of a doctor at a specific index
    private static void editDoctorDetails(int index, ArrayList<String[]> doctorsList, Scanner scanner) {
        String[] doctorDetails = doctorsList.get(index);
        System.out.println("Current Details of Doctor '" + index + "': " + Arrays.toString(doctorDetails));

        System.out.println("\nSelect the option to edit:");
        System.out.println("1. Edit Doctor Name");
        System.out.println("2. Edit Doctor Start Time");
        System.out.println("3. Edit Doctor End Time");
        System.out.println("4. Edit Doctor Ward");
        System.out.println("5. Edit Doctor Specialty");
        System.out.print("Enter your choice: ");
        int editChoice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        switch (editChoice) {
            case 1:
                System.out.print("Enter new doctor name: ");
                String newName = scanner.nextLine();
                doctorDetails[1] = newName;
                break;
            case 2:
                System.out.print("Enter new start time: ");
                String newStartTime = scanner.nextLine();
                doctorDetails[2] = newStartTime;
                break;
            case 3:
                System.out.print("Enter new end time: ");
                String newEndTime = scanner.nextLine();
                doctorDetails[3] = newEndTime;
                break;
            case 4:
                System.out.print("Enter new ward: ");
                String newWard = scanner.nextLine();
                doctorDetails[4] = newWard;
                break;
            case 5:
                System.out.print("Enter new specialty: ");
                String newSpecialty = scanner.nextLine();
                doctorDetails[5] = newSpecialty;
                break;
            default:
                System.out.println("Invalid edit choice. No changes made.");
        }

        System.out
                .println("Details of Doctor '" + doctorDetails[0] + "' after edit: " + Arrays.toString(doctorDetails));
    }

    // Delete a doctor from the database based on user input (by name or ID)
    private static void deleteDoctorFromUserInput(ArrayList<String[]> doctorsList, Scanner scanner) {
        System.out.println("Choose the option to delete:");
        System.out.println("1. Delete by Name");
        System.out.println("2. Delete by ID");
        System.out.print("Enter your choice: ");

        int deleteOption = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        switch (deleteOption) {
            case 1:
                deleteDoctorByName(doctorsList, scanner);
                break;
            case 2:
                deleteDoctorById(doctorsList, scanner);
                break;
            default:
                System.out.println("Invalid choice. No changes made.");
        }
    }

    // Delete a doctor from the database based on user input (by name)
    private static void deleteDoctorByName(ArrayList<String[]> doctorsList, Scanner scanner) {
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

    // Delete a doctor from the database based on user input (by ID)
    private static void deleteDoctorById(ArrayList<String[]> doctorsList, Scanner scanner) {
        System.out.print("\nEnter the ID of the doctor to delete: ");
        String doctorIdToDelete = scanner.nextLine();

        // Check if the doctor ID exists in the database
        int doctorIndexToDelete = getDoctorIndexById(doctorsList, doctorIdToDelete);

        if (doctorIndexToDelete != -1) {
            // Delete the doctor
            doctorsList.remove(doctorIndexToDelete);
            System.out.println("Doctor with ID '" + doctorIdToDelete + "' has been deleted.");
        } else {
            System.out.println("Doctor with ID '" + doctorIdToDelete + "' not found in the database.");
        }
    }

    // View specific doctor details based on user input (by name or ID)
    private static void viewSpecificDoctorDetails(ArrayList<String[]> doctorsList, Scanner scanner) {
        System.out.println("\n View Specific Doctor Details :");
        System.out.println("1. by Name ");
        System.out.println("2. by ID ");
        System.out.print("Enter your choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        switch (choice) {
            case 1:
                viewDoctorDetailsByName(doctorsList, scanner);
                break;
            case 2:
                viewDoctorDetailsById(doctorsList, scanner);
                break;
            default:
                System.out.println("Invalid choice. ");
        }
    }

    // View specific doctor details based on user input (by name)
    private static void viewDoctorDetailsByName(ArrayList<String[]> doctorsList, Scanner scanner) {
        System.out.print("\nEnter the name of the doctor to view details: ");
        String doctorNameToView = scanner.nextLine();

        // Check if the doctor name exists in the database
        int doctorIndexToView = getDoctorIndexByName(doctorsList, doctorNameToView);

        if (doctorIndexToView != -1) {
            // View the details of the specific doctor
            String[] doctorDetails = doctorsList.get(doctorIndexToView);
            System.out.println("Details of Doctor '" + doctorNameToView + "': " + Arrays.toString(doctorDetails));
        } else {
            System.out.println("Doctor with name '" + doctorNameToView + "' not found in the database.");
        }
    }

    // View specific doctor details based on user input (by ID)
    private static void viewDoctorDetailsById(ArrayList<String[]> doctorsList, Scanner scanner) {
        System.out.print("\nEnter the ID of the doctor to view details: ");
        String doctorIdToView = scanner.nextLine();

        // Check if the doctor ID exists in the database
        int doctorIndexToView = getDoctorIndexById(doctorsList, doctorIdToView);

        if (doctorIndexToView != -1) {
            // View the details of the specific doctor
            String[] doctorDetails = doctorsList.get(doctorIndexToView);
            System.out.println("Details of Doctor '" + doctorIdToView + "': " + Arrays.toString(doctorDetails));
        } else {
            System.out.println("Doctor with ID '" + doctorIdToView + "' not found in the database.");
        }
    }

    // Get the index of a doctor by name
    private static int getDoctorIndexByName(ArrayList<String[]> doctorsList, String name) {
        for (int i = 0; i < doctorsList.size(); i++) {
            if (name.equals(doctorsList.get(i)[1])) {
                return i;
            }
        }
        return -1;
    }

    public static void updateDatabaseFile(String filePath, ArrayList<String[]> dataList) {
        Runnable r2 = () -> {
            try {
                FileWriter docFWriter = new FileWriter(filePath, false);
                PrintWriter pw = new PrintWriter(docFWriter);
                for (String[] data : dataList) {
                    pw.println(Arrays.toString(data));
                }
                pw.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        Thread t = new Thread(r2);
        t.start();

    }
    // Add a new patient's diagnosis information based on user input
    private static void addDiagnosisInformation(ArrayList<String[]> diagnosisList, Scanner scanner) {
        System.out.print("Enter patient name: ");
        String patientName = scanner.nextLine();
        System.out.print("Enter symptoms: ");
        String symptoms = scanner.nextLine();
        System.out.print("Enter diagnosis: ");
        String diagnosis = scanner.nextLine();
        System.out.print("Enter prescribed medicines: ");
        String medicines = scanner.nextLine();
        System.out.print("Need to be hospitalized? (yes/no): ");
        String needHospitalization = scanner.nextLine();
        String suggestedWard = "";
        if (needHospitalization.equalsIgnoreCase("yes")) {
            System.out.print("Suggested Ward: ");
            suggestedWard = scanner.nextLine();
        }
        System.out.print("Additional Notes: ");
        String additionalNotes = scanner.nextLine();

        // Creating a new array to store diagnosis information
        String[] newDiagnosis = {patientName, symptoms, diagnosis, medicines, needHospitalization, suggestedWard, additionalNotes};

        // Adding the new diagnosis to the ArrayList
        diagnosisList.add(newDiagnosis);

        System.out.println("Diagnosis information added successfully.");
    }
    // Display full history of a patient based on user input (by name or ID)
    private static void displayFullPatientHistory(ArrayList<String[]> diagnosisList, Scanner scanner) {
        System.out.print("Enter patient name: ");
        String patientName = scanner.nextLine();

        // Check if the patient name exists in the database
        boolean patientFound = false;
        for (String[] diagnosis : diagnosisList) {
            if (diagnosis[0].equalsIgnoreCase(patientName)) {
                System.out.println("Full History of Patient '" + patientName + "': " + arrayToString(diagnosis));
                patientFound = true;
            }
        }

        if (!patientFound) {
            System.out.println("Patient with name '" + patientName + "' not found in the database.");
        }
    }

    // Display upcoming appointments for a doctor
    private static void checkUpcomingAppointments(ArrayList<String[]> appointmentsList, Scanner scanner) {
        System.out.print("Enter doctor name: ");
        String doctorName = scanner.nextLine();

        // Check if the doctor name exists in the database
        boolean doctorFound = false;
        for (String[] appointment : appointmentsList) {
            if (appointment[1].equalsIgnoreCase(doctorName)) {
                System.out.println("Upcoming Appointments for Doctor '" + doctorName + "': " + arrayToString(appointment));
                doctorFound = true;
            }     }

            if (!doctorFound) {
                System.out.println("Doctor with name '" + doctorName + "' not found in the database.");
            }
        }
    // Helper method to convert an array to a string for diagnosis information
    private static String arrayToString(String[] array) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            result.append(array[i]);
            if (i < array.length - 1) {
                result.append(", ");
            }
        }
        return result.toString();
    }
       
}