package io.cdap.wrangler.parser;
import io.cdap.wrangler.api.parser.TimeDuration;

import org.junit.Assert;
import org.junit.Test;

public class TimeDurationTest {
    @Test
    public void testParseTimeDuration() {
        TimeDuration duration = new TimeDuration("2h");
        Assert.assertEquals(2L * 60 * 60 * 1000, duration.getMillis());

        duration = new TimeDuration("30m");
        Assert.assertEquals(30L * 60 * 1000, duration.getMillis());
    }
}
