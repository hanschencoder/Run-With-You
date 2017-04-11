package site.hanschen.runwithyou.utils;


import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author HansChen
 */
public class MD5Utils {

    private MD5Utils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 获得字符串md5值，注意，不要使用{@link DigestUtils#md5Hex(String)}方法直接返回字符串，会报NoSuchMethodError异常，因为android内置的类没有这个方法
     *
     * @param input 需要加密的字符串
     * @return 32位MD5值
     */
    public static String getMD5(String input) {
        return new String(Hex.encodeHex(DigestUtils.md5(input)));
    }

    /**
     * 获得文件md5值，注意，不要使用{@link DigestUtils#md5Hex(InputStream)}方法直接返回字符串，会报NoSuchMethodError异常，因为android内置的类没有这个方法
     *
     * @param file 需要加密的文件
     * @return 32位MD5值
     */
    public static String getMD5(File file) throws IOException {
        return new String(Hex.encodeHex(DigestUtils.md5(new FileInputStream(file))));
    }
}
