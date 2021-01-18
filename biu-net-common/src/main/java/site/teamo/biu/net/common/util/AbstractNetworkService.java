package site.teamo.biu.net.common.util;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * @author 爱做梦的锤子
 * @create 2021/1/16
 */
@Slf4j
public abstract class AbstractNetworkService implements NetworkService {

    protected volatile Status status;
    protected FutureTask<Boolean> future;
    protected Thread thread;

    @Override
    public synchronized void start(boolean permanent) {
        /**
         * 如果当前Server已经启动，则不进行任何操作
         */
        if (Status.STARTED.equals(status)) {
            return;
        }
        /**
         * 先修改Server状态为启动，然后启动Server
         */
        status = Status.STARTED;
        Callable<Boolean> startTask = start0(permanent);
        this.future = new FutureTask<>(startTask);
        this.thread = new Thread(future);
        thread.start();
    }

    public abstract Callable<Boolean> start0(boolean permanent);

    @Override
    public abstract void shutdown();

    public synchronized Boolean shutdown0() throws Exception {
        status = Status.STOPPED;
        if (thread.isAlive() || !thread.isInterrupted()) {
            thread.interrupt();
        }
        if (!future.isDone() && !future.isCancelled()) {
            future.cancel(true);
        }
        return future.get();
    }

    @Override
    public Status status() {
        return status;
    }
}
