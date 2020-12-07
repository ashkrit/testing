package query.timeseries;

import query.timeseries.impl.DefaultTimeSeriesDatabase;
import query.timeseries.sst.memory.InMemorySSTable;

public class PersistentTimeSeriesDatabaseTest extends TimeSeriesStoreContractTest {
    @Override
    void create() {

        db = new DefaultTimeSeriesDatabase(new InMemorySSTable<>(10));
    }
}
