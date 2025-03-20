package vn.ptit.hrms.domain;

import vn.ptit.hrms.constant.TrainingCompletionStatusEnum;

public class TrainingResult {
    private Integer id;
    private Employee employee;
    private TrainingCourse course;
    private TrainingCompletionStatusEnum completionStatus;
    private Double score;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public TrainingCourse getCourse() {
        return course;
    }

    public void setCourse(TrainingCourse course) {
        this.course = course;
    }

    public TrainingCompletionStatusEnum getCompletionStatus() {
        return completionStatus;
    }

    public void setCompletionStatus(TrainingCompletionStatusEnum completionStatus) {
        this.completionStatus = completionStatus;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
