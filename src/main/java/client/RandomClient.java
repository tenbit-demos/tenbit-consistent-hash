package client;

import cn.tenbit.hare.core.lite.util.HareSleepUtils;
import config.Config;
import data.impl.PersonService;
import domain.ClientRequest;
import domain.ClientResponse;
import support.BeanContainer;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @Author bangquan.qian
 * @Date 2019-07-23 11:19
 */
public class RandomClient implements Runnable {

    private final PersonService service = BeanContainer.getInstance().getPersonService();

    private final Random random = new Random();

    @Override
    public void run() {
        while (true) {
            ClientRequest request = new ClientRequest();
            request.setId(random.nextLong());
            ClientResponse response = service.query(request);
            // HarePrintUtils.jsonConsole(response);
            HareSleepUtils.sleep(TimeUnit.MILLISECONDS, Config.CLIENT_MS_SPEED);
        }
    }
}
