package db.rocks;

import db.KVDatabase;
import db.Table;
import db.memory.InMemoryTable;
import org.rocksdb.RocksDB;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.Collections.emptyMap;

public class RocksKeyValueStore implements KVDatabase {
    private final Map<String, Table<?>> tables = new HashMap<>();
    private final RocksDB rocksDB;

    public RocksKeyValueStore(File rootFolder) {
        this.rocksDB = RocksConnection.openDatabase(rootFolder);
    }

    @Override
    public <Row_Type> Table<Row_Type> createTable(String tableName, Map<String, Function<Row_Type, Object>> cols, Map<String, Function<Row_Type, String>> indexes) {
        Table<Row_Type> table = new RocksTable<>(rocksDB, tableName, indexes, cols);
        registerTable(tableName, table);
        return table;
    }

    private <Row_Type> void registerTable(String tableName, Table<Row_Type> table) {
        tables.put(tableName, table);
    }

    @Override
    public <Row_Type> Table<Row_Type> createTable(String tableName, Map<String, Function<Row_Type, Object>> cols) {
        return createTable(tableName, cols, emptyMap());
    }

    @Override
    public List<String> desc(String tableName) {
        Table<?> table = tables.get(tableName);
        return table.cols();
    }

    public void close() {
        this.rocksDB.close();
    }
}
