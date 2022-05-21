package com.simm.web.controller;

import com.alibaba.fastjson.JSON;
import com.simm.common.model.BizException;
import com.simm.common.model.QwNotice;
import com.simm.common.utils.OkHttpUtil;
import com.simm.web.service.IStarShipService;
import com.simm.web.service.impl.GitlabServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
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
    @Value("${notice.qw.publish.addr:}")
    private String qwAddr;
    @Value("#{'${notice.qw.publish.users:wup06,wangc21,liulj}'.split(',')}")
    private List<String> noticeUsers;

    @Resource
    private OkHttpUtil okHttpUtil;
    @Resource
    private GitlabServiceImpl gitlabService;
    @Resource
    private IStarShipService starShipService;

    private final String MAIN_BRANCH = "master";

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
//        if ("master".equals(source.toLowerCase())) {
//            return ok;
//        }
        if (!MAIN_BRANCH.equalsIgnoreCase(target)) {
            return ok;
        }
        // 发消息给测试
        QwNotice.QwContent content = QwNotice.QwContent.builder().content(
                String.format("【%s】 %s 正在合并代码，%s -> %s", gitlabService.getVersion(), project, source, target)
        ).mentionedList(noticeUsers).build();
        QwNotice notice = QwNotice.builder().msgType("text")
                .text(content).build();
        log.info(JSON.toJSONString(notice));
        okHttpUtil.post(qwAddr, notice, null);
        return ok;
    }

    /**
     * 获取当前版本
     *
     * @return
     */
    @GetMapping("/bpm/version")
    public String getVersion() {
        return gitlabService.getVersion();
    }

    /**
     * 获取当前版本
     *
     * @return
     */
    @GetMapping("/starship/version")
    public String getStarShipVersion() {
        return starShipService.getVersion();
    }

    /**
     * 获取当前版本
     *
     * @return
     */
    @PutMapping("/bpm/version")
    public String updateVersion(@RequestHeader("PRIVATE-TOKEN") String token) {
        if (!gitlabService.getToken().equals(token)) {
            throw new BizException("非法操作，请向管理员申请授权");
        }
        // 校验token
        return gitlabService.updateVersion();
    }

}
