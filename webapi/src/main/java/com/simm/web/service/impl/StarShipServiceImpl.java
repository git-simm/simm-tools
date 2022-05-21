package com.simm.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.simm.bpm.entity.StarShipVersion;
import com.simm.common.utils.OkHttpUtil;
import com.simm.web.service.IStarShipService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * gitlab服务
 * @author miscr
 */
@Service
@Slf4j
public class StarShipServiceImpl implements IStarShipService {
    @Value("${sys.starship.app.version-url:https://starship.mypaas.com.cn/plapi/task/associate_version/product_list?task_id=3a028a00-ed48-2de4-f000-c8ba458e99ca&pipeline_id=3a00e915-03ca-e11c-9f44-fe2ac7a9a693}")
    private String versionUrl;

    @Autowired
    private OkHttpUtil okHttpUtil;

    /**
     * 获取星舟上绑定的版本号
     * @return
     */
    @Override
    public String getVersion() {
        Map<String, String> headers = new HashMap<>();
        headers.put("group-id", "3a00ad2c-1a07-de97-c4e2-8e93b2db043a");
        headers.put("cookie", "__tracker_user_id__=249c5527a93cb00-aaf000dd2d-2b2c4c20; token=f4c4fc35f0bdeba450dca6f8664123c1d9c463c6; account=simm; grafana_auth_account=c8b480b6d97521234460769777ecfbda; __fast_sid__=24b4dc9aca82120-0f3200f981-4e6b206f");
        String body = okHttpUtil.get(versionUrl, null, headers);
        StarShipVersion file = JSON.parseObject(body, StarShipVersion.class);
        if(CollectionUtils.isEmpty(file.getData())){
            return "";
        }
        return file.getData().get(0).getVersionName();
    }
}
