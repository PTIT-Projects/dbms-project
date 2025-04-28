USE msdb; -- SQL Server Agent Jobs được lưu trữ trong cơ sở dữ liệu msdb
GO

-- Bước 1: Khai báo các biến và Bắt đầu Transaction
DECLARE @jobId BINARY(16);
DECLARE @ReturnCode INT;
DECLARE @StartDateInt INT; -- Biến mới để lưu ngày bắt đầu

SET @ReturnCode = 0;

-- BẮT ĐẦU TRANSACTION
BEGIN TRANSACTION;

-- Tính toán ngày bắt đầu dạng YYYYMMDD
SET @StartDateInt = CONVERT(INT, CONVERT(VARCHAR(8), GETDATE(), 112));

-- Đảm bảo Job Category tồn tại (Tùy chọn nhưng nên làm)
IF NOT EXISTS (SELECT name FROM msdb.dbo.syscategories WHERE name=N'[Data Warehouse ETL]')
BEGIN
    EXEC @ReturnCode = msdb.dbo.sp_add_category @class=N'JOB', @type=N'LOCAL', @name=N'[Data Warehouse ETL]';
    IF (@@ERROR <> 0 OR @ReturnCode <> 0) GOTO QuitWithRollback;
END

-- Bước 2: Xóa Job cũ nếu tồn tại (để chạy lại script dễ dàng hơn khi cần cập nhật)
IF EXISTS (SELECT name FROM msdb.dbo.sysjobs WHERE name = N'HRMS_Warehouse_Daily_ETL')
BEGIN
    EXEC @ReturnCode = msdb.dbo.sp_delete_job @job_name=N'HRMS_Warehouse_Daily_ETL', @delete_history=1, @delete_unused_schedule=1;
    IF (@@ERROR <> 0 OR @ReturnCode <> 0) GOTO QuitWithRollback; -- Thêm kiểm tra lỗi sau lệnh DELETE
END

-- Bước 3: Tạo Job mới
EXEC @ReturnCode = msdb.dbo.sp_add_job @job_name=N'HRMS_Warehouse_Daily_ETL',
    @enabled=1, -- Bật Job ngay sau khi tạo
    @notify_level_eventlog=0, -- Không ghi vào event log khi Job bắt đầu/kết thúc
    @notify_level_email=0, -- Không gửi email thông báo
    @notify_level_netsend=0, -- Không gửi thông báo qua mạng
    @notify_level_page=0, -- Không gửi thông báo qua pager
    @delete_level=0, -- Không xóa Job khi hoàn thành
    @description=N'Job ETL hàng ngày cập nhật dữ liệu từ nhansucongty sang hrms_warehouse.',
    @category_name=N'[Data Warehouse ETL]', -- Gán vào category đã tạo
    @owner_login_name=N'sa', -- !!! THAY 'sa' BẰNG LOGIN PHÙ HỢP !!!
    @job_id = @jobId OUTPUT;
IF (@@ERROR <> 0 OR @ReturnCode <> 0) GOTO QuitWithRollback;

-- Bước 4: Thêm các Bước (Steps) vào Job - (Giữ nguyên các bước từ 1 đến 12 như trước)
-- Step 1: Populate dim_date
EXEC @ReturnCode = msdb.dbo.sp_add_jobstep @job_id=@jobId, @step_name=N'1. Populate dim_date',
    @step_id=1, @cmdexec_success_code=0, @on_success_action=3, @on_fail_action=2, @retry_attempts=0, @retry_interval=0, @os_run_priority=0, @subsystem=N'TSQL',
    @command=N'EXEC hrms_warehouse.dbo.sp_populate_dim_date;', @database_name=N'hrms_warehouse', @flags=0;
IF (@@ERROR <> 0 OR @ReturnCode <> 0) GOTO QuitWithRollback;

-- Step 2: Load dim_departments
EXEC @ReturnCode = msdb.dbo.sp_add_jobstep @job_id=@jobId, @step_name=N'2. Load dim_departments',
    @step_id=2, @cmdexec_success_code=0, @on_success_action=3, @on_fail_action=2, @retry_attempts=0, @retry_interval=0, @os_run_priority=0, @subsystem=N'TSQL',
    @command=N'EXEC hrms_warehouse.dbo.sp_load_dim_departments;', @database_name=N'hrms_warehouse', @flags=0;
IF (@@ERROR <> 0 OR @ReturnCode <> 0) GOTO QuitWithRollback;

-- Step 3: Load dim_positions
EXEC @ReturnCode = msdb.dbo.sp_add_jobstep @job_id=@jobId, @step_name=N'3. Load dim_positions',
    @step_id=3, @cmdexec_success_code=0, @on_success_action=3, @on_fail_action=2, @retry_attempts=0, @retry_interval=0, @os_run_priority=0, @subsystem=N'TSQL',
    @command=N'EXEC hrms_warehouse.dbo.sp_load_dim_positions;', @database_name=N'hrms_warehouse', @flags=0;
IF (@@ERROR <> 0 OR @ReturnCode <> 0) GOTO QuitWithRollback;

-- Step 4: Load dim_employees
EXEC @ReturnCode = msdb.dbo.sp_add_jobstep @job_id=@jobId, @step_name=N'4. Load dim_employees',
    @step_id=4, @cmdexec_success_code=0, @on_success_action=3, @on_fail_action=2, @retry_attempts=0, @retry_interval=0, @os_run_priority=0, @subsystem=N'TSQL',
    @command=N'EXEC hrms_warehouse.dbo.sp_load_dim_employees;', @database_name=N'hrms_warehouse', @flags=0;
IF (@@ERROR <> 0 OR @ReturnCode <> 0) GOTO QuitWithRollback;

-- Step 5: Load fact_attendance
EXEC @ReturnCode = msdb.dbo.sp_add_jobstep @job_id=@jobId, @step_name=N'5. Load fact_attendance',
    @step_id=5, @cmdexec_success_code=0, @on_success_action=3, @on_fail_action=2, @retry_attempts=0, @retry_interval=0, @os_run_priority=0, @subsystem=N'TSQL',
    @command=N'EXEC hrms_warehouse.dbo.sp_load_fact_attendance;', @database_name=N'hrms_warehouse', @flags=0;
IF (@@ERROR <> 0 OR @ReturnCode <> 0) GOTO QuitWithRollback;

-- Step 6: Load fact_salary
EXEC @ReturnCode = msdb.dbo.sp_add_jobstep @job_id=@jobId, @step_name=N'6. Load fact_salary',
    @step_id=6, @cmdexec_success_code=0, @on_success_action=3, @on_fail_action=2, @retry_attempts=0, @retry_interval=0, @os_run_priority=0, @subsystem=N'TSQL',
    @command=N'EXEC hrms_warehouse.dbo.sp_load_fact_salary;', @database_name=N'hrms_warehouse', @flags=0;
IF (@@ERROR <> 0 OR @ReturnCode <> 0) GOTO QuitWithRollback;

-- Step 7: Load fact_leave_balance
EXEC @ReturnCode = msdb.dbo.sp_add_jobstep @job_id=@jobId, @step_name=N'7. Load fact_leave_balance',
    @step_id=7, @cmdexec_success_code=0, @on_success_action=3, @on_fail_action=2, @retry_attempts=0, @retry_interval=0, @os_run_priority=0, @subsystem=N'TSQL',
    @command=N'EXEC hrms_warehouse.dbo.sp_load_fact_leave_balance;', @database_name=N'hrms_warehouse', @flags=0;
IF (@@ERROR <> 0 OR @ReturnCode <> 0) GOTO QuitWithRollback;

-- Step 8: Load fact_work_trips
EXEC @ReturnCode = msdb.dbo.sp_add_jobstep @job_id=@jobId, @step_name=N'8. Load fact_work_trips',
    @step_id=8, @cmdexec_success_code=0, @on_success_action=3, @on_fail_action=2, @retry_attempts=0, @retry_interval=0, @os_run_priority=0, @subsystem=N'TSQL',
    @command=N'EXEC hrms_warehouse.dbo.sp_load_fact_work_trips;', @database_name=N'hrms_warehouse', @flags=0;
IF (@@ERROR <> 0 OR @ReturnCode <> 0) GOTO QuitWithRollback;

-- Step 9: Load fact_recruitment_plan
EXEC @ReturnCode = msdb.dbo.sp_add_jobstep @job_id=@jobId, @step_name=N'9. Load fact_recruitment_plan',
    @step_id=9, @cmdexec_success_code=0, @on_success_action=3, @on_fail_action=2, @retry_attempts=0, @retry_interval=0, @os_run_priority=0, @subsystem=N'TSQL',
    @command=N'EXEC hrms_warehouse.dbo.sp_load_fact_recruitment_plan;', @database_name=N'hrms_warehouse', @flags=0;
IF (@@ERROR <> 0 OR @ReturnCode <> 0) GOTO QuitWithRollback;

-- Step 10: Load fact_application
EXEC @ReturnCode = msdb.dbo.sp_add_jobstep @job_id=@jobId, @step_name=N'10. Load fact_application',
    @step_id=10, @cmdexec_success_code=0, @on_success_action=3, @on_fail_action=2, @retry_attempts=0, @retry_interval=0, @os_run_priority=0, @subsystem=N'TSQL',
    @command=N'EXEC hrms_warehouse.dbo.sp_load_fact_application;', @database_name=N'hrms_warehouse', @flags=0;
IF (@@ERROR <> 0 OR @ReturnCode <> 0) GOTO QuitWithRollback;

-- Step 11: Load fact_registrations
EXEC @ReturnCode = msdb.dbo.sp_add_jobstep @job_id=@jobId, @step_name=N'11. Load fact_registrations',
    @step_id=11, @cmdexec_success_code=0, @on_success_action=3, @on_fail_action=2, @retry_attempts=0, @retry_interval=0, @os_run_priority=0, @subsystem=N'TSQL',
    @command=N'EXEC hrms_warehouse.dbo.sp_load_fact_registrations;', @database_name=N'hrms_warehouse', @flags=0;
IF (@@ERROR <> 0 OR @ReturnCode <> 0) GOTO QuitWithRollback;

-- Step 12: Load fact_decision (Bước cuối cùng)
EXEC @ReturnCode = msdb.dbo.sp_add_jobstep @job_id=@jobId, @step_name=N'12. Load fact_decision',
    @step_id=12, @cmdexec_success_code=0, @on_success_action=1, @on_fail_action=2, @retry_attempts=0, @retry_interval=0, @os_run_priority=0, @subsystem=N'TSQL',
    @command=N'EXEC hrms_warehouse.dbo.sp_load_fact_decision;', @database_name=N'hrms_warehouse', @flags=0;
IF (@@ERROR <> 0 OR @ReturnCode <> 0) GOTO QuitWithRollback;


-- Bước 5: Đặt Bước bắt đầu cho Job
EXEC @ReturnCode = msdb.dbo.sp_update_job @job_id = @jobId, @start_step_id = 1;
IF (@@ERROR <> 0 OR @ReturnCode <> 0) GOTO QuitWithRollback;

-- Bước 6: Thêm Lịch trình (Schedule) cho Job (Ví dụ: Chạy hàng ngày lúc 2:00 AM)
EXEC @ReturnCode = msdb.dbo.sp_add_jobschedule @job_id=@jobId, @name=N'Daily_Run_0200',
    @enabled=1, -- Bật lịch trình
    @freq_type=4, -- Hàng ngày
    @freq_interval=1, -- Mỗi ngày
    @freq_subday_type=1, -- Theo thời gian cụ thể
    @freq_subday_interval=0, -- Không sử dụng (khi freq_subday_type=1)
    @freq_relative_interval=0, -- Không sử dụngS
    @active_start_date=@StartDateInt, -- Ngày bắt đầu lịch trình (hôm nay)
    @active_end_date=99991231, -- Ngày kết thúc lịch trình (không giới hạn)
    @active_start_time=20000, -- Thời gian bắt đầu (02:00:00)
    @active_end_time=235959; -- Thời gian kết thúc
IF (@@ERROR <> 0 OR @ReturnCode <> 0) GOTO QuitWithRollback;

-- Bước 7: Thêm Target Server (Thường là Server Local)
EXEC @ReturnCode = msdb.dbo.sp_add_jobserver @job_id = @jobId, @server_name = N'(local)';
IF (@@ERROR <> 0 OR @ReturnCode <> 0) GOTO QuitWithRollback;

-- Kết thúc: Nếu không có lỗi, COMMIT TRANSACTION
COMMIT TRANSACTION; -- Commit transaction đã bắt đầu ở trên
GOTO EndSave;

-- Xử lý khi có lỗi
QuitWithRollback:
    -- Nếu có lỗi và có transaction đang mở, ROLLBACK TRANSACTION
    IF (@@TRANCOUNT > 0) ROLLBACK TRANSACTION;
    PRINT 'An error occurred. Rolling back changes.'; -- Thêm thông báo lỗi chi tiết hơn nếu cần

EndSave:
GO
