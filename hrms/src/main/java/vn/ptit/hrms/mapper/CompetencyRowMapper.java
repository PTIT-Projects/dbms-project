package vn.ptit.hrms.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import vn.ptit.hrms.constant.CompetencyTypeEnum;
import vn.ptit.hrms.dao.PositionDao;
import vn.ptit.hrms.domain.Competency;
import vn.ptit.hrms.domain.Position;

public class CompetencyRowMapper implements RowMapper<Competency> {
    private final PositionDao positionDAO;

    public CompetencyRowMapper(PositionDao positionDAO) {
        this.positionDAO = positionDAO;
    }

    @Override
    public Competency mapRow(ResultSet rs, int rowNum) throws SQLException {
        Competency competency = new Competency();

       
        competency.setId(rs.getInt("CompetencyID"));

       
       
        int positionId = rs.getInt("PositionID");
        Position position = positionDAO.getPositionById(positionId);
        competency.setPosition(position);

       
       
        String competencyTypeValue = rs.getString("CompetencyType");
        if (competencyTypeValue != null) {
            competency.setCompetencyType(getCompetencyType(competencyTypeValue));
        }

       
        competency.setDescription(rs.getString("Description"));

        return competency;
    }

    private CompetencyTypeEnum getCompetencyType(String value) {
        for (CompetencyTypeEnum type : CompetencyTypeEnum.values()) {
           
            if (type.getValue().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown competency type value: " + value);
    }
}