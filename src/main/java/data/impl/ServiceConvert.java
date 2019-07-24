package data.impl;

import domain.ClientResponse;
import domain.Person;

/**
 * @Author bangquan.qian
 * @Date 2019-07-23 18:26
 */
public class ServiceConvert {

    public static ClientResponse convert2ClientResponse(Person person) {
        ClientResponse response = new ClientResponse();
        response.setPerson(person);
        return response;
    }
}
