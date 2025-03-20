package vn.ptit.hrms.domain;

import java.time.LocalDate;

public class TrainingCourse {
    private Integer id;
    private String courseName;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Employee trainer;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Employee getTrainer() {
        return trainer;
    }

    public void setTrainer(Employee trainer) {
        this.trainer = trainer;
    }
}
