package vn.ptit.hrms.dao.warehouse;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import vn.ptit.hrms.domain.warehouse.DimDate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Repository
public class DimDateDao {
    private final JdbcTemplate jdbcTemplate;

    public DimDateDao(@Qualifier("warehouseJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public DimDate getDateByDateSk(Integer dateSk) {
        String sql = "SELECT * FROM dim_date WHERE date_sk = ?";
        return jdbcTemplate.queryForObject(sql, new DimDateRowMapper(), dateSk);
    }

    public DimDate getDateByDate(LocalDate date) {
        String sql = "SELECT * FROM dim_date WHERE date = ?";
        return jdbcTemplate.queryForObject(sql, new DimDateRowMapper(), date);
    }

    public List<DimDate> getAllDates() {
        String sql = "SELECT * FROM dim_date";
        return jdbcTemplate.query(sql, new DimDateRowMapper());
    }

    public List<DimDate> getDatesByYear(Integer year) {
        String sql = "SELECT * FROM dim_date WHERE year = ?";
        return jdbcTemplate.query(sql, new DimDateRowMapper(), year);
    }

    public List<DimDate> getDatesByYearAndMonth(Integer year, Integer month) {
        String sql = "SELECT * FROM dim_date WHERE year = ? AND month = ?";
        return jdbcTemplate.query(sql, new DimDateRowMapper(), year, month);
    }

    private static class DimDateRowMapper implements RowMapper<DimDate> {
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
}