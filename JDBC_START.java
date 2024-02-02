import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.io.*;
import java.util.*;
import java.util.Date;

import oracle.jdbc.OraclePreparedStatement;



public class JDBC_START {
	
	static final String DB_URL = "jdbc:oracle:thin:@cisvm-oracle.unfcsd.unf.edu:1521:orcl";
	public final int LeftPad = 20;
	private Connection Con;
	
	
	
	public static void main(String[] args) throws Exception{
		
		Scanner username = new Scanner(System.in);
		System.out.println("Enter Sql Username: ");
		String usr = username.nextLine();
		
		
		Scanner password = new Scanner(System.in);
		System.out.println("Enter Sql Password: ");
		String pw = password.nextLine();
		
		JDBC_START pgm = new JDBC_START();

		try {
			pgm.Con = pgm.GetConnection(DB_URL,usr,pw);
		} catch(SQLException sqe) {
			System.out.println("An Error has occured in the main method"+sqe.getMessage());	
		}
		pgm.MainMenu();
	}

	Connection GetConnection(String url, String usr, String pw)throws Exception{
	
		try {
			Con = DriverManager.getConnection(DB_URL, usr, pw);
		} catch(SQLException e) {
			System.out.println("Failed to connect to DBMS using the given information please try again: "+e.getMessage());	
		}
		return Con;
	}
	
	int MainMenu() throws SQLException{
		String usrInput = "";
		Scanner sc = new Scanner(System.in);
		System.out.printf("~~~Welcome to the SWOOP ER Software.~~~~\n", LeftPad);
		do {
			System.out.printf("Please choose a selection.\n", LeftPad);
			System.out.printf("1. Create/Edit Customer Information.\n", LeftPad);
			System.out.printf("2. Create/Edit Doctor Information.\n", LeftPad);
			System.out.printf("3. Create/Edit Appointments.\n", LeftPad);
			System.out.printf("4. Create/Edit Bills.\n", LeftPad);
			System.out.printf("5. Create Reports.\n", LeftPad);
			System.out.printf("6. Exit Program.\n", LeftPad);
		System.out.printf("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n",LeftPad);
			usrInput = sc.next();
			if (usrInput.equals("1")){
				UpdateCustomerDetails();
			} else if(usrInput.equals("2")){
				UpdateDoctorInformation();
			} else if(usrInput.equals("3")) {
				UpdateAppointments();
			} else if(usrInput.equals("4")) {
				UpdateBills();
			} else if(usrInput.equals("5")) {
				UpdateReports();
			}

				;//Must be time to quit
		}while(usrInput.equals("6") != true);
		for(int i = 0; i < 7; i++) {
			System.out.printf("\n", LeftPad);
		}
		System.out.printf("~~~Thank you For using our Software~~~", LeftPad);
		sc.close();
		return 0;
		
	}


	private void UpdateCustomerDetails() {
		// TODO Auto-generated method stub
		
		String usrInput = "";
		Scanner sca = new Scanner(System.in);
		System.out.printf("Please choose a selection.\n", LeftPad);
		System.out.printf("1. Create Customer Information.\n", LeftPad);
		System.out.printf("2. Edit Customer Information.\n", LeftPad);
		usrInput = sca.next();
		
		if(usrInput.equals("1")){
			
			CreatingCustomerDetails();	
		}
		else {
			EditCustomerDetails();
		}
		
	}
		
		private void CreatingCustomerDetails() {
		    
			
			try 	
			{

		    Statement st = Con.createStatement();//Creating a statement 
			Scanner sc = new Scanner(System.in);// SCAnner
			
			System.out.printf("~~~Please enter in your information~~~\n");
			System.out.printf("Enter First Name: \n");
			String F_name  = (String) sc.next();// does this need to be string or char??
			System.out.printf("Enter Last Name: \n");
			String L_name  = (String) sc.next();// does this need to be string or char??
		   
			String sql = ("INSERT INTO Patient" +" VALUES (F_name, L_name)");
			
			st.executeUpdate(sql);
			} catch(Exception e) {
			      e.printStackTrace();
			}
			
			}// end private void CreatingCustomerDetails()
			
			private void EditCustomerDetails() {
			    
				try 	
				{
			    Statement st = Con.createStatement();//Creating a statement 
				Scanner sc = new Scanner(System.in);// SCAnner
				
				System.out.printf("~~~Please enter in your information~~~\n");
				System.out.printf("Edit First Name: \n");
				String F_name  = (String) sc.next();// does this need to be string or char??
				System.out.printf("Edit Last Name: \n");
				String L_name  = (String) sc.next();// does this need to be string or char??
			   
				String sql = ("INSERT INTO Patient" + " VALUES (F_name, L_name)");
				
				st.executeUpdate(sql);
				} catch(Exception e) {
				      e.printStackTrace();
				}
		
	}
	
	
	@SuppressWarnings("resource")
	private void UpdateDoctorInformation() {
		System.out.printf("Please choose a selection.\n", LeftPad);
		System.out.printf("1. Create Doctor Information.\n", LeftPad);
		System.out.printf("2. Edit Doctor Information.\n", LeftPad);
		String fName = null;
		String lName = null;
		int ssn = -1;
		int newSsn = -1;
		int usrInpt;
		String stmt;
		PreparedStatement ps;
		String correct = "N";
		Scanner input = new Scanner(System.in);
		usrInpt = input.nextInt();

		while (usrInpt != 1 && usrInpt != 2) {
			System.out.println("Invalid input. Please input either 1 or 2");
			usrInpt = input.nextInt();
		}

		switch(usrInpt) {
			case 1:
				System.out.print("Input doctor's first name: ");
				//add input validation
				fName = input.next();
				System.out.print("Input doctor's last name: ");
				lName = input.next();
				System.out.print("Input doctor's SSN: ");
				ssn = input.nextInt();
				stmt = "insert into DOCTOR (firstname, lastname, doctor_id) values (?, ?, ?)";
				try {
					ps = Con.prepareStatement(stmt);
					ps.clearParameters();
					ps.setString(1, fName);
					ps.setString(2, lName);
					ps.setInt(3, ssn);
					ps.executeUpdate();
				} catch (SQLIntegrityConstraintViolationException uniquenessViolation) {
					System.out.println("A doctor with this SSN already exists! Please try again.");
					break;
				} catch (SQLException e1){
					e1.printStackTrace();
				}
				System.out.println("Doctor added.");
				break;
			case 2:
				while(correct.equals("N")) {
					System.out.print("Input SSN of doctor you would like to edit: ");
					ssn = input.nextInt();
					stmt = "select * from DOCTOR where doctor_id = ?";
					try {
						ps = Con.prepareStatement(stmt);
						ps.clearParameters();
						ps.setInt(1, ssn);
						ResultSet rs = ps.executeQuery();
						if(!rs.next()){
							System.out.println("Doctor could not be found. Please try again.");
							continue;
						}
						ssn = rs.getInt(1);
						fName = rs.getString(2);
						lName = rs.getString(3);
					} catch (SQLException e) {
						e.printStackTrace();
					}
					System.out.println("First Name: " + fName);
					System.out.println("Last Name: " + lName);
					System.out.println("SSN: " + ssn);
					System.out.print("Is this correct? Y/N: ");
					correct = input.next().toUpperCase();
					while(!correct.equals("Y") && !correct.equals("N")) {
						System.out.print("Invalid input. Input \"Y\" for yes or \"N\" for no: ");
						correct = input.next();
					} 
				}
				System.out.println("What attribute would you like to edit?");
				System.out.println("1. First Name");
				System.out.println("2. Last Name");
				System.out.println("3. SSN");
				usrInpt = input.nextInt();
				while(usrInpt < 1 || usrInpt > 3) {
					System.out.println("Invalid input. Please input 1, 2, or 3: ");
					usrInpt = input.nextInt();
				}
				switch(usrInpt) {
					case 1:
						System.out.print("Input new first name: ");
						fName = input.next();
						stmt = "update doctor set FIRSTNAME = ? where doctor_id = " + ssn;
					try {
						ps = Con.prepareStatement(stmt);
						ps.clearParameters();
						ps.setString(1, fName);
						ps.executeUpdate();
					} catch (SQLException e) {
						e.printStackTrace();
					}

						System.out.println("Doctor's first name updated.");
						break;
					case 2:
						System.out.print("Input new last name: ");
						lName = input.next();
						stmt = "update doctor set LASTNAME = ? where doctor_id = " + ssn;
					try {
						ps = Con.prepareStatement(stmt);
						ps.clearParameters();
						ps.setString(1, lName);
						ps.executeUpdate();
					} catch (SQLException e) {
						e.printStackTrace();
					}
						System.out.println("Doctor's last name updated.");
						break;
					case 3:
						System.out.print("Input new SSN: ");
						newSsn = input.nextInt();
						stmt = "update doctor set DOCTOR_ID = ? where doctor_id = " + ssn;
					try {
						ps = Con.prepareStatement(stmt);
						ps.clearParameters();
						ps.setInt(1, newSsn);
						ps.executeUpdate();
					} catch (SQLException e) {
						e.printStackTrace();
					}
						System.out.println("Doctor's SSN updated.");
						break;
				}
				break;
		}
		
	}
	
	private void UpdateAppointments() {
		System.out.printf("Please choose a selection.\n", LeftPad);
		System.out.printf("1. Create Appointments.\n", LeftPad);
		System.out.printf("2. Edit Appointments.\n", LeftPad);
		System.out.printf("3. Cancel Appointments.\n", LeftPad);
		Calendar cal = Calendar.getInstance();
		int patientSsn = -1;
		int doctorSsn = -1;
		int month = -1;
		int day = -1;
		int year = -1;
		int maxDate = 31;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		Timestamp timeAndDate = null;
		int hour;
		int minute;
		String appointmentId = null;
		int usrInpt;
		String correct = "N";
		String stmt;
		PreparedStatement ps;
		Scanner input = new Scanner(System.in);
		usrInpt = input.nextInt();
		
		
		while (usrInpt != 1 && usrInpt != 2 && usrInpt != 3) {
			System.out.println("Invalid input. Please input either 1, 2, or 3");
			usrInpt = input.nextInt();
		}
		
		switch(usrInpt) {
			case 1:
				patientSsn = validateInputForPatientOrDoctor("patient");
				doctorSsn = validateInputForPatientOrDoctor("doctor");
				System.out.print("Input year of appointment: ");
				year = input.nextInt();
				while(year < cal.get(Calendar.YEAR) || year > (cal.get(Calendar.YEAR) + 5)) {
					System.out.print("Invalid year! Please try again: ");
					year = input.nextInt();
				}
				System.out.print("Input month of appointment: ");
				month = input.nextInt();
				while(month < 1 || month > 12) {
					System.out.print("Invalid month! Please try again: ");
					month = input.nextInt();
				}
				if(month == 2) {
					maxDate = 28;
				} else if(month == 4 || month == 6 || month == 9 || month == 11) {
					maxDate = 30;
				}
				System.out.print("Input date of appointment: ");
				day = input.nextInt();
				while(day < 1 || day > maxDate) {
					System.out.print("Invalid date! Please try again:");
					day = input.nextInt();
				}
				System.out.print("Input hour of time of appointment (use 24 hour format): ");
				hour = input.nextInt();
				while(hour < 1 || hour > 24) {
					System.out.print("Invalid hour! Please try again: ");
					hour = input.nextInt();
				}
				System.out.print("Input minute of time of appointment: ");
				minute = input.nextInt();
				while(minute < 0 || minute > 59) {
					System.out.print("Invalid minute! Please try again: ");
					minute = input.nextInt();
				}
				try {
					date = dateFormat.parse(year + "-" + month + "-" + day + " " + hour + ":" + minute + ":00");
				} catch (ParseException e) {
					e.printStackTrace();
				}
				timeAndDate = new Timestamp(date.getTime());
				appointmentId = String.valueOf(patientSsn % 10000) + (doctorSsn % 10000) + (year % 100) + month + day;
				stmt = "insert into appointment (appointment_id, timeanddate, patient_id, doctor_id) "
						+ "values (?, ?, ?, ?)";
				try {
					ps = Con.prepareStatement(stmt);
					ps.clearParameters();
					ps.setDouble(1, Double.parseDouble(appointmentId));
					ps.setTimestamp(2, timeAndDate);
					ps.setInt(3, patientSsn);
					ps.setInt(4, doctorSsn);
					ps.executeUpdate();
				} catch (SQLIntegrityConstraintViolationException uniquenessViolation) {
					System.out.println("This appointment already exists! Please try again.");
					break;
				} catch (SQLException e1){
					e1.printStackTrace();
				}
				System.out.println("Appointment created. ");
				break;
			case 2:
				while(correct.equals("N")) {
					System.out.print("Input ID number of appointment you would like to edit: ");
					appointmentId = input.next();
					stmt = "select * from APPOINTMENT where appointment_id = ?";
					try {
						ps = Con.prepareStatement(stmt);
						ps.clearParameters();
						ps.setString(1, appointmentId);
						ResultSet rs = ps.executeQuery();
						if(!rs.next()){
							System.out.println("Appointment could not be found. Please try again.");
							continue;
						}
						appointmentId = rs.getString(1);
						patientSsn = rs.getInt(2);
						doctorSsn = rs.getInt(3);
						timeAndDate = rs.getTimestamp(4);
					} catch (SQLException e) {
						e.printStackTrace();
					}
					System.out.println("Appointment ID: " + appointmentId);
					System.out.println("Patient SSN: " + patientSsn);
					System.out.println("Doctor SSN: " + doctorSsn);
					System.out.println("Date and Time: " + dateFormat.format(timeAndDate.getTime()));
					System.out.print("Is this correct? Y/N: ");
					correct = input.next().toUpperCase();
					while(!correct.equals("Y") && !correct.equals("N")) {
						System.out.print("Invalid input. Input \"Y\" for yes or \"N\" for no: ");
						correct = input.next();
					} 
				}
				System.out.println("What attribute would you like to edit?");
				System.out.println("1. Patient");
				System.out.println("2. Doctor");
				System.out.println("3. Date and Time");
				usrInpt = input.nextInt();
				while(usrInpt < 1 || usrInpt > 3) {
					System.out.println("Invalid input. Please input 1, 2, or 3: ");
					usrInpt = input.nextInt();
				}
				switch(usrInpt) {
					case 1:
						patientSsn = validateInputForPatientOrDoctor("patient");
						stmt = "update appointment set PATIENT_ID = ? where appointment_id = " + appointmentId;
					try {
						ps = Con.prepareStatement(stmt);
						ps.clearParameters();
						ps.setInt(1, patientSsn);
						ps.executeUpdate();
					} catch (SQLException e) {
						e.printStackTrace();
					}
						System.out.println("Patient updated.");
						break;
					case 2:
						doctorSsn = validateInputForPatientOrDoctor("doctor");
						stmt = "update appointment set DOCTOR_ID = ? where appointment_id = " + appointmentId;
					try {
						ps = Con.prepareStatement(stmt);
						ps.clearParameters();
						ps.setInt(1, doctorSsn);
						ps.executeUpdate();
					} catch (SQLException e) {
						e.printStackTrace();
					}
						System.out.println("Doctor updated.");
						break;
					case 3:
						System.out.print("Input year of appointment: ");
						year = input.nextInt();
						while(year < cal.get(Calendar.YEAR) || year > (cal.get(Calendar.YEAR) + 5)) {
							System.out.print("Invalid year! Please try again: ");
							year = input.nextInt();
						}
						System.out.print("Input month of appointment: ");
						month = input.nextInt();
						while(month < 1 || month > 12) {
							System.out.print("Invalid month! Please try again: ");
							month = input.nextInt();
						}
						if(month == 2) {
							maxDate = 28;
						} else if(month == 4 || month == 6 || month == 9 || month == 11) {
							maxDate = 30;
						}
						System.out.print("Input new date of appointment: ");
						day = input.nextInt();
						while(day < 1 || day > maxDate) {
							System.out.print("Invalid date! Please try again:");
							day = input.nextInt();
						}
						System.out.print("Input hour of time of appointment (use 24 hour format): ");
						hour = input.nextInt();
						while(hour < 1 || hour > 24) {
							System.out.print("Invalid hour! Please try again: ");
							hour = input.nextInt();
						}
						System.out.print("Input minute of time of appointment: ");
						minute = input.nextInt();
						while(minute < 0 || minute > 59) {
							System.out.print("Invalid minute! Please try again: ");
							minute = input.nextInt();
						}
						try {
							date = dateFormat.parse(year + "-" + month + "-" + day + " " + hour + ":" + minute + ":00");
						} catch (ParseException e1) {
							e1.printStackTrace();
						}
						timeAndDate = new Timestamp(date.getTime());
						stmt = "update appointment set TimeAndDate = ? where appointment_id = " + appointmentId;
					try {
						ps = Con.prepareStatement(stmt);
						ps.clearParameters();
						ps.setTimestamp(1, timeAndDate);
						ps.executeUpdate();
					} catch (SQLException e) {
						e.printStackTrace();
					}
						System.out.println("Date and time updated.");
						break;
				}
				break;
			case 3:
				while(correct.equals("N")) {
					System.out.print("Input ID number of appointment you would like to delete: ");
					appointmentId = input.next();
					stmt = "select * from APPOINTMENT where appointment_id = ?";
					try {
						ps = Con.prepareStatement(stmt);
						ps.clearParameters();
						ps.setString(1, appointmentId);
						ResultSet rs = ps.executeQuery();
						if(!rs.next()){
							System.out.println("Appointment could not be found. Please try again.");
							continue;
						}
						appointmentId = rs.getString(1);
						patientSsn = rs.getInt(2);
						doctorSsn = rs.getInt(3);
						timeAndDate = rs.getTimestamp(4);
					} catch (SQLException e) {
						e.printStackTrace();
					}
					System.out.println("Appointment ID: " + appointmentId);
					System.out.println("Patient SSN: " + patientSsn);
					System.out.println("Doctor SSN: " + doctorSsn);
					System.out.println("Date and Time: " + dateFormat.format(timeAndDate.getTime()));
					System.out.print("Is this correct? Y/N: ");
					correct = input.next().toUpperCase();
					while(!correct.equals("Y") && !correct.equals("N")) {
						System.out.print("Invalid input. Input \"Y\" for yes or \"N\" for no: ");
						correct = input.next();
					} 
				}
				stmt = "select * from visit where appointment_id = ?";
				try {
					ps = Con.prepareStatement(stmt);
					ps.clearParameters();
					ps.setString(1, appointmentId);
					ResultSet rs = ps.executeQuery();
					if(rs.next()) {
						System.out.println("Appointment cannot be cancelled; it has already been billed.");
						break;
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				stmt = "delete from appointment where appointment_id = " + appointmentId;
				try {
					ps = Con.prepareStatement(stmt);
					ps.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				System.out.println("Appointment cancelled.");
				break;
		}
	
	}
	
	private int validateInputForPatientOrDoctor(String relation) {
		String correct = "N";
		Scanner input = new Scanner(System.in);
		int ssn = -1;
		String stmt;
		PreparedStatement ps;
		String fName = null;
		String lName = null;
		while(correct.equals("N")) {
			System.out.print("Input SSN of " + relation + ": ");
			ssn = input.nextInt();
			stmt = "select * from " + relation.toUpperCase() + " where " + relation + "_id = ?";
			try {
				ps = Con.prepareStatement(stmt);
				ps.clearParameters();
				ps.setInt(1, ssn);
				ResultSet rs = ps.executeQuery();
				if(!rs.next()){
					System.out.println("This " + relation + " could not be found. Please try again.");
					continue;
				}
				ssn = rs.getInt(1);
				fName = rs.getString(2);
				lName = rs.getString(3);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.out.println("First Name: " + fName);
			System.out.println("Last Name: " + lName);
			System.out.println("SSN: " + ssn);
			System.out.print("Is this correct? Y/N: ");
			correct = input.next().toUpperCase();
			while(!correct.equals("Y") && !correct.equals("N")) {
				System.out.print("Invalid input. Input \"Y\" for yes or \"N\" for no: ");
				correct = input.next();
			} 
		}
		return ssn;
	}
	
	private void UpdateBills() throws SQLException {
		System.out.printf("Please choose a selection.\n", LeftPad);
		System.out.printf("1. Create Bills.\n", LeftPad);
		System.out.printf("2. Make a payment.\n", LeftPad);
		
		int visitId = 0;
		int billId = 0;
		int paymentId = 0;
		String stmt = null;
		double startingAmount = 0;
		double amountLeft = 0;
		PreparedStatement ps;
		ResultSet rs;
		String treatments[] = {null, null, null};
		double appointmentId;
		int numTreatments;
		int paymentAmount;
		String insuranceCompany = null;
		Date dateOfPayment = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String insuranceYN = "N";
		Scanner input = new Scanner(System.in);
		int usrInpt = input.nextInt();
		
		while (usrInpt != 1 && usrInpt != 2 && usrInpt != 3) {
			System.out.println("Invalid input. Please input either 1, 2, or 3");
			usrInpt = input.nextInt();
		}
		
		switch(usrInpt) {
			case 1:
				System.out.print("Input patient's appointment ID: ");
				appointmentId = input.nextDouble();
				System.out.println("How many treatments did the patient get? Input number 1-3: ");
				numTreatments = input.nextInt();
				while(numTreatments < 1 || numTreatments > 3) {
					System.out.println("Invalid input! Please input a number 1-3: ");
					numTreatments = input.nextInt();
				}
				for(int i = 0; i < numTreatments; i++) {
					System.out.println("Input name of treatment " + (i + 1) + " (Checkup, Immunization, Prescription, or Consultation): ");
					treatments[i] = input.next();
					while(!treatments[i].equalsIgnoreCase("Checkup") && !treatments[i].equalsIgnoreCase("Immunization") &&
							!treatments[i].equalsIgnoreCase("Prescription") && !treatments[i].equalsIgnoreCase("Consultation")) {
						System.out.println("Invalid input! Please try again. ");
						treatments[i] = input.next();
					}
				}

				Random rnd = new Random();
			    int number = rnd.nextInt(999999);
			    
			    String.format("%06d", number);
				
				visitId = number;
				billId = number;
				
				stmt = "insert into visit (visit_id, appointment_id, treatment1, treatment2, treatment3) values (?, ?, ?, ?, ?)";
				try {
					ps= Con.prepareStatement(stmt);
					ps.clearParameters();
					ps.setInt(1, visitId);
					ps.setDouble(2, appointmentId);
					ps.setString(3, treatments[0]);
					ps.setString(4, treatments[1]);
					ps.setString(5, treatments[2]);
					ps.executeUpdate();
				}
				catch (SQLIntegrityConstraintViolationException uniquenessViolation) {
					System.out.println("This bill already exists! Please try again."+uniquenessViolation.getMessage());
					break;
				}
				catch(InputMismatchException me) {
					me.getMessage();	
				}
				
				for(int i = 0; i < numTreatments; i++) {
					stmt = "select cost from treatment where exists "
							+ "(select * from visit where treatment" + (i + 1) +" = procedure "
							+ "AND visit_id = ? AND treatment" + (i + 1) + " IS NOT NULL)";
					try {
						ps = Con.prepareStatement(stmt);
						ps.clearParameters();
						ps.setInt(1, visitId);
						rs = ps.executeQuery();
						if(!rs.next()){
							System.out.println("Something went wrong! Please try again.");
							break;
						}
						startingAmount += rs.getInt(1);
					} catch(SQLException e) {
						System.out.println("Something went wrong! Bill amount could not be generated. Please try again.");
					}
				}
				
				amountLeft = startingAmount;
				
				stmt = "insert into bill(bill_id, samount, aleft, visit_id) values (?, ?, ?, ?)";
				try {
					ps= Con.prepareStatement(stmt);
					ps.clearParameters();
					ps.setInt(1,billId);
					ps.setDouble(2,startingAmount);
					ps.setDouble(3,amountLeft);
					ps.setDouble(4, visitId);
					ps.executeUpdate();
				} catch (SQLIntegrityConstraintViolationException uniquenessViolation) {
					System.out.println("This bill already exists! Please try again."+uniquenessViolation.getMessage());
					break;
				} catch (SQLException e1){
					e1.printStackTrace();
				}
				catch(IllegalFormatConversionException ife){
					ife.printStackTrace();
				}
				System.out.println("Bill Created");
				break;
			case 2:
				System.out.println("Input ID for bill you would like to pay: ");
				billId = input.nextInt();
				System.out.println("Input amount of payment: ");
				paymentAmount = input.nextInt();
				stmt = "select aleft from bill where bill_id = ?";
				do {
					try {
						ps = Con.prepareStatement(stmt);
						ps.clearParameters();
						ps.setInt(1, billId);
						rs = ps.executeQuery();
						if(!rs.next()){
							System.out.println("Something went wrong! Please try again.");
							break;
						}
						amountLeft = rs.getDouble(1);
					} catch (SQLException e1){
						e1.printStackTrace();
					}
					if(amountLeft < paymentAmount) {
						System.out.println("Amount of payment cannot be more than amount left to pay. Please try again. ");
						paymentAmount = input.nextInt();
					}
				} while(amountLeft < paymentAmount);
				System.out.println("Is this an insurance payment (Y/N)? ");
				insuranceYN = input.next();
				if(insuranceYN.equals("Y")) {
					System.out.println("Please enter name of insurance company: ");
					insuranceCompany = input.next();
				}
				Random rand = new Random(99999);
				paymentId = rand.nextInt();
				stmt = "insert into payment(payment_id, apay, bill_id, insurancecompany) values(?, ?, ?, ?)";
				try {
					ps = Con.prepareStatement(stmt);
					ps.clearParameters();
					ps.setInt(1, paymentId);
					ps.setInt(2, paymentAmount);
					ps.setInt(3, billId);
					ps.setString(4, insuranceCompany);
				} catch (SQLException e1){
					e1.printStackTrace();
				}
				amountLeft -= paymentAmount;
				stmt = "update bill set aleft = ? where bill_id = ?";
				try {
					ps = Con.prepareStatement(stmt);
					ps.clearParameters();
					ps.setDouble(1, amountLeft);
					ps.setInt(2, billId);
				} catch (SQLException e1){
					e1.printStackTrace();
				}
				System.out.println("Payment made.");
				break;
			}
		}	
	
	private void UpdateReports() {
		// TODO Auto-generated method stub
		
	}

	}
