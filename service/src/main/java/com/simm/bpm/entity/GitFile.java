package com.simm.bpm.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * git源文件
 *
 * @author miscr
 */
@Data
public class GitFile implements Serializable {
    @JSONField(name = "file_name")
    private String fileName;
    @JSONField(name = "file_path")
    private String filePath;
    @JSONField(name = "ref")
    private String ref;
    /**
     * 内容为base64编码
     */
    @JSONField(name = "content")
    private String content;
}
