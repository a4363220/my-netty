package com.yixing.mynetty.bio;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 描述：
 * <p>
 * 由于线程池和消息队列都有有界的，因此无论客户端并发连接多大，它
 * 都不会导致线程个数过于膨胀或者内存溢出，相比于传统的一连接一线程模型是一种改良
 * <p>
 * 虽然避免了为每个请求创建一个线程，但底层通信依然采用同步阻塞队列
 *
 * @author 小谷
 * @Date 2020/5/11 15:19
 */
public class TimeServerHandlerExecutePool {

    private ExecutorService executorService;

    public TimeServerHandlerExecutePool(int maxPoolSize, int queueSize) {
        executorService = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
                maxPoolSize, 120L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(queueSize));
    }

    public void execute(Runnable task) {
        executorService.execute(task);
    }
}
