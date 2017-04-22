package site.hanschen.runwithyou.utils;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author HansChen
 */
public class CommonUtilsTest {

    @Test
    public void isPasswordValid() throws Exception {
        assertTrue(CommonUtils.isPasswordValid("12345678abc"));
        assertFalse(CommonUtils.isPasswordValid("12345678"));
        assertFalse(CommonUtils.isPasswordValid("123456789"));
        assertFalse(CommonUtils.isPasswordValid("aa"));
        assertFalse(CommonUtils.isPasswordValid("aaaaaaaa"));
        assertTrue(CommonUtils.isPasswordValid("aaaaaaaa1"));
        assertTrue(CommonUtils.isPasswordValid("123456789a"));
    }
}