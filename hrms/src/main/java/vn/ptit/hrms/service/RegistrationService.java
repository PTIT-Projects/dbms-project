package vn.ptit.hrms.service;

import org.springframework.stereotype.Service;
import vn.ptit.hrms.dao.RegistrationDao;

@Service
public class RegistrationService {
    private final RegistrationDao registrationDao;

    public RegistrationService(RegistrationDao registrationDao) {
        this.registrationDao = registrationDao;
    }
}
