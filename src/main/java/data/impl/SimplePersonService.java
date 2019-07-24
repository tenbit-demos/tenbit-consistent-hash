package data.impl;

import domain.ClientRequest;
import domain.ClientResponse;
import domain.Person;
import lombok.Setter;

/**
 * @Author bangquan.qian
 * @Date 2019-07-23 11:40
 */
public class SimplePersonService implements PersonService {

    @Setter
    private PersonDao personDao;

    @Override
    public ClientResponse query(ClientRequest request) {
        ServiceValidate.validate(request);

        PersonQuery query = new PersonQuery();
        query.setId(request.getId());
        Person person = personDao.select(query);

        return ServiceConvert.convert2ClientResponse(person);
    }
}
