package vn.ptit.hrms.domain;

import vn.ptit.hrms.constant.CompetencyTypeEnum;

public class Competency {
    private Integer id;
    private Position position;
    private CompetencyTypeEnum competencyType;
    private String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public CompetencyTypeEnum getCompetencyType() {
        return competencyType;
    }

    public void setCompetencyType(CompetencyTypeEnum competencyType) {
        this.competencyType = competencyType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
