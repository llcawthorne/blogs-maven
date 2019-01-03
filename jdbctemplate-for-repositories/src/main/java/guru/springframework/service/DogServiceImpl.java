package guru.springframework.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DogServiceImpl implements DogService {
    @Autowired
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    private long generatedKey;

    private String rescuedString;

    @Override
    public void addDog(String name, Date rescued, Boolean vaccinated) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update("INSERT INTO dog(name, rescued, vaccinated) VALUES (?, ?, ?)",
                name, rescued, vaccinated);
    }

    @Override
    public void deleteDog(String name, Long id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update("DELETE FROM dog WHERE name=? AND id=?", name, id);
    }

    @Override
    public List<String> atRiskDogs(Date rescued) {
        // this isn't using rescued Date in any way that I can follow in practice
        String sql = "SELECT * FROM dog WHERE rescued < '" + rescued + "' AND vaccinated = '0'";
        List<String> dogList = new ArrayList<>();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.query(sql, new ResultSetExtractor() {
            public List extractData(ResultSet rs) throws SQLException {
                while (rs.next()) {
                    String name = rs.getString("name");
                    dogList.add(name);
                }
                return dogList;
            }
        });
        System.out.println("doglist");
        return dogList;
    }

    @Override
    public long getGeneratedKey(String name, Date rescued, Boolean vaccinated) {
        String sql = "INSERT INTO dog(name, rescued, vaccinated) VALUES (?, ?, ?)";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        KeyHolder holder = new GeneratedKeyHolder();
        Format formatter = new SimpleDateFormat("yyyy-MM-dd");
        rescuedString = formatter.format(rescued);
        System.out.println(rescuedString);
        java.sql.Date rescusedSql = java.sql.Date.valueOf(rescuedString);
        System.out.println(rescusedSql);
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement statement = con.prepareStatement(sql.toString(),
                        Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, name);
                statement.setDate(2, rescusedSql);
                statement.setBoolean(3, vaccinated);
                return statement;
            }
        }, holder);
        generatedKey = holder.getKey().longValue();
        System.out.println("generated key is " + generatedKey);
        return generatedKey;
    }
}
