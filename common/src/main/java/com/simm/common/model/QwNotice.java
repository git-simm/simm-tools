package com.simm.common.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 企微通知
 * @author miscr
 */
@Data
@Builder
public class QwNotice implements Serializable {
    @JSONField(name="msgtype")
    private String msgType;
    private QwContent markdown;
    private QwContent text;

    @Data
    @Builder
    public static class QwContent implements Serializable{
        private String content;
        @JSONField(name = "mentioned_list")
        private List<String> mentionedList;
    }
}

