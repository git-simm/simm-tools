package com.simm.web.controller;

import com.simm.web.service.IWarningService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.lang.management.ManagementFactory;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * 门户测试
 *
 * @author miscr
 * docs: https://blog.csdn.net/yeweilei/article/details/80109584
 * poll: https://www.cnblogs.com/twodog/p/12137487.html
 * https://zhuanlan.zhihu.com/p/92460075
 */
@RestController
@Slf4j
public class HomeController {
    @Resource
    IWarningService warningService;
    private String country;
    private String country2;
    @Value("${random.int底3,4顶}")
    private String random;
    @Value("${spring.profiles.active:dev}")
    private String active;
//    @Value("${user.country2}")
//    private void setCountry2(String country){
//        this.country2 = country;
//    }
//    @Value("${user.country}")
//    private void setCountry(String country){
//        this.country = country;
//    }

    @GetMapping({"", "/"})
    public Mono<String> say() {
        log.info("random："+random);
        log.info("spring.profiles.active："+active);
//        System.out.println("user.country："+ country);
        log.info("------ 系统变量 --------");
        System.getProperties().forEach((pKey,pVal)->{
            log.info(String.format("%s：%s",pKey,pVal));
        });
        log.info("------ 环境变量 --------");
        System.getenv().forEach((pKey,pVal)->{
            log.info(String.format("%s：%s",pKey,pVal));
        });
        log.info("------ JVM变量 --------");
        List<String> inputArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
        log.info(String.valueOf(inputArguments));
        //非阻塞式
        return Mono.fromSupplier(() -> "hello,flux");
    }

    @GetMapping({"", "/hello"})
    public Mono<String> hello() {
        log.info("开始接待");
        // Mono<String> result = Mono.just(getInfo()); 阻塞式
        //非阻塞式
        Mono<String> result = Mono.fromSupplier(() -> getInfo());

        warningService.run("flux发出报警信息");

        log.info("接待完毕");
        return result;
    }

    @GetMapping(value = "poll",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> poll() {
        log.info("开始接待");
        return Flux.interval(Duration.ofSeconds(3))
                .doOnNext(e -> log.info(e.toString()))
                .doOnError(e -> e.printStackTrace())
                .map(data -> "hello flux,--"+data.toString());
    }

    /**
     * Flux : 返回0-n个元素
     * 注：需要指定MediaType
     * @return
     */
    @GetMapping(value = "/3", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    private Flux<String> flux() {
        Flux<String> result = Flux.fromStream(IntStream.range(1, 5).mapToObj(i -> {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                    }
                    return "flux data--" + i;
                }));
        return result;
    }

    private String getInfo() {
        log.info("开始服务");
        try {
            TimeUnit.SECONDS.sleep(5);
            return "Hello webflux.";
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            log.info("服务完毕");
        }
        return "Hello webflux.";
    }
}
