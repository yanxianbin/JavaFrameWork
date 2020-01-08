import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.vavr.Function3;
import io.vavr.control.Try;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.scheduling.concurrent.ThreadPoolExecutorFactoryBean;
import org.springframework.util.Assert;

import javax.script.ScriptEngineManager;
import java.time.Duration;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.IntStream;

/**
 * @Classname RateLimiterTest
 * @Description TODO
 * @Date 2020/1/5 10:55
 * @Created by 125937
 */
@Slf4j
public class RateLimiterTest {

    private RateLimiterConfig config;
    private AtomicInteger count = new AtomicInteger(0);
    private CountDownLatch latch = new CountDownLatch(100);

    @Test
    public void test() {
        config = RateLimiterConfig.custom()
                .limitRefreshPeriod(Duration.ofMillis(1000))
                .limitForPeriod(1)
                .timeoutDuration(Duration.ofMillis(1000))
                .build();
        // 创建限流器
        RateLimiter rateLimiter = RateLimiter.of("emailSendRateLimiter", config);

        //重试机制
        RetryConfig retryConfig=RetryConfig.custom().intervalFunction(IntervalFunction.of(Duration.ofMillis(300))).maxAttempts(10).build();

        Retry retry=Retry.of("retryName",retryConfig);
        RateLimiter.Metrics metrics = rateLimiter.getMetrics();
        rateLimiter.getEventPublisher().onSuccess(event -> {
            System.out.println(event.getEventType() + ":::可用令牌数: " + metrics.getAvailablePermissions() + ", 等待线程数: "
                    + metrics.getNumberOfWaitingThreads());
        }).onFailure(event -> {
            System.out.println(event.getEventType() + ":::可用令牌数: " + metrics.getAvailablePermissions() + ", 等待线程数: "
                    + metrics.getNumberOfWaitingThreads());
        });
//        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(1, 100, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(50));
//        ThreadPoolExecutorFactoryBean threadFactory = new ThreadPoolExecutorFactoryBean();
//        threadFactory.setThreadNamePrefix("emailSendThreadPool-");
//        poolExecutor.setThreadFactory(threadFactory);
        long start = System.currentTimeMillis();


        IntStream.rangeClosed(1, 10)
                .parallel()
                .forEach(i -> {
                    Supplier task = RateLimiter.decorateSupplier(rateLimiter, ()-> new SendThread(i));
                    task=Retry.decorateSupplier(retry,task);
                    //Runnable task = Bulkhead.decorateRunnable(bulkhead, new SendThread(i));
                    Try.ofSupplier(task).get();
                    //poolExecutor.execute(task);
                    //System.out.println(poolExecutor.getActiveCount()+"::"+poolExecutor.getTaskCount());
                });
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("耗时: {} ms, count={}", (System.currentTimeMillis() - start), count.get());
    }

    class SendThread implements Runnable {
        private int id;

        public SendThread(int id) {
            this.id = id;
        }

        @SneakyThrows
        @Override
        public void run() {
            //Thread.sleep(200);
            log.info("发送邮件, id={}", id);
            count.incrementAndGet();
            latch.countDown();
        }

    }
}
