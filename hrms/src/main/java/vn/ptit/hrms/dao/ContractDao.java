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

    public void createContract(Contract contract) {
        String sql = "INSERT INTO Contracts (EmployeeID, ContractType, StartDate, EndDate, Status) VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                contract.getEmployee() != null ? contract.getEmployee().getId() : null,
                contract.getContractType() != null ? contract.getContractType().getValue() : null,
                contract.getStartDate() != null ? java.sql.Date.valueOf(contract.getStartDate()) : null,
                contract.getEndDate() != null ? java.sql.Date.valueOf(contract.getEndDate()) : null,
                contract.getStatus() != null ? contract.getStatus().getValue() : null);
    }

    public Contract getContractById(Integer id) {
        String sql = "SELECT * FROM Contracts WHERE ContractID = ?";
        return jdbcTemplate.queryForObject(sql, contractRowMapper, id);
    }

    public List<Contract> getAllContracts() {
        String sql = "SELECT * FROM Contracts";
        return jdbcTemplate.query(sql, contractRowMapper);
    }

    public void updateContract(Contract contract) {
        String sql = "UPDATE Contracts SET EmployeeID = ?, ContractType = ?, StartDate = ?, EndDate = ?, Status = ? WHERE ContractID = ?";

        jdbcTemplate.update(sql,
                contract.getEmployee() != null ? contract.getEmployee().getId() : null,
                contract.getContractType() != null ? contract.getContractType().getValue() : null,
                contract.getStartDate() != null ? java.sql.Date.valueOf(contract.getStartDate()) : null,
                contract.getEndDate() != null ? java.sql.Date.valueOf(contract.getEndDate()) : null,
                contract.getStatus() != null ? contract.getStatus().getValue() : null,
                contract.getId());
    }

    public void deleteContract(Integer id) {
        String sql = "DELETE FROM Contracts WHERE ContractID = ?";
        jdbcTemplate.update(sql, id);
    }
}