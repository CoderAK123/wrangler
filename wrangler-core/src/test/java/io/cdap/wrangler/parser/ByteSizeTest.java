package io.cdap.wrangler.parser;
import io.cdap.wrangler.api.parser.ByteSize;

import org.junit.Assert;
import org.junit.Test;

public class ByteSizeTest {
    @Test
    public void testParseBytes() {
        ByteSize byteSize = new ByteSize("2GB");
        Assert.assertEquals(2L * 1024 * 1024 * 1024, byteSize.getBytes());

        byteSize = new ByteSize("500MB");
        Assert.assertEquals(500L * 1024 * 1024, byteSize.getBytes());
    }
}
