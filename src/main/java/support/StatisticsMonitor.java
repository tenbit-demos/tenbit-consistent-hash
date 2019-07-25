package support;

import cache.NetCacheNode;
import cache.QpsCacheNode;
import cn.tenbit.hare.core.lite.constant.HareConsts;
import cn.tenbit.hare.core.lite.util.*;
import com.alibaba.fastjson.serializer.SerializerFeature;
import config.Config;

import java.io.FileWriter;

/**
 * @Author bangquan.qian
 * @Date 2019-07-23 12:01
 */
public class StatisticsMonitor implements Runnable {

    private final String logfile;

    private StatisticsMonitor(String logfile) {
        this.logfile = logfile;
    }

    public static StatisticsMonitor of(String logfile) {
        return new StatisticsMonitor(logfile);
    }

    @Override
    public void run() {
        HareInvokeUtils.invokeWithSwallow(() -> {
            Statistics statistics = Statistics.getInstance();
            FileWriter writer = new FileWriter(logfile);
            try {
                while (true) {
                    Statistics.Status status = statistics.getStatus();
                    logfile(writer, status);
                    console(status);
                    HareSleepUtils.sleepSeconds(Config.STATISTICS_MONITOR_S_SPEED);
                }
            } finally {
                writer.close();
            }
        });
    }

    private static final String TAB = "\t";

    private void logfile(FileWriter writer, Statistics.Status status) throws Throwable {
        StringBuilder sb = new StringBuilder();

        sb.append(HareStringUtils.toNotNullString(status.getDb().getQps().get())).append(TAB);
        sb.append(HareStringUtils.toNotNullString(status.getDb().getTimes().get())).append(TAB);

        sb.append(HareStringUtils.toNotNullString(status.getCache().getSize())).append(TAB);
        sb.append(HareStringUtils.toNotNullString(status.getCache().getNodes())).append(TAB);

        for (NetCacheNode.Status stat : status.getCache().getStatuses()) {
            sb.append(HareJsonUtils.toJsonString(stat.getRawIp())).append(TAB);
            sb.append(HareJsonUtils.toJsonString(stat.getIp())).append(TAB);
            sb.append(HareStringUtils.toNotNullString(stat.getSize())).append(TAB);
            sb.append(HareStringUtils.toNotNullString(((QpsCacheNode.Qps) stat.getQps()).getQps().get())).append(TAB);
            sb.append(HareStringUtils.toNotNullString(((QpsCacheNode.Qps) stat.getQps()).getTimes().get())).append(TAB);
        }

        writer.append(sb.toString()).append(HareConsts.NEW_LINE);
        writer.flush();
    }

    private void console(Statistics.Status status) {
        HarePrintUtils.console(HareJsonUtils.toJsonString(status, SerializerFeature.PrettyFormat));
        HarePrintUtils.newline();
    }
}
