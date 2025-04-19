-- Cấu trúc 
Fact Tables
    
FactEmployee
    EmployeeID (Primary Key)
    DepartmentID
    PositionID
    HireDate
    Status
    Gender
    DateOfBirth

FactSalary
    SalaryID (Primary Key)
    EmployeeID
    Month
    Year
    BasicSalary
    Allowance
    Deductions
    NetSalary
    
FactAttendance
    AttendanceID (Primary Key)
    EmployeeID
    Date
    CheckInTime
    CheckOutTime
    Status
    
FactContracts
    ContractID (Primary Key)
    EmployeeID
    ContractType
    StartDate
    EndDate
    Status
    
FactDecisions
    DecisionID (Primary Key)
    EmployeeID
    DecisionType
    DecisionDate
    Details
    
FactLeaveBalances
    LeaveBalanceID (Primary Key)
    EmployeeID
    Year
    TotalLeaveDays
    UsedLeaveDays
    RemainingLeaveDays
    
Dimension Tables
    
DimEmployee
    EmployeeID (Primary Key)
    FullName
    Address
    Phone
    Email
    
DimDepartment
    DepartmentID (Primary Key)
    DepartmentName
    
DimPosition
    PositionID (Primary Key)
    PositionName
    
DimDate
    DateKey (Primary Key)
    Date
    Day
    Month
    Year
    Quarter

-- SQL
-- Dimension Tables
CREATE TABLE DimEmployee (
    EmployeeID INT PRIMARY KEY,
    FullName NVARCHAR(100),
    Address NVARCHAR(255),
    Phone NVARCHAR(15),
    Email NVARCHAR(100)
);

CREATE TABLE DimDepartment (
    DepartmentID INT PRIMARY KEY,
    DepartmentName NVARCHAR(100)
);

CREATE TABLE DimPosition (
    PositionID INT PRIMARY KEY,
    PositionName NVARCHAR(100)
);

CREATE TABLE DimDate (
    DateKey INT PRIMARY KEY,
    Date DATE,
    Day INT,
    Month INT,
    Year INT,
    Quarter INT
);

-- Fact Tables
CREATE TABLE FactEmployee (
    EmployeeID INT PRIMARY KEY,
    DepartmentID INT,
    PositionID INT,
    HireDate DATE,
    Status NVARCHAR(50),
    Gender NVARCHAR(10),
    DateOfBirth DATE,
    FOREIGN KEY (EmployeeID) REFERENCES DimEmployee(EmployeeID),
    FOREIGN KEY (DepartmentID) REFERENCES DimDepartment(DepartmentID),
    FOREIGN KEY (PositionID) REFERENCES DimPosition(PositionID)
);

CREATE TABLE FactSalary (
    SalaryID INT PRIMARY KEY,
    EmployeeID INT,
    Month INT,
    Year INT,
    BasicSalary DECIMAL(18, 2),
    Allowance DECIMAL(18, 2),
    Deductions DECIMAL(18, 2),
    NetSalary DECIMAL(18, 2),
    FOREIGN KEY (EmployeeID) REFERENCES DimEmployee(EmployeeID)
);

CREATE TABLE FactAttendance (
    AttendanceID INT PRIMARY KEY,
    EmployeeID INT,
    Date DATE,
    CheckInTime TIME,
    CheckOutTime TIME,
    Status NVARCHAR(50),
    FOREIGN KEY (EmployeeID) REFERENCES DimEmployee(EmployeeID),
    FOREIGN KEY (Date) REFERENCES DimDate(Date)
);

CREATE TABLE FactContracts (
    ContractID INT PRIMARY KEY,
    EmployeeID INT,
    ContractType NVARCHAR(50),
    StartDate DATE,
    EndDate DATE,
    Status NVARCHAR(50),
    FOREIGN KEY (EmployeeID) REFERENCES DimEmployee(EmployeeID)
);

CREATE TABLE FactDecisions (
    DecisionID INT PRIMARY KEY,
    EmployeeID INT,
    DecisionType NVARCHAR(50),
    DecisionDate DATE,
    Details NVARCHAR(MAX),
    FOREIGN KEY (EmployeeID) REFERENCES DimEmployee(EmployeeID)
);

CREATE TABLE FactLeaveBalances (
    LeaveBalanceID INT PRIMARY KEY,
    EmployeeID INT,
    Year INT,
    TotalLeaveDays INT,
    UsedLeaveDays INT,
    RemainingLeaveDays INT,
    FOREIGN KEY (EmployeeID) REFERENCES DimEmployee(EmployeeID)
);
