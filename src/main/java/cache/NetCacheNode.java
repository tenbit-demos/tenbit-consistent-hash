package cache;

import cn.tenbit.hare.core.lite.util.HareInet4AddressUtils;
import lombok.Getter;
import support.Statusable;

import java.io.Serializable;

/**
 * @Author bangquan.qian
 * @Date 2019-07-23 15:29
 */
public class NetCacheNode<T, ID extends Serializable> implements Cache<T, ID>, Statusable<NetCacheNode.Status> {

    private final String rawIp;

    @Getter
    private final int ip;

    private final Cache<T, ID> node;
    private final Status status = new Status();

    public NetCacheNode(String rawIp) {
        this.rawIp = rawIp;
        this.node = new QpsCacheNode<>(new TtlCacheNode<>(new CacheNode<>()));
        this.ip = HareInet4AddressUtils.text2Number(rawIp);
    }

    @Override
    public T get(ID id) {
        return node.get(id);
    }

    @Override
    public T put(ID id, T t) {
        return node.put(id, t);
    }

    @Override
    public T remove(ID id) {
        return node.remove(id);
    }

    @Override
    public int size() {
        return node.size();
    }

    @Override
    public Status getStatus() {
        status.ip = this.ip;
        status.rawIp = this.rawIp;
        status.size = this.size();
        if (node instanceof CacheQpsable) {
            status.qps = ((CacheQpsable) node).getQps();
        }
        return status;
    }

    @Getter
    public static class Status {

        private volatile String rawIp;
        private volatile int ip;
        private volatile int size;

        private volatile Object qps;

        private Status() {
        }
    }
}
