package vn.ptit.hrms.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.ptit.hrms.dao.primary.EmployeeDao;
import vn.ptit.hrms.domain.primary.Employee;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final EmployeeDao employeeDao;

    public CustomUserDetailsService(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee employee = this.employeeDao.findEmployeeByEmail(username);
        return User.withUsername(employee.getEmail())
                .password(employee.getPassword())
                .authorities(employee.getRoleName()).build();
    }
}
