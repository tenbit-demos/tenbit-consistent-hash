package support;

import cn.tenbit.hare.core.lite.util.HarePrintUtils;
import cn.tenbit.hare.core.lite.util.HareSleepUtils;
import config.Config;

/**
 * @Author bangquan.qian
 * @Date 2019-07-23 12:01
 */
public class StatisticsMonitor implements Runnable {

    @Override
    public void run() {
        Statistics statistics = Statistics.getInstance();
        while (true) {
            HarePrintUtils.console(statistics.getStatus());
            HarePrintUtils.newline();
            HareSleepUtils.sleepSeconds(Config.STATISTICS_MONITOR_S_SPEED);
        }
    }
}
