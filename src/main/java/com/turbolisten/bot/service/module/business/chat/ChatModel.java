package com.turbolisten.bot.service.module.business.chat;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.turbolisten.bot.service.common.constant.BaseEnum;
import com.turbolisten.bot.service.module.business.chat.domain.ChatMenuDTO;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 对话 菜单
 *
 * @author Turbolisten
 * @date 2021/7/17 22:32
 */
public class ChatModel {

    /**
     * 聊天模式缓存
     */
    private static Cache<String, ModelEnum> CHAT_MODEL_CACHE = CacheBuilder.newBuilder().expireAfterWrite(4, TimeUnit.HOURS).build();

    /**
     * 查询聊天模式
     *
     * @param fromWxId
     */
    public static ModelEnum getModel(String fromWxId) {
        return CHAT_MODEL_CACHE.getIfPresent(fromWxId);
    }

    /**
     * 设置聊天模式
     *
     * @param fromWxId
     */
    public static void setModel(String fromWxId, ModelEnum modelEnum) {
        CHAT_MODEL_CACHE.put(fromWxId, modelEnum);
    }

    /**
     * 移除聊天模式
     *
     * @param fromWxId
     */
    public static void removeChatModelCache(String fromWxId) {
        CHAT_MODEL_CACHE.invalidate(fromWxId);
    }

    public static List<ChatMenuDTO> menuList;

    static {
        menuList = Lists.newArrayList();
        // 闲聊模式
        ChatMenuDTO menuDTO = new ChatMenuDTO();
        menuDTO.setMenuModel(ModelEnum.CHAT.type);
        menuDTO.setMenuName(ModelEnum.CHAT.desc);
        menuList.add(menuDTO);

        // 答题模式
        menuDTO = new ChatMenuDTO();
        menuDTO.setMenuModel(ModelEnum.QUESTION.type);
        menuDTO.setMenuName(ModelEnum.QUESTION.desc);
        menuList.add(menuDTO);
    }

    /**
     * 菜单模式 枚举类
     */
    public enum ModelEnum implements BaseEnum {

        CHAT(100, "聊天灌水"),

        QUESTION(200, "答题模式"),

        ;
        private final Integer type;

        private final String desc;

        ModelEnum(Integer type, String desc) {
            this.type = type;
            this.desc = desc;
        }

        @Override
        public Integer getValue() {
            return type;
        }

        @Override
        public String getDesc() {
            return desc;
        }
    }
}
