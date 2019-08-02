package test;

import org.junit.Test;
import utils.SaltGeneratorUtil;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;


public class SaltGeneratorUtilTest {

    private static final String TEST_PASS = "TeStPassWORD123";

    @Test
    public void getSalt() {
        assertNotNull(SaltGeneratorUtil.getSalt());
        assertEquals(6, SaltGeneratorUtil.getSalt().length());
    }

    @Test
    public void saltPassword() {
        String salt = SaltGeneratorUtil.getSalt();
        String saltPass = SaltGeneratorUtil.saltPassword(TEST_PASS, salt);
        assertEquals(salt.length() + TEST_PASS.length(), saltPass.length());
        assertEquals(salt.substring(0, 3), saltPass.substring(0, 3));
        assertEquals(salt.substring(salt.length() - 3), saltPass.substring(saltPass.length() - 3));
        assertEquals(TEST_PASS, saltPass.substring(3, saltPass.length() - 3));
    }
}
