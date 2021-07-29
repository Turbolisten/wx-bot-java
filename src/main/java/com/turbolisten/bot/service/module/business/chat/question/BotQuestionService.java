package com.turbolisten.bot.service.module.business.chat.question;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.turbolisten.bot.service.common.domain.ResponseDTO;
import com.turbolisten.bot.service.module.business.bot.WxBotConst;
import com.turbolisten.bot.service.module.business.bot.WxBotSendMsgService;
import com.turbolisten.bot.service.module.business.chat.ChatModel;
import com.turbolisten.bot.service.module.business.question.question.QuestionService;
import com.turbolisten.bot.service.module.business.question.question.domain.QuestionQueryDTO;
import com.turbolisten.bot.service.module.business.question.question.domain.QuestionVO;
import com.turbolisten.bot.service.module.business.question.record.QuestionAnswerRecordService;
import com.turbolisten.bot.service.util.SmartBigDecimalUtils;
import io.github.wechaty.user.Message;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 机器试题业务
 *
 * @author Turbolisten
 * @date 2021/7/17 22:23
 */
@Service
public class BotQuestionService {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private WxBotSendMsgService sendMsgService;

    @Autowired
    private QuestionAnswerRecordService answerRecordService;

    /**
     * 随机试题缓存
     */
    private Cache<String, List<QuestionVO>> RANDOM_QUESTION_CACHE = CacheBuilder.newBuilder().expireAfterWrite(8, TimeUnit.HOURS).build();

    /**
     * 当前试题缓存
     */
    private Cache<String, QuestionVO> CURRENT_QUESTION_CACHE = CacheBuilder.newBuilder().expireAfterWrite(8, TimeUnit.HOURS).build();

    /**
     * 当前用户答题统计缓存
     */
    private Cache<String, BotQuestionAnswerStatisticsDTO> ANSWER_STATISTICS_CACHE = CacheBuilder.newBuilder().expireAfterWrite(8, TimeUnit.HOURS).build();

    private void clearCache(String fromWxId) {
        RANDOM_QUESTION_CACHE.invalidate(fromWxId);
        CURRENT_QUESTION_CACHE.invalidate(fromWxId);
        ANSWER_STATISTICS_CACHE.invalidate(fromWxId);
        ChatModel.removeChatModelCache(fromWxId);
    }

    /**
     * 进入刷题模式
     *
     * @param fromWxId
     * @param message
     */
    public void enterQuestionModel(String fromWxId, Message message) {
        ChatModel.setModel(fromWxId, ChatModel.ModelEnum.QUESTION);
        StringBuilder builder = new StringBuilder();
        builder.append(WxBotConst.Emoji.JIA_YOU)
                .append("马上开始！准备在知识的海洋里狗刨吧~\n")
                .append(WxBotConst.Emoji.SHOU_ZHI_YOU).append("退出请回复：退出刷题");
        sendMsgService.sendMsg(message, builder.toString());
        this.handleQuestionModel(fromWxId, message);
    }

    /**
     * 退出刷题
     */
    public void exitQuestionModel(String fromWxId, Message message) {
        // 发送最后1道题解析
        List replyList = Lists.newArrayList();
        QuestionVO currentQuestion = CURRENT_QUESTION_CACHE.getIfPresent(fromWxId);
        if (null != currentQuestion) {
            // 获取答案
            List<String> rightAnswer = BotQuestionUtil.getQuestionRightAnswer(currentQuestion);
            String analysis = BotQuestionUtil.buildAnalysisText(currentQuestion, rightAnswer.toString());
            replyList.add(analysis);
        }

        replyList.add(WxBotConst.Emoji.YU_KUAI + "已退出刷题模式，智力+999");

        // 查询拼装答题统计结果
        BotQuestionAnswerStatisticsDTO statisticsDTO = ANSWER_STATISTICS_CACHE.getIfPresent(fromWxId);
        if (null != statisticsDTO) {
            replyList.add(BotQuestionUtil.buildAnswerStatistics(statisticsDTO));
        }
        // 发送消息
        sendMsgService.sendMsgMulti(message, replyList);
        this.clearCache(fromWxId);
    }

    /**
     * 出题
     *
     * @param fromWxId
     * @param message
     */
    public void handleQuestionModel(String fromWxId, Message message) {
        List<QuestionVO> questionList;
        try {
            questionList = RANDOM_QUESTION_CACHE.get(fromWxId, () -> {
                // 查询试题 随机100道
                QuestionQueryDTO queryDTO = new QuestionQueryDTO();
                queryDTO.setLimit(50);
                return questionService.query(queryDTO);
            });
        } catch (ExecutionException e) {
            sendMsgService.sendMsg(message, WxBotConst.Emoji.YU_KUAI + "题库空空如也~");
            return;
        }

        if (CollectionUtils.isEmpty(questionList)) {
            sendMsgService.sendMsg(message, WxBotConst.Emoji.YU_KUAI + "题库空空如也~");
            return;
        }

        /**
         * 获取当前答题位置
         * 获取下一道题
         * 发送试题
         */
        QuestionVO currentQuestion = CURRENT_QUESTION_CACHE.getIfPresent(fromWxId);
        int currentNo = null != currentQuestion ? currentQuestion.getQuestionNo() : 0;
        Optional<QuestionVO> optional = questionList.stream().filter(e -> e.getQuestionNo() > currentNo).limit(1).findFirst();
        if (!optional.isPresent()) {
            sendMsgService.sendMsg(message, WxBotConst.Emoji.YU_KUAI + "本轮刷题结束啦~已自动退出刷题模式");
            this.clearCache(fromWxId);
            return;
        }
        // 存储当前试题
        QuestionVO questionVO = optional.get();
        CURRENT_QUESTION_CACHE.put(fromWxId, questionVO);

        // 拼装试题及选项
        List<String> questionContent = BotQuestionUtil.buildQuestionText(questionVO);
        sendMsgService.sendMsgMulti(message, questionContent);
    }

    /**
     * 处理 试题回答
     *
     * @param fromWxId
     * @param message
     */
    public void handleQuestionAnswer(String fromWxId, String msg, Message message) {
        // 获取当前试题缓存
        QuestionVO questionVO = CURRENT_QUESTION_CACHE.getIfPresent(fromWxId);
        if (null == questionVO) {
            return;
        }

        // 获取题目正确答案
        List<String> questionRightAnswer = BotQuestionUtil.getQuestionRightAnswer(questionVO);

        // 判断回答是否正确
        ResponseDTO<Boolean> res = BotQuestionUtil.isAnswerRight(questionVO, questionRightAnswer, msg);
        if (!res.isSuccess()) {
            sendMsgService.sendMsg(message, res.getMsg());
            return;
        }
        Boolean isAnswerRight = res.getData();

        // 保存本次答题结果
        this.saveCurrentAnswer(fromWxId, questionVO.getQuestionId(), isAnswerRight);

        // 获取 回答是否正确 文本
        String answerResult = BotQuestionUtil.buildAnswerResult(isAnswerRight);

        // 获取 试题解析 文本
        String analysis = BotQuestionUtil.buildAnalysisText(questionVO, questionRightAnswer.toString());

        // 发送消息
        sendMsgService.sendMsgMulti(message, Lists.newArrayList(answerResult, analysis));

        // 发送下一题
        try {
            Thread.sleep(1300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.handleQuestionModel(fromWxId, message);
    }

    /**
     * 保存回答
     *
     * @param fromWxId
     * @param isRight
     */
    public void saveCurrentAnswer(String fromWxId, Long questionId, boolean isRight) {
        int rightPlus = isRight ? 1 : 0;
        BotQuestionAnswerStatisticsDTO statisticsDTO = ANSWER_STATISTICS_CACHE.getIfPresent(fromWxId);
        if (null == statisticsDTO) {
            statisticsDTO = new BotQuestionAnswerStatisticsDTO();
            statisticsDTO.setStartTime(LocalDateTime.now());
            statisticsDTO.setTotalQuestionNum(1);
            statisticsDTO.setAnswerRightNum(rightPlus);
            statisticsDTO.setUseTime(0L);
            statisticsDTO.setRightRate(SmartBigDecimalUtils.bigDecimalPercent(statisticsDTO.getTotalQuestionNum(), statisticsDTO.getAnswerRightNum(), 2));
        } else {
            statisticsDTO.setTotalQuestionNum(statisticsDTO.getTotalQuestionNum() + 1);
            statisticsDTO.setAnswerRightNum(statisticsDTO.getAnswerRightNum() + rightPlus);
            statisticsDTO.setUseTime(Duration.between(statisticsDTO.getStartTime(), LocalDateTime.now()).getSeconds());
            statisticsDTO.setRightRate(SmartBigDecimalUtils.bigDecimalPercent(statisticsDTO.getAnswerRightNum(), statisticsDTO.getTotalQuestionNum(), 2));
        }
        ANSWER_STATISTICS_CACHE.put(fromWxId, statisticsDTO);

        // 保存答题记录
        answerRecordService.saveRecord(fromWxId, questionId, isRight);
    }
}
