package com.simm.web.service.impl;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import com.simm.web.events.WarningEvent;
import com.simm.web.service.IWarningService;

import javax.annotation.Resource;

/**
 * 报警服务实现类
 * @author miscr
 */
@Service
public class WarningServiceImpl implements IWarningService {
    @Resource
    ApplicationContext applicationContext;

//    @Async
    @Override
    public void run(String message) {
        applicationContext.publishEvent(new WarningEvent(this,message));
    }
}
