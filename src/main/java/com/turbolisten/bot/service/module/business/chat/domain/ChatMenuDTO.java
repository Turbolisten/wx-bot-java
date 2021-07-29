package com.turbolisten.bot.service.module.business.chat.domain;

import lombok.Data;

import java.util.List;

/**
 * 对话菜单 DTO
 *
 * @author Turbolisten
 * @date 2021/7/17 22:37
 */
@Data
public class ChatMenuDTO {
    /**
     * 菜单编号
     */
    private Integer menuModel;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 子菜单
     */
    private List<ChatMenuDTO> children;
}
