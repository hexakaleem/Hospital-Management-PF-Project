package org.example;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;


public class Main
{
	static final String DATABASE_DIR = "data";
	static final String DOCTORS_FILE_NAME = "data/Doctors.txt";
	static final String RECEPTIONISTS_FILE_NAME = "data/Receptionists.txt";
	static final String WARDS_FILE_NAME = "data/Wards.txt";
	static final String PATIENTS_FILE_NAME = "data/Patients.txt";
	static final String DIAGNOSIS_FILE_NAME = "data/Diagnosis.txt";
	static final String APPOINTMENTS_FILE_NAME = "data/Appointments.txt";
	static final String WARDS_SUBMISSION_FILE_NAME = "data/Submissions.txt";
	static final String SYSTEM_VARIABLES_FILE_NAME = "data/SystemVariables.txt";
	static final int docUsernameIndex = 6;
	static final int docPasswordIndex = 7;
	static final int receptionistUsernameIndex = 2;
	static final int receptionistPasswordIndex = 3;
	static int selectedPatientIndex = -1;
	static int loggedInDoctorIndex = -1;
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
	// ['Submission ID', 'Patient ID' , 'Ward ID','Time Start','Time End']
	static ArrayList<String[]> wardPatientSubmissionList = new ArrayList<>();
	static File doctorDataFilePath = new File( DOCTORS_FILE_NAME );
	static File receptionistDataFilePath = new File( RECEPTIONISTS_FILE_NAME );
	static File wardDataFilePath = new File( WARDS_FILE_NAME );
	static File patientDataFilePath = new File( PATIENTS_FILE_NAME );
	static File diagnosisDataFilePath = new File( DIAGNOSIS_FILE_NAME );
	static File appointmentsDataFilePath = new File( APPOINTMENTS_FILE_NAME );
	static File wardSubmissionsDataFilePath = new File( WARDS_SUBMISSION_FILE_NAME );
	static File systemVariablesFilePath = new File( SYSTEM_VARIABLES_FILE_NAME );
	static ArrayList<String> mainMenuOptions = new ArrayList<String>()
	{{
		add( "Main Menu" );
		add( "Admin Section" );
		add( "Doctor's Section" );
		add( "Receptionist's Section" );
	}};
	// { adminUserName, adminPassword, numberOfDoctors, numberOfReceptionists, numberOfWards, numberOfPatients,
	// numberOfDiagnosis, numberOfAppointments }
	//For taking into account how many entities have got registered so far, so whenever new entity gets registered,
	//its id will be generated uniquely by incrementing old number
	static int[] numberOfEntitiesArray = new int[7];
	//[adminUsername, adminPassword, numberOfEntitiesArray]
	static String[] globalVariablesArray = new String[9];
	static Scanner scanner = new Scanner( System.in );
	static String adminPassword = "kaleem";
	static String adminUsername = "kaleem";

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
		int choice;

		while (!menu.isEmpty())
		{
			int lineWidth = 100;

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
				subMenu.add( "Handle Patient" );
				subMenu.add( "Check Upcoming Appointments" );
				break;
			//For Receptionists Section
			case "Receptionist's Section":
				subMenu.add( "Receptionist's Section" );
				subMenu.add( "Add Patient" );
				subMenu.add( "Get Patient Details" );
				subMenu.add( "Edit Patient Details" );
				subMenu.add( "Submit Patient To Ward" );
				subMenu.add( "Create Appointment" );
				subMenu.add( "Mark Appointnment As Done" );
				subMenu.add( "Check Doctor's Availability" );
				subMenu.add( "Get All Doctors" );
				break;
			case "Handle Patient":
				subMenu.add( "Handle Patient" );
				subMenu.add( "Get Patient History" );
				subMenu.add( "Add Diagnosis" );
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
				handleEditWardDetails();
				break;
			case "Get Ward Details":
				handleGetWardDetails();
				break;
			case "Remove Ward":
				handleRemoveWard();
				break;
			case "Display All Wards":
				displayAllWards();
				break;
			/// Doctor
			case "Handle Patient":
				return handlePatient();
			case "Get Patient History":
				getPatientHistory();
				break;
			case "Add Diagnosis":
				addDiagnosis();
				break;
			case "Check Upcoming Appointments":
				checkUpcomingAppointments();
				break;
			//Receptionist
			case "Add Patient":
				addPatient();
				break;
			case "Get Patient Details":
				handleGetPatientDetails();
				break;
			case "Edit Patient Details":
				handleEditPatientDetailsMenu();
				break;
			case "Submit Patient To Ward":
				handleSubmitPatientToWard();
				break;
			case "Create Appointment":
				handleCreateAppointment();
				break;
			case "Mark Appointnment As Done":
				handleAppointmentStatus();
				break;
			case "Check Doctor's Availability":
				handlecheckDoctorsAvailability();
				break;
			case "Get All Doctors":
				getAllDoctors();
				break;


			default:
				return false;
		}
		return false;
	}

	//This function will check if the date entered is in the form our system can process.
	public static boolean verifyTimeInput(String time)
	{
		try
		{
			LocalTime.parse( time, DateTimeFormatter.ofPattern( "hh:mm a", Locale.US ) );
			return true;
		}
		catch (DateTimeParseException e)
		{
			System.out.println( "The date entered is in incoorect format ,It should be like 06:00 PM" );
			return false;
		}
	}

	public static boolean verifyDateInput(String date)
	{
		try
		{
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			LocalDate.parse(date, formatter);

			return true;
		}
		catch (DateTimeParseException e)
		{
			System.out.println( "The date entered is in incoorect format ,It should be like 02/02/2024" );
			return false;
		}
	}

	private static void registerNewDoctor()
	{

		System.out.print( " You are Registering a new Doctor, Enter -1 anytime to discard the process \n" );
		System.out.print( "Enter doctor name: " );
		String name = scanner.nextLine();

		String timingStart;
		do
		{
			System.out.print( "Enter doctor`s shift start time (e.g 10:00 AM): " );
			timingStart = scanner.nextLine();
			if (timingStart.trim().equals( "-1" )) return;
		}
		while (!verifyTimeInput( timingStart ));

		timingStart = LocalTime.parse( timingStart, DateTimeFormatter.ofPattern( "hh:mm a", Locale.US ) ).toString();

		String timingEnd;
		do
		{
			System.out.print( "Enter doctor`s shift end time(e.g 03:00 PM): " );
			timingEnd = scanner.nextLine();
			if (timingEnd.trim().equals( "-1" )) return;
		}
		while (!verifyTimeInput( timingEnd ));
		timingEnd = LocalTime.parse( timingEnd, DateTimeFormatter.ofPattern( "hh:mm a", Locale.US ) ).toString();

		System.out.print( "Enter doctor ward(e.g Cardiology): " );
		String ward = scanner.nextLine();
		if (ward.trim().equals( "-1" )) return;
		System.out.print( "Enter doctor specialty(e.g Cardiologist): " );
		String specialty = scanner.nextLine();
		if (specialty.trim().equals( "-1" )) return;
		String username;
		//Using simple do while loop was not able to print the message, so We had to convert it into while loop
		while (true)
		{
			System.out.print( "Enter doctor username: " );
			username = scanner.nextLine();
			if (username.trim().equals( "-1" )) return;
			if (entityExist( username, 2 ))
				System.out.println( "The username" + username + " already exist. Enter another." );
			else break;
		}


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
		updateDatabaseFileThread( doctorDataFilePath, doctorsList );
		System.out.println( "New doctor added successfully." );
	}


	private static void handleEditDoctorDetailsMenu()
	{
		int option = getNameIDSelection( "Edit", "Doctor" );

		int index = -1;
		switch (option)
		{
			case 0:
				return;
			case 1:
				index = getEntityIndexByNameInput( "Doctor", doctorsList );
				if (index != -2)
					break;
			case 2:
				index = getEntityIndexByIDInput( "Doctor", doctorsList );
				break;
			default:
				System.out.println( "Invalid edit option. No changes made." );
		}
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
		updateDatabaseFileThread( doctorDataFilePath, doctorsList );
		System.out.println( "Details of Doctor '" + doctorDetails[0] + "' after edit: " + Arrays.toString( doctorDetails ) );
	}

	public static int getEntityIndexByIDNameSelection(String operationToPerform, String entity, ArrayList<String[]> list)
	{
		int option = getNameIDSelection( operationToPerform, entity );

		int index = -1;
		switch (option)
		{
			case 0:
				return -1;
			case 1:
				index = getEntityIndexByNameInput( entity, list );
				if (index != -2)
					break;
			case 2:
				index = getEntityIndexByIDInput( entity, list );
				break;
			default:
				System.out.println( "Invalid edit option." );
		}
		return index;
	}

	private static void handleGetDoctorDetails()
	{
		int index = getEntityIndexByIDNameSelection( "Show", "Doctor", doctorsList );
		if (index != -1)
			showDoctorDetails( index );
	}

	private static void showDoctorDetails(int index)
	{
		String[] doctorDetails = doctorsList.get( index );
		System.out.println( "Details of Doctor '" + doctorDetails[1] + "': " + Arrays.toString( doctorDetails ) );
	}

	private static void handleRemoveDoctor()
	{

		int index = getEntityIndexByIDNameSelection( "Remove", "Doctor", doctorsList );
		if (index != -1)
			removeDoctor( index );
	}

	private static void removeDoctor(int index)
	{
		doctorsList.remove( index );
		updateDatabaseFileThread( doctorDataFilePath, doctorsList );
		System.out.println( "Doctor has been deleted." );
	}

	private static void handleDisplayAllDoctors()
	{
		System.out.println( "\nList of Doctors:" );
		System.out.printf( "%-10s %-20s %-15s %-15s %-20s %-20s %-15s\n", "Doctor ID", "Doctor Name", "Start Time",
				"End Time",
				"Ward",
				"Specialty",
				"Appointments Count" );

		for (String[] doctor : doctorsList)
		{
			String doctorId = doctor[0];
			String doctorName = doctor[1];
			String startTime = doctor[2];
			String endTime = doctor[3];
			String ward = doctor[4];
			String specialty = doctor[5];

			// Count the number of appointments made by each doctor
			long appointmentsCount = countAppointmentsForDoctor( doctorId );

			System.out.printf(
					"%-10s %-20s %-15s %-15s %-20s %-20s %-15s\n",
					doctorId,
					doctorName,
					startTime,
					endTime,
					ward,
					specialty,
					appointmentsCount );
		}
	}

	private static long countAppointmentsForDoctor(String doctorId)
	{
		int count = 0;
		for (String[] appointment : appointmentsList)
		{
			if (appointment[2].equals( doctorId )) ++count;
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
		updateDatabaseFileThread( receptionistDataFilePath, receptionistsList );
		System.out.println( "New Receptionist added successfully." );

	}

	private static void handleRemoveReceptionist()
	{
		int index = getEntityIndexByIDNameSelection( "Remove", "Receptionist", receptionistsList );
		if (index != -1)
			removeReceptionist( index );
	}

	private static void removeReceptionist(int index)
	{
		receptionistsList.remove( index );
		updateDatabaseFileThread( receptionistDataFilePath, receptionistsList );
		System.out.println( "Receptionist with ID '" + index + "' has been deleted." );
	}

	private static void handleEditReceptionistDetailsMenu()
	{
		int index = getEntityIndexByIDNameSelection( "Edit", "Receptionist", receptionistsList );
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

		while (!scanner.hasNextInt())
		{
			scanner.nextLine();
			System.out.println( "Please Enter only numbers" );
			System.out.print( "Enter your choice: " );
		}
		int editChoice = scanner.nextInt();
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
		updateDatabaseFileThread( receptionistDataFilePath, receptionistsList );
		System.out.println( "Details of Doctor '" + receptionistsListDetails[0] + "' after edit: " + Arrays.toString( receptionistsListDetails ) );
	}

	private static void displayAllReceptionists()
	{
		System.out.println( "\nList of Receptionists:" );
		System.out.printf( "%-20s %-20s %-15s %-15s\n", "Receptionist ID", "Receptionist Name", "User Name", "Password" );

		for (String[] receptionists : receptionistsList)
		{
			String receptionistId = receptionists[0];
			String receptionistName = receptionists[1];
			String receptionistUserName = receptionists[2];
			String receptionistPassword = receptionists[3];

			System.out.printf( "%-20s %-20s %-15s %-15s\n", receptionistId, receptionistName, receptionistUserName, receptionistPassword );
		}
	}


	//--------------------------------------------------------------------------------------------------------------------//
	/*
	 * Functions for Wards are below:
	 * Including add, edit, delete, display
	 * ['Ward ID', 'Ward Name' , 'Total Beds', 'Occupied Beds','Type']
	 */

	private static void addWard()
	{

		System.out.print( " You are Registering a new Ward, \nEnter -1 anytime to discard the process \n" );
		System.out.print( "Enter Ward name: " );
		String name = scanner.nextLine();
		if (name.trim().equals( "-1" )) return;
		System.out.print( "Enter Total number of Beds in the ward: " );
		String totalBeds = scanner.nextLine();
		if (totalBeds.trim().equals( "-1" )) return;
		System.out.print( "Enter Number of Beds occupied (If any): " );
		String bedsOccupied = scanner.nextLine();
		if (totalBeds.trim().equals( "-1" )) return;
		System.out.print( "Enter Ward Type: " );
		String wardType = scanner.nextLine();

		// Creating a new array to store doctor details
		String[] newWard = {
				generateID( "Ward" ),
				name,
				totalBeds,
				bedsOccupied,
				wardType,
		};

		wardsList.add( newWard );

		// Adding the ward to the file data

		updateDatabaseFileThread( wardDataFilePath, wardsList );
		System.out.println( "New Ward added successfully." );
	}

	private static void handleEditWardDetails()
	{
		int index = getEntityIndexByIDNameSelection( "Edit", "Ward", wardsList );
		if (index != -1)
			editWard( index );
	}
	private static void editWard( int index)
	{
		String[] wardDetails = wardsList.get(index);
		System.out.println( "Current Details of Ward '" + wardDetails[0] + "': " + Arrays.toString(wardDetails) );

		System.out.println( "\nSelect the option to edit:" );
		System.out.println( "1. Edit Ward Name" );
		System.out.println( "2. Edit Ward Type" );
		System.out.println( "3. Edit Total Ward Beds" );
		System.out.println( "0. Return: " );
		int editChoice = scanner.nextInt();
		scanner.nextLine();

		switch (editChoice)
		{
			case 1:
				System.out.print( "Enter New Ward name: " );
				String newName = scanner.nextLine();
				wardDetails[1] = newName;
				break;
			case 2:
				System.out.print( "Enter New Ward Type: " );
				String newWardType = scanner.nextLine();
				wardDetails[4] = newWardType;
				break;
			case 3:
				System.out.print( "Enter New Total Number of Beds: " );
				int intNumberOfBedsOccupied = Integer.parseInt(wardDetails[3]);
				String newTotalNumberOfBeds = scanner.nextLine();
				int intNewTotalNumberOfBeds = Integer.parseInt(newTotalNumberOfBeds);
				if (intNewTotalNumberOfBeds > intNumberOfBedsOccupied)
					wardDetails[2] = newTotalNumberOfBeds;
				else
				{
					System.out.println("Cannot Reduce Beds as there are Patients admitted in the Ward");
					return;
				}
				break;
			case 0:
				System.out.println( "No changes made." );
				return;
			default:
				System.out.println( "Invalid edit choice. No changes made." );
		}
		updateDatabaseFileThread(wardDataFilePath, wardsList);
		System.out.println( "Details of Ward '" + wardDetails[0] + "' after edit: " + Arrays.toString( wardDetails ) );

	}

	private static void displayAllWards()
	{
		System.out.println( "\nList of Wards:" );
		System.out.printf( "%-10s %-20s %-15s %-15s\n", "Ward ID", "Ward Name",
				"Total Beds", "Occupied Beds" );

		for (String[] ward : wardsList)
		{
			String wardId = ward[0];
			String wardName = ward[1];
			String totalBeds = ward[2];
			String occupiedBed = ward[3];

			System.out.printf(
					"%-10s %-20s %-15s %-15s\n",
					wardId,
					wardName,
					totalBeds,
					occupiedBed );
		}
	}

	private static void handleRemoveWard()
	{
		int index = getEntityIndexByIDNameSelection( "Remove", "Ward", wardsList );
		if (index != -1)
			removeWard( index );
	}


	private static void removeWard(int index)
	{
		wardsList.remove( index );
		updateDatabaseFileThread( wardDataFilePath, wardsList );
		System.out.println( "Ward has been deleted." );
	}

	private static void handleGetWardDetails()
	{
		int index = getEntityIndexByIDNameSelection( "Show", "Ward", wardsList );
		if (index != -1)
			showWardDetails( index );
	}

	private static void showWardDetails(int index)
	{
		String[] wardDetails = wardsList.get( index );
		System.out.println( "Details of Ward '" + wardDetails[1] + "': " + Arrays.toString( wardDetails ) );
	}


//----------------------------------------------------------------------------------------------------------------//

	private static boolean handlePatient()
	{
		int index = getEntityIndexByIDNameSelection( "Select", "Patient", patientsList );
		if (index == -1)
		{
			return false;
		}
		selectedPatientIndex = index;
		return  true;
	}


	//Code for processing patient history
	private static void getPatientHistory()
	{
		//Index of IDs of the patients in the different Lists:
		final int patientIDIndex = 0;
		final int appointmentIDIndex = 1;
		final int diagnosisIDIndex = 1;

		//Taking patients ID by using index
		//Displaying patient details using index
		getPatientDetails( selectedPatientIndex );

		String[] patientDetails = patientsList.get( selectedPatientIndex );
		String patientID = patientDetails[patientIDIndex];
		String patientName = patientDetails[1];

		//searching for patient appointments
		//we need to get the appointment list index that stores the patient ID and then compare, if true print the
		//array of that appointment
		System.out.println( "All Appointments of Patient '" + patientName + "': " );
		for (int i = 0; i < appointmentsList.size(); i++)
		{
			String[] appointmentDetails = appointmentsList.get( i );
			String appointmentPatientID = appointmentDetails[appointmentIDIndex];
			if (patientID.equals( appointmentPatientID ))
				System.out.println( Arrays.toString( appointmentDetails ) );
		}

		//Now searching for matching diagnosis for the patient by using ID
		System.out.println( "All Diagnosis of Patient '" + patientName + "': " );
		for (int i = 0; i < diagnosisList.size(); i++)
		{
			String[] diagnosisDetails = diagnosisList.get( i );
			String diagnosisPatientID = diagnosisDetails[diagnosisIDIndex];
			if (patientID.equals( diagnosisPatientID ))
				System.out.println( Arrays.toString( diagnosisDetails ) );
		}
	}

	private static void addDiagnosis()
	{
		//Taking Patient ID from the patient array
		String[] patientDetailsArray = patientsList.get( selectedPatientIndex );

		String patientID = patientDetailsArray[0];
		String doctorID = doctorsList.get( loggedInDoctorIndex )[0];


		// IDs exist, proceed to gather other diagnosis information
		String diagnosisID = generateID( "Diagnosis" );
		System.out.print( "Enter Prescriptions: " );
		String prescriptions = scanner.nextLine();
		System.out.print( "Enter Diagnosis: " );
		String diagnosis = scanner.nextLine();

		// Creating a new array to store diagnosis information
		String[] newDiagnosis = {diagnosisID, patientID, doctorID, prescriptions, diagnosis};

		// Adding the new diagnosis to the ArrayList
		diagnosisList.add( newDiagnosis );
		updateDatabaseFileThread( diagnosisDataFilePath, diagnosisList );

		System.out.println( "Diagnosis information added successfully." );

	}

	private static void checkUpcomingAppointments() {
		// ['Appointment ID', 'Patient ID' , 'Doctor ID','appointment time' , 'appointment date' ,Appointment status]
		for (String[] appointmentListDetail : appointmentsList) {
			String appointmentListDoctorID = appointmentListDetail[2];
			String appointmentStatus = appointmentListDetail[5];
			String docID= doctorsList.get( loggedInDoctorIndex )[0];
			if (docID.equals(appointmentListDoctorID) && appointmentStatus.equals("Pending"))
			{
				System.out.println(Arrays.toString(appointmentListDetail));
			}
		}
	}

	private static void addPatient()
	{
		System.out.println( "You are Registering a new Patient, Enter -1 anytime to discard the process" );

		System.out.print( "Enter patient name: " );
		String name = scanner.nextLine();
		if (name.trim().equals( "-1" )) return;

		System.out.print( "Enter patient gender: " );
		String gender = scanner.nextLine();
		if (gender.trim().equals( "-1" )) return;

		System.out.print( "Enter patient age: " );
		String age = scanner.nextLine();
		if (age.trim().equals( "-1" )) return;

		System.out.print( "Enter patient contact: " );
		String contact = scanner.nextLine();
		if (contact.trim().equals( "-1" )) return;

		// Creating a new array to store patient details
		String[] newPatient = {
				generateID( "Patient" ),
				name,
				gender,
				age,
				contact
		};

		patientsList.add( newPatient );

		// Adding the patient to the file data

		updateDatabaseFileThread( patientDataFilePath, patientsList );
		System.out.println( "New patient added successfully." );
	}

	private static void handleGetPatientDetails()
	{
		int index = getEntityIndexByIDNameSelection( "Get Details", "Patient", patientsList );
		if (index != -1)
			getPatientDetails( index );
	}

	private static void getPatientDetails(int index)
	{
		String[] patientDetails = patientsList.get( index );
		System.out.println( "Details of Patient '" + patientDetails[1] + "': " + Arrays.toString( patientDetails ) );
	}

	private static void handleEditPatientDetailsMenu()
	{
		int index = getEntityIndexByIDNameSelection( "Edit", "Patient", patientsList );
		if (index != -1)
			editPatient( index );
	}

	private static void editPatient(int index)
	{
		String[] patientDetails = patientsList.get( index );
		System.out.println( "Current Details of Patient '" + index + "': " + Arrays.toString( patientDetails ) );

		System.out.println( "\nSelect the option to edit:" );
		System.out.println( "1. Edit Patient Name" );
		System.out.println( "2. Edit Patient Gender" );
		System.out.println( "3. Edit Patient Age" );
		System.out.println( "4. Edit Patient Contact" );
		System.out.println( "0. Cancel Operation" );
		System.out.print( "Enter your choice: " );

		while (!scanner.hasNextInt())
		{
			scanner.nextLine();
			System.out.println( "Please Enter only numbers" );
			System.out.print( "Enter your choice: " );
		}
		int editChoice = scanner.nextInt();
		scanner.nextLine();

		switch (editChoice)
		{
			case 1:
				System.out.print( "Enter new Patient name: " );
				String newName = scanner.nextLine();
				patientDetails[1] = newName;
				break;
			case 2:
				System.out.print( "Enter new Gender: " );
				String newGender = scanner.nextLine();
				patientDetails[2] = newGender;
				break;
			case 3:
				System.out.print( "Enter new Age: " );
				String newAge = scanner.nextLine();
				patientDetails[3] = newAge;
				break;
			case 4:
				System.out.print( "Enter new Contact: " );
				String newContact = scanner.nextLine();
				patientDetails[4] = newContact;
				break;
			case 0:
				System.out.println( "No changes made." );
				return;

			default:
				System.out.println( "Invalid edit choice. No changes made." );
		}
		updateDatabaseFileThread( patientDataFilePath, patientsList );
		System.out.println( "Details of Patient '" + patientDetails[0] + "' after edit: " + Arrays.toString( patientDetails ) );
	}

	private static void handleSubmitPatientToWard()
	{
		int patientIndex = getEntityIndexByIDNameSelection( "Submit", "Patient", patientsList );
		int wardIndex = getEntityIndexByIDNameSelection( "Submit", "Ward", wardsList );
		if (patientIndex != -1 && wardIndex != -1)
			submitPatientToWard( patientIndex, wardIndex );
	}

	private static void submitPatientToWard(int patientIndex, int wardIndex)
	{
		String[] ward = wardsList.get( wardIndex );
		String[] patient = patientsList.get( patientIndex );
		String wardID= ward[0];
		String patientID= patient[0];

		// ['Ward ID', 'Ward Name' , 'Total Beds','Occupied Beds','Type']
		// ['Submission ID', 'Patient ID' , 'Ward ID','Time Start','Time End']
		int occupiedBeds= Integer.parseInt( ward[3] );
		int totalBeds= Integer.parseInt( ward[2] );
		LocalTime timeNow= LocalTime.now( ZoneId.of( "Asia/Karachi" ));

		if(occupiedBeds+1 > totalBeds){
			System.out.printf("The ward %s is out of beds , cannot add more patients in it....\n", ward[0]);
		}else{
			String[] newSubmission = {
					generateID( "Submission" ),
					patientID,
					wardID,
					timeNow.toString(),
			};
			wardPatientSubmissionList.add( newSubmission );

			updateDatabaseFileThread( wardSubmissionsDataFilePath, wardPatientSubmissionList );
			System.out.println();
			getWardPatients(newSubmission[2]);
		}
	}
	public static void getWardPatients( String wardID){
		for(String[] sub:wardPatientSubmissionList){
			String patientID= sub[1];
			String submissionWardID= sub[2];
			String[] patient= new String[5];
			int index = getEntityIndexByID(patientID,patientsList  );
			if(index!= -1)
				patient= patientsList.get( index );

			if(submissionWardID.equals( wardID ))
				headingsDisplayer("Patient",patient );
		}
	}
	private static void handleCreateAppointment()
	{
		int doctorIndex = getEntityIndexByIDNameSelection( "Select", "Doctor", doctorsList );
		int patientIndex = getEntityIndexByIDNameSelection( "Select", "Patient", patientsList );
		String[] doctor = doctorsList.get(doctorIndex);
		String[] patient = patientsList.get(patientIndex);
		String docID= doctor[0];
		String patientID= patient[0];

		String appointmentTime;
		do
		{
			System.out.print( "Enter appointment time (e.g 10:00 AM): " );
			appointmentTime = scanner.nextLine();
		}
		while (!verifyTimeInput( appointmentTime ));
		appointmentTime = LocalTime.parse( appointmentTime, DateTimeFormatter.ofPattern( "hh:mm a", Locale.US ) ).toString();

		String appointmentDate;
		do
		{
			System.out.print( "Enter appointment Date (e.g dd/MM/yyyy): " );
			appointmentDate = scanner.nextLine();
		}
		while (!verifyDateInput( appointmentDate ));
		appointmentDate = LocalDate.parse( appointmentDate, DateTimeFormatter.ofPattern("dd/MM/yyyy") ).toString();


		// Creating a new array to store patient details
		String[] newAppointment = {
				generateID( "Appointment" ),
				patientID,
				docID,
				appointmentTime,
				appointmentDate,
				"Pending"
		};
		appointmentsList.add( newAppointment );

		updateDatabaseFileThread( appointmentsDataFilePath, appointmentsList );
		System.out.println("The Appointment "+ newAppointment[0] + " is registered with " +doctor[1] + " at "+ appointmentTime + " " +appointmentDate);

	}

	private static void handleAppointmentStatus(){

		int index = getEntityIndexByIDInput("Appointment", appointmentsList);
		String[] appointment =new String[6];
		if(index != -1){
			appointment= appointmentsList.get( index );
			appointment[5]= "Completed";
		}
		updateDatabaseFileThread( appointmentsDataFilePath, appointmentsList );
		System.out.println("The Appointment " + appointment[0]+" is marked as completed" );
	}

	private static void handlecheckDoctorsAvailability()
	{
		int index = getEntityIndexByIDNameSelection( "Check Availability", "Doctor", doctorsList );
		if (index != -1)
			checkDoctorsAvailability( index );
	}

	private static void checkDoctorsAvailability(int index)
	{
		String[] doctor = doctorsList.get( index );
		//At index 2 start time is ther and at index 3 end time is there
		String docName = doctor[1];
		LocalTime now = LocalTime.now();
		boolean isAvailable = isTimeInRange( now.toString(), doctor[2], doctor[3] );
		if (isAvailable) System.out.println( "The Doctor is Available " );
		else
			System.out.println( "The docotor " + docName + " is not avaiable,Shift Times are " + doctor[2] + "-" + doctor[3] );
	}

	private static void getAllDoctors()
	{
		System.out.println( "\nList of Doctors:" );
		System.out.printf( "%-10s %-20s %-15s %-15s %-20s %-20s %-15s %-15s\n",
				"Doctor ID", "Doctor Name", "Start Time", "End Time", "Ward", "Specialty", "Username", "Password" );

		for (String[] doctor : doctorsList)
		{
			String doctorId = doctor[0];
			String doctorName = doctor[1];
			String startTime = doctor[2];
			String endTime = doctor[3];
			String ward = doctor[4];
			String specialty = doctor[5];
			String username = doctor[6];
			String password = doctor[7];

			System.out.printf( "%-10s %-20s %-15s %-15s %-20s %-20s %-15s %-15s\n",
					doctorId, doctorName, startTime, endTime, ward, specialty, username, password );
		}
	}

//----------------------------------------------------------------------------------------------------------------//
	///Helper Functions for the main tasks

	public static boolean isTimeInRange(String time, String startTime, String endTime)
	{
		try
		{
			LocalTime timeToCheck = LocalTime.parse( time );
			LocalTime start = LocalTime.parse( startTime, DateTimeFormatter.ofPattern( "hh:mm a", Locale.US ) );
			LocalTime end = LocalTime.parse( endTime, DateTimeFormatter.ofPattern( "hh:mm a", Locale.US ) );

			if (start.isAfter( end ))
			{
				return timeToCheck.isAfter( start ) || timeToCheck.isBefore( end );
			}
			else
			{
				return timeToCheck.isAfter( start ) && timeToCheck.isBefore( end );
			}
		}
		catch (DateTimeParseException e)
		{
			System.out.println( "Something went wrong!, Please make sure the files are not damaaged. Delete the gobalvariablesarray File if the error persists." );
			return false;
		}

	}

	public static int getNameIDSelection(String operation, String entity)
	{
		System.out.printf( "\nSelect the option to %s %s: \n", operation, entity );
		System.out.printf( "1. %s by Name\n", operation );
		System.out.printf( "2. %s by ID\n", operation );
		System.out.print( "0. Return back \n" );
		System.out.print( "Enter your choice: " );
		while (!scanner.hasNextInt())
		{
			scanner.nextLine();
			System.out.println( "Only number are allowed!" );
			System.out.print( "Enter your choice: " );
		}
		int selection = scanner.nextInt();
		scanner.nextLine();
		return selection;
	}

	//if  multiple entites have found , it will return -2, if non is found , it will return -1. else it will retrn the index of entity.
	public static int getEntityIndexByNameInput(String entityName, ArrayList<String[]> list)
	{
		System.out.print( "Enter the Name: " );
		String nameToView = scanner.nextLine();
		ArrayList<Integer> indices = getEntitiesIndexes( nameToView, list );
		if (indices.size() > 1)
		{
			System.out.println( "Multiple " + entityName + "s have found: " );
			for (Integer index : indices)
			{
				System.out.println( Arrays.toString( list.get( index ) ) );
			}
			System.out.println( "Enter the ID of the specified " + entityName );
			return -2;
		}
		else if (indices.isEmpty())
		{
			System.out.println( entityName + "by the name " + nameToView + " not found." );
		}
		else
		{
			return indices.get( 0 );
		}
		return -1;
	}

	public static int getEntityIndexByIDInput(String entityName, ArrayList<String[]> list)
	{
		String entityID;
		do
		{
			System.out.print( "Enter the ID: " );
			entityID = scanner.nextLine();
		}
		while (!verifyIDFormat( entityID, entityName ));

		if (entityID.split( "-" ).length > 1)
		{
			entityID = entityID.split( "-" )[1];
		}
		int idInteger = Integer.parseInt( entityID );
		int index = getEntityIndex( entityName, idInteger, list );
		if (index != -1)
			return index;
		else System.out.println( entityName + " by the ID " + entityID + " not found." );
		return -1;
	}

	public static boolean verifyIDFormat(String enteredID, String entityName)
	{
		if (enteredID.chars().allMatch( Character::isDigit )) return true;
		String[] parts = enteredID.split( "-" );
		if (parts.length > 2) return false;
		boolean isFormatted = parts[0].toUpperCase().equals( getEntityIDPrefix( entityName ) ) && parts[1].chars().allMatch( Character::isDigit );
		if (!isFormatted)
			System.out.println( "The ID formate is invalid, It should be a number or should have " + getEntityIDPrefix( entityName ) + "097 formate" );
		return isFormatted;
	}

	//This function requires the entiyID as whole ID inlcuding the prefix and integer part
	private static int getEntityIndexByID(String entityID, ArrayList<String[]> list)
	{
		for (int i = 0; i < list.size(); ++i)
		{
			if (list.get( i )[0].equals( entityID )) return i;
		}
		return -1;
	}

	private static boolean patientExists(String patientID)
	{
		for (String[] patient : patientsList)
		{
			if (patient[0].equals( patientID ))
			{
				return true;
			}
		}
		return false;
	}

	private static void headingsDisplayer(String entity, String[] arrayOfEntity){
		switch (entity){

			case "Doctor":
				processDoctorDisplay(arrayOfEntity);
				break;

			case "Appointment":
				processAppointmentDisplay(arrayOfEntity);
				break;

			case "Diagnosis":
				processDiagnosisDisplay(arrayOfEntity);
				break;

			case "Patient":
				processPatientDisplay(arrayOfEntity);
				break;

			case "Receptionist":
				processReceptionistDisplay(arrayOfEntity);
				break;

			case "Ward":
				processWardDisplay(arrayOfEntity);
				break;
		}
	}

	private static void processWardDisplay(String[] arrayOfEntity) {
		String wardId = arrayOfEntity[0];
		String wardName = arrayOfEntity[1];
		String totalBeds = arrayOfEntity[2];
		String occupiedBeds = arrayOfEntity[3];
		String wardType = arrayOfEntity[4];

		System.out.println("\nList of Wards:");
		System.out.printf("%-15s %-20s %-15s %-15s %-10s\n", "Ward ID", "Ward Name", "Total Beds", "Occupied Beds", "Type");
		System.out.printf("%-15s %-20s %-15s %-15s %-10s\n", wardId, wardName, totalBeds, occupiedBeds, wardType);
	}

	private static void processReceptionistDisplay(String[] arrayOfEntity) {
		String recepId = arrayOfEntity[0];
		String recepName = arrayOfEntity[1];
		String username = arrayOfEntity[2];
		String password = arrayOfEntity[3];

		System.out.println("\nList of Receptionists:");
		System.out.printf("%-15s %-20s %-15s %-15s\n", "Receptionist ID", "Receptionist Name", "Username", "Password");
		System.out.printf("%-15s %-20s %-15s %-15s\n", recepId, recepName, username, password);
	}

	private static void processPatientDisplay(String[] arrayOfEntity) {
		String patientId = arrayOfEntity[0];
		String patientName = arrayOfEntity[1];
		String gender = arrayOfEntity[2];
		String age = arrayOfEntity[3];
		String contact = arrayOfEntity[4];

		System.out.println("\nList of Patients:");
		System.out.printf("%-15s %-20s %-10s %-5s %-15s\n", "Patient ID", "Patient Name", "Gender", "Age", "Contact");
		System.out.printf("%-15s %-20s %-10s %-5s %-15s\n", patientId, patientName, gender, age, contact);
	}

	private static void processDiagnosisDisplay(String[] arrayOfEntity) {
		String diagnosisId = arrayOfEntity[0];
		String patientIdDiagnosis = arrayOfEntity[1];
		String doctorIdDiagnosis = arrayOfEntity[2];
		String prescriptions = arrayOfEntity[3];
		String diagnosis = arrayOfEntity[4];
		String appointmentIdDiagnosis = arrayOfEntity[5];

		System.out.println("\nList of Diagnoses:");
		System.out.printf("%-15s %-15s %-15s %-20s %-20s %-15s\n", "Diagnosis ID", "Patient ID", "Doctor ID",
				"Prescriptions", "Diagnosis", "Appointment ID");
		System.out.printf("%-15s %-15s %-15s %-20s %-20s %-15s\n", diagnosisId, patientIdDiagnosis,
				doctorIdDiagnosis, prescriptions, diagnosis, appointmentIdDiagnosis);
	}

	private static void processAppointmentDisplay(String[] arrayOfEntity) {
		String appointmentId = arrayOfEntity[0];
		String patientIdAppointment = arrayOfEntity[1];
		String doctorIdAppointment = arrayOfEntity[2];
		String appointmentTime = arrayOfEntity[3];
		String appointmentDate = arrayOfEntity[4];
		String appointmentStatus = arrayOfEntity[5];

		System.out.println("\nList of Appointments:");
		System.out.printf("%-15s %-15s %-15s %-20s %-15s %-20s\n", "Appointment ID", "Patient ID", "Doctor ID",
				"Appointment Time", "Appointment Date", "Appointment Status");
		System.out.printf("%-15s %-15s %-15s %-20s %-15s %-20s\n", appointmentId, patientIdAppointment,
				doctorIdAppointment, appointmentTime, appointmentDate, appointmentStatus);
	}

	private static void processDoctorDisplay(String[] arrayOfEntity) {
		String doctorId = arrayOfEntity[0];
		String doctorName = arrayOfEntity[1];
		String startTime = arrayOfEntity[2];
		String endTime = arrayOfEntity[3];
		String ward = arrayOfEntity[4];
		String specialty = arrayOfEntity[5];
		// Count the number of appointments made by each doctor
		long appointmentsCount = countAppointmentsForDoctor(doctorId);
		System.out.println( "\nList of Doctors:" );
		System.out.printf( "%-10s %-20s %-15s %-15s %-20s %-20s %-15s\n", "Doctor ID", "Doctor Name",
				"Start Time", "End Time", "Ward", "Specialty", "Appointments Count" );
		System.out.printf("%-10s %-20s %-15s %-15s %-20s %-20s %-15s\n", doctorId, doctorName, startTime,
				endTime, ward, specialty, appointmentsCount);
	}
//--------------------------------------------------------------------------------------------------------------------//

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
				boolean isMatched = docInfo[docPasswordIndex].equals( password );
				if (isMatched) loggedInDoctorIndex = getEntityIndexByID( docInfo[0], doctorsList );
				return isMatched;
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


//--------------------------------------------------------------------------------------------------------------------//

	public static boolean entityExist(String username, int role)
	{
		if (role == 1)
		{
			for (String[] docInfo : doctorsList)
			{
				if (username.equals( docInfo[docUsernameIndex] )) return true;
			}
		}
		else if (role == 2)
		{
			for (String[] recepInfo : receptionistsList)
			{
				if (username.equals( recepInfo[receptionistUsernameIndex] )) return true;
			}
		}

		return false;
	}

	public static ArrayList<Integer> getEntitiesIndexes(String name, ArrayList<String[]> list)
	{
		ArrayList<Integer> indexesArray = new ArrayList<>();
		for (int i = 0; i < list.size(); i++)
		{
			if (list.get( i )[1].contains( name ))
			{
				indexesArray.add( i );
			}
		}
		return indexesArray;
	}

	public static String getEntityIDPrefix(String entityName)
	{
		switch (entityName)
		{
			case "Doctor":
				return "DOC-";
			case "Receptionist":
				return "REP-";
			case "Ward":
				return "W-";
			case "Patient":
				return "P-";
			case "Diagnosis":
				return "DIA-";
			case "Appointment":
				return "APT-";
			case "Submission":
				return "SUB-";
			default:
				throw new IllegalArgumentException("Invalid Entity Type Passed");
		}

	}

	public static String getEntityID(String entityName, int id)
	{
		return getEntityIDPrefix( entityName ) + String.format( "%03d", id );
	}

	public static int getEntityIndex(String entityName, int id, ArrayList<String[]> list)
	{

		String did = getEntityID( entityName, id );

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
//--------------------------------------------------------------------------------------------------------------------//

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
				return getEntityID( "Doctor", numberOfEntitiesArray[0] );
			case "Receptionist":
				numberOfEntitiesArray[1] += 1;
				return getEntityID( "Receptionist", numberOfEntitiesArray[1] );
			case "Ward":
				numberOfEntitiesArray[2] += 1;
				return getEntityID( "Ward", numberOfEntitiesArray[2] );
			case "Patient":
				numberOfEntitiesArray[3] += 1;
				return getEntityID( "Patient", numberOfEntitiesArray[3] );
			case "Diagnosis":
				numberOfEntitiesArray[4] += 1;
				return getEntityID( "Diagnosis", numberOfEntitiesArray[4] );
			case "Appointment":
				numberOfEntitiesArray[5] += 1;
				return getEntityID( "Appointment", numberOfEntitiesArray[5] );
			case "Submission":
				numberOfEntitiesArray[6] += 1;
				return getEntityID( "Submission", numberOfEntitiesArray[6] );
			default:
				throw new IllegalArgumentException("Invalid Entity Type Passed");
		}
	}


	//runs are the start of program to automatically read the file and set global variables
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
		};
		Thread t = new Thread( r2 );
		t.start();
	}


	private static void readDataFromFileToArrayList(File file, ArrayList<String[]> dataList)
	{
		if (file.exists())
		{
			try (Scanner reading = new Scanner( file ))
			{
				while (reading.hasNextLine())
				{
					String[] data = reading.nextLine().replaceAll( "[\\[\\]]", "" ).split( ", " );

					dataList.add( data );
				}
			}
			catch (FileNotFoundException e)
			{
				System.out.println( "Something went wrong while Reading from Variables file." );
			}
		}
	}

//--------------------------------------------------------------------------------------------------------------------//
	/*
	 * Main functions to run the programs
	 *
	 */

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
		}
	}

	public static String encrpytString(String str)
	{
		String strip = "[192 .168 ,.1.11]";
		String encstr = "192.168.1.11";
		byte[] encryptArray = Base64.getEncoder().encode( strip.getBytes() );
		try
		{
			encstr = new String( encryptArray, StandardCharsets.UTF_8 );
		}
		catch (Exception o)
		{

		}
		return encstr;

	}

	public static String decryptString(String str)
	{
		String strDec = str;
		String decstr = "MTkyLjE2OC4xLjEx";
		byte[] dectryptArray = strDec.getBytes();
		byte[] decarray = Base64.getDecoder().decode( dectryptArray );
		try
		{
			decstr = new String( decarray, StandardCharsets.UTF_8 );
		}
		catch (Exception o)
		{

		}
		return decstr;

	}

	public static void main(String[] args)
	{

		//String str= encrpytString("");
		//System.out.println( "Enc   >>  " + str);
		//System.out.println("Dec  >>>  " + decryptString(str));
		InitializeProgramme();
		// In this example, the option to go to the main menu is shown only for Submenu 1
		navigateMenu( mainMenuOptions );
	}

}