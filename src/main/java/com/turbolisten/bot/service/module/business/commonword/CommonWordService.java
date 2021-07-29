package com.turbolisten.bot.service.module.business.commonword;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.turbolisten.bot.service.common.constant.CommonConst;
import com.turbolisten.bot.service.module.business.commonword.constant.CommonWordSceneTypeEnum;
import com.turbolisten.bot.service.module.business.commonword.domain.CommonWordAddDTO;
import com.turbolisten.bot.service.module.business.commonword.domain.CommonWordDTO;
import com.turbolisten.bot.service.module.business.commonword.domain.CommonWordEntity;
import com.turbolisten.bot.service.util.SmartBeanUtil;
import com.turbolisten.bot.service.util.SmartRandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 常用文案 业务
 *
 * @author Turbolisten
 * @date 2021/7/11 13:16
 */
@Slf4j
@Service
public class CommonWordService {

    @Autowired
    private CommonWordDao commonWordDao;

    /**
     * 文案缓存
     */
    private Cache<Integer, List<CommonWordDTO>> cache = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.HOURS)
            .build();

    /**
     * 添加文案
     *
     * @param addDTO
     */
    public boolean add(CommonWordAddDTO addDTO) {
        Integer sceneType = addDTO.getSceneType();
        sceneType = null == sceneType ? CommonWordSceneTypeEnum.COMMON.getValue() : sceneType;

        // 查询数据库是否已存在
        Integer content = commonWordDao.existByContent(addDTO.getContent());
        if (null != content) {
            return false;
        }

        CommonWordEntity wordEntity = SmartBeanUtil.copy(addDTO, CommonWordEntity.class);
        commonWordDao.insert(wordEntity);

        // 刷新缓存
        cache.invalidate(sceneType);
        return true;
    }

    public List<CommonWordDTO> query(Integer sceneType) {
        List<CommonWordDTO> list = CommonConst.EMPTY_LIST;
        try {
            list = cache.get(sceneType, () -> commonWordDao.queryByScene(sceneType, false));
        } catch (ExecutionException e) {
            log.error("query word error: ", e);
        }
        return list;
    }


    /**
     * 获取一条随机场景文案
     *
     * @param sceneType
     * @return
     */
    public CommonWordDTO randomOne(Integer sceneType) {
        return SmartRandomUtil.randomOne(query(sceneType));
    }

    /**
     * 获取一条随机场景文案
     *
     * @param sceneType
     * @return
     */
    public CommonWordDTO randomOne(CommonWordSceneTypeEnum sceneType) {
        return randomOne(sceneType.getValue());
    }

    public List<CommonWordDTO> query(CommonWordSceneTypeEnum sceneType) {
        return query(sceneType.getValue());
    }
}
