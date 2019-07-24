package cache;

/**
 * @Author bangquan.qian
 * @Date 2019-07-23 18:09
 */
public interface CacheManagable<ID, T> {

    T getNode(ID id);

    boolean removeNode(ID id);

    boolean putNode(ID id, T t);

}
