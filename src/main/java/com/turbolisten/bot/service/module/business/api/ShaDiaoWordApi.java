package com.turbolisten.bot.service.module.business.api;

import com.turbolisten.bot.service.common.constant.BaseEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 一些第三方文案内容 api
 *
 * @author Turbolisten
 * @date 2021/7/11 13:13
 */
@Slf4j
@Service
public class ShaDiaoWordApi {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 查询 沙雕文案
     *
     * @return
     */
    public String queryShaDiao(ShaDiaoWordType type) {
        ResponseEntity<String> res = restTemplate.getForEntity(type.getValue(), String.class);
        if (res.getStatusCode() != HttpStatus.OK) {
            log.error("query sha diao word error:{}", res.getStatusCode());
            return null;
        }
        String body = res.getBody();
        if (StringUtils.contains(body, "访问太频繁服务器受不了啦")) {
            log.error("query sha diao word error:{}", body);
            return null;
        }
        return body;
    }

    public enum ShaDiaoWordType implements BaseEnum {

        CHP("https://chp.shadiao.app/api.php", "彩虹屁"),

        DU("https://du.shadiao.app/api.php", "毒鸡汤"),

        PYQ("https://pyq.shadiao.app/api.php", "朋友圈文案"),

        NMSL("https://nmsl.shadiao.app/api.php?level=min&lang=zh_cn", "祖安骂人"),

        ;
        private String url;

        private String desc;

        ShaDiaoWordType(String url, String desc) {
            this.url = url;
            this.desc = desc;
        }

        @Override
        public String getValue() {
            return url;
        }

        @Override
        public String getDesc() {
            return desc;
        }
    }

}
