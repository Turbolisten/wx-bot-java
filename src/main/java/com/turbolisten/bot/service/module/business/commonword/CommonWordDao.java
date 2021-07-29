package com.turbolisten.bot.service.module.business.commonword;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.turbolisten.bot.service.module.business.commonword.domain.CommonWordDTO;
import com.turbolisten.bot.service.module.business.commonword.domain.CommonWordEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 常用文案dao
 *
 * @author Turbolisten
 * @date 2021/7/11 13:16
 */
@Component
public interface CommonWordDao extends BaseMapper<CommonWordEntity> {
    /**
     * 根据 场景 查询
     *
     * @param scene
     * @param deletedFlag 可为null
     * @return
     */
    List<CommonWordDTO> queryByScene(@Param("scene") Integer scene,
                                     @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 根据内容查询是否存在
     *
     * @param content
     * @return
     */
    Integer existByContent(String content);
}
