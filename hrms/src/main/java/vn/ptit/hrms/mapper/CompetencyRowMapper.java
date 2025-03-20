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

        // Map Competency id - use the correct column name CompetencyID
        competency.setId(rs.getInt("CompetencyID"));

        // Retrieve position id from ResultSet and fetch the full Position using PositionDAO.
        // Use the correct column name PositionID
        int positionId = rs.getInt("PositionID");
        Position position = positionDAO.getPositionById(positionId);
        competency.setPosition(position);

        // Map CompetencyTypeEnum field using a helper method.
        // Use the correct column name CompetencyType
        String competencyTypeValue = rs.getString("CompetencyType");
        if (competencyTypeValue != null) {
            competency.setCompetencyType(getCompetencyType(competencyTypeValue));
        }

        // Map description - use the correct column name Description
        competency.setDescription(rs.getString("Description"));

        return competency;
    }

    /**
     * Converts a string value from the database into the corresponding CompetencyTypeEnum.
     */
    private CompetencyTypeEnum getCompetencyType(String value) {
        for (CompetencyTypeEnum type : CompetencyTypeEnum.values()) {
            // Compare with getValue() instead of name() since the database stores the display value
            if (type.getValue().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown competency type value: " + value);
    }
}