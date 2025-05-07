package vn.ptit.hrms.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class DataGeneratorService {
    // Database connection parameters - these will come from application.properties
    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUser;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    // Constants for data generation
    private static final int BATCH_SIZE = 1000;
    private static final int DEPARTMENT_COUNT = 20;
    private static final int POSITION_COUNT = 50;
    private static final int EMPLOYEE_COUNT = 100; // Adjust as needed
    private static final int TOTAL_RECORDS = 1000000; // Total records to generate

    // Vietnamese names for more realistic data
    private static final String[] FIRST_NAMES = {"Anh", "Bảo", "Châu", "Dũng", "Hà", "Hiền", "Hương", "Khoa",
            "Lan", "Linh", "Mai", "Minh", "Nam", "Ngọc", "Nhung", "Phương", "Quang", "Thanh", "Thảo", "Tuấn",
            "Việt", "Xuân", "Yến"};

    private static final String[] LAST_NAMES = {"Nguyễn", "Trần", "Lê", "Phạm", "Hoàng", "Huỳnh", "Phan", "Vũ",
            "Võ", "Đặng", "Bùi", "Đỗ", "Hồ", "Ngô", "Dương", "Lý"};

    private static final String[] MIDDLE_NAMES = {"Văn", "Thị", "Đức", "Hữu", "Quốc", "Hoài", "Minh", "Quang",
            "Tuấn", "Thành", "Ngọc", "Hoàng", "Đình", "Hồng", "Bảo", "Thu", "Thanh", "Phương", "Hà"};

    private static final String[] CITIES = {"Hà Nội", "Hồ Chí Minh", "Đà Nẵng", "Hải Phòng", "Cần Thơ",
            "Biên Hòa", "Nha Trang", "Huế", "Quảng Ninh", "Bắc Ninh"};

    private static final String[] DISTRICTS = {"Hoàn Kiếm", "Ba Đình", "Đống Đa", "Hai Bà Trưng", "Hoàng Mai",
            "Thanh Xuân", "Cầu Giấy", "Quận 1", "Quận 3", "Quận 7"};

    private static final String[] STREETS = {"Lê Lợi", "Nguyễn Huệ", "Trần Hưng Đạo", "Lý Thường Kiệt",
            "Nguyễn Trãi", "Bà Triệu", "Phan Đình Phùng", "Hoàng Diệu", "Nguyễn Công Trứ", "Trường Chinh"};

    private static final String[] DEPARTMENT_NAMES = {"Nhân sự", "Tài chính", "Kế toán", "Kinh doanh",
            "Marketing", "Kỹ thuật", "CNTT", "Hành chính", "Pháp chế", "Sản xuất", "Nghiên cứu phát triển",
            "Chăm sóc khách hàng", "Bảo vệ", "Đào tạo", "Logistics", "Quản lý chất lượng", "Bán hàng",
            "Thu mua", "Xuất nhập khẩu", "Truyền thông"};

    private static final String[] POSITION_PREFIXES = {"Giám đốc", "Trưởng phòng", "Phó phòng", "Trưởng nhóm",
            "Chuyên viên", "Nhân viên", "Thực tập sinh", "Kỹ sư", "Cố vấn", "Quản lý", "Điều phối viên",
            "Trợ lý", "Phó giám đốc", "Trưởng dự án", "Phó dự án"};

    private static final String[] GENDERS = {"Nam", "Nữ", "Khác"};
    private static final String[] EMPLOYEE_STATUS = {"Đang làm việc", "Nghỉ việc", "Nghỉ phép"};
    private static final String[] CONTRACT_TYPES = {"Toàn thời gian", "Bán thời gian"};
    private static final String[] CONTRACT_STATUS = {"Hiệu lực", "Hết hạn", "Chấm dứt"};
    private static final String[] DECISION_TYPES = {"Bổ nhiệm", "Khen thưởng", "Kỷ luật"};
    private static final String[] REGISTRATION_TYPES = {"Xe", "Ăn uống", "Phòng họp", "Nghỉ phép", "Làm thêm giờ", "Tạm ứng", "Từ chức"};
    private static final String[] REQUEST_STATUS = {"Đang chờ", "Đã duyệt", "Từ chối"};
    private static final String[] ATTENDANCE_STATUS = {"Có mặt", "Vắng mặt", "Đi muộn", "Làm thêm giờ"};
    private static final String[] INSURANCE_TYPES = {"Bảo hiểm xã hội", "Bảo hiểm y tế", "Bảo hiểm thất nghiệp", "Bảo hiểm sức khỏe", "Bảo hiểm tai nạn"};
    private static final String[] APPLICANT_STATUS = {"Đã ứng tuyển", "Đã phỏng vấn", "Đã tuyển", "Từ chối"};

    private static final Random random = new Random();
    private List<Integer> departmentIds = new ArrayList<>();
    private List<Integer> positionIds = new ArrayList<>();
    private List<Integer> employeeIds = new ArrayList<>();
    private List<Integer> planIds = new ArrayList<>();

    public void generateData() {
        try {
            // Load the SQL Server JDBC driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Clean up existing data
            cleanDatabase();

            // Generate data
            long startTime = System.currentTimeMillis();

            generateDepartments();
            generatePositions();
            generateEmployees();
            generateDepartmentManagers();
            generateContracts();
            generateSalaries();
            generateDecisions();
            generateLeaveBalances();
            generateNotifications();
            generateRegistrations();
            generateWorkTripRequests();
            generateAttendance();
            generateInsurance();
            generateRecruitmentPlans();
            generateApplicants();

            long endTime = System.currentTimeMillis();
            System.out.println("Data generation completed in " + (endTime - startTime) / 1000 + " seconds.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cleanDatabase() {
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            System.out.println("Cleaning existing data...");

            String[] tables = {
                    "Applicants", "RecruitmentPlans", "Insurance", "Attendance",
                    "WorkTripRequests", "Registrations", "Notifications", "LeaveBalances",
                    "Decisions", "Contracts", "Salary", "DepartmentManager",
                    "Employees", "Positions", "Departments"
            };

            for (String table : tables) {
                try {
                    PreparedStatement ps = conn.prepareStatement("DELETE FROM " + table);
                    ps.executeUpdate();
                    ps.close();
                    System.out.println("Cleaned table: " + table);
                } catch (SQLException e) {
                    System.out.println("Error cleaning table " + table + ": " + e.getMessage());
                }
            }

            // Reset identity columns
            for (String table : tables) {
                try {
                    PreparedStatement ps = conn.prepareStatement("DBCC CHECKIDENT('" + table + "', RESEED, 0)");
                    ps.executeUpdate();
                    ps.close();
                } catch (SQLException e) {
                    System.out.println("Error resetting identity for " + table + ": " + e.getMessage());
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void generateDepartments() {
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            System.out.println("Generating departments...");

            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO Departments (DepartmentName) VALUES (?)",
                    java.sql.Statement.RETURN_GENERATED_KEYS);

            for (int i = 0; i < DEPARTMENT_COUNT; i++) {
                String deptName = DEPARTMENT_NAMES[i % DEPARTMENT_NAMES.length];
                if (i >= DEPARTMENT_NAMES.length) {
                    deptName += " " + (i / DEPARTMENT_NAMES.length + 1);
                }

                ps.setString(1, deptName);
                ps.executeUpdate();

                // Get generated keys
                try (var rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        departmentIds.add(rs.getInt(1));
                    }
                }
            }
            ps.close();

            System.out.println("Generated " + departmentIds.size() + " departments");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void generatePositions() {
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            System.out.println("Generating positions...");

            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO Positions (PositionName, DepartmentID) VALUES (?, ?)",
                    java.sql.Statement.RETURN_GENERATED_KEYS);

            for (int i = 0; i < POSITION_COUNT; i++) {
                String prefix = POSITION_PREFIXES[i % POSITION_PREFIXES.length];
                int deptIndex = i % departmentIds.size();
                String positionName = prefix + " " + DEPARTMENT_NAMES[deptIndex % DEPARTMENT_NAMES.length];

                ps.setString(1, positionName);
                ps.setInt(2, departmentIds.get(deptIndex));
                ps.executeUpdate();

                try (var rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        positionIds.add(rs.getInt(1));
                    }
                }
            }
            ps.close();

            System.out.println("Generated " + positionIds.size() + " positions");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void generateEmployees() {
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            System.out.println("Generating employees...");

            String sql = "INSERT INTO Employees (userPassword, roleName, FullName, DateOfBirth, " +
                    "Gender, Address, Phone, Email, DepartmentID, PositionID, HireDate, Status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);

            Set<String> usedEmails = new HashSet<>();
            int batchCount = 0;

            // Create admin user first
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String adminPassword = encoder.encode("123456");
            String adminFullName = "Administrator System";
            LocalDate adminDob = LocalDate.of(1990, 1, 1);
            String adminGender = "Nam";
            String adminAddress = "123 Admin Street, Admin District, Admin City";
            String adminPhone = "0909123456";
            String adminEmail = "admin@gmail.com";

            // Use the first department and position for admin
            int adminDeptId = departmentIds.get(0);
            int adminPosId = positionIds.get(0);

            LocalDate adminHireDate = LocalDate.of(2010, 1, 1);
            String adminStatus = "Đang làm việc";

            // Set admin parameters
            ps.setString(1, adminPassword);
            ps.setString(2, "ROLE_ADMIN");
            ps.setString(3, adminFullName);
            ps.setDate(4, java.sql.Date.valueOf(adminDob));
            ps.setString(5, adminGender);
            ps.setString(6, adminAddress);
            ps.setString(7, adminPhone);
            ps.setString(8, adminEmail);
            ps.setInt(9, adminDeptId);
            ps.setInt(10, adminPosId);
            ps.setDate(11, java.sql.Date.valueOf(adminHireDate));
            ps.setString(12, adminStatus);

            ps.executeUpdate();

            // Add admin email to used emails
            usedEmails.add(adminEmail);

            // Get admin ID and add to employeeIds
            try (var rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int adminId = rs.getInt(1);
                    employeeIds.add(adminId);
                    System.out.println("Added admin user with ID: " + adminId);
                }
            }

            // Now generate regular employees
            for (int i = 0; i < EMPLOYEE_COUNT; i++) {
                if (i > 0 && i % 1000 == 0) {
                    System.out.println("Generated " + i + " employees...");
                }

                // Generate employee data
                String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
                String middleName = MIDDLE_NAMES[random.nextInt(MIDDLE_NAMES.length)];
                String firstName = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
                String fullName = lastName + " " + middleName + " " + firstName;

                LocalDate dob = randomDate(1970, 2000);
                String gender = GENDERS[random.nextInt(GENDERS.length)];

                String address = generateRandomAddress();
                String phone = generateRandomPhone();

                // Generate simple email as requested by user
                String email = "user" + i + "@gmail.com";
                usedEmails.add(email);

                int deptId = departmentIds.get(random.nextInt(departmentIds.size()));
                int posId = positionIds.get(random.nextInt(positionIds.size()));

                LocalDate hireDate = randomDate(2010, 2025);
                String status = EMPLOYEE_STATUS[random.nextInt(EMPLOYEE_STATUS.length)];

                // Set parameters
                ps.setString(1, encoder.encode("123456")); // Default password
                ps.setString(2, "ROLE_USER"); // Default role
                ps.setString(3, fullName);
                ps.setDate(4, java.sql.Date.valueOf(dob));
                ps.setString(5, gender);
                ps.setString(6, address);
                ps.setString(7, phone);
                ps.setString(8, email);
                ps.setInt(9, deptId);
                ps.setInt(10, posId);
                ps.setDate(11, java.sql.Date.valueOf(hireDate));
                ps.setString(12, status);

                ps.executeUpdate();

                try (var rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        employeeIds.add(rs.getInt(1));
                    }
                }

                batchCount++;
                if (batchCount >= BATCH_SIZE) {
                    batchCount = 0;
                }
            }
            ps.close();

            System.out.println("Generated " + employeeIds.size() + " employees (including admin)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void generateDepartmentManagers() {
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            System.out.println("Generating department managers...");

            // First, turn on IDENTITY_INSERT to allow explicit values for DepartmentID
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("SET IDENTITY_INSERT DepartmentManager ON");
            }

            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO DepartmentManager (DepartmentID, ManagerID) VALUES (?, ?)");

            // For each department, assign a random employee as manager
            for (Integer deptId : departmentIds) {
                // Get a random employee
                Integer employeeId = employeeIds.get(random.nextInt(employeeIds.size()));

                ps.setInt(1, deptId);
                ps.setInt(2, employeeId);
                ps.addBatch();
            }

            ps.executeBatch();
            ps.close();

            // Turn off IDENTITY_INSERT when done
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("SET IDENTITY_INSERT DepartmentManager OFF");
            }

            System.out.println("Generated department managers");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void generateContracts() {
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            System.out.println("Generating contracts...");

            String sql = "INSERT INTO Contracts (EmployeeID, ContractType, StartDate, EndDate, Status) " +
                    "VALUES (?, ?, ?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql);
            int batchCount = 0;
            int totalContracts = 0;

            // Generate 1-3 contracts per employee
            for (Integer empId : employeeIds) {
                int numContracts = random.nextInt(3) + 1;

                for (int i = 0; i < numContracts; i++) {
                    String contractType = CONTRACT_TYPES[random.nextInt(CONTRACT_TYPES.length)];
                    LocalDate startDate = randomDate(2010, 2025);
                    LocalDate endDate = startDate.plusYears(random.nextInt(3) + 1);
                    String status = CONTRACT_STATUS[random.nextInt(CONTRACT_STATUS.length)];

                    ps.setInt(1, empId);
                    ps.setString(2, contractType);
                    ps.setDate(3, java.sql.Date.valueOf(startDate));
                    ps.setDate(4, java.sql.Date.valueOf(endDate));
                    ps.setString(5, status);
                    ps.addBatch();

                    batchCount++;
                    totalContracts++;

                    if (batchCount >= BATCH_SIZE) {
                        ps.executeBatch();
                        batchCount = 0;
                        System.out.println("Generated " + totalContracts + " contracts...");
                    }
                }
            }

            if (batchCount > 0) {
                ps.executeBatch();
            }

            ps.close();
            System.out.println("Generated " + totalContracts + " contracts");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void generateSalaries() {
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            System.out.println("Generating salaries...");

            String sql = "INSERT INTO Salary (EmployeeID, Month, Year, BasicSalary, Allowance, Deductions, NetSalary) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql);
            int batchCount = 0;
            int totalSalaries = 0;

            // Generate 12 months of salary for 2 years for each employee
            for (Integer empId : employeeIds) {
                for (int year = 2022; year <= 2025; year++) {
                    for (int month = 1; month <= 12; month++) {
                        // Generate salary data
                        double basicSalary = 5000000 + (random.nextDouble() * 15000000); // 5M to 20M VND
                        double allowance = 500000 + (random.nextDouble() * 2000000); // 500K to 2.5M VND
                        double deductions = 500000 + (random.nextDouble() * 1000000); // 500K to 1.5M VND
                        double netSalary = basicSalary + allowance - deductions;

                        ps.setInt(1, empId);
                        ps.setInt(2, month);
                        ps.setInt(3, year);
                        ps.setDouble(4, basicSalary);
                        ps.setDouble(5, allowance);
                        ps.setDouble(6, deductions);
                        ps.setDouble(7, netSalary);
                        ps.addBatch();

                        batchCount++;
                        totalSalaries++;

                        if (batchCount >= BATCH_SIZE) {
                            ps.executeBatch();
                            batchCount = 0;
                            System.out.println("Generated " + totalSalaries + " salary records...");
                        }
                    }
                }
            }

            if (batchCount > 0) {
                ps.executeBatch();
            }

            ps.close();
            System.out.println("Generated " + totalSalaries + " salary records");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void generateDecisions() {
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            System.out.println("Generating decisions...");

            String sql = "INSERT INTO Decisions (EmployeeID, DecisionType, DecisionDate, Details) " +
                    "VALUES (?, ?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql);
            int batchCount = 0;
            int totalDecisions = 0;

            // Generate 0-5 decisions per employee
            for (Integer empId : employeeIds) {
                int numDecisions = random.nextInt(6); // 0-5 decisions

                for (int i = 0; i < numDecisions; i++) {
                    String decisionType = DECISION_TYPES[random.nextInt(DECISION_TYPES.length)];
                    LocalDate decisionDate = randomDate(2020, 2025);
                    String details = "Chi tiết quyết định " + decisionType + " cho nhân viên " + empId;

                    ps.setInt(1, empId);
                    ps.setString(2, decisionType);
                    ps.setDate(3, java.sql.Date.valueOf(decisionDate));
                    ps.setString(4, details);
                    ps.addBatch();

                    batchCount++;
                    totalDecisions++;

                    if (batchCount >= BATCH_SIZE) {
                        ps.executeBatch();
                        batchCount = 0;
                        System.out.println("Generated " + totalDecisions + " decisions...");
                    }
                }
            }

            if (batchCount > 0) {
                ps.executeBatch();
            }

            ps.close();
            System.out.println("Generated " + totalDecisions + " decisions");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void generateLeaveBalances() {
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            System.out.println("Generating leave balances...");

            String sql = "INSERT INTO LeaveBalances (EmployeeID, Year, TotalLeaveDays, UsedLeaveDays, RemainingLeaveDays) " +
                    "VALUES (?, ?, ?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql);
            int batchCount = 0;
            int totalRecords = 0;

            // Generate leave balances for 2022-2023 for each employee
            for (Integer empId : employeeIds) {
                for (int year = 2022; year <= 2025; year++) {
                    int totalDays = 12; // Standard 12 days per year
                    int usedDays = random.nextInt(totalDays + 1); // 0 to totalDays used
                    int remainingDays = totalDays - usedDays;

                    ps.setInt(1, empId);
                    ps.setInt(2, year);
                    ps.setInt(3, totalDays);
                    ps.setInt(4, usedDays);
                    ps.setInt(5, remainingDays);
                    ps.addBatch();

                    batchCount++;
                    totalRecords++;

                    if (batchCount >= BATCH_SIZE) {
                        ps.executeBatch();
                        batchCount = 0;
                        System.out.println("Generated " + totalRecords + " leave balance records...");
                    }
                }
            }

            if (batchCount > 0) {
                ps.executeBatch();
            }

            ps.close();
            System.out.println("Generated " + totalRecords + " leave balance records");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void generateNotifications() {
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            System.out.println("Generating notifications...");

            String sql = "INSERT INTO Notifications (Title, Content, CreatedDate, CreatedBy) " +
                    "VALUES (?, ?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql);
            int totalNotifications = 0;

            // Generate 100 notifications
            for (int i = 0; i < 100; i++) {
                String title = "Thông báo " + (i + 1);
                String content = "Nội dung thông báo " + (i + 1) + ": " + generateRandomText(50, 200);
                LocalDateTime createdDate = LocalDate.now().minusDays(random.nextInt(365)).atTime(
                        random.nextInt(24), random.nextInt(60));
                Integer createdBy = employeeIds.get(random.nextInt(employeeIds.size()));

                ps.setString(1, title);
                ps.setString(2, content);
                ps.setTimestamp(3, java.sql.Timestamp.valueOf(createdDate));
                ps.setInt(4, createdBy);
                ps.addBatch();

                totalNotifications++;
            }

            ps.executeBatch();
            ps.close();

            System.out.println("Generated " + totalNotifications + " notifications");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void generateRegistrations() {
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            System.out.println("Generating registrations...");

            String sql = "INSERT INTO Registrations (EmployeeID, RegistrationType, RequestDate, Details, Status, ApprovedBy) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql);
            int batchCount = 0;
            int totalRegistrations = 0;

            // Generate 0-10 registrations per employee
            for (Integer empId : employeeIds) {
                int numRegistrations = random.nextInt(11); // 0-10 registrations

                for (int i = 0; i < numRegistrations; i++) {
                    String regType = REGISTRATION_TYPES[random.nextInt(REGISTRATION_TYPES.length)];
                    LocalDate requestDate = randomDate(2022, 2025);
                    String details = "Chi tiết đăng ký " + regType;
                    String status = REQUEST_STATUS[random.nextInt(REQUEST_STATUS.length)];

                    // Find a different employee as approver
                    Integer approverEmpId = null;
                    if (status.equals("Đã duyệt") || status.equals("Từ chối")) {
                        do {
                            approverEmpId = employeeIds.get(random.nextInt(employeeIds.size()));
                        } while (approverEmpId.equals(empId));
                    }

                    ps.setInt(1, empId);
                    ps.setString(2, regType);
                    ps.setDate(3, java.sql.Date.valueOf(requestDate));
                    ps.setString(4, details);
                    ps.setString(5, status);

                    if (approverEmpId != null) {
                        ps.setInt(6, approverEmpId);
                    } else {
                        ps.setNull(6, java.sql.Types.INTEGER);
                    }

                    ps.addBatch();

                    batchCount++;
                    totalRegistrations++;

                    if (batchCount >= BATCH_SIZE) {
                        ps.executeBatch();
                        batchCount = 0;
                        System.out.println("Generated " + totalRegistrations + " registrations...");
                    }
                }
            }

            if (batchCount > 0) {
                ps.executeBatch();
            }

            ps.close();
            System.out.println("Generated " + totalRegistrations + " registrations");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void generateWorkTripRequests() {
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            System.out.println("Generating work trip requests...");

            String sql = "INSERT INTO WorkTripRequests (EmployeeID, Destination, StartDate, EndDate, Purpose, Status) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql);
            int batchCount = 0;
            int totalRequests = 0;

            // Generate 0-5 work trip requests per employee
            for (Integer empId : employeeIds) {
                int numRequests = random.nextInt(6); // 0-5 work trip requests

                for (int i = 0; i < numRequests; i++) {
                    String destination = CITIES[random.nextInt(CITIES.length)];
                    LocalDate startDate = randomDate(2022, 2025);
                    LocalDate endDate = startDate.plusDays(random.nextInt(10) + 1); // 1-10 days trip
                    String purpose = "Mục đích công tác tại " + destination;
                    String status = REQUEST_STATUS[random.nextInt(REQUEST_STATUS.length)];

                    ps.setInt(1, empId);
                    ps.setString(2, destination);
                    ps.setDate(3, java.sql.Date.valueOf(startDate));
                    ps.setDate(4, java.sql.Date.valueOf(endDate));
                    ps.setString(5, purpose);
                    ps.setString(6, status);
                    ps.addBatch();

                    batchCount++;
                    totalRequests++;

                    if (batchCount >= BATCH_SIZE) {
                        ps.executeBatch();
                        batchCount = 0;
                        System.out.println("Generated " + totalRequests + " work trip requests...");
                    }
                }
            }

            if (batchCount > 0) {
                ps.executeBatch();
            }

            ps.close();
            System.out.println("Generated " + totalRequests + " work trip requests");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void generateAttendance() {
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            System.out.println("Generating attendance records...");

            String sql = "INSERT INTO Attendance (EmployeeID, Date, CheckInTime, CheckOutTime, Status) " +
                    "VALUES (?, ?, ?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql);
            int batchCount = 0;
            int totalRecords = 0;

            // Generate attendance for the first 100 employees for the past 30 days
            int employeeLimit = Math.min(employeeIds.size(), 100);
            LocalDate today = LocalDate.now();

            for (int i = 0; i < employeeLimit; i++) {
                Integer empId = employeeIds.get(i);

                for (int day = 0; day < 30; day++) {
                    LocalDate date = today.minusDays(day);

                    // Skip weekends
                    if (date.getDayOfWeek().getValue() >= 6) {
                        continue;
                    }

                    String status = ATTENDANCE_STATUS[random.nextInt(ATTENDANCE_STATUS.length)];

                    // Generate check-in and check-out times based on status
                    LocalTime checkInTime = null;
                    LocalTime checkOutTime = null;

                    if (status.equals("Có mặt")) {
                        checkInTime = LocalTime.of(8, random.nextInt(30));
                        checkOutTime = LocalTime.of(17, 30 + random.nextInt(30));
                    } else if (status.equals("Đi muộn")) {
                        checkInTime = LocalTime.of(9, random.nextInt(30));
                        checkOutTime = LocalTime.of(17, 30 + random.nextInt(30));
                    } else if (status.equals("Làm thêm giờ")) {
                        checkInTime = LocalTime.of(8, random.nextInt(30));
                        checkOutTime = LocalTime.of(19, random.nextInt(60));
                    }

                    ps.setInt(1, empId);
                    ps.setDate(2, java.sql.Date.valueOf(date));

                    if (checkInTime != null) {
                        ps.setTime(3, java.sql.Time.valueOf(checkInTime));
                    } else {
                        ps.setNull(3, java.sql.Types.TIME);
                    }

                    if (checkOutTime != null) {
                        ps.setTime(4, java.sql.Time.valueOf(checkOutTime));
                    } else {
                        ps.setNull(4, java.sql.Types.TIME);
                    }

                    ps.setString(5, status);
                    ps.addBatch();

                    batchCount++;
                    totalRecords++;

                    if (batchCount >= BATCH_SIZE) {
                        ps.executeBatch();
                        batchCount = 0;
                        System.out.println("Generated " + totalRecords + " attendance records...");
                    }
                }
            }

            if (batchCount > 0) {
                ps.executeBatch();
            }

            ps.close();
            System.out.println("Generated " + totalRecords + " attendance records");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void generateInsurance() {
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            System.out.println("Generating insurance records...");

            String sql = "INSERT INTO Insurance (EmployeeID, InsuranceNumber, InsuranceType, StartDate, EndDate) " +
                    "VALUES (?, ?, ?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql);
            int batchCount = 0;
            int totalRecords = 0;

            // Generate 1-3 insurance records per employee
            for (Integer empId : employeeIds) {
                int numInsurances = random.nextInt(3) + 1; // 1-3 insurance records

                for (int i = 0; i < numInsurances; i++) {
                    String insuranceNumber = "BH" + empId + "_" + (i + 1) + "_" + System.currentTimeMillis() % 10000;
                    String insuranceType = INSURANCE_TYPES[random.nextInt(INSURANCE_TYPES.length)];
                    LocalDate startDate = randomDate(2015, 2025);
                    LocalDate endDate = startDate.plusYears(random.nextInt(5) + 1); // 1-5 years

                    ps.setInt(1, empId);
                    ps.setString(2, insuranceNumber);
                    ps.setString(3, insuranceType);
                    ps.setDate(4, java.sql.Date.valueOf(startDate));
                    ps.setDate(5, java.sql.Date.valueOf(endDate));
                    ps.addBatch();

                    batchCount++;
                    totalRecords++;

                    if (batchCount >= BATCH_SIZE) {
                        ps.executeBatch();
                        batchCount = 0;
                        System.out.println("Generated " + totalRecords + " insurance records...");
                    }
                }
            }

            if (batchCount > 0) {
                ps.executeBatch();
            }

            ps.close();
            System.out.println("Generated " + totalRecords + " insurance records");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void generateRecruitmentPlans() {
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            System.out.println("Generating recruitment plans...");

            String sql = "INSERT INTO RecruitmentPlans (PositionID, DepartmentID, Quantity, StartDate, EndDate) " +
                    "VALUES (?, ?, ?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);

            // Generate 50 recruitment plans
            for (int i = 0; i < 50; i++) {
                int posId = positionIds.get(random.nextInt(positionIds.size()));
                int deptId = departmentIds.get(random.nextInt(departmentIds.size()));
                int quantity = random.nextInt(10) + 1; // 1-10 positions
                LocalDate startDate = randomDate(2022, 2025);
                LocalDate endDate = startDate.plusMonths(random.nextInt(6) + 1); // 1-6 months

                ps.setInt(1, posId);
                ps.setInt(2, deptId);
                ps.setInt(3, quantity);
                ps.setDate(4, java.sql.Date.valueOf(startDate));
                ps.setDate(5, java.sql.Date.valueOf(endDate));
                ps.executeUpdate();

                // Get generated keys
                try (var rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        planIds.add(rs.getInt(1));
                    }
                }
            }

            ps.close();
            System.out.println("Generated " + planIds.size() + " recruitment plans");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void generateApplicants() {
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            System.out.println("Generating applicants...");

            String sql = "INSERT INTO Applicants (PlanID, FullName, Email, Phone, Status) " +
                    "VALUES (?, ?, ?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql);
            int batchCount = 0;
            int totalApplicants = 0;

            // Generate 5-20 applicants per recruitment plan
            for (Integer planId : planIds) {
                int numApplicants = random.nextInt(16) + 5; // 5-20 applicants

                for (int i = 0; i < numApplicants; i++) {
                    // Generate applicant data
                    String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
                    String middleName = MIDDLE_NAMES[random.nextInt(MIDDLE_NAMES.length)];
                    String firstName = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
                    String fullName = lastName + " " + middleName + " " + firstName;

                    String email = "applicant" + totalApplicants + "@gmail.com";
                    String phone = generateRandomPhone();
                    String status = APPLICANT_STATUS[random.nextInt(APPLICANT_STATUS.length)];

                    ps.setInt(1, planId);
                    ps.setString(2, fullName);
                    ps.setString(3, email);
                    ps.setString(4, phone);
                    ps.setString(5, status);
                    ps.addBatch();

                    batchCount++;
                    totalApplicants++;

                    if (batchCount >= BATCH_SIZE) {
                        ps.executeBatch();
                        batchCount = 0;
                        System.out.println("Generated " + totalApplicants + " applicants...");
                    }
                }
            }

            if (batchCount > 0) {
                ps.executeBatch();
            }

            ps.close();
            System.out.println("Generated " + totalApplicants + " applicants");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Utility method to generate a random date between startYear and endYear
    private LocalDate randomDate(int startYear, int endYear) {
        int year = startYear + random.nextInt(endYear - startYear + 1);
        int month = random.nextInt(12) + 1;
        int day = random.nextInt(28) + 1; // Simplified for all months
        return LocalDate.of(year, month, day);
    }

    // Generate a random address
    private String generateRandomAddress() {
        int number = random.nextInt(100) + 1;
        String street = STREETS[random.nextInt(STREETS.length)];
        String district = DISTRICTS[random.nextInt(DISTRICTS.length)];
        String city = CITIES[random.nextInt(CITIES.length)];

        return number + " " + street + ", " + district + ", " + city;
    }

    // Generate a random phone number
    private String generateRandomPhone() {
        String[] prefixes = {"090", "091", "092", "093", "094", "096", "097", "098", "099"};
        String prefix = prefixes[random.nextInt(prefixes.length)];

        StringBuilder sb = new StringBuilder(prefix);
        for (int i = 0; i < 7; i++) {
            sb.append(random.nextInt(10));
        }

        return sb.toString();
    }

    // Generate random text of given length range
    private String generateRandomText(int minLength, int maxLength) {
        String[] words = {"công ty", "nhân viên", "thông báo", "lịch", "họp", "dự án", "kế hoạch",
                "báo cáo", "tài liệu", "khách hàng", "đối tác", "hợp đồng", "đào tạo",
                "phòng ban", "sự kiện", "chính sách", "qui định", "lương", "thưởng", "phúc lợi"};

        int length = minLength + random.nextInt(maxLength - minLength + 1);
        StringBuilder sb = new StringBuilder();

        while (sb.length() < length) {
            sb.append(words[random.nextInt(words.length)]).append(" ");
        }

        return sb.toString().trim();
    }
}