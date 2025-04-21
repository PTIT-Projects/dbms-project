package vn.ptit.hrms.service;

import org.springframework.stereotype.Service;
import vn.ptit.hrms.dao.primary.SalaryDao;
import vn.ptit.hrms.domain.primary.Salary;

import java.util.List;

@Service
public class SalaryService {
    public final SalaryDao salaryDao;

    public SalaryService(SalaryDao salaryDao) {
        this.salaryDao = salaryDao;
    }

    public void createSalary(Salary salary) {
        salaryDao.createSalary(salary);
    }

    public Salary getSalaryById(Integer id) {
        return salaryDao.getSalaryById(id);
    }

    public List<Salary> getAllSalaries() {
        return salaryDao.getAllSalaries();
    }

    public List<Salary> getSalariesByEmployee(Integer employeeId) {
        List<Salary> allSalaries = salaryDao.getAllSalaries();
        return allSalaries.stream()
                .filter(salary -> salary.getEmployee() != null &&
                        salary.getEmployee().getId().equals(employeeId))
                .toList();
    }

    public List<Salary> getSalariesByMonthYear(Integer month, Integer year) {
        List<Salary> allSalaries = salaryDao.getAllSalaries();
        return allSalaries.stream()
                .filter(salary -> salary.getMonth().equals(month) &&
                        salary.getYear().equals(year))
                .toList();
    }

    public void updateSalary(Salary salary) {
        salaryDao.updateSalary(salary);
    }

    public void deleteSalary(Integer id) {
        salaryDao.deleteSalary(id);
    }

    public double calculateTotalSalaryExpense(Integer month, Integer year) {
        List<Salary> salaries = getSalariesByMonthYear(month, year);
        return salaries.stream()
                .mapToDouble(Salary::getNetSalary)
                .sum();
    }
}