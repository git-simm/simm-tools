package com.simm.bpm.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * git源文件
 *
 * @author miscr
 */
@Data
public class StarShipVersion implements Serializable {
    @JSONField(name = "code")
    private Integer code;
    @JSONField(name = "msg")
    private String msg;
    @JSONField(name = "data")
    private List<VersionInfo> data;

    @Data
    public static class VersionInfo implements Serializable{
        @JSONField(name = "id")
        private String id;
        @JSONField(name = "version_name")
        private String versionName;
    }
}
