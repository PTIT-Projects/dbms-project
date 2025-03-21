package vn.ptit.hrms.service;

import org.springframework.stereotype.Service;
import vn.ptit.hrms.dao.ContractDao;

@Service
public class ContractService {
    private final ContractDao contractDao;

    public ContractService(ContractDao contractDao) {
        this.contractDao = contractDao;
    }
}
