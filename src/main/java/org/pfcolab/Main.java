package org.pfcolab;
import java.io.*;
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

    //Different files for different entity data and their path stored in variables
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
    final static String NUMBER_OF_ENTITIES_FILE_NAME = "NumberofEntities.txt";
    static File entityDataFilePath = new File(NUMBER_OF_ENTITIES_FILE_NAME);

    //Password for admin
    static String savedAdminPassword = "kaleem";
    static String currentDir = System.getProperty("user.dir");
    final static String databaseFilePath = currentDir + "//" + DATABASE + ".accdb";

//--------------------------------------------------------------------------------------------------------------------//
    /*
    * Array lists for the stored data of multiple entries
    * We are using String array to store the data of one entity
    * The structure of each Entity is commented before declaration the line
     */



    //['Doc ID', 'Doc Name' , 'Time Start', 'Time End', 'Ward Name', 'Specialized In']
    static ArrayList<String[]> doctorsList = new ArrayList<>();

    //['Recep ID', 'Receptionist Name' , 'Password']
    static ArrayList<String[]> receptionistsList = new ArrayList<>();

    //['Ward ID', 'Ward Name' , 'Total Beds','Occupied Beds','Type']
    static ArrayList<String[]> wardsList = new ArrayList<>();

    //['Patient ID', 'Patient Name' , 'Gender','Age','Contact']
    static ArrayList<String[]> patientsList = new ArrayList<>();

    //['Diagnosis ID', 'Patient ID' , 'Doctor ID','Prescriptions','Diagnosis','Appointment ID']
    static ArrayList<String[]> diagnosisList = new ArrayList<>();

    //['Appointment ID', 'Patient ID' , 'Doctor ID','Time Start','Time End']
    static ArrayList<String[]> appointmentsList = new ArrayList<>();

    static int numberOfDoctors,numberOfReceptionists,numberOfWards,numberOfPatients,numberOfDiagnosis,numberOfAppointments;
    static int[] numberOfEntitiesArray = {numberOfDoctors, numberOfReceptionists, numberOfWards, numberOfPatients,
            numberOfDiagnosis, numberOfAppointments};

//--------------------------------------------------------------------------------------------------------------------//

    //main function! wow!! ;)
    public static void main(String[] args) {
        //Reading all the data after the program boots
        mainDataReader();
        //opening the main portal
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
        System.out.println("\n=== Admin Portal Menu ===");
        System.out.printf("%-2s %-24s\t%-2s %-24s\t%-2s %-24s\t%-2s %-24s\n", "1", "Add Doctors Data", "6", "Add Receptionists", "10", "Add Ward", "14", "Return to Main Menu");
        System.out.printf("%-2s %-24s\t%-2s %-24s\t%-2s %-24s\n", "2", "Edit Doctors Data", "7", "Edit Receptionists", "11", "Edit Wards");
        System.out.printf("%-2s %-24s\t%-2s %-24s\t%-2s %-24s\n", "3", "Delete Doctors Data", "8", "Delete Receptionists", "12", "Delete Wards");
        System.out.printf("%-2s %-24s\t%-2s %-24s\t%-2s %-24s\n", "4", "Display all Doctors", "9", "Display Receptions", "13", "Display Wards");
        System.out.printf("%-2s %-24s\t\n", "5", "Display Specific Doctor");
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

    //Now we are making a function for admin login it works only if the user has the correct password
    //and login Name.
    static boolean isAdminPasswordCorrect() {
        System.out.println("Enter your Password: ");
        String enteredAdminPassword = scanner.nextLine();
        System.out.println(enteredAdminPassword);
        System.out.println(savedAdminPassword);
        return enteredAdminPassword.equals(savedAdminPassword);
    }

    public static void passwordChecker() {
        // Checking if the password is correct
        int loginTries = 0;
        while (!isAdminPasswordCorrect()) {
            System.out.println("Wrong Password!");
            loginTries++;
            if (loginTries == 5) {
                System.out.println("You have entered the wrong password 5 times, returning to Main Menu");
                mainPortal();
                return; // Exit the method to avoid the unnecessary check below
            }
        }

        // This code is reached only if the password is correct
        System.out.println("Password is Correct");
    }

    //This is the admin portal. It has the ability to add and remove doctors, receptionists and wards
    public static void adminPortal() {

        passwordChecker();
        boolean exit = false;
        while (!exit)
        {
            //displaying options for admin portal
            displayOptionsAdminPortal();
            //inputting user for admin portal options
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
                    // Add your logic for this option
                    break;
                case 3:
                    System.out.println("You selected: Delete Doctors Data");
                    // Add your logic for this option
                    break;
                case 4:
                    System.out.println("You selected: Display all Doctors");
                    displayAllDoctors(doctorsList);
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
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    /* Functions to Generate ID are below:
    * These include checking if a file for Entity Numbers exits and adding initial 0 values in a newly created file
    * Reading the file and writing values back in it
    */

    //checking if entity file is created, if not, then it is created with initial values staring from 0
    public static void entityFileCreator() {

        try {

            // Check if the file doesn't exist, create it
            if (!entityDataFilePath.exists())
            {
                entityDataFilePath.createNewFile();
                FileOutputStream fos = new FileOutputStream(entityDataFilePath, false);
                PrintStream ps = new PrintStream(fos);

                // Storing 0 values in new file
                for (int i = 0; i < 6; i++) {
                    ps.println(0);
                }
                ps.close();
                //new file is created with initial 0 values so that there is no conflict when reading
                //initially, although it will not be used because the database will be initialized,
                //I made it because I felt like it otherwise the system won't be complete.
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Function reads the number of entities present in the files updating their values in numberOfEntitiesArray[i]
    public static void entityReaderFromFile() {
        try {
            // Check if the file doesn't exist, create it and initialize 0 values
            entityFileCreator();
            Scanner in = new Scanner(entityDataFilePath);
            // Taking the values of the number of stored entities from the Number-of-Entities file
            // numberOfEntitiesArray[] = [doctors, receptionists, wards, patients, diagnosis, appointments]
            // Using a for loop to make it input values from the text file
            for (int i = 0; i < 6; i++) {
                numberOfEntitiesArray[i] = in.nextInt();
            }
            in.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // This function will update the number of Entities after they have been updated during
    // new entity ID creating, which occurs when a new Entity is created.
    public static void entityUpdaterToFile() {

        try {
            FileOutputStream fos = new FileOutputStream(entityDataFilePath, false);
            PrintStream ps = new PrintStream(fos);

            // Now we have to store the updated values in the NumberofEntities file
            for (int i = 0; i < 6; i++) {
                ps.println(numberOfEntitiesArray[i]);
            }
            ps.close(); // Close the stream after writing
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static String generateID(String entityType) {

        entityReaderFromFile();
        switch (entityType) {
            case "Doctor":
                numberOfEntitiesArray[0]+=1;
                entityUpdaterToFile();
                return "DOC-" + String.format("%03d", numberOfEntitiesArray[0]);
            case "Receptionist":
                numberOfEntitiesArray[1]+=1;
                entityUpdaterToFile();
                return "REP-" + String.format("%03d", numberOfEntitiesArray[1]);
            case "Ward":
                numberOfEntitiesArray[2]+=1;
                entityUpdaterToFile();
                return "W-" + String.format("%03d", numberOfEntitiesArray[2]);
            case "Patient":
                numberOfEntitiesArray[3]+=1;
                entityUpdaterToFile();
                return "P-" + String.format("%03d", numberOfEntitiesArray[3]);
            case "Diagnosis":
                numberOfEntitiesArray[4]+=1;
                entityUpdaterToFile();
                return "DIA-" + String.format("%03d", numberOfEntitiesArray[4]);
            case "Appointment":
                numberOfEntitiesArray[5]+=1;
                entityUpdaterToFile();
                return "APT-" + String.format("%03d", numberOfEntitiesArray[5]);
            default:
                return "";
        }
    }

//--------------------------------------------------------------------------------------------------------------------//
    /* Functions to Read Data into their corresponding ArrayLists are below:
     * These include functions for Doctor Data, Receptionist Data, Ward Data, Patient Data, Diagnosis Data,
     * Appointment Data.
     * These are all then connected to a main Data Storage Function mainDataReader();
     */

    //1-Doctor Data, 2-Receptionist Data, 3-Ward Data, 4-Patient Data, 5-Diagnosis Data, 6-Appointment Data.
    //Main Data reader menu containing all the functions for other readers
    //Changing mainDataReader to read all data once the program boots
    public static void mainDataReader() {
    //For now keeping dataNumber<=1 because we have not made other txt files database
        for (int dataNumber = 1; dataNumber <= 1; dataNumber++) {

            switch (dataNumber) {
                case 1:
                    // Read doctors' data
                    readDoctorsDataToArrayList();
                    break;
                case 2:
                    // Read receptionists' data
                    readReceptionistsDataToArrayList();
                    break;
                case 3:
                    // Read ward data
                    readWardsDataToArrayList();
                    break;
                case 4:
                    // Read patient data
                    readPatientsDataToArrayList();
                    break;
                case 5:
                    // Read diagnosis data
                    readDiagnosisDataToArrayList();
                    break;
                case 6:
                    // Read appointment data
                    readAppointmentsDataToArrayList();
                    break;
                default:
                    System.out.println("Invalid data number.");
            }
        }
    }


    //Function to read doctor data from the file containing all the doctors
    //['Doc ID', 'Doc Name' , 'Time Start', 'Time End', 'Ward Name', 'Specialized In']
    public static void readDoctorsDataToArrayList() {

        try {
            Scanner reading = new Scanner(doctorDataFilePath);
            while (reading.hasNextLine()) {
                String line = reading.nextLine();
                // Remove square brackets and split by comma and space
                String[] doctorDataArray = line.replaceAll("[\\[\\]]", "").split(", ");
                doctorsList.add(doctorDataArray);
            }
            reading.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //Reading Receptionist data into array list
    //['Recep ID', 'Receptionist Name' , 'Password']
    public static void readReceptionistsDataToArrayList() {
        try {
            Scanner reading = new Scanner(receptionistDataFilePath);
            while (reading.hasNextLine()) {
                String[] receptionistsDataArray = reading.nextLine().replaceAll("[\\[\\]]", "")
                        .split(", ");
                receptionistsList.add(receptionistsDataArray);
            }
            reading.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //['Ward ID', 'Ward Name' , 'Total Beds','Occupied Beds','Type']
    public static void readWardsDataToArrayList() {
        try {
            Scanner reading = new Scanner(wardDataFilePath);
            while (reading.hasNextLine()) {
                String[] wardsDataArray = reading.nextLine().replaceAll("[\\[\\]]", "").split(", ");
                wardsList.add(wardsDataArray);
            }
            reading.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //['Patient ID', 'Patient Name' , 'Gender','Age','Contact']
    public static void readPatientsDataToArrayList() {
        try {
            Scanner reading = new Scanner(patientDataFilePath);
            while (reading.hasNextLine()) {
                String[] patientsDataArray = reading.nextLine().replaceAll("[\\[\\]]", "").split(", ");
                patientsList.add(patientsDataArray);
            }
            reading.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //['Diagnosis ID', 'Patient ID' , 'Doctor ID','Prescriptions','Diagnosis','Appointment ID']
    public static void readDiagnosisDataToArrayList() {
        try {
            Scanner reading = new Scanner(diagnosisDataFilePath);
            while (reading.hasNextLine()) {
                String[] diagnosisDataArray = reading.nextLine().replaceAll("[\\[\\]]", "").split(", ");
                diagnosisList.add(diagnosisDataArray);
            }
            reading.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //['Appointment ID', 'Patient ID' , 'Doctor ID','Time Start','Time End']
    public static void readAppointmentsDataToArrayList() {
        try {
            Scanner reading = new Scanner(appointmentsDataFilePath);
            while (reading.hasNextLine()) {
                String[] appointmentsDataArray = reading.nextLine().replaceAll("[\\[\\]]", "").split(", ");
                appointmentsList.add(appointmentsDataArray);
            }
            reading.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


//--------------------------------------------------------------------------------------------------------------------//

    /* Functions to write Data into their corresponding txt files are below:
     * These include functions for Doctor Data, Receptionist Data, Ward Data, Patient Data, Diagnosis Data,
     * Appointment Data.
     * These are all then connected to a main Data Storage Function mainDataWriter();
     */

    //Store the doctor data to the txt file
    public static void storeDoctorToFile(String doctorsData) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(doctorDataFilePath, true))) {
            writer.println(doctorsData);
        } catch (Exception e) {
            System.out.println("\nThere is an error saving the data to the file. Make sure the file is not opened anywhere.");
            e.printStackTrace();
        }
    }

    //Rest functions to be added...

//--------------------------------------------------------------------------------------------------------------------//

    /* Functions to manipulate doctor to the array (which are then updated to the database txt file) are below:
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
        String[] newDoctor = {generateID("Doctor"),name, timingStart,timingEnd, ward, specialty};

        // Adding the new doctor to the ArrayList
        //Although I think since we are reading data from the txt file, adding to ArrayList would be pointless since
        //it will be overwritten every time from the adminPortal() function when a new doctor is added
        //One thing we can do is only read once at the start of program instead of reading everytime??
        doctorsList.add(newDoctor);

        //Adding the doctor to the file data
        String docData= Arrays.toString(newDoctor);
        storeDoctorToFile(docData);
        System.out.println("New doctor added successfully.");
    }

    // Display all doctors in the database
    private static void displayAllDoctors(ArrayList<String[]> doctorsList) {
        System.out.println("\nList of Doctors:");
        for (int i = 0; i < doctorsList.size(); i++) {
            System.out.println("Doctor ID: " + doctorsList.get(i)[0] + ", Doctor Info: "
                    + Arrays.toString(doctorsList.get(i)));
        }
    }

    // View specific doctor details based on user input (by name)
    private static void viewSpecificDoctorDetails(ArrayList<String[]> doctorsList, Scanner scanner) {
        System.out.println("\n View Specific Doctor Details :");
        System.out.println("1. by Name ");
        System.out.println("2. by ID ");
        System.out.print("Enter your choice: ");
        int choice= scanner.nextInt();
        scanner.nextLine();
        switch (choice){
            case 1:
                break;
            case 2:
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
                break;
            default:
                System.out.println("Invalid choice. ");

        }

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

    // Edit details of a doctor based on user input (by name)
    private static void editDoctorDetails(ArrayList<String[]> doctorsList, Scanner scanner) {
        System.out.print("\nEnter the ID of the doctor to edit details: ");
        String doctorIdToEdit = scanner.nextLine();

        // Check if the doctor ID exists in the database
        int doctorIndexToEdit = getDoctorIndexById(doctorsList, doctorIdToEdit);

        if (doctorIndexToEdit != -1) {
            // Edit the details of the specific doctor
            String[] doctorDetails = doctorsList.get(doctorIndexToEdit);
            System.out.println("Current Details of Doctor '" + doctorIndexToEdit + "': " + Arrays.toString(doctorDetails));

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

            System.out.println("Details of Doctor '" + doctorIndexToEdit + "' after edit: " + Arrays.toString(doctorDetails));
        } else
        {
            System.out.println("Doctor with name '" + doctorIndexToEdit + "' not found in the database.");
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
            if (name.equals(doctorsList.get(i)[1])) {
                return i;
            }
        }
        return -1;
    }


}