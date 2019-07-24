package data.impl;

import cn.tenbit.hare.core.lite.util.HareAssertUtils;
import domain.ClientRequest;

/**
 * @Author bangquan.qian
 * @Date 2019-07-23 18:26
 */
public class ServiceValidate {

    public static void validate(ClientRequest request) {
        HareAssertUtils.notNull(request);
        HareAssertUtils.notNull(request.getId());
    }
}
