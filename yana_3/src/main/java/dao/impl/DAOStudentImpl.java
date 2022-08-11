package dao.impl;

import dao.Dao;
import dto.DTOCurator;
import dto.DTOGroup;
import dto.DTOStudent;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static configuration.PostgreDatasource.getPostgreDataSource;

public class DAOStudentImpl implements Dao<DTOStudent> {
    private final DataSource dataSource = getPostgreDataSource();

    private static final String CREATE_STUDENT_TABLE = """
            CREATE TABLE IF NOT EXISTS Student
            (ID SERIAL PRIMARY KEY,
             FIO VARCHAR(150),
             SEX VARCHAR(1) CHECK ( SEX = 'M' or SEX = 'W' ),
             ID_GROUP INTEGER references "group" (ID))
            """;

    private static final String CREATE_STUDENT = """
            INSERT INTO Student (FIO, SEX, ID_GROUP)
            values (?, ?, ?)
            """;

    private static final String FIND_ALL_STUDENTS = """
            SELECT st.id, st.fio, sex, id_group, gr.name, gr.id_curator, cur.fio
            from student st
            join "group" gr on st.id_group = gr.id
            join curator cur on cur.id = gr.id_curator""";

    private static final String FIND_STUDENT_BY_ID = """
            SELECT st.id, st.fio, sex, id_group, gr.name, gr.id_curator, cur.fio
            from student st
            join "group" gr on st.id_group = gr.id
            join curator cur on cur.id = gr.id_curator
            where st.id = ?""";

    private static final String FIND_STUDENTS_BY_NAME_GROUP = """
            SELECT *
            from student st
            where id_group =
            (select id from "group" where name = ?)""";

    private static final String DELETE_STUDENTS = """
            DROP table if exists student cascade
            """;

    @Override
    public void deleteAllValue() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(DELETE_STUDENTS);
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public List<DTOStudent> findByNameGroup(String name) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preStatement = connection.prepareStatement(FIND_STUDENTS_BY_NAME_GROUP)) {
            preStatement.setString(1, name);
            ResultSet resultSet = preStatement.executeQuery();
            var students = new ArrayList<DTOStudent>();
            while(resultSet.next()){
                students.add(DTOStudent.builder()
                        .id(resultSet.getInt(1))
                        .fio(resultSet.getString(2))
                        .sex(resultSet.getString(3))
                        .idGroup(DTOGroup.builder()
                                .id(resultSet.getInt(4))
                                .build())
                        .build());
            }
            return students;
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }


    @Override
    public Optional<DTOStudent> findById(int id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preStatement = connection.prepareStatement(FIND_STUDENT_BY_ID)) {
            preStatement.setInt(1, id);
            ResultSet resultSet = preStatement.executeQuery();
            return resultSet.next() ? Optional.of(DTOStudent.builder()
                    .id(resultSet.getInt(1))
                    .fio(resultSet.getString(2))
                    .sex(resultSet.getString(3))
                    .idGroup(DTOGroup.builder()
                            .id(resultSet.getInt(4))
                            .nameGroup(resultSet.getString(5))
                            .idCurator(DTOCurator.builder()
                                    .id(resultSet.getInt(6))
                                    .fio(resultSet.getString(7))
                                    .build())
                            .build())
                    .build()) : Optional.of(new DTOStudent());
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public List<DTOStudent> findAllValues() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(FIND_ALL_STUDENTS);
            var students = new ArrayList<DTOStudent>();
            while(resultSet.next()){
                students.add(DTOStudent.builder()
                        .id(resultSet.getInt(1))
                        .fio(resultSet.getString(2))
                        .sex(resultSet.getString(3))
                        .idGroup(DTOGroup.builder()
                                .id(resultSet.getInt(4))
                                .nameGroup(resultSet.getString(5))
                                .idCurator(DTOCurator.builder()
                                        .id(resultSet.getInt(6))
                                        .fio(resultSet.getString(7))
                                        .build())
                                .build())
                        .build());
            }
            return students;
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public void createTable() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(CREATE_STUDENT_TABLE);
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public void createValueInTable(DTOStudent value) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preStatement = connection.prepareStatement(CREATE_STUDENT)) {

            preStatement.setString(1, value.getFio());
            preStatement.setString(2, value.getSex());
            preStatement.setInt(3, 1);
            preStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public void createValuesInTable(List<DTOStudent> values) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preStatement = connection.prepareStatement(CREATE_STUDENT)) {

            for (DTOStudent dtoStudent : values) {
                preStatement.setString(1, dtoStudent.getFio());
                preStatement.setString(2, dtoStudent.getSex());
                preStatement.setInt(3, dtoStudent.getIdGroup().getId());
                preStatement.addBatch();
            }
            preStatement.executeBatch();
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public void update(List<DTOStudent> values) {
        throw new RuntimeException("Метод не реализован");
    }
}
