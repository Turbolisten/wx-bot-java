package com.turbolisten.bot.service.module.business.question.question;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.turbolisten.bot.service.module.business.question.question.domain.QuestionOptionVO;
import com.turbolisten.bot.service.module.business.question.question.domain.entity.QuestionOptionEntity;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 试题选项 dao
 *
 * @author Turbolisten
 * @date 2021/7/14 22:54
 */
@Component
public interface QuestionOptionDao extends BaseMapper<QuestionOptionEntity> {

    /**
     * 查询试题选项
     *
     * @param questionIdList
     * @return
     */
    List<QuestionOptionVO> queryByQuestionId(List<Long> questionIdList);
}
