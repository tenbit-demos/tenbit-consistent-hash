package cache;

import java.io.Serializable;

/**
 * @Author bangquan.qian
 * @Date 2019-07-23 14:19
 */
public class NetCacheManager<T, ID extends Serializable> implements CacheManagable<Integer, NetCacheNode<T, ID>> {

    private final NetCacheColony<T, ID> colony;

    public NetCacheManager(NetCacheColony<T, ID> colony) {
        this.colony = colony;
    }

    @Override
    public NetCacheNode<T, ID> getNode(Integer id) {
        return colony.getNode(id);
    }

    @Override
    public boolean removeNode(Integer id) {
        return colony.removeNode(id);
    }

    @Override
    public boolean putNode(Integer id, NetCacheNode<T, ID> node) {
        return colony.putNode(id, node);
    }
}
