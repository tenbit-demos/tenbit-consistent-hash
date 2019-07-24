package cache;

/**
 * @Author bangquan.qian
 * @Date 2019-07-24 14:28
 */
public interface CacheIdMappingStrategy<ID, T> {

    T calc(ID id);
}
