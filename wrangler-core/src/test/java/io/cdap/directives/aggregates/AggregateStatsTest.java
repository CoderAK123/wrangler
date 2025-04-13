package io.cdap.directives.aggregates;

import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.utils.testing.RecipeTester;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;

public class AggregateStatsTest {

    @Test
    public void testAggregateStats() throws Exception {
        // Prepare input data
        Row row1 = new Row();
        row1.add("data_transfer_size", "1024B"); // 1 KB
        row1.add("response_time", "1000ms");    // 1 second

        Row row2 = new Row();
        row2.add("data_transfer_size", "1MB");  // 1 MB
        row2.add("response_time", "2000ms");    // 2 seconds

        List<Row> inputRows = Arrays.asList(row1, row2);

        // Define recipe
        String[] recipe = new String[] {
            "aggregate-stats data_transfer_size response_time total_size_mb total_time_sec"
        };

        // Execute pipeline using RecipeTester
        List<Row> output = RecipeTester.run(recipe, inputRows);

        // Verify: only one output row (aggregate)
        assertEquals(1, output.size());

        Row result = output.get(0);

        // Validate results
        double expectedTotalSizeMB = (1024 + (1 * 1024 * 1024)) / (1024.0 * 1024.0); // ≈ 1.001 MB
        double expectedTotalTimeSec = (1000 + 2000) / 1000.0; // 3.0 seconds

        assertEquals(expectedTotalSizeMB, (Double) result.getValue("total_size_mb"), 0.001);
        assertEquals(expectedTotalTimeSec, (Double) result.getValue("total_time_sec"), 0.001);
    }
}
