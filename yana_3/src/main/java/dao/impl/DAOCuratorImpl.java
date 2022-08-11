package dao.impl;

import dao.Dao;
import dto.DTOCurator;

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

public class DAOCuratorImpl implements Dao<DTOCurator> {
    private final DataSource dataSource = getPostgreDataSource();

    private static final String CREATE_CURATOR_TABLE = """
            CREATE TABLE IF NOT EXISTS curator
            (ID SERIAL PRIMARY KEY,
             FIO VARCHAR(150) NOT NULL)
            """;

    private static final String CREATE_CURATOR = """
            INSERT INTO curator (FIO)
            values (?)
            """;

    private static final String FIND_CURATOR_BY_ID = """
            SELECT *
            FROM curator
            WHERE ID = ?
            """;

    private static final String FIND_ALL_CURATORS = """
            SELECT *
            FROM curator
            """;

    private static final String DELETE_CURATOR = """
            DROP table if exists curator cascade
            """;

    @Override
    public void deleteAllValue() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(DELETE_CURATOR);
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public void update(List<DTOCurator> values) {
        throw new RuntimeException("Метод не реализован");
    }

    @Override
    public List<DTOCurator> findByNameGroup(String name) {
        throw new RuntimeException("Метод не реализован");
    }

    @Override
    public void createTable() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(CREATE_CURATOR_TABLE);
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public Optional<DTOCurator> findById(int id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preStatement = connection.prepareStatement(FIND_CURATOR_BY_ID)) {
            preStatement.setInt(1, id);
            ResultSet resultSet = preStatement.executeQuery();
            return resultSet.next() ? Optional.of(DTOCurator.builder()
                    .id(resultSet.getInt(1))
                    .fio(resultSet.getString(2))
                    .build()): Optional.of(new DTOCurator());
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public List<DTOCurator> findAllValues() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(FIND_ALL_CURATORS);
            var curators = new ArrayList<DTOCurator>();
            while(resultSet.next()){
                curators.add(DTOCurator.builder()
                        .id(resultSet.getInt(1))
                        .fio(resultSet.getString(2))
                        .build());
            }
            return curators;
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public void createValueInTable(DTOCurator curator) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preStatement = connection.prepareStatement(CREATE_CURATOR)) {

            preStatement.setString(1, curator.getFio());
            preStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public void createValuesInTable(List<DTOCurator> curators) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preStatement = connection.prepareStatement(CREATE_CURATOR)) {

            for (DTOCurator dtoCurator : curators) {
                preStatement.setString(1, dtoCurator.getFio());
                preStatement.addBatch();
            }
            preStatement.executeBatch();
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
}