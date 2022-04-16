package com.simm.web.controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * git通知控制器
 *
 * @author miscr
 */
@RestController("/git")
@Slf4j
public class GitNoticeController {
    @GetMapping()
    public Mono<String> success() {
        return Mono.fromSupplier(() -> "hello,this is simm's tools");
    }

    /**
     * 消息通知
     * @param merge 合并消息
     * @return 返回处理结果
     */
    @PostMapping("/notice")
    public Mono<String> notice(Map merge) {
        log.info(JSON.toJSONString(merge));
        return Mono.fromSupplier(() -> "ok");
    }
}
