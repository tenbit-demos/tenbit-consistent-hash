package support;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author bangquan.qian
 * @Date 2019-07-23 16:47
 */
public class ExecuteThreadPool {

    private static final ExecutorService POOL = Executors.newCachedThreadPool();

    public static ExecutorService getPool() {
        return POOL;
    }
}
