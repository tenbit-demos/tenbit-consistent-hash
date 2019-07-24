package cache;

import support.Statusable;

import java.io.Serializable;

/**
 * @Author bangquan.qian
 * @Date 2019-07-23 18:15
 */
public class NetCacheDelegate<T, ID extends Serializable> implements Statusable<NetCacheColony.Status>, Cache<T, ID> {

    private final NetCacheColony<T, ID> colony;

    public NetCacheDelegate(NetCacheColony<T, ID> colony) {
        this.colony = colony;
    }

    @Override
    public T get(ID id) {
        return colony.get(id);
    }

    @Override
    public T put(ID id, T t) {
        return colony.put(id, t);
    }

    @Override
    public T remove(ID id) {
        return colony.remove(id);
    }

    @Override
    public int size() {
        return colony.size();
    }

    @Override
    public NetCacheColony.Status getStatus() {
        return colony.getStatus();
    }
}
