package cn.cheungchingyin.ysm.utils;

/**
 * @Author 张正贤
 * @Date 2023/4/8 17:55
 * @Version 1.0
 */
public class StringUtil {

    /**
     * 字符串是否为空
     *
     * @param str 字符串
     * @return 为空返回true
     */
    public static boolean isBlank(String str) {
        if (str == null || str.length() == 0) {
            return true;
        }
        return false;
    }
}
