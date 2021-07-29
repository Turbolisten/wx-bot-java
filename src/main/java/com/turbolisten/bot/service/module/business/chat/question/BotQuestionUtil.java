package com.turbolisten.bot.service.module.business.chat.question;

import com.google.common.collect.Lists;
import com.turbolisten.bot.service.common.codeconst.ResponseCodeConst;
import com.turbolisten.bot.service.common.constant.CommonConst;
import com.turbolisten.bot.service.common.domain.ResponseDTO;
import com.turbolisten.bot.service.module.business.bot.WxBotConst;
import com.turbolisten.bot.service.module.business.question.question.constant.QuestionTypeEnum;
import com.turbolisten.bot.service.module.business.question.question.domain.QuestionOptionVO;
import com.turbolisten.bot.service.module.business.question.question.domain.QuestionVO;
import com.turbolisten.bot.service.util.SmartBaseEnumUtil;
import com.turbolisten.bot.service.util.SmartLocalDateUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 机器试题业务
 *
 * @author Turbolisten
 * @date 2021/7/17 22:23
 */
public class BotQuestionUtil {

    /**
     * 拼装试题及选项 文本字符串
     *
     * @param questionVO
     * @return
     */
    public static List<String> buildQuestionText(QuestionVO questionVO) {
        // 查询试题类型
        String questionType = SmartBaseEnumUtil.getEnumDescByValue(questionVO.getQuestionType(), QuestionTypeEnum.class);

        // 拼装试题题目
        StringBuilder sb = new StringBuilder();
        sb.append(WxBotConst.Emoji.JIA_YOU + " 第 ").append(questionVO.getQuestionNo()).append(" 题\n");
        sb.append(WxBotConst.Emoji.QI_QIU).append(questionVO.getParentCatalogName()).append("-").append(questionVO.getCatalogName())
                .append("[").append(questionVO.getQuestionId()).append("]\n\n");
        sb.append("【").append(questionType).append("】").append(questionVO.getQuestionTitle());

        List<String> questionContentList = Lists.newArrayList(sb.toString());

        // 拼装选项
        List<QuestionOptionVO> optionList = questionVO.getOptionList();
        if (CollectionUtils.isNotEmpty(optionList)) {
            // 换行显示
            List<String> optionNameList = optionList.stream().map(QuestionOptionVO::getOptionName).collect(Collectors.toList());
            String option = StringUtils.join(optionNameList, "\n");
            questionContentList.add(option);
        }
        return questionContentList;
    }

    /**
     * 拼装答案解析 文本
     *
     * @param questionVO
     * @param rightAnswer
     * @return
     */
    public static String buildAnalysisText(QuestionVO questionVO, String rightAnswer) {
        return new StringBuilder()
                .append("【正确答案】")
                .append(rightAnswer)
                .append("\n")
                .append(questionVO.getQuestionAnalysis())
                .append("\n")
                .append(questionVO.getRemark())
                .toString();
    }

    /**
     * 获取题目 正确答案
     *
     * @param questionVO
     * @return
     */
    public static List<String> getQuestionRightAnswer(QuestionVO questionVO) {
        QuestionTypeEnum questionType = SmartBaseEnumUtil.getEnumByValue(questionVO.getQuestionType(), QuestionTypeEnum.class);
        switch (questionType) {
            case SINGLE:
            case MULTIPLE:
                return questionVO.getOptionList().stream().filter(QuestionOptionVO::getRightFlag)
                        .map(QuestionOptionVO::getOptionTag).collect(Collectors.toList());
            case TRUE_FALSE:
                return Lists.newArrayList(questionVO.getRightFlag() ? "对" : "错");
            default:
                return CommonConst.EMPTY_LIST;
        }
    }

    /**
     * 判断选择题回答是否正确
     *
     * @param rightTag
     * @param answer
     * @return
     */
    public static Boolean isRight4ChooseQuestion(List<String> rightTag, String answer) {
        if (!Pattern.matches("[a-zA-Z\\s]+", answer)) {
            return null;
        }
        // 回答移除空格
        List<String> answerList = Stream.of(answer.toUpperCase().split(""))
                .filter(StringUtils::isNotBlank).collect(Collectors.toList());
        return CollectionUtils.isEqualCollection(rightTag, answerList);
    }

    public static void main(String[] args) {
        System.out.println(CollectionUtils.isEqualCollection(Lists.newArrayList("C", "A"), Lists.newArrayList("A", "C")));
    }

    /**
     * 判断题回答是否正确
     *
     * @param right
     * @param answer
     * @return
     */
    public static Boolean isRight4CheckQuestion(boolean right, String answer) {
        answer = answer.trim();
        boolean answerFlag;
        if (Objects.equals(answer, "对")
                || Objects.equals(answer, "正确")) {
            answerFlag = true;
        } else if (Objects.equals(answer, "错")
                || Objects.equals(answer, "错误")) {
            answerFlag = false;
        } else {
            return null;
        }
        return right == answerFlag;
    }

    /**
     * 构建 回答结果 是否正确
     *
     * @param isAnswerRight
     * @return
     */
    public static String buildAnswerResult(boolean isAnswerRight) {
        if (isAnswerRight) {
            return WxBotConst.Emoji.YU_KUAI + "回答正确，你可真是个小天才";
        }
        return WxBotConst.Emoji.ZHU_TOU + "回答错误，再接再厉啊~";
    }

    /**
     * 判断回答是否正确
     *
     * @param questionVO
     * @param questionRightOption
     * @param answer
     * @return
     */
    public static ResponseDTO<Boolean> isAnswerRight(QuestionVO questionVO, List<String> questionRightOption, String answer) {
        // 判断回答是否正确
        QuestionTypeEnum questionType = SmartBaseEnumUtil.getEnumByValue(questionVO.getQuestionType(), QuestionTypeEnum.class);
        Boolean isAnswerRight;
        switch (questionType) {
            case SINGLE:
            case MULTIPLE:
                // 选择题
                isAnswerRight = isRight4ChooseQuestion(questionRightOption, answer);
                if (null == isAnswerRight) {
                    return ResponseDTO.wrapMsg(ResponseCodeConst.ERROR_PARAM, "选择题请回复：A-Z");
                }
                break;
            case TRUE_FALSE:
                isAnswerRight = isRight4CheckQuestion(questionVO.getRightFlag(), answer);
                if (null == isAnswerRight) {
                    return ResponseDTO.wrapMsg(ResponseCodeConst.ERROR_PARAM, "判断题请回复：对|错");
                }
                break;
            default:
                throw new RuntimeException("not handle question type");
        }
        return ResponseDTO.succData(isAnswerRight);
    }

    /**
     * 拼装 答题结果 文本字符串
     *
     * @param statisticsDTO
     * @return
     */
    public static String buildAnswerStatistics(BotQuestionAnswerStatisticsDTO statisticsDTO) {
        StringBuilder builder = new StringBuilder();
        builder.append(WxBotConst.Emoji.JIA_YOU + "本次答题统计\n")
                .append("总答题数：").append(statisticsDTO.getTotalQuestionNum()).append("\n")
                .append("回答正确：").append(statisticsDTO.getAnswerRightNum()).append("\n")
                .append("用时：").append(SmartLocalDateUtil.secondToTime(statisticsDTO.getUseTime())).append("\n")
                .append("正确率：").append(statisticsDTO.getRightRate()).append("%\n")
                .append("加油！永远年轻，永远热爱学习");
        return builder.toString();
    }
}
