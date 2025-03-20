package vn.ptit.hrms.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import vn.ptit.hrms.constant.CompetencyTypeEnum;
import vn.ptit.hrms.domain.Competency;
import vn.ptit.hrms.domain.Position;

public class CompetancyRowMapper implements RowMapper<Competency> {

    @Override
    public Competency mapRow(ResultSet rs, int rowNum) throws SQLException {
        Competency competency = new Competency();

        // Map Competency id
        competency.setId(rs.getInt("id"));

        // Retrieve position id from ResultSet and fetch the full Position using a method
        int positionId = rs.getInt("position_id");
        Position position = findPositionById(positionId); // Implement this method as needed
        competency.setPosition(position);

        // Map CompetencyTypeEnum
        String competencyTypeValue = rs.getString("competency_type");
        if (competencyTypeValue != null) {
            competency.setCompetencyType(getCompetencyType(competencyTypeValue));
        }

        // Map description
        competency.setDescription(rs.getString("description"));

        return competency;
    }

    /**
     * Converts a string value from the database into the corresponding CompetencyTypeEnum.
     */
    private CompetencyTypeEnum getCompetencyType(String value) {
        for (CompetencyTypeEnum type : CompetencyTypeEnum.values()) {
            // Adjust the comparison if your enum uses a custom value
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown competency type value: " + value);
    }

    // Placeholder for a method to retrieve Position by ID
    private Position findPositionById(int positionId) {
        // Implement the logic to retrieve a Position object by its ID
        // This could involve calling a PositionDAO or similar service
        return null; // Replace with actual implementation
    }
}
