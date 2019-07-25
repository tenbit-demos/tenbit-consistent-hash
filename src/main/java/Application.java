import cache.NetCacheManager;
import cache.NetCacheNode;
import client.RandomClient;
import cn.tenbit.hare.core.lite.util.HarePrintUtils;
import cn.tenbit.hare.core.lite.util.HareSleepUtils;
import domain.Person;
import support.BeanContainer;
import support.ExecuteThreadPool;
import support.StatisticsMonitor;

import java.util.concurrent.ExecutorService;

interface AppConfig {

    String logfile = "/Users/chainz/Temporary/consistent-hash.txt";

    int CLIENT_NUM = 10;

    int APP_RUN_SECONDS = 60 * 10;

    String[] ips = {"255.255.255.255", "1.1.1.1", "64.64.64.64", "127.127.127.127", "192.192.192.192"};
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
        for (String sip : AppConfig.ips) {
            NetCacheNode<Person, Long> node = new NetCacheNode<>(new String[]{sip});
            for (int ip : node.getIp()) {
                HarePrintUtils.jsonConsole(sip, ip, cacheManager.putNode(ip, node));
            }
        }

        ExecutorService executorService = ExecuteThreadPool.getPool();

        tip("--- RUN ---");

        executorService.execute(StatisticsMonitor.of(AppConfig.logfile));

        for (int idx = 0; idx < AppConfig.CLIENT_NUM; idx++) {
            executorService.execute(new RandomClient());
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
