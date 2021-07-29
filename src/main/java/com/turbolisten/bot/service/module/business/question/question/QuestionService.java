package com.turbolisten.bot.service.module.business.question.question;

import com.turbolisten.bot.service.module.business.question.question.constant.QuestionTypeEnum;
import com.turbolisten.bot.service.module.business.question.question.domain.QuestionOptionVO;
import com.turbolisten.bot.service.module.business.question.question.domain.QuestionQueryDTO;
import com.turbolisten.bot.service.module.business.question.question.domain.QuestionVO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 试题业务
 *
 * @author Turbolisten
 * @date 2021/7/17 22:08
 */
@Service
public class QuestionService {

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private QuestionOptionDao optionDao;

    /**
     * 查询随机试题
     *
     * @param queryDTO
     * @return
     */
    public List<QuestionVO> query(QuestionQueryDTO queryDTO) {
        queryDTO.setDeletedFlag(false);
        queryDTO.setPublishFlag(true);
        List<QuestionVO> questionList = questionDao.queryByRand(queryDTO);
        if (CollectionUtils.isEmpty(questionList)) {
            return questionList;
        }

        // 设置本次试题编号
        int no = 1;
        for (QuestionVO question : questionList) {
            question.setQuestionNo(no++);
        }

        // 查询选择题试题选项
        List<QuestionVO> checkList = questionList.stream().filter(e -> QuestionTypeEnum.SINGLE.equalsValue(e.getQuestionType()) || QuestionTypeEnum.MULTIPLE.equalsValue(e.getQuestionType())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(checkList)) {
            List<Long> idList = checkList.stream().map(QuestionVO::getQuestionId).collect(Collectors.toList());
            Map<Long, List<QuestionOptionVO>> optionMap = optionDao.queryByQuestionId(idList).stream().collect(Collectors.groupingBy(QuestionOptionVO::getQuestionId));
            // 设置试题选项
            checkList.forEach(e -> {
                e.setOptionList(optionMap.get(e.getQuestionId()));
            });
        }
        return questionList;
    }


}
