-- Time dimension
CREATE TABLE dim_time (
    time_key INT IDENTITY(1,1) PRIMARY KEY,
    full_date DATE,
    day_of_week TINYINT,
    day_name NVARCHAR(10),
    day_of_month TINYINT,
    month_num TINYINT, 
    month_name NVARCHAR(10),
    quarter TINYINT,
    year_num SMALLINT,
    is_weekend BIT,
    is_holiday BIT,
    holiday_name NVARCHAR(50)
);

-- Employee dimension 
CREATE TABLE dim_employee (
    employee_key INT IDENTITY(1,1) PRIMARY KEY,
    employee_id INT,
    full_name NVARCHAR(100),
    gender NVARCHAR(10),
    department_id INT,
    department_name NVARCHAR(100),
    position_id INT, 
    position_name NVARCHAR(100),
    employee_status NVARCHAR(50),
    is_current BIT DEFAULT 1,
    effective_date DATE,
    expiration_date DATE
);

-- Department dimension
CREATE TABLE dim_department (
    department_key INT IDENTITY(1,1) PRIMARY KEY,
    department_id INT,
    department_name NVARCHAR(100),
    manager_id INT,
    manager_name NVARCHAR(100),
    is_current BIT DEFAULT 1,
    effective_date DATE,
    expiration_date DATE
);

-- Position dimension
CREATE TABLE dim_position (
    position_key INT IDENTITY(1,1) PRIMARY KEY,
    position_id INT,
    position_name NVARCHAR(100),
    department_key INT,
    is_current BIT DEFAULT 1,
    effective_date DATE,
    expiration_date DATE,
    FOREIGN KEY (department_key) REFERENCES dim_department(department_key)
);

-- Salary Fact table
CREATE TABLE fact_salary (
    salary_key INT IDENTITY(1,1) PRIMARY KEY,
    employee_key INT,
    time_key INT,
    basic_salary DECIMAL(18,2),
    allowance DECIMAL(18,2), 
    deductions DECIMAL(18,2),
    net_salary DECIMAL(18,2),
    FOREIGN KEY (employee_key) REFERENCES dim_employee(employee_key),
    FOREIGN KEY (time_key) REFERENCES dim_time(time_key)
);

-- Leave Fact table 
CREATE TABLE fact_leave (
    leave_key INT IDENTITY(1,1) PRIMARY KEY,
    employee_key INT,
    time_key INT,
    total_days INT,
    used_days INT,
    remaining_days INT,
    FOREIGN KEY (employee_key) REFERENCES dim_employee(employee_key),
    FOREIGN KEY (time_key) REFERENCES dim_time(time_key)
);

-- Attendance Fact table
CREATE TABLE fact_attendance (
    attendance_key INT IDENTITY(1,1) PRIMARY KEY, 
    employee_key INT,
    time_key INT,
    status VARCHAR(20),
    check_in_time TIME,
    check_out_time TIME,
    working_hours DECIMAL(4,2),
    FOREIGN KEY (employee_key) REFERENCES dim_employee(employee_key),
    FOREIGN KEY (time_key) REFERENCES dim_time(time_key)
);

-- Performance Fact table
CREATE TABLE fact_performance (
    performance_key INT IDENTITY(1,1) PRIMARY KEY,
    employee_key INT,
    time_key INT,
    score DECIMAL(4,2),
    bonus_amount DECIMAL(18,2),
    comments NVARCHAR(500),
    FOREIGN KEY (employee_key) REFERENCES dim_employee(employee_key),  
    FOREIGN KEY (time_key) REFERENCES dim_time(time_key)
);

-- Training Fact table
CREATE TABLE fact_training (
    training_key INT IDENTITY(1,1) PRIMARY KEY,
    employee_key INT,
    time_key INT,
    course_name NVARCHAR(100),
    completion_status VARCHAR(20),
    score DECIMAL(4,2),
    certification VARCHAR(100),
    FOREIGN KEY (employee_key) REFERENCES dim_employee(employee_key),
    FOREIGN KEY (time_key) REFERENCES dim_time(time_key)
);

-- Recruitment Fact table
CREATE TABLE fact_recruitment (
    recruitment_key INT IDENTITY(1,1) PRIMARY KEY,
    position_key INT,
    department_key INT,
    time_key INT,
    applicants_count INT,
    hired_count INT,
    cost DECIMAL(18,2),
    FOREIGN KEY (position_key) REFERENCES dim_position(position_key),
    FOREIGN KEY (department_key) REFERENCES dim_department(department_key),
    FOREIGN KEY (time_key) REFERENCES dim_time(time_key)
);

-- Project Dimension
CREATE TABLE dim_project (
    project_key INT IDENTITY(1,1) PRIMARY KEY,
    project_id INT,
    project_name NVARCHAR(100),
    project_manager_key INT,
    start_date DATE,
    end_date DATE,
    status VARCHAR(20),
    budget DECIMAL(18,2),
    FOREIGN KEY (project_manager_key) REFERENCES dim_employee(employee_key)
);

-- Project Assignment Fact table
CREATE TABLE fact_project_assignment (
    assignment_key INT IDENTITY(1,1) PRIMARY KEY,
    project_key INT,
    employee_key INT,
    time_key INT,
    role NVARCHAR(50),
    hours_allocated INT,
    actual_hours INT,
    FOREIGN KEY (project_key) REFERENCES dim_project(project_key),
    FOREIGN KEY (employee_key) REFERENCES dim_employee(employee_key),
    FOREIGN KEY (time_key) REFERENCES dim_time(time_key)
);

-- Skill Dimension  
CREATE TABLE dim_skill (
    skill_key INT IDENTITY(1,1) PRIMARY KEY,
    skill_name NVARCHAR(100),
    skill_category NVARCHAR(50),
    description NVARCHAR(500)
);

-- Employee Skills Bridge table
CREATE TABLE fact_employee_skills (
    employee_key INT,
    skill_key INT,
    proficiency_level VARCHAR(20),
    acquired_date DATE,
    certified BIT,
    PRIMARY KEY (employee_key, skill_key),
    FOREIGN KEY (employee_key) REFERENCES dim_employee(employee_key),
    FOREIGN KEY (skill_key) REFERENCES dim_skill(skill_key)
);

-- Benefits Dimension
CREATE TABLE dim_benefit (
    benefit_key INT IDENTITY(1,1) PRIMARY KEY,
    benefit_name NVARCHAR(100),
    benefit_type NVARCHAR(50),
    description NVARCHAR(500),
    cost_type VARCHAR(20)
);

-- Employee Benefits Fact table
CREATE TABLE fact_employee_benefits (
    benefit_assignment_key INT IDENTITY(1,1) PRIMARY KEY,
    employee_key INT,
    benefit_key INT,
    time_key INT,
    coverage_amount DECIMAL(18,2),
    employee_contribution DECIMAL(18,2),
    employer_contribution DECIMAL(18,2),
    status VARCHAR(20),
    FOREIGN KEY (employee_key) REFERENCES dim_employee(employee_key),
    FOREIGN KEY (benefit_key) REFERENCES dim_benefit(benefit_key),
    FOREIGN KEY (time_key) REFERENCES dim_time(time_key)
);
