package cache;

import cn.tenbit.hare.core.lite.util.HareAssertUtils;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author bangquan.qian
 * @Date 2019-07-23 14:19
 */
public class CacheNode<T, ID extends Serializable> implements Cache<T, ID> {

    private final Map<ID, T> map = new ConcurrentHashMap<>();

    private final AtomicInteger size = new AtomicInteger();

    @Override
    public T get(ID id) {
        HareAssertUtils.notNull(id);
        return map.get(id);
    }

    @Override
    public T put(ID id, T t) {
        HareAssertUtils.notNull(id);
        HareAssertUtils.notNull(t);

        T p = map.put(id, t);
        if (p == null) {
            size.incrementAndGet();
        }

        return p;
    }

    @Override
    public T remove(ID id) {
        HareAssertUtils.notNull(id);

        T p = map.remove(id);
        if (p != null) {
            size.decrementAndGet();
        }

        return p;
    }

    @Override
    public int size() {
        return size.get();
    }
}
