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
--4.b Điểm đến công tác phổ biến 
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
--4.c -- Tổng chi phí công tác theo phòng ban
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

--4.d -- Phân tích theo mục đích công tác
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

--4.e -- Xu hướng công tác theo tháng
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
--4.f -- Chi phí công tác theo phòng ban
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

--4.g-- Chi phí công tác theo vị trí/chức vụ
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

--5.Phân tích tuyển dụng 
--  Mục đích : Đánh giá hiệu quả kế hoạch tuyển dụng, tối ưu quy trình và dự báo nhu cầu nhân sự.
--5.a số lượng vị trí còn trống theo phòng ban / vị trí
SELECT 
    department_name,
    position_name,
    SUM(quantity) AS total_positions,
    SUM(remaining_positions) AS remaining_positions,
    SUM(quantity - remaining_positions) AS filled_positions
FROM fact_recruitment_plan
GROUP BY department_name, position_name
ORDER BY remaining_positions DESC;

--5.b Thời gian trung bình hoàn thành kế hoạch 
SELECT
    rp.recruitment_id AS plan_id,
    rp.position_name,
    rp.department_name,
    DATEDIFF(DAY, start_date.date, end_date.date) AS completion_days,
    start_date.date AS start_date,
    end_date.date AS end_date
FROM fact_recruitment_plan rp
JOIN dim_date start_date ON rp.start_date_sk = start_date.date_sk
JOIN dim_date end_date ON rp.end_date_sk = end_date.date_sk
WHERE end_date.date <= GETDATE()
ORDER BY DATEDIFF(DAY, start_date.date, end_date.date) DESC;  -- Sửa tại đây: Thay bằng biểu thức gốc hoặc alias

--5.c Tỷ lệ ứng viên được duyệt 
SELECT 
    rp.recruitment_id,
    rp.position_name,
    rp.department_name,
    rp.quantity AS target_quantity,
    COUNT(app.applicant_id) AS total_applicants,
    SUM(CASE WHEN app.status = 'Đã duyệt' THEN 1 ELSE 0 END) AS approved_applicants,
    ROUND(
        CASE 
            WHEN COUNT(app.applicant_id) = 0 THEN 0 
            ELSE SUM(CASE WHEN app.status = 'Đã duyệt' THEN 1 ELSE 0 END) * 100.0 / COUNT(app.applicant_id) 
        END, 2
    ) AS approval_rate_percentage
FROM 
    fact_recruitment_plan rp
LEFT JOIN 
    fact_application app ON rp.recruitment_sk = app.recruitment_sk
GROUP BY 
    rp.recruitment_id,
    rp.position_name,
    rp.department_name,
    rp.quantity
ORDER BY 
    approval_rate_percentage DESC;

--5.d Tỷ lệ hoàn thành chỉ tiêu tuyển dụng theo phòng ban
SELECT 
    rp.department_name,
    SUM(rp.quantity) AS total_target,
    SUM(rp.quantity - rp.remaining_positions) AS filled_positions,
    ROUND(SUM(rp.quantity - rp.remaining_positions) * 100.0 / SUM(rp.quantity), 2) AS fulfillment_rate
FROM 
    fact_recruitment_plan rp
GROUP BY 
    rp.department_name
ORDER BY 
    fulfillment_rate DESC;

--5.e XU hướng tuyển dụng theo tháng 
SELECT 
    d.year,
    d.month,
    COUNT(rp.recruitment_sk) AS total_plans
FROM 
    fact_recruitment_plan rp
JOIN 
    dim_date d ON rp.start_date_sk = d.date_sk
GROUP BY 
    d.year, d.month
ORDER BY 
    d.year, d.month;


--5.f số lượng ứng viên trung bình cho mỗi vị trí 
SELECT 
    rp.position_name,
    AVG(applicant_count) AS avg_applicants_per_position
FROM 
    fact_recruitment_plan rp
LEFT JOIN (
    SELECT 
        recruitment_sk, 
        COUNT(applicant_id) AS applicant_count
    FROM 
        fact_application
    GROUP BY 
        recruitment_sk
) app ON rp.recruitment_sk = app.recruitment_sk
GROUP BY 
    rp.position_name;

--5.g Cảnh báo kế hoạch có nguy cơ không hoàn thành
SELECT 
    rp.recruitment_id,
    rp.position_name,
    rp.remaining_positions,
    d_end.date AS end_date,
    DATEDIFF(DAY, CAST(GETDATE() AS DATE), d_end.date) AS days_remaining
FROM 
    fact_recruitment_plan rp
JOIN 
    dim_date d_end ON rp.end_date_sk = d_end.date_sk
WHERE 
    rp.remaining_positions > 0
    AND d_end.date BETWEEN CAST(GETDATE() AS DATE) AND DATEADD(DAY, 30, CAST(GETDATE() AS DATE))
ORDER BY 
    days_remaining ASC;

--6. Phân tích đăng ký (nghỉ phép, làm thêm, công tác)
--Mục đích: Quản lý các yêu cầu đăng ký của nhân viên để đảm bảo quy trình phê duyệt hợp lý.
--6.a Số lượng đăng ký theo loại và phòng ban
SELECT 
    r.department_name,
    r.registration_type,
    COUNT(r.registration_sk) AS total_registrations
FROM 
    fact_registrations r
GROUP BY 
    r.department_name, 
    r.registration_type
ORDER BY 
    r.department_name, 
    total_registrations DESC;

--6.b Số lượng đăng ký theo loại của từng nhân viên
SELECT 
    e.full_name,
    r.registration_type,
    COUNT(r.registration_sk) AS total_registrations
FROM 
    fact_registrations r
JOIN 
    dim_employees e ON r.employee_sk = e.employee_sk
GROUP BY 
    e.full_name, 
    r.registration_type
ORDER BY 
    e.full_name, 
    total_registrations DESC;

--6.c Tỷ lệ duyệt đăng ký theo loại
SELECT 
    registration_type,
    COUNT(*) AS total_registrations,
    SUM(CASE WHEN status = 'Đã duyệt' THEN 1 ELSE 0 END) AS approved_count,
    ROUND(
        SUM(CASE WHEN status = 'Đã duyệt' THEN 1.0 ELSE 0 END) / COUNT(*) * 100, 
        2
    ) AS approval_rate_percentage
FROM 
    fact_registrations
GROUP BY 
    registration_type
ORDER BY 
    approval_rate_percentage DESC;

--6.d Xu hướng đăng ký theo thời gian

SELECT 
    d.year,
    d.month,
    r.registration_type,
    COUNT(r.registration_sk) AS total_registrations
FROM 
    fact_registrations r
JOIN 
    dim_date d ON r.request_date_sk = d.date_sk
GROUP BY 
    d.year, 
    d.month, 
    r.registration_type
ORDER BY 
    d.year, 
    d.month;

--6.e top 5 phòng ban có số đăng ký cao nhất 
SELECT TOP 5
    department_name,
    COUNT(registration_sk) AS total_registrations
FROM 
    fact_registrations
GROUP BY 
    department_name
ORDER BY 
    total_registrations DESC;

--6.f thống kê đăng ký theo trạng thái  
SELECT 
    status,
    registration_type,
    COUNT(*) AS count,
    ROUND(COUNT(*) * 100.0 / SUM(COUNT(*)) OVER (PARTITION BY registration_type), 2) AS percentage
FROM 
    fact_registrations
GROUP BY 
    registration_type, 
    status;

--6.g số gio làm thêm trung bình được duyệt
SELECT 
    AVG(registration_duration) AS avg_overtime_hours
FROM 
    fact_registrations
WHERE 
    registration_type = 'Làm thêm' 
    AND status = 'Đã duyệt';

--7. Phân tích quyết định khen thưởng và kỷ luật
--Mục đích: Đánh giá tác động của các quyết định đến hiệu suất làm việc và văn hóa tổ chức.
--7.a Số lượng quyết định theo loại và phòng ban
SELECT 
    d.department_name,
    fd.decision_type,
    COUNT(fd.decision_sk) AS total_decisions
FROM 
    fact_decision fd
JOIN 
    dim_departments d ON fd.department_name = d.department_name
GROUP BY 
    d.department_name, 
    fd.decision_type
ORDER BY 
    total_decisions DESC;

--7.b Phân tích số lượng quyết định theo tháng/năm.
SELECT 
    dd.year,
    dd.month,
    fd.decision_type,
    COUNT(fd.decision_sk) AS total_decisions
FROM 
    fact_decision fd
JOIN 
    dim_date dd ON fd.decision_date_sk = dd.date_sk
GROUP BY 
    dd.year, 
    dd.month, 
    fd.decision_type
ORDER BY 
    dd.year, 
    dd.month;

--7.c Tỷ lệ khen thưởng vs kỷ luật 
SELECT 
    decision_type,
    COUNT(*) AS total_decisions,
    ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM fact_decision), 2) AS percentage
FROM 
    fact_decision
GROUP BY 
    decision_type;

--7.d Ảnh hưởng đến hành vi làm việc (Kiểm tra xem quyết định kỷ luật có giảm tỷ lệ đi muộn không).
SELECT 
    e.employee_sk,
    e.full_name,
    COUNT(CASE WHEN fd.decision_type = 'Kỷ luật' THEN 1 END) AS disciplinary_count,
    AVG(CAST(fa.is_late AS FLOAT)) AS avg_late_rate_after_decision
FROM 
    fact_decision fd
JOIN 
    dim_employees e ON fd.employee_sk = e.employee_sk
JOIN 
    fact_attendance fa ON e.employee_sk = fa.employee_sk
    AND fa.date_sk > fd.decision_date_sk -- Xét dữ liệu sau quyết định
WHERE 
    fd.decision_type = 'Kỷ luật'
GROUP BY 
    e.employee_sk, 
    e.full_name;

--7.e thời gian hiệu lực trung bình của quyết định (đánh giá thời gian tác động của quyết định)
SELECT 
    decision_type,
    AVG(DATEDIFF(DAY, decision_effective_date, decision_expiry_date)) AS avg_duration_days
FROM 
    fact_decision
WHERE 
    decision_expiry_date IS NOT NULL
GROUP BY 
    decision_type;

--7.f Top 5 nhân viên được khen thưởng nhiều nhất
SELECT TOP 5
    e.full_name,
    d.department_name,
    COUNT(fd.decision_sk) AS reward_count
FROM 
    fact_decision fd
JOIN 
    dim_employees e ON fd.employee_sk = e.employee_sk
JOIN 
    dim_departments d ON e.department_sk = d.department_sk
WHERE 
    fd.decision_type = 'Khen thưởng'
GROUP BY 
    e.full_name, 
    d.department_name
ORDER BY 
    reward_count DESC;

--7.g tương quan giữa khen thưởng và giờ làm thêm 
SELECT 
    e.employee_sk,
    e.full_name,
    COUNT(fd.decision_sk) AS reward_count,
    SUM(fr.registration_duration) AS total_overtime_hours
FROM 
    fact_decision fd
JOIN 
    dim_employees e ON fd.employee_sk = e.employee_sk
LEFT JOIN 
    fact_registrations fr ON e.employee_sk = fr.employee_sk
    AND fr.registration_type = 'Làm thêm'
WHERE 
    fd.decision_type = 'Khen thưởng'
GROUP BY 
    e.employee_sk, 
    e.full_name;

--7.h phân tích theo chức vụ 
SELECT 
    p.position_name,
    fd.decision_type,
    COUNT(*) AS decision_count
FROM 
    fact_decision fd
JOIN 
    dim_employees e ON fd.employee_sk = e.employee_sk
JOIN 
    dim_positions p ON e.position_sk = p.position_sk
GROUP BY 
    p.position_name, 
    fd.decision_type
ORDER BY 
    decision_count DESC;


--8. Phân tích thời gian 
--Mục đích: Theo dõi xu hướng và biến động của các chỉ số nhân sự theo thời gian.

--8.a Lương trung bình của nhân viên qua các năm 


