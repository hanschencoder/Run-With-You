package site.hanschen.runwithyou.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author HansChen
 */
public class CommonUtils {

    private CommonUtils() {
    }

    public static boolean isEmailValid(String email) {
        if (isEmpty(email)) {
            return false;
        }
        String regex = "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?";
        Pattern p = Pattern.compile(regex);
        Matcher matcher = p.matcher(email);
        return matcher.matches();
    }

    /**
     * 密码的强度必须是包含字母和数字的组合，不能使用特殊字符，长度在8-20之间
     *
     * @return 是否有效的密码
     */
    public static boolean isPasswordValid(String password) {
        if (isEmpty(password)) {
            return false;
        }
        String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,20}$";
        Pattern p = Pattern.compile(regex);
        Matcher matcher = p.matcher(password);
        return matcher.matches();
    }

    public static boolean isEmpty(CharSequence str) {
        return (str == null || str.length() == 0);
    }
}
