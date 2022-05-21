package com.simm.bpm.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * git提交
 *
 * @author miscr
 */
@Data
public class CommitInfo implements Serializable {
    /**
     * 代码分支
     */
    @JSONField(name = "branch")
    private String branch;
    /**
     * 提交说明
     */
    @JSONField(name = "commit_message")
    private String commitMessage;
    /**
     * 文件编码
     */
    @JSONField(name = "encoding")
    private String encoding = "base64";
    /**
     * 文件内容
     */
    @JSONField(name = "content")
    private String content;
}
