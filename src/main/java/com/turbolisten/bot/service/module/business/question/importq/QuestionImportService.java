package com.turbolisten.bot.service.module.business.question.importq;

import com.google.common.collect.Lists;
import com.turbolisten.bot.service.common.constant.CommonConst;
import com.turbolisten.bot.service.common.domain.ResponseDTO;
import com.turbolisten.bot.service.module.business.question.importq.domain.QuestionAnswerDTO;
import com.turbolisten.bot.service.module.business.question.importq.domain.QuestionTitleDTO;
import com.turbolisten.bot.service.module.business.question.question.QuestionDao;
import com.turbolisten.bot.service.module.business.question.question.QuestionOptionManager;
import com.turbolisten.bot.service.module.business.question.question.constant.QuestionTypeEnum;
import com.turbolisten.bot.service.module.business.question.question.domain.entity.QuestionEntity;
import com.turbolisten.bot.service.module.business.question.question.domain.entity.QuestionOptionEntity;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author Turbolisten
 * @date 2021/7/16 22:37
 */
@Slf4j
@Service
public class QuestionImportService {

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private QuestionOptionManager optionManager;

    public static void main(String[] args) {
        String str = "D.具有创立容易、经营管理灵活自由、不需要交纳企业所得税等优点 ";

        System.out.println(parseCheckOption(str));
    }

    /**
     * 导入试题
     *
     * @param path
     * @param catalogId
     * @return
     */
    public ResponseDTO<String> importQuestion(String path, Long catalogId) {
        List<String> list = readFile(path);
        QuestionParseResultDTO resultDTO = parseTxt(list);
        return this.saveQuestion(resultDTO, catalogId);
    }

    /**
     * 保存试题
     *
     * @param resultDTO
     * @param catalogId
     * @return
     */
    private ResponseDTO<String> saveQuestion(QuestionParseResultDTO resultDTO, Long catalogId) {
        Map<Long, QuestionEntity> questionMap = resultDTO.getQuestionMap();
        if (MapUtils.isEmpty(questionMap)) {
            return ResponseDTO.succMsg("试题空空空如也");
        }

        int saveCount = 0;
        Set<Map.Entry<Long, QuestionEntity>> entries = questionMap.entrySet();
        Map<Long, List<QuestionOptionEntity>> optionMap = resultDTO.getOptionMap();

        // 校验
        for (Map.Entry<Long, QuestionEntity> entry : entries) {
            Long questionId = entry.getKey();
            QuestionEntity questionEntity = entry.getValue();

            // 查询选项
            List<QuestionOptionEntity> optionList = optionMap.get(questionId);

            // 校验选项 选择题
            Integer questionType = questionEntity.getQuestionType();
            String questionTitle = questionEntity.getQuestionTitle();
            if (QuestionTypeEnum.SINGLE.equalsValue(questionType)
                    || QuestionTypeEnum.MULTIPLE.equalsValue(questionType)) {
                if (CollectionUtils.isEmpty(optionList)) {
                    throw new RuntimeException("缺少选项 --->" + questionId + " -> " + questionTitle);
                }
                // 校验选项是否有正确答案
                boolean anyMatch = optionList.stream().anyMatch(QuestionOptionEntity::getRightFlag);
                if (!anyMatch) {
                    throw new RuntimeException("选项缺少正确答案 --->" + questionId + " -> " + questionTitle);
                }
            }

            // 判断题
            if (QuestionTypeEnum.TRUE_FALSE.equalsValue(questionType) && null == questionEntity.getRightFlag()) {
                throw new RuntimeException("判断题缺少答案 --->" + questionId + " -> " + questionTitle);
            }
        }

        // 保存数据
        for (Map.Entry<Long, QuestionEntity> entry : entries) {
            Long questionId = entry.getKey();
            QuestionEntity questionEntity = entry.getValue();

            // 校验题目是否存在
            Long id = questionDao.selectIdByTitle(questionEntity.getQuestionTitle());
            if (null != id) {
                continue;
            }

            // 保存数据
            questionEntity.setCatalogId(catalogId);
            questionEntity.setPoint(1);
            questionEntity.setPublishFlag(true);
            questionEntity.setQuestionId(null);
            questionDao.insert(questionEntity);

            // 查询选项
            List<QuestionOptionEntity> optionList = optionMap.get(questionId);
            if (CollectionUtils.isNotEmpty(optionList)) {
                optionList.forEach(e -> e.setQuestionId(questionEntity.getQuestionId()));
                optionManager.saveBatch(optionList);
            }

            saveCount++;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("总题数:" + questionMap.size());
        sb.append("成功导入:" + saveCount);

        return ResponseDTO.succMsg(sb.toString());
    }

    public static List<String> readFile(String path) {
        List<String> lines = CommonConst.EMPTY_LIST;
        File file = new File(path);
        try {
            FileInputStream inputStream = new FileInputStream(file);
            lines = IOUtils.readLines(inputStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("read file error:", e);
        }
        return lines;
    }


    public static QuestionParseResultDTO parseTxt(List<String> lineList) {
        QuestionEntity questionEntity;
        QuestionOptionEntity optionEntity;
        QuestionTypeEnum questionType = null;
        List<QuestionOptionEntity> optionList = Lists.newArrayList();
        QuestionTitleDTO titleDTO = null;

        Map<Long, QuestionEntity> questionMap = new LinkedHashMap<>();
        Map<Long, List<QuestionOptionEntity>> optionMap = new LinkedHashMap<>();

        boolean isResolutionPart = false;
        Long answerNo = null;
        for (String line : lineList) {
            // 空行
            if (StringUtils.isBlank(StringUtils.trimToEmpty(line))) {
                // 保存上一道题
                if (null != titleDTO) {
                    questionEntity = new QuestionEntity();
                    questionEntity.setQuestionId(titleDTO.getNo());
                    questionEntity.setQuestionTitle(titleDTO.getTitle());
                    questionEntity.setQuestionType(questionType.getValue());

                    questionMap.put(titleDTO.getNo(), questionEntity);
                    if (CollectionUtils.isNotEmpty(optionList)) {
                        optionMap.put(titleDTO.getNo(), optionList);
                        optionList = Lists.newArrayList();
                    }
                    titleDTO = null;
                }
                continue;
            }

            // 解析答案
            if (isResolutionPart) {
                // 答案选项
                QuestionAnswerDTO answerDTO = parseAnswer(line);
                if (null != answerDTO) {
                    // 设置正确选项
                    answerNo = answerDTO.getNo();
                    String rightOption = answerDTO.getRightOption();

                    questionEntity = questionMap.get(answerNo);

                    // 选择ti
                    if (QuestionTypeEnum.SINGLE.equalsValue(questionEntity.getQuestionType())
                            || QuestionTypeEnum.MULTIPLE.equalsValue(questionEntity.getQuestionType())) {
                        optionList = optionMap.get(answerNo);

                        // 分隔是为了多选题
                        String[] split = rightOption.split("");
                        for (String right : split) {
                            if (StringUtils.isBlank(right)) {
                                continue;
                            }
                            optionList.forEach(e -> {
                                if (StringUtils.contains(e.getOptionName(), right)) {
                                    e.setRightFlag(true);
                                }
                            });
                        }
                    }
                    // 判断题
                    if (QuestionTypeEnum.TRUE_FALSE.equalsValue(questionEntity.getQuestionType())) {
                        questionEntity.setRightFlag(StringUtils.contains(rightOption, "Y"));
                    }
                    continue;
                }
                // 解析答案
                String analysis = parseAnalysis(line);
                if (null != analysis) {
                    // 设置解析
                    questionEntity = questionMap.get(answerNo);
                    questionEntity.setQuestionAnalysis(analysis);
                }
                // 知识点
                String point = parsePoint(line);
                if (null != point) {
                    // 设置知识点
                    questionEntity = questionMap.get(answerNo);
                    questionEntity.setRemark(point);
                }
                continue;
            }

            // 是否答案部分
            if (!isResolutionPart) {
                isResolutionPart = isResolutionPart(line);
            }

            // 判断题型
            QuestionTypeEnum tempQuestionType = parseQuestionType(line);
            if (null != tempQuestionType) {
                questionType = tempQuestionType;
                continue;
            }

            // 读取题目
            QuestionTitleDTO tempTitleDTO = parseQuestionTitle(line);
            if (null != tempTitleDTO) {
                titleDTO = tempTitleDTO;
                continue;
            }

            if (null != questionType) {
                switch (questionType) {
                    case SINGLE:
                    case MULTIPLE:
                        QuestionOptionParseResultDTO resultDTO = parseCheckOption(line);
                        if (null != resultDTO && StringUtils.isNotBlank(resultDTO.getOptionName())) {
                            continue;
                        }
                        optionEntity = new QuestionOptionEntity();
                        optionEntity.setQuestionId(titleDTO.getNo());
                        optionEntity.setOptionTag(resultDTO.getOptionTag());
                        optionEntity.setOptionName(resultDTO.getOptionName());
                        optionEntity.setRightFlag(false);

                        optionList.add(optionEntity);
                        break;
                    case TRUE_FALSE:
                        break;
                    default:
                        continue;
                }
            }
        }
        QuestionParseResultDTO resultDTO = new QuestionParseResultDTO();
        resultDTO.setQuestionMap(questionMap);
        resultDTO.setOptionMap(optionMap);

        return resultDTO;
    }


    public static QuestionAnswerDTO parseAnswer(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }

        int dotIndex = str.indexOf(".");
        if (dotIndex == -1) {
            return null;
        }
        String num = str.substring(0, dotIndex);
        if (!NumberUtils.isParsable(num)) {
            return null;
        }

        int endIndex = str.indexOf("】");
        String rightOption = str.substring(endIndex + 1).trim();

        QuestionAnswerDTO answerDTO = new QuestionAnswerDTO();
        answerDTO.setNo(NumberUtils.createLong(num));
        answerDTO.setRightOption(rightOption);
        answerDTO.setAnalysis(null);
        return answerDTO;
    }


    public static String parseAnalysis(String str) {
        if (StringUtils.contains(str, "【答案解析】")) {
            return str.trim();
        }
        return null;
    }

    public static String parsePoint(String str) {
        if (StringUtils.contains(str, "【该题针对")) {
            return str.trim();
        }
        return null;
    }

    /**
     * 是否进入答案部分
     *
     * @param str
     * @return
     */
    public static boolean isResolutionPart(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        return StringUtils.contains(str, "答案部分");
    }

    /**
     * 解析选择题选项
     *
     * @param str
     * @return
     */
    public static QuestionOptionParseResultDTO parseCheckOption(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        boolean matches = Pattern.matches("^[A-Za-z].[\\S|\\s]+", str);
        if (!matches) {
            return null;
        }
        int dotIndex = str.indexOf(".");
        if (dotIndex == -1) {
            return null;
        }
        String tag = str.substring(0, dotIndex);

        QuestionOptionParseResultDTO resultDTO = new QuestionOptionParseResultDTO();
        resultDTO.setOptionTag(tag.toUpperCase());
        resultDTO.setOptionName(str.trim());
        return resultDTO;
    }

    /**
     * 解析题目
     *
     * @param str
     * @return
     */
    public static QuestionTitleDTO parseQuestionTitle(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        boolean matches = Pattern.matches("^[0-9].[\\S|\\s]+", str);
        if (!matches) {
            return null;
        }
        int dotIndex = str.indexOf(".");
        String num = str.substring(0, dotIndex);
        if (!NumberUtils.isParsable(num)) {
            return null;
        }

        QuestionTitleDTO titleDTO = new QuestionTitleDTO();
        titleDTO.setNo(NumberUtils.createLong(num));
        String title = str.substring(dotIndex + 1);
        titleDTO.setTitle(title.trim());
        return titleDTO;
    }

    /**
     * 判断题型
     *
     * @param str
     * @return
     */
    public static QuestionTypeEnum parseQuestionType(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        if (StringUtils.contains(str, "单项选择题")) {
            return QuestionTypeEnum.SINGLE;
        }
        if (StringUtils.contains(str, "多项选择题")) {
            return QuestionTypeEnum.MULTIPLE;
        }
        if (StringUtils.contains(str, "判断题")) {
            return QuestionTypeEnum.TRUE_FALSE;
        }
        // 其他题型 不支持
        return null;
    }

    /**
     * 试题解析结果
     */
    @Data
    public static class QuestionParseResultDTO {

        private Map<Long, QuestionEntity> questionMap;

        private Map<Long, List<QuestionOptionEntity>> optionMap;

    }

    /**
     * 试题选项解析结果
     */
    @Data
    public static class QuestionOptionParseResultDTO {

        private String optionTag;

        private String optionName;

    }
}
