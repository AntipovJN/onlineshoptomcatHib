package test;

import org.apache.log4j.Logger;
import org.junit.Test;
import utils.SHA256StringHashUtil;
import static org.junit.Assert.*;

public class SHA256StringHashUtilTest {

    private static final Logger logger = Logger.getLogger(SHA256StringHashUtilTest.class);

    private static final String TEST_STRING = "testString";
    private static final String OTHER_TEST_STRING = "otherString";
    private static final String EMPTY_STRING = "";
    private static final String EXPECTED_EXCEPTION_MESSAGE = "java.lang.IllegalArgumentException: Empty string";

    @Test
    public void getSha256() {
        assertNotNull(SHA256StringHashUtil.getSha256(TEST_STRING));
        assertNotNull(SHA256StringHashUtil.getSha256(OTHER_TEST_STRING));

        assertEquals(64, SHA256StringHashUtil.getSha256(TEST_STRING).length());
        assertEquals(SHA256StringHashUtil.getSha256(TEST_STRING).length(),
                SHA256StringHashUtil.getSha256(TEST_STRING).length());

        assertNotEquals(SHA256StringHashUtil.getSha256(TEST_STRING),
                SHA256StringHashUtil.getSha256(OTHER_TEST_STRING));

        try {
            SHA256StringHashUtil.getSha256(EMPTY_STRING);
        } catch (Exception e) {
            assertEquals(EXPECTED_EXCEPTION_MESSAGE, e.getMessage());
            logger.info("Expected exception is exist");
        }
        logger.info("TEST DONE!");
    }
}
