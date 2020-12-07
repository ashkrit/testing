package query.page;

import org.junit.jupiter.api.Test;
import query.page.allocator.HeapPageAllocator;
import query.page.allocator.PageAllocator;
import query.page.write.WritePage;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static query.page.ApplicationClock.fromTs;
import static query.page.ApplicationClock.now;

public class PageAllocatorTest {

    @Test
    public void create_heap_pages() {
        PageAllocator pa = new HeapPageAllocator((byte) 1, 1024);
        LocalDateTime now = fromTs(now());

        ApplicationClock.provider = () -> System.currentTimeMillis() + 1000; // Move clock

        WritePage page = pa.newPage();

        assertEquals(1, pa.noOfPages());
        assertEquals(1024, pa.pageSize());
        assertEquals(1, pa.version());

        assertAll(
                () -> assertEquals(1, page.pageNumber()),
                () -> assertEquals(1007, page.capacity()),
                () -> assertEquals(0, page.noOfTuple()),
                () -> assertTrue(page.createdTime().isAfter(now))
        );
    }
}
