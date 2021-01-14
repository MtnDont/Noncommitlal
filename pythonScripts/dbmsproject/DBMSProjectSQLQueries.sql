DROP TABLE IF EXISTS Business;
DROP TABLE IF EXISTS Church;
DROP TABLE IF EXISTS OrgCredit;
DROP TABLE IF EXISTS OrgCheck;
DROP TABLE IF EXISTS OrgDonation;
DROP TABLE IF EXISTS DonorCredit;
DROP TABLE IF EXISTS DonorCheck;
DROP TABLE IF EXISTS DonorDonation;
DROP TABLE IF EXISTS Donor;
DROP TABLE IF EXISTS Expense;
DROP TABLE IF EXISTS Sponsors;
DROP TABLE IF EXISTS CaresFor;
DROP TABLE IF EXISTS Volunteers;
DROP TABLE IF EXISTS Team;
DROP TABLE IF EXISTS Employee;
DROP TABLE IF EXISTS Volunteer;
DROP TABLE IF EXISTS Insurance;
DROP TABLE IF EXISTS Needs;
DROP TABLE IF EXISTS Client;
DROP TABLE IF EXISTS EmergencyContact;
DROP TABLE IF EXISTS ContactInfo;
DROP TABLE IF EXISTS Person;
DROP TABLE IF EXISTS Organization;

CREATE TABLE Organization (
    orgname VARCHAR(64) PRIMARY KEY NOT NULL,
    email VARCHAR(256) NOT NULL,
    phone VARCHAR(22) NOT NULL,
    contact VARCHAR(64) NOT NULL
);

CREATE UNIQUE INDEX orgname
ON Organization (orgname, email, phone, contact);

CREATE TABLE Person (
    ssn VARCHAR(11) PRIMARY KEY NOT NULL,
    orgname VARCHAR(64),
    pname VARCHAR(64) NOT NULL,
    dob DATE NOT NULL,
    race VARCHAR(32) NOT NULL,
    gender VARCHAR(16) NOT NULL,
    profession VARCHAR(128) NOT NULL,
    onmailinglist BIT NOT NULL,
    CONSTRAINT FK_orgName_Person FOREIGN KEY (orgname) REFERENCES Organization
);

CREATE TABLE ContactInfo (
    ssn VARCHAR(11) PRIMARY KEY NOT NULL,
    addr VARCHAR(256) NOT NULL,
    email VARCHAR(256) NOT NULL,
    homePhone VARCHAR(22) NOT NULL,
    workPhone VARCHAR(22) NOT NULL,
    cellPhone VARCHAR(22) NOT NULL,
    CONSTRAINT FK_ssn_ContactInfo FOREIGN KEY (ssn) REFERENCES Person
);

CREATE TABLE EmergencyContact (
    ssn VARCHAR(11) NOT NULL,
    ecname VARCHAR(64) NOT NULL,
    relation VARCHAR(64) NOT NULL,
    addr VARCHAR(256) NOT NULL,
    email VARCHAR(256) NOT NULL,
    homePhone VARCHAR(22) NOT NULL,
    workPhone VARCHAR(22) NOT NULL,
    cellPhone VARCHAR(22) NOT NULL,
    CONSTRAINT PK_EmergencyContact PRIMARY KEY (ssn, ecname),
    CONSTRAINT FK_ssn_EmergencyContact FOREIGN KEY (ssn) REFERENCES Person
);

CREATE TABLE Client (
    ssn VARCHAR(11) PRIMARY KEY NOT NULL,
    attorneyname VARCHAR(64) NOT NULL,
    attorneynum VARCHAR(22) NOT NULL,
    docname VARCHAR(64) NOT NULL,
    docnum VARCHAR(22) NOT NULL,
    assignmentDate DATE NOT NULL,
    CONSTRAINT FK_ssn_Client FOREIGN KEY (ssn) REFERENCES Person
);

CREATE INDEX ssn
ON Client (ssn, attorneyname, attorneynum, docname, docnum, assignmentDate);

CREATE TABLE Needs (
    ssn VARCHAR(11) NOT NULL,
    importance INT NOT NULL,
    nname VARCHAR(16) NOT NULL,
    CONSTRAINT PK_Needs PRIMARY KEY (ssn, nname),
    CONSTRAINT FK_ssn_Needs FOREIGN KEY (ssn) REFERENCES Client,
    CONSTRAINT CHK_importance CHECK (importance BETWEEN 1 AND 10)
);

CREATE TABLE Insurance (
    ssn VARCHAR(11) NOT NULL,
    policyid INT IDENTITY(1,1) NOT NULL,
    providerid INT NOT NULL,
    addr VARCHAR(256) NOT NULL,
    insurancetype VARCHAR(8) NOT NULL,
    CONSTRAINT PK_Insurance PRIMARY KEY (ssn, policyid, providerid),
    CONSTRAINT FK_ssn_Insurance FOREIGN KEY (ssn) REFERENCES Client,
    CONSTRAINT CHK_insurancetype CHECK (insurancetype IN ('life','health','home','auto'))
);

CREATE TABLE Volunteer (
    ssn VARCHAR(11) PRIMARY KEY NOT NULL,
    joinDate DATE NOT NULL,
    trainDate DATE NOT NULL,
    trainLoc VARCHAR(256) NOT NULL,
    CONSTRAINT FK_ssn_Volunteer FOREIGN KEY (ssn) REFERENCES Person
);

CREATE INDEX ssn
ON Volunteer (ssn, joinDate, trainDate, trainLoc);

CREATE TABLE Employee (
    ssn VARCHAR(11) PRIMARY KEY NOT NULL,
    salary INT NOT NULL,
    maritalstatus VARCHAR(16) NOT NULL,
    hiredate DATE NOT NULL,
    CONSTRAINT FK_ssn_Employee FOREIGN KEY (ssn) REFERENCES Person
);

CREATE INDEX ssn
ON Employee (ssn, salary, maritalstatus, hiredate);

CREATE TABLE Team (
    teamname VARCHAR(64) PRIMARY KEY NOT NULL,
    leaderssn VARCHAR(11) NOT NULL,
    employeessn VARCHAR(11) NOT NULL,
    teamtype VARCHAR(64) NOT NULL,
    formationdate DATE NOT NULL,
    CONSTRAINT FK_leaderSSN FOREIGN KEY (leaderssn) REFERENCES Volunteer(ssn),
    CONSTRAINT FK_employeeSSN FOREIGN KEY (employeessn) REFERENCES Employee(ssn)
);

CREATE INDEX teamname
ON Team (teamname, leaderssn, employeessn, teamtype, formationdate);

CREATE TABLE Volunteers (
    ssn VARCHAR(11) NOT NULL,
    teamname VARCHAR(64) NOT NULL,
    isActive BIT NOT NULL,
    monthlyHours INT NOT NULL,
    CONSTRAINT PK_Volunteers PRIMARY KEY (ssn, teamname),
    CONSTRAINT FK_ssn_Volunteers FOREIGN KEY (ssn) REFERENCES Volunteer,
    CONSTRAINT FK_teamname_Volunteers FOREIGN KEY (teamname) REFERENCES Team
);

CREATE TABLE CaresFor (
    ssn VARCHAR(11) NOT NULL,
    teamname VARCHAR(64) NOT NULL,
    isactive BIT NOT NULL,
    CONSTRAINT PK_CaresFor PRIMARY KEY (ssn, teamname),
    CONSTRAINT FK_teamname_CaresFor FOREIGN KEY (teamname) REFERENCES Team,
    CONSTRAINT FK_ssn_CaresFor FOREIGN KEY (ssn) REFERENCES Client
);

CREATE TABLE Sponsors (
    teamname VARCHAR(64) NOT NULL,
    orgname VARCHAR(64) NOT NULL,
    CONSTRAINT PK_Sponsors PRIMARY KEY (teamname, orgname),
    CONSTRAINT FK_teamname_Sponsors FOREIGN KEY (teamname) REFERENCES Team,
    CONSTRAINT FK_orgname_Sponsors FOREIGN KEY (orgname) REFERENCES Organization
);

CREATE TABLE Expense (
    ssn VARCHAR(11) NOT NULL,
    expensedate DATE NOT NULL,
    amount REAL NOT NULL,
    expensedescription VARCHAR(256) NOT NULL,
    CONSTRAINT PK_Expense PRIMARY KEY (ssn, expensedate),
    CONSTRAINT FK_ssn_Expense FOREIGN KEY (ssn) REFERENCES Employee
);

CREATE INDEX expensedate
ON Expense (ssn, expensedate, amount, expensedescription);

CREATE TABLE Donor (
    ssn VARCHAR(11) PRIMARY KEY NOT NULL,
    CONSTRAINT FK_ssn_Donor FOREIGN KEY (ssn) REFERENCES Person
);

CREATE INDEX ssn
ON Donor (ssn);

CREATE TABLE DonorDonation (
    ssn VARCHAR(11) NOT NULL,
    amount REAL NOT NULL,
    donationdate DATE NOT NULL,
    campaign VARCHAR(64) NOT NULL,
    isanonymous BIT NOT NULL,
    CONSTRAINT PK_DonorDonation PRIMARY KEY (ssn, amount, donationdate),
    CONSTRAINT FK_ssn_DonorDonation FOREIGN KEY (ssn) REFERENCES Donor
);

CREATE TABLE DonorCheck (
    ssn VARCHAR(11) NOT NULL,
    amount REAL NOT NULL,
    donationdate DATE NOT NULL,
    checknum INT NOT NULL,
    CONSTRAINT PK_DonorCheck PRIMARY KEY (ssn, amount, donationdate),
    CONSTRAINT FK_DonorCheck FOREIGN KEY (ssn, amount, donationdate) REFERENCES DonorDonation(ssn, amount, donationdate)
);

CREATE TABLE DonorCredit (
    ssn VARCHAR(11) NOT NULL,
    amount REAL NOT NULL,
    donationdate DATE NOT NULL,
    cardnum VARCHAR(16) NOT NULL,
    cardtype VARCHAR(32) NOT NULL,
    expirdate VARCHAR(5) NOT NULL,
    CONSTRAINT PK_DonorCredit PRIMARY KEY (ssn, amount, donationdate),
    CONSTRAINT FK_DonorCredit FOREIGN KEY (ssn, amount, donationdate) REFERENCES DonorDonation(ssn, amount, donationdate)
);

CREATE TABLE OrgDonation (
    orgname VARCHAR(64) NOT NULL,
    amount REAL NOT NULL,
    donationdate DATE NOT NULL,
    campaign VARCHAR(64) NOT NULL,
    isAnonymous BIT NOT NULL,
    CONSTRAINT PK_OrgDonation PRIMARY KEY (orgname, amount, donationdate),
    CONSTRAINT FK_orgname_OrgDonation FOREIGN KEY (orgname) REFERENCES Organization
);

CREATE TABLE OrgCheck (
    orgname VARCHAR(64) NOT NULL,
    amount REAL NOT NULL,
    donationdate DATE NOT NULL,
    checknum INT NOT NULL,
    CONSTRAINT PK_OrgCheck PRIMARY KEY (orgname, amount, donationdate),
    CONSTRAINT FKOrgCheck FOREIGN KEY (orgname, amount, donationdate) REFERENCES OrgDonation(orgname, amount, donationdate)
);

CREATE TABLE OrgCredit (
    orgname VARCHAR(64) NOT NULL,
    amount REAL NOT NULL,
    donationdate DATE NOT NULL,
    cardnum VARCHAR(16) NOT NULL,
    cardtype VARCHAR(32) NOT NULL,
    expirdate VARCHAR(5) NOT NULL,
    CONSTRAINT PK_OrgCredit PRIMARY KEY (orgname, amount, donationdate),
    CONSTRAINT FK_OrgCredit FOREIGN KEY (orgname, amount, donationdate) REFERENCES OrgDonation(orgname, amount, donationdate)
);

CREATE TABLE Church (
    orgname VARCHAR(64) PRIMARY KEY NOT NULL,
    religiousaffiliation VARCHAR(128),
    CONSTRAINT FK_orgname_Church FOREIGN KEY (orgname) REFERENCES Organization
);

CREATE TABLE Business (
    orgname VARCHAR(64) PRIMARY KEY NOT NULL,
    businesstype VARCHAR(32) NOT NULL,
    businesssize INT NOT NULL,
    website VARCHAR(128) NOT NULL,
    CONSTRAINT FK_orgname_Business FOREIGN KEY (orgname) REFERENCES Organization
);

/*
TESTING DATA
*/
INSERT INTO Organization
(orgname, email, phone, contact)
VALUES
('Crg1', 'hahafuni@org1.org', '000-000-0000', 'Jerry')

INSERT INTO Person
(ssn, orgName, pname, dob, race, gender, profession, onmailinglist)
VALUES
('444-44-4441', 'Crg1', 'Lore Mipsum', '1976-01-01', 'ist', 'M', 'Data Analyst', 0),
('444-44-4442', 'Crg1', 'John Doe', '1976-01-01', 'ist', 'M', 'Car dealer', 0),
('444-44-4443', 'Crg1', 'Jane Doe', '1976-01-01', 'ist', 'F', 'Laboratory technician', 0),
('444-44-4444', 'Crg1', 'F Oobar', '1976-01-01', 'ist', 'M', 'Disc jockey', 1),
('444-44-4445', 'Crg1', 'Fo OBar', '1976-01-01', 'ist', 'M', 'Soothesayer', 1),
('444-44-4446', 'Crg1', 'Foob Ar', '1976-01-01', 'ist', 'F', 'Immigration officer', 1),
('444-44-4447', 'Crg1', 'Mike Hawk', '1976-01-01', 'ist', 'M', 'Lift engineer', 1);

INSERT INTO Donor
(ssn)
VALUES
('444-44-4445'),
('444-44-4447');

INSERT INTO DonorDonation
(ssn, amount, donationdate, campaign, isanonymous)
VALUES
('444-44-4445', 1200, '2000-01-01', 'charity', 0),
('444-44-4447', 1200, '2000-01-01', 'charity', 0),
('444-44-4445', 1000, '2000-01-02', 'charity', 0);

INSERT INTO ContactInfo
(ssn, addr, email, homePhone, workPhone, cellPhone)
VALUES
('444-44-4441', '123 Main St Norman, OK 73071', 'garret@email.org', '123-456-7890', '000-000-0000', '000-000-0000'),
('444-44-4442', '123 Main St Norman, OK 73071', 'barlows@email.org', '123-456-7890', '000-000-0000', '000-000-0000'),
('444-44-4443', '123 Main St Norman, OK 73071', 'memebigboy@email.org', '123-456-7890', '000-000-0000', '000-000-0000'),
('444-44-4444', '123 Main St Norman, OK 73071', 'memebigboy@email.org', '123-456-7890', '000-000-0000', '000-000-0000'),
('444-44-4445', '123 Main St Norman, OK 73071', 'memebigboy@email.org', '123-456-7890', '000-000-0000', '000-000-0000'),
('444-44-4446', '123 Main St Norman, OK 73071', 'memebigboy@email.org', '123-456-7890', '000-000-0000', '000-000-0000'),
('444-44-4447', '123 Main St Norman, OK 73071', 'memebigboy@email.org', '123-456-7890', '000-000-0000', '000-000-0000');

INSERT INTO Client
(ssn, attorneyname, attorneynum, docname, docnum, assignmentDate)
VALUES
('444-44-4441', 'Elon', '123-456-7890', 'Musk', '123-456-7890', '1212-12-12'),
('444-44-4442', 'Ace', '123-456-7890', 'House', '123-456-7890', '1212-12-12'),
('444-44-4443', 'Aaron', '123-456-7890', 'Abbott', '123-456-7890', '1212-12-12'),
('444-44-4444', 'Brooke', '123-456-7890', 'Jenice', '123-456-7890', '1212-12-12');

INSERT INTO Volunteer
(ssn, joinDate, trainDate, trainLoc)
VALUES
('444-44-4446', '2000-01-01', '2000-01-05', 'Mesa');

INSERT INTO Employee
(ssn, salary, maritalstatus, hiredate)
VALUES
('444-44-4445', 12000, 'divorced', '1976-01-01'),
('444-44-4447', 69, 'married', '2000-01-01');

INSERT INTO Expense
(ssn, expensedate, amount, expensedescription)
VALUES
('444-44-4445', '2020-11-19', 12000, 'lorem ipsum'),
('444-44-4445', '2020-11-20', 12000, 'lorem ipsum'),
('444-44-4447', '2019-11-19', 12000, 'lorem ipsum'),
('444-44-4447', '2019-11-20', 10000, 'lorem ipsum');

INSERT INTO Team
(teamname, leaderssn, employeessn, teamtype, formationdate)
VALUES
('team1', '444-44-4446', '444-44-4445', 'sports', '1976-12-12'),
('team2', '444-44-4446', '444-44-4445', 'fundraising', '1976-12-12'),
('team3', '444-44-4446', '444-44-4445', 'soccer', '1976-12-12');

INSERT INTO Volunteers
(ssn, teamname, isActive, monthlyHours)
VALUES
('444-44-4446', 'team1', 1, 120);

INSERT INTO Sponsors
(teamname, orgname)
VALUES
('team1', 'crg1'),
('team2', 'crg1'),
('team3', 'crg1');

SET IDENTITY_INSERT Insurance ON

INSERT INTO Insurance
(ssn, policyid, providerid, addr, insurancetype)
VALUES
('444-44-4444', 1, 1, '123 Main St', 'life'),
('444-44-4442', 1, 2, '123 Main St', 'auto'),
('444-44-4444', 2, 1, '123 Main St', 'auto'),
('444-44-4443', 1, 3, '123 Main St', 'health'),
('444-44-4442', 2, 3, '123 Main St', 'health'),
('444-44-4443', 2, 3, '123 Main St', 'auto');

SET IDENTITY_INSERT Insurance OFF

INSERT INTO Needs
(ssn, importance, nname)
VALUES
('444-44-4441', 1, 'functionality'),
('444-44-4442', 1, 'functionality'),
('444-44-4442', 4, 'transportation'),
('444-44-4443', 5, 'transportation'),
('444-44-4444', 1, 'functionality'),
('444-44-4443', 1, 'functionality');

INSERT INTO CaresFor
(ssn, teamname, isactive)
VALUES
('444-44-4442', 'team1', 1),
('444-44-4441', 'team1', 0),
('444-44-4442', 'team2', 1),
('444-44-4443', 'team1', 1),
('444-44-4442', 'team3', 1);

SELECT * FROM Volunteer
SELECT * FROM Team

/*
Query 1 
Enter a new team into the database (1/month)
*/
INSERT INTO Team
VALUES
(?, ?, ?, ?, ?);

/*
Query 2
Enter a new client into the database and associate him or her with one or more teams
*/
INSERT INTO Person
VALUES
(?, ?, ?, ?, ?, ?, ?, ?);

INSERT INTO ContactInfo
VALUES
(?, ?, ?, ?, ?, ?);

INSERT INTO EmergencyContact
VALUES
(?, ?, ?, ?, ?, ?, ?, ?);

INSERT INTO Client
VALUES
(?, ?, ?, ?, ?, ?);

INSERT INTO Insurance
(ssn, providerid, addr, insurancetype)
VALUES
(?, ?, ?, ?);

INSERT INTO Needs
VALUES
(?, ?, ?);

INSERT INTO CaresFor
VALUES
(?, ?, ?);

/*
Query 3
Enter a new volunteer into the database and associate him or her with one or more teams.
*/
INSERT INTO Person
VALUES
(?, ?, ?, ?, ?, ?, ?, ?);

INSERT INTO ContactInfo
VALUES
(?, ?, ?, ?, ?, ?);

INSERT INTO EmergencyContact
VALUES
(?, ?, ?, ?, ?, ?, ?, ?);

INSERT INTO Volunteer
VALUES
(?, ?, ?, ?);

INSERT INTO Volunteers
VALUES
(?, ?, ?, ?);

/*
Query 4
Enter the number of hours a volunteer worked this month for a particular team.
*/
GO
CREATE PROCEDURE insertNumHoursVolunteerTeam
    @ssn VARCHAR(11),
    @teamname VARCHAR(64),
    @hours INT
AS
BEGIN
    UPDATE Volunteers
    SET monthlyHours = @hours
    WHERE Volunteers.ssn = @ssn and Volunteers.teamname = @teamname
END

/*
Query 5
Enter a new employee into the database and associate him or her with one or more teams.
*/
INSERT INTO Person
VALUES
(?, ?, ?, ?, ?, ?, ?, ?);

INSERT INTO ContactInfo
VALUES
(?, ?, ?, ?, ?, ?)

INSERT INTO EmergencyContact
VALUES
(?, ?, ?, ?, ?, ?, ?, ?)

INSERT INTO Employee
VALUES
(?, ?, ?, ?);

GO
CREATE PROCEDURE associateEmployeeWithTeam
    @employeessn VARCHAR(11),
    @teamname VARCHAR(64)
AS
BEGIN
    UPDATE Team
    SET employeessn = @employeessn
    WHERE Team.teamname = @teamname
END

/*
Query 6
Enter an expense charged by an exmployee
*/
GO
CREATE PROCEDURE expenseFromEmployee
    @ssn VARCHAR(11),
    @expensedate DATE,
    @amount REAL,
    @expensedescription VARCHAR(25)
AS
BEGIN
    INSERT INTO Expense
    (ssn, expensedate, amount, expensedescription)
    VALUES
    (@ssn, @expensedate, @amount, @expensedescription);
END


/*
Query 7
Enter a new organization and associate it to one or more PAN teams
*/
INSERT INTO Organization
VALUES
(?, ?, ?, ?);

INSERT INTO Business
VALUES
(?, ?, ?, ?);

INSERT INTO Church
VALUES
(?, ?);

INSERT INTO Sponsors
VALUES
(?, ?);

/*
Query 8
Enter a new donor and associate him or her with several donations
*/
INSERT INTO Person
(ssn, orgName, pname, dob, race, gender, profession)
VALUES
(?, ?, ?, ?, ?, ?, ?);

INSERT INTO ContactInfo
(ssn, addr, email, homePhone, workPhone, cellPhone)
VALUES
(?, ?, ?, ?, ?, ?);

INSERT INTO EmergencyContact
(ssn, ecname, relation, addr, email, homePhone, workPhone, cellPhone)
VALUES
(?, ?, ?, ?, ?, ?, ?, ?);

INSERT INTO Donor
(ssn)
VALUES
(?);

INSERT INTO DonorDonation
(ssn, amount, donationdate, campaign, isanonymous)
VALUES
(?, ?, ?, ?, ?);

INSERT INTO DonorCheck
(ssn, amount, donationdate, checknum)
VALUES
(?, ?, ?, ?);

INSERT INTO DonorCredit
(ssn, amount, donationdate, cardnum, cardtype, expirdate)
VALUES
(?, ?, ?, ?, ?, ?);

/*
Query 9
Enter a new organization and associate it with several donations (1/day).
*/

INSERT INTO Organization
VALUES
(?, ?, ?, ?);

INSERT INTO Business
VALUES
(?, ?, ?, ?);

INSERT INTO Church
VALUES
(?, ?);

INSERT INTO OrgDonation
(orgname, amount, donationdate, campaign, isanonymous)
VALUES
(?, ?, ?, ?, ?);

INSERT INTO OrgCheck
(orgname, amount, donationdate, checknum)
VALUES
(?, ?, ?, ?);

INSERT INTO OrgCredit
(orgname, amount, donationdate, cardnum, cardtype, expirdate)
VALUES
(?, ?, ?, ?, ?, ?);

/*
Query 10
Retrieve the name and phone number of the doctor of a particular client
*/
GO
CREATE PROCEDURE getClientDoctorNameNumber
    @clientssn VARCHAR(11)
AS
BEGIN

    SELECT Client.docname, Client.docnum
    FROM Client
    WHERE Client.ssn = @clientssn

END

/*
Query 11
Retrieve the total amount of expenses charged by each employee for a particular period of time. The list should be sorted by the total amount of expenses
*/
GO
CREATE PROCEDURE getTotalEmployeeExpensesOverTime
    @before DATE,
    @after DATE
AS
BEGIN

    SELECT Employee.ssn, SUM(Expense.amount) AS Total_Expenses
    FROM Employee, Expense
    WHERE Employee.ssn = Expense.ssn and Expense.expensedate BETWEEN @after and @before
    GROUP BY Employee.ssn
    ORDER BY SUM(Expense.amount)

END

/*
Query 12
Retrieve the list of volunteers that are members of teams that support a particular client (4/year).
*/
GO
CREATE PROCEDURE getVolunteersWhoseTeamSupportsClient
    @clientssn VARCHAR(11)
AS
BEGIN

    SELECT Volunteer.*
    FROM Volunteer, Volunteers, Team, CaresFor, Client
    WHERE Client.ssn = @clientssn and Client.ssn = CaresFor.ssn and CaresFor.teamname = Team.teamname and Team.teamname = Volunteers.teamname and Volunteers.ssn = Volunteer.ssn

END

/*
Query 13
Retrieve the names and contact information of the clients that are supported by teams sponsored by an organization whose name starts with a letter between B and K. The client list should be sorted by name (1/week).
*/
GO
CREATE PROCEDURE getClientNamesContactsWithOrgBetweenBK
AS
BEGIN

    SELECT Person.pname, ContactInfo.addr, ContactInfo.email, ContactInfo.homePhone, ContactInfo.workPhone, ContactInfo.cellPhone
    FROM Person, ContactInfo, ( 
        SELECT DISTINCT Person.ssn
        FROM Person, Client, CaresFor, Team, Sponsors, Organization
        WHERE Person.ssn = Client.ssn and Client.ssn = CaresFor.ssn and CaresFor.isactive = 1 and CaresFor.teamname = Team.teamname and Team.teamname = Sponsors.teamname and Sponsors.orgname = Organization.orgname and SUBSTRING(Organization.orgname, 1, 1) BETWEEN 'B' and 'K'
    ) AS Socials
    WHERE Socials.ssn = Person.ssn and Person.ssn = ContactInfo.ssn
    ORDER BY Person.pname

END

/*
Query 14
Retrieve the name and total amount donated by donors that are also employees. The list should be sorted by the total amount of the donations,and indicate if each donor wishes to remain anonymous.
*/
GO
CREATE PROCEDURE getNameOfEmployeeDonors
AS
BEGIN

    SELECT Person.pname, SUM(DonorDonation.amount) as Total_Donations, DonorDonation.isanonymous
    FROM DonorDonation, Donor, Employee, Person
    WHERE DonorDonation.ssn = Donor.ssn and Donor.ssn = Employee.ssn and Employee.ssn = Person.ssn 
    GROUP BY Person.ssn, Person.pname, DonorDonation.isanonymous

END

/*
Query 15
Retrieve the names of all teams that were founded after a particular date.
*/
GO
CREATE PROCEDURE getTeamNamesAfter
    @dateAfter DATE
AS
BEGIN

    SELECT Team.teamname
    FROM Team
    WHERE team.formationdate > @dateAfter

END

/*
Query 16 
Increase the salary by 10% of all employees to whom more than one team must report.
*/
DROP PROCEDURE IF EXISTS increaseEmployeeSalaryMultipleTeams;
GO
CREATE PROCEDURE increaseEmployeeSalaryMultipleTeams
AS
BEGIN

    UPDATE Employee
    SET salary = salary * 1.1
    WHERE Employee.ssn = (
        SELECT Employee.ssn
        FROM Employee, Team
        WHERE Team.employeessn = Employee.ssn
        GROUP BY Employee.ssn
        HAVING COUNT(*) > 1
    );

END

/*
Query 17
Delete all clients who do not have health insurance and whose value of importance for transportation is less than 5.
*/

DROP PROCEDURE IF EXISTS deleteClientHealthTransport;
GO
CREATE PROCEDURE deleteClientHealthTransport
AS
BEGIN

    DELETE FROM Insurance
    WHERE Insurance.ssn NOT IN (
        SELECT Client.ssn
        FROM Client, Insurance, Needs
        WHERE Client.ssn = Insurance.ssn and Client.ssn = Needs.ssn and Insurance.insurancetype = 'health' and Needs.nname = 'transportation' and Needs.importance >= 5
    );

    DELETE FROM CaresFor
    WHERE CaresFor.ssn NOT IN (
        SELECT DISTINCT Insurance.ssn
        FROM CaresFor, Insurance
        WHERE CaresFor.ssn = Insurance.ssn 
    );

    DELETE FROM Needs
    WHERE Needs.ssn NOT IN (
        SELECT DISTINCT Insurance.ssn
        FROM Needs, Insurance
        WHERE Needs.ssn = Insurance.ssn 
    );

    DELETE FROM Client
    WHERE Client.ssn NOT IN (
        SELECT DISTINCT Insurance.ssn
        FROM Client, Insurance
        WHERE Client.ssn = Insurance.ssn 
    );

END

/*
Query 18
*/
SELECT * FROM Team
SELECT * FROM Volunteer

/*
Query 19
Retrieve names and mailing addresses of all people on the mailing  
*/
GO
CREATE PROCEDURE getNamesMailingList
AS
BEGIN
    SELECT Person.pname, ContactInfo.email
    FROM Person, ContactInfo
    WHERE Person.onmailinglist = 1 and Person.ssn = ContactInfo.ssn
END

/*
q1 order
*/
SELECT * FROM Client
SELECT * FROM Person
SELECT * FROM ContactInfo
SELECT * FROM EmergencyContact
SELECT * FROM Team

SELECT * FROM Client
SELECT * FROM Person
SELECT * FROM ContactInfo
SELECT * FROM EmergencyContact
SELECT * FROM Insurance
SELECT * FROM Needs
SELECT * FROM CaresFor