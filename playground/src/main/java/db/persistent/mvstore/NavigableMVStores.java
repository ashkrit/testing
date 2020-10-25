package db.persistent.mvstore;

import db.persistent.NavigablePersistentStore;
import org.h2.mvstore.Cursor;
import org.h2.mvstore.MVMap;
import org.h2.mvstore.MVStore;

import java.util.function.Consumer;
import java.util.function.Function;

public class NavigableMVStores implements NavigablePersistentStore {
    private final MVStore store;
    private final MVMap<byte[], byte[]> db;

    public NavigableMVStores(MVStore store, String table) {
        this.store = store;
        this.db = this.store.openMap(table);
    }

    @Override
    public void put(byte[] key, byte[] value) {
        db.put(key, value);
    }

    @Override
    public byte[] get(byte[] key) {
        return db.get(key);
    }

    @Override
    public <Row_Type> void iterate(String fromKey, Function<byte[], Row_Type> converter, Consumer<Row_Type> consumer, int limit) {
        Cursor<byte[], byte[]> itr = db.cursor(fromKey.getBytes());
        int tracker = limit;
        while (itr.hasNext() && tracker > 0) {
            String s = new String(itr.getKey());
            if (!s.startsWith(fromKey)) {
                break;
            }
            consumer.accept(converter.apply(itr.getValue()));
            itr.next();
            tracker--;
        }
    }

    @Override
    public <Row_Type> void iterate(String fromKey, String toKey, Function<byte[], Row_Type> converter, Consumer<Row_Type> consumer, int limit) {
        Cursor<byte[], byte[]> itr = db.cursor(fromKey.getBytes());
        int tracker = limit;
        while (itr.hasNext() && tracker > 0) {
            String s = new String(itr.getKey());
            if (s.compareTo(toKey) > 0) {
                break;
            }
            consumer.accept(converter.apply(itr.getValue()));
            itr.next();
            tracker--;
        }
    }
}
