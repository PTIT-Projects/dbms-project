package vn.ptit.hrms.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import vn.ptit.hrms.domain.Contract;
import vn.ptit.hrms.mapper.ContractRowMapper; // Import your ContractRowMapper
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
        String sql = "INSERT INTO contracts (employee_id, contract_type, start_date, end_date, salary, status) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, contract.getEmployee().getId(), contract.getContractType(), contract.getStartDate(), contract.getEndDate(), contract.getSalary(), contract.getStatus().name());
    }

    // Method to get a contract by ID
    public Contract getContractById(Integer id) {
        String sql = "SELECT * FROM contracts WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, contractRowMapper, id);
    }

    // Method to get all contracts
    public List<Contract> getAllContracts() {
        String sql = "SELECT * FROM contracts";
        return jdbcTemplate.query(sql, contractRowMapper);
    }

    // Method to update a contract
    public void updateContract(Contract contract) {
        String sql = "UPDATE contracts SET employee_id = ?, contract_type = ?, start_date = ?, end_date = ?, salary = ?, status = ? WHERE id = ?";
        jdbcTemplate.update(sql, contract.getEmployee().getId(), contract.getContractType(), contract.getStartDate(), contract.getEndDate(), contract.getSalary(), contract.getStatus().name(), contract.getId());
    }

    // Method to delete a contract
    public void deleteContract(Integer id) {
        String sql = "DELETE FROM contracts WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
