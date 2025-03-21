package vn.ptit.hrms.service;

import org.springframework.stereotype.Service;
import vn.ptit.hrms.dao.ContractDao;
import vn.ptit.hrms.domain.Contract;
import vn.ptit.hrms.constant.ContractStatusEnum;

import java.time.LocalDate;
import java.util.List;

@Service
public class ContractService {
    public final ContractDao contractDao;

    public ContractService(ContractDao contractDao) {
        this.contractDao = contractDao;
    }

    public void createContract(Contract contract) {
        contractDao.createContract(contract);
    }

    public Contract getContractById(Integer id) {
        return contractDao.getContractById(id);
    }

    public List<Contract> getAllContracts() {
        return contractDao.getAllContracts();
    }

    public List<Contract> getContractsByEmployee(Integer employeeId) {
        List<Contract> allContracts = contractDao.getAllContracts();
        return allContracts.stream()
                .filter(contract -> contract.getEmployee() != null &&
                        contract.getEmployee().getId().equals(employeeId))
                .toList();
    }

    public List<Contract> getActiveContracts() {
        List<Contract> allContracts = contractDao.getAllContracts();
        return allContracts.stream()
                .filter(contract -> contract.getStatus() == ContractStatusEnum.ACTIVE)
                .toList();
    }

    public List<Contract> getExpiringContracts(int daysToExpire) {
        LocalDate thresholdDate = LocalDate.now().plusDays(daysToExpire);
        List<Contract> allContracts = contractDao.getAllContracts();

        return allContracts.stream()
                .filter(contract -> contract.getStatus() == ContractStatusEnum.ACTIVE &&
                        contract.getEndDate() != null &&
                        contract.getEndDate().isBefore(thresholdDate))
                .toList();
    }

    public void updateContract(Contract contract) {
        contractDao.updateContract(contract);
    }

    public void deleteContract(Integer id) {
        contractDao.deleteContract(id);
    }
}