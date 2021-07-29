package com.turbolisten.bot.service.module.business.question.question;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.turbolisten.bot.service.module.business.question.question.domain.QuestionQueryDTO;
import com.turbolisten.bot.service.module.business.question.question.domain.QuestionVO;
import com.turbolisten.bot.service.module.business.question.question.domain.entity.QuestionEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 试题 dao
 *
 * @author Turbolisten
 * @date 2021/7/14 22:54
 */
@Component
public interface QuestionDao extends BaseMapper<QuestionEntity> {

    /**
     * 根据题目查询id
     *
     * @param title
     * @return
     */
    Long selectIdByTitle(String title);

    /**
     * 随机查询试题
     *
     * @param queryDTO
     * @return
     */
    List<QuestionVO> queryByRand(@Param("query") QuestionQueryDTO queryDTO);
}
