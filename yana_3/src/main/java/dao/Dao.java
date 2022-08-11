package dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    void createTable();
    void createValueInTable(T value);
    void createValuesInTable(List<T> values);
    Optional<T> findById(int id);
    List<T> findAllValues();
    void update(List<T> values);
    List<T> findByNameGroup(String name);
    void deleteAllValue();
}
