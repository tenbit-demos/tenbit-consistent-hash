package cache;

import cn.tenbit.hare.core.lite.util.HareAssertUtils;
import cn.tenbit.hare.core.lite.util.HareObjectUtils;
import cn.tenbit.hare.core.lite.util.HareSleepUtils;
import cn.tenbit.hare.core.lite.util.HareTimeUtils;
import config.Config;
import support.ExecuteThreadPool;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author bangquan.qian
 * @Date 2019-07-23 16:36
 */
public class TtlCacheNode<T, ID extends Serializable> implements Cache<T, ID>, CacheTtlable {

    private final Map<ID, Long> map = new ConcurrentHashMap<>();

    private final Cache<T, ID> node;

    public TtlCacheNode(Cache<T, ID> node) {
        HareAssertUtils.notNull(node);
        this.node = node;
        ExecuteThreadPool.getPool().execute(new TtlCacheMonitor<>(this));
    }

    @Override
    public T get(ID id) {
        return node.get(id);
    }

    @Override
    public T put(ID id, T t) {
        T p = node.put(id, t);
        if (get(id) != null) {
            map.put(id, HareTimeUtils.currentTimeMs());
        }
        return p;
    }

    @Override
    public T remove(ID id) {
        T p = node.remove(id);
        if (p != null) {
            map.remove(id);
        }
        return p;
    }

    @Override
    public int size() {
        return node.size();
    }

    private static class TtlCacheMonitor<ID extends Serializable> implements Runnable {

        private final TtlCacheNode<?, ID> node;

        public TtlCacheMonitor(TtlCacheNode<?, ID> node) {
            this.node = node;
        }

        @Override
        public void run() {
            while (true) {
                long now = HareTimeUtils.currentTimeMs();
                Set<ID> set = new LinkedHashSet<>();
                for (Map.Entry<ID, Long> entry : node.map.entrySet()) {
                    long value = HareObjectUtils.safeUnboxing(entry.getValue(), now);
                    if (now - value < Config.REFRESH_CACHE_MONITOR_S_SPEED * 1000) {
                        continue;
                    }
                    set.add(entry.getKey());
                }
                for (ID id : set) {
                    node.remove(id);
                }
                HareSleepUtils.sleepSeconds(Config.REFRESH_CACHE_MONITOR_S_SPEED);
            }
        }
    }
}
