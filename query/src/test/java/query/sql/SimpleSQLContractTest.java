package query.sql;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import query.kv.KeyValueStore;
import query.kv.SSTable;
import query.tables.Order;

import java.util.*;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static java.util.Collections.sort;
import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class SimpleSQLContractTest {

    KeyValueStore db;

    @Test
    void select_all_records() {

        SSTable<Order> orders = db.createTable("orders", Order.class, cols());

        List<Order> expectedRows = asList(
                Order.of(100, "1", 20200901, "SHIPPED", 107.6d, 5),
                Order.of(101, "2", 20200902, "SHIPPED", 967.6d, 15),
                Order.of(102, "1", 20200903, "SHIPPED", 767.6d, 25)
        );
        expectedRows.forEach(orders::insert);

        List<Order> returnRows = new ArrayList<>();

        db.execute("select * From orders", row -> {
            returnRows.add(Order.of(row.getLong("orderId"), row.getString("customerId"), row.getInt("orderDate"),
                    row.getString("status"), row.getDouble("amount"), row.getInt("noOfItem")));
        });

        assertResult(expectedRows, returnRows);

    }

    @Test
    void select_records_with_limit() {

        SSTable<Order> orders = db.createTable("orders", Order.class, cols());

        asList(
                Order.of(100, "1", 20200901, "SHIPPED", 107.6d, 5),
                Order.of(101, "2", 20200902, "SHIPPED", 967.6d, 15),
                Order.of(102, "1", 20200903, "SHIPPED", 767.6d, 25)
        ).forEach(orders::insert);

        List<Order> returnRows = new ArrayList<>();

        db.execute("select * From orders limit 1", row -> {
            returnRows.add(Order.of(row.getLong("orderId"), row.getString("customerId"), row.getInt("orderDate"),
                    row.getString("status"), row.getDouble("amount"), row.getInt("noOfItem")));
        });

        assertEquals(1, returnRows.size());
    }

    @Test
    void select_rows_based_on_criteria() {
        SSTable<Order> orders = db.createTable("orders", Order.class, cols());

        asList(
                Order.of(100, "1", 20200901, "SHIPPED", 107.6d, 5),
                Order.of(101, "2", 20200902, "SHIPPED", 967.6d, 15),
                Order.of(102, "1", 20200903, "SHIPPED", 767.6d, 25)
        ).forEach(orders::insert);

        List<Order> returnRows = new ArrayList<>();

        db.execute("select * From orders where orderId=100", row -> {
            returnRows.add(Order.of(row.getLong("orderId"), row.getString("customerId"), row.getInt("orderDate"),
                    row.getString("status"), row.getDouble("amount"), row.getInt("noOfItem")));
        });

        List<Order> expectedRows = asList(
                Order.of(100, "1", 20200901, "SHIPPED", 107.6d, 5)
        );
        assertResult(expectedRows, returnRows);
    }

    @Test
    void select_rows_based_on_multi_field_criteria() {
        SSTable<Order> orders = db.createTable("orders", Order.class, cols());

        asList(
                Order.of(100, "1", 20200901, "SHIPPED", 107.6d, 5),
                Order.of(101, "2", 20200902, "SHIPPED", 967.6d, 15),
                Order.of(102, "1", 20200903, "SHIPPED", 767.6d, 25)
        ).forEach(orders::insert);

        List<Order> returnRows = new ArrayList<>();

        db.execute("select * From orders where orderId=101 and customerId='2' ", row -> {
            returnRows.add(Order.of(row.getLong("orderId"), row.getString("customerId"), row.getInt("orderDate"),
                    row.getString("status"), row.getDouble("amount"), row.getInt("noOfItem")));
        });

        List<Order> expectedRows = asList(
                Order.of(101, "2", 20200902, "SHIPPED", 967.6d, 15)
        );
        assertResult(expectedRows, returnRows);
    }

    @Test
    void select_rows_based_on_multi_field_by_or_criteria() {
        SSTable<Order> orders = db.createTable("orders", Order.class, cols());

        asList(
                Order.of(100, "1", 20200901, "SHIPPED", 107.6d, 5),
                Order.of(101, "2", 20200902, "SHIPPED", 967.6d, 15),
                Order.of(102, "1", 20200903, "SHIPPED", 767.6d, 25)
        ).forEach(orders::insert);

        List<Order> returnRows = new ArrayList<>();

        db.execute("select * From orders where orderId=101 or orderId=102", row -> {
            returnRows.add(Order.of(row.getLong("orderId"), row.getString("customerId"), row.getInt("orderDate"),
                    row.getString("status"), row.getDouble("amount"), row.getInt("noOfItem")));
        });

        List<Order> expectedRows = asList(
                Order.of(101, "2", 20200902, "SHIPPED", 967.6d, 15),
                Order.of(102, "1", 20200903, "SHIPPED", 767.6d, 25)
        );
        assertResult(expectedRows, returnRows);
    }

    @Test
    void select_rows_based_on_multi_field_by_or_criteria_using_different_column() {
        SSTable<Order> orders = db.createTable("orders", Order.class, cols());

        asList(
                Order.of(100, "1", 20200901, "SHIPPED", 107.6d, 5),
                Order.of(101, "2", 20200902, "SHIPPED", 967.6d, 15),
                Order.of(102, "1", 20200903, "SHIPPED", 767.6d, 25)
        ).forEach(orders::insert);

        List<Order> returnRows = new ArrayList<>();

        db.execute("select * From orders where orderdate=20200902 or status='SHIPPED' ", row -> {
            returnRows.add(Order.of(row.getLong("orderId"), row.getString("customerId"), row.getInt("orderDate"),
                    row.getString("status"), row.getDouble("amount"), row.getInt("noOfItem")));
        });

        List<Order> expectedRows = asList(
                Order.of(100, "1", 20200901, "SHIPPED", 107.6d, 5),
                Order.of(101, "2", 20200902, "SHIPPED", 967.6d, 15),
                Order.of(102, "1", 20200903, "SHIPPED", 767.6d, 25)
        );
        assertResult(expectedRows, returnRows);
    }

    @Test
    public void nested_condition() {
        SSTable<Order> orders = db.createTable("orders", Order.class, cols());

        asList(
                Order.of(100, "1", 20200901, "SHIPPED", 107.6d, 5),
                Order.of(101, "2", 20200902, "SHIPPED", 967.6d, 15),
                Order.of(102, "1", 20200903, "SHIPPED", 767.6d, 25)
        ).forEach(orders::insert);

        List<Order> returnRows = new ArrayList<>();

        db.execute("select * From orders where (status='PENDING' or status='SHIPPED') AND (orderdate=20200902) ", row -> {
            returnRows.add(Order.of(row.getLong("orderId"), row.getString("customerId"), row.getInt("orderDate"),
                    row.getString("status"), row.getDouble("amount"), row.getInt("noOfItem")));
        });

        List<Order> expectedRows = asList(
                Order.of(101, "2", 20200902, "SHIPPED", 967.6d, 15)
        );
        assertResult(expectedRows, returnRows);
    }

    private Map<String, Function<Order, Object>> cols() {
        Map<String, Function<Order, Object>> cols = new HashMap<String, Function<Order, Object>>() {{
            put("orderid", Order::orderId);
            put("customerid", Order::customerId);
            put("orderdate", Order::orderDate);
            put("status", Order::status);
            put("amount", Order::amount);
            put("noofitem", Order::noOfItems);
        }};
        return cols;
    }

    private void assertResult(List<Order> expectedRows, List<Order> actualRows) {
        sort(expectedRows, Comparator.comparing(Order::orderId));
        sort(actualRows, Comparator.comparing(Order::orderId));
        assertEquals(expectedRows, actualRows);
    }

    @AfterEach
    public void cleanDB() {
        this.db.close();
    }


}