package com.turbolisten.bot.service.module.business.chat.question;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 答题结果统计
 *
 * @author Turbolisten
 * @date 2021/7/18 0:48
 */
@Data
public class BotQuestionAnswerStatisticsDTO {

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 总答题数
     */
    private Integer totalQuestionNum;

    /**
     * 回答正确数量
     */
    private Integer answerRightNum;

    /**
     * 耗时/秒
     */
    private Long useTime;

    /**
     * 正确率
     */
    private BigDecimal rightRate;
}
