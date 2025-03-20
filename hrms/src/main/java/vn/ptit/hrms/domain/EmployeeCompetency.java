package vn.ptit.hrms.domain;

import vn.ptit.hrms.constant.EmployeeCompetencyLevelEnum;

public class EmployeeCompetency {
    private Integer id;
    private Employee employee;
    private Competency competency;
    private EmployeeCompetencyLevelEnum level;

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

    public Competency getCompetency() {
        return competency;
    }

    public void setCompetency(Competency competency) {
        this.competency = competency;
    }

    public EmployeeCompetencyLevelEnum getLevel() {
        return level;
    }

    public void setLevel(EmployeeCompetencyLevelEnum level) {
        this.level = level;
    }
}
