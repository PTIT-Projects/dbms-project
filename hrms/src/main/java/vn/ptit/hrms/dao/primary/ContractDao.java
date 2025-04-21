package vn.ptit.hrms.dao.primary;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import vn.ptit.hrms.domain.primary.Contract;
import vn.ptit.hrms.mapper.ContractRowMapper;

import java.util.ArrayList;
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

    public Page<Contract> getContractPage(
            Pageable pageable,
            String employeeSearch,
            String contractType,
            String status) {

        StringBuilder countSql = new StringBuilder("SELECT COUNT(*) FROM Contracts c LEFT JOIN Employees e ON c.EmployeeID = e.EmployeeID");
        StringBuilder dataSql = new StringBuilder("SELECT c.*, e.* FROM Contracts c LEFT JOIN Employees e ON c.EmployeeID = e.EmployeeID");
        List<Object> params = new ArrayList<>();
        
        if ((employeeSearch != null && !employeeSearch.isEmpty()) || 
            (contractType != null && !contractType.isEmpty()) || 
            (status != null && !status.isEmpty())) {
            
            String whereClause = buildWhereClause(employeeSearch, contractType, status, params);
            countSql.append(whereClause);
            dataSql.append(whereClause);
        }
        
        Integer total = jdbcTemplate.queryForObject(countSql.toString(), Integer.class, params.toArray());
        
        dataSql.append(" ORDER BY c.StartDate DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
        params.add(pageable.getOffset());
        params.add(pageable.getPageSize());
        List<Contract> contracts = jdbcTemplate.query(dataSql.toString(), contractRowMapper, params.toArray());

        return new PageImpl<>(contracts, pageable, total != null ? total : 0);
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
    
    private String buildWhereClause(String employeeSearch, String contractType, String status, List<Object> params) {
        StringBuilder whereClause = new StringBuilder(" WHERE ");
        boolean whereAdded = false;

        if (employeeSearch != null && !employeeSearch.isEmpty()) {
            whereClause.append(" e.FullName LIKE ?");
            params.add("%" + employeeSearch + "%");
            whereAdded = true;
        }

        if (contractType != null && !contractType.isEmpty()) {
            if (whereAdded) {
                whereClause.append(" AND c.ContractType = ?");
            } else {
                whereClause.append(" c.ContractType = ?");
                whereAdded = true;
            }
            params.add(contractType);
        }

        if (status != null && !status.isEmpty()) {
            if (whereAdded) {
                whereClause.append(" AND c.Status = ?");
            } else {
                whereClause.append(" c.Status = ?");
            }
            params.add(status);
        }
        
        return whereClause.toString();
    }
}