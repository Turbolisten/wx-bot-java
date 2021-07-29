package com.turbolisten.bot.service.module.business.question.answerstatistics;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.turbolisten.bot.service.module.business.question.answerstatistics.domain.QuestionAnswerStatisticsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 答题统计 业务
 *
 * @author Turbolisten
 * @date 2021/7/19 22:08
 */
@Service
public class QuestionAnswerStatisticsService {

    @Autowired
    private QuestionAnswerStatisticsDao wrongRecordDao;

    private static final Interner<String> lock = Interners.newWeakInterner();

    /**
     * 更新答题统计
     *
     * @param userId
     * @param questionId
     * @param isRight
     */
    public void update(String userId, Long questionId, boolean isRight) {
        synchronized (lock.intern(userId + "_" + questionId)) {
            // 根据用户id 试题id 查询记录
            QuestionAnswerStatisticsEntity recordEntity = wrongRecordDao.selectByUserAndQuestion(userId, questionId);
            if (null == recordEntity) {
                // 新增
                recordEntity = new QuestionAnswerStatisticsEntity();
                recordEntity.setUserId(userId);
                recordEntity.setQuestionId(questionId);
                recordEntity.setAnswerCount(1);
                recordEntity.setWrongCount(isRight ? 0 : 1);
                wrongRecordDao.insert(recordEntity);
                return;
            }
            // 更新
            QuestionAnswerStatisticsEntity updateEntity = new QuestionAnswerStatisticsEntity();
            updateEntity.setRecordId(recordEntity.getRecordId());
            updateEntity.setAnswerCount(recordEntity.getAnswerCount() + 1);
            if (!isRight) {
                updateEntity.setWrongCount(recordEntity.getWrongCount() + 1);
            }
            wrongRecordDao.updateById(updateEntity);
        }
    }
}
