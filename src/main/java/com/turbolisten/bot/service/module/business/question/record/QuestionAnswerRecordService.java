package com.turbolisten.bot.service.module.business.question.record;

import com.turbolisten.bot.service.config.AsyncConfig;
import com.turbolisten.bot.service.module.business.question.answerstatistics.QuestionAnswerStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 答题记录 业务
 *
 * @author Turbolisten
 * @date 2021/7/19 22:08
 */
@Service
public class QuestionAnswerRecordService {

    @Autowired
    private QuestionAnswerStatisticsService answerStatisticsService;

    /**
     * 保存答题记录
     *
     * @param userId
     * @param questionId
     * @param isRight
     */
    @Async(AsyncConfig.ASYNC_EXECUTOR)
    public void saveRecord(String userId, Long questionId, boolean isRight) {
        // TODO 保存答题记录

        // 更新答题统计
        answerStatisticsService.update(userId, questionId, isRight);
    }
}
