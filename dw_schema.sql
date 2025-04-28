USE [hrms_warehouse];
GO

-- 1. Stored Procedure cho dim_date
-- Bảng này thường được populate một lần hoặc định kỳ hàng năm. SP này sẽ tạo dữ liệu ngày tháng cho một khoảng thời gian nhất định.
-- SQL

DROP PROCEDURE IF EXISTS sp_populate_dim_date;
GO

CREATE PROCEDURE sp_populate_dim_date
    @StartDate DATE = '2020-01-01', -- Ngày bắt đầu populate
    @EndDate DATE = '2030-12-31' -- Ngày kết thúc populate
AS
BEGIN
    SET NOCOUNT ON;

    -- Sử dụng một CTE đệ quy để tạo chuỗi ngày
    WITH DateSequence AS (
        SELECT @StartDate AS DateValue
        UNION ALL
        SELECT DATEADD(day, 1, DateValue)
        FROM DateSequence
        WHERE DateValue < @EndDate
    )
    -- Chèn hoặc cập nhật dữ liệu vào dim_date
    MERGE dim_date AS target
    USING (
        SELECT
            CAST(FORMAT(DateValue, 'yyyyMMdd') AS INT) AS date_sk, -- Khóa surrogate dạng INT (YYYYMMDD)
            DateValue AS date,
            YEAR(DateValue) AS year,
            MONTH(DateValue) AS month,
            DAY(DateValue) AS day,
            DATEPART(week, DateValue) AS week, -- Tuần trong năm
            DATEPART(quarter, DateValue) AS quarter -- Quý trong năm
        FROM DateSequence
    ) AS source ON target.date_sk = source.date_sk
    WHEN NOT MATCHED THEN
        INSERT (date_sk, date, year, month, day, week, quarter)
        VALUES (source.date_sk, source.date, source.year, source.month, source.day, source.week, source.quarter)
    WHEN MATCHED THEN
        -- Cập nhật các thuộc tính nếu cần (thường không cần cho bảng ngày)
        UPDATE SET
            target.date = source.date,
            target.year = source.year,
            target.month = source.month,
            target.day = source.day,
            target.week = source.week,
            target.quarter = source.quarter
    -- THÊM DẤU CHẤM PHẨY TRƯỚC OPTION
    OPTION (MAXRECURSION 0); -- Cho phép đệ quy không giới hạn (cẩn thận với khoảng ngày quá lớn)

END;
GO

-- 2. Stored Procedure cho dim_departments
-- Tải dữ liệu phòng ban, bao gồm cả thông tin quản lý từ bảng DepartmentManager và tên quản lý từ Employees.
-- SQL

DROP PROCEDURE IF EXISTS sp_load_dim_departments;
GO

CREATE PROCEDURE sp_load_dim_departments
AS
BEGIN
    SET NOCOUNT ON;

    MERGE dim_departments AS target
    USING (
        SELECT
            d.DepartmentID,
            d.DepartmentName,
            dm.ManagerID -- ID quản lý từ bảng nguồn
            -- Sẽ cần join với dim_employees để lấy manager_sk và manager_name sau khi dim_employees được tải
        FROM [nhansucongty].[dbo].Departments AS d
        LEFT JOIN [nhansucongty].[dbo].DepartmentManager AS dm ON d.DepartmentID = dm.DepartmentID
    ) AS source ON target.department_id = source.DepartmentID
    WHEN NOT MATCHED THEN
        INSERT (department_id, department_name, manager_sk, manager_name)
        VALUES (source.DepartmentID, source.DepartmentName, NULL, NULL) -- Tạm thời để NULL, sẽ cập nhật sau khi dim_employees có dữ liệu
    WHEN MATCHED THEN
        -- Cập nhật thông tin phòng ban và quản lý nếu thay đổi (SCD Type 1)
        UPDATE SET
            target.department_name = source.DepartmentName;
            -- Lưu ý: Việc cập nhật manager_sk và manager_name sẽ được thực hiện SAU khi tải dim_employees

    -- Cập nhật manager_sk và manager_name sau khi dim_employees đã được tải
    -- Sử dụng LEFT JOIN để đảm bảo các phòng ban không có quản lý vẫn giữ nguyên giá trị NULL đã insert ban đầu
    UPDATE dd
    SET dd.manager_sk = de.employee_sk,
        dd.manager_name = de.full_name
    FROM dim_departments AS dd
    JOIN [nhansucongty].[dbo].DepartmentManager AS dm ON dd.department_id = dm.DepartmentID
    JOIN dim_employees AS de ON dm.ManagerID = de.employee_id;

END;
GO

-- 3. Stored Procedure cho dim_positions
-- Tải dữ liệu chức vụ và thông tin phòng ban liên quan.
-- SQL

DROP PROCEDURE IF EXISTS sp_load_dim_positions;
GO

CREATE PROCEDURE sp_load_dim_positions
AS
BEGIN
    SET NOCOUNT ON;

    MERGE dim_positions AS target
    USING (
        SELECT
            p.PositionID,
            p.PositionName,
            d.DepartmentID, -- ID phòng ban từ nguồn
            d.DepartmentName -- Tên phòng ban từ nguồn
        FROM [nhansucongty].[dbo].Positions AS p
        JOIN [nhansucongty].[dbo].Departments AS d ON p.DepartmentID = d.DepartmentID
    ) AS source ON target.position_id = source.PositionID
    WHEN NOT MATCHED THEN
        INSERT (position_id, position_name, department_sk, department_name)
        VALUES (source.PositionID, source.PositionName, NULL, source.DepartmentName) -- Tạm thời để department_sk là NULL
    WHEN MATCHED THEN
        -- Cập nhật thông tin chức vụ và tên phòng ban nếu thay đổi (SCD Type 1)
        UPDATE SET
            target.position_name = source.PositionName,
            target.department_name = source.DepartmentName;

    -- Cập nhật department_sk sau khi dim_departments đã được tải
    -- Đã sửa JOIN để kết nối đúng từ dim_positions -> source Positions -> dim_departments
    UPDATE dp
    SET dp.department_sk = dd.department_sk
    FROM dim_positions AS dp
    -- Join với bảng nguồn Positions để lấy DepartmentID
    JOIN [nhansucongty].[dbo].Positions AS sp ON dp.position_id = sp.PositionID
    -- Sau đó join với dim_departments để lấy department_sk
    JOIN dim_departments AS dd ON sp.DepartmentID = dd.department_id;

END;
GO

-- 4. Stored Procedure cho dim_employees
-- Tải dữ liệu nhân viên, bao gồm thông tin phòng ban, chức vụ, hợp đồng, bảo hiểm và các cột dẫn xuất.
-- SQL

DROP PROCEDURE IF EXISTS sp_load_dim_employees;
GO

CREATE PROCEDURE sp_load_dim_employees
AS
BEGIN
    SET NOCOUNT ON;

    -- Để xử lý hợp đồng và bảo hiểm hiện tại, ta cần tìm bản ghi mới nhất cho mỗi nhân viên
    WITH LatestContract AS (
        SELECT
            ContractID,
            EmployeeID,
            ContractType,
            StartDate,
            EndDate,
            ROW_NUMBER() OVER(PARTITION BY EmployeeID ORDER BY StartDate DESC, ContractID DESC) as rn -- Lấy hợp đồng mới nhất
        FROM [nhansucongty].[dbo].Contracts
        -- Có thể thêm điều kiện WHERE Status = N'Hiệu lực' nếu chỉ muốn lấy hợp đồng đang có hiệu lực
    ),
    LatestInsurance AS (
        SELECT
            InsuranceID,
            EmployeeID,
            InsuranceNumber,
            InsuranceType,
            StartDate,
            EndDate,
            ROW_NUMBER() OVER(PARTITION BY EmployeeID ORDER BY StartDate DESC, InsuranceID DESC) as rn -- Lấy bảo hiểm mới nhất
        FROM [nhansucongty].[dbo].Insurance
        -- Có thể thêm điều kiện WHERE EndDate >= GETDATE() nếu chỉ muốn lấy bảo hiểm còn hạn
    )
    MERGE dim_employees AS target
    USING (
        SELECT
            e.EmployeeID,
            e.FullName,
            e.DateOfBirth,
            -- Ánh xạ giới tính sang 1 ký tự
            CASE
                WHEN e.Gender = N'Nam' THEN 'M'
                WHEN e.Gender = N'Nữ' THEN 'F'
                ELSE 'O' -- Other/Khác
            END AS Gender,
            e.Address,
            e.Phone,
            e.Email,
            e.DepartmentID, -- ID phòng ban nguồn
            e.PositionID, -- ID chức vụ nguồn
            e.HireDate,
            -- Thông tin hợp đồng từ bản ghi mới nhất
            lc.ContractType,
            lc.StartDate AS ContractStartDate,
            lc.EndDate AS ContractEndDate,
            -- Thông tin bảo hiểm từ bản ghi mới nhất
            li.InsuranceNumber,
            li.InsuranceType,
            li.StartDate AS InsuranceStartDate,
            li.EndDate AS InsuranceEndDate,
            -- Tính toán tuổi (giả định tính đến ngày chạy ETL)
            DATEDIFF(year, e.DateOfBirth, GETDATE()) -
            CASE WHEN MONTH(e.DateOfBirth) > MONTH(GETDATE()) OR (MONTH(e.DateOfBirth) = MONTH(GETDATE()) AND DAY(e.DateOfBirth) > DAY(GETDATE())) THEN 1 ELSE 0 END AS Age,
            -- Tính toán số năm làm việc (đến ngày chạy ETL)
            CAST(DATEDIFF(day, e.HireDate, GETDATE()) / 365.25 AS DECIMAL(5,2)) AS TotalYearsWorked,
            -- Tính toán thời lượng hợp đồng (nếu có EndDate)
            DATEDIFF(day, lc.StartDate, lc.EndDate) AS ContractDuration,
             -- Tính toán thời lượng bảo hiểm (nếu có EndDate)
            DATEDIFF(day, li.StartDate, li.EndDate) AS InsuranceDuration

        FROM [nhansucongty].[dbo].Employees AS e
        LEFT JOIN LatestContract AS lc ON e.EmployeeID = lc.EmployeeID AND lc.rn = 1
        LEFT JOIN LatestInsurance AS li ON e.EmployeeID = li.EmployeeID AND li.rn = 1
    ) AS source ON target.employee_id = source.EmployeeID
    WHEN NOT MATCHED THEN
        INSERT (employee_id, full_name, date_of_birth, gender, address, phone, email, department_sk, department_name, position_sk, position_name, age, hire_date, current_contract_type, contract_start_date, contract_end_date, contract_duration, total_years_worked, insurance_number, insurance_type, insurance_start_date, insurance_end_date, insurance_duration)
        VALUES (
            source.EmployeeID,
            source.FullName,
            source.DateOfBirth,
            source.Gender,
            source.Address,
            source.Phone,
            source.Email,
            NULL, -- department_sk (sẽ cập nhật sau)
            NULL, -- department_name (sẽ cập nhật sau)
            NULL, -- position_sk (sẽ cập nhật sau)
            NULL, -- position_name (sẽ cập nhật sau)
            source.Age,
            source.HireDate,
            source.ContractType,
            source.ContractStartDate,
            source.ContractEndDate,
            source.ContractDuration,
            source.TotalYearsWorked,
            source.InsuranceNumber,
            source.InsuranceType,
            source.InsuranceStartDate,
            source.InsuranceEndDate,
            source.InsuranceDuration
        )
    WHEN MATCHED THEN
        -- Cập nhật thông tin nhân viên và các cột dẫn xuất (SCD Type 1)
        UPDATE SET
            target.full_name = source.FullName,
            target.date_of_birth = source.DateOfBirth,
            target.gender = source.Gender,
            target.address = source.Address,
            target.phone = source.Phone,
            target.email = source.Email,
            -- department_sk, department_name, position_sk, position_name sẽ được cập nhật sau
            target.age = source.Age,
            target.hire_date = source.HireDate,
            target.current_contract_type = source.ContractType,
            target.contract_start_date = source.ContractStartDate,
            target.contract_end_date = source.ContractEndDate,
            target.contract_duration = source.ContractDuration,
            target.total_years_worked = source.TotalYearsWorked,
            target.insurance_number = source.InsuranceNumber,
            target.insurance_type = source.InsuranceType,
            target.insurance_start_date = source.InsuranceStartDate,
            target.insurance_end_date = source.InsuranceEndDate,
            target.insurance_duration = source.InsuranceDuration;

    -- Cập nhật department_sk, department_name, position_sk, position_name sau khi các bảng chiều khác được tải
    -- Sử dụng LEFT JOIN để đảm bảo nhân viên không có phòng ban/chức vụ vẫn giữ NULL
    UPDATE de
    SET de.department_sk = dd.department_sk,
        de.department_name = dd.department_name,
        de.position_sk = dp.position_sk,
        de.position_name = dp.position_name
    FROM dim_employees AS de
    JOIN [nhansucongty].[dbo].Employees AS se ON de.employee_id = se.EmployeeID -- Join với bảng nguồn Employee
    LEFT JOIN dim_departments AS dd ON se.DepartmentID = dd.department_id -- LEFT JOIN với dim_departments
    LEFT JOIN dim_positions AS dp ON se.PositionID = dp.position_id; -- LEFT JOIN với dim_positions

END;
GO

-- 5. Stored Procedure cho fact_attendance
-- Tải dữ liệu chấm công. Cần join với dim_employees và dim_date để lấy khóa surrogate.
-- Lưu ý: Việc tính toán hours_worked, is_late, overtime_hours, is_early_leave cần dựa vào quy định ca làm việc cụ thể của công ty, điều này không có trong lược đồ nguồn. Tôi sẽ đưa ra logic tính toán giờ làm việc đơn giản và ghi chú cho các trường khác.
-- SQL

DROP PROCEDURE IF EXISTS sp_load_fact_attendance;
GO

CREATE PROCEDURE sp_load_fact_attendance
AS
BEGIN
    SET NOCOUNT ON;

    -- Tải dữ liệu mới từ nguồn vào bảng fact_attendance
    INSERT INTO fact_attendance (
        -- attendance_sk (IDENTITY column, REMOVED from insert list)
        attendance_id,
        employee_sk,
        employee_name,
        department_name,
        position_name,
        date_sk,
        check_in_time,
        check_out_time,
        hours_worked,
        is_late,
        overtime_hours,
        is_early_leave,
        status
    )
    SELECT
        a.AttendanceID,
        de.employee_sk, -- Lấy khóa surrogate từ dim_employees
        de.full_name,
        de.department_name, -- Lấy tên phòng ban từ dim_employees (đã được tải)
        de.position_name,
		dd.date_sk, -- Lấy khóa surrogate từ dim_date
        a.CheckInTime,
        a.CheckOutTime,
        -- Tính toán giờ làm việc (giả sử CheckOutTime > CheckInTime cùng ngày)
        -- Cần xử lý các trường hợp checkin/checkout qua đêm nếu có
        CAST(DATEDIFF(minute, a.CheckInTime, a.CheckOutTime) / 60.0 AS DECIMAL(5,2)),
        -- Logic tính is_late, overtime_hours, is_early_leave cần dựa vào quy định ca làm việc
        -- Ví dụ:
        -- CASE WHEN a.CheckInTime > '08:00:00' THEN 1 ELSE 0 END, -- Giả sử giờ bắt đầu là 8:00
        -- CASE WHEN a.CheckOutTime > '17:00:00' THEN CAST(DATEDIFF(minute, '17:00:00', a.CheckOutTime) / 60.0 AS DECIMAL(5,2)) ELSE 0 END, -- Giả sử giờ kết thúc là 17:00
        -- CASE WHEN a.CheckOutTime < '17:00:00' THEN 1 ELSE 0 END -- Giả sử giờ kết thúc là 17:00
        NULL, -- Placeholder cho is_late
        NULL, -- Placeholder cho overtime_hours
        NULL, -- Placeholder cho is_early_leave
        a.Status
    FROM [nhansucongty].[dbo].Attendance AS a
    JOIN dim_employees AS de ON a.EmployeeID = de.employee_id -- Join với dim_employees
    JOIN dim_date AS dd ON CAST(FORMAT(a.Date, 'yyyyMMdd') AS INT) = dd.date_sk -- Join với dim_date bằng khóa surrogate ngày
    LEFT JOIN fact_attendance AS ft ON a.AttendanceID = ft.attendance_id -- Kiểm tra bản ghi đã tồn tại chưa (chỉ insert mới)
    WHERE ft.attendance_id IS NULL; -- Chỉ chèn các bản ghi chưa có trong fact_attendance

    -- Ghi chú: Bạn cần bổ sung logic tính toán cho is_late, overtime_hours, is_early_leave
    -- dựa trên giờ bắt đầu/kết thúc ca làm việc thực tế của công ty.

END;
GO

-- 6. Stored Procedure cho fact_salary
-- Tải dữ liệu lương. Cần join với dim_employees và dim_date.
-- SQL

DROP PROCEDURE IF EXISTS sp_load_fact_salary;
GO

CREATE PROCEDURE sp_load_fact_salary
AS
BEGIN
    SET NOCOUNT ON;

    INSERT INTO fact_salary (
        -- salary_sk (IDENTITY column, REMOVED from insert list)
        salary_id,
        employee_sk,
        employee_name,
        department_name,
        position_name,
        date_sk,
        basic_salary,
        allowance,
        deductions,
        total_salary,
        payment_status
    )
    SELECT
        s.SalaryID,
        de.employee_sk, -- Lấy khóa surrogate từ dim_employees
        de.full_name,
        de.department_name, -- Lấy tên phòng ban từ dim_employees
        de.position_name, -- Lấy tên chức vụ từ dim_employees
        dd.date_sk, -- Lấy khóa surrogate từ dim_date (cho ngày đầu tháng)
        s.BasicSalary,
        s.Allowance,
        s.Deductions,
        -- Tính tổng lương
        s.BasicSalary + s.Allowance - s.Deductions,
        'Chưa thanh toán' -- Giả định trạng thái ban đầu
    FROM [nhansucongty].[dbo].Salary AS s
    JOIN dim_employees AS de ON s.EmployeeID = de.employee_id -- Join với dim_employees
    JOIN dim_date AS dd ON CAST(FORMAT(DATEFROMPARTS(s.Year, s.Month, 1), 'yyyyMMdd') AS INT) = dd.date_sk -- Join với dim_date (ngày đầu tháng)
    LEFT JOIN fact_salary AS fs ON s.SalaryID = fs.salary_id -- Kiểm tra bản ghi đã tồn tại chưa
    WHERE fs.salary_id IS NULL; -- Chỉ chèn các bản ghi chưa có

    -- Ghi chú: Trạng thái payment_status cần được cập nhật thông qua một quy trình khác
    -- khi lương thực sự được thanh toán.

END;
GO

-- 7. Stored Procedure cho fact_leave_balance
-- Tải dữ liệu số ngày nghỉ phép còn lại. Cần join với dim_employees và dim_date.
-- SQL

DROP PROCEDURE IF EXISTS sp_load_fact_leave_balance;
GO

CREATE PROCEDURE sp_load_fact_leave_balance
AS
BEGIN
    SET NOCOUNT ON;

    INSERT INTO fact_leave_balance (
        -- leave_balance_sk (IDENTITY column, REMOVED from insert list)
        leave_balance_id,
        employee_sk,
        employee_name,
        department_name,
        position_name,
        leave_type,
        date_sk,
        granularity,
        total_leave_days,
        used_leave_days,
        remaining_leave_days
    )
    SELECT
        lb.LeaveBalanceID,
        de.employee_sk, -- Lấy khóa surrogate từ dim_employees
        de.full_name,
        de.department_name, -- Lấy tên phòng ban từ dim_employees
        de.position_name, -- Lấy tên chức vụ từ dim_employees
        N'Nghỉ năm', -- Mặc định là 'Nghỉ năm' theo thiết kế bảng fact
        dd.date_sk, -- Lấy khóa surrogate từ dim_date (cho ngày đầu năm)
        N'Năm', -- Mức độ chi tiết là 'Năm'
        lb.TotalLeaveDays,
        lb.UsedLeaveDays,
        lb.RemainingLeaveDays
    FROM [nhansucongty].[dbo].LeaveBalances AS lb
    JOIN dim_employees AS de ON lb.EmployeeID = de.employee_id -- Join với dim_employees
     -- Tạo ngày đầu năm để join với dim_date
    JOIN dim_date AS dd ON CAST(FORMAT(DATEFROMPARTS(lb.Year, 1, 1), 'yyyyMMdd') AS INT) = dd.date_sk
    LEFT JOIN fact_leave_balance AS flb ON lb.LeaveBalanceID = flb.leave_balance_id -- Kiểm tra bản ghi đã tồn tại chưa
    WHERE flb.leave_balance_id IS NULL; -- Chỉ chèn các bản ghi chưa có

END;
GO

-- 8. Stored Procedure cho fact_work_trips
-- Tải dữ liệu công tác. Cần join với dim_employees và dim_date cho ngày bắt đầu và kết thúc.
-- SQL

DROP PROCEDURE IF EXISTS sp_load_fact_work_trips;
GO

CREATE PROCEDURE sp_load_fact_work_trips
AS
BEGIN
    SET NOCOUNT ON;

    INSERT INTO fact_work_trips (
        -- work_trip_sk (IDENTITY column, REMOVED from insert list)
        work_trip_id,
        employee_sk,
        employee_name,
        department_name,
        position_name,
        start_date_sk,
        end_date_sk,
        trip_duration,
        destination,
        purpose,
        total_cost, -- Giả định chưa có chi phí ở nguồn, để giá trị mặc định hoặc cần nguồn khác
        status
    )
    SELECT
        wt.RequestID,
        de.employee_sk, -- Lấy khóa surrogate từ dim_employees
        de.full_name,
        de.department_name, -- Lấy tên phòng ban từ dim_employees
        de.position_name, -- Lấy tên chức vụ từ dim_employees
        dds.date_sk, -- Khóa surrogate ngày bắt đầu
        dde.date_sk, -- Khóa surrogate ngày kết thúc
        -- Tính toán thời lượng chuyến đi (bao gồm cả ngày bắt đầu và kết thúc)
        DATEDIFF(day, wt.StartDate, wt.EndDate) + 1,
        wt.Destination,
        wt.Purpose,
        0, -- Giả định chi phí là 0 hoặc cần lấy từ nguồn khác
        wt.Status
    FROM [nhansucongty].[dbo].WorkTripRequests AS wt
    JOIN dim_employees AS de ON wt.EmployeeID = de.employee_id -- Join với dim_employees
    JOIN dim_date AS dds ON CAST(FORMAT(wt.StartDate, 'yyyyMMdd') AS INT) = dds.date_sk -- Join với dim_date cho ngày bắt đầu
    JOIN dim_date AS dde ON CAST(FORMAT(wt.EndDate, 'yyyyMMdd') AS INT) = dde.date_sk -- Join với dim_date cho ngày kết thúc
    LEFT JOIN fact_work_trips AS fwt ON wt.RequestID = fwt.work_trip_id -- Kiểm tra bản ghi đã tồn tại chưa
    WHERE fwt.work_trip_id IS NULL; -- Chỉ chèn các bản ghi chưa có

     -- Ghi chú: Trường total_cost cần được lấy từ nguồn dữ liệu chi phí công tác (nếu có)
     -- hoặc cập nhật sau khi chi phí được quyết toán.

END;
GO

-- 9. Stored Procedure cho fact_recruitment_plan
-- Tải dữ liệu kế hoạch tuyển dụng. Cần join với dim_positions, dim_departments, và dim_date.
-- Lưu ý: Trường remaining_positions trong fact cần tính toán dựa trên số lượng cần tuyển và số lượng ứng viên đã được tuyển cho kế hoạch đó.
-- SQL

DROP PROCEDURE IF EXISTS sp_load_fact_recruitment_plan;
GO

CREATE PROCEDURE sp_load_fact_recruitment_plan
AS
BEGIN
    SET NOCOUNT ON;

    -- Tính toán số lượng ứng viên 'Đã tuyển' cho mỗi kế hoạch
    WITH HiredApplicants AS (
        SELECT
            a.PlanID,
            COUNT(a.ApplicantID) AS HiredCount
        FROM [nhansucongty].[dbo].Applicants AS a
        WHERE a.Status = N'Đã tuyển'
        GROUP BY a.PlanID
    )
    INSERT INTO fact_recruitment_plan (
        -- recruitment_sk (IDENTITY column, REMOVED from insert list)
        recruitment_id,
        position_sk,
        position_name,
        department_sk,
        department_name,
        start_date_sk,
        end_date_sk,
        quantity,
        recruitment_duration,
        remaining_positions
    )
    SELECT
        rp.PlanID,
        dp.position_sk, -- Lấy khóa surrogate từ dim_positions
        dp.position_name,
        dd.department_sk, -- Lấy khóa surrogate từ dim_departments
        dd.department_name,
        dds.date_sk, -- Khóa surrogate ngày bắt đầu
        dde.date_sk, -- Khóa surrogate ngày kết thúc
        rp.Quantity,
        -- Tính toán thời lượng kế hoạch (bao gồm cả ngày bắt đầu và kết thúc)
        DATEDIFF(day, rp.StartDate, rp.EndDate) + 1,
        -- Tính toán số vị trí còn trống
        rp.Quantity - ISNULL(ha.HiredCount, 0)
    FROM [nhansucongty].[dbo].RecruitmentPlans AS rp
    JOIN dim_positions AS dp ON rp.PositionID = dp.position_id -- Join với dim_positions
    JOIN dim_departments AS dd ON rp.DepartmentID = dd.department_id -- Join với dim_departments
    JOIN dim_date AS dds ON CAST(FORMAT(rp.StartDate, 'yyyyMMdd') AS INT) = dds.date_sk -- Join với dim_date cho ngày bắt đầu
    JOIN dim_date AS dde ON CAST(FORMAT(rp.EndDate, 'yyyyMMdd') AS INT) = dde.date_sk -- Join với dim_date cho ngày kết thúc
    LEFT JOIN HiredApplicants AS ha ON rp.PlanID = ha.PlanID -- Join với CTE để lấy số lượng đã tuyển
    LEFT JOIN fact_recruitment_plan AS frp ON rp.PlanID = frp.recruitment_id -- Kiểm tra bản ghi đã tồn tại chưa
    WHERE frp.recruitment_id IS NULL; -- Chỉ chèn các bản ghi chưa có

END;
GO

-- 10. Stored Procedure cho fact_application
-- Tải dữ liệu ứng viên. Cần join với fact_recruitment_plan và dim_date.
-- SQL

DROP PROCEDURE IF EXISTS sp_load_fact_application;
GO

CREATE PROCEDURE sp_load_fact_application
AS
BEGIN
    SET NOCOUNT ON;

    INSERT INTO fact_application (
        -- application_sk (IDENTITY column, REMOVED from insert list)
        applicant_id,
        recruitment_sk, -- Khóa surrogate đến fact_recruitment_plan
        application_date_sk,
        position_name,
        department_name,
        status
    )
    SELECT
        a.ApplicantID,
        frp.recruitment_sk, -- Lấy khóa surrogate từ fact_recruitment_plan
        dd.date_sk, -- Lấy khóa surrogate từ dim_date
        frp.position_name, -- Lấy tên vị trí từ fact_recruitment_plan
        frp.department_name, -- Lấy tên phòng ban từ fact_recruitment_plan
        a.Status
    FROM [nhansucongty].[dbo].Applicants AS a
    JOIN [nhansucongty].[dbo].RecruitmentPlans AS rp_source ON a.PlanID = rp_source.PlanID -- Join tạm với bảng nguồn để lấy ID kế hoạch
    JOIN fact_recruitment_plan AS frp ON rp_source.PlanID = frp.recruitment_id -- Join với fact_recruitment_plan đã tải
    -- Lưu ý: Bảng Applicants nguồn không có cột ngày nộp đơn. Tôi đang tạm dùng StartDate của RecruitmentPlans.
    -- Nếu ngày nộp đơn là quan trọng, bạn cần bổ sung cột này vào bảng Applicants nguồn.
    JOIN dim_date AS dd ON CAST(FORMAT(rp_source.StartDate, 'yyyyMMdd') AS INT) = dd.date_sk -- Sử dụng ngày bắt đầu kế hoạch làm ngày nộp đơn tạm thời
    LEFT JOIN fact_application AS fa ON a.ApplicantID = fa.applicant_id -- Kiểm tra bản ghi đã tồn tại chưa
    WHERE fa.applicant_id IS NULL; -- Chỉ chèn các bản ghi chưa có

    -- Ghi chú: Nếu ngày nộp đơn thực tế khác với ngày bắt đầu kế hoạch, bạn cần sửa lại
    -- logic join với dim_date để sử dụng cột ngày nộp đơn từ bảng Applicants nguồn.

END;
GO

-- 11. Stored Procedure cho fact_registrations
-- Tải dữ liệu các loại đăng ký. Cần join với dim_employees (cho người yêu cầu và người duyệt) và dim_date.
-- Lưu ý: Tính toán registration_duration phụ thuộc vào loại đăng ký và chi tiết yêu cầu, điều này phức tạp. Tôi sẽ để logic tính toán này đơn giản hoặc ghi chú.
-- SQL

DROP PROCEDURE IF EXISTS sp_load_fact_registrations;
GO

CREATE PROCEDURE sp_load_fact_registrations
AS
BEGIN
    SET NOCOUNT ON;

    INSERT INTO fact_registrations (
        -- registration_sk (IDENTITY column, REMOVED from insert list)
        registration_id,
        employee_sk,
        employee_name,
        department_name,
        approved_by_name,
        registration_type,
        request_date_sk,
        registration_duration,
        status,
        approved_by_sk
    )
    SELECT
        r.RegistrationID,
        de_req.employee_sk, -- Lấy khóa surrogate cho người yêu cầu
        de_req.full_name,
        de_req.department_name, -- Lấy tên phòng ban của người yêu cầu
        de_app.full_name, -- Tên người duyệt
        r.RegistrationType,
        dd.date_sk, -- Lấy khóa surrogate ngày yêu cầu
        -- Tính toán thời lượng đăng ký (ví dụ đơn giản: 1 ngày cho hầu hết các loại)
        -- Logic này cần được điều chỉnh dựa trên RegistrationType và Details
        CASE
            WHEN r.RegistrationType = N'Nghỉ phép' THEN -- Cần phân tích Details để biết số ngày nghỉ
                 1 -- Giả định 1 ngày nghỉ nếu không phân tích Details
            WHEN r.RegistrationType = N'Làm thêm giờ' THEN -- Cần phân tích Details để biết số giờ
                 0 -- Giả định 0 nếu không phân tích Details
            ELSE 1 -- Mặc định là 1 cho các loại khác
        END,
        r.Status,
        de_app.employee_sk -- Lấy khóa surrogate cho người duyệt (NULL nếu không có)
    FROM [nhansucongty].[dbo].Registrations AS r
    JOIN dim_employees AS de_req ON r.EmployeeID = de_req.employee_id -- Join với dim_employees (người yêu cầu)
    LEFT JOIN dim_employees AS de_app ON r.ApprovedBy = de_app.employee_id -- Join với dim_employees (người duyệt, có thể NULL)
    JOIN dim_date AS dd ON CAST(FORMAT(r.RequestDate, 'yyyyMMdd') AS INT) = dd.date_sk -- Join với dim_date
    LEFT JOIN fact_registrations AS fr ON r.RegistrationID = fr.registration_id -- Kiểm tra bản ghi đã tồn tại chưa
    WHERE fr.registration_id IS NULL; -- Chỉ chèn các bản ghi chưa có

    -- Ghi chú: Logic tính toán registration_duration cần được cải tiến đáng kể
    -- dựa trên cấu trúc và nội dung của trường Details trong bảng Registrations nguồn.

END;
GO

-- 12. Stored Procedure cho fact_decision
-- Tải dữ liệu các quyết định. Cần join với dim_employees và dim_date.
-- SQL

DROP PROCEDURE IF EXISTS sp_load_fact_decision;
GO

CREATE PROCEDURE sp_load_fact_decision
AS
BEGIN
    SET NOCOUNT ON;

    INSERT INTO fact_decision (
        -- decision_sk (IDENTITY column, REMOVED from insert list)
        decision_id,
        employee_sk,
        employee_name,
        department_name,
        decision_date_sk,
        decision_type,
        decision_details,
        decision_effective_date, -- Cần kiểm tra nếu có cột này ở nguồn, nếu không thì NULL
        decision_expiry_date -- Cần kiểm tra nếu có cột này ở nguồn, nếu không thì NULL
    )
    SELECT
        d.DecisionID,
        de.employee_sk, -- Lấy khóa surrogate từ dim_employees
        de.full_name,
        de.department_name, -- Lấy tên phòng ban từ dim_employees
        dd.date_sk, -- Lấy khóa surrogate ngày quyết định
        d.DecisionType,
        d.Details, -- Sử dụng cột Details từ nguồn làm decision_details
        NULL, -- Giả định không có cột effective_date ở nguồn
        NULL -- Giả định không có cột expiry_date ở nguồn
    FROM [nhansucongty].[dbo].Decisions AS d
    JOIN dim_employees AS de ON d.EmployeeID = de.employee_id -- Join với dim_employees
    JOIN dim_date AS dd ON CAST(FORMAT(d.DecisionDate, 'yyyyMMdd') AS INT) = dd.date_sk -- Join với dim_date
    LEFT JOIN fact_decision AS fd ON d.DecisionID = fd.decision_id -- Kiểm tra bản ghi đã tồn tại chưa
    WHERE fd.decision_id IS NULL; -- Chỉ chèn các bản ghi chưa có

    -- Ghi chú: Nếu bảng Decisions nguồn có cột ngày hiệu lực hoặc ngày hết hiệu lực,
    -- bạn cần cập nhật SP này để ánh xạ dữ liệu đó vào decision_effective_date và decision_expiry_date.

END;
GO


