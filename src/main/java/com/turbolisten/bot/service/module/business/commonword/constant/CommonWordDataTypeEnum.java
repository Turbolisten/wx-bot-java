package com.turbolisten.bot.service.module.business.commonword.constant;

import com.turbolisten.bot.service.common.constant.BaseEnum;
import io.github.wechaty.schemas.MessageType;

/**
 * 文案数据类型 枚举类
 *
 * @author listen
 * @date 2021/07/11 10:50
 */
public enum CommonWordDataTypeEnum implements BaseEnum {

    /**
     * 0 文本内容
     */
    TEXT(0, MessageType.Text, "文本内容"),

    /**
     * 1 链接
     */
    URL(1, MessageType.Url, "链接"),

    /**
     * 2 小程序
     */
    MINI_PROGRAM(2, MessageType.MiniProgram, "小程序"),

    /**
     * 3 文件附件
     */
    ATTACHMENT(3, MessageType.Attachment, "文件附件"),

    /**
     * 4 语音
     */
    AUDIO(4, MessageType.Audio, "语音"),

    /**
     * 5 图片
     */
    IMAGE(5, MessageType.Image, "图片"),

    /**
     * 6 视频
     */
    VIDEO(6, MessageType.Video, "视频"),

    /**
     * 7 联系人
     */
    CONTACT(7, MessageType.Contact, "联系人"),

    /**
     * 8 地理位置
     */
    LOCATION(8, MessageType.Location, "地理位置"),


    ;
    private Integer type;

    private MessageType messageType;

    private String desc;

    CommonWordDataTypeEnum(Integer type, MessageType messageType, String desc) {
        this.type = type;
        this.messageType = messageType;
        this.desc = desc;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    /**
     * 获取枚举类的值
     *
     * @return Integer
     */
    @Override
    public Integer getValue() {
        return type;
    }

    /**
     * 获取枚举类的说明
     *
     * @return String
     */
    @Override
    public String getDesc() {
        return desc;
    }
}
