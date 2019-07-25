package client;

import cn.tenbit.hare.core.lite.util.HareSleepUtils;
import config.Config;
import data.impl.PersonService;
import domain.ClientRequest;
import domain.ClientResponse;
import support.BeanContainer;

import java.util.concurrent.TimeUnit;

/**
 * @Author bangquan.qian
 * @Date 2019-07-23 11:19
 */
public class Client implements Runnable {

    private final PersonService service = BeanContainer.getInstance().getPersonService();

    private static final long MIN = 0L;
    private static final long MAX = 1000L;

    @Override
    public void run() {
        while (true) {
            for (long idx = MIN; idx < MAX; idx++) {
                ClientRequest request = new ClientRequest();
                request.setId(idx);
                ClientResponse response = service.query(request);
                // HarePrintUtils.jsonConsole(response);
                HareSleepUtils.sleep(TimeUnit.MILLISECONDS, Config.CLIENT_MS_SPEED);
            }
        }
    }
}
