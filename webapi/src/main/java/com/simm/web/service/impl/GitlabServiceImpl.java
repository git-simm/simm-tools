package com.simm.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.simm.bpm.entity.CommitInfo;
import com.simm.bpm.entity.GitFile;
import com.simm.common.model.BizException;
import com.simm.common.utils.OkHttpUtil;
import com.simm.web.service.IGitlabService;
import com.simm.web.service.IStarShipService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * gitlab服务
 *
 * @author miscr
 */
@Service
@Slf4j
@Data
public class GitlabServiceImpl implements IGitlabService {
    @Value("${sys.bpm.app.file-url:https://git.mingyuanyun.com/api/v4/projects/808/repository/files/%s}")
    private String fileUrl;
    @Value("${sys.bpm.app.token-key:PRIVATE-TOKEN}")
    private String tokenKey;
    @Value("${sys.bpm.app.token:wvzYgebknZt6gy91tYop}")
    private String token;
    @Value("${sys.bpm.app.branch:Pre}")
    private String branch;

    private final String PARAM_FILE = "config%2Fparams.php";
    private final String PARAM_TPL_FILE = "config%2Fparams.php.tpl";

    @Resource
    private OkHttpUtil okHttpUtil;
    @Resource
    private IStarShipService starShipService;

    @Override
    public String getVersion() {
        String fileC = getFileContent(this.PARAM_FILE);
        String[] arr = fileC.split("\\r?\\n");
        for (String line : arr) {
            if (line.trim().startsWith("'version'")) {
                return getVer(line);
            }
        }
        // 获取版本号
        return null;
    }

    @Override
    public String getFileContent(String filePath) {
        Map<String, String> headers = new HashMap<>();
        headers.put(tokenKey, token);
        String body = okHttpUtil.get(String.format(fileUrl + "?ref=%s", filePath, branch), null, headers);
        GitFile file = JSON.parseObject(body, GitFile.class);
        if (file != null && !StringUtils.isEmpty(file.getContent())) {
            try {
                return new String(Base64Utils.decodeFromString(file.getContent()), "utf-8");
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return null;
            }
        }
        return null;
    }

    @Override
    public String commitFile(String filePath, CommitInfo commitInfo) {
        Map<String, String> headers = new HashMap<>();
        headers.put(tokenKey, token);
        return okHttpUtil.put(String.format(fileUrl, filePath), commitInfo, headers);
    }

    @Override
    public String updateVersion() {
        String version = starShipService.getVersion();
        if (StringUtils.isEmpty(version)) {
            throw new BizException("未获取到版本号");
        }
        CommitInfo commitInfo = new CommitInfo();
        commitInfo.setBranch(branch);
        commitInfo.setCommitMessage("调整版本号为" + version);
        // PARAM_FILE
        String content = Base64Utils.encodeToString(updateVerNum(this.PARAM_FILE, version).getBytes());
        commitInfo.setContent(content);
        commitFile(this.PARAM_FILE, commitInfo);
        // PARAM_TPL_FILE
        content = Base64Utils.encodeToString(updateVerNum(this.PARAM_TPL_FILE, version).getBytes());
        commitInfo.setContent(content);
        commitFile(this.PARAM_TPL_FILE, commitInfo);
        return "success";
    }

    private String updateVerNum(String filePath, String version) {
        String content = this.getFileContent(filePath);
        String[] arr = content.split("\\r?\\n");
        for (int i = 0; i < arr.length; i++) {
            String line = arr[i];
            if (line.trim().startsWith("'version'")) {
                arr[i] = line.replace(getVer(line), 'v' + version);
                return String.join("\r\n", arr);
            }
        }
        return content;
    }

    private String getVer(String line) {
        return line.split("=>")[1].trim().replace("'", "").replace(",", "");
    }
}
