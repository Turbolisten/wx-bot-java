package com.turbolisten.bot.service.module.business.chat.qingyunke;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.turbolisten.bot.service.module.business.bot.WxBotExceptionHandle;
import com.turbolisten.bot.service.util.SmartRandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * 文本对答
 *
 * @author Turbolisten
 * @date 2019/12/07 16:40
 */
@Slf4j
@Service
public class QingYunKeChatService {

    @Autowired
    private RestTemplate restTemplate;

    private static final Map<String, List<String>> WORD_MAP;

    static {
        WORD_MAP = new HashMap<>();
        WORD_MAP.put("我靠你大爷", Lists.newArrayList("我对你的爱,就像拖拉机上山,突突突突~",
                "我和你除了恋爱没什么好谈的。",
                "你想我了吗？你都不想我，可我好想你。",
                "我还是很喜欢你，像风走了八千里，不问归期。",
                "春水初生，春林初盛，春风十里不如你。",
                "我们的关系就像双曲线和渐近线，我一直想再一起，但却一直不可及，但没关系，因为我和一直陪着你。",
                "看见你的那一刻，浩瀚众星皆降为尘",
                "孤烟无垠万里沙，幸能与你踏。"));

        WORD_MAP.put("菲菲", Lists.newArrayList("小羽"));
        WORD_MAP.put("\\{br}", Lists.newArrayList("\n"));
        WORD_MAP.put("梅州行", Lists.newArrayList("大洛阳"));
        WORD_MAP.put("梅州", Lists.newArrayList("大洛阳"));
        WORD_MAP.put("鑫总", Lists.newArrayList("冬哥"));
        WORD_MAP.put("张鑫", Lists.newArrayList("冬哥"));
        WORD_MAP.put("别笑了，露出一嘴的黄牙", Lists.newArrayList("别笑了，我都没法安心当机器人了", "别笑了，小心被我主人听到~"));
        WORD_MAP.put("丑八怪", Lists.newArrayList("小可爱"));
        WORD_MAP.put("笑得这么难看", Lists.newArrayList("笑得这么好看啊"));
        WORD_MAP.put("你笑得比哭还难看", Lists.newArrayList("你笑起来好看的一批", "我突然释怀的笑，笑声盘旋半山腰"));
        WORD_MAP.put("主人小宇住在温州市，龙湾区后陈，主人何玲燕住在，温州市，龙湾区山北玲燕网吧", Lists.newArrayList("主人故乡位于千年洛阳，秋名山分山之牵羊坡，五连发卡弯之上"));
    }

    /**
     * 随便调用了 网上免费的一个 api
     *
     * @param msg
     * @return
     */
    public String chat(String msg) {
        String content;
        try {
            content = restTemplate.getForObject("http://api.qingyunke.com/api.php?key=free&appid=0&msg={1}", String.class, msg);
            content = JSON.parseObject(content).getString("content");
        } catch (Exception e) {
            WxBotExceptionHandle.sendErrMsg(e, "query qingyun chat api error");
            content = "我大概是出bug了，让我好好想一想~";
        }
        content = replaceWord(content);
        // 替换表情
        content = replaceEmoji(content);
        return content;
    }

    private static String replaceEmoji(String msg) {
        int indexStart = msg.indexOf("{face:");
        if (indexStart == -1) {
            return msg;
        }
        int indexEnd = msg.indexOf("}");
        String emoji = msg.substring(indexStart, indexEnd + 1);
        msg = msg.replace(emoji, "[愉快]");
        // 递归替换
        msg = replaceEmoji(msg);
        return msg;
    }

    /**
     * 过滤 敏感词
     *
     * @param msg
     * @return
     */
    private static String replaceWord(String msg) {
        Set<String> keySet = WORD_MAP.keySet();
        for (String word : keySet) {
            msg = msg.replaceAll(word, Objects.requireNonNull(SmartRandomUtil.randomOne(WORD_MAP.get(word))));
        }
        return msg;
    }

    public static void main(String[] args) {
        String msg = "我靠你大爷";
        msg = replaceWord(msg);
        System.out.println(msg);
    }
}
