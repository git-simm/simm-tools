package com.simm.common.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.task.TaskDecorator;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * 异步配置
 *
 * @author simm
 */
@Configuration
@Order(-9999)
public class AsyncConfig {
    /**
     * 获取线程装饰
     *
     * @return
     */
    @Bean
    TaskDecorator getTaskDecorator() {
        return new MyContextDecorator();
    }

    public static class MyContextDecorator implements TaskDecorator {
        @Override
        public Runnable decorate(Runnable runnable) {
            // 获取主线程中的请求信息（我们的用户信息也放在里面）
            RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
//            Map<String, Object> authVal = BizContext.getAll();
//            Map<String, Object> threadContext = getAll();
            return () -> {
                try {
                    // 将主线程的请求信息，设置到子线程中
                    RequestContextHolder.setRequestAttributes(attributes);
                    // 执行子线程，这一步不要忘了
                    runnable.run();
                } finally {
                    // 线程结束，清空这些信息，否则可能造成内存泄漏
                    RequestContextHolder.resetRequestAttributes();
                }
            };
        }
    }
}
