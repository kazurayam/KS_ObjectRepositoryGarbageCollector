package com.kazurayam.ks.logging

import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import static org.junit.Assert.assertNotNull

class SimplifiedStopWatchTest {

    private static Logger logger = LoggerFactory.getLogger(SimplifiedStopWatchTest.class)

    @Test
    void test_toJson() {
        SimplifiedStopWatch ssw = new SimplifiedStopWatch()
        Thread.sleep(3000)
        ssw.stop()
        String json = ssw.toJson()
        logger.info(json)
        assertNotNull(json)
    }
}
