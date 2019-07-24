package cache;

import cn.tenbit.hare.core.lite.util.HareAssertUtils;
import cn.tenbit.hare.core.lite.util.HareSleepUtils;
import config.Config;
import lombok.Getter;
import support.ExecuteThreadPool;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author bangquan.qian
 * @Date 2019-07-23 16:36
 */
public class QpsCacheNode<T, ID extends Serializable> implements Cache<T, ID>, CacheQpsable<QpsCacheNode.Qps> {

    private final Cache<T, ID> node;
    private final Qps qps = new Qps();

    public QpsCacheNode(Cache<T, ID> node) {
        HareAssertUtils.notNull(node);
        this.node = node;
        ExecuteThreadPool.getPool().execute(new QpsCacheMonitor<>(this));
    }

    @Override
    public T get(ID id) {
        takeQps();
        return node.get(id);
    }

    @Override
    public T put(ID id, T t) {
        takeQps();
        return node.put(id, t);
    }

    @Override
    public T remove(ID id) {
        takeQps();
        return node.remove(id);
    }

    @Override
    public int size() {
        return node.size();
    }

    private void takeQps() {
        qps.times.incrementAndGet();
    }

    @Override
    public Qps getQps() {
        return qps;
    }

    @Getter
    public static class Qps {

        private final AtomicLong times = new AtomicLong();

        private final AtomicLong qps = new AtomicLong();
    }

    private static class QpsCacheMonitor<ID extends Serializable> implements Runnable {

        private final QpsCacheNode<?, ID> node;

        private long lastTimes;

        public QpsCacheMonitor(QpsCacheNode<?, ID> node) {
            this.node = node;
        }

        @Override
        public void run() {
            while (true) {
                lastTimes = node.qps.times.get();
                HareSleepUtils.sleepSeconds(Config.QPS_MONITOR_S_SPEED);
                long currentTimes = node.qps.times.get();
                node.qps.qps.getAndSet(currentTimes - lastTimes);
            }
        }
    }
}
