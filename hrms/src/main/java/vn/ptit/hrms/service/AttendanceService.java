package vn.ptit.hrms.service;

import org.springframework.stereotype.Service;
import vn.ptit.hrms.dao.AttendanceDao;

@Service
public class AttendanceService {
    private final AttendanceDao attendanceDao;

    public AttendanceService(AttendanceDao attendanceDao) {
        this.attendanceDao = attendanceDao;
    }
}
