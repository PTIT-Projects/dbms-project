USE [hrms_warehouse];
GO

CREATE OR ALTER PROCEDURE sp_ETL_HRMS_Warehouse
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        BEGIN TRANSACTION;

        -- 1. POPULATE dim_date (CRITICAL FOR FACT TABLES)
        DECLARE @StartDate DATE = '2020-01-01', @EndDate DATE = '2030-12-31'
        WHILE @StartDate <= @EndDate
        BEGIN
            IF NOT EXISTS (SELECT 1 FROM dim_date WHERE date = @StartDate)
            BEGIN
                INSERT INTO dim_date (date_sk, date, year, month, day, week, quarter)
                VALUES (
                    CONVERT(INT, CONVERT(VARCHAR, @StartDate, 112)), -- YYYYMMDD format as surrogate key
                    @StartDate,
                    YEAR(@StartDate),
                    MONTH(@StartDate),
                    DAY(@StartDate),
                    DATEPART(WEEK, @StartDate),
                    DATEPART(QUARTER, @StartDate)
                )
            END
            SET @StartDate = DATEADD(DAY, 1, @StartDate)
        END

        -- 2. dim_departments: Upsert (insert new, update changed)
        MERGE dim_departments AS target
        USING (
            SELECT DepartmentID, DepartmentName
            FROM nhansucongty.dbo.Departments
        ) AS source
        ON target.department_id = source.DepartmentID
        WHEN MATCHED AND target.department_name <> source.DepartmentName THEN
            UPDATE SET target.department_name = source.DepartmentName
        WHEN NOT MATCHED BY TARGET THEN
            INSERT (department_sk, department_id, department_name, manager_sk, manager_name)
            VALUES (source.DepartmentID, source.DepartmentID, source.DepartmentName, NULL, NULL);

        -- 3. dim_positions: Upsert
        MERGE dim_positions AS target
        USING (
            SELECT p.PositionID, p.PositionName, p.DepartmentID, d.DepartmentName
            FROM nhansucongty.dbo.Positions p
            JOIN nhansucongty.dbo.Departments d ON p.DepartmentID = d.DepartmentID
        ) AS source
        ON target.position_id = source.PositionID
        WHEN MATCHED AND (target.position_name <> source.PositionName OR target.department_sk <> source.DepartmentID) THEN
            UPDATE SET 
                target.position_name = source.PositionName,
                target.department_sk = source.DepartmentID,
                target.department_name = source.DepartmentName
        WHEN NOT MATCHED BY TARGET THEN
            INSERT (position_sk, position_id, position_name, department_sk, department_name)
            VALUES (source.PositionID, source.PositionID, source.PositionName, source.DepartmentID, source.DepartmentName);

        -- 4. dim_employees: Upsert
		MERGE dim_employees AS target
		USING (
			SELECT
				e.EmployeeID, e.FullName, e.DateOfBirth,
				CASE
					WHEN e.Gender = N'Nam' THEN 'M' 
					WHEN e.Gender = N'Nữ' THEN 'F'  
					ELSE 'O'                      
				END AS Gender,
				e.Address, e.Phone, e.Email,
				e.DepartmentID, d.DepartmentName, e.PositionID, p.PositionName,
				DATEDIFF(YEAR, e.DateOfBirth, GETDATE()) AS age, e.HireDate,
				c.ContractType, c.StartDate, c.EndDate,
				DATEDIFF(DAY, c.StartDate, ISNULL(c.EndDate, GETDATE())) AS contract_duration,
				DATEDIFF(YEAR, e.HireDate, GETDATE()) AS total_years_worked,
				i.InsuranceNumber, i.InsuranceType, i.StartDate AS insurance_start_date, i.EndDate AS insurance_end_date,
				DATEDIFF(DAY, i.StartDate, ISNULL(i.EndDate, GETDATE())) AS insurance_duration
			FROM nhansucongty.dbo.Employees e
			LEFT JOIN nhansucongty.dbo.Departments d ON e.DepartmentID = d.DepartmentID
			LEFT JOIN nhansucongty.dbo.Positions p ON e.PositionID = p.PositionID
			OUTER APPLY (
				SELECT TOP 1 c.* FROM nhansucongty.dbo.Contracts c
				WHERE c.EmployeeID = e.EmployeeID
				ORDER BY c.StartDate DESC
			) c
			OUTER APPLY (
				SELECT TOP 1 i.* FROM nhansucongty.dbo.Insurance i
				WHERE i.EmployeeID = e.EmployeeID
				ORDER BY i.StartDate DESC
			) i
		) AS source
		ON target.employee_id = source.EmployeeID
		WHEN MATCHED THEN
			UPDATE SET
				target.full_name = source.FullName,
				target.date_of_birth = source.DateOfBirth,
				target.gender = source.Gender,
				target.address = source.Address,
				target.phone = source.Phone,
				target.email = source.Email,
				target.department_sk = source.DepartmentID,
				target.department_name = source.DepartmentName,
				target.position_sk = source.PositionID,
				target.position_name = source.PositionName,
				target.age = source.age,
				target.hire_date = source.HireDate,
				target.current_contract_type = source.ContractType,
				target.contract_start_date = source.StartDate,
				target.contract_end_date = source.EndDate,
				target.contract_duration = source.contract_duration,
				target.total_years_worked = source.total_years_worked,
				target.insurance_number = source.InsuranceNumber,
				target.insurance_type = source.InsuranceType,
				target.insurance_start_date = source.insurance_start_date,
				target.insurance_end_date = source.insurance_end_date,
				target.insurance_duration = source.insurance_duration
		WHEN NOT MATCHED BY TARGET THEN
			INSERT (
				employee_sk, employee_id, full_name, date_of_birth, gender, address, phone, email,
				department_sk, department_name, position_sk, position_name, age, hire_date,
				current_contract_type, contract_start_date, contract_end_date, contract_duration,
				total_years_worked, insurance_number, insurance_type, insurance_start_date,
				insurance_end_date, insurance_duration
			)
			VALUES (
				source.EmployeeID, source.EmployeeID, source.FullName, source.DateOfBirth, source.Gender,
				source.Address, source.Phone, source.Email,
				source.DepartmentID, source.DepartmentName, source.PositionID, source.PositionName, source.age, source.HireDate,
				source.ContractType, source.StartDate, source.EndDate, source.contract_duration,
				source.total_years_worked, source.InsuranceNumber, source.InsuranceType, source.insurance_start_date,
				source.insurance_end_date, source.insurance_duration
			);

        -- 5. Update manager_sk, manager_name in dim_departments
        UPDATE d
        SET d.manager_sk = e.employee_sk, d.manager_name = e.full_name
        FROM dim_departments d
        JOIN nhansucongty.dbo.DepartmentManager dm ON d.department_id = dm.DepartmentID
        JOIN dim_employees e ON dm.ManagerID = e.employee_id;

        -- 6. fact_attendance: ETL
        MERGE fact_attendance AS target
        USING (
            SELECT
                a.AttendanceID,
                e.employee_sk,
                e.full_name AS employee_name,
                e.department_name,
                e.position_name,
                CONVERT(INT, CONVERT(VARCHAR, a.Date, 112)) AS date_sk,
                a.CheckInTime,
                a.CheckOutTime,
                -- Calculate hours worked
                CASE 
                    WHEN a.CheckInTime IS NOT NULL AND a.CheckOutTime IS NOT NULL 
                    THEN DATEDIFF(MINUTE, a.CheckInTime, a.CheckOutTime) / 60.0 
                    ELSE 0 
                END AS hours_worked,
                -- Is late (assuming 8:30 AM is start time)
                CASE WHEN a.CheckInTime > '08:30:00' THEN 1 ELSE 0 END AS is_late,
                -- Overtime hours (assuming 5:30 PM is end time)
                CASE 
                    WHEN a.CheckOutTime > '17:30:00' 
                    THEN DATEDIFF(MINUTE, '17:30:00', a.CheckOutTime) / 60.0 
                    ELSE 0 
                END AS overtime_hours,
                -- Early leave (assuming 5:30 PM is end time)
                CASE WHEN a.CheckOutTime < '17:30:00' THEN 1 ELSE 0 END AS is_early_leave,
                a.Status
            FROM nhansucongty.dbo.Attendance a
            JOIN dim_employees e ON a.EmployeeID = e.employee_id
            WHERE CONVERT(INT, CONVERT(VARCHAR, a.Date, 112)) IN (SELECT date_sk FROM dim_date)
        ) AS source
        ON target.attendance_id = source.AttendanceID
        WHEN MATCHED THEN
            UPDATE SET
                target.employee_sk = source.employee_sk,
                target.employee_name = source.employee_name,
                target.department_name = source.department_name,
                target.position_name = source.position_name,
                target.date_sk = source.date_sk,
                target.check_in_time = source.CheckInTime,
                target.check_out_time = source.CheckOutTime,
                target.hours_worked = source.hours_worked,
                target.is_late = source.is_late,
                target.overtime_hours = source.overtime_hours,
                target.is_early_leave = source.is_early_leave,
                target.status = source.Status
        WHEN NOT MATCHED BY TARGET THEN
            INSERT (
                attendance_sk, attendance_id, employee_sk, employee_name, department_name, position_name,
                date_sk, check_in_time, check_out_time, hours_worked, is_late,
                overtime_hours, is_early_leave, status
            )
            VALUES (
                source.AttendanceID, source.AttendanceID, source.employee_sk, source.employee_name, 
                source.department_name, source.position_name, source.date_sk, source.CheckInTime,
                source.CheckOutTime, source.hours_worked, source.is_late,
                source.overtime_hours, source.is_early_leave, source.Status
            );
            
        -- 7. fact_salary: ETL
        MERGE fact_salary AS target
        USING (
            SELECT
                s.SalaryID,
                e.employee_sk,
                e.full_name AS employee_name,
                e.department_name,
                e.position_name,
                -- Create date_sk for first day of the month in the given year
                CONVERT(INT, CONVERT(VARCHAR, DATEFROMPARTS(s.Year, s.Month, 1), 112)) AS date_sk,
                s.BasicSalary,
                s.Allowance,
                s.Deductions,
                s.NetSalary AS total_salary,
                N'Đã thanh toán' AS payment_status -- Assuming all salaries in source are paid
            FROM nhansucongty.dbo.Salary s
            JOIN dim_employees e ON s.EmployeeID = e.employee_id
            WHERE CONVERT(INT, CONVERT(VARCHAR, DATEFROMPARTS(s.Year, s.Month, 1), 112)) IN (SELECT date_sk FROM dim_date)
        ) AS source
        ON target.salary_id = source.SalaryID
        WHEN MATCHED THEN
            UPDATE SET
                target.employee_sk = source.employee_sk,
                target.employee_name = source.employee_name,
                target.department_name = source.department_name,
                target.position_name = source.position_name,
                target.date_sk = source.date_sk,
                target.basic_salary = source.BasicSalary,
                target.allowance = source.Allowance,
                target.deductions = source.Deductions,
                target.total_salary = source.total_salary,
                target.payment_status = source.payment_status
        WHEN NOT MATCHED BY TARGET THEN
            INSERT (
                salary_sk, salary_id, employee_sk, employee_name, department_name, position_name,
                date_sk, basic_salary, allowance, deductions, total_salary, payment_status
            )
            VALUES (
                source.SalaryID, source.SalaryID, source.employee_sk, source.employee_name,
                source.department_name, source.position_name, source.date_sk,
                source.BasicSalary, source.Allowance, source.Deductions,
                source.total_salary, source.payment_status
            );

        -- 8. fact_leave_balance: ETL
        MERGE fact_leave_balance AS target
        USING (
            SELECT
                lb.LeaveBalanceID,
                e.employee_sk,
                e.full_name AS employee_name,
                e.department_name,
                e.position_name,
                N'Nghỉ năm' AS leave_type,
                CONVERT(INT, CONVERT(VARCHAR, DATEFROMPARTS(lb.Year, 1, 1), 112)) AS date_sk,
                N'Năm' AS granularity,
                lb.TotalLeaveDays,
                lb.UsedLeaveDays,
                lb.RemainingLeaveDays
            FROM nhansucongty.dbo.LeaveBalances lb
            JOIN dim_employees e ON lb.EmployeeID = e.employee_id
            WHERE CONVERT(INT, CONVERT(VARCHAR, DATEFROMPARTS(lb.Year, 1, 1), 112)) IN (SELECT date_sk FROM dim_date)
        ) AS source
        ON target.leave_balance_id = source.LeaveBalanceID
        WHEN MATCHED THEN
            UPDATE SET
                target.employee_sk = source.employee_sk,
                target.employee_name = source.employee_name,
                target.department_name = source.department_name,
                target.position_name = source.position_name,
                target.leave_type = source.leave_type,
                target.date_sk = source.date_sk,
                target.granularity = source.granularity,
                target.total_leave_days = source.TotalLeaveDays,
                target.used_leave_days = source.UsedLeaveDays,
                target.remaining_leave_days = source.RemainingLeaveDays
        WHEN NOT MATCHED BY TARGET THEN
            INSERT (
                leave_balance_sk, leave_balance_id, employee_sk, employee_name, department_name, position_name,
                leave_type, date_sk, granularity, total_leave_days, used_leave_days, remaining_leave_days
            )
            VALUES (
                source.LeaveBalanceID, source.LeaveBalanceID, source.employee_sk, source.employee_name,
                source.department_name, source.position_name, source.leave_type, source.date_sk,
                source.granularity, source.TotalLeaveDays, source.UsedLeaveDays, source.RemainingLeaveDays
            );

        -- 9. fact_work_trips: ETL
        MERGE fact_work_trips AS target
        USING (
            SELECT
                wt.RequestID AS work_trip_id,
                e.employee_sk,
                e.full_name AS employee_name,
                e.department_name,
                e.position_name,
                CONVERT(INT, CONVERT(VARCHAR, wt.StartDate, 112)) AS start_date_sk,
                CONVERT(INT, CONVERT(VARCHAR, wt.EndDate, 112)) AS end_date_sk,
                DATEDIFF(DAY, wt.StartDate, wt.EndDate) + 1 AS trip_duration,
                wt.Destination,
                wt.Purpose,
                CASE 
                    WHEN wt.Status = N'Đã duyệt' AND GETDATE() BETWEEN wt.StartDate AND wt.EndDate THEN N'Đang diễn ra'
                    WHEN wt.Status = N'Đã duyệt' AND GETDATE() > wt.EndDate THEN N'Hoàn thành'
                    WHEN wt.Status = N'Từ chối' THEN N'Đã hủy'
                    ELSE wt.Status
                END AS status
            FROM nhansucongty.dbo.WorkTripRequests wt
            JOIN dim_employees e ON wt.EmployeeID = e.employee_id
            WHERE 
                CONVERT(INT, CONVERT(VARCHAR, wt.StartDate, 112)) IN (SELECT date_sk FROM dim_date) AND
                CONVERT(INT, CONVERT(VARCHAR, wt.EndDate, 112)) IN (SELECT date_sk FROM dim_date)
        ) AS source
        ON target.work_trip_id = source.work_trip_id
        WHEN MATCHED THEN
            UPDATE SET
                target.employee_sk = source.employee_sk,
                target.employee_name = source.employee_name,
                target.department_name = source.department_name,
                target.position_name = source.position_name,
                target.start_date_sk = source.start_date_sk,
                target.end_date_sk = source.end_date_sk,
                target.trip_duration = source.trip_duration,
                target.destination = source.Destination,
                target.purpose = source.Purpose,
                target.status = source.status
        WHEN NOT MATCHED BY TARGET THEN
            INSERT (
                work_trip_sk, work_trip_id, employee_sk, employee_name, department_name, position_name,
                start_date_sk, end_date_sk, trip_duration, destination, purpose, status
            )
            VALUES (
                source.work_trip_id, source.work_trip_id, source.employee_sk, source.employee_name,
                source.department_name, source.position_name, source.start_date_sk, source.end_date_sk,
                source.trip_duration, source.Destination, source.Purpose, source.status
            );

        -- 10. fact_recruitment_plan: ETL
        MERGE fact_recruitment_plan AS target
        USING (
            SELECT
                rp.PlanID AS recruitment_id,
                p.position_sk,
                p.position_name,
                d.department_sk,
                d.department_name,
                CONVERT(INT, CONVERT(VARCHAR, rp.StartDate, 112)) AS start_date_sk,
                CONVERT(INT, CONVERT(VARCHAR, rp.EndDate, 112)) AS end_date_sk,
                rp.Quantity,
                DATEDIFF(DAY, rp.StartDate, rp.EndDate) + 1 AS recruitment_duration,
                rp.Quantity - ISNULL(
                    (SELECT COUNT(*) FROM nhansucongty.dbo.Applicants 
                     WHERE PlanID = rp.PlanID AND Status = N'Đã tuyển'), 0
                ) AS remaining_positions
            FROM nhansucongty.dbo.RecruitmentPlans rp
            JOIN dim_positions p ON rp.PositionID = p.position_id
            JOIN dim_departments d ON rp.DepartmentID = d.department_id
            WHERE 
                CONVERT(INT, CONVERT(VARCHAR, rp.StartDate, 112)) IN (SELECT date_sk FROM dim_date) AND
                CONVERT(INT, CONVERT(VARCHAR, rp.EndDate, 112)) IN (SELECT date_sk FROM dim_date)
        ) AS source
        ON target.recruitment_id = source.recruitment_id
        WHEN MATCHED THEN
            UPDATE SET
                target.position_sk = source.position_sk,
                target.position_name = source.position_name,
                target.department_sk = source.department_sk,
                target.department_name = source.department_name,
                target.start_date_sk = source.start_date_sk,
                target.end_date_sk = source.end_date_sk,
                target.quantity = source.Quantity,
                target.recruitment_duration = source.recruitment_duration,
                target.remaining_positions = source.remaining_positions
        WHEN NOT MATCHED BY TARGET THEN
            INSERT (
                recruitment_sk, recruitment_id, position_sk, position_name, department_sk, department_name,
                start_date_sk, end_date_sk, quantity, recruitment_duration, remaining_positions
            )
            VALUES (
                source.recruitment_id, source.recruitment_id, source.position_sk, source.position_name,
                source.department_sk, source.department_name, source.start_date_sk, source.end_date_sk,
                source.Quantity, source.recruitment_duration, source.remaining_positions
            );

        -- 11. fact_application: ETL
        MERGE fact_application AS target
        USING (
            SELECT
                a.ApplicantID AS applicant_id,
                rp.recruitment_sk,
                CONVERT(INT, CONVERT(VARCHAR, GETDATE(), 112)) AS application_date_sk, -- Using current date as application date
                rp.position_name,
                rp.department_name,
                a.Status
            FROM nhansucongty.dbo.Applicants a
            JOIN fact_recruitment_plan rp ON a.PlanID = rp.recruitment_id
            WHERE CONVERT(INT, CONVERT(VARCHAR, GETDATE(), 112)) IN (SELECT date_sk FROM dim_date)
        ) AS source
        ON target.applicant_id = source.applicant_id
        WHEN MATCHED THEN
            UPDATE SET
                target.recruitment_sk = source.recruitment_sk,
                target.application_date_sk = source.application_date_sk,
                target.position_name = source.position_name,
                target.department_name = source.department_name,
                target.status = source.Status
        WHEN NOT MATCHED BY TARGET THEN
            INSERT (
                application_sk, applicant_id, recruitment_sk, application_date_sk,
                position_name, department_name, status
            )
            VALUES (
                source.applicant_id, source.applicant_id, source.recruitment_sk, source.application_date_sk,
                source.position_name, source.department_name, source.Status
            );

        -- 12. fact_registrations: ETL
        MERGE fact_registrations AS target
        USING (
            SELECT
                r.RegistrationID,
                e.employee_sk,
                e.full_name AS employee_name,
                e.department_name,
                approver.full_name AS approved_by_name,
                r.RegistrationType,
                CONVERT(INT, CONVERT(VARCHAR, r.RequestDate, 112)) AS request_date_sk,
                -- Estimate duration based on registration type
                CASE 
                    WHEN r.RegistrationType = N'Nghỉ phép' THEN 1 -- Default 1 day leave
                    WHEN r.RegistrationType = N'Làm thêm giờ' THEN 2 -- Default 2 hours overtime
                    ELSE 0
                END AS registration_duration,
                r.Status,
                approver.employee_sk AS approved_by_sk
            FROM nhansucongty.dbo.Registrations r
            JOIN dim_employees e ON r.EmployeeID = e.employee_id
            LEFT JOIN dim_employees approver ON r.ApprovedBy = approver.employee_id
            WHERE CONVERT(INT, CONVERT(VARCHAR, r.RequestDate, 112)) IN (SELECT date_sk FROM dim_date)
        ) AS source
        ON target.registration_id = source.RegistrationID
        WHEN MATCHED THEN
            UPDATE SET
                target.employee_sk = source.employee_sk,
                target.employee_name = source.employee_name,
                target.department_name = source.department_name,
                target.approved_by_name = source.approved_by_name,
                target.registration_type = source.RegistrationType,
                target.request_date_sk = source.request_date_sk,
                target.registration_duration = source.registration_duration,
                target.status = source.Status,
                target.approved_by_sk = source.approved_by_sk
        WHEN NOT MATCHED BY TARGET THEN
            INSERT (
                registration_sk, registration_id, employee_sk, employee_name, department_name, approved_by_name,
                registration_type, request_date_sk, registration_duration, status, approved_by_sk
            )
            VALUES (
                source.RegistrationID, source.RegistrationID, source.employee_sk, source.employee_name,
                source.department_name, source.approved_by_name, source.RegistrationType, 
                source.request_date_sk, source.registration_duration, source.Status, source.approved_by_sk
            );

        -- 13. fact_decision: ETL
        MERGE fact_decision AS target
        USING (
            SELECT
                d.DecisionID,
                e.employee_sk,
                e.full_name AS employee_name,
                e.department_name,
                CONVERT(INT, CONVERT(VARCHAR, d.DecisionDate, 112)) AS decision_date_sk,
                d.DecisionType,
                d.Details AS decision_details,
                d.DecisionDate AS decision_effective_date,
                DATEADD(MONTH, 6, d.DecisionDate) AS decision_expiry_date -- Assuming decisions expire after 6 months
            FROM nhansucongty.dbo.Decisions d
            JOIN dim_employees e ON d.EmployeeID = e.employee_id
            WHERE CONVERT(INT, CONVERT(VARCHAR, d.DecisionDate, 112)) IN (SELECT date_sk FROM dim_date)
        ) AS source
        ON target.decision_id = source.DecisionID
        WHEN MATCHED THEN
            UPDATE SET
                target.employee_sk = source.employee_sk,
                target.employee_name = source.employee_name,
                target.department_name = source.department_name,
                target.decision_date_sk = source.decision_date_sk,
                target.decision_type = source.DecisionType,
                target.decision_details = source.decision_details,
                target.decision_effective_date = source.decision_effective_date,
                target.decision_expiry_date = source.decision_expiry_date
        WHEN NOT MATCHED BY TARGET THEN
            INSERT (
                decision_sk, decision_id, employee_sk, employee_name, department_name,
                decision_date_sk, decision_type, decision_details, 
                decision_effective_date, decision_expiry_date
            )
            VALUES (
                source.DecisionID, source.DecisionID, source.employee_sk, source.employee_name,
                source.department_name, source.decision_date_sk, source.DecisionType,
                source.decision_details, source.decision_effective_date, source.decision_expiry_date
            );

        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
        
        -- Log error information
        DECLARE @ErrorMessage NVARCHAR(4000) = ERROR_MESSAGE();
        DECLARE @ErrorSeverity INT = ERROR_SEVERITY();
        DECLARE @ErrorState INT = ERROR_STATE();
        
        RAISERROR(@ErrorMessage, @ErrorSeverity, @ErrorState);
    END CATCH
END
GO
