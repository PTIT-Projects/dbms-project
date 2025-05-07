-- Đảm bảo bạn đang ở đúng database context (msdb)
USE msdb;
GO

-- === KHAI BÁO BIẾN ===
DECLARE @jobId BINARY(16);
DECLARE @job_name NVARCHAR(128) = N'HRMS Warehouse ETL Daily Update'; -- Tên Job
DECLARE @schedule_name NVARCHAR(128) = N'Daily 7 PM';           -- Tên Lịch trình
DECLARE @step_name NVARCHAR(128) = N'Run sp_ETL_HRMS_Warehouse'; -- Tên Bước thực thi
DECLARE @database_name NVARCHAR(128) = N'hrms_warehouse';        -- Tên Database chứa SP
DECLARE @procedure_name NVARCHAR(128) = N'sp_ETL_HRMS_Warehouse'; -- Tên Stored Procedure
DECLARE @owner_login_name NVARCHAR(128) = N'sa';                  -- Login sẽ chạy Job (thay 'sa' nếu cần)
DECLARE @server_name NVARCHAR(128) = @@SERVERNAME;               -- Server hiện tại

-- === 1. XÓA JOB CŨ NẾU TỒN TẠI (Để tránh lỗi khi chạy lại script) ===
-- Kiểm tra xem Job đã tồn tại chưa
SELECT @jobId = job_id FROM msdb.dbo.sysjobs WHERE name = @job_name;

-- Nếu Job tồn tại, xóa nó đi
IF (@jobId IS NOT NULL)
BEGIN
    PRINT N'Deleting existing Job: ' + @job_name;
    EXEC msdb.dbo.sp_delete_job @job_name = @job_name, @delete_unused_schedule = 1;
    SET @jobId = NULL; -- Reset jobId sau khi xóa
END

-- === 2. TẠO JOB MỚI ===
BEGIN TRANSACTION; -- Bắt đầu Transaction để đảm bảo các bước được thực hiện cùng nhau

    -- Tạo Job
    EXEC msdb.dbo.sp_add_job
        @job_name = N'HRMS Warehouse ETL Daily Update', -- Tên Job
        @enabled = 1, -- Job được kích hoạt
        @notify_level_eventlog = 0, -- Không ghi vào event log khi thành công
        @notify_level_email = 0,
        @notify_level_netsend = 0,
        @notify_level_page = 0,
        @delete_level = 0,
        @description = N'Chạy ETL hàng ngày vào 7h tối để cập nhật dữ liệu cho HRMS Warehouse.', -- Mô tả Job
        @category_name = N'[Uncategorized (Local)]', -- Phân loại Job (có thể thay đổi)
        @owner_login_name = N'sa', -- Login sở hữu Job (đảm bảo login này có quyền)
        @job_id = @jobId OUTPUT; -- Lấy ID của Job vừa tạo

    IF @@ERROR <> 0 OR @jobId IS NULL GOTO QuitWithRollback; -- Kiểm tra lỗi

    PRINT N'Job created: ' + N'HRMS Warehouse ETL Daily Update' + N' with ID: ' + CONVERT(NVARCHAR(36), @jobId);

-- === 3. TẠO BƯỚC THỰC THI (Job Step) ===
    -- Thêm bước để thực thi Stored Procedure
    EXEC msdb.dbo.sp_add_jobstep
        @job_id = @jobId,
        @step_name = N'Run sp_ETL_HRMS_Warehouse', -- Tên của bước
        @step_id = 1, -- ID của bước (bắt đầu từ 1)
        @cmdexec_success_code = 0, -- Mã thành công mặc định
        @on_success_action = 1, -- 1 = Quit job reporting success (Kết thúc và báo thành công)
        @on_success_step_id = 0,
        @on_fail_action = 2, -- 2 = Quit job reporting failure (Kết thúc và báo thất bại)
        @on_fail_step_id = 0,
        @retry_attempts = 0, -- Không thử lại nếu thất bại
        @retry_interval = 0,
        @os_run_priority = 0,
        @subsystem = N'TSQL', -- Loại subsystem là T-SQL
        @command = N'USE [hrms_warehouse]; EXEC dbo.sp_ETL_HRMS_Warehouse;', -- Lệnh T-SQL cần thực thi
        @database_name = N'hrms_warehouse', -- Database context cho lệnh
        @flags = 0; -- Cờ (flags)

    IF @@ERROR <> 0 GOTO QuitWithRollback; -- Kiểm tra lỗi

    PRINT N'Job Step created: ' + N'Run sp_ETL_HRMS_Warehouse';

-- === 4. CẬP NHẬT ĐIỂM BẮT ĐẦU CHO JOB ===
    -- Thiết lập bước bắt đầu cho Job (là bước 1)
    EXEC msdb.dbo.sp_update_job
        @job_id = @jobId,
        @start_step_id = 1;

    IF @@ERROR <> 0 GOTO QuitWithRollback; -- Kiểm tra lỗi

-- === 5. TẠO LỊCH TRÌNH (Schedule) ===
    -- Thêm lịch trình chạy hàng ngày vào 19:00:00
    EXEC msdb.dbo.sp_add_jobschedule
        @job_id = @jobId,
        @name = N'Daily 7 PM', -- Tên lịch trình
        @enabled = 1, -- Kích hoạt lịch trình
        @freq_type = 4, -- 4 = Daily (Hàng ngày)
        @freq_interval = 1, -- Chạy mỗi 1 ngày
        @freq_subday_type = 1, -- 1 = At the specified time (Vào một thời điểm cụ thể)
        @freq_subday_interval = 0,
        @freq_relative_interval = 0,
        @freq_recurrence_factor = 1, -- Lặp lại mỗi 1 khoảng thời gian (ở đây là 1 ngày)
        @active_start_date = 20200101, -- Ngày bắt đầu có hiệu lực (có thể đặt ngày hiện tại hoặc quá khứ)
        @active_end_date = 99991231, -- Ngày kết thúc (99991231 nghĩa là không bao giờ kết thúc)
        @active_start_time = 190000, -- Thời gian bắt đầu chạy (19:00:00)
        @active_end_time = 235959; -- Thời gian kết thúc trong ngày (không cần thiết khi freq_subday_type = 1)

    IF @@ERROR <> 0 GOTO QuitWithRollback; -- Kiểm tra lỗi

    PRINT N'Schedule created: ' + N'Daily 7 PM';

-- === 6. GÁN JOB VÀO SERVER HIỆN TẠI ===
    -- Chỉ định server sẽ chạy Job này (thường là server cục bộ)
    EXEC msdb.dbo.sp_add_jobserver
        @job_id = @jobId,
        @server_name = N'(local)'; -- Hoặc @@SERVERNAME

    IF @@ERROR <> 0 GOTO QuitWithRollback; -- Kiểm tra lỗi

COMMIT TRANSACTION; -- Hoàn tất Transaction nếu mọi thứ thành công
PRINT N'Job successfully created and scheduled.';
GOTO EndSave;

QuitWithRollback:
    IF (@@TRANCOUNT > 0) ROLLBACK TRANSACTION; -- Hoàn tác Transaction nếu có lỗi
    PRINT N'An error occurred. Rolling back changes.';
    -- Bạn có thể thêm lệnh RAISERROR ở đây nếu muốn
EndSave:
GO
