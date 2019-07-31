package test;

import org.junit.Test;
import utils.SaltGenerator;

import static org.junit.Assert.*;

public class SaltGeneratorTest {

    @Test
    public void getSalt() {
        assertNotNull("Salt is null", SaltGenerator.getSalt());
        assertEquals("Salt length is incorrect", 6, SaltGenerator.getSalt().length());
    }

    @Test
    public void saltPassword() {
        String password = "admin";
        String salt = SaltGenerator.getSalt();
        String saltPass = SaltGenerator.saltPassword(password, salt);
        assertEquals("lenth of salt pass is incorrect",
                salt.length() + password.length(), saltPass.length());
        assertEquals(salt.substring(0,3),saltPass.substring(0,3));
        assertEquals(salt.substring(salt.length()-3), saltPass.substring(saltPass.length()-3));
        assertEquals(password, saltPass.substring(3,saltPass.length()-3));
    }
}