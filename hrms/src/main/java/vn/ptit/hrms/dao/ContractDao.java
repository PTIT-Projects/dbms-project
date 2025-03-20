package vn.ptit.hrms.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import vn.ptit.hrms.domain.Contract;
import vn.ptit.hrms.mapper.ContractRowMapper;

import java.util.List;

@Repository
public class ContractDao {
    private final JdbcTemplate jdbcTemplate;
    private final ContractRowMapper contractRowMapper;

    public ContractDao(JdbcTemplate jdbcTemplate, ContractRowMapper contractRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.contractRowMapper = contractRowMapper;
    }

    // Method to create a new contract
    public void createContract(Contract contract) {
        String sql = "INSERT INTO Contracts (EmployeeID, ContractType, StartDate, EndDate, Salary, Status) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                contract.getEmployee().getId(),
                contract.getContractType(),
                contract.getStartDate(),
                contract.getEndDate(),
                contract.getSalary(),
                contract.getStatus().getValue());
    }

    // Method to get a contract by ID
    public Contract getContractById(Integer id) {
        String sql = "SELECT * FROM Contracts WHERE ContractID = ?";
        return jdbcTemplate.queryForObject(sql, contractRowMapper, id);
    }

    // Method to get all contracts
    public List<Contract> getAllContracts() {
        String sql = "SELECT * FROM Contracts";
        return jdbcTemplate.query(sql, contractRowMapper);
    }

    // Method to update a contract
    public void updateContract(Contract contract) {
        String sql = "UPDATE Contracts SET EmployeeID = ?, ContractType = ?, StartDate = ?, EndDate = ?, Salary = ?, Status = ? WHERE ContractID = ?";
        jdbcTemplate.update(sql,
                contract.getEmployee().getId(),
                contract.getContractType(),
                contract.getStartDate(),
                contract.getEndDate(),
                contract.getSalary(),
                contract.getStatus().getValue(),
                contract.getId());
    }

    // Method to delete a contract
    public void deleteContract(Integer id) {
        String sql = "DELETE FROM Contracts WHERE ContractID = ?";
        jdbcTemplate.update(sql, id);
    }
}