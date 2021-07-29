package com.turbolisten.bot.service.util;


import com.turbolisten.bot.service.common.constant.BaseEnum;

import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * 枚举工具类
 *
 * @author listen
 * @date 2017/10/10 18:17
 */
public class SmartBaseEnumUtil {

    /**
     * 校验参数与枚举类比较是否合法
     *
     * @param value     参数
     * @param enumClass 枚举类必须实现BaseEnum接口
     * @return boolean
     * @Author listen
     */
    public static boolean checkEnum(Object value, Class<? extends BaseEnum> enumClass) {
        if (null == value) {
            return false;
        }
        return Stream.of(enumClass.getEnumConstants()).anyMatch(e -> e.equalsValue(value));
    }

    /**
     * 获取枚举类的说明 value : info 的形式
     *
     * @param enumClass
     * @return String
     */
    public static String getEnumDesc(Class<? extends BaseEnum> enumClass) {
        BaseEnum[] enums = enumClass.getEnumConstants();
        // value : info 的形式
        StringBuilder sb = new StringBuilder();
        for (BaseEnum baseEnum : enums) {
            sb.append(baseEnum.getValue()).append("：").append(baseEnum.getDesc()).append("，");
        }
        return sb.toString();
    }

    /**
     * 获取与参数相匹配的枚举类实例的 说明
     *
     * @param value     参数
     * @param enumClass 枚举类必须实现BaseEnum接口
     * @return String 如无匹配枚举则返回null
     */
    public static String getEnumDescByValue(Object value, Class<? extends BaseEnum> enumClass) {
        if (null == value) {
            return null;
        }
        return Stream.of(enumClass.getEnumConstants())
                .filter(e -> e.equalsValue(value))
                .findFirst()
                .map(BaseEnum::getDesc)
                .orElse(null);
    }

    /**
     * 根据参数获取枚举类的实例
     *
     * @param value     参数
     * @param enumClass 枚举类必须实现BaseEnum接口
     * @return BaseEnum 无匹配值返回null
     * @Author listen
     */
    public static <T extends BaseEnum> T getEnumByValue(Object value, Class<T> enumClass) {
        if (null == value) {
            return null;
        }
        return Stream.of(enumClass.getEnumConstants())
                .filter(e -> e.equalsValue(value))
                .findFirst()
                .orElse(null);
    }

    /**
     * 根据实例描述与获取枚举类的实例
     *
     * @param desc      参数描述
     * @param enumClass 枚举类必须实现BaseEnum接口
     * @return BaseEnum 无匹配值返回null
     * @Author listen
     */
    public static <T extends BaseEnum> T getEnumByDesc(String desc, Class<T> enumClass) {
        return Stream.of(enumClass.getEnumConstants())
                .filter(e -> Objects.equals(e.getDesc(), desc))
                .findFirst()
                .orElse(null);
    }

    /**
     * 根据lambda getter/setter 注入
     *
     * @param list
     * @param getter
     * @param setter
     * @param enumClass
     * @param <T>
     */
    public static <T> void inject(List<T> list, Function<T, Integer> getter, BiConsumer<T, String> setter, Class<? extends BaseEnum> enumClass) {
        if (list == null || list.isEmpty()) {
            return;
        }
        for (T t : list) {
            Integer enumValue = getter.apply(t);
            if (enumValue != null) {
                setter.accept(t, getEnumDescByValue(enumValue, enumClass));
            }
        }
    }

}
