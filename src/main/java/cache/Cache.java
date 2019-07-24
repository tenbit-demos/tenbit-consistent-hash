package cache;

/**
 * @Author bangquan.qian
 * @Date 2019-07-23 14:19
 */
public interface Cache<T, ID> {

    T get(ID id);

    T put(ID id, T t);

    T remove(ID id);

    int size();
}
