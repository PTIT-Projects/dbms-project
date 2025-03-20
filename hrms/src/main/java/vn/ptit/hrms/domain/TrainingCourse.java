package vn.ptit.hrms.domain;

import java.time.LocalDate;

public class TrainingCourse {
    private Integer id;
    private String courseName;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Employee trainer;
}
