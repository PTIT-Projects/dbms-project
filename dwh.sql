-- Bảng dim_date: Lưu trữ thông tin về ngày tháng để hỗ trợ phân tích theo thời gian.
CREATE TABLE dim_date (
    date_sk INT PRIMARY KEY,
    date DATE,
    year INT,
    month INT,
    day INT,
    week INT,
    quarter INT
);

-- Bảng dim_positions: Lưu trữ thông tin về các vị trí công việc.
CREATE TABLE dim_positions (
    position_sk INT PRIMARY KEY,
    position_id INT,  -- ID gốc từ bảng Positions
    position_name VARCHAR(100)
	department_sk INT --khoá ngoại đến dim_departments
	FOREIGN KEY (department_sk) REFERENCES dim_departments(department_sk);
);

-- Bảng dim_departments: Lưu trữ thông tin về các phòng ban.
CREATE TABLE dim_departments (
    department_sk INT PRIMARY KEY,
    department_id INT,  -- ID gốc từ bảng Departments
    department_name VARCHAR(100),
    manager_sk INT,  -- Khóa ngoại tới dim_employees
    FOREIGN KEY (manager_sk) REFERENCES dim_employees(employee_sk)
);

-- Bảng dim_employees: Lưu trữ thông tin chi tiết về nhân viên.
-- Bảng dim_employees: Lưu trữ thông tin chi tiết về nhân viên.
CREATE TABLE dim_employees (
    employee_sk INT PRIMARY KEY,
    employee_id INT,  -- ID gốc từ bảng Employees
    full_name VARCHAR(100),
    date_of_birth DATE,
    gender CHAR(1),
    address VARCHAR(255),
    phone VARCHAR(20),
    email VARCHAR(100),
    department_sk INT,  -- Khóa ngoại tới dim_departments
    position_sk INT,  -- Khóa ngoại tới dim_positions
    hire_date DATE,
    current_contract_type VARCHAR(50),  -- Từ bảng Contracts: ContractType
    contract_start_date DATE,  -- Từ bảng Contracts: StartDate
    contract_end_date DATE,  -- Từ bảng Contracts: EndDate
    insurance_number VARCHAR(50),  -- Từ bảng Insurance: InsuranceNumber
    insurance_type VARCHAR(50),  -- Từ bảng Insurance: InsuranceType
    insurance_start_date DATE,  -- Từ bảng Insurance: StartDate
    insurance_end_date DATE,  -- Từ bảng Insurance: EndDate
    FOREIGN KEY (department_sk) REFERENCES dim_departments(department_sk),
    FOREIGN KEY (position_sk) REFERENCES dim_positions(position_sk)
);

-- Bảng fact_attendance: Lưu trữ dữ liệu điểm danh của nhân viên.
CREATE TABLE fact_attendance (
    attendance_sk INT PRIMARY KEY,
    attendance_id INT,  -- ID gốc từ bảng Attendance
    employee_sk INT,  -- Khóa ngoại tới dim_employees
    date_sk INT,  -- Khóa ngoại tới dim_date
    check_in_time TIME,
    check_out_time TIME,
    status VARCHAR(20),
    FOREIGN KEY (employee_sk) REFERENCES dim_employees(employee_sk),
    FOREIGN KEY (date_sk) REFERENCES dim_date(date_sk)
);

-- Bảng fact_salary: Lưu trữ dữ liệu lương của nhân viên.
CREATE TABLE fact_salary (
    salary_sk INT PRIMARY KEY,
    salary_id INT,  -- ID gốc từ bảng Salary
    employee_sk INT,  -- Khóa ngoại tới dim_employees
    date_sk INT,  -- Khóa ngoại tới dim_date (ngày đầu tháng)
    basic_salary DECIMAL(10,2),
    allowance DECIMAL(10,2),
    deductions DECIMAL(10,2),
    net_salary DECIMAL(10,2),
    FOREIGN KEY (employee_sk) REFERENCES dim_employees(employee_sk),
    FOREIGN KEY (date_sk) REFERENCES dim_date(date_sk)
);

-- Bảng fact_leave_balance: Lưu trữ dữ liệu số ngày nghỉ phép hàng năm của nhân viên.
CREATE TABLE fact_leave_balance (
    leave_balance_sk INT PRIMARY KEY,
    leave_balance_id INT,  -- ID gốc từ bảng LeaveBalance
    employee_sk INT,  -- Khóa ngoại tới dim_employees
    year INT,
    total_leave_days INT,
    used_leave_days INT,
    remaining_leave_days INT,
    FOREIGN KEY (employee_sk) REFERENCES dim_employees(employee_sk)
);

-- Bảng fact_work_trips: Lưu trữ dữ liệu về các chuyến công tác của nhân viên.
CREATE TABLE fact_work_trips (
    work_trip_sk INT PRIMARY KEY,
    work_trip_id INT,  -- ID gốc từ bảng WorkTrips
    employee_sk INT,  -- Khóa ngoại tới dim_employees
    start_date_sk INT,  -- Khóa ngoại tới dim_date
    end_date_sk INT,  -- Khóa ngoại tới dim_date
    destination VARCHAR(100),
    purpose VARCHAR(255),
    status VARCHAR(20),
    FOREIGN KEY (employee_sk) REFERENCES dim_employees(employee_sk),
    FOREIGN KEY (start_date_sk) REFERENCES dim_date(date_sk),
    FOREIGN KEY (end_date_sk) REFERENCES dim_date(date_sk)
);

-- Bảng fact_recruitment_plan: Lưu trữ dữ liệu về các kế hoạch tuyển dụng.
CREATE TABLE fact_recruitment_plan (
    recruitment_sk INT PRIMARY KEY,
    recruitment_id INT,  -- ID gốc từ bảng RecruitmentPlan
    position_sk INT,  -- Khóa ngoại tới dim_positions
    department_sk INT,  -- Khóa ngoại tới dim_departments
    start_date_sk INT,  -- Khóa ngoại tới dim_date
    end_date_sk INT,  -- Khóa ngoại tới dim_date
    quantity INT,  -- Số lượng vị trí cần tuyển
    FOREIGN KEY (position_sk) REFERENCES dim_positions(position_sk),
    FOREIGN KEY (department_sk) REFERENCES dim_departments(department_sk),
    FOREIGN KEY (start_date_sk) REFERENCES dim_date(date_sk),
    FOREIGN KEY (end_date_sk) REFERENCES dim_date(date_sk)
);


-- Bảng fact_application: Lưu trữ dữ liệu về các ứng viên trong quá trình tuyển dụng.
CREATE TABLE fact_application (
    application_sk INT PRIMARY KEY,
    applicant_id INT,  -- ID gốc từ bảng Applicants
    recruitment_sk INT,  -- Khóa ngoại tới fact_recruitment_plan
    application_date_sk INT,  -- Khóa ngoại tới dim_date
    status VARCHAR(20),  -- Trạng thái ứng viên (Đã duyệt, Đã từ chối, Đang chờ)
    FOREIGN KEY (recruitment_sk) REFERENCES fact_recruitment_plan(recruitment_sk),
    FOREIGN KEY (application_date_sk) REFERENCES dim_date(date_sk)
);
-- Bảng fact_registrations: Lưu trữ dữ liệu về các đăng ký (ví dụ: nghỉ phép).
CREATE TABLE fact_registrations (
    registration_sk INT PRIMARY KEY,
    registration_id INT,  -- ID gốc từ bảng Registrations
    employee_sk INT,  -- Khóa ngoại tới dim_employees
    registration_type VARCHAR(50),
    request_date_sk INT,  -- Khóa ngoại tới dim_date
    status VARCHAR(20),
    approved_by_sk INT,  -- Khóa ngoại tới dim_employees
    FOREIGN KEY (employee_sk) REFERENCES dim_employees(employee_sk),
    FOREIGN KEY (request_date_sk) REFERENCES dim_date(date_sk),
    FOREIGN KEY (approved_by_sk) REFERENCES dim_employees(employee_sk)
);
-- Bảng fact_decision: Lưu trữ dữ liệu về các quyết định khen thưởng hoặc kỷ luật của nhân viên.
CREATE TABLE fact_decision (
    decision_sk INT PRIMARY KEY,
    decision_id INT,  -- ID gốc từ bảng Decisions
    employee_sk INT,  -- Khóa ngoại tới dim_employees
    decision_date_sk INT,  -- Khóa ngoại tới dim_date
    decision_type VARCHAR(50),  -- Loại quyết định (Khen thưởng, Kỷ luật)
    decision_details VARCHAR(255),  -- Chi tiết quyết định
    FOREIGN KEY (employee_sk) REFERENCES dim_employees(employee_sk),
    FOREIGN KEY (decision_date_sk) REFERENCES dim_date(date_sk)
);

