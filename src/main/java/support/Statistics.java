package support;

import cn.tenbit.hare.core.lite.util.HareJsonUtils;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author bangquan.qian
 * @Date 2019-07-23 11:21
 */
public class Statistics implements Statusable<String> {

    private Statistics() {
    }

    public static Statistics getInstance() {
        return Instance.INSTANCE;
    }

    @Override
    public String getStatus() {
        Map<String, Object> map = new HashMap<>();
        showDbStat(map);
        showCacheStat(map);
        return HareJsonUtils.toJsonString(map, SerializerFeature.PrettyFormat);
    }

    private void showCacheStat(Map<String, Object> map) {
        map.put("cache", BeanContainer.getInstance().getCacheColony().getStatus());
    }

    private void showDbStat(Map<String, Object> map) {
        map.put("db", BeanContainer.getInstance().getStatusablePersonDao().getStatus());
    }

    private static class Instance {
        private static final Statistics INSTANCE = new Statistics();
    }
}
