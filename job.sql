-- Script to create the SQL Server Agent Job for Daily ETL Load

USE msdb; -- Important: Jobs are managed in the msdb database
GO

DECLARE @jobId BINARY(16);
DECLARE @jobName NVARCHAR(128) = N'HRMS Warehouse ETL - Daily Load';
DECLARE @ownerLoginName NVARCHAR(128) = SUSER_SNAME(); -- Use current user or specify service account like 'NT SERVICE\SQLSERVERAGENT'

-- Check if the job already exists
IF EXISTS (SELECT job_id FROM msdb.dbo.sysjobs WHERE name = @jobName)
BEGIN
    PRINT 'Job "' + @jobName + '" already exists. Deleting it first.';
    EXEC msdb.dbo.sp_delete_job @job_name = @jobName, @delete_unused_schedule = 1;
END;

-- Add the job
PRINT 'Creating Job "' + @jobName + '"...';
EXEC msdb.dbo.sp_add_job
    @job_name = @jobName,
    @enabled = 1, -- Enable the job
    @notify_level_eventlog = 0,
    @notify_level_email = 2, -- Notify on failure
    @notify_level_netsend = 0,
    @notify_level_page = 0,
    @delete_level = 0,
    @description = N'Runs the daily ETL process to load data from nhansucongty into hrms_warehouse.',
    @category_name = N'[Uncategorized (Local)]', -- Or choose a relevant category
    @owner_login_name = @ownerLoginName, -- Set appropriate owner
    @job_id = @jobId OUTPUT;

-- Add the job step to execute the master stored procedure
PRINT 'Adding Job Step "Run Master ETL SP"...';
EXEC msdb.dbo.sp_add_jobstep
    @job_id = @jobId,
    @step_name = N'Run Master ETL SP',
    @step_id = 1,
    @cmdexec_success_code = 0,
    @on_success_action = 1, -- Quit the job reporting success
    @on_success_step_id = 0,
    @on_fail_action = 2, -- Quit the job reporting failure
    @on_fail_step_id = 0,
    @retry_attempts = 0, -- Number of retry attempts
    @retry_interval = 0, -- Wait interval between retries in minutes
    @os_run_priority = 0,
    @subsystem = N'TSQL', -- Type of step is Transact-SQL
    @command = N'-- Example: Run full load daily, or load recent attendance for last 7 days
EXEC hrms_warehouse.dbo.sp_Run_ETL_hrms_warehouse @LoadRecentAttendanceDays = 7;
',
    @database_name = N'hrms_warehouse', -- Database context for the step
    @flags = 0;

-- Update the job to set the start step
PRINT 'Setting start step for the job...';
EXEC msdb.dbo.sp_update_job
    @job_id = @jobId,
    @start_step_id = 1;

-- Add a schedule for the job (Example: Run daily at 2:00 AM)
PRINT 'Adding schedule "Daily 02:00 AM" to the job...';
DECLARE @scheduleId INT;
EXEC msdb.dbo.sp_add_jobschedule
    @job_id = @jobId,
    @name = N'Daily 02:00 AM',
    @enabled = 1,
    @freq_type = 4, -- Frequency type: 4 = Daily
    @freq_interval = 1, -- Run every 1 day
    @freq_subday_type = 1, -- Frequency subday type: 1 = At the specified time
    @freq_subday_interval = 0,
    @freq_relative_interval = 0,
    @freq_recurrence_factor = 1,
    @active_start_date = 20250101, -- Schedule active start date (YYYYMMDD)
    @active_end_date = 99991231, -- Schedule active end date (YYYYMMDD)
    @active_start_time = 20000, -- Schedule active start time (HHMMSS), 02:00:00
    @active_end_time = 235959,
    @schedule_id = @scheduleId OUTPUT;

-- (Optional) Add job server assignment
PRINT 'Assigning job to the current server...';
EXEC msdb.dbo.sp_add_jobserver
    @job_id = @jobId,
    @server_name = N'(local)'; -- Or your specific server name

PRINT 'Job "' + @jobName + '" created successfully.';
GO

-- ---------------------------------------------------------------------
-- (Optional) Script to create a Job for populating dim_date (Run manually or less frequently)
-- ---------------------------------------------------------------------
USE msdb;
GO

DECLARE @jobId BINARY(16);
DECLARE @jobName NVARCHAR(128) = N'HRMS Warehouse Util - Populate Dim Date';
DECLARE @ownerLoginName NVARCHAR(128) = SUSER_SNAME();

IF EXISTS (SELECT job_id FROM msdb.dbo.sysjobs WHERE name = @jobName)
BEGIN
    PRINT 'Job "' + @jobName + '" already exists. Deleting it first.';
    EXEC msdb.dbo.sp_delete_job @job_name = @jobName, @delete_unused_schedule = 1;
END;

PRINT 'Creating Job "' + @jobName + '"...';
EXEC msdb.dbo.sp_add_job
    @job_name = @jobName,
    @enabled = 0, -- Disabled by default, run manually
    @description = N'Populates the dim_date table in hrms_warehouse. Run manually or schedule yearly/as needed.',
    @category_name=N'[Uncategorized (Local)]',
    @owner_login_name = @ownerLoginName,
    @job_id = @jobId OUTPUT;

PRINT 'Adding Job Step "Run Populate Dim Date SP"...';
EXEC msdb.dbo.sp_add_jobstep
    @job_id=@jobId,
    @step_name=N'Run Populate Dim Date SP',
    @subsystem=N'TSQL',
    @command=N'-- Populate dates from 2020 to 2030
EXEC hrms_warehouse.dbo.sp_Load_dim_date @StartDate = ''2020-01-01'', @EndDate = ''2030-12-31'';
',
    @database_name=N'hrms_warehouse',
    @on_success_action = 1, -- Quit reporting success
    @on_fail_action = 2; -- Quit reporting failure

PRINT 'Setting start step for the job...';
EXEC msdb.dbo.sp_update_job @job_id = @jobId, @start_step_id = 1;

PRINT 'Assigning job to the current server...';
EXEC msdb.dbo.sp_add_jobserver @job_id = @jobId, @server_name = N'(local)';

PRINT 'Job "' + @jobName + '" created successfully (Disabled by default).';
GO
