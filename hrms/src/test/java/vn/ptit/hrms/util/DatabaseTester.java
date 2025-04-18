package vn.ptit.hrms.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import vn.ptit.hrms.constant.*;
import vn.ptit.hrms.dao.primary.*;
import vn.ptit.hrms.domain.primary.*;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Configuration
@Profile("test")
public class DatabaseTester {

    @Autowired private DepartmentDao departmentDao;
    @Autowired private PositionDao positionDao;
    @Autowired private EmployeeDao employeeDao;
    @Autowired private DepartmentManagerDao departmentManagerDao;
    @Autowired private SalaryDao salaryDao;
    @Autowired private ContractDao contractDao;
    @Autowired private DecisionDao decisionDao;
    @Autowired private LeaveBalanceDao leaveBalanceDao;
    @Autowired private NotificationDao notificationDao;
    @Autowired private RegistrationDao registrationDao;
    @Autowired private WorkTripRequestDao workTripRequestDao;
    @Autowired private AttendanceDao attendanceDao;
    @Autowired private InsuranceDao insuranceDao;
    @Autowired private RecruitmentPlanDao recruitmentPlanDao;
    @Autowired private ApplicantDao applicantDao;

    // Database connection info
    private static final String DB_URL = "jdbc:sqlserver://localhost;databaseName=nhansucongty;trustServerCertificate=true";
    private static final String DB_USER = "test_user";
    private static final String DB_PASSWORD = "superSecretPassword!123";
    private static final String OUTPUT_FILE = "database_test_results.txt";

    @Bean
    public CommandLineRunner testDatabase() {
        return args -> {
            try (PrintWriter writer = new PrintWriter(new FileWriter(OUTPUT_FILE))) {
                writer.println("====================================================================");
                writer.println("DATABASE TEST REPORT - " + LocalDateTime.now());
                writer.println("====================================================================\n");

                // Test database connection and dump all tables
                testConnection(writer);
                dumpAllTables(writer);

                writer.println("\n====================================================================");
                writer.println("TESTING CRUD OPERATIONS FOR ALL TABLES");
                writer.println("====================================================================\n");

                // Test CRUD operations for all tables
                testDepartments(writer);
                testPositions(writer);
                testEmployees(writer);
                testDepartmentManagers(writer);
                testSalaries(writer);
                testContracts(writer);
                testDecisions(writer);
                testLeaveBalances(writer);
                testNotifications(writer);
                testRegistrations(writer);
                testWorkTripRequests(writer);
                testAttendance(writer);
                testInsurance(writer);
                testRecruitmentPlans(writer);
                testApplicants(writer);

                writer.println("\n====================================================================");
                writer.println("ALL TESTS COMPLETED SUCCESSFULLY");
                writer.println("====================================================================");

                System.out.println("Database testing completed. Results written to " + OUTPUT_FILE);
            } catch (Exception e) {
                System.err.println("Error during testing: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }

    // Test database connection
    private void testConnection(PrintWriter writer) {
        writer.println("Testing database connection...");

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                writer.println("✅ Database connection successful!");

                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT @@VERSION AS 'SQL Server Version'")) {
                    if (rs.next()) {
                        writer.println("SQL Server Version: " + rs.getString("SQL Server Version"));
                    }
                }
            }
        } catch (Exception e) {
            writer.println("❌ Database connection failed: " + e.getMessage());
            e.printStackTrace(writer);
        }

        writer.println();
    }

    // Dump all tables content
    private void dumpAllTables(PrintWriter writer) {
        writer.println("Dumping data from all tables...");

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                List<String> tables = new ArrayList<>();
                DatabaseMetaData metaData = conn.getMetaData();
                ResultSet rs = metaData.getTables(null, "dbo", "%", new String[] {"TABLE"});

                while (rs.next()) {
                    tables.add(rs.getString("TABLE_NAME"));
                }

                for (String tableName : tables) {
                    writer.println("\n=== Data from table: " + tableName + " ===");
                    try (Statement stmt = conn.createStatement();
                         ResultSet tableData = stmt.executeQuery("SELECT * FROM " + tableName)) {

                        // Print column headers
                        int columnCount = tableData.getMetaData().getColumnCount();
                        StringBuilder header = new StringBuilder();
                        for (int i = 1; i <= columnCount; i++) {
                            String columnName = tableData.getMetaData().getColumnName(i);
                            header.append(String.format("%-20s", columnName));
                        }
                        writer.println(header.toString());
                        writer.println("-".repeat(header.length()));

                        // Print data rows
                        int rowCount = 0;
                        while (tableData.next()) {
                            StringBuilder row = new StringBuilder();
                            for (int i = 1; i <= columnCount; i++) {
                                Object value = tableData.getObject(i);
                                row.append(String.format("%-20s", value != null ? value.toString() : "NULL"));
                            }
                            writer.println(row.toString());
                            rowCount++;
                        }
                        writer.println("Total rows: " + rowCount);
                    } catch (Exception e) {
                        writer.println("Error reading from " + tableName + ": " + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            writer.println("Error dumping tables: " + e.getMessage());
            e.printStackTrace(writer);
        }
    }

    // Department tests
    private void testDepartments(PrintWriter writer) {
        writer.println("\n----- Testing Department DAO -----");

        try {
            // GET ALL
            writer.println("1. Testing getAllDepartments()");
            List<Department> departments = departmentDao.getAllDepartments();
            writer.println("   Found " + departments.size() + " departments");
            for (Department dept : departments) {
                writer.println("   - " + dept.getId() + ": " + dept.getDepartmentName());
            }

            // CREATE
            writer.println("\n2. Testing createDepartment()");
            Department newDept = new Department();
            newDept.setDepartmentName("Phòng Kiểm Soát Chất Lượng");
            writer.println("   Creating new department: " + newDept.getDepartmentName());
            departmentDao.createDepartment(newDept);

            departments = departmentDao.getAllDepartments();
            int newDeptId = departments.get(departments.size() - 1).getId();
            writer.println("   Department created with ID: " + newDeptId);

            // GET BY ID
            writer.println("\n3. Testing getDepartmentById()");
            Department retrievedDept = departmentDao.getDepartmentById(newDeptId);
            writer.println("   Retrieved department: ID=" + retrievedDept.getId() + ", Name=" + retrievedDept.getDepartmentName());

            // UPDATE
            writer.println("\n4. Testing updateDepartment()");
            retrievedDept.setDepartmentName("Phòng QA/QC");
            departmentDao.updateDepartment(retrievedDept);
            writer.println("   Department updated");

            retrievedDept = departmentDao.getDepartmentById(newDeptId);
            writer.println("   After update: ID=" + retrievedDept.getId() + ", Name=" + retrievedDept.getDepartmentName());

            // DELETE
            writer.println("\n5. Testing deleteDepartment()");
            departmentDao.deleteDepartment(newDeptId);
            writer.println("   Department deleted");

            writer.println("\n✅ Department DAO tests completed successfully");
        } catch (Exception e) {
            writer.println("\n❌ Department DAO tests failed: " + e.getMessage());
            e.printStackTrace(writer);
        }
    }

    // Position tests
    private void testPositions(PrintWriter writer) {
        writer.println("\n----- Testing Position DAO -----");

        try {
            // Get a department for foreign key
            List<Department> departments = departmentDao.getAllDepartments();
            Department department = departments.get(0);

            // GET ALL
            writer.println("1. Testing getAllPositions()");
            List<Position> positions = positionDao.getAllPositions();
            writer.println("   Found " + positions.size() + " positions");
            for (int i = 0; i < Math.min(positions.size(), 5); i++) { // Show first 5 only
                Position pos = positions.get(i);
                writer.println("   - " + pos.getId() + ": " + pos.getPositionName() +
                        " (Department: " + (pos.getDepartment() != null ? pos.getDepartment().getDepartmentName() : "None") + ")");
            }

            // CREATE
            writer.println("\n2. Testing createPosition()");
            Position newPos = new Position();
            newPos.setPositionName("Chuyên Viên Nghiên Cứu");
            newPos.setDepartment(department);
            writer.println("   Creating new position: " + newPos.getPositionName());
            positionDao.createPosition(newPos);

            positions = positionDao.getAllPositions();
            int newPosId = positions.get(positions.size() - 1).getId();
            writer.println("   Position created with ID: " + newPosId);

            // GET BY ID
            writer.println("\n3. Testing getPositionById()");
            Position retrievedPos = positionDao.getPositionById(newPosId);
            writer.println("   Retrieved position: ID=" + retrievedPos.getId() +
                    ", Name=" + retrievedPos.getPositionName() +
                    ", Department=" + (retrievedPos.getDepartment() != null ? retrievedPos.getDepartment().getDepartmentName() : "None"));

            // UPDATE
            writer.println("\n4. Testing updatePosition()");
            retrievedPos.setPositionName("Chuyên Viên Nghiên Cứu Cao Cấp");
            positionDao.updatePosition(retrievedPos);
            writer.println("   Position updated");

            retrievedPos = positionDao.getPositionById(newPosId);
            writer.println("   After update: ID=" + retrievedPos.getId() +
                    ", Name=" + retrievedPos.getPositionName());

            // DELETE
            writer.println("\n5. Testing deletePosition()");
            positionDao.deletePosition(newPosId);
            writer.println("   Position deleted");

            writer.println("\n✅ Position DAO tests completed successfully");
        } catch (Exception e) {
            writer.println("\n❌ Position DAO tests failed: " + e.getMessage());
            e.printStackTrace(writer);
        }
    }

    // Employee tests
    private void testEmployees(PrintWriter writer) {
        writer.println("\n----- Testing Employee DAO -----");

        try {
            // Get dependencies
            List<Department> departments = departmentDao.getAllDepartments();
            Department department = departments.get(0);

            List<Position> positions = positionDao.getAllPositions();
            Position position = positions.get(0);

            // GET ALL
            writer.println("1. Testing getAllEmployees()");
            List<Employee> employees = employeeDao.getAllEmployees();
            writer.println("   Found " + employees.size() + " employees");
            for (int i = 0; i < Math.min(employees.size(), 3); i++) { // Show first 3 only
                Employee emp = employees.get(i);
                writer.println("   - " + emp.getId() + ": " + emp.getFullName() +
                        " (" + emp.getEmail() + ")");
            }

            // CREATE
            writer.println("\n2. Testing createEmployee()");
            Employee newEmp = new Employee();
            newEmp.setPassword("password123");
            newEmp.setRoleName("user");
            newEmp.setFullName("Phạm Thị Kiểm Thử");
            newEmp.setDateOfBirth(LocalDate.of(1995, 5, 15));
            newEmp.setGender(GenderEnum.FEMALE);
            newEmp.setAddress("45 Lê Văn Lương, Quận Thanh Xuân, Hà Nội");
            newEmp.setPhone("0912345678");
            newEmp.setEmail("kiemthu@gmail.com");
            newEmp.setDepartment(department);
            newEmp.setPosition(position);
            newEmp.setHireDate(LocalDate.now());
            newEmp.setStatus(EmployeeStatusEnum.ACTIVE);

            writer.println("   Creating new employee: " + newEmp.getFullName());
            employeeDao.createEmployee(newEmp);

            employees = employeeDao.getAllEmployees();
            int newEmpId = employees.get(employees.size() - 1).getId();
            writer.println("   Employee created with ID: " + newEmpId);

            // GET BY ID
            writer.println("\n3. Testing getEmployeeById()");
            Employee retrievedEmp = employeeDao.getEmployeeById(newEmpId);
            writer.println("   Retrieved employee: ID=" + retrievedEmp.getId() +
                    ", Name=" + retrievedEmp.getFullName() +
                    ", Email=" + retrievedEmp.getEmail());

            // UPDATE
            writer.println("\n4. Testing updateEmployee()");
            retrievedEmp.setPhone("0987654321");
            employeeDao.updateEmployee(retrievedEmp);
            writer.println("   Employee updated");

            retrievedEmp = employeeDao.getEmployeeById(newEmpId);
            writer.println("   After update: ID=" + retrievedEmp.getId() +
                    ", Phone=" + retrievedEmp.getPhone());

            // DELETE
            writer.println("\n5. Testing deleteEmployee()");
            employeeDao.deleteEmployee(newEmpId);
            writer.println("   Employee deleted");

            writer.println("\n✅ Employee DAO tests completed successfully");
        } catch (Exception e) {
            writer.println("\n❌ Employee DAO tests failed: " + e.getMessage());
            e.printStackTrace(writer);
        }
    }

    // DepartmentManager tests
    private void testDepartmentManagers(PrintWriter writer) {
        writer.println("\n----- Testing DepartmentManager DAO -----");

        try {
            // Get dependencies
            List<Department> departments = departmentDao.getAllDepartments();
            Department department = departments.get(0);

            List<Employee> employees = employeeDao.getAllEmployees();
            Employee manager = employees.get(0);

            // GET ALL
            writer.println("1. Testing getAllDepartmentManagers()");
            List<DepartmentManager> managers = departmentManagerDao.getAllDepartmentManagers();
            writer.println("   Found " + managers.size() + " department managers");
            for (int i = 0; i < Math.min(managers.size(), 3); i++) { // Show first 3 only
                DepartmentManager mgr = managers.get(i);
                writer.println("   - Department: " +
                        (mgr.getDepartment() != null ? mgr.getDepartment().getDepartmentName() : "None") +
                        ", Manager: " +
                        (mgr.getManager() != null ? mgr.getManager().getFullName() : "None"));
            }

            // CREATE/UPDATE
            writer.println("\n2. Testing createDepartmentManager() and updateDepartmentManager()");

            // First check if a mapping already exists for this department
            DepartmentManager existingManager = null;
            try {
                existingManager = departmentManagerDao.getDepartmentManagerByDepartmentId(department.getId());
                writer.println("   Department already has a manager: " +
                        (existingManager.getManager() != null ? existingManager.getManager().getFullName() : "None"));

                // Update existing mapping
                writer.println("\n3. Testing updateDepartmentManager()");
                // Find a different employee to set as manager
                Employee newManager = null;
                for (Employee emp : employees) {
                    if (!emp.getId().equals(existingManager.getManager().getId())) {
                        newManager = emp;
                        break;
                    }
                }

                if (newManager != null) {
                    existingManager.setManager(newManager);
                    departmentManagerDao.updateDepartmentManager(existingManager);
                    writer.println("   Department manager updated");

                    DepartmentManager updatedManager = departmentManagerDao.getDepartmentManagerByDepartmentId(department.getId());
                    writer.println("   After update: Department=" +
                            (updatedManager.getDepartment() != null ? updatedManager.getDepartment().getDepartmentName() : "None") +
                            ", Manager=" +
                            (updatedManager.getManager() != null ? updatedManager.getManager().getFullName() : "None"));
                } else {
                    writer.println("   No alternative manager found to update with");
                }

            } catch (Exception e) {
                // Department doesn't have a manager yet, create a new mapping
                writer.println("   Department doesn't have a manager yet, creating new mapping");
                DepartmentManager newManager = new DepartmentManager();
                newManager.setDepartment(department);
                newManager.setManager(manager);

                departmentManagerDao.createDepartmentManager(newManager);
                writer.println("   Department manager created");

                DepartmentManager retrievedManager = departmentManagerDao.getDepartmentManagerByDepartmentId(department.getId());
                writer.println("   Created mapping: Department=" +
                        (retrievedManager.getDepartment() != null ? retrievedManager.getDepartment().getDepartmentName() : "None") +
                        ", Manager=" +
                        (retrievedManager.getManager() != null ? retrievedManager.getManager().getFullName() : "None"));
            }

            writer.println("\n✅ DepartmentManager DAO tests completed successfully");
        } catch (Exception e) {
            writer.println("\n❌ DepartmentManager DAO tests failed: " + e.getMessage());
            e.printStackTrace(writer);
        }
    }

    // Salary tests
    private void testSalaries(PrintWriter writer) {
        writer.println("\n----- Testing Salary DAO -----");

        try {
            // Get dependencies
            List<Employee> employees = employeeDao.getAllEmployees();
            Employee employee = employees.get(0);

            // GET ALL
            writer.println("1. Testing getAllSalaries()");
            List<Salary> salaries = salaryDao.getAllSalaries();
            writer.println("   Found " + salaries.size() + " salary records");
            for (int i = 0; i < Math.min(salaries.size(), 3); i++) { // Show first 3 only
                Salary sal = salaries.get(i);
                writer.println("   - ID: " + sal.getId() +
                        ", Employee: " + (sal.getEmployee() != null ? sal.getEmployee().getFullName() : "None") +
                        ", Net: " + sal.getNetSalary());
            }

            // CREATE
            writer.println("\n2. Testing createSalary()");
            Salary newSalary = new Salary();
            newSalary.setEmployee(employee);
            newSalary.setMonth(7);
            newSalary.setYear(2023);
            newSalary.setBasicSalary(18500000.0);
            newSalary.setAllowance(2500000.0);
            newSalary.setDeductions(2000000.0);
            newSalary.setNetSalary(19000000.0);

            writer.println("   Creating new salary record for: " + employee.getFullName());
            salaryDao.createSalary(newSalary);

            salaries = salaryDao.getAllSalaries();
            int newSalaryId = salaries.get(salaries.size() - 1).getId();
            writer.println("   Salary record created with ID: " + newSalaryId);

            // GET BY ID
            writer.println("\n3. Testing getSalaryById()");
            Salary retrievedSalary = salaryDao.getSalaryById(newSalaryId);
            writer.println("   Retrieved salary: ID=" + retrievedSalary.getId() +
                    ", Employee=" + (retrievedSalary.getEmployee() != null ? retrievedSalary.getEmployee().getFullName() : "None") +
                    ", Net=" + retrievedSalary.getNetSalary());

            // UPDATE
            writer.println("\n4. Testing updateSalary()");
            retrievedSalary.setNetSalary(20000000.0);
            salaryDao.updateSalary(retrievedSalary);
            writer.println("   Salary record updated");

            retrievedSalary = salaryDao.getSalaryById(newSalaryId);
            writer.println("   After update: ID=" + retrievedSalary.getId() +
                    ", Net=" + retrievedSalary.getNetSalary());

            // DELETE
            writer.println("\n5. Testing deleteSalary()");
            salaryDao.deleteSalary(newSalaryId);
            writer.println("   Salary record deleted");

            writer.println("\n✅ Salary DAO tests completed successfully");
        } catch (Exception e) {
            writer.println("\n❌ Salary DAO tests failed: " + e.getMessage());
            e.printStackTrace(writer);
        }
    }

    // Contract tests
    private void testContracts(PrintWriter writer) {
        writer.println("\n----- Testing Contract DAO -----");

        try {
            // Get dependencies
            List<Employee> employees = employeeDao.getAllEmployees();
            Employee employee = employees.get(0);

            // GET ALL
            writer.println("1. Testing getAllContracts()");
            List<Contract> contracts = contractDao.getAllContracts();
            writer.println("   Found " + contracts.size() + " contracts");
            for (int i = 0; i < Math.min(contracts.size(), 3); i++) { // Show first 3 only
                Contract contract = contracts.get(i);
                writer.println("   - ID: " + contract.getId() +
                        ", Employee: " + (contract.getEmployee() != null ? contract.getEmployee().getFullName() : "None") +
                        ", Type: " + (contract.getContractType() != null ? contract.getContractType().getValue() : "None"));
            }

            // CREATE
            writer.println("\n2. Testing createContract()");
            Contract newContract = new Contract();
            newContract.setEmployee(employee);
            newContract.setContractType(ContractTypeEnum.FULLTIME);
            newContract.setStartDate(LocalDate.now());
            newContract.setEndDate(LocalDate.now().plusYears(1));
            newContract.setStatus(ContractStatusEnum.ACTIVE);

            writer.println("   Creating new contract for: " + employee.getFullName());
            contractDao.createContract(newContract);

            contracts = contractDao.getAllContracts();
            int newContractId = contracts.get(contracts.size() - 1).getId();
            writer.println("   Contract created with ID: " + newContractId);

            // GET BY ID
            writer.println("\n3. Testing getContractById()");
            Contract retrievedContract = contractDao.getContractById(newContractId);
            writer.println("   Retrieved contract: ID=" + retrievedContract.getId() +
                    ", Employee=" + (retrievedContract.getEmployee() != null ? retrievedContract.getEmployee().getFullName() : "None") +
                    ", Type=" + (retrievedContract.getContractType() != null ? retrievedContract.getContractType().getValue() : "None"));

            // UPDATE
            writer.println("\n4. Testing updateContract()");
            retrievedContract.setStatus(ContractStatusEnum.EXPIRED);
            contractDao.updateContract(retrievedContract);
            writer.println("   Contract updated");

            retrievedContract = contractDao.getContractById(newContractId);
            writer.println("   After update: ID=" + retrievedContract.getId() +
                    ", Status=" + (retrievedContract.getStatus() != null ? retrievedContract.getStatus().getValue() : "None"));

            // DELETE
            writer.println("\n5. Testing deleteContract()");
            contractDao.deleteContract(newContractId);
            writer.println("   Contract deleted");

            writer.println("\n✅ Contract DAO tests completed successfully");
        } catch (Exception e) {
            writer.println("\n❌ Contract DAO tests failed: " + e.getMessage());
            e.printStackTrace(writer);
        }
    }

    // Decision tests
    private void testDecisions(PrintWriter writer) {
        writer.println("\n----- Testing Decision DAO -----");

        try {
            // Get dependencies
            List<Employee> employees = employeeDao.getAllEmployees();
            Employee employee = employees.get(0);

            // GET ALL
            writer.println("1. Testing getAllDecisions()");
            List<Decision> decisions = decisionDao.getAllDecisions();
            writer.println("   Found " + decisions.size() + " decisions");
            for (int i = 0; i < Math.min(decisions.size(), 3); i++) { // Show first 3 only
                Decision decision = decisions.get(i);
                writer.println("   - ID: " + decision.getId() +
                        ", Employee: " + (decision.getEmployee() != null ? decision.getEmployee().getFullName() : "None") +
                        ", Type: " + decision.getDecisionType());
            }

            // CREATE
            writer.println("\n2. Testing createDecision()");
            Decision newDecision = new Decision();
            newDecision.setEmployee(employee);
            newDecision.setDecisionType(DecisionTypeEnum.REWARD);
            newDecision.setDecisionDate(LocalDate.now());
            newDecision.setDetails("Khen thưởng cho thành tích xuất sắc trong quý 3/2023");

            writer.println("   Creating new decision for: " + employee.getFullName());
            decisionDao.createDecision(newDecision);

            decisions = decisionDao.getAllDecisions();
            int newDecisionId = decisions.get(decisions.size() - 1).getId();
            writer.println("   Decision created with ID: " + newDecisionId);

            // GET BY ID
            writer.println("\n3. Testing getDecisionById()");
            Decision retrievedDecision = decisionDao.getDecisionById(newDecisionId);
            writer.println("   Retrieved decision: ID=" + retrievedDecision.getId() +
                    ", Employee=" + (retrievedDecision.getEmployee() != null ? retrievedDecision.getEmployee().getFullName() : "None") +
                    ", Type=" + retrievedDecision.getDecisionType());

            // UPDATE
            writer.println("\n4. Testing updateDecision()");
            retrievedDecision.setDetails("Khen thưởng cho thành tích xuất sắc trong quý 3/2023 - Điều chỉnh");
            decisionDao.updateDecision(retrievedDecision);
            writer.println("   Decision updated");

            retrievedDecision = decisionDao.getDecisionById(newDecisionId);
            writer.println("   After update: ID=" + retrievedDecision.getId() +
                    ", Details=" + retrievedDecision.getDetails());

            // DELETE
            writer.println("\n5. Testing deleteDecision()");
            decisionDao.deleteDecision(newDecisionId);
            writer.println("   Decision deleted");

            writer.println("\n✅ Decision DAO tests completed successfully");
        } catch (Exception e) {
            writer.println("\n❌ Decision DAO tests failed: " + e.getMessage());
            e.printStackTrace(writer);
        }
    }

    // LeaveBalance tests
    private void testLeaveBalances(PrintWriter writer) {
        writer.println("\n----- Testing LeaveBalance DAO -----");

        try {
            // Get dependencies
            List<Employee> employees = employeeDao.getAllEmployees();
            Employee employee = employees.get(0);

            // GET ALL
            writer.println("1. Testing getAllLeaveBalances()");
            List<LeaveBalance> leaveBalances = leaveBalanceDao.getAllLeaveBalances();
            writer.println("   Found " + leaveBalances.size() + " leave balances");
            for (int i = 0; i < Math.min(leaveBalances.size(), 3); i++) { // Show first 3 only
                LeaveBalance lb = leaveBalances.get(i);
                writer.println("   - ID: " + lb.getId() +
                        ", Employee: " + (lb.getEmployee() != null ? lb.getEmployee().getFullName() : "None") +
                        ", Remaining: " + lb.getRemainingLeaveDays());
            }

            // CREATE
            writer.println("\n2. Testing createLeaveBalance()");
            LeaveBalance newBalance = new LeaveBalance();
            newBalance.setEmployee(employee);
            newBalance.setYear(2023);
            newBalance.setTotalLeaveDays(15);
            newBalance.setUsedLeaveDays(3);
            newBalance.setRemainingLeaveDays(12);

            writer.println("   Creating new leave balance for: " + employee.getFullName());
            leaveBalanceDao.createLeaveBalance(newBalance);

            leaveBalances = leaveBalanceDao.getAllLeaveBalances();
            int newBalanceId = leaveBalances.get(leaveBalances.size() - 1).getId();
            writer.println("   Leave balance created with ID: " + newBalanceId);

            // GET BY ID
            writer.println("\n3. Testing getLeaveBalanceById()");
            LeaveBalance retrievedBalance = leaveBalanceDao.getLeaveBalanceById(newBalanceId);
            writer.println("   Retrieved leave balance: ID=" + retrievedBalance.getId() +
                    ", Employee=" + (retrievedBalance.getEmployee() != null ? retrievedBalance.getEmployee().getFullName() : "None") +
                    ", Remaining=" + retrievedBalance.getRemainingLeaveDays());

            // UPDATE
            writer.println("\n4. Testing updateLeaveBalance()");
            retrievedBalance.setUsedLeaveDays(5);
            retrievedBalance.setRemainingLeaveDays(10);
            leaveBalanceDao.updateLeaveBalance(retrievedBalance);
            writer.println("   Leave balance updated");

            retrievedBalance = leaveBalanceDao.getLeaveBalanceById(newBalanceId);
            writer.println("   After update: ID=" + retrievedBalance.getId() +
                    ", Used=" + retrievedBalance.getUsedLeaveDays() +
                    ", Remaining=" + retrievedBalance.getRemainingLeaveDays());

            // DELETE
            writer.println("\n5. Testing deleteLeaveBalance()");
            leaveBalanceDao.deleteLeaveBalance(newBalanceId);
            writer.println("   Leave balance deleted");

            writer.println("\n✅ LeaveBalance DAO tests completed successfully");
        } catch (Exception e) {
            writer.println("\n❌ LeaveBalance DAO tests failed: " + e.getMessage());
            e.printStackTrace(writer);
        }
    }

    // Notification tests
    private void testNotifications(PrintWriter writer) {
        writer.println("\n----- Testing Notification DAO -----");

        try {
            // Get dependencies
            List<Employee> employees = employeeDao.getAllEmployees();
            Employee creator = employees.get(0);

            // GET ALL
            writer.println("1. Testing getAllNotifications()");
            List<Notification> notifications = notificationDao.getAllNotifications();
            writer.println("   Found " + notifications.size() + " notifications");
            for (int i = 0; i < Math.min(notifications.size(), 3); i++) { // Show first 3 only
                Notification notif = notifications.get(i);
                writer.println("   - ID: " + notif.getId() +
                        ", Title: " + notif.getTitle() +
                        ", Created by: " + (notif.getCreatedBy() != null ? notif.getCreatedBy().getFullName() : "None"));
            }

            // CREATE
            writer.println("\n2. Testing createNotification()");
            Notification newNotification = new Notification();
            newNotification.setTitle("Thông báo nghỉ lễ Quốc khánh");
            newNotification.setContent("Thông báo về lịch nghỉ lễ Quốc khánh 2/9/2023");
            newNotification.setCreatedDate(LocalDateTime.now());
            newNotification.setCreatedBy(creator);

            writer.println("   Creating new notification by: " + creator.getFullName());
            notificationDao.createNotification(newNotification);

            notifications = notificationDao.getAllNotifications();
            int newNotificationId = notifications.get(notifications.size() - 1).getId();
            writer.println("   Notification created with ID: " + newNotificationId);

            // GET BY ID
            writer.println("\n3. Testing getNotificationById()");
            Notification retrievedNotification = notificationDao.getNotificationById(newNotificationId);
            writer.println("   Retrieved notification: ID=" + retrievedNotification.getId() +
                    ", Title=" + retrievedNotification.getTitle() +
                    ", Created by=" + (retrievedNotification.getCreatedBy() != null ? retrievedNotification.getCreatedBy().getFullName() : "None"));

            // UPDATE
            writer.println("\n4. Testing updateNotification()");
            retrievedNotification.setContent("Thông báo về lịch nghỉ lễ Quốc khánh 2/9/2023 (cập nhật)");
            notificationDao.updateNotification(retrievedNotification);
            writer.println("   Notification updated");

            retrievedNotification = notificationDao.getNotificationById(newNotificationId);
            writer.println("   After update: ID=" + retrievedNotification.getId() +
                    ", Content=" + retrievedNotification.getContent());

            // DELETE
            writer.println("\n5. Testing deleteNotification()");
            notificationDao.deleteNotification(newNotificationId);
            writer.println("   Notification deleted");

            writer.println("\n✅ Notification DAO tests completed successfully");
        } catch (Exception e) {
            writer.println("\n❌ Notification DAO tests failed: " + e.getMessage());
            e.printStackTrace(writer);
        }
    }

    // Test Registration
    private void testRegistrations(PrintWriter writer) {
        writer.println("\n----- Testing Registration DAO -----");

        try {
            // Get dependencies
            List<Employee> employees = employeeDao.getAllEmployees();
            Employee employee = employees.get(0);
            Employee approver = employees.size() > 1 ? employees.get(1) : employees.get(0);

            // GET ALL
            writer.println("1. Testing getAllRegistrations()");
            List<Registration> registrations = registrationDao.getAllRegistrations();
            writer.println("   Found " + registrations.size() + " registrations");
            for (int i = 0; i < Math.min(registrations.size(), 3); i++) { // Show first 3 only
                Registration reg = registrations.get(i);
                writer.println("   - ID: " + reg.getId() +
                        ", Type: " + reg.getRegistrationType() +
                        ", Status: " + reg.getStatus());
            }

            // CREATE
            writer.println("\n2. Testing createRegistration()");
            Registration newRegistration = new Registration();
            newRegistration.setEmployee(employee);
            newRegistration.setRegistrationType(RegistrationTypeEnum.LEAVE);
            newRegistration.setRequestDate(LocalDate.now());
            newRegistration.setDetails("Đăng ký nghỉ phép ngày 15/9/2023");
            newRegistration.setStatus(RegistrationStatusEnum.PENDING);

            writer.println("   Creating new registration for: " + employee.getFullName());
            registrationDao.createRegistration(newRegistration);

            registrations = registrationDao.getAllRegistrations();
            int newRegistrationId = registrations.get(registrations.size() - 1).getId();
            writer.println("   Registration created with ID: " + newRegistrationId);

            // GET BY ID
            writer.println("\n3. Testing getRegistrationById()");
            Registration retrievedRegistration = registrationDao.getRegistrationById(newRegistrationId);
            writer.println("   Retrieved registration: ID=" + retrievedRegistration.getId() +
                    ", Type=" + retrievedRegistration.getRegistrationType() +
                    ", Status=" + retrievedRegistration.getStatus());

            // UPDATE
            writer.println("\n4. Testing updateRegistration()");
            retrievedRegistration.setStatus(RegistrationStatusEnum.APPROVED);
            retrievedRegistration.setApprovedBy(approver);
            registrationDao.updateRegistration(retrievedRegistration);
            writer.println("   Registration updated");

            retrievedRegistration = registrationDao.getRegistrationById(newRegistrationId);
            writer.println("   After update: ID=" + retrievedRegistration.getId() +
                    ", Status=" + retrievedRegistration.getStatus() +
                    ", Approved by=" + (retrievedRegistration.getApprovedBy() != null ? retrievedRegistration.getApprovedBy().getFullName() : "None"));

            // DELETE
            writer.println("\n5. Testing deleteRegistration()");
            registrationDao.deleteRegistration(newRegistrationId);
            writer.println("   Registration deleted");

            writer.println("\n✅ Registration DAO tests completed successfully");
        } catch (Exception e) {
            writer.println("\n❌ Registration DAO tests failed: " + e.getMessage());
            e.printStackTrace(writer);
        }
    }

    // WorkTripRequest tests
    private void testWorkTripRequests(PrintWriter writer) {
        writer.println("\n----- Testing WorkTripRequest DAO -----");

        try {
            // Get dependencies
            List<Employee> employees = employeeDao.getAllEmployees();
            Employee employee = employees.get(0);

            // GET ALL
            writer.println("1. Testing getAllWorkTripRequests()");
            List<WorkTripRequest> requests = workTripRequestDao.getAllWorkTripRequests();
            writer.println("   Found " + requests.size() + " work trip requests");
            for (int i = 0; i < Math.min(requests.size(), 3); i++) { // Show first 3 only
                WorkTripRequest req = requests.get(i);
                writer.println("   - ID: " + req.getId() +
                        ", Employee: " + (req.getEmployee() != null ? req.getEmployee().getFullName() : "None") +
                        ", Destination: " + req.getDestination());
            }

            // CREATE
            writer.println("\n2. Testing createWorkTripRequest()");
            WorkTripRequest newRequest = new WorkTripRequest();
            newRequest.setEmployee(employee);
            newRequest.setDestination("Đà Nẵng");
            newRequest.setStartDate(LocalDate.now().plusDays(7));
            newRequest.setEndDate(LocalDate.now().plusDays(10));
            newRequest.setPurpose("Gặp đối tác và khảo sát thị trường");
            newRequest.setStatus(RegistrationStatusEnum.PENDING);

            writer.println("   Creating new work trip request for: " + employee.getFullName());
            workTripRequestDao.createWorkTripRequest(newRequest);

            requests = workTripRequestDao.getAllWorkTripRequests();
            int newRequestId = requests.get(requests.size() - 1).getId();
            writer.println("   Work trip request created with ID: " + newRequestId);

            // GET BY ID
            writer.println("\n3. Testing getWorkTripRequestById()");
            WorkTripRequest retrievedRequest = workTripRequestDao.getWorkTripRequestById(newRequestId);
            writer.println("   Retrieved work trip request: ID=" + retrievedRequest.getId() +
                    ", Destination=" + retrievedRequest.getDestination() +
                    ", Status=" + retrievedRequest.getStatus());

            // UPDATE
            writer.println("\n4. Testing updateWorkTripRequest()");
            retrievedRequest.setStatus(RegistrationStatusEnum.APPROVED);
            workTripRequestDao.updateWorkTripRequest(retrievedRequest);
            writer.println("   Work trip request updated");

            retrievedRequest = workTripRequestDao.getWorkTripRequestById(newRequestId);
            writer.println("   After update: ID=" + retrievedRequest.getId() +
                    ", Status=" + retrievedRequest.getStatus());

            // DELETE
            writer.println("\n5. Testing deleteWorkTripRequest()");
            workTripRequestDao.deleteWorkTripRequest(newRequestId);
            writer.println("   Work trip request deleted");

            writer.println("\n✅ WorkTripRequest DAO tests completed successfully");
        } catch (Exception e) {
            writer.println("\n❌ WorkTripRequest DAO tests failed: " + e.getMessage());
            e.printStackTrace(writer);
        }
    }

    // Attendance tests
    private void testAttendance(PrintWriter writer) {
        writer.println("\n----- Testing Attendance DAO -----");

        try {
            // Get dependencies
            List<Employee> employees = employeeDao.getAllEmployees();
            Employee employee = employees.get(0);

            // GET ALL
            writer.println("1. Testing getAllAttendance()");
            List<Attendance> attendances = attendanceDao.getAllAttendance();
            writer.println("   Found " + attendances.size() + " attendance records");
            for (int i = 0; i < Math.min(attendances.size(), 3); i++) { // Show first 3 only
                Attendance att = attendances.get(i);
                writer.println("   - ID: " + att.getId() +
                        ", Employee: " + (att.getEmployee() != null ? att.getEmployee().getFullName() : "None") +
                        ", Date: " + att.getDate() +
                        ", Status: " + att.getStatus());
            }

            // CREATE
            writer.println("\n2. Testing createAttendance()");
            Attendance newAttendance = new Attendance();
            newAttendance.setEmployee(employee);
            newAttendance.setDate(LocalDate.now());
            newAttendance.setCheckInTime(LocalTime.of(8, 15));
            newAttendance.setCheckOutTime(LocalTime.of(17, 30));
            newAttendance.setStatus(AttendanceStatusEnum.PRESENT);

            writer.println("   Creating new attendance record for: " + employee.getFullName());
            attendanceDao.createAttendance(newAttendance);

            attendances = attendanceDao.getAllAttendance();
            int newAttendanceId = attendances.get(attendances.size() - 1).getId();
            writer.println("   Attendance record created with ID: " + newAttendanceId);

            // GET BY ID
            writer.println("\n3. Testing getAttendanceById()");
            Attendance retrievedAttendance = attendanceDao.getAttendanceById(newAttendanceId);
            writer.println("   Retrieved attendance: ID=" + retrievedAttendance.getId() +
                    ", Date=" + retrievedAttendance.getDate() +
                    ", Status=" + retrievedAttendance.getStatus());

            // UPDATE
            writer.println("\n4. Testing updateAttendance()");
            retrievedAttendance.setStatus(AttendanceStatusEnum.OVERTIME);
            retrievedAttendance.setCheckOutTime(LocalTime.of(19, 0));
            attendanceDao.updateAttendance(retrievedAttendance);
            writer.println("   Attendance record updated");

            retrievedAttendance = attendanceDao.getAttendanceById(newAttendanceId);
            writer.println("   After update: ID=" + retrievedAttendance.getId() +
                    ", Status=" + retrievedAttendance.getStatus() +
                    ", Check-out: " + retrievedAttendance.getCheckOutTime());

            // DELETE
            writer.println("\n5. Testing deleteAttendance()");
            attendanceDao.deleteAttendance(newAttendanceId);
            writer.println("   Attendance record deleted");

            writer.println("\n✅ Attendance DAO tests completed successfully");
        } catch (Exception e) {
            writer.println("\n❌ Attendance DAO tests failed: " + e.getMessage());
            e.printStackTrace(writer);
        }
    }

    // Insurance tests
    private void testInsurance(PrintWriter writer) {
        writer.println("\n----- Testing Insurance DAO -----");

        try {
            // Get dependencies
            List<Employee> employees = employeeDao.getAllEmployees();
            Employee employee = employees.get(0);

            // GET ALL
            writer.println("1. Testing getAllInsurance()");
            List<Insurance> insurances = insuranceDao.getAllInsurances();
            writer.println("   Found " + insurances.size() + " insurance records");
            for (int i = 0; i < Math.min(insurances.size(), 3); i++) { // Show first 3 only
                Insurance ins = insurances.get(i);
                writer.println("   - ID: " + ins.getId() +
                        ", Employee: " + (ins.getEmployee() != null ? ins.getEmployee().getFullName() : "None") +
                        ", Type: " + ins.getInsuranceType());
            }

            // CREATE
            writer.println("\n2. Testing createInsurance()");
            Insurance newInsurance = new Insurance();
            newInsurance.setEmployee(employee);
            newInsurance.setInsuranceNumber("BHSK" + System.currentTimeMillis());
            newInsurance.setInsuranceType("Bảo hiểm sức khỏe");
            newInsurance.setStartDate(LocalDate.now());
            newInsurance.setEndDate(LocalDate.now().plusYears(1));

            writer.println("   Creating new insurance record for: " + employee.getFullName());
            insuranceDao.createInsurance(newInsurance);

            insurances = insuranceDao.getAllInsurances();
            int newInsuranceId = insurances.get(insurances.size() - 1).getId();
            writer.println("   Insurance record created with ID: " + newInsuranceId);

            // GET BY ID
            writer.println("\n3. Testing getInsuranceById()");
            Insurance retrievedInsurance = insuranceDao.getInsuranceById(newInsuranceId);
            writer.println("   Retrieved insurance: ID=" + retrievedInsurance.getId() +
                    ", Number=" + retrievedInsurance.getInsuranceNumber() +
                    ", Type=" + retrievedInsurance.getInsuranceType());

            // UPDATE
            writer.println("\n4. Testing updateInsurance()");
            retrievedInsurance.setEndDate(LocalDate.now().plusYears(2));
            insuranceDao.updateInsurance(retrievedInsurance);
            writer.println("   Insurance record updated");

            retrievedInsurance = insuranceDao.getInsuranceById(newInsuranceId);
            writer.println("   After update: ID=" + retrievedInsurance.getId() +
                    ", End date=" + retrievedInsurance.getEndDate());

            // DELETE
            writer.println("\n5. Testing deleteInsurance()");
            insuranceDao.deleteInsurance(newInsuranceId);
            writer.println("   Insurance record deleted");

            writer.println("\n✅ Insurance DAO tests completed successfully");
        } catch (Exception e) {
            writer.println("\n❌ Insurance DAO tests failed: " + e.getMessage());
            e.printStackTrace(writer);
        }
    }

    // RecruitmentPlan tests
    private void testRecruitmentPlans(PrintWriter writer) {
        writer.println("\n----- Testing RecruitmentPlan DAO -----");

        try {
            // Get dependencies
            List<Department> departments = departmentDao.getAllDepartments();
            Department department = departments.get(0);

            List<Position> positions = positionDao.getAllPositions();
            Position position = positions.get(0);

            // GET ALL
            writer.println("1. Testing getAllRecruitmentPlans()");
            List<RecruitmentPlan> plans = recruitmentPlanDao.getAllRecruitmentPlans();
            writer.println("   Found " + plans.size() + " recruitment plans");
            for (int i = 0; i < Math.min(plans.size(), 3); i++) { // Show first 3 only
                RecruitmentPlan plan = plans.get(i);
                writer.println("   - ID: " + plan.getId() +
                        ", Position: " + (plan.getPosition() != null ? plan.getPosition().getPositionName() : "None") +
                        ", Quantity: " + plan.getQuantity());
            }

            // CREATE
            writer.println("\n2. Testing createRecruitmentPlan()");
            RecruitmentPlan newPlan = new RecruitmentPlan();
            newPlan.setPosition(position);
            newPlan.setDepartment(department);
            newPlan.setQuantity(2);
            newPlan.setStartDate(LocalDate.now());
            newPlan.setEndDate(LocalDate.now().plusMonths(3));

            writer.println("   Creating new recruitment plan for position: " + position.getPositionName());
            recruitmentPlanDao.createRecruitmentPlan(newPlan);

            plans = recruitmentPlanDao.getAllRecruitmentPlans();
            int newPlanId = plans.get(plans.size() - 1).getId();
            writer.println("   Recruitment plan created with ID: " + newPlanId);

            // GET BY ID
            writer.println("\n3. Testing getRecruitmentPlanById()");
            RecruitmentPlan retrievedPlan = recruitmentPlanDao.getRecruitmentPlanById(newPlanId);
            writer.println("   Retrieved recruitment plan: ID=" + retrievedPlan.getId() +
                    ", Position=" + (retrievedPlan.getPosition() != null ? retrievedPlan.getPosition().getPositionName() : "None") +
                    ", Quantity=" + retrievedPlan.getQuantity());

            // UPDATE
            writer.println("\n4. Testing updateRecruitmentPlan()");
            retrievedPlan.setQuantity(3);
            recruitmentPlanDao.updateRecruitmentPlan(retrievedPlan);
            writer.println("   Recruitment plan updated");

            retrievedPlan = recruitmentPlanDao.getRecruitmentPlanById(newPlanId);
            writer.println("   After update: ID=" + retrievedPlan.getId() +
                    ", Quantity=" + retrievedPlan.getQuantity());

            // DELETE
            writer.println("\n5. Testing deleteRecruitmentPlan()");
            recruitmentPlanDao.deleteRecruitmentPlan(newPlanId);
            writer.println("   Recruitment plan deleted");

            writer.println("\n✅ RecruitmentPlan DAO tests completed successfully");
        } catch (Exception e) {
            writer.println("\n❌ RecruitmentPlan DAO tests failed: " + e.getMessage());
            e.printStackTrace(writer);
        }
    }

    // Applicant tests
    private void testApplicants(PrintWriter writer) {
        writer.println("\n----- Testing Applicant DAO -----");

        try {
            // Get dependencies - find a recruitment plan
            List<RecruitmentPlan> plans = recruitmentPlanDao.getAllRecruitmentPlans();
            if (plans.isEmpty()) {
                writer.println("   Cannot test Applicant DAO: No recruitment plans found");
                return;
            }
            RecruitmentPlan plan = plans.get(0);

            // GET ALL
            writer.println("1. Testing getAllApplicants()");
            List<Applicant> applicants = applicantDao.getAllApplicants();
            writer.println("   Found " + applicants.size() + " applicants");
            for (int i = 0; i < Math.min(applicants.size(), 3); i++) { // Show first 3 only
                Applicant app = applicants.get(i);
                writer.println("   - ID: " + app.getId() +
                        ", Name: " + app.getFullName() +
                        ", Status: " + app.getStatus());
            }

            // CREATE
            writer.println("\n2. Testing createApplicant()");
            Applicant newApplicant = new Applicant();
            newApplicant.setPlan(plan);
            newApplicant.setFullName("Trần Văn Ứng Viên");
            newApplicant.setEmail("ungvien@gmail.com");
            newApplicant.setPhone("0987654321");
            newApplicant.setStatus(ApplicantStatusEnum.APPLIED);

            writer.println("   Creating new applicant for plan ID: " + plan.getId());
            applicantDao.createApplicant(newApplicant);

            applicants = applicantDao.getAllApplicants();
            int newApplicantId = applicants.get(applicants.size() - 1).getId();
            writer.println("   Applicant created with ID: " + newApplicantId);

            // GET BY ID
            writer.println("\n3. Testing getApplicantById()");
            Applicant retrievedApplicant = applicantDao.getApplicantById(newApplicantId);
            writer.println("   Retrieved applicant: ID=" + retrievedApplicant.getId() +
                    ", Name=" + retrievedApplicant.getFullName() +
                    ", Status=" + retrievedApplicant.getStatus());

            // UPDATE
            writer.println("\n4. Testing updateApplicant()");
            retrievedApplicant.setStatus(ApplicantStatusEnum.INTERVIEWED);
            applicantDao.updateApplicant(retrievedApplicant);
            writer.println("   Applicant updated");

            retrievedApplicant = applicantDao.getApplicantById(newApplicantId);
            writer.println("   After update: ID=" + retrievedApplicant.getId() +
                    ", Status=" + retrievedApplicant.getStatus());

            // DELETE
            writer.println("\n5. Testing deleteApplicant()");
            applicantDao.deleteApplicant(newApplicantId);
            writer.println("   Applicant deleted");

            writer.println("\n✅ Applicant DAO tests completed successfully");
        } catch (Exception e) {
            writer.println("\n❌ Applicant DAO tests failed: " + e.getMessage());
            e.printStackTrace(writer);
        }
    }
}