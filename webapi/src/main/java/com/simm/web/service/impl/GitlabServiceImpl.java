package com.simm.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.simm.bpm.entity.CommitInfo;
import com.simm.bpm.entity.GitFile;
import com.simm.common.utils.OkHttpUtil;
import com.simm.web.service.IGitlabService;
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

    private final String PARAM_FILE = "config%2Fparams.php";
    @Resource
    private OkHttpUtil okHttpUtil;

    @Override
    public String getVersion() {
        String fileC = getFileContent(this.PARAM_FILE);
        String[] arr = fileC.split("\\r?\\n");
        for (String line : arr) {
            if (line.trim().startsWith("'version'")) {
                return line.split("=>")[1].trim().replace("'", "").replace(",", "");
            }
        }
        // 获取版本号
        return null;
    }

    @Override
    public String getFileContent(String filePath) {
        Map<String, String> headers = new HashMap<>();
        headers.put(tokenKey, token);
        String body = okHttpUtil.get(String.format(fileUrl+"?ref=Pre",filePath), null, headers);
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
    public String commitFile(String filePath,CommitInfo commitInfo) {
        Map<String, String> headers = new HashMap<>();
        headers.put(tokenKey, token);
        return okHttpUtil.put(String.format(fileUrl,filePath), commitInfo, headers);
    }

    @Override
    public String updateVersion() {
        CommitInfo commitInfo = new CommitInfo();
        commitInfo.setBranch("f-20220518-react-api");
        commitInfo.setCommitMessage("调整版本号为4.2.1");
        commitInfo.setContent("PD9waHAKJGxvY2FsUGFyYW0gPSBbXTsKaWYgKGZpbGVfZXhpc3RzKF9fRElSX18gLiBESVJFQ1RPUllfU0VQQVJBVE9SIC4gJ3BhcmFtcy1sb2NhbC5waHAnKSkgewogICAgJGxvY2FsUGFyYW0gPSByZXF1aXJlX29uY2UoX19ESVJfXyAuIERJUkVDVE9SWV9TRVBBUkFUT1IgLiAncGFyYW1zLWxvY2FsLnBocCcpOwp9CgokcGFyYW1zQ29uZiA9IFsKICAgICd2ZXJzaW9uJyA9PiAndjQuMi4xJywKICAgICdhZG1pbkVtYWlsJyA9PiAnYWRtaW5AZXhhbXBsZS5jb20nLAogICAgJ2VudicgPT4gJ2RldicsCiAgICAnZnJvbnRlbmREZWZhdWx0VXJsJyA9PiAnL2Zsb3djZW50ZXIvZmxvdy9uZXcnLAogICAgJ3JldHVyblVybFBhcmFtRm9yTG9naW4nID0+ICdyZWRpcmVjdHVybCcsIC8vIOeZu+W9leaXtueahHJldHVyblVybOWPguaVsOWQje+8jOa1geeoi+S4reW/g+m7mOiupOS4jemAmui/h1VSTOS8oOmAkuWbnui3s+WcsOWdgO+8jOWmguaenOaYr+esrOS4ieaWueezu+e7n+eZu+W9le+8jOWPr+iDvemcgOimgemAmui/h1VSTOWPguaVsOS8oOmAku+8jOatpOaXtumcgOimgemFjee9ruS4gOS4i+WPguaVsOWQjQogICAgJ2Zhc3RfYXBpJyA9PiAnaHR0cHM6Ly9taWMtb3Blbi5teXBhYXMuY29tLmNuL3dlYi1sb2ctdHJhY2tlci9NeUJQTS9NeUJQTV9QUk8vbXlXZWJMb2dUcmFja2VyLm1pbi5qcycsCiAgICAnZmFzdF9vcGVuX2hvc3QnID0+ICdodHRwczovL2Zhc3QubXlwYWFzLmNvbS8nLAogICAgJ2Zhc3Rfb3Blbl9hY2Nlc3Nfa2V5JyA9PiAnTFRBSTRHNkp4dW42eEJ0S3RIVTRFNGE5JywKICAgICdmYXN0X29wZW5fYWNjZXNzX2tleV9zZWNyZXQnID0+ICdxR3p4a3UyS2JsSmlOdG9VS1BXOWFRT29MRUg5UUwnLAogICAgJ2Zhc3RfbG9nX2VuYWJsZScgPT4gJzAnLAogICAgJ2Zhc3Rfb3Blbl9lbnZfY29kZSc9PicnLAogICAgJ2Zhc3Rfb3Blbl9kYl9jb25maWdzJyA9PiAndXNlcl9vcHI9NDY2NTY3NDI5NDE0MTk1MjAwO2Vycm1zZz00ODk0NzQ2MTk5OTcxNjM1MjA7YXBpPTQ4OTQ3NDE5ODMyMjgxMDg4MDttc2dfdHJhY2U9NDY2NTY3MTI5NTcxNzkwODQ4O21zZ190cmFjZV9hcGk9NDY2NTY3MjIxODU5MDYxNzYwO3RyaWdnZXJfZXZlbnQ9NDY2NTY3MzgyODQwNjQzNTg0O2V4cGFuZF9idXR0b249NDY2NTY2ODU0MDAxODIzNzQ0JwpdOwoKcmV0dXJuIGFycmF5X21lcmdlKCRwYXJhbXNDb25mLCAkbG9jYWxQYXJhbSk7");
        return commitFile(this.PARAM_FILE,commitInfo);
    }
}
