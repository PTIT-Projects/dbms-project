package vn.ptit.hrms.mapper.warehouse;

import org.springframework.jdbc.core.RowMapper;
import vn.ptit.hrms.domain.warehouse.DimDate;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DimDateRowMapper implements RowMapper<DimDate> {
    @Override
    public DimDate mapRow(ResultSet rs, int rowNum) throws SQLException {
        DimDate date = new DimDate();
        date.setDateSk(rs.getInt("date_sk"));
        date.setDate(rs.getDate("date").toLocalDate());
        date.setYear(rs.getInt("year"));
        date.setMonth(rs.getInt("month"));
        date.setDay(rs.getInt("day"));
        date.setWeek(rs.getInt("week"));
        date.setQuarter(rs.getInt("quarter"));
        return date;
    }
}
