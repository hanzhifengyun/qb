package com.hzfy.library.util;


/**
 * 检查类
 */
public class CheckUtil {

    /**
     * Returns true if the string is null or 0-length.
     *
     * @param str the string to be examined
     * @return true if str is null or zero length
     */
    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }


    /**
     * 比较两个String 是否内容相同
     */
    public static boolean equals(String left, String right) {
        if (!isEmpty(left) && !isEmpty(right)) {
            return left.equals(right);
        }
        return false;
    }
}
