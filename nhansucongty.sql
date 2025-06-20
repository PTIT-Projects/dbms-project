USE [nhansucongty]
--2. Quản lý Nhân sự
--Bảng Phòng ban (Departments)

CREATE TABLE Departments (
    DepartmentID INT PRIMARY KEY IDENTITY(1,1),
    DepartmentName NVARCHAR(100)
);
CREATE TABLE Positions (
    PositionID INT PRIMARY KEY IDENTITY(1,1),
    PositionName NVARCHAR(100),
    DepartmentID INT,
    CONSTRAINT FK_Positions_Department FOREIGN KEY (DepartmentID) REFERENCES Departments(DepartmentID)
);

CREATE TABLE Employees (
    userPassword NVARCHAR(255),
    roleName NVARCHAR(50),
    EmployeeID INT PRIMARY KEY IDENTITY(1,1),
    FullName NVARCHAR(100),
    DateOfBirth DATE,
    Gender NVARCHAR(10) CHECK (Gender IN (N'Nam', N'Nữ', N'Khác')),
    Address NVARCHAR(255),
    Phone NVARCHAR(15),
    Email NVARCHAR(100),
    DepartmentID INT,
    
    PositionID INT REFERENCES Positions(PositionID),
    
    HireDate DATE,
    Status NVARCHAR(50) CHECK (Status IN (N'Đang làm việc', N'Nghỉ việc', N'Nghỉ phép')),
    CONSTRAINT FK_Employees_Department FOREIGN KEY (DepartmentID) REFERENCES Departments(DepartmentID),
	CONSTRAINT UNIQUE_EMAIL UNIQUE(Email) 
);

CREATE TABLE DepartmentManager (
    DepartmentID INT PRIMARY KEY IDENTITY(1,1),
    ManagerID INT,
    CONSTRAINT FK_ManagerandDepartments_Departments FOREIGN KEY (DepartmentID) REFERENCES Departments(DepartmentID),
    CONSTRAINT FK_ManagerandDepartments_Employees FOREIGN KEY (ManagerID) REFERENCES Employees(EmployeeID)
);

--Bảng Hợp đồng (Contracts)
CREATE TABLE Salary (
    SalaryID INT PRIMARY KEY IDENTITY(1,1),
    EmployeeID INT,
    Month INT,
    Year INT,
    BasicSalary DECIMAL(18, 2),
    Allowance DECIMAL(18, 2),
    Deductions DECIMAL(18, 2),
    NetSalary DECIMAL(18, 2),
    CONSTRAINT FK_Salary_Employee FOREIGN KEY (EmployeeID) REFERENCES Employees(EmployeeID)
);

CREATE TABLE Contracts (
    ContractID INT PRIMARY KEY IDENTITY(1,1),
    EmployeeID INT,
    ContractType NVARCHAR(50) CHECK(ContractType IN (N'Toàn thời gian', N'Bán thời gian')),
    StartDate DATE,
    EndDate DATE,
    Status NVARCHAR(50) CHECK (Status IN (N'Hiệu lực', N'Hết hạn', N'Chấm dứt')),
    CONSTRAINT FK_Contracts_Employee FOREIGN KEY (EmployeeID) REFERENCES Employees(EmployeeID)
);
--Bảng Quyết định (Decisions)
CREATE TABLE Decisions (
    DecisionID INT PRIMARY KEY IDENTITY(1,1),
    EmployeeID INT,
    DecisionType NVARCHAR(50) CHECK (DecisionType IN (N'Bổ nhiệm', N'Khen thưởng', N'Kỷ luật')),
    DecisionDate DATE,
    Details NVARCHAR(MAX),
    CONSTRAINT FK_Decisions_Employee FOREIGN KEY (EmployeeID) REFERENCES Employees(EmployeeID)
);
--Bảng Số ngày tối đa còn lại được nghỉ (LeaveBalances)
CREATE TABLE LeaveBalances (
    LeaveBalanceID INT PRIMARY KEY IDENTITY(1,1),
    EmployeeID INT,
    Year INT,
    TotalLeaveDays INT,
    UsedLeaveDays INT,
    RemainingLeaveDays INT,
    CONSTRAINT FK_LeaveBalances_Employee FOREIGN KEY (EmployeeID) REFERENCES Employees(EmployeeID)
);


--1. Quản lý hành chính: Thông báo, đăng ký dùng cơ sở vật chất của công ty

--Bảng Thông báo (Notifications)
CREATE TABLE Notifications (
    NotificationID INT PRIMARY KEY IDENTITY(1,1),
    Title NVARCHAR(100),
    Content NVARCHAR(MAX),
    CreatedDate DATETIME,
    CreatedBy INT,
    CONSTRAINT FK_Notifications_CreatedBy FOREIGN KEY (CreatedBy) REFERENCES Employees(EmployeeID)
);

--Bảng Đăng ký dùng cơ sở vật chất (Registrations)
CREATE TABLE Registrations (
    RegistrationID INT PRIMARY KEY IDENTITY(1,1),
    EmployeeID INT,
    RegistrationType NVARCHAR(50) CHECK (RegistrationType IN (N'Xe', N'Ăn uống', N'Phòng họp', N'Nghỉ phép', N'Làm thêm giờ', N'Tạm ứng', N'Từ chức')),
    RequestDate DATE,
    Details NVARCHAR(MAX),
    Status NVARCHAR(50) CHECK (Status IN (N'Đang chờ', N'Đã duyệt', N'Từ chối')),
    ApprovedBy INT,
    CONSTRAINT FK_Registrations_Employee FOREIGN KEY (EmployeeID) REFERENCES Employees(EmployeeID),
    CONSTRAINT FK_Registrations_ApprovedBy FOREIGN KEY (ApprovedBy) REFERENCES Employees(EmployeeID)
);

--Bảng thông tin công tác của từng nhân viên (WorkTripRequests)
CREATE TABLE WorkTripRequests (
    RequestID INT PRIMARY KEY IDENTITY(1,1),
    EmployeeID INT,
    Destination NVARCHAR(255),
    StartDate DATE,
    EndDate DATE,
    Purpose NVARCHAR(MAX),
    Status NVARCHAR(50) CHECK (Status IN (N'Đang chờ', N'Đã duyệt', N'Từ chối')),
    CONSTRAINT FK_WorkTripRequests_Employee FOREIGN KEY (EmployeeID) REFERENCES Employees(EmployeeID)
);


--3. Quản lý chấm công + Tăng ca
--Bảng chấm công (Attendance)

CREATE TABLE Attendance (
    AttendanceID INT PRIMARY KEY IDENTITY(1,1),
    EmployeeID INT,
    Date DATE,
    CheckInTime TIME,
    CheckOutTime TIME,
    Status NVARCHAR(50) CHECK (Status IN (N'Có mặt', N'Vắng mặt', N'Đi muộn', N'Làm thêm giờ')),
    CONSTRAINT FK_Attendance_Employee FOREIGN KEY (EmployeeID) REFERENCES Employees(EmployeeID),
);
--Bảng Phiếu chấm công bằng tay (ManualAttendance)



--4. Bảo hiểm
--Bảng Bảo hiểm (Insurance)

CREATE TABLE Insurance (
    InsuranceID INT PRIMARY KEY IDENTITY(1,1),
    EmployeeID INT,
    InsuranceNumber NVARCHAR(50),
    InsuranceType NVARCHAR(50),
    StartDate DATE,
    EndDate DATE,
    CONSTRAINT FK_Insurance_Employee FOREIGN KEY (EmployeeID) REFERENCES Employees(EmployeeID)
);

--5. Quản lý Lương
--Bảng Lương (Salary)




--6. Quản lý tuyển dụng
--Bảng tuyển dụng (RecruitmentPlans)
CREATE TABLE RecruitmentPlans (
    PlanID INT PRIMARY KEY IDENTITY(1,1),
    PositionID INT REFERENCES Positions(PositionID),
    DepartmentID INT,
    Quantity INT,
    StartDate DATE,
    EndDate DATE,
    CONSTRAINT FK_RecruitmentPlans_Department FOREIGN KEY (DepartmentID) REFERENCES Departments(DepartmentID)
);
--Bảng Ứng viên (Applicants)

CREATE TABLE Applicants (
    ApplicantID INT PRIMARY KEY IDENTITY(1,1),
    PlanID INT,
    FullName NVARCHAR(100),
    Email NVARCHAR(100),
    Phone NVARCHAR(15),
    Status NVARCHAR(50) CHECK (Status IN (N'Đã ứng tuyển', N'Đã phỏng vấn', N'Đã tuyển', N'Từ chối')),
    CONSTRAINT FK_Applicants_Plan FOREIGN KEY (PlanID) REFERENCES RecruitmentPlans(PlanID)
);