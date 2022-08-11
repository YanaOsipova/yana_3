package dao.impl;

import dao.Dao;
import dto.DTOCurator;
import dto.DTOGroup;

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

public class DAOGroupImpl implements Dao<DTOGroup> {
    private final DataSource dataSource = getPostgreDataSource();

    private static final String CREATE_GROUP_TABLE = """
            CREATE TABLE IF NOT EXISTS "group"
            (ID SERIAL PRIMARY KEY,
             NAME VARCHAR(150) NOT NULL,
             ID_CURATOR INTEGER)
            """;

    private static final String CREATE_GROUP = """
            INSERT INTO "group" (NAME, ID_CURATOR)
            values (?, ?)
            """;

    private static final String FIND_GROUP_BY_ID = """
            SELECT "group".id, name, id_curator, fio
            FROM "group"
            JOIN curator ON curator.id = "group".id_curator
            where "group".id  = 1""";

    private static final String FIND_ALL_GROUP = """
            SELECT "group".id, name, id_curator, fio
            FROM "group"
            JOIN curator ON curator.id = "group".id_curator""";

    private static final String UPDATE_GROUP = """
            UPDATE "group" set id_curator = ?
            where id = ?""";

    private static final String DELETE_GROUP = """
            DROP table if exists "group" cascade
            """;

    @Override
    public void deleteAllValue() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(DELETE_GROUP);
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public void update(List<DTOGroup> groups) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preStatement = connection.prepareStatement(UPDATE_GROUP)) {

            for (DTOGroup dtoGroup : groups) {
                preStatement.setInt(1, dtoGroup.getIdCurator().getId());
                preStatement.setInt(2, dtoGroup.getId());
                preStatement.addBatch();
            }
            preStatement.executeBatch();
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public List<DTOGroup> findAllValues() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(FIND_ALL_GROUP);
            var curators = new ArrayList<DTOGroup>();
            while(resultSet.next()){
                curators.add(DTOGroup.builder()
                        .id(resultSet.getInt(1))
                        .nameGroup(resultSet.getString(2))
                        .idCurator(DTOCurator.builder()
                                .id(resultSet.getInt(3))
                                .fio(resultSet.getString(4))
                                .build())
                        .build());
            }
            return curators;
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public Optional<DTOGroup> findById(int id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preStatement = connection.prepareStatement(FIND_GROUP_BY_ID)) {
            preStatement.setInt(1, id);
            ResultSet resultSet = preStatement.executeQuery();
            return resultSet.next() ? Optional.of(DTOGroup.builder()
                    .id(resultSet.getInt(1))
                    .nameGroup(resultSet.getString(2))
                    .idCurator(DTOCurator.builder()
                            .id(resultSet.getInt(3))
                            .fio(resultSet.getString(4))
                            .build())
                    .build()) : Optional.of(new DTOGroup());
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public void createTable() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(CREATE_GROUP_TABLE);
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public void createValueInTable(DTOGroup group) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preStatement = connection.prepareStatement(CREATE_GROUP)) {

            preStatement.setString(1, group.getNameGroup());
            preStatement.setInt(2, group.getIdCurator().getId());
            preStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public void createValuesInTable(List<DTOGroup> groups) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preStatement = connection.prepareStatement(CREATE_GROUP)) {

            for (DTOGroup dtoGroup : groups) {
                preStatement.setString(1, dtoGroup.getNameGroup());
                preStatement.setInt(2, dtoGroup.getIdCurator().getId());
                preStatement.addBatch();
            }
            preStatement.executeBatch();
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public List<DTOGroup> findByNameGroup(String name) {
        throw new RuntimeException("Метод не реализован");
    }
}
