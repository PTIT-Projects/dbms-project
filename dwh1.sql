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
    position_name NVARCHAR(100),
	department_sk INT, --khoá ngoại đến dim_departments
	department_name NVARCHAR(100)
);
-- Bảng dim_departments: Lưu trữ thông tin về các phòng ban.
CREATE TABLE dim_departments (
    department_sk INT PRIMARY KEY,
    department_id INT,  -- ID gốc từ bảng Departments
    department_name NVARCHAR(100),
    manager_sk INT,  -- Khóa ngoại tới dim_employees
	manager_name NVARCHAR(100),
);

-- Bảng dim_employees:  Lưu trữ thông tin chi tiết về nhân viên.
CREATE TABLE dim_employees (
    employee_sk INT PRIMARY KEY,
    employee_id INT,  -- ID gốc từ bảng Employees
    full_name NVARCHAR(100),
    date_of_birth DATE,
    gender CHAR(1),
    address NVARCHAR(255),
    phone NVARCHAR(20),
    email NVARCHAR(100),
    department_sk INT,  -- Khóa ngoại tới dim_departments
	department_name NVARCHAR(100),
    position_sk INT,  -- Khóa ngoại tới dim_positions
	position_name NVARCHAR(100),
	age INT,
    hire_date DATE,
    current_contract_type NVARCHAR(50),  -- Từ bảng Contracts: ContractType
    contract_start_date DATE,  -- Từ bảng Contracts: StartDate
    contract_end_date DATE,  -- Từ bảng Contracts: EndDate
	contract_duration INT,  --Số ngày của hợp đồng hiện tại
	total_years_worked DECIMAL(5,2), --Số năm làm việc tính từ hire_date đến ngày hiện tại
    insurance_number NVARCHAR(50),  -- Từ bảng Insurance: InsuranceNumber
    insurance_type NVARCHAR(50),  -- Từ bảng Insurance: InsuranceType
    insurance_start_date DATE,  -- Từ bảng Insurance: StartDate
    insurance_end_date DATE,  -- Từ bảng Insurance: EndDate
	insurance_duration INT,   ----Số ngày của bảo hiểm hiện tại
    FOREIGN KEY (department_sk) REFERENCES dim_departments(department_sk),
    FOREIGN KEY (position_sk) REFERENCES dim_positions(position_sk)
);

-- dim_positions → dim_departments
ALTER TABLE dim_positions
ADD CONSTRAINT fk_positions_department
FOREIGN KEY (department_sk) REFERENCES dim_departments(department_sk);

-- dim_departments → dim_employees (manager_sk là nhân viên)
ALTER TABLE dim_departments
ADD CONSTRAINT fk_departments_manager
FOREIGN KEY (manager_sk) REFERENCES dim_employees(employee_sk);

-- Bảng fact_attendance: Lưu trữ dữ liệu điểm danh của nhân viên.
CREATE TABLE fact_attendance (
    attendance_sk INT PRIMARY KEY,
    attendance_id INT,  -- ID gốc từ bảng Attendance
    employee_sk INT,  -- Khóa ngoại tới dim_employees
	employee_name NVARCHAR(100),
	department_name NVARCHAR(100),
	position_name NVARCHAR(100),
    date_sk INT,  -- Khóa ngoại tới dim_date
    check_in_time TIME,
    check_out_time TIME,
	hours_worked DECIMAL(5,2), --số giờ làm việc trong ngày
	is_late BIT,  --Xác định nhân viên có đi muộn hay không (so với giờ bắt đầu ca làm việc).
	overtime_hours DECIMAL(5,2), --Số giờ làm thêm nếu có.
	is_early_leave BIT, --Xác định nhân viên có rời sớm hay không
    status NVARCHAR(20),
    FOREIGN KEY (employee_sk) REFERENCES dim_employees(employee_sk),
    FOREIGN KEY (date_sk) REFERENCES dim_date(date_sk)
);

-- Bảng fact_salary: Lưu trữ dữ liệu lương của nhân viên.
CREATE TABLE fact_salary (
    salary_sk INT PRIMARY KEY,
    salary_id INT,  -- ID gốc từ bảng Salary
    employee_sk INT,  -- Khóa ngoại tới dim_employees
	employee_name NVARCHAR(100),
    department_name NVARCHAR(100),
    position_name NVARCHAR(100), 
    date_sk INT,  -- Khóa ngoại tới dim_date (ngày đầu tháng)
    basic_salary DECIMAL(10,2),
    allowance DECIMAL(10,2), ---- Tổng các khoản phụ cấp
    deductions DECIMAL(10,2), ---- Các khoản khấu trừ khác (bảo hiểm, phạt, v.v., thuế)
	total_salary DECIMAL(10,2),-- Tổng lương = basic + allow - deductions
	payment_status NVARCHAR(20) DEFAULT 'Chưa thanh toán', --trạng thái thanh toán lương
    FOREIGN KEY (employee_sk) REFERENCES dim_employees(employee_sk),
    FOREIGN KEY (date_sk) REFERENCES dim_date(date_sk)
);


-- Bảng fact_leave_balance: Lưu trữ dữ liệu số ngày nghỉ phép của nhân viên.
CREATE TABLE fact_leave_balance (
    leave_balance_sk INT PRIMARY KEY,
    leave_balance_id INT,  -- ID gốc từ bảng LeaveBalance
    employee_sk INT,  -- Khóa ngoại tới dim_employees
	employee_name NVARCHAR(100),
    department_name NVARCHAR(100),
    position_name NVARCHAR(100),   

	leave_type NVARCHAR(50) DEFAULT 'Nghỉ năm',
	date_sk INT,
	granularity NVARCHAR(10) DEFAULT 'Năm',  -- 'Năm', 'Quý', 'Tháng'
    total_leave_days INT,
    used_leave_days INT,
    remaining_leave_days INT,
    FOREIGN KEY (employee_sk) REFERENCES dim_employees(employee_sk),
	FOREIGN KEY (date_sk) REFERENCES dim_date(date_sk)
);
-- Bảng fact_work_trips: Lưu trữ dữ liệu về các chuyến công tác của nhân viên.
CREATE TABLE fact_work_trips (
    work_trip_sk INT PRIMARY KEY,
    work_trip_id INT,  -- ID gốc từ bảng WorkTrips
    employee_sk INT,  -- Khóa ngoại tới dim_employees
	employee_name NVARCHAR(100),
    department_name NVARCHAR(100),
    position_name NVARCHAR(100),
    start_date_sk INT,  -- Khóa ngoại tới dim_date
    end_date_sk INT,  -- Khóa ngoại tới dim_date
	trip_duration INT, -- Tổng số ngày đi công tác
    destination NVARCHAR(100), -- Điểm đến của chuyến công tác
    purpose NVARCHAR(255), --Mục đích công tác (họp khách hàng, đào tạo, khảo sát...)
    total_cost DECIMAL(10,2) DEFAULT 0, --Tổng chi phí của chuyến công tác 
    status NVARCHAR(20),   --'Hoàn thành', 'Đang diễn ra', 'Đã hủy'...
    FOREIGN KEY (employee_sk) REFERENCES dim_employees(employee_sk),
    FOREIGN KEY (start_date_sk) REFERENCES dim_date(date_sk),
    FOREIGN KEY (end_date_sk) REFERENCES dim_date(date_sk)
);

-- Bảng fact_recruitment_plan: Lưu trữ dữ liệu về các kế hoạch tuyển dụng.
CREATE TABLE fact_recruitment_plan (
    recruitment_sk INT PRIMARY KEY,
    recruitment_id INT,  -- ID gốc từ bảng RecruitmentPlan
    position_sk INT,  -- Khóa ngoại tới dim_positions
	position_name NVARCHAR(100),
    department_sk INT,  -- Khóa ngoại tới dim_departments
	department_name NVARCHAR(100),
    start_date_sk INT,  -- Khóa ngoại tới dim_date
    end_date_sk INT,  -- Khóa ngoại tới dim_date
    quantity INT,  -- Số lượng vị trí cần tuyển
	recruitment_duration INT, --Số ngày của kế hoạch tuyển dụng
    remaining_positions INT, --Số vị trí còn trống(sô lượng đã tuyển trừ đi)
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
	position_name NVARCHAR(100),
	department_name NVARCHAR(100),
    status NVARCHAR(20),  -- Trạng thái ứng viên (Đã duyệt, Đã từ chối, Đang xét duyệt, Đã phỏng vấn, Chưa phỏng vấn)
    FOREIGN KEY (recruitment_sk) REFERENCES fact_recruitment_plan(recruitment_sk),
    FOREIGN KEY (application_date_sk) REFERENCES dim_date(date_sk)
);

-- Bảng fact_registrations: Lưu trữ dữ liệu về các đăng ký (ví dụ: nghỉ phép).
CREATE TABLE fact_registrations (
    registration_sk INT PRIMARY KEY,
    registration_id INT,  -- ID gốc từ bảng Registrations
    employee_sk INT,  -- Khóa ngoại tới dim_employees
	employee_name NVARCHAR(100),
	department_name NVARCHAR(100),
    approved_by_name NVARCHAR(100), -- Tên người duyệt
    registration_type NVARCHAR(50), -- Loại đăng ký (ví dụ: 'Nghỉ phép', 'Làm thêm', 'Công tác', v.v.)
    request_date_sk INT,  -- Khóa ngoại tới dim_date
	registration_duration INT, -- Thời lượng đăng ký (ví dụ: số ngày nghỉ hoặc số giờ làm thêm)
    status NVARCHAR(20),
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
	employee_name NVARCHAR(100),
	department_name NVARCHAR(100),
    decision_date_sk INT,  -- Khóa ngoại tới dim_date
    decision_type NVARCHAR(50),  -- Loại quyết định (Khen thưởng, Kỷ luật)
    decision_details NNVARCHAR(255),  -- Chi tiết quyết định
	decision_effective_date DATE, --Ngày có hiệu lực
    decision_expiry_date DATE, -- Ngyà hết hiệu lực(nếu có)
    FOREIGN KEY (employee_sk) REFERENCES dim_employees(employee_sk),
    FOREIGN KEY (decision_date_sk) REFERENCES dim_date(date_sk)
);

