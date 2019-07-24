import cache.NetCacheManager;
import cache.NetCacheNode;
import client.Client;
import cn.tenbit.hare.core.lite.util.HarePrintUtils;
import cn.tenbit.hare.core.lite.util.HareSleepUtils;
import domain.Person;
import support.BeanContainer;
import support.ExecuteThreadPool;
import support.StatisticsMonitor;

import java.util.concurrent.ExecutorService;

interface AppConfig {

    int CLIENT_NUM = 10;

    int APP_RUN_SECONDS = 30;

    String[] ips = {"172.10.32.1"};
}

/**
 * @Author bangquan.qian
 * @Date 2019-07-23 12:15
 */
public class Application {

    public static void main(String[] args) throws Exception {

        tip("--- INT ---");

        BeanContainer container = BeanContainer.getInstance();

        NetCacheManager<Person, Long> cacheManager = container.getCacheManager();
        for (String ip : AppConfig.ips) {
            NetCacheNode<Person, Long> node = new NetCacheNode<>(ip);
            HarePrintUtils.jsonConsole(ip, cacheManager.putNode(node.getIp(), node));
        }

        ExecutorService executorService = ExecuteThreadPool.getPool();

        tip("--- RUN ---");

        executorService.execute(new StatisticsMonitor());

        for (int idx = 0; idx < AppConfig.CLIENT_NUM; idx++) {
            executorService.execute(new Client());
        }

        HareSleepUtils.sleepSeconds(AppConfig.APP_RUN_SECONDS);

        executorService.shutdown();

        tip("--- END ---");
    }

    private static void tip(String s) {
        HarePrintUtils.newline();
        HarePrintUtils.console(s);
        HarePrintUtils.newline();
    }
}
