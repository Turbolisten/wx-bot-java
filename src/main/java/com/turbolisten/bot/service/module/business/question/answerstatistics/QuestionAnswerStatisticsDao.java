package com.turbolisten.bot.service.module.business.question.answerstatistics;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.turbolisten.bot.service.module.business.question.answerstatistics.domain.QuestionAnswerStatisticsEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * 答题统计 dao
 *
 * @author Turbolisten
 * @date 2021/7/19 22:07
 */
@Component
public interface QuestionAnswerStatisticsDao extends BaseMapper<QuestionAnswerStatisticsEntity> {

    /**
     * 根据用户 试题id 查询答题统计记录
     *
     * @param userId
     * @param questionId
     * @return
     */
    QuestionAnswerStatisticsEntity selectByUserAndQuestion(@Param("userId") String userId,
                                                           @Param("questionId") Long questionId);

}
