-- Dimension table for recruitment plans
CREATE TABLE dim_recruitment_plan (
    recruitment_plan_key INT IDENTITY(1,1) PRIMARY KEY,
    plan_id INT,  -- source system ID
    position_name NVARCHAR(100),
    department_name NVARCHAR(100),
    quantity INT,
    start_date DATE,
    end_date DATE,
    is_current BIT DEFAULT 1,
    valid_from DATE,
    valid_to DATE
);

-- Dimension table for registrations
CREATE TABLE dim_registration (
    registration_key INT IDENTITY(1,1) PRIMARY KEY,
    registration_id INT,  -- source system ID
    employee_name NVARCHAR(100),
    registration_type NVARCHAR(50),
    request_date DATE,
    details NVARCHAR(MAX),
    status NVARCHAR(50),
    approved_by NVARCHAR(100),
    is_current BIT DEFAULT 1,
    valid_from DATE,
    valid_to DATE
);

-- Dimension table for work trip requests
CREATE TABLE dim_work_trip_request (
    work_trip_request_key INT IDENTITY(1,1) PRIMARY KEY,
    request_id INT,  -- source system ID
    employee_name NVARCHAR(100),
    destination NVARCHAR(255),
    start_date DATE,
    end_date DATE,
    purpose NVARCHAR(MAX),
    status NVARCHAR(50),
    is_current BIT DEFAULT 1,
    valid_from DATE,
    valid_to DATE
);

-- Dimension table for salary
CREATE TABLE dim_salary (
    salary_key INT IDENTITY(1,1) PRIMARY KEY,
    employee_key INT FOREIGN KEY REFERENCES dim_employee(employee_key),
    time_key INT FOREIGN KEY REFERENCES dim_time(time_id),
    basic_salary DECIMAL(18,2),
    allowance DECIMAL(18,2),
    deductions DECIMAL(18,2),
    net_salary DECIMAL(18,2),
    month INT,
    year INT
);

-- Fact table for recruitment plans
CREATE TABLE fact_recruitment_plan (
    recruitment_plan_key INT FOREIGN KEY REFERENCES dim_recruitment_plan(recruitment_plan_key),
    time_key INT FOREIGN KEY REFERENCES dim_time(time_id),
    position_name NVARCHAR(100),
    department_name NVARCHAR(100),
    quantity INT,
    start_date DATE,
    end_date DATE
);

-- Fact table for registrations
CREATE TABLE fact_registration (
    registration_key INT FOREIGN KEY REFERENCES dim_registration(registration_key),
    time_key INT FOREIGN KEY REFERENCES dim_time(time_id),
    employee_name NVARCHAR(100),
    registration_type NVARCHAR(50),
    request_date DATE,
    details NVARCHAR(MAX),
    status NVARCHAR(50),
    approved_by NVARCHAR(100)
);

-- Fact table for work trip requests
CREATE TABLE fact_work_trip_request (
    work_trip_request_key INT FOREIGN KEY REFERENCES dim_work_trip_request(work_trip_request_key),
    time_key INT FOREIGN KEY REFERENCES dim_time(time_id),
    employee_name NVARCHAR(100),
    destination NVARCHAR(255),
    start_date DATE,
    end_date DATE,
    purpose NVARCHAR(MAX),
    status NVARCHAR(50)
);

-- Fact table for salary 
CREATE TABLE fact_salary (
    salary_key INT FOREIGN KEY REFERENCES dim_salary(salary_key),
    employee_key INT FOREIGN KEY REFERENCES dim_employee(employee_key),
    time_key INT FOREIGN KEY REFERENCES dim_time(time_id),
    basic_salary DECIMAL(18,2),
    allowance DECIMAL(18,2),
    deductions DECIMAL(18,2),
    net_salary DECIMAL(18,2),
    month INT,
    year INT
);
