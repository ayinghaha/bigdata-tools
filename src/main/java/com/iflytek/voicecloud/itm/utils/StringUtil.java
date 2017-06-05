package com.iflytek.voicecloud.itm.utils;

import sun.misc.BASE64Encoder;

import java.security.MessageDigest;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * 字符串工具类
 */
public class StringUtil {

    /**
     * 生成密码盐
     * @return  String
     */
    public static String generateSalt() {
        Random random = new Random();
        StringBuffer strBuf = new StringBuffer(16);
        strBuf.append(random.nextInt(99999999)).append(random.nextInt(99999999));
        int length = strBuf.length();
        if (length < 16) {
            for (int i = length; i < 16 ; i++) {
                strBuf.append(0);
            }
        }

        return strBuf.toString();
    }

    /**
     * 生成md5字符串
     * @param sourceStr  字符串
     * @return  md5加密后的字符串
     */
    public static String generateMd5(String sourceStr) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
            /*System.out.println("MD5(" + sourceStr + ",32) = " + result);
            System.out.println("MD5(" + sourceStr + ",16) = " + buf.toString().substring(8, 24));*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 过滤字符串中的特殊字符
     * @param str   检测字符串
     * @return      是否包含
     * @throws PatternSyntaxException   异常
     */
    public static String StringFilter(String str) throws PatternSyntaxException {
        // 只允许字母和数字
        // String   regEx  =  "[^a-zA-Z0-9]";
        // 清除掉所有特殊字符
        String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\]<>/?]";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(str);

        return matcher.replaceAll("").trim();
    }

    /**
     * 检测字符串是否为null
     * @param str  检测字符串
     * @return boolean  true 字符串
     */
    public static boolean isStringNull(String str) {
        return str == null || str.equals("");
    }
}
