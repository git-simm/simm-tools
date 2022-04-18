package com.simm.web.jobs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

/**
 * 通知JOB
 * @author simm
 */
@Configuration
@EnableScheduling
@Slf4j
public class SpringNoticeJob {
    //3.添加定时任务
    //或直接指定时间间隔，例如：5秒
    // @Scheduled(fixedRate=5000)
    /**
     * 测试定时任务
     */
//    @Scheduled(cron = "0/5 * * * * ?")
    private void configureTasks() {
        log.info("执行静态定时任务时间: " + LocalDateTime.now());
    }
}
