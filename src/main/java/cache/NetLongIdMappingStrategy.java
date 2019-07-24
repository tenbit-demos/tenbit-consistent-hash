package cache;

import cn.tenbit.hare.core.lite.util.HareAssertUtils;

/**
 * @Author bangquan.qian
 * @Date 2019-07-24 14:28
 */
public class NetLongIdMappingStrategy implements CacheIdMappingStrategy<Long, Integer> {

    @Override
    public Integer calc(Long id) {
        HareAssertUtils.notNull(id);
        return (int) (id >>> 32);
    }
}
