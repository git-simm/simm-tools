package com.simm.web.controller;

import com.alibaba.fastjson.JSON;
import com.simm.common.model.QwNotice;
import com.simm.common.utils.OkHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Map;

/**
 * git通知控制器
 *
 * @author simm
 */
@RestController
@RequestMapping(value = "/git")
@Slf4j
public class GitNoticeController {
    @Value("${git-notice-addr:https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=643d5e0c-5579-4857-84b1-13c816a05a43}")
    private String qwAddr;
    @Value("#{'${git-notice-users:wup06,wangc21,liulj}'.split(',')}")
    private List<String> noticeUsers;
    @Autowired
    private OkHttpUtil okHttpUtil;

    @GetMapping()
    public Mono<String> success() {
        return Mono.fromSupplier(() -> "hello,this is simm's tools");
    }

    /**
     * 消息通知
     *
     * @param merge 合并消息
     * @return 返回处理结果
     */
    @PostMapping("/notice")
    public Mono<String> notice(@RequestBody Map merge) {
        Mono<String> ok = Mono.fromSupplier(() -> "ok");
        String project = ((Map) merge.get("project")).get("name").toString();
        Map objAttrs = (Map) merge.get("object_attributes");
        String source = objAttrs.get("source_branch").toString();
        String target = objAttrs.get("target_branch").toString();
        if ("master".equals(source.toLowerCase())) {
            return ok;
        }
        // 发消息给测试
        QwNotice.QwContent content = QwNotice.QwContent.builder().content(
                String.format("%s 正在合并代码，%s -> %s",project,source,target)
        ).mentionedList(noticeUsers).build();
        QwNotice notice = QwNotice.builder().msgType("text")
                .text(content).build();
        log.info(JSON.toJSONString(notice));
        okHttpUtil.post(qwAddr,notice);
        return ok;
    }
}
