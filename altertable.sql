--1 bảng dim_positions
ALTER TABLE dim_positions
ADD department_name VARCHAR(100);

--2 bảng dim_departments
ALTER TABLE dim_departments
ADD manager_name VARCHAR(100);
--3 bảng employess

BEGIN
    ALTER TABLE dim_employees ADD department_name VARCHAR(100);
    ALTER TABLE dim_employees ADD position_name VARCHAR(100);
    ALTER TABLE dim_employees ADD total_years_worked DECIMAL(5,2);
    ALTER TABLE dim_employees ADD age INT;
    ALTER TABLE dim_employees ADD contract_duration INT;
    ALTER TABLE dim_employees ADD insurance_duration INT;
END

--4 bang attendance
-- Thêm các trường tính toán liên quan đến thời gian làm việc
ALTER TABLE fact_attendance
ADD hours_worked DECIMAL(5,2),
	is_late BIT,
	overtime_hours DECIMAL(5,2),
	is_early_leave BIT;

--Thêm các trường denormalize từ dim_employees

ALTER TABLE fact_attendance
ADD department_name VARCHAR(100),
	employee_name VARCHAR(100),
	 position_name VARCHAR(100);


--5 bang fact_salary
-- Thêm các trường denormalize từ dim_employees
ALTER TABLE fact_salary
ADD employee_name VARCHAR(100),
    department_name VARCHAR(100),
    position_name VARCHAR(100);

-- Thêm các trường tính toán và trạng thái
ALTER TABLE fact_salary
ADD total_salary DECIMAL(10,2),
    tax_deductions DECIMAL(10,2),
    payment_status VARCHAR(20) DEFAULT 'Chưa thanh toán';

--6 bang fact_leave_balance
-- Thêm các trường denormalize từ dim_employees
ALTER TABLE fact_leave_balance
ADD employee_name VARCHAR(100),
    department_name VARCHAR(100),
    position_name VARCHAR(100);

-- Thêm các trường liên quan đến loại nghỉ phép
ALTER TABLE fact_leave_balance
ADD leave_type VARCHAR(50) DEFAULT 'Nghỉ năm',
    pending_leave_days INT DEFAULT 0;

--7 bang fact_work_trip
-- Thêm các trường denormalize từ dim_employees
ALTER TABLE fact_work_trips
ADD employee_name VARCHAR(100),
    department_name VARCHAR(100),
    position_name VARCHAR(100);

-- Thêm các trường tính toán
ALTER TABLE fact_work_trips
ADD trip_duration INT,
    total_cost DECIMAL(10,2) DEFAULT 0;
--8 bang fact_recruitment_plan
-- Thêm các trường denormalize từ dim_positions và dim_departments
ALTER TABLE fact_recruitment_plan
ADD position_name VARCHAR(100),
    department_name VARCHAR(100);

-- Thêm các trường tính toán
ALTER TABLE fact_recruitment_plan
ADD recruitment_duration INT,
    remaining_positions INT;

--9 bang  fact_application
-- Thêm trường denormalize từ fact_recruitment_plan
ALTER TABLE fact_application
ADD position_name VARCHAR(100);

-- Thêm các trường tính toán và trạng thái
ALTER TABLE fact_application
ADD days_since_application INT,
    interview_status VARCHAR(20) DEFAULT 'Chưa phỏng vấn';

--10 bang fact_registrations
-- Thêm các trường denormalize từ dim_employees
ALTER TABLE fact_registrations
ADD employee_name VARCHAR(100),
    approved_by_name VARCHAR(100);

-- Thêm các trường tính toán thời gian
ALTER TABLE fact_registrations
ADD registration_duration INT,
    approval_time INT;

--11 bảng fact_decision
-- Thêm trường denormalize từ dim_employees
ALTER TABLE fact_decision
ADD employee_name VARCHAR(100);

-- Thêm các trường ngày hiệu lực
ALTER TABLE fact_decision
ADD decision_effective_date DATE,
    decision_expiry_date DATE;