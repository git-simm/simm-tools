package com.simm.web.controller;

import com.alibaba.fastjson.JSON;
import com.simm.bpm.entity.GitFile;
import com.simm.common.model.QwNotice;
import com.simm.common.utils.OkHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
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
    @Value("${sys.bpm.app.version-url:https://git.mingyuanyun.com/api/v4/projects/808/repository/files/config%2Fparams.php?ref=Pre}")
    private String versionUrl;
    @Value("${sys.bpm.app.token-key:PRIVATE-TOKEN}")
    private String tokenKey;
    @Value("${sys.bpm.app.token:wvzYgebknZt6gy91tYop}")
    private String token;
    @Autowired
    private OkHttpUtil okHttpUtil;
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
                String.format("【%s】 %s 正在合并代码，%s -> %s", getVersion(), project, source, target)
        ).mentionedList(noticeUsers).build();
        QwNotice notice = QwNotice.builder().msgType("text")
                .text(content).build();
        log.info(JSON.toJSONString(notice));
        okHttpUtil.post(qwAddr, notice);
        return ok;
    }

    /**
     * 获取当前版本
     *
     * @return
     */
    @GetMapping("/bpm/version")
    public String getVersion() {
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put(tokenKey, token);
            String body = okHttpUtil.get(versionUrl, null, headers);
            GitFile file = JSON.parseObject(body, GitFile.class);
            if (file != null && !StringUtils.isEmpty(file.getContent())) {
                String fileC = new String(Base64Utils.decodeFromString(file.getContent()), "utf-8");
                String[] arr = fileC.split("\\r?\\n");
                for (String line : arr) {
                    if (line.trim().startsWith("'version'")) {
                        return line.split("=>")[1].trim().replace("'", "").replace(",", "");
                    }
                }
                // 获取版本号
                return fileC;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}
