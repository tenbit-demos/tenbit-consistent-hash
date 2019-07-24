package data.impl;

import domain.ClientRequest;
import domain.ClientResponse;

/**
 * @Author bangquan.qian
 * @Date 2019-07-23 11:39
 */
public interface PersonService {

    ClientResponse query(ClientRequest request);
}
