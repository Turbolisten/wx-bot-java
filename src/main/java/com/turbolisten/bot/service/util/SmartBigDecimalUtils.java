package com.turbolisten.bot.service.util;


import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.List;
import java.util.function.Function;

/**
 * 全局 BigDecimal 工具类
 *
 * @author listen
 * @date 2018/01/17 13:54
 */
public class SmartBigDecimalUtils {

    /**
     * 价格类型 保留小数点 2
     */
    public static final int PRICE_DECIMAL_POINT = 2;

    /**
     * 价格类型 保留小数点 6
     */
    public static final int SIX_PRICE_DECIMAL_POINT = 6;

    /**
     * 价格类型 保留小数点 8
     */
    public static final int MONEY_DECIMAL_POINT = 8;

    /**
     * 重量类型保留小数点 3
     */
    public static final int WEIGHT_DECIMAL_POINT = 3;

    /**
     * BigDecimal 加法 num1 + num2
     * 未做非空校验
     *
     * @param num1
     * @param num2
     * @param point 请使用BigDecimalUtils.PRICE_DECIMAL_POINT | BigDecimalUtils.WEIGHT_DECIMAL_POINT
     * @return BigDecimal
     */
    public static BigDecimal add(BigDecimal num1, BigDecimal num2, int point) {
        return setScale(num1.add(num2), point);
    }

    /**
     * BigDecimal 乘法 num1 x num2
     * 未做非空校验
     *
     * @param num1
     * @param num2
     * @param point 请使用BigDecimalUtils.PRICE_DECIMAL_POINT | BigDecimalUtils.WEIGHT_DECIMAL_POINT
     * @return BigDecimal
     */
    public static BigDecimal multiply(BigDecimal num1, BigDecimal num2, int point) {
        return setScale(num1.multiply(num2), point);
    }

    /**
     * BigDecimal 减法 num1 - num2
     * 未做非空校验
     *
     * @param num1
     * @param num2
     * @param point 请使用BigDecimalUtils.PRICE_DECIMAL_POINT | BigDecimalUtils.WEIGHT_DECIMAL_POINT
     * @return BigDecimal
     */
    public static BigDecimal subtract(BigDecimal num1, BigDecimal num2, int point) {
        return setScale(num1.subtract(num2), point);
    }

    /**
     * BigDecimal 除法 num1/num2
     * 未做非空校验
     *
     * @param num1
     * @param num2
     * @param point 请使用BigDecimalUtils.PRICE_DECIMAL_POINT | BigDecimalUtils.WEIGHT_DECIMAL_POINT
     * @return BigDecimal
     */
    public static BigDecimal divide(BigDecimal num1, BigDecimal num2, int point) {
        return num1.divide(num2, point, RoundingMode.HALF_UP);
    }

    /**
     * 设置小数点类型为 四舍五入
     *
     * @param num
     * @param point
     * @return BigDecimal
     */
    public static BigDecimal setScale(BigDecimal num, int point) {
        return num.setScale(point, RoundingMode.HALF_UP);
    }

    /**
     * 设置小数点类型为 均舍近零
     *
     * @param num
     * @param point
     * @return BigDecimal
     */
    public static BigDecimal setScaleDown(BigDecimal num, int point) {
        return num.setScale(point, RoundingMode.DOWN);
    }

    /**
     * 比较 num1 是否大于 num2
     *
     * @param num1
     * @param num2
     * @return boolean
     */
    public static boolean isGreaterThan(BigDecimal num1, BigDecimal num2) {
        return num1.compareTo(num2) > 0;
    }

    /**
     * 比较 num1 是否大于等于 num2
     *
     * @param num1
     * @param num2
     * @return boolean
     */
    public static boolean isGreaterOrEqual(BigDecimal num1, BigDecimal num2) {
        return isGreaterThan(num1, num2) || equals(num1, num2);
    }

    /**
     * 比较 num1 是否小于 num2
     *
     * @param num1
     * @param num2
     * @return boolean
     */
    public static boolean isLessThan(BigDecimal num1, BigDecimal num2) {
        return num1.compareTo(num2) < 0;
    }

    /**
     * 比较num1 num2 返回最大的值
     *
     * @param num1
     * @param num2
     * @return BigDecimal
     */
    public static BigDecimal max(BigDecimal num1, BigDecimal num2) {
        return num1.compareTo(num2) > 0 ? num1 : num2;
    }

    /**
     * 比较num1 num2 返回最小的值
     *
     * @param num1
     * @param num2
     * @return BigDecimal
     */
    public static BigDecimal min(BigDecimal num1, BigDecimal num2) {
        return num1.compareTo(num2) < 0 ? num1 : num2;
    }

    /**
     * 比较 num1 是否小于等于 num2
     *
     * @param num1
     * @param num2
     * @return boolean
     */
    public static boolean isLessOrEqual(BigDecimal num1, BigDecimal num2) {
        return isLessThan(num1, num2) || equals(num1, num2);
    }

    /**
     * 比较 num1 是否等于 num2
     *
     * @param num1
     * @param num2
     * @return
     */
    public static boolean equals(BigDecimal num1, BigDecimal num2) {
        return num1.compareTo(num2) == 0;
    }

    /**
     * 计算 num1 / num2 的百分比
     *
     * @param num1
     * @param num2
     * @return String
     */
    public static String getPercentage(BigDecimal num1, BigDecimal num2) {
        BigDecimal result = num1.divide(num2, 4, RoundingMode.HALF_UP);
        NumberFormat percent = NumberFormat.getPercentInstance();
        percent.setMaximumFractionDigits(2);
        return percent.format(result.doubleValue());
    }

    /**
     * 计算 num1 / num2 的百分比
     *
     * @param num1
     * @param num2
     * @param point 保留几位小数
     * @return String
     */
    public static BigDecimal bigDecimalPercent(Integer num1, Integer num2, int point) {
        if (num1 == null || num2 == null) {
            return BigDecimal.ZERO;
        }
        if (num2 == 0) {
            return BigDecimal.ZERO;
        }
        return bigDecimalPercent(new BigDecimal(num1), new BigDecimal(num2), point);
    }

    /**
     * 计算 num1 / num2 的百分比
     *
     * @param num1
     * @param num2
     * @param point 保留几位小数
     * @return 结果会x100 如 10.15
     */
    public static BigDecimal bigDecimalPercent(BigDecimal num1, BigDecimal num2, int point) {
        if (num1 == null || num2 == null) {
            return BigDecimal.ZERO;
        }
        if (equals(BigDecimal.ZERO, num2)) {
            return BigDecimal.ZERO;
        }
        BigDecimal percent = num1.divide(num2, point + 2, RoundingMode.HALF_UP);
        BigDecimal percent100 = percent.multiply(new BigDecimal(100)).setScale(point);
        return percent100;
    }

    /**
     * 判断num是否为空 或者 零
     *
     * @param num
     * @return String
     */
    public static Boolean isEmpty(BigDecimal num) {
        return null == num || equals(BigDecimal.ZERO, num);
    }

    /**
     * 判断num是否 不等于null 并且不等于零
     *
     * @param num
     * @return String
     */
    public static Boolean isNotEmpty(BigDecimal num) {
        return !isEmpty(num);
    }

    /**
     * 转换为万
     *
     * @param num
     * @param point
     * @return
     */
    public static BigDecimal convertTenThousand(BigDecimal num, int point) {
        BigDecimal decimal = num.divide(new BigDecimal(10000), point, RoundingMode.HALF_UP);
        return decimal;
    }

    /**
     * 转换为负数
     *
     * @param num
     * @return
     */
    public static BigDecimal convertToMinusNumber(BigDecimal num) {
        if (isLessOrEqual(num, BigDecimal.ZERO)) {
            return num;
        }
        return BigDecimal.ZERO.subtract(num);
    }

    /**
     * 将 list 中对象的属性相加
     *
     * @param list
     * @param func
     * @param <T>
     * @return
     */
    public static <T> BigDecimal add(List<T> list, Function<T, BigDecimal> func) {
        BigDecimal total = BigDecimal.ZERO;
        if (CollectionUtils.isEmpty(list)) {
            return total;
        }
        for (T e : list) {
            total = add(total, func.apply(e), 2);
        }
        return total;
    }

    /**
     * 将金额的单位：元 转为 分
     * 小数点两位后 四舍五入
     *
     * @param amount
     * @return
     */
    public static int convertToCent(BigDecimal amount) {
        return multiply(amount, new BigDecimal(100), 0).intValue();
    }

    /**
     * 将金额的单位：分 转为 元
     *
     * @param fee
     * @return BigDecimal
     */
    public static BigDecimal convertToYuan(int fee) {
        return SmartBigDecimalUtils.divide(new BigDecimal(fee), new BigDecimal(100), 2);
    }

    /**
     * 金额相关计算方法：四舍五入 保留2位小数点
     */
    public static class Amount {

        public static BigDecimal add(BigDecimal num1, BigDecimal num2) {
            return setScale(num1.add(num2), PRICE_DECIMAL_POINT);
        }

        public static BigDecimal multiply(BigDecimal num1, BigDecimal num2) {
            return setScale(num1.multiply(num2), PRICE_DECIMAL_POINT);
        }

        public static BigDecimal subtract(BigDecimal num1, BigDecimal num2) {
            return setScale(num1.subtract(num2), PRICE_DECIMAL_POINT);
        }

        public static BigDecimal divide(BigDecimal num1, BigDecimal num2) {
            return num1.divide(num2, PRICE_DECIMAL_POINT, RoundingMode.HALF_UP);
        }

    }

    /**
     * 线上收益金额相关计算方法：均舍近零 保留2位小数点
     */
    public static class SchoolOnlineIncomeAmount {

        public static BigDecimal add(BigDecimal num1, BigDecimal num2) {
            return setScaleDown(num1.add(num2), PRICE_DECIMAL_POINT);
        }

        public static BigDecimal multiply(BigDecimal num1, BigDecimal num2) {
            return setScaleDown(num1.multiply(num2), PRICE_DECIMAL_POINT);
        }

        public static BigDecimal subtract(BigDecimal num1, BigDecimal num2) {
            return setScaleDown(num1.subtract(num2), PRICE_DECIMAL_POINT);
        }

        public static BigDecimal divide(BigDecimal num1, BigDecimal num2) {
            return num1.divide(num2, PRICE_DECIMAL_POINT, RoundingMode.DOWN);
        }

    }

    /**
     * 金额相关计算方法：四舍五入 保留2位小数点
     */
    public static class AmountSix {

        public static BigDecimal add(BigDecimal num1, BigDecimal num2) {
            return setScale(num1.add(num2), SIX_PRICE_DECIMAL_POINT);
        }

        public static BigDecimal multiply(BigDecimal num1, BigDecimal num2) {
            return setScale(num1.multiply(num2), SIX_PRICE_DECIMAL_POINT);
        }

        public static BigDecimal subtract(BigDecimal num1, BigDecimal num2) {
            return setScale(num1.subtract(num2), SIX_PRICE_DECIMAL_POINT);
        }

        public static BigDecimal divide(BigDecimal num1, BigDecimal num2) {
            return num1.divide(num2, PRICE_DECIMAL_POINT, RoundingMode.HALF_UP);
        }
    }

    /**
     * 重量相关计算方法：四舍五入 保留3位小数点
     */
    public static class Weight {

        public static BigDecimal add(BigDecimal num1, BigDecimal num2) {
            return setScale(num1.add(num2), WEIGHT_DECIMAL_POINT);
        }

        public static BigDecimal multiply(BigDecimal num1, BigDecimal num2) {
            return setScale(num1.multiply(num2), WEIGHT_DECIMAL_POINT);
        }

        public static BigDecimal subtract(BigDecimal num1, BigDecimal num2) {
            return setScale(num1.subtract(num2), WEIGHT_DECIMAL_POINT);
        }

        public static BigDecimal divide(BigDecimal num1, BigDecimal num2) {
            return num1.divide(num2, WEIGHT_DECIMAL_POINT, RoundingMode.HALF_UP);
        }
    }

    /**
     * 计算折扣后的数值 = 原价 x (折扣/100)
     *
     * @param originNum
     * @param discountRatio
     * @return
     */
    public static BigDecimal discount(BigDecimal originNum, BigDecimal discountRatio) {
        discountRatio = Amount.divide(discountRatio, new BigDecimal(100));
        return Amount.multiply(originNum, discountRatio);
    }

    public static void main(String[] args) {
        System.out.println(SmartBigDecimalUtils.bigDecimalPercent(new BigDecimal("1"),new BigDecimal("1"),2));
    }
}
