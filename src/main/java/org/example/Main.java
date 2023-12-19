package org.example;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class Main
{
	static final String DATABASE_DIR = "data";
	static final String DOCTORS_FILENAME = "data/Doctors.txt";
	static final String RECEPTIONISTS_FILE_NAME = "data/Receptionists.txt";
	static final String WARDS_FILE_NAME = "data/Wards.txt";
	static final String PATIENTS_FILE_NAME = "data/Patients.txt";
	static final String DIAGNOSIS_FILE_NAME = "data/Diagnosis.txt";
	static final String APPOINTMENTS_FILE_NAME = "data/Appointments.txt";
	static final String SYSTEM_VARIABLES_FILE_NAME = "data/SystemVariables.txt";
	// ['Doc ID', 'Doc Name' , 'Time Start', 'Time End', 'Ward Name', 'Specialized
	// In', 'username' , 'password']
	static ArrayList<String[]> doctorsList = new ArrayList<>();
	// ['Recep ID', 'Receptionist Name' , 'username','password']
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
	static File doctorDataFilePath = new File( DOCTORS_FILENAME );
	static File receptionistDataFilePath = new File( RECEPTIONISTS_FILE_NAME );
	static File wardDataFilePath = new File( WARDS_FILE_NAME );
	static File patientDataFilePath = new File( PATIENTS_FILE_NAME );
	static File diagnosisDataFilePath = new File( DIAGNOSIS_FILE_NAME );
	static File appointmentsDataFilePath = new File( APPOINTMENTS_FILE_NAME );
	static File systemVariablesFilePath = new File( SYSTEM_VARIABLES_FILE_NAME );
	static ArrayList<String> mainMenuOptions = new ArrayList<String>()
	{{
		add( "Main Menu" );
		add( "Admin Section" );
		add( "Doctor's Section" );
		add( "Receptionist's Section" );
	}};
	// { numberOfDoctors, numberOfReceptionists, numberOfWards, numberOfPatients,
	// numberOfDiagnosis, numberOfAppointments }
	//For taking into account how many entities have got registered so far, so whenever new entity gets registered,
	//its id will be generated uniquely by incrementing old number
	static int[] numberOfEntitiesArray = new int[6];

	//[adminUsername, adminPassword, numberOfEntitiesArray]
	static String[] globalVariablesArray = new String[8];

	static Scanner scanner = new Scanner( System.in );
	static String adminPassword = "kaleem";
	static String adminUsername = "kaleem";
	static int docUsernameIndex = 6;
	static int docPasswordIndex = 7;
	static int receptionistUsernameIndex = 2;
	static int receptionistPasswordIndex = 3;


	// Method to display center-aligned text
	private static void displayCenterAlignedText(String text, int lineWidth)
	{
		int totalPadding = lineWidth - text.length();
		int leftPadding = totalPadding / 2;
		int rightPadding = totalPadding - leftPadding;

		System.out.printf( "%s%s%s\n", "=".repeat( leftPadding ), text, "=".repeat( rightPadding ) );
	}

	public static void navigateMenu(List<String> menu)
	{
		int choice = -1;

		while (!menu.isEmpty())
		{
			int lineWidth = 40;

			displayCenterAlignedText( menu.get( 0 ), lineWidth );


			// Display the rest of the menu options
			for (int i = 1; i < menu.size(); i++)
			{
				System.out.printf( "%s %-" + (lineWidth - 2) + "s\n", "*".repeat( lineWidth / 4 ), (i) + ". " + menu.get( i ) );
			}

			displayCenterAlignedText( "0. Go back", lineWidth );


			System.out.print( "Enter your choice: " );
			while (!scanner.hasNextInt())
			{
				scanner.nextLine();
				System.out.println( "Please Enter only numbers" );
				System.out.print( "Enter your choice: " );
			}
			choice = scanner.nextInt();
			scanner.nextLine();

			if (choice == 0)
			{
				System.out.println( "Going back to the previous menu..." );
				return;
			}
			else if (choice > 0 && choice < menu.size())
			{
				String selectedSubMenu = menu.get( choice );
				if (handleSubMenu( selectedSubMenu ))
				{
					navigateMenu( getSubMenu( selectedSubMenu ) );
				}
			}
			else
			{
				System.out.println( "Invalid choice. Please try again." );
			}
		}
	}


	/*
	 * The menuNavigator Function requires the list of strings
	 * The first index of list will contain the menu name and other indices will be having the sub-menus
	 * of that menu
	 * The sub-menus will have further sub-menus , for getting the list of submenus based upon menu,
	 * we are making this function
	 * */
	public static List<String> getSubMenu(String menuName)
	{
		List<String> subMenu = new ArrayList<>();


		switch (menuName)
		{
			///For First Screen
			case "Main Menu":
				subMenu.add( "Main Menu" );
				subMenu.add( "Admin Section" );
				subMenu.add( "Doctor's Section" );
				subMenu.add( "Receptionist's Section" );
				break;
			// For admin Screen
			case "Admin Section":
				subMenu.add( "Admin Section" );
				subMenu.add( "Register New Doctor" );
				subMenu.add( "Edit Existing Doctor" );
				subMenu.add( "Get Doctor Details" );
				subMenu.add( "Remove Doctor" );
				subMenu.add( "Display All Doctors" );
				subMenu.add( "Register New Receptionist" );
				subMenu.add( "Remove Receptionist" );
				subMenu.add( "Edit Receptionist" );
				subMenu.add( "Display All Receptionist" );
				subMenu.add( "Add Ward" );
				subMenu.add( "Edit Ward" );
				subMenu.add( "Get Ward Details" );
				subMenu.add( "Remove Ward" );
				subMenu.add( "Display All Wards" );
				subMenu.add( "Fill Database With Fake Data" );
				break;
			//For Doctors Screen
			case "Doctor's Section":
				subMenu.add( "Doctor's Section" );
				subMenu.add( "Get Patient History" );
				subMenu.add( "Add Diagnosis" );
				subMenu.add( "Check Upcoming Appointments" );
				break;
			//For Receptionists Section
			case "Receptionist's Section":
				subMenu.add( "Receptionist's Section" );
				subMenu.add( "Add Patient" );
				subMenu.add( "Get Patient Details" );
				subMenu.add( "Edit Patient Details" );
				subMenu.add( "Submit Patient To Ward" );
				subMenu.add( "Check Doctor's Availability" );
				subMenu.add( "Get All Doctors" );
				break;
			case "Display All Wards":
				subMenu.add( "Display All Wards" );
				subMenu.add( "Filter Wards By Name" );
				break;

		}

		return subMenu;
	}

	//if there is some Function which is needed tobe called before specific submenu
	public static boolean handleSubMenu(String subMenu)
	{
		switch (subMenu)
		{
			case "Admin Section":
				return verifyLoginDetails( 1 );
			case "Doctor's Section":
				return verifyLoginDetails( 2 );
			case "Receptionist's Section":
				return verifyLoginDetails( 3 );
			case "Register New Doctor":
				registerNewDoctor();
				break;
			case "Edit Existing Doctor":
				handleEditDoctorDetailsMenu();
				break;
			case "Get Doctor Details":
				handleGetDoctorDetails();
				break;
			case "Remove Doctor":
				handleRemoveDoctor();
				break;
			case "Display All Doctors":
				handleDisplayAllDoctors();
				break;
			case "Register New Receptionist":
				handleRegisterNewReceptionist();
				break;
			case "Edit Receptionist":
				handleEditReceptionistDetailsMenu();
				break;
			case "Remove Receptionist":
				handleRemoveReceptionist();
				break;
			case "Display All Receptionist":
				displayAllReceptionists();
				break;
			case "Add Ward":
				addWard();
				break;
			case "Edit Ward":
				editWard();
				break;
			case "Get Ward Details":
				getWardDetails();
				break;
			case "Remove Ward":
				removeWard();
				break;
			case "Display All Wards":
				displayAllWards();
				break;
				/// Doctor
			case "Get Patient History":
				getPatientHistory();
				break;
			case "Add Diagnosis":
				addDiagnosis();
				break;
			case "Check Upcoming Appointments":
				checkUpcomingAppointments();
				break;
			case "Add Patient":
				addPatient();
				break;
			case "Get Patient Details":
				getPatientDetails();
				break;
			case "Edit Patient Details":
				editPatientDetails();
				break;
			case "Submit Patient To Ward":
				submitPatientToWard();
				break;
			case "Check Doctor's Availability":
				checkDoctorsAvailability();
				break;
			case "Get All Doctors":
				getAllDoctors();
				break;
			case "Fill Database With Fake Data":
				optionCreateFakeFiles();
				break;

			default:
				return false;
		}
		return false;
	}


	private static void registerNewDoctor()
	{

		System.out.print( " You are Registering a new Doctor, Enter -1 anytime to discard the process \n" );
		System.out.print( "Enter doctor name: " );
		String name = scanner.nextLine();
		if (name.trim().equals( "-1" )) return;
		System.out.print( "Enter doctor`s shift start time (e.g 10AM): " );
		String timingStart = scanner.nextLine();
		if (timingStart.trim().equals( "-1" )) return;
		System.out.print( "Enter doctor`s shift end time(e.g 3PM): " );
		String timingEnd = scanner.nextLine();
		if (timingEnd.trim().equals( "-1" )) return;
		System.out.print( "Enter doctor ward(e.g Cardiology): " );
		String ward = scanner.nextLine();
		if (ward.trim().equals( "-1" )) return;
		System.out.print( "Enter doctor specialty(e.g Cardiologist): " );
		String specialty = scanner.nextLine();
		if (specialty.trim().equals( "-1" )) return;
		System.out.print( "Enter doctor username: " );
		String username = scanner.nextLine();
		if (username.trim().equals( "-1" )) return;
		System.out.print( "Enter doctor password: " );
		String password = scanner.nextLine();
		if (password.trim().equals( "-1" )) return;

		// Creating a new array to store doctor details
		String[] newDoctor = {
				generateID( "Doctor" ),
				name,
				timingStart,
				timingEnd,
				ward,
				specialty,
				username,
				password,
		};

		doctorsList.add( newDoctor );

		// Adding the doctor to the file data
		updateDatabaseFile( "Doctor" );
		System.out.println( "New doctor added successfully." );
	}



	private static void handleEditDoctorDetailsMenu()
	{
		showNameIDSelectionMenu("Edit", "Doctor");
		int editOption = scanner.nextInt();
		scanner.nextLine();

		switch (editOption)
		{
			case 0:
				return;
			case 1:
				editDoctorByName();
				break;
			case 2:
				editDoctorByID();
				break;
			default:
				System.out.println( "Invalid edit option. No changes made." );
		}
	}

	public static void editDoctorByName()
	{
		int index = getEntityIndexByNameInput("Doctor", doctorsList);
		if (index != -1)
			editDoctor( index );
	}

	public static void editDoctorByID()
	{
		int index = getEntityByIDInput("Doctor", doctorsList);
		if (index != -1)
			editDoctor( index );
	}

	private static void editDoctor(int index)
	{

		String[] doctorDetails = doctorsList.get( index );
		System.out.println( "Current Details of Doctor '" + index + "': " + Arrays.toString( doctorDetails ) );


		System.out.println( "\nSelect the option to edit:" );
		System.out.println( "1. Edit Doctor Name" );
		System.out.println( "2. Edit Doctor Start Time" );
		System.out.println( "3. Edit Doctor End Time" );
		System.out.println( "4. Edit Doctor Ward" );
		System.out.println( "5. Edit Doctor Specialty" );
		System.out.println( "6. Cancel Operation" );
		System.out.print( "Enter your choice: " );
		int editChoice = scanner.nextInt();
		scanner.nextLine();

		switch (editChoice)
		{
			case 1:
				System.out.print( "Enter new doctor name: " );
				String newName = scanner.nextLine();
				doctorDetails[1] = newName;
				break;
			case 2:
				System.out.print( "Enter new shift start time: " );
				String newStartTime = scanner.nextLine();
				doctorDetails[2] = newStartTime;
				break;
			case 3:
				System.out.print( "Enter new shift end time: " );
				String newEndTime = scanner.nextLine();
				doctorDetails[3] = newEndTime;
				break;
			case 4:
				System.out.print( "Enter new ward: " );
				String newWard = scanner.nextLine();
				doctorDetails[4] = newWard;
				break;
			case 5:
				System.out.print( "Enter new specialty: " );
				String newSpecialty = scanner.nextLine();
				doctorDetails[5] = newSpecialty;
				break;
			case 6:
				System.out.println( "No changes made." );
				return;

			default:
				System.out.println( "Invalid edit choice. No changes made." );
		}
		updateDatabaseFile( "Doctor" );
		System.out.println( "Details of Doctor '" + doctorDetails[0] + "' after edit: " + Arrays.toString( doctorDetails ) );
	}

	private static void handleGetDoctorDetails()
	{
		showNameIDSelectionMenu("Show", "Doctor");
		int option = scanner.nextInt();
		scanner.nextLine();

		switch (option)
		{
			case 0:
				return;
			case 1:
				getDoctorDetailsByName();
				break;
			case 2:
				getDoctorDetailsByID();
				break;
			default:
				System.out.println( "Invalid edit option." );
		}
	}

	public static void getDoctorDetailsByName()
	{
		int index = getEntityIndexByNameInput("Doctor", doctorsList);
		if (index != -1)
			showDoctorDetails( index );
	}
	public static void getDoctorDetailsByID()
	{
		int index = getEntityByIDInput("Doctor", doctorsList);
		if (index != -1)
			showDoctorDetails( index );
	}
	private static void showDoctorDetails(int index)
	{
		String[] doctorDetails = doctorsList.get(index);
		System.out.println( "Details of Doctor '" + doctorDetails[1] + "': " + Arrays.toString(doctorDetails));
	}

	private static void handleRemoveDoctor()
	{
		showNameIDSelectionMenu("Remove", "Doctor");
		int editOption = scanner.nextInt();
		scanner.nextLine();

		switch (editOption)
		{
			case 0:
				return;
			case 1:
				removeDoctorByName();
				break;
			case 2:
				removeDoctorByID();
				break;
			default:
				System.out.println( "Invalid edit option. No changes made." );
		}
	}
	public static void removeDoctorByName()
	{
		int index = getEntityIndexByNameInput("Doctor", doctorsList);
		if (index != -1)
			removeDoctor( index );
	}
	public static void removeDoctorByID()
	{
		int index = getEntityByIDInput( "Doctor", doctorsList );
		if (index != -1)
			removeDoctor( index );
	}
	private static void removeDoctor(int index)
	{
		doctorsList.remove(index);
		updateDatabaseFile( "Doctor" );
		System.out.println( "Doctor has been deleted.");
	}

	private static void handleDisplayAllDoctors()
	{
		System.out.println("\nList of Doctors:");
		System.out.printf("%-10s %-20s %-15s %-15s %-20s %-20s %-15s\n","Doctor ID","Doctor Name","Start Time",
				"End Time",
				"Ward",
				"Specialty",
				"Appointments Count");

		for (String[] doctor : doctorsList) {
			String doctorId = doctor[0];
			String doctorName = doctor[1];
			String startTime = doctor[2];
			String endTime = doctor[3];
			String ward = doctor[4];
			String specialty = doctor[5];

			// Count the number of appointments made by each doctor
			long appointmentsCount = countAppointmentsForDoctor(doctorId);

			System.out.printf(
					"%-10s %-20s %-15s %-15s %-20s %-20s %-15s\n",
					doctorId,
					doctorName,
					startTime,
					endTime,
					ward,
					specialty,
					appointmentsCount);
		}
	}
	private static long countAppointmentsForDoctor(String doctorId) {
		int count=0;
		for(String[] appointment:appointmentsList){
			if(appointment[2].equals(doctorId)) ++count;
		}
		return count;
	}

	private static void handleRegisterNewReceptionist()
	{
		System.out.print( " You are Registering a new Receptionist, Enter -1 anytime to discard the process \n" );
		System.out.print( "Enter Receptionists name: " );
		String name = scanner.nextLine();
		if (name.trim().equals( "-1" )) return;
		System.out.print( "Enter Receptionist`s username: " );
		String username = scanner.nextLine();
		if (username.trim().equals( "-1" )) return;
		System.out.print( "Enter Receptionist`s password: " );
		String password = scanner.nextLine();
		if (password.trim().equals( "-1" )) return;


		// Creating a new array to store receptionist details
		String[] newReceptionist = {
				generateID( "Receptionist" ),
				name,
				username,
				password,
		};

		receptionistsList.add( newReceptionist );

		// Adding the doctor to the file data
		updateDatabaseFile( "Receptionist" );
		System.out.println( "New Receptionist added successfully." );

	}

	private static void handleRemoveReceptionist()
	{
		showNameIDSelectionMenu("Remove", "Receptionist");
		int editOption = scanner.nextInt();
		scanner.nextLine();

		switch (editOption)
		{
			case 0:
				return;
			case 1:
				removeReceptionistByName();
				break;
			case 2:
				removeReceptionistByID();
				break;
			default:
				System.out.println( "Invalid edit option. No changes made." );
		}
	}
	public static void removeReceptionistByName()
	{
		int index = getEntityIndexByNameInput( "Receptionist",receptionistsList );
		if (index != -1)
			removeReceptionist( index );
	}
	public static void removeReceptionistByID()
	{
		int index = getEntityIndex("Receptionist", receptionistsList);
		if (index != -1)
			removeReceptionist( index );
	}
	private static void removeReceptionist(int index)
	{
		receptionistsList.remove(index);
		updateDatabaseFile( "Receptionist" );
		System.out.println( "Receptionist with ID '" + index + "' has been deleted.");
	}

	private static void handleEditReceptionistDetailsMenu()
	{
		showNameIDSelectionMenu("Edit", "Receptionist");
		int editOption = scanner.nextInt();
		scanner.nextLine();

		switch (editOption)
		{
			case 0:
				return;
			case 1:
				editReceptionistByName();
				break;
			case 2:
				editReceptionistByID();
				break;
			default:
				System.out.println( "Invalid edit option. No changes made." );
		}
	}

	public static void editReceptionistByName()
	{
		int index = getEntityIndexByNameInput("Receptionist", receptionistsList);
		if (index != -1)
			editReceptionist( index );
	}

	public static void editReceptionistByID()
	{
		int index = getEntityByIDInput("Receptionist", receptionistsList);
		if (index != -1)
			editReceptionist( index );
	}

	private static void editReceptionist(int index)
	{

		String[] receptionistsListDetails = receptionistsList.get( index );
		System.out.println( "Current Details of Receptionist '" + index + "': " + Arrays.toString( receptionistsListDetails ) );


		System.out.println( "\nSelect the option to edit:" );
		System.out.println( "1. Edit Receptionist Name" );
		System.out.println( "2. Edit Receptionist UserName" );
		System.out.println( "3. Edit Receptionist Password" );
		System.out.println( "0. Cancel Operation" );
		System.out.print( "Enter your choice: " );
		int editChoice = scanner.nextInt();
		while (!scanner.hasNextInt())
		{
			scanner.nextLine();
			System.out.println( "Please Enter only numbers" );
			System.out.print( "Enter your choice: " );
		}
		scanner.nextLine();

		switch (editChoice)
		{
			case 1:
				System.out.print( "Enter new Receptionist name: " );
				String newName = scanner.nextLine();
				receptionistsListDetails[1] = newName;
				break;
			case 2:
				System.out.print( "Enter new UserName: " );
				String username = scanner.nextLine();
				receptionistsListDetails[2] = username;
				break;
			case 3:
				System.out.print( "Enter new Password for Receptionist: " );
				String password = scanner.nextLine();
				receptionistsListDetails[3] = password;
				break;
			case 0:
				System.out.println( "No changes made." );
				return;

			default:
				System.out.println( "Invalid edit choice. No changes made." );
		}
		updateDatabaseFile( "Doctor" );
		System.out.println( "Details of Doctor '" + receptionistsListDetails[0] + "' after edit: " + Arrays.toString( receptionistsListDetails ) );
	}

	private static void displayAllReceptionists() {
		System.out.println("\nList of Receptionists:");
		System.out.printf("%-20s %-20s %-15s %-15s\n", "Receptionist ID", "Receptionist Name", "User Name", "Password");

		for (String[] receptionists : receptionistsList) {
			String receptionistId = receptionists[0];
			String receptionistName = receptionists[1];
			String receptionistUserName = receptionists[2];
			String receptionistPassword = receptionists[2];

			System.out.printf("%-20s %-20s %-15s %-15s\n", receptionistId, receptionistName,receptionistUserName, receptionistPassword);
		}
	}


	private static void addWard()
	{
		// Your logic for adding a ward
	}

	private static void editWard()
	{
		// Your logic for editing a ward
	}

	private static void getWardDetails()
	{
		// Your logic for getting ward details
	}

	private static void removeWard()
	{
		// Your logic for removing a ward
	}

	private static void displayAllWards()
	{
		// Your logic for displaying all wards
	}

	private static void optionCreateFakeFiles()
	{
		System.out.print( "How many records to make? :" );
		while (!scanner.hasNextInt())
		{
			System.out.println( "Invalid Input, Please Enter only numbers " );
			System.out.print( "How many records to make? :" );
		}
		int records = scanner.nextInt();
		createFakeDataFiles( records );
	}



	private static void getPatientHistory()
	{
		// Your logic for getting patient history
	}

	private static void addDiagnosis()
	{
		// Your logic for adding a diagnosis
	}

	private static void checkUpcomingAppointments()
	{
		// Your logic for checking upcoming appointments
	}

	private static void addPatient()
	{
		// Your logic for adding a patient
	}

	private static void getPatientDetails()
	{
		// Your logic for getting patient details
	}

	private static void editPatientDetails()
	{
		// Your logic for editing patient details
	}

	private static void submitPatientToWard()
	{
		// Your logic for submitting a patient to a ward
	}

	private static void checkDoctorsAvailability()
	{
		// Your logic for checking doctor's availability
	}

	private static void getAllDoctors()
	{
		// Your logic for getting all doctors
	}

	//--------------------------------------------------------//
	public static void showNameIDSelectionMenu(String  operation, String entity){
		System.out.printf( "\nSelect the option to %s %s: \n",operation,entity);
		System.out.printf( "1. %s by Name\n" , operation);
		System.out.printf( "2. %s by ID\n", operation );
		System.out.print( "0. Return back " );
		System.out.print( "Enter your choice: " );
		while (!scanner.hasNextInt())
		{
			scanner.nextLine();
			System.out.println( "Only number are allowed!" );
			System.out.print( "Enter your choice: " );
		}

	}

	public static int getEntityIndexByNameInput(String entityName, ArrayList<String[]> list )
	{
		System.out.print( "Enter the Name: " );
		String nameToView = scanner.nextLine();
		int index = getEntityIndex( nameToView , list);
		if (index != -1)
			return index;
		else System.out.println( entityName+ "by the name " + nameToView + " not found." );
		return -1;
	}

	public static int getEntityByIDInput(String entityName, ArrayList<String[]> list)
	{
		System.out.print( "Enter the ID: " );

		while (!scanner.hasNextInt())
		{
			scanner.nextLine();
			System.out.println( "Please Enter only numbers" );
			System.out.print( "Enter your choice: " );
		}
		int idToView = scanner.nextInt();
		scanner.nextLine();

		int index = getEntityIndex( entityName, idToView, list );
		if (index != -1)
			return index;
		else System.out.println( entityName+ " by the ID " + idToView + " not found." );
		return -1;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


	public static boolean checkLoginDetailsForAdmin(String username, String password)
	{
		return (username.equals( adminUsername ) && password.equals( adminPassword ));
	}

	public static boolean checkLoginDetailsForDoctor(String username, String password)
	{
		for (String[] docInfo : doctorsList)
		{
			if (username.equals( docInfo[docUsernameIndex] ))
			{
				return docInfo[docPasswordIndex].equals( password );
			}
		}
		return false;
	}

	public static boolean checkLoginDetailsForReceptionist(String username, String password)
	{
		for (String[] recpInfo : receptionistsList)
		{
			if (username.equals( recpInfo[receptionistUsernameIndex] ))
			{
				return recpInfo[receptionistPasswordIndex].equals( password );
			}
		}
		return false;
	}

	public static boolean getCredentials(int role)
	{

		System.out.print( "Enter Your Username: " );
		String username = scanner.nextLine();
		System.out.print( "Enter Your Password: " );
		String password = scanner.nextLine();

		switch (role)
		{
			case 1:
				return checkLoginDetailsForAdmin( username, password );
			case 2:
				return checkLoginDetailsForDoctor( username, password );
			case 3:
				return checkLoginDetailsForReceptionist( username, password );
			default:
				System.out.println( "Something Wrong Happened" );
		}
		return false;
	}

	public static boolean verifyLoginDetails(int role)
	{
		int loginTries = 1;
		while (!getCredentials( role ))
		{
			System.out.println( "Invalid Login Details! " + (5 - loginTries) + " tries left." );

			if (loginTries == 5)
			{
				System.out.println( "You have entered wrong credentials " + loginTries + " times, returning to Main Menu" );

				return false;
			}
			loginTries++;
		}

		// This code is reached only if the password is correct
		System.out.println( "Logged In Successfully" );
		return true;
	}

	public static boolean isStrongPassword(String password)
	{
		if (password.length() < 8)
			return false;
		char[] passwordCharacters = password.toCharArray();
		boolean hasDigit = false, hasUpCharacter = false;
		for (char ch : passwordCharacters)
		{
			if (ch != ' ')
			{
				if (Character.isUpperCase( ch ))
					hasUpCharacter = true;
				if (Character.isDigit( ch ))
					hasDigit = true;
			}
		}
		return hasDigit && hasUpCharacter;
	}

	public static boolean setAdminPassword(String username, String password)
	{
		if (isStrongPassword( password ))
		{
			adminUsername = username;
			adminPassword = password;
			System.out.println( "\n Credentials set Successfully" );
			return true;
		}
		else
		{
			System.out.println(
					"\n Password is too weak, It must contain a Digit, UpperCaseLetter and should have length of 8" );
			return false;
		}
	}

	public static void setAdminCredentialsForTheFirstTime()
	{
		System.out.println( "The system is running for the first time!! . You are required to setup the admin Credentials" );
		System.out.print( "Enter username: " );
		String username = scanner.nextLine();

		String pass;
		do
		{
			System.out.print( "Enter Password:" );
			pass = scanner.nextLine();
		}
		while (!setAdminPassword( username, pass ));
	}

	public static boolean userExist(String username, int role)
	{
		for (String[] docInfo : doctorsList)
		{
			if (username.equals( docInfo[docUsernameIndex] )) return true;
		}
		return false;
	}

	public static int getEntityIndex(String name, ArrayList<String[]>  list)
	{
		for (int i = 0; i < list.size(); i++)
		{
			if (name.equals( list.get( i )[1] ))
			{
				return i;
			}
		}
		return -1;
	}
	public static String getEntityID(String entityName, int id){
		switch (entityName)
		{
			case "Doctor":
				return "DOC-" + String.format( "%03d", id );
			case "Receptionist":
				return "REP-" + String.format( "%03d", id );
			case "Ward":
				return "W-" + String.format( "%03d", id );
			case "Patient":
				return "P-" + String.format( "%03d", id );
			case "Diagnosis":
				return "DIA-" + String.format( "%03d", id );
			case "Appointment":
				return "APT-" + String.format( "%03d", id );
			default:
				return "";
		}
	}
	public static int getEntityIndex(String entityName,int id, ArrayList<String[]>  list)
	{    

		String did = getEntityID(entityName,id);
		System.out.println("\nID: "+did);
		for (int i = 0; i < list.size(); i++)
		{
			if (did.equals( list.get( i )[0] ))
			{
				return i;
			}
		}
		return -1;
	}
/*
////////////////////////////////////////////////////////////////////////////////////////////////////////////
How is the handling of global variables like number of entites and admin password and username is handled>?
	 we have created an array numberOfEntitiesArray of int type
	 this array stores only the number of entites which are registered so far into the system
	 as this array is int type, and username and password are of string type , we need an array which is of Stirng type
	by which we can store all the data in one array,
	 so we have created globalVariablesArray of String type
	 at the start of programme we read the system vatiables file into globalVariablesArray
	 set the admin user name and password from frist 2 indices of this array
	rest of the indices are parsed into int and stored into numberOfEntitiesArray

*/

	public static String generateID(String entityType)
	{
		switch (entityType)
		{
			case "Doctor":
				numberOfEntitiesArray[0] += 1;
				return getEntityID("Doctor", numberOfEntitiesArray[0]);
			case "Receptionist":
				numberOfEntitiesArray[1] += 1;
				return getEntityID("Receptionist", numberOfEntitiesArray[1]);
			case "Ward":
				numberOfEntitiesArray[2] += 1;
				return getEntityID("Ward", numberOfEntitiesArray[2]);
			case "Patient":
				numberOfEntitiesArray[3] += 1;
				return getEntityID("Ward", numberOfEntitiesArray[3]);
			case "Diagnosis":
				numberOfEntitiesArray[4] += 1;
				return getEntityID("Diagnosis", numberOfEntitiesArray[4]);
			case "Appointment":
				numberOfEntitiesArray[5] += 1;
				return getEntityID("Appointment", numberOfEntitiesArray[5]);
			default:
				return "";
		}
	}

	//runs are the start of prograame to automatically read the file and set global variables
	public static void loadSystemVariablesFileIntoMemory()
	{
		try
		{
			FileReader fileReader = new FileReader( systemVariablesFilePath );
			Scanner variablesFileScanner = new Scanner( fileReader );
			int i = 0;
			while (variablesFileScanner.hasNextLine())
			{
				globalVariablesArray[i++] = variablesFileScanner.nextLine();
			}
		}
		catch (FileNotFoundException e)
		{
			System.out.println( "Something went wrong: " + e.getMessage() );
		}
		setGlobalVariablesFromArray();
	}

	public static void setGlobalVariablesFromArray()
	{
		adminUsername = globalVariablesArray[0];
		adminPassword = globalVariablesArray[1];
		for (int i = 0; i < numberOfEntitiesArray.length; ++i)
		{
			numberOfEntitiesArray[i] = Integer.parseInt( globalVariablesArray[i + 2] );
		}
	}

	public static void loadGlobalVariablesIntoArray()
	{
		globalVariablesArray[0] = adminUsername;
		globalVariablesArray[1] = adminPassword;
		for (int i = 0; i < numberOfEntitiesArray.length; ++i)
		{
			globalVariablesArray[i + 2] = Integer.toString( numberOfEntitiesArray[i] );
		}
	}

	//
	public static void updateSystemVariablesFile()
	{
		loadGlobalVariablesIntoArray();
		try
		{
			FileOutputStream fos = new FileOutputStream( systemVariablesFilePath, false );
			PrintStream ps = new PrintStream( fos );
			for (String s : globalVariablesArray)
			{
				ps.println( s );
			}
		}
		catch (FileNotFoundException e)
		{
			try
			{
				systemVariablesFilePath.createNewFile();
				updateSystemVariablesFile();
			}
			catch (Exception o)
			{
				System.out.printf( o.getMessage() );
			}

		}
	}

	public static void loadDatabaseFilesIntoMemory()
	{
		readDataFromFileToArrayList( doctorDataFilePath, doctorsList );
		readDataFromFileToArrayList( receptionistDataFilePath, receptionistsList );
		readDataFromFileToArrayList( wardDataFilePath, wardsList );
		readDataFromFileToArrayList( patientDataFilePath, patientsList );
		readDataFromFileToArrayList( diagnosisDataFilePath, diagnosisList );
		readDataFromFileToArrayList( appointmentsDataFilePath, appointmentsList );
	}

	public static void updateDatabaseFileThread(File filePath, ArrayList<String[]> dataList)
	{
		Runnable r2 = () ->
		{

			{
				try
				{
					FileWriter docFWriter = new FileWriter( filePath, false );
					PrintWriter pw = new PrintWriter( docFWriter );
					for (String[] data : dataList)
					{
						pw.println( Arrays.toString( data ) );
					}
					pw.close();
				}
				catch (IOException e)
				{
					System.out.printf( e.getMessage() );
				}
			}


		};
		Thread t = new Thread( r2 );
		t.start();
	}

	public static void updateDatabaseFile(String nameOfEntityChanged)
	{
		if (nameOfEntityChanged.equals( "Doctor" ))
		{
			updateDatabaseFileThread( doctorDataFilePath, doctorsList );
		}
		else if (nameOfEntityChanged.equals( "Receptionist" ))
		{
			updateDatabaseFileThread( receptionistDataFilePath, receptionistsList );
		}
		else if (nameOfEntityChanged.equals( "Ward" ))
		{
			updateDatabaseFileThread( wardDataFilePath, wardsList );
		}
		else if (nameOfEntityChanged.equals( "Patient" ))
		{
			updateDatabaseFileThread( patientDataFilePath, patientsList );
		}
		else if (nameOfEntityChanged.equals( "Diagnosis" ))
		{
			updateDatabaseFileThread( diagnosisDataFilePath, diagnosisList );
		}
		else if (nameOfEntityChanged.equals( "Appointment" ))
		{
			updateDatabaseFileThread( appointmentsDataFilePath, appointmentsList );
		}
		updateSystemVariablesFile();
	}

	private static void readDataFromFileToArrayList(File file, ArrayList<String[]> dataList)
	{
		if (file.exists())
		{
			try (Scanner reading = new Scanner( file ))
			{
				while (reading.hasNextLine())
				{
					String[] data = reading
							.nextLine()
							.replaceAll( "[\\[\\]]", "" )
							.split( ", " );
					dataList.add( data );
				}
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void addDummyDoctor()
	{
		String doctorID = generateID( "Doctor" );
		String doctorName = "Doctor" + RandomStringUtils.randomNumeric( 2 );
		String timingStart = RandomStringUtils.randomNumeric( 1 ) + "AM";
		String timingEnd = RandomStringUtils.randomNumeric( 1 ) + "PM";
		String ward = "Ward" + RandomStringUtils.randomNumeric( 2 );
		String specialty = "Specialty" + RandomStringUtils.randomNumeric( 2 );
		String username = RandomStringUtils.randomAlphanumeric( 8 );
		String password = RandomStringUtils.randomAlphanumeric( 8 );
		String[] dummyDoctor = {
				doctorID,
				doctorName,
				timingStart,
				timingEnd,
				ward,
				specialty,
				username,
				password,
		};
		doctorsList.add( dummyDoctor );
	}

	public static void addDummyReceptionist()
	{
		String receptionistID = generateID( "Receptionist" );
		String receptionistName = "Receptionist" + RandomStringUtils.randomNumeric( 2 );
		String username = RandomStringUtils.randomAlphanumeric( 8 );
		String password = RandomStringUtils.randomAlphanumeric( 8 );

		String[] dummyReceptionist = {receptionistID, receptionistName, username, password};
		receptionistsList.add( dummyReceptionist );
	}

	public static void addDummyWard()
	{
		String wardID = generateID( "Ward" );
		String wardName = "Ward" + RandomStringUtils.randomNumeric( 2 );
		String totalBeds = RandomStringUtils.randomNumeric( 2 );
		String occupiedBeds = RandomStringUtils.randomNumeric( 1 );
		String wardType = "Type" + RandomStringUtils.randomNumeric( 2 );

		String[] dummyWard = {
				wardID,
				wardName,
				totalBeds,
				occupiedBeds,
				wardType,
		};
		wardsList.add( dummyWard );
	}

	public static void addDummyPatient()
	{
		String patientID = generateID( "Patient" );
		String patientName = "Patient" + RandomStringUtils.randomNumeric( 2 );
		String gender = (RandomUtils.nextInt( 0, 1 ) == 1) ? "Male" : "Female";
		String age = RandomStringUtils.randomNumeric( 2 );
		String contact = RandomStringUtils.randomNumeric( 10 );

		String[] dummyPatient = {patientID, patientName, gender, age, contact};
		patientsList.add( dummyPatient );
	}

	public static void createFakeDataFiles(int records)
	{
		System.out.println( "Creating Fake Files For Representation Purpose" );
		for (int i = 0; i < records; ++i)
		{
			addDummyDoctor();
			addDummyReceptionist();
			addDummyWard();
			addDummyPatient();
		}
		updateDatabaseFileThread( doctorDataFilePath, doctorsList );
		updateDatabaseFileThread( appointmentsDataFilePath, appointmentsList );
		updateDatabaseFileThread( diagnosisDataFilePath, diagnosisList );
		updateDatabaseFileThread( patientDataFilePath, patientsList );
		updateDatabaseFileThread( wardDataFilePath, wardsList );
		updateDatabaseFileThread( receptionistDataFilePath, receptionistsList );
		updateDatabaseFile( "VariablesArray" );
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static void InitializeProgramme()
	{
		if (systemVariablesFilePath.exists())
		{
			loadSystemVariablesFileIntoMemory();
			loadDatabaseFilesIntoMemory();
		}
		else
		{
			File dir = new File( DATABASE_DIR );
			dir.mkdir();
			setAdminCredentialsForTheFirstTime();
			updateSystemVariablesFile();
			//createFakeDataFiles(50);
		}
	}

	public static void main(String[] args)
	{
		InitializeProgramme();
		// In this example, the option to go to the main menu is shown only for Submenu 1
		navigateMenu( mainMenuOptions );
	}


}
