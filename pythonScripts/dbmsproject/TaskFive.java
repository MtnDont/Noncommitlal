import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Scanner;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class TaskFive
{
    static Scanner sc;
    
    // Database credentials
    final static String HOSTNAME = System.getenv().get("AZURE_HOST");
    final static String DBNAME = System.getenv().get("AZURE_DB");
    final static String USERNAME = System.getenv().get("AZURE_USER");
    final static String PASSWORD = System.getenv().get("AZURE_PASS");

    // Database connection string
    final static String URL = String.format(
            "jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;",
            HOSTNAME, DBNAME, USERNAME, PASSWORD);

    // Queries
    final static String QUERY1 = "INSERT INTO Team VALUES (?, ?, ?, ?, ?);"; // REALLY Done
    final static String QUERY2 = "INSERT INTO Client VALUES (?, ?, ?, ?, ?, ?);"; // Done
    final static String QUERY3 = "INSERT INTO Volunteer VALUES (?, ?, ?, ?);"; // Done
    final static String QUERY4 = "EXEC insertNumHoursVolunteerTeam @ssn = ?, @teamname = ?, @hours = ?;"; // Done
    final static String QUERY5 = "INSERT INTO Employee VALUES (?, ?, ?, ?);"; // Done
    final static String QUERY6 = "EXEC expenseFromEmployee @ssn = ?, @expensedate = ?, @amount = ?, @expensedescription = ?;"; // REALLY Done
    final static String QUERY7 = "INSERT INTO Organization VALUES (?, ?, ?, ?);"; // Done
    final static String QUERY8 = "INSERT INTO Donor VALUES (?);"; // Done
    final static String QUERY10 = "EXEC getClientDoctorNameNumber @clientssn = ?;"; // REALLY Done
    final static String QUERY11 = "EXEC getTotalEmployeeExpensesOverTime @before = ?, @after = ?;"; // REALLY Done
    final static String QUERY12 = "EXEC getVolunteersWhoseTeamSupportsClient @clientssn = ?;"; // REALLY Done
    final static String QUERY13 = "EXEC getClientNamesContactsWithOrgBetweenBK;"; // REALLY Done
    final static String QUERY14 = "EXEC getNameOfEmployeeDonors;"; // REALLY Done
    final static String QUERY15 = "EXEC getTeamNamesAfter @dateAfter = ?;"; // REALLY Done
    final static String QUERY16 = "EXEC increaseEmployeeSalaryMultipleTeams;"; // REALLY Done
    final static String QUERY17 = "EXEC deleteClientHealthTransport;"; // REALLY Done
    final static String QUERY18 = "INSERT INTO Team VALUES (?, ?, ?, ?, ?);"; // REALLY Done
    final static String QUERY19 = "EXEC getNamesMailingList;"; // REALLY Done

    // User input prompt
    final static String PROMPT = "\nWELCOME TO THE PATIENT ASSISTANT NETWORK DATABASE SYSTEM\n" +
            "\nPlease select one of the options below: \n" +
            "(1)  Enter a new team into the database. \n" +
            "(2)  Enter a new client into the database and asssociate him or her with one or more teams. \n" + 
            "(3)  Enter a new volunteer into the database and associate him or her with one or more teams. \n" +
            "(4)  Enter the number of hours a volunteer worked this month for a particular team. \n" +
            "(5)  Enter a new employee into the database and associate him or her with one or more teams. \n" +
            "(6)  Enter an expense charged by an employee. \n" +
            "(7)  Enter a new organization and associate it to one or more PAN teams. \n" +
            "(8)  Enter a new donor and associate him or her with several donations. \n" +
            "(9)  Enter a new organization and associate it with several donations. \n" +
            "(10) Retrive the name and phone number of the doctor of a particular client. \n" +
            "(11) Retrieve the total amount of expenses charged by each employee for a particular period of time. The list should be sorted by the total amount of expenses. \n" +
            "(12) Retrieve the list of volunteers that are members of teams that support a particular client. \n" +
            "(13) Retrieve the names and contact information of the clients that are supported by teams sponsored by an organization whose name starts with a letter between B and K. The client list should be sorted by name. \n" +
            "(14) Retrieve the name and total amount donated by donors that are also employees. The list should be sorted by the total amount of the donations,and indicate if each donor wishes to remain anonymous. \n" +
            "(15) Retrieve the names of all teams that were founded after a particular date. \n" +
            "(16) Increase the salary by 10% of all employees to whom more than one team must report. \n" +
            "(17) Delete all clients who do not have health insurance and whose value of importance for transportation is less than 5. \n" +
            "(18) Import: enter new teams from a data file until the file is empty. \n" +
            "(19) Export: Retrieve names and mailing addresses of all people on the mailing listand output them to a data file instead of screen. \n" +
            "(20) Quit";

    // Method to add Person row to database as it is often called
    public static void createPerson(Connection connection, String ssn) throws SQLException
    {
        // Basic query
        String query = "INSERT INTO Person VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        // User input
        System.out.println("Please enter the person's affiliation, if none hit enter:");
        String orgname = sc.nextLine();
        System.out.println("Please enter the person's name:");
        String pname = sc.nextLine();
        System.out.println("Please enter the person's date of birth in the format(yyyy-MM-dd):");
        String dob = sc.nextLine();
        System.out.println("Please enter the person's race:");
        String race = sc.nextLine();
        System.out.println("Please enter the person's gender:");
        String gender = sc.nextLine();
        System.out.println("Please enter the person's profession:");
        String profession = sc.nextLine();
        System.out.println("Please enter if the person is on the PAN mailing list Y/N:");
        
        // Make sure mailinglist is proper input so types can be converted
        String onmailinglist = sc.nextLine();
        while(!(onmailinglist.toLowerCase().contains("y") || onmailinglist.toLowerCase().contains("n"))) {
            System.out.println("Please enter Y or N");
            onmailinglist = sc.nextLine();
        }
            
        try (final PreparedStatement statement = connection.prepareStatement(query))
        {
            // Populate the query template with the data collected from the user
            statement.setString(1, ssn);
            if (orgname.equals(""))
                statement.setNull(2, java.sql.Types.VARCHAR);
            else
                statement.setString(2, orgname);
            statement.setString(3, pname);
            statement.setString(4, dob);
            statement.setString(5, race);
            statement.setString(6, gender);
            statement.setString(7, profession);
            if (onmailinglist.toLowerCase().contains("y"))
                statement.setString(8, "1");
            else 
                statement.setString(8, "0");
            
            // Execute query
            statement.executeUpdate();
        }
        
        createContactInfo(connection, ssn);
        
        // Create multiple emergency contacts
        do {
            createEmergencyContact(connection, ssn);
            System.out.println("Would you like to enter another emergency contact (Y/N)?");
        } while (sc.nextLine().toLowerCase().equals("y"));
        
        
    }
    
    // Method to add ContactInfo row to database as it is often called
    public static void createContactInfo(Connection connection, String ssn) throws SQLException
    {
        // Basic query
        String query = "INSERT INTO ContactInfo VALUES (?, ?, ?, ?, ?, ?)";

        // User input
        System.out.println("Please enter the person's address:");
        String addr = sc.nextLine();
        System.out.println("Please enter the person's email:");
        String email = sc.nextLine();
        System.out.println("Please enter the person's home phone number:");
        String homePhone = sc.nextLine();
        System.out.println("Please enter the person's work phone number:");
        String workPhone = sc.nextLine();
        System.out.println("Please enter the person's cell phone number:");
        String cellPhone = sc.nextLine();
            
        try (final PreparedStatement statement = connection.prepareStatement(query))
        {
            // Populate the query template with the data collected from the user
            statement.setString(1, ssn);
            statement.setString(2, addr);
            statement.setString(3, email);
            statement.setString(4, homePhone);
            statement.setString(5, workPhone);
            statement.setString(6, cellPhone);
            
            // Execute query
            statement.executeUpdate();
        }
        
    }
    
    // Method to add EmergencyContact row to database as it is often called
    public static void createEmergencyContact(Connection connection, String ssn) throws SQLException
    {
        // Basic query
        String query = "INSERT INTO EmergencyContact VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        // User input
        System.out.println("Please enter the emergency contact's name:");
        String ecname = sc.nextLine();
        System.out.println("Please enter the emergency contact's relationship to the person:");
        String relation = sc.nextLine();
        System.out.println("Please enter the emergency contact's address:");
        String addr = sc.nextLine();
        System.out.println("Please enter the emergency contact's email:");
        String email = sc.nextLine();
        System.out.println("Please enter the emergency contact's home phone number:");
        String homePhone = sc.nextLine();
        System.out.println("Please enter the emergency contact's work phone number:");
        String workPhone = sc.nextLine();
        System.out.println("Please enter the emergency contact's cell phone number:");
        String cellPhone = sc.nextLine();
            
        try (final PreparedStatement statement = connection.prepareStatement(query))
        {
            // Populate the query template with the data collected from the user
            statement.setString(1, ssn);
            statement.setString(2, ecname);
            statement.setString(3, relation);
            statement.setString(4, addr);
            statement.setString(5, email);
            statement.setString(6, homePhone);
            statement.setString(7, workPhone);
            statement.setString(8, cellPhone);
            
            // Execute query
            statement.executeUpdate();
        }
        
    }
    
    // Method to add Insurance row to database as it is often called
    public static void createInsurance(Connection connection, String ssn) throws SQLException
    {
        // Basic query
        // policyid is not included in the query as it will increment automatically in the table
        String query = "INSERT INTO Insurance (ssn, providerid, addr, insurancetype) VALUES (?, ?, ?, ?)";
        
        // User input
        System.out.println("Please enter the insurance provider ID:");
        int providerid = sc.nextInt();
        sc.nextLine();
        System.out.println("Please enter the insurance company's address:");
        String addr = sc.nextLine();
        System.out.println("Please enter the insurance type:");
        String insurancetype = sc.nextLine();
            
        try (final PreparedStatement statement = connection.prepareStatement(query))
        {
            // Populate the query template with the data collected from the user
            statement.setString(1, ssn);
            statement.setInt(2, providerid);
            statement.setString(3, addr);
            statement.setString(4, insurancetype);
            
            // Execute query
            statement.executeUpdate();
        }
        
    }
    
    // Method to add Needs row to database as it is often called
    public static void createNeeds(Connection connection, String ssn) throws SQLException
    {
        // Basic query
        String query = "INSERT INTO Needs VALUES (?, ?, ?)";

        // User input
        System.out.println("Please enter the name of the client's need:");
        String nname = sc.nextLine();
        System.out.println("Please enter the need's importance from 1-10:");
        int importance = sc.nextInt();
        sc.nextLine();
            
        try (final PreparedStatement statement = connection.prepareStatement(query))
        {
            // Populate the query template with the data collected from the user
            statement.setString(1, ssn);
            statement.setInt(2, importance);
            statement.setString(3, nname);
            
            // Execute query
            statement.executeUpdate();
        }
        
    }
    
    // Method to add Organization row to database as it is often called
    public static void createOrganization(Connection connection, String orgname) throws SQLException
    {
        // Basic query
        String query = "INSERT INTO Organization VALUES (?, ?, ?, ?)";
        
        // User input
        System.out.println("Please enter the organization's email:");
        String email = sc.nextLine();
        System.out.println("Please enter the organization's phone number:");
        String phone = sc.nextLine();
        System.out.println("Please enter the name of the organization's contact:");
        String contact = sc.nextLine();
        
        try (final PreparedStatement statement = connection.prepareStatement(query))
        {
            // Populate the query template with the data collected from the user
            statement.setString(1, orgname);
            statement.setString(2, email);
            statement.setString(3, phone);
            statement.setString(4, contact);
            
            // Execute query
            statement.executeUpdate();
        }
        
        System.out.println("Is the organization a business or a church?");
        String borc = sc.nextLine().toLowerCase();
        while (!(borc.equals("business") || borc.equals("church"))) {
            System.out.println("Please enter business or church:");
            borc = sc.nextLine().toLowerCase();
        }
        // create abstraction as assigned by user input
        if (borc.equals("business")) {
            createBusiness(connection, orgname);
        }
        else {
            createChurch(connection, orgname);
        }
        
        
    }
    
    // Method to add Business row to database as it is often called
    public static void createBusiness(Connection connection, String orgname) throws SQLException
    {
        // Basic query
        String query = "INSERT INTO Business VALUES (?, ?, ?, ?);";
        
        // User input
        System.out.println("Please enter the type of the business:");
        String businesstype = sc.nextLine();
        System.out.println("Please enter the size of the business:");
        int businesssize = sc.nextInt();
        sc.nextLine();
        System.out.println("Please enter the business's website:");
        String website = sc.nextLine();
        
        try (final PreparedStatement statement = connection.prepareStatement(query))
        {
            // Populate the query template with the data collected from the user
            statement.setString(1, orgname);
            statement.setString(2, businesstype);
            statement.setInt(3, businesssize);
            statement.setString(4, website);
            
            // Execute query
            statement.executeUpdate();
        }
        
    }
    
    // Method to add Church row to database as it is often called
    public static void createChurch(Connection connection, String orgname) throws SQLException
    {
        // Basic query
        String query = "INSERT INTO Church VALUES (?, ?);";
        
        // User input
        System.out.println("Please enter the church's religious affiliation:");
        String religiousaffiliation = sc.nextLine();
        
        try (final PreparedStatement statement = connection.prepareStatement(query))
        {
            // Populate the query template with the data collected from the user
            statement.setString(1, orgname);
            statement.setString(2, religiousaffiliation);
            
            // Execute query
            statement.executeUpdate();
        }
        
    }
    
    // Method to add Sponsors row to database as it is often called
    public static void createSponsors(Connection connection, String orgname) throws SQLException
    {
        // Basic query
        String query = "INSERT INTO Sponsors VALUES (?, ?);";
        
        // User input
        System.out.println("Please enter the team the organization sponsors:");
        String teamname = sc.nextLine();
        
        try (final PreparedStatement statement = connection.prepareStatement(query))
        {
            // Populate the query template with the data collected from the user
            statement.setString(1, teamname);
            statement.setString(2, orgname);
            
            // Execute query
            statement.executeUpdate();
        }
        
    }
    
    // Method to add DonorDonation row to database as it is often called
    public static void createDonorDonation(Connection connection, String ssn) throws SQLException
    {
        // Basic query
        String query = "INSERT INTO DonorDonation VALUES (?, ?, ?, ?, ?);";
        
        // User input
        System.out.println("Please enter the donation amount:");
        float amount = sc.nextFloat();
        sc.nextLine();
        System.out.println("Please enter the date of the donation:");
        String donationdate = sc.nextLine();
        System.out.println("Please enter the donation campaign:");
        String campaign = sc.nextLine();
        System.out.println("Please enter if the donation is anonymous (Y/N):");
        String isAnonymous = sc.nextLine();
        
        try (final PreparedStatement statement = connection.prepareStatement(query))
        {
            // Populate the query template with the data collected from the user
            statement.setString(1, ssn);
            statement.setFloat(2, amount);
            statement.setString(3, donationdate);
            statement.setString(4, campaign);
            if (isAnonymous.contains("Y"))
                statement.setString(5, "1");
            else
                statement.setString(5, "0");
            
            // Execute query
            statement.executeUpdate();
        }
        
        System.out.println("Is the Donation payed with a check or credit?");
        String checkcredit = sc.nextLine().toLowerCase();
        while (!(checkcredit.equals("check") || checkcredit.equals("credit"))) {
            System.out.println("Please enter check or credit:");
            checkcredit = sc.nextLine().toLowerCase();
        }
        if (checkcredit.equals("check")) {
            createDonorCheck(connection, ssn, amount, donationdate);
        }
        else {
            createDonorCredit(connection, ssn, amount, donationdate);
        }
        
    }
    
    // Method to add DonorCheck row to database as it is often called
    public static void createDonorCheck(Connection connection, String ssn, float amount, String donationdate) throws SQLException
    {
        // Basic query
        String query = "INSERT INTO DonorCheck VALUES (?, ?, ?, ?);";
        
        // User input
        System.out.println("Please enter if the donation's check number:");
        int checkNum = sc.nextInt();
        sc.nextLine();
        
        try (final PreparedStatement statement = connection.prepareStatement(query))
        {
            // Populate the query template with the data collected from the user
            statement.setString(1, ssn);
            statement.setFloat(2, amount);
            statement.setString(3, donationdate);
            statement.setInt(4, checkNum);
            
            // Execute query
            statement.executeUpdate();
        }
        
    }
    
    // Method to add DonorCredit row to database as it is often called
    public static void createDonorCredit(Connection connection, String ssn, float amount, String donationdate) throws SQLException
    {
        // Basic query
        String query = "INSERT INTO DonorCredit VALUES (?, ?, ?, ?, ?, ?);";
        
        // User input
        System.out.println("Please enter if the donation's card number:");
        String cardnum = sc.nextLine();
        System.out.println("Please enter if the card's type:");
        String cardtype = sc.nextLine();
        System.out.println("Please enter if the card's expiration date in the format (MM/yy):");
        String expirdate = sc.nextLine();
        
        try (final PreparedStatement statement = connection.prepareStatement(query))
        {
            // Populate the query template with the data collected from the user
            statement.setString(1, ssn);
            statement.setFloat(2, amount);
            statement.setString(3, donationdate);
            statement.setString(4, cardnum);
            statement.setString(5, cardtype);
            statement.setString(6, expirdate);
            
            // Execute query
            statement.executeUpdate();
        }
        
    }
    
    // Method to add OrgDonation row to database as it is often called
    public static void createOrgDonation(Connection connection, String orgname) throws SQLException
    {
        // Basic query
        String query = "INSERT INTO OrgDonation VALUES (?, ?, ?, ?, ?);";
        
        // User input
        System.out.println("Please enter the donation amount:");
        float amount = sc.nextFloat();
        sc.nextLine();
        System.out.println("Please enter the date of the donation:");
        String donationdate = sc.nextLine();
        System.out.println("Please enter the donation campaign:");
        String campaign = sc.nextLine();
        System.out.println("Please enter if the donation is anonymous (Y/N):");
        String isAnonymous = sc.nextLine();
        
        try (final PreparedStatement statement = connection.prepareStatement(query))
        {
            // Populate the query template with the data collected from the user
            statement.setString(1, orgname);
            statement.setFloat(2, amount);
            statement.setString(3, donationdate);
            statement.setString(4, campaign);
            if (isAnonymous.contains("Y"))
                statement.setString(5, "1");
            else
                statement.setString(5, "0");
            
            // Execute query
            statement.executeUpdate();
        }
        
        System.out.println("Is the Donation payed with a check or credit?");
        String checkcredit = sc.nextLine().toLowerCase();
        while (!(checkcredit.equals("check") || checkcredit.equals("credit"))) {
            System.out.println("Please enter check or credit:");
            checkcredit = sc.nextLine().toLowerCase();
        }
        if (checkcredit.equals("check")) {
            createOrgCheck(connection, orgname, amount, donationdate);
        }
        else {
            createOrgCredit(connection, orgname, amount, donationdate);
        }
        
    }
    
    // Method to add OrgCheck row to database as it is often called
    public static void createOrgCheck(Connection connection, String orgname, float amount, String donationdate) throws SQLException
    {
        // Basic query
        String query = "INSERT INTO OrgCheck VALUES (?, ?, ?, ?);";
        
        // User input
        System.out.println("Please enter if the donation's check number:");
        int checkNum = sc.nextInt();
        sc.nextLine();
        
        try (final PreparedStatement statement = connection.prepareStatement(query))
        {
            // Populate the query template with the data collected from the user
            statement.setString(1, orgname);
            statement.setFloat(2, amount);
            statement.setString(3, donationdate);
            statement.setInt(4, checkNum);
            
            // Execute query
            statement.executeUpdate();
        }
        
    }
    
    // Method to add OrgCredit row to database as it is often called
    public static void createOrgCredit(Connection connection, String orgname, float amount, String donationdate) throws SQLException
    {
        // Basic query
        String query = "INSERT INTO OrgCredit VALUES (?, ?, ?, ?, ?, ?);";
        
        // User input
        System.out.println("Please enter if the donation's card number:");
        String cardnum = sc.nextLine();
        System.out.println("Please enter if the card's type:");
        String cardtype = sc.nextLine();
        System.out.println("Please enter if the card's expiration date in the format (MM/yy):");
        String expirdate = sc.nextLine();
        
        try (final PreparedStatement statement = connection.prepareStatement(query))
        {
            // Populate the query template with the data collected from the user
            statement.setString(1, orgname);
            statement.setFloat(2, amount);
            statement.setString(3, donationdate);
            statement.setString(4, cardnum);
            statement.setString(5, cardtype);
            statement.setString(6, expirdate);
            
            // Execute query
            statement.executeUpdate();
        }
        
    }
    
    public static void main(String[] args) throws SQLException
    {
        System.out.println("Welcome to the Faculty Insertion Application!");

        sc = new Scanner(System.in); // Scanner is used to collect the user input
        String option = ""; // Initialize user option selection as nothing
        while (!option.equals("20"))
        { // As user for options until option 20 is selected
            System.out.println(PROMPT); // Print the available options
            option = sc.nextLine(); // Read in the user option selection

            switch (option)
            { // Switch between different options
                case "1":
                    // Take user input
                    System.out.println("Please enter the team's name:");
                    String query1_teamname = sc.nextLine();
                    System.out.println("Please enter the team leader's SSN:");
                    String query1_leaderssn = sc.nextLine();
                    System.out.println("Please enter the SSN of the employee the team reports to:");
                    String query1_employeessn = sc.nextLine();
                    System.out.println("Please enter the team's type:");
                    String query1_teamtype = sc.nextLine();
                    System.out.println("Please enter the team's formation date:");
                    String query1_formationdate = sc.nextLine();

                    try (final Connection connection = DriverManager.getConnection(URL))
                    {
                        try (final PreparedStatement statement = connection.prepareStatement(QUERY1))
                        {
                            // Populate the query template with the data collected from the user
                            statement.setString(1, query1_teamname);
                            statement.setString(2, query1_leaderssn);
                            statement.setString(3, query1_employeessn);
                            statement.setString(4, query1_teamtype);
                            statement.setString(5, query1_formationdate);
                            
                            // Execute update
                            final int rows_inserted = statement.executeUpdate();
                            System.out.println(String.format("Done. %d row(s) inserted.", rows_inserted));
                        }
                    }
                    break;
                case "2":
                    try (final Connection connection = DriverManager.getConnection(URL))
                    {
                        // Take user input
                        System.out.println("Please enter the client's SSN");
                        String client_ssn = sc.nextLine();
                        // Create Person table
                        createPerson(connection, client_ssn);
                        System.out.println("Please enter the client's attorney's name:");
                        String att_name = sc.nextLine();
                        System.out.println("Please enter the client's attorney's phone number:");
                        String att_num = sc.nextLine();
                        System.out.println("Please enter the client's doctor's name:");
                        String doc_name = sc.nextLine();
                        System.out.println("Please enter the client's doctor's phone number:");
                        String doc_num = sc.nextLine();
                        System.out.println("Please enter the date the client was first assigned to the organization:");
                        String date_assigned = sc.nextLine();
                        try (final PreparedStatement statement = connection.prepareStatement(QUERY2))
                        {
                            // Populate the query template with the data collected from the user
                            statement.setString(1, client_ssn);
                            statement.setString(2, att_name);
                            statement.setString(3, att_num);
                            statement.setString(4, doc_name);
                            statement.setString(5, doc_num);
                            statement.setString(6, date_assigned);
                            
                            // Execute query
                            statement.executeUpdate();
                        }
                        
                        do {
                            createInsurance(connection, client_ssn);
                            System.out.println("Insert another insurance policy (Y/N)?");
                        } while (!sc.nextLine().toLowerCase().equals("n"));
                        
                        do {
                            createNeeds(connection, client_ssn);
                            System.out.println("Insert another insurance policy (Y/N)?");
                        } while (!sc.nextLine().toLowerCase().equals("n"));
                        
                        do {
                            System.out.println("Please enter the team name associated with the client");
                            String query2_teamname = sc.nextLine();
                            String teamclientquery = "INSERT INTO CaresFor VALUES (?, ?, ?)";
                            try (final PreparedStatement statement = connection.prepareStatement(teamclientquery))
                            {
                                // Populate the query template with the data collected from the user
                                statement.setString(1, client_ssn);
                                statement.setString(2, query2_teamname);
                                statement.setString(3, "1");
                                
                                // Execute query
                                statement.executeUpdate();
                            }
                            System.out.println("Associate the client with another team? (Y/N)");
                        } while (!sc.nextLine().toLowerCase().equals("n"));
                    }
                    break;
                case "3":
                    try (final Connection connection = DriverManager.getConnection(URL))
                    {
                        // Take user input
                        System.out.println("Please enter the volunteer's SSN");
                        String volunteer_ssn = sc.nextLine();
                        createPerson(connection, volunteer_ssn);
                        System.out.println("Please enter the date the volunteer joined PAN:");
                        String joinDate = sc.nextLine();
                        System.out.println("Please enter the date the volunteer was last trained:");
                        String trainDate = sc.nextLine();
                        System.out.println("Please enter the location the volunteer was last trained at:");
                        String trainLoc = sc.nextLine();
                        try (final PreparedStatement statement = connection.prepareStatement(QUERY3))
                        {
                            // Populate the query template with the data collected from the user
                            statement.setString(1, volunteer_ssn);
                            statement.setString(2, joinDate);
                            statement.setString(3, trainDate);
                            statement.setString(4, trainLoc);
                            
                            // Execute query
                            statement.executeUpdate();
                        }
                        do {
                            System.out.println("Please enter the team name associated with the client");
                            String query3_teamname = sc.nextLine();
                            String teamclientquery = "INSERT INTO Volunteers VALUES (?, ?, ?, ?)";
                            try (final PreparedStatement statement = connection.prepareStatement(teamclientquery))
                            {
                                // Populate the query template with the data collected from the user
                                statement.setString(1, volunteer_ssn);
                                statement.setString(2, query3_teamname);
                                statement.setString(3, "1");
                                statement.setInt(4, 0);
                                
                                // Execute query
                                statement.executeUpdate();
                            }
                            System.out.println("Associate the client with another team? (Y/N)");
                        } while (!sc.nextLine().toLowerCase().equals("n"));
                    }
                    break;
                case "4":
                    // Take user input
                    System.out.println("Please enter the volunteer's SSN:");
                    String vol_ssn = sc.nextLine();
                    System.out.println("Please enter the team's name:");
                    String vol_team = sc.nextLine();
                    System.out.println("Please enter the number of hours the volunteer has worked this month for the team:");
                    int vol_hours = sc.nextInt();
                    sc.nextLine();
                    try (final Connection connection = DriverManager.getConnection(URL))
                    {
                        try (final PreparedStatement statement = connection.prepareStatement(QUERY4))
                        {
                            // Populate the query template with the data collected from the user
                            statement.setString(1, vol_ssn);
                            statement.setString(2, vol_team);
                            statement.setInt(3, vol_hours);
                            
                            // Execute query
                            final int rows_inserted = statement.executeUpdate();
                            System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
                        }
                    }
                    break;
                case "5":
                    // Take user input
                    System.out.println("Please enter the employee's SSN:");
                    String query5_ssn = sc.nextLine();
                    try (final Connection connection = DriverManager.getConnection(URL))
                    {
                        // Create person row
                        createPerson(connection, query5_ssn);
                        
                        // Take more user input
                        System.out.println("Please enter the employee's salary:");
                        String query5_salary = sc.nextLine();
                        System.out.println("Please enter the employee's marital status:");
                        String query5_maritalstatus = sc.nextLine();
                        System.out.println("Please enter the date the employee was hired:");
                        String query5_hiredate = sc.nextLine();
                    
                        try (final PreparedStatement statement = connection.prepareStatement(QUERY5))
                        {
                            // Populate the query template with the data collected from the user
                            statement.setString(1, query5_ssn);
                            statement.setString(2, query5_salary);
                            statement.setString(3, query5_maritalstatus);
                            statement.setString(4, query5_hiredate);
                            
                            // Execute query
                            final int rows_inserted = statement.executeUpdate();
                            System.out.println(String.format("Done. %d row(s) inserted.", rows_inserted));
                        }

                        // Second query to generate row for association
                        String case5query2 = "EXEC associateEmployeeWithTeam @employeessn = ?, @teamname = ?";
                        do {
                            System.out.println("Please enter the name of the team to associate with the employee:");
                            String query5_teamname = sc.nextLine();
                            try (final PreparedStatement statement = connection.prepareStatement(case5query2))
                            {
                                // Populate the query template with the data collected from the user
                                statement.setString(1, query5_ssn);
                                statement.setString(2, query5_teamname);
                                
                                // Execute query
                                final int rows_inserted = statement.executeUpdate();
                                System.out.println(String.format("Done. %d row(s) updated.", rows_inserted));
                            }
                            System.out.println("Would you like to associate the employee with another team?(Y/N):");
                        } while (!sc.nextLine().toLowerCase().equals("n"));
                    }
                    break;
                case "6":
                    // Take user input 
                    System.out.println("Please enter the employee's SSN:");
                    String query6_ssn = sc.nextLine();
                    
                    System.out.println("Please enter the date of the expense:");
                    String query6_expensedate = sc.nextLine();
                    
                    System.out.println("Please enter the expense amount:");
                    float query6_amount = sc.nextFloat();
                    sc.nextLine();
                    
                    System.out.println("Please enter the description of the expense:");
                    String query6_expensedescription = sc.nextLine();
                    
                    try (final Connection connection = DriverManager.getConnection(URL))
                    {
                        try (final PreparedStatement statement = connection.prepareStatement(QUERY6))
                        {
                            // Populate the query template with the data collected from the user
                            statement.setString(1, query6_ssn);
                            statement.setString(2, query6_expensedate);
                            statement.setFloat(3, query6_amount);
                            statement.setString(4, query6_expensedescription);
                            
                            // Execute query
                            final int rows_inserted = statement.executeUpdate();
                            System.out.println(String.format("Done. %d row(s) inserted.", rows_inserted));
                        }
                    }
                    break;
                case "7":
                    // Take user input
                    System.out.println("Please enter the name of the organization:");
                    String query7_orgname = sc.nextLine();
                    try (final Connection connection = DriverManager.getConnection(URL))
                    {
                        // Create new organization row
                        createOrganization(connection, query7_orgname);
                        // Create one or more sponsors
                        do {
                            createSponsors(connection, query7_orgname);
                            System.out.println("Would you like to associate the organization with another team?(Y/N):");
                        } while (!sc.nextLine().toLowerCase().equals("n"));
                    }
                    
                    break;
                case "8":
                    // Take user input
                    System.out.println("Please enter the donor's SSN:");
                    String query8_ssn = sc.nextLine();
                    
                    try (final Connection connection = DriverManager.getConnection(URL))
                    {
                        // Create new person row
                        createPerson(connection, query8_ssn);
                        try (final PreparedStatement statement = connection.prepareStatement(QUERY8))
                        {
                            // Populate the query template with the data collected from the user
                            statement.setString(1, query8_ssn);
                            
                            // Execute query
                            statement.executeUpdate();
                        }
                        // Create one or more donations
                        do {
                            createDonorDonation(connection, query8_ssn);
                            System.out.println("Associate the donor with another donation? (Y/N)");
                        } while (!sc.nextLine().toLowerCase().equals("n"));
                    }
                    break;
                case "9":
                    // Take user input
                    System.out.println("Please enter the name of the organization:");
                    String query9_orgname = sc.nextLine();
                    try (final Connection connection = DriverManager.getConnection(URL))
                    {
                        // Create new organization row
                        createOrganization(connection, query9_orgname);

                        // Create one or more donations
                        do {
                            createOrgDonation(connection, query9_orgname);
                            System.out.println("Would you like to associate the organization with another donation?(Y/N):");
                        } while (!sc.nextLine().toLowerCase().equals("n"));
                    }
                    break;
                case "10": 
                    // Collect the client data from the user
                    System.out.println("Please enter client SSN:");
                    final String ssn = sc.nextLine(); // Read in the user input of client ssn

                    System.out.println("Connecting to the database...");
                    // Get a database connection and prepare a query statement
                    try (final Connection connection = DriverManager.getConnection(URL))
                    {
                        try (final PreparedStatement statement = connection.prepareStatement(QUERY10))
                        {
                            // Populate the query template with the data collected from the user
                            statement.setString(1, ssn);
                            ResultSet resultSet = statement.executeQuery();
                            System.out.println("Doctor Name | Doctor Number");
                            
                            while (resultSet.next())
                            {
                                System.out.println(String.format("%s | %s ", resultSet.getString(1),
                                        resultSet.getString(2)));
                            }
                        }
                    }

                    break;
                case "11":
                    System.out.println("Please enter date of expenses after:");
                    final String strAfter = sc.nextLine(); // Read in the user input of client ssn
                    
                    System.out.println("Please enter date of expenses before:");
                    final String strBefore = sc.nextLine(); // Read in the user input of client ssn
                    
                    System.out.println("Connecting to the database...");
                    // Get a database connection and prepare a query statement
                    try (final Connection connection = DriverManager.getConnection(URL))
                    {
                        try (final PreparedStatement statement = connection.prepareStatement(QUERY11))
                        {
                            // Populate the query template with the data collected from the user
                            statement.setString(1, strBefore);
                            statement.setString(2, strAfter);
                            ResultSet resultSet = statement.executeQuery();
                            System.out.println("Employee SSN | Total Expenses");
                            
                            while (resultSet.next())
                            {
                                System.out.println(String.format("%s | %s ", resultSet.getString(1),
                                        resultSet.getString(2)));
                            }
                        }
                    }
                    break;
                case "12":
                    System.out.println("Please enter the client's SSN:");
                    final String query12_ssn = sc.nextLine(); // Read in the user input of client ssn
                    
                    System.out.println("Connecting to the database...");
                    // Get a database connection and prepare a query statement
                    try (final Connection connection = DriverManager.getConnection(URL))
                    {
                        try (final PreparedStatement statement = connection.prepareStatement(QUERY12))
                        {
                            // Populate the query template with the data collected from the user
                            statement.setString(1, query12_ssn);
                            ResultSet resultSet = statement.executeQuery();
                            System.out.println("Volunteer SSN | Join Date | Last Training Date | Last Training Location");
                            
                            while (resultSet.next())
                            {
                                System.out.println(String.format("%s | %s | %s | %s ", resultSet.getString(1),
                                        resultSet.getString(2), resultSet.getString(3), resultSet.getString(4)));
                            }
                        }
                    }
                    break;
                case "13":
                    System.out.println("Connecting to the database...");
                    // Get the database connection, create statement and execute it right away, as
                    // no user input need be collected
                    try (final Connection connection = DriverManager.getConnection(URL))
                    {
                        System.out.println("Dispatching the query...");
                        try (final PreparedStatement statement = connection.prepareStatement(QUERY13))
                        {
                            ResultSet resultSet = statement.executeQuery();
                            System.out.println("Name | Address | Email | Home Phone | Work Phone | Cell Phone ");

                            // Unpack the tuples returned by the database and print them out to the user
                            while (resultSet.next())
                            {
                                System.out.println(String.format("%s | %s | %s | %s | %s | %s ", resultSet.getString(1),
                                        resultSet.getString(2), resultSet.getString(3), resultSet.getString(4),
                                        resultSet.getString(5), resultSet.getString(6)));
                            }
                        }
                    }
                    break;
                case "14":
                    System.out.println("Connecting to the database...");
                    // Get the database connection, create statement and execute it right away, as
                    // no user input need be collected
                    try (final Connection connection = DriverManager.getConnection(URL))
                    {
                        System.out.println("Dispatching the query...");
                        try (final PreparedStatement statement = connection.prepareStatement(QUERY14))
                        {
                            ResultSet resultSet = statement.executeQuery();
                            System.out.println("Name | Total Donations | Is Anonymous ");

                            // Unpack the tuples returned by the database and print them out to the user
                            while (resultSet.next())
                            {
                                System.out.println(String.format("%s | %s | %s ", resultSet.getString(1),
                                        resultSet.getString(2), resultSet.getString(3)));
                            }
                        }
                    }
                    break;
                case "15":
                    System.out.println("Please enter the date before teams were formed:");
                    final String query15_date = sc.nextLine(); // Read in the user input of client ssn
                    
                    System.out.println("Connecting to the database...");
                    // Get a database connection and prepare a query statement
                    try (final Connection connection = DriverManager.getConnection(URL))
                    {
                        try (final PreparedStatement statement = connection.prepareStatement(QUERY15))
                        {
                            // Populate the query template with the data collected from the user
                            statement.setString(1, query15_date);
                            ResultSet resultSet = statement.executeQuery();
                            System.out.println("Team Name ");
                            
                            while (resultSet.next())
                            {
                                System.out.println(String.format("%s ", resultSet.getString(1)));
                            }
                        }
                    }
                    break;
                case "16":
                    System.out.println("Connecting to the database...");
                    // Get the database connection, create statement and execute it right away, as
                    // no user input need be collected
                    try (final Connection connection = DriverManager.getConnection(URL))
                    {
                        System.out.println("Dispatching the query...");
                        try (final PreparedStatement statement = connection.prepareStatement(QUERY16))
                        {
                            final int rows_inserted = statement.executeUpdate();
                            System.out.println(String.format("Done. %d rows updated.", rows_inserted));
                        }
                    }
                    break;
                case "17":
                    System.out.println("Connecting to the database...");
                    // Get the database connection, create statement and execute it right away, as
                    // no user input need be collected
                    try (final Connection connection = DriverManager.getConnection(URL))
                    {
                        System.out.println("Dispatching the query...");
                        try (final PreparedStatement statement = connection.prepareStatement(QUERY17))
                        {
                            final int rows_inserted = statement.executeUpdate();
                            System.out.println(String.format("Done. %d rows affected.", rows_inserted));
                        }
                    }
                    break;
                case "18":
                    System.out.println("Please enter file name to read teams from:");
                    
                    // Get filename
                    final String filename = sc.nextLine();
                    
                    System.out.println("Connecting to the database...");
                    // Get the database connection, create statement and execute it right away, as
                    // no user input need be collected
                    try (final Connection connection = DriverManager.getConnection(URL))
                    {
                        // attempt to find the provided filename
                        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
                            String line;
                            String query = "INSERT INTO Team VALUES (?, ?, ?, ?, ?);";
                            System.out.println("Dispatching the query...");
                            int i = 0;
                            // Create CSV like file containing every row in Team
                            while ((line = br.readLine()) != null) {
                                String[] teamparams = line.split(",");
                                try (final PreparedStatement statement = connection.prepareStatement(query))
                                {
                                    statement.setString(1, teamparams[0]);
                                    statement.setString(2, teamparams[1]);
                                    statement.setString(3, teamparams[2]);
                                    statement.setString(4, teamparams[3]);
                                    statement.setString(5, teamparams[4]);
                                    
                                    // Actually execute the populated query
                                    final int rows_inserted = statement.executeUpdate();
                                    i += rows_inserted;
                                }
                            }
                            br.close();
                            System.out.println(String.format("Done. %d rows inserted.", i));
                        }
                        catch (IOException e)
                        {
                            System.out.println("Could not read from file " + filename);
                        }
                    }
                    break;
                case "19":
                    System.out.println("Please enter file name to write to:");
                    
                    // get filename to write to
                    final String write_filename = sc.nextLine();
                    
                    System.out.println("Connecting to the database...");
                    // Get the database connection, create statement and execute it right away, as
                    // no user input need be collected
                    try (final Connection connection = DriverManager.getConnection(URL))
                    {
                        System.out.println("Dispatching the query...");
                        try (final PreparedStatement statement = connection.prepareStatement(QUERY19))
                        {
                            // Get query results to write to file
                            ResultSet resultSet = statement.executeQuery();
                            try {
                                BufferedWriter bw = new BufferedWriter(new FileWriter(write_filename));
                                // Unpack the tuples returned by the database and print them out to the user
                                while (resultSet.next())
                                {
                                    // Write query output to file rather than STDOUT
                                    bw.write(String.format("%s,%s\n", resultSet.getString(1),
                                            resultSet.getString(2)));
                                }
                                // Done with writer, close
                                bw.close();
                                System.out.println("Successfully wrote to file " + write_filename);
                            }
                            catch(IOException e) {
                                System.out.println("Unable to write to file");
                            }
                        }
                    }
                    break;
                case "20": // Do nothing, the while loop will terminate upon the next iteration
                    System.out.println("Quiting! Goodbye!");
                    break;
                default: // Unrecognized option, re-prompt the user for the correct one
                    System.out.println(String.format("Unrecognized option: %s\n" + "Please try again!", option));
                    break;
            }
        }
        sc.close(); // Close the scanner before exiting the application
    }
}
