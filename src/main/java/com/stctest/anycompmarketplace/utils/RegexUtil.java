package com.stctest.anycompmarketplace.utils;

import java.util.regex.Pattern;

public final class RegexUtil {

    
    /**
     * 手机号匹配
     */
    //    private static Pattern phonePattern = Pattern.compile("^1(3[0-9]|4[57]|5[0-35-9]|7[0135678]|8[0-9])\\d{8}$");
    private static Pattern phonePattern = Pattern.compile("^!*([0-9]!*){10,}$");

    /**
     * 用户邮箱匹配
     */
    private static Pattern emailPattern = Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");

    /**
     * 昵称，2-16位
     * 中文 \u4e00-\u9fA5
     * 韩文 \uac00-\ud7ff
     * 日文 \u0800-\u4e00
     */
    private static Pattern nicknamePattern = Pattern.compile("^[ _a-zA-Z\u4E00-\u9FA5\uac00-\ud7ff\u0800-\u4e00]{2,32}$");

    /**
     * 留言，0-20位
     * 中文 \u4e00-\u9fA5
     * 韩文 \uac00-\ud7ff
     * 日文 \u0800-\u4e00
     */
    private static Pattern memoPattern = Pattern.compile("^[\\w\u4E00-\u9FA5\uac00-\ud7ff\u0800-\u4e00]{0,20}$");

   
    /**
     * 校验手机号码(仅限中国地区)
     *
     * @param phone 手机号码
     */
    public static boolean isMobileValid(String phone) {
        return phonePattern.matcher(phone).matches();
    }

    /**
     * 判断会员姓名是否合法
     * 支持中英文字符（特殊字符除外），长度2-16
     *
     * @return 合法返回true，否则返回false
     */
    public static boolean isNicknameValid(String nickname) {
        //初步判断是否为中英文字符
        return nicknamePattern.matcher(nickname).matches();
    }

    /**
     * 判断邮箱是否合法
     *
     * @return 合法返回true，否则返回false
     */
    public static boolean isEmailValid(String email) {
        return emailPattern.matcher(email).matches();
    }

    /**
     * 判断留言是否合法
     *
     * @return 合法返回true，否则返回false
     */
    public static boolean isMemoValid(String memo) {
        return memoPattern.matcher(memo).matches();
    }
}
