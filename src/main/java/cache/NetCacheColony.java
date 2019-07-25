package cache;

import cn.tenbit.hare.core.lite.function.HareExecutor;
import cn.tenbit.hare.core.lite.util.HareInvokeUtils;
import lombok.Getter;
import support.Statusable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Author bangquan.qian
 * @Date 2019-07-23 14:19
 */
public class NetCacheColony<T, ID extends Serializable> implements Cache<T, ID>, Statusable<NetCacheColony.Status>, CacheManagable<Integer, NetCacheNode<T, ID>> {

    private final Map<Integer, NetCacheNode<T, ID>> map = new ConcurrentHashMap<>();

    private final List<Integer> net = new LinkedList<>();

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    private final Status status = new Status();

    private final CacheIdMappingStrategy<ID, Integer> strategy;

    public NetCacheColony(CacheIdMappingStrategy<ID, Integer> strategy) {
        this.strategy = strategy;
    }

    @Override
    public T get(ID id) {
        NetCacheNode<T, ID> node = findNodeByNetId(calcNetId(id));
        if (node == null) {
            return null;
        }
        return node.get(id);
    }

    @Override
    public T put(ID id, T t) {
        NetCacheNode<T, ID> node = findNodeByNetId(calcNetId(id));
        if (node == null) {
            return null;
        }
        return node.put(id, t);
    }

    @Override
    public T remove(ID id) {
        NetCacheNode<T, ID> node = findNodeByNetId(calcNetId(id));
        if (node == null) {
            return null;
        }
        return node.remove(id);
    }

    private Integer calcNetId(ID id) {
        return strategy.calc(id);
    }

    private static final long MAX_DISTANCE = (long) Integer.MAX_VALUE - (long) Integer.MIN_VALUE;

    private NetCacheNode<T, ID> findNodeByNetId(Integer netid) {
        return handleWithReadLock(() -> {
            long distance = MAX_DISTANCE;
            boolean bfind = false;
            int find = 0;
            for (Integer id : net) {
                long d = Math.abs(netid - id);
                if (d < distance) {
                    distance = d;
                    find = id;
                    bfind = true;
                }
            }
            if (!bfind) {
                return null;
            }
            return map.get(find);
        });
    }

    private <T> T handleWithReadLock(HareExecutor<T> f) {
        try {
            lock.readLock().lock();
            return HareInvokeUtils.invokeWithTurnRe(f);
        } finally {
            lock.readLock().unlock();
        }
    }

    private <T> T handleWithWriteLock(HareExecutor<T> f) {
        try {
            lock.writeLock().lock();
            return HareInvokeUtils.invokeWithTurnRe(f);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public NetCacheNode<T, ID> getNode(Integer id) {
        return handleWithReadLock(() -> map.get(id));
    }

    @Override
    public boolean removeNode(Integer id) {
        return handleWithWriteLock(() -> net.remove(id) && map.remove(id) != null);
    }

    @Override
    public boolean putNode(Integer id, NetCacheNode<T, ID> node) {
        return handleWithWriteLock(() -> {
            boolean binsert = false;
            boolean bupdate = false;
            int insert = -1;
            int update = -1;
            for (int idx = 0; idx < net.size(); idx++) {
                Integer nid = net.get(idx);
                if (nid < id) {
                    continue;
                }
                if (nid.equals(id)) {
                    update = idx;
                    bupdate = true;
                    break;
                }
                if (nid > id) {
                    insert = idx > 0 ? idx - 1 : 0;
                    binsert = true;
                    break;
                }
            }
            if (net.size() < 1) {
                insert = 0;
                binsert = true;
            }
            if (net.size() > 0) {
                insert = net.size();
                binsert = true;
            }
            if (binsert) {
                net.add(insert, id);
                map.put(id, node);
            } else if (bupdate) {
                net.set(update, id);
                map.put(id, node);
            } else {
                return false;
            }
            return true;
        });
    }

    @Override
    public int size() {
        return handleWithReadLock(() -> {
            int size = 0;
            List<Integer> nodes = new ArrayList<>(net);
            for (Integer node : nodes) {
                Cache<T, ID> cache = map.get(node);
                if (cache != null) {
                    size += cache.size();
                }
            }
            return size;
        });
    }

    @Override
    public Status getStatus() {
        return handleWithReadLock(() -> {
            List<NetCacheNode.Status> statuses = new ArrayList<>();
            List<Integer> nodes = new ArrayList<>(net);
            for (Integer node : nodes) {
                NetCacheNode<T, ID> cache = map.get(node);
                if (cache != null) {
                    statuses.add(cache.getStatus());
                }
                this.status.nodes = statuses.size();
                this.status.size = this.size();
                this.status.statuses = statuses;
            }
            return status;
        });
    }

    @Getter
    public static class Status {
        private volatile List<NetCacheNode.Status> statuses;
        private volatile int nodes;
        private volatile int size;
    }

}
