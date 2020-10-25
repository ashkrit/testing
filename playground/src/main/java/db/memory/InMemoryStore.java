package db.memory;

import db.KeyValueStore;
import db.SSTable;
import db.TableInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.Collections.emptyMap;

public class InMemoryStore implements KeyValueStore {

    private final Map<String, SSTable<?>> tables = new HashMap<>();

    @Override
    public <Row_Type> SSTable<Row_Type> createTable(String tableName, Class<Row_Type> type, Map<String, Function<Row_Type, Object>> schema, Map<String, Function<Row_Type, String>> indexes) {
        TableInfo<Row_Type> tableInfo = new TableInfo<>(tableName, schema, indexes, null, null, $ -> String.valueOf(System.nanoTime()));
        return createTable(tableInfo);
    }

    private <Row_Type> void registerTable(String tableName, InMemorySSTable<Row_Type> table) {
        tables.put(tableName, table);
    }

    @Override
    public <Row_Type> SSTable<Row_Type> createTable(String tableName, Class<Row_Type> type, Map<String, Function<Row_Type, Object>> schema) {
        return createTable(tableName, type, schema, emptyMap());
    }

    @Override
    public <Row_Type> SSTable<Row_Type> createTable(TableInfo<Row_Type> tableInfo) {
        InMemorySSTable<Row_Type> table = new InMemorySSTable<>(tableInfo);
        registerTable(tableInfo.getTableName(), table);
        return table;
    }

    @Override
    public List<String> desc(String tableName) {
        SSTable<?> SSTable = tables.get(tableName);
        return SSTable.cols();
    }
}
