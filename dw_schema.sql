-- Stored Procedure for dim_date
CREATE PROCEDURE sp_transfer_dim_date
AS
BEGIN
    INSERT INTO hrms_warehouse.dbo.dim_date (date_sk, date, year, month, day, week, quarter)
    SELECT date_sk, date, year, month, day, week, quarter
    FROM original_database.dbo.dim_date;
END;
--Stored Procedure for dim_positions
CREATE PROCEDURE sp_transfer_dim_positions
AS
BEGIN
    INSERT INTO hrms_warehouse.dbo.dim_positions (position_sk, position_id, position_name, department_sk, department_name)
    SELECT position_sk, position_id, position_name, department_sk, department_name
    FROM original_database.dbo.dim_positions;
END;
--Stored Procedure for dim_departments
CREATE PROCEDURE sp_transfer_dim_departments
AS
BEGIN
    INSERT INTO hrms_warehouse.dbo.dim_departments (department_sk, department_id, department_name, manager_sk, manager_name)
    SELECT department_sk, department_id, department_name, manager_sk, manager_name
    FROM original_database.dbo.dim_departments;
END;
--Stored Procedure for dim_employees
CREATE PROCEDURE sp_transfer_dim_employees
AS
BEGIN
    INSERT INTO hrms_warehouse.dbo.dim_employees (employee_sk, employee_id, full_name, date_of_birth, gender, address, phone, email, department_sk, department_name, position_sk, position_name, age, hire_date, current_contract_type, contract_start_date, contract_end_date, contract_duration, total_years_worked, insurance_number, insurance_type, insurance_start_date, insurance_end_date, insurance_duration)
    SELECT employee_sk, employee_id, full_name, date_of_birth, gender, address, phone, email, department_sk, department_name, position_sk, position_name, age, hire_date, current_contract_type, contract_start_date, contract_end_date, contract_duration, total_years_worked, insurance_number, insurance_type, insurance_start_date, insurance_end_date, insurance_duration
    FROM original_database.dbo.dim_employees;
END;
--Creating a Job to Run Procedures Daily
USE msdb;
GO

EXEC dbo.sp_add_job
    @job_name = N'TransferDataJob';

EXEC dbo.sp_add_jobstep
    @job_name = N'TransferDataJob',
    @step_name = N'Transfer dim_date',
    @subsystem = N'TSQL',
    @command = N'EXEC hrms_warehouse.dbo.sp_transfer_dim_date',
    @retry_attempts = 5,
    @retry_interval = 5;

EXEC dbo.sp_add_jobstep
    @job_name = N'TransferDataJob',
    @step_name = N'Transfer dim_positions',
    @subsystem = N'TSQL',
    @command = N'EXEC hrms_warehouse.dbo.sp_transfer_dim_positions',
    @retry_attempts = 5,
    @retry_interval = 5;

-- Add more steps for other procedures...

EXEC dbo.sp_add_schedule
    @job_name = N'TransferDataJob',
    @name = N'DailySchedule',
    @freq_type = 4,  -- Daily
    @freq_interval = 1,
    @active_start_time = 010000;  -- 1:00 AM

EXEC dbo.sp_attach_schedule
    @job_name = N'TransferDataJob',
    @schedule_name = N'DailySchedule';

EXEC dbo.sp_add_jobserver
    @job_name = N'TransferDataJob',
    @server_name = N'(local)';
GO
