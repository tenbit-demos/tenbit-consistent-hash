package support;

import cache.NetCacheColony;
import data.impl.StatusablePersonDao;
import lombok.Getter;

/**
 * @Author bangquan.qian
 * @Date 2019-07-23 11:21
 */
public class Statistics implements Statusable<Statistics.Status> {

    private Statistics() {
    }

    public static Statistics getInstance() {
        return Instance.INSTANCE;
    }


    private final Status status = new Status();

    @Override
    public Status getStatus() {
        status.cache = BeanContainer.getInstance().getCacheColony().getStatus();
        status.db = BeanContainer.getInstance().getStatusablePersonDao().getStatus();
        return status;
    }

    private static class Instance {
        private static final Statistics INSTANCE = new Statistics();
    }

    @Getter
    public static class Status {
        private volatile NetCacheColony.Status cache;
        private volatile StatusablePersonDao.Status db;
    }
}
