package data;

/**
 * @Author bangquan.qian
 * @Date 2019-07-23 11:21
 */
public interface DataDao<T, Q> {

    T select(Q query);
}
