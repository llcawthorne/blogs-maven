package jdbctemplate_queries;

import jdbctemplate_queries.domain.Employee;
import jdbctemplate_queries.rowmappers.EmployeeRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@SpringBootApplication
public class Application implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String args[]) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    JdbcTemplate jdbcTemplate;  // we only need jdbcTemplate for most of our examples

    @Autowired
    DataSource dataSource; // we need dataSource for the SimpleJdbc_ calls later

    @Override
    public void run(String... strings) throws Exception {

        // Easy to create tables here using JdbcTemplate.execute, but we're loading them from
        // schema-h2.sql for H2 and expecting them to exist in other databases

        // log.info("Creating tables");
        // jdbcTemplate.execute("DROP TABLE employee IF EXISTS");
        // jdbcTemplate.execute("CREATE TABLE employee(" +
        //         "id SERIAL, first_name VARCHAR(255), last_name VARCHAR(255), country VARCHAR(255))");

        int countEmployees = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM EMPLOYEE", Integer.class);

        if (countEmployees == 0) {
            // We could've done a batchUpdate too
            jdbcTemplate.update("INSERT INTO EMPLOYEE (first_name, last_name, country) VALUES (?, ?, ?)",
                    "Bill", "Gates", "USA");
            jdbcTemplate.update("INSERT INTO EMPLOYEE (first_name, last_name, country) VALUES (?, ?, ?)",
                    "Lewis", "Cawthorne", "USA");
            jdbcTemplate.update("INSERT INTO EMPLOYEE (first_name, last_name, country) VALUES (?, ?, ?)",
                    "Josh", "Long", "USA");
            jdbcTemplate.update("INSERT INTO EMPLOYEE (first_name, last_name, country) VALUES (?, ?, ?)",
                    "Josh", "Bloch", "USA");
            jdbcTemplate.update("INSERT INTO EMPLOYEE (first_name, last_name, country) VALUES (?, ?, ?)",
                    "Pawan", "Agarawal", "India");

            countEmployees = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM EMPLOYEE", Integer.class);
            assertEquals(5, countEmployees);
        }

        // We can do a standard paramterized select
        String firstName = jdbcTemplate.queryForObject("SELECT first_name from EMPLOYEE WHERE ID = ?",
                new Object[] { 1 }, String.class);
        // Or we can use NamedParameterJdbcTemplate
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("id", 1);
        String firstNameAgain = namedParameterJdbcTemplate.queryForObject(
                "SELECT first_name from EMPLOYEE WHERE ID = :id", namedParameters, String.class);
        assertEquals(firstName, firstNameAgain);

        // You can also use a BeanParameterSource with a NamedParameterJdbcTemplate
        Employee employee = new Employee();
        employee.setFirstName("Josh");
        String SELECT_COUNT_BY_NAME = "SELECT COUNT(*) FROM EMPLOYEE WHERE FIRST_NAME = :firstName";
        namedParameters = new BeanPropertySqlParameterSource(employee);
        int joshCount = namedParameterJdbcTemplate.queryForObject(SELECT_COUNT_BY_NAME, namedParameters, Integer.class);
        assertEquals(2, joshCount);

        // We can query objects without setting up a rowmapper
        List<Employee> joshesWithoutMapper = jdbcTemplate.query(
                "SELECT * FROM employee WHERE first_name = ?", new Object[] { "Josh" },
                (rs, rowNum) -> new Employee(rs.getInt("ID"),
                                             rs.getString("FIRST_NAME"),
                                             rs.getString("LAST_NAME"),
                                             rs.getString("COUNTRY")));
        assertEquals(2, joshesWithoutMapper.size());

        // Custom row mappers make it easy to query for objects
        String queryById = "SELECT * FROM EMPLOYEE WHERE ID = ? ";
        Employee bill = jdbcTemplate.queryForObject(queryById, new Object[] { 1 }, new EmployeeRowMapper());
        assertEquals("Bill", bill.getFirstName());
        // It can also be used for a List of multiple objects with the query method
        String queryByFirst = "SELECT * FROM EMPLOYEE WHERE FIRST_NAME = ?";
        List<Employee> joshes = jdbcTemplate.query(queryByFirst, new Object[] { "Josh" }, new EmployeeRowMapper());
        assertEquals(2, joshes.size());
        // We could've defined our RowMapper inline
        joshes = jdbcTemplate.query(queryByFirst, new Object[] { "Josh" },
                // Same as our other RowMapper, but we're using constructor
                new RowMapper<Employee>() {
                    public Employee mapRow(ResultSet rs, int rowNumb) throws SQLException {
                        Employee e = new Employee(rs.getInt("ID"), rs.getString("FIRST_NAME"),
                                rs.getString("LAST_NAME"), rs.getString("COUNTRY"));
                        return e;
                    }
                });
        assertEquals(2, joshes.size());
        // since the fields match the instance variables, you could even use the BeanPropertyRowMapper
        joshes = jdbcTemplate.query(queryByFirst, new Object[] { "Josh" }, new BeanPropertyRowMapper(Employee.class));
        assertEquals(2, joshes.size());
        // BeanPropertyRowMapper works with queryForObject too, but you need a cast
        bill = (Employee) jdbcTemplate
                .queryForObject(queryById, new Object[] { 1 }, new BeanPropertyRowMapper(Employee.class));
        assertEquals("Bill", bill.getFirstName());
        // You can always query for a plain old String if you just need a field value
        String queryForFirst = "SELECT FIRST_NAME FROM EMPLOYEE WHERE ID = ?";
        firstName = jdbcTemplate.queryForObject(queryForFirst, new Object[] { 1 }, String.class);
        assertEquals("Bill", firstName);

        // If you don't want to setup all these objects, you could just use queryForList to get back a Map
        String queryForNames = "SELECT FIRST_NAME, LAST_NAME from EMPLOYEE";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(queryForNames);
        for (Map<String, Object> row : list) {
            System.out.println(row.get("FIRST_NAME") + " " + row.get("LAST_NAME"));
        }
        // queryForList isn't always so crude with it's return types
        String queryForAllFirst = "SELECT FIRST_NAME FROM EMPLOYEE";
        List<String> firstNames = jdbcTemplate.queryForList(queryForAllFirst, String.class);
        assertEquals(5, firstNames.size());

        // Now on to another way to use JDBC with Spring, SimpleJdbcInsert
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("EMPLOYEE")
                .usingGeneratedKeyColumns("ID");
        Number id;
        String SELECT_COUNT_BY_FNAME = "SELECT COUNT(*) FROM EMPLOYEE WHERE FIRST_NAME = ?";
        int countSamuel = jdbcTemplate.queryForObject(SELECT_COUNT_BY_FNAME, new Object[] { "Samuel" }, Integer.class);
        if (countSamuel == 0) {
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("FIRST_NAME", "Samuel");
            parameters.put("LAST_NAME", "Adams");
            parameters.put("COUNTRY", "USA");
            id = simpleJdbcInsert.executeAndReturnKey(parameters);
            System.out.println("Samuel created with id " + id);
        }
        // We can also use a SqlParameterSource or a BeanPropertySqlParameterSource
        int countDonald = jdbcTemplate.queryForObject(SELECT_COUNT_BY_FNAME, new Object[] { "Donald" }, Integer.class);
        if (countDonald == 0) {
            SqlParameterSource insertParameters = new MapSqlParameterSource()
                    .addValue("FIRST_NAME", "Donald")
                    .addValue("LAST_NAME", "Trump")
                    .addValue("COUNTRY_NAME", "USA");
            id = simpleJdbcInsert.executeAndReturnKey(insertParameters);
            System.out.println("Donald created with id " + id);
        }
        int countLinda = jdbcTemplate.queryForObject(SELECT_COUNT_BY_FNAME, new Object[] { "Linda" }, Integer.class);
        if (countLinda == 0) {
            Employee linda = new Employee("Linda", "Craig", "UK");
            id = simpleJdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(linda));
            System.out.println("Linda created with id " + id);
        }

        // And also useful, but not demonstratable in our sample databases, is SimpleJdbcCall to call procedures
        // SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dataSource).withProcedureName("READ_EMPLOYEE");
        // SqlParameterSource in = new MapSqlParameterSource().addValue("in_id", id);
        // Map<String, Object> out = simpleJdbcCall.execute(in);
        // String procedureReturnedFirstName = (String) out.get("FIRST_NAME");

        // There's also batch operations
        List<Employee> recentHires = new ArrayList<>();
        recentHires.add(new Employee("Doug", "Jones", "USA"));
        recentHires.add(new Employee("Chris", "Christopherson", "USA"));
        recentHires.add(new Employee("Julia", "Childs", "USA"));
        jdbcTemplate.batchUpdate("INSERT INTO EMPLOYEE (FIRST_NAME, LAST_NAME, COUNTRY) VALUES (?, ?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, recentHires.get(i).getFirstName());
                        ps.setString(2, recentHires.get(i).getLastName());
                        ps.setString(3, recentHires.get(i).getCountry());
                    }
                    @Override
                    public int getBatchSize() {
                        if (recentHires.size() < 50) {
                            return recentHires.size();
                        }
                        return 50;
                    }
                });
        // Or with named parameters
        List<Employee> moreRecentHires = new ArrayList<>();
        moreRecentHires.add(new Employee("Martin", "Short", "USA"));
        moreRecentHires.add(new Employee("Mr", "Bean", "UK"));
        moreRecentHires.add(new Employee("Mary", "Allen", "USA"));
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(moreRecentHires.toArray());
        int[] updateCounts = namedParameterJdbcTemplate.batchUpdate(
                "INSERT INTO EMPLOYEE (FIRST_NAME, LAST_NAME, COUNTRY) VALUES (:firstName, :lastName, :country)", batch);

        int finalCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM EMPLOYEE", Integer.class);
        System.out.println("There are " + finalCount + " employees in the database.");
    }
}
