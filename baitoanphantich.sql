--1. Phân tích hiệu suất làm việc của nhân viên
-- Mục đích: Đánh giá hiệu suất làm việc thông qua các chỉ số như số giờ làm việc, đi muộn, làm thêm giờ, và rời sớm.
--1.a  Tổng số giờ làm việc của mỗi nhân viên trong năm 2025
SELECT 
    employee_name,
    department_name,
    SUM(hours_worked) AS total_hours_worked,
    COUNT(*) AS working_days
FROM fact_attendance fa
INNER JOIN dim_date dd ON fa.date_sk = dd.date_sk
WHERE dd.year = 2025 
GROUP BY employee_name, department_name
ORDER BY total_hours_worked DESC;
--1.b Phòng ban có tỷ lệ đi muộn cao nhất 
SELECT TOP 3
    department_name,
    COUNT(*) AS total_days,
    SUM(CAST(is_late AS INT)) AS late_days,
    ROUND(SUM(CAST(is_late AS FLOAT)) * 100 / COUNT(*), 2) AS late_percentage
FROM fact_attendance
GROUP BY department_name
ORDER BY late_percentage DESC;
--1.c Top 10 nhân viên có tổng giờ làm thêm cao nhất
SELECT TOP 10
    employee_name,
    department_name,
    SUM(overtime_hours) AS total_overtime,
    ROUND(AVG(overtime_hours), 2) AS avg_overtime_per_day
FROM fact_attendance
WHERE overtime_hours > 0
GROUP BY employee_name, department_name
ORDER BY total_overtime DESC;
--1.d Thống kê hiệu suất theo phòng ban
SELECT 
    department_name,
    COUNT(DISTINCT employee_name) AS employee_count,
    ROUND(AVG(hours_worked), 2) AS avg_hours_per_day,
    SUM(CAST(is_late AS INT)) * 100.0 / COUNT(*) AS late_percentage,
    SUM(overtime_hours) AS total_overtime
FROM fact_attendance
GROUP BY department_name
ORDER BY avg_hours_per_day DESC;

--2. Phân tích lương và phúc lợi
-- Mục đích: Hiểu rõ chi phí lương, phụ cấp, và khấu trừ để tối ưu hóa ngân sách và đảm bảo công bằng trong chính sách lương.
--2.a -- Lương trung bình theo phòng ban và vị trí
SELECT 
    department_name,
    position_name,
    COUNT(DISTINCT employee_name) AS employee_count,
    AVG(basic_salary) AS avg_basic_salary,
    AVG(allowance) AS avg_allowance,
    AVG(deductions) AS avg_deductions,
    AVG(total_salary) AS avg_total_salary
FROM fact_salary
WHERE payment_status = 'Đã thanh toán' -- Chỉ xét các lần đã thanh toán
GROUP BY department_name, position_name
ORDER BY department_name, avg_total_salary DESC;
--2.b Lương cao nhất và thấp nhất 
-- Top 10 nhân viên có tổng lương cao nhất
SELECT TOP 10
    employee_name,
    department_name,
    position_name,
    total_salary,
    basic_salary,
    allowance,
    deductions,
    ROUND((total_salary * 100.0) / (SELECT MAX(total_salary) FROM fact_salary), 2) AS salary_percentage
FROM fact_salary
WHERE payment_status = 'Đã thanh toán'
ORDER BY total_salary DESC;

-- Top 10 nhân viên có tổng lương thấp nhất
SELECT TOP 10
    employee_name,
    department_name,
    position_name,
    total_salary,
    basic_salary,
    allowance,
    deductions
FROM fact_salary
WHERE payment_status = 'Đã thanh toán'
ORDER BY total_salary ASC;
--2.c  Tỷ lệ lương cơ bản/tổng lương
-- theo phòng ban
SELECT 
    department_name,
    AVG(basic_salary) AS avg_basic,
    AVG(total_salary) AS avg_total,
    ROUND(AVG(basic_salary * 100.0 / total_salary), 2) AS basic_salary_ratio,
    ROUND(AVG(allowance * 100.0 / total_salary), 2) AS allowance_ratio,
    ROUND(AVG(deductions * 100.0 / total_salary), 2) AS deductions_ratio
FROM fact_salary
WHERE total_salary > 0 AND payment_status = 'Đã thanh toán'
GROUP BY department_name
ORDER BY basic_salary_ratio DESC;

-- Chi tiết theo từng nhân viên
SELECT 
    employee_name,
    department_name,
    position_name,
    basic_salary,
    allowance,
    deductions,
    total_salary,
    ROUND(basic_salary * 100.0 / total_salary, 2) AS basic_salary_percentage,
    ROUND(allowance * 100.0 / total_salary, 2) AS allowance_percentage,
    ROUND(deductions * 100.0 / total_salary, 2) AS deductions_percentage
FROM fact_salary
WHERE total_salary > 0 AND payment_status = 'Đã thanh toán'
ORDER BY basic_salary_percentage DESC;

--2.d-- Xu hướng lương theo tháng năm 2025(ohân tích biến đọng lương)
SELECT 
    dd.year,
    dd.month,
    AVG(fs.basic_salary) AS avg_basic,
    AVG(fs.total_salary) AS avg_total,
    SUM(fs.total_salary) AS department_total_payroll
FROM fact_salary fs
INNER JOIN dim_date dd ON fs.date_sk = dd.date_sk
WHERE dd.year = 2025 AND fs.payment_status = 'Đã thanh toán'
GROUP BY dd.year, dd.month, fs.department_name
ORDER BY dd.year, dd.month;
--2.e Phân tích lương theo độ tuổi giới tính  
SELECT 
    de.gender,
    CASE 
        WHEN de.age < 25 THEN 'Dưới 25'
        WHEN de.age BETWEEN 25 AND 35 THEN '25-35'
        WHEN de.age BETWEEN 36 AND 45 THEN '36-45'
        WHEN de.age > 45 THEN 'Trên 45'
    END AS age_group,
    AVG(fs.total_salary) AS avg_salary,
    COUNT(*) AS employee_count
FROM fact_salary fs
INNER JOIN dim_employees de ON fs.employee_sk = de.employee_sk
WHERE fs.payment_status = 'Đã thanh toán'
GROUP BY 
    de.gender,
    CASE 
        WHEN de.age < 25 THEN 'Dưới 25'
        WHEN de.age BETWEEN 25 AND 35 THEN '25-35'
        WHEN de.age BETWEEN 36 AND 45 THEN '36-45'
        WHEN de.age > 45 THEN 'Trên 45'
    END
ORDER BY age_group, gender;

--3 Phân tích nghỉ phép
--Mục đích: Quản lý số ngày nghỉ phép để đảm bảo cân bằng giữa công việc và nghỉ ngơi cho nhân viên.
--3.a  Số ngày nghỉ còn lại theo phòng ban và vị trí
SELECT 
    department_name,
    position_name,
    COUNT(DISTINCT employee_name) AS employee_count,
    AVG(total_leave_days) AS avg_total_leave,
    AVG(used_leave_days) AS avg_used_leave,
    AVG(remaining_leave_days) AS avg_remaining_leave,
    ROUND(AVG(used_leave_days * 100.0 / total_leave_days), 2) AS usage_percentage
FROM fact_leave_balance
WHERE granularity = 'Năm' -- Chỉ xét nghỉ phép năm
GROUP BY department_name, position_name
ORDER BY department_name, usage_percentage DESC;
--3.b -- Nhân viên đã dùng hết hoặc gần hết ngày nghỉ
SELECT 
    employee_name,
    department_name,
    position_name,
    total_leave_days,
    used_leave_days,
    remaining_leave_days,
    ROUND(used_leave_days * 100.0 / total_leave_days, 2) AS usage_percentage
FROM fact_leave_balance
WHERE 
    granularity = 'Năm' 
    AND remaining_leave_days <= 2 -- Còn lại 2 ngày hoặc ít hơn
ORDER BY usage_percentage DESC;
--3.c Xu hướng nghỉ phép 
-- Xu hướng sử dụng nghỉ phép theo tháng
SELECT 
    dd.year,
    dd.month,
    SUM(fl.used_leave_days) AS total_leave_days_used,
    COUNT(DISTINCT fl.employee_name) AS employees_took_leave
FROM fact_leave_balance fl
JOIN dim_date dd ON fl.date_sk = dd.date_sk
WHERE granularity = 'Năm'
GROUP BY dd.year, dd.month
ORDER BY dd.year, dd.month;

-- Xu hướng theo quý
SELECT 
    dd.year,
    dd.quarter,
    SUM(fl.used_leave_days) AS total_leave_days_used,
    ROUND(AVG(fl.used_leave_days), 2) AS avg_leave_per_employee
FROM fact_leave_balance fl
JOIN dim_date dd ON fl.date_sk = dd.date_sk
WHERE granularity = 'Năm'
GROUP BY dd.year, dd.quarter
ORDER BY dd.year, dd.quarter;
--3d.Phân tích loại nghỉ phép được sử dụng nhiều nhất
-- Phân bổ loại nghỉ phép
SELECT 
    leave_type,
    SUM(used_leave_days) AS total_days_used,
    COUNT(DISTINCT employee_sk) AS total_employees_used
FROM fact_leave_balance
GROUP BY leave_type
ORDER BY total_days_used DESC;

--3.e.Phân tích nghỉ phép theo thâm niên
-- Nghỉ phép theo thâm niên nhân viên
SELECT 
    CASE 
        WHEN de.total_years_worked < 1 THEN 'Dưới 1 năm'
        WHEN de.total_years_worked BETWEEN 1 AND 3 THEN '1-3 năm'
        WHEN de.total_years_worked BETWEEN 3 AND 5 THEN '3-5 năm'
        WHEN de.total_years_worked > 5 THEN 'Trên 5 năm'
    END AS experience_level,
    AVG(fl.total_leave_days) AS avg_allocated_leave,
    AVG(fl.used_leave_days) AS avg_used_leave,
    ROUND(AVG(fl.used_leave_days * 100.0 / fl.total_leave_days), 2) AS usage_percentage
FROM fact_leave_balance fl
JOIN dim_employees de ON fl.employee_name = de.full_name
WHERE fl.granularity = 'Năm'
GROUP BY 
    CASE 
        WHEN de.total_years_worked < 1 THEN 'Dưới 1 năm'
        WHEN de.total_years_worked BETWEEN 1 AND 3 THEN '1-3 năm'
        WHEN de.total_years_worked BETWEEN 3 AND 5 THEN '3-5 năm'
        WHEN de.total_years_worked > 5 THEN 'Trên 5 năm'
    END
ORDER BY experience_level;

--4. Phân tích công tác
--4.a Công tác nhiều nhất 
--nhân viên có nhiều chuyến công tác nhất
SELECT TOP 10
    employee_name,
    department_name,
    position_name,
    COUNT(*) AS total_trips,
    SUM(trip_duration) AS total_days_on_trip,
    SUM(total_cost) AS total_trip_cost
FROM fact_work_trips
WHERE status = 'Hoàn thành'
GROUP BY employee_name, department_name, position_name
ORDER BY total_trips DESC, total_days_on_trip DESC;
--3.b Điểm đến công tác phổ biến 
SELECT TOP 10
    destination,
    COUNT(*) AS trip_count,
    COUNT(DISTINCT employee_name) AS distinct_employees,
    SUM(total_cost) AS total_cost,
    AVG(total_cost) AS avg_cost_per_trip,
    AVG(trip_duration) AS avg_duration
FROM fact_work_trips
WHERE status = 'Hoàn thành'
GROUP BY destination
ORDER BY trip_count DESC;
--3.c -- Tổng chi phí công tác theo phòng ban
SELECT 
    department_name,
    COUNT(*) AS total_trips,
    SUM(trip_duration) AS total_days,
    SUM(total_cost) AS total_cost,
    SUM(total_cost) / SUM(trip_duration) AS cost_per_day,
    SUM(total_cost) / COUNT(DISTINCT employee_name) AS cost_per_employee
FROM fact_work_trips
WHERE status = 'Hoàn thành'
GROUP BY department_name
ORDER BY total_cost DESC;

--3.d -- Phân tích theo mục đích công tác
SELECT 
    purpose,
    COUNT(*) AS trip_count,
    COUNT(DISTINCT destination) AS distinct_destinations,
    AVG(trip_duration) AS avg_duration,
    SUM(total_cost) AS total_cost,
    SUM(total_cost) / COUNT(*) AS avg_cost_per_trip
FROM fact_work_trips
WHERE status = 'Hoàn thành'
GROUP BY purpose
ORDER BY total_cost DESC;

--3.e -- Xu hướng công tác theo tháng
SELECT 
    dd.year,
    dd.month,
    COUNT(*) AS trip_count,
    SUM(fwt.total_cost) AS monthly_cost,
    SUM(fwt.trip_duration) AS total_days
FROM fact_work_trips fwt
JOIN dim_date dd ON fwt.start_date_sk = dd.date_sk
WHERE fwt.status = 'Hoàn thành'
GROUP BY dd.year, dd.month
ORDER BY dd.year, dd.month;
--3.f -- Chi phí công tác theo phòng ban
SELECT 
    department_name,
    COUNT(DISTINCT employee_name) AS employees_with_trips,
    SUM(total_cost) AS total_cost,
    SUM(trip_duration) AS total_days,
    SUM(total_cost) / SUM(trip_duration) AS cost_per_day,
    SUM(total_cost) / COUNT(*) AS cost_per_trip
FROM fact_work_trips
WHERE status = 'Hoàn thành'     
GROUP BY department_name
ORDER BY cost_per_day DESC;

--3.g-- Chi phí công tác theo vị trí/chức vụ
SELECT 
    position_name,
    COUNT(*) AS trip_count,
    SUM(total_cost) AS total_cost,
    AVG(total_cost) AS avg_cost_per_trip,
    AVG(trip_duration) AS avg_duration
FROM fact_work_trips
WHERE status = 'Hoàn thành'
GROUP BY position_name
ORDER BY total_cost DESC;

