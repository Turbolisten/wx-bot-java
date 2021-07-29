package com.turbolisten.bot.service.util;

import java.util.regex.Pattern;

/**
 * 验证工具类
 *
 * @author listen
 * @date 2017/11/06 10:54
 */
public class SmartVerificationUtil {

    /**
     * 手机号码验证规则
     */
    public static final String PHONE_REGEXP = "^1(2|3|4|5|6|7|8|9)\\d{9}$";

    /**
     * 人名正则
     */
    public static final String PERSON_NAME = "[\u4E00-\u9FA5]{2,5}(?:·[\u4E00-\u9FA5]{2,5})*";

    public static void main(String[] args) {
        boolean matches = Pattern.matches(PERSON_NAME, "李晓冬冬冬·冬冬冬冬冬");
        System.out.println(matches);
    }

    /**
     * 固定号码验证规则
     */
    public static final String FIXED_PHONE_REGEXP = "^0\\d{2,3}-[1-9]\\d{6,7}$";

    /**
     * 18位 营业执照号码 校验
     */
    public static final String LICENSE_18 = "^([159Y]{1})([1239]{1})([0-9ABCDEFGHJKLMNPQRTUWXY]{6})([0-9ABCDEFGHJKLMNPQRTUWXY]{9})([0-90-9ABCDEFGHJKLMNPQRTUWXY])$";

    /**
     * 数字格式验证证规则
     */
    public static final String FIXED_NUMBER = "^[0-9]+$";

    /**
     * 账号正则
     */
    public static final String USER_ACCOUNT_REGEXP = "^[a-z0-9]{6,16}$";

    /**
     * 后管账号正则
     */
    public static final String ADMIN_ACCOUNT_REGEXP = "^[A-Za-z0-9]{3,16}$";

    /**
     * 密码正则校验
     */
    public static final String PWD_REGEXP = "^[A-Za-z0-9._]{6,16}$";

    /**
     * 身份证 正则校验
     */
    public static final String IDENTITY_CARD = "^[1-9][0-9]{2}\\d{3}([12]\\d{3})(0[1-9]|1[012])(0[1-9]|[12]\\d|3[01])\\d{3}([0-9Xx])$";

    /**
     * 邮箱正则校验，参照正则：https://blog.csdn.net/liudglink/article/details/78511759
     */
    public static final String EMAIL_REGEXP = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$";

    /**
     * 日期校验 yyyy-MM-dd
     */
    public static final String IS_DATE = "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))"
            + "|(02-(0[1-9]|[1][0-9]|2[0-8])))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9"
            + "][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)";

    /**
     * 日期年月日校验 yyyy-MM-dd HH:mm:ss
     */
    public static final String IS_DATE_TIME = "^((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9" +
            "]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29))\\s+([0-1]?[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$";

    /**
     * 日期年月日校验 yyyy-MM-dd HH:mm
     */
    public static final String IS_DATE_TIME_HM = "^((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9" +
            "]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29))\\s+([0-1]?[0-9]|2[0-3]):([0-5][0-9])$";

    /**
     * 固话和手机校验
     */
    public static final String FIXED_PHONE_AND_PHONE_REGEXP = "(^1(3|4|5|7|8|9)\\d{9}$)|(^0\\d{2,3}-[1-9]\\d{6,7}$)";


    public static final String regSecond = "([0-9]{1,2}|[0-9]{1,2}\\-[0-9]{1,2}|\\*|[0-9]{1,2}\\/[0-9]{1,2}|[0-9]{1,2}\\,[0-9]{1,2})";

    public static final String regMinute = "\\s([0-9]{1,2}|[0-9]{1,2}\\-[0-9]{1,2}|\\*|[0-9]{1,2}\\/[0-9]{1,2}|[0-9]{1,2}\\,[0-9]{1,2})";

    public static final String regHour = "\\s([0-9]{1,2}|[0-9]{1,2}\\-[0-9]{1,2}|\\*|[0-9]{1,2}\\/[0-9]{1,2}|[0-9]{1,2}\\,[0-9]{1,2})";

    public static final String regDay = "\\s([0-9]{1,2}|[0-9]{1,2}\\-[0-9]{1,2}|\\*|[0-9]{1,2}\\/[0-9]{1,2}|[0-9]{1,2}\\,[0-9]{1,2}|\\?|L|W|C)";

    public static final String regMonth = "\\s([0-9]{1,2}|[0-9]{1,2}\\-[0-9]{1,2}|\\*|[0-9]{1,2}\\/[0-9]{1,2}|[0-9]{1,2}\\,[0-9]{1,2}|JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)";

    public static final String regWeek = "\\s([1-7]{1}|[1-7]{1}\\-[1-7]{1}|[1-7]{1}\\#[1-7]{1}|\\*|[1-7]{1}\\/[1-7]{1}|[1-7]{1}\\,[1-7]{1}|MON|TUES|WED|THUR|FRI|SAT|SUN|\\?|L|C)";

    public static final String regYear = "(\\s([0-9]{4}|[0-9]{4}\\-[0-9]{4}|\\*|[0-9]{4}\\/[0-9]{4}|[0-9]{4}\\,[0-9]{4})){0,1}";

    /**
     * cron表达式校验
     */
    public static final String CRON_EXPRESSION_REGEXP = regSecond + regMinute + regHour + regDay + regMonth + regWeek + regYear;


}
