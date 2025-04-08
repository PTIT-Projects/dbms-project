package vn.ptit.hrms.service;

import org.springframework.stereotype.Service;
import vn.ptit.hrms.dao.InsuranceDao;
import vn.ptit.hrms.domain.Insurance;

import java.time.LocalDate;
import java.util.List;

@Service
public class InsuranceService {
    public final InsuranceDao insuranceDao;

    public InsuranceService(InsuranceDao insuranceDao) {
        this.insuranceDao = insuranceDao;
    }

    public void createInsurance(Insurance insurance) {
        insuranceDao.createInsurance(insurance);
    }

    public Insurance getInsuranceById(Integer id) {
        return insuranceDao.getInsuranceById(id);
    }

    public List<Insurance> getAllInsurances() {
        return insuranceDao.getAllInsurances();
    }

    public List<Insurance> getInsurancesByEmployee(Integer employeeId) {
        List<Insurance> allInsurances = insuranceDao.getAllInsurances();
        return allInsurances.stream()
                .filter(insurance -> insurance.getEmployee() != null &&
                        insurance.getEmployee().getId().equals(employeeId))
                .toList();
    }

    public List<Insurance> getExpiredInsurances() {
        LocalDate now = LocalDate.now();
        List<Insurance> allInsurances = insuranceDao.getAllInsurances();

        return allInsurances.stream()
                .filter(insurance -> insurance.getEndDate() != null &&
                        insurance.getEndDate().isBefore(now))
                .toList();
    }

    public List<Insurance> getSoonExpiringInsurances(int daysToExpire) {
        LocalDate thresholdDate = LocalDate.now().plusDays(daysToExpire);
        LocalDate now = LocalDate.now();
        List<Insurance> allInsurances = insuranceDao.getAllInsurances();

        return allInsurances.stream()
                .filter(insurance -> insurance.getEndDate() != null &&
                        !insurance.getEndDate().isBefore(now) &&
                        insurance.getEndDate().isBefore(thresholdDate))
                .toList();
    }

    public void updateInsurance(Insurance insurance) {
        insuranceDao.updateInsurance(insurance);
    }

    public void deleteInsurance(Integer id) {
        insuranceDao.deleteInsurance(id);
    }
}