package vn.ptit.hrms.domain;

import vn.ptit.hrms.constant.TrainingCompletionStatusEnum;

public class TrainingResult {
    private Integer id;
    private Employee employee;
    private TrainingCourse course;
    private TrainingCompletionStatusEnum completionStatus;
    private Double score;
}
