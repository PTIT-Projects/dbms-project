USE [hrms_warehouse];
GO

-- =============================================
-- SP for dim_date Population (Run once or periodically)
-- =============================================
CREATE OR ALTER PROCEDURE sp_Load_dim_date
    @StartDate DATE = '2020-01-01',
    @EndDate DATE = '2030-12-31'
AS
BEGIN
    SET NOCOUNT ON;
    PRINT 'Starting sp_Load_dim_date...';

    -- Ensure the table exists (optional, defensive programming)
    IF OBJECT_ID('dbo.dim_date', 'U') IS NULL
    BEGIN
        PRINT 'Error: dim_date table does not exist.';
        RETURN;
    END;

    BEGIN TRY
        BEGIN TRANSACTION;

        DECLARE @CurrentDate DATE = @StartDate;

        WHILE @CurrentDate <= @EndDate
        BEGIN
            IF NOT EXISTS (SELECT 1 FROM dbo.dim_date WHERE date = @CurrentDate)
            BEGIN
                INSERT INTO dbo.dim_date (date, year, month, day, week, quarter)
                VALUES (
                    @CurrentDate,
                    DATEPART(YEAR, @CurrentDate),
                    DATEPART(MONTH, @CurrentDate),
                    DATEPART(DAY, @CurrentDate),
                    DATEPART(WEEK, @CurrentDate),
                    DATEPART(QUARTER, @CurrentDate)
                );
            END
            SET @CurrentDate = DATEADD(DAY, 1, @CurrentDate);
        END

        COMMIT TRANSACTION;
        PRINT 'Successfully loaded dates into dim_date.';
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
        PRINT 'Error loading dim_date: ' + ERROR_MESSAGE();
        -- Consider logging the error to a table
        THROW; -- Re-throw the error
    END CATCH;

    PRINT 'Finished sp_Load_dim_date.';
END;
GO

-- =============================================
-- SP for dim_departments
-- =============================================
CREATE OR ALTER PROCEDURE sp_Load_dim_departments
AS
BEGIN
    SET NOCOUNT ON;
    PRINT 'Starting sp_Load_dim_departments...';

    BEGIN TRY
        BEGIN TRANSACTION;

        MERGE INTO hrms_warehouse.dbo.dim_departments AS Target
        USING (
            SELECT
                DepartmentID,
                DepartmentName
            FROM nhansucongty.dbo.Departments
        ) AS Source
        ON Target.department_id = Source.DepartmentID -- Match based on business key
        WHEN MATCHED AND Target.department_name <> Source.DepartmentName THEN
            UPDATE SET
                Target.department_name = Source.DepartmentName
                -- Manager info will be updated in a separate step after employees are loaded
        WHEN NOT MATCHED BY Target THEN
            INSERT (department_id, department_name, manager_sk, manager_name)
            VALUES (Source.DepartmentID, Source.DepartmentName, NULL, NULL); -- Insert new departments
        -- WHEN NOT MATCHED BY SOURCE THEN DELETE; -- Optional: Handle deletions

        COMMIT TRANSACTION;
        PRINT 'Successfully merged data into dim_departments.';
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
        PRINT 'Error in sp_Load_dim_departments: ' + ERROR_MESSAGE();
        THROW;
    END CATCH;
     PRINT 'Finished sp_Load_dim_departments.';
END;
GO

-- =============================================
-- SP for dim_positions
-- =============================================
CREATE OR ALTER PROCEDURE sp_Load_dim_positions
AS
BEGIN
    SET NOCOUNT ON;
    PRINT 'Starting sp_Load_dim_positions...';

    BEGIN TRY
        BEGIN TRANSACTION;

        MERGE INTO hrms_warehouse.dbo.dim_positions AS Target
        USING (
            SELECT
                p.PositionID,
                p.PositionName,
                p.DepartmentID,
                dd.department_sk, -- Lookup the surrogate key
                dd.department_name -- Denormalize name
            FROM nhansucongty.dbo.Positions p
            INNER JOIN hrms_warehouse.dbo.dim_departments dd ON p.DepartmentID = dd.department_id -- Join to get department_sk
        ) AS Source
        ON Target.position_id = Source.PositionID
        WHEN MATCHED AND (Target.position_name <> Source.PositionName
                          OR ISNULL(Target.department_sk, -1) <> ISNULL(Source.department_sk, -1) -- Check if department changed
                          OR Target.department_name <> Source.department_name) THEN
            UPDATE SET
                Target.position_name = Source.PositionName,
                Target.department_sk = Source.department_sk,
                Target.department_name = Source.department_name
        WHEN NOT MATCHED BY Target THEN
            INSERT (position_id, position_name, department_sk, department_name)
            VALUES (Source.PositionID, Source.PositionName, Source.department_sk, Source.department_name);
        -- WHEN NOT MATCHED BY SOURCE THEN DELETE; -- Optional

        COMMIT TRANSACTION;
        PRINT 'Successfully merged data into dim_positions.';
    END TRY
    BEGIN CATCH
         IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
        PRINT 'Error in sp_Load_dim_positions: ' + ERROR_MESSAGE();
        THROW;
    END CATCH;
    PRINT 'Finished sp_Load_dim_positions.';
END;
GO

-- =============================================
-- SP for dim_employees
-- =============================================
CREATE OR ALTER PROCEDURE sp_Load_dim_employees
AS
BEGIN
    SET NOCOUNT ON;
    PRINT 'Starting sp_Load_dim_employees...';
    DECLARE @CurrentDate DATE = GETDATE(); -- For age/years worked calculation

    BEGIN TRY
        BEGIN TRANSACTION;

        -- Use CTEs to get the most relevant Contract and Insurance record per employee
        -- Assumption: Latest StartDate defines the "current" record. Adjust logic if needed.
        ;WITH LatestContract AS (
            SELECT *, ROW_NUMBER() OVER(PARTITION BY EmployeeID ORDER BY StartDate DESC, ContractID DESC) as rn
            FROM nhansucongty.dbo.Contracts
            -- WHERE Status = N'Hiệu lực' -- Uncomment if only active contracts are relevant
        ),
        LatestInsurance AS (
            SELECT *, ROW_NUMBER() OVER(PARTITION BY EmployeeID ORDER BY StartDate DESC, InsuranceID DESC) as rn
            FROM nhansucongty.dbo.Insurance
        )
        MERGE INTO hrms_warehouse.dbo.dim_employees AS Target
        USING (
            SELECT
                e.EmployeeID,
                e.FullName,
                e.DateOfBirth,
                e.Gender,
                e.Address,
                e.Phone,
                e.Email,
                dd.department_sk,
                dd.department_name,
                dp.position_sk,
                dp.position_name,
                e.HireDate,
                lc.ContractType AS current_contract_type,
                lc.StartDate AS contract_start_date,
                lc.EndDate AS contract_end_date,
                CASE WHEN lc.StartDate IS NOT NULL AND lc.EndDate IS NOT NULL THEN DATEDIFF(DAY, lc.StartDate, lc.EndDate) ELSE NULL END AS contract_duration,
                CAST(DATEDIFF(DAY, e.HireDate, @CurrentDate) / 365.25 AS DECIMAL(5,2)) AS total_years_worked,
                li.InsuranceNumber AS insurance_number,
                li.InsuranceType AS insurance_type,
                li.StartDate AS insurance_start_date,
                li.EndDate AS insurance_end_date,
                CASE WHEN li.StartDate IS NOT NULL AND li.EndDate IS NOT NULL THEN DATEDIFF(DAY, li.StartDate, li.EndDate) ELSE NULL END AS insurance_duration,
                CASE WHEN e.DateOfBirth IS NOT NULL THEN DATEDIFF(YEAR, e.DateOfBirth, @CurrentDate) ELSE NULL END as age
            FROM nhansucongty.dbo.Employees e
            LEFT JOIN hrms_warehouse.dbo.dim_departments dd ON e.DepartmentID = dd.department_id
            LEFT JOIN hrms_warehouse.dbo.dim_positions dp ON e.PositionID = dp.position_id
            LEFT JOIN LatestContract lc ON e.EmployeeID = lc.EmployeeID AND lc.rn = 1
            LEFT JOIN LatestInsurance li ON e.EmployeeID = li.EmployeeID AND li.rn = 1
            WHERE e.Status = N'Đang làm việc' -- Example: Load only active employees. Adjust if needed.
        ) AS Source
        ON Target.employee_id = Source.EmployeeID
        WHEN MATCHED THEN
            UPDATE SET -- Update all fields in case of changes (SCD Type 1)
                Target.full_name = Source.FullName,
                Target.date_of_birth = Source.DateOfBirth,
                Target.gender = Source.Gender,
                Target.address = Source.Address,
                Target.phone = Source.Phone,
                Target.email = Source.Email,
                Target.department_sk = Source.department_sk,
                Target.department_name = Source.department_name,
                Target.position_sk = Source.position_sk,
                Target.position_name = Source.position_name,
                Target.hire_date = Source.HireDate,
                Target.current_contract_type = Source.current_contract_type,
                Target.contract_start_date = Source.contract_start_date,
                Target.contract_end_date = Source.contract_end_date,
                Target.contract_duration = Source.contract_duration,
                Target.total_years_worked = Source.total_years_worked,
                Target.insurance_number = Source.insurance_number,
                Target.insurance_type = Source.insurance_type,
                Target.insurance_start_date = Source.insurance_start_date,
                Target.insurance_end_date = Source.insurance_end_date,
                Target.insurance_duration = Source.insurance_duration,
                Target.age = Source.age
        WHEN NOT MATCHED BY Target THEN
            INSERT (employee_id, full_name, date_of_birth, gender, address, phone, email,
                    department_sk, department_name, position_sk, position_name, age, hire_date,
                    current_contract_type, contract_start_date, contract_end_date, contract_duration,
                    total_years_worked, insurance_number, insurance_type, insurance_start_date,
                    insurance_end_date, insurance_duration)
            VALUES (Source.EmployeeID, Source.FullName, Source.DateOfBirth, Source.Gender, Source.Address, Source.Phone, Source.Email,
                    Source.department_sk, Source.department_name, Source.position_sk, Source.position_name, Source.age, Source.HireDate,
                    Source.current_contract_type, Source.contract_start_date, Source.contract_end_date, Source.contract_duration,
                    Source.total_years_worked, Source.insurance_number, Source.insurance_type, Source.insurance_start_date,
                    Source.insurance_end_date, Source.insurance_duration);
        -- WHEN NOT MATCHED BY SOURCE THEN UPDATE SET Target.IsActive = 0; -- Optional: Mark missing employees as inactive

        COMMIT TRANSACTION;
        PRINT 'Successfully merged data into dim_employees.';
    END TRY
    BEGIN CATCH
         IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
        PRINT 'Error in sp_Load_dim_employees: ' + ERROR_MESSAGE();
        THROW;
    END CATCH;
     PRINT 'Finished sp_Load_dim_employees.';
END;
GO

-- =============================================
-- SP to update Manager Info in dim_departments
-- (Run AFTER dim_employees is loaded)
-- =============================================
CREATE OR ALTER PROCEDURE sp_Update_dim_departments_Manager
AS
BEGIN
    SET NOCOUNT ON;
    PRINT 'Starting sp_Update_dim_departments_Manager...';

    BEGIN TRY
        BEGIN TRANSACTION;

        UPDATE dd
        SET dd.manager_sk = de.employee_sk,
            dd.manager_name = de.full_name
        FROM hrms_warehouse.dbo.dim_departments dd
        INNER JOIN nhansucongty.dbo.DepartmentManager dm ON dd.department_id = dm.DepartmentID
        INNER JOIN hrms_warehouse.dbo.dim_employees de ON dm.ManagerID = de.employee_id
        WHERE ISNULL(dd.manager_sk, -1) <> de.employee_sk OR ISNULL(dd.manager_name, '') <> de.full_name;

        -- Handle cases where manager is no longer set or manager left (optional)
        UPDATE dd
        SET dd.manager_sk = NULL,
            dd.manager_name = NULL
        FROM hrms_warehouse.dbo.dim_departments dd
        LEFT JOIN nhansucongty.dbo.DepartmentManager dm ON dd.department_id = dm.DepartmentID
        WHERE dm.DepartmentID IS NULL AND (dd.manager_sk IS NOT NULL OR dd.manager_name IS NOT NULL);


        COMMIT TRANSACTION;
        PRINT 'Successfully updated manager info in dim_departments.';
    END TRY
    BEGIN CATCH
         IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
        PRINT 'Error in sp_Update_dim_departments_Manager: ' + ERROR_MESSAGE();
        THROW;
    END CATCH;
    PRINT 'Finished sp_Update_dim_departments_Manager.';
END;
GO


-- =============================================
-- SP for fact_attendance
-- =============================================
CREATE OR ALTER PROCEDURE sp_Load_fact_attendance
    @LoadRecentDays INT = NULL -- Optional: For incremental load (e.g., load last 7 days)
AS
BEGIN
    SET NOCOUNT ON;
    PRINT 'Starting sp_Load_fact_attendance...';
    DECLARE @CutoffDate DATE = CASE WHEN @LoadRecentDays IS NOT NULL THEN DATEADD(DAY, -@LoadRecentDays, GETDATE()) ELSE '1900-01-01' END;

    BEGIN TRY
        BEGIN TRANSACTION;

        MERGE INTO hrms_warehouse.dbo.fact_attendance AS Target
        USING (
            SELECT
                a.AttendanceID,
                de.employee_sk,
                de.full_name AS employee_name,
                de.department_name,
                de.position_name,
                dd.date_sk,
                a.CheckInTime,
                a.CheckOutTime,
                -- Basic hours worked calculation. Needs refinement based on actual shifts/rules.
                CASE
                    WHEN a.CheckInTime IS NOT NULL AND a.CheckOutTime IS NOT NULL
                    THEN CAST(DATEDIFF(MINUTE, a.CheckInTime, a.CheckOutTime) / 60.0 AS DECIMAL(5,2))
                    ELSE 0
                END AS hours_worked,
                -- Placeholder logic for late/early/overtime. Requires business rules.
                CASE WHEN a.Status = N'Đi muộn' THEN 1 ELSE 0 END AS is_late,
                CASE WHEN a.Status = N'Làm thêm giờ' THEN
                     -- Requires calculation based on standard hours vs actual hours worked
                     CASE WHEN a.CheckInTime IS NOT NULL AND a.CheckOutTime IS NOT NULL THEN
                        GREATEST(0, CAST(DATEDIFF(MINUTE, a.CheckInTime, a.CheckOutTime) / 60.0 - 8.0 AS DECIMAL(5,2))) -- Assuming 8 hour standard day
                     ELSE 0 END
                     ELSE 0
                END AS overtime_hours,
                0 AS is_early_leave, -- Requires comparison to standard shift end time
                a.Status
            FROM nhansucongty.dbo.Attendance a
            INNER JOIN hrms_warehouse.dbo.dim_employees de ON a.EmployeeID = de.employee_id
            INNER JOIN hrms_warehouse.dbo.dim_date dd ON a.Date = dd.date
            WHERE a.Date >= @CutoffDate -- Apply filter for incremental load if @LoadRecentDays is specified
        ) AS Source
        ON Target.attendance_id = Source.AttendanceID -- Match on source primary key
        WHEN MATCHED THEN
            UPDATE SET
                Target.employee_sk = Source.employee_sk,
                Target.employee_name = Source.employee_name,
                Target.department_name = Source.department_name,
                Target.position_name = Source.position_name,
                Target.date_sk = Source.date_sk,
                Target.check_in_time = Source.CheckInTime,
                Target.check_out_time = Source.CheckOutTime,
                Target.hours_worked = Source.hours_worked,
                Target.is_late = Source.is_late,
                Target.overtime_hours = Source.overtime_hours,
                Target.is_early_leave = Source.is_early_leave,
                Target.status = Source.Status
        WHEN NOT MATCHED BY Target THEN
            INSERT (attendance_id, employee_sk, employee_name, department_name, position_name, date_sk,
                    check_in_time, check_out_time, hours_worked, is_late, overtime_hours, is_early_leave, status)
            VALUES (Source.AttendanceID, Source.employee_sk, Source.employee_name, Source.department_name, Source.position_name, Source.date_sk,
                    Source.CheckInTime, Source.CheckOutTime, Source.hours_worked, Source.is_late, Source.overtime_hours, Source.is_early_leave, Source.Status);
        -- WHEN NOT MATCHED BY SOURCE AND Target.date_sk IN (SELECT date_sk FROM hrms_warehouse.dbo.dim_date WHERE date >= @CutoffDate) THEN DELETE; -- Optional: Delete old records if doing full refresh for the period

        COMMIT TRANSACTION;
        PRINT 'Successfully merged data into fact_attendance.';
    END TRY
    BEGIN CATCH
         IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
        PRINT 'Error in sp_Load_fact_attendance: ' + ERROR_MESSAGE();
        THROW;
    END CATCH;
     PRINT 'Finished sp_Load_fact_attendance.';
END;
GO

-- =============================================
-- SP for fact_salary
-- =============================================
CREATE OR ALTER PROCEDURE sp_Load_fact_salary
AS
BEGIN
    SET NOCOUNT ON;
    PRINT 'Starting sp_Load_fact_salary...';

    BEGIN TRY
        BEGIN TRANSACTION;

        MERGE INTO hrms_warehouse.dbo.fact_salary AS Target
        USING (
            SELECT
                s.SalaryID,
                de.employee_sk,
                de.full_name AS employee_name,
                de.department_name,
                de.position_name,
                dd.date_sk, -- Map to the first day of the month
                s.BasicSalary,
                s.Allowance,
                s.Deductions,
                -- Use NetSalary from source if available, otherwise calculate
                ISNULL(s.NetSalary, s.BasicSalary + s.Allowance - s.Deductions) AS total_salary,
                'Chưa thanh toán' AS payment_status -- Default value from warehouse schema
            FROM nhansucongty.dbo.Salary s
            INNER JOIN hrms_warehouse.dbo.dim_employees de ON s.EmployeeID = de.employee_id
            -- Construct the first day of the month to join with dim_date
            INNER JOIN hrms_warehouse.dbo.dim_date dd ON dd.date = DATEFROMPARTS(s.Year, s.Month, 1)
        ) AS Source
        ON Target.salary_id = Source.SalaryID
        WHEN MATCHED THEN
            UPDATE SET
                Target.employee_sk = Source.employee_sk,
                Target.employee_name = Source.employee_name,
                Target.department_name = Source.department_name,
                Target.position_name = Source.position_name,
                Target.date_sk = Source.date_sk,
                Target.basic_salary = Source.BasicSalary,
                Target.allowance = Source.Allowance,
                Target.deductions = Source.Deductions,
                Target.total_salary = Source.total_salary
                -- Keep existing payment_status unless logic dictates otherwise
        WHEN NOT MATCHED BY Target THEN
            INSERT (salary_id, employee_sk, employee_name, department_name, position_name, date_sk,
                    basic_salary, allowance, deductions, total_salary, payment_status)
            VALUES (Source.SalaryID, Source.employee_sk, Source.employee_name, Source.department_name, Source.position_name, Source.date_sk,
                    Source.BasicSalary, Source.Allowance, Source.Deductions, Source.total_salary, Source.payment_status);

        COMMIT TRANSACTION;
        PRINT 'Successfully merged data into fact_salary.';
    END TRY
    BEGIN CATCH
         IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
        PRINT 'Error in sp_Load_fact_salary: ' + ERROR_MESSAGE();
        THROW;
    END CATCH;
    PRINT 'Finished sp_Load_fact_salary.';
END;
GO


-- =============================================
-- SP for fact_leave_balance
-- =============================================
CREATE OR ALTER PROCEDURE sp_Load_fact_leave_balance
AS
BEGIN
    SET NOCOUNT ON;
    PRINT 'Starting sp_Load_fact_leave_balance...';

    BEGIN TRY
        BEGIN TRANSACTION;

        MERGE INTO hrms_warehouse.dbo.fact_leave_balance AS Target
        USING (
            SELECT
                lb.LeaveBalanceID,
                de.employee_sk,
                de.full_name AS employee_name,
                de.department_name,
                de.position_name,
                'Nghỉ năm' AS leave_type, -- Default value
                 dd.date_sk, -- Map to the first day of the year
                 'Năm' AS granularity, -- Default value
                 lb.TotalLeaveDays,
                 lb.UsedLeaveDays,
                 -- Use Remaining from source if available, otherwise calculate
                 ISNULL(lb.RemainingLeaveDays, lb.TotalLeaveDays - lb.UsedLeaveDays) AS remaining_leave_days
            FROM nhansucongty.dbo.LeaveBalances lb
            INNER JOIN hrms_warehouse.dbo.dim_employees de ON lb.EmployeeID = de.employee_id
             -- Construct the first day of the year to join with dim_date
            INNER JOIN hrms_warehouse.dbo.dim_date dd ON dd.date = DATEFROMPARTS(lb.Year, 1, 1)
        ) AS Source
        ON Target.leave_balance_id = Source.LeaveBalanceID -- Match on source key
        WHEN MATCHED THEN
            UPDATE SET
                Target.employee_sk = Source.employee_sk,
                Target.employee_name = Source.employee_name,
                Target.department_name = Source.department_name,
                Target.position_name = Source.position_name,
                Target.leave_type = Source.leave_type,
                Target.date_sk = Source.date_sk,
                Target.granularity = Source.granularity,
                Target.total_leave_days = Source.TotalLeaveDays,
                Target.used_leave_days = Source.UsedLeaveDays,
                Target.remaining_leave_days = Source.remaining_leave_days
        WHEN NOT MATCHED BY Target THEN
            INSERT (leave_balance_id, employee_sk, employee_name, department_name, position_name,
                    leave_type, date_sk, granularity, total_leave_days, used_leave_days, remaining_leave_days)
            VALUES (Source.LeaveBalanceID, Source.employee_sk, Source.employee_name, Source.department_name, Source.position_name,
                    Source.leave_type, Source.date_sk, Source.granularity, Source.TotalLeaveDays, Source.UsedLeaveDays, Source.remaining_leave_days);

        COMMIT TRANSACTION;
        PRINT 'Successfully merged data into fact_leave_balance.';
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
        PRINT 'Error in sp_Load_fact_leave_balance: ' + ERROR_MESSAGE();
        THROW;
    END CATCH;
    PRINT 'Finished sp_Load_fact_leave_balance.';
END;
GO

-- =============================================
-- SP for fact_work_trips
-- =============================================
CREATE OR ALTER PROCEDURE sp_Load_fact_work_trips
AS
BEGIN
    SET NOCOUNT ON;
     PRINT 'Starting sp_Load_fact_work_trips...';

    BEGIN TRY
        BEGIN TRANSACTION;

        MERGE INTO hrms_warehouse.dbo.fact_work_trips AS Target
        USING (
            SELECT
                wt.RequestID AS work_trip_id,
                de.employee_sk,
                de.full_name AS employee_name,
                de.department_name,
                de.position_name,
                dd_start.date_sk AS start_date_sk,
                dd_end.date_sk AS end_date_sk,
                CASE WHEN wt.StartDate IS NOT NULL AND wt.EndDate IS NOT NULL THEN DATEDIFF(DAY, wt.StartDate, wt.EndDate) + 1 ELSE NULL END AS trip_duration, -- Include start/end day
                wt.Destination,
                wt.Purpose,
                0 AS total_cost, -- Source does not have cost info
                wt.Status
            FROM nhansucongty.dbo.WorkTripRequests wt
            INNER JOIN hrms_warehouse.dbo.dim_employees de ON wt.EmployeeID = de.employee_id
            LEFT JOIN hrms_warehouse.dbo.dim_date dd_start ON wt.StartDate = dd_start.date
            LEFT JOIN hrms_warehouse.dbo.dim_date dd_end ON wt.EndDate = dd_end.date
        ) AS Source
        ON Target.work_trip_id = Source.work_trip_id
        WHEN MATCHED THEN
            UPDATE SET
                Target.employee_sk = Source.employee_sk,
                Target.employee_name = Source.employee_name,
                Target.department_name = Source.department_name,
                Target.position_name = Source.position_name,
                Target.start_date_sk = Source.start_date_sk,
                Target.end_date_sk = Source.end_date_sk,
                Target.trip_duration = Source.trip_duration,
                Target.destination = Source.Destination,
                Target.purpose = Source.Purpose,
                Target.total_cost = Source.total_cost,
                Target.status = Source.Status
        WHEN NOT MATCHED BY Target THEN
            INSERT (work_trip_id, employee_sk, employee_name, department_name, position_name,
                    start_date_sk, end_date_sk, trip_duration, destination, purpose, total_cost, status)
            VALUES (Source.work_trip_id, Source.employee_sk, Source.employee_name, Source.department_name, Source.position_name,
                    Source.start_date_sk, Source.end_date_sk, Source.trip_duration, Source.Destination, Source.Purpose, Source.total_cost, Source.Status);

        COMMIT TRANSACTION;
         PRINT 'Successfully merged data into fact_work_trips.';
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
        PRINT 'Error in sp_Load_fact_work_trips: ' + ERROR_MESSAGE();
        THROW;
    END CATCH;
    PRINT 'Finished sp_Load_fact_work_trips.';
END;
GO

-- =============================================
-- SP for fact_recruitment_plan
-- =============================================
CREATE OR ALTER PROCEDURE sp_Load_fact_recruitment_plan
AS
BEGIN
    SET NOCOUNT ON;
    PRINT 'Starting sp_Load_fact_recruitment_plan...';

    BEGIN TRY
        BEGIN TRANSACTION;

        MERGE INTO hrms_warehouse.dbo.fact_recruitment_plan AS Target
        USING (
            SELECT
                rp.PlanID AS recruitment_id,
                dp.position_sk,
                dp.position_name,
                dd.department_sk,
                dd.department_name,
                dd_start.date_sk AS start_date_sk,
                dd_end.date_sk AS end_date_sk,
                rp.Quantity,
                CASE WHEN rp.StartDate IS NOT NULL AND rp.EndDate IS NOT NULL THEN DATEDIFF(DAY, rp.StartDate, rp.EndDate) + 1 ELSE NULL END AS recruitment_duration,
                -- Remaining positions calculation is complex, depends on applications.
                -- Defaulting to Quantity here, maybe update later based on fact_application?
                rp.Quantity AS remaining_positions
            FROM nhansucongty.dbo.RecruitmentPlans rp
            LEFT JOIN hrms_warehouse.dbo.dim_positions dp ON rp.PositionID = dp.position_id
            LEFT JOIN hrms_warehouse.dbo.dim_departments dd ON rp.DepartmentID = dd.department_id
            LEFT JOIN hrms_warehouse.dbo.dim_date dd_start ON rp.StartDate = dd_start.date
            LEFT JOIN hrms_warehouse.dbo.dim_date dd_end ON rp.EndDate = dd_end.date
        ) AS Source
        ON Target.recruitment_id = Source.recruitment_id
        WHEN MATCHED THEN
            UPDATE SET
                Target.position_sk = Source.position_sk,
                Target.position_name = Source.position_name,
                Target.department_sk = Source.department_sk,
                Target.department_name = Source.department_name,
                Target.start_date_sk = Source.start_date_sk,
                Target.end_date_sk = Source.end_date_sk,
                Target.quantity = Source.Quantity,
                Target.recruitment_duration = Source.recruitment_duration,
                Target.remaining_positions = Source.remaining_positions -- Or potentially update based on applications loaded
        WHEN NOT MATCHED BY Target THEN
            INSERT (recruitment_id, position_sk, position_name, department_sk, department_name,
                    start_date_sk, end_date_sk, quantity, recruitment_duration, remaining_positions)
            VALUES (Source.recruitment_id, Source.position_sk, Source.position_name, Source.department_sk, Source.department_name,
                    Source.start_date_sk, Source.end_date_sk, Source.Quantity, Source.recruitment_duration, Source.remaining_positions);

        COMMIT TRANSACTION;
        PRINT 'Successfully merged data into fact_recruitment_plan.';
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
        PRINT 'Error in sp_Load_fact_recruitment_plan: ' + ERROR_MESSAGE();
        THROW;
    END CATCH;
    PRINT 'Finished sp_Load_fact_recruitment_plan.';
END;
GO

-- =============================================
-- SP for fact_application
-- =============================================
CREATE OR ALTER PROCEDURE sp_Load_fact_application
AS
BEGIN
    SET NOCOUNT ON;
    PRINT 'Starting sp_Load_fact_application...';
    DECLARE @DefaultDate DATE = '1900-01-01'; -- Fallback if needed

    BEGIN TRY
        BEGIN TRANSACTION;

        MERGE INTO hrms_warehouse.dbo.fact_application AS Target
        USING (
            SELECT
                a.ApplicantID AS applicant_id,
                frp.recruitment_sk,
                 -- Source 'Applicants' lacks an application date. Using Plan StartDate as proxy. Needs verification!
                ISNULL(frp.start_date_sk, d_default.date_sk) AS application_date_sk,
                frp.position_name,
                frp.department_name,
                a.Status
            FROM nhansucongty.dbo.Applicants a
            INNER JOIN hrms_warehouse.dbo.fact_recruitment_plan frp ON a.PlanID = frp.recruitment_id
            LEFT JOIN hrms_warehouse.dbo.dim_date d_default ON d_default.date = @DefaultDate -- Join for default date_sk if needed
             -- WHERE a.ApplicationDate >= @CutoffDate -- Add date filter if source had application date & incremental load needed
        ) AS Source
        ON Target.applicant_id = Source.applicant_id -- Match on source key
        WHEN MATCHED THEN
            UPDATE SET
                Target.recruitment_sk = Source.recruitment_sk,
                Target.application_date_sk = Source.application_date_sk,
                Target.position_name = Source.position_name,
                Target.department_name = Source.department_name,
                Target.status = Source.Status
        WHEN NOT MATCHED BY Target THEN
            INSERT (applicant_id, recruitment_sk, application_date_sk, position_name, department_name, status)
            VALUES (Source.applicant_id, Source.recruitment_sk, Source.application_date_sk, Source.position_name, Source.department_name, Source.Status);

        COMMIT TRANSACTION;
         PRINT 'Successfully merged data into fact_application (Warning: Used plan start date as proxy for application date).';
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
        PRINT 'Error in sp_Load_fact_application: ' + ERROR_MESSAGE();
        THROW;
    END CATCH;
    PRINT 'Finished sp_Load_fact_application.';
END;
GO

-- =============================================
-- SP for fact_registrations
-- =============================================
CREATE OR ALTER PROCEDURE sp_Load_fact_registrations
AS
BEGIN
    SET NOCOUNT ON;
    PRINT 'Starting sp_Load_fact_registrations...';

    BEGIN TRY
        BEGIN TRANSACTION;

        MERGE INTO hrms_warehouse.dbo.fact_registrations AS Target
        USING (
            SELECT
                r.RegistrationID AS registration_id,
                de_req.employee_sk,
                de_req.full_name AS employee_name,
                de_req.department_name,
                de_app.full_name AS approved_by_name,
                r.RegistrationType,
                dd_req.date_sk AS request_date_sk,
                NULL AS registration_duration, -- Source does not have duration info
                r.Status,
                de_app.employee_sk AS approved_by_sk
            FROM nhansucongty.dbo.Registrations r
            INNER JOIN hrms_warehouse.dbo.dim_employees de_req ON r.EmployeeID = de_req.employee_id
            LEFT JOIN hrms_warehouse.dbo.dim_employees de_app ON r.ApprovedBy = de_app.employee_id -- Left join as ApprovedBy can be NULL
            LEFT JOIN hrms_warehouse.dbo.dim_date dd_req ON r.RequestDate = dd_req.date
        ) AS Source
        ON Target.registration_id = Source.registration_id
        WHEN MATCHED THEN
            UPDATE SET
                Target.employee_sk = Source.employee_sk,
                Target.employee_name = Source.employee_name,
                Target.department_name = Source.department_name,
                Target.approved_by_name = Source.approved_by_name,
                Target.registration_type = Source.RegistrationType,
                Target.request_date_sk = Source.request_date_sk,
                Target.registration_duration = Source.registration_duration,
                Target.status = Source.Status,
                Target.approved_by_sk = Source.approved_by_sk
        WHEN NOT MATCHED BY Target THEN
            INSERT (registration_id, employee_sk, employee_name, department_name, approved_by_name,
                    registration_type, request_date_sk, registration_duration, status, approved_by_sk)
            VALUES (Source.registration_id, Source.employee_sk, Source.employee_name, Source.department_name, Source.approved_by_name,
                    Source.RegistrationType, Source.request_date_sk, Source.registration_duration, Source.Status, Source.approved_by_sk);

        COMMIT TRANSACTION;
         PRINT 'Successfully merged data into fact_registrations (Warning: Registration duration is NULL).';
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
        PRINT 'Error in sp_Load_fact_registrations: ' + ERROR_MESSAGE();
        THROW;
    END CATCH;
    PRINT 'Finished sp_Load_fact_registrations.';
END;
GO

-- =============================================
-- SP for fact_decision
-- =============================================
CREATE OR ALTER PROCEDURE sp_Load_fact_decision
AS
BEGIN
    SET NOCOUNT ON;
     PRINT 'Starting sp_Load_fact_decision...';

    BEGIN TRY
        BEGIN TRANSACTION;

        MERGE INTO hrms_warehouse.dbo.fact_decision AS Target
        USING (
            SELECT
                d.DecisionID AS decision_id,
                de.employee_sk,
                de.full_name AS employee_name,
                de.department_name,
                dd_dec.date_sk AS decision_date_sk,
                d.DecisionType,
                d.Details AS decision_details,
                d.DecisionDate AS decision_effective_date, -- Using DecisionDate as EffectiveDate
                NULL AS decision_expiry_date -- Source does not have expiry info
            FROM nhansucongty.dbo.Decisions d
            INNER JOIN hrms_warehouse.dbo.dim_employees de ON d.EmployeeID = de.employee_id
            LEFT JOIN hrms_warehouse.dbo.dim_date dd_dec ON d.DecisionDate = dd_dec.date
        ) AS Source
        ON Target.decision_id = Source.decision_id
        WHEN MATCHED THEN
            UPDATE SET
                Target.employee_sk = Source.employee_sk,
                Target.employee_name = Source.employee_name,
                Target.department_name = Source.department_name,
                Target.decision_date_sk = Source.decision_date_sk,
                Target.decision_type = Source.DecisionType,
                Target.decision_details = Source.decision_details,
                Target.decision_effective_date = Source.decision_effective_date,
                Target.decision_expiry_date = Source.decision_expiry_date
        WHEN NOT MATCHED BY Target THEN
            INSERT (decision_id, employee_sk, employee_name, department_name, decision_date_sk,
                    decision_type, decision_details, decision_effective_date, decision_expiry_date)
            VALUES (Source.decision_id, Source.employee_sk, Source.employee_name, Source.department_name, Source.decision_date_sk,
                    Source.DecisionType, Source.decision_details, Source.decision_effective_date, Source.decision_expiry_date);

        COMMIT TRANSACTION;
        PRINT 'Successfully merged data into fact_decision (Warning: Used DecisionDate as effective date, expiry date is NULL).';
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
        PRINT 'Error in sp_Load_fact_decision: ' + ERROR_MESSAGE();
        THROW;
    END CATCH;
    PRINT 'Finished sp_Load_fact_decision.';
END;
GO

-- =============================================
-- Master ETL Procedure
-- =============================================
CREATE OR ALTER PROCEDURE sp_Run_ETL_hrms_warehouse
    @LoadRecentAttendanceDays INT = NULL -- Pass parameter to attendance load if needed
AS
BEGIN
    SET NOCOUNT ON;
    PRINT 'Starting Master ETL Process for hrms_warehouse at ' + CONVERT(VARCHAR, GETDATE(), 120);
    DECLARE @StartTime DATETIME = GETDATE();

    BEGIN TRY
        -- 1. Load Dimensions (Order matters due to dependencies)
        PRINT '--- Loading Dimensions ---';
        EXEC sp_Load_dim_departments;
        EXEC sp_Load_dim_positions; -- Depends on Departments
        EXEC sp_Load_dim_employees; -- Depends on Departments, Positions
        EXEC sp_Update_dim_departments_Manager; -- Depends on Employees, Departments

        -- 2. Load Facts (Can often run in parallel if resources allow, but sequentially here)
        PRINT '--- Loading Facts ---';
        EXEC sp_Load_fact_attendance @LoadRecentDays = @LoadRecentAttendanceDays; -- Depends on Employees, Date
        EXEC sp_Load_fact_salary; -- Depends on Employees, Date
        EXEC sp_Load_fact_leave_balance; -- Depends on Employees, Date
        EXEC sp_Load_fact_work_trips; -- Depends on Employees, Date
        EXEC sp_Load_fact_recruitment_plan; -- Depends on Positions, Departments, Date
        EXEC sp_Load_fact_application; -- Depends on Recruitment Plan, Date
        EXEC sp_Load_fact_registrations; -- Depends on Employees, Date
        EXEC sp_Load_fact_decision; -- Depends on Employees, Date

        DECLARE @EndTime DATETIME = GETDATE();
        PRINT 'Finished Master ETL Process for hrms_warehouse at ' + CONVERT(VARCHAR, GETDATE(), 120);
        PRINT 'Total Duration: ' + CAST(DATEDIFF(SECOND, @StartTime, @EndTime) AS VARCHAR) + ' seconds.';

    END TRY
    BEGIN CATCH
        PRINT '!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!';
        PRINT '!!! Master ETL Process FAILED at ' + CONVERT(VARCHAR, GETDATE(), 120);
        PRINT '!!! Error: ' + ERROR_MESSAGE();
        PRINT '!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!';
        -- Consider sending an alert/email here
        THROW; -- Re-throw error to make SQL Agent Job aware of failure
    END CATCH;
END;
GO
