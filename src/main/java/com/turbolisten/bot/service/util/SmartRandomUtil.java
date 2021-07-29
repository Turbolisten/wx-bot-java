package com.turbolisten.bot.service.util;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;

import java.security.SecureRandom;
import java.util.*;

/**
 * 产生随机数，打乱List顺序,随机取值
 *
 * @author jiaozi
 */
public class SmartRandomUtil extends RandomUtils {

    private static final String NUM = "1234567890";

    private static final String WORD = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";

    /**
     * Returns a random long List within the specified range.
     *
     * @return
     */
    public static List<Long> randomDiffInRange(final long startInclusive, final long endExclusive, int randomCount) {
        if (randomCount < 1) {
            return Collections.emptyList();
        }

        if (startInclusive >= endExclusive) {
            throw new RuntimeException("<<KuRandomUtil>> randomLong , endExclusive >= startInclusive, " + endExclusive + " >= " + startInclusive);
        }

        if (randomCount >= (endExclusive - startInclusive)) {
            ArrayList<Long> result = new ArrayList<Long>();
            for (long i = startInclusive; i < endExclusive; i++) {
                result.add(i);
            }
            return result;
        } else {
            HashSet<Long> set = new HashSet<Long>(randomCount);
            int i = 0;
            while (i < randomCount) {
                long nextLong = nextLong(startInclusive, endExclusive);
                if (set.add(nextLong)) {
                    i++;
                }
            }
            return new ArrayList<Long>(set);
        }
    }

    public static List<Integer> randomDiffInRange(final int startInclusive, final int endExclusive, int randomCount) {
        if (randomCount < 1) {
            return Collections.emptyList();
        }

        if (startInclusive >= endExclusive) {
            throw new RuntimeException("<<KuRandomUtil>> randomLong , endExclusive >= startInclusive, " + endExclusive + " >= " + startInclusive);
        }

        if (randomCount >= (endExclusive - startInclusive)) {
            ArrayList<Integer> result = new ArrayList<Integer>();
            for (int i = startInclusive; i < endExclusive; i++) {
                result.add(i);
            }
            return result;
        } else {
            HashSet<Integer> set = new HashSet<Integer>(randomCount);
            int i = 0;
            while (i < randomCount) {
                int nextLong = nextInt(startInclusive, endExclusive);
                if (set.add(nextLong)) {
                    i++;
                }
            }
            return new ArrayList<Integer>(set);
        }
    }

    /**
     * 随机取一个
     *
     * @param list
     * @return
     */
    public static <T> T randomOne(List<T> list) {
        List<T> randomDifferent = randomDiffInRearrange(list, 1);
        if (randomDifferent.isEmpty()) {
            return null;
        }
        return randomDifferent.get(0);
    }

    /**
     * 打乱顺序，重新排列List, 返回的还是原来的list
     *
     * @param list
     * @return
     */
    public static <T> List<T> rearrange(List<T> list) {
        List<T> result = new ArrayList<T>(list);
        int size = list.size(), j = - 1;
        T element = null;
        Random random = new Random();
        for (int i = 0; i < size / 2; i++) {
            j = random.nextInt(size - i) + i;
            if (j != i) {
                element = result.get(i);
                result.set(i, result.get(j));
                result.set(j, element);
            }
        }
        return result;
    }

    /**
     * 打乱顺序，并从中随机选取几个
     *
     * @param list
     * @param totalRandomCount
     * @return
     */
    public static <K> List<K> randomDiffInRearrange(List<K> list, int totalRandomCount) {
        if (CollectionUtils.isEmpty(list) || totalRandomCount < 1) {
            return Collections.emptyList();
        }

        if (totalRandomCount >= list.size()) {
            return rearrange(list);
        }
        Random random = new Random();
        ArrayList<K> res = new ArrayList<K>(totalRandomCount);
        int len = list.size();
        for (int i = 0; i < len; i++) {
            if (random.nextInt(len - i) < totalRandomCount) {
                res.add(list.get(i));
                totalRandomCount--;
                if (totalRandomCount == 0) {
                    break;
                }
            }
        }
        return res;
    }

    /**
     * 生成N位的随机字符串
     * 包含 大写A-Z 小写a-z 以及数字
     *
     * @param length
     * @return
     * @author listen
     */
    public static String generateRandomString(int length) {
        final String SOURCES = WORD + NUM;
        char[] text = new char[length];
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < length; i++) {
            text[i] = SOURCES.charAt(secureRandom.nextInt(SOURCES.length()));
        }
        return new String(text);
    }

    /**
     * 生成N位的随机数字
     *
     * @param length
     * @return
     * @author listen
     */
    public static String generateRandomNum(int length) {
        final String SOURCES = NUM;
        char[] text = new char[length];
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < length; i++) {
            text[i] = SOURCES.charAt(secureRandom.nextInt(SOURCES.length()));
        }
        return new String(text);
    }

}
