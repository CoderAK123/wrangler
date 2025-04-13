package io.cdap.directives.aggregates;

import io.cdap.wrangler.api.Arguments;
import io.cdap.wrangler.api.Directive;
import io.cdap.wrangler.api.ExecutorContext;
import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.api.annotations.Public;
import io.cdap.wrangler.api.parser.ByteSize;
import io.cdap.wrangler.api.parser.TimeDuration;
import io.cdap.wrangler.api.parser.TokenType;
import io.cdap.wrangler.api.parser.UsageDefinition;

import java.util.ArrayList;
import java.util.List;

/**
 * AggregateStats Directive
 *
 * This directive aggregates totals from two source columns—one holding byte size values and
 * one holding time duration values. It returns a single row with the aggregated totals converted
 * to megabytes (for size) and seconds (for time).
 *
 * Usage:
 *   aggregate-stats <sizeSourceCol> <timeSourceCol> <sizeTargetCol> <timeTargetCol>
 */
@Public
public class AggregateStats implements Directive {

    /** Source column for byte sizes. */
    private String sizeSourceCol;

    /** Source column for time durations. */
    private String timeSourceCol;

    /** Target column for aggregated size (MB). */
    private String sizeTargetCol;

    /** Target column for aggregated time (seconds). */
    private String timeTargetCol;

    private static final double BYTES_IN_MB = 1024.0 * 1024.0;
    private static final double MILLISECONDS_IN_SECOND = 1000.0;


/** {@inheritDoc} */
    @Override
    public UsageDefinition define() {
        UsageDefinition.Builder builder = UsageDefinition.builder("aggregate-stats");
        builder.define("sizeSourceCol", TokenType.COLUMN_NAME,
                      "Source column for byte sizes");
        builder.define("timeSourceCol", TokenType.COLUMN_NAME,
                      "Source column for time durations");
        builder.define("sizeTargetCol", TokenType.COLUMN_NAME,
                      "Target column for aggregated size (MB)");
        builder.define("timeTargetCol", TokenType.COLUMN_NAME,
                      "Target column for aggregated time (seconds)");
        return builder.build();
    }
/** {@inheritDoc} */

    @Override
    public final void  initialize(final Arguments arguments) {
        this.sizeSourceCol = arguments.value("sizeSourceCol");
        this.timeSourceCol = arguments.value("timeSourceCol");
        this.sizeTargetCol = arguments.value("sizeTargetCol");
        this.timeTargetCol = arguments.value("timeTargetCol");
    }
/** {@inheritDoc} */
    @Override
    public final List<Row> execute(final List<Row> rows, ExecutorContext context) {
        long totalSize = 0L;
        long totalTime = 0L;

        for (Row row : rows) {
            Object sizeObj = row.getValue(sizeSourceCol);
            Object timeObj = row.getValue(timeSourceCol);

            // Process size column with null and type checks
            if (sizeObj != null) {
                try {
                    if (sizeObj instanceof ByteSize) {
                        totalSize += ((ByteSize) sizeObj).getBytes();
                    } else if (sizeObj instanceof String) {
                        ByteSize bs = new ByteSize((String) sizeObj);
                        totalSize += bs.getBytes();
                    }
                } catch (Exception e) {
                    throw new RuntimeException(String.format(
                        "Invalid byte size value '%s' in column '%s'", 
                        sizeObj, sizeSourceCol), e);
                }
            }

            // Process time column with null and type checks
            if (timeObj != null) {
                try {
                    if (timeObj instanceof TimeDuration) {
                        totalTime += ((TimeDuration) timeObj).getMillis();
                    } else if (timeObj instanceof String) {
                        TimeDuration td = new TimeDuration((String) timeObj);
                        totalTime += td.getMillis();
                    }
                } catch (Exception e) {
                    throw new RuntimeException(String.format(
                        "Invalid time duration value '%s' in column '%s'", 
                        timeObj, timeSourceCol), e);
                }
            }
        }

        // Convert to target units:
        double totalSizeMB = totalSize / BYTES_IN_MB;
        double totalTimeSec = totalTime / MILLISECONDS_IN_SECOND; // Milliseconds to seconds

        Row output = new Row();
        output.add(sizeTargetCol, totalSizeMB);
        output.add(timeTargetCol, totalTimeSec);

        List<Row> result = new ArrayList<>();
        result.add(output);
        return result;
    }
/** {@inheritDoc} */
    @Override
    public void destroy() {
        // No resources to release.
    }
}
