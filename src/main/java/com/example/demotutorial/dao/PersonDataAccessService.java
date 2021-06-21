package com.example.demotutorial.dao;

import com.example.demotutorial.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("postgres")
public class PersonDataAccessService implements PersonDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int insertPerson(UUID id, Person person) {
        // Mila
            jdbcTemplate.update(
                    "INSERT INTO person VALUES (?, ?)", id, person.getName());
            return 1;
//        if (person == null) {
//            throw new IllegalArgumentException("person cannot be null");
//        }
//        if (person.getId() != null) {
//            throw new IllegalArgumentException("person.getId() must be null when creating a " + Person.class.getName());
//        }
//        KeyHolder keyHolder = new GeneratedKeyHolder();
//        jdbcTemplate.update(new PreparedStatementCreator() {
//            @Override
//            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
//                PreparedStatement ps = connection.prepareStatement(
//                        "INSERT INTO person (name) VALUES (?)",
//                        new String[]{"id"}
//                );
//                ps.setString(1, person.getName());
//                return ps;
//            }
//        }, keyHolder);

//        String sql = "INSERT INTO person (name) VALUES (?)";
//        jdbcTemplate.update(sql, person.getName());
//        final String sql = "INSERT INTO person (name) VALUES (:name)";
//        KeyHolder holder = new GeneratedKeyHolder();
//        SqlParameterSource param = new MapSqlParameterSource()
//                .addValue("name", person.getName());
//        jdbcTemplate.update(sql, param, holder);
//        return keyHolder.getKey().intValue();//1;
    }

    @Override
    public List<Person> selectAllPeople() {
        final String sql = "SELECT id, name FROM person";
//        List<Person> people = jdbcTemplate.query(sql, (resultSet, i) -> {
        return jdbcTemplate.query(sql, (resultSet, i) -> {
            UUID id = UUID.fromString(resultSet.getString("id"));
            String name = resultSet.getString("name");
            return new Person(id, name);
        });
//        return people;
        //return List.of(new Person(UUID.randomUUID(), "FROM POSTGRES DB"));
    }

    @Override
    public Optional<Person> selectPersonById(UUID id) {
        final String sql = "SELECT id, name FROM person WHERE id = ?";
        Person person = jdbcTemplate.queryForObject(
                sql,
                new Object[]{id},
                (resultSet, i) -> {
                    UUID personId = UUID.fromString(resultSet.getString("id"));
                    String name = resultSet.getString("name");
                    return new Person(personId, name);
                });
        return Optional.ofNullable(person);
    }

    @Override
    public int deletePersonById(UUID id) {
        // Mila
        String sql = "DELETE FROM person WHERE id = ?";
        jdbcTemplate.update(sql, id);
        return 1;
    }

    @Override
    public int updatePersonById(UUID id, Person person) {
        // Mila
        final String sql = "UPDATE person SET name = ? WHERE id = ?";
        jdbcTemplate.update(sql, person.getName(), id);
        return 1;
    }
}
